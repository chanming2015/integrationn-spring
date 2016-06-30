package com.github.chanming2015.common.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.github.chanming2015.common.util.Charsets;

/**
 * Description: 
 * Create Date:2016年6月30日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class ZipUtil
{
    private static final int TOOMANY = 1024;
    private static final int BUFFER = 512;
    private static final int TOOBIG = 0x6400000;

    /**
     * Description: 
     * Create Date:2016年6月30日
     * @author XuMaoSen
     */
    public static boolean zip(String targetFilePath, String... sourcePaths) throws IOException
    {
        FileUtil.createFile(targetFilePath);
        File targetFile = new File(targetFilePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream out = null;
        try
        {
            fos = new FileOutputStream(targetFile);
            bos = new BufferedOutputStream(fos, BUFFER * 4);
            out = new ZipOutputStream(bos, Charsets.UTF_8);
            for (String filePath : sourcePaths)
            {
                writeToZip(out, new File(filePath), "");
            }
            return true;
        }
        finally
        {
            StreamCloseUtil.close(out, bos, fos);
        }

    }

    /**
     * Description: 
     * Create Date:2016年6月30日
     * @author XuMaoSen
     * @throws IOException 
     */
    private static void writeToZip(ZipOutputStream out, File filePath, String base)
            throws IOException
    {
        if (filePath.isDirectory())
        {
            for (File file : filePath.listFiles())
            {
                writeToZip(out, file,
                        String.format("%s%s%s", base, filePath.getName(), File.separator));
            }
        }
        else
        {
            addFileToZip(out, filePath, String.format("%s%s", base, filePath.getName()));
        }
    }

    /**
     * Description: 
     * Create Date:2016年6月30日
     * @author XuMaoSen
     * @throws IOException 
     */
    private static void addFileToZip(ZipOutputStream out, File filePath, String base)
            throws IOException
    {
        byte[] buff = new byte[BUFFER];
        int len;
        ZipEntry entry = new ZipEntry(base);

        FileInputStream in = null;
        BufferedInputStream bis = null;
        try
        {
            in = new FileInputStream(filePath);
            bis = new BufferedInputStream(in, BUFFER * 4);
            out.putNextEntry(entry);
            while (-1 != (len = bis.read(buff)))
            {
                out.write(buff, 0, len);
            }
            out.flush();
        }
        finally
        {
            StreamCloseUtil.close(bis, in);
        }
    }

    /**
     * Description: 
     * Create Date:2016年6月30日
     * @author XuMaoSen
     */
    public static void unZip(String zipFilePath, String targetPath) throws IOException
    {
        File dictory = new File(targetPath);
        int total = 0;
        int entries = 0;
        if (!dictory.isDirectory() && !dictory.mkdirs())
        {
            return;
        }

        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(zipFilePath, Charsets.UTF_8);
            Enumeration<? extends ZipEntry> enu = zipFile.entries();
            while (enu.hasMoreElements())
            {
                ZipEntry entry = enu.nextElement();
                String name = sanitzeFileName(entry.getName(), targetPath);
                total = writeToDir(zipFile, entry, new File(name), total);
                if (total > TOOBIG)
                {
                    throw new IllegalStateException("File being unzipped is too big.");
                }
                entries++;
                if (entries > TOOMANY)
                {
                    throw new IllegalStateException("Too many files to unzip.");
                }
            }
        }
        finally
        {
            StreamCloseUtil.close(zipFile);
        }
    }

    /**
     * Description: 
     * Create Date:2016年6月30日
     * @author XuMaoSen
     * @throws IOException 
     */
    private static int writeToDir(ZipFile zipFile, ZipEntry entry, File file, int total)
            throws IOException
    {
        if (entry.isDirectory())
        {
            if (!file.mkdirs())
            {
                return 0;
            }
        }
        else
        {
            File parentFile = file.getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs())
            {
                return 0;
            }

            SeekableByteChannel sbc = null;
            InputStream in = null;
            BufferedInputStream bis = null;
            try
            {
                byte[] buff = new byte[BUFFER];
                in = zipFile.getInputStream(entry);
                bis = new BufferedInputStream(in, BUFFER * 4);
                sbc = FileSecurityUtil.getSeekableByteChannel(file.getAbsolutePath());
                int len;
                while (-1 != (len = bis.read(buff)))
                {
                    ByteBuffer buffer = ByteBuffer.wrap(buff, 0, len);
                    sbc.write(buffer);
                    total += len;
                }
            }
            finally
            {
                StreamCloseUtil.close(sbc, bis, in);
            }
        }
        return total;
    }

    /**
     * Description: 
     * Create Date:2016年6月30日
     * @author XuMaoSen
     * @throws IOException 
     */
    private static String sanitzeFileName(String entryName, String parent) throws IOException
    {
        File f = new File(parent, entryName);
        String canonicalPath = f.getCanonicalPath();

        File parentFile = new File(parent);
        String parentCanonicalPath = parentFile.getCanonicalPath();

        if (canonicalPath.startsWith(parentCanonicalPath))
        {
            return canonicalPath;
        }
        else
        {
            throw new IllegalStateException("File is outside extraction target directory");
        }
    }

}
