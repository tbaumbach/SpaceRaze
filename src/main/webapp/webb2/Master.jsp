<!DOCTYPE html>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="CSS/style.css">
</head>

<script type="text/javascript">
	var x=0
	var y=0

	function show_coords(event)
	{
	//	document.form1.txtX.value=event.clientX -x;
	//	document.form1.txtY.value=event.clientY - y;
	}
	function MoveOrigo(event)
	{
		x=event.clientX;
		y=event.clientY;
	}

	function TextToLayer(sText,oLayer)
	{
		document.getElementById(oLayer).className ='GuideAreaBlue';
		document.getElementById(oLayer).innerHTML = sText;
	}

	function TextToLayerRED(sText,oLayer)
	{
//	alert('TESST');
		document.getElementById(oLayer).className ='GuideAreaRED';
		document.getElementById(oLayer).innerHTML = sText;
	}


	function OnMouseOverNOut_Image(oControl,sImageName,sText,oLayer)
	{
		TextToLayer(sText,oLayer);
		oControl.src = sImageName;
	}

	function OnMouseOverNOut_ImageWarning(oControl,sImageName,sText,oLayer)
	{
		if (document.getElementById(oLayer).className !='GuideAreaRED')
		{
			TextToLayer(sText,oLayer);
		}	
		oControl.src = sImageName;
	}


	function OnMouseOverNOut_Class(oControl,sClassName)
	{
		TextToLayer("Closing Window",'GuideArea')
		oControl.className = sClassName;
	}

	function OnMouseOverNOut_Color(oControl,sColor)
	{
	
	}

	function ShowHelpLayer(oLayer)
	{
		document.getElementById(oLayer).style.display = '';
	}

	function HideHelpLayer(oLayer)
	{	
		document.getElementById(oLayer).style.display = 'none';
	}

	function Logout()
	{		
		document.forms["form2"].submit();			
	}

	function ShowLayer(oLayer)
{
	if( document.getElementById(oLayer).style.display=='inline')
	{
		document.getElementById(oLayer).style.display='none';
	}
	else
	{
		document.getElementById(oLayer).style.display='inline';
	}
}

	function TranparentRow(RowName,iColumns,onoff)
	{
		for(i=0;i< iColumns;i++)
		{
			temp = RowName + (i+1);
			//alert(temp);
			if (onoff)
			{
				document.getElementById(temp).style.backgroundColor='#148f14';
			}
			else
			{
				document.getElementById(temp).style.backgroundColor='#107110';
			}
		}
	}
	
</script>

