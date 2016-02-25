package com.github.chanming2015.common.util.result;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Project:common
 * Package:com.github.chanming2015.common.util.result
 * FileName:Result.java
 * Comments:
 * JDK Version:
 * Author XuMaoSen
 * Create Date:2015年12月4日 下午7:30:50
 * Description: 接口返回结果封装对象
 * Version:1.0.0
 */
@SuppressWarnings("serial")
public class Result<T> implements Serializable
{

    /**
     * 接口调用成功，有返回对象
     */
    public static <T> Result<T> newSuccess(T object)
    {
        Result<T> result = new Result<>();
        result.setObject(object);
        return result;
    }

    /**
     * 接口调用失败，返回错误结果
     */
    public static <T> Result<T> newFailure(Exception e, String error)
    {
        Result<T> result = newException(e);
        result.setError(error);
        return result;
    }

    /**
     * 接口调用失败，返回异常信息
     */
    public static <T> Result<T> newException(Exception e)
    {
        Result<T> result = new Result<>();
        result.setCode(-1);
        result.setException(e);
        result.setMessage(e.getMessage());
        return result;
    }

    /**
     * 接口返回码：0成功 -1失败
     */
    private int code;
    /**
     * 接口返回数据对象
     */
    private T object;
    /**
     * 开发人员约定的错误代码
     */
    private String error;
    /**
     * 错误信息，通常为异常信息
     */
    private String message;
    /**
     * 抓住的异常
     */
    private Exception exception;

    /** 判断返回结果是否成功 */
    public boolean success()
    {
        return code == 0;
    }

    /** 判断返回结果是否有结果对象 */
    public boolean hasObject()
    {
        return code == 0 && object != null;
    }

    /** 判断返回结果是否有异常 */
    public boolean hasException()
    {
        return exception != null;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public T getObject()
    {
        return object;
    }

    public void setObject(T object)
    {
        this.object = object;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Exception getException()
    {
        return exception;
    }

    public void setException(Exception exception)
    {
        this.exception = exception;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder("Result");
        if (object != null)
        {
            result.append("<");
            result.append(object.getClass().getSimpleName());
            result.append(">");
        }
        result.append(": {code=");
        result.append(code);
        if (object != null)
        {
            if (object.getClass().isArray())
            {
                result.append(", object=");
                result.append(Arrays.toString(((Object[]) object)));
            }
            else
            {
                result.append(", object=");
                result.append(object);
            }
        }
        if (error != null)
        {
            result.append(", error=");
            result.append(error);
        }
        if (message != null)
        {
            result.append(", message=");
            result.append(message);
        }
        if (exception != null)
        {
            StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));
            result.append(", exception=");
            result.append(stringWriter.toString());
        }
        result.append(" }");
        return result.toString();
    }
}
