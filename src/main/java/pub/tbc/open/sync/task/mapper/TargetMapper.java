package pub.tbc.open.sync.task.mapper;

import java.util.List;

/**
 * 同步目标表的共通操作
 *
 * @Author tbc by 2020/7/25 3:06 下午
 */
public interface TargetMapper<T> {

    /**
     * 查询待同步数据中已存在的ID
     * @param ids
     * @return
     */
    List<Long> findInIds(List<Long> ids);

    int batchInsert(List<T> data);

    int batchUpdate(List<T> data);

    int batchInsertOnDuplicateKey(List<T> data);

}
