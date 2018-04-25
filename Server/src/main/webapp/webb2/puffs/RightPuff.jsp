<%@page import="sr.webb.users.User"%>
<%@page import="sr.server.ranking.RankingHandler"%>
<%@page import="sr.server.ranking.RankedPlayer"%>
<%@page import="sr.server.ServerHandler"%>
<%@page import="sr.webb.users.UserHandler"%>
<%@page import="java.util.LinkedList"%>
<%@page import="sr.webb.news.NewsHandler"%>
<%@page import="sr.webb.news.NewsArticle"%>
<%
	if (theUser.isGuest()){
%>
	<div class="Form_name" style="width:250"><div class="SolidText">Register</div></div>
	<div class="Form_Header" style="width:250"><div class="SolidText"><b>New Player?</b></div></div>
	<div class="Form_Text" style="width:250">
		<div class="SolidText">
			To play SpaceRaze you must create a player account.<br> 
			Click on the Register button below to go to the registration page.<br>
		</div>
	</div>
	<div class="Form_header"  style="width:250" ALIGN=RIGHT><div class="SolidText"><A href="Master.jsp?action=register"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_register.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_register.jpg','Register: Create your SpaceRaze account','GuideArea');" height=21 alt="Register" hspace=0 src="images/btn_register.jpg" width=85 vspace=0 border=0></A><BR></div></div>
	<div class="List_End"></div>		
<div style="height:6px;font-size: 6px;">&nbsp;</div>	
<%
	}
		if (theUser.isPlayerOrAdmin()){
		
			// check if a ServerHandler exists
	//String found = ServerStatus.checkServerHandler(request,application);
	Object foundit = application.getAttribute("serverhandler");
	String messageE = "";
	ServerHandler shar = null;
	if (foundit == null){
		// if not, create it and store it in the application scope
		// create a new serverhandler
		shar = new ServerHandler();
		application.setAttribute("serverhandler",shar);
		messageE = "New ServerHandler created";
	}else{
		// if it does, retrieve it
		messageE = "ServerHandler already exists";
		shar = (ServerHandler)foundit;
	}
		
		
%>

	<div class="Form_name" style="width:250"><div class="SolidText">Your Games</div></div>
	<div class="Form_Header" style="width:250"><div class="SolidText"><b>Games List</b></div></div>

		<div class="List" style="width:250;">
			<%= shar.getCurrentPlayingGamesListNOShort(theUser) %>
		</div>
	<div class="Form_header"  style="width:250" ALIGN=RIGHT><div class="SolidText"><A href="Master.jsp?action=games_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_games.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_games.jpg','Register: Create your SpaceRaze account','GuideArea');" height=21 alt="Register" hspace=0 src="images/btn_games.jpg" width=85 vspace=0 border=0></A><BR></div></div>
	<div class="List_End"></div>		
<div style="height:6px;font-size: 6px;">&nbsp;</div>	

<%
	}
%>



	<!-- Ranking box -->
<%
	RankedPlayer[] players = RankingHandler.getRanking();
%>
	
	<div class="Form_name" style="width:250"><div class="SolidText">Ranking</div></div>

		<div class="List" style="width:250">

			<table class="ListTable" cellspacing='0' cellpadding='0' width="100%">
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


	<div class="Form_name" style="width:250"><div class="SolidText">Latest News</div></div>
	<div class="List" style="width:250">
				<table width='100%' border='0' cellspacing='0' cellpadding='0'>
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
					<td class='ListHeaderSmall' align=right nowrap><div class="SolidText"><i><%= naRp.getCreatedShortString() %></i></div></td>
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
	
	<div class="Form_header" style="width:250" ALIGN=RIGHT><div class="SolidText"><A href="Master.jsp?action=news_archive"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_newsarchive.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_newsarchive.jpg','Guide: How to play, good beginners guide!','GuideArea');" alt="How To Play" hspace=0 src="images/btn_newsarchive.jpg" vspace=0 border=0></A></div></div>
	<div class="List_End"></div>	
	
	<%
		if (theUser.isPlayerOrAdmin()){
	%>
	<div style="height:6px;font-size: 6px;">&nbsp;</div>
		<div class="Form_name" style="width:250"><div class="SolidText">SpaceRaze notifier</div></div>	<div class="List" style="width:250">
				<table width='100%' border='0' cellspacing='0' cellpadding='0'>
	
				<tr class='ListTextRow'>
					<td class='ListText' width="5">&nbsp;</td>
					<td colspan=2 class="ListText" width=240><div class="SolidText">The Spaceraze notifier vill tell you when you have an unfinished move in any of your games.<br><a href="notifier/userfiles/notifier_<%= theUser.getLogin() %>.jnlp">Download notifier</a></div></td>
					<td class='ListText' width="5">&nbsp;</td>
				</tr>
				</table>
	</div>
	
	<div class="Form_header" style="width:250" ALIGN=RIGHT></div>
	<div class="List_End"></div>		
	<%
			}
	%>		