<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.news.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="sr.server.ranking.*"%>


<%
	// get PageURL
	String PageURL = request.getParameter("action"); 

		
	User theUser = null;
	User tmpUser = null;
	if (theUser == null){
		theUser = UserHandler.getUser(session,request,response);
		if (theUser.isGuest()){
			// try to check if player is logged in using the session object
			tmpUser = (User)session.getAttribute("user");
		}
		else
		{ 
				// user is logged in using the session object
				theUser = tmpUser;
		}
	}


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<link REL="STYLESHEET" HREF="CSS/style.css">
<link REL="STYLESHEET" HREF="CSS/styleDiv.css">
<head>
<title>SpaceRaze Change Log</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

</head>
<body background="images/spaze.gif">

<div style="left:130px;width:718px;position: absolute;top: 88px;padding-bottom:20px;">
	<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b>Versions</b></div></div>
	<div class="Form_Text" style="width:718">
	<div class="SolidText">
<table width="700" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>
<h2>Change log for different SpaceRaze versions</h2>
This page contains info about what changes have been done in the latest and older versions of SpaceRaze.<br>
It covers both the webpages, the client, the server and game-balancing.
<!--
<P>
<strong>?.?.?</strong><br>
Date ?<br>
 * <br>
 * <br>
 * <br>
-->
<P>
<strong>4.0.5</strong><br>
Date 071115<br>
 * Change in website.<br>
 * Bug fixed: Troops can now attack enemy planetes.<br>
 * Bug fixed: building type panel: child and parent button will be removed as it should<br>
 * Bug fixed: Planet cannon error<br>
<P>
<strong>4.0.4 (current version)</strong><br>
Date 071101<br>
* Change in website.<br>
* Bug fixed: Troops can now attack planetes.<br>
* Bug fixed: Psych warfare countet twice.<br>
<P>
<strong>4.0.3 (current version)</strong><br>
Date 070921<br>
 * Change in research panel: "Me tree" is now listing all on going advantages and all available advantages.<br>
 * Change in research panel: Buildings is now listing in the same way as ships, troops and VIPs.<br>
 * Change in VIP order panel: The location selectbox divide between planets, ships and troops.<br>
 * Bug fixed: Troops killed by bombardment is now removed.<br>
 * Change in website.<br>
<P>
<strong>4.0.2</strong><br>
Date 070919<br>
 * Bugs fixed: VIPs one ship and troops could not move<br>
 * Bug fixed: only non-aliens may have their resistance lowered by besieging<br>
 * Added alignment and faction listings to VIPTypes in databank<br>
 * Troops and siege bonus abilities for ships, factions and VIPs replaced by the psychWarfare ability<br>
<P>
<strong>4.0.1</strong><br>
Date 070911<br>
 * Bugs fixed: Various fixes to Titanium gameworld<br>
 * Bug fixed: could not join Space Opera gameworld<br>
 * Bug fixed: VIPs i databank, removed faction alternatives and renamed all<br>
 * Bug fixed: Ground attack squadrons now attack in land battle sim<br> 
 * Bug fixed: Troop costs<br>
 * Bug fixed: crash when neutral troops on planets conquered by diplomacy<br>
 * Bug fixed: troops conquered on neutral planets with diplomacy have correct kills, hits and tech<br>
 * Bug fixed: ships conquered on neutral planets with diplomacy have correct kills, hits and tech<br>
 * Bug fixed: message when repairing troops<br>
 * Change: Incom from buildings is now comulative<br>
<P>
<strong>4.0.0 (current version)</strong><br>
Date 070905<br>
 * Major addition: New webb design<br>
 * Major addition: Research<br>
 * Major addition: Land battles and troop units<br>
 * Major addition: Buildings<br>
 * Ships larger than small are available later in the black market (medium turn 4, large turn 6 and huge turn 10)<br>
 * Bug fixed: old orders deleted correctly when selecting to selfdestruct a ship<br>
 * Bug fixed: error when saving games when game data is larger than 5 MB<br>
 * Bug fixed: Space Opera faction IRSOL now have correct spaceshiptypes (no ABS and HMF & Q-ships instead)<br>
 * Bug fixed: exterminators now only try to kill enemy infestators<br>
 * Bug fixed: diplomats on neutral planets who are conquered by another player are killed<br>
 * Bug fixed: minor bug concerning updates when selecting ships in minishippanel<br>
 * Bug fixed: alien player with retreating ships who abandon game crash game<br>
 * Bug fixed: Java 6 (1.6) now works and is required in the client<br>
<P>
<strong>3.6.19</strong><br>
Date 061123<br>
 * Bug fixed: error when creating new players, also cause client Battle Sim to crash<br>
 * Increased byte array size in client and tunnel from 2 MB to 5 MB<br>
<P>
<strong>3.6.18</strong><br>
Date 061120<br>
 * Bug fixed: error label in notifier update correctly<br>
 * Corruption info shown in income & expence panel and in turn info<br>
 * Bug fixed: governor killed on neutral planet because of attack by friendly force, is now handled correctly<br>
 * Changed font in client battle sim force A & B textareas<br>
 * Bug fixed: squadrons starting at neutral planets are not removed (fixed by Tobbe)<br>
<P>
<strong>3.6.17</strong><br>
Date 061111<br>
 * Bug fixed: status field in notifier now show correct info<br>
 * When "-> BattleSim" button is pressed in ship panel the battle sim is shown<br>
 * Bombardment is now always non-cumulative in all GameWorlds<br>
 * Squadrons never survive outside friendly carrier on enemy planets in all gameworlds<br>
 * Players can now lose income and upkeep to corruption when they grow big. Different faction can have different corruption levels. Games in progress will have no corruption.<br>
