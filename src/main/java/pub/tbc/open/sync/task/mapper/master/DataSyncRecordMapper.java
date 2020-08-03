package pub.tbc.open.sync.task.mapper.master;

import org.apache.ibatis.annotations.Param;
import pub.tbc.open.sync.task.entity.DataSyncRecord;

/**
 * @Author tbc by 2020/7/20 7:38 下午
 */
public interface DataSyncRecordMapper {

    int insert(DataSyncRecord record);

    /**
     * 按同步类型获取同步记录
     *
     * @param type
     * @return
     */
    DataSyncRecord findByType(String type);

    /**
     * 更新同步位置
     *
     * @param recordId
     * @param position
     * @return
     */
    int updatePosition(@Param("recordId") int recordId, @Param("position") String position);

}
