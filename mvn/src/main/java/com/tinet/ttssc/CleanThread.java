package com.tinet.ttssc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.TimerTask;

import com.tinet.ttssc.db.DbConnect;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.service.SystemSettingService;
import com.tinet.ttssc.util.DateUtil;


public class CleanThread extends TimerTask  implements Runnable{
	
	public void run(){
		Connection conn = DbConnect.getConnection(Const.DB_POOL_NAME);
		Statement stmt = null;
		if(conn!=null){
			try{
				Integer  day= Integer.parseInt(SystemSettingService.getSystemSetting(Const.MONITOR_CONF).getProperty());
				String startTime = DateUtil.format(DateUtil.addDay(new Date(), -day), DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
				String sql = "delete from tts_monitor where create_time<'" + startTime + "'";
				stmt = conn.createStatement();
				stmt.execute(sql);
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
