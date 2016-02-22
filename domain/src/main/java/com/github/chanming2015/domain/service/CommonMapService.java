/*
 *www.github.com
 *Copyright (c) 2015 All Rights Reserved
 */
/**
 * Author XuMaoSen
 */
package com.github.chanming2015.domain.service;

import java.util.Map;

/**
 * Project:domain
 * Package:com.github.chanming2015.domain.service
 * FileName:CommonMapService.java
 * Comments:
 * JDK Version:
 * Author XuMaoSen
 * Create Date:2015年12月3日 下午9:54:02
 * Description:
 * Version:1.0.0
 */
public interface CommonMapService {
	
	/**
	 *	获取所有Map
	 */
	Map<String, Map<String, String>> buildTotalMap();
	
}
