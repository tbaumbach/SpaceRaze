<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Server administration page</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%@ include file="admin_only.jsp" %>
<%
	String message = null;
	ServerHandler sh = (ServerHandler)application.getAttribute("serverhandler");
	String todoStr = request.getParameter("todo");
	if ((todoStr != null) && (todoStr.equals("Update game"))){
		String gameName = request.getParameter("gamename_update");
		int nrTurns = Integer.parseInt(request.getParameter("turnNr"));
		try{
			sh.updateGame(gameName,nrTurns);
			message = "<font color=\"#00FF00\">Game updated</font>";
		}catch(Exception e){
			message = "<font color=\"#FF0000\">Error while updating</font>";
		}
	}else
	if ((todoStr != null) && (todoStr.equals("Delete game"))){
		String gameName = request.getParameter("gamename_delete");
		sh.deleteGame(gameName);
		message = "<font color=\"#00FF00\">" + gameName + " deleted</font>";
	}
%>
<body background="images/spaze.gif">
<!-- <%= todoStr %> -->
<h2>Games administration page</h2>
This page allows administrators to start, update and delete games.
<% if (message != null){ %>
<hr color="#FFBF00">
<p>
<%= message %>
<p>
<% } %>
<hr color="#FFBF00">
<form action="server_admin.jsp">
<h3>Update game</h3>
<table>
<tr>
<td>Choose game:&nbsp;&nbsp;&nbsp;</td>
<td><select name="gamename_update">
<%= sh.getNameOptions() %>
</select></td>
</tr>
<tr>
<td>Number of turns to update:&nbsp;&nbsp;&nbsp;</td>
<td><input type="text" name="turnNr" value="1"></td>
</tr>
<tr>
<td colspan="2"><input name="todo" type="submit" value="Update game"></td>
</tr>
</table>
</form>
<p>
<form action="game_new.jsp">
<hr color="#FFBF00">
<h3>Start new game</h3>
<table>
<tr>
<td>Choose map:&nbsp;&nbsp;&nbsp;</td>
<td><select name="mapname">
<%= MapHandler.getMapHTML() %>
</select></td>
</tr>
<tr>
<td colspan="2"><input name="todo" type="submit" value="New game"></td>
</tr>
</table>
</form>
<hr color="#FFBF00">
<form action="server_admin.jsp">
<h3>Delete game</h3>
<table>
<tr>
<td>Choose game:&nbsp;&nbsp;&nbsp;</td>
<td><select name="gamename_delete">
<%= sh.getNameOptions() %>
</select></td>
</tr>
<tr>
<td colspan="2"><input name="todo" type="submit" value="Delete game"></td>
</tr>
</table>
</form>
<hr color="#FFBF00">
</body>
</html>
