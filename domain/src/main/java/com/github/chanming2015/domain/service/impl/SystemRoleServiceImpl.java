package com.github.chanming2015.domain.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chanming2015.common.util.result.ErrorMessage;
import com.github.chanming2015.common.util.result.PageObject;
import com.github.chanming2015.common.util.result.Pager;
import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.common.util.sql.SpecParam;
import com.github.chanming2015.domain.entity.SystemRole;
import com.github.chanming2015.domain.entity.SystemRoleFunction;
import com.github.chanming2015.domain.entity.SystemUserRole;
import com.github.chanming2015.domain.repository.SystemFunctionRepository;
import com.github.chanming2015.domain.repository.SystemRoleFunctionRepository;
import com.github.chanming2015.domain.repository.SystemRoleRepository;
import com.github.chanming2015.domain.repository.SystemUserRoleRepository;
import com.github.chanming2015.domain.service.QueuingService;
import com.github.chanming2015.domain.service.SystemRoleService;
import com.github.chanming2015.domain.util.SpecUtil;

@Service("systemRoleService")
public class SystemRoleServiceImpl implements SystemRoleService {
	
	private static final String QUEUING_MAKE_SRC = "01";
	
	@Autowired private SystemRoleRepository systemRoleRepository;
	@Autowired private SystemFunctionRepository systemFunctionRepository;
	@Autowired private SystemRoleFunctionRepository systemRoleFunctionRepository;
	@Autowired private SystemUserRoleRepository systemUserRoleRepository;
	@Autowired private QueuingService queuingService;
	
	private ErrorMessage errorResult = new ErrorMessage("c2-errorcode.properties");


	@Override
	public Result<Pager<SystemRole>> pageSystemRoles(SpecParam<SystemRole> specs, Pager<SystemRole> pager) {
		Result<Pager<SystemRole>> result;
		// 参数校验
		if(specs == null || pager == null){
			result = errorResult.newFailure("CMS1291");
			return result;
		}
		if(pager.getPageSize() <= 0){
			result = errorResult.newFailure("CMS1292");
			return result;
		}
		// 准备分页参数
		Pageable pageable = pager.getDirection() == null || pager.getProperties() == null ? 
								new PageRequest(pager.getCurrentPage() - 1, pager.getPageSize()) :
								new PageRequest(pager.getCurrentPage() - 1, pager.getPageSize(), 
												Direction.valueOf(pager.getDirection()), 
												pager.getProperties().split(","));
		// 准备查询参数
		specs.eq("deleted", false);		// 未删除
		// 执行查询
		Page<SystemRole> resultSet = null;
		try{			
			resultSet = systemRoleRepository.findAll(SpecUtil.spec(specs), pageable);
		}catch(Exception e){
			result = errorResult.newFailure("CMS0151");
			return result;
		}
		
		// 返回结果
		PageObject<SystemRole> pageObject = new PageObject<SystemRole> ((int)resultSet.getTotalElements(), resultSet.getContent());
		pager.setElements(pageObject.getList());
		pager.init(pageObject.getTotal());
		result = Result.newSuccess(pager);
		return result;
	}

	@Override
	public Result<SystemRole> getSystemRole(Long id) {
		Result<SystemRole> result;
		// 参数校验
		if(id == null){
			result = errorResult.newFailure("CMS1041");
			return result;
		}
		
		// 执行查询
		SystemRole systemRole = null;
		try{
			systemRole = systemRoleRepository.findOne(id);
			List<SystemRoleFunction> relations = systemRoleFunctionRepository.findByRoleId(id);
			if(relations != null){
				Set<Long> functionIds = new HashSet<Long>();
				for(SystemRoleFunction relation : relations){
					functionIds.add(relation.getFunctionId());
				}
				systemRole.setFunctionIds(functionIds);
			}
		}catch(Exception e){
			result = errorResult.newFailure("CMS0021");
			return result;
		}
		
		// 返回结果
		result = Result.newSuccess(systemRole);
		return result;
	}

	@Override
	public Result<SystemRole> createSystemRole(SystemRole systemRole) {
		Result<SystemRole> result;
		// 校验参数
		if(systemRole == null){
			result = errorResult.newFailure("CMS1311");
			return result;
		}
		// 准备序列号
		Result<Long> systemRoleQueuing = queuingService.next(QUEUING_MAKE_SRC);
	    if(systemRoleQueuing.getError() != null ){
	    	return errorResult.newFailure("CMS0032");
	    }
		// 持久对象
	    SystemRole systemRoleSaved = null;
		try{
			systemRole.setId(systemRoleQueuing.getObject());
			systemRoleSaved = systemRoleRepository.save(systemRole);
		}catch(Exception e){
			result = errorResult.newFailure("CMS0171");
			return result;
		}
		// 返回结果
		result = Result.newSuccess(systemRoleSaved);
		return result;
	}

