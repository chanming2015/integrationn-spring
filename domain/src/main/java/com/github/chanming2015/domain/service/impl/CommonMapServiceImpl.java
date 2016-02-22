package com.github.chanming2015.domain.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.chanming2015.domain.entity.CommonMap;
import com.github.chanming2015.domain.entity.CommonMapEntry;
import com.github.chanming2015.domain.repository.CommonMapRepository;
import com.github.chanming2015.domain.service.CommonMapService;

/**
 * Project:domain
 * Package:com.github.chanming2015.domain.service.impl
 * FileName:CommonMapServiceImpl.java
 * Comments:
 * JDK Version:
 * @author XuMaoSen
 * Create Date:2015年12月4日 下午7:58:36
 * Description: doMain层通用字典生成实现类
 * Version:1.0.0
 */
@Service("commonMapService")
public class CommonMapServiceImpl implements CommonMapService {

	@Autowired
	CommonMapRepository commonMapRepository;

	/**
	 * @author XuMaoSen
	 */
	@Override
	public Map<String, Map<String, String>> buildTotalMap() {
		Map<String, Map<String, String>> totalMap = new HashMap<String, Map<String, String>>();
		
		List<CommonMap> mapsResult = commonMapRepository.findAll();

		for (CommonMap commonMap : mapsResult) {
			Map<String, String> map = new HashMap<String, String>();
			for (CommonMapEntry entry : commonMap.getEntries()) {
				map.put(entry.getKey(), entry.getValue());
			}
			totalMap.put(commonMap.getName(), map);
		}
		return totalMap;
	}

}
