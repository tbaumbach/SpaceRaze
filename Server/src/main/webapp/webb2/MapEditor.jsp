<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<HTML>
<HEAD>
<TITLE>MapEditorApplet.class</TITLE>
</HEAD>
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

%>
<%
	String action = request.getParameter("action");
	String mapName = request.getParameter("mapname");
	String userName = tmpUser.getLogin();
	sr.server.persistence.PHash.incCounter("pageloaded.MapEditor");
	sr.server.persistence.PHash.incCounter("pageloaded.MapEditor.player." + tmpUser.getLogin());
%>
<BODY bgcolor="#000000">
<p>Remove this page... Apllet is dead</p>
<!-- <%= action %> -->
<!-- <%= mapName %> -->
<!-- <%= userName %> -->
<APPLET codebase="." ARCHIVE="spaceraze.jar" CODE = "sr.mapeditor.MapEditorApplet.class" WIDTH = 930 HEIGHT = 560>
    <PARAM NAME="scriptable" VALUE="false">
    <PARAM NAME = "username" VALUE="<%= userName %>">
    <PARAM NAME = "action" VALUE="<%= action %>">
    <PARAM NAME = "mapname" VALUE="<%= mapName %>">
</APPLET>

</BODY>
</HTML>
