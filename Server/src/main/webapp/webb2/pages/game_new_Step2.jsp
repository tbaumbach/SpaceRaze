<%@page import="spaceraze.servlethelper.handlers.GameWorldHandler"%>
<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.server.map.*"%>
<%@ page import="spaceraze.world.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="spaceraze.webb.support.world.GameWorldHelper"%>
<%@ page import="java.io.*"%>

<!DOCTYPE html>
<html>
<head>
<title>Create new SpaceRaze game</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>




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


	String message = "";
		String gameWorld = request.getParameter("gameworld");
		String gameName = request.getParameter("gamename_new");
		String gamePassword = request.getParameter("game_password");
		String autoBalance = request.getParameter("autobalance");
		String emailPlayers = request.getParameter("emailplayers");
		String mapName = request.getParameter("mapname");
		String ranked = request.getParameter("ranked");

	ServerHandler sh = (ServerHandler)application.getAttribute("serverhandler");
	String todoStr = request.getParameter("todo");

	Map aMap = MapHandler.getMap(mapName);
	int maxPlayers = aMap.getMaxNrStartPlanets();
//		String login = tmpUser.getLogin();
%>

<%
	GameWorld gw = GameWorldHandler.getGameWorld(gameWorld);
	String factionsCheckboxesHTML = new GameWorldHelper(gw).getFactionsCheckboxesHTML();
%>


<body background="images/spaze.gif">
<!-- <%= todoStr %> -->

<% if (!message.equals("")){ %>
<p>
Server message: 
<% if (message.equalsIgnoreCase("Game started")){ %>
<font color="#00FF00"><%= message %></font>
<% }else{ %>
<font color="#FF0000"><%= message %></font>
<% } %>

<br>
<p>

<% } %>


<div style="left:130px;width:718px;position: absolute;top: 88px;">
	<div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b>Start a new game - Step 2</b></div></div>
	<div class="Form_Text"  style="width:718"><div class="SolidText">


<form id="form3" action="Master.jsp">

<input type="hidden" name="mapname" value="<%= mapName %>">
<input type="hidden" name="gameworld" value="<%= gameWorld %>">
<input type="hidden" name="gamename_new" value="<%= gameName %>">
<input type="hidden" name="game_password" value="<%= gamePassword %>">
<input type="hidden" name="autobalance" value="<%= autoBalance %>">
<input type="hidden" name="emailplayers" value="<%= emailPlayers %>">
<input type="hidden" name="action" value="game_new_confirmation">
<input type="hidden" name="todo" value="Start new game">
<input type="hidden" name="ranked" value="<%= ranked %>">


<table>
<tr>
<td>Choose how often the game will update:&nbsp;&nbsp;&nbsp;</td>
<td><select name="time" style="width:350px;" class="InputText">
	<option value="5">5 times a week (all mornings except Saturday and Sunday)</option>
	<!--option value="0">Never</option-->
	<option value="1">1 times a week (Monday morning)</option>
	<option value="2">2 times a week (Monday and Thursday mornings)</option>
	<option value="3">3 times a week (Monday, Wednesday and Friday mornings)</option>
	<option value="7">7 times a week (every morning)</option>
</select></td>
</tr>
<tr>
<td>Choose min steps between players:&nbsp;&nbsp;&nbsp;</td>
<td><select name="steps" style="width:350px;" class="InputText">
	<option value="10" selected>10</option>
	<option value="0">0 (can be neighbours)</option>
	<option value="1">1</option>
	<option value="2">2</option>
	<option value="3">3</option>
	<option value="4">4</option>
	<option value="5">5</option>
	<option value="6">6</option>
</select></td>
</tr>
<tr>
<td>Choose diplomacy type:&nbsp;&nbsp;&nbsp;</td>
<td><select name="diplomacy" style="width:350px;" class="InputText">
	<option value="faction">Faction deathmatch (team battle faction vs faction)</option>
	<option value="deathmatch">Deathmatch (all players always at war)</option>
	<option value="gameworld">Gameworld (as specified in the gameworld)</option>
	<option value="open">Open (all options open for all players)</option>
