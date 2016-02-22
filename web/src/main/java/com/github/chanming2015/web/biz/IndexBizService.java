package com.github.chanming2015.web.biz;

import java.util.Map;

import com.github.chanming2015.common.util.result.Result;

/**
 * Project:web
 * Package:com.github.chanming2015.web.biz
 * FileName:IndexBizService.java
 * Comments:
 * JDK Version:
 * @author XuMaoSen
 * Create Date:2015年12月4日 下午10:31:43
 * Description:
 * Version:1.0.0
 */
public interface IndexBizService {

	Result<Long> next(String src);
	Map<String, Map<String, String>> buildTotalMap();
}
