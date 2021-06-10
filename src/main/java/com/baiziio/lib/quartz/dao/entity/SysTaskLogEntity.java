package com.baiziio.lib.quartz.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 计划任务日志表
 * </p>
 *
 * @author juwencheng
 * @since 2020-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_task_log")
public class SysTaskLogEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 状态，start 开始 finished 完成 error 错误
     */
    @TableField("status")
    private String status;

    /**
     * 任务ID
     */
    @TableField("task_id")
    private Integer taskId;

    /**
     * 信息
     */
    @TableField("message")
    private String message;

    /**
     * 执行时间
     */
    @TableField("create_at")
    private LocalDateTime createAt;

    /**
     * 失败原因
     */
    @TableField("reason")
    private String reason;

    @TableField("duration")
    private long duration;

    /**
     * 结束时间
     */
    @TableField("finish_at")
    private LocalDateTime finishAt;


}
