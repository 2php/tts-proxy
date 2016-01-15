package com.tinet.ttssc.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tinet.ttssc.db.DbConnect;
import com.tinet.ttssc.entity.SystemSetting;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.inc.Macro;

public class SystemSettingService {
	public static void init(){
		Macro.systemSettings.clear();
		Connection conn = DbConnect.getConnection(Const.DB_POOL_NAME);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if(conn!=null){
			try{
				pstmt = conn.prepareStatement("select * from system_setting");
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SystemSetting ss = new SystemSetting();
					ss.setId(rs.getInt("id"));
					ss.setName(rs.getString("name"));
					ss.setValue(rs.getString("value"));
					ss.setProperty(rs.getString("property"));
					ss.setCreateTime(rs.getDate("create_time"));
					Macro.systemSettings.add(ss);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
				DbConnect.close(Const.DB_POOL_NAME, conn);
			}
		}
	}
	public static SystemSetting getSystemSetting(String name){
		for(SystemSetting ss: Macro.systemSettings){
			if(ss.getName().equals(name)){
				return ss;
			}
		}
		return null;
	}
	
}
