<%@ page import="sr.webb.*"%>
<%@ page import="spaceraze.world.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.amarantin.imagepack.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Current Game status</title>
<meta charset="UTF-8">
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
</body>
</html>
