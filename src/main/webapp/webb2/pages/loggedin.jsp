<!-- Start loggedin.jsp fragment -->

<%@page import="sr.webb.users.UserHandler"%>
<%@page import="sr.webb.users.User"%>
<%
User userLoggedin = session.getAttribute("user") != null ? (User)session.getAttribute("user") : UserHandler.getUser(session,request,response);
%>
<form id="loggedinform" name="loggedinform" action="Master.jsp">
<input type="hidden" name="action" value="logout">
<table class="ListTable">
	<tr><td><img src='/images/space.gif' width='1' height='15'></td>
	</Tr>
	<tr>
		<td width=683></td>
		<td width=50><span class=MenuMain>Player:</span></td>
		<td width=97><span class=MenuMain><%= userLoggedin.getName() %></span></td>
	</Tr>
	<tr>
	<td></td>
	<td	colspan=2><span class=MenuMain><a href="default.jsp?action=manage_user">Manage Account</a></span></td>
	</Tr>
	<tr>
		<td></td>
		<td></td>
		<td align=right>
			<input type="image" src="images\btn_logout.jpg">
		</td>
	</Tr>
</table>	
</form>
<!-- End loggedin.jsp fragment -->
