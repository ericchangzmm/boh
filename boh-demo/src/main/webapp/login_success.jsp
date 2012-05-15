<%@page import="com.jije.boh.core.memsession.MemSessionUtil"%>
<%@page import="com.jije.boh.demo.dto.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>good</title>
</head>
<body>
<%
	UserInfo userInfo = (UserInfo)MemSessionUtil.getAttribute(request, "current_user");
if(userInfo != null)
{
	out.println("hello " + userInfo.getName());
}
else
{
	out.println("you have not logged in !");
}
%>

<center>
<form action="LogoutAction" method="post">
<input type="submit" value="logout"/><br/>
</form>
</center>
</body>
</html>