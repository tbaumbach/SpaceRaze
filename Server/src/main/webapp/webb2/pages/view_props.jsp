<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.properties.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.amarantin.imagepack.*"%>
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
	ImageText it = new ImageText("buttonimage");
	it.setAttributes(" border=\"0\" ");
	String filename = request.getParameter("propname");
	String proptext = PropertiesHandler.getPropertiesContent(filename);
%>
<body background="images/spaze.gif">
<h2><%= filename %></h2>
<p>
<%= proptext %>
<p>
<a href="edit_props.jsp?propname=<%= filename %>"><%= it.getTagAndImage("Edit file") %></a>
</body>
</html>
