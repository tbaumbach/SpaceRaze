<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.general.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.amarantin.imagepack.*"%>
<%@ page import="spaceraze.util.general.*"%>

<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">

<%

	User theUser = null;
	if (theUser == null){
		theUser = UserHandler.getUser(session,request,response);
		if (theUser.isGuest()){
			// try to check if player is logged in using the session object
			User tmpUser = (User)session.getAttribute("user");
			if (tmpUser != null){ 
				// user is logged in using the session object
				System.out.println("User logged in using session: " + tmpUser);
				theUser = tmpUser;
			}
		}
	}

	boolean showJoin = true;
	String autoUser = request.getParameter("autouser");
	int port = Integer.parseInt(request.getParameter("port"));
	// check if a ServerHandler exists
	//String found = ServerStatus.checkServerHandler(request,application);
	Object found = application.getAttribute("serverhandler");
	String message = "";
	ServerHandler sh = null;
	String Endturn="";
	if (found == null){
		// create a new serverhandler
		sh = new ServerHandler();
		application.setAttribute("serverhandler",sh);
		message = "New ServerHandler created";
	}else{
		message = "ServerHandler already exists";
		sh = (ServerHandler)found;
	}
	SR_Server aServer = sh.findGame(port);
	
	if(aServer.getEndTurn() > 0)
	{
		Endturn = "/" + aServer.getEndTurn();
	}
	String returnTo = request.getParameter("returnto");
	
	if (aServer.isPlayerParticipating(theUser)){
		if (!aServer.getLastUpdateComplete()){
			showJoin = false;
		}
	}
	else
	{
		showJoin = false;
	}
%>

<%
	String message_todo = null;
	String todoStr = request.getParameter("todo");
	if ((todoStr != null) && (todoStr.equals("Update game"))){
		String gameName = request.getParameter("gamename_update");
		int nrTurns = Integer.parseInt(request.getParameter("turnNr"));
		try{
			sh.updateGame(gameName,nrTurns);
			message_todo = "<font color=\"#FFFF00\"><b>Game updated</b></font>";
		}catch(Exception e){
			message_todo = "<font color=\"#FF0000\"><b>Error while updating</b></font>";
		}
	}else
	if ((todoStr != null) && (todoStr.equals("Delete game"))){
		String gameName = request.getParameter("gamename_delete");
		sh.deleteGame(gameName);
		message_todo = "<font color=\"#FFFF00\"><b>" + gameName + " deleted</b></font>";
		%>
		<script>
		location.href="redirect.jsp";
		</script>
		<%
	}	
%>

<!-- <%= todoStr %> -->

<div style="left:130px;width:718px;position: absolute;top: 88px;">
		<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>Game Information - <%= aServer.getGameName() %></b>&nbsp;</div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">

<table>
<tr>
	<td width="150"><b>Game Info:</b></td>
	<td></td>
	<td width=50></td>
	<td>
		<b>Players:</b>
	</td>
</tr>	


<tr>
	<td></td>
	<td></td>
	<td width=50></td>
	<td rowspan=12 valign=top>
		<%= ServerStatus.getPlayerListNO(aServer.getGalaxy(),autoUser) %>
	</td>
</tr>	


<tr>
	<td>Map:</td>
	<td><%= MapHandler.getMapName(aServer.getMapFileName()) %></td>
</tr>

<tr>
	<td>Ranked Game:</td>
	<td><%= aServer.getGalaxy().getranked() %></td>
</tr>


<tr>
	<td>SinglePlayer Game:</td>
	<td><%= aServer.getGalaxy().getsinglePlayer() %></td>
</tr>

<tr>
	<td>Gameworld:</td>
	<td><%= aServer.getGalaxy().getGameWorld().getFullName() %></td>
</tr>
<tr>
	<td>Diplomacy:</td>
	<td><%= aServer.getGalaxy().getDiplomacyGameType().getLongText() %></td>
</tr>
<tr>
	<td>Started by:</td>
	<td><%= aServer.getStartedByPlayerName() %></td>
</tr>

<tr>
	<td>Min nr of steps:</td>
	<td><%= aServer.getGalaxy().getSteps() %></td>
</tr>

<tr>
	<td>Group faction:</td>
	<td><%= Functions.getYesNo(aServer.getGalaxy().isGroupSameFaction()) %></td>
</tr>

<tr>
	<td>Autobalance:</td>
	<td><%= Functions.getYesNo(aServer.getGalaxy().getAutoBalance()) %></td>
</tr>


<tr>
	<td>Random factions:</td>
	<td><%= Functions.getYesNo(aServer.getGalaxy().isRandomFaction()) %></td>
</tr>
<tr>
	<td>Open factions:</td>
	<td><%= aServer.getGalaxy().getFactionListString() %></td>
</tr>	
<tr>
	<td>Statistics:</td>
	<td><%= aServer.getGalaxy().getStatisticsHandler().getStatisticGameType().getText() %></td>
</tr>	

<tr>
	<td>Turn number:</td>
	<td><%= aServer.getTurn() %><%=Endturn %></td>
</tr>	

<tr>
	<td>Last updated:&nbsp;</td>
	<td><%= aServer.getLastUpdatedString() %></td>
</tr>	
<tr>
	<td>&nbsp;</td>
</tr>
<tr>
	<td>Solo Win %:&nbsp;</td>
	<td><%= aServer.getSoloWin() %></td>
</tr>	

<tr>
	<td>Faction Win %:&nbsp;</td>
	<td><%= aServer.getFactionWin() %></td>
</tr>	

<tr>
	<td>Starting planets:&nbsp;</td>
	<td><%= aServer.getNumberOfStartPlanet() %></td>
</tr>	



