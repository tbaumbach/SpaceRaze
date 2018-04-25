<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="sr.server.ranking.*"%>


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

	<div class="Form_name"><div class="SolidText">News Archive</div></div>
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
	
	<div class="Form_header" ALIGN=RIGHT><div class="SolidText">
	<%if (theUser.isAdmin()){%>
			<A href="Master.jsp?action=create_edit_article&newsaction=new"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_createnew.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_createnew.jpg','Create new: News Article','GuideArea');" alt="Create new News Article" hspace=0 src="images/btn_createnew.jpg" vspace=0 border=0></A>
	<% } 
%>
	</div>
	</div>
	<div class="List_End"> </div>		
	</div>


<div style="left: 601px;width: 250px;position: absolute;top: 90px;">
<%
	if (theUser.isGuest()){
%>
	<!-- Register box -->
	<div class="Form_name" style="width:250"><div class="SolidText">Register</div></div>
	<div class="Form_Header" style="width:250"><div class="SolidText"><b>New Player?</b></div></div>
	<div class="Form_Text" style="width:250">
	<div class="SolidText">
			To play SpaceRaze you must create a player account.<br> 
			Click on the Register button below to go to the registration page.<br>
	</div>
	</div>
	<div class="Form_header"  style="width:250" ALIGN=RIGHT><div class="SolidText"><A href="Master.jsp?action=register"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_register.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_register.jpg','Register','GuideArea');" height=21 alt="Register" hspace=0 src="images/btn_register.jpg" width=85 vspace=0 border=0></A><BR></div></div>
	<div class="List_End"></div>		
<%
	}
%>

<div style="height:6px;font-size: 6px;">&nbsp;</div>	

	<!-- Ranking box -->
<%
	RankedPlayer[] players = RankingHandler.getRanking();
%>
	
	<div class="Form_name" style="width:250"><div class="SolidText">Ranking</div></div>

		<div class="List" style="width:250">

			<table class="ListTable" style="width:100%; border-collapse: collapse; border-spacing: 0;border-width: 0">
				<tr class='ListheaderRow' height="16" style="width:250">
					<td class='ListHeader'>&nbsp;</td>
					<td class='ListHeader'><div class="SolidText">Players</div></td>
					<td class='ListHeader' align=Center width=20><div class="SolidText">W</div></td>
					<td class='ListHeader' align=Center width=20><div class="SolidText">F</div></td>
					<td class='ListHeader' align=Center width=20><div class="SolidText">L</div></td>
					<td class='ListHeader' align=Center width=20><div class="SolidText"><b>GP</b></div></td>
					<td class='ListHeader'>&nbsp;</td>
				</tr>
	
				<% 
				for (int i = 0; ((i < players.length) & (i < 5)); i++){ 
					String userName = players[i].getLogin();
					String RowName = i + "ListRow"; 
					
					User statUser = UserHandler.findUser(userName);
					if (statUser != null){
						userName = statUser.getName() + " (" + players[i].getLogin() + ")";
					}
					
					%>
					
					<tr class='' onMouseOver="TranparentRow('<%=RowName%>',7,1);" onMouseOut="TranparentRow('<%=RowName%>',7,0);">
						<td id='<%=RowName%>1' class='ListText'>&nbsp;</td>
						<td id='<%=RowName%>2' class='ListText'><div class="SolidText"><%= i + 1 %>. <%= userName %></div></td>
						<td id='<%=RowName%>3' class='ListText' align=Center><div class="SolidText"><%= players[i].getSoloWin() %></div></td>
						<td id='<%=RowName%>4' class='ListText' align=Center><div class="SolidText"><%= players[i].getFactionWin() %></div></td>
						<td id='<%=RowName%>5' class='ListText' align=Center><div class="SolidText"><%= players[i].getLoss() %></div></td>
						<td id='<%=RowName%>6' class='ListText' align=Center><div class="SolidText"><b><%= players[i].getNrDefeatedPlayers() %></b></div></td>
						<td id='<%=RowName%>7' class='ListText'>&nbsp;</td>
					</tr>
				<% } %>
			</table>
		</div>
		<div class="Form_header" style="width:250" ALIGN=RIGHT><div class="SolidText"><A href="Master.jsp?action=ranking"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_ranking.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_ranking.jpg','Ranking: Spaceraze player ranking','GuideArea');" alt="Ranking" hspace=0 src="images/btn_ranking.jpg" vspace=0 border=0></A></div></div>
		<div class="List_End"></div>

<div style="height:6px;font-size: 6px;">&nbsp;</div>
<!-- News box -->
<%
	// get news handler
	NewsHandler nhRp = (NewsHandler)application.getAttribute("newshandler");
	if (nhRp == null){
		// create a new newshandler
		nhRp = new NewsHandler();
		application.setAttribute("newshandler",nhRp);
	}
	LinkedList<NewsArticle> allNewsRp = nhRp.getAllNews();
	System.out.println("newsnr: " + allNewsRp.size());
%>


	<div class="Form_name">Latest News</div>
	<div class="List" style="width:250">
				<table style="width:100%; border-collapse: collapse; border-spacing: 0;border-width: 0">
<% 
	for (int iRp = 0; iRp < allNewsRp.size(); iRp++){ 
		NewsArticle naRp = (NewsArticle)allNewsRp.get(iRp);
	if (iRp == 4){
			iRp =allNewsRp.size();
		}
%>
<!-- start news item -->
				<tr class="ListheaderRow">
					<td class='ListHeader' width="5">&nbsp;</td>
					<%if (naRp.getTitleShort().length() > 22)
					{%>
						<td class="ListHeader"><div class="SolidText"><b><%= naRp.getTitleShort().substring(0,22) %>...</b></div></td>
					<%}
					else
					{%>
						<td class="ListHeader"><div class="SolidText"><b><%= naRp.getTitleShort() %></b></div></td>
					<%}%>					
					<td class='ListHeaderSmall' align=right nowrap><i><%= naRp.getCreatedShortString() %></i></td>
					<td class='ListHeader'>&nbsp;</td>
				</tr>			
				<tr class='ListTextRow'>
					<td class='ListText' width="5">&nbsp;</td>
					<td colspan=2 class="ListText" width=240><div class="SolidText"><%= naRp.getContentShort() %></div></td>
					<td class='ListText' width="5">&nbsp;</td>
				</tr>
				<tr class='ListTextRow'>
					<td class='ListText' width="5">&nbsp;</td>
					<td colspan=2 class="ListText" width=240 align="right" style="padding-bottom:5px"><div class="SolidText"><a href="Master.jsp?action=news_archive">Läs Mer >></a></div></td>
					<td class='ListText' width="5">&nbsp;</td>
				</tr>
				
<!-- end news item  -->
<% 
	} // end for 
%>
				</table>
	</div>
	
	<div class="Form_header" ALIGN=RIGHT><A href="Master.jsp?action=news_archive"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_newsarchive.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_newsarchive.jpg','Guide: How to play, good beginners guide!','GuideArea');" height=17 alt="How To Play" hspace=0 src="images/btn_newsarchive.jpg" width=81 vspace=0 border=0></A></div>
	<div class="List_End"></div>		
			
</div>


</body>
</html>
