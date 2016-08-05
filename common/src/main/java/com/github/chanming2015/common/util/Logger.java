package com.github.chanming2015.common.util;

import org.slf4j.LoggerFactory;

/**
 * Description:
 * Create Date:2016年8月5日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class Logger
{

    private final org.slf4j.Logger logger;

    private Logger()
    {
        logger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    private Logger(Class<?> clazz)
    {
        logger = LoggerFactory.getLogger(clazz);
    }

    private Logger(String name)
    {
        logger = LoggerFactory.getLogger(name);
    }

    public static Logger getRootLogger()
    {
        return new Logger();
    }

    public static Logger getLogger(Class<?> clazz)
    {
        return new Logger(clazz);
    }

    public static Logger getLogger(String name)
    {
        return new Logger(name);
    }

    // debug

    public void debug(String msg)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(msg);
        }
    }

    public void debug(String format, Object... args)
    {
        debug(String.format(format, args));
    }

    public void debug(String msg, Throwable t)
    {
        if (logger.isInfoEnabled())
        {
            logger.info(msg, t);
        }
    }

    // info

    public void info(String msg)
    {
        if (logger.isInfoEnabled())
        {
            logger.info(msg);
        }
    }

    public void info(String format, Object... args)
    {
        info(String.format(format, args));
    }

    public void info(String msg, Throwable t)
    {
        if (logger.isInfoEnabled())
        {
            logger.info(msg, t);
        }
    }

    // warn

    public void warn(String msg)
    {
        if (logger.isWarnEnabled())
        {
            logger.warn(msg);
        }
    }

    public void warn(String format, Object... args)
    {
        warn(String.format(format, args));
    }

    public void warn(String msg, Throwable t)
    {
        if (logger.isWarnEnabled())
        {
            logger.warn(msg, t);
        }
    }

    // error

    public void error(String msg)
    {
        if (logger.isErrorEnabled())
        {
            logger.error(msg);
        }
    }

    public void error(String format, Object... args)
    {
        error(String.format(format, args));
    }

    public void error(String msg, Throwable t)
    {
        if (logger.isErrorEnabled())
        {
            logger.error(msg, t);
        }
    }

}
