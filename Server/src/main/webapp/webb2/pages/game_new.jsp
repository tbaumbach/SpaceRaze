<%@page import="spaceraze.servlethelper.handlers.GameWorldHandler"%>
<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="spaceraze.world.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>


<!DOCTYPE html>
<html>
<head>
<title>Create new SpaceRaze game</title>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="CSS/style.css">
</head>




<%
	// get PageURL
	String PageURL = request.getParameter("action");

	boolean show = false;
	User theUser = null;
	User tmpUser = null;
	if (theUser == null) {
		theUser = UserHandler.getUser(session, request, response);
		if (theUser.isGuest()) {
			// try to check if player is logged in using the session object
			tmpUser = (User) session.getAttribute("user");
			show = true;
		} else {
			// user is logged in using the session object
			theUser = tmpUser;
		}
	}
%>

<body background="images/spaze.gif">

	<div style="left: 130px; width: 718px; position: absolute; top: 88px;">
		<div class="Form_Name" style="width: 718">
			<div class="SolidText">SpaceRaze</div>
		</div>
		<div class="Form_Header" style="width: 718">
			<div class="SolidText">
				<b>Start a new game - Step 1</b>
			</div>
		</div>
		<div class="Form_Text" style="width: 718">
			<div class="SolidText">


				<form name="form3" id="form3" action="Master.jsp?action=game_new_Step2.jsp">
					<input type="hidden" name="action" value="game_new_Step2">
					<input type="hidden" name="todo" value="Start new game">
					<table>
						<tr>
							<td>Name of game:&nbsp;&nbsp;&nbsp;</td>
							<td><input class="InputText" type="text" name="gamename_new"
								style="width: 300px;" value="" MAXLENGTH=20></td>
						</tr>
						<tr>
							<td>GameWorld:&nbsp;&nbsp;&nbsp;</td>
							<td><select class="InputText" name="gameworld"
								style="width: 300px;">
									<%=GameWorldHandler.getGameWorldOptionsHTML()%>
							</select></td>
						</tr>
						<tr>
							<td>Map:&nbsp;&nbsp;&nbsp;</td>
							<td><select name="mapname" style="width: 300px;"
								class="InputText">
									<%=MapHandler.getMapHTML()%>
							</select></td>
						</tr>
						<tr>
							<td>Password:&nbsp;&nbsp;&nbsp;</td>
							<td><input type="text" class="InputText"
								style="width: 300px;" name="game_password" value="" MAXLENGTH=20>&nbsp;&nbsp;(empty
								= open for all)</td>
						</tr>
						<tr>
							<td>Ranked game:&nbsp;&nbsp;&nbsp;</td>
							<td><input type="checkbox" checked name="ranked" value="yes"></td>
						</tr>
						<tr>
							<td>Autobalance players:&nbsp;&nbsp;&nbsp;</td>
							<td><input type="checkbox" checked name="autobalance"
								value="yes"></td>
						</tr>
						<tr>
							<td>Send e-mail to all players:&nbsp;&nbsp;&nbsp;</td>
							<td><input class="InputText" type="checkbox"
								name="emailplayers" value="yes"></td>
						</tr>
						<tr>
							<td colspan="2">
								<!--input name="todo" type="submit" value="Start new game"-->
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		<div class="Form_Header" ALIGN=RIGHT style="width: 718">
			<div class="SolidText">
				<A href="#" onclick="document.forms['form3'].submit();"><IMG
					onmouseout="OnMouseOverNOut_Image(this,'images/btn_continue.jpg','&nbsp;','GuideArea');"
					onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_continue.jpg','Continue: Go to next step to create a new game.','GuideArea');"
					height=19 alt="Continue" vspace="3" src="images/btn_continue.jpg"
					width=83 border=0></A>
			</div>
		</div>
		<div class="Form_End"></div>
	</div>
	
</body>
</html>
