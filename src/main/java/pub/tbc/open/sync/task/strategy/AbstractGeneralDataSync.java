package pub.tbc.open.sync.task.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import pub.tbc.open.sync.task.AbstractDataSyncTask;
import pub.tbc.open.sync.task.LongPrimaryProvider;
import pub.tbc.open.sync.task.MapperProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author tbc by 2020/8/3 6:23 下午
 */
@Slf4j
public abstract class AbstractGeneralDataSync<S, T extends LongPrimaryProvider, P>
        extends AbstractDataSyncTask<S, T, P>
        implements MapperProvider<S, T, P> {

    // @formatter:off
    /** 默认同步位置提前量 */
    private final int DEFAULT_LEAD = 10;

    /** 单次获取数据量最大扩充次数 */
    private final int MAX_EXPAND = 5;

    /** 同步位置提前量 */
    protected P positionLead(P position) {
        return position;
    }

    /** 真正获取数据的地方 */
    private List<S> realGetSourceData(P position, int count) {
        return getSourceMapper().findByPositionLimit(position, count);
    }
    // @formatter:on

    @Override
    protected List<S> getSourceData(P position) {
        // 同步位置给个提前量，减小漏同步的概率
        P leadPosition = positionLead(position);
        int count = plainSyncCount();
        List<S> data = realGetSourceData(leadPosition, count);
        // 最多扩容次数
        int max_num = 3;
        int num = 0;
        // 如果新的同步位置与旧的相同，并且本次获取的数据量达到了计划获取数量
        while (position.equals(extractNewPosition(data)) && data.size() == count) {
            count += count;
            data = realGetSourceData(leadPosition, count);
            if (num++ >= max_num) {
                log.error("单次获取数据量达到 {} 位置偏移量仍然没有往前推进，考虑数据有问题，人工检查处理", count);
                break;
            }
        }
        return data;
    }

    @Override
    protected boolean write(List<T> data) {
        return saveOrUpdate(data);
    }

    protected boolean saveOrUpdate(List<T> data) {
        // todo : insert on duplicate key update

        // 先拿出新获取的数据的 ID
        List<Long> dataIds = data.stream().map(T::primary).collect(toList());
        // 去数据库中查找已存的ID，这些需要 update
        List<Long> updateIds = getTargetMapper().findInIds(dataIds);
        // 筛选出所有不存在的ID，这些需要 insert
        List<Long> insertIds = dataIds.stream().filter(id -> !updateIds.contains(id)).collect(toList());

        insert(data.stream().filter(t -> insertIds.contains(t.primary())).collect(toList()));
        update(data.stream().filter(t -> updateIds.contains(t.primary())).collect(toList()));

        return true;
    }

    protected boolean insert(List<T> data) {
        if (CollectionUtils.isEmpty(data)) {
            log.info("没有需要插入的数据");
            return false;
        }
        return getTargetMapper().batchInsert(data) > 0;
    }

    protected boolean update(List<T> data) {
        if (CollectionUtils.isEmpty(data)) {
            log.info("没有需要更新的数据");
            return false;
        }
        return getTargetMapper().batchUpdate(data) > 0;
    }
}
