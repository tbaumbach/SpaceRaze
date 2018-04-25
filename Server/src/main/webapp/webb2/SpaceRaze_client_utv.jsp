<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="java.io.*"%>
<HTML>
<HEAD>
<%
	Object found = application.getAttribute("serverhandler");
	ServerHandler sh = null;
	if (found == null){
		// create a new serverhandler
		sh = new ServerHandler();
		application.setAttribute("serverhandler",sh);
	}else{
		sh = (ServerHandler)found;
	}
	int port = Integer.parseInt(request.getParameter("port"));
	SR_Server aServer = sh.findGame(port);
	String gameName = aServer.getGameName();
%>
<TITLE>SpaceRaze Game: <%= gameName %></TITLE>
</HEAD>
<BODY bgcolor="#000000">
<table width=100%>
<tr>
<td align="center" valign="middle">
<APPLET codebase="." ARCHIVE="spaceraze.jar" CODE = "sr.client.SpaceRazeApplet.class" WIDTH = 1200 HEIGHT = 710>
    <PARAM NAME="scriptable" VALUE="false">
    <PARAM NAME = "port" VALUE="<%= request.getParameter("port") %>">
    <PARAM NAME = "username" VALUE="pabod">
    <PARAM NAME = "userpassword" VALUE="5by5">
    <PARAM NAME = "returnto" VALUE="null">
    <PARAM NAME = "returnto_delete" VALUE="null">
    <PARAM NAME = "autouser" VALUE="true">
</APPLET>
</td>
</tr>
</table>
</BODY>
</HTML>
