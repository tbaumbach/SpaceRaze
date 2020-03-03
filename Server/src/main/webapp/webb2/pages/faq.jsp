m<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="sr.server.ranking.*"%>
<%@ page import="sr.server.*"%>

<%
	// get PageURL
	String PageURL = request.getParameter("action"); 

			User theUser = null;
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


<html>
<head>
<title>SpaceRaze FAQ</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">
</head>

<body background="images/spaze.gif">

<div style="left: 129px;width: 450px;position: absolute;top: 89px;">	
	<div class="Form_Name" style="width:450"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:450"><div class="SolidText"><b>FAQ - Frequently Asked Questions</b></div></div>
	<div class="Form_Text" style="width:450">
	<div class="SolidText">

<table width="438" border="0" cellpadding="0" cellspacing="0">
<tr>
<td class="Mainmenu">

Here some common questions is answered.
<P>
<strong>1. Why does my ships retreat before they even engage in combat?</strong><br>
On all planets each player can set the maximum relative size his ships should engage ("Attack max. size"). 
This is set to "same size" as default to all planets except the players homeplanet, where is is set to "one size larger".
If it is set to "same size" at a planet, it means that a players fleet at that planet will retreat 
immediately if the largest size on 
an enemys fleets ships is larger than his own largest ship. <br>
<!--Here is an example from the manual:<br>
<i>A force whose largest ship is a medium ship retreats immediately if attack max size is set to 
"same size" and the opponent has at least one large ship or bigger.</i>-->
<p>
<strong>2. Why doesn't my ships attack neutral planets?</strong><br>
        For your ships to attack any neutral planet, and its defenders, the Attack 
        If Neutral checkbox in the planet panel for that planet must be checked. 
      <p>
<strong>3. How do I besiege a planet?</strong><br>
        For you to besiege a planet so that it will join you, you must first destroy any neutral or enemy ships at that planet. 
        After that, each turn that you stay at the planet with at least 1 ship with Troops you will lower the planets resistance with one. 
        When the planets resistance reaches zero it capitulates and join you.<br>
        Some VIPs, ships or factions can have bonuses that will lower the resistance faster.<br>
        Bombardment will lower it even more but will also lower the planets production.
      <p>
<strong>4. How do I move my governor to a neutral planet so that he can persuade it to join me?</strong><br>
Your governor can not move directly to a neutral planet but must move there by ship. So for your governor to travel
to a neutral planet, you must first move him to one of your ships at the same planet as the governor, and then order the
ship to the neutral planet. This can be performed in the same turn, since VIP movement is performed before spaceship
movement.<br>
When your governor arrive at the neutral planet you simply move him to the neutral planet itself.
      <p> 
<strong>5. Why does my ships raze many of the planets I try to conquer?</strong><br>
        The default Maximum Bombardment is set to "None" on all planets. If you 
        have changed this to &quot;No limit&quot; or a high value, and you attack 
        a planet with a fleet with a high combined bombardment, it is easy to 
        bombard most planets to smoking ruins in one turn.
      <br>
        If you want to be certain to avoid accidentally razing any planets keep 
        Maximum Bombardment to "none", or if you know the planets production, 
        a sufficiently low number so that you do not lower the planets production 
        to zero or below. 
      <p>
<strong>6. How do you make peace?</strong><br>
        You can't.<br>
		Ships from the three factions will always begin shooting at each other if they are in the same system at the same time.<br>
		There can never be a shared victory of players from different factions.<br>
		If you want to make a temporary alliance with someone from another faction you both just have to make certain that your
		ships never meet in the same system.
      <p>
<strong>7. What does the little scarlet cross (<img src="images/scarlet_cross.gif">) after some planets in the map mean?</strong><br>
        It means that those planets are open.<br>
		If a planet is closed (no scarlet cross) you can't see it's production
and resistance, and neither can you see what size the largest spaceship at the planet is.<br>
Open planets have a bonus on income.
      <p>
<strong>8. Why can't I increase the production on planet X?</strong><br>
        Because the planet X has reached it's maximum production, and can not be increased any more.<br>
		No planet can have a greater production than double the value of it's initial/base production.<br>
		Example: the planet X begins the game with production 2, so it can never be increased higher than 4.
      <p>
<strong>9. Are there any shortcuts to the map?</strong><br>
        If you left-click-and-drag on the map you can move it around.<br>
		If you right-click-and-drag up and down on the map you can zoom in and out.<br>
		If you right-click on a planet in the map, the spaceships list will be shown in the right panel.
      <p>
<strong>10. I can see in the highlights that I have recieved a message, but I can't find where to read it.</strong><br>
        Recieved messages are shown in the Turn Info panel.<br>
        Your own sent messages from previous turns are also shown in the Turn Info panel.<br>
        The Messages panel is only for sending new messages.<br>
      <p>
</td>
</tr>
</table>
	</div>
	</div>
	<div class="List_End"></div>		
	</div>
	

<div style="left: 601px;width: 250px;position: absolute;top: 90px;">
	<%@ include file="../puffs/RightPuff.jsp" %>
</div>
	
</body>
</html>
