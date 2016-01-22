package com.tinet.ttssc.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tinet.ttssc.TtsJni;

public class TestJni {
	@BeforeClass
	// 在所有方法执行之前执行
	public static void globalInit() {
	System.out.println("init all method...");
	}
	@AfterClass
	// 在所有方法执行之后执行
	public static void globalDestory() {
	System.out.println("destory all method...");
	}
	
	@Test
	public void testJni(){
		TtsJni jni = new TtsJni();
		String text = "junit测试";
		String fileName = "test/"+text+".wav";
    	System.out.println("request text=" + text);
    	jni.request(text, fileName, "172.16.15.241",0,1,10);
    	File file = new File(fileName);
    	if(file.exists()){
    		FileInputStream fis = null;
    		try{
	        fis = new FileInputStream(file);
	        Integer size = fis.available();
	        System.out.println(fileName + " size=" + size);
	        fis.close();
	        file.delete();
	        assertTrue(size>44);
    		}catch(Exception e){
	        	e.printStackTrace();
	        }
    	}
    	fail("test error");
	}
	@Before
	public void setUp() throws Exception {  
		System.out.println("初始化TTSSC");
		try{
			TtsJni jni = new TtsJni();
			jni.Initialize();
		}
		catch(RuntimeException e){
			e.printStackTrace();
		}
	}  
	
	@After
	public void tearDown() throws Exception {  
		System.out.println("测试完成");
    }  
}