<P>
<strong>3.6.16</strong><br>
Date 061108<br>
 * Bug fixed: "(t:100)" error when copying ship to battle sim fixed<br>
 * Improved GUI for SpaceRaze Notifier<br>
 * Players no longer need to enter a login in the SpaceRaze Notifier<br>
 * Bug fixed: changed tooltip text in loginpanel<br>
 * Governor name and faction choice widened in login panel<br>
 * Bug fixed: assassins, duels and counter espionage can now happen from and at spaceships<br>
<P>
<strong>3.6.15</strong><br>
Date 061101<br>
 * Aliens can now get VIPs when conquering neutral planets, just like non-alien players. They can even get a random VIP when conquering a planet that is razed initially<br>
 * Bug fixed: wharf list scroller is now working properly<br>
 * Ships destinations lists planets are now sorted<br>
 * Ship list at planet is now sorted by type, cost and name<br>
 * Shiptypes in wharf build lists are now sorted by build cost<br>
 * Wharfs in wharf list are now sorted by size<br>
 * VIP destinations lists planets are now sorted<br>
 * Bug fixed: notes now have a scroller<br>
 * Bug fixed: razed planets production and resistance are now displayed correctly in the map (if a player has a spy etc) as "- / -"<br>
 * Bug fixed: squadrons without move can't retreat on their own<br>
 * Servlet tunnel now zip the data transferred<br>
 * Lost In Space is now included in new turn mails<br>
 * A progress panel is now shown when saving and loading<br>
 * Players can now create notes for individual planets and the notes are shown in the map<br>
 * Bug fixed: Tomcat HEAP size increased to 256 MB, otherwise games on large maps may crash<br> 
 * Notifier application added, which is started through Java Web Start from a link on the startpage<br> 
 * Bug fixed: planets can no longer be saved with empty name field in the MapEditor<br>
 * Bug fixed: planetary survey will now register as last known data about planet<br>
 * Last known production and resistance is now shown in the map in gray<br>
 * Version field added to maps and it is increased every time a map is published in the map editor<br>
 * Version field added to gameworld<br>
 * New auto-fill carriers button added in ship panel (by Tobbe)<br>
 * Black Market offers are removed if there have been no bids for 3 turns<br>
 * Short description field added to GameWorld, and it is shown in the DataBank (by Tobbe)<br>
 * Short description, advantages, disadvantages and history fields added to Faction, and they are shown in the DataBank (by Tobbe)<br>
 * Short description, advantages, disadvantages and history fields added to ShipType (by Tobbe)<br>
 * History field added to VipType (by Tobbe)<br>
 * Games that have been Game Over for more than 2 weeks are removed automatically from server<br>
 * Players who are broke for more than 5 turns in a row lose the game. A warning will be given after 4 turns.<br>
 * Bug fixed: diplomacy only workd on non-razed neutral planets<br>
 * Bug fixed: VIPs with siege bonus can give bonus even if they are on a civilian ship<br>
 * SpaceRaze Expanded NC GameWorld removed, SpaceRaze Expanded gameWorld now have non-cumulative bombardment<br>
 * Bug fixed: empty line missing in turn info for alien player who has planet under siege<br>
 * Battle Simulator added to game client<br>
 * Statistics added to game client<br>
 * Bug fixed: wrong cost of upgrading orbital wharfs<br>
 * Bug fixed: squadrons who will be scuttled due to lack of carrier, cannot besiege a planet<br>
 * Bug fixed: Orderpanel is now updated immediately if new orders are given to a ship in the shippanel<br>
 * Bug fixed: abandoned squadrons automatically attached to a carrier can no longer exceed a carriers capacity<br>
 * Web-based battle sim nerfed: fewer iterations, fewer max nr ships and less cpu demanding simulations<br>
 * Ships can now be copied from the ships panel to the battle sim panel<br>
<P>
<strong>3.6.14</strong><br>
Date 060916<br>
 * Bug fixed: planets of defeated alien players are now razed and all ships and wharfs of the defeated alien player is destroyed<br>
 * Bug fixed: planets abandoned by alien players are razed and any wharfs and space stations are destroyed<br>
 * Bug fixed: if client can not reach server during save error message is shown and client does not freeze up<br>
 * Game can now be saved even if server is restarted after loading a game in client<br>
 * Min number of steps are now shown on current_game.jsp and in new game mails<br>
 * Bug fixed: all users is now shown in user list admin page<br>
 * Bug fixed: improved VIP conflict recursive algorithms and they are now about 1000 times more effective, and thus avoiding stack overflow in games with large number of VIPs<br>
<P>
<strong>3.6.13</strong><br>
Date 0600821<br>
 * Orbital Structure is now always called Space Station<br>
 * New show most used GameWorld and map functions<br>
 * Bug fixed: minor error causing a NullPointerException when aUser is null<br>
 * Bug fixed: causing maps with large number of planets to crash map_view.jsp<br>
 * Bug fixed: spelling error in manual (raxed...)<br>
 * Bug fixed: game crash when ship carrying VIP is selfdestructed<br>
<P>
<strong>3.6.12</strong><br>
Date 6.7.9<br>
 * Orbital wharfs always destroyed when a planet is razed<br>
<P>
<strong>3.6.11</strong><br>
Date 060708<br>
 * Bug fixed: alien planets can now be besieged<br>
 * Bug fixed: errors in siege algorithm concerning alien planets and alien besieging taskforces<br>
 * Bug fixed: reconstruct checkbox visible after game is over<br>
 * Bug fixed: retreats panel is now enabled<br>
 * Bug fixed: reconstructed planet will not have red X as if razed<br>
 * Added highlight for reconstructing planets<br>
<P>
<strong>3.6.10</strong><br>
Date 060629<br>
 * Income from ships is now shown in gameworld and faction pages<br>
 * Shiptypes can now be sorted by name in shiptypepanel in game client<br>
 * New icon (yellow check) for saved games on startpage list<br>
 * Bug fixed: Error in sw gameworld fixed causing it to crash on turn 1<br>
 * Bug fixed: a player need survey to see max prod on a closed planet, not only a ship in the same system<br>
 * Bug fixed: error when changing squadron orders to move to carrier instead of planet<br>
 * Bug fixed: squadron to carrier moves not visible in orders panel<br>
