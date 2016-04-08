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
     * @author XuMaoSen
     * Create Time:2016年3月1日
     * Description 获取get方法
     * @param cls
     * @param fieldName
     * @return
     */
    private static Method getGetMethod(Class<?> cls, String fieldName)
    {
        StringBuilder sb = new StringBuilder("get");
        sb.append(fieldName.substring(0, 1).toUpperCase(Locale.US));
        sb.append(fieldName.substring(1));
        try
        {
            return cls.getMethod(sb.toString());
        }
        catch (NoSuchMethodException e)
        {
            log.error("getGetMethod NoSuchMethodException", e);
        }
        catch (SecurityException e)
        {
            log.error("getGetMethod SecurityException", e);
        }
        return null;
    }

    /**
     * @author XuMaoSen
     * Create Time:2016年3月2日
     * Description 获取set方法
     * @param cls
     * @param fieldName
     * @return
     */
    private static Method getSetMethod(Class<?> cls, String fieldName)
    {
        Class<?>[] parameterTypes = new Class<?>[1];
        try
        {
            Field field = cls.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuilder sb = new StringBuilder("set");
            sb.append(fieldName.substring(0, 1).toUpperCase(Locale.US));
            sb.append(fieldName.substring(1));
            return cls.getMethod(sb.toString(), parameterTypes);
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
        return null;
    }

    /**
     * @author XuMaoSen
     * Create Time:2016年3月1日
     * Description 执行get方法
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object invokeGet(Object obj, String fieldName)
    {
        if (!EmptyUtil.isNull(obj, fieldName))
        {
            Method method = getGetMethod(obj.getClass(), fieldName);
            try
            {
                if (method != null)
                {
                    return method.invoke(obj, new Object[0]);
                }
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
        }

        return null;
    }

    /**
     * @author XuMaoSen
     * Create Time:2016年3月2日
     * Description 执行set方法
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void invokeSet(Object obj, String fieldName, Object value)
    {
        if (!EmptyUtil.isNull(obj, fieldName, value))
        {
            Method method = getSetMethod(obj.getClass(), fieldName);
            try
            {
                if (method != null)
                {
                    method.invoke(obj, value);
                }
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
        }
    }

    public enum MethodEnum
    {
        GET("get"), SET("set");

        private String value;

        private MethodEnum(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }

    }

    /**
     * @author XuMaoSen
     * Create Time:2016年3月2日
     * Description 获取该类的get或者set方法的字段的集合
     * @param cls
     * @param me
     * @return
     */
    public static List<String> getFieldList(Class<?> cls, MethodEnum me)
    {
        List<String> fieldList = new ArrayList<String>();
        Method[] methods = cls.getMethods();
        for (Method method : methods)
        {
            String name = method.getName();
            if (name.startsWith(me.getValue()))
            {
                fieldList.add(name.substring(3, 4).toLowerCase(Locale.US)
                        + name.substring(4));
            }
        }
        if (MethodEnum.GET.equals(me))
        {
            fieldList.remove("class");
        }
        return fieldList;
    }

    /**
     * @author XuMaoSen
     * Create Time:2016年3月2日
     * Description 获取该类的get和set方法的字段的集合(同时包含get和set方法)
     * @param cls
     * @return
     */
    public static List<String> getFieldList(Class<?> cls)
    {
        List<String> fieldSetList = getFieldList(cls, MethodEnum.SET);
        List<String> fieldGetList = getFieldList(cls, MethodEnum.GET);

        fieldSetList.retainAll(fieldGetList);

        return fieldSetList;
    }

}
