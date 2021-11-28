<%@ page import="sr.webb.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="com.amarantin.imagepack.*"%>
<!DOCTYPE html>
<html>
<head>
<title>Startpage</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%// TODO Not in used, only used from ServerHandler creating HTML list of games. Should remove all both the HTML lists and this JSP page. %>
<%@ include file="checklogin2.jsp" %>
<%
	// check if a ServerHandler exists
	//String found = ServerStatus.checkServerHandler(request,application);
	Object found = application.getAttribute("serverhandler");
	String message = "";
	ServerHandler sh = null;
	if (found == null){
		// if not, create it and store it in the application scope
		// create a new serverhandler
		sh = new ServerHandler();
		application.setAttribute("serverhandler",sh);
		message = "New ServerHandler created";
	}else{
		// if it does, retrieve it
		message = "ServerHandler already exists";
		sh = (ServerHandler)found;
	}
	// handle news
	Object found2 = application.getAttribute("newshandler");
	String message2 = "";
	NewsHandler nh = null;
	if (found2 == null){
		// create a new newshandler
		nh = new NewsHandler();
		application.setAttribute("newshandler",nh);
		message2 = "New NewsHandler created";
	}else{
		message2 = "NewsHandler already exists";
		nh = (NewsHandler)found2;
	}
%>
<body background="images/spaze.gif">
<!--<%= message %><br>-->
<!--<%= message2 %><br>-->
<h2>Your Current Games</h2>
<%= sh.getCurrentPlayingGamesList(tmpUser) %><p>
<h2>Games starting up</h2>
<%= sh.getCurrentOpenGamesList(tmpUser) %><p>
<a href="startpage.jsp">Refresh Lists</a>
<h2>Latest News</h2>
<% 
	List<NewsArticle> allNews = nh.getAllNews();
	int showNr = allNews.size();
	if (allNews.size() > 5){
		showNr = 5;
	}
	if (showNr > 0){
		for (int i = 0; i < showNr; i++){
			NewsArticle na = allNews.get(i);
%>
<%= nh.getNewsArticleHTML(na.getId()) %>
<%
		}
	}else{
%>
No articles exist yet.
<%
	}
%>
</body>
</html>
