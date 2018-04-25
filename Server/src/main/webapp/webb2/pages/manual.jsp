<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="sr.server.ranking.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.enums.*"%>

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
<title>SpaceRaze Manual</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">

<style type="text/css">
table.sr td {
	border-width: 0px;
	padding: 0px;
	background-color: #552500;
}
</style>


</head>

<body background="images/spaze.gif">

<div style="left: 129px;width: 450px;position: absolute;top: 89px;padding-bottom:20px;">	
	<div class="Form_name" style="width:450"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:450"><div class="SolidText"><b>Manual</b></div></div>
	<div class="Form_Text" style="width:450"><div class="SolidText">
		<b>Table of contents</b><br>
		<a href="#intro">1. Introduction</a><br>
		<a href="#planets">2. Planets</a><br>
		<a href="#wharfs">3. Orbital Wharfs</a><br>
		<a href="#spacestations">4. Space Stations</a><br>
		<a href="#spaceships">5. Spaceships</a><br>
		<a href="#troops">6. Troops</a><br>
		<a href="#factions">7. Factions</a><br>
		<a href="#vips">8. Very Important Persons (VIPs)</a><br>
		<a href="#blackmarket">9. Black Market</a><br>
		<a href="#diplomacy">10. Diplomacy</a><br>
		<a href="#winning">11. Winning the game</a><br>
		<a href="#losing">12. Losing the game</a><br>
		<a href="#advanced">13. Advanced</a><br>
		<a href="#initiative">14. Initiative base computation</a><br>

</div></div>
	<div class="List_End"></div>		
<br>

<div class="Form_name" style="width:450"><div class="SolidText">SpaceRaze Manual</div></div>
<div class="Form_Header" style="width:450"><div class="SolidText"><a name="intro"><b>1. Introduction</b></a></div></div>
<div class="Form_Text" style="width:450" style="width:450"><div class="SolidText">

SpaceRaze is a game of intergalactic warfare and conquest. Players try to win control over a quadrant of space containing several other players and factions. 
In SpaceRaze there are no active computer-controlled players, and cooperation with friendly players and diplomacy with enemy players may have a big impact on who will eventually emerge as the winner of a game.
<br><br>
The game is a turn-based strategy game. 
In SpaceRaze every player will independantly perform each turn of the game, 
and when all players have performed their turn, the server will update the game to the next turn.<br>
Alternatively, a game can be set to update at certain intervals, or an administrator can always update a game manually.
</div></div>

<div class="Form_Header"style="width:450"><div class="SolidText"><a name="planets"><b>2. Planets</b></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

Planets are the most central objects of the game. 
A planet give income to a player and allows a player to build orbital shipwharfs where he can build spaceships. A planet has two important properties: production and resistance. Production is a measurement of how much income a player recieves each turn and resistance how difficult it is to conquer that planet.
<br><br><b>Production</b><br>
All homeplanets (the planets the players starts with) always have production 7 and all other planets have between 1 and 6 in production. The production of a planet can be increased by 1 for the cost of the current production. Example: it costs 6 to increase a planets production from 6 to 7. This can only be done once per turn per planet. No planet can increase its production to more than double its initial value.<br>
Planets give a player an income each turn equal its production. A blocked planet or a planet under siege does not give any income. <br>
Planets also allow free upkeep of spaceships equal to their production. If a players upkeep of his fleet is larger than his total production of all his planets he will have to pay for the surplus.<br>
<br><b>Resistance</b><br>
Resistance is a measurement of how difficult a planet is to conquer. Each turn a planet is under siege resistance is decreased by the following factors:<br>
- if the besieging fleet has at least 1 ship with troops resistance is lowered by 1<br>
- resistance is lowered by an equal amount of bombardement<br>
Some ships can have bonuses that make resistance to decrease faster, and also some VIPs can have that effect. Some factions can also have bonuses that affect resistance of besiged planets.<br>
A planets resistance can be increased by 1 by paying an equal amount to the current resistance. Example: it costs 1 to raise a planets resistance from 1 to 2. This can only be done once per turn per planet.<br>
A planets resistance can be boosted by the following factors:<br>
- the precence of some VIPs can increase the resistance as long as they stay on the planet<br>
- some factions can have a permanent resistance bonus to all newly conquered planets<br>
A newly conquerered planet starts with resistance 1 (plus any faction bonus...).<br>
<br><b>Razed planets</b><br>
Any planet whose production is reduced to zero or lower from bombardment is RAZED. A razed planets production and infrastructure has benn so badly damaged that it will take a long time for the planet to recover and produce a profit again. <br>
A razed planet cannot be owned by any player and can not increase its production above zero for the remainder of the game.<br>
<br><b>Open/Closed status</b><br>
Each planet can be either closed or open. An open planet will give an additional income of +2 but will at the same time reveal information about itself to all other players, including current owner, population, resistance, shipwharfs and largest shipsize. A closed planet will give no bonus to income but will not reveal anything about about itself, not even who controls it.<br>
Any planet that is put under blockade or siege is automatically closed.<br>
<b>Orbital structures</b><br>
A planet can also build two sorts of orbital structures at planets: orbital wharfs and space stations.<br>
These are described in the two following chapters.

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="wharfs"><b>3. Orbital wharfs</b></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

