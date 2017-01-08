package com.tgb.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import net.sf.json.JSONObject;  

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;









import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiniu.util.Auth;
import com.tgb.entity.LoginError;
import com.tgb.entity.PassWord;
import com.tgb.entity.Token;
import com.tgb.entity.User;
import com.tgb.manager.UserManager;

@Controller
public class UserController {

	@Resource(name="userManager")
	private UserManager userManager;
	
	//���Ǵ���ǰ�˷����ĵ�¼��post���󣬱���Ϊpost������Ȼ�������Ǳ����ݣ�����ֻ��ҪgetParameter�Ϳ���
	//�õ����ݣ������Ǳ������@ResponseBody������ǰ����ʾ404����Ҳ�����Ҳ���·������Ӧ���ǿ�ܵ�����
	//����ֵ��Ϊ����ΪToken����LoginError���ͣ����Ͳ�̫���ã����Է���Object���������Կ���ת��Ϊjson
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	public @ResponseBody JSONObject loginUser(HttpServletRequest request){
		String username = request.getParameter("mUserName");
		String pass = request.getParameter("mPassWord");
		User checkuser = userManager.getUserByTel(username);
		if(checkuser==null) {
			//101���ֻ���δע����Ĵ���
			LoginError error = new LoginError(101);
			System.out.println("user��û��ע���أ�");
			System.out.println(username+","+pass);
			User u = new User();
			u.setmMessage("101:��û��ע����");
			return JSONObject.fromObject(u);
		}
		else {
			//����һ����ʾ��¼�ɹ�������ǰ��json��Token
			if((checkuser.getmPassWord()).equals(pass)) {
				
				Token token = new Token(checkuser.getmUserId());
				HttpSession session = request.getSession();
				session.setAttribute("globaltoken", token.getToken());
				//���û���ID��ΪaccessToken��һ��ʱ��֮��ʧЧ
				session.setMaxInactiveInterval(900); 
				System.out.println("��¼�ɹ�������һ����");
				System.out.println(username+","+pass);
				checkuser.setmMessage("success");
				return JSONObject.fromObject(checkuser);
			}
			else {
				//102������͸��û������ݿ������Բ��ϵĴ�
				LoginError error = new LoginError(102);
				System.out.println("������������˰ɣ�");
				System.out.println(username+","+pass);
				checkuser.setmMessage("102:�����������");
				return JSONObject.fromObject(checkuser);
			}
		}
		//JSONObject obj = new JSONObject().fromObject(json);
	}
	

	
	//����ǰ�˵�ע�����󣬺�loginһ������post
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	public @ResponseBody JSONObject registerUser(HttpServletRequest request){
		System.out.println("��û��ע�᣿");
		String name = request.getParameter("mUserName");
		String pass = request.getParameter("mPassWord");
		String email = request.getParameter("mEmail");
		String tel = request.getParameter("mPhone");
		User checkuser = userManager.getUserByTel(tel);
		
		
		if(checkuser!=null) {
			//100----�û��Ѿ�������ֻ���ע����Ĵ���
			LoginError error = new LoginError(100);
			checkuser.setmMessage("100:�û��ֻ�����ע���");
			return JSONObject.fromObject(checkuser);
		}
		else {
			User adduser = new User();
			adduser.setmEmail(email);
			adduser.setmUserName(name);
			adduser.setmPassWord(pass);
			adduser.setmPhone(tel);
			userManager.addUser(adduser);
			adduser.setmMessage("success");
			System.out.println(adduser.getmUserId() + "--" + adduser.getmUserId());
			//����ID��ʾע��ɹ�
			return JSONObject.fromObject(adduser);
		}
	}
	
	
	//�ϴ��û�ͷ��
	@RequestMapping(value="/user/upimageurl",method=RequestMethod.POST)
	public @ResponseBody JSONObject upImageUrl(HttpServletRequest request){
		String url = request.getParameter("mHeadImageUrl");
		String id = request.getParameter("mUserId");
		User u_check = userManager.getUserById(id);
		if(u_check!=null) {
			u_check.setmHeadImageUrl(url);
			u_check.setmMessage("ͷ���ϴ��ɹ�");
			return JSONObject.fromObject(u_check);
		}
		else {
			User u_fail = new User();
			u_fail.setmMessage("�ϴ�ͷ��ʧ�ܣ������ڸ��û�");
			return JSONObject.fromObject(u_fail);
		}
	}
	
	
	
	
	@RequestMapping(value="bjws/app.user/login",method=RequestMethod.GET)
	public boolean bjwsUserLogin(HttpServletRequest request){
			return true;
	}
	@RequestMapping(value="bjws/app.menu/getMenu",method=RequestMethod.GET)
	@ResponseBody
	public User bjwsUserGetMenu(){
		User user = new User();
		user.setmPhone("111");
		user.setmPassWord("222");
			
		return user;
		
	}
	

	@RequestMapping("/user/getAllUser")
	public String getAllUser(HttpServletRequest request){
		
		request.setAttribute("userList", userManager.getAllUser());
		
		return "/index";
	}
	
