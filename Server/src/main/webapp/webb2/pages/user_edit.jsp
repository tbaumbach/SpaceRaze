<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Edit User page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<%
	String userLogin = request.getParameter("login");
	User aUser = UserHandler.findUser(userLogin);
	String selectAdmin = "";
	if (aUser.isAdmin()){
		selectAdmin = "selected";
	}
	String turnChecked = "";
	if (aUser.getRecieveMail(User.WANT_EMAIL_TURN)){
		turnChecked = " checked";
	}
	String gameChecked = "";
	if (aUser.getRecieveMail(User.WANT_EMAIL_GAME)){
		gameChecked = " checked";
	}
	String adminChecked = "";
	if (aUser.getRecieveMail(User.WANT_EMAIL_ADMIN)){
		adminChecked = " checked";
	}
	
%>
<body background="images/spaze.gif">
<h2>Edit User</h2>
This page allows administrators to edit a user.
<br>
<form action="user_saved.jsp">
<table>
<tr>
<td>Name:&nbsp;&nbsp;&nbsp;</td>
<td><input type="text" name="name" value="<%= aUser.getName() %>"></td>
</tr>
<tr>
<td>Login (can't be changed):&nbsp;&nbsp;&nbsp;</td>
<td><%= aUser.getLogin() %><input type="hidden" name="login" value="<%= aUser.getLogin() %>"></td>
</tr>
<tr>
<td>Password:&nbsp;&nbsp;&nbsp;</td>
<td><input type="text" name="password" value="<%= aUser.getPassword() %>"></td>
</tr>
<tr>
<td>Role:&nbsp;&nbsp;&nbsp;</td>
<td>
 <select name="role">
  <option value="player">Player</option>
  <option value="admin" <%= selectAdmin %>>Administrator</option>
 </select>
</td>
</tr>
<tr>
<td>E-mail (separate multiple addresses with blanks):&nbsp;&nbsp;&nbsp;</td>
<td><input type="text" name="email" value="<%= aUser.getEmails() %>"></td>
</tr>
<tr>
<td>Turn email:&nbsp;&nbsp;&nbsp;</td>
<td><input type="checkbox" name="turn_email" value="true"<%= turnChecked %>></td>
</tr>
<tr>
<td>New game email:&nbsp;&nbsp;&nbsp;</td>
<td><input type="checkbox" name="newgame_email" value="true"<%= gameChecked %>></td>
</tr>
<tr>
<td>Admin email:&nbsp;&nbsp;&nbsp;</td>
<td><input type="checkbox" name="admin_email" value="true"<%= adminChecked %>></td>
</tr>
</table>
<br>
<input name="todo" type="submit" value="Save User">
</form>
<p>
<a href="user_show.jsp?login=<%= aUser.getLogin() %>">Back to View User</a>
</body>
</html>
