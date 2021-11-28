<%@ page import="spaceraze.util.properties.PropertiesHandler" %>
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
	String filename = request.getParameter("propname");
	String proptext = PropertiesHandler.getPropertiesContent(filename);
%>
<body background="images/spaze.gif">
<h2><%= filename %></h2>
<p>
<%= proptext %>
<p>
<a href="edit_props.jsp?propname=<%= filename %>">Edit file</a>
</body>
</html>
