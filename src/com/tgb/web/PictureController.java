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
	      //如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解   
	        //如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解   
	        //并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件   
	  
		 
		 try{
	        for(MultipartFile myfile : myfiles){   
	            if(myfile.isEmpty()){   
	                System.out.println("文件未上传");   
	                return "文件未上传";
	            }else{   
	            	long size = myfile.getSize();
	            	String contenttype = myfile.getContentType();
	            	String picname = myfile.getName();
	            	String originalname = myfile.getOriginalFilename();
	            	
	            	//如果用的是Tomcat服务器，则文件会上传到  {服务发布位置}\\WEB-INF\\picture\\文件夹中 ，绝对路径
	                String accesspath = request.getSession().getServletContext().getRealPath("/picture"); 
	               
	                
	            	Picture p = new Picture(size,contenttype,picname,originalname,accesspath);
	            	
	            
	                
	                System.out.println("文件长度: " + size);   
	                System.out.println("文件类型: " + contenttype);   
	                System.out.println("文件名称: " + picname);   
	                System.out.println("文件原名: " + originalname);   
	                System.out.println("路径: " + accesspath);  
	                
	                picManager.savePic(p);
	                //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉   
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