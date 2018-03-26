package com.github.chanming2015.web.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.utils.sql.SpecParam;
import com.github.chanming2015.utils.sql.SpecUtil;
import com.github.chanming2015.web.biz.PersonInfoService;
import com.github.chanming2015.web.entity.PersonInfo;
import com.github.chanming2015.web.repository.PersonInfoRepository;

/**
 * Description:
 * Create Date:2018年3月26日
 * @author XuMaoSen
 * Version:1.0.0
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService
{
    @Autowired
    private PersonInfoRepository personInfoRepository;

    @Override
    public Result<Page<PersonInfo>> getPersonInfos(SpecParam<PersonInfo> specs, int page, int size)
    {
        return Result.newSuccess(personInfoRepository.findAll(SpecUtil.spec(specs), new PageRequest(page, size)));
    }
}