<P>
<strong>3.6.9</strong><br>
Date 060621<br>
 * Bug fixed: civilian ships cannot besiege or bombard planets<br>
 * Bug fixed: error on versions.jsp<br>
 * Bug fixed: error on beginners_guide.jsp<br>
 * Changed text in beginners guide concerning besieging planets and that aliens are not covered in the guide<br>
 * Bug fixed: wrong planet graphics, values and text for razed and alien planets i map panel and ShowPlanet panel<br>
 * Added (short) text about infestator VIP ability to manual<br>
 * Admins can now update, delete and start games through the start game and game details pages<br>
 * Admin Games removed from left menu<br>
 * PHash link in left menu (admins only) renamed to Counters<br>
 * Bug fixed: only alien players with the same alignment as an infestator can use the infestate ability<br>
 * Players can now choose to save a turn without automatically causing the server to update<br> 
 * Bug fixed: give orders to several squadrons without range at the same time<br>
 * Maximum production is now shown for planets<br>
 * Bug fixed: error when counting aliens size when computing victory conditions<br>
 * Bug fixed: aliens should not be able to increase population<br>
 * Factions now have a maximum how much resistance can be increased<br>
 * Bug fixed: correct MiniPlanetPanel controls for razed planets<br>
 * Bug fixed: VIPs in abandoned squadrons is now handled<br>
<P>
<strong>3.6.8</strong><br>
Date 060608<br>
 * GameWorlds can now have different ratios of open/closed neutral planets<br>
 * Bug fixed: File name for map changed to full map name in new game mails<br>
 * Factions list in game client is now sorted<br>
 * Factions can now have differents costs for upgrading wharfs to different sizes<br>
 * Factions can now lack the ability to upgrade wharfs to certain sizes<br>
 * Bug fixed: "AAn" in VIP highlights<br>
 * Decreased weight of current damage capacity when determining when a task force should flee<br>
 * Bug fixed: squadron weapon strength now added when determining when a task force should flee<br>
 * Now compute actual attack strength including damage to ships when determining when a task force should flee<br>
 * New shiptype ability: visible on planet<br>
 * Bug fixed: applet uses wrong port to servlet tunnel<br>
 * Bug fixed: wrong shields value in battle report (could have more then 100% shields left)<br>
 * Razed planets are only visible if visited by a player (ships or spy) in the same way as ownership is visible<br>
 * Bugs fixed: minor bugs when showing razed status on planets<br>
 * GameWorlds can now start with a percentage of the planets razed (and closed)<br>
 * New faction ability: reconstruct, can rebuild razed planets. Differents factions can also have different costs to rebuild planets<br>
 * Bug fixed: error in text when a planet is persuaded to join a player<br>
 * Bug fixed: check for enemy vips on planets conquered by diplomacy<br>
 * Bug fixed: messeges to factions with blanks in the name was sent as public message instead<br>
 * Bug fixed: public and faction messages display error if factions have blanks in the name<br>
 * New faction ability: alien. Alien players live on razed planets and use resistance as source for income and support. Cannot use diplomacy VIP ability, but can use infestate instead.<br>
 * New VIPType ability: infestate, can infest planet to join an alien player<br>
 * New VIPType ability: exterminate, can kill infestators<br>
 * Destination list for ships and vips on the Black Market is now sorted<br>
 * Bug fixed: error in computation of faction color in faction jsp page<br>
 * Gameworlds can now define their own set of alignments and how the alignments should work<br>
 * Bug fixed: VIPtype description texts for wharf build and upgrade bonus added, and build squadron text changed<br>
 * Bug fixed: wrong percentage limit for faction win (was 60%, should have been 70%)<br>
 * Changed percentage for solo player win from 60% to 65%<br>
 * Changed percentage for solo faction win to 75% (see bug above)<br>
 * SpaceRaze Expanded GW: ISD & SSD now have siege bonus of +1 and +2<br>
 * SpaceRaze Expanded GW: League now has +2 income bonus on open planets<br>
 * SpaceRaze Expanded GW: Pirate faction no longer have a general siege bonus<br>
 * SpaceRaze Expanded GW: Pirate Raider now have a siege bonus of +1 and is somewhat weaker<br>
 * SpaceRaze Expanded GW: Rebels now start with an extra VIP: a Diplomat<br>
 * SpaceRaze Expanded GW: TIE-defender, X-wing & A-wing squadron attack increased<br>
 * All players who susvive until the end of a game are awarded an extra ranking point (including winners)<br>
 * All players on the winning faction are awarded an extra ranking point<br>
 * Surviving players on the same faction as a 65% winner gets a cooperative victory and two ranking points (1 for surviving and 1 for being on the winning faction)<br>
 * Factions can now start with a Space Station on their homeplanet<br>
 * SpaceRaze Expanded GW: Empire, Rebels and League now start with a Space Station on their homeplanets<br>
 * SpaceRaze Expanded GW: Pirates cannot have any space stations<br>
 * Players can choose which factions should be selectable for players when starting a new game<br>
 * Players can choose that faction should be random for players when starting a new game<br> 
 * Bug fixed: supply level text for spaceship types error<br>
 * New panel in client: Alignments in the DataBank<br>
 * Bug fixed: assassins can now assassinate VIPs on spaceships in the same system as the assassin<br>
 * Bug fixed: siege bonus for VIPs and ships are only used if there are troops in the same fleet<br>
 * Admins can now update and delete games from the game details page<br>
 * Bug fixed: VIPs not killed correctly if on conquered planet<br>
 * Improved messages to players when a VIP is killed on a conquered planet<br>
 * Added info in client and jsp page about GameWorlds ratios for open/closed neutral planets<br>
