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
//		title = request.getParameter("title");
//		content = request.getParameter("content");
//		creator = request.getParameter("creator");
//		id = request.getParameter("id");
//		if ((id != null) && (!id.equals(""))){ // save edited article
//			int idInt = Integer.parseInt(id);
//			nh.modifyArticle(title,content,creator,idInt);
//		}else{ // save new article
//			nh.addNewArticle(title,content,creator);
//		}
//		try{
//			response.sendRedirect(response.encodeURL("Master.jsp?action=news_archive"));

//			response.sendRedirect("Master.jsp?action=news_archive");
//			pageContext.forward("Master.jsp?action=news_archive");
//		}
//		catch (ServletException  ioe){
//			ioe.printStackTrace();
//		}
//		catch (IOException  ioe){
//			ioe.printStackTrace();
//		}
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
%>

<script type="text/javascript">
	tinyMCE.init({
		// General options
		mode : "textareas",
		theme : "advanced",
		plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",

		// Theme options
		theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect",
		theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
		theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
		theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_statusbar_location : "bottom",
		theme_advanced_resizing : true,

		// Example content CSS (should be your site CSS)
		content_css : "css/content.css",

		// Drop lists for link/image/media/template dialogs
		template_external_list_url : "lists/template_list.js",
		external_link_list_url : "lists/link_list.js",
		external_image_list_url : "lists/image_list.js",
		media_external_list_url : "lists/media_list.js",

		// Replace values for the template plugin
		template_replace_values : {
			username : "Some User",
			staffid : "991234"
		}
	});
</script>
<form id="formNews" name="formNews" method="post" action="NewsOperator.jsp">
<div style="left: 132px;width: 710px;position: absolute;top: 90px;">	
	<div class="Form_Name"><div class="SolidText">SpaceRaze - Newshandler</div></div>
	<div class="Form_Header"><div class="SolidText"><b>Edit - <%= pageTitle %></b></div></div>
	<div class="Form_Text"  style="width:710"><div class="SolidText">

<input type="hidden" id="action" name="action" value="create_edit_article">
<input type="hidden" id="todo" name="todo" value="Save">
<input type="hidden" id="id" name="id" value="<%= id %>"> <!-- only used when editing -->
Title: <input type="text" class="InputText" id="title" name="title" value="<%= title %>">
<br><br>
Article content:<br>
<textarea name="content" class="InputText" id="content" cols="100" rows="20"><%= content %></textarea>
<br><br>
Posted by: <input type="text" class="InputText" id="creator" name="creator" value="<%= creator %>">
<br>
<p>
<!--input id="todo" name="todo" type="submit" value="Save"-->
<!--input id="todo" name="todo" type="button" onclick="submit();" value="Save"-->

	</div>
	<div class="Form_Header" ALIGN=RIGHT>
	<%if (theUser.isAdmin()){%>
			<A href="#" id="nas" onclick='document.forms["formNews"].submit();'><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_save.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_save.jpg','Save: Save or update Article.','GuideArea');" alt="Save" hspace=0 src="images/btn_save.jpg" vspace=0 border=0></A>
	<% } 
%>
			
	</div></div>
	<div class="List_End"> </div>		
	</div>
	
</form>
<%}%>
