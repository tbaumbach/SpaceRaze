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



<body background="images/spaze.gif">
<div style="left: 130px;width: 450px;position: absolute;top: 89px;">	
	<div class="Form_Name" style="width:450"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:450"><div class="SolidText">Requirements</div></div>
	<div class="Form_Text" style="width:450">
	<div style="COLOR: #99ccff; POSITION: relative">
		<div class="SolidText">
		<table width="438" border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="text">
            <span class="Form_bold">Requirements</span><br>
		This page contains info about what you need to play SpaceRaze.
		<P>
		<span class="Form_bold">Java</span><br>
		To play SpaceRaze you need to have Java installed as a plug-in to your browser.<p>
		The Java runtime must be version 1.6 (or Java 6 as it is also called). Java can be downloaded from the <a href="http://java.sun.com/javase/downloads/index.jsp" target="_blank">Java download page</a> at Sun.
		<p>
		<span class="Form_bold">Screen resolution</span><br>
		The SpaceRaze website and applet clients are meant to use at resolution of at least 1024*768.

		</td>
		</tr>
		</table>
		</div>
	</div>
	</div>
		<div class="Form_End"></div>
</div>

<div style="left: 601px;width: 250px;position: absolute;top: 90px;">
	<%@ include file="../puffs/RightPuff.jsp" %>
</div>


