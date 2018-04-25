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
%>
<TITLE>SpaceRaze Game: <%= gameName %></TITLE>
</HEAD>
<%="A=" + theUser.isGuest()%>
<%
	String returnTo = request.getParameter("returnto");
	String returnToDelete = request.getParameter("returnto_delete");
	String autoUser = request.getParameter("autouser");
	String userName = "";
	String userPassword = "";
	if ((autoUser != null) && (autoUser.equals("true"))){
		userName = theUser.getLogin();
		userPassword = theUser.getPassword();
	}
	sr.server.persistence.PHash.incCounter("pageloaded.SpaceRaze_client");
	sr.server.persistence.PHash.incCounter("pageloaded.SpaceRaze_client.player." + theUser.getLogin());
%>
<BODY bgcolor="#000000">
<!-- <%= autoUser %> -->
<!-- <%= userName %> -->
<table width=100%>
<tr>
<td align="center" valign="middle">
<APPLET codebase="." ARCHIVE="spaceraze.jar" CODE = "sr.client.SpaceRazeApplet.class" WIDTH = 1200 HEIGHT = 710>
    <PARAM NAME="scriptable" VALUE="false">
    <PARAM NAME = "port" VALUE="<%= request.getParameter("port") %>">
    <PARAM NAME = "username" VALUE="<%= userName %>">
    <PARAM NAME = "userpassword" VALUE="<%= userPassword %>">
    <PARAM NAME = "returnto" VALUE="<%= returnTo %>">
    <PARAM NAME = "returnto_delete" VALUE="<%= returnToDelete %>">
    <PARAM NAME = "autouser" VALUE="<%= autoUser %>">
</APPLET>
</td>
</tr>
</table>
</BODY>
</HTML>
