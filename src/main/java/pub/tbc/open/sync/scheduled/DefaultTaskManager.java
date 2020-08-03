package pub.tbc.open.sync.scheduled;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pub.tbc.open.sync.task.DataSyncTask;
import pub.tbc.open.sync.task.entity.SyncTask;
import pub.tbc.open.sync.task.mapper.master.SyncTaskMapper;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * @Author tbc by 2020/7/20 10:27 上午
 */
@Slf4j
@Component
public class DefaultTaskManager implements TaskManager {

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private SyncTaskMapper taskMapper;

    private ThreadPoolTaskScheduler taskScheduler;

    private void initTaskScheduler() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
    }

    private List<SyncTask> getCandidateTasks(ConfigurableApplicationContext context) {
        return taskMapper.findEnableTasks();
    }

    private Predicate<SyncTask> getTaskFilterBySystem() {
        String enableStr = System.getProperty("task.enable");
        // 未指定启用的任务时，启用全部
        if (StringUtils.isEmpty(enableStr) || "ALL".equalsIgnoreCase(enableStr)) {
            return a -> true;
        }
        List<String> enableTaskNames = Splitter.on(",").splitToList(enableStr)
                .stream()
                .map(String::toLowerCase)
                .collect(toList());
        return task -> enableTaskNames.contains(task.getTaskName());
    }

    /**
     * 统一包装要调度的任务，成功修改任务状态才会被执行
     *
     * @param syncTask
     * @param task
     * @return
     */
    private Runnable taskWrap(DataSyncTask syncTask, SyncTask task) {
        return () -> {
            boolean continueExec = false;
            do {
                if (tryUpdateStatusForStart(task)) {
                    try {
                        log.info("开始执行任务：{}", task.getTaskName());
                        continueExec = syncTask.executeSync();
                    } finally {
                        // 任务结束要恢复状态
                        finishTask(task);
                    }
                } else {
                    continueExec = false;
                    log.info("同步任务 [{}] 正在执行中，本次任务中止", task.getTaskName());
                }
            } while (continueExec);
        };
    }

    private void finishTask(SyncTask task) {
        try {
            if (updateStatusForStop(task)) {
                log.info("任务 [{}] 执行结束", task.getTaskName());
            } else {
                log.error("任务执行结束，恢复任务为可调度状态失败，数据异常，任务: {}", task.getTaskName());
            }
        } catch (Exception e) {
            boolean isOk;
            log.error("修改状态发生异常，准备重试：{} - {}", e.getClass().getName(), e.getMessage());
            int i = 0, end = 3;
            do {
                if (isOk = updateStatusForStop(task)) {
                    break;
                }
                i++;
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ie) {
                    log.error("休眠时发生中断：{}", ie.getMessage(), ie);
                }
            } while (i < end);
            if (isOk) {
                log.info("任务 [{}] 执行结束", task.getTaskName());
            }
        }
    }

    /**
     * 尝试修改任务状态为 2（执行中）
     *
     * @param task
     * @return
     */
    private boolean tryUpdateStatusForStart(SyncTask task) {
        int result = taskMapper.execTaskUpdateStatus(task.getId());
        log.info("tryUpdateStatusStart ==> {}", result);
        return result == 1;
    }

    /**
     *
     * @param task
     * @return
     */
    private boolean updateStatusForStop(SyncTask task) {
        int result = taskMapper.finishTaskUpdateStatus(task.getId());
        log.info("updateStatusStop ==> {}", result);
        return result == 1;
    }

    /**
     * 初始化所有任务
     *
     * @param context
     */
    @Override
    public void initTask(ConfigurableApplicationContext context) {
        // 数据库中的候选任务
        List<SyncTask> candidateTasks = getCandidateTasks(context);
        if (CollectionUtils.isEmpty(candidateTasks)) {
            throw new NullPointerException("没有候选任务");
        }
        // 按系统参数来一遍过滤（调试方便）
        List<SyncTask> realTasks = candidateTasks.stream().filter(getTaskFilterBySystem()).collect(toList());
        if (CollectionUtils.isEmpty(realTasks)) {
            throw new NullPointerException("没有可用任务");
        }
        // 有可执行任务才需要初始化调度器
        initTaskScheduler();
        // 初始化每个任务
        realTasks.forEach(this::initTask);
    }

    @Override
    public void initTask(SyncTask task) {
        log.info("init task {} - {}", task.getTaskName(), task);
        // 约定：处理任务的 bean 名称跟 taskName 保持一致
        DataSyncTask syncTask = applicationContext.getBean(task.getTaskName(), DataSyncTask.class);
        if (Objects.isNull(syncTask)) {
            log.error("无可用任务处理器（spring bean）：{}", task.getTaskName());
            return;
        }
        CronTask cronTask = new CronTask(taskWrap(syncTask, task), task.getCron());
        taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
    }


}
