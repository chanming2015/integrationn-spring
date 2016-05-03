package com.github.chanming2015.common.util.config;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 执行单次任务或定时任务工具类
 * Create Date:2016年4月18日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class TaskUtil
{
    private static final Logger logger = LoggerFactory.getLogger(TaskUtil.class);

    private static ExecutorService cachedExecutor = null;
    private static ScheduledExecutorService scheduledExecutor = null;
    private static Map<Runnable, Future<?>> keepRunningTasks = null;
    private static Map<Future<?>, CallBack> callbackdTasks = null;

    static
    {
        // 创建一个缓存线程池（立即执行）
        cachedExecutor = Executors.newCachedThreadPool(new TaskUtilThreadFactory("cached"));
        // 创建一个单一线程的线程池（延迟执行）
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new TaskUtilThreadFactory(
                "scheduled"));
        // 线程池随虚拟机关闭
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                TaskUtil.shutdown();
            }
        });

        logger.info("TaskUtil inited");
    }

    /**
     * Description: 关闭线程池，通常情况下不必手动调用
     * Create Date:2016年4月18日
     * @author XuMaoSen
     */
    public static void shutdown()
    {
        scheduledExecutor.shutdown();
        cachedExecutor.shutdown();
        if (!scheduledExecutor.isTerminated())
        {
            scheduledExecutor.shutdownNow();
        }
        if (!cachedExecutor.isTerminated())
        {
            scheduledExecutor.shutdownNow();
        }
        logger.info("TaskUtil executors shutdown.");
    }

    /*
     * ****************** 立即执行任务 *****************************
     */

    /**
     * Description: 提交任务，获取返回值（非线程阻塞）
     * Create Date:2016年4月18日
     * @author XuMaoSen
     */
    public static Future<?> submit(Runnable task)
    {
        return cachedExecutor.submit(task);
    }

    /**
     * Description: 提交任务，获取返回值（非线程阻塞）
     * Create Date:2016年4月18日
     * @author XuMaoSen
     */
    public static <T> Future<T> submit(Callable<T> task)
    {
        return cachedExecutor.submit(task);
    }

    /**
     * Description: 提交任务，等待返回值（线程阻塞，超时等待30秒）
     * Create Date:2016年4月18日
     * @author XuMaoSen
     */
    public static <T> T wait(Callable<T> task)
    {
        Future<T> future = cachedExecutor.submit(task);
        try
        {
            return future.get(30, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            logger.error("exec wait InterruptedException");
        }
        catch (ExecutionException e)
        {
            logger.error("exec wait ExecutionException");
        }
        catch (TimeoutException e)
        {
            logger.error("exec wait TimeoutException");
        }
        return null;
    }

    /*
     * ****************** 延迟执行任务 *****************************
     */

    /**
     * Description: 延迟执行任务一次（线程阻塞）
     * Create Date:2016年4月18日
     * @author XuMaoSen
     */
    public static <T> ScheduledFuture<T> schedule(Callable<T> task, long delay, TimeUnit unit)
    {
        return scheduledExecutor.schedule(new ScheduleCallable<T>(task), delay, unit);
    }

    /**
     * Description: 延迟执行任务一次，例如延迟5秒：schedule(task,5,TimeUnit.SECONDS)
     * Create Date:2016年4月18日
     * @author XuMaoSen
     */
    public static ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit)
    {
        return scheduledExecutor.schedule(new ScheduleRunnable(task), delay, unit);
    }

    /**
     * Description: 定时执行任务一次，比如下午两点：scheduleAt(task, DateUtils.setHours(new Date(), 13))
     * Create Date:2016年4月19日
     * @author XuMaoSen
     */
    public static ScheduledFuture<?> scheduleAt(Runnable task, Date time)
    {
        long mills = time.getTime() - System.currentTimeMillis();
        return schedule(task, mills > 0 ? mills : 5, TimeUnit.MILLISECONDS);
    }

    /**
     * Description: 定时重复执行延迟任务，比如延迟5秒，每10分钟执行一次：scheduleAtFixRate(task, 5, TimeUnit.MINUTES.toSeconds(10), TimeUnit.SECONDS)
     * Create Date:2016年4月18日
     * @author XuMaoSen
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay,
            long delay, TimeUnit unit)
    {
        return scheduledExecutor.scheduleWithFixedDelay(new ScheduleRunnable(task), initialDelay,
                delay, unit);
    }

    /**
     * Description: 定时重复执行任务，比如下午两点开始，每小时执行一次：scheduleAtFixRate(task, DateUtils.setHours(new Date(), 13), 1, TimeUnit.HOURS) 
     * Create Date:2016年4月19日
     * @author XuMaoSen
     */
    public static ScheduledFuture<?> scheduleAtFixtRate(Runnable task, Date time, long delay,
            TimeUnit unit)
    {
        long mills = time.getTime() - System.currentTimeMillis();
        return scheduleAtFixedRate(task, mills > 0 ? mills : 5, unit.toMillis(delay),
                TimeUnit.MILLISECONDS);
    }

    private static void checkInitCachedTasks()
    {
        if (keepRunningTasks != null)
        {
            return;
        }

        keepRunningTasks = new HashMap<Runnable, Future<?>>();
        callbackdTasks = new HashMap<Future<?>, CallBack>();
        scheduleAtFixedRate(new CachedTasksMonitor(), 1, 1, TimeUnit.MINUTES);
    }

    /**
     * Description: 自动保持任务持续运行，每分钟监视一次
     * Create Date:2016年4月19日
     * @author XuMaoSen
     */
    public static Future<?> submitKeepRunning(Runnable task)
    {
        Future<?> future = submit(task);
        checkInitCachedTasks();
        synchronized (keepRunningTasks)
        {
            keepRunningTasks.put(task, future);
        }
        return future;
    }

    /**
     * Description: 提交带返回值的任务，支持后续处理
     * Create Date:2016年4月19日
     * @author XuMaoSen
     */
    public static <T> Future<T> submit(Callable<T> task, CallBack callback)
    {
        Future<T> future = submit(task);
        checkInitCachedTasks();
        if (callback != null)
        {
            synchronized (callbackdTasks)
            {
                callbackdTasks.put(future, callback);
            }
        }
        return future;
    }

    /**
     * Description: 监视需要保持运行的任务
     * Create Date:2016年4月18日
     * @author XuMaoSen
     * Version:1.0.0
     */
    private static class CachedTasksMonitor implements Runnable
    {
        @Override
        public void run()
        {
            if (keepRunningTasks.size() > 0)
            {
                synchronized (keepRunningTasks)
                {
                    Map<Runnable, Future<?>> tempTasks = new HashMap<Runnable, Future<?>>();
                    for (Entry<Runnable, Future<?>> entry : keepRunningTasks.entrySet())
                    {
                        Runnable task = entry.getKey();
                        Future<?> future = entry.getValue();
                        if (future.isDone())
                        {
                            future = submit(task);
                            tempTasks.put(task, future);
                        }
                    }
                    if (tempTasks.size() > 0)
                    {
                        keepRunningTasks.putAll(tempTasks);
                    }
                }
            }

            if (callbackdTasks.size() > 0)
            {
                synchronized (callbackdTasks)
                {
                    List<Future<?>> callbackedFutures = new LinkedList<Future<?>>();
                    for (Entry<Future<?>, CallBack> entry : callbackdTasks.entrySet())
                    {
                        final Future<?> future = entry.getKey();
                        final CallBack callback = entry.getValue();
                        if (future.isDone())
                        {
                            try
                            {
                                final Object result = future.get(5, TimeUnit.SECONDS);
                                submit(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        callback.handle(result);
                                    }
                                });
                                callbackedFutures.add(future);
                            }
                            catch (InterruptedException e)
                            {
                                logger.error("exec wait InterruptedException");
                            }
                            catch (ExecutionException e)
                            {
                                logger.error("exec wait ExecutionException");
                            }
                            catch (TimeoutException e)
                            {
                                logger.error("exec wait TimeoutException");
                            }
                        }
                    }

                    for (Future<?> future : callbackedFutures)
                    {
                        callbackdTasks.remove(future);
                    }
                }
            }
        }
    }

    /**
     * Description: 回调结果处理接口
     * Create Date:2016年4月18日
     * @author XuMaoSen
     * Version:1.0.0
     */
    public static interface CallBack
    {
        void handle(Object result);
    }
}
