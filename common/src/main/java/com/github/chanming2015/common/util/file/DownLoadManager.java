package com.github.chanming2015.common.util.file;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chanming2015.common.util.TaskUtil;

/**
 * Description: 
 * Create Date:2016年6月23日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class DownLoadManager
{
    private static final Logger logger = LoggerFactory.getLogger(DownLoadManager.class);

    private static final Map<String, Set<Future<?>>> monitorTask = new HashMap<String, Set<Future<?>>>();

    private static final long UNITSIZE = 100 * 1024;

    public static String doDownLoad(String remoteFileUrl, String tarDir) throws IOException
    {
        String fileName = new URL(remoteFileUrl).getFile();
        fileName = fileName.substring(fileName.lastIndexOf("=") + 1, fileName.length());
        fileName = String.format("%s/%s", tarDir, fileName);
        long fileSize = FileUtil.getRemoteFileSize(remoteFileUrl);

        if (fileSize == 0)
        {
            return null;
        }

        FileUtil.createFile(fileName, fileSize);

        long threadCount = fileSize % UNITSIZE == 0 ? fileSize / UNITSIZE : fileSize / UNITSIZE + 1;
        logger.info("共启动%d个线程.", threadCount);

        long offset = 0;
        if (fileSize <= UNITSIZE)
        {
            DownLoadThread downLoadThread = new DownLoadThread(remoteFileUrl, fileName, offset,
                    fileSize);
            putToMonitor(fileName, TaskUtil.submit(downLoadThread));
        }
        else
        {
            for (int i = 1; i < threadCount; i++)
            {
                DownLoadThread downLoadThread = new DownLoadThread(remoteFileUrl, fileName, offset,
                        UNITSIZE);
                putToMonitor(fileName, TaskUtil.submit(downLoadThread));
                offset += UNITSIZE;
            }

            // 最后一次下载
            long size = UNITSIZE;
            if (fileSize % UNITSIZE != 0)
            {
                size = fileSize - UNITSIZE * (threadCount - 1);
            }

            DownLoadThread downLoadThread = new DownLoadThread(remoteFileUrl, fileName, offset,
                    size);
            putToMonitor(fileName, TaskUtil.submit(downLoadThread));
        }

        while (!isFinish(fileName))
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                logger.error("DownLoadManager sleep error");
            }
        }
        return fileName;
    }

    /**
     * Description: 将任务放入监视器
     * Create Date:2016年6月29日
     * @author XuMaoSen
     */
    private static void putToMonitor(String key, Future<?> value)
    {
        Set<Future<?>> set = monitorTask.get(key);
        if (set == null)
        {
            set = new HashSet<Future<?>>();
            monitorTask.put(key, set);
        }

        if (!set.contains(value))
        {
            set.add(value);
        }
    }

    private static boolean isFinish(String key)
    {
        Set<Future<?>> set = monitorTask.get(key);
        if (set != null)
        {
            for (Future<?> future : set)
            {
                if (!future.isDone())
                {
                    return false;
                }
            }
            monitorTask.remove(key);
        }
        return true;
    }

}
