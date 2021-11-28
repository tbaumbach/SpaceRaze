<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.guides.*"%>
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

	// get guides handler
		GuideHandler nh = (GuideHandler)application.getAttribute("GuideHandler");
	if (nh == null){
		// create a new GuideHandler
		nh = new GuideHandler();
		application.setAttribute("GuideHandler",nh);
	}
	List<Guide> allguides = nh.getAllguides();

	// handle different actionS
	String action = request.getParameter("action");
	String todo = request.getParameter("todo");
	String id = "";
	String pageTitle = "";
	String title = "";
	String content = "";
	String creator = "";
	String dateStr = "";
	String published = "";
	
	if ((action != null) && (action.equals("delete"))){
		id = request.getParameter("id");
		int idIntD = Integer.parseInt(id);
		nh.deleteGuide(idIntD);
	}

	if ((todo != null) && (todo.equals("Save"))){
		title = request.getParameter("title");
		content = request.getParameter("content");
		creator = request.getParameter("creator");
		published = request.getParameter("published");
		id = request.getParameter("id");
		if ((id != null) && (!id.equals(""))){ // save edited article
			int idInt = Integer.parseInt(id);
			nh.modifyGuide(title,content,creator,published,idInt);
		}else{ // save new article
			nh.addNewGuide(title,content,creator);
		}
	}else{
		if ((action != null) && (action.equals("new"))){
			pageTitle = "Create New Article";
			creator = theUser.getName();
		}else{
			if ((action != null) && (action.equals("edit"))){ // edit guides article
				pageTitle = "Edit guides article";
				id = request.getParameter("id");
				int idInt = Integer.parseInt(id);
				Guide tmpNa = nh.findGuide(idInt);
				title = tmpNa.getTitle();
				content = tmpNa.getContent();
				creator = tmpNa.getCreator();
				dateStr = tmpNa.getCreatedString();
			}
		}
	}
	
	response.sendRedirect("Master.jsp?action=guides_list");
%>