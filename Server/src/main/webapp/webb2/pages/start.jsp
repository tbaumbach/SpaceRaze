<!-- Start start.jsp fragment -->
<%@page import="spaceraze.servlethelper.handlers.GameWorldHandler"%>
<%@page import="sr.server.map.MapHandler"%>
<div style="left: 132px;width: 450px;position: absolute;top: 90px;">
	
	
		<% if (!theUser.isGuest()){ %>
		<div class="Form_name" style="width:450"><div class="SolidText">Welcome to SpaceRaze</div></div>
		<div class="Form_Header" style="width:450"><div class="SolidText"><b>New features:</b></div></div>
		<div class="Form_Text" style="width:450">
		<div class="SolidText">
		 <b>Important fixes:</b><br>
			* INGAME Mail, det fungerar att skicka mail som kommer fram direkt, så man kan prata diplomati med sina medspelare innan varje drag<br>
			* Möjligheten att ha fler än en startplanet.<br>
			* Man kan sätta ett parti turnbaserat<br>
			* Fixxar av klient layout<br>
			* Fixxar av Webb Layout<br><br>
			
			<a href="http://www.spaceraze.com/webb2/Master.jsp?action=guides_list"> * Guides: Läs nya guides, Klicka här</a><br>		
			 <a href="http://www.spaceraze.com/webb2/Master.jsp?action=gameworlds_list">* Gameworlds: Lär dig nya spelvärldar, Klicka här</a><br>		
		</div>
		</div>


		<%}else{%>

	<div class="Form_name" style="width:450"><div class="SolidText">SpaceRaze introduction</div></div>
		<div class="Form_Header" style="width:450"><div class="SolidText"><b>Welcome to SpaceRaze</b></div></div>
		<div class="Form_Text" style="width:450">
		<div class="SolidText">
		  SpaceRaze is a 
                  turn-based strategy game of intergalactic warfare and 
                  conquest. Players try to win control over a quadrant of space, 
                  containing several other players and factions. A typical 
                  SpaceRaze game will update a few times every week and will be 
                  finished after a few weeks. <BR><BR>
                  <span class="Form_bold"></span>
                  <BR>The SpaceRaze site also 
                  contain a map editor, allowing players to create their own 
                  maps which can be used in SpaceRaze games, and a battle 
                  simulator allowing players to test the strength of different 
                  spaceship types. <BR><BR>All games take place in one of 
                  several different game worlds, each containing its own 
                  spaceships, people, factions and rules.<br>
        </div>
		</div>
		<div class="Form_Header" style="width:450"><div class="SolidText"><b>Main features:</b></div></div>
		<div class="Form_Text" style="width:450">
		<div class="SolidText">
		 <b>SpaceRaze's main features:</b><br>
						&nbsp;&nbsp;-Participate in any number of games<br>
						&nbsp;&nbsp;-Start new games<br>
						&nbsp;&nbsp;-Create maps in the map editor<br>
						&nbsp;&nbsp;-Battle simulator<br>
						&nbsp;&nbsp;-Ranking<br>
		</div>
		</div>


		<%}%>

		<div class="Form_Header" style="width:450"><div class="SolidText"><b>SpaceRaze Statistics:</b></div></div>
		<div class="Form_Text" style="width:450">
<div class="SolidText">
						
<b>Some numbers about SpaceRaze:</b><br>
&nbsp;&nbsp;-Number of players: <%= UserHandler.getUserNr() %><br>
<%
	// check if a ServerHandler exists
	Object found = application.getAttribute("serverhandler");
	ServerHandler sh = null;
	if (found == null){
		// if not, create it and store it in the application scope
		// create a new serverhandler
		sh = new ServerHandler();
		application.setAttribute("serverhandler",sh);
	}else{
		// if it does, retrieve it
		sh = (ServerHandler)found;
	}
%>
&nbsp;&nbsp;-Number of games running: <%= sh.getGamesNrRunning() %><br>
&nbsp;&nbsp;-Number of games starting up: <%= sh.getGamesNrStarting() %><br>
&nbsp;&nbsp;-Number of maps: <%= MapHandler.getMapsNr() %><br>
&nbsp;&nbsp;-Number of game worlds: <%= GameWorldHandler.getGameWorldsNr() %><br>
</div>
		</div>
		<div class="Form_End"></div>
</div>

<div style="left: 601px;width: 250px;position: absolute;top: 90px;">
		<%@ include file="../puffs/RightPuff.jsp" %>
</div>

<!-- End start.jsp fragment -->
