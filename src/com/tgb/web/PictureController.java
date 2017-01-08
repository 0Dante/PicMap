package com.tgb.web;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.json.JSONObject;  

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tgb.entity.LoginError;
import com.tgb.entity.Picture;
import com.tgb.entity.Token;
import com.tgb.entity.User;
import com.tgb.manager.PicManager;
import com.tgb.manager.UserManager;

@Controller
public class PictureController {
	
	@Resource(name="picManager")
	private PicManager picManager;

	 @RequestMapping(value="/uploadPicture",method=RequestMethod.POST)
	 public String upload(@RequestParam MultipartFile[] myfiles,HttpServletRequest request) throws IOException  
	    {  
	      //���ֻ���ϴ�һ���ļ�����ֻ��ҪMultipartFile���ͽ����ļ����ɣ�����������ʽָ��@RequestParamע��   
	        //������ϴ�����ļ�����ô�����Ҫ��MultipartFile[]�����������ļ������һ�Ҫָ��@RequestParamע��   
	        //�����ϴ�����ļ�ʱ��ǰ̨���е�����<input type="file"/>��name��Ӧ����myfiles������������myfiles�޷���ȡ�������ϴ����ļ�   
	  
		 
		 try{
	        for(MultipartFile myfile : myfiles){   
	            if(myfile.isEmpty()){   
	                System.out.println("�ļ�δ�ϴ�");   
	                return "�ļ�δ�ϴ�";
	            }else{   
	            	long size = myfile.getSize();
	            	String contenttype = myfile.getContentType();
	            	String picname = myfile.getName();
	            	String originalname = myfile.getOriginalFilename();
	            	
	            	//����õ���Tomcat�����������ļ����ϴ���  {���񷢲�λ��}\\WEB-INF\\picture\\�ļ����� ������·��
	                String accesspath = request.getSession().getServletContext().getRealPath("/picture"); 
	               
	                
	            	Picture p = new Picture(size,contenttype,picname,originalname,accesspath);
	            	
	            
	                
	                System.out.println("�ļ�����: " + size);   
	                System.out.println("�ļ�����: " + contenttype);   
	                System.out.println("�ļ�����: " + picname);   
	                System.out.println("�ļ�ԭ��: " + originalname);   
	                System.out.println("·��: " + accesspath);  
	                
	                picManager.savePic(p);
	                //���ﲻ�ش���IO���رյ����⣬��ΪFileUtils.copyInputStreamToFile()�����ڲ����Զ����õ���IO���ص�   
	                FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(accesspath, myfile.getOriginalFilename()));   
	            }   
	        }   
	          
	        return "success";  
	    }  catch(Exception e){
	    	return "failure"+e.getMessage();
	    }
	}  
	
	 
	
	@RequestMapping(value="/qiniu_getToken",method=RequestMethod.GET)
	public @ResponseBody JSONObject qnupload(HttpServletRequest request,HttpServletResponse response) throws IOException  
    {  
		String ACCESS_KEY = "-AFXSfkeqfsYJXNrUvb1_6z6RkdNGzLyBNy-F1WN";
	    String SECRET_KEY = "WplNvyvDCyl3p8U24svKwdoyU5SfWkYqf6iAEHdH";
	    String bucketname = "picmap";
	    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		String token = auth.uploadToken(bucketname);
		String tjson1 = "{\"token\":\""+ token + "\"}";
		System.out.println(token);
		JSONObject tjson = JSONObject.fromObject(tjson1);
		return tjson;
    }
}