package com.github.chanming2015.common.util.config;

import java.util.concurrent.Callable;

/**
 * Description: 自定义延迟任务类
 * Create Date:2016年4月18日
 * @author XuMaoSen
 * Version:1.0.0
 * @param <T>
 */
public class ScheduleCallable<T> implements Callable<T>
{

    private Callable<T> task;

    public ScheduleCallable(Callable<T> task)
    {
        this.task = task;
    }

    @Override
    public T call() throws Exception
    {
        return TaskUtil.wait(task);
    }

}
