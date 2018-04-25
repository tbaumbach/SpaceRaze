<%@ page import="sr.webb.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Spaceship Types Table</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>

<body background="images/spaze.gif">
<h2>Spaceship Types</h2>
This is a simplified list of all spaceship types in the SpaceRaze game.<br>
A more detailed list of the different spaceship types can be found in the manual.<p>
<table width="540" border="0" cellpadding="0">
  <tr>
    <td align="center"><img src="images/troops.gif"></td>
    <td>Troops</td>
  </tr>
  <tr>
    <td align="center"><img src="images/bombardment.gif"></td>
    <td>Bombardment level (each can bombard 1 point)</td>
  </tr>
  <tr>
    <td><img src="images/x-wing.gif">&nbsp;<img src="images/z-95.gif">&nbsp;<img src="images/tie.gif"></td>
    <td>Number of starfighter squadrons (each gives +10% initiative)</td>
  </tr>
  <tr>
    <td><img src="images/x-wings.gif">&nbsp;<img src="images/z-95s.gif">&nbsp;<img src="images/ties.gif"></td>
    <td>Number of support starfighter squadrons (each gives +10% initiative)</td>
  </tr>
  <tr>
    <td align="center"><img src="images/hyperblock.gif"></td>
    <td>Blocks hyperjumps</td>
  </tr>
</table>

<br>

<table width="700px" border="0" cellpadding="0">
  <tr>
    <td>Image</td>
    <td>Spaceship Type (abbr.)</td>
    <td>Strength</td>
    <td>Range</td>
    <td>Build/Support</td>
    <td>Factions</td>
    <td>Abilities</td>
  </tr>
  <tr>
    <td colspan="7" bgcolor="#FFBF00" height="1"><img src="images/pix.gif" height="1"></td>
  </tr>
  <tr>
    <td colspan="7" bgcolor="#FFBF00" height="1"><img src="images/pix.gif" height="1"></td>
  </tr>
  <tr>
    <td colspan="7" align="center">Small ships</td>
  </tr>
  <tr>
    <td colspan="7" bgcolor="#FFBF00" height="1"><img src="images/pix.gif" height="1"></td>
  </tr>
<%= SpaceshipTable.getSpaceship("crv","Corvette","Crv",1,"Long",3,1,"All",false,0,0,0,false) %>
<%= SpaceshipTable.getSpaceship("stc","Strike Cruiser","StC",1,"Long",6,2,"All",true,0,0,0,false) %>
<%= SpaceshipTable.getSpaceship("gi","Golan I","GI",2,"None",3,1,"All",false,0,0,0,false) %>
<%= SpaceshipTable.getSpaceship("sfs","Starfighter Squadron","SfS",3,"Long",4,2,"Rebels",false,0,0,1,false) %>
<%= SpaceshipTable.getSpaceship("neba","Nebulon A Frigate","NebA",6,"Long",7,3,"League",false,0,0,0,false) %>
  <tr>
    <td colspan="7" bgcolor="#FFBF00" height="1"><img src="images/pix.gif" height="1"></td>
  </tr>
  <tr>
    <td colspan="7" align="center">Medium ships</td>
  </tr>
  <tr>
    <td colspan="7" bgcolor="#FFBF00" height="1"><img src="images/pix.gif" height="1"></td>
  </tr>
<%= SpaceshipTable.getSpaceship("int","Interdictor","Int",4,"Long",14,4,"Empire",false,1,0,0,true) %>
<%= SpaceshipTable.getSpaceship("esc","Escort Carrier","ESC",5,"Long",12,4,"League",false,0,0,2,false) %>
<%= SpaceshipTable.getSpaceship("nebb","Nebulon B Frigate","NebB",7,"Long",10,4,"All",false,1,0,0,false) %>
<%= SpaceshipTable.getSpaceship("gii","Golan II","GII",8,"None",6,1,"All",false,0,0,0,false) %>
<%= SpaceshipTable.getSpaceship("drd","Dreadnaught","Drd",9,"Long",14,4,"Rebels",true,1,0,0,false) %>
<%= SpaceshipTable.getSpaceship("giib","Golan IIB","GIIb",10,"None",7,1,"League",false,0,0,0,false) %>
  <tr>
    <td colspan="7" bgcolor="#FFBF00" height="1"><img src="images/pix.gif" height="1"></td>
  </tr>
  <tr>
    <td colspan="7" align="center">Large ships</td>
  </tr>
  <tr>
    <td colspan="7" bgcolor="#FFBF00" height="1"><img src="images/pix.gif" height="1"></td>
  </tr>
<%= SpaceshipTable.getSpaceship("giii","Golan III","GIII",11,"None",9,2,"All",false,0,0,1,false) %>
<%= SpaceshipTable.getSpaceship("vsd","Victory Star Destroyer","VSD",12,"Short",18,5,"League",true,1,1,0,false) %>
<%= SpaceshipTable.getSpaceship("mcc","Mon Calamari Cruiser","MCC",13,"Short",20,6,"Rebels",true,1,2,0,false) %>
<%= SpaceshipTable.getSpaceship("isd","Imperial Star Destroyer","ISD",14,"Short",24,8,"Empire",true,2,3,0,false) %>
  <tr>
    <td colspan="7" bgcolor="#FFBF00" height="1"><img src="images/pix.gif" height="1"></td>
  </tr>
  <tr>
    <td colspan="7" align="center">Huge ships</td>
  </tr>
  <tr>
    <td colspan="7" bgcolor="#FFBF00" height="1"><img src="images/pix.gif" height="1"></td>
  </tr>
<%= SpaceshipTable.getSpaceship("ssd","Super Star Destroyer","SSD",15,"Short",40,12,"Empire",true,3,4,0,false) %>
</table>

</body>
</html>