All planets can build an unlimited number af orbital wharfs. 
Orbital wharfs are where spaceships are built. 
The cost for building and upgrading an orbital wharf depends on which faction the player belongs to.
Only one new wharf may be built at each planet each turn. 
All newly build wharfs are small in size. 
To be able to build larger spaceships a wharf have to be upgraded. 
A wharf may be upgraded to medium, large and finally huge. <br>
All wharfs have a number of slots where ships can be built (see table below). <br>
<table border="0" cellspacing="1" cellpadding="0" class="MainMenu">
<tr>
<td width="120">Wharfsize</td>
<td width="60">Slots</td>
</tr>
<tr>
<td colspan="2" height="1"><img src="images/yellow_pix.gif" width="184" height="1"></td>
</tr>
<tr>
<td>Small</td>
<td>1</td>
</tr>
<tr>
<td>Medium</td>
<td>2</td>
</tr>
<tr>
<td>Large</td>
<td>3</td>
</tr>
<tr>
<td>Huge</td>
<td>5</td>
</tr>
</table>
Ships of different sizes take a certain number of slots (see table).
<table border="0" cellspacing="4" cellpadding="0" class="MainMenu">
<tr>
<td width="120">Shipsize</td>
<td width="60">Slots</td>
</tr>
<tr>
<td colspan="2" height="1"><img src="images/yellow_pix.gif" width="184" height="1"></td>
</tr>
<tr>
<td>Small</td>
<td>1</td>
</tr>
<tr>
<td>Medium</td>
<td>2</td>
</tr>
<tr>
<td>Large</td>
<td>3</td>
</tr>
<tr>
<td>Huge</td>
<td>5</td>
</tr>
</table>
A larger wharf can build any combination of ships as long as the ships total number of slots does not exceed the number of slots the wharf has. Example: a large wharf can build 1 large ship, 1 medium and 1 small or 3 small ships.

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="spacestations"><b>Space Stations</b></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

Another type of orbital structure are Space Stations. <br>
Those factions that can build Space Stations can gain different advantages by building a Space Station at a planet.
These are: 
-increased income for that planet (open and/or closed)<br>
-increased tech level at that planet<br>
-the ability to move short range ships between two planets at long range, if both planets belong to the same faction and have Space Stations with this ability<br>
If a planet with a Space Station is lost in any way the any Space Station is always destroyed.<br>
Different faction may have different costs for building Space Stations. Mostly it is more expensive to build a Space Station at a small planet than at a large planet.<br>
Planets can only have one space station.

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="spaceships"><b>5. Spaceships</b></a></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

