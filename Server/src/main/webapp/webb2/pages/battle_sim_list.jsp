<%@page import="spaceraze.webb.support.world.GameWorldHelper"%>
<%@ page import="sr.server.*"%>

<!DOCTYPE html>
<html>
<head>
<title>GameWorlds page</title>
<meta charset="UTF-8">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">
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
<form name="form1" id="form1">

<div style="left:130px;width:718px;position: absolute;top: 89px;">
		<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>BattleSim - Gameworlds List</b>&nbsp;</div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
			Battle Simulator - Select gameworld for the battle simulator
		</div></div>


		<div class=List>
			<table style="width:716" class="ListTable MenuMain">
				<%= GameWorldHelper.getGameWorldsTableContentHTMLNO_BattleSim() %>
				<tr class='ListTextRow' height='3'><td colspan='10' class='ListText'></td></tr>
				<tr class='ListTextRow' height='20'><td colspan='10' class='ListText'> <div class='ListHeadLeft' id="PageAction"></div></td></tr>
			</table>
		</div>
		
		<div class="Form_Header" ALIGN="RIGHT" style="width:718"><div class="SolidText"></div>
			<div class="SolidText"><A href="Master.jsp?action=battle_sim_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_refreshlist.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_refreshlist.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_refreshlist.jpg" vspace=0 border=0></A></div>
		</div>
		<div class="List_End"></div>	
</div>
</form>
</body>
</html>
