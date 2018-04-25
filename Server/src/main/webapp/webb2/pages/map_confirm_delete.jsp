<%@ page import="sr.webb.*"%>
<%@ page import="sr.mapeditor.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.world.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="sr.server.properties.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Delete Map</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">

<style type="text/css">
table.sr td {
	border-width: 0px;
	padding: 0px;
	background-color: #552500;
}
</style>

</head>
<%@ include file="checklogin2.jsp" %>
<%
	String mapName = request.getParameter("mapname");
	Map aMap = MapHandler.getMap(mapName);
%>
<body background="images/spaze.gif">
<!-- <%= mapName %> -->
<h2>Delete Map?</h2>
Do you want to delete the map <%= aMap.getNameFull() %> (<%= aMap.getFileName() %>)?
<p>
<a href="map_files.jsp?action=delete&mapname=map.<%= mapName %>">Yes, delete it.</a>
<p>
<a href="map_files.jsp">No, go back.</a>
</body>
</html>
