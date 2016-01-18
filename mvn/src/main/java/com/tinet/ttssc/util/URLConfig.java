package com.tinet.ttssc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/***
 * 获取配置文件数据
 * @author wanghl
 *
 */
public class URLConfig {
	
	//URLConfig.getInstance().getString("webRoot");
	private static Properties prop = new Properties();
	private static URLConfig instance=null;
	private static String propertiesURL="/jdbc.properties";
	
	private URLConfig(){
		try {
			   InputStream in = URLConfig.class.getResourceAsStream(propertiesURL);
			   prop.clear();
			   prop.load(in);
			   in.close();
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	
	public synchronized static URLConfig getInstance(){
		if(instance==null){
			instance = new URLConfig();
		}
		return instance;
	}
	
	public String getString(String key){
		return prop.getProperty(key, "");
	}
	
	
	
	public String getString(String key,String defaultValue){
		return prop.getProperty(key, defaultValue);
	}
	
	public Integer getInt(String key, int defaultValue){
        String property = prop.getProperty(key, String.valueOf(defaultValue));
        return Integer.valueOf(property);
    }
	
	public double getDouble(String key,double defaultValue){
		 String property = prop.getProperty(key, String.valueOf(defaultValue));
		 return Double.parseDouble(property);
	}
	
	public long getLong(String key,double defaultValue){
		 String property = prop.getProperty(key, String.valueOf(defaultValue));
		 return Long.parseLong(property);
	}
	
	
	public boolean getBoolean(String key){
		 String property = prop.getProperty(key, "false");
		 return Boolean.parseBoolean(property);
	}
	
	
	//测试
	public   static   void   main(String   args[]) 
	{ 
		
	}

}