<P>
<strong>3.6.7</strong><br>
Date 060405<br>
 * Bug fixed: weighted initiative computation error<br>
 * Bug fixed: good faction players cannot buy evil vips and vice versa<br>
 * Improved faction panel layout for space stations<br>
 * Changed sorting order in shiptype panel to include civilian ships<br>
 * Removed civilian ships from battle sim shiptype list<br>
 * Added info in shiptype panel: income, civilian and look like civilian<br>
 * Changed armor and weapons layout in shiptype panel<br>
 * Added short name to shiptype panel<br>
 * Added xenomorph field to factions panel<br>
 * Added sorting of planets in map controls "Center On" list<br>
 * Squadrons will now be attached to a carrier if possible instead of scrapped<br>
<P>
<strong>3.6.6</strong><br>
Date 060323<br>
 * Bug fixed: first turn messages<br>
 * Retreating ships can now be scheduled for destruction<br>
 * Bug fixed: games crash if there is no winner (not due to razings)<br>
 * Game can now be abandoned<br>
 * Shiptype panel changed to DataBank, containing data for both shiptypes, VIP types, factions and gameworld<br>
 * Bug fixed: wrong highlight text for diplomat<br>
 * Added persistant hashmap object database, primarity for counters<br>
 * Added counters for several pages, finished games, battle sim and more...<br>
 * Added shitype filtering in shiptype panel<br>
 * Governor now have a +2 resistance bonus in SpaceRaze Classic & SpaceRaze Expanded<br>
 * League & Empire now start with NebB,Crv,StC in SpaceRaze Expanded<br>
 * Pirates now start with a Golan II in addition to their usual ships in SpaceRaze Expanded<br>
 * Pirates can now build GI & GII in SpaceRaze Expanded<br>
 * Cheaper Space Stations in SpaceRaze Expanded<br>
 * Removed "name" from "Governor name" in lower middle panel in client, allowing for longer governor name<br>
 * Added lists of sent and recieved messages to messages panel in client<br>
 * Bug fixed: error when computing upgrade wharf cost<br>
 * Bug fixed: no ship can cost less than 1 regardless of bonuses<br>
 * SR expanded expert engineer now decrease squadrons cost by 1<br>
 * Bug fixed: Factions can now have space in the faction name<br>
 * Bug fixed: could crash when computing a random VIP for a new player<br>
 * Bug fixed: starting messages, minor language errors fixed<br>
 * When starting a new game, players from the same faction can now be placed closer together<br>
 * New spaceship ability: can give income on different types of planets (open/closed, own/friendly/neutral/enemy)<br>
 * Spaceship ability to besiege planets can now be turned off for specific spaceship types in the gameworld<br>
 * Spaceships can now look as a civilian ship<br>
 * Spaceships can now be civilian, which means that they can not fight, and are automatically destroyed by enemy military ships<br>
 * Bug fixed: in special cases VIPs could not move from a besieged neutral planet as they should<br>
 * Added info about gameworld & grouping of players from the same faction to the new game mail message body<br>
 * Faction images removed from game client, and game gui rearranged<br>
 * GameWorld and map info included in turn info panel in game client<br>
 * Improved ranking sorting algorithm: players with the same ranking score are sorted with the least number of losses higher<br>
 * Minor error in manual<br>
 * Gov names limited to 20 characters<br>
 * Bug fixed: orderpanel shows incorrectly when giving planet orders<br>
 * Bug fixed: in some cases players could give orders after a game was over<br>
<P>
<strong>3.6.5</strong><br>
Date 061215<br>
 * Rearranged minishippanel widgets<br>
 * Bug fixed: details button on minishippanel visible before a ship is chosen<br>
 * Moved shiptype description box to shiptype panel from minishippanel<br>
 * Changed to short name for carrier names in a squadron destination drop-down in minishippanel<br>
 * Changed to short name for shipnames VIPs destination drop-down in minivippanel<br>
 * Changed to short name for shipname destination in VIP list in minivippanel<br>
 * Changed to short name for VIP ship location in minivippanel<br>
 * Rearranged fields in minivippanel<br>
 * Added alignment field in minivippanel<br>
 * Added short vip name to type field in minivippanel<br>
 * Removed damage through shield in battle report when a ship is destroyed<br>
 * For each kill duellist and assassin skill increases with +5<br>
 * There is always a 5% chance that the least skilled duellist wins in a duel<br>
 * An assassin can never have more than 95% to assassinate another VIP<br>
 * Added to manual about changed rules about kills<br>
 * Bug fixed: faction win turn info message<br>
 * Spaceship with kills now get increased weapons & shields<br>
 * Added to manual about kills increasing weapons and shields of spaceships<br>
 * Added gameworld and current active players to current games list<br>
 * Added build and supply costs to battle sim results<br>
 * Decreased number of simulated battles in battle sim from 200 to 100<br>
 * Bug fixed: minor rounding error in battle sim results<br>
 * Bug fixed: updaterunner does not update finished games<br>
 * Added more fields in current game page<br>
 * Mails are no longer sent to defeated player when game updates (unless they were defeated in the last turn)<br>
 * Added shiptype description field to SpaceshipType class<br>
 * Added description to factions<br> 
 * Added description to VIP types<br>
 * Games can now be password protected to join<br>
 * Bug fixed: wrong message when ships are stopped from retreating by a ship with stops retreats<br>
 * Added check to game name when starting a new game, can only contain letters, digits, space and -<br>
 * Ships are now repaired and resupplied later in the game round, check manual -> Advanced -> Turn sequence for details<br>
 * Check for end of game is now performed at the end of each turn instead of in the beginning<br>
 * Bug fixed: several minor errors in end game messages<br>
 * Bug fixed: fixed several minor errors in killed VIP messages & highlights<br>
 * Added new shiptype ability: attack screened ships<br>
 * Shiptype list i game client is now sorted<br>
 * Lib files moved from common/lib to SpaceRaze webapp<br>
 * Various improvements with webapp and CVS structure and content<br>
 * New gameworld parameter: if squadrons should survive without a carrier on non-friendly planets<br>
