package pub.tbc.open.sync.task;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 数据同步应用上下文，用于方法调用间传递信息
 *
 * @Author tbc by 2020/5/14 3:40 下午
 */
@Data
@Accessors(chain = true)
public class DataSyncContext<S, T, P> {

    public static DataSyncContext instance() {
        return new DataSyncContext();
    }

    // @formatter:off

    /** 源数据 */
    List<S> sources;

    /** 处理后的数据 */
    List<T> targets;

    /** 原同步位置 */
    P beforePosition;

    /** 新的同步位置 */
    P afterPosition;

    /** 计划同步数量 */
    int plainSyncCount;

    /** 实际同步数量 */
    int realSyncCount;

}
