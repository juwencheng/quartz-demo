package com.baiziio.lib.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baiziio.lib.quartz.dao.entity.SysTaskEntity;
import com.baiziio.lib.quartz.dao.entity.SysTaskLogEntity;
import com.baiziio.lib.quartz.dao.mapper.SysTaskLogMapper;
import com.baiziio.lib.quartz.dao.mapper.SysTaskMapper;
import com.baiziio.lib.quartz.service.SysTaskService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 计划任务表 服务实现类
 * </p>
 *
 * @author juwencheng
 * @since 2020-08-22
 */
@Slf4j
@Service
public class SysTaskServiceImpl extends ServiceImpl<SysTaskMapper, SysTaskEntity> implements SysTaskService {

    private static final String GROUP_NAME = "com.baiziio";


    private final Scheduler scheduler;
    private final SysTaskLogMapper logMapper;

    @Autowired
    public SysTaskServiceImpl(Scheduler scheduler, SysTaskLogMapper logMapper) {
        this.scheduler = scheduler;
        this.logMapper = logMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysTaskEntity createOrUpdateTask(SysTaskEntity entity) {
        if (Objects.isNull(entity.getId())) {
            try {
                entity.setCreateAt(LocalDateTime.now());
                entity.setUpdateAt(LocalDateTime.now());
                boolean save = save(entity);
                if (!save) {
                    throw new RuntimeException("新建任务到数据库失败");
                }
                deployTask(entity.getId());
                return getById(entity.getId());
            } catch (Exception e) {
                log.error("创建任务失败", e);
                throw e;
            }
        } else {
            SysTaskEntity updateEntity = new SysTaskEntity();
            updateEntity.setId(entity.getId());
            updateEntity.setName(entity.getName());
            updateEntity.setRemark(entity.getRemark());
            updateEntity.setCronExpression(entity.getCronExpression());
            updateEntity.setExecutorClazz(entity.getExecutorClazz());
            updateEntity.setData(entity.getData());
            updateEntity.setStatus(entity.getStatus());
            updateEntity.setUpdateAt(LocalDateTime.now());
            boolean b = updateById(updateEntity);
            if (!b) {
                throw new RuntimeException("更新任务到数据库失败");
            }
            deployTask(entity.getId());
            return getById(entity.getId());
        }
    }

    private void deployTask(Integer taskId) {
        SysTaskEntity byId = getById(taskId);
        if (Objects.isNull(byId)) {
            log.error("任务不存在");
            return;
        }
        JobKey jobKey = buildJobKey(taskId);
        try {
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                scheduler.deleteJob(jobKey);
            }
            //表达式调度构建器(即任务执行的时间,不立即执行)
            CronScheduleBuilder scheduleBuilder =
                    CronScheduleBuilder.cronSchedule(byId.getCronExpression()).withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger =
                    TriggerBuilder.newTrigger().withIdentity(byId.getId().toString()).startAt(new Date())
                            .withSchedule(scheduleBuilder).build();
            JobDetail jobDetail =
                    JobBuilder.newJob((Class<? extends Job>) Class.forName(byId.getExecutorClazz()))
                            .withIdentity(jobKey)
                            .withDescription(byId.getRemark())
                            .storeDurably(true)
                            .usingJobData(JOB_DATA_KEY, byId.getData())
                            .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("部署任务失败", e);
            throw new RuntimeException("部署失败" + e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error("任务不存在", e);
            throw new RuntimeException("任务不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTask(int taskId) {
        SysTaskEntity byId = getById(taskId);
        if (Objects.isNull(byId)) {
            return true;
        }
        JobKey jobKey = buildJobKey(taskId);
        try {
            if (!scheduler.checkExists(jobKey)) {
                return true;
            }
            return scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("异常", e);
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean pauseTask(int taskId) {
        SysTaskEntity byId = getById(taskId);
        if (Objects.isNull(byId)) {
            return true;
        }
        if (JOB_STATUS_PAUSE.equals(byId.getStatus())) {
            return true;
        }
        JobKey jobKey = buildJobKey(taskId);
        try {
            if (!scheduler.checkExists(jobKey)) {
                throw new RuntimeException("任务不存在，无需暂停，如需启用，请先部署");
            }
            scheduler.pauseJob(jobKey);
            SysTaskEntity updateEntity = new SysTaskEntity();
            updateEntity.setStatus(JOB_STATUS_PAUSE);
            updateEntity.setId(taskId);
            updateById(updateEntity);
            return true;
        } catch (SchedulerException e) {
            log.error("异常", e);
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resumeTask(int taskId) {
        SysTaskEntity byId = getById(taskId);
        if (Objects.isNull(byId)) {
            return true;
        }
        if (JOB_STATUS_RUNNING.equals(byId.getStatus())) {
            return true;
        }
        JobKey jobKey = buildJobKey(taskId);
        try {
            if (!scheduler.checkExists(jobKey)) {
                throw new RuntimeException("任务不存在，如需启用，请先部署");
            }
            scheduler.resumeJob(jobKey);
            SysTaskEntity updateEntity = new SysTaskEntity();
            updateEntity.setStatus(JOB_STATUS_RUNNING);
            updateEntity.setId(taskId);
            updateById(updateEntity);
            return true;
        } catch (SchedulerException e) {
            log.error("异常", e);
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Boolean triggerTask(int taskId) {
        SysTaskEntity byId = getById(taskId);
        if (Objects.isNull(byId)) {
            return true;
        }
        JobKey jobKey = buildJobKey(taskId);
        try {
            if (!scheduler.checkExists(jobKey)) {
                throw new RuntimeException("任务不存在，如需启用，请先部署");
            }
            scheduler.triggerJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            log.error("异常", e);
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<SysTaskEntity> taskList() {
        QueryWrapper<SysTaskEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_at");
        return list(queryWrapper);
    }

    @Override
    public List<SysTaskLogEntity> logByTaskId(int taskId) {
        QueryWrapper<SysTaskLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        queryWrapper.orderByDesc("create_at");
        return logMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<SysTaskLogEntity> pageLogByTaskId(int taskId, int current, int pageSize) {
        QueryWrapper<SysTaskLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        queryWrapper.orderByDesc("create_at");
        IPage<SysTaskLogEntity> iPage = new Page<>(current, pageSize);

        return logMapper.selectPage(iPage, queryWrapper);
    }

    @Override
    public IPage<SysTaskLogEntity> taskLogPageList(int current, int pageSize) {
        QueryWrapper<SysTaskLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_at");
        IPage<SysTaskLogEntity> iPage = new Page<>(current, pageSize);
        return logMapper.selectPage(iPage, queryWrapper);
    }

    @Override
    public SysTaskLogEntity createTaskLog(Integer taskId) {
        SysTaskLogEntity logEntity = new SysTaskLogEntity();
        logEntity.setTaskId(taskId);
        logEntity.setStatus(JOB_EXE_STATUS_START);
        logEntity.setMessage("执行任务");
        logEntity.setCreateAt(LocalDateTime.now());
        logMapper.insert(logEntity);
        return logMapper.selectById(logEntity);
    }

    @Override
    public void updateTaskLog(SysTaskLogEntity logEntity) {
        logMapper.updateById(logEntity);
    }

    @Override
    public void clear() throws SchedulerException {
        scheduler.clear();
    }

    private JobKey buildJobKey(Integer taskId) {
        return new JobKey(taskId.toString(), GROUP_NAME);
    }
}