	@RequestMapping("/user/getUser")
	public String getUser(String id,HttpServletRequest request){
		
		request.setAttribute("user", userManager.getUser(id));
	
		return "/editUser";
	}
	
	@RequestMapping("/user/toAddUser")
	public String toAddUser(){
		return "/addUser";
	}
	
	@RequestMapping("/user/addUser")
	public String addUser(User user,HttpServletRequest request){
		
		userManager.addUser(user);
		
		return "redirect:/user/getAllUser";
	}
	
	@RequestMapping("/user/delUser")
	public void delUser(String id,HttpServletResponse response){
		
		String result = "{\"result\":\"error\"}";
		
		if(userManager.delUser(id)){
			result = "{\"result\":\"success\"}";
		}
		
		response.setContentType("application/json");
		
		try {
			PrintWriter out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/user/updateUser")
	public String updateUser(User user,HttpServletRequest request){
		
		if(userManager.updateUser(user)){
			user = userManager.getUser(user.getmUserId());
			request.setAttribute("user", user);
			return "redirect:/user/getAllUser";
		}else{
			return "/error";
		}
	}
	
	@RequestMapping("/user/json")
	public String json(HttpServletRequest request){
		
		request.setAttribute("userList", userManager.getAllUser());
		
		return "/json";
	}
	
	
	
	
	
	//�����ʼ����û�
	@RequestMapping(value="/user/passwordMail",method=RequestMethod.POST)
	public @ResponseBody JSONObject passwordMail(HttpServletRequest request) throws MessagingException, FileNotFoundException, IOException{
		System.out.println("email enter");
		//�ж���ϵ�˴������
		String email = request.getParameter("mEmail");
		String userid = userManager.checkMail(email);
		System.out.println(userid+"wwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
		if(userid == null || userid.equals("")){
			return JSONObject.fromObject("no this user");
		}
		
		//��url���û�����password���ݴ�����
		PassWord p = new PassWord(email,userid);
		userManager.addPassWord(p);
		
		Properties props = new Properties();
		props.setProperty("mail.host", "smtp.163.com");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");
		props.put("mail.smtp.port","25");
		Authenticator authenticator = new MailAuthenticator("wang6223323@163.com", "QWEDFGHasd111");
		Session session = Session.getInstance(props,authenticator);
		session.setDebug(true);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("WANG6226693@163.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(email));
		message.setSubject("�޸�����!");
		message.setText("����뵽�����ַ�޸��������:" + p.getUrl());
          
		Transport ts = session.getTransport();
		
		ts.connect("smtp.163.com", "WANG6226693", "QWEDFGHasd111");
		
		
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();
		return  JSONObject.fromObject("{\"mMessage\":\"success\"}");

	}
	
	class MailAuthenticator extends Authenticator {
	    String userName = null;
	    String password = null;
	    public MailAuthenticator() {
	    }
	    public MailAuthenticator(String username, String password) {
	        this.userName = username;
	        this.password = password;
	    }
	    protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication(userName, password);
	    }
	}
	
	public static MimeMessage createImageMail(Session session) throws AddressException, MessagingException, FileNotFoundException, IOException{
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("1021689380@qq.com"));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress("524924155@qq.com"));
		message.setSubject("picmap����ظ�");
		
		MimeBodyPart text = new MimeBodyPart();
		int check = (int)(Math.random()*(9999-1000+1))+1000;
		text.setContent("������֤����:"+check, "text/html;charset=UTF-8");
		
		MimeBodyPart image = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource("src\\1.jpg"));
		image.setDataHandler(dh);
		image.setContentID("1.jpg");
		
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text);
		mm.addBodyPart(image);
		mm.setSubType("related");
		  
		message.setContent(mm);
		message.saveChanges();
		//�������õ��ʼ�д�뵽E�����ļ�����ʽ���б���
		message.writeTo(new FileOutputStream("E:\\ImageMail.eml"));
		return message;
		
		
	}
	
	//��Ӧ�û�����ʼ�֮�������
	@RequestMapping(value="/user/changepassword/{userId}",method=RequestMethod.GET)
	public String changePassWord(HttpServletRequest request,@PathVariable String userId){
		
		PassWord p = new PassWord();
		p.setUrl(request.getRequestURL().toString());
		p.setUserId(userId);
		if(!userManager.getUserByPassWord(p)) {
			return "/error";
		}
		
		request.setAttribute("mUserId",userId);
		return "/changepassword";
	}
	
	//��Ӧ�û�����ʼ�֮�������
	@RequestMapping(value="/user/updatepassword",method=RequestMethod.POST)
	public String updatePassword(HttpServletRequest request){
		String ps = request.getParameter("password1");
		String ps2 = request.getParameter("password2");
		
		String userid = request.getParameter("mUserId");
		
		System.out.println(ps+"qqqqqqqqqq"+ps2);
		//�����û������ͬʱɾ��password����url
		userManager.updatePassWord(userid,ps);
		
		
		return "/success";
	}
	
}