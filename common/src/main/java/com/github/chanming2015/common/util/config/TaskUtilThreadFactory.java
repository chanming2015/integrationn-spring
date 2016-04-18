package com.github.chanming2015.common.util.config;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 任务工具线程工厂
 * Create Date:2016年4月18日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class TaskUtilThreadFactory implements ThreadFactory
{

    private final static AtomicInteger taskutilThreadNumber = new AtomicInteger(1);
    private final String threadNamePrefix;

    TaskUtilThreadFactory(String threadNamePrefix)
    {
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public Thread newThread(Runnable r)
    {
        Thread t = new Thread(r, String.format("TaskUtil-%d-%s",
                taskutilThreadNumber.getAndIncrement(), this.threadNamePrefix));
        t.setDaemon(true);
        t.setPriority(Thread.MIN_PRIORITY);
        return t;
    }

}
