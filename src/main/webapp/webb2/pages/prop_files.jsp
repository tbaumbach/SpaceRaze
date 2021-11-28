<%@ page import="spaceraze.util.properties.PropertiesHandler" %>
<!DOCTYPE html>
<html>
<head>
<title>View prop files</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="//css/styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<body style="background-image: url('../images/SpacerazeBG.jpg')">
<h2>View Properties Files</h2>
<br>
<hr style="color:#FFBF00">
<%= PropertiesHandler.getPropertyFiles() %>
<hr style="color:#FFBF00">
</body>
</html>
