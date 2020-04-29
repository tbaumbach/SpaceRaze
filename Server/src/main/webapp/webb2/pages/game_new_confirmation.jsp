<%@page import="spaceraze.servlethelper.handlers.GameWorldHandler"%>
<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="spaceraze.world.Map"%>
<%@ page import="spaceraze.world.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<title>Server administration page</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>

<%
	// get PageURL
	String PageURL = request.getParameter("action"); 

	boolean show = false;
	User theUserTwo = null;
	User tmpUserTwo = null;
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
				tmpUserTwo = theUser;
				theUserTwo = theUser;				
			}
		}
	}
%>
<!-- 
Player	=	<%=theUser.isPlayerOrAdmin()%>
Admin	=	<%=theUser.isAdmin()%>
GUEST	=	<%=theUser.isGuest()%>
Name	=	<%=theUser.getName()%>
Login	=	<%=theUser.getLogin()%>
 -->


<%
		String message = "";
	ServerHandler sh = (ServerHandler)application.getAttribute("serverhandler");
	String todoStr = request.getParameter("todo");
	String mapName = request.getParameter("mapname");
//	Map aMap = MapHandler.getMap(mapName);
//	int maxPlayers = aMap.getMaxNrStartPlanets();
	if ((todoStr != null) && (todoStr.equals("Start new game"))){
	
		int startingplanets = Integer.parseInt(request.getParameter("startingplanets"));
		int factionWin = Integer.parseInt(request.getParameter("factionWin"));
		int soloWin = Integer.parseInt(request.getParameter("soloWin"));
		int nrTurns = Integer.parseInt(request.getParameter("nrTurns"));	
	
		String gameWorld = request.getParameter("gameworld");
		String gameName = request.getParameter("gamename_new");
		String gamePassword = request.getParameter("game_password");
		String stepsNr = request.getParameter("steps");
		String autoBalance = request.getParameter("autobalance");
		String emailPlayers = request.getParameter("emailplayers");
		String groupFaction = request.getParameter("groupfaction");
		String time = request.getParameter("time");
		String nrPlayers = request.getParameter("maxnrplayers");
		String randomFaction = request.getParameter("randomfaction");
		String login = theUser.getLogin();
		String diplomacy = request.getParameter("diplomacy");
		GameWorld gw = GameWorldHandler.getGameWorld(gameWorld);
		String ranked = request.getParameter("ranked");
		StatisticGameType statisticGameType = StatisticGameType.findStatisticGameType(request.getParameter("statistics"));
		List<Faction> gwFactions = gw.getFactions();
		List<String> selectableFactions = new LinkedList<String>();
		for (int i = 0; i < gwFactions.size(); i++){
			Faction tmpFaction = gwFactions.get(i);
			String tmpFStr = request.getParameter("faction_" + tmpFaction.getName());
//			System.out.println(tmpFStr); // null if not checked...
			if (tmpFStr != null){
				selectableFactions.add(tmpFaction.getName());
			}
		}		
		message = sh.startNewGame(gameWorld, gameName, mapName, stepsNr, autoBalance, time, emailPlayers, nrPlayers, login, gamePassword, groupFaction, selectableFactions, randomFaction, diplomacy, ranked, factionWin, soloWin, nrTurns, startingplanets, statisticGameType);
	
	}
%>
<body background="images/spaze.gif">

<div style="left:130px;width:718px;position: absolute;top: 88px;">	
	<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b>Confirmation - Step 3</b></div></div>
	<div class="Form_Text"  style="width:718"><div class="SolidText">


<!-- <%= todoStr %> -->
<h2>New game status</h2>
<hr color="#FFBF00">
<% if (!message.equals("")){ %>
<p>
Server message: 
<% if (message.equalsIgnoreCase("Game started")){ %>
<font color="#00FF00"><%= message %></font>
<% }else{ %>
<font color="#FF0000"><%= message %></font>
<% } %>


<br>
<p>
<hr color="#FFBF00">
<% } %>

</div></div>
	<div class="Form_Header" ALIGN=RIGHT style="width:718"><div class="SolidText"><A href="Master.jsp?action=games_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_games.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_games.jpg','Games list: go to the Games list','GuideArea');" height=19 alt="Games list" hspace="3" src="images/btn_games.jpg" width=83 vspace="3" border=0></A></div></div>
	<div class="Form_End"></div>
</div>
</body>
</html>
