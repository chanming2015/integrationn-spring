package com.github.chanming2015.common.util;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: Jaxb工具--Java、XML转换工具
 * Create Date:2016年3月26日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class JaxbUtil
{
    private static final Logger log = LoggerFactory.getLogger(JaxbUtil.class);

    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(Class<T> cls, String content)
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(cls);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(new StringReader(content));
        }
        catch (JAXBException e)
        {
            log.error("xmlToBean JAXBException");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T streamToBean(Class<T> cls, InputStream in)
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(cls);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(in);
        }
        catch (JAXBException e)
        {
            log.error("xmlToBean JAXBException");
        }
        return null;
    }

    public static <T> String beanToXml(T t)
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(t.getClass());
            Marshaller u = jc.createMarshaller();
            StringWriter sw = new StringWriter();
            u.marshal(t, sw);
            return sw.toString();
        }
        catch (JAXBException e)
        {
            log.error("beanToXml JAXBException");
        }
        return null;
    }

}
