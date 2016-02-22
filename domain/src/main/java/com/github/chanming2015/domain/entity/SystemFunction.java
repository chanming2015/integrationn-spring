package com.github.chanming2015.domain.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 系统功能
 */

@Entity
public class SystemFunction extends BaseEntity {
	
	private static final long serialVersionUID = -2964909775391565832L;
	
	private Long parentId;		// 父功能ID
	private Integer level;		// 所处层级
	private Integer serial;		// 同级序号
	private String name;		// 功能名称
	private Boolean menu;		// 是否菜单
	private String actionUrl;	// 访问路径
	
	@Transient private SystemFunction parent;
	@Transient private Set<SystemFunction> children;
	

	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getSerial() {
		return serial;
	}
	public void setSerial(Integer serial) {
		this.serial = serial;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getMenu() {
		return menu;
	}
	public void setMenu(Boolean menu) {
		this.menu = menu;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public SystemFunction getParent() {
		return parent;
	}
	public void setParent(SystemFunction parent) {
		this.parent = parent;
	}
	public Set<SystemFunction> getChildren() {
		return children;
	}
	public void setChildren(Set<SystemFunction> children) {
		this.children = children;
	}
	
}
