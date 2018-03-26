package com.github.chanming2015.domain.service;

import java.util.List;

import com.github.chanming2015.common.util.result.Pager;
import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.domain.entity.SystemRole;
import com.github.chanming2015.domain.sql.SpecParam;

public interface SystemRoleService {
	Result<Pager<SystemRole>> pageSystemRoles(SpecParam<SystemRole> specs, Pager<SystemRole> pager);
	Result<SystemRole> getSystemRole(Long id);
	Result<SystemRole> createSystemRole(SystemRole systemRole);
	Result<SystemRole> updateSystemRole(SystemRole systemRole);
	Result<Boolean> deleteSystemRole(Long id);
	Result<Boolean> assign(Long id, Long[] functions);
	Result<List<SystemRole>> getSystemRoles(SpecParam<SystemRole> specs);
}