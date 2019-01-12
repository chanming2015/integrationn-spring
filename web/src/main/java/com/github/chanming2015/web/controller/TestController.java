package com.github.chanming2015.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.chanming2015.web.biz.PersonInfoService;

/**
 * Description:
 * Create Date:2018年3月26日
 * @author XuMaoSen
 * Version:1.0.0
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestController
{
    @Autowired
    private PersonInfoService personInfoService;

    @GetMapping(value = "/home")
    ResponseEntity<String> test()
    {
        //        Result<Page<PersonInfo>> result = personInfoService.getPersonInfos(new SpecParam<PersonInfo>(), 0, 100);
        ResponseEntity<String> resp = new ResponseEntity<>(new JSONObject().toJSONString(), HttpStatus.OK);
        return resp;
    }
}
