package com.baiziio.lib.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baiziio.lib.quartz.dao.entity.SysTaskEntity;
import com.baiziio.lib.quartz.dao.entity.SysTaskLogEntity;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * <p>
 * 计划任务表 服务类
 * </p>
 *
 * @author juwencheng
 * @since 2020-08-22
 */
public interface SysTaskService extends IService<SysTaskEntity> {

    String JOB_DATA_KEY = "com.baiziio.params";
    Integer JOB_STATUS_RUNNING = 0;
    Integer JOB_STATUS_PAUSE = 1;
    String JOB_EXE_STATUS_START = "start";
    String JOB_EXE_STATUS_FINISH = "finish";
    String JOB_EXE_STATUS_ERROR = "error";


    /**
     * 创建或更新任务
     * @param entity
     * @return
     */
    SysTaskEntity createOrUpdateTask(SysTaskEntity entity);

    /**
     * 删除任务
     * @param taskId
     * @return
     */
    Boolean deleteTask(int taskId);

    /**
     * 暂停任务
     * @param taskId
     * @return
     */
    Boolean pauseTask(int taskId);

    /**
     * 恢复任务
     * @param taskId
     * @return
     */
    Boolean resumeTask(int taskId);

    /**
     * 立即执行任务
     * @param taskId
     * @return
     */
    Boolean triggerTask(int taskId);

    /**
     * 任务列表
     * @return
     */
    List<SysTaskEntity> taskList();

    /**
     * 根据任务Id查询执行日志
     * @param taskId
     * @return
     */
    List<SysTaskLogEntity> logByTaskId(int taskId);

    /**
     * 根据任务Id分页查询任务
     * @param taskId
     * @param current
     * @param pageSize
     * @return
     */
    IPage<SysTaskLogEntity> pageLogByTaskId(int taskId, int current, int pageSize);

    /**
     * 分页查询执行日志
     * @param current
     * @param pageSize
     * @return
     */
    IPage<SysTaskLogEntity> taskLogPageList(int current, int pageSize);

    /**
     * 创建任务日志
     * @param taskId
     * @return
     */
    SysTaskLogEntity createTaskLog(Integer taskId);

    /**
     * 更新
     * @param logEntity
     */
    void updateTaskLog(SysTaskLogEntity logEntity);

    void clear() throws SchedulerException;
}
