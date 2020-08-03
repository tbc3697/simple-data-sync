package pub.tbc.open.sync.scheduled;

import org.springframework.context.ConfigurableApplicationContext;
import pub.tbc.open.sync.task.entity.SyncTask;

/**
 * @Author tbc by 2020/7/20 7:51 下午
 */
public interface TaskManager {

    /**
     * 初始化所有任务
     *
     * @param context
     * @return
     */
    void initTask(ConfigurableApplicationContext context);

    /**
     * 初始化指定任务，任务必须跟数据库中记录匹配
     *
     * @param task
     * @return
     */
    void initTask(SyncTask task);
}