<%
	if ((aServer.getTurn() == 0) & (aServer.isPasswordProtected())){
%>
<tr>
	<td>Game is password-protected</td>
	<td></td>
</tr>	
<%
	}
%>

</table>
<br>
<% if (aServer.getLastUpdateComplete()){ %>
	<%= ServerStatus.getUpdateText(aServer) %>
<% }else{%>
	Game could not complete last turn due to an error.<p>
<% if (theUser.isAdmin()){ %>
	<a href=view_last_game_log.jsp?gameid=<%= port %>>View last game log</a><p>
<% } %>
<% }%>

<%= ServerStatus.getStartingText(aServer.getGalaxy()) %><br>
<%= ServerStatus.getGameOverText(aServer.getGalaxy()) %><br>
<br>
<!--
<h2>Status of game <%= aServer.getGameName() %></h2>
Started by: <%= aServer.getStartedByPlayerName() %><br>
Map filename: <%= MapHandler.getMapName(aServer.getMapFileName()) %><br>
Gameworld: <%= aServer.getGalaxy().getGameWorld().getFullName() %><br>
Autobalance: <%= Functions.getYesNo(aServer.getGalaxy().getAutoBalance()) %><br>
Min number of steps: <%= aServer.getGalaxy().getSteps() %><br>
Group players from same faction: <%= Functions.getYesNo(aServer.getGalaxy().isGroupSameFaction()) %><br>
Random factions: <%= Functions.getYesNo(aServer.getGalaxy().isRandomFaction()) %><br>
Open factions: <%= aServer.getGalaxy().getFactionListString() %><br>
<%
	if ((aServer.getTurn() == 0) & (aServer.isPasswordProtected())){
%>
<br>
Game is password-protected
<br>
<%
	}
%>

<p>
Turn number is <%= aServer.getTurn() %>
<p>
-->
<!-- autoUser: <%= autoUser %> test-->
<%
if(!theUser.isGuest()){
	if (aServer.getLastUpdateComplete()){
		if (aServer.getTurn() == 0){
			if (!aServer.isPlayerParticipating(theUser) | (autoUser.equals("false"))){
%>
<a href="../applet/SpaceRaze_client.jsp?port=<%= port %>&autouser=<%= autoUser %>&returnto=current_game.jsp"  target="_top">Login to Game</a>
<p>
<a href="../applet/SpaceRaze_client.jsp?port=<%= port %>&autouser=<%= autoUser %>&returnto=close.jsp"  target="new_window">Login to Game (open in separate window)</a>
<p>
<%
			}
		}else{
%>
<p>

<p>
<%
		}
	}
	}
%>


<!--Last updated:&nbsp;<%= aServer.getLastUpdatedString() %>
-->
<p>


<!--%= ServerStatus.getPlayerList(aServer.getGalaxy(),autoUser) %-->
<br>
<!--A href="Master.jsp?action=games_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_refreshlist.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_refreshlist.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_refreshlist.jpg" vspace=0 border=0></A-->
</div>
		</div>
				<div class="Form_header" ALIGN="RIGHT" style="width:718"><div class="SolidText"></div>			
			<div class="SolidText">
<table><tr><td valign=top>
<%if (theUser.isAdmin())
	{
		if ((aServer.getTurn() > 0) | (aServer.getGalaxy().getNrPlayers() > 1))
		{%>
				<form action="Master.jsp" id="updateForm" name="updateForm" method="get"><input class="InputTextRed" type="text" name="turnNr" value="1"><input type="hidden" name="action" value="current_game"><input type="hidden" name="gamename_update" value="<%= aServer.getGameName() %>">
				&nbsp;
				<input type="hidden" name="port" value="<%= port %>"><input type="hidden" name="autouser" value="<%= autoUser %>"><input name="todo" type="hidden" value="Update game"><A href="javascript:document.getElementById('updateForm').submit();"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Red_update.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Red_Over_update.jpg','Refresh: Update page','GuideArea');" alt="Update" hspace=0 src="images/btn_Red_update.jpg" vspace=0 border=0></A></form>
			</td><td valign=top>
	  <%}%>
		<form action="Master.jsp" id="deleteForm" name="deleteForm"><input type="hidden" name="action" value="current_game"><input type="hidden" name="gamename_delete" value="<%= aServer.getGameName() %>"><input type="hidden" name="port" value="<%= port %>"><input type="hidden" name="autouser" value="<%= autoUser %>"><input name="todo" type="hidden" value="Delete game"><A href="javascript:document.getElementById('deleteForm').submit();"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Red_deletegame.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Red_Over_deletegame.jpg','Refresh: Delete Game','GuideArea');" alt="Delete" hspace=0 src="images/btn_Red_deletegame.jpg" vspace=0 border=0></A></form>
		</td><td valign=top>
  <%}%>
			
			<!--a href="SpaceRaze_client.jsp?port=<%= port %>&autouser=<%= autoUser %>&returnto=close.jsp"  target="new_window">Login to Game (open in separate window)</a-->
			<A href="Master.jsp?action=current_game&port=<%=port%>&autouser=<%=autoUser%>"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_refreshlist.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_refreshlist.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_refreshlist.jpg" vspace=0 border=0></A>
			<a href="SpaceRaze_client.jsp?port=<%= port %>&autouser=<%= autoUser %>&returnto=current_game&returnto_delete=<%= returnTo %>" target="_top">
<%if (showJoin){%>
	<IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_play.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_play.jpg','Requirements: What do you need to play SpaceRaze.','GuideArea');" height=21 alt="Requirements" hspace=0 src="images/btn_play.jpg" width=85 vspace=0 border=0>
  <%}%>	
	</td>
	</tr>
	</table>
	
</a>
			
			
			</div>
			
		</div>
		<div class="List_End"></div>	
		
</div>

		