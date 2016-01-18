package com.tinet.ttssc;

public class TtsJni {
	static 
	{ 
		System.out.println(System.getProperty("java.library.path"));
		try{
			System.out.println("loading Library ttsc...");
			System.loadLibrary("ttsc");
			System.out.println("loding Library ttsc done!");
		}catch(Exception e){
			e.printStackTrace();
		}
	} 
	public native int Initialize(); 
	public native int Uninitialize(); 
	public native int request(String text, String wavFile, String serverIp, int speed, int vid, int volume);
}
