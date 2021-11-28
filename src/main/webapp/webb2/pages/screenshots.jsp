<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="sr.server.ranking.*"%>


<%
	// get PageURL
	String PageURL = request.getParameter("action"); 

	boolean show = false;
	User theUser = null;
	User tmpUser = null;
	if (theUser == null){
		theUser = UserHandler.getUser(session,request,response);
		if (theUser.isGuest()){
			// try to check if player is logged in using the session object
			tmpUser = (User)session.getAttribute("user");
			show = true;
		}
		else
		{ 
				// user is logged in using the session object
				theUser = tmpUser;
		}
	}


%>

<!DOCTYPE html>
<html>
<head>
<title>History</title>
<meta charset="UTF-8">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">
</head>

<body background="images/spaze.gif">

<div style="left:130px;width:718px;position: absolute;top: 88px;padding-bottom:20px;">
	<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b>Screenshots</b></div></div>
	<div class="Form_Text" style="width:718;">
	<div class="SolidText">
<table class="ListTable">
<tr>
<td>

<p>
<a href="pages/screenshotsAlone.jsp" style="border:0"><img src="images/screenshot.gif" width="700"></a>
<br>
Ingame Screenshot. (Click on image to enlarge)<br>

</td>
</tr>
</table>
</div>
</div>
		<div class="Form_End"></div>
</div>

<br><br><br><br><br><br><br><br>
</body>
</html>
