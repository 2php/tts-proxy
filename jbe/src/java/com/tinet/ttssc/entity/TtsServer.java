package com.tinet.ttssc.entity;

import java.util.Date;

public class TtsServer implements java.io.Serializable{
	private Integer id;
	private String ip;
	private Integer type; //1 本地机房 2 远程机房
	private Integer license;
	private Integer active;
	private Integer vid;
	private Date createTime;
	private Integer success;
	private Integer fail;
	public TtsServer(){
		createTime = new Date();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLicense() {
		return license;
	}
	public void setLicense(Integer license) {
		this.license = license;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getVid() {
		return vid;
	}

	public void setVid(Integer vid) {
		this.vid = vid;
	}
	
}
