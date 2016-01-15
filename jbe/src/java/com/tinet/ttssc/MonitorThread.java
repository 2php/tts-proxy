package com.tinet.ttssc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.postgresql.util.PGobject;

import com.tinet.ttssc.db.DbConnect;
import com.tinet.ttssc.entity.TtsMonitor;
import com.tinet.ttssc.inc.Const;


public class MonitorThread extends TimerTask  implements Runnable{
	private static Logger logger = Logger.getLogger(MonitorThread.class);
	
	
	public void run(){
		TtsMonitor monitor = Monitor.getMonitor();
		Connection conn = DbConnect.getConnection(Const.DB_POOL_NAME);
		PreparedStatement stmt = null;
		if(conn!=null){
			try{

				String sql = "insert into tts_monitor(total_engine_count, used_engine_count, wait_count, deal_count, fail_count, engine_info)" +
					"values(?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, monitor.getTotalEngineCount());
				stmt.setInt(2, monitor.getUsedEngineCount());
				stmt.setInt(3, monitor.getWaitCount());
				stmt.setInt(4, monitor.getDealCount());
				stmt.setInt(5, monitor.getFailCount());
				PGobject jsonObject = new PGobject();
				jsonObject.setType("json");
				jsonObject.setValue(monitor.getEngineInfo().toString());
				stmt.setObject(6, jsonObject);
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
