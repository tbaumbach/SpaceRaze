<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>

<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">

<%
	String userLogin = request.getParameter("login");
	User aUser = UserHandler.findUser(userLogin);
/*	String userName = request.getParameter("name");
	String userPassword = request.getParameter("password");
	String userRole = request.getParameter("role");
	String email = request.getParameter("email");
	String turnEmail = request.getParameter("turn_email");
	String gameEmail = request.getParameter("newgame_email");
	String adminEmail = request.getParameter("admin_email");
*/
%>

<div style="left:130px;width:718px;position: absolute;top: 88px;">
		<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>Admin Users</div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText"><h2>Choose User to edit</h2> <br> This page allows administrators to choose which user to edit.<p><br>

<h2>View User Data</h2>
This page allows administrators to view the data of a user.

<!--form action="user_edit.jsp"-->
<h3>Add new user</h3>
<table>
<tr>
<td>Name:&nbsp;&nbsp;&nbsp;</td>
<td><%= aUser.getName() %></td>
</tr>
<tr>
<td>Login:&nbsp;&nbsp;&nbsp;</td>
<td><%= aUser.getLogin() %></td>
</tr>
<tr>
<td>Password:&nbsp;&nbsp;&nbsp;</td>
<td><%= aUser.getPassword() %></td>
</tr>
<tr>
<td>Role:&nbsp;&nbsp;&nbsp;</td>
<td><%= aUser.getRole() %></td>
</tr>
<tr>
<td>E-mail (separate multiple addresses with blanks):&nbsp;&nbsp;&nbsp;</td>
<td><%= aUser.getEmails() %></td>
</tr>
<tr>
<td>Turn email:&nbsp;&nbsp;&nbsp;</td>
<td><%= aUser.getRecieveMail(User.WANT_EMAIL_TURN) %></td>
</tr>
<tr>
<td>New game email:&nbsp;&nbsp;&nbsp;</td>
<td><%= aUser.getRecieveMail(User.WANT_EMAIL_GAME) %></td>
</tr>
<tr>
<td>Admin email:&nbsp;&nbsp;&nbsp;</td>
<td><%= aUser.getRecieveMail(User.WANT_EMAIL_ADMIN) %></td>
</tr>
</table>
<br>
<p>
<a href="pages/user_edit.jsp?login=<%= aUser.getLogin() %>">Edit User</a>
<p>
<a href="pages/user_list.jsp">Back to User List</a>
<!--/form-->
</div></div>

		<div class="List_End"></div>	
<br><br><br><br><br>	

