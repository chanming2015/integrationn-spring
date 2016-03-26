package com.github.chanming2015.common.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Description: Json--Java转换工具
 * Create Date:2016年3月26日 下午9:56:19
 * @author XuMaoSen
 * Version:1.0.0
 */
public class JacksonUtil
{
    private static final Logger log = LoggerFactory
            .getLogger(JacksonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJsonString(Object obj)
    {
        try
        {
            return OBJECT_MAPPER.writeValueAsString(obj);
        }
        catch (JsonProcessingException e)
        {
            log.error("toJsonString JsonProcessingException");
        }
        return null;
    }

    public static <T> T toJsonObject(Class<T> cls, String str)
    {
        try
        {
            return OBJECT_MAPPER.readValue(str, cls);
        }
        catch (JsonParseException e)
        {
            log.error("toJsonObject JsonParseException");
        }
        catch (JsonMappingException e)
        {
            log.error("toJsonObject JsonMappingException");
        }
        catch (IOException e)
        {
            log.error("toJsonObject IOException");
        }
        return null;
    }

}
