<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.amarantin.imagepack.*"%>
<!DOCTYPE html>

<script>
	function validate(){
		retVal = false;
		if (passform.game_password.value == ""){
			alert("Password can not be empty");
		}else{
			retVal = true;
		}
		return retVal;
	}
</script>

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

	String gameId = request.getParameter("port");
	String returnPage = request.getParameter("returnto");
	
%>
<form id="passiform" name="passiform" action="Master.jsp"  method="post" onSubmit="return validate();">
<input type="hidden" name="action" value="password_check">
<input type="hidden" name="port" value="<%= gameId %>">
<input type="hidden" name="autouser" value="true">
<input type="hidden" name="returnto" value="<%= returnPage %>">

<div style="left: 129px;width: 450px;position: absolute;top: 89px;padding-bottom:20px;">	
	<div class="Form_Name" style="width:450"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:450"><div class="SolidText"><b>Game Protected by password</b></div></div>
	<div class="Form_Text" style="width:450"><div class="SolidText">
<table class="ListTable">
<tr>
<td>
<h2>Enter game password</h2>
This game is protected, please enter the game's password and press Join.
</td>
</tr>
<tr>
<td>
<table>
<tr>
<td>
	Password:
</td>
<td>
	<input type="password" name="game_password">
</td>
</tr>
<tr>
<td colspan="2" align="center">
	&nbsp;
</td>
</tr>
<tr>
<td colspan="2">
<!--input type="image" src="http://localhost:8080/SpaceRaze/webb2/images/btn_login.jpg"-->
</td>
</tr>
</table>


</td>
</tr>
</table>
</div></div>
	<div class="Form_Header" ALIGN=RIGHT>
					<div class="SolidText"><A  href="#" id="nas" onclick='document.forms["passiform"].submit();'><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_continue.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_continue.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_continue.jpg" vspace=0 border=0></A></div>
	</div>
	<div class="List_End"></div>		
			
</div>
</form>
