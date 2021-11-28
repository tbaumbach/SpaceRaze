 <%@page import="java.io.IOException"%>
<%@page import="sr.webb.users.User"%>
<%
	System.out.println("checklogin.jsp");
	// the page that includes this code must import java.io.*
	boolean doForward = false;
	User tmpUser = (User)session.getAttribute("user");
	if (tmpUser == null){ // user not logged in...
		doForward = true;
		System.out.println("doForward = true");
	}else{
		System.out.println("doForward = false");
	}
	if (doForward){
//		response.sendRedirect("login.jsp");
		try{
			pageContext.forward("login.jsp");
		}
		catch (ServletException  ioe){
			ioe.printStackTrace();
		}
		catch (IOException  ioe){
			ioe.printStackTrace();
		}
	}
%>