package com.tinet.ttssc.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
*执行Linux系统命令工具类
*<p>
* 文件名： SystemCmd.java
*<p>
* Copyright (c) 2006-2010 T&I Net Communication CO.,LTD.  All rights reserved.
* @author 安静波
* @since 1.0
* @version 1.0
* @see com.tinet.ccic.util.VoiceFile
* @see com.tinet.ccic.wm.common.service.imp.SystemCmdServiceImp
*/

public class SystemCmd {
	
	public static String executeCmd(String cmd) {
		Runtime r = Runtime.getRuntime();
		try {
			Process p = r.exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String inline;
			while (null != (inline = br.readLine())) {
				sb.append(inline).append("\n");
			}
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while (null != (inline = br.readLine())) {
				sb.append(inline).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	
	
	/**
	 * 执行特殊命令,如带通配符*号等
	 * @param cmd 命令，如 String[] delCmd = {"sh", "-c", "/bin/rm -f /var/nfs/logo/logo_tmp/3000001-2001-*"};
	 * @return
	 */
	public static String executeCmd(String[] cmd) {
		Runtime r = Runtime.getRuntime();
		try {
			Process p = r.exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String inline;
			while (null != (inline = br.readLine())) {
				sb.append(inline).append("\n");
			}
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while (null != (inline = br.readLine())) {
				sb.append(inline).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
}