</select></td>
</tr>
<tr>
<td>Choose maximum number of players:&nbsp;&nbsp;&nbsp;</td>
<td><select name="maxnrplayers" style="width:350px;" class="InputText">
<%= aMap.getNrPlayersHTML() %>
</select></td>
</tr>
<tr>
<td>Startingplanets:&nbsp;&nbsp;&nbsp;</td>
<td><select name="startingplanets" style="width:350px;" class="InputText">
<option value="1">1</option>
<option value="2">2</option>
<option value="3">3</option>
<option value="4">4</option>
<option value="5">5</option>
<option value="6">6</option>
<option value="7">7</option>
<option value="8">8</option>
<option value="9">9</option>
<option value="10">10</option>
<option value="11">11</option>
<option value="12">12</option>
<option value="13">13</option>
<option value="14">14</option>
<option value="15">15</option>
<option value="16">16</option>
<option value="17">17</option>
<option value="18">18</option>
<option value="19">19</option>
<option value="20">20</option>
<option value="21">21</option>
<option value="22">22</option>
<option value="23">23</option>
<option value="24">24</option>
<option value="25">25</option>
<option value="26">26</option>
<option value="27">27</option>
<option value="28">28</option>
<option value="29">29</option>
<option value="30">30</option>
<option value="31">31</option>
<option value="32">32</option>
<option value="33">33</option>
<option value="34">34</option>
<option value="35">35</option>
<option value="36">36</option>
<option value="37">37</option>
<option value="38">38</option>
<option value="39">39</option>
<option value="40">40</option>
<option value="41">41</option>
<option value="42">42</option>
<option value="43">43</option>
<option value="44">44</option>
<option value="45">45</option>
<option value="46">46</option>
<option value="47">47</option>
<option value="48">48</option>
<option value="49">49</option>
<option value="50">50</option>
<option value="51">51</option>
<option value="52">52</option>
<option value="53">53</option>
<option value="54">54</option>
<option value="55">55</option>
<option value="56">56</option>
<option value="57">57</option>
<option value="58">58</option>
<option value="59">59</option>
<option value="60">60</option>
<option value="61">61</option>
<option value="62">62</option>
<option value="63">63</option>
<option value="64">64</option>
<option value="65">65</option>
<option value="66">66</option>
<option value="67">67</option>
<option value="68">68</option>
<option value="69">69</option>
<option value="70">70</option>
<option value="71">71</option>
<option value="72">72</option>
<option value="73">73</option>
<option value="74">74</option>
<option value="75">75</option>
<option value="76">76</option>
<option value="87">77</option>
<option value="78">78</option>
<option value="79">79</option>
<option value="80">80</option>
<option value="81">81</option>
<option value="82">82</option>
<option value="83">83</option>
<option value="84">84</option>
<option value="85">85</option>
<option value="86">86</option>
<option value="87">87</option>
<option value="88">88</option>
<option value="89">89</option>

<option value="90">90</option>
<option value="91">91</option>
<option value="92">92</option>
<option value="93">93</option>
<option value="94">94</option>
<option value="95">95</option>
<option value="96">96</option>
<option value="97">97</option>
<option value="98">98</option>
<option value="99">99</option>

<option value="100">100</option>
<option value="101">101</option>
<option value="102">102</option>
<option value="103">103</option>
<option value="104">104</option>
<option value="105">105</option>
<option value="106">106</option>
<option value="107">107</option>
<option value="108">108</option>
<option value="109">109</option>

</select></td>
</tr>
<tr>
<td>Solo Win:&nbsp;&nbsp;&nbsp;</td>
<td><select name="soloWin" style="width:350px;" class="InputText">
<option value="60">60%</option>
<option value="50">50%</option>
<option value="51">51%</option>
<option value="55">55%</option>
<option value="60">60%</option>
<option value="65">65%</option>
<option value="70">70%</option>
<option value="75">75%</option>
<option value="80">80%</option>
<option value="100">100%</option>
</select></td>
</tr>
<tr>
<td>Faction Win:&nbsp;&nbsp;&nbsp;</td>
<td><select name="factionWin" style="width:350px;" class="InputText">
<option value="60">60%</option>
<option value="50">50%</option>
<option value="51">51%</option>
<option value="55">55%</option>
<option value="60">60%</option>
<option value="65">65%</option>
<option value="70">70%</option>
<option value="75">75%</option>
<option value="80">80%</option>
<option value="100">100%</option>
</select></td>
</tr>
<tr>
<td>Turns:&nbsp;&nbsp;&nbsp;</td>
<td><select name="nrTurns" style="width:350px;" class="InputText">
<option value="0">Unlimited</option>
<option value="10">10</option>
<option value="20">20</option>
<option value="30">30</option>
<option value="40">40</option>
<option value="50">50</option>
<option value="60">60</option>
<option value="70">70</option>
<option value="80">80</option>
<option value="90">90</option>
<option value="100">100</option>
</select></td>
</tr>
<tr>
<td>Statistics:&nbsp;&nbsp;&nbsp;</td>
<td><select name="statistics" style="width:350px;" class="InputText">
<option value="<%= StatisticGameType.ALL.toString() %>"><%= StatisticGameType.ALL.getText() %></option>
<option value="<%= StatisticGameType.PRODUCTION_ONLY.toString() %>"><%= StatisticGameType.PRODUCTION_ONLY.getText() %></option>
<option value="<%= StatisticGameType.NONE.toString() %>"><%= StatisticGameType.NONE.getText() %></option>
</select></td>
</tr>
<tr>
<td>Group players from same faction:&nbsp;&nbsp;&nbsp;</td>
<td><input type="checkbox" checked name="groupfaction" value="yes"></td>
</tr>
<tr>
<td>Random faction:&nbsp;&nbsp;&nbsp;</td>
<td><input type="checkbox" name="randomfaction" value="yes"></td>
</tr>
</table>

<!-- Iterate through all factions and create checkboxes -->
<p>
<%= factionsCheckboxesHTML %>
<br>
<!--input name="todo" type="submit" value="Start new game"-->



</form>

</div></div>
	<div class="Form_Header" ALIGN=RIGHT style="width:718"><div class="SolidText"><A href="#" onclick="document.forms['form3'].submit();"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_continue.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_continue.jpg','Continue: Go to next step to create a new game.','GuideArea');" height=19 alt="Continue" hspace="3" src="images/btn_continue.jpg" width=83 vspace="3" border=0></A></div></div>
	<div class="Form_End"></div>

</div>
</body>
</html>
