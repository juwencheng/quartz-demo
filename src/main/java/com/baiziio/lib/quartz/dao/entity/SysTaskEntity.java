package com.baiziio.lib.quartz.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 计划任务表
 * </p>
 *
 * @author juwencheng
 * @since 2020-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_task")
public class SysTaskEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务表达式
     */
    @TableField("cron_expression")
    private String cronExpression;

    /**
     * 任务名称
     */
    @TableField("name")
    private String name;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 任务类名
     */
    @TableField("executor_clazz")
    private String executorClazz;

    /**
     * 参数
     */
    @TableField("data")
    private String data;

    /**
     * 创建时间
     */
    @TableField("create_at")
    private LocalDateTime createAt;

    /**
     * 更新时间
     */
    @TableField("update_at")
    private LocalDateTime updateAt;

    /**
     * 状态，0 运行中 1 暂停
     */
    @TableField("status")
    private Integer status;

    /**
     * 乐观锁版本控制
     */
    @TableField("revision")
    @Version
    private Integer revision;

    /**
     * 删除状态位
     */
    @TableField("is_delete")
    @TableLogic
    private Boolean isDelete;


}
