<%@page import="spaceraze.servlethelper.handlers.GameWorldHandler"%>
<%@page import="spaceraze.webb.support.world.FactionHelper"%>
<%@ page import="sr.server.*"%>
<%@ page import="spaceraze.world.*"%>

<!DOCTYPE html>
<html>
<head>
<title>Faction page</title>
<meta charset="UTF-8">
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
Resistance bonus: <%= f.getResistanceBonus() %><br>
TechBonus: <%= f.getTechBonus() %><br>
<%--
Siege bonus: <%= f.getSiegeBonus() %><br>
 --%>
<%--
Start wharf size: <%= OrbitalWharf.getSizeString(f.getStartingWharfSize()) %><br>
WharfBuildCost: <%= f.getWharfBuildCost() %><br>
WharfUpgradeCost: <%= f.getWharfUpgradeCost() %><br>
--%>
<p>
<%--
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
--%>
<p>
<h3>Spaceship Types</h3>
<!-- Spaceships list -->
<table class='ListTextRow' class="sr">
  <!--%= new FactionHelper(f).getSpaceshipTypesTableContentHTML() %--->
</table>
<p>
<h3>Start VIP Types</h3>
Number of random VIPs: <%= f.getNrStartingRandomVIPs() %>
<p>
<!-- Start VIP types -->

<p>
<h3>Start ships</h3>
<!-- Start ships -->
<!--%= new FactionHelper(f).getStartingSpaceshipsHTML() %-->
</body>
</html>
