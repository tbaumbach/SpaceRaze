	<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.guides.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<title>View Map files</title>
<meta charset="UTF-8">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">


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

<%
	// get PageURL
	String PageURL = request.getParameter("action"); 

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
	List<Guide> allNewsRp = nh.getAllguides();
	System.out.println("newsnr: " + allNewsRp.size());
%>
		
		
	<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>Guides</b>&nbsp;</div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
			Published Guides - This list contains all the guides written for Spaceraze, they are one of the best ways to learn how to PLAY, how to WIN and how to Master Spaceraze
			<br>
			From this page you can also create new Guides and edit the Guides you have created.
			
		</div></div>
		<div class="List" style="width:718">
			<table class="ListTable">
	<tr height=1 class='ListLine'><td colspan=8></td></tr>
			<tr class='ListheaderRow' height=16>
				<td class='ListHeader' WIDTH='10'></td>
				<td class='ListHeader'><div class="SolidText">Guidename</div></td>
				<td class='ListHeader'><div class="SolidText"># Reads</div></td>
				<td class='ListHeader'><div class="SolidText">Last changed</div></td>
				<td class='ListHeader'><div class="SolidText">Created</div></td>
				<td class='ListHeader'><div class="SolidText">Author</div></td>
				<td class='ListHeader'><div class="SolidText">Edit/Delete*</div></td>
			</tr>

			
		
	
	
<% 
String Name= "GuideList";
	for (int iRp = 0; iRp < allNewsRp.size(); iRp++){ 
		Guide naRp = allNewsRp.get(iRp);
		String RowName= iRp + Name;
%>
<!-- start news item -->
<%if (theUser.isAdmin() && naRp.getPublished() !=1 || naRp.getPublished() == 1) {%>

  <tr class='ListTextRow' onMouseOver="TranparentRow('<%=RowName%>',7,1);" onMouseOut="TranparentRow('<%=RowName%>',7,0);" onclick="location.href='Master.jsp?action=show_guide&GuideID=<%= naRp.getId()%>'">
	<td id='<%=RowName%>1' class='ListText'></td>
	<td id='<%=RowName%>2' class='ListText'><div class="SolidText"><a href="#"><%=naRp.getTitleShort()%></a></div></td>
	<td id='<%=RowName%>3' class='ListText'><div class="SolidText"><%= naRp.getReads() %></div></td>
	<td id='<%=RowName%>4' class='ListText'><div class="SolidText"><%= naRp.getLastModifiedString() %></div></td>
	<td id='<%=RowName%>5' class='ListText'><div class="SolidText"><%= naRp.getCreatedShortString() %></div></td>
	<td id='<%=RowName%>6' class='ListText'><div class="SolidText"><%= naRp.getCreator() %></div></td>
	<td id='<%=RowName%>7' class='ListText'><div class="SolidText">
		<%if (theUser.isAdmin()){%>
			<a href="Master.jsp?action=create_edit_guide&guideaction=edit&id=<%= naRp.getId()%>">Edit</a>
		<% } %>
	</div></td>
	
				</tr>			
<% } %>
				
<!-- end news item  -->
<% 
	} // end for 
%>
				</table>
	</div>
	
	<div class="Form_Header" ALIGN="RIGHT" style="width:718">
	<div class="SolidText">
		<%if (theUser.isAdmin()){%>
			<A href="Master.jsp?action=create_edit_guide&guideaction=new"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_createnew.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_createnew.jpg','Create new: News Article','GuideArea');" alt="Create new News Article" hspace=0 src="images/btn_createnew.jpg" vspace=0 border=0></A>&nbsp;&nbsp;&nbsp;&nbsp;
	<% } %>
	<A href="Master.jsp?action=guides_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_refreshlist.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_refreshlist.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_refreshlist.jpg" vspace=0 border=0></A>
	</div></div>
	<div class="List_End"></div>	

</div>
</body>
</html>