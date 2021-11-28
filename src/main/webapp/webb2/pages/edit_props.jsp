<%@ page import="spaceraze.util.properties.PropertiesHandler" %>
<!DOCTYPE html>
<html>
<head>
<title>Edit properties file</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<%
	// get filename
	String fileName = request.getParameter("propname");
	// maybe save file 
	String todoStr = request.getParameter("todo");
	String message = null;
	if ((todoStr != null) && (todoStr.equals("Save"))){
		String newContent = request.getParameter("content");
		PropertiesHandler.saveFile(fileName,newContent);
		message = "File saved";
	}
	// get contents
	String propText = PropertiesHandler.getPropertiesContent2(fileName);
%>
<body background="images/spaze.gif">
<h2>Edit <%= fileName %></h2>
<% if (message != null){%>
<br>
<font color="#00FF00">
<%= message %>
</font>
<p>
<% } %>
<form action="edit_props.jsp">
<input type="hidden" name="propname" value="<%= fileName %>">
<textarea name="content" cols="80" rows="25"><%= propText %></textarea>
<br>
<input name="todo" type="submit" value="Save">
</form>

</body>
</html>
