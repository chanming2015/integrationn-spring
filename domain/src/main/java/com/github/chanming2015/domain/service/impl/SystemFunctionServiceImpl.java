package com.github.chanming2015.domain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.github.chanming2015.common.util.result.PageObject;
import com.github.chanming2015.common.util.result.Pager;
import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.common.util.sql.SpecParam;
import com.github.chanming2015.domain.entity.SystemFunction;
import com.github.chanming2015.domain.repository.SystemFunctionRepository;
import com.github.chanming2015.domain.service.QueuingService;
import com.github.chanming2015.domain.service.SystemFunctionService;
import com.github.chanming2015.domain.util.SpecUtil;

@Service("systemFunctionService")
public class SystemFunctionServiceImpl implements SystemFunctionService
{

    private static final String QUEUING_MAKE_SRC = "01";

    @Autowired
    private SystemFunctionRepository systemFunctionRepository;
    @Autowired
    private QueuingService queuingService;

    @Override
    public Result<Pager<SystemFunction>> pageSystemFunctions(SpecParam<SystemFunction> specs,
            Pager<SystemFunction> pager)
    {
        Result<Pager<SystemFunction>> result;
        // 参数校验
        if (specs == null || pager == null)
        {
            result = Result.newFailure("CMS1291", "");
            return result;
        }
        if (pager.getPageSize() <= 0)
        {
            result = Result.newFailure("CMS1292", "");
            return result;
        }
        // 准备分页参数
        Pageable pageable = pager.getDirection() == null || pager.getProperties() == null ? new PageRequest(
                pager.getCurrentPage() - 1, pager.getPageSize()) : new PageRequest(
                pager.getCurrentPage() - 1, pager.getPageSize(), Direction.valueOf(pager
                        .getDirection()), pager.getProperties().split(","));
        // 准备查询参数
        specs.eq("deleted", false); // 未删除
        // 执行查询
        Page<SystemFunction> resultSet = null;
        try
        {
            resultSet = systemFunctionRepository.findAll(SpecUtil.spec(specs), pageable);
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0151", "");
            return result;
        }

        // 返回结果
        PageObject<SystemFunction> pageObject = new PageObject<SystemFunction>(
                (int) resultSet.getTotalElements(), resultSet.getContent());
        pager.setElements(pageObject.getList());
        pager.init(pageObject.getTotal());
        result = Result.newSuccess(pager);
        return result;
    }

    @Override
    public Result<SystemFunction> getSystemFunction(Long id)
    {
        Result<SystemFunction> result;
        // 参数校验
        if (id == null)
        {
            result = Result.newFailure("CMS1041", "");
            return result;
        }

        // 执行查询
        SystemFunction systemFunction = null;
        try
        {
            systemFunction = systemFunctionRepository.findOne(id);
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0021", "");
            return result;
        }

        // 返回结果
        result = Result.newSuccess(systemFunction);
        return result;
    }

    @Override
    public Result<SystemFunction> createSystemFunction(SystemFunction systemFunction)
    {
        Result<SystemFunction> result;
        // 校验参数
        if (systemFunction == null)
        {
            result = Result.newFailure("CMS1311", "");
            return result;
        }
        // 准备序列号
        Result<Long> systemFunctionQueuing = queuingService.next(QUEUING_MAKE_SRC);
        if (systemFunctionQueuing.getError() != null)
        {
            return Result.newFailure("CMS0032", "");
        }
        // 持久对象
        SystemFunction systemFunctionSaved = null;
        try
        {
            systemFunction.setId(systemFunctionQueuing.getObject());
            systemFunctionSaved = systemFunctionRepository.save(systemFunction);
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0171", "");
            return result;
        }
        // 返回结果
        result = Result.newSuccess(systemFunctionSaved);
        return result;
    }

    @Override
    public Result<SystemFunction> updateSystemFunction(SystemFunction systemFunction)
    {
        Result<SystemFunction> result;
        // 校验参数
        if (systemFunction == null)
        {
            result = Result.newFailure("CMS1081", "");
            return result;
        }
        if (systemFunction.getId() == null)
        {
            result = Result.newFailure("CMS1082", "");
            return result;
        }
        SystemFunction systemFunctionFetched;
        try
        {
            // 准备对象
            systemFunctionFetched = systemFunctionRepository.findOne(systemFunction.getId());
            systemFunctionFetched
                    .setParentId(systemFunction.getParentId() == null ? systemFunctionFetched
                            .getParentId() : systemFunction.getParentId());
            systemFunctionFetched
                    .setLevel(systemFunction.getLevel() == null ? systemFunctionFetched.getLevel()
                            : systemFunction.getLevel());
            systemFunctionFetched
                    .setSerial(systemFunction.getSerial() == null ? systemFunctionFetched
                            .getSerial() : systemFunction.getSerial());
            systemFunctionFetched.setName(systemFunction.getName() == null ? systemFunctionFetched
                    .getName() : systemFunction.getName());
            systemFunctionFetched.setMenu(systemFunction.getMenu() == null ? systemFunctionFetched
                    .getMenu() : systemFunction.getMenu());
            systemFunctionFetched
                    .setActionUrl(systemFunction.getActionUrl() == null ? systemFunctionFetched
                            .getActionUrl() : systemFunction.getActionUrl());
            // 持久对象
            systemFunctionRepository.save(systemFunctionFetched);
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0041", "");
            return result;
        }
        // 返回结果
        result = Result.newSuccess(systemFunctionFetched);
        return result;
    }

    @Override
    public Result<Boolean> deleteSystemFunction(Long id)
    {
        Result<Boolean> result;
        // 参数校验
        if (id == null)
        {
            result = Result.newFailure("CMS1331", "");
            return result;
        }
        // 准备对象
        SystemFunction systemFunction = systemFunctionRepository.findOne(id);
        if (systemFunction != null)
        {
            systemFunction.setDeleted(true);
        }
        // 持久化操作
        try
        {
            systemFunctionRepository.save(systemFunction);
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0191", "");
            return result;
        }
        // 返回结果
        result = Result.newSuccess(true);
        return result;
    }

    /** @author XuMaoSen 
     */
    @Override
    public Result<List<SystemFunction>> listFunctions(SpecParam<SystemFunction> specs)
    {

        Result<List<SystemFunction>> result;
        // 准备条件
        specs.eq("deleted", false);
        List<SystemFunction> functions;
        try
        {
            functions = systemFunctionRepository.findAll(SpecUtil.spec(specs));
        }
        catch (Exception e)
        {
            result = Result.newFailure("100", "在获取功能列表时发生数据操作错误。");
            return result;
        }
        // 返回结果
        result = Result.newSuccess(functions);
        return result;
    }
}
