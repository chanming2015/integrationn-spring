package com.github.chanming2015.common.util;

/**
 * Description: 
 * Create Date:2016年6月21日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class PathUtil
{

    public static String getProjectPath()
    {
        return System.getProperty("user.dir");
    }

    public static String[] getClassPath()
    {
        return System.getProperty("java.class.path").split(";");
    }

}
