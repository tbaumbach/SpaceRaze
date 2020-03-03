<%@page import="sr.webb.users.UserHandler"%>
<%@page import="sr.webb.users.User"%>
<%@page import="java.io.IOException"%>
<%
	User userAdminOnly = session.getAttribute("user") != null ? (User)session.getAttribute("user") : UserHandler.getUser(session,request,response);
	if (!userAdminOnly.isAdmin()){
		try{
			pageContext.forward("unauthorized.htm");
		}
		catch (ServletException  ioe){
			ioe.printStackTrace();
		}
		catch (IOException  ioe){
			ioe.printStackTrace();
		}
	}
%>