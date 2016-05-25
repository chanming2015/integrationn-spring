package com.github.chanming2015.common.util;

import java.util.regex.Pattern;

/**
 * Description: 
 * Create Date:2016年5月25日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class VerifyUtil
{

    public static final Pattern IPV4 = Pattern.compile("^\\d+\\.\\d+\\.\\d+\\.\\d+$");

    public static final Pattern NUMBER = Pattern.compile("^\\d*$");

}
