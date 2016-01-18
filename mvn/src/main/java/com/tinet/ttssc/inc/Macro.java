package com.tinet.ttssc.inc;

import java.util.ArrayList;
import java.util.List;

import com.tinet.ttssc.TtssEngine;
import com.tinet.ttssc.entity.SystemSetting;
import com.tinet.ttssc.entity.TtsServer;

public class Macro {
	public static String DB_DRIVER;
	public static String DB_URL;
	public static String DB_USER;
	public static String DB_PWD;
	public static Integer DB_MIN_POOL;
	public static Integer DB_MAX_POOL;
	
	public static List<SystemSetting> systemSettings = new ArrayList<SystemSetting>();
	public static List<TtsServer> ttsServers = new ArrayList<TtsServer>();
	public static List<TtssEngine> engines = new ArrayList<TtssEngine>();
	
	public static Integer threadIdIndex=0;
}
