package com.github.chanming2015.common.util.file;

import java.io.Closeable;
import java.io.IOException;

/**
 * Description: 
 * Create Date:2016年6月23日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class StreamCloseUtil
{

    public static void close(Closeable... closeables) throws IOException
    {
        for (Closeable closeable : closeables)
        {
            if (closeable != null)
            {
                closeable.close();
            }
        }
    }
}