<P>
<strong>3.6.4</strong><br>
Date 060120<br>
 * Bug fixed: shiptypepanel now shows survey ability correct<br>
 * Bug fixed: retreating squadrons, who can move, in carrier causes games to crash<br>
 * Bug fixed: ships scuttled by their crews when they have nowhere to retreat to, causes games to crash<br>
 * Bug fixed: error causing players to be erased from users database<br>
<P>
<strong>3.6.3</strong><br>
Date 060117<br>
 * Minor changes to gameworlds<br>
 * Bug fixed: Space Stations on abandoned planets<br>
 * Fixed messages concerning stopped retreats from ships with Stops Retreat ability<br>
 * Bug fixed: neutral fleets not retreating<br>
 * VIP type can now have Black Market frequency "never"<br>
 * Bug fixed: squadrons assigned to a Carrier is now reloaded after each won battle<br>
 * Manual: updated with info about carriers and retreats<br>
 * Bug fixed: squadrons removed from their carrier if the carrier is destroyed/selfdestructed/scuttled<br>
 * Bug fixed: diplomats belonging to the same players do not block each other when persuating the same planet<br>
 * Manual: added that several diplomats can cooperate to persuade a planet faster<br>
 * Bugs fixed: checkboxes on mini planet panel now hide correctly on abandoned planets<br>
 * Bug fixed: selfdestruct on mini wharf panel now update orders panel correctly<br>
 * Added Pirate emblem<br>
 * Removed hardcoded default admin user login<br>
 * Game name is now shown in page title on game client jsp page<br>
 * Improved gameworlds list in gameworlds page<br>
 * New gameworld: Corporate Space, a basic gameworld ideal for new players<br> 
 * Changed so that short name on ships is shown i shiplist in MiniShipPanel<br>
 * Changed what fields are shown in MiniShipPanel<br>
 * Added link from MiniShipPanel to shiptype panel to show data for a specific ship<br>
