<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="sr.mapeditor.*"%>
<%@ page import="spaceraze.world.*"%>
<%@ page import="sr.server.properties.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>View Map files</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>




<%
	String mapName = request.getParameter("mapname");
	Map aMap = MapHandler.getMap(mapName);
	
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

<h2><%= aMap.getNameFull() %></h2>
Filename: <%= aMap.getFileName() %><br>
Author: <%= aMap.getAuthorName() %><br>
<hr color="#FFBF00">
Description: <%= aMap.getDescription() %><br>
Recommended # of players: <%= aMap.getMaxNrStartPlanets() %><br>
Created date: <%= aMap.getCreatedDate() %><br>
Changed date: <%= aMap.getChangedDate() %><br>
# Planets: <%= aMap.getNrPlanets() %><br>
# Connections: <%= aMap.getNrConnections() %> (<%= aMap.getNrConnectionsShort() %> short and <%= aMap.getNrConnectionsLong() %> long connections)<br>
Average # of connections per planet: <%= aMap.getAverageNrConnectionsString() %><br>

<!--
<% if (theUserNO.isUser(aMap.getAuthorLogin())){ %>
<hr color="#FFBF00">
<a href="../applet/MapEditor.jsp?action=<%= MapEditorApplet.LOAD_PUB %>&mapname=<%= aMap.getFileName() %>" target="_top">Edit this map</a><br>
<% } %>
-->
<hr color="#FFBF00">
<%= MapHandler.showMapNO(mapName) %>
<hr color="#FFBF00">
</div></div>
		<div class="Form_header" ALIGN="RIGHT" style="width:718"><div class="SolidText"></div>
			<div class="SolidText"><A href="Master.jsp?action=map_files"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_maps.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_maps.jpg','View Maps','GuideArea');" alt="Refresh" hspace=0 src="images/btn_maps.jpg" vspace=0 border=0></A></div>
		</div>
		<div class="List_End"></div>	

</div>
</body>
</html>