To be able to defend themselves and be able to conquer new planets players will need spaceships. Spaceships have many attributes that affect how they work.
<br><b>Building ships</b><br>
Spaceships are built at orbital wharfs. Spaceships come in 4 sizes: small, medium, large and huge. To be able to build a spaceship of a certain kind 3 factors must be fulfilled:<br>
1. the player must have a sufficiently large wharf to build the ship in (see wharfs above)<br>
2. the player must be able to build that type of ships,depending on their faction (see factions below)<br>
3. the player must be able to pay the build cost of that type of ship.<br>
<br><b>Weapons</b><br>
The amount of damage a spaceship does when shooting on a capital ship is their 
combined Weapons rating.
A shiptype can have up to four different sizes of weapons to use against capital ships:<br>
-weapons that can target all sizes of ships (all ships always have this)<br>
-weapons that can target medium or bigger ships<br>
-weapons that can target large or bigger ships<br>
-weapons that can target huge ships<br>
Example: a ship with weapons of all four sizes meet a medium enemy ship, then it 
can only use its two smallest weapon types against that ship.<br>
The three last types of weapons may have a limited number of shots before it has to resupply (by starting a turn in the same system as a wharf or supply ship of sufficient size/capacity).
<br>
All ships also have a separate attack value for when it attacks a squadron.
<br>
A ship that have been damaged has it's weapon ratings decreased by the percentage of damage.<br>
Example: a ship that has suffered 60% damage will only do 40% damage compared to when it is unharmed.<br>
All ships always do at least 1 point of damage no matter how damaged they are.<br>
<br><b>Randomized damage</b><br>
Whenever a ship shoots at another ship there is an element of chance deciding exactly how big the damage will be, but the average damage will be very near the base damage.<br>
The exact algorithm is in the <a href="#randomDamage">Advanced Section</a>.
<br><b>Resupply</b><br>
Wharfs can resupply ships up to the same size of the wharf itself.<br>
Ships with the supply ability can also resupply ships up to the limit of their ability (small, medium, large or huge).<br> 
A Carrier can also resupply any Starfighter Squadron assigned to it.<br>
<br><b>Shields</b><br>
All ships have shield to protect themselves from damage. Only when the shields are reduced to zero will a ship start to take damage from fire. Shields are replentished between battles. A damaged ship has its shields lowered in the same way a ships weapons is reduced (see Weapos above).
<br><b>Armor</b><br>
All ships can have armor.<br>
Armor is divided inte the different sizes that can hit a ship of a certain size.<br>
This means that a small ship only has armor vs small weapons (since no larger weapons can hit it.), and a huge ship has separate armor levels against small, medium large and huge weapons.<br>
Armor only decrease (by a percentage) any damage after the shields has been depleted.<br> 
Damage vs Squadrons are counted as small weapons in regard to armor.<br>
<br><b>Damage Capacity and Repairs</b><br>
All ships have a maximum number of damage it can sustain before being destroyed. <br>
A damaged ship does less damage. <br>
To repair a damaged ship a player can move it to a planet with a wharf of the same or larger size and if a damaged ship stays 1 turn there it will be repaired to its full damage capacity.
<br><b>Initiative Bonus</b><br>
Certain ships have a bonus to initiative in battle. This may be a "normal" or support initiative bonus.<br>
See the battlerules below for details about how this works.
<br><b>Initiative Defence</b><br>
Certain ships have a defence bonus to initiative in battle. See the battlerules below for details about how this works.
<b>Stops retreats</b><br>
Some ships have a special ability which stops all enemies with whom the ships fleet meets or is engaged to run away. 
<b>Starfighter Squadrons</b><br>
Some shiptypes are starfighter squadrons, which means that the unit represents a number of smaller ships instead of one big capital ships.<br>
Squadrons work a little different than capital ships:<br>
-anyone firing at a squadron uses it's anti-squadron weapons<br>
-squadrons can be carried in carriers (see below) and also be resupplied between combats by their carrier<br>
-any squadron at a neutral or enemy planet will be destroyed if there isn't at least one carrier from the same player in that system<br>
-squadrons always move bofore capital ships<br>
Note that squadrons with no range cannot move to other systems than by being carried in a carrier.<br>
<b>Carriers</b><br>
Some ships can carry starfighter squadrons. 
These have a Squadron Capacity larger than 0.<br>
A carrier that retreats from battle will abandon all squadrons attached to it.<br>
<b>Targeting</b><br>
Ships can have different targeting preferences:<br>
-Anti Air: will target squadrons three times as often as capital ships<br>
-Balanced: will target all enemies with equal chance<br>
-Anti MBU: will target capital ships three times as often as squadrons<br>
<b>Siege Bonus</b><br>
Ships can have a bonus when besieging a planet, and cause the resistance of that planet to decrease faster.<br>
This bonus is not cumulative, only the ship with the biggest Siege Bonus in a besieging fleet is counted.
<b>Planetary Survey</b><br>
Ships with this ability reveal production and resistance of closed planets.
<b>Black Market</b><br>
Some shiptypes can not appear on the black market.
<b>Movement & Range</b><br>
Ships either have long, short or no range. Ships with no range cannot move from the system they were built in. Ships with short range can only move along short spacelines in the quadrant (marked red on the map). Ships with long range can move both along short and long (blue on the map) spacelines in the quadrant.<br>
It always take one turn to move between two planets regardless if it is long or short range.
<b>VIPs and spaceships</b><br>
Spaceships can also function as carriers of VIPs. A VIP can travel to any ship and then automatically move with that ship as a passenger.
<b>Combat</b><br>
After movement have been performed, when two or more hostile fleets are present at the same planet, combat may occur. 
If more than two hostile forces are present they battle each other in a random sequence, starting with the defending force and one of the attackers.
<br>
Before combat begins, "Attack max size" for the planet where the fleets meet determines if any of the fleets retreat immediately. "Same size" means that the forces from a player only engages in combat if the maximum size of any of the opponents ships are the same size as his own biggest ships. 
For example: a force whose largest ship is a medium sized ship retreats immediately if attack max size is set to "same size" and the opponent has at least one large or huge ship.
<br>
If combat is initiated the fighting is divided into rounds. <br>
In each round one spaceship can fire at one of the enemy ships. <br>
To determine which side should fire each round a base initiative value is computed. 
This value can be computed using one of three formulas depending on the gameworld used
and the formulas are described in detail in the <a href="#initiative">Advanced section</a> of the manual.
To summarize the formulas there either are a fifty-fifty chance or the bigger fleet have a bigger chance of firing each round.
<!--
To decide which side should shoot each round a certain formula is used:
<br> 
First the relative size of the opposing fleets are computed using the following table:
<br>
<table  border="0" cellspacing="4" cellpadding="0" class="sr">
<tr>
<td>Actual # of non-screened ships</td>
<td>Relative size</td>
</tr>
<tr>
<td colspan="2" height="1"><img src="images/yellow_pix.gif" width="360" height="1"></td>
</tr>
<tr>
<td>1</td>
<td>1</td>
</tr>
<tr>
<td>2</td>
<td>1,8</td>
</tr>
<tr>
<td>3</td>
<td>2,5</td>
</tr>
<tr>
<td>4+</td>
<td>1 + Square Root(actual #)</td>
</tr>
</table>
For example, a fleet with 4 non-screened ships counts as relative size 3, 9 count as 4, 16 as 5 and 25 as 6.
<br>
Then the fleets have an equal chance of firing each round as their relative size divided by the total relative sizes of the two fleets.
<br>
For example: a fleet with 3 (non-screened) ships is in combat with an enemy fleet with 9 (non-screened) ships. <br>
The 3 ship fleet have a relative size of 2,5 and the 9 fleet have a relative size of 4.<br>
The 3 ship fleet then have 38% (2,5/(2,5+4)) chance of firing each turn, and the 9 ship fleet have a 62% (4/(2,5+4)) chance each turn. 
-->
<br>
This basic initiative base (%) is modified by a fleets initiative bonus. 
If the basic initiative base is computed to 60% for a fleet and they had a ship with a 20% initiative bonus and a VIP with an additional 10% initiative bonus, and the opposing fleet didn't have any bonuses at all, they would have a 30% initiative bonus. <br>
Then they would have a chance of firing each turn equal to (60% + (40% * 0.3)) = 72%. The other fleet would only have 28% chance each round of shooting.<br>
<br>
Some ship are initiative support units, and the best initiative support unit can add it's init bonus to the best init bonus from a non-support unit. 
Example: a fleet whose ship with the best non-support bonus give a +20% initiative bonus, and the ship with the best support initiative bonus gives a +10% initiative bonus have a total init bonus (before adding bonus from VIPs) of 30%.
<br>
Some ship are initiative defence units, and the best initiative defence will subtract it's defence bonus from the enemies total initiative bonus. 
Example: the fleet in the example above (with a total initiative bonus of +30%) meets an opposing fleet with a ship with an initiative defence bonus of +10%, the first fleet will have a total initiative bonus of 20% (20% - 10%).<br>
Initiative defence can never lower an enemys initiative bonus below 0. 
<br>
Initiative bonus are not cumulative but only the best bonus from a non-support ship, a init-support ship and the best bonus from a VIP is counted. 
If both sides have an initiative bonus the smaller is subtracted from the larger. 
Example: if a fleet with a total init bonus of +30 meets a fleet with +20% thefirst fleet is counted as having a bonus of +10% (and the second fleet as it haven't any bonus at all).
<b>Screened ships</b><br>
Any ship can be screened, that is, if it is together with any non-screened ships it will hide behind the other ship(s) in combat. Only if there are no more non-screened ships left it will be forced into combat.
A screened ship will never be counted for when computing who will win the initiative.
A VIP on a screened ship can still give an initiative bonus.
<b>Retreats</b><br>
Any ships that starts retreating will continue to retreat until it reaches a planet that is owned by the player, and is neither besieged nor blocked. The ship will move 1 step each turn until it reaches such a planet.<br>
If there is no eligble planet to move to, the crew will scuttle the ship.<br>
A player has no control over a retreating ship until it reaches an eligble plane, and cannot give any orders to a retreating ship.<br>
A carrier that retreats due to size before combat has occured will keep all it's assigned squadrons with it while retreating.<br>
A carrier that retreats in the middle of a combat will abandon all of it's squadrons.<br>
<b>Kills</b><br>
Kills give a bonus to all weapons and shields of a ship. The bonus is computed by counting the number of kills, divide it by the number of slots needed to build the ship and multiplied it by 10%. 

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="troops"><b>6. Troops</b></a></div></div>
<div class="Form_Text" style="width:450" style="width:450"><div class="SolidText">

In some gameworlds players may build troops to conquer other planets or use for defending their own planets.
<br><b>Abilities</b><br>

<br><b>Battle position</b><br>
A troop in a land battle can be in one of 4 different positions:<br>
- First-line: attack enemy forces head on<br>
- Support: second-line troops who can use artillery attack (if they have any)<br>
- Flanker: will try to encircle enemy first-line troops to attack support troops or attack enemy first-line troops from behind<br>
- Defence: protects support units from up to 2 enemy flankers<br>
Note 1: only troops with the "Flanker"-ability can be used as flankers.<br>
Note 2: if both side have flankers they will combat each other one-on-one. Only the side with more flankers will have the excess do flanking attacks against enemy support etc.
<br><b>Lineup</b><br>
Before the fighting begin in a land battle, lineup is performed. In the lineup, units may change position depending on what kind and how many units
the enemy force have.<br>
If the enemy have more than double the number of first-line troops flankers, defence and even support units will be moved to first-line, until the enemy no longer have more than twice as many firts-line troops.
<br><b>Cost</b><br>
All troops have both a build cost and a support cost. Supply cost for a troop on (or in orbit around) an own planet is not
subtracted from income unless the total troop supply cost for that planet exceeds the resstance of the planet, and then only the 
amound exceeding is withdrawn from income. <br>
All troops in orbit or on enemy planets cost their full supply cost.
<br><b>Sieging with troops</b><br>
If both sides have troops a land battle can be joined. If all defenders are destroyed the planet is immediately conquered by the attacker.<br>
If the defender lacks troops and an attacker have troops the planet is conquered immediately.<br>
If the defender have troops and the attacker lacks troops the defender may resist indefinitely.<br>
If both sides lack troops resistance may be decreased if the attacker have "troops" ability on a ship or use bombardment, and the planet conquered if resistance reaches 0.
<br><b>Bombardment</b><br>
Attacking spaceships that bombard a planet with troops will also damage the troops on the planet. Each point of bombardment will cause damage to one of the defending troops. 
The bombardment will also cause the usual damage to production and resistance.
<br><b>Land battles</b><br>
In a land battle all involved troops in first-line, flank and artillery make a number of attacks against enemy troops. Each
troop have a fixed number of attacks they can perform, but some troops have a penalty for this on the same turn they land on an enemy planet.<br>
If both sides still have troops left on the planet, after all involved troops in a battle have performed their attacks, the battle is 
a draw and the fighting can continue the next turn and so on. 
<br><b>Squadrons in land battles</b><br>
The attacking side may use squadrons with ground attack to attack the defending forces. Every time a squadron attacks, all defending troops will pool their anti-air fire
and counter-attack against the attacking squadron.
<br><b>VIPs</b><br>
VIPs can be placed on troops and are killed if the troop are destroyed (unless they have the "Hard to kill" ability).<br>
Some VIPs have abilities that can affect the troop or side they are fighting on.
<br><b>Repairing</b><br>
Troops are repaired if they are on a troop carrier or on own planet.<br>
The amouts of repair are:<br>
- in troop carrier at non-own planet or retreating: 5%<br>
- in troop carrier at own planet: 15%<br>
- on own planet: 25%<br>
<br><b>Building new troops</b><br>
To build new troops a player must have a building that can build troops.

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="factions"><b>7. Factions</b></a></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

Players always belong to a factions in every SpaceRaze game. <br>
Players belonging to the same faction can never attack each other and are always at war with all other factions. Players belonging to the same factions can win the game together (see Winning and losing below).<br>
Depending on which faction the player belong to he gains different advantages and disadvantages.<br>
The abilities that factions can have are described below.<br>
<br><b>Income bonuses</b><br>
Some factions may have a general bonus to open and/or closed income from all their planets.
<br><b>Resistance bonus</b><br>
Some factions may have a general resistance bonus affecting all conquered planets (including planets persuaded to join).<br>
The startplanet also has this bonus.
<br><b>Tech bonus</b><br>Some factions may have a general tech bonus affecting all ships built by players belonging to that faction.
<br><b>Space Station</b><br>
Some factions allow players to build space stations of a certain type. But even if two factions can build the same type of space station the costs may vary. 
<br><b>Wharf costs</b><br>
Players belonging to different factions can have different costs for building and upgrading orbital wharfs.
<br><b>Alignment</b><br>
A factions alignment can be either good, neutral (not to be confused with neutral planets) or evil.<br>
A good factions player can not have evil VIPs and vice versa.
<br><b>VIPs</b><br>
Except for a governor (which all players start with) a player belonging to a certain faction can start with a number of random or specified VIPs.
<br><b>Spaceship types</b><br>
Factions may only be able to use some of the total number of shiptypes available in a gameworld.
<br><b>Siege bonus</b><br>
Some factions may have a general siege bonus lowering a besieged planets resistance faster.
<br><b>Color</b><br>
All factions have a base color that is used in the game client, and is also used in the map etc.

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="vips"><b>8. Very Important Persons</b></a></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

One important part in SpaceRaze are the Very Important Persons (VIPs) that all players can control. 
They can give significant bonuses and can affect the outcome of a game.
<br><b>Aquisition</b><br>
Players can get VIPs in three different ways: <br>
* at the start of a game all players start with a Governor and (depending on faction) zero or more random and/or specified VIP<br>
* each time a player conquers a neutral planet that no-one has already conquered in the game there are a 50% chance of finding a random VIP on that planet.<br>
* the Black Market often offer VIPs for sale<br>
When conquering a neutral planet a player can only find VIPs that his faction allows, for instance an Empire player never find Light Jedis. Also a player can never buy VIPs from the Black Market that they cannot have.<br>
<br><b>Duellists</b><br>
Some VIPs can be duellists.<br> 
This is a special type of VIP which will duel to the death with other duellists under certain circumstances:<br>
-whenever two duellists from different faction are in the same system they will immediately find each other and duel to the death.<br>
-an exception to this is that good duellists never fight each other. <br>
-furthermore an evil and good duellist in the same system will always attack each other even if they belong to the same player!<br>
All duels happens before any spacebattles are performed on that turn.<br>
Duellists can have other abilities like any other VIP.<br>
In a duel no duellist can have more than 95% chance of winning a duel.<br>
<br><b>Losing</b><br>
VIPs can be killed in a variety of ways. 
The most common way is by being on a spaceship that is blown up or being on a planet that is conquered by the enemy (or razed). 
All VIPs except those with the ability Hard To Kill can be killed that way. 
A third way is to be killed by an enemy assassin. 
VIPs with the ability Well Guarded are immune to assassinations. 
VIPs with the ability Can Visit Enemy Planets can be caught by enemy VIPs with the ability Counter Espionage if they are on enemy planets and they are killed if they are caught. 
VIPs with the Hard To Kill ability is immune to Counter Espionage.
<br> 
Governors can be killed if they are on a neutral planet and that planet is attacked or besieged by any force from the same faction as the Governor. In this case the enraged population of the attacked planet lynches the visiting Governor.
<br>
Duellists can off cource be killed in duels with other duellists. 
<br><b>Movement</b><br>
VIPs can move between planets at long range, between planets and ships at that planet and between ships at the same planets. 
Only some VIPs can move to and from enemy and neutral planets (those with the ability Can Visit Enemy Planets) and some can move to and from neutral planets from a ship (those with the ability Can Visit Neutral Planets).<br>
If any of the VIPs who cannot move to enemy planets are caught on a planet when it is besieged or under blockade they cannot move away from there until the siege is lifted.<br>
<br><b>Frequency</b><br>
VIPs can be more or less common. All VIP types have a Frequency value which determines how often that type are found 
on conquered planets and in the black market.<br>
<br><b>VIP abilities</b><br>
All VIPs can have any number of VIP abilities. All abilities available are described in the following chapters.
<br><b>Governor</b><br>
All players beging each game with a VIP with the governor ability.<br>
The Governor is the most important VIP for all players for one reason: if he dies the player has lost the game.<br>
Governors can have other abilities just like other VIPs.
If a Governor is in a retreating spaceship he cannot give any move orders to his spaceships or VIPs.<br>
<br><b>Diplomat</b><br>
A VIP with this ability on a neutral planet can convince it to join that player (this has a 100% chance of succeding but takes a number of turns equal to the neutral planets resistance).<br>
This presume that he is the only diplomat present at that neutral planet. 
If one or more hostile diplomats are present at the same neutral planet, no one will get forward in the persuation process. 
Or, if there are one or more diplomats from the same faction present the planet will not join anyone.<br>
If several diplomats from the same player are present on the same neutral planet, they will persuade the planet faster.<br>
<br><b>Alignment</b><br>
A VIPs alignment can be either good, neutral or evil.<br>
A good factions player can not have evil VIPs and vice versa.
<br><b>Assassin</b><br>
Assassins can kill other VIPs, all except those with the Well Guarded skill. 
Anytime an assassin is in the same system as enemy VIPs it can kill it will have a 50% chance of killing any one of them. 
This means that an assassin can kill more than ove VIP in one turn if it is lucky. 
Assassins can kill VIPs both on a planet or in ships in orbit around that planet on which they are located. <br>
Assassins also shows any VIPs on any planets he visits.<br>
Assassinations take place after defending spies try to discover visiting VIPs.<br>
No assassin can ever have more than a 95% chance to kill a victim.<br>
<br><b>Counter espionage</b><br>
Whenever a VIP with this ability is located on a players own planets he will try to discover (and kill...) any enemy spy or assassin that is on the same planet.<br>
<br><b>Spying</b><br>
Whenever a VIP with the spying ability is on a closed enemy or neutral planet it will show information as if the planet is open.<br>
<br><b>Immune to counter espionage</b><br>
A VIP with this ability is immune to other VIPs with the Counter Espionage ability.<br>
<br><b>Initiative bonus</b><br>
Gives a fleet a bonus to initiative in battle.<br>
Comes in two variants, normal and support initiative bonus.<br> 
Only the highest normal and the highest support bonus is counted in battle.<br>
VIPs with these bonuses must be on a capital ship for the bonus to work.<br>
<br><b>Squadron initiative bonus</b><br>
When a VIP with this bonus is on a first line Squadron in battle he will increase his taskforce total initiative bonus.<br>
Only the highest bonus is counted.<br>
<br><b>Initiative defence</b><br>
Decreases the enemies fleet initiative bonus.<br> 
The enemy fleet can never have a lower init bonus than 0.<br>
This bonus is not cumulative with other VIPs with initiative defence, only the biggest bonus is counted.<br>
<br><b>Siege bonus</b><br>
Decreases resistance faster each turn on a besieged planet when in the besieging fleet. 
<br><b>Resistance bonus</b><br>
Gives a bonus to resistance when on a planet. 
<br><b>Cheaper ship building</b><br>
This is actually four separate bonuses, one for each size of spaceships, 
and the bonus lowers the cost for building any spaceships at all wharfs at 
the same planet as a VIP with this ability is located at.<br>
Note that a ship can never have a building cost lower than 1.<br>
If a VIP with this ability is moved it still gives its bonus to the original planet the same turn as it moves, and
give no bonus to the planet it moves to that same turn. <br>
These bonuses are not cumulative, only the biggest bonus, if more than one VIP is present, is counted.<br>
<br><b>Cheaper wharfs</b><br>
This is actually two separate abilities: to make building of new wharfs cheaper and making upgrades to a wharf cheaper.<br>
If a VIP with this ability is moved it still gives its bonus to the original planet the same turn as it moves, and
give no bonus to the planet it moves to that same turn. <br>
These bonuses are not cumulative, only the biggest bonus, if more than one VIP is present, is counted.<br>
<br><b>Building better ships</b><br>
This ability improves the shields and all weapons for newly build ships by a specified percentage.<br>
If a VIP with this ability is moved it still gives its bonus to the original planet the same turn as it moves, and
give no bonus to the planet it moves to that same turn. <br>
This bonus is not cumulative, only the biggest bonus, if more than one VIP is present, is counted.<br>
<br><b>Income Bonuses</b><br>
VIPs can have bonuses on income for both closed and/or open planets.<br>
These bonuses have no effect the same turn it arrives to a planet.<br>
These bonuses are not cumulative, only the biggest bonus, if more than one VIP is present, is counted.<br>
<br><b>Can Visit Enemy Planets</b><br>
VIP can move freely to all type of planets, including enemy and neutral planets.
<br><b>Can Visit Neutral Planets</b><br>
VIP can visit neutral planets, but can only move there from a ship.
<br><b>Duellist</b><br>
If the VIP is a duellist and how good he is at duelling.
<br><b>Hard to kill</b><br>
Cannot be killed by ship destroyed or planet conquered/razed.
<br><b>Well Guarded</b><br>
Cannot be assassinated.
<br><b>FTL bonus</b><br>
Enables a short range ship to travel long range.
<br><b>Show On Open Planet</b><br>
A VIP with this ability can be seen on the map by all players when on an open planet.<br>
<br><b>Kills</b><br>
Each time a duellist or assassin kills another VIP the kills score is increased by one.<br> 
Each kill increases the killers duellist and assassination skills by +5.<br>

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="blackmarket"><b>9. Black market</b></a></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

Black market plays an important role in SpaceRaze. Mostly because it is one of the two possible sources of new VIPs (the other source is conquered neutral planets) but also because a player can buy spaceships, sometimes cheap, which are placed at any of his planets if he is the highest bidder.
<br><b>Items for sale</b><br>
There are three types of items that can be for sale on the black market:<br>
* VIPs<br>
* Spaceships<br>
* Hot stuff<br>
Each turn (except the first) there are 1-2 new items for sale.<br>
<br><b>VIPs</b><br>
Any VIP can be for sale on the black market, but a player playing an evil faction cannot buy any good VIPs and vice versa.
<br><b>Spaceships</b><br>
Can be any ship the ability "Can appear on Black Market" set to true.<br>
This has two exceptions:<br>
-the ship can not have a movement of zero.<br>
-The second turn only small ships can appear on the black market, the third only medium or small and som on until turn 5 when there are no more size restriction.<br>
<br><b>Hot stuff</b><br>
Hot stuff is a cargo of illegal merchandice which will generate a random profit between 3-18. The actual profit is unknown until after a player has won the bidding for a hot stuff.
<br><b>Refunds</b><br>
If a bid fails a player always get all money refunded to the next turn.
<br><b>Draws</b><br>
If 2 or more players make the same highest bid, no one gets the item for sale and all get their bids refunded. The item will be for sale again the next turn.
</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="diplomacy"><b>10. Diplomacy</b></a></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">
Diplomacy governs who players will fight each other and if any players can win cooperative victories.<p>
There are four different diplomacy game types, and all games always use one of them. The different diplomacy game types are:<br>
<table  border="0" cellspacing="4" cellpadding="0" class="Mainmenu">
<tr>
<td>Diplomacy Game Types</td>
<td>Description</td>
</tr>
<tr>
<td colspan="2" height="1"><img src="images/yellow_pix.gif" width="360" height="1"></td>
</tr>
<tr>
<td>GameWorld Diplomacy</td>
<td>Diplomacy limited by rules set by gameworld</td>
</tr>
<tr>
<td>Open Diplomacy</td>
<td>All diplomacy options open</td>
</tr>
<tr>
<td>Deathmatch</td>
<td>All players start in eternal war against all other players</td>
</tr>
<tr>
<td>Faction Deathmatch</td>
<td>Players from the same factions start in confederacy, and eternal war against all other players</td>
</tr>
</table>
<br>
All players have a diplomacy relation with each other. The relation always have one of the eight different diplomacy levels, which are:
<table  border="0" cellspacing="4" cellpadding="0" class="Mainmenu">
<tr>
<td>Diplomacy Level</td>
<td>Durability</td>
<td>Description</td>
</tr>
<tr>
<td colspan="3" height="1"><img src="images/yellow_pix.gif" width="360" height="1"></td>
</tr>
<tr>
<td>Eternal War</td>
<td><%= DiplomacyLevel.ETERNAL_WAR.getDurability() %></td>
<td><%= DiplomacyLevel.ETERNAL_WAR.getDesc() %></td>
</tr>
<tr>
<td>War</td>
<td><%= DiplomacyLevel.WAR.getDurability() %></td>
<td><%= DiplomacyLevel.WAR.getDesc() %></td>
</tr>
<tr>
<td>Cease Fire</td>
<td><%= DiplomacyLevel.CEASE_FIRE.getDurability() %></td>
<td><%= DiplomacyLevel.CEASE_FIRE.getDesc() %></td>
</tr>
<tr>
<td>Peace</td>
<td><%= DiplomacyLevel.PEACE.getDurability() %></td>
<td><%= DiplomacyLevel.PEACE.getDesc() %></td>
</tr>
<tr>
<td>Alliance</td>
<td><%= DiplomacyLevel.ALLIANCE.getDurability() %></td>
<td><%= DiplomacyLevel.ALLIANCE.getDesc() %></td>
</tr>
<tr>
<td>Confederacy</td>
<td><%= DiplomacyLevel.CONFEDERACY.getDurability() %></td>
<td><%= DiplomacyLevel.CONFEDERACY.getDesc() %></td>
</tr>
<tr>
<td>Lord</td>
<td><%= DiplomacyLevel.LORD.getDurability() %></td>
<td><%= DiplomacyLevel.LORD.getDesc() %></td>
</tr>
<tr>
<td>Vassal</td>
<td><%= DiplomacyLevel.VASSAL.getDurability() %></td>
<td><%= DiplomacyLevel.VASSAL.getDesc() %></td>
</tr>
</table>
Diplomacy levels with permanent durability can never be changed during a game.<br>
Levels with stable durability can be changed, but only by making changes or offers by one or both players.<br>
Levels with unstable durability can be changed by players but can also be changed automatically to a more hostile level if certain conditions apply (se table above).<br>
<b>Changing diplomacy</b><br>
To change a diplomatic state (between two players) to a less hostile state, players can make an offer to another player, and if the other player accept the offer the next turn, diplomatic state is changed.<br>
If two players make offers to each other the same turn, their state change.<br>
To change diplomatic state to a more hostile state a player can order a change which is performed regardless of what the other player does.<br>
Note that all changes of diplomacy state is performed at the end of the turn.<br>
Also note that diplomacy state never can change more than one step each turn.<br>
<b>Diplomatic orders</b><br>
To issue diplomatic orders, offers or cancel orders/offers, click on diplomacy level icons in the Diplomacy panel.<br>
<b>Confederacies</b><br>
If players have confederacy they share the ranking points for defeated opponents they gain if they win.<br>
If a player wants to join a confederacy that already have more than one player, all of the members must accept the new member of the confederacy, either by makings offers to the joining player or accepting an offer from the joining player.<br> 
<b>Lords and vassals</b><br>
Players who have a lord/vassal state have a kind of unequal confederacy.<br>
If they win the lord player gain ranking points as if in a single victory, and all his vassals gain 2 points each (one for surviving the game and one for being on the winning side).<br>
A lord can have any number of vassals.<br>
A lord can take taxes from his vassals, but he can never take more income than they have, so a vassal can never become broke due to taxes.<br>
Diplomatic state between vassals to the same lord are not affected by the fact that they are vassals to the same lord, and therefore it is possible for vassals to the same lord to fight each other.<br>
<b>VIP conflicts</b><br>
VIPs (assassins, counter-spies, infestators, duellists etc) will only be hostile if state is cease fire, war or eternal war.<br> 
<b>Civilian ships</b><br>
Civilian shiåps will only be attacked and destroyed if state is war or eternal war.<br>
<b>Sieges</b><br>
Ships will only besiege planets if a state is war or eternal war.<br>
<b>Traders</b><br>
Ships that gain income for being at different types of planets gain income according to the following table:<br>
<table  border="0" cellspacing="4" cellpadding="0" class="Mainmenu">
<tr>
<td>Income type</td>
<td>Occurances</td>
</tr>
<tr>
<td colspan="2" height="1"><img src="images/yellow_pix.gif" width="360" height="1"></td>
</tr>
<tr>
<td>Own</td>
<td>Own planet</td>
</tr>
<tr>
<td>Freindly</td>
<td>Confederacy/alliance/lord/vassal</td>
</tr>
<tr>
<td>Neutral</td>
<td>Neutral planet or peace/cease fire</td>
</tr>
<tr>
<td>Hostile</td>
<td>Eternal war/war</td>
</tr>
</table>
</div></div>
<div class="Form_Header" style="width:450"><div class="SolidText"><a name="winning"><b>11. Winning</b></a></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">
There are three ways of winning SpaceRaze, which whom one of them is cooperative and the other two are single-player victories. The three ways to win are described below.
<br><b>Lone player left</b><br>
If at any time there are only one player left and he has control over at least one planet, he is the sole victor of that game.
<br><b>60% single player domination</b><br>
If at the beginning of any turn one player has at least 65% of the total population of all the planets in the quadrant he has won that game.
<br><b>70% faction domination</b><br>
If at the beginning of any turn the players of one faction together has at least 65% of the total population of all the planets in the quadrant they have won that game together in a cooperative victory.

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="losing"><b>12. Losing</b></a></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

There are three ways of losing a game of SpaceRaze. These are described below:
<br><b>No ships, no planets...</b><br>
If, at the beginning of any turn, a player controls no spaceships and no planets, he has lost. Any VIPs or money in the treasury he has will be lost.
<br><b>Governor killed</b><br>
If a players Governor is killed he has lost the game. 
Any VIPs or money in the treasury he has will be lost. 
His spaceships at planets not belonging to him will be scuttled, and the rest of his planets and spaceships will become neutral. 
<br><b>Somebody else wins</b><br>
If any of the domination victory conditions are fulfilled by another player or faction all other players have lost the game.

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="advanced"><b>13. Advanced</b></a></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

This chapter contains information about the inner working of the game updater and the game creator algorithms. <br>
Note: the rpg notation XdY is used to describe equally distributed random integers, where X is the number of random integers, and Y is the maximum value. 2d4 means 2 random integers from 1 to 4.<br>
<br><b>Neutral planets randomization</b><br>
When a new game is started all neutral planets are randomized.<br>
First production is set to 1d3 + 1d4 - 1, which result in a value from 1 to 6, with an average of 3,5.<br>
Then resistance is set to 1d4 for planets with production smaller than 4, and 2d3 for larger planets.<br>
Then each neutral planet has a 60% of being closed, and else it is open.<br>
In the gameworld three different shiptypes have been set to be used a neutral planet defence ships, neutralDef1, neutralDef2 and neutralDef3.
Finally the planets defence is determined. <br>
-If the planets production is 1-2, it has 1d3 - 1 neutralDef1 (which means there are a 1/3 chance of having no defence at all...).<br>
-If the planets production is 3-4, it has 2/3 chance of having 1d3 + 1 neutralDef1, and 1/3 chance of having 1d2 neutralDef2.<br>
-If the planets production is 5-6, it has 1/3 chance of having 1d3 + 1 neutralDef2, and 2/3 chance of having 1d3 neutralDef3.<br>
<br><b>Turn sequence</b><br>
Each time the game server updates the game to a new turn it performs the following steps in sequence, and each steps is performed for all players before it moves on to the next step:<br>
1. Update treasury<br>
2. Pay upkeep<br>
3. Check if broke<br>
4. Perform orders<br>
&nbsp;&nbsp;5.1 Expenses<br>
&nbsp;&nbsp;5.2 Move VIPs<br>
&nbsp;&nbsp;5.3 Squadron to Carrier moves<br>
&nbsp;&nbsp;5.3 Move ships<br>
&nbsp;&nbsp;5.4 Change planet visibilities<br>
&nbsp;&nbsp;5.5 Abandon Planets<br>
&nbsp;&nbsp;5.6 Self destruct ships<br>
&nbsp;&nbsp;5.7 Self destruct wharfs<br>
&nbsp;&nbsp;5.8 Screen/descreen ships<br>
&nbsp;&nbsp;5.9 Send messages<br>
5. Perform Black market<br>
6. Move retreating ships<br>
7. Check and perform VIP conflicts (assassins, spies and Jedi duels)<br>
8. Check Governors on neutrals<br>
9. Check and perform spaceship battles<br>
10. Repair and resupply ships<br>
11. Check for defeated players<br>

</div></div>

<div class="Form_Header" style="width:450"><div class="SolidText"><a name="initiative"><b>14. Initiative computation</b></a></div></div>
<div class="Form_Text" style="width:450"><div class="SolidText">

To compute the initiative in spaceship combat, the following algorithm is used.
<br>
First the relative size of the opposing fleets are computed using the following table:
<table  border="0" cellspacing="4" cellpadding="0" class="Mainmenu">
<tr>
<td>Actual # of non-screened ships</td>
<td>Relative size</td>
</tr>
<tr>
<td colspan="2" height="1"><img src="images/yellow_pix.gif" width="360" height="1"></td>
</tr>
<tr>
<td>1</td>
<td>2</td>
</tr>
<tr>
<td>2</td>
<td>2,8</td>
</tr>
<tr>
<td>3</td>
<td>3,5</td>
</tr>
<tr>
<td>4+</td>
<td>2 + Square Root(actual #)</td>
</tr>
</table>
For example, a fleet with 4 non-screened ships counts as relative size 4, 9 count as 5, 16 as 6 and 25 as 7.
<br>
This weight is computed separatedly for capital ships and for squadrons, and then added together. To get the highest initiative from a limited number of ships, an equal number of capital ships and squadrons is preferred.
<b>Computation of initiative base</b><br>
The fleets have a chance of firing each round as their relative size divided by the total relative sizes of the two fleets.
<br>
For example: a fleet with 3 (non-screened) ships is in combat with an enemy fleet with 9 (non-screened) ships. <br>
The 3 ship fleet have a relative size of 3,5 and the 9 fleet have a relative size of 5.<br>
The 3 ship fleet then have 44% (3,5/(3,5+5)) chance of firing each turn, and the 9 ship fleet have a 56% (5/(3,5+5)) chance each turn. 
<br>
<b>VIP bonuses</b><br>
The percentage can be further modified by VIPs with the "initiative bonus" or "squadron initiative bonus" abilities. Ideally you should have two VIPs, one with each ability, to maximize the initiative percentage. 
<a name="randomDamage"><br><b>Randomized damage algorithm</b><br>
Here is a description of the algorithm for deciding the exact damage for any shot a ship fires.<br>
Each time a number between 1-20 is randomized, divided by 10 and the result is multiplied to the base damage.<br>
This means that a ship will do from 5%-200% of its base damage each time it fires on another ship.<br>
Furthermore if the randomized numner is 19 or 20, a new number between 1-20 is randomized and added before it is divided by 10. And this procedure is repeated indefinately until a number less than 19 is produced.<br>
This means that approximately there is a chance of 10% that a ship will do triple damage, a 1% chance of doing x5 damage, a 0.1% chance of doing x7 damage and so on.<br> 

</div></div>

<div class="List_End"></div>		

</div>

<div style="left: 601px;width: 250px;position: absolute;top: 90px;">
		<%@ include file="../puffs/RightPuff.jsp" %>
</div>


</body>
</html>
