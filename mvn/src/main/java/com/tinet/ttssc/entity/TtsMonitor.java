package com.tinet.ttssc.entity;

import java.util.Date;
import java.util.List;

import com.tinet.ttssc.TtssEngine;

import net.sf.json.JSONObject;



public class TtsMonitor {
	private Integer totalEngineCount;
	private Integer usedEngineCount;

	private List<JSONObject> engineInfo;
	private Date createTime;
	
	public TtsMonitor(){
		this.createTime = new Date();
	}
	
	public Integer getTotalEngineCount() {
		return totalEngineCount;
	}

	public void setTotalEngineCount(Integer totalEngineCount) {
		this.totalEngineCount = totalEngineCount;
	}

	public Integer getUsedEngineCount() {
		return usedEngineCount;
	}

	public void setUsedEngineCount(Integer usedEngineCount) {
		this.usedEngineCount = usedEngineCount;
	}

	public Integer getWaitCount() {
		return TtssEngine.getWaitCount();
	}


	public Integer getDealCount() {
		return TtssEngine.getDealCount();
	}


	public Integer getFailCount() {
		return TtssEngine.getFailCount();
	}

	public List<JSONObject> getEngineInfo() {
		return engineInfo;
	}

	public void setEngineInfo(List<JSONObject> engineInfo) {
		this.engineInfo = engineInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
}
