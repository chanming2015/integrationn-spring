package com.github.chanming2015.common.util.config;

import java.util.Properties;

/**
 * Project:common
 * Package:com.github.chanming2015.common.util.config
 * FileName:Config.java
 * Comments:
 * JDK Version:
 * @author XuMaoSen
 * Create Date:2015年12月4日 下午9:14:59
 * Description: 获取config.properties配置文件内容
 * Version:1.0.0
 */
public class Config {
	
	private static final String resource = "config.properties";
	private static final Properties props = PropertiesUtil.get(resource, "utf-8");
	
	public static String getProperty(String name, String defValue) {
		return props.getProperty(name, defValue);
	}
	
	public static String getProperty(String name) {
		return props.getProperty(name);
	}
}
