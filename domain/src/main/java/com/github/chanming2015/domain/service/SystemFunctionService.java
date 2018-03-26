package com.github.chanming2015.domain.service;

import java.util.List;

import com.github.chanming2015.common.util.result.Pager;
import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.domain.entity.SystemFunction;
import com.github.chanming2015.domain.sql.SpecParam;

public interface SystemFunctionService {
	Result<Pager<SystemFunction>> pageSystemFunctions(SpecParam<SystemFunction> specs, Pager<SystemFunction> pager);
	Result<SystemFunction> getSystemFunction(Long id);
	Result<SystemFunction> createSystemFunction(SystemFunction systemFunction);
	Result<SystemFunction> updateSystemFunction(SystemFunction systemFunction);
	Result<Boolean> deleteSystemFunction(Long id);
	Result<List<SystemFunction>> listFunctions(SpecParam<SystemFunction> specs);
}