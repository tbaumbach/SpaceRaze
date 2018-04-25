<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.general.logging.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Server administration page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<body background="images/spaze.gif">
<h2>Tomcat logs page</h2>
View Tomcat logs on the server.<p>
<p>
<%= LoggingHandler.getLogFilesString() %>
</body>
</html>
