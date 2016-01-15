package com.tinet.ttssc.test;

import com.tinet.ttssc.TtsJni;

public class TtsThread extends Thread  {
		public Integer threadId;
		private TtsJni jni = new TtsJni();
	    /**
	     * 重写（Override）run()方法 JVM会自动调用该方法
	     */
	    public void run() {
	        System.out.println("thread:" + threadId + " runing");
	        for(int i=0;i<10000;i++){
	        	String text = String.valueOf(threadId*10000 + i);
	        	System.out.println("request text=" + text);
	        	jni.request(text, "tts_voices/"+text+".wav", "172.16.15.241",0,1,10);
	        }
	    }
}
