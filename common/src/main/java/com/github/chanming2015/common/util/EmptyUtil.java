package com.github.chanming2015.common.util;

/**
 * Description: 
 * Create Date:2016年4月9日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class EmptyUtil
{

    /**
     * Description: 检验是否有NULL
     * Create Date:2016年4月9日
     * @author XuMaoSen
     */
    public static boolean isNull(Object... objs)
    {
        for (Object obj : objs)
        {
            if (obj == null)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Description: 检验是否有NULL和""的情况
     * Create Date:2016年4月9日
     * @author XuMaoSen
     */
    public static boolean isEmpty(String... args)
    {
        for (String str : args)
        {
            if (str == null || 0 == str.length())
            {
                return true;
            }
        }

        return false;
    }

}
