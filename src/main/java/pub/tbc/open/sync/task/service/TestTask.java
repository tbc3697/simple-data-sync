package pub.tbc.open.sync.task.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.tbc.open.sync.task.DataSyncTask;

import java.util.concurrent.TimeUnit;

/**
 * @Author tbc by 2020/7/22 7:00 下午
 */
@Deprecated
@Service
@Slf4j
public class TestTask implements DataSyncTask {
    @Override
    public boolean executeSync() {
        log.info("test task execute");
        try {
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
