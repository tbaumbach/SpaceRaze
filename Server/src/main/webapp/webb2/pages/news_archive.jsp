<%@page import="spaceraze.util.properties.RankingHandler"%>
<%@page import="spaceraze.util.general.RankedPlayer"%>
<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>


<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">
</head>

</head>

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
%>
<body>


<!-- News box -->
<%
	// get news handler
	NewsHandler nh = (NewsHandler)application.getAttribute("newshandler");
	if (nh == null){
		// create a new newshandler
		nh = new NewsHandler();
		application.setAttribute("newshandler",nh);
	}
	LinkedList<NewsArticle> allNews = nh.getAllNews();
	System.out.println("newsnr: " + allNews.size());
%>

<div style="left: 132px;width: 450px;position: absolute;top: 90px;padding-bottom:20px;">

	<div class="Form_Name"><div class="SolidText">News Archive</div></div>
	<div class="List" style="width:450">
				<table style="width:450; border-collapse: collapse; border-spacing: 0;border-width: 0">
<% 
	for (int ij = 0; ij < allNews.size(); ij++){ 
		NewsArticle na_Archive = allNews.get(ij);
%>

<% 
	if (ij > 0){
		} // end if 
%>
<!-- start news item -->
				<tr class="ListheaderRow">
					<td class='ListHeader' width="5">&nbsp;</td>
					<td class="ListHeader"><div class="SolidText"><b><%= na_Archive.getTitle() %></b></div></td>
					<td class='ListHeaderSmall' align=right nowrap><div class="SolidText"><i><%= na_Archive.getCreatedShortString() %></i></div></td>
					<td class='ListHeader'>&nbsp;</td>
				</tr>			
				<tr class='ListTextRow'>
					<td class='ListTextNowLine' width="5">&nbsp;</td>
					<td colspan=2 class="ListTextNowLine" width=440><div class="SolidText"><%= na_Archive.getContent() %></div></td>
					<td class='ListTextNowLine' width="5">&nbsp;</td>
				</tr>
				
				<tr class="ListTextRow">
					<td class='ListTextNowLine' width="5">&nbsp;</td>
					<td class="ListTextNowLine" align="right" colspan="2">
					<div class="SolidText">
					<%if (theUser.isAdmin()){%>
					<A href="Master.jsp?action=create_edit_article&newsaction=edit&id=<%= na_Archive.getId() %>">Edit</a> &nbsp;&nbsp;
					<A href="NewsOperator.jsp?newsaction=delete&id=<%= na_Archive.getId() %>">Delete</a> &nbsp;&nbsp;					
					<%}%>
					</div>
					</td>
					<td class='ListTextNowLine'>&nbsp;</td>
				</tr>			
				
				
<!-- end news item  -->
<% 
	} // end for 
%>
				</table>
	</div>
	
	<div class="Form_Header" ALIGN=RIGHT><div class="SolidText">
	<%if (theUser.isAdmin()){%>
			<A href="Master.jsp?action=create_edit_article&newsaction=new"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_createnew.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_createnew.jpg','Create new: News Article','GuideArea');" alt="Create new News Article" hspace=0 src="images/btn_createnew.jpg" vspace=0 border=0></A>
	<% } 
%>
	</div>
	</div>
	<div class="List_End"> </div>		
	</div>

<div style="left: 601px;width: 250px;position: absolute;top: 90px;">
		<%@ include file="../puffs/RightPuff.jsp" %>
</div>


</body>
</html>
