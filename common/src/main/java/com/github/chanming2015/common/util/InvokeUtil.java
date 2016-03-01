package com.github.chanming2015.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author XuMaoSen
 * Create Date:2016年3月1日
 * Description: 反射调用工具
 * Version:1.0.0
 */
public class InvokeUtil
{
    private static final Logger log = LoggerFactory.getLogger(InvokeUtil.class);

    /**
     * 
     * @author XuMaoSen
     * Create Time:2016年3月1日
     * Description 获取get方法
     * @param cls
     * @param fieldName
     * @return
     */
    private static Method getGetMethod(Class<?> cls, String fieldName)
    {
        Method method = null;
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase(Locale.US));
        sb.append(fieldName.substring(1));
        try
        {
            method = cls.getMethod(sb.toString());
        }
        catch (NoSuchMethodException e)
        {
            log.error("getGetMethod NoSuchMethodException", e);
        }
        catch (SecurityException e)
        {
            log.error("getGetMethod SecurityException", e);
        }
        return method;
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2016年3月1日
     * Description 执行get方法
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object invokeGet(Object obj, String fieldName)
    {
        Method method = getGetMethod(obj.getClass(), fieldName);
        try
        {
            return method.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException e)
        {
            log.error("invokeGet IllegalAccessException", e);
        }
        catch (IllegalArgumentException e)
        {
            log.error("invokeGet IllegalArgumentException", e);
        }
        catch (InvocationTargetException e)
        {
            log.error("invokeGet InvocationTargetException", e);
        }
        return null;
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2016年3月2日
     * Description 获取set方法
     * @param cls
     * @param fieldName
     * @return
     */
    private static Method getSetMethod(Class<?> cls, String fieldName)
    {
        Method method = null;
        Class<?>[] parameterTypes = new Class<?>[1];
        StringBuffer sb = new StringBuffer();
        try
        {
            Field field = cls.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase(Locale.US));
            sb.append(fieldName.substring(1));
            method = cls.getMethod(fieldName, parameterTypes);
        }
        catch (NoSuchFieldException e)
        {
            log.error("getSetMethod NoSuchFieldException", e);
        }
        catch (SecurityException e)
        {
            log.error("getSetMethod SecurityException", e);
        }
        catch (NoSuchMethodException e)
        {
            log.error("getSetMethod NoSuchMethodException", e);
        }
        return method;
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2016年3月2日
     * Description 执行set方法
     * @param obj
     * @param fieldName
     * @param value
     * @return
     */
    public static Object invokeSet(Object obj, String fieldName, Object value)
    {
        Method method = getSetMethod(obj.getClass(), fieldName);
        try
        {
            return method.invoke(obj, new Object[] {value});
        }
        catch (IllegalAccessException e)
        {
            log.error("invokeSet IllegalAccessException", e);
        }
        catch (IllegalArgumentException e)
        {
            log.error("invokeSet IllegalArgumentException", e);
        }
        catch (InvocationTargetException e)
        {
            log.error("invokeSet InvocationTargetException", e);
        }
        return null;
    }

    public static List<String> getSetFieldList(Class<?> cls)
    {
        List<String> fieldList = new ArrayList<String>();
        Method[] methods = cls.getMethods();
        for (Method method : methods)
        {
            String name = method.getName();
            if (name.startsWith("set"))
            {
                fieldList.add(name.substring(3, 4).toLowerCase(Locale.US)
                        + name.substring(4));
            }
        }
        return fieldList;
    }

    public static List<String> getGetFieldList(Class<?> cls)
    {
        List<String> fieldList = new ArrayList<String>();
        Method[] methods = cls.getMethods();
        for (Method method : methods)
        {
            String name = method.getName();
            if (name.startsWith("get"))
            {
                fieldList.add(name.substring(3, 4).toLowerCase(Locale.US)
                        + name.substring(4));
            }
        }
        return fieldList;
    }

    /**
     * 
     * @author XuMaoSen
     * Create Time:2016年3月2日
     * Description 获取该类的有get和set方法的字段的集合
     * @param cls
     * @return
     */
    public static List<String> getFieldList(Class<?> cls)
    {
        List<String> fieldSetList = getSetFieldList(cls);
        List<String> fieldGetList = getGetFieldList(cls);

        List<String> fieldList = new ArrayList<String>();
        for (String str : fieldGetList)
        {
            if (fieldSetList.contains(str))
            {
                fieldList.add(str);
            }
        }
        return fieldList;
    }
}
