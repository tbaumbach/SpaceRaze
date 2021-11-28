<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Download save files</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<%
	SavesHandler sh = new SavesHandler();
%>
<body background="images/spaze.gif">
<h2>Download Save Files</h2>
In IE6: Right-click and choose "Save Target As...".<br>
Important: Make sure saves files have the suffix ".srg". 
IE will suggest the suffix ".htm".<br>
<br>
<hr color="#FFBF00">
<h3>Active games</h3>
<%= sh.getActiveSaves() %>
<br>
<hr color="#FFBF00">
<h3>Backup files</h3>
<%= sh.getPreviousSaves() %>
<br>
<hr color="#FFBF00">
</body>
</html>
