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
	String filename = request.getParameter("logname");
	String logtext = LogPrinter.getTomcatLog(filename);
%>
<body background="images/spaze.gif">
<h2><%= filename %></h2>
<%= logtext %>
</body>
</html>
