<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Server administration page</title>
<meta charset="UTF-8">
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
