package com.tinet.ttssc.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tinet.ttssc.db.DbConnect;
import com.tinet.ttssc.entity.TtsServer;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.inc.Macro;

public class TtsServerService {
	public static List<TtsServer> init(){
		List<TtsServer> ttsServers = new ArrayList<TtsServer>();
		Connection conn = DbConnect.getConnection(Const.DB_POOL_NAME);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if(conn!=null){
			try{
				pstmt = conn.prepareStatement("select * from tts_server order by type,license");
				rs = pstmt.executeQuery();
				while (rs.next()) {
					TtsServer ts = new TtsServer();
					ts.setId(rs.getInt("id"));
					ts.setActive(rs.getInt("active"));
					ts.setIp(rs.getString("ip"));
					ts.setLicense(rs.getInt("license"));
					ts.setCreateTime(rs.getDate("create_time"));
					ts.setType(rs.getInt("type"));
					ts.setVid(rs.getInt("vid"));
					ttsServers.add(ts);
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
		return ttsServers;
	}
	public static TtsServer getTtsServer(Integer ttsServerId){
		TtsServer ttsServer = null;
		Connection conn = DbConnect.getConnection(Const.DB_POOL_NAME);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if(conn!=null){
			try{
				pstmt = conn.prepareStatement("select * from tts_server where id="+ ttsServerId);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ttsServer = new TtsServer();
					ttsServer.setId(rs.getInt("id"));
					ttsServer.setActive(rs.getInt("active"));
					ttsServer.setIp(rs.getString("ip"));
					ttsServer.setLicense(rs.getInt("license"));
					ttsServer.setCreateTime(rs.getDate("create_time"));
					ttsServer.setType(rs.getInt("type"));
					ttsServer.setVid(rs.getInt("vid"));
					break;
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
		return ttsServer;
	}
}
