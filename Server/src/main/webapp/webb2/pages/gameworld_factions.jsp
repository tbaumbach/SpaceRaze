<%@ page import="sr.server.*"%>
<%@ page import="sr.world.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>GameWorld page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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


<script language="javascript">
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
	<div class="Form_name" style="width:718"><div class="SolidText">Spaceraze</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b><%= gw.getFullName() %> - Factions</b></div></div>
	<div class="Form_Text" style="width:718">
		<div class="SolidText">
<B>	Factions</B>
	</div></div>
		<div class="List" style="width:718">
<table border="0" width="716" cellspacing="0" cellpadding="0" class="">
  <%= gw.getFactionsTableContentHTMLNO() %>
</table>
  </div>
		<div class="List_End"></div></div>


</body>
</html>
