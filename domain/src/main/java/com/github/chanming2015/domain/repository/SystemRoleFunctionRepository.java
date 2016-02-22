package com.github.chanming2015.domain.repository;

import java.util.List;

import com.github.chanming2015.domain.entity.SystemRoleFunction;

public interface SystemRoleFunctionRepository extends BaseRepository<SystemRoleFunction> {
	List<SystemRoleFunction> findByRoleId(Long roleId);
}
