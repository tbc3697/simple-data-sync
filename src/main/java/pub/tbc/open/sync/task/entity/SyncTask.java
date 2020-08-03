package pub.tbc.open.sync.task.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 任务定义
 *
 * @author tbc by 2020-07-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SyncTask {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 任务名称，应用中也会做为bean 名称
     */
    private String taskName;

    /**
     * 超时时间
     */
    private int expireTime;

    private String cron;

    /**
     * 任务状态： 0.停用（不会被调度）; 1.启用-未运行（等待调度）; 2.运行中；
     */
    private Integer status;

    private LocalDateTime createTime;

    /**
     * 承担业务职责，updateTime > date_sub(now(), interval ${expireTime} second)
     */
    private LocalDateTime updateTime;

}
