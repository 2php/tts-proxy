package com.tinet.ttssc.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.inc.Macro;


/**
 * 
 * 管理类DbManager支持对一个或多个由属性文件定义的数据库连接 池的访问.客户程序可以调用getInstance()方法访问本类的唯一实例.
 *<p>
 * 文件名： DbManager.java
 *<p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD.  All rights reserved.
 * @author 安静波
 * @since 1.0
 * @version 1.0
 */

public class DbManager {
	static private DbManager instance; // 唯一实例
	static private int clients;

	private Vector<Driver> drivers = new Vector<Driver>();
	private Logger logger = LoggerFactory.getLogger(getClass());
	private Hashtable<String, DbPool> pools = new Hashtable<String, DbPool>();

	/**
	 * 返回唯一实例.如果是第一次调用此方法,则创建实例
	 * 
	 * @return DbManager 唯一实例
	 */
	static synchronized public DbManager getInstance() {
		if (instance == null) {
			instance = new DbManager();
		}
		clients++;
		return instance;
	}

	/**
	 * 建构函数私有以防止其它对象创建本类实例
	 */
	private DbManager() {
		init();
	}

	/**
	 * 将连接对象返回给由名字指定的连接池
	 * 
	 * @param name
	 *            在属性文件中定义的连接池名字
	 * @param con
	 *            连接对象
	 */
	public void freeConnection(String name, Connection conn) {
		DbPool pool = (DbPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(conn);
		}
	}

	/**
	 * 获取连接池中
	 * 
	 * @return int
	 * @param name
	 *            String
	 */
	public synchronized int getSize(String name) {
		DbPool pool = (DbPool) pools.get(name);
		if (pool != null) {
			return pool.getSize();
		}
		return 0;
	}

	/**
	 * 052 * 获得一个可用的(空闲的)连接.如果没有可用连接,且已有连接数小于最大连接数 053 * 限制,则创建并返回新连接 054 * 055
	 * * @param name 在属性文件中定义的连接池名字 056 *
	 * 
	 * @return Connection 可用连接或null 057
	 * 
	 * @param name
	 *            String
	 * @return Connection
	 */
	public Connection getConnection(String name) {
		DbPool pool = (DbPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection();
		}
		return null;
	}

	/**
	 * 获得一个可用连接.若没有可用连接,且已有连接数小于最大连接数限制, 则创建并返回新连接.否则,在指定的时间内等待其它线程释放连接.
	 * 
	 * @param name
	 *            连接池名字
	 * @param time
	 *            以毫秒计的等待时间
	 * @return Connection 可用连接或null
	 */
	public Connection getConnection(String name, long time) {
		DbPool pool = (DbPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	/**
	 * 关闭所有连接,撤销驱动程序的注册
	 */
	public synchronized void release() {
		// 等待直到最后一个客户程序调用
		if (--clients != 0) {
			return;
		}
		
		Enumeration<DbPool> allPools = pools.elements();
		while (allPools.hasMoreElements()) {
			DbPool pool = (DbPool) allPools.nextElement();
			pool.release();
		}
		Enumeration<Driver> allDrivers = drivers.elements();
		while (allDrivers.hasMoreElements()) {
			Driver driver = (Driver) allDrivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				logger.info("撤销JDBC驱动程序 " + driver.getClass().getName() + "的注册");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据指定属性创建连接池实例.
	 *
	 */
	private void createPools() {

		DbPool pool = new DbPool(Const.DB_POOL_NAME, Macro.DB_URL, Macro.DB_USER, Macro.DB_PWD,
						Macro.DB_MAX_POOL, Macro.DB_MIN_POOL);
		pools.put(Const.DB_POOL_NAME, pool);
		logger.info("成功创建连接池"+Const.DB_POOL_NAME);
	}
	/**
	 * 读取属性完成初始化
	 */
	private void init() {
		loadDrivers();
		createPools();
	}

	/**
	 * 装载和注册所有JDBC驱动程序
	 * 
	 * @param props
	 *            属性
	 */
	private void loadDrivers() {
		try {
			Driver driver = (Driver) Class.forName(Macro.DB_DRIVER)
					.newInstance();
			DriverManager.registerDriver(driver);
			drivers.addElement(driver);
			logger.info("成功注册JDBC驱动程序" + Macro.DB_DRIVER);
		} catch (Exception e) {
			logger.info("无法注册JDBC驱动程序: " + Macro.DB_DRIVER + ", 错误: " + e);
		}
	}

}
