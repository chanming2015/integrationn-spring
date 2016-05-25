package com.github.chanming2015.common.util;

import java.text.DecimalFormat;
import java.util.HashMap;

import com.github.chanming2015.common.util.DateUtil.FormatType;

/**
 * Project:common
 * Package:com.github.chanming2015.common.util
 * FileName:QueuingMake.java
 * Comments:
 * JDK Version:
 * Author XuMaoSen
 * Create Date:2015年12月4日 下午7:29:08
 * Description: 
 * 系统流水号生成器,输入值为调用方在系统设定的来源代码以01,02,03,04等表示，00表示为未知来源
 * 格式解释：yyMMddHHmmssNNKKKK
 * yyMMddHHmmss为时间到秒的时间序列，不含年份的头两位。
 * NN为来源代码，系统定义，可以支持00～99，00表示未知来源
 * KKKK为该来源的消息队列定义，从0001～9999顺序递增，系统重启则从0001开始
 * 暂定为
 *   客户系统为01,
 *   交易系统为02，
 *   帐务系统为03,
 *   CMS系统为04
 * 举例：
 * Long queuing = QueuingMake.next("01");
 * Version:1.0.0
 */
public class QueuingMake
{
    private static final HashMap<String, Integer> map = new HashMap<String, Integer>();
    private static final ThreadLocal<DecimalFormat> decimalFormat = new ThreadLocal<DecimalFormat>()
    {

        @Override
        protected DecimalFormat initialValue()
        {
            return new DecimalFormat("0000");
        }

    };

    /**
     * @author XuMaoSen
     * Create Time:2015年12月4日 下午7:45:24
     * Description 获取流水号
     * @param src
     * @return
     */
    public final static Long next(String src)
    {
        String time = DateUtil.format(DateUtil.getNow(), FormatType.SMALLMICROSECONDS);
        if (src == null)
        {
            src = "00";
        }
        else
        {
            try
            {
                Integer.parseInt(src);
            }
            catch (Exception e)
            {
                src = "00";
            }
        }
        Integer now = map.get(src.toUpperCase());
        if (now == null || now == 9999)
        {
            now = new Integer(1);
        }
        else
        {
            now += 1;
        }
        String queuing = time + src + decimalFormat.get().format(now);
        map.put(src.toUpperCase(), now);
        return Long.parseLong(queuing);
    }

}
