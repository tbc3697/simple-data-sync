package pub.tbc.open.sync.task;

/**
 * 数据同步任务接口
 *
 * @Author tbc by 2020/5/14 1:19 下午
 */
public interface DataSyncTask {
    /**
     * 执行数据同步任务
     *
     * @return 同步结果（可表示多重意义，如本次任务是否成功需要回滚、同步数据量是否达到单次最大等）
     */
    boolean executeSync();
}
