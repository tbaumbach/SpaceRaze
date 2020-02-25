<%@page import="spaceraze.servlethelper.handlers.GameWorldHandler"%>
<%@ page import="sr.server.*"%>
<%@ page import="spaceraze.world.*"%>

<!DOCTYPE html>
<html>
<head>
<title>GameWorld page</title>
<meta charset="UTF-8">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">

<style type="text/css">
table.sr td {
	border-width: 0px;
	padding: 0px;
	background-color: #552500;
}
</style>

</head>
<%
	String gameWorldFileName = request.getParameter("gameworldfilename");
	GameWorld gw = GameWorldHandler.getGameWorld(gameWorldFileName);
%>


<script>
function OnMouseOver(Control)
{
	Control.className = "OnMouseOver";	
	//document.getElementById('PageAction').innerHTML = 'Click on a Gameworld to see detailed information!';	
}
function OnMouseOut(Control)
{
	Control.className = "TRMain";	
	//document.getElementById('PageAction').innerHTML = '';
}
</script>

<body background="images/spaze.gif">

<div style="left: 130px;width: 1650px;position: absolute;top: 88px;">	
	<div class="Form_name" style="width:1650"><div class="SolidText">Spaceraze</div></div>
	<div class="Form_Header" style="width:1650"><div class="SolidText"><b><%= gw.getFullName() %> - Spaceship Types</b></div></div>
	<div class="Form_Text" style="width:1650">
		<div class="SolidText">

<!-- Spaceships list -->
<table border="0" width="1650" cellspacing="1" cellpadding="1" class="MenuMain">
  <% //= gw.getSpaceshipTypesTableContentHTMLNO() %>
  <tr class='ListMain' height='3'><td colspan='38'></td></tr>
<tr class='ListHeadLeft' height='20'><td colspan='38'> <div class='ListHeadLeft' id="PageAction" name="PageAction"></div></td></tr>
</table>
</div></div>
<div class="List_End"></div>	
</div>
</body>
</html>
