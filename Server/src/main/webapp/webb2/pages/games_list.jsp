<%@ page import="sr.webb.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="com.amarantin.imagepack.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Startpage</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link REL="STYLESHEET" HREF="CSS/style.css">

</head>


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


	ImageText it = new ImageText("buttonimage");
	it.setAttributes(" border=\"0\" onClick=\"document.location='games_list.jsp'\"");
	// check if a ServerHandler exists
	//String found = ServerStatus.checkServerHandler(request,application);
	Object found = application.getAttribute("serverhandler");
	String message = "";
	ServerHandler sh = null;
	if (found == null){
		// if not, create it and store it in the application scope
		// create a new serverhandler
		sh = new ServerHandler();
		application.setAttribute("serverhandler",sh);
		message = "New ServerHandler created";
	}else{
		// if it does, retrieve it
		message = "ServerHandler already exists";
		sh = (ServerHandler)found;
	}
%>
<body background="images/spaze.gif">
<div style="left: 130px;width: 718px;position: absolute;top: 88px;">	

<%if(!theUser.isGuest()){%>
		<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze - Game List</div></div>
		<div class="Form_Header" style="width:718;"><div class="SolidText"><b>Your Current Games</b></div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
			Your Active games			
		</div></div>
		
		<div class="List" style="width:718;">
			<%= sh.getCurrentPlayingGamesListNO(theUser) %>
		</div>
		<div class="Form_header" ALIGN="RIGHT" style="width:718;padding-bottom:4px;"><div class="SolidText">&nbsp;</div></div>		
		<div class="List_End"></div>
		<br>

		<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze - Single Player Game List</div></div>
		<div class='Form_Header' style="width:718"><div class="SolidText"><b>Your Single Player Games</b></div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
			Single Games
		</div></div>
		
		<div class="List" style="width:718;">
			<%= sh.getCurrentPlayingGamesListNOSingle(theUser) %>
		</div>

		<div class="Form_header" ALIGN="RIGHT" style="width:718"><div class="SolidText"></div>
			<div class="SolidText"><A href="Master.jsp?action=games_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_refreshlist.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_refreshlist.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_refreshlist.jpg" vspace=0 border=0></A></div>
		</div>
		<div class="List_End"></div>	
<br>

		<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze - Game List</div></div>
		<div class='Form_Header' style="width:718"><div class="SolidText"><b>Games starting up</b></div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
			Games that you have joined, but not yet started
		</div></div>
		
		<div class="List" style="width:718;">
			<%= sh.getCurrentOpenGamesListNO(theUser) %>
		</div>
		<div class="Form_header" ALIGN="RIGHT" style="width:718;padding-bottom:7px;"><div class="SolidText"></div></div>
		<div class="List_End"></div>
		<br>
<%}%>




		<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze - Game List</div></div>
		<div class='Form_Header' style="width:718"><div class="SolidText"><b>All Ongoing games</b></div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
			All Active games
		</div></div>
		
		<div class="List" style="width:718;">
			<%= sh.getCurrentGamesListNO(theUser) %>
		</div>

		<div class="Form_header" ALIGN="RIGHT" style="width:718"><div class="SolidText"></div>
			<div class="SolidText"><A href="Master.jsp?action=games_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_refreshlist.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_refreshlist.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_refreshlist.jpg" vspace=0 border=0></A></div>
		</div>
		<div class="List_End"></div>	
	</div>

</body>
</html>
