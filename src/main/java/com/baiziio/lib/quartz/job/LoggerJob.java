package com.baiziio.lib.quartz.job;

import com.baiziio.lib.quartz.dao.entity.SysTaskLogEntity;
import com.baiziio.lib.quartz.service.SysTaskService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @author juwencheng
 * @date 2021 2021/6/9 10:37 下午
 */
@Slf4j
public class LoggerJob implements Job {

    @Autowired
    private SysTaskService taskService;

    protected void doExecute(JobExecutionContext context) throws Exception {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Integer taskId = getTaskId(context);
        long start = System.currentTimeMillis();
        SysTaskLogEntity logEntity = taskService.createTaskLog(taskId);
        try {
            doExecute(context);
            logEntity.setStatus(SysTaskService.JOB_EXE_STATUS_FINISH);
            logEntity.setReason("");
            logEntity.setDuration(System.currentTimeMillis() - start);
            logEntity.setFinishAt(LocalDateTime.now());
            taskService.updateTaskLog(logEntity);
        } catch (Exception e) {
            log.error("执行任务发生异常", e);
            logEntity.setStatus(SysTaskService.JOB_EXE_STATUS_ERROR);
            logEntity.setReason(e.getMessage());
            logEntity.setDuration(System.currentTimeMillis() - start);
            taskService.updateTaskLog(logEntity);
        }
    }

    protected Integer getTaskId(JobExecutionContext context) {
        try {
            return Integer.parseInt(context.getJobDetail().getKey().getName());
        }catch (Exception e) {
            return -1;
        }
    }
}
