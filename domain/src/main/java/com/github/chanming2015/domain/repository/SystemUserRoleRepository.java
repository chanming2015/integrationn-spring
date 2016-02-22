package com.github.chanming2015.domain.repository;

import java.util.List;

import com.github.chanming2015.domain.entity.SystemUserRole;

public interface SystemUserRoleRepository extends BaseRepository<SystemUserRole> {
	List<SystemUserRole> findByUserId(Long userId);
}
