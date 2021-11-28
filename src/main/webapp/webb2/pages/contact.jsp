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
<title>SpaceRaze Contact Page</title>
<meta charset="UTF-8">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">
</head>
<body background="images/spaze.gif">


<div style="left: 132px;width: 450px;position: absolute;top: 90px;">	
	<div class="Form_Name" style="width:450"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:450"><div class="SolidText"><b>Contact Information</b></div></div>
	<div class="Form_Text" style="width:450">
	<div class="SolidText">
<table width="430" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>
<h2>Contact</h2>
To contact the SpaceRaze Team, use the mailaddress below:
<P>
<strong>spaceraze@gmail.com</strong><br>
<p>
<!--
Please mail us information about:
<ul>
<li>Bugs</li>
<li>Errors in the manual</li>
<li>Things that should be in the FAQ</li>
<li>Game balancing</li>
<% if (show){ %>
<li>If you want to play SpaceRaze</li>
<% } %>
<li>Anything else about the game ;-)</li>
</ul>
-->
</td>
</tr>
</table>
</div>
</div>
		<div class="Form_End"></div>
</div>

<div style="left: 601px;width: 250px;position: absolute;top: 90px;">
		<%@ include file="../puffs/RightPuff.jsp" %>
</div></body>
</html>
