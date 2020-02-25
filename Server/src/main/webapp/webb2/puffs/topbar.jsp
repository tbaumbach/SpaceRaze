<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Topbar</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="../pages/checklogin2.jsp" %>
<%
	User user = (User)tmpUser;
%>
<body background="images/spaze.gif">
<table width="95%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width=500 valign="middle"><h1>SpaceRaze</h1></td>
    <td>
	<table width="95%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>&nbsp;</td>
    <td>Current version: </td>
    <td><%= SR_Server.version %></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">
		User:
	</td>
    <td valign="top">
		<%= user.getName() %>&nbsp;(<%= user.getRole() %>)
	</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td valign="top">
    	<% if (!user.isGuest()){ %>
		<a href="change_password.jsp" target="mainFrame">Change Password</a>
		<% }%>
		<!--&nbsp;&nbsp;-->
	</td>
    <td valign="top">
		<a href="logout.jsp" target="_top">Log out</a>
	</td>
  </tr>
</table>

	</td>
	<td>
	
<!-- Begin Nedstat Basic code -->
<!-- Title: SpaceRaze 3 -->
<!-- URL: http://www.spaceraze.com/ -->

<script src="http://m1.nedstatbasic.net/basic.js">
</script>
<script>
<!--
 nedstatbasic("ADihBgMy9wRZEb6V/n0am0Mkv1XA", 0);
-->
</script>
<noscript>
<a target="_blank" href="http://www.nedstatbasic.net/stats?ADihBgMy9wRZEb6V/n0am0Mkv1XA"><img
src="http://m1.nedstatbasic.net/n?id=ADihBgMy9wRZEb6V/n0am0Mkv1XA"
border="0" width="18" height="18"
alt="Webstats4U - Free web site statistics
Personal homepage website counter"></a><br>
<a target="_blank" href="http://www.nedstatbasic.net/">Free counter</a>
</noscript>
<!-- End Nedstat Basic code -->
	
	</td>
  </tr>
</table>


</body>
</html>
