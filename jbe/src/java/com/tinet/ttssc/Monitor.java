package com.tinet.ttssc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tinet.ttssc.entity.TtsMonitor;
import com.tinet.ttssc.entity.TtsRequest;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.inc.Macro;
import com.tinet.ttssc.service.SystemSettingService;
import com.tinet.ttssc.util.MD5Encoder;
import com.tinet.ttssc.util.RemoteClient;
import com.tinet.ttssc.util.StringUtil;

public class Monitor extends HttpServlet {
	public boolean ttsDone = false;
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
		
		PrintWriter out = response.getWriter();
		String contentType = request.getParameter("contentType");
		
		TtsMonitor monitor = getMonitor();
		JSONObject info = new JSONObject();
		info.put("totalEngineCount", monitor.getTotalEngineCount());
		info.put("usedEngineCount", monitor.getUsedEngineCount());
		info.put("waitCount", monitor.getWaitCount());
		info.put("dealCount", monitor.getDealCount());
		info.put("failCount", monitor.getFailCount());
		info.put("engineInfo", monitor.getEngineInfo());
		if("plain".equals(contentType)){
			StringBuilder sb = new StringBuilder();
			json2plain("", info, sb);
			out.append(sb.toString());
		}else{
			out.append(info.toString());
		}
		out.flush();
		out.close();
		return;

	}
	public static TtsMonitor getMonitor(){
		TtsMonitor monitor = new TtsMonitor();
		monitor.setTotalEngineCount(Macro.engines.size());
		Integer usedCount = 0;
		List<JSONObject> engines = new ArrayList<JSONObject>();
		for(TtssEngine engine: Macro.engines){
			if(engine.isRunning()){
				usedCount ++;
			}
			JSONObject engineInfo = new JSONObject();
			engineInfo.put("id", engine.getId());
			engineInfo.put("threadId", engine.getThreadId());
			engineInfo.put("serverIp", engine.getTtsServer().getIp());
			engineInfo.put("serverType", engine.getTtsServer().getType());
			engineInfo.put("dealCount", engine.getThreadDealCount());
			engineInfo.put("failCount", engine.getThreadFailCount());
			engines.add(engineInfo);
		}
		monitor.setEngineInfo(engines);
		monitor.setUsedEngineCount(usedCount);
		return monitor;
	}
	public String json2plain(String prefix, JSONObject object, StringBuilder sb){
	    Iterator<String> keys = object.keys();  
	    Object o;  
	    String key;  
	    while(keys.hasNext()){  
	        key=keys.next();  
	        try {
				o=object.get(key);
				if(prefix==null || prefix.equals("")){
					
				}
				try {
					JSONObject jo = JSONObject.fromObject(o.toString());
					if(prefix==null || prefix.equals("")){
						json2plain(key, jo, sb);
					}else{
						json2plain(prefix + "." + key, jo, sb);
					}
				} catch (Exception e) {
					try{
	                    JSONArray ja = JSONArray.fromObject(o.toString());
	                    for(int i=0; i< ja.size();i++){
	                    	if(prefix==null || prefix.equals("")){
	                    		json2plain(key+"["+i+"]", (JSONObject)ja.get(i), sb);
	        				}else{
	                            json2plain(prefix + "." + key+"["+i+"]", (JSONObject)ja.get(i), sb);
	        				}
	                    }
	            	}catch (Exception e1) {
	            		if(prefix==null || prefix.equals("")){
	    					sb.append(key + ":" + o.toString() + "\r\n");
	    				}else{
	    					sb.append(prefix + "." + key + ":" + o.toString() + "\r\n");
	    				}
	            	}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}  
	    }
	    return sb.toString();
	}
}
