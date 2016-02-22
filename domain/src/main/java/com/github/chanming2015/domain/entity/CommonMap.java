package com.github.chanming2015.domain.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 * Project:domain
 * Package:com.github.chanming2015.domain.entity
 * FileName:CommonMap.java
 * Comments:
 * JDK Version:
 * Author XuMaoSen
 * Create Date:2015年12月3日 下午9:27:23
 * Description: 字典目录（由字典名称找到相应字典）
 * Version:1.0.0
 */
@Entity
public class CommonMap extends BaseEntity {

	private static final long serialVersionUID = 6076982546887862952L;
	
	/**
	 * 字典名称
	 */
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "map")
    private Set<CommonMapEntry> entries = new HashSet<CommonMapEntry>();

	/**
	 * Author XuMaoSen
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Author XuMaoSen
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Author XuMaoSen
	 * @return the entries
	 */
	public Set<CommonMapEntry> getEntries() {
		return entries;
	}

	/**
	 * Author XuMaoSen
	 * @param entries the entries to set
	 */
	public void setEntries(Set<CommonMapEntry> entries) {
		this.entries = entries;
	}

}