	@Override
	public Result<SystemRole> updateSystemRole(SystemRole systemRole) {
		Result<SystemRole> result;
		// 校验参数
		if(systemRole == null){
			result = errorResult.newFailure("CMS1081");
			return result;
		}
		if(systemRole.getId() == null){
			result = errorResult.newFailure("CMS1082");
			return result;
		}
		SystemRole systemRoleFetched;
		try{
			// 准备对象
			systemRoleFetched = systemRoleRepository.findOne(systemRole.getId());
			systemRoleFetched.setName(systemRole.getName() == null ? systemRoleFetched.getName() : systemRole.getName());
			systemRoleFetched.setDescription(systemRole.getDescription() == null ? systemRoleFetched.getDescription() : systemRole.getDescription());
			systemRoleFetched.setStatus(systemRole.getStatus() == null ? systemRoleFetched.getStatus() : systemRole.getStatus());
			// 持久对象
			systemRoleRepository.save(systemRoleFetched);
			
			// 如果是禁用角色 撤销用户已经扮演的该类角色
			if("NO".equals(systemRole.getStatus())){
				SpecParam<SystemUserRole> specs = new SpecParam<SystemUserRole>();
				specs.eq("roleId", systemRole.getId());
				List<SystemUserRole> relations = systemUserRoleRepository.findAll(SpecUtil.spec(specs));
				if(relations != null){
					for(SystemUserRole relation : relations){
						systemUserRoleRepository.delete(relation);
					}
				}
			}
		}catch(Exception e){
			result = errorResult.newFailure("CMS0041");
			return result;
		}
		// 返回结果
		result = Result.newSuccess(systemRoleFetched);
		return result;
	}

	@Override
	public Result<Boolean> deleteSystemRole(Long id) {
		Result<Boolean> result;
		// 参数校验
		if(id == null){
			result = errorResult.newFailure("CMS1331");
			return result;
		}
		// 准备对象
		SystemRole systemRole = systemRoleRepository.findOne(id);
		if(systemRole != null){
			systemRole.setDeleted(true);
		}
		// 持久化操作
		try{
			systemRoleRepository.save(systemRole);
		}catch(Exception e){
			result = errorResult.newFailure("CMS0191");
			return result;
		}	
		// 返回结果
		result = Result.newSuccess(true);
		return result;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public Result<Boolean> assign(Long id, Long[] functions) {
		Result<Boolean> result;
		// 参数校验
		if(id == null){
			result = errorResult.newFailure("参数缺失", "分配权限时缺少必要的参数。");
			return result;
		}
		if(functions == null){
			result = errorResult.newFailure("参数缺失", "分配权限时缺少必要的参数或者参数不完整。");
			return result;
		}
		// 删除旧记录
		List<SystemRoleFunction>  toDeleteList = systemRoleFunctionRepository.findByRoleId(id);
		if(toDeleteList != null){
			for(SystemRoleFunction relation : toDeleteList){
				systemRoleFunctionRepository.delete(relation);
			}
		}
		// 准备并保存新纪录
		List<SystemRoleFunction> toSaveList = new ArrayList<SystemRoleFunction>();
		for(Long fid : functions){
			SystemRoleFunction relation = new SystemRoleFunction();
			relation.setId(queuingService.next(QUEUING_MAKE_SRC).getObject());
			relation.setRoleId(id);
			relation.setFunctionId(fid);
			toSaveList.add(relation);
		}
		systemRoleFunctionRepository.save(toSaveList);
		// 返回结果
		result = Result.newSuccess(true);
		return result;
	}

	@Override
	public Result<List<SystemRole>> getSystemRoles(SpecParam<SystemRole> specs) {
		Result<List<SystemRole>> result;
		// 参数校验
		if(specs == null){
			result = errorResult.newFailure("CMS1331");
			return result;
		}
		specs.eq("deleted", false);
		List<SystemRole> roles = systemRoleRepository.findAll(SpecUtil.spec(specs));
		// 返回结果
		result = Result.newSuccess(roles);
		return result;
	}
}
