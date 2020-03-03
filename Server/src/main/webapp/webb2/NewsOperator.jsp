<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%
	// get PageURL
	String PageURL = request.getParameter("action"); 

	boolean show = false;
	User theUser = null;
//	User tmpUser = null;
	if (theUser == null){
		theUser = UserHandler.getUser(session,request,response);
		if (theUser.isGuest()){
			// try to check if player is logged in using the session object
			User tmpUser = (User)session.getAttribute("user");
			if (tmpUser != null){ 
				// user is logged in using the session object
				System.out.println("User logged in using session: " + tmpUser);
				theUser = tmpUser;
			}
		}
	}

	// get news handler
	NewsHandler nh = (NewsHandler)application.getAttribute("newshandler");
	List<NewsArticle> allNews = nh.getAllNews();

	// handle different action
	String action = request.getParameter("newsaction");
	String todo = request.getParameter("todo");
	String id = "";
	String pageTitle = "";
	String title = "";
	String content = "";
	String creator = "";
	String dateStr = "";
	
	if ((action != null) && (action.equals("delete"))){
		id = request.getParameter("id");
		int idIntD = Integer.parseInt(id);
		nh.deleteArticle(idIntD);
	}

	if ((todo != null) && (todo.equals("Save"))){
		title = request.getParameter("title");
		content = request.getParameter("content");
		creator = request.getParameter("creator");
		id = request.getParameter("id");
		if ((id != null) && (!id.equals(""))){ // save edited article
			int idInt = Integer.parseInt(id);
			nh.modifyArticle(title,content,creator,idInt);
		}else{ // save new article
			nh.addNewArticle(title,content,creator);
		}
	}else{
		if ((action != null) && (action.equals("new"))){
			pageTitle = "Create New Article";
			creator = theUser.getName();
		}else{
			if ((action != null) && (action.equals("edit"))){ // edit news article
				pageTitle = "Edit news article";
				id = request.getParameter("id");
				int idInt = Integer.parseInt(id);
				NewsArticle tmpNa = nh.findNewsArticle(idInt);
				title = tmpNa.getTitle();
				content = tmpNa.getContent();
				creator = tmpNa.getCreator();
				dateStr = tmpNa.getCreatedString();
			}
		}
	}
	
	response.sendRedirect("Master.jsp?action=news_archive");
%>