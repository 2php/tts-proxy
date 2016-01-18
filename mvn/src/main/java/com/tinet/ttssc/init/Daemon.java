package com.tinet.ttssc.init;

import java.util.Timer;

import javax.servlet.ServletContextEvent;

import com.tinet.ttssc.CleanThread;
import com.tinet.ttssc.MonitorThread;
import com.tinet.ttssc.TtsJni;
import com.tinet.ttssc.TtssEngine;
import com.tinet.ttssc.db.DbConnect;
import com.tinet.ttssc.entity.TtsServer;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.inc.Macro;
import com.tinet.ttssc.service.SystemSettingService;
import com.tinet.ttssc.service.TtsServerService;
import com.tinet.ttssc.util.StringUtil;
import com.tinet.ttssc.util.SystemCmd;
import com.tinet.ttssc.util.URLConfig;


/**
* 初始化各模块启动全局参数
*<p>
* 文件名： Daemon.java
*<p>
* Copyright (c) 2006-2010 T&I Net Communication CO.,LTD.  All rights reserved.
* @author 安静波
* @since 1.0
* @version 1.0
*/
public class Daemon implements javax.servlet.ServletContextListener {

	public Daemon() {
	}

	/**
	 * 系统初始化
	 */
	public void contextInitialized(ServletContextEvent sce) {
		//Jni初始化
		System.out.println("初始化TTSSC");
		try{
			TtsJni jni = new TtsJni();
			jni.Initialize();
		}
		catch(RuntimeException e){
			e.printStackTrace();
		}
		//读取配置文件
		URLConfig config = URLConfig.getInstance();
		Macro.DB_DRIVER = config.getString("jdbc.driverClassName");
		Macro.DB_URL = config.getString("jdbc.url");
		Macro.DB_USER = config.getString("jdbc.username");
		Macro.DB_PWD = config.getString("jdbc.password");
		Macro.DB_MIN_POOL = Integer.parseInt(config.getString("jdbc.minPool"));
		Macro.DB_MAX_POOL = Integer.parseInt(config.getString("jdbc.maxPool"));
		//初始化数据库连接池
		DbConnect.init();
		//初始化sytemSetting
		SystemSettingService.init();
		//初始化目录
		String path = SystemSettingService.getSystemSetting(Const.TTS_CACHE_ABS_PATH).getValue();
		if(StringUtil.isNotEmpty(path)){
			String cmd= "/bin/mkdir -p " + path;
			System.out.println("创建目录 cmd=[" + cmd + "]");
			SystemCmd.executeCmd(cmd);
			for(char i='a'; i<= 'f'; i++){
				for(int j=0; j<= 9; j++){
					cmd = "/bin/mkdir -p " + path + "/" + i + j;
					System.out.println("创建目录 cmd=[" + cmd + "]");
					SystemCmd.executeCmd(cmd);
				}
				for(char j='a'; j<= 'f'; j++){
					cmd = "/bin/mkdir -p " + path + "/" + i + j;
					System.out.println("创建目录 cmd=[" + cmd + "]");
					SystemCmd.executeCmd(cmd);
				}
			}
			for(int i=0; i<= 9; i++){
				for(int j=0; j<= 9; j++){
					cmd = "/bin/mkdir -p " + path + "/" + i + j;
					System.out.println("创建目录 cmd=[" + cmd + "]");
					SystemCmd.executeCmd(cmd);
				}
				for(char j='a'; j<= 'f'; j++){
					cmd = "/bin/mkdir -p " + path + "/" + i + j;
					System.out.println("创建目录 cmd=[" + cmd + "]");
					SystemCmd.executeCmd(cmd);
				}
			}
		}
		
		//初始化ttsServer
		Macro.ttsServers = TtsServerService.init();
		//启动转换线程
		for(TtsServer server: Macro.ttsServers){
			if(server.getActive() == 1){
				for(int i=0;i<server.getLicense();i++){
					TtssEngine engine = new TtssEngine();
					engine.setTtsServer(server);
					engine.setThreadId(Macro.threadIdIndex++);
					engine.setName("TTSS engine " + server.getIp() + " thread:" + Macro.threadIdIndex);
					engine.start();
					Macro.engines.add(engine);
				}
			}
		}
		
		Timer monitorTimer =new Timer();
		Integer frequency = Integer.parseInt(SystemSettingService.getSystemSetting(Const.MONITOR_CONF).getValue());
		monitorTimer.schedule(new MonitorThread(), 3 * 1000,+ frequency*1000);
		System.out.println("开启监控tts使用量，每"+frequency+"秒执行一次");
		
		Timer cleanTimer =new Timer();
		cleanTimer.schedule(new CleanThread(), 60 * 1000, 3600*24*1000);
		System.out.println("开启清理过期数据线程，每天清理一次");
		return;
	}

	/**
	 * 关闭
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		//TtsJni.Uninitialize();
		System.out.println("关闭TtssEngine");
		for(TtssEngine engine: Macro.engines){
			engine.shutDown();
		}
	}
}

