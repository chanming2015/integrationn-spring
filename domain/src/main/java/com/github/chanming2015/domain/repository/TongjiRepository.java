/*
 *www.github.com
 *Copyright (c) 2015 All Rights Reserved
 */
/**
 * Author XuMaoSen
 */
package com.github.chanming2015.domain.repository;

import java.util.Map;

/**
 * 
 * Project:domain
 * Package:com.github.chanming2015.domain.repository
 * FileName:TongjiRepository.java
 * Comments:
 * JDK Version:
 * Author XuMaoSen
 * Create Date:2015年12月3日 下午9:52:23
 * Description: 统计接口
 * Version:1.0.0
 */
public interface TongjiRepository {

	int count(String sql);

	int count(String sql, Map<String, Object> params);

	float sum(String sql);

	float sum(String sql, Map<String, Object> params);
}
