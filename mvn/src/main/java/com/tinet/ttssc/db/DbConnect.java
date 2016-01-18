package com.tinet.ttssc.db;

import java.sql.Connection;

/**
* 数据库连接实现类
*<p>
* 文件名： DbConnect.java
*<p>
* Copyright (c) 2006-2010 T&I Net Communication CO.,LTD.  All rights reserved.
* @author 安静波
* @since 1.0
* @version 1.0
*/

public class DbConnect {
	public DbConnect() {
	}

	private static DbManager connMgr;

	public static void init() {
		connMgr = DbManager.getInstance();
		System.out.println("初始化数据库连接池OK！");
	}

	/**
	 * 直接与数据库建立连接
	 * 
	 * @return 一个连接,失败返回NULL
	 */
	public static Connection getConnection(String className) {
		Connection conn = connMgr.getConnection(className);
		return conn;
	}

	public static void close(String className, Connection conn) {
		connMgr.freeConnection(className, conn);
	}
}
