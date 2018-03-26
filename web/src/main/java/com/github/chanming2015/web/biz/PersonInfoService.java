package com.github.chanming2015.web.biz;

import org.springframework.data.domain.Page;

import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.utils.sql.SpecParam;
import com.github.chanming2015.web.entity.PersonInfo;

/**
 * Description:
 * Create Date:2018年3月26日
 * @author XuMaoSen
 * Version:1.0.0
 */
public interface PersonInfoService
{
    Result<Page<PersonInfo>> getPersonInfos(SpecParam<PersonInfo> specs, int page, int size);
}
