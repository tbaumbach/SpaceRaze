<!-- Start loggedin.jsp fragment -->

<form id="loggedinform" name="loggedinform" action="Master.jsp">
<input type="hidden" name="action" value="logout">
<table width='840' border='0' cellspacing='0' cellpadding='0'>
	<tr><td><img src='/images/space.gif' width='1' height='15'></td>
	</Tr>
	<tr>
		<td width=683></td>
		<td width=50><span class=MenuMain>Player:</span></td>
		<td width=97><span class=MenuMain><%= theUser.getName() %></span></td>
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

<!-- End loggedin.jsp fragment -->
