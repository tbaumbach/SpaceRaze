<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.properties.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>View prop files</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<%
	PropertiesHandler ph = new PropertiesHandler();
%>
<body background="images/spaze.gif">
<h2>View Properties Files</h2>
<br>
<hr color="#FFBF00">
<%= ph.getPropertyFiles() %>
<hr color="#FFBF00">
</body>
</html>
