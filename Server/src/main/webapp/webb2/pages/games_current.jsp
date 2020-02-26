<%@ page import="sr.webb.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="com.amarantin.imagepack.*"%>


<%
	// get PageURL
	String PageURL = request.getParameter("action"); 
	
	User theUser = null;
	User tmpUser = null;
	if (theUser == null){
		theUser = UserHandler.getUser(session,request,response);
		if (theUser.isGuest()){
			// try to check if player is logged in using the session object
			tmpUser = (User)session.getAttribute("user");
			if (tmpUser != null){ 
				// user is logged in using the session object
				System.out.println("User logged in using session: " + tmpUser);
				theUser = tmpUser;
			}
		}
	}


%>
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">

<!-- Start games_current.jsp fragment -->


<div class=Header710>Active Games</div>
<div class=TextHead710><b>Game List</b></div>
<div class=TextArea710>
<%
	// check if a ServerHandler exists
	//String found = ServerStatus.checkServerHandler(request,application);
	Object foundz = application.getAttribute("serverhandler");
	String message = "";
	ServerHandler shz = null;
	if (foundz == null){
		// if not, create it and store it in the application scope
		// create a new serverhandler
		shz = new ServerHandler();
		application.setAttribute("serverhandler",shz);
		message = "New ServerHandler created";
	}else{
		// if it does, retrieve it
		message = "ServerHandler already exists";
		shz = (ServerHandler)foundz;
	}
%>
<!--<%= message %><br>-->


<h2>Your Current Games</h2>
<%= shz.getCurrentPlayingGamesList(tmpUser) %><p>
<h2>Games starting up</h2>
</div>

<div class=TextHead710><b>Game List</b></div>
<div class=TextArea710>

<%if (theUser.isGuest()){%>
	<%= shz.getCurrentPlayingGamesListNoUser()%><p>
<%}else{%>
	<%= shz.getCurrentGamesList(tmpUser) %><p>
<%}%>

<%= it2.getTagAndImage("Refresh") %>

</div>

<!-- End games_current.jsp fragment -->
