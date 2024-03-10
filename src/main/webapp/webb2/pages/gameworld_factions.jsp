<%@page import="spaceraze.servlethelper.handlers.GameWorldHandler"%>
<%@ page import="sr.server.*"%>
<%@ page import="spaceraze.world.*"%>
<%@ page import="spaceraze.webb.support.world.*"%>

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
<div style="left: 130px;width: 718px;position: absolute;top: 88px;">	
	<div class="Form_Name" style="width:718"><div class="SolidText">Spaceraze</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b><%= gw.getFullName() %> - Factions</b></div></div>
	<div class="Form_Text" style="width:718">
		<div class="SolidText">
<B>	Factions</B>
	</div></div>
		<div class="List" style="width:718">
<table class="ListTable">
	<!--%String factionTable = new GameWorldHelper(gw).getFactionsTableContentHTMLNO(); %-->
  <%= factionTable%>
</table>
  </div>
		<div class="List_End"></div></div>


</body>
</html>
