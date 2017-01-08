package com.tgb.web;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UserTokenInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		//�����·��
        String contextPath=request.getContextPath();
		String url = request.getRequestURI();
		
		//�ж�token�Ƿ����ʱ�䣬������loginʱ�������session��setMaxInactiveInterval
		if(request.getSession(false) == null){
			PrintWriter out = response.getWriter();
			out.write("failure");
			out.flush();
//			request.getRequestDispatcher("http://127.0.0.1:8080/PicMap/user/login").forward(request, response);
			return false;
		};
		//�жϵ�ǰҳ���Ƿ�Ϊ��¼����ע�����
		if(url.indexOf("login") >= 0 || url.indexOf("register") >= 0){
			return true;
		}
		
		HttpSession session = request.getSession();
		String token = (String)session.getAttribute("globaltoken");
		System.out.println(token);
		if(null != token){
			return true;
		}else{
			//response.sendRedirect("http://127.0.0.1:8080/PicMap/user/login");
			PrintWriter out = response.getWriter();
			out.write("failure");
			out.flush();
			return false;
		}
		
		
	}

}
