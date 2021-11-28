<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>User administration page</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<%
	String todoStr = request.getParameter("todo");
	if ((todoStr != null) && (todoStr.equals("Add new user"))){
		String userName = request.getParameter("name");
		String userLogin = request.getParameter("login");
		//String userPassword = request.getParameter("password");
		String userRole = request.getParameter("role");
		String email = request.getParameter("email");
		String turnEmail = request.getParameter("turn_email");
		String gameEmail = request.getParameter("newgame_email");
		String adminEmail = request.getParameter("admin_email");
		UserHandler.addUser(userName, userLogin, userRole, email ,turnEmail, gameEmail, adminEmail, true);
	}else
	if ((todoStr != null) && (todoStr.equals("Delete player"))){
		String userName = request.getParameter("delete_name");
		UserHandler.deleteUser(userName);
	}
%>
<body background="images/spaze.gif">
<!-- <%= todoStr %> -->
<h2>User administration page</h2>
This page allows administrators to control the users.
<hr color="#FFBF00">
<form action="user_admin.jsp">
<h3>Add new user</h3>
<table>
<tr>
<td>Name:&nbsp;&nbsp;&nbsp;</td>
<td><input type="text" name="name" value=""></td>
</tr>
<tr>
<td>Login:&nbsp;&nbsp;&nbsp;</td>
<td><input type="text" name="login" value=""></td>
</tr>
<tr>
<td>Password:&nbsp;&nbsp;&nbsp;</td>
<td><input type="text" name="password" value=""></td>
</tr>
<tr>
<td>Role:&nbsp;&nbsp;&nbsp;</td>
<td>
 <select name="role">
  <option value="player">Player</option>
  <option value="admin">Administrator</option>
 </select>
</td>
</tr>
<tr>
<td>E-mail (separate multiple addresses with blanks):&nbsp;&nbsp;&nbsp;</td>
<td><input type="text" name="email" value=""></td>
</tr>
<tr>
<td>Turn email:&nbsp;&nbsp;&nbsp;</td>
<td><input type="checkbox" checked name="turn_email" value="true"></td>
</tr>
<tr>
<td>New game email:&nbsp;&nbsp;&nbsp;</td>
<td><input type="checkbox" checked name="newgame_email" value="true"></td>
</tr>
<tr>
<td>Admin email:&nbsp;&nbsp;&nbsp;</td>
<td><input type="checkbox" checked name="admin_email" value="true"></td>
</tr>
</table>
<br>
<input name="todo" type="submit" value="Add new user">
</form>
<p>
<hr color="#FFBF00">
<h3>Delete user</h3>
<form action="user_admin.jsp">
<table>
<tr>
<td>Choose player to delete:&nbsp;&nbsp;&nbsp;</td>
<td><select name="delete_name">
<%= UserHandler.getRemovableUsers() %>
</select></td>
</tr>
<tr>
<td colspan="2"><input name="todo" type="submit" value="Delete player"></td>
</tr>
</table>
</form>
<hr color="#FFBF00">
<h3>Edit user</h3>
Edit user information<p>
<a href="user_list.jsp">Edit User</a>
<hr color="#FFBF00">
</body>
</html>
