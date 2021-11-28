<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="sr.server.ranking.*"%>


<%
	// get PageURL
	String PageURL = request.getParameter("action"); 

	boolean show = false;
	User theUser = null;
	User tmpUser = null;
	if (theUser == null){
		theUser = UserHandler.getUser(session,request,response);
		if (theUser.isGuest()){
			// try to check if player is logged in using the session object
			tmpUser = (User)session.getAttribute("user");
			show = true;
		}
		else
		{ 
				// user is logged in using the session object
				theUser = tmpUser;
		}
	}


%>

<!DOCTYPE html>
<html>
<head>
<title>History</title>
<meta charset="UTF-8">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">
</head>

<body background="images/spaze.gif">

<div style="left:130px;width:718px;position: absolute;top: 88px;padding-bottom:20px;">
	<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b>History</b></div></div>
	<div class="Form_Text" style="width:718;">
	<div class="SolidText">
<table class="ListTable">
<tr>
<td>
<b>History of SpaceRaze</b><br>
The SpaceRaze game has been around since the late nineties, and has been through a few makeovers over the years. This page gives a brief summary of the history of the SpaceRaze game. 
<br><br><b>Raze play by phone</b><br>
The "Raze" part of SpaceRaze comes from a homemade play by phone game that Paul Bodin was gamemaster for in 1995(?).<br>
It was a fantasy strategy game with 10 players where one turn was performed per week, where all players phoned in to recieve information of the last turn and to give new orders.<br> 
In Raze countries could be "Razed" by enemy armies and those countries could not be used after that for the duration of the game.<br>

<br><br><b>SpaceRaze tabletop game</b><br>
The first SpaceRaze game saw the light of day in 1997 as a tabletop game, with a large paper starmap and markers cut from a pizza box for the different units of the game - spaceships, starfighters (there were a lot of these...), planets, wharfs etc.<br>
The game was played by 4-6 people and no single game was ever finished due to the amount of time the game took to play as the complexity increased with each turn.<br>
Most of the game was created by Paul Bodin and Mats Johansson, with the added help from Robert Weitz, Roger Heinänän, Savina Fornsäter and Otto Björkström.

<br><br><b>SpaceRaze play by mail</b><br>
In 1998 Mats Johansson took SpaceRaze to a new level as he transformed it into a play by mail game, where the central information about the state of the game was a continually updated website, where a hard-coded starmap (see image below) and <a href="issue5.htm">newspaper</a> about the progress of the game was kept.<br>
The play by mail version of SpaceRaze was played successfully 1 1/2 time (the second game was never finished) with 6 and 9 players respectively participating.
<p>
<img src="images/quadrant_small.gif">
<br>
The image shows a part of the starmap from the play by mail game.<br>

<br><br><b>SpaceRaze Galaxy</b><br>
The first computer-based SpaceRaze was developed in the autumn 1998 by Paul Bodin, who developed the game as a way to learn client/server programming in Java.<br>
The game used different gameplay than the previous versions and was, unfortunately, not as fun. It also used an over-enthusiastic data model which proved very complex and bug-prone.<br>
This game never survived beyond beta-testing and the looong bug-lists they produced...  

<br><br><b>SpaceRaze 2</b><br>
The second computer-based version of SpaceRaze returned to the successful gameplay 
      developed in the boardgame and play by phone versions, and used the graphical 
      look of the map from the play by mail version. <br>
	  From SpaceRaze Galaxy it kept the better part of the client/server architecture and 3D map mechanics among other things.<br>
No unnessecary time, however, was spent with the GUI part of the game and it had an unmistakeable windowish feel to it.<br>
This version was developed in 2002 by Paul Bodin and was never played beyond testing.
<p>
<img src="images/srg_small.gif">
<br>
The image shows the SpaceRaze 2 GUI and star map.<br>

<br><br><b>SpaceRaze 3</b><br>
In 2003 Paul Bodin made a new better looking GUI as a way to learn how to use Java Swing components, and continued polishing on the server and game-engine from SpaceRaze 2.<br>
<p>
<img src="images/sr3.gif">
<br>
The image shows the SpaceRaze 3.0.0 GUI.<br>
</td>
</tr>
</table>
</div>
</div>
		<div class="Form_End"></div>
</div>

<br><br><br><br><br><br><br><br>
</body>
</html>
