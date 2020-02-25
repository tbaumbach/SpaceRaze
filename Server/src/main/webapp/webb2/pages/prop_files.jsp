<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.properties.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>View prop files</title>
<meta charset="UTF-8">
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
