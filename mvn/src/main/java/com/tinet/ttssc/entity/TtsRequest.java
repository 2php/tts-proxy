package com.tinet.ttssc.entity;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tinet.ttssc.db.DbConnect;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.util.DateUtil;


public class TtsRequest implements Serializable, Comparable<TtsRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6264613745798370214L;
	private String text;
	private String hash;
	private String enterpriseId;
	private Integer priority;
	private String uuid;
	private String uniqueId;
	private Date requestTime;
	private Date startTime;
	private Date endTime;
	private Integer position;
	private Integer hitCache;
	private Integer result;
	private TtsServer ttsServer;
	private Integer threadId;
	private String fromIp;
	private Thread notifyThread;
	private List<TtsServer> validServer = new ArrayList<TtsServer>();
	private Integer retry;
	private Integer speed;
	private Integer vid;
	private Integer volume;
	private Integer redirect;
	private Integer sync;
	private boolean done;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public TtsServer getTtsServer() {
		return ttsServer;
	}
	public void setTtsServer(TtsServer ttsServer) {
		this.ttsServer = ttsServer;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public Integer getHitCache() {
		return hitCache;
	}
	public void setHitCache(Integer hitCache) {
		this.hitCache = hitCache;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	
	public Thread getNotifyThread() {
		return notifyThread;
	}
	public void setNotifyThread(Thread notifyThread) {
		this.notifyThread = notifyThread;
	}
	
	public Integer getThreadId() {
		return threadId;
	}
	public void setThreadId(Integer threadId) {
		this.threadId = threadId;
	}
	
	public String getFromIp() {
		return fromIp;
	}
	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}
	public Integer getRetry() {
		return retry;
	}
	public void setRetry(Integer retry) {
		this.retry = retry;
	}
	
	
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public Integer getVid() {
		return vid;
	}
	public void setVid(Integer vid) {
		this.vid = vid;
	}
	
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
	public Integer getRedirect() {
		return redirect;
	}
	public void setRedirect(Integer redirect) {
		this.redirect = redirect;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	
	public Integer getSync() {
		return sync;
	}
	public void setSync(Integer sync) {
		this.sync = sync;
	}
	public boolean isValid(TtsServer ttsServer){
		if(validServer.contains(ttsServer)){
			boolean vidValid = false;
			if(vid==1){//普通话
				if (ttsServer.getVid().equals(1) || ttsServer.getVid().equals(3)){
					vidValid = true;
				}else{
					vidValid = false;
				}
			}else if(vid==3){//粤语
				if (ttsServer.getVid().equals(2) || ttsServer.getVid().equals(3)){
					vidValid = true;
				}else{
					vidValid = false;
				}
			}
			if(vidValid){
				if(ttsServer.getType() == 1){
					return true;
				}else{
					for(TtsServer server :validServer){
						if(server.getType() == 1){
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	public void removeValid(TtsServer ttsServer){
		validServer.remove(ttsServer);
	}
	public boolean hasValid(){
		return validServer.size()==0? false : true;
	}
	public void SetValidServer(List<TtsServer> ttsServerList){
		for(TtsServer server:ttsServerList){
			if(server.getActive() == 1){
				validServer.add(server);
			}
		}
	}

	/**
	 * CURL 优先级判断
	 * 返回-1表示在队列中的优先级高
	 */
	public int compareTo(TtsRequest o) {
		if (priority < o.priority) { //首先判断优先级, priority越小优先级越高
			return -1;
		} else if(priority > o.priority) {
			return 1;
		}
		//优先级一样根据requestTime判断
		return requestTime.compareTo(o.requestTime);
	}
	public static void saveTtsLog(TtsRequest request){
		Connection conn = DbConnect.getConnection(Const.DB_POOL_NAME);
		PreparedStatement stmt = null;
		if(conn!=null){
			try{
				String sql = "insert into tts_log(enterprise_id, priority, unique_id, uuid, text, hash, " +
						"request_time, start_time, end_time, position, hit_cache, result, tts_server_ip, tts_server_type, thread_id, from_ip,speed,vid,volume,redirect,sync)" +
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, request.getEnterpriseId());
				stmt.setInt(2, request.getPosition());
				stmt.setString(3, request.getUniqueId());
				stmt.setString(4, request.getUuid());
				stmt.setString(5, request.getText());
				stmt.setString(6, request.getHash());
				stmt.setTimestamp(7, Timestamp.valueOf(DateUtil.format(request.getRequestTime(), DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss)));
				stmt.setTimestamp(8, Timestamp.valueOf(DateUtil.format(request.getStartTime(), DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss)));
				stmt.setTimestamp(9, Timestamp.valueOf(DateUtil.format(request.getEndTime(), DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss)));
				stmt.setInt(10, request.getPosition());
				stmt.setInt(11, request.getHitCache());
				stmt.setInt(12, request.getResult());
				if(request.getTtsServer() != null){
					stmt.setString(13, request.getTtsServer().getIp());
					stmt.setInt(14, request.getTtsServer().getType());
				}else{
					stmt.setString(13, "");
					stmt.setInt(14, -1);
				}
				if(request.getThreadId() != null){
					stmt.setInt(15, request.getThreadId());
				}else{
					stmt.setInt(15, -1);
				}
				stmt.setString(16, request.getFromIp());
				stmt.setInt(17, request.getSpeed());
				stmt.setInt(18, request.getVid());
				stmt.setInt(19, request.getVolume());
				stmt.setInt(20, request.getRedirect());
				stmt.setInt(21, request.getSync());
				stmt.execute();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
				DbConnect.close(Const.DB_POOL_NAME, conn);
			}
		}
	}
}
