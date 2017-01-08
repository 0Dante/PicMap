<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>  
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
    <head>  
        <script src="jquery-1.3.2.min.js"> 
        </script>  
        <script src="json2.js">  
        </script>  
        <script>  
            function tokenuser(mPhone, password){  
                this.mPhone = mPhone;  
                this.password = password;  
            }  
              
           function sendAjax4(){  
                var jsonObject={  
                    "tel":"tom",  
                    "password":"123"  
                 } ;
                var jsonString=JSON.stringify(jsonObject);  
                  $.ajax({  
                        type: "POST",  
                        data: jsonString,  
                        url: "http://localhost:8080/PicMap/user/login",  
                        contentType: "application/json"  
                    });  
            }
        </script>  
    </head>  
    <body>  
        <input type="button" onclick="sendAjax4()"/>  
   
    </body>  
</html>  