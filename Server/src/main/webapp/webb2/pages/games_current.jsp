<%@ page import="sr.webb.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="com.amarantin.imagepack.*"%>


<%
	// get PageURL
	String PageURL = request.getParameter("action"); 
	
	User userGamesCurrent = session.getAttribute("user") != null ? (User)session.getAttribute("user") : UserHandler.getUser(session,request,response);


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
	String messagGamecurrent = "";
	ServerHandler shz = null;
	if (foundz == null){
		// if not, create it and store it in the application scope
		// create a new serverhandler
		shz = new ServerHandler();
		application.setAttribute("serverhandler",shz);
		messagGamecurrent = "New ServerHandler created";
	}else{
		// if it does, retrieve it
		messagGamecurrent = "ServerHandler already exists";
		shz = (ServerHandler)foundz;
	}
%>
<!--<%= messagGamecurrent %><br>-->


<h2>Your Current Games</h2>
<%= shz.getCurrentPlayingGamesList(userGamesCurrent) %><p>
<h2>Games starting up</h2>
</div>

<div class=TextHead710><b>Game List</b></div>
<div class=TextArea710>

<%if (userGamesCurrent.isGuest()){%>
	<%= shz.getCurrentPlayingGamesListNoUser()%><p>
<%}else{%>
	<%= shz.getCurrentGamesList(userGamesCurrent) %><p>
<%}%>
</div>

<!-- End games_current.jsp fragment -->
