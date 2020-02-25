<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="sr.server.ranking.*"%>
<%@ page import="sr.server.*"%>


<%
	// get PageURL
	String PageURL = request.getParameter("action"); 

	boolean show = false;
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
<!DOCTYPE html>
<html>
<head>
<title>SpaceRaze Credits Page</title>
<meta charset="UTF-8">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">
</head>

<body background="images/spaze.gif">

<div style="left: 132px;width: 450px;position: absolute;top: 90px;">	
	<div class="Form_name" style="width:450"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:450"><div class="SolidText"><b>Credits</b></div></div>
	<div class="Form_Text" style="width:450">
	<div style="COLOR: #99ccff; POSITION: relative">

<table width="430" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>
<b>Great work!!</b>
<P>
Paul Bodin - Lead Designer/Developer/Project Owner<br>
Thobias Baumbach - Developer/Game Design<br>
Ragnar Klinga - Developer/Game Design<br>
Nicklas Ohlsén - Webb Developer/Game Design<br>
Peter Jansson - Extreme Tester/Game Design<br>
Annika Ericsson Extreme Tester<br>
Tomas Norremo - Extreme Tester<br>
Henrik Sjöstedt - Extreme Tester<br>
Markus gemstad - Hosting<br>
</p>

</td>
</tr>
</table>
</div>
</div>
		<div class="Form_End"></div>
</div>

<div style="left: 601px;width: 250px;position: absolute;top: 90px;">
		<%@ include file="../puffs/RightPuff.jsp" %>
</div>
</body>
</html>
