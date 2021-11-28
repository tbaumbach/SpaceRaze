<%@ page import="sr.server.map.*"%>
<!DOCTYPE html>
<html>
<head>
<title>View Map Drafts</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">

<style type="text/css">
table.sr td {
	border-width: 0px;
	padding: 0px;
	background-color: #552500;
}
</style>

</head>
<%@ include file="checklogin2.jsp" %>
<%
	// denna del anv�nds inte???
	String message = "";
	// maybe reload maps 
	String reloadMaps = request.getParameter("reload");
	if (reloadMaps != null){
//		MapHandler.reloadMaps();
		message = "Maps has been reloaded";
	}
%>
<body background="images/spaze.gif">
<h2>Map Drafts</h2>
The list below show all your saved drafts of maps.
<font color="#00FF00">
<%= message %>
</font>
<br>
<hr color="#FFBF00">


<table class="ListTable">
<tr>
<td width="120">Name</td>
<td width="120"># Planets</td>
<td width="140">Last changed</td>
<td width="120">Edit</td>
</tr>
<tr>
<td colspan="4" height="1"><img src="images/yellow_pix.gif" width="512" height="1"></td>
</tr>

<%= MapHandler.getMapDraftFiles(tmpUser) %>

</table>
<%	if (MapHandler.getNrMapDrafts(tmpUser.getLogin()) == 0){%>
&nbsp;You have no map drafts
<%	}%>

<hr color="#FFBF00">

</body>
</html>
