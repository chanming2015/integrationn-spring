package com.github.chanming2015.common.util.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chanming2015.common.util.Charsets;
import com.github.chanming2015.common.util.EmptyUtil;

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
     * @throws IOException 
     */
    public static void writeString(String filePath, String content) throws IOException
    {
        writeString(new File(filePath), content, false);
    }

    /**
     * Description: 将字符串写入文件
     * @param file 待写入文件
     * @param content 写入内容
     * @param append 是否采用追加方式
     * Create Date:2016年5月3日
     * @author XuMaoSen
     * @throws IOException 
     */
    public static void writeString(String filePath, String content, boolean append)
            throws IOException
    {
        writeString(new File(filePath), content, append);
    }

    /**
     * Description: 将字符串写入文件
     * @param file 待写入文件
     * @param content 写入内容
     * Create Date:2016年5月3日
     * @author XuMaoSen
     * @throws IOException 
     */
    public static void writeString(File file, String content) throws IOException
    {
        writeString(file, content, false);
    }

    /**
     * Description: 将字符串写入文件
     * @param file 待写入文件
     * @param content 写入内容
     * @param append 是否采用追加方式
     * Create Date:2016年5月3日
     * @author XuMaoSen
     */
    public static void writeString(File file, String content, boolean append) throws IOException
    {
        if (!EmptyUtil.isNull(file, content))
        {
            byte[] buff = content.getBytes(Charsets.CHARSET_UTF_8);
            OpenOption option = StandardOpenOption.CREATE;
            if (append && file.exists())
            {
                option = StandardOpenOption.APPEND;
            }

            Files.write(Paths.get(file.toURI()), buff, option);
        }
    }

    /**
     * Description: 读取文件内容
     * @param file 待读取文件路径
     * Create Date:2016年5月25日
     * @author XuMaoSen
     * @throws IOException 
     */
    public static String readString(String filePath) throws IOException
    {
        return readString(new File(filePath));
    }

    /**
     * Description: 读取文件内容
     * @param file 待读取文件
     * Create Date:2016年5月25日
     * @author XuMaoSen
     * @throws IOException 
     */
    public static String readString(File file) throws IOException
    {
        byte[] buff = Files.readAllBytes(Paths.get(file.toURI()));
        return new String(buff, Charsets.CHARSET_UTF_8);
    }

    /**
     * Description: 获取指定jar包中的指定资源
     * Create Date:2016年6月23日
     * @author XuMaoSen
     */
    public static List<String> findFile(List<String> jarPaths, String fileName) throws IOException,
            URISyntaxException
    {
        List<String> list = new ArrayList<String>();
        List<File> jarFiles = new ArrayList<File>(jarPaths.size());

        // 读取jar包，为获取jar包URL做准备
        for (String jarPath : jarPaths)
        {
            jarFiles.add(new File(jarPath));
        }

        URL[] urls = new URL[jarFiles.size()];
        for (int i = 0; i < jarFiles.size(); i++)
        {
            urls[i] = jarFiles.get(i).toURI().toURL();
        }

        URLClassLoader ucl = null;
        try
        {
            ucl = new URLClassLoader(urls);
            Enumeration<URL> enumeration = ucl.findResources(fileName);
            if (enumeration != null)
            {
                while (enumeration.hasMoreElements())
                {
                    list.add(enumeration.nextElement().toURI().toURL().toString());
                }
            }
        }
        finally
        {
            if (ucl != null)
            {
                ucl.close();
            }
        }
        return list;
    }

    /**
     * Description: 搜索特定文件
     * Create Date:2016年6月23日
     * @author XuMaoSen
     */
    public static List<File> filePattren(File file, Pattern p)
    {
        if (file == null || p == null)
        {
            return null;
        }

        if (file.isFile())
        {
            if (p.matcher(file.getName()).matches())
            {
                List<File> list = new ArrayList<File>(1);
                list.add(file);
                return list;
            }
        }
        else if (file.isDirectory())
        {
            File[] files = file.listFiles();
            if (files != null && files.length > 0)
            {
                List<File> list = new ArrayList<File>();
                for (File file2 : files)
                {
                    List<File> rlist = filePattren(file2, p);
                    if (rlist != null)
                    {
                        list.addAll(rlist);
                    }
                }
                return list;
            }
        }
        return null;
    }

    /**
     * Description:  从指定目录下搜索特定文件
     * Create Date:2016年6月23日
     * @author XuMaoSen
     */
    public static List<File> getFiles(String dir, String target)
    {
        File file = new File(dir);
        target = target.replace(".", "\\.").replace("*", ".*").replace("?", ".?");
        Pattern p = Pattern.compile(String.format("^%s$", target));
        return filePattren(file, p);
    }

    /**
     * Description: 获取远程文件大小
     * Create Date:2016年6月24日
     * @author XuMaoSen
     */
    public static long getRemoteFileSize(String remoteFileUrl) throws IOException
    {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(remoteFileUrl)
                .openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode >= 400)
        {
            log.error("服务器响应码：%d", responseCode);
            return 0;
        }

        long fileSize = httpURLConnection.getContentLengthLong();

        log.info("文件大小：%d字节", fileSize);
        return fileSize;
    }

    public static void createFile(String fileName, long fileSize) throws IOException
    {
        RandomAccessFile raf = null;
        try
        {
            raf = new RandomAccessFile(fileName, "rw");
            raf.setLength(fileSize);
        }
        finally
        {
            if (raf != null)
            {
                raf.close();
            }
        }
    }

}
