<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String mId  = (String)request.getAttribute("mUserId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh-CN">
  <head>
  	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>My JSP 'changepassword.jsp' starting page</title>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/js/bootstrap.min.css">
	<script src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
	<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
	
	<script type="text/javascript">
		window.onload=function(){
			document.getElementById("ps").disabled = true;
		};
		function validate() {
			var pw1 = document.getElementById("password1").value;
			var pw2 = document.getElementById("password2").value;
            if(pw1 == pw2) {
            	document.getElementById("tishi").innerHTML="<font color='green'>两次密码相同</font>";
            	document.getElementById("ps").disabled = false;
            } else {
                document.getElementById("tishi").innerHTML="<font color='red'>两次密码不相同</font>";
                document.getElementById("ps").disabled = true;
              }
          }
         
	</script>
  </head>
  
  <body>
	<form role="form" method="post" action="/PicMap/user/updatepassword">
 		<div class="form-group">
    		<label for="password1">修改后密码</label>
    		<input type="password" class="form-control"name="password1" id="password1" placeholder="Password">
 		</div>
		<div class="form-group">
			<label for="password2">请重复密码</label>
		    <input type="password" class="form-control" name="password2" id="password2" placeholder="Password" onkeyup="validate()"">
			<span id="tishi"></span>
		</div>
		<input type="hidden" name="mUserId" value="<%=mId %>"/>
		<input type="submit" id="ps" class="btn btn-success" value= "  提交    "></button>
		
	</form>
  </body>
  
  
  <!--<script src="/js/changepassword.js"></script>  -->
  

</html>
