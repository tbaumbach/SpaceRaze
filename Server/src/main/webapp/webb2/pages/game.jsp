<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.amarantin.imagepack.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Current Game status</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%
	String autoUser = request.getParameter("autouser");
	int port = Integer.parseInt(request.getParameter("port"));
	// check if a ServerHandler exists
	//String found = ServerStatus.checkServerHandler(request,application);
	Object found = application.getAttribute("serverhandler");
	String message = "";
	ServerHandler sh = null;
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
	ImageText it = new ImageText("buttonimage");
	it.setAttributes(" border=\"0\" onclick=\"document.location='current_game.jsp?port=" + port + "&autouser=" + autoUser + "'\"");
	
%>
<body background="images/spaze.gif">
<h2>Status of game <%= aServer.getGameName() %></h2>
Started by: <%= aServer.getStartedByPlayerName() %><br>
Map filename: <%= MapHandler.getMapName(aServer.getMapFileName()) %><br>
Gameworld: <%= aServer.getGalaxy().getGameWorld().getFullName() %><br>
<%
	if ((aServer.getTurn() == 0) & (aServer.isPasswordProtected())){
%>
<br>
Game is password-protected
<br>
<%
	}
%>
<!-- autoUser: <%= autoUser %> test-->
<p>
Turn number is <%= aServer.getTurn() %>
<p>
<%
	if (aServer.getLastUpdateComplete()){
		if (aServer.getTurn() == 0){
			if (!aServer.isPlayerParticipating(tmpUser) | (autoUser.equals("false"))){
%>
<a href="../applet/SpaceRaze_client.jsp?port=<%= port %>&autouser=<%= autoUser %>&returnto=current_game.jsp"  target="_top">Login to Game</a>
<p>
<a href="../applet/SpaceRaze_client.jsp?port=<%= port %>&autouser=<%= autoUser %>&returnto=close.jsp"  target="new_window">Login to Game (open in separate window)</a>
<p>
<%
			}
		}else{
%>
<a href="../applet/SpaceRaze_client.jsp?port=<%= port %>&autouser=<%= autoUser %>&returnto=current_game.jsp" target="_top">Login to Game</a>
<p>
<a href="../applet/SpaceRaze_client.jsp?port=<%= port %>&autouser=<%= autoUser %>&returnto=close.jsp"  target="new_window">Login to Game (open in separate window)</a>
<p>
<%
		}
	}
%>
Last updated:&nbsp;<%= aServer.getLastUpdatedString() %>
<p>
<%= ServerStatus.getStartingText(aServer.getGalaxy()) %>
<%= ServerStatus.getGameOverText(aServer.getGalaxy()) %>
<% if (aServer.getLastUpdateComplete()){ %>
<%= ServerStatus.getUpdateText(aServer) %>
<% }else{%>
Game could not complete last turn due to an error.<p>
<% if (tmpUser.isAdmin()){ %>
<a href=view_last_game_log.jsp?gameid=<%= port %>>View last game log</a><p>
<% } %>
<% }%>
<%= ServerStatus.getPlayerList(aServer.getGalaxy(),autoUser) %>
<br>
<%= it.getTagAndImage("Refresh List") %>
</body>
</html>
