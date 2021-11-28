<!-- Start register.jsp fragment -->

<%
	// handle session/login/logout
	if ("login".equals(request.getParameter("action"))){
		// log in player
		if ("ok".equals(UserHandler.loginUser(request, response))){
			// user loggrd in
			// get a User instance
			String lgn = request.getParameter("login");
			String pwd = request.getParameter("password");
			session.setAttribute("user", UserHandler.getUser(lgn,pwd));
		}
	}else
	if ("logout".equals(request.getParameter("action"))){
		// log out player 
		// remove any session handled by cookies
		UserHandler.logoutUser(request,response);
		// remove any session handled by session object
		session.removeAttribute("user");
	}
	if (session.getAttribute("user") == null){
		User user = UserHandler.getUser(session, request, response);
		if (user.isGuest()){
			// try to check if player is logged in using the session object
			User tmpUser = (User)session.getAttribute("user");
			if (tmpUser != null){ 
				// user is logged in using the session object
				System.out.println("User logged in using session: " + tmpUser);
			}
		}
	}

	String todoStr = request.getParameter("todo");
	String userMessage = "";
	String userName2 = "";
	String userLogin = "";
	//String userPassword = "";
	//String userPassword2 = "";
	String userRole = User.ROLE_PLAYER;
	String email = "";
	String turnEmail = "checked";
	String gameEmail = "checked";
	String adminEmail = "checked";
	boolean rulesOk = false;

	if ((todoStr != null) && (todoStr.equals("new_player"))){
		userName2 = request.getParameter("name");
		userLogin = request.getParameter("new_login");
		//userPassword = request.getParameter("new_password");
		//userPassword2 = request.getParameter("new_password2");
		userRole = User.ROLE_PLAYER;
		email = request.getParameter("email");
		turnEmail = request.getParameter("turn_email");
		gameEmail = request.getParameter("newgame_email");
		adminEmail = request.getParameter("admin_email");
		rulesOk = request.getParameter("rules") == "checked" ? true : false;
		userMessage = UserHandler.addUser(userName2, userLogin, userRole, email, turnEmail, gameEmail, adminEmail, rulesOk);
		//TODO UserHandler.addUser(userName2,userLogin,userPassword,userPassword2,userRole,email,turnEmail,gameEmail,adminEmail,rulesOk); Removed the password, need to secure the logic first around password.
	}
%>
<!--table width='710' border='0' cellpadding='0' cellspacing='0'>
<tr>
<td width=450!-->

<head>
<title>Register</title>
<link REL="STYLESHEET" HREF="CSS/style.css">

</head>

<div style="left: 132px;width: 450px;position: absolute;top: 90px;">	
	<div class="Form_Name" style="width:450"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:450"><div class="SolidText"><b>Register - Create a player account</b></div></div>
	<div class="Form_Text" style="width:450"><div class="SolidText">
