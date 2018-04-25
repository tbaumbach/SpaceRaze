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

	// handle different action
	String action = request.getParameter("guideaction");
	String todo = request.getParameter("todo");
	String id = "";
	String pageTitle = "";
	String title = "";
	String content = "";
	String creator = "";
	String dateStr = "";
	String checked = "";
	int published = 0;
	String Publish = "no";	
	
	if ((action != null) && (action.equals("delete"))){
		id = request.getParameter("id");
		int idIntD = Integer.parseInt(id);
		nh.deleteGuide(idIntD);
	}

	if ((todo != null) && (todo.equals("Save"))){

	}else{
		if ((action != null) && (action.equals("new"))){
			pageTitle = "Create New Article";
			creator = theUser.getName();
		}else{
			if ((action != null) && (action.equals("edit"))){ // edit guides article
				pageTitle = "Edit guide";
				id = request.getParameter("id");
				int idInt = Integer.parseInt(id);
				Guide tmpNa = nh.findGuide(idInt);
				title = tmpNa.getTitle();
				content = tmpNa.getContent();
				creator = tmpNa.getCreator();
				dateStr = tmpNa.getCreatedString();
				published=tmpNa.getPublished();
				if( published == 1 )
				{
					checked = "checked";
					Publish = "yes";
				}
			}
		}
%>

<form id="formNews" name="formNews" method="post" action="GuidesOperator.jsp"  ENCTYPE="multipart/form-data">
<div style="left: 132px;width: 718px;position: absolute;top: 90px;">	
	<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze - Guides</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b>Edit - <%= pageTitle %> NOT implemented! DO NOT USE</b></div></div>
	<div class="Form_Text"  style="width:718"><div class="SolidText">

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

	<%if (theUser.isAdmin()){%>
			Published: <input type="checkbox" name="published" value="yes" <%=checked%>>
	<% }else{ %>
			Published: <input type="hidden" name="published" value="<%=published%>">
	<% } %>

<!--input id="todo" name="todo" type="submit" value="Save"-->
<!--input id="todo" name="todo" type="button" onclick="submit();" value="Save"-->

	</div></div>
	<div class="Form_header" ALIGN=RIGHT>
		<div class="SolidText">
	<%if (theUser.isAdmin()){%>
			<A href="#" id="nas" onclick='document.forms["formNews"].submit();'><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_save.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_save.jpg','Save: Save new or update guide.','GuideArea');" alt="Save" hspace=0 src="images/btn_save.jpg" vspace=0 border=0></A>
	<% } %>
	
	<A href="Master.jsp?action=guides_list" id="cas"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Red_cancel.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Red_Over_cancel.jpg','Cancel, return to guide list.','GuideArea');" alt="Save" hspace=0 src="images/btn_Red_cancel.jpg" vspace=0 border=0></A>
	<br><br><br>
	

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
	<br><br><br>
			
	</div></div>
	<div class="List_End"> </div>		
	</div>
	
</form>
<%}%>
