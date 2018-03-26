package com.github.chanming2015.domain.service.impl.nolog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.chanming2015.domain.entity.SystemFunction;
import com.github.chanming2015.domain.entity.SystemRole;
import com.github.chanming2015.domain.entity.SystemRoleFunction;
import com.github.chanming2015.domain.entity.SystemUser;
import com.github.chanming2015.domain.entity.SystemUserRole;
import com.github.chanming2015.domain.repository.SystemFunctionRepository;
import com.github.chanming2015.domain.repository.SystemRoleFunctionRepository;
import com.github.chanming2015.domain.repository.SystemRoleRepository;
import com.github.chanming2015.domain.repository.SystemUserRepository;
import com.github.chanming2015.domain.repository.SystemUserRoleRepository;
import com.github.chanming2015.domain.service.AuthorityService;
import com.github.chanming2015.domain.sql.SpecParam;
import com.github.chanming2015.domain.sql.SpecUtil;

@Service("authorityService")
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired private SystemUserRepository systemUserRepository;
	@Autowired private SystemUserRoleRepository systemUserRoleRepository;
	@Autowired private SystemRoleRepository systemRoleRepository;
	@Autowired private SystemRoleFunctionRepository systemRoleFunctionRepository;
	@Autowired private SystemFunctionRepository systemFunctionRepository;
	
	@Override
	public Set<String> getRoles(String userName) {
		Set<String> roles = new HashSet<>();
		SystemUser user = systemUserRepository.findByLoginName(userName);
		if(user != null){
			List<SystemUserRole> relations = systemUserRoleRepository.findByUserId(user.getId());
			List<Long> roleIds = new ArrayList<Long>();
			if(relations != null){
				for(SystemUserRole relation : relations){
					roleIds.add(relation.getRoleId());
				}
				List<SystemRole> systemRoles = systemRoleRepository.findAll(roleIds);
				if(systemRoles != null){
					for(SystemRole systemRole : systemRoles){
						roles.add(systemRole.getName());
					}
				}
			}
		}
		return roles;
	}

	@Override
	public Set<String> getPermissions(String userName) {
		Set<String> permissions = new HashSet<>();
		SystemUser user = systemUserRepository.findByLoginName(userName);
		if(user != null){
			List<SystemUserRole> relations = systemUserRoleRepository.findByUserId(user.getId());
			List<Long> roleIds = new ArrayList<Long>();
			List<Long> functionIds = new ArrayList<Long>();
			if(relations != null){
				for(SystemUserRole relation : relations){
					roleIds.add(relation.getRoleId());
				}
				if(roleIds.size() > 0){
					SpecParam<SystemRoleFunction> params = new SpecParam<SystemRoleFunction>();
					params.in("roleId", roleIds);
					List<SystemRoleFunction> systemRoleFunctions = systemRoleFunctionRepository.findAll(SpecUtil.spec(params));
					if(systemRoleFunctions != null){
						for(SystemRoleFunction systemRoleFunction : systemRoleFunctions){
							functionIds.add(systemRoleFunction.getFunctionId());
						}
						List<SystemFunction> functions = systemFunctionRepository.findAll(functionIds);
						if(functions != null){
							for(SystemFunction function : functions){
								permissions.add(function.getName());
							}
						}
					}
				}
			}
		}
		return permissions;
	}

}
