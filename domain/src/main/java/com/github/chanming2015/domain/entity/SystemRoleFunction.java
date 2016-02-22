package com.github.chanming2015.domain.entity;

import javax.persistence.Entity;

/**
 * 角色功能关联
 */

@Entity
public class SystemRoleFunction extends BaseEntity {
	
	private static final long serialVersionUID = 5880878512927862818L;
	
	private Long roleId;			// 角色编号
	private Long functionId;		// 功能编号
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}
}
