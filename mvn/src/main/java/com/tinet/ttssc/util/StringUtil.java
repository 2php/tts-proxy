// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   StringUtil.java

package com.tinet.ttssc.util;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 扩展自org.apache.commons.lang.StringUtils，提供操作String的工具函数。
 *<p>
 * 文件名： StringUtil.java
 *<p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD.  All rights reserved.
 * @author 周营昭
 * @since 1.0
 * @version 1.0
 */
public final class StringUtil{

	public StringUtil() {
	}
	
	public static boolean inArray(String e, ArrayList<String> array){
		for(String str:array){
			if(str.equals(e)){
				return true;
			}
		}
		return false;
	}
	
	public static int indexOf(String e, String[] array) {
		for(int i = 0; i < array.length; i++) {
			if (array[i].equals(e)) {
				return i;
			}
		}
		return -1;
	}
	
	public static boolean existSame(ArrayList<String> array1, ArrayList<String> array2){
		for(String str1:array1){
			for(String str2:array2){
				if(str2.equals(str1)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 使用指定的分割符(可以是多个)做String分词。参数seperators中的所有字符都作为分隔符。
	 * <p>
	 * 如果参数include标志为true，则分隔符也作为分词的一部分返回；如果为false，则分隔符将被跳过。
	 * 
	 * @param seperators 分隔符字符串。
	 * @param list 待分词的字符串。
	 * @param include 返回的分词中是否包含分隔符。
	 * @return 分割后的字符串数组。
	 */
	public static String[] split(String seperators, String list, boolean include) {
		StringTokenizer tokens = new StringTokenizer(list, seperators, include);
		String result[] = new String[tokens.countTokens()];
		int i = 0;
		
		while (tokens.hasMoreTokens())
			result[i++] = tokens.nextToken();
		return result;
	}
	public static void main(String[] argv){
		System.out.println(StringUtil.parseNameStyle("ThisIsATest", 1));
		System.out.println(StringUtil.parseNameStyle("this_is_a_test", 0));
	}

	/**
	 * 将字符串转成boolean类型。
	 * 如果字符串等于"true"、"t"、"1"，则返回true，否则返回false。
	 * @param tfString 待转换字符串。
	 * @return
	 */
	public static boolean booleanValue(String tfString) {
		String trimmed = tfString.trim().toLowerCase();
		return "true".equals(trimmed) 
				|| "1".equals(trimmed) 
				|| "t".equals(trimmed);
	}
	
	/**
	 * 根据提供的标示符,分割字符串。
	 * 参数delim中的字符都会被作为分隔符使用，分隔符不会被作为分词的一部分。
	 * 
	 * @param str 待分割字符串。
	 * @param delim 分隔符。
	 * @return
	 */
	public static final String[] explodeString(String str, String delim) {
		if (str == null || str.equals("")) {
			String[] retstr = new String[1];
			retstr[0] = "";
			return retstr;
		}
		StringTokenizer st = new StringTokenizer(str, delim);
		String[] retstr = new String[st.countTokens()];
		int i = 0;
		
		while (st.hasMoreTokens()) {
			retstr[i] = st.nextToken();
			i++;
		}
		
		if (i == 0) {
			retstr[0] = str;
		}
		
		return retstr;
	}

	/**
	 * 判断是否为整型数字类型。
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[1-9][0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否为整型数字类型。
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断一个字符是否不是空字符串。
	 * 如果一个字符为null或者长度为0的字符串，则被认为是Empty。
	 * 长度不为0的空格符字符串也被认为not empty。
	 * @param string
	 * @return true 字符串不为空字符串。
	 */
	public static boolean isNotEmpty(String string) {
		return string != null && string.trim().length() > 0;
	}

	/**
	 * 判断一个字符是否为空字符串。
	 * 如果一个字符为null或者长度为0的字符串，则被认为是Empty。
	 * 长度不为0的空格符字符串也被认为not empty。
	 * 
	 * @param string
	 * @return true 字符串为空字符串。
	 */
	public static boolean isEmpty(String string) {
		return string == null || string.trim().length() == 0;
	}
	
	/**
	 * 判断字符串是否非空。
	 * <p> 功能等同于{@link StringUtil#isNotEmpty}。 
	 * 
	 * @param src 要判断的字符串。
	 * @return null或长度为0则返回false，否则返回true。
	 */
	public static boolean isNotNull(String src) {
		return isNotEmpty(src);
	}

	/**
	 * 过滤空值。
	 * 如果参数src不为空，则返回src的字符串表示，否则返回参数defaultValue指定的值。
	 * 
	 * @param src 要过滤的对象。
	 * @param defaultValue 默认返回值。
	 * @return 过滤null之后的返回值，如果为null，则返回默认值，否则去掉两边的空格后返回。
	 */
	public static String filterNull(Object src, String defaultValue) {
		if (src != null) {
			return src.toString().trim();
		}

		return defaultValue;
	}

	

	/**
	 * 从参数src左边取参数length指定数量的字符。
	 * 如果参数src为null，则返回参数defaultValue。
	 * 
	 * @param src 源字符串。
	 * @param length 截取长度。
	 * @param defaultValue src为null情况下返回的值。
	 * @return
	 */
	public static String left(Object src, int length, String defaultValue) {
		if (src != null) {
			String temp = src.toString();

			if (temp.length() >= length) {
				return temp.substring(0, length);
			}

			return temp;
		}

		return defaultValue;
	}

	

	/**
	 * 从参数src右边取参数length指定数量的字符。
	 * 如果参数src为null，则返回参数defaultValue。
	 * 
	 * @param src 源字符串。
	 * @param length 截取长度。
	 * @param defaultValue src为null情况下返回的值。
	 * @return
	 */
	public static String right(Object src, int length, String defaultValue) {
		if (src != null) {
			String temp = src.toString();
			int tempLen = temp.length();

			if (tempLen >= length) {
				return temp.substring(tempLen - length, tempLen);
			}

			return temp;
		}

		return defaultValue;
	}


	/**
	 * 获取不带扩展名的文件名。
	 * 比如：StringUtil.getFilenameWithNoExt("C:/dir/a.txt") = "C:/dir/a";
	 * 
	 * @param filename 要处理的文件名。
	 * @return 去掉扩展名后的文件名。
	 */
	public static String getFilenameWithNoExt(String filename) {
		return filename.substring(0, filename.lastIndexOf("."));
	}

	/**
	 * 获取文件的扩展名。
	 * 比如：StringUtil.getFilenameWithNoExt("C:/dir/a.txt") = "txt";
	 * 
	 * @param filename 要处理的文件名。
	 * @return 文件名的扩展名。
	 */
	public static String getFilenameExt(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	/**
	 * 将参数src指定的字符串转成整型，如果无法解析为整型数字，则返回参数defaultValue指定的值。
	 * 
	 * @param src
	 * @param defaultValue
	 * @return int 解析后的整数。
	 */
	public static int toInt(Object src, int defaultValue) {
		try {
			return Integer.parseInt(src.toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String numToChinese(String input) {
		String s1 = "零壹贰叁肆伍陆柒捌玖";
		String s4 = "分角整元拾佰仟万拾佰仟亿拾佰仟";
		String temp = "";
		String result = "";
		if (input == null)
			return "输入字串不是数字串只能包括以下字符（??0??～??9??，??.??)，输入字串最大只能精确到仟亿，小数点只能两位！";
		temp = input.trim();

		try {
			Float.parseFloat(temp);
		} catch (Exception e) {
			return "输入字串不是数字串只能包括以下字符（??0??～??9??，??.??)，输入字串最大只能精确到仟亿，小数点只能两位！";
		}
		
		int len = 0;
		if (temp.indexOf(".") == -1)
			len = temp.length();
		else
			len = temp.indexOf(".");
		if (len > s4.length() - 3)
			return ("输入字串最大只能精确到仟亿，小数点只能两位！");
		int n1, n2 = 0;
		String num = "";
		String unit = "";
		String tempNum = "";
		for (int i = 0; i <= len + 2; i++)
		{
			if (temp.length() == i)
			{
				break;
			}
			if (i == len)
			{
				tempNum = "";
				continue;
			}
			n1 = Integer.parseInt(String.valueOf(temp.charAt(i)));
			num = s1.substring(n1, n1 + 1);
			n1 = len - i + 2;
			unit = s4.substring(n1, n1 + 1);
			if (num.equals("零"))
			{
				if (tempNum.equals("零"))
				{
					if (unit.equals("万") || unit.equals("亿") || unit.equals("元")||unit.equals("角") ||unit.equals("分"))
					{
						if (result.indexOf("零") == result.length() - 1)
						{
							result = result.substring(0, result.length() - 1);
						}
						if(!unit.equals("角") &&!unit.equals("分")){
							num = "";
						}else if(unit.equals("角") || unit.equals("分")){
							tempNum = num;
							continue;
						}
					}else
					{
						tempNum = num;
						continue;
					}

				}else
				{
					if (unit.equals("万") || unit.equals("亿") || unit.equals("元"))
					{
						if (len > 1)
						{
							num = "";
						}
					}else if(!unit.equals("角") &&!unit.equals("分"))
					{
						unit = "";
					}else if(unit.equals("角") || unit.equals("分")){
						tempNum = num;
						continue;
					}
				}
			}
			result = result.concat(num).concat(unit);
			tempNum = num;
		}
		if ((len == temp.length()) || (len == temp.length() - 1)||result.indexOf("元") == result.length() - 1)
			result = result.concat("整");

		return result;
	}
	public static String escapeHtml(String str){
		if(str == null){
			return "";
		}
		return str.replace("<", "&lt").replace(">", "&gt");
	}
	
	public static boolean isIp(String ip){
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher matcher = pattern.matcher(ip); 
        return matcher.matches();
	}
	/**
	 * 尝试转换字符串风格
	 * mode为1时将Java风格转为C风格
	 * 否则将C风格转为Java风格
	 * [c]str_arr <=> [java]StrArr 
	 * @param input 
	 * @param mode
	 * @return
	 */
	public static String parseNameStyle(String input, int mode){
		if(mode == 1){// 将Java风格转为C风格
			return trim(input.replaceAll("[A-Z]", "_$0").toLowerCase(), "_");
		}else{// 将C风格转为Java风格
			String[] arrtmp = input.split("_");
			StringBuffer result = new StringBuffer();
			for(int i = 0; i < arrtmp.length; i++){
				result = result.append(upperCaseFirst(arrtmp[i]));
			}
			return result.toString();
		}
	}
	/**
	 * 去除字符串以target为前后缀的字符串
	 * 若字符串为空，则去除字符串前后留白
	 * @param input
	 * @param target
	 * @return
	 */
	public static String trim(String input, String target){
		if(isNotEmpty(target)){
			if(input.startsWith(target)){
				input = input.substring(target.length());
			}
			if(input.endsWith(target)){
				input = input.substring(0, input.lastIndexOf(target));
			}
		}
		return input;
	}
	/**
	 * 大写字符串第一个字母
	 * @param input
	 * @return
	 */
	public static String upperCaseFirst(String input){
		StringBuffer str = new StringBuffer(input.substring(0, 1).toUpperCase()); 
		return str.append(input.substring(1)).toString();
	}
	/**
	 * 小写字符串第一个字母
	 * @param input
	 * @return
	 */
	public static String lowerCaseFirst(String input){
		StringBuffer str = new StringBuffer(input.substring(0, 1).toLowerCase());
		return str.append(input.substring(1)).toString();
	}


	
}
