package com.tinet.ttssc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.tinet.ttssc.entity.SystemSetting;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.inc.Macro;
import com.tinet.ttssc.util.JSONObject;
import com.tinet.ttssc.util.MD5Encoder;

public class AwsDynamoDbService {
	private static BasicAWSCredentials awsCredential;
	private static DynamoDB dynamoDB;
	private static AmazonDynamoDBClient dynamoDBClient ;
	
	public static void init(){
		String accessKey = SystemSettingService.getSystemSetting(Const.AWS_CREDENTIAL).getValue();
		String secretKey = SystemSettingService.getSystemSetting(Const.AWS_CREDENTIAL).getProperty();
		awsCredential = new BasicAWSCredentials(accessKey, secretKey);
		dynamoDBClient = new AmazonDynamoDBClient(awsCredential);
		dynamoDBClient.setRegion(Region.getRegion(Regions.CN_NORTH_1));
		dynamoDB = new DynamoDB(dynamoDBClient);
	}
	public static List<JSONObject> query(String tableName, String keyName, String keyValue){
		Table table = dynamoDB.getTable(tableName);
		ItemCollection<QueryOutcome> items = table.query(keyName, keyValue);
		
		List<JSONObject> res = new ArrayList<JSONObject>();
		Iterator<Item> iterator = items.iterator();
		Item item = null;
		while (iterator.hasNext()) {
		    item = iterator.next();
		    JSONObject object = JSONObject.fromObject(item.toJSON());
		    res.add(object);
		    
		}
		return res;
	}
	
	public static boolean createItem(String tableName, String keyName, String keyValue, HashMap<String, Object> params ){
		 Table table = dynamoDB.getTable(tableName);
		 try {
            Item item = new Item()
                .withPrimaryKey(keyName, keyValue);
            for(String key :params.keySet()){
            	if(params.get(key) instanceof Integer){
            		item.withInt(key, (Integer)params.get(key));
            	}else if(params.get(key) instanceof String){
            		item.withString(key, (String)params.get(key));
            	}else if(params.get(key) instanceof Long){
            		item.withLong(key, (Long)params.get(key));
            	}else{
            		item.withString(key, params.get(key).toString());
            	}
            }
            table.putItem(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}
	
	public static void main(String[] argv){
		SystemSetting setting = new SystemSetting();
		setting.setCreateTime(new Date());
		setting.setId(1);
		setting.setName(Const.AWS_CREDENTIAL);
		setting.setValue("AKIAONGNWNAKPRAIAAAQ");
		setting.setProperty("lw/H5RR0pSXbSqjUrh+OqcLZGYS6n/ryXcfpw9HR");
		Macro.systemSettings.add(setting);
		
		AwsDynamoDbService.init();
		String tableName = "tts-cache";
		String text = "你好";
		String key = MD5Encoder.encode(text);
		long createTime = new Date().getTime()/1000; 
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("createTime", createTime);
		params.put("text", text);

		boolean res = AwsDynamoDbService.createItem(tableName, "key", key, params);
		System.out.println("AwsDynamoDbService.createItem tableName=" + tableName + " key=" + key + " params=" + params.toString() + " res=" + res);
		List<JSONObject> list = AwsDynamoDbService.query(tableName, "key", key);
		System.out.println("AwsDynamoDbService.query key=" + key + " size=" + list.size());
		for(JSONObject object : list){
			System.out.println(object.toString());
		}
	}
}
