package com.github.chanming2015.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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

    public static <T> T xmlToBean(Class<T> cls, String content)
    {
        Reader reader = null;
        try
        {
            reader = new StringReader(content);
            return readerToBean(cls, reader);
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    log.error("reader close error");
                }
            }
        }
    }

    public static <T> T streamToBean(Class<T> cls, InputStream in)
    {
        Reader reader = null;
        try
        {
            reader = new InputStreamReader(in, Charsets.CHARSET_UTF_8);
            return readerToBean(cls, reader);
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    log.error("reader close error");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readerToBean(Class<T> cls, Reader reader)
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(cls);
            Unmarshaller u = jc.createUnmarshaller();

            // 检查所使用的底层xml解析库，默认禁止外部实体的解析
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);

            return (T) u.unmarshal(xsr);
        }
        catch (JAXBException e)
        {
            log.error("xmlToBean JAXBException");
        }
        catch (XMLStreamException e)
        {
            log.error("xmlToBean XMLStreamException");
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
