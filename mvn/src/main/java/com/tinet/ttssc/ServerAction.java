package com.tinet.ttssc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tinet.ttssc.entity.TtsServer;
import com.tinet.ttssc.inc.Macro;
import com.tinet.ttssc.service.TtsServerService;
import com.tinet.ttssc.util.DateUtil;
import com.tinet.ttssc.util.JSONArray;
import com.tinet.ttssc.util.JSONObject;

public class ServerAction extends HttpServlet {
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
		
		String action=request.getParameter("action");
		String idstr = request.getParameter("id"); //id是tts_server id
		
		JSONObject res = new JSONObject();
		
		Integer id = -1;
		try{
			id = Integer.parseInt(idstr);
		}catch(Exception e){
		}
		
		if("start".equals(action)){
			if(!id.equals(-1)){
				TtsServer server = TtsServerService.getTtsServer(id);
				if(server != null){
					TtsServer toBeRemoved = null;
					for(TtsServer ts:Macro.ttsServers){
						if(ts.getId().equals(id)){
							toBeRemoved = ts;
							break;
						}
					}
					Macro.ttsServers.remove(toBeRemoved);
					Macro.ttsServers.add(server);
					
					//所有这个server的engine shutdown
					List<TtssEngine> removeList = new ArrayList<TtssEngine>();
					for(TtssEngine engine:Macro.engines){
						if(engine.getTtsServer().getId().equals(id)){
							engine.shutDown();
							removeList.add(engine);
						}
					}
					Macro.engines.removeAll(removeList);
					for(int i=0;i<server.getLicense();i++){
						TtssEngine engine = new TtssEngine();
						engine.setTtsServer(server);
						engine.setThreadId(Macro.threadIdIndex);
						engine.setName("TTSS engine " + server.getIp() + " thread:" + Macro.threadIdIndex);
						Macro.threadIdIndex++;
						engine.start();
						Macro.engines.add(engine);
					}
					res.put("result", 0);
					out.append(res.toString());
					out.flush();
					out.close();
					return;
				}else{
					res.put("result", 1);
					res.put("message", "id不存在");
					out.append(res.toString());
					out.flush();
					out.close();
					return;
				}
			}else{
				res.put("result", 1);
				res.put("message", "id为空或非数字");
				out.append(res.toString());
				out.flush();
				out.close();
				return;
			}

		}else if("stop".equals(action)){
			if(!id.equals(-1)){
				TtsServer server = TtsServerService.getTtsServer(id);
				if(server != null){	
					List<TtssEngine> removeList = new ArrayList<TtssEngine>();
					for(TtssEngine engine:Macro.engines){
						if(engine.getTtsServer().getId().equals(id)){
							engine.shutDown();
							removeList.add(engine);
						}
					}
					Macro.engines.removeAll(removeList);
					Macro.ttsServers.remove(server);
					res.put("result", 0);
					out.append(res.toString());
					out.flush();
					out.close();
					return;
				}else{
					res.put("result", 1);
					res.put("message", "id不存在");
					out.append(res.toString());
					out.flush();
					out.close();
					return;
				}
			}else{
				res.put("result", 1);
				res.put("message", "id为空或非数字");
				out.append(res.toString());
				out.flush();
				out.close();
				return;
			}
		}else if("list".equals(action)){
			List<TtsServer> ttsServers = TtsServerService.init();
			JSONArray serverList = new JSONArray();
			for(TtsServer server: ttsServers){
				JSONObject info = new JSONObject();
				info.put("id", server.getId());
				info.put("active", server.getActive());
				info.put("ip", server.getIp());
				info.put("license", server.getLicense());
				info.put("type", server.getType());
				info.put("vid", server.getVid());
				info.put("createTime", DateUtil.format(server.getCreateTime(), DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss));
				serverList.add(info);
			}
			out.append(serverList.toString());
			out.flush();
			out.close();
		}
		
	}
}
