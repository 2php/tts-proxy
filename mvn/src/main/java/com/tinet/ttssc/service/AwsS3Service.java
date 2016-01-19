package com.tinet.ttssc.service;

import java.io.File;
import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tinet.ttssc.entity.SystemSetting;
import com.tinet.ttssc.inc.Const;
import com.tinet.ttssc.inc.Macro;

public class AwsS3Service {
	private static BasicAWSCredentials awsCredential;
	private static AmazonS3Client s3Client;

	public static void init() {
		String accessKey = SystemSettingService.getSystemSetting(Const.AWS_CREDENTIAL).getValue();
		String secretKey = SystemSettingService.getSystemSetting(Const.AWS_CREDENTIAL).getProperty();
		awsCredential = new BasicAWSCredentials(accessKey, secretKey);
		s3Client = new AmazonS3Client(awsCredential);
		s3Client.setRegion(Region.getRegion(Regions.CN_NORTH_1));
	}

	public static boolean upload(String uploadFileName, String s3FileName){
		try{
			String bucketName = SystemSettingService.getSystemSetting(Const.S3_BUCKET).getValue();
			File file = new File(uploadFileName);
            s3Client.putObject(new PutObjectRequest(bucketName, s3FileName, file));
            return true;
		} catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }catch(Exception e){
			e.printStackTrace();
		}return false;
	}
	
	public static void main(String[] argv){
		SystemSetting setting = new SystemSetting();
		setting.setCreateTime(new Date());
		setting.setId(1);
		setting.setName(Const.AWS_CREDENTIAL);
		setting.setValue("");
		setting.setProperty("");
		Macro.systemSettings.add(setting);
		
		setting = new SystemSetting();
		setting.setCreateTime(new Date());
		setting.setId(2);
		setting.setName(Const.S3_BUCKET);
		setting.setValue("tinet-tts-cache");
		setting.setProperty("");	
		Macro.systemSettings.add(setting);
		
		AwsS3Service.init();
		String uploadFileName= "/Users/AaronAn/github/tts-proxy/sql/view.sql";
		String s3FileName="abc/123.sql";
		AwsS3Service.upload(uploadFileName, s3FileName);
		
		
	}
}
