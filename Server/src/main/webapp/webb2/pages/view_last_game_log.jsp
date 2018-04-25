<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Server administration page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<%
	int gameid = Integer.parseInt(request.getParameter("gameid"));
	ServerHandler sh = (ServerHandler)application.getAttribute("serverhandler");
	SR_Server aServer = sh.findGame(gameid);
	String logtext = aServer.getLastLog();
%>
<body background="images/spaze.gif">
<h2>Last log for game <%= aServer.getGameName() %></h2>
<%= logtext %>
</body>
</html>
