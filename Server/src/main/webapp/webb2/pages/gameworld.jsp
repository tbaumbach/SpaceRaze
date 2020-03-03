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

<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">


<body background="images/spaze.gif">
<div style="left: 130px;width: 718px;position: absolute;top: 88px;">	
	<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b>Gameworld <%= gw.getFullName() %></b></div></div>
	<div class="Form_Text" style="width:718">
		<div class="SolidText">
			Filename: <%= gw.getFileName() %><br>
			Description: <%= gw.getDescription() %><br>
			Created by user: <%= gw.getCreatedByUser() %><br>
			Created date: <%= gw.getCreatedDate() %><br>
			Changed date: <%= gw.getChangedDate() %><br>
			Cumulative bombardment: <%= gw.isCumulativeBombardment() %><br>
			Initiative method: <%= gw.getInitMethod().toString() %><br>
		</div>
	</div>


	<div class="Form_Header" style="width:718"><div class="SolidText"><b>Neutral ships</b></div></div>
	<div class="Form_Text" style="width:718">
		<div class="SolidText">
			<!-- Neutral ships -->
			Neutral size 1 shiptype: <%= gw.getNeutralSize1().getName() %><br>
			Neutral size 2 shiptype: <%= gw.getNeutralSize2().getName() %><br>
			Neutral size 3 shiptype: <%= gw.getNeutralSize3().getName() %><br>
		</div>
	</div>

	<div class="Form_Header" style="width:718"><div class="SolidText"><b>BattleSim default ships</b></div></div>
	<div class="Form_Text" style="width:718">
		<div class="SolidText">
			<!-- Battlesim default ship string -->
			Side A: <%= gw.getBattleSimDefaultShips1() %><br>
			Side B: <%= gw.getBattleSimDefaultShips2() %><br>
		</div>
	</div>

	<div class="Form_Header" style="width:718" align='right'><div class="SolidText"><b>
		<a href="Master.jsp?action=gameworld_buildings&gameworldfilename=<%=gameWorldFileName%>">Buildings</a>&nbsp;&nbsp;&nbsp;
		<a href="Master.jsp?action=gameworld_Spaceships&gameworldfilename=<%=gameWorldFileName%>">Spaceships</a>&nbsp;&nbsp;&nbsp;
		<a href="Master.jsp?action=gameworld_factions&gameworldfilename=<%=gameWorldFileName%>">Factions</a>&nbsp;&nbsp;&nbsp;
		<a href="Master.jsp?action=gameworld_vips&gameworldfilename=<%=gameWorldFileName%>">VIPS</a>&nbsp;&nbsp;&nbsp;
	</b></div></div>
	<div class="List_End"></div>	
</div>

</body>
</html>