<%
	// get PageURL
	String PageURL = request.getParameter("action"); 

	// handle session/login/logout
	String message = "";
	User theUser = null;
	if (PageURL != null){
		if (PageURL.equals("login")){
			// log in player
			message = UserHandler.loginUser(request, response);		

			if (message.equals("ok")){
		
				// user loggrd in
				// get a User instance
				String lgn = request.getParameter("login");
				String pwd = request.getParameter("password");
				// also log in player by using session object
				session.setAttribute("user", UserHandler.getUser(lgn,pwd));
			}
		}else
		if (PageURL.equals("logout")){
			// log out player 
			// remove any session handled by cookies
			message = UserHandler.logoutUser(request,response);		
			// remove any session handled by session object
			session.removeAttribute("user");
		}
	}
	if (theUser == null){
		theUser = UserHandler.getUser(session, request, response);
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
<!-- <%="MESSAGE" + message %> -->

<!-- body class="main" bgcolor="#000000" background="" onmousedown="MoveOrigo(event);"  onmousemove="show_coords(event)" onLoad="document.forms[0].login.focus()"-->
<body bgcolor="#000000" background=images/SpacerazeBG.jpg style="background-repeat: no-repeat;margin-top: 0px;margin-left: 0px;" >
<%if (!theUser.isGuest()){ %>
<FORM id="form2" method="post" action="Master.jsp">
<input type="hidden" name="action" id="action" value="logout">
</form>
<%} %>

	<!--Include Left Menu Area -->
		<%@ include file="buttons/menu_buttons.jsp" %>


	<!--Include Content Area -->
		<%if (PageURL == null || PageURL.equals("login") || PageURL.equals("logout"))
		{
			%>
			<%@ include file="pages/start.jsp" %>
			<%
		}
		else
		{
			PageURL = "pages/" + PageURL + ".jsp";
			%>
			<jsp:include page='<%= PageURL %>' />
			<%
		}	%>

<FORM name="form1" id="form1" method="post" action="Master.jsp?action=login">
	<!--Include Content Area -->
		<div id="GuideDiv" style="left: 160px;width: 388px;position: absolute;top: 57px;z-index:99;">
				<div id="GuideArea" class='GuideAreaBlue'>
				  This is spaceraze startpage from here you can do a various amount.
				</div>
		</div>

			<%if ((PageURL != null) && (PageURL.equals("login")))
			{
				if ((message != null) && (!message.equals("ok")))
				{
					String errorType = "Login Error";
					String errorMessage = message;%>
					
					<script>
						TextToLayerRED("<b>Login Error:</b> Wrong Username or Password",'GuideArea');
					</script>
		
					<%
				}
			}	%>



	<!--Include Help Area -->
		<div id="HelpDiv" style="left: 300px;width: 200px;position: absolute;top: 250px;z-index:99;display:none;">
			<div class="Header">Help</div>
				<div class="TextHead"><b>Startpage</b></div>
				<div class="TextArea">
				  This is spaceraze startpage from here you can do a various amount of tasks.
				</div>
				<div class="TextHead" ALIGN="RIGHT">
					<a href="#" onclick="HideHelpLayer('HelpDiv')">Close</a><br>
				</div>
				<div class="Form_End">
				</div>
		</div>

	<!--Include Login Area -->

			
			
	<%if (theUser.isGuest()){%>
	
	<script type="text/javascript">
	
	function Enter(e)
	{
if( !e ) {
if( window.event ) {
//DOM
e = window.event;
} else {
//TOTAL FAILURE, WE HAVE NO WAY OF REFERENCING THE EVENT
return;
}
}
if( typeof( e.which ) == 'number' ) {
//NS 4, NS 6+, Mozilla 0.9+, Opera
e = e.which;
if (e=='13')document.forms["form1"].submit();		
} else if( typeof( e.keyCode ) == 'number' ) {
//IE, NS 6+, Mozilla 0.9+
e = e.keyCode;
if (e=='13')document.forms["form1"].submit();		
} else if( typeof( e.charCode ) == 'number' ) {
//also NS 6+, Mozilla 0.9+
e = e.charCode;
if (e=='13')document.forms["form1"].submit();		
} else {

return;
}	
	//	if (event.keyCode == 13)
	//	{
			
	//	}
	}
	
	document.onkeydown=Enter
	
	</script>
	
	
	<input type="hidden" value="login" id="action" name="action"  >
		<div id="LoginDiv" style="left: 575px;width: 200px;position: absolute;top: 25px;z-index:99;">
			    <table class="ListTable">
				<tr>
				    <td class="LoginText">UserID:</td>
				    <td><input tabindex="1" class="LoginInputText" id="login" name="login" type=text value=""/></td>
				    <td colspan="2"></td>
				</tr>
				<tr>
				    <td class="LoginText">Password:</td>
				    <td><input tabindex="2" class="LoginInputText" id="password" name="password" type="password" value=""/></td>
				    <td></td>
				    <td align="right">
						<A href="#" onclick='document.forms["form1"].submit();' tabindex="3"><IMG onmouseout="OnMouseOverNOut_ImageWarning(this,'images/btn_Blue_login.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_ImageWarning(this,'images/btn_Blue_Over_login.jpg','Login: Enter SpaceRaze Acoount name and password.','GuideArea');" height=21 alt="Login" hspace=0 src="images/btn_Blue_login.jpg" width=85 vspace=0 border=0></A><BR>
				    </td>
				</tr>
				</table>
		</div>
	<%}else{%>
		<div id="LoginDiv" style="left: 575px;width: 272px;position: absolute;top: 25px;z-index:99;">
			    <table class="ListTable" style="width: 100%">
				<tr>
				    <td class="LoginText">UserID:</td>
				    <td><input class="LoginInputText" id="login" name="login" type=text value="<%=theUser.getName()%>" disabled="disabled"/></td>
				    <td></td>
				    <td class="Text"></td>
				</tr>
				<tr>
				    <td colspan="2">
				    	<a class="Login" href="Master.jsp?action=settings">Settings</a>&nbsp;&nbsp;&nbsp;
				    	<a class="Login" href="Master.jsp?action=change_password">Change Password</a>
				    	<input class="InputText" id="password" name="password" type="hidden" value=""/>
				    </td>
				    <td></td>
				    <td align="right">
						<A href="#" onclick="Logout()"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_logout.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_logout.jpg','Logout: Take care, Welcome back','GuideArea');" height=21 alt="Logout" hspace=0 src="images/btn_Blue_logout.jpg" width=85 vspace=0 border=0></A><BR>
				    </td>
				</tr>
				</table>
		</div>	
	<%}%>


<!-- test 
<font color="#FFFFFF">
login: <%= request.getParameter("login") %><br>
action: <%= PageURL %><br>
message: <%= message %><br> 
Logged in user name: <%= theUser.getName() %><br>
</font>
-->

<DIV id="divLocation" style="LEFT: 30px; POSITION: absolute; TOP: 200px" z-index="4"></DIV><LAYER id="layerLocation" z-index="4" height="97" width="205"></LAYER><script>
//showHelpLayerImp('layerLocation','divLocation','helpLayer0','175')</script>
		<span class="Text">
			<input class="inpCords" name="txtX" id="txtX" type=hidden style="width=20;" value="">
			<input class="inpCords" type=hidden value="" name="txtY" id="txtY">
		</span>
</form>
			
<!--a href=Master.jsp?action=admin_mail>admin_mail</a><br>
<a href=Master.jsp?action=admin_only> admin_only</a><br>
<a href=Master.jsp?action=battle_sim> battle_sim</a><br>
<a href=Master.jsp?action=battle_sim_list> battle_sim_list</a><br>
<a href=Master.jsp?action=change_password> change_password</a><br>
<a href=Master.jsp?action=checklogin> checklogin</a><br>
<a href=Master.jsp?action=checklogin2> checklogin2</a><br>
<a href=Master.jsp?action=contact> contact</a><br>
<a href=Master.jsp?action=create_edit_article> create_edit_article</a><br>
<a href=Master.jsp?action=current_game> current_game</a><br>
<a href=Master.jsp?action=current_games> current_games</a><br>
<a href=Master.jsp?action=default> default</a><br>
<a href=Master.jsp?action=edit_props> edit_props</a><br>
<a href=Master.jsp?action=error_message_box> error_message_box</a><br>
<a href=Master.jsp?action=faction> faction</a><br>
<a href=Master.jsp?action=games_current> games_current</a><br>
<a href=Master.jsp?action=gameworld> gameworld</a><br>
<a href=Master.jsp?action=gameworlds_list> gameworlds_list</a><br>
<a href=Master.jsp?action=game_new> game_new</a><br>
<a href=Master.jsp?action=leftmenu> leftmenu</a><br>
<a href=Master.jsp?action=loggedin> loggedin</a><br>
<a href=Master.jsp?action=map_confirm_delete> map_confirm_delete</a><br> 
<a href=Master.jsp?action=map_create> map_create</a><br> 
<a href=Master.jsp?action=map_drafts> map_drafts</a><br> 
<a href=Master.jsp?action=map_files> map_files</a><br> 
<a href=Master.jsp?action=map_view> map_view</a><br> 
<a href=Master.jsp?action=menu_buttons> menu_buttons</a><br> 
<a href=Master.jsp?action=password_check> password_check</a><br> 
<a href=Master.jsp?action=password_protected_game> password_protected_game</a><br> 
<a href=Master.jsp?action=prop_files> prop_files</a><br> 
<a href=Master.jsp?action=ranking> ranking</a><br> 
<a href=Master.jsp?action=register> register</a><br>
<a href=Master.jsp?action=requirements> requirements</a><br> 
<a href=Master.jsp?action=save_files> save_files</a><br> 
<a href=Master.jsp?action=server_admin> server_admin</a><br> 
<a href=Master.jsp?action=spaceships> spaceships</a><br> 
<a href=Master.jsp?action=start> start</a><br> 
<a href=Master.jsp?action=startpage> startpage</a><br> 
<a href=Master.jsp?action=tomcat_logs> tomcat_logs</a><br> 
<a href=Master.jsp?action=topbar> topbar</a><br> 
<a href=Master.jsp?action=unknown_fragment> unknown_fragment</a><br> 
<a href=Master.jsp?action=user_admin> user_admin</a><br> 
<a href=Master.jsp?action=user_edit> user_edit</a><br> 
<a href=Master.jsp?action=user_list> user_list</a><br> 
<a href=Master.jsp?action=user_saved> user_saved</a><br> 
<a href=Master.jsp?action=user_show> user_show</a><br> 
<a href=Master.jsp?action=view_last_game_log> view_last_game_log</a><br> 
<a href=Master.jsp?action=view_log> view_log</a><br> 
<a href=Master.jsp?action=view_props> view_props</a><br--> 






	
			
</body>
</html>
