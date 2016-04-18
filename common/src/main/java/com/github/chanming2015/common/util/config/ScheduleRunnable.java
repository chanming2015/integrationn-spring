package com.github.chanming2015.common.util.config;

/**
 * Description: 自定义延迟任务类
 * Create Date:2016年4月18日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class ScheduleRunnable implements Runnable
{

    private Runnable task;

    public ScheduleRunnable(Runnable task)
    {
        this.task = task;
    }

    @Override
    public void run()
    {
        TaskUtil.submit(task);
    }

}
