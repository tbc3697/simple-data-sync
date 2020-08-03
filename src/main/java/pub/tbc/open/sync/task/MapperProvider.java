package pub.tbc.open.sync.task;

import pub.tbc.open.sync.task.mapper.SourceMapper;
import pub.tbc.open.sync.task.mapper.TargetMapper;

/**
 * 同步相关基础 mapper 提供者
 *
 * @param <S> 待同步数据的类型
 * @param <T> 待同步数据处理后待写入的数据类型
 * @param <P> 同步位置的数据类型
 * @Author tbc by 2020/7/25 3:24 下午
 */
public interface MapperProvider<S, T, P> {

    /**
     * 获取原始数据的 mapper
     *
     * @return
     */
    SourceMapper<S, P> getSourceMapper();

    /**
     * 目标库的 mapper
     *
     * @return
     */
    TargetMapper<T> getTargetMapper();
}
