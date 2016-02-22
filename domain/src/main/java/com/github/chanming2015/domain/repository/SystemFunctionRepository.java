package com.github.chanming2015.domain.repository;

import com.github.chanming2015.domain.entity.SystemFunction;

public interface SystemFunctionRepository extends BaseRepository<SystemFunction> {
	SystemFunction findByParentId(Long parentId);
}
