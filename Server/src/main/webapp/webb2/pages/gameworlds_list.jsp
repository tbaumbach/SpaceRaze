<%@page import="spaceraze.webb.support.world.GameWorldHelper"%>
<%@ page import="sr.server.*"%>

<!DOCTYPE html>
<html>
<head>
<title>GameWorlds page</title>
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

<script>
function OnMouseOver(Control)
{
	Control.className = "OnMouseOver";	
	document.form1.PageAction.innerHTML = 'Click on a Gameworld to see detailed information!';	
}
function OnMouseOut(Control)
{
	Control.className = "TRMain";	
	document.form1.PageAction.innerHTML = '';
}
</script>


<body background="images/spaze.gif">

<div style="left:130px;width:718px;position: absolute;top: 89px;">
		<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>Gameworlds</b>&nbsp;</div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
			Published Gameworlds - Read all about the gameworlds.
			
		</div></div>

<div class="List" style="width:718">
<table class="ListTable MenuMain">
<%= GameWorldHelper.getGameWorldsTableContentHTMLNO() %>
<tr class='ListTextRow' height='3'><td colspan='10' class='ListText'></td></tr>
<tr class='ListTextRow' height='20'><td colspan='10' class='ListText'> <div class='ListHeadLeft' id="PageAction"></div></td></tr>
</table>
</div>
		<div class="Form_Header" ALIGN="RIGHT" style="width:718"><div class="SolidText"></div>
			<div class="SolidText"><A href="Master.jsp?action=gameworlds_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_refreshlist.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_refreshlist.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_refreshlist.jpg" vspace=0 border=0></A></div>
		</div>
		<div class="List_End"></div>	
</div>
</body>
</html>
