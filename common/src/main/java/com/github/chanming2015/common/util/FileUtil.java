package com.github.chanming2015.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 
 * Create Date:2016年5月3日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class FileUtil
{

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Description: 将字符串写入文件
     * @param file 待写入文件
     * @param content 写入内容
     * Create Date:2016年5月3日
     * @author XuMaoSen
     */
    public static boolean writeString(String filePath, String content)
    {
        return writeString(new File(filePath), content, false);
    }

    /**
     * Description: 将字符串写入文件
     * @param file 待写入文件
     * @param content 写入内容
     * @param append 是否采用追加方式
     * Create Date:2016年5月3日
     * @author XuMaoSen
     */
    public static boolean writeString(String filePath, String content, boolean append)
    {
        return writeString(new File(filePath), content, append);
    }

    /**
     * Description: 将字符串写入文件
     * @param file 待写入文件
     * @param content 写入内容
     * Create Date:2016年5月3日
     * @author XuMaoSen
     */
    public static boolean writeString(File file, String content)
    {
        return writeString(file, content, false);
    }

    /**
     * Description: 将字符串写入文件
     * @param file 待写入文件
     * @param content 写入内容
     * @param append 是否采用追加方式
     * Create Date:2016年5月3日
     * @author XuMaoSen
     */
    public static boolean writeString(File file, String content, boolean append)
    {
        if (!EmptyUtil.isNull(file, content))
        {
            byte[] buff = content.getBytes(Charsets.CHARSET_UTF_8);
            OpenOption option = StandardOpenOption.CREATE;
            if (append && file.exists())
            {
                option = StandardOpenOption.APPEND;
            }

            try
            {
                Files.write(Paths.get(file.toURI()), buff, option);
                return true;
            }
            catch (IOException e)
            {
                log.error("writeString IOException");
            }
        }

        return false;
    }

    /**
     * Description: 读取文件内容
     * @param file 待读取文件路径
     * Create Date:2016年5月25日
     * @author XuMaoSen
     */
    public static String readString(String filePath)
    {
        return readString(new File(filePath));
    }

    /**
     * Description: 读取文件内容
     * @param file 待读取文件
     * Create Date:2016年5月25日
     * @author XuMaoSen
     */
    public static String readString(File file)
    {
        try
        {
            byte[] buff = Files.readAllBytes(Paths.get(file.toURI()));
            return new String(buff, Charsets.CHARSET_UTF_8);
        }
        catch (IOException e)
        {
            log.error("readString IOException");
        }
        return null;
    }
}
