package pub.tbc.open.sync.task;

import pub.tbc.open.sync.task.entity.DataSyncRecord;

import java.util.List;

/**
 * 同步位置提取器
 *
 * @param <S> 得到的原始数据类型
 * @param <P> 同步位的数据类型
 * @Author tbc by 2020/7/16 8:21 下午
 */
public interface PositionHandler<S, P> {
    /**
     * 提取原同步位置
     *
     * @param record
     * @return
     */
    P extractCurrentPosition(DataSyncRecord record);

    /**
     * 提取新的同步位置
     *
     * @param sourceData
     * @return
     */
    P extractNewPosition(List<S> sourceData);

    /**
     * 从字符串转换
     *
     * @param position
     * @return
     */
    P covertFromString(String position);

    /**
     * 转换为 String 型，DataSyncRecord 中存储 long 值
     *
     * @param p
     * @return
     */
    String convertToString(P p);
}
