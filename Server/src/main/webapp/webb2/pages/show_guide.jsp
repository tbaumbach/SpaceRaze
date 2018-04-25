<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.guides.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>View Map files</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">



<%
	// get PageURLs
	String PageURL = request.getParameter("action"); 
	String GuideID = request.getParameter("GuideID"); 

	boolean show = false;
	User theUser = null;
//	User tmpUser = null;
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

	GuideHandler nh = (GuideHandler)application.getAttribute("GuideHandler");
	if (nh == null){
		// create a new GuideHandler
		nh = new GuideHandler();
		application.setAttribute("GuideHandler",nh);
	}
%>


<style type="text/css">
table.sr td {
	border-width: 0px;
	padding: 0px;
	background-color: #552500;
}
</style>

</head>

<body background="images/spaze.gif">

<div style="left:130px;width:718px;position: absolute;top: 88px;">
		<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze - Guides</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>Guides</b>&nbsp;</div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
			<%=nh.getGuideHTMLNO(Integer.parseInt(GuideID))%>
		</div></div>
	<div class="Form_header" ALIGN="RIGHT" style="width:718">
	<div class="SolidText">
	<%if (theUser.isAdmin()){%>
			<a href="Master.jsp?action=create_edit_guide&guideaction=edit&id=<%= GuideID%>"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_edit.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_edit.jpg','Update guide.','GuideArea');" alt="Create new News Article" hspace=0 src="images/btn_edit.jpg" vspace=0 border=0></A>&nbsp;&nbsp;&nbsp;&nbsp;
		<% } %>
		<%if (theUser.isAdmin()){%>
			<A href="Master.jsp?action=create_edit_guide&guideaction=new"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_createnew.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_createnew.jpg','Create new: News Guide','GuideArea');" alt="Create new News Article" hspace=0 src="images/btn_createnew.jpg" vspace=0 border=0></A>&nbsp;&nbsp;&nbsp;&nbsp;
	<% } %>
	<A href="Master.jsp?action=guides_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_guides.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_guides.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_guides.jpg" vspace=0 border=0></A>
	</div></div>
	<div class="List_End"></div>	
</div>		
</div>		
		