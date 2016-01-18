package com.tinet.ttssc.entity;

// default package

import java.sql.Timestamp;
import java.util.Date;

/**
* 系统设置表
*<p>
* 文件名： MusicOnHold.java
*<p>
* Copyright (c) 2006-2010 T&I Net Communication CO.,LTD.  All rights reserved.
* @author MyEclipse Persistence Tools
* @since 1.0
* @version 1.0
*/

public class SystemSetting implements java.io.Serializable {

	// Fields
	private Integer id;
	private String name;
	private String value;
	private String property;
	private Date createTime;

	// Constructors

	/** default constructor */
	public SystemSetting() {
		this.setCreateTime(new Date());
	}

	/** full constructor */
	public SystemSetting(String name, String value, String property,
			Timestamp createTime) {
		this.name = name;
		this.value = value;
		this.property = property;
		this.createTime = createTime;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}