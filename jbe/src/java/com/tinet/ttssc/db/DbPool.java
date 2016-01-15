package com.tinet.ttssc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 数据库连接池实现类
 *<p>
 * 文件名： DbPool.java
 *<p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD.  All rights reserved.
 * @author 安静波
 * @since 1.0
 * @version 1.0
 */
class DbPool {
	private int checkedOut;
	private Vector<Connection> freeConnections = new Vector<Connection>();
	private int maxConn;
	private int minConn;
	private String password;
	private String url;
	private String user;
	private String name;

	public DbPool(String name, String url, String user, String password,
			int maxConn, int minConn) {
		this.name = name;
		this.url = url;
		this.user = user;
		this.password = password;
		this.maxConn = maxConn;
		this.minConn = minConn;
	}

	public synchronized void freeConnection(Connection conn) {
		if(checkedOut > minConn){
			checkedOut--;
			try{
				conn.close();
				}catch(Exception e){
					
				}
		}else{
			freeConnections.addElement(conn);
			checkedOut--;
			notifyAll();
		}
	}
	public String getName(){
		return name;
	}
	public synchronized int getSize() {
		return freeConnections.size();
	}

	public synchronized Connection getConnection() {
		Connection conn = null;
		if (freeConnections.size() > 0) {
			conn = (Connection) freeConnections.firstElement();
			freeConnections.removeElementAt(0);
			try {
				if (conn.isClosed()) {
					conn = newConnection();
				}
			} catch (SQLException e) {
				conn = newConnection();
			}
		} else if (maxConn == 0 || checkedOut < maxConn) {
			conn = newConnection();
		}
		if (conn != null) {
			checkedOut++;
		}
		return conn;
	}

	public synchronized Connection getConnection(long timeout) {
		long startTime = new Date().getTime();
		Connection con;
		while ((con = getConnection()) == null) {
			try {
				wait(timeout);
			} catch (InterruptedException e) {
			}
			if ((new Date().getTime() - startTime) >= timeout) {
				return null;
			}
		}
		return con;
	}

	public synchronized void release() {
		Enumeration<Connection> allConnections = freeConnections.elements();
		while (allConnections.hasMoreElements()) {
			Connection conn = (Connection) allConnections.nextElement();
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		freeConnections.removeAllElements();
	}

	private Connection newConnection() {
		Connection conn = null;
		try {
			for(int i = 0; ((conn == null) && (i < 3)); i++){
				if (user == null) {
					conn = DriverManager.getConnection(url);
				} else {
					conn = DriverManager.getConnection(url, user, password);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}
}
