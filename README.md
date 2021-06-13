# 介绍
本项目实现快速集成Quartz到SpringBoot项目中的功能，并提供API方式实现以下功能
- 添加任务 post /quartz/createTask
- 修改任务 post /quartz/createTask
- 删除任务 get /quartz/deleteTask
- 暂停任务 get /quartz/pauseTask
- 恢复任务 get /quartz/resumeTask
- 触发任务 get /quartz/triggerTask
- 查询所有任务 get /quartz/taskList
- 分页查询所有任务执行日志 get /quartz/taskLogPageList
- 分页查询某个任务执行日志 get /quartz/taskLogPageListById

# 准备
初始化数据库表，执行resources/mysql/init.sql文件，创建`sys_task_log`和`sys_task`表，分别表示任务执行日志表和任务表。

添加`dependency`到`pom.xml`中

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
```

# 集成

## 1. 拷贝 com 包到项目的 java 包下面

## 2. 在项目中添加`QuartzConfig`文件，内容如下

```java
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.baiziio.lib.quartz.controller",
        "com.baiziio.lib.quartz.service",
})
@MapperScan(value = {"com.baiziio.lib.quartz.dao.mapper"})
public class QuartzConfig {

}
```

## 3. 配置 quartz

在`application.yml`中添加关于`quartz`的配置信息

```yaml
spring:
  quartz:
    auto-startup: true
    scheduler-name: quartz
    job-store-type: jdbc
    wait-for-jobs-to-complete-on-shutdown: true
    overwrite-existing-jobs: false
    jdbc:
      initialize-schema: never
    properties:
      org:
        quartz:
          jobStore:
            isClustered: true
#            class: org.quartz.impl.jdbcjobstore.JobStoreTX
#            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
          scheduler:
            instanceId: AUTO
```

更多关于`quartz`的配置可以查看 [quartzstarter](https://github.com/zemian/quartz-starter/tree/master/learn/src/main/resources/zemian
/quartzstarter)，里面有各种场景下的配置信息。

如果接口不满足需求，可自行改造

