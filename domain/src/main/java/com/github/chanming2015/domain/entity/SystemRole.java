package com.github.chanming2015.domain.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 系统角色
 */

@Entity
public class SystemRole extends BaseEntity {
	
	private static final long serialVersionUID = -8303384761473305312L;
	
	private String name;			// 名称
	private String description;		// 描述
	private String status;			// 状态
	
	@Transient private Set<Long> functionIds;                              	// 功能ID集合
	@Transient private Set<SystemFunction> functions;                       // 功能集合

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Long> getFunctionIds() {
		return functionIds;
	}

	public void setFunctionIds(Set<Long> functionIds) {
		this.functionIds = functionIds;
	}

}
