package com.tinet.ttssc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tinet.ttssc.entity.TtsRequest;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.inc.Macro;
import com.tinet.ttssc.service.AwsDynamoDbService;
import com.tinet.ttssc.service.AwsS3Service;
import com.tinet.ttssc.service.SystemSettingService;
import com.tinet.ttssc.util.JSONObject;
import com.tinet.ttssc.util.MD5Encoder;
import com.tinet.ttssc.util.RemoteClient;
import com.tinet.ttssc.util.StringUtil;

public class TtsGet extends HttpServlet {
	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String text = request.getParameter("text");
		PrintWriter out = response.getWriter();
		if(StringUtil.isEmpty(text)){
			out.append("text字段不能为空");
			out.flush();
			out.close();
			return;
		}
		String uniqueId	= request.getParameter("uniqueId");
		if(StringUtil.isEmpty(uniqueId)){
			out.append("uniqueId字段不能为空");
			out.flush();
			out.close();
			return;
		}
		TtsRequest ttsRequest = new TtsRequest();
		ttsRequest.setFromIp(RemoteClient.getIpAddr(request));
		String enterpriseId = request.getParameter("enterpriseId");
		ttsRequest.setEnterpriseId(enterpriseId);
		String priority	= request.getParameter("priority");
		if(StringUtil.isEmpty(priority)){
			ttsRequest.setPriority(Integer.parseInt(SystemSettingService.getSystemSetting(Const.DEFAULT_PRIORITY).getValue()));
		}else{
			ttsRequest.setPriority(Integer.parseInt(priority));
		}
		
		ttsRequest.setUniqueId(uniqueId);
		String timeoutStr	= request.getParameter("timeout");
		Integer timeout = Integer.parseInt(SystemSettingService.getSystemSetting(Const.DEFAULT_TIMEOUT).getValue());
		if(StringUtil.isNotEmpty(timeoutStr)){
			timeout = Integer.parseInt(timeoutStr);
		}
		String speedStr	= request.getParameter("speed");
		if(StringUtil.isNotEmpty(speedStr) && StringUtil.isNumber(speedStr)){
			Integer speed = Integer.parseInt(speedStr);
			if(speed >= -500 && speed <= 500){
				ttsRequest.setSpeed(speed);
			}else{
				out.append("speed字段范围-500到+500");
				out.flush();
				out.close();
				return;
			}
		}else{
			ttsRequest.setSpeed(0);
		}
		String redirect	= request.getParameter("redirect");
		if(StringUtil.isNotEmpty(redirect) && StringUtil.isNumber(redirect)){
			Integer redirectInteger = Integer.parseInt(redirect);
			if(redirectInteger==0 || redirectInteger==1){
				ttsRequest.setRedirect(redirectInteger);
			}else{
				out.append("redirect范围0或1");
				out.flush();
				out.close();
				return;
			}
		}else{
			ttsRequest.setRedirect(1);
		}
		String volumeStr = request.getParameter("volume");
		if(StringUtil.isNotEmpty(volumeStr) && StringUtil.isNumber(volumeStr)){
			Integer volume = Integer.parseInt(volumeStr);
			if(volume >= -20 && volume <= 20){
				ttsRequest.setVolume(volume);
			}else{
				out.append("volume字段范围-20到+20");
				out.flush();
				out.close();
				return;
			}
		}else{
			ttsRequest.setVolume(10);
		}
		String vidStr	= request.getParameter("vid");
		if(StringUtil.isNotEmpty(vidStr) && StringUtil.isNumber(vidStr)){
			Integer vid = Integer.parseInt(vidStr);
			if(vid == 1 ||  vid == 3){
				ttsRequest.setVid(vid);
			}else{
				out.append("vid只能为1（普通话）或者3（粤语）");
				out.flush();
				out.close();
				return;
			}
		}else{
			ttsRequest.setVid(1);
		}
		String syncStr	= request.getParameter("sync");
		if(StringUtil.isNotEmpty(syncStr) && StringUtil.isNumber(syncStr)){
			Integer sync = Integer.parseInt(syncStr);
			if(sync == 1 ||  sync == 0){
				ttsRequest.setSync(sync);
			}else{
				out.append("sync只能为1（同步）或者0（异步）");
				out.flush();
				out.close();
				return;
			}
		}else{
			ttsRequest.setSync(1);
		}
		
		ttsRequest.setRequestTime(new Date());
		ttsRequest.setText(text);
		ttsRequest.setHash(MD5Encoder.encode(text));
		
		
		String fileName = SystemSettingService.getSystemSetting(Const.TTS_CACHE_ABS_PATH).getValue() + "/" + ttsRequest.getHash().substring(0,2) + "/" + ttsRequest.getHash() + ".wav"; 
		List<JSONObject> list = AwsDynamoDbService.query(Const.DYNAMODB_TABLE, Const.DYNAMODB_PRIMARY_ID, ttsRequest.getHash());
		if(list.size() > 0){
			ttsRequest.setStartTime(new Date());
			ttsRequest.setHitCache(1);
			ttsRequest.setPosition(0);
			ttsRequest.setResult(0);
			ttsRequest.setTtsServer(null);
			ttsRequest.setEndTime(new Date());
			ttsRequest.setUuid(UUID.randomUUID().toString());
			ttsRequest.setDone(true);
			//写日志入库
			TtsRequest.saveTtsLog(ttsRequest);
		}else {
			ttsRequest.setHitCache(0);
			ttsRequest.setRetry(3);
			ttsRequest.SetValidServer(Macro.ttsServers);
			ttsRequest.setNotifyThread(Thread.currentThread());
			ttsRequest.setDone(false);
			//System.out.println("ttsGet put this=" + Thread.currentThread() + " ttsRequest=" + ttsRequest);
			boolean pushRes = TtssEngine.pushRequest(ttsRequest);
			if(pushRes == false){
				out.append("超过TTSSC最大排队数");
				out.flush();
				out.close();
				return;
			}
			synchronized (Thread.currentThread()) {
				try {
					//System.out.println("start wait Thread:" + Thread.currentThread() + " time=" + timeout);
					Thread.currentThread().wait(1000*timeout);
					//System.out.println("finish wait Thread:" + Thread.currentThread());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(ttsRequest.getSync().equals(1)){
			//System.out.println("等待结束，判断是否完成 Thread" + Thread.currentThread() + "ttsRequest.isDone=" + ttsRequest.isDone());
			if(ttsRequest.isDone()){
				String bucketName = SystemSettingService.getSystemSetting(Const.S3_BUCKET).getValue();
				
				String webfileName = "http://" + bucketName + "." + Const.AWS_TTS_CACHE_URL_POSTFIX + "/" + ttsRequest.getHash().substring(0,2) + "/" + ttsRequest.getHash() + ".wav";
				//System.out.println("合成完成，文件："+webfileName + "时间=" + System.currentTimeMillis() + " thread:" + Thread.currentThread());
				//System.out.println("合成完成，文件绝对路径："+fileName);
	    		
    			if(ttsRequest.getRedirect().equals(1)){
	        		response.sendRedirect(webfileName);
	        	}else{
		        	out.append("success");
					out.flush();
					out.close();
	        	}
			}else{
				out.append("合成超时");
				out.flush();
				out.close();
			}
		}else{
			out.append("success");
			out.flush();
			out.close();
		}

	}

}
