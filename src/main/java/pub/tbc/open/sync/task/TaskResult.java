package pub.tbc.open.sync.task;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author tbc by 2020/7/24 4:21 下午
 */
@Data
@Accessors(chain = true)
public class TaskResult {
    /**
     * 任务执行是否成功
     */
    private boolean isOk;
    /**
     * 是否继续执行
     */
    private boolean continueExec;
}
