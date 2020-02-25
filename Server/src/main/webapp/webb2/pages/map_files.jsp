<%@ page import="sr.webb.*"%>
<%@ page import="sr.mapeditor.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="sr.server.properties.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
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
<%
	String message = null;
	String action = request.getParameter("action");
	if ((action != null) && (action.equals("delete"))){
		String mapName = request.getParameter("mapname");
		message = MapHandler.deleteMap(mapName);
	}
	
	User theUserNO = null;
	
	if (theUserNO == null){
		theUserNO = UserHandler.getUser(session,request,response);
		if (theUserNO.isGuest()){
			// try to check if player is logged in using the session object
			User tmpUserNO = (User)session.getAttribute("user");
			if (tmpUserNO != null){ 
				// user is logged in using the session object
				System.out.println("User logged in using session: " + tmpUserNO);
				theUserNO = tmpUserNO;
			}
		}
	}
	
	
%>
<body background="images/spaze.gif">

<div style="left:130px;width:718px;position: absolute;top:89px;">
		<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>Maps</b>&nbsp;</div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">

			Published Maps - This list contain all the maps that are available in SpaceRaze when starting new games.
			View data and images about the maps available in the SpaceRaze game.
			<br>
			From this page you can also create new maps and edit the maps you have created.
			<% if (message != null){ %>
				<%= message %>
			<% } %>
		</div></div>

		<div class="List" style="width:718px">
			<table border="0" width="716" cellspacing="0" cellpadding="0" class="MenuMain">
			<tr height=1 class='ListLine'><td colspan=8></td></tr>
			<tr class='ListheaderRow' 'height=16'>
				<td class='ListHeader' WIDTH='10'></td>
				<td class='ListHeader'><div class="SolidText">Name</div></td>
				<td class='ListHeader'><div class="SolidText">Filename</div></td>
				<td class='ListHeader'><div class="SolidText"># Planets</div></td>
				<td class='ListHeader'><div class="SolidText">Last changed</div></td>
				<td class='ListHeader'><div class="SolidText">Author</div></td>
				<td class='ListHeader'><div class="SolidText">Edit/Delete*</div></td>
			</tr>

			<%if (!theUserNO.isGuest()){%>
				<%= MapHandler.getMapFiles(theUserNO) %>
			<%	}else{%>
				<%= MapHandler.getMapFilesNO() %>
			<%	}%>

			</table>
		</div>
		<div class="Form_header" ALIGN="RIGHT" style="width:718">
			<div class="SolidText">
				<%if (!theUserNO.isGuest()){%>
				<%	}%>	
				<A href="Master.jsp?action=map_files"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_refreshlist.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_refreshlist.jpg','Refresh: Update page','GuideArea');" alt="Refresh" hspace=0 src="images/btn_refreshlist.jpg" vspace=0 border=0></A>
			</div>
		</div>
		<div class="List_End"></div>	
<%if(!theUserNO.isGuest()){%>		
		<BR>
		
		<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
		<div class="Form_Header" style="width:718"><div class="SolidText"><b>Map Drafts</b>&nbsp;</div></div>
		<div class="Form_Text" style="width:718"><div class="SolidText">
		<br>
* You can only delete maps you have created yourself. You can edit maps that other players have created but you must save them under a new name.
<p>
			The list below show all your saved drafts of maps.
		</div></div>
		
		<div class="List" style="width:718">
		<table border="0" cellspacing="0" cellpadding="0" class="MenuMain" width="716">
		<tr class='ListheaderRow' 'height=16'>
			<td class='ListHeader' WIDTH='10'></td>
			<td class='ListHeader'><div class="SolidText">Name</div></td>
			<td class='ListHeader'><div class="SolidText">Filename</div></td>
			<td class='ListHeader'><div class="SolidText"># Planets</div></td>
			<td class='ListHeader'><div class="SolidText">Last changed</div></td>
			<td class='ListHeader'><div class="SolidText">Author</div></td>
			<td class='ListHeader'><div class="SolidText">Edit/Delete</div></td>
		</tr>

		<%if (!theUserNO.isGuest()){%>
			<%= MapHandler.getMapDraftFilesNO(theUserNO) %>
			<%	if (MapHandler.getNrMapDrafts(theUserNO.getLogin()) == 0){%>
			<%	}%>
		<%	}%>
		</table>
		</div>
			<div class="Form_header" ALIGN="RIGHT" style="width:718">
			<div class="SolidText">
			</div>
		</div>
		<div class="List_End"></div>	
		<%	}%>	
</div>
<br><br>
</body>
</html>
