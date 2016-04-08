package com.github.chanming2015.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project:common
 * Package:com.github.chanming2015.common.util
 * FileName:DateUtil.java
 * Comments:
 * JDK Version:
 * Author XuMaoSen
 * Create Date:2015年12月4日 下午7:28:04
 * Description: 常见日期类型处理：字符串、日期Date、长整数new Date(long)，parse(time).getTime()
 * Version:1.0.0
 */
public class DateUtil
{

    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    /**
     * yyyy-MM-dd
     */
    private static final ThreadLocal<SimpleDateFormat> day = new ThreadLocal<SimpleDateFormat>()
    {

        @Override
        protected SimpleDateFormat initialValue()
        {

            return new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        }

    };

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private static final ThreadLocal<SimpleDateFormat> dayTime = new ThreadLocal<SimpleDateFormat>()
    {

        @Override
        protected SimpleDateFormat initialValue()
        {

            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        }

    };

    /**
     * yyMMddHHmmss
     */
    private static final ThreadLocal<SimpleDateFormat> smallMicroSeconds = new ThreadLocal<SimpleDateFormat>()
    {

        @Override
        protected SimpleDateFormat initialValue()
        {

            return new SimpleDateFormat("yyMMddHHmmss", Locale.US);
        }

    };

    public static enum FormatType
    {
        /** yyyy-MM-dd, curdate() */
        DAY,
        /** yyyy-MM-dd HH:mm:ss, now() */
        DAYTIME,
        /** yyMMddHHmmss, 12位毫秒时间序列号 */
        SMALLMICROSECONDS
    };

    public static Date parse(String time, FormatType type)
    {
        if (EmptyUtil.isEmpty(time))
        {
            return null;
        }

        try
        {
            switch (type)
            {
            case DAY:
                return day.get().parse(time);
            case DAYTIME:
                return dayTime.get().parse(time);
            case SMALLMICROSECONDS:
                return smallMicroSeconds.get().parse(time);
            }
        }
        catch (ParseException e)
        {
            log.error(time + " ParseException");
        }

        return null;
    }

    public static String format(Date date, FormatType type)
    {
        if (date == null)
        {
            return null;
        }
        switch (type)
        {
        case DAY:
            return day.get().format(date);
        case DAYTIME:
            return dayTime.get().format(date);
        case SMALLMICROSECONDS:
            return smallMicroSeconds.get().format(date);
        }
        return null;
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年10月10日 下午4:15:05
     * Description 使用date初始化Calendar
     * @param date
     * @return
     */
    private static Calendar initCalendar(Date date)
    {
        Calendar c = Calendar.getInstance();
        if (date != null)
        {
            c.setTime(date);
        }
        return c;
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午9:53:48
     * Description 设置当天开始时间（0时0分0秒）
     * @param c
     */
    private static void setDayBegin(Calendar c)
    {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午9:53:48
     * Description 设置当天结束时间（23时59分59秒）
     * @param c
     */
    private static void setDayEnd(Calendar c)
    {
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午9:30:23
     * Description 获取当天开始时间
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date)
    {
        Calendar c = initCalendar(date);
        setDayBegin(c);
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午9:30:23
     * Description 获取当天结束时间
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date)
    {
        Calendar c = initCalendar(date);
        setDayEnd(c);
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午9:55:04
     * Description 设置时间往后增加interval天（可以是负数）
     * @param date
     * @param interval
     * @return
     */
    public static Date getDayAdd(Date date, int interval)
    {
        Calendar c = initCalendar(date);
        c.add(Calendar.DAY_OF_MONTH, interval);
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午10:07:03
     * Description 设置时间往后增加interval分钟
     * @param date
     * @param interval
     * @return
     */
    public static Date getTimeAdd(Date date, int interval)
    {
        Calendar c = initCalendar(date);
        c.add(Calendar.MINUTE, interval);
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午9:31:20
     * Description 获取下一天开始时间
     * @param date
     * @return
     */
    public static Date getNextDayBegin(Date date)
    {
        Calendar c = initCalendar(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        setDayBegin(c);
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午9:31:20
     * Description 获取下一天结束时间
     * @param date
     * @return
     */
    public static Date getNextDayEnd(Date date)
    {
        Calendar c = initCalendar(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        setDayEnd(c);
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午9:32:03
     * Description 获取当月第一天开始时间
     * @param date
     * @return
     */
    public static Date getMonthDayBegin(Date date)
    {
        Calendar c = initCalendar(date);
        c.set(Calendar.DATE, 1);
        setDayBegin(c);
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月5日 上午9:33:08
     * Description 获取当月最后一天结束时间
     * @param date
     * @return
     */
    public static Date getMonthDayEnd(Date date)
    {
        Calendar c = initCalendar(date);
        int max = c.getActualMaximum(Calendar.DATE);
        c.set(Calendar.DATE, max);
        setDayEnd(c);
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年10月10日 下午4:32:25
     * Description 使用时间参数设置时间
     * @param date
     * @param hours
     * @param minutes
     * @param seconds
     * @return
     */
    public static Date getTime(Date date, int hours, int minutes, int seconds)
    {
        Calendar c = initCalendar(date);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, seconds);
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月4日 下午4:24:47
     * Description 获取当前系统时间
     * @return
     */
    public static Date getNow()
    {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2015年8月3日 下午4:30:25
     * Description 计算参数时间跟当前时间的差数（当前时间 - 参数时间）
     * @param date
     * @return 相差天数
     */
    public static int getDayDiffer(Date date)
    {
        Calendar c = Calendar.getInstance();
        long time = c.getTimeInMillis() - date.getTime();
        int result = (int) (time / 86400000); // 1000*60*60*24 = 86400000
        return result;
    }
}