<P>
<strong>3.6.2</strong><br>
Date 051219<br>
 * Players can now start a number of games<br>
 * Bug fixed with wrong bombardment value in label in MiniShipPanel<br>
 * Added "Show on open planet" ability to Expert Engineer and Economic Genious VIP types<br>
 * Bug fixed concerning neutral ships trying to run away (which they can't)<br>
 * Bug fixed concerning naming and numbering of ships gained through diplomacy<br>
 * Squadrons loaded inte a carrier will now retreat with their carrier if running away due to Max Size<br>
 * Bug fixed concerning "Build Space Station" checkbox in MiniPlanetPanel<br> 
 * Bug fixed concerning messages from duels<br>
 * Bug fixed concerning moving a short range ship to a long range planet, with a FTL VIP who has a move order from that ship<br>
 * Bug fixed so that players can only view orange planetary connections from their own faction<br>
 * Games will no longer update automatically if last update contained an error<br>
 * Minor changes to gameworlds<br>
<P>
<strong>3.6.1</strong><br>
Date 051206<br>
 * Bug concerning naming & numbering of spaceships fixed<br>
 * New factions page under gameworld page<br>
 * Added spaceshiptype sorting in gameworld and faction pages<br>
 * Minor layout changes to the battle sim page<br>
 * Fixed bugs with battle sim parsing errors<br>
 * Fixed bug concerning scuttling of squadrons in neutral or hostile systems<br>
<P>
<strong>3.6.0</strong><br>
Date 051202<br>
 * Bug fixed concerning admin log files list page<br>
 * Admin view log page now loads alot faster<br>
 * Defeated icon is shown in games list for individual defeated player, not only after a game has finished<br>
 * Governor income bonus on planets are no longer cumulative with income bonuses from other VIPs<br>
 * Highlight message has been added when a Jedi kill another of the same players Jedis<br>
 * Spaceraze can now be played in <b>several GameWorlds</b> (initially three), each with unique sets of factions, VIPs and spaceships<br>
 * When defining VIP-types in a GameWorld, assassins, counter-spies & Jedis can now have different skill levels<br>
 * Bug fixed when selecting multiple ships when broke<br>
 * VIPtypes can now have variable bonus to decrease resistance of a besieged planet (example Generals have -1)<br>
 * GameWorlds can have comulative bombardment or only use largest bombardment in a fleet<br>
 * Factions can now have siege bonus, and is only effective when troops are present<br>
 * New VIP ability: FTL mastery. Enables a short range ship to travel at long range.<br>
 * VIPTypes can now be more or less common<br>
 * Different factions can now have different size on their starting orbital wharf<br>
 * All shiptypes in a GameWorld can now be eligble for the Black Market except those who can't move<br>
 * GameWorlds can now use any one of three different methods for computing initiative base ratio<br>
 * Shiptypes can now have armor that reduces damage against different types (sizes) of damage<br>
 * Neutral planets can now have 1-3 GIII instead of 1-2 GIII<br>
 * Different GameWorlds can now have different neutral defence ships<br>
 * Players from different factions can now start with different spaceships<br>
 * New spaceship ability: planetary survey, can se production/resistance of visited planet<br>
 * Different factions can now have different costs for building and upgrading orbital wharfs<br>
 * Bug fixed causing besieging player to be defeated as well when a governor from the same faction is killed on the besieged neutral planet<br>
 * Factions can now have tech bonus affecting all ships built by that faction<br>
 * Added check for unique planet names when publishing in MapEditor<br>
 * Different factions can now start with a number of specified or random VIPs<br>
 * The spaceshiptype abilities stops retreats and siege bonus are now two separate abilities<br>
 * Removed logging showing VIP locations in browser Java console<br>
 * Rebels now begin with a Dreadnaught instead of a Nebulon B Frigate<br>
 * New type of Orbital Structure: Space Stations<br>
 * Spaceships are now divided into two types: capital ships and starfighter squadrons<br>
 * All ships now have a separate attach value for firing upon squadrons<br>
 * Capital ships can now have the new ability to carry starfighter squadrons (and become a starfighter carrier)<br>
 * In the WEIGHED initiative algorithm having both squadrons and capital ships will give an extra bonus<br>
 * Squadrons that are at a neutral of hostile planet without at least one carrier in system will be destroyed<br>
 * Battle Sim has been modified to work with all gameworld<br>
 * All ships now have a new value, TargetingType, determining how often that ships target capital ships and squadrons<br>
 * Starfighters attached to a carrier gets reloaded by the carrier after every combat<br>
 * VIPs with initBonus must be on a capital ship to get init advantage<br>
 * VIPs with squadronInitBonus must be on a unscreened squadron to get init advantage<br>
 <br>
<b>Important: no more changes to individual GameWorlds will be entered into this change log!</b>
<P>
<strong>3.5.3</strong><br>
Date 050926<br>
 * Bug fixed concerning retreating ships on maps where not all planets can be reached by short range<br>
 * Crv & StC -5 shields, -15 hits & -5 small+ arnament<br>
 * NebA -20 shields & -5 large+ arnament<br>
 * NebB -5 med+ & +5 small+ arnament<br>
 * Drd -5 med+ & +5 small+ & +20 large+ arnament<br>
 * VSD,MCC,ISD & SSD +5 small+ arnament<br>
 * Manual (shiptypes table) updated with new weapons sizes<br>
 * Bug fixed concerning extra mails sent about ended games<br>
 * League open planets bonus is decreased to +1 (from +2)<br>
 * Recipient label i messages popup widened<br>
<P>
<strong>3.5.2</strong><br>
Date 050903<br>
 * Ship weapons now come in four sizes, and some of them can have limited number of shots<br>
 * Most shiptypes have been modified to use the new weapon sizes<br>
 * Ships can now have the ability to resupply other ships weapons<br>
 * New shiptype created: the Supply Freighter (available to all factions)<br>
 * Renamed "Server admin" to "Games admin"<br>
 * Updated text in games admin page and removed references to server<br>
 * Modified (and simplified) the requirements page<br>
 * Fixed bug with VIPs showing on open planets<br>
 * Fixed images for Lan & SF<br>
 * Fixed bug in startpage current games list<br>
 * Fixed bug causing a game to crash when engaging neutral enemies<br>
 * Changed so that the client applet can use an archive (jar) file to load classes<br>
 * Fixed bug concerning clearing of weapon fields when selecting multiple ships<br>
 * Fixed bug concerning besiged planets from defeated player<br>
<P>
<strong>3.5.1</strong><br>
Date 050828<br>
 * New spaceship ability: initiative defence<br>
 * New VIP ability: initiative defence<br>
 * New VIP type: AA Master Gunner, give +10% init defence<br>
 * New shiptype: Lancer Frigate (Empire only)<br>
 * New VIPtype & shiptype added to BattleSim<br>
 * Starfighter Squadron now cost 3 in support<br>
 * Changed SpaceRazeApplet & MapEditorApplet so that they get applet codebase themselves instead of using a property in a properties file<br>
 * Changed how wharfs are shown in map<br>
 * Ships are now shown in one sorted column per planet, with numbers denoting where there are more than one ship per class<br>
 * VIPs onboard ships are now shown in paranthesis behind the ships class at the planet on the map<br>
 * New VIP ability: visible on open planets. A VIP with this ability can be seen on the map on open planets by all other players<br>
 * Expert Engineer & Economic Genious is visible on open planets<br>
 * Additional Info textarea removed from MiniPlanetPanel, since all that info are now shown in the map<br>
 * Removed "Show VIPs / Show ships"-button from MapControls, it is not needed any more<br>
 * Increased size of "Show Map"button in MapControls<br>
 * Renamed "Versions/Release Notes" to "Change Log"<br>
 * Fixed bug in Black Market offer popup<br>
<P>
<strong>3.5.0</strong><br>
Date 050824<br>
 * Fixed bug concerning defeated player fleet retreating from neutral planet<br>
 * Fixed error in label text on create new game page<br>
 * Fixed shiptype name errors in battle sim page<br>
 * Fixed bug with startplanet data transferred between games with same map<br>
 * Fixed error in manual concerning Interdictor range & troops capacity<br>
 * Fixed spelling error in League starting message<br>
 * Fixed properties cashing problem, thereby solving runtime update of ranking list<br>
 * Players can now create and edit their own maps in the SpaceRaze Map Editor<br>
 * Fixed bug with list item prefix when selfdestructing a non-moving ships<br>
 * Fixed bug when deselecting selfdestruct for a non-moving ship<br>
 * Changed battle sim results to %<br>
 * Fixed admin properties save error concerning rankings<br>
 * Fixed initial startplanet selection in map<br>
 * Players can now start one game each<br> 
 * Changed so that VIPs move before spaceships (as they should)<br>
 * Orbital wharfs is now shown in the map<br>
 <P>
<strong>3.4.8</strong><br>
Date 050615<br>
 * Fixed typing error in spaceships page with GIII<br>
 * Removed blank from beginning of some highlights in new turn mails<br>
 * Fixed errors in first turn messages (League & Rebels)<br>
 * Fixed spelling error in turn info text when winning a black market offer<br>
 * Fixed JavaScript error in login.jsp, focus is now correctly set on login input field<br>
 * Battle sim does not run at 100% speed any longer, and can allow server to perform other tasks at the same time<br>
 * Different # iterations and max nr ships for admins and players in Battle sim<br>
 * New maps page, with images and information about all maps<br>
 * New games will read map data from text/properties file instead of using compiled data<br> 
 * New Create new maps page created, with information and an example of how to create new maps<br>
 * Fixed spelling error, govenor -> governor<br>
 * Admin can now choose maximum number of players when starting a new game<br>
 * "Create new game" now fetches a dynamic list of all maps<br>
 * Updated requirements page<br>
 * Improved error handling for crached games<br>
 * Improved logging, especially for crashed games<br>
 * Automatic send emails to admins when errors occurs while updating is performed<br>
 * Sent messages is now shown in the turn info panel<br>
 * Fixed update bug in Inc & Exp panel<br>
 * Improved Inc & Exp panel fields layout<br>
 * Fixed bug with cost of increasing production<br>
 * Improved economy report in turn info (which is also included in new turn mails)<br>
 * Players can now open the game client in a separate window<br>
 * Fixed bug concerning giving move orders to multiple ships when broke<br>
 * If governor is on a retreating ship player can not give any move orders to ships and VIPs<br>
 * Fixed bug when removing all VIPs from a defeated player<br>
 * Governor is killed on a neutral planet if it is attacked or besieged by any member of the same faction<br>
 * Governor name is now verified and only letters and blanks is allowed<br>
 * Fixed bug with textarea in turn info panel<br>
 * Neutral planets are now persuaded by governors before combat phase, this 
 should stop situations where a neutral planet isn't attacked by an arriving 
 enemy force the same turn as it joins a player.<br>
 * Fixed bug with Strike Cruiser image file<br>
 * Added news functionality, including last news article on startpage and a news archive page<br>
 * Fixed error in histary page<br>
 * Fixed error in menu<br>
 * Added text about spaceship movement, fixed several spelling errors and removed a small HTML bug from the manual page<br>
 * Added details to Turn Sequence chapter in manual<br>
 * If governor is at a neutral planet and there are also at least one hostile gov present, no-one will go forward in persuation process<br>
 * If governor is at a neutral planet and there are also at least one friendly or hostile gov present, the planet will not join anyone<br>
 * More feedback to players in Turn Info about persuation of neutral planets, including the knowledge when there is only onte turn left to joining<br>
<P>
<strong>3.4.7</strong><br>
Date 050511<br>
 * Modified MailHandler to be able to communicate with smtp servers that do not require AUTH<br>
 * Added functionality to battle sim page making it easier to use and more advanced<br>
 * Admins can now start games without automatic emails being sent to all players<br>
<P>
<strong>3.4.6</strong><br>
Date 050510<br>
 * Fixed battle sim page error for players<br>
 * Ship list on a planet is now multiple select (ctrl/shift)<br>
 * Selfdestruct status is now shown in ship list on a planet<br>
 * Added text about retreats in manual<br>
<P>
<strong>3.4.5</strong><br>
Date 050509<br>
 * Fixed bug: Drd & Int is now long range as they should<br>
 * Fixed bad link from edit user page<br>
<P>
<strong>3.4.4</strong><br>
Date 050509<br>
 * Added email fields to user admin page for new player<br>
 * Send a welcome email with site url, login & password to a new user<br>
 * Removed javax.mail code from MailHandler<br>
 * New Edit User page for admins<br>
 * Battle simulation results page replaced by the battle simulator page<br>
 * Turn off logging while performing battle simulations<br>
 * Added contact page to guest users<br>
 * Planets under siege or blockade give support, but not income<br>
<P>
<strong>3.4.3</strong><br>
Date 050503<br>
 * When a govenor is killed, all the defeated players ships at planets not owned by him are scuttled, and all other ships and all his planets are turned into neutrals<br>
 * Turn info and highlights are now included in new turn e-mails<br>
 * MCC +5 damage<br>
 * Drd -20 hits<br>
 * NebA +10 shields<br>
 * NebA +10 hits<br>
 * New page with battle simulation results<br>
 * Link to SpaceRaze website now included in new turn mails, with auto login and password encrypted<br>
 * Link to SpaceRaze website now included in new game open to join mails, with auto login and password encrypted<br>
 * Interdictor is now long range<br>
 * Interdictor no longer carries any troops<br>
<P>
<strong>3.4.2</strong><br>
Date 050428<br>
 * If govenor is on neutral planet and production is known, # of turns until the planet joins the player is shown<br>
 * Govenors are now killed if on a neutral planet that is razed<br>
 * Last known prod/res for razed planets is no longer shown, "-/-" is shown instead <br>
 * Interdictors now stops all types of retreats<br>
 * Message is shown in turn info if a fleet cannot retreat because they are all defence platforms<br>
 * New map: Solen15, a 15 players map for up to 15 players. All names in the map are based on Star Wars. Map is made by Nicklas Ohlsen.<br>
 * Only League can have Victory Star Destroyers (VSD)<br>
 * Lost in Space now only shows actual losses, and does not show factions/neutral if there is no losses. If no losses whatsoever "None" is shown.<br>
<P>
<strong>3.4.1</strong><br>
Date 050426<br>
 * Govenors can no longer move to razed planets<br>
 * After player has changed password, he can now play old games<br>
 * Besieged planets are not eligble to retreat to<br>
 * Ships can run away from and the return to the same planet, if it still owned by the player and not besieged<br>
 * Ships will (once again) retreat, several turns if needed, until they reach a planet owned by the same player and not besieged<br>
 * Added lost in space enrty if retreating ship is scuttled<br>
 * Added highlights if a player is defeated and if a players govenor is killed<br>
 * Added highlights and turn info to all other players when a player is defeated<br>
 * Hides ship, VIP and wharf minipanels if a player has been defeated. Also hides all checkboxes and most labels in miniplanetpanel.<br>
<P>
<strong>3.4.0</strong><br>
Date 050422<br>
 * Destination of a VIP is now shown in the list over VIPs on a planet<br>
 * Added cost of upgrades/ships in wharf panel<br>
 * Fixed spelling error in faq<br>
 * Adjusted turn 1 message texts<br>
 * Cost of upgrading res/prod, build new wharf, and income bonus of open planet is now shown in planet panel<br>
 * Removed flickering when showing planet/ships/VIPs/wharfs<br>
 * Message and Notes textareas now wrap text<br>
 * Fixed bug that makes ships appear in retreats panel even after they have finished their retreat<br>
 * Added attack max size info to map<br>
 * Production/resistance on own, open and spied upon planets are now shown in map<br>
 * Added choice of scheduled updates to new games<br>
 * Fixed bug where player could save data even if server had already updated<br>
 * Modified find startplanet algorithm to check both short and long distance<br>
 * Administrators can now se which players are participating in games (not only Govenor names)<br>
 * Changed label text in shiptype panel to reflect init supply units<br>
 * Shields and hits adjusted for all shiptypes (lesser shields, more hits)<br>
 * Escort carrier and Golan III are now init support units<br>
 * GIIB +2 cost<br>
 * Added short update description to new games list on startpage<br>
 * Added short next update description to current games list on startpage<br>
 * Guest users can no longer change the Guest password :-)<br>
 * Fixed spelling error in In a Nutshell<br>
 * Added to FAQ about maximum production increase on planets.<br>
 * Added new icons for victory and defeat on the current playing games list on startpage.jsp<br>
 * Bug with combo box popup list on popup window panels fixed<br>
 * Bug with focus on combo box on popup window panels fixed<br>
 * Changed text in manual that SSD is huge, not large<br>
 * Added to FAQ about how to besiege planets<br> 
 * Added email functionality, server emails players about new games, updated games and admin messages<br>
 * Probably fixed bug when deleting a game that has not started<br>
 * New map: wigge12. Up to 12 players. Based on the Wigge9 map.<br>
 * Adjusted location and list info on mini wharf panel <br>
 * Adjusted location info on mini VIP panel <br>
 * Word-manual replaced by HTML-version. Word-version of manual will be discontinued.<br>
 * Several rewrites and corrections has been made to the new HTML-manual.<br>
 * Fixed bug with player name in turn info textarea when sending a message<br>
 * In list over games to delete (for admins), game over status is now displayed<br>
 * Disables some buttons in the navbar in the game client if a player is defeated<br>
 * Save files are now compressed<br>
 * Added to FAQ about map shortcuts<br>
 * Last known production and resistance is now shown on closed planets<br>
 * If a besieging force does not lower the resistance of a planet a message is written in the info panel<br>
 * Retreating ships only retreat one turn before player regains control over the ship<br>
 * Retreating ships that move the same turn as they are forced to retread does not automatically retreat to their old location<br>
 * Ships can retreat several turns in a row without selfdestructing<br>
 <P>
