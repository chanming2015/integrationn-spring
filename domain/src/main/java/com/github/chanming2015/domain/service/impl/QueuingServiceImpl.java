package com.github.chanming2015.domain.service.impl;

import org.springframework.stereotype.Service;

import com.github.chanming2015.common.util.QueuingMake;
import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.domain.service.QueuingService;

/**
 * Project:domain
 * Package:com.github.chanming2015.domain.service.impl
 * FileName:QueuingServiceImpl.java
 * Comments:
 * JDK Version:
 * @author XuMaoSen
 * Create Date:2015年12月4日 下午7:57:18
 * Description: doMain层流水号生成器实现类
 * Version:1.0.0
 */
@Service("queuingService")
public class QueuingServiceImpl implements QueuingService {

	public Result<Long> next(String src) {
		Long queuing = QueuingMake.next(src);
		return Result.newSuccess(queuing);
	}

}
