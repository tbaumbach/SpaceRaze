<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>

<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">

<%
	String userListHTML = UserHandler.getEditableUserListNO();
%>
<body background="images/spaze.gif">

<div style="left:130px;width:718px;position: absolute;top: 88px;">
		<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>Admin Users</b></div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText"><h2>Choose User to edit</h2> <br> This page allows administrators to choose which user to edit.<p></div></div>
		

		<div class="List" style="width:718;">
			<%= userListHTML %>
		</div>

		<div class="List_End"></div>	
<br><br><br><br><br>		
</div>
