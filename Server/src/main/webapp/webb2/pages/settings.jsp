<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.amarantin.imagepack.*"%>
<!DOCTYPE html>
<html>
<head>
<title>SpaceRaze change password Page</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">

<script>
	function validate(){
		retVal = false;
		if (chapassform.password1.value == ""){
			alert("New password can not be empty");
		}else
		if (chapassform.password2.value == ""){
			alert("New password can not be empty");
		}else
		if (chapassform.password1.value != chapassform.password2.value){
			alert("Fields does not contain the same word");
		}else{
			retVal = true;
		}
		return retVal;
	}
</script>
</head>
<%
	String ok = request.getParameter("ok");
	if ((ok != null) && (ok.equals("true"))){
		// change the password
		String password = request.getParameter("password1");
		User tmpUser = (User)session.getAttribute("user");
		if (tmpUser != null){ // user not logged in...
			ServerHandler sh = (ServerHandler)application.getAttribute("serverhandler");
			UserHandler.newPassword(tmpUser.getLogin(),password,sh);
		}
	}
%>
<% if ((ok != null) && (ok.equals("true"))){ %>
<body background="images/spaze.gif">
<% } else { %>
<body background="images/spaze.gif" onLoad="document.forms[0].password1.focus()">
<% } %>

<div style="left:130px;width:718px;position: absolute;top: 89px;">
 <div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
 <div class="Form_Header" style="width:718"><div class="SolidText"><b>Settings:</b>&nbsp;</div></div>
 <div class="Form_Text" style="width:718"><div class="SolidText">
				Settings - 
		</div></div>
 <div class="Form_Header" style="width:718"><div class="SolidText"><b>Settings:</b>&nbsp;</div></div>
<div class="Form_Text" style="width:718"><div class="SolidText">

<!-- <%= ok %> -->
<table class="ListTable">
<tr>
<td>
<% if((ok != null) && (ok.equals("true"))){ %>
<h2>Password Changed</h2>
<% } else { %>
<h2>Change Password</h2>
</td>
</tr>
<tr>
<td>
<form name="chapassform" action="Master.jsp" method="post" onSubmit="return validate();">
<input type="hidden" name="ok" value="true">
<input type="hidden" name="action" value="change_password">

<table>
<tr>
<td>
	Old password:
</td>
<td>
	<input type="password" name="passwordOLD" class="InputText">
</td>
</tr>

<tr>
<td>
	New password:
</td>
<td>
	<input type="password" name="password1" class="InputText">
</td>
</tr>
<tr>
<td>
	Repeat new password:
</td>
<td>
	<input type="password" name="password2" class="InputText">
</td>
</tr>
<tr>
<td colspan="2" align="center">
	&nbsp;
</td>
</tr>
<tr>
<td colspan="2">
</td>
</tr>
</table>
</form>
<% } %>
</td>
</tr>
</table>
</div></div>
		<div class="Form_Header" ALIGN=RIGHT style="width:718"><div class="SolidText">
		<% if((ok != null) && (ok.equals("true"))){ %>
			<A href="Master.jsp" onclick=""><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_continue.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_continue.jpg','Continue: Go to next step to create a new game.','GuideArea');" height=19 alt="Continue" hspace="3" src="images/btn_continue.jpg" width=83 vspace="3" border=0></A>

		<% } else { %>
			<A href="#" onclick="document.forms['chapassform'].submit();"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_continue.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_continue.jpg','Continue: Go to next step to create a new game.','GuideArea');" height=19 alt="Continue" hspace="3" src="images/btn_continue.jpg" width=83 vspace="3" border=0></A>
		<% } %>
		</div></div>		
		<div class="List_End"></div>	
</div>

</body>
</html>
