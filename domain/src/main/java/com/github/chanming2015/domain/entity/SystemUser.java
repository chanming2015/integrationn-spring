package com.github.chanming2015.domain.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 系统用户
 */

@Entity
public class SystemUser extends BaseEntity {
	
	private static final long serialVersionUID = -1819900863679785803L;
	
	private String realName;			// 真实名
	private String loginName;           // 登录名
	private String password;			// 密码
	private String salt;				// 盐
	private String mobile;				// 手机号
	private String memo;				// 备注
	private String status;				// 状态
	
	@Transient private Set<Long> roleIds;									// 角色ID集合
	@Transient private Set<SystemRole> roles;                               // 角色集合
	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Set<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public Set<SystemRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<SystemRole> roles) {
		this.roles = roles;
	}
	
}
