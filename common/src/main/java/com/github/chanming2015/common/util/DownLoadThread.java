package com.github.chanming2015.common.util;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 自定义下载线程
 * Create Date:2016年6月23日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class DownLoadThread implements Runnable
{
    private static final Logger logger = LoggerFactory.getLogger(DownLoadThread.class);

    /**
     * 待下载文件
     */
    private final String url;
    /**
     * 目标文件名
     */
    private final String fileName;
    /**
     * 偏移量
     */
    private long offset;
    /**
     * 本线程下载字节数
     */
    private final long length;

    public DownLoadThread(String url, String fileName, long offset, long length)
    {
        this.url = url;
        this.fileName = fileName;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public void run()
    {
        InputStream in = null;
        BufferedInputStream bis = null;
        try
        {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("RANGE",
                    String.format("bytes=%d-%d", offset, offset + length - 1));
            in = httpURLConnection.getInputStream();
            bis = new BufferedInputStream(in, 1024);
            byte[] buff = new byte[512];
            RandomAccessFile raf = null;
            try
            {
                raf = new RandomAccessFile(fileName, "rw");
                int size;
                while ((size = bis.read(buff)) != -1)
                {
                    raf.seek(offset);
                    raf.write(buff, 0, size);
                    offset += size;
                }
            }
            catch (FileNotFoundException e)
            {
                logger.error("File not found");
            }
            catch (IOException e)
            {
                logger.error("File write error");
            }
            finally
            {
                try
                {
                    StreamCloseUtil.close(raf);
                }
                catch (IOException e)
                {
                    logger.error("File close error");
                }
            }
        }
        catch (MalformedURLException e)
        {
            logger.error("url error");
        }
        catch (IOException e)
        {
            logger.error("Connection error");
        }
        finally
        {
            try
            {
                StreamCloseUtil.close(bis, in);
            }
            catch (IOException e)
            {
                logger.error("Stream close error");
            }
        }
    }
}
