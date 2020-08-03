package pub.tbc.open.sync.task.strategy;

import lombok.extern.slf4j.Slf4j;
import pub.tbc.open.sync.task.AbstractDataSyncTask;

/**
 * 基于 自增主键 的数据同步策略类 <br>
 * <br>
 * 基于自增 ID 进行同步，仍然存在漏数据问题，比如长事务初期 insert 的记录，很容易被漏掉；<br>
 * 物理删除的数据仍然没办法检测；<br>
 *
 * @Author tbc by 2020/5/14 1:36 下午
 */
@Slf4j
public abstract class AbstractDataSyncByPrimary<S, T> extends AbstractDataSyncTask<S, T, Long> {

}
