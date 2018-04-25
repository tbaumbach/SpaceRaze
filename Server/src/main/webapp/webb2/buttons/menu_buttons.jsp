<!-- Start menu_buttons.jsp fragment -->


<div style="left: 20px;width: 100px;position: absolute;top: 115px;line-height:20px;">

	<table cellpadding="0" cellspacing="0">
	<tr><td><SPAN class="h2">&nbsp;</SPAN><BR></td></tr>
	<tr><td style="padding-top:5px;"><A href="Master.jsp"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_home.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_home.jpg','Home: get a brief view of Spaceraze news and Statistcs','GuideArea');" height=21 alt="Home" hspace=0 src="images/btn_Blue_home.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><a href="Master.jsp?action=news_archive"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_newsarchive.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_newsarchive.jpg','News Archive: Read all Spaceraze related news.','GuideArea');" height=21 alt="News Archive" hspace=0 src="images/btn_Blue_newsarchive.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><A href="Master.jsp?action=requirements"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_requirements.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_requirements.jpg','Requirements: What do you need to play SpaceRaze.','GuideArea');" height=21 alt="Requirements" hspace=0 src="images/btn_Blue_requirements.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td style="padding-top:22px;">
		<% if (!theUser.isGuest()){ %>
	 	   <A href="Master.jsp?action=game_new"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Red_newgame.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Red_Over_newgame.jpg','New Game: This is where you start up new SpaceRaze games','GuideArea');" height=21 alt="Create a new Game!" hspace=0 src="images/btn_Red_newgame.jpg" width=85 vspace=0 border=0></A>
		<%}else{%>
			<A href="Master.jsp?action=register"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Red_register.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Red_Over_register.jpg','<b>Register:</b> this is where you where you get your SpaceRaze account.','GuideArea');" height=21 alt="Register your own account!" hspace=0 src="images/btn_Red_register.jpg" width=85 vspace=0 border=0></A>
		<%}%>
	</td></tr>
	<tr><td style="padding-top:41px;"><A href="Master.jsp?action=games_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_games.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_games.jpg','Games: List all active, new  or games starting up. ','GuideArea');" height=21 alt="Games List" hspace=0 src="images/btn_Blue_games.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><A href="Master.jsp?action=ranking"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_ranking.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_ranking.jpg','Ranking: List all players SpaceRaze ranking','GuideArea');" height=21 alt="Ranking" hspace=0 src="images/btn_Blue_ranking.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><A href="Master.jsp?action=map_files"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_maps.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_maps.jpg','Maps: List, Create and view all maps that are active in SpaceRaze ','GuideArea');" height=21 alt="Maps" hspace=0 src="images/btn_Blue_maps.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><A href="Master.jsp?action=gameworlds_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_gameworlds.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_gameworlds.jpg','Gameworlds: Read history and get all facts about each GW','GuideArea');" height=21 alt="Gameworlds" hspace=0 src="images/btn_Blue_gameworlds.jpg" width=85 vspace=0 border=0></A></td></tr>
	<!-- Fungerar inte och ingen bra ide att ha på serven.
	<tr><td><A href="Master.jsp?action=battle_sim_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_battlesim.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_battlesim.jpg','Battle sim: Simulate epic or future battles.','GuideArea');" height=21 alt="Battle simulator" hspace=0 src="images/btn_Blue_battlesim.jpg" width=85 vspace=0 border=0></A></td></tr>
	-->
	<!-- 
	<tr><td style="padding-top:43px;"><A href="Master.jsp?action=guides_list"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_guides.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_guides.jpg','Guides: Read other players guides or write your own ','GuideArea');" height=21 alt="Guides" hspace=0 src="images/btn_Blue_guides.jpg" width=85 vspace=0 border=0></A></td></tr>
	 -->
	<tr><td style="padding-top:63px;"><A href="Master.jsp?action=manual"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_manual.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_manual.jpg','Manual: The Bible of Spaceraze, or ','GuideArea');" height=21 alt="Manual" hspace=0 src="images/btn_Blue_manual.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><A href="Master.jsp?action=faq"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_faq.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_faq.jpg','FAQ: Frequently Asked Questions, get your answers here.','GuideArea');" height=21 alt="FAQ" hspace=0 src="images/btn_Blue_faq.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><A href="Master.jsp?action=screenshots"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_screenshots.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_screenshots.jpg','Screenshots: View ingame Screenshots of Spaceraze','GuideArea');" height=21 alt="Screenshots" hspace=0 src="images/btn_Blue_screenshots.jpg" width=85 vspace=0 border=0></A></td></tr>
	
	<tr><td style="padding-top:70px;"><A href="Master.jsp?action=versions"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_changelog.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_changelog.jpg','Change Log: View the latest changes of Spaceraze!','GuideArea');" height=21 alt="Change log" hspace=0 src="images/btn_Blue_changelog.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><A href="Master.jsp?action=history"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_history.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_history.jpg','History: The history of Spaceraze','GuideArea');" height=21 alt="History" hspace=0 src="images/btn_Blue_history.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><A href="Master.jsp?action=contact"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_contact.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_contact.jpg','Contact: Contact the creators of Spaceraze','GuideArea');" height=21 alt="Contact" hspace=0 src="images/btn_Blue_contact.jpg" width=85 vspace=0 border=0></A></td></tr>
	<tr><td><A href="Master.jsp?action=credits"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_credits.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_credits.jpg','Credits: Great work!!','GuideArea');" height=21 alt="Credits" hspace=0 src="images/btn_Blue_credits.jpg" width=85 vspace=0 border=0></A></td></tr>
		<tr><td>

		
	<% if (theUser.isAdmin()){ %>
	<br>
	<span class=h2>Admin</span><br>
	<A href="Master.jsp?action=admin_users"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_adminusers.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_adminusers.jpg','Admin Users:','GuideArea');" height=21 alt="Administrate Users" hspace=0 src="images/btn_Blue_adminusers.jpg" width=85 vspace=0 border=0></A><BR>
	<!-- Kan inte hitta dessa sidor, inte utvecklade eller ligger de på någon server, leta lite i gamla tomtcats
	<A href="Master.jsp?action=admin_games"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_admingames.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_admingames.jpg','Admin games:','GuideArea');" height=21 alt="Administrate Games" hspace=0 src="images/btn_Blue_admingames.jpg" width=85 vspace=0 border=0></A><BR>
	<A href="Master.jsp?action=admin_mail"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_adminmail.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_adminmail.jpg','Admin Mail:','GuideArea');" height=21 alt="Administrate Mail" hspace=0 src="images/btn_Blue_adminmail.jpg" width=85 vspace=0 border=0></A><BR>
	<A href="Master.jsp?action=admin_saves"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_adminsaves.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_adminsaves.jpg','Admin Saves:','GuideArea');" height=21 alt="Administrate Saves" hspace=0 src="images/btn_Blue_adminsaves.jpg" width=85 vspace=0 border=0></A><BR>
	<A href="Master.jsp?action=admin_properties"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_adminproperties.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_adminproperties.jpg','Admin Properties:','GuideArea');" height=21 alt="Administrate Properties" hspace=0 src="images/btn_Blue_adminproperties.jpg" width=85 vspace=0 border=0></A><BR>
	<A href="Master.jsp?action=admin_hash"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Blue_adminhash.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Blue_Over_adminhash.jpg','Admin Statistics','GuideArea');" height=21 alt="Administrator Statistics" hspace=0 src="images/btn_Blue_adminhash.jpg" width=85 vspace=0 border=0></A><BR>
	-->
	<% } %>
		</td></tr>
		</table>

	</div>
<!-- End menu_buttons.jsp fragment -->
