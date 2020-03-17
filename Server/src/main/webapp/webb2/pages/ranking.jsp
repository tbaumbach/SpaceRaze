<%@page import="spaceraze.util.properties.RankingHandler"%>
<%@page import="spaceraze.util.general.RankedPlayer"%>
<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Player ranking page</title>
<meta charset="UTF-8">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">
</head>

<script src="JS/MouseOver.js"></script>

<%
	RankedPlayer[] players = RankingHandler.getRanking();
%>
<body background="images/spaze.gif">

<div style="left:130px;width:718px;position: absolute;top: 88px;">

		<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>SpaceRaze Rankings</b>&nbsp;</div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
		
		
Ranking score is calculated by counting the number of defeated opponents when a player
wins.<br>
If a player wins a solo victory, he gains ranking points equal to the number of
opposing players that he defeated.<br>
If two or more players win a cooperative (faction) victory, they divide the 
ranking points (equal to the number of opposing players that they defeated) 
between them (rounded up).<br>
A defeated player neither gains nor loses any ranking points.
		
		</div></div>

<div class="List" style="width:718">
<table class="ListTable">
<tr class='ListheaderRow'>
	<td class='ListHeader'>&nbsp;</td>
	<td class='ListHeader'><div class="SolidText">Rank&nbsp;&nbsp;&nbsp;</div></td>
	<td class='ListHeader'><div class="SolidText">Player&nbsp;&nbsp;&nbsp;</div></td>
	<td class='ListHeader'><div class="SolidText">Games Played&nbsp;&nbsp;&nbsp;</div></td>
	<td class='ListHeader'><div class="SolidText">Score&nbsp;&nbsp;&nbsp;</div></td>
	<td class='ListHeader'><div class="SolidText">Solo Wins&nbsp;&nbsp;&nbsp;</div></td>
	<td class='ListHeader'><div class="SolidText">Faction Wins&nbsp;&nbsp;&nbsp;</div></td>
	<td class='ListHeader'><div class="SolidText">Survivior</div></td>
	<td class='ListHeader'><div class="SolidText">Defeated</div></td>
</tr>


<% 
	for (int i = 0; i < players.length; i++){ 
	String RowName = i + "ListRow"; 
		String userName = players[i].getLogin();
		User theUser = UserHandler.findUser(players[i].getLogin());
		if (theUser != null){
			userName = theUser.getName() + " (" + players[i].getLogin() + ")";
		}
%>
<tr class='ListTextRow' onMouseOver="TranparentRow('<%=RowName%>',9,1);" onMouseOut="TranparentRow('<%=RowName%>',9,0);">
	<td id='<%=RowName%>1' class='ListText'>&nbsp;</td>
	<td id='<%=RowName%>2' class='ListText'><div class="SolidText">
		<%= i + 1 %></div>
	</td>

	<td id='<%=RowName%>4' class='ListText'><div class="SolidText">
		<%= userName %>&nbsp;&nbsp;&nbsp;</div>
	</td>
	<td id='<%=RowName%>3' class='ListText'><div class="SolidText">
		<%= players[i].getTotalNrGames() %></div>
	</td>
	<td id='<%=RowName%>5' class='ListText'><div class="SolidText">
		<%= players[i].getNrDefeatedPlayers() %></div>
	</td>
	<td id='<%=RowName%>6' class='ListText'><div class="SolidText">
<%= players[i].getSoloWin() %></div>
	</td>
	<td id='<%=RowName%>7' class='ListText'><div class="SolidText">
<%= players[i].getFactionWin() %></div>
	</td>
	<td id='<%=RowName%>9' class='ListText'><div class="SolidText">
		<%= players[i].getSurvival() %></div></td>
	<td id='<%=RowName%>8' class='ListText'><div class="SolidText">
<%= players[i].getLoss() %></div>
	</td>
</tr>
<% } %>

</table>
</div>
<!--div class="Form_Header" ALIGN=RIGHT><A href="Master.jsp?action=ranking"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_ranking.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_ranking.jpg','Guide: How to play, good beginners guide!','GuideArea');" height=19 alt="How To Play" hspace=0 src="images/btn_ranking.jpg" width=83 vspace=0 border=0></A></div-->

		<div class="Form_Header" ALIGN="RIGHT" style="width:718"><div class="SolidText"></div>
			<div class="SolidText"><A href="Master.jsp?action=ranking"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_refreshlist.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_refreshlist.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_refreshlist.jpg" vspace=0 border=0></A></div>
		</div>
		<div class="List_End"></div>	
<br>

<!--	

Ranking score is calculated by counting the number of defeated opponents when a player
wins.<br>
If a player wins a solo victory, he gains ranking points equal to the number of
opposing players that he defeated.<br>
If two or more players win a cooperative (faction) victory, they divide the 
ranking points (equal to the number of opposing players that they defeated) 
between them (rounded up).<br>
A defeated player neither gains nor loses any ranking points.
</div>
<div class="FORM_End"></div-->
</div>
</body>
</html>
