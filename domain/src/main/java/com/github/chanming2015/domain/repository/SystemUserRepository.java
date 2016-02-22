package com.github.chanming2015.domain.repository;

import com.github.chanming2015.domain.entity.SystemUser;

public interface SystemUserRepository extends BaseRepository<SystemUser> {
	SystemUser findByLoginName(String loginName);
	SystemUser findByLoginNameAndDeleted(String loginName, boolean deleted);
}