<%
	if (userMessage.equals("ok")){
%>
<span class="paulTxtGreen">A player account has been created and you are welcome to login to SpaceRaze!</span>
</div>
</div>

	<div class="Form_Header" style="width:450px;border-right: #00fe23 1px solid;font-size: 14px;border-left: #00fe23 1px solid;color: #00fe23;padding-right: 5px;padding-left: 5px;padding-top: 3px;padding-bottom: 3px;font-family: Arial;background-color: #055510;font-weight: bold;" ALIGN="RIGHT"><div class="SolidText"><A href="#" onclick="Master.jsp"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_continue.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_continue.jpg','Register: Create your Spaceraze account.','GuideArea');" alt="Register" hspace=0 src="images/btn_continue.jpg" vspace=0 border=0></A></div></div>
	<div class="List_End"></div>

</div>
<%
	}else{
%>
<span class="paulTxtGreen">All fields (except login) can be changed later in the user account page.</span>
<br>
<span class="paulTxtGreen">Required fields are marked with a *.</span>
<br>
<br>
<%
	if (!userMessage.equals("")){
		session.setAttribute("errorType", "Registration Error");
		session.setAttribute("errorMessage", userMessage);
%>
<!-- register error message -->
<%@ include file="error_message_box.jsp" %>


<br><br>
<%
	}
%>
<%
	if (userMessage.equals("ok")){
%>

<%
	}else{
%>


<form id="registerform" name="registerform" metod="post" action="Master.jsp?action=register">
<input type="hidden" name="todo" value="new_player">
<input type="hidden" name="action" value="register">
<span class="paulTxtGreen">
<table cellpadding=0 cellspacing=0 width="350">
<tr>
<td><span class="paulTxtGreen">Public name*:</span></span></td>
<td><input class="InputText" type="text" name="name" value="<%= userName2 %>"></td>
</tr>
<tr>
<td><span class="paulTxtGreen">Login*:&nbsp;&nbsp;<i>(cannot be changed)</i></span></td>
<td><nobr><input class="InputText" type="text" name="new_login" value="<%= userLogin %>"></nobr></td>
</tr>
<%-- 
<tr>
<td><span class="paulTxtGreen">Password*:</span></td>
<td><input class="InputText" type="password" name="new_password" value="<%= userPassword %>"></td>
</tr>
<tr>
<td><span class="paulTxtGreen">Repeat password*:</span></td>
<td><input class="InputText" type="password" name="new_password2" value="<%= userPassword %>"></td>
</tr>
--%>
<tr>
<td><span class="paulTxtGreen">E-mail:</span></td>
<td><input class="InputText" type="text" name="email" value="<%= email %>"></td>
</tr>
<tr>
<td><span class="paulTxtGreen">Recieve new turn e-mail:</span></td>
<td><input class="" type="checkbox" name="turn_email" value="checked" <%= turnEmail %>></td>
</tr>
<tr>
<td><span class="paulTxtGreen">Recieve new game e-mail:</span></td>
<td><input class="" type="checkbox" name="newgame_email" value="checked" <%= gameEmail %>></td>
</tr>
<tr>
<td><span class="paulTxtGreen"><nobr>Recieve info from admin e-mail:</nobr></span></td>
<td><input class="" type="checkbox" name="admin_email" value="checked" <%= adminEmail %>></td>
</tr>
</table>
<br>
<b>Copyright and sharing</b>
<br>
<!-- 
1. It is not allowed to add copyrighted information to SpaceRaze. 
This includes copyrighted names, images and/or texts.
If this happens the admins of this site reserve the right to remove any such material, and if need be remove the whole object in which such data is embedded.
Repeated breaking of this rule may result in that the offenders account is removed.<br>
2.--> 
1.Any information added to SpaceRaze can be copied and used by other people.  
It is considered to be Public Domain.
<br><br>
&nbsp;&nbsp;<input type="checkbox" name="rules" value="checked" <%= rulesOk %>>&nbsp;&nbsp;&nbsp;I acknowledge that I have read and understood the above information
<br>

<%
	}
%>


</div>
</div>

	<div class="Form_Header" style="width:450px;border-right: #00fe23 1px solid;font-size: 14px;border-left: #00fe23 1px solid;color: #00fe23;padding-right: 5px;padding-left: 5px;padding-top: 3px;padding-bottom: 3px;font-family: Arial;background-color: #055510;font-weight: bold;" ALIGN="RIGHT"><div class="SolidText"><A href="#" onclick="document.forms[1].submit();"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_register.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_register.jpg','Register: Create your Spaceraze account.','GuideArea');" alt="Register" hspace=0 src="images/btn_register.jpg" vspace=0 border=0></A></div></div>
	<div class="List_End"></div>

</div>



<div style="left: 601px;width: 250px;position: absolute;top: 90px;">
		<%@ include file="../puffs/RightPuff.jsp" %>
</div>

</form>

<%
	}
%>

<!-- End register.jsp fragment -->
