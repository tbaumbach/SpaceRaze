<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="spaceraze.world.*"%>
<%@ page import="sr.server.properties.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Create new maps</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<body background="images/spaze.gif">
<h2>Create New Map Files</h2>
To create a new map, a properties text file must be created, where data is specified in 
<i>key</i> = <i>data</i> pairs.<br>
The name of the text file must be <i>mapname</i>.properties.<br>
Planets, planet connections, max number of planets and some information about the map must be specified in the file.<br>
See this <a href="test.properties.txt">example map file</a> (the <a href="map_view.jsp?mapname=test">test</a> map) for how to do it.<p>

When you are finished, e-mail the map to a SpaceRaze administrator so he can test it and deploy it.<p>

<a href=map_files.jsp>Back to map list</a><br>
</body>
</html>
