package com.github.chanming2015.common.util.access;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author XuMaoSen
 * Create Date:2015年12月4日 下午11:59:56
 * Description: http请求发送器
 * Version:1.0.0
 */
public class HttpAccessUtil
{

    private static final Logger logger = LoggerFactory
            .getLogger(HttpAccessUtil.class);

    /**
     * Description: 执行HTTP-GET请求
     * @param url 请求地址
     * @param head 请求头部信息
     * @param config 超时设置
     * Create Date:2016年4月8日
     * @author XuMaoSen
     */
    public static String get(String url, Map<String, String> head,
            RequestConfig config)
    {
        HttpGet httpGet = new HttpGet(url);

        if (head != null)
        {
            for (Entry<String, String> entry : head.entrySet())
            {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (config != null)
        {
            httpGet.setConfig(config);
        }

        return excute(httpGet);
    }

    /**
     * Description: 执行HTTP-DELETE请求
     * @param url 请求地址
     * @param head 请求头部信息
     * @param config 超时设置
     * Create Date:2016年4月8日
     * @author XuMaoSen
     */
    public static String delete(String url, Map<String, String> head,
            RequestConfig config)
    {
        HttpDelete httpDelete = new HttpDelete(url);

        if (head != null)
        {
            for (Entry<String, String> entry : head.entrySet())
            {
                httpDelete.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (config != null)
        {
            httpDelete.setConfig(config);
        }

        return excute(httpDelete);
    }

    /**
     * Description: 执行HTTP-POST请求
     * @param url 请求地址
     * @param jsonData json字符串
     * @param head 请求头部信息
     * @param config 超时设置
     * Create Date:2016年4月8日
     * @author XuMaoSen
     */
    public static String postJson(String url, String jsonData,
            Map<String, String> head, RequestConfig config)
    {
        HttpPost httpPost = new HttpPost(url);

        if (head != null)
        {
            for (Entry<String, String> entry : head.entrySet())
            {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (config != null)
        {
            httpPost.setConfig(config);
        }

        if (jsonData != null)
        {
            httpPost.setEntity(new StringEntity(jsonData,
                    ContentType.APPLICATION_JSON));
        }

        return excute(httpPost);
    }

    /**
     * Description: 执行HTTP-PUT请求
     * @param url 请求地址
     * @param jsonData json字符串
     * @param head 请求头部信息
     * @param config 超时设置
     * Create Date:2016年4月8日
     * @author XuMaoSen
     */
    public static String putJson(String url, String jsonData,
            Map<String, String> head, RequestConfig config)
    {
        HttpPut httpPut = new HttpPut(url);

        if (head != null)
        {
            for (Entry<String, String> entry : head.entrySet())
            {
                httpPut.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (config != null)
        {
            httpPut.setConfig(config);
        }

        if (jsonData != null)
        {
            httpPut.setEntity(new StringEntity(jsonData,
                    ContentType.APPLICATION_JSON));
        }

        return excute(httpPut);
    }

    /**
     * Description: 执行HTTP请求
     * Create Date:2016年4月8日
     * @author XuMaoSen
     */
    private static String excute(HttpUriRequest request)
    {
        String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try
        {
            // 执行HTTP请求
            CloseableHttpResponse response = httpclient.execute(request);

            try
            {
                // 获取响应对象
                HttpEntity entity = response.getEntity();
                if (entity != null)
                {
                    result = EntityUtils.toString(entity);
                }
                // 销毁实体
                EntityUtils.consume(entity);
            }
            finally
            {
                response.close();
            }
        }
        catch (ClientProtocolException e)
        {
            logger.error("httpclient protocol error");
        }
        catch (IOException e)
        {
            logger.error("httpclient execute error");
        }
        catch (ParseException e)
        {
            logger.error("entity parse error");
        }
        finally
        {
            try
            {
                httpclient.close();
            }
            catch (IOException e)
            {
                logger.error("httpclient close error");
            }
        }

        return result;
    }
}
