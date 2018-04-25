<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.mail.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Edit properties file</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<%
	// maybe send mail 
	String todoStr = request.getParameter("todo");
	String message = null;
	if ((todoStr != null) && (todoStr.equals("Send"))){
		String newSubject = request.getParameter("subject");
		String newContent = request.getParameter("content");
		MailHandler.sendAdminMessage(newSubject,newContent,tmpUser);
		message = "Mail sent";
	}
%>
<body background="images/spaze.gif">
<h2>Send mail to players</h2>
Use this page to send an email to all players who want to recieve messages from 
the administrators of SpaceRaze.<br>
<% if (message != null){%>
<br>
<%= message %>
<p>
<% } %>
<form action="admin_mail.jsp">
<input type="text" name="subject" value="">
<br>
<textarea name="content" cols="80" rows="15"></textarea>
<br>
<input name="todo" type="submit" value="Send">
</form>

</body>
</html>
