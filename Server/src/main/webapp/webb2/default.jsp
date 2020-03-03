<%@ page import="com.amarantin.imagepack.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.properties.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="sr.server.ranking.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title></title>
<link REL="STYLESHEET" HREF="css/style.css">
<style>
<!--
#foldheader{cursor:pointer;cursor:hand ; font-weight:bold ;
list-style-image:url(fold.gif)}

.paulTxtRed
{
    FONT-SIZE: 10px;
    FONT-FAMILY: Tahoma, Verdana, Arial;
    COLOR: #FF0000;
}

.paulTxtGreen
{
    FONT-SIZE: 10px;
    FONT-FAMILY: Tahoma, Verdana, Arial;
    COLOR: #00fe23;
}

.paul2TxtGreen
{
    FONT-SIZE: 25px;
    FONT-FAMILY: Tahoma, Verdana, Arial;
    COLOR: #00fe23;
}

.inpTxtGreen
{
    BORDER-RIGHT: #00fe23 1px solid;
    BORDER-TOP: #00fe23 1px solid;
    BORDER-LEFT: #00fe23 1px solid;
    BORDER-BOTTOM: #00fe23 1px solid;
    BACKGROUND-COLOR: #107110;
    FONT-SIZE: 9px;
    FONT-FAMILY: Tahoma, Verdana, Arial;
    COLOR: #00fe23;
}

.inpCords
{
    BORDER-RIGHT: #000000 1px solid;
    BORDER-TOP: #000000 1px solid;
    BORDER-LEFT: #000000 1px solid;
    BORDER-BOTTOM: #000000 1px solid;
    BACKGROUND-COLOR: #000000;
    FONT-SIZE: 9px;
    FONT-FAMILY: Tahoma, Verdana, Arial;
    COLOR: #00fe23;
}


