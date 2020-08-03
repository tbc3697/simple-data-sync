package pub.tbc.open.sync.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 同步记录
 *
 * @Author tbc by 2020/7/16 8:21 下午
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DataSyncRecord implements Serializable {
    // @formatter:off

    private int id;
    /** 同步类型 */
    private String syncType;
    /** 同步位置 */
    private String position;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;
    /** 版本，预留字段 */
    private int version;

}
