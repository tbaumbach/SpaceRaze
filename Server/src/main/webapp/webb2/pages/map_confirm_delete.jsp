<%@ page import="spaceraze.world.*"%>
<%@ page import="sr.server.map.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Delete Map</title>
<meta charset="UTF-8">
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
