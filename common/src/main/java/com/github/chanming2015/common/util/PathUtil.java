package com.github.chanming2015.common.util;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getExtendlibs()
    {
        List<String> libs = new ArrayList<String>();
        // 获取工程目录
        String projectPath = getProjectPath();
        // 获取classpath目录
        String[] paths = getClassPath();
        String projectLibPath = String.format("%s\\lib\\", projectPath);
        for (String path : paths)
        {
            if (path.contains(projectLibPath))
            {
                libs.add(path);
            }
        }
        return libs;
    }
}
