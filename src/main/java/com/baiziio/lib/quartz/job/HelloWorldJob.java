package com.baiziio.lib.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author juwencheng
 * @date 2021 2021/6/9 6:03 下午
 */
public class HelloWorldJob extends LoggerJob {
    @Override
    public void doExecute(JobExecutionContext context) throws JobExecutionException {
        // 测试抛出异常后，是如何处理的，没有特殊处理，如果是恢复的话可能会一直执行
        System.out.println("你好，Quartz~");
        // 事务有啥影响吗？
//        throw new JobExecutionException("故意抛出后");
    }
}
