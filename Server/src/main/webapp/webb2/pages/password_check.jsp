<%@ page import="sr.server.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Check protected game password page</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%
	String gameId = request.getParameter("port");
	String returnPage = request.getParameter("returnto");
	String gamePassword = request.getParameter("game_password");
	
	ServerHandler sh = (ServerHandler)application.getAttribute("serverhandler");
	
	boolean showErrorPage = true;
	String redirectString = "";
	if (sh.checkProtectedGamePassword(gamePassword,gameId)){
		showErrorPage = false;
	}
	

	if (showErrorPage){
%>
<div style="left: 129px;width: 450px;position: absolute;top: 89px;padding-bottom:20px;">	
	<div class="Form_Name" style="width:450px"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:450px"><div class="SolidText"><b>Game Protected by password</b></div></div>
	<div class="Form_Text" style="width:450px"><div class="SolidText">
<table class="ListTable">
<tr>
<td>
<h2>Wrong Password</h2>
The password entered did not match the password for this game.
</td>
</tr>
<tr>
<td>
&nbsp;
</td>
</tr>
<tr>
<td>
<br>
</td>
</tr>
</table>
</div></div>
	<div class="Form_Header" ALIGN=RIGHT>
				<div class="SolidText"><A href="Master.jsp?action=games_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_continue.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_continue.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_continue.jpg" vspace=0 border=0></A></div>
	</div>
	<div class="List_End"></div>		
			
</div>
<%
	}
%>

