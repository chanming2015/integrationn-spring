package com.github.chanming2015.domain.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Project:domain
 * Package:com.github.chanming2015.domain.entity
 * FileName:CommonMapEntry.java
 * Comments:
 * JDK Version:
 * Author XuMaoSen
 * Create Date:2015年12月3日 下午9:30:03
 * Description: 由字典键找到值
 * Version:1.0.0
 */
@Entity
public class CommonMapEntry extends BaseEntity {

	private static final long serialVersionUID = -5225046270513827955L;

	/**
	 * 字典-键
	 */
	private String key;	
	
	/**
	 * 字典-值
	 */
	private String value;		
	
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, optional = false)
    private CommonMap map;

	/**
	 * Author XuMaoSen
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Author XuMaoSen
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Author XuMaoSen
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Author XuMaoSen
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Author XuMaoSen
	 * @return the map
	 */
	public CommonMap getMap() {
		return map;
	}

	/**
	 * Author XuMaoSen
	 * @param map the map to set
	 */
	public void setMap(CommonMap map) {
		this.map = map;
	}
	
}
