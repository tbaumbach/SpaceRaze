<%@page import="java.io.IOException"%>
<%
	if (!tmpUser.isAdmin()){
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