package com.github.chanming2015.domain.entity;

import javax.persistence.Entity;

/**
 * 用户角色关联
 */

@Entity
public class SystemUserRole extends BaseEntity {
	
	private static final long serialVersionUID = 2215954421671958340L;
	
	private Long userId;		// 用户ID
	private Long roleId;		// 角色ID
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
