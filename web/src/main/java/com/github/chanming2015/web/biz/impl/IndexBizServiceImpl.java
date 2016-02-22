/** @author XuMaoSen
 */
package com.github.chanming2015.web.biz.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.domain.service.CommonMapService;
import com.github.chanming2015.domain.service.QueuingService;
import com.github.chanming2015.web.biz.IndexBizService;

/**
 * Project:web
 * Package:com.github.chanming2015.web.biz.impl
 * FileName:IndexBizServiceImpl.java
 * Comments:
 * JDK Version:
 * @author XuMaoSen
 * Create Date:2015年12月4日 下午10:36:20
 * Description:
 * Version:1.0.0
 */
@Service("indexBizService")
public class IndexBizServiceImpl implements IndexBizService {
	
	@Autowired private CommonMapService commonMapService;
	@Autowired private QueuingService queuingService;

	/** @author XuMaoSen
	 */
	@Override
	public Result<Long> next(String src) {

		return queuingService.next(src);
	}

	/** @author XuMaoSen
	 */
	@Override
	public Map<String, Map<String, String>> buildTotalMap() {

		return commonMapService.buildTotalMap();
	}

}