<strong>3.3.3</strong><br>
Date 050321<br>
 * Cannot save a black market bid with "None" as destination<br>
 * Popup panels from Black Market, Messages & Gifts are now modal<br>
 * Removed port property and use gameid value instead internally in application<br>
 * Fixed bug on default.jsp concerning redirect to login.jsp if not logged in<br>
 * Fixed javascript bug on login.jsp with guest checkbox<br>
<P>
<strong>3.3.2</strong><br>
Date: 050318<br>
 * Created a new versions page (this page)<br>
 * NebA, NebB and Esc +1 support cost<br>
 * ISD and SSD +10% initiative<br>
 * New spaceship ability: initiative support (Example: MCC and SfS together get +30% ini)<br>
 * SfS is a initiative support unit<br>
 * Adjusted spaceships.jsp & manual for initiative support & SfS<br>
 * Cannot login to game from current_game.jsp to game already joined<br>
 * New algorithm for computing shooting side based on square root of size<br>
 * Moved new joined games from Current Games list to Games Starting Up list<br>
 * Minimum nr of steps between players homeplanets can be set when creating a new game<br>
 * URL params for auto login renamed from loginstr -> login, and pwdstr -> password<br>
 * Removed three duplicate long range planet connections on the wigge9 map<br>
 * Added autobalancing (on or off) of faction members in new games<br>
 * Fixed bug on current_game.jsp causing login-link to disappear<br>
 * Modified tool tip text on LoginPanelFull.java<br>
 * Moved SpaceRazeTunnel.java from default package<br>
 * Fixed bug in startpage.jsp concerning open games list<br>
 * Widened messagefield in GeneralMessagePanel.java<br>
 * Darkened all zeroes in the rankings page<br>
 * Added editing of properties-files for admins<br>
