<%@page import="sr.webb.CheckLogin"%>
<%@page import="spaceraze.user.User"%>
<%
	User tmpUser = CheckLogin.getUser(session,request,response);
%>