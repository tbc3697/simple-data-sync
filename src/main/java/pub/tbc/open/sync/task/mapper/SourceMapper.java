package pub.tbc.open.sync.task.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author tbc by 2020/7/25 2:49 下午
 */
public interface SourceMapper<S, P> {
    /**
     * 按同步位置查询指定数量的数据
     *
     * @param position
     * @param count
     * @return
     */
    List<S> findByPositionLimit(@Param("position") P position, @Param("count") int count);
}
