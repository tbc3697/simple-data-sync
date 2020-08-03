package pub.tbc.open.sync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pub.tbc.open.sync.scheduled.DefaultTaskManager;

/**
 * @Author tbc by 2020/8/1 2:40 下午
 */
@Slf4j
@SpringBootApplication
public class SimpleDataSyncApplication  {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SimpleDataSyncApplication.class, args);
        log.info("系统启动完成，开始初始化同步任务 >>>>>>>>>");
        initTask(context);
    }

    private static void initTask(ConfigurableApplicationContext context) {
        DefaultTaskManager taskManager = context.getBean(DefaultTaskManager.class);
        taskManager.initTask(context);
    }
}
