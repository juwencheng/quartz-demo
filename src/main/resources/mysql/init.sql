
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_task_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_task_log`;
CREATE TABLE `sys_task_log` (
    `id` varchar(32) NOT NULL,
    `status` varchar(20) DEFAULT '0' COMMENT '状态，1 开始 2 结束 3 错误',
    `task_id` varchar(255) DEFAULT NULL COMMENT '任务ID',
    `message` text COMMENT '信息',
    `reason` text COMMENT '失败原因',
    `create_at` datetime(3) DEFAULT NULL COMMENT '执行时间',
    `duration` int(11) DEFAULT NULL COMMENT '持续时间',
    `finish_at` datetime(3) DEFAULT NULL COMMENT '结束时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计划任务日志表';


-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `cron_expression` varchar(255) DEFAULT NULL COMMENT '任务表达式',
    `name` varchar(255) DEFAULT NULL COMMENT '任务名称',
    `executor_clazz` varchar(255) DEFAULT NULL COMMENT '任务类名',
    `data` text COMMENT '参数',
    `remark` text COMMENT '备注',
    `create_at` datetime(3) DEFAULT NULL COMMENT '创建时间',
    `update_at` datetime(3) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `status` int(11) DEFAULT '0' COMMENT '状态，0 运行中 1 暂停 ',
    `revision` int(11) DEFAULT '1' COMMENT '乐观锁版本控制',
    `is_delete` tinyint(1) DEFAULT '0' COMMENT '删除状态位',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='计划任务表';

SET FOREIGN_KEY_CHECKS = 1;
