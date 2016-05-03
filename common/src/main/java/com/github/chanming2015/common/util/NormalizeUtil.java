package com.github.chanming2015.common.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * @author XuMaoSen
 * Create Date:2016年3月2日 上午12:23:40
 * Description: 归一化工具
 * Version:1.0.0
 */
public class NormalizeUtil
{
    public static String normalize(CharSequence param)
    {
        if (EmptyUtil.isEmpty(param))
        {
            return "";
        }
        return Normalizer.normalize(param, Form.NFKC);
    }
}
