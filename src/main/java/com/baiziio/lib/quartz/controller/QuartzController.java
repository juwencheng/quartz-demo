package com.baiziio.lib.quartz.controller;

import com.baiziio.lib.quartz.dao.entity.SysTaskEntity;
import com.baiziio.lib.quartz.dao.entity.SysTaskLogEntity;
import com.baiziio.lib.quartz.service.SysTaskService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author juwencheng
 * @date 2021 2021/6/9 5:39 下午
 */
@Slf4j
@RestController
@RequestMapping("/quartz")
public class QuartzController {

    @Autowired
    private SysTaskService taskService;


    @PostMapping("createTask")
    public SysTaskEntity createTask(@RequestBody SysTaskEntity taskEntity) {
//        String cron = "0/10 * * * * ? *";
        return taskService.createOrUpdateTask(taskEntity);
    }

    @GetMapping("deleteTask")
    public Boolean deleteTask(@RequestParam Integer id) {
        return taskService.deleteTask(id);
    }

    @GetMapping("pauseTask")
    public Boolean pauseTask(@RequestParam Integer id) {
        return taskService.pauseTask(id);
    }

    @GetMapping("resumeTask")
    public Boolean resumeTask(@RequestParam Integer id) {
        return taskService.resumeTask(id);
    }

    @GetMapping("triggerTask")
    public Boolean triggerTask(@RequestParam Integer id) {
        return taskService.triggerTask(id);
    }

    @GetMapping("taskList")
    public List<SysTaskEntity> taskList() {
        return taskService.taskList();
    }

    @GetMapping("taskLogPageList")
    public IPage<SysTaskLogEntity> taskLogPageList(@RequestParam int current, @RequestParam int pageSize) {
        return taskService.taskLogPageList(current, pageSize);
    }

    @GetMapping("taskLogPageListById")
    public IPage<SysTaskLogEntity> taskLogByIdPageList(@RequestParam int taskId, @RequestParam int current,
                                                       @RequestParam int pageSize) {
        return taskService.pageLogByTaskId(taskId, current, pageSize);
    }

    @GetMapping("clear")
    public void clear() throws SchedulerException {
        taskService.clear();
    }

}
