package com.github.chanming2015.domain.service;

import com.github.chanming2015.common.util.result.Pager;
import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.common.util.sql.SpecParam;
import com.github.chanming2015.domain.entity.SystemUser;

public interface SystemUserService {
	Result<Pager<SystemUser>> pageSystemUsers(SpecParam<SystemUser> specs, Pager<SystemUser> pager);
	Result<SystemUser> getSystemUser(Long id);
	Result<SystemUser> getSystemUser(String loginName);
	Result<SystemUser> createSystemUser(SystemUser systemUser);
	Result<SystemUser> updateSystemUser(SystemUser systemUser);
	Result<Boolean> deleteSystemUser(Long id);
}