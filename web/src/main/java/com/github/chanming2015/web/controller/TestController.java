package com.github.chanming2015.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.utils.sql.SpecParam;
import com.github.chanming2015.web.biz.PersonInfoService;
import com.github.chanming2015.web.entity.PersonInfo;

/**
 * Description:
 * Create Date:2018年3月26日
 * @author XuMaoSen
 * Version:1.0.0
 */
@RestController
public class TestController
{
    @Autowired
    private PersonInfoService personInfoService;

    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<String> test()
    {
        Result<Page<PersonInfo>> result = personInfoService.getPersonInfos(new SpecParam<PersonInfo>(), 0, 100);
        ResponseEntity<String> resp = new ResponseEntity<>(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue), HttpStatus.OK);
        return resp;
    }
}