#foldinglist{list-style-image:url(list.gif)}
.Top{background-color: #B3B3F1;}
.Linkbars{background-color: #9999ff;}
.LinkbarsBorder{background-color: #ffffff;}
.LinkbarsSideBorder{background-color: #9999ff;}
.Sidebars{background-color: #9999ff;}
.Main{background-color: #000000;}
.Statusbar{background-color: #B3B3F1;}
.StatusbarBorder{background-color: #000000;}
.MenuHead{background-color: #055510;FONT-SIZE: 9px;FONT-FAMILY: Arial, Tahoma, Verdana ;COLOR: #00fe23;FONT-WEIGHT: bold;}
.MenuTop{background-color: #003902;FONT-SIZE: 9px;FONT-FAMILY: Arial, Tahoma, Verdana ;COLOR: #00fe23;FONT-WEIGHT: bold;}

.MenuTopGray{background-color: #333333;FONT-SIZE: 9px;FONT-FAMILY: Arial, Tahoma, Verdana ;COLOR: #00fe23;FONT-WEIGHT: bold;}
.TRMainGray{background-color: #666666;}

.MenuBorder{background-color: #00fe23;FONT-SIZE: 9px;FONT-FAMILY: Arial, Verdana, Arial;}

.MenuBorderLine{background-color: #055510;FONT-SIZE: 9px;FONT-FAMILY: Arial, Verdana, Tahoma;}
.MenuBorderLineMiddle{background-color: #00fe23;FONT-SIZE: 9px;FONT-FAMILY: Arial, Verdana, Tahoma;}
.MenuBorderLineBottom{background-color: #107110;FONT-SIZE: 9px;FONT-FAMILY: Arial, Verdana, Tahoma;}
a{COLOR: #00fe23;FONT-FAMILY: Tahoma, Verdana, Arial;}
.MenuMain{FONT-SIZE: 9px;FONT-FAMILY: Tahoma, Verdana, Arial;COLOR: #00fe23;}
.TRMain{background-color: #107110;}


.MenuRedTop{background-color: #551005;FONT-SIZE: 9px;FONT-FAMILY: Tahoma, Verdana, Arial;COLOR: #fe0000;}
.MenuRedBorder{background-color: #fe2300;FONT-SIZE: 9px;FONT-FAMILY: Tahoma, Verdana, Arial;}

.MenuRedBorderLine{background-color: #551005;FONT-SIZE: 9px;FONT-FAMILY: Tahoma, Verdana, Arial;}
.MenuRedBorderLineMiddle{background-color: #fe2300;FONT-SIZE: 9px;FONT-FAMILY: Tahoma, Verdana, Arial;}
.MenuRedBorderLineBottom{background-color: #711010;FONT-SIZE: 9px;FONT-FAMILY: Tahoma, Verdana, Arial;}

.MenuRedMain{FONT-SIZE: 9px;FONT-FAMILY: Tahoma, Verdana, Arial;COLOR: #fe2300;}

.H3{FONT-SIZE: 11px;FONT-FAMILY: Arial, Verdana, Arial;COLOR: #00fe23;}
.H2{FONT-SIZE: 10px;FONT-FAMILY: Arial, Verdana, Arial;COLOR: #24c920;}
.H1{FONT-SIZE: 14px;FONT-FAMILY: Arial, Verdana, Arial;COLOR: #00fe23;}
.TRMainREDRED{background-color: #711010;}

//-->

</style>

<script type="text/javascript" src="js/fader.js"></script>
<SCRIPT>
var helpLayerName = new Array();
var helpLayerText = new Array();

helpLayerName[0]="helpLayer0";

var txtLink="Close";

helpLayerText[0]="";

function showHelpLayerImp(inLayerName,inDivName,inTextName,inWidth)
{if(checkDivLayerVisibility(inLayerName,inDivName) && visibleName==inTextName){showDivLayer(inDivName,inLayerName,0);}else{if((inLayerName != visibleLayer && visibleLayer !="") || (inDivName != visibleDiv && visibleDiv !="")){showDivLayer(visibleDiv,visibleLayer,0);}var HTMLText="<table width='250' border='0' cellspacing='0' cellpadding='0'><tr><td class='MenuBorder' rowspan='13'><img src='/images/space.gif' width='1' height='1'></td><td class='MenuBorder' colspan='3'><img src='/images/space.gif' width='1' height='1'></td><td class='MenuBorder' rowspan='13'><img src='/images/space.gif' width='1' height='1'></td><td><img src='/images/space.gif' width='1' height='1'></td></tr><tr><td class='MenuTop' colspan=3><img src='/images/space.gif' width='4' height='4'></td><td><img src='/images/space.gif' width='4' height='4'></td></tr><tr><td colspan='3' class='MenuTop' width='100%' class='useText3'><b>&nbsp;Mera om:</b></td><td bgcolor='#999999' rowspan='12'><img src='/images/space.gif' width='1' height='1'></tr><tr><td colspan='3' class='MenuBorderLine' width='100%' class='useText3'><IMG height='1' src='spaceblack.gif' width='1'></td></tr><tr><td colspan='3' class='MenuBorderLineMiddle' width='100%' class='useText3'><IMG height='1' src='spaceblack.gif' width='1'></td></tr><tr><td colspan='3' class='.MenuBorderLineBottom' width='100%' class='useText3'><IMG height='1' src='spaceblack.gif' width='1'></td></tr><tr><td colspan='3' class='MenuMain' onMouseOver=this.style.backgroundColor='#ffffff'; onMouseOut=this.style.backgroundColor=''; width='100%' class='useText3'><a href=background.htm>&nbsp;&nbsp;Aura</a></td><tr><td colspan='3' class='MenuMain' onMouseOver=this.style.backgroundColor='#ffffff'; onMouseOut=this.style.backgroundColor=''; width='100%' class='useText3'><a href=background.htm>&nbsp;&nbsp;Chakra</a></td></tr><tr><td colspan='3' class='MenuMain' onMouseOver=this.style.backgroundColor='#ffffff'; onMouseOut=this.style.backgroundColor=''; width='100%' class='useText3'><a href=background.htm>&nbsp;&nbsp;Meditation</a></td></tr><tr><td colspan='3' class='MenuMain' onMouseOver=this.style.backgroundColor='#ffffff'; onMouseOut=this.style.backgroundColor=''; width='100%' class='useText3'><a href=background.htm>&nbsp;&nbsp;Energi</a></td></tr><tr class='menumain'><td colspan=3 class='menumain'><br><br><br><br><br><br><br><br><br><br><br></td></tr><tr><td colspan='3' align=right class='MenuMain' onMouseOver=this.style.backgroundColor='#148f14'; onMouseOut=this.style.backgroundColor=''; width='100%' class='useText3'><a class=AdminLink href='javascript:showDivLayer(\""+inDivName+"\",\""+inLayerName+"\",0);show_arrDivInputField();'>"+txtLink+"&nbsp;</a></td></tr><tr><td colspan='3' class='MenuMain' width='100%' align='right' class='useText3'></td></tr><tr><td class='MenuMain' colspan='3'><img src='/images/space.gif' width='4' height='4'></td></tr><tr><td class='MenuBorder' colspan='5'><img src='/images/space.gif' width='1' height='1'></td></tr><tr><td><img src='/images/space.gif' width='1' height='1'></td><td width=4><img src='/images/space.gif' width='4' height='4'></td><td width=100% bgcolor='#999999'><img src='/images/space.gif' width='1' height='1'></td><td bgcolor='#999999'><img src='/images/space.gif' width='1' height='1'></td><td bgcolor='#999999'><img src='/images/space.gif' width='1' height='1'></td><td bgcolor='#999999'><img src='/images/space.gif' width='1' height='1'></td></tr></table>";setDivLayerText(inDivName,inLayerName,HTMLText);showDivLayer(inDivName,inLayerName,1);visibleLayer=inLayerName;visibleDiv=inDivName;visibleName=inTextName;}
}

var visibleLayer="";
var visibleDiv="";
var visibleName="";

var arrDivInputField="";

var ns4 = (document.layers)? true:false;
var ie4 = (document.all)? true:false;
var ns6 = ((document.getElementById) && (!ie4))? true:false;

if(ns4)
{window.captureEvents(Event.RESIZE);window.onResize=updatewindow;
}

function layerpos() 
{if(ns4){if(eval(document.anchors["ancLocation"])){document.layers.layerLocation.x = document.anchors["ancLocation"].x;document.layers.layerLocation.y = document.anchors["ancLocation"].y;}}if(ns6){if(eval(document.anchors["ancLocation"])){document.getElementById(layerSelect).x = document.anchors["ancLocation"].x;document.getElementById(layerSelect).y = document.anchors["ancLocation"].y;}}
}

function updatewindow()
{if(ns4){layerpos();}
}
function setDivLayerText(inDiv,inLayer,inText)
{
if(ns6){document.getElementById(inDiv).innerHTML = inText;}else if(ie4){document.all(inDiv).innerHTML = inText;}else if(ns4){document.layers[inLayer].document.write(inText);document.layers[inLayer].document.close();}
}

function showDivLayer(inDiv,inLayer,bShow)
{
if(ns6){if(bShow)document.getElementById(inDiv).style.visibility = "Visible";elsedocument.getElementById(inDiv).style.visibility = "hidden";}else if(ie4){if(bShow)document.all(inDiv).style.visibility = "Visible";elsedocument.all(inDiv).style.visibility = "hidden";}else if(ns4){if(bShow)document.layers[inLayer].visibility = "Visible";elsedocument.layers[inLayer].visibility = "hide";}
}

function show_arrDivInputField()
{if(!ns4){for(var i=0;i<arrDivInputField.length;i++){showDivLayer(arrDivInputField[i],arrDivInputField[i],1);}}
}

function getHelpLayerText(inName)
{for(var i=0;i<helpLayerName.length;i++){if(helpLayerName[i]==inName){return helpLayerText[i];}}return "";
}

function checkDivLayerVisibility(inLayer,inDiv)
{	
	if(ns6){
		if(document.getElementById(inDiv).style.visibility == "visible")
			return true;
		else
			return false;
	}else 
	if(ie4){
		if(document.all(inDiv).style.visibility == "visible")
			return true;
		else
			return false;
	}else 
	if(ns4){
		if(document.layers[inLayer].visibility == "show")
			return true;
		else
			return false;
	}
}

</SCRIPT>
<script type="text/javascript">
var x=0
var y=0

function show_coords(event)
{
	document.form1.txtX.value=event.clientX -x;
	document.form1.txtY.value=event.clientY - y;
}
function MoveOrigo(event)
{
	x=event.clientX;
	y=event.clientY;
}
</SCRIPT>
</head>
<!--Background="space3.gif"!-->

<%
	// get action
	String action = request.getParameter("action");
	// handle session/login/logout
	String message = "";
	User theUser = null;
	if (action != null){
		if (action.equals("login")){
			// log in player
			message = UserHandler.loginUser(request,response);		
			if (message.equals("ok")){
				// user loggrd in
				// get a User instance
				String lgn = request.getParameter("login");
				String pwd = request.getParameter("password");
				theUser = UserHandler.getUser(lgn,pwd);
				// also log in player by using session object
				session.setAttribute("user",theUser);
			}
		}else
		if (action.equals("logout")){
			// log out player 
			// remove any session handled by cookies
			message = UserHandler.logoutUser(request,response);		
			// remove any session handled by session object
			session.removeAttribute("user");
		}
	}
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

<body class="main" bgcolor="#000000" background="" onmousedown="MoveOrigo(event);"  onmousemove="show_coords(event)" onLoad="document.forms[0].login.focus()">

<%
	if (theUser.isGuest()){
%>
<!-- user is guest, show login fields -->
<%@ include file="pages/login.jsp" %>
<%
	}else{
%>
<!-- an user is logged in -->
<%@ include file="pages/loggedin.jsp" %>
<%
	}
%>

<br>
<table width='932' border='0' cellspacing='0' cellpadding='0'>
	<tr>
	<td width=13>

<!-- include menu buttons -->
<%@ include file="buttons/menu_buttons.jsp" %>

		<td valign=top align=left width='470'>
		<!--span class=MenuMain>
	Lat=<input class="inpCords" name="txtX" id="txtX" type=text style="width=20;" value="">Long=<input class="inpCords" type=text value="" name="txtY" id="txtY">
</span-->

<%
	if ((action != null) && (action.equals("login"))){
		if ((message != null) && (!message.equals("ok"))){
			session.setAttribute("errorType", "Login Error");
			session.setAttribute("errorMessage", message);
%>
<!-- login error message -->
<%@ include file="pages/error_message_box.jsp" %>
<br>
<%
		}
	}
%>
	
<%-- compute which page should be shown --%>	
<%
	if (action == null || action.equals("start")){
%>
<%@ include file="pages/start.jsp" %>
<%
	}else
	if (action.equals("login")){
		if (theUser.isGuest()){
%>
<!-- action is login and user is guest -->
<%@ include file="pages/start.jsp" %>
<%
		}else{
%>
<!-- action is login and user is logged in -->
<%@ include file="pages/games_current.jsp" %>
<%
		}		
	}else
	if (action.equals("register")){
%>
<!-- action is register -->
<%@ include file="pages/register.jsp" %>
<%
	}else{
%>
<!-- action is unknown: <%= action %> -->
<%@ include file="pages/unknown_fragment.jsp" %>
<%			
	}
%>
	
</td>
</tr>
<tr>
	<td colspan=4 align="center">
		<table>
			<tr>
				<td align="center">
					<font style="font-size:9px;FONT-FAMILY: Tahoma, Verdana, Arial;COLOR: #00fe23">
						Copyleft&nbsp;&nbsp;&nbsp;&nbsp;Version: <%= SR_Server.version %>
					</font>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
<!-- test 
<font color="#FFFFFF">
login: <%= request.getParameter("login") %><br>
password: <%= request.getParameter("password") %><br>
action: <%= action %><br>
message: <%= message %><br>
Logged in user name: <%= theUser.getName() %><br>
</font>
-->

<DIV id="divLocation" style="LEFT: 30px; POSITION: absolute; TOP: 200px" z-index="4"></DIV><LAYER id="layerLocation" z-index="4" height="97" width="205"></LAYER><SCRIPT>
//showHelpLayerImp('layerLocation','divLocation','helpLayer0','175')</SCRIPT>
</form>
</body>
</html>
