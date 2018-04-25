<%@ page import="sr.server.*"%>
<%@ page import="sr.world.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Faction page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">

<style type="text/css">
table.sr td {
	border-width: 0px;
	padding: 0px;
	background-color: #552500;
}
</style>

</head>
<%
	String gameWorldFileName = request.getParameter("gameworldfilename");
	GameWorld gw = GameWorldHandler.getGameWorld(gameWorldFileName);
	String factionName = request.getParameter("factionname");
	Faction f = gw.findFaction(factionName);
%>
<body background="images/spaze.gif">
<h2><%= f.getName() %> Faction</h2>
Description: <%= f.getDescription() %><br>
Open planet income bonus: <%= f.getOpenPlanetBonus() %><br>
Closed planet income bonus: <%= f.getClosedPlanetBonus() %><br>
Reistance bonus: <%= f.getResistanceBonus() %><br>
TechBonus: <%= f.getTechBonus() %><br>
Siege bonus: <%= f.getSiegeBonus() %><br>
Alignment: <%= f.getAlignment() %><br>
Start wharf size: <%= OrbitalWharf.getSizeString(f.getStartingWharfSize()) %><br>
WharfBuildCost: <%= f.getWharfBuildCost() %><br>
WharfUpgradeCost: <%= f.getWharfUpgradeCost() %><br>
<p>
<h3>Space Station Abilities</h3>
<!-- Space Station Abilities -->
<%
	if (f.canBuildOrbitalStructures()){
%>
Open planet bonus: <%= f.getOrbitalStructure().getOpenProdBonus() %><br>
Closed planet bonus: <%= f.getOrbitalStructure().getClosedProdBonus() %><br>
Tech bonus: <%= f.getOrbitalStructure().getTechBonus() %><br>
Spaceport: <%= f.getOrbitalStructure().isSpaceport() %><br>
<br>
Build cost base: <%= f.getBuildOrbitalStructureCostBase() %><br>
Build cost multiplier: <%= f.getBuildOrbitalStructureCostMulitplier() %><br>
<%
	}else{
%>
This faction can not build Space Stations.
<%
	}
%>
<p>
<h3>Spaceship Types</h3>
<!-- Spaceships list -->
<table border="0" cellspacing="4" cellpadding="0" class="sr">
  <%= f.getSpaceshipTypesTableContentHTML() %>
</table>
<p>
<h3>Start VIP Types</h3>
Number of random VIPs: <%= f.getNrStartingRandomVIPs() %>
<p>
<!-- Start VIP types -->
  <%= f.getStartVIPTypesTableContentHTML() %>
<p>
<h3>Start ships</h3>
<!-- Start ships -->
<%= f.getStartingSpaceshipsHTML() %>
</body>
</html>
