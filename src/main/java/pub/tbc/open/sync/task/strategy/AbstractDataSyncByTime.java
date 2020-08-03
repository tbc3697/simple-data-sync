package pub.tbc.open.sync.task.strategy;

import org.springframework.util.StringUtils;
import pub.tbc.open.sync.task.AbstractDataSyncTask;
import pub.tbc.open.sync.task.entity.DataSyncRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import static java.time.LocalDateTime.of;
import static java.util.Comparator.naturalOrder;

/**
 * 基于 updateTime 的数据同步策略类
 * <p>
 * 基于时间戳进行数据同步，本身存在无法解决的问题：<br>
 * 问题1：对于物理删除的数据，毫无办法；<br>
 * 问题2：仍然存在漏数据问题，比如这样的场景：<br>
 * <li>1. A 事务开始，对记录 aa 执行了一个 update 操作，此时时间为 T1；</li>
 * <li>2. A 事务处理其它事情，耗时较长；</li>
 * <li>3. B 事务开始，对记录 bb 执行了一个 insert 操作，此时时间为 T2；</li>
 * <li>4. B 事务提交，此时 bb 更改对其它线程可见了；</li>
 * <li>5. 同步程序启动，读到了 bb，并且记录了最后更新时间 T2；</li>
 * <li>6. A 事务提交，此时 aa 对其它线程可见；</li>
 * <li>7. 同步程序启动，查询大于 T2 的记录，因为 aa 的变更时间 T1 小于 T2，那么 aa 的变更就漏掉了；</li>
 * </p>
 * <p>
 *     解决办法：<br>
 *     1. 同步时间提前量，查询数据的时间，将最后更新时间提前一段时间，可大大减少漏数据的概率；<br>
 *     2. 使用触发器进行同步；<br>
 *     3. 基于 binlog 同步；<br>
 * </p>
 *
 * @Author tbc by 2020/5/14 7:24 下午
 */
public abstract class AbstractDataSyncByTime<S, T> extends AbstractDataSyncTask<S, T, LocalDateTime> {
    /**
     * 默认同步开始时间: unix 时间戳起始时间
     */
    private final LocalDateTime DEFAULT_SYNC_BEGIN_TIME = of(
            LocalDate.of(1970, 1, 1),
            LocalTime.of(0, 0, 0, 000)
    );

    /**
     * 精确到纳秒的标准日期格式 formatter
     */
    private DateTimeFormatter STANDARD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    protected LocalDateTime extractNewPositionByFunction(List<S> data, Function<S, LocalDateTime> func) {
        return data.stream().map(func).max(naturalOrder()).orElse(null);
    }


    @Override
    public LocalDateTime extractCurrentPosition(DataSyncRecord record) {
        return covertFromString(record.getPosition());
    }

    @Override
    public LocalDateTime covertFromString(String position) {
        if (StringUtils.isEmpty(position)) {
            return DEFAULT_SYNC_BEGIN_TIME;
        }
        return LocalDateTime.from(STANDARD_FORMATTER.parse(position));
    }

    @Override
    public String convertToString(LocalDateTime dateTime) {
        return dateTime.format(STANDARD_FORMATTER);
    }

    @Override
    protected DataSyncRecord initRecord() {
        return new DataSyncRecord().setSyncType(getSyncType()).setPosition(convertToString(DEFAULT_SYNC_BEGIN_TIME));
    }

}
