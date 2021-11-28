<%@page import="sr.webb.CheckLogin"%>
<%@page import="sr.webb.users.User"%>
<%
	User tmpUser = CheckLogin.getUser(session,request,response);
%>