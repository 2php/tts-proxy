package com.tinet.ttssc.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 使用Jackson构造的json-lib适配器，以为了在不修改原有实现的基础上替换掉json-lib的JSONArray
 * 
 * @author Jiangsl
 *
 */
@SuppressWarnings("serial")
public class JSONArray extends ArrayList<Object> {

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
	}

	public static JSONArray fromObject(String json) {
		try {
			if(json.indexOf("\"")!=-1){
				return mapper.readValue(json, JSONArray.class);
			}else{
				mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
				mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
				json = json.replace("[{", "[{'").replace("=", "':'").replace(",", "','").replace("'[", "[").replace(" ", "");
				return mapper.readValue(json, JSONArray.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONArray fromObject(Object object) {
		JSONArray ja = new JSONArray();
		List<Object> list = (List<Object>)object;
		for(Object o : list){
			ja.add(o);
		}
		return ja;
	}

	public JSONObject getJSONObject(int index) {
		return mapper.convertValue(super.get(index), JSONObject.class);
//		return (JSONObject) super.get(index);
	}

	public String getString(int index) {
		return super.get(index).toString();
	}

	public int getInt(int index) {
		return Integer.parseInt((String) super.get(index));
	}

	@Override
	public String toString() {
		String json = null;

		try {
			json = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return json;
	}

}