<br>
<P>
<strong>3.3.1</strong><br>
Date: 050311<br>
 *   Moved tunnel from ROOT to the SpaceRaze webapp<br>
 *   Fixed bug in tunnel<br>
 *   Fixed bug och rebuilt current_game.jsp without sockets are used<br>
 *   Rebuild tunnel without sockets<br>
 *   Changed how weapons/shields/damage capacity is programmed for spaceships<br>
 *   See number of players in a game in startpage.jsp<br>
 *   Broke players can not move spaceships and VIPs<br>
 *   Broke players get info in highlight och turn info panels<br>
 *   Applet code and config moved to own folder in webapp<br>
<P>
<strong>3.3.0</strong><br>
 *   Can have blanks in govenorname<br>
 *   Fixed bug in spaceship destination choice<br>
 *   New gui for gifts<br>
 *   New gui for messages<br>
 *   New gui for black market<br>
 *   Fixed spelling error in faq<br>
 *   Tunnels through servlet / client does not need open ports any more<br>
 *   NebA -10 shields<br>
 *   Removed port info from current_game.jsp, requirements.jsp<br>
 *   Fixed bug with retreats<br>
<P>
<strong>3.0 - 3.2.5</strong><br>
Older versions of SpaceRaze 3 are not covered in this document. 
<P>
    </td>
</tr>
</table>
</div>
</div>
	<div class="List_End"></div>		
	<br>
		<br>
			<br>
	</div>


</body>
</html>
