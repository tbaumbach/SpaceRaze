<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>User Saved</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<%
	String userName = request.getParameter("name");
	String userLogin = request.getParameter("login");
	String userPassword = request.getParameter("password");
	String userRole = request.getParameter("role");
	String email = request.getParameter("email");
	String turnEmail = request.getParameter("turn_email");
	String gameEmail = request.getParameter("newgame_email");
	String adminEmail = request.getParameter("admin_email");
	ServerHandler sh = (ServerHandler)application.getAttribute("serverhandler");
	UserHandler.saveUser(userName,userLogin,userPassword,userRole,email,turnEmail,gameEmail,adminEmail,sh);
%>

<body background="images/spaze.gif">
<h2>User Saved</h2>
User data has been saved.<p>
<a href="user_show.jsp?login=<%= userLogin %>">Back to View User page</a>
</body>
</html>
