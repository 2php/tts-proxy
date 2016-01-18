package com.tinet.ttssc;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinet.ttssc.entity.TtsRequest;
import com.tinet.ttssc.entity.TtsServer;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.service.SystemSettingService;


public class TtssEngine extends Thread{
	private static PriorityBlockingQueue<TtsRequest> queue = new PriorityBlockingQueue<TtsRequest>();
	private static Integer MAX_COUNT = 200;
	private static Logger logger = LoggerFactory.getLogger(TtssEngine.class);
	private static boolean terminated = false;
	private static AtomicInteger dealCount = new AtomicInteger(0);
	private static AtomicInteger failCount = new AtomicInteger(0);
	private Integer threadDealCount = 0;
	private Integer threadFailCount = 0;
	private Integer threadId;
	private TtsServer ttsServer;
	private boolean running = false;
	private TtsJni jni = new TtsJni();

	
	public Integer getThreadId() {
		return threadId;
	}
	public void setThreadId(Integer threadId) {
		this.threadId = threadId;
	}
	public TtsServer getTtsServer() {
		return ttsServer;
	}
	public void setTtsServer(TtsServer ttsServer) {
		this.ttsServer = ttsServer;
	}
	public boolean isRunning(){
		return running;
	}
	public TtssEngine() {
	}
	public static boolean pushRequest(TtsRequest ttsRequest) {
		try {
			if (queue != null) {
				if(queue.size() > MAX_COUNT){
					return false;
				}
				ttsRequest.setPosition(queue.size());
				queue.put(ttsRequest);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public void run(){
		terminated = false;
		logger.error("TTSSC engine start server=" + ttsServer.getIp() + " threadId=" + threadId);
		while (!terminated) {
			try {
				TtsRequest ttsRequest = queue.poll(10, TimeUnit.SECONDS);
				if (ttsRequest != null ) {
					if(ttsRequest.isValid(ttsServer)){//说明可以使用这个server转换
						running = true;
						dealCount.incrementAndGet();
						threadDealCount++;
						try{
							ttsRequest.setTtsServer(ttsServer);
							ttsRequest.setThreadId(threadId);
							ttsRequest.setStartTime(new Date());
							ttsRequest.setUuid(UUID.randomUUID().toString());
							String fileName = SystemSettingService.getSystemSetting(Const.TTS_CACHE_ABS_PATH).getValue() + "/" + ttsRequest.getHash().substring(0,2) + "/" + ttsRequest.getHash() + ".wav";
							int res = jni.request(ttsRequest.getText(), fileName, ttsServer.getIp(), ttsRequest.getSpeed().intValue(), ttsRequest.getVid().intValue(), ttsRequest.getVolume().intValue());
							//System.out.println("jni.request ok res= " + res + " time=" + System.currentTimeMillis() + " thread:" + ttsRequest.getNotifyThread() + " ttsRequest:" + ttsRequest);
							ttsRequest.setEndTime(new Date());
							ttsRequest.setResult(res);
							//存储到数据库日志中
							TtsRequest.saveTtsLog(ttsRequest);
							if (res !=0){
								failCount.incrementAndGet();
								threadFailCount++;
								if(ttsRequest.getRetry() > 0){
									ttsRequest.setRetry(ttsRequest.getRetry() - 1);
									ttsRequest.removeValid(ttsServer);
									if(ttsRequest.hasValid()){//如果还有有效的server再丢进去，否则就丢弃了
										TtssEngine.pushRequest(ttsRequest);
									}
								}
							}else{
								//Thread.sleep(20*1000);
								synchronized (ttsRequest.getNotifyThread()) {
									ttsRequest.setDone(true);
									ttsRequest.getNotifyThread().notifyAll();
								}
							}
						}
						catch (Exception e){
							e.printStackTrace();
						}
						finally{
							running = false;
						}
					}else {
						//说明这个server试过了，换下一个
						TtssEngine.pushRequest(ttsRequest);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("Error accours when polling webCallQueue event!");
			}
		}//end while
		logger.error("webcall engine terminated!");
	}
	public void shutDown(){
		terminated = true;
	}
	public static Integer getWaitCount(){
		return queue.size();
	}
	public static Integer getDealCount(){
		return dealCount.get();
	}
	public static Integer getFailCount(){
		return failCount.get();
	}
	public Integer getThreadDealCount() {
		return threadDealCount;
	}
	public void setThreadDealCount(Integer threadDealCount) {
		this.threadDealCount = threadDealCount;
	}
	public Integer getThreadFailCount() {
		return threadFailCount;
	}
	public void setThreadFailCount(Integer threadFailCount) {
		this.threadFailCount = threadFailCount;
	}
	
}
