package com.github.chanming2015.common.util;

import java.lang.reflect.Array;

/**
 * Description: 
 * Create Date:2016年6月22日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class ArrayUtil
{

    @SuppressWarnings("unchecked")
    public static <T> T[] newInstance(Class<T> componentType, int length)
    {
        return (T[]) Array.newInstance(componentType, length);
    }
}
