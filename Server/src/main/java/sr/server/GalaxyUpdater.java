//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger på Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package sr.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sr.enums.DiplomacyGameType;
import sr.enums.DiplomacyLevel;
import sr.enums.HighlightType;
import sr.enums.InitiativeMethod;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.message.MessageDatabase;
import sr.server.persistence.PHash;
import sr.server.ranking.RankingHandler;
import sr.webb.mail.MailHandler;
import sr.world.Building;
import sr.world.EconomyReport;
import sr.world.Faction;
import sr.world.Galaxy;
import sr.world.Message;
import sr.world.Planet;
import sr.world.PlanetInfos;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.SpaceshipType;
import sr.world.TaskForce;
import sr.world.Troop;
import sr.world.TroopType;
import sr.world.VIP;
import sr.world.diplomacy.DiplomacyState;
import sr.world.landbattle.LandBattle;
import sr.world.orders.Orders;
import sr.world.spacebattle.AttackReportSpace;
import sr.world.spacebattle.SpaceBattleReport;

public class GalaxyUpdater {
  protected Galaxy g;
  
  public GalaxyUpdater(Galaxy g){
    this.g = g;
  }

  public void performUpdate(SR_Server aSR_Server) throws Exception{
  	try{
  		Logger.info("Update started");
  		g.setLastUpdateComplete(false);
  		Logger.info("g.setLastUpdateComplete(false);");
  		if (g.gameEnded){
  			// spelet skall ej uppdateras
  			Logger.info("Game is over. Galaxy not updated.");
  		}else
  		// om det är det första draget, innan spelet har börjat
  		if (g.turn == 0){
  			// antal startplaneter
  			g.setStartPlanets(this);
  			Logger.info("First turn.");
  			g.setPlayerDiplomacy();
  			for (int x = 0; x < g.players.size(); x++){
  				Player temp = (Player)g.players.get(x);
  				temp.setOrders(new Orders());
  				addFirstTurnMessages(temp,aSR_Server.getMessageDatabase());
  				temp.createPlanetInfos();
				temp.resetDiplomacyOffers();
				// add start income to income report
				g.getPlayerIncomeWithoutCorruption(temp,true);
  			}
			updateMapPlanetInfos();
			// add statistics for first turn
			g.performStatistics();
  			g.turn++;
  	  		g.setLastUpdated(new Date());
  		}else{ // update galaxy
			Logger.info("Update galaxy");
			// update reports
			for (int x = 0; x < g.players.size(); x++){
				Player temp = (Player)g.players.get(x);
				temp.updateTurnInfo();
				temp.setUpdatedThisTurn(false);
				temp.setFinishedThisTurn(false);
				temp.addToGeneral("Game has been updated to turn " + (g.turn + 1) + ".");
				temp.addToGeneral("");
				temp.resetDiplomacyOffers();
			}
			// add last turn economy data to economy report
			updateEconomyReport1();
			// check if any players wish to abandon the game
			checkAbandonGame();
			if (g.playersLeft()){
				// get income
				updateTreasury();
				// pay upkeep for ships
				payUpkeepShips();
				// pay upkeep for troops
				payUpkeepTroops();
				// pay upkeep for VIPs
				payUpkeepVIPs();
				// check if broke players have negative treasury, in that case set treasury = 0;
				checkBroke();
				// Intitialize general reports
				initGeneralReports();
				// perform orders
				performOrders();
				//check if any troops on allied planets.
				removeTroopsOnAlliedPlanets();
				// perform BlackMarket
				performBlackMarket();
				// flytta flyende skepp
				moveRetreatingShips();
				// update squadrons locations
				updateSquadronsLocation();
				// check for and perform conflicts between VIPs
				checkVIPConflicts();
				// check if infestation vips have conquered any planets
				checkInfestationFromVIPs();
				// check if diplomats have conquered any neutral planets
				checkDiplomatsOnNeutrals();
				// clean besieged on planets. 
				peaceOnAllPlanets();
				
				// check for and perform battles and besieging
				checkSpaceBattles();
				
				checkGroundBattles();
				// check if there are any troops on razed planets
				// (this check is also performed on planets in the checkBattles algorithm)
				// checkAbandonedTroopsOnPlanets();
				// check if any planets are infestated by aliens from ships with troops
				checkInfestationFromShips();
				// destroy abandoned squadrons
				checkAbandonedSquadrons();
				// check destruction of civilian ships
				checkCivilianShips();
				// perform diplomacy orders
				performDiplomacyOrders();
				// repair damaged ships
				performShipRepairs();
				// repair damaged troops
				performTroopRepairs();
				// resupply ships with missiles
				performResupply();
				// write new upkeep cost to general reports
				writeUpkeepInfo();
				// raise negative res on non-besieged planets
				checkNonBesiegedPlanets();
				// kolla efter besegrade spelare
				defeatedPlayers();
				// uppdatera PlanetInfos
				updatePlanetInfos();
				updateMapPlanetInfos();
				// nollställ orders
				clearOrders();
				// reset diplomacy states
				g.getDiplomacy().resetDiplomacyStates();
				// check if govenor are on a retreating ship
				checkRetreatingGovenor();
				// add next turn economy data to economy report
				updateEconomyReport2();
				// check if any players lose the game due to being broke for more than 5 turns
				checkRepeatedBroke();
				// statistics
				g.performStatistics();
				Logger.info("Galaxy updated.");

				// check if game is over
				if (allPlanetsRazedAndUninfected()){
					Logger.info("... all planets RAZED!!!");
					for (int x = 0; x < g.players.size(); x++){
						Player temp = (Player)g.players.get(x);
						Logger.info("Player defeated due to razed planets");
						rankingLoss(temp.getName(),false);
						temp.addToGeneralAt("All planets in this sector Razed!",4);
						temp.addToGeneralAt("There is no winner.",5);
						temp.addToGeneralAt("Game have ended.",6);
						temp.addToGeneralAt("",7);
						temp.addToHighlights("All planets in this sector Razed!",HighlightType.TYPE_SPECIAL_1);
						temp.addToHighlights("There is no winner.",HighlightType.TYPE_SPECIAL_2);
						temp.addToHighlights("Game have ended",HighlightType.TYPE_SPECIAL_3);
					}
					g.gameEnded = true;
					PHash.incCounter("game.finished.total");
					PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
					PHash.incCounter("game.finished.map." + g.getMapFileName());
					PHash.incCounter("game.finished.allrazed");
				}else   // one player remains and is the winner of the game
				if (g.checkSoloPlayerWinner() != null){
					Logger.info("... and we have a (solo) WINNER!");
					Player winner = g.checkSoloPlayerWinner();
					for (int x = 0; x < g.players.size(); x++){
						Player temp = (Player)g.players.get(x);
						Logger.info("Solo win, loop player: " + temp.getName());
						if (temp != winner){
							Logger.info("Player defeated due to solo remaining player");
							rankingLoss(temp.getName(),false);
							temp.addToGeneralAt("Player " + winner.getName() + " (Governor " + winner.getGovenorName() + ") is the only player left and has won the game.",4);
							temp.addToGeneralAt("Game have ended.",5);
							temp.addToGeneralAt("",6);
							temp.addToHighlights(winner.getName() + " have won the game!",HighlightType.TYPE_SPECIAL_1);
							temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
						}else{
							temp.setWin(true);
							updateWinRanking(temp, true);
							temp.addToGeneralAt("You have won the game!!!!",4);
							temp.addToGeneralAt("Only you remain in the game and are the ruler of this sector (or what remains of it...).",5);
							temp.addToGeneralAt("Game have ended.",6);
							temp.addToGeneralAt("",7);
							temp.addToHighlights("You have won the game!",HighlightType.TYPE_SPECIAL_1);
							temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
						}
					}
					g.gameEnded = true;
					PHash.incCounter("game.finished.total");
					PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
					PHash.incCounter("game.finished.map." + g.getMapFileName());
					PHash.incCounter("game.finished.singlewin." + g.getGameWorld().getFileName() + "." + winner.getFaction().getName());
				}else // only one faction remains and wins the game
//				if (g.checkSoloFactionWinner() != null){
				if (g.checkSoloConfederacyWinner()){
					Logger.info("... and we have a winning confederacy!");
//					Faction faction = g.checkSoloFactionWinner();
					List<Player> confPlayers = g.getSoloConfederacyWinner();
					for (int x = 0; x < g.players.size(); x++){
						Player temp = (Player)g.players.get(x);
						Logger.info("Single confederacy win, loop player: " + temp.getName());
//						if (!faction.isFaction(temp.getFaction().getName())){
						if (temp.isDefeated()){
							Logger.info("Player defeated due to only remaining confederacy");
							rankingLoss(temp.getName(),false);
							temp.addToGeneralAt("You have lost the game!",4);
							temp.addToGeneralAt("Only one victorious confederacy remains, and they have defeated all other players and won the game.",5);
							temp.addToGeneralAt("Game have ended.",6);
							temp.addToGeneralAt("",7);
							temp.addToHighlights("Last remaining confederacy have won the game!",HighlightType.TYPE_SPECIAL_1);
							temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
						}else{
							temp.setWin(true);
							updateWinRankingConfederacy(temp, confPlayers);
							temp.addToGeneralAt("Your confederacy have won the game!!!!",4);
							temp.addToGeneralAt("Only your confederacy remain in the game and are the ruler of this sector (or what remains of it...).",5);
							temp.addToGeneralAt("Game have ended.",6);
							temp.addToGeneralAt("",7);
							temp.addToHighlights("Your confederacy have won the game!",HighlightType.TYPE_SPECIAL_1);
							temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
						}
					}
					g.gameEnded = true;
					PHash.incCounter("game.finished.total");
					PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
					PHash.incCounter("game.finished.map." + g.getMapFileName());
					PHash.incCounter("game.finished.confederacywin." + g.getGameWorld().getFileName());
				}else // one player has at least XX% of all pop in the sector and wins the game
				if (g.checkWinningPlayer() != null){
					Logger.info("... and we have a (XX% prod) WINNER!");
					Player winner = g.checkWinningPlayer();
					for (int x = 0; x < g.players.size(); x++){
						Player temp = (Player)g.players.get(x);
						if (temp != winner){
							if ((temp.getFaction() == winner.getFaction()) & !temp.isDefeated()){
								// since player is on the same faction as the winner he gets a faction win without any 
								// bonus ranking points for defeated opponents
								Logger.info("Player on same faction as " + g.getSingleVictory()+"% win player");
								temp.setWin(true);
								rankingWin(0, temp.getName(), false);
								temp.addToGeneralAt(winner.getName() + " have won the game!",4);
								temp.addToGeneralAt(winner.getName() + " control more than 65% of the total population in this sector",5);
								temp.addToGeneralAt("Since you are on the same faction you are awarded a cooperative victory!",6);
								temp.addToGeneralAt("Game have ended.",7);
								temp.addToGeneralAt("",8);
								temp.addToHighlights("You are on the winning side!",HighlightType.TYPE_SPECIAL_1);
								temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
							}else{
								Logger.info("Player defeated due to " + g.getSingleVictory()+"% domination player");
								rankingLoss(temp.getName(),!temp.isDefeated());
								temp.setDefeated(true);
								temp.addToGeneralAt("You have lost the game!",4);
								temp.addToGeneralAt("Player " + winner.getName() + " (Governor " + winner.getGovenorName() + ") has control over more than " + g.getSingleVictory()+"% of all population in this sector and has won the game.",5);
								temp.addToGeneralAt("Game have ended.",6);
								temp.addToGeneralAt("",7);
								temp.addToHighlights(winner.getName() + " have won the game!",HighlightType.TYPE_SPECIAL_1);
								temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
							}
						}else{
							temp.setWin(true);
							updateWinRanking(temp, true);
							temp.addToGeneralAt("You have won the game!!!!",4);
							temp.addToGeneralAt("You control more than " + g.getSingleVictory()+"% of the total population in this sector and are proclaimed ruler of this sector (or what remains of it...).",5);
							temp.addToGeneralAt("Game have ended.",6);
							temp.addToGeneralAt("",7);
							temp.addToHighlights("You have won the game!",HighlightType.TYPE_SPECIAL_1);
							temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
						}
					}
					g.gameEnded = true;
					PHash.incCounter("game.finished.total");
					PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
					PHash.incCounter("game.finished.map." + g.getMapFileName());
					PHash.incCounter("game.finished.singlewin." + g.getGameWorld().getFileName() + "." + winner.getFaction().getName());
				}else // one faction controls at least XX% of all pop in sector and wins the game
				if ((g.getDiplomacyGameType() == DiplomacyGameType.FACTION) && (g.checkWinningFaction() != null)){
					Logger.info("... and we have a (faction) WINNER!");
					Faction winner = g.checkWinningFaction();
					for (int x = 0; x < g.players.size(); x++){
						Player temp = (Player)g.players.get(x);
						if (temp.getFaction() != winner){
							Logger.info("Player defeated due to " + g.getFactionVictory()+"% domination faction");
							rankingLoss(temp.getName(),!temp.isDefeated());
							temp.setDefeated(true);
							temp.addToGeneralAt("You have lost the game!",4);
							temp.addToGeneralAt("The " + winner.getName() + " faction has control over more than " + g.getFactionVictory()+"% of the total population in this sector and the",5);
							temp.addToGeneralAt("members of the " + winner.getName() + " faction has won the game together.",6);
							temp.addToGeneralAt("Game have ended.",7);
							temp.addToGeneralAt("",8);
						}else{
							temp.setWin(true);
							updateWinRanking(temp, false);
							temp.addToGeneralAt("Shared Victory!",4);
							temp.addToGeneralAt("The " + winner.getName() + " faction has control over more than " + g.getFactionVictory()+"% of the total population in this sector and you",5);
							temp.addToGeneralAt("and the other members of the " + winner.getName() + " faction has won the game together.",6);
							temp.addToGeneralAt("Game have ended.",7);
							temp.addToGeneralAt("",8);
						}
						temp.addToHighlights("The " + winner.getName() + " faction have won the game!",HighlightType.TYPE_SPECIAL_1);
						temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
					}
					g.gameEnded = true;
					PHash.incCounter("game.finished.total");
					PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
					PHash.incCounter("game.finished.map." + g.getMapFileName());
					PHash.incCounter("game.finished.factionwin." + g.getGameWorld().getFileName() + "." + winner.getName());
				}else // one confederacy have at least XX% production
				if (((g.getDiplomacyGameType() == DiplomacyGameType.OPEN) | (g.getDiplomacyGameType() == DiplomacyGameType.GAMEWORLD)) && (g.checkWinningConfederacy() != null)){
					Logger.info("... and we have a (confederacy) WINNER!");
					List<Player> winnerConf = g.checkWinningConfederacy();
					for (Player aPlayer : g.players){
						if (!winnerConf.contains(aPlayer) | aPlayer.isDefeated()){
							Logger.info("Player defeated due to " + g.getFactionVictory() + "% domination confederacy");
							rankingLoss(aPlayer.getName(),!aPlayer.isDefeated());
							aPlayer.setDefeated(true);
							aPlayer.addToGeneralAt("You have lost the game!",4);
							aPlayer.addToGeneralAt("A confederacy has control over more than " + g.getFactionVictory()+"% of the total production in this sector and the",5);
							aPlayer.addToGeneralAt("members of the confederacy has won the game together.",6);
							aPlayer.addToGeneralAt("Game have ended.",7);
							aPlayer.addToGeneralAt("",8);
						}else{
							aPlayer.setWin(true);
							updateWinRankingConfederacy(aPlayer, winnerConf);
							aPlayer.addToGeneralAt("Shared Victory!",4);
							aPlayer.addToGeneralAt("Your confederacy have control over more than " + g.getFactionVictory()+"% of the total production in this sector and you",5);
							aPlayer.addToGeneralAt("and the other members of the confederacy have won the game together.",6);
							aPlayer.addToGeneralAt("Game have ended.",7);
							aPlayer.addToGeneralAt("",8);
						}
						aPlayer.addToHighlights("A confederacy of players have won the game!",HighlightType.TYPE_SPECIAL_1);
						aPlayer.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
					}
					g.gameEnded = true;
					PHash.incCounter("game.finished.total");
					PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
					PHash.incCounter("game.finished.map." + g.getMapFileName());
					PHash.incCounter("game.finished.confederacywin." + g.getGameWorld().getFileName());
				}else // one Lord controls at least XX% of all pop in sector and wins the game
				if (((g.getDiplomacyGameType() == DiplomacyGameType.OPEN) | (g.getDiplomacyGameType() == DiplomacyGameType.GAMEWORLD)) && (g.checkWinningLord() != null)){
					Logger.info("... and we have a (Lord) WINNER!");
					List<Player> winnerLord = g.checkWinningLord();
					for (Player aPlayer : g.players){
						if (!winnerLord.contains(aPlayer) | aPlayer.isDefeated()){
							Logger.info("Player defeated due to " + g.getFactionVictory() + "% domination Lord");
							rankingLoss(aPlayer.getName(),!aPlayer.isDefeated());
							aPlayer.setDefeated(true);
							aPlayer.addToGeneralAt("You have lost the game!",4);
							aPlayer.addToGeneralAt("A Lord has control over more than " + g.getFactionVictory()+"% of the total production in this sector and",5);
							aPlayer.addToGeneralAt("have won the game.",6);
							aPlayer.addToGeneralAt("Game have ended.",7);
							aPlayer.addToGeneralAt("",8);
						}else{
							if (winnerLord.get(0) == aPlayer){
								aPlayer.setWin(true);
								updateWinRankingLord(aPlayer);
								aPlayer.addToGeneralAt("You have won the game!",4);
								aPlayer.addToGeneralAt("Your and your vassals have control over more than " + g.getFactionVictory()+"% of the total production in this sector and you",5);
								aPlayer.addToGeneralAt("have won the game.",6);
								aPlayer.addToGeneralAt("Game have ended.",7);
								aPlayer.addToGeneralAt("",8);
							}else{ // vassal on the winning side
								rankingWinVassal(aPlayer.getName());
								aPlayer.addToGeneralAt("Your Lord have won the game!",4);
								aPlayer.addToGeneralAt("Your lord and his vassals have control over more than " + g.getFactionVictory()+"% of the total production in this sector and you",5);
								aPlayer.addToGeneralAt("are on the winning side.",6);
								aPlayer.addToGeneralAt("Game have ended.",7);
								aPlayer.addToGeneralAt("",8);
							}
						}
						aPlayer.addToHighlights("A confederacy of players have won the game!",HighlightType.TYPE_SPECIAL_1);
						aPlayer.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
					}
					g.gameEnded = true;
					PHash.incCounter("game.finished.total");
					PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
					PHash.incCounter("game.finished.map." + g.getMapFileName());
					PHash.incCounter("game.finished.confederacywin." + g.getGameWorld().getFileName());
				}else 
				if (g.getEndTurn() > 0 && g.getEndTurn() <= g.getTurn()){ //maximum turns.
					List<Faction> largestFactions = g.getLargestFactions();
					String winnerString = "";
					
					if(largestFactions.size()> 1){ // Shared Victory.
						for (Faction faction : largestFactions) {
							if(winnerString.equals("")){
								winnerString =faction.getName();
							}else{
								winnerString += ", " + faction.getName();
							}
						}
						
						for (int x = 0; x < g.players.size(); x++){
							Player temp = (Player)g.players.get(x);
							if (!largestFactions.contains(temp.getFaction())){
								Logger.info("Player defeated due to max");
								rankingLoss(temp.getName(),!temp.isDefeated());
								temp.setDefeated(true);
								temp.addToGeneralAt("You have lost the game!",4);
								temp.addToGeneralAt("The " + winnerString + " factions has control most of the total population in this sector and the",5);
								temp.addToGeneralAt("members of the " +winnerString + " factions has won the game together.",6);
								temp.addToGeneralAt("Game have ended.",7);
								temp.addToGeneralAt("",8);
							}else{
								temp.setWin(true);
								updateWinRanking(temp, false);
								temp.addToGeneralAt("Shared Victory!",4);
								temp.addToGeneralAt("The " + winnerString + " factions has control most of the total population in this sector and you",5);
								temp.addToGeneralAt("and the other members of the " + winnerString + " factions has won the game together.",6);
								temp.addToGeneralAt("Game have ended.",7);
								temp.addToGeneralAt("",8);
							}
							temp.addToHighlights("The " + winnerString + " factions have won the game!",HighlightType.TYPE_SPECIAL_1);
							temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
						}
						g.gameEnded = true;
						PHash.incCounter("game.finished.total");
						PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
						PHash.incCounter("game.finished.map." + g.getMapFileName());
						PHash.incCounter("game.finished.factionwin." + g.getGameWorld().getFileName() + "." + winnerString);
						
					}else{// one faction win.  checking if singel player win or fatcion win.
						if(g.getFactionMember(largestFactions.get(0)).size() > 1){// Shared Victory.
							for (int x = 0; x < g.players.size(); x++){
								Player temp = (Player)g.players.get(x);
								if (!largestFactions.contains(temp.getFaction())){
									Logger.info("Player defeated due to max turn");
									rankingLoss(temp.getName(),!temp.isDefeated());
									temp.setDefeated(true);
									temp.addToGeneralAt("You have lost the game!",4);
									temp.addToGeneralAt("The " + winnerString + " faction has control most of the total population in this sector and the",5);
									temp.addToGeneralAt("members of the " +largestFactions.get(0).getName() + " faction has won the game together.",6);
									temp.addToGeneralAt("Game have ended.",7);
									temp.addToGeneralAt("",8);
								}else{
									temp.setWin(true);
									updateWinRanking(temp, false);
									temp.addToGeneralAt("Shared Victory!",4);
									temp.addToGeneralAt("The " + largestFactions.get(0).getName() + " faction has control most of the total population in this sector and you",5);
									temp.addToGeneralAt("and the other members of the " + largestFactions.get(0).getName() + " faction has won the game together.",6);
									temp.addToGeneralAt("Game have ended.",7);
									temp.addToGeneralAt("",8);
								}
								temp.addToHighlights("The " + largestFactions.get(0).getName() + " faction have won the game!",HighlightType.TYPE_SPECIAL_1);
								temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
							}
							g.gameEnded = true;
							PHash.incCounter("game.finished.total");
							PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
							PHash.incCounter("game.finished.map." + g.getMapFileName());
							PHash.incCounter("game.finished.factionwin." + g.getGameWorld().getFileName() + "." + largestFactions.get(0).getName());
						}else{// one player win
							Player winner = g.getFactionMember(largestFactions.get(0)).get(0);
							for (int x = 0; x < g.players.size(); x++){
								Player temp = (Player)g.players.get(x);
								if (!largestFactions.contains(temp.getFaction())){
								
									Logger.info("Player defeated due to max turn");
									rankingLoss(temp.getName(),!temp.isDefeated());
									temp.setDefeated(true);
									temp.addToGeneralAt("You have lost the game!",4);
									temp.addToGeneralAt("The player " + winnerString + "(Governor " + winner.getGovenorName() + ") control most of the total population in this sector",5);
									temp.addToGeneralAt("and has won the game.",6);
									temp.addToGeneralAt("Game have ended.",7);
									temp.addToGeneralAt("",8);
									temp.addToHighlights(winner.getName() + " have won the game!",HighlightType.TYPE_SPECIAL_1);
									temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
								}else{
									temp.setWin(true);
									updateWinRanking(temp, true);
									temp.addToGeneralAt("You have won the game!!!!",4);
									temp.addToGeneralAt("You control most of the total population in this sector and are proclaimed ruler of this sector (or what remains of it...).",5);
									temp.addToGeneralAt("Game have ended.",6);
									temp.addToGeneralAt("",7);
									temp.addToHighlights("You have won the game!",HighlightType.TYPE_SPECIAL_1);
									temp.addToHighlights("Game have ended.",HighlightType.TYPE_SPECIAL_2);
								}
							}
						
							g.gameEnded = true;
							PHash.incCounter("game.finished.total");
							PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
							PHash.incCounter("game.finished.map." + g.getMapFileName());
							PHash.incCounter("game.finished.singlewin." + g.getGameWorld().getFileName() + "." + winner.getFaction().getName());
							
						
						}
					}
				}
			}else{ // no players left, game is over
				Logger.info("No players left, game is over");
				for (int x = 0; x < g.players.size(); x++){
					Player temp = (Player)g.players.get(x);
					Logger.info("No players left");
					rankingLoss(temp.getName(),false);
					temp.addToGeneralAt("No players left.",4);
					temp.addToGeneralAt("There is no winner.",5);
					temp.addToGeneralAt("Game have ended.",6);
					temp.addToGeneralAt("",7);
					temp.addToHighlights("No players left.",HighlightType.TYPE_SPECIAL_1);
					temp.addToHighlights("There is no winner.",HighlightType.TYPE_SPECIAL_2);
					temp.addToHighlights("Game have ended",HighlightType.TYPE_SPECIAL_3);
				}
				g.gameEnded = true;
				PHash.incCounter("game.finished.total");
				PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
				PHash.incCounter("game.finished.map." + g.getMapFileName());
				PHash.incCounter("game.finished.nowinner");
			}			
			g.turn++;
	  		g.setLastUpdated(new Date());
			PHash.incCounter("game.turn_performed.total");
			for (Player aPlayer : g.getPlayers()) {
				if (!aPlayer.isDefeated()){
					PHash.incCounter("game.turn_performed.player." + aPlayer.getName());
				}
			}
		}
  		Logger.info("Update complete, turn is: " + g.getTurn());
  		g.setLastUpdateComplete(true);
  		Logger.info("g.setLastUpdateComplete(true)");
  		// update succsessful, log not needed
  		g.clearLastLog();
  		Logger.info("update succsessful, last log cleared");
  	}
  	catch(Exception e){
  		Logger.severe("Exception caught: " + e.toString());
  		StackTraceElement[] stackArray = e.getStackTrace();
  		for (int i = 0; i < stackArray.length; i++) {
			Logger.severe(stackArray[i].toString());
		}
  		MailHandler.sendErrorToAdmins("Update - " + g.getGameName(),g.getLastLog());
  		throw e;
  	}
  }
  
  protected void removeTroopsOnAlliedPlanets() {
	if(g.getGameWorld().isTroopGameWorld()){
		List<Player> players = g.getPlayers();
		for (Player player : players) {
			List<Troop> troops = g.getPlayersTroops(player);
			for (Troop troop : troops) {
				if(troop.getPlanetLocation() != null && troop.getPlanetLocation().isPlayerPlanet() && !troop.getPlanetLocation().getPlayerInControl().equals(player)){
					DiplomacyState diplomacyState = g.getDiplomacyState(player, troop.getPlanetLocation().getPlayerInControl());
					if(diplomacyState.getCurrentLevel().isHigher(DiplomacyLevel.WAR)){//friendly
						//remove the troop
						player.getTurnInfo().addToLatestTroopsLostInSpace(troop);
						player.getTurnInfo().addToLatestGeneralReport("The troop " + troop.getUniqueName() + "have be dismissed to avoid conlict with our ally on the planet "  + troop.getPlanetLocation().getName() + ".");
						g.removeTroop(troop);
					}
					
				}
			}
		}
	}
	
	
}

protected void checkGroundBattles() {
	  if(g.getGameWorld().isTroopGameWorld()){
		  Logger.fine("checkGroundBattles called");
	      // leta igenom alla planeter
		  List<Planet> planets = g.getPlanets();
		  for (Planet planet : planets) {
			  troopFight(planet);
		  }
		  Logger.finer("checkGroundBattles finished");
	  }
  }

protected void rankingLoss(String playerLogin, boolean survived){
	  if (g.getranked()){
		  RankingHandler.addPlayerLoss(playerLogin,survived);
	  }
  }
  
  protected void rankingWin(int nrDefeatedOpp, String playerLogin, boolean soloWin){
	  if (g.getranked()){
		  RankingHandler.addPlayerWin(nrDefeatedOpp,playerLogin,soloWin);
	  }
  }

  protected void rankingWinVassal(String playerLogin){
	  if (g.getranked()){
		  RankingHandler.addVassalWin(playerLogin);
	  }
  }
  
  /**
   * Check if there are any unguarded civilian ships in the same system as enemy 
   * military ships, and if so they are destroyed.
   *  
   */
  protected void checkCivilianShips(){
      Logger.finer("checkCivilianShips called");
	  // loop through all planets
	  for (Planet aPlanet : g.getPlanets()) {
	    Logger.finer("aPlanet: " + aPlanet.getName());
	  	// find all civilian ships at the current planet
		List<Spaceship> civsAtPlanet = g.getShips(aPlanet,true);
	  	// for each civilian ship
		for (Spaceship aSpaceship : civsAtPlanet) {			
		    Logger.finer("aSpaceship: " + aSpaceship.getName());
	  		// find if there are any friendly military ships in the system
			List<Spaceship> militaryAtPlanet = g.getShips(aPlanet,false);
			List<Spaceship> friendlyMilitarys = getFriendlyMilitaryShips(aSpaceship,militaryAtPlanet); 
			Logger.finest("militaryAtPlanet.size(): " + militaryAtPlanet.size());
			Logger.finest("friendlyMilitarys.size(): " + friendlyMilitarys.size());
	  		// if there are no friendly ships
			if (friendlyMilitarys.size() == 0){
	  			// get a list over all enemy military ships in the system
				List<Spaceship> enemyMilitarys = getEnemyMilitaryShips(aSpaceship,militaryAtPlanet); 
				Logger.finer("enemyMilitarys.size(): " + enemyMilitarys.size());
	  			// if there are at least one enemy military ship
				if (enemyMilitarys.size() > 0){
					boolean stopRetreats = getStopRetreats(enemyMilitarys);
					List<Player> enemyPlayers = getEnemyPlayers(enemyMilitarys);
					Logger.finer("aSpaceship.isAlwaysRetreat(): " + aSpaceship.isAlwaysRetreat());
					if (aSpaceship.isAlwaysRetreat() & !stopRetreats){
						boolean gotAway = aSpaceship.retreat();
						Logger.finer("gotAway: " + gotAway);
						if (gotAway){ // ship have retreated
							for (Player player : enemyPlayers) {
								if (aSpaceship.getOwner() != null){
									player.getTurnInfo().addToLatestCivilianReport("A civilian ship of the type " + aSpaceship.getSpaceshipType().getName() + " from govenor " + aSpaceship.getOwner().getName() + " have retreated in the " + aPlanet.getName() + " system.");
								}else{ // civ ship is neutral
									player.getTurnInfo().addToLatestCivilianReport("A neutral civilian ship of the type " + aSpaceship.getSpaceshipType().getName() + " have retreated in the " + aPlanet.getName() + " system.");
								}
								player.getTurnInfo().addToLatestHighlights(aPlanet.getName(),HighlightType.TYPE_ENEMY_CIVILIAN_SHIP_RETREATED);
							}
							if (aSpaceship.getOwner() != null){
								aSpaceship.getOwner().getTurnInfo().addToLatestCivilianReport("Your civilian ship " + aSpaceship.getName() + " has retreated from the planet " + aPlanet.getName() + ".");
								aSpaceship.getOwner().getTurnInfo().addToLatestHighlights(aPlanet.getName(),HighlightType.TYPE_OWN_CIVILIAN_SHIP_RETREATED);
							}
						}else{ // ship had nowhere to retreat to, is scuttled
							for (Player player : enemyPlayers) {
								if (aSpaceship.getOwner() != null){
									player.getTurnInfo().addToLatestCivilianReport("A civilian ship of the type " + aSpaceship.getSpaceshipType().getName() + " from govenor " + aSpaceship.getOwner().getName() + " in the " + aSpaceship.getLocation().getName() + " system have been scuttled by it's own crew, when it had nowhere to retreat to.");
								}else{ // civ ship is neutral
									player.getTurnInfo().addToLatestCivilianReport("A neutral civilian ship of the type " + aSpaceship.getSpaceshipType().getName() + " in the " + aSpaceship.getLocation().getName() + " system have been scuttled by it's own crew, when it had nowhere to retreat to.");
								}
								player.getTurnInfo().addToLatestShipsLostInSpace(aSpaceship);
								player.getTurnInfo().addToLatestHighlights(aSpaceship.getSpaceshipType().getName(),HighlightType.TYPE_ENEMY_CIVILIAN_SHIP_DESTROYED);
							}
							if (aSpaceship.getOwner() != null){
								aSpaceship.getOwner().getTurnInfo().addToLatestCivilianReport("Your civilian ship " + aSpaceship.getName() + " has been scuttled in the system " + aSpaceship.getLocation().getName() + " when it had nowhere to retreat to.");
								aSpaceship.getOwner().getTurnInfo().addToLatestShipsLostInSpace(aSpaceship);
								aSpaceship.getOwner().getTurnInfo().addToLatestHighlights(aSpaceship.getName(),HighlightType.TYPE_OWN_CIVILIAN_SHIP_DESTROYED);
								g.checkVIPsInDestroyedShips(aSpaceship, aSpaceship.getOwner());
								g.checkTroopsInDestroyedShips(aSpaceship, aSpaceship.getOwner());
							}
					        g.removeShip(aSpaceship);
						}
					}else{ // ship is destroyed
		  				// add a general message to owner of civilian ship that the ship has been destroyed
						if (aSpaceship.getOwner() != null){
							if (aSpaceship.isAlwaysRetreat() & stopRetreats){
								aSpaceship.getOwner().getTurnInfo().addToLatestCivilianReport("Your civilian ship " + aSpaceship.getName() + " has been destroyed in the system " + aSpaceship.getLocation().getName() + ". It tried to retreat but was stopped by an enemy ship with the stop retreats ability.");
							}else{
								aSpaceship.getOwner().getTurnInfo().addToLatestCivilianReport("Your civilian ship " + aSpaceship.getName() + " has been destroyed in the system " + aSpaceship.getLocation().getName() + ".");
							}
							aSpaceship.getOwner().getTurnInfo().addToLatestShipsLostInSpace(aSpaceship);
							aSpaceship.getOwner().getTurnInfo().addToLatestHighlights(aSpaceship.getName(),HighlightType.TYPE_OWN_CIVILIAN_SHIP_DESTROYED);
						}
		  				// add a general message to each enemy player about the destruction of the civilian ship
//						List<Player> enemyPlayers = getEnemyPlayers(enemyMilitarys);
						Logger.finest("enemyPlayers: " + enemyPlayers.size());
						for (Player player : enemyPlayers) {
							if (aSpaceship.getOwner() != null){
								Logger.finest("addToLatestCivilianReport");
								if (aSpaceship.isAlwaysRetreat() & stopRetreats){
									player.getTurnInfo().addToLatestCivilianReport("A civilian ship of the type " + aSpaceship.getSpaceshipType().getName() + " from govenor " + aSpaceship.getOwner().getName() + " couldn't retreat and has been destroyed in the " + aSpaceship.getLocation().getName() + " system.");
								}else{
									player.getTurnInfo().addToLatestCivilianReport("A civilian ship of the type " + aSpaceship.getSpaceshipType().getName() + " from govenor " + aSpaceship.getOwner().getName() + " has been destroyed in the " + aSpaceship.getLocation().getName() + " system.");
								}
							}else{ // civ ship is neutral
								player.getTurnInfo().addToLatestCivilianReport("A neutral civilian ship of the type " + aSpaceship.getSpaceshipType().getName() + " has been destroyed in the " + aSpaceship.getLocation().getName() + " system.");
							}
							player.getTurnInfo().addToLatestShipsLostInSpace(aSpaceship);
							player.getTurnInfo().addToLatestHighlights(aSpaceship.getSpaceshipType().getName(),HighlightType.TYPE_ENEMY_CIVILIAN_SHIP_DESTROYED);
						}
						// destroy the civilian ship
						g.checkVIPsInDestroyedShips(aSpaceship, aSpaceship.getOwner());
						g.checkTroopsInDestroyedShips(aSpaceship, aSpaceship.getOwner());
				        g.removeShip(aSpaceship);
					}
				}
			}
		}
	  }
  }
  
  protected boolean getStopRetreats(List<Spaceship> enemyMilitarys) {
	  boolean stopRetreats = false;
	  for (Spaceship spaceship : enemyMilitarys) {
		  if (spaceship.getNoRetreat()){
			  stopRetreats = true;
		  }
	  }
	  return stopRetreats;
  }

  protected List<Player> getEnemyPlayers(List<Spaceship> enemyMilitaryShips){
	  List<Player> enemyPlayers = new LinkedList<Player>();
	  for (Spaceship anEnemyShip : enemyMilitaryShips) {
		  Player tmpPlayer = anEnemyShip.getOwner();
		  if (tmpPlayer != null){
			  if (!enemyPlayers.contains(tmpPlayer)){
				  enemyPlayers.add(tmpPlayer);
			  }
		  }
	  }
	  return enemyPlayers;
  }
  
  protected List<Spaceship> getFriendlyMilitaryShips(Spaceship aSpaceship,List<Spaceship> militaryAtPlanet){
	  List<Spaceship> ships = new LinkedList<Spaceship>();
	  Player owner = aSpaceship.getOwner(); // civ owner
	  for (Spaceship tmpSpaceship : militaryAtPlanet) {
		  if (tmpSpaceship.getOwner() != null){
			  if (owner != null){
				  if (g.getDiplomacy().friendlyCivilians(tmpSpaceship.getOwner(),owner)){
					  ships.add(tmpSpaceship);
				  }
			  }
		  }else{
			  if (owner == null){
				  // neutral civilian & neutral military -> add!
				  ships.add(tmpSpaceship);
			  }
		  }
	  }
	  return ships;
  }

  protected List<Spaceship> getEnemyMilitaryShips(Spaceship aSpaceship,List<Spaceship> militaryAtPlanet){
	  List<Spaceship> ships = new LinkedList<Spaceship>();
	  Player owner = aSpaceship.getOwner();
	  for (Spaceship tmpSpaceship : militaryAtPlanet) {
		  // only military ships belonging to players can be considered enemy
		  if (tmpSpaceship.getOwner() != null){
			  if (owner == null){
				  // civilian ship is neutral, check i tmpSpaceship is hosrile 
				  //   towards this neutral planet
			      boolean hostile = tmpSpaceship.getOwner().getPlanetOrderStatuses().isAttackIfNeutral(aSpaceship.getLocation().getName());
			      if (hostile){
			    	  ships.add(tmpSpaceship);
			      }
			  }else
			  if (g.getDiplomacy().hostileCivilians(tmpSpaceship.getOwner(),owner)){
				  ships.add(tmpSpaceship);
			  }
		  }
	  }
	  return ships;
  }

  /**
   * Destroy all squadrons who are on a neutral or enemy planet without
   * at least one carrier from the same player at the same planet
   *
   */
  protected void checkAbandonedSquadrons(){
      Logger.fine("checkAbandonedSquadrons called");
	  for (Player aPlayer : g.getPlayers()) {
		  g.checkAbandonedSquadrons(aPlayer);
	  }
  }

  protected void initGeneralReports(){
    Logger.fine("initGeneralReports called");
	for (int x = 0; x < g.players.size(); x++){
		Player temp = (Player)g.players.get(x);
		temp.addToGeneral("General Reports");
		temp.addToGeneral("--------------------------------------------------------------------------------------------------------------------------------");
	}
  }

  protected void checkRetreatingGovenor(){
  	Logger.fine("called");
  	for (int x = 0; x < g.players.size(); x++){
  		Player aPlayer = (Player)g.players.get(x);
  		if (!aPlayer.isDefeated()){
  			VIP theGov = g.findVIPGovenor(aPlayer);
  			if ((theGov != null) && theGov.onShip()){ // must check if theGov is null, because that can happen in single player tutorial
  				Spaceship tempShip = theGov.getShipLocation(); 
  				if (tempShip.isRetreating()){
  					aPlayer.setRetreatingGovenor(true);
  					aPlayer.addToHighlights(tempShip.getName(),HighlightType.TYPE_GOVENOR_RETREATING);
  					aPlayer.addToGeneral("Your governor is currently on a retreating ship (" + tempShip.getName() + ").");
  					aPlayer.addToGeneral("As long as your governor is on a retreating ship he cannot give any order to ships or VIPs.");
  					aPlayer.addToGeneral("");
  				}else{
  					aPlayer.setRetreatingGovenor(false);
  				}
  			}else{
  				aPlayer.setRetreatingGovenor(false);
  			}
  		}else{
  			aPlayer.setRetreatingGovenor(false);
  		}
  	}
  }
		
  protected void clearOrders(){
  	Logger.fine("clear orders");
  	for (int x = 0; x < g.players.size(); x++){
  		Player temp = (Player)g.players.get(x);
  		
  	/*	LoggingHandler.fine( this, g, " add research order getOnGoingResearchedAdvantages().size()", new Integer(temp.getResearch().getOnGoingResearchedAdvantages().size()).toString());
  		if(temp.getOrders().getResearchOrders().size() > 0){
  		ResearchOrder researchOrdernew = (ResearchOrder)temp.getOrders().getResearchOrders().get(0);
  		LoggingHandler.fine( this, g, " add research order", researchOrdernew.getAdvantageName());	
  		}
  		
  		*/
  		temp.setOrders(new Orders(temp));
  	}
  }
 
  /**
   * Iterate through all (non-defeated) players and update their economy report for
   * the last turn.
   */
  protected void updateEconomyReport1(){
    Logger.fine("updateEconomyReport1 called");
    for (int x = 0; x < g.players.size(); x++){
        Player aPlayer = (Player)g.players.get(x);
        if (!aPlayer.isDefeated()){
        	EconomyReport er = aPlayer.getTurnInfo().getLatestEconomyReport();
        	// actual upkeep cost for ships last turn
        	int upkeep = g.getPlayerUpkeepShips(aPlayer);
            er.setSupportShipsLastTurn(upkeep);
            // actual upkeep cost for troops
        	int upkeepTroops = g.getPlayerUpkeepTroops(aPlayer);
            er.setSupportTroopsLastTurn(upkeepTroops);
            // actual upkeep cost for VIPs
        	int upkeepVIPs = g.getPlayerUpkeepVIPs(aPlayer);
            er.setSupportVIPsLastTurn(upkeepVIPs);
            // upkeep lost to corruption last turn
            int upkeepTmp = g.getPlayerFreeUpkeepWithoutCorruption(aPlayer);
            int upkeepLostToCorr = aPlayer.getLostToCorruption(upkeepTmp);
            er.setCorruptionUpkeepShipsLastTurn(aPlayer.getLostToCorruption(upkeepLostToCorr));
            // expenses last turn
            er.setExpensesLastTurn(aPlayer.getOrders().getExpensesCost(g));
            // actual income last turn
            int income = g.getPlayerIncomeWithoutCorruption(aPlayer,false);
            er.setIncomeLastTurn(income);
            // income lost to corruption last turn
            int incomeTmp = g.getPlayerIncomeWithoutCorruption(aPlayer,false);
            int incomeLostToCorr = aPlayer.getLostToCorruption(incomeTmp);
            er.setCorruptionIncomeLastTurn(incomeLostToCorr);
            // saved to next turn
            er.setSavedLastTurn(aPlayer.getTreasury());
        }
    }
  }

  /**
   * Iterate through all (non-defeated) players and update their economy report for
   * the next turn.
   */
  protected void updateEconomyReport2(){
    Logger.fine("updateEconomyReport2 called");
    for (int x = 0; x < g.players.size(); x++){
        Player aPlayer = (Player)g.players.get(x);
        if (!aPlayer.isDefeated()){
        	EconomyReport er = aPlayer.getTurnInfo().getLatestEconomyReport();
        	// actual upkeep cost for ships next turn
        	int upkeep = g.getPlayerUpkeepShips(aPlayer);
            er.setSupportShipsNextTurn(upkeep);
        	// actual troops upkeep cost next turn
        	int upkeepTroops = g.getPlayerUpkeepTroops(aPlayer);
            er.setSupportTroopsNextTurn(upkeepTroops);
//        	int upkeepVIPs = g.getPlayerUpkeepVIPs(aPlayer);
            er.setSupportVIPsNextTurn(upkeepTroops);
            // upkeep lost to corruption next turn
            int upkeepTmp = g.getPlayerFreeUpkeepWithoutCorruption(aPlayer);
            int upkeepLostToCorr = aPlayer.getLostToCorruption(upkeepTmp);
            er.setCorruptionUpkeepShipsNextTurn(aPlayer.getLostToCorruption(upkeepLostToCorr));
            // actual income next turn
            int income = g.getPlayerIncomeWithoutCorruption(aPlayer,true);
            er.setIncomeNextTurn(income);
            // income lost to corruption next turn
//            int incomeTmp = g.getPlayerIncomeWithoutCorruption(aPlayer,false);
            int incomeLostToCorr = aPlayer.getLostToCorruption(income);
            er.setCorruptionIncomeNextTurn(incomeLostToCorr);
            // saved to next turn
            er.setSavedNextTurn(aPlayer.getTreasury());
        }
    }
  }

  protected void updateWinRanking(Player p, boolean soloWin){
	  Logger.info("updateWinRanking called: " + p.getName() + " " + p.getFaction().getName() + " " + soloWin);
	  int nrDefeatedOpp = 0;
	  int nrFaction = g.getFactionMemberNr(p.getFaction());
	  int nrHostile = g.players.size() - nrFaction;
	  Logger.finer(nrFaction + " " + nrHostile);
	  if (soloWin){
		  nrDefeatedOpp = nrHostile;
		  Logger.finer(nrFaction + "SoloWin: " + nrDefeatedOpp);
	  }else{
		  int nrFactionUndefeated = g.getUndefeatedFactionMemberNr(p.getFaction());
		  double average = (double)nrHostile / (double)nrFactionUndefeated;
		  nrDefeatedOpp = (int)Math.round(Math.ceil(average));
		  Logger.finer(nrFaction + "Not solo win: " + nrDefeatedOpp + " " + average);
	  }
	  rankingWin(nrDefeatedOpp, p.getName(), soloWin);
  }

  protected void updateWinRankingConfederacy(Player p,List<Player> winnerConf){
	  Logger.info("updateWinRankingConfederacy called: " + p.getName() + " " + winnerConf.size());
	  int nrDefeatedOpp = 0;
	  int nrConf = winnerConf.size();
	  int nrHostile = g.players.size() - nrConf;
	  Logger.finer(nrConf + " " + nrHostile);
	  double average = (double)nrHostile / (double)nrConf;
	  nrDefeatedOpp = (int)Math.round(Math.ceil(average));
	  Logger.finer(nrConf + "Not solo win: " + nrDefeatedOpp + " " + average);
	  rankingWin(nrDefeatedOpp, p.getName(), false);
  }

  protected void updateWinRankingLord(Player p){
	  Logger.info("updateWinRankingLord called: " + p.getName());
	  int nrDefeatedOpp = g.players.size() - 1;
	  Logger.finer("LordWin: " + nrDefeatedOpp);
	  rankingWin(nrDefeatedOpp, p.getName(), false);
  }
  
  /**
   * Used for single player games
   * @param aPlayer
   */
  protected void addFirstTurnMessages(Player aPlayer){
	  	aPlayer.updateTurnInfo();
		aPlayer.addToHighlights("Game has started.",HighlightType.TYPE_SPECIAL_1);
	  	aPlayer.addToGeneral("Game has started.");
	 	aPlayer.addToGeneral("");  
  }

//  protected void addFirstTurnMessages(Player aPlayer, SR_Server aSR_Server){
  protected void addFirstTurnMessages(Player aPlayer, MessageDatabase aMessageDatabase){
  	aPlayer.updateTurnInfo();
  	List<VIP> vips = g.findPlayersVIPsOnPlanetOrShipsOrTroops(aPlayer.getHomeplanet(),aPlayer);
  	int nrFaction = g.getFactionMemberNr(aPlayer.getFaction());
  	aPlayer.addToGeneral("Game has started.");
	aPlayer.addToHighlights("Game has started.",HighlightType.TYPE_SPECIAL_1);
//  	aPlayer.addToGeneral("Welcome, Governor " + aPlayer.getGovenorName() + ".");
 	aPlayer.addToGeneral("");
  	String planetsStr = "";
 	List<Planet> playerPlanets = g.getPlayersPlanets(aPlayer);
 	if (playerPlanets.size() == 1){
 		planetsStr += "You have one planet under your control - the planet " + aPlayer.getHomeplanet().getName() + ".\n";
// 		aPlayer.addToGeneral("You have one planet under your control - the planet " + aPlayer.getHomeplanet().getName() + ".");
 	}else{
 		planetsStr += "Your homeplanet and main base is the planet " + aPlayer.getHomeplanet().getName() + ".\n";
 	  	for (Planet aPlanet : playerPlanets) {
			if (aPlanet != aPlayer.getHomeplanet()){
				planetsStr += "You also control the planet " + aPlanet.getName() + ".\n";
			}
		}

 	}
  	String unitsStr = "";
  	Map<String, Integer> map = new HashMap<String, Integer>();
  	for (VIP aVIP : vips) {
		if (!aVIP.isGovernor()){
			int sum = 1;
			if(map.containsKey(aVIP.getName())){
				sum += map.get(aVIP.getName());
			}
			map.put(aVIP.getName(), sum);
			
		}
	}
  	if(map.size() > 0){
  		unitsStr += "\nVIPs under your command.\n";
  	}
  	for (VIP aVIP : vips) {
		if(map.containsKey(aVIP.getName())){
			if(map.get(aVIP.getName()) > 1){
				unitsStr += map.get(aVIP.getName()) + " " + aVIP.getName() + ".\n";
			}else{
				unitsStr += aVIP.getName() + ".\n";
			}
			map.remove(aVIP.getName());
		}
	}
  	map.clear();
  	
  	
  	// add text about starting ships
  	List<Spaceship> playerShips = aPlayer.getGalaxy().getPlayersSpaceships(aPlayer);
 	
 	for (Spaceship ss : playerShips) {
		int sum = 1;
		if(map.containsKey(ss.getSpaceshipType().getName())){
			sum += map.get(ss.getSpaceshipType().getName());
		}
		map.put(ss.getSpaceshipType().getName(), sum);
	}
  	if(map.size() > 0){
  		unitsStr += "\nShips under your command.\n";
  	}
  	for (Spaceship ss : playerShips) {
		if(map.containsKey(ss.getSpaceshipType().getName())){
			if(map.get(ss.getSpaceshipType().getName()) > 1){
				unitsStr += map.get(ss.getSpaceshipType().getName()) + " " + ss.getSpaceshipType().getName() + ".\n";
			}else{
				unitsStr += ss.getSpaceshipType().getName() + ".\n";
			}
			map.remove(ss.getSpaceshipType().getName());
		}
	}
  	map.clear();
 	
 	// add text about starting troops
 	List<Troop> playerTroops = aPlayer.getGalaxy().getPlayersTroops(aPlayer);
 	
 	for (Troop aTroop : playerTroops) {
		int sum = 1;
		if(map.containsKey(aTroop.getTroopType().getUniqueName())){
			sum += map.get(aTroop.getTroopType().getUniqueName());
		}
		map.put(aTroop.getTroopType().getUniqueName(), sum);
	}
  	if(map.size() > 0){
  		unitsStr += "\nTroops under your command.\n";
  	}
  	for (Troop aTroop : playerTroops) {
		if(map.containsKey(aTroop.getTroopType().getUniqueName())){
			if(map.get(aTroop.getTroopType().getUniqueName()) > 1){
				unitsStr += map.get(aTroop.getTroopType().getUniqueName()) + " " + aTroop.getTroopType().getUniqueName() + ".\n";
			}else{
				unitsStr += aTroop.getTroopType().getUniqueName() + ".\n";
			}
			map.remove(aTroop.getTroopType().getUniqueName());
		}
	}
 	
 	// message from faction
 	Faction f = aPlayer.getFaction();
 	aPlayer.addToGeneral("Recieved messages");
 	aPlayer.addToGeneral("-----------------");

 	String messageText = "";
 	String tmpText = null;
 	aPlayer.addToGeneral("You have recieved a message from " + f.getName() + " Headquarters.");
 	aPlayer.addToGeneral("");

 	tmpText = "Greetings, Governor " + aPlayer.getGovenorName();
// 	aPlayer.addToGeneral(tmpText);
 	messageText += tmpText + "\n";

 	tmpText = "We welcome you to the " + f.getName() + " faction.";
// 	aPlayer.addToGeneral(tmpText);
 	messageText += tmpText + "\n";
 	messageText += "\n";
 	messageText += planetsStr + "\n";

 	messageText += unitsStr + "\n";
 	
 	if(aPlayer.getFaction().getAdvantages() != null && !aPlayer.getFaction().getAdvantages().equals("")){
 		messageText += "Faction advantages: \n";
 	 	messageText += aPlayer.getFaction().getAdvantages() + "\n\n";
 	}
 	
 	if(aPlayer.getFaction().getDisadvantages() != null && !aPlayer.getFaction().getDisadvantages().equals("")){
 		messageText += "Faction disadvantages: \n";
 	 	messageText += aPlayer.getFaction().getDisadvantages() + "\n\n";
 	}

	if(aPlayer.getFaction().getHowToPlay() != null && !aPlayer.getFaction().getHowToPlay().equals("")){
		messageText += "How to play your faction: \n";
	 	messageText += aPlayer.getFaction().getHowToPlay() + "\n\n";
	}

	if(g.getGameWorld().getHowToPlay() != null && !g.getGameWorld().getHowToPlay().equals("")){
		messageText += "The way to play this world: \n";
	 	messageText += g.getGameWorld().getHowToPlay() + "\n\n";
	}

 	
 	int nrHostile = g.players.size();
  	if (g.getDiplomacyGameType() == DiplomacyGameType.FACTION){
  		nrHostile -= nrFaction;
  	}else{ // in all other diplomacy types
  		nrHostile -= 1;
  	}
  	
  	if (g.getDiplomacyGameType() == DiplomacyGameType.FACTION){
  		tmpText = "Be careful, according to our information there are " + nrHostile + " hostile governors in this quadrant.";
  	}else
  	if (g.getDiplomacyGameType() == DiplomacyGameType.DEATHMATCH){
  		tmpText = "Be careful, according to our information there are " + nrHostile + " hostile governors in this quadrant.";
  	}else{ // OPEN & GAMEWORLD
  		tmpText = "Be careful, according to our information there are " + nrHostile + " potentially hostile governors in this quadrant.";
  	}

  	// 	aPlayer.addToGeneral(tmpText);
 	messageText += tmpText + "\n";

 	tmpText = addEnemyGovMessage(nrFaction,aPlayer);
 	if (tmpText != null){
 		messageText += tmpText + "\n";
 	}

 	messageText += "\n";
 	tmpText = "Good luck, Governor";
// 	aPlayer.addToGeneral(tmpText);
 	messageText += tmpText + "\n";
 	
 	Message newMessage = new Message(messageText, aPlayer, new Player(f.getName() + " Headquarters","password",g,"govname",f.getName(),new Planet(0,0,0,"name",0,0,true, true)));
 	
 	aMessageDatabase.addMessage(newMessage, aPlayer.getGalaxy(), 0);

  	// add to highlights
// 	if(g.getNrStartPlanets() == 0){
//	 	playerShips = aPlayer.getGalaxy().getPlayersSpaceships(aPlayer);
//	 	for (Iterator iter = playerShips.iterator(); iter.hasNext();) {
//			Spaceship ss = (Spaceship) iter.next();
//			aPlayer.addToHighlights("You have a " + ss.getSpaceshipType().getName() + " under your command",1);
//		}
// 	}

 	aPlayer.addToHighlights("You have recieved a message from " + f.getName() + " Command",HighlightType.TYPE_SPECIAL_LAST);
  }
  
  protected String addEnemyGovMessage(int nrFaction, Player aPlayer){
	  String retText = null;
	  if (g.getDiplomacyGameType() != DiplomacyGameType.DEATHMATCH){
		  if (nrFaction == 2){
			  retText = "There are also one other " + aPlayer.getFaction().getName() + " governor in this quadrant of the same faction as you.";
		  }else{
			  if (nrFaction > 2){
				  retText = "There are also " + (nrFaction - 1) + " other " + aPlayer.getFaction().getName() + " governors in this quadrant of the same faction as you.";
			  }else{
				  retText = "";
			  }
		  }
	  }
	  return retText;
  }

  protected void performBlackMarket(){
  	Logger.fine("performBlackMarket called");
    // perform all bids on current offers
    g.performBlackMarket();
    // add new offers
    g.blackMarketNewTurn();
  }

  protected void checkVIPConflicts(){
  	Logger.fine("checkVIPConflicts called");
    // check conflicts between duellists
    g.checkDuels();
    // check if spies catch any visiting VIPs that isn't immune
    g.checkCounterEspionage();
    // check if any Assassins kill other VIPs that isn't well guarded
    g.checkAssassins();
    // check if any exterminators catch any infestators
    g.checkExtermination();
  }

  protected void checkDiplomatsOnNeutrals(){
  	Logger.fine("checkDiplomatsOnNeutrals called");
  	List<VIP> allDips = g.getAllDiplomatsOnNeutralPlanets();
    for (int i = 0; i < allDips.size(); i++){
    	VIP tempDip = (VIP)allDips.get(i);
        Planet tempLocation = tempDip.getPlanetLocation();
        List<VIP> hostileDips = getAllHostileDiplomatOnNeutral(tempDip,tempLocation,allDips);
        List<VIP> friendlyDips = getAllFriendlyDiplomatOnNeutral(tempDip,tempLocation,allDips);
        Player aPlayer = tempDip.getBoss();
        int total = hostileDips.size() + friendlyDips.size();
        if ((hostileDips.size() > 0) | (friendlyDips.size() > 0)){
    		aPlayer.addToGeneral("Your " + tempDip.getName() + " tries to convince the neutral planet " + tempLocation.getName() + " to join you.");
            for (VIP aFriendlyDip : friendlyDips) {
				aPlayer.addToGeneral("A " + aFriendlyDip.getName() + " from your own faction is also present at the neutral planet " + tempLocation.getName());
			}
            for (VIP aHostileDip : hostileDips) {
				aPlayer.addToGeneral("A " + aHostileDip.getName() + " from the " + aHostileDip.getBoss().getFaction().getName() + " faction is also present at the neutral planet " + tempLocation.getName());
			}
            String pluralS = "";
            if (total > 1){
            	pluralS = "s";
            }
        	if (tempDip.getGovCounter() >= tempLocation.getResistance()){
        		// join has been blocked
                aPlayer.addToHighlights(pluralS,HighlightType.TYPE_GOVENOR_NEUTRAL_JOIN_BLOCKED);
                if (total > 1){
                	aPlayer.addToGeneral("The presense of other Diplomats has blocked your effort to convince the planet to join you. If the other Diplomats leave the planet the planet will join you immediately.");
                }else{
                	aPlayer.addToGeneral("The presense of another Diplomat has blocked your effort to convince the planet to join you. If the other Diplomat leave the planet the planet will join you immediately.");
        		}
        	}else{
        		if (hostileDips.size() > 0){
        			// persuation has been blocked
        			aPlayer.addToHighlights(pluralS,HighlightType.TYPE_GOVENOR_NEUTRAL_PERSUATION_BLOCKED);
                    if (total > 1){
                    	aPlayer.addToGeneral("The presense of hostile Diplomats has blocked your effort to persuade the planet to join you. As long as the hostile Diplomats remain on the planet you will not get any closer to persuading it.");
                    }else{
                    	aPlayer.addToGeneral("The presense of a hostile Diplomat has blocked your effort to persuade the planet to join you. As long as the hostile Diplomat remain on the planet you will not get any closer to persuading it.");
            		}
        		}else{
        			// can persuade, but will not join unless...
                    tempDip.incGovCounter();
                    if (tempDip.getGovCounter() == tempLocation.getResistance()){  // one turn left to join
                    	aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will soon join you, if you are the lone Diplomat left at this planet.");
                    }else{
                    	aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will need at least one more turn of negotiations, but will not join you unless you are the lone Diplomat left on this planet.");
                    }
        		}
        	}
        }else{ // dip can persuades to join (no dips from other players)
        	if (tempDip.getGovLastTurn() < g.getTurn()){
        		Logger.fine("Persuation for planet " + tempLocation.getName());
        		aPlayer.addToGeneral("Your " + tempDip.getName() + " tries to convince the neutral planet " + tempLocation.getName() + " to join you.");
	            tempDip.incGovCounter();
	            tempDip.setLastTurn(g.getTurn());
	            List<VIP> ownDips = getAllOwnDiplomatOnNeutral(tempDip,tempLocation,allDips);
	            for (VIP anotherDip : ownDips) {
	        		aPlayer.addToGeneral("Your " + anotherDip.getName() + " tries to convince the neutral planet " + tempLocation.getName() + " to join you.");
	                tempDip.incGovCounter();
		            anotherDip.setLastTurn(g.getTurn());
	                anotherDip.incGovCounter(ownDips.size()+1);
				}
	            if (tempDip.getGovCounter() >= tempLocation.getResistance()){  
	            	// the planet joins
	            	tempLocation.joinsVisitingDiplomat(tempDip, true);
	            	shipsJoinGovenor(tempLocation,tempDip);
	            	troopsJoinGovenor(tempLocation, tempDip);
	            	g.checkVIPsOnConqueredPlanet(tempLocation,aPlayer);
	//            	tempDip.clearGovCounter(); not needed, done later for all vips
	            }else{
	            	// not join yet
	                if (tempDip.getGovCounter() == (tempLocation.getResistance() - 1)){  
	                	// one turn left to join
	                	aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will soon join you.");
	                }else{
	                	aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will need at least one more turn of negotiations.");
	                }
	            }
        	}else{
        		Logger.fine("Persuation for planet " + tempLocation.getName() + " is already handled");
        	}
        }
		aPlayer.addToGeneral("");
    }
    clearDiplomatCounters();
  }

  protected void clearDiplomatCounters(){
	    // clear counters from all diplomats not on neutral planets
	    List <VIP> allDips = g.getAllDiplomatsNotOnNeutralPlanets();
	    for (VIP aDip : allDips) {
			aDip.clearGovCounter();
			aDip.clearLastTurn();
		}
  }

  protected void clearInfestatorCounters(){
	    // clear counters from all infestators not in action
	    List <VIP> allInfs = g.getAllInfestatorsNotOnActionPlanets();
	    for (VIP vip : allInfs) {
	    	vip.clearGovCounter();
	    	vip.clearLastTurn();
		}
  }

  protected void checkInfestationFromVIPs(){
	  Logger.fine("checkInfestationFromVIPs called");
	  List<VIP> allInfs = g.getAllInfestatorsOnPlanets();
	  for (VIP tempInf : allInfs) {
		  Logger.finer("tempInf: " + tempInf.getName());
		  Planet tempLocation = tempInf.getPlanetLocation();
		  Player aPlayer = tempInf.getBoss();
		  if (g.getDiplomacy().hostileInfestator(aPlayer,tempLocation)){
			  List<VIP> otherInfs = getAllOtherInfestators(tempInf,tempLocation,allInfs);
			  if (otherInfs.size() > 0){
				  aPlayer.addToGeneral("Your " + tempInf.getName() + " tries to infect the planet " + tempLocation.getName() + " to join you.");
				  for (VIP anotherInf : otherInfs) {
					  aPlayer.addToGeneral(Functions.getDeterminedForm(anotherInf.getName(),true) + " " + anotherInf.getTypeName() + " from the " + anotherInf.getBoss().getFaction().getName() + " faction is also present at the planet " + tempLocation.getName());
				  } 
				  String pluralS = "";
				  if (otherInfs.size() > 1){
					  pluralS = "s";
				  }
				  if (otherInfs.size() > 0){
					  // infestation has been blocked
					  aPlayer.addToHighlights(pluralS,HighlightType.TYPE_INFESTATION_BLOCKED);
					  if (otherInfs.size() > 1){
						  aPlayer.addToGeneral("The presense of other infestators has blocked your effort to infect the planet to join you. As long as the other infestators remain on the planet you will not get any closer to infecting it.");
					  }else{
						  aPlayer.addToGeneral("The presense of another infestator has blocked your effort to infect the planet to join you. As long as the other infestator remain on the planet you will not get any closer to infecting it.");
					  }
				  }
				  if (tempLocation.getPlayerInControl() != null){
					  tempLocation.getPlayerInControl().addToHighlights(tempLocation.getName(),HighlightType.TYPE_OWN_PLANET_INFESTATION_IN_PROGRESS);
					  tempLocation.getPlayerInControl().addToGeneral("Your planet " + tempLocation.getName() + " are being infected by aliens.");
				  }
			  }else{ // no infs from other players present, persuades to join
				  if (tempInf.getGovLastTurn() < g.getTurn()){
					  Logger.finer("inf is alone, persuades to join");
					  Logger.fine("Infestation for planet " + tempLocation.getName());
		  			  aPlayer.addToGeneral("Your " + tempInf.getName() + " tries to infect the planet " + tempLocation.getName() + " to join you.");
		  			  tempInf.incGovCounter();
		  			  tempInf.setLastTurn(g.getTurn());
		  			  List<VIP> ownInfs = getAllOwnInfestators(tempInf,tempLocation,allInfs);
		  			  for (VIP anotherInf : ownInfs) {
			  			  aPlayer.addToGeneral("Your " + anotherInf.getName() + " tries to infect the planet " + tempLocation.getName() + " to join you.");
		  				  tempInf.incGovCounter();
		  				  anotherInf.setLastTurn(g.getTurn());
		  				  anotherInf.incGovCounter(ownInfs.size()+1);
		  			  }
					  Logger.finer("tempInf.getGovCounter(): " + tempInf.getGovCounter());
					  Logger.finer("tempLocation.getPopulation(): " + tempLocation.getPopulation());
					  if (tempInf.getGovCounter() >= tempLocation.getPopulation()){  
						  // the planet joins
						  if (tempLocation.getPlayerInControl() == null){
							  removeNeutralShips(tempLocation,tempInf);
						  }
						  g.checkTroopsOnInfestedPlanet(tempLocation, aPlayer);
						  tempLocation.joinsVisitingInfestator(tempInf);
						  // check for diplomats/other vips killed on infestated planets
						  g.checkVIPsOnConqueredPlanet(tempLocation,aPlayer);
					  }else{
						  // not join yet
						  if (tempInf.getGovCounter() == (tempLocation.getPopulation() - 1)){  
							  // one turn left to join
							  aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will soon join you.");
						  }else{
							  aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will need at least one more turn of infection.");
						  }
						  if (tempLocation.getPlayerInControl() != null){
							  tempLocation.getPlayerInControl().addToHighlights(tempLocation.getName(),HighlightType.TYPE_OWN_PLANET_INFESTATION_IN_PROGRESS);
							  tempLocation.getPlayerInControl().addToGeneral("Your planet " + tempLocation.getName() + " is being infected with by aliens.");
						  }
					  }
				  }else{
		        		Logger.fine("Infestation for planet " + tempLocation.getName() + " is already handled");
				  }
			  }
		  }
		  aPlayer.addToGeneral("");
	  }
	  clearInfestatorCounters();  
  }

  /**
   * Check if there are any hostile diplomats (other faction than aGov) on the neutral planet aPlanet
   * @param aGov the gov on planet aPlanet
   * @param aPlanet a neutral planet where aGov are
   * @return true if there is at least one hostile gov on aPlanet
   */
  protected List<VIP> getAllHostileDiplomatOnNeutral(VIP aDip, Planet aPlanet, List<VIP> allDips){
  	Logger.finer("called: " + aDip.getBoss().getGovenorName() + " " + aPlanet.getName());
  	List<VIP> found = new LinkedList<VIP>();
    for (int i = 0; i < allDips.size(); i++){
      VIP tempVIP = (VIP)allDips.get(i);
      if (tempVIP != aDip){ // kolla om tempVIP inte är aGov
        Planet tempLocation = tempVIP.getPlanetLocation();
        if (tempLocation == aPlanet){ // Dip är vid aPlanet
        	Faction f1 = aDip.getBoss().getFaction();
        	Faction f2 = tempVIP.getBoss().getFaction();
        	if (f1 != f2){
        		found.add(tempVIP);
        	}
        }
      }
    }
  	return found;
  }

  /**
   * Check if there are any other infestators (other players than aGov) on the planet aPlanet
   * @param anInf the inf on planet aPlanet
   * @param aPlanet the planet where anInf are
   * @return true if there is at least one other inf on aPlanet
   */
  protected List<VIP> getAllOtherInfestators(VIP anInf, Planet aPlanet, List<VIP> allInfs){
  	Logger.fine("called: " + anInf.getBoss().getGovenorName() + " " + aPlanet.getName());
  	List<VIP> found = new LinkedList<VIP>();
  	for (VIP tempVIP : allInfs) {
  		if (tempVIP != anInf){ 
  			Planet tempLocation = tempVIP.getPlanetLocation();
  			if (tempLocation == aPlanet){ // tempVIP är vid aPlanet
  				if (anInf.getBoss() != (tempVIP.getBoss())){
  					found.add(tempVIP);
  				}
  			}
  		}
    }
  	return found;
  }

  /**
   * Find all other own infestators
   * @param anInf
   * @param aPlanet
   * @param allInfs
   * @return
   */
  protected List<VIP> getAllOwnInfestators(VIP anInf, Planet aPlanet, List<VIP> allInfs){
	  Logger.fine("called: " + anInf.getBoss().getGovenorName() + " " + aPlanet.getName());
	  List<VIP> found = new LinkedList<VIP>();
	  for (VIP tempVIP : allInfs) {
		  if (tempVIP != anInf){ 
			  Planet tempLocation = tempVIP.getPlanetLocation();
			  if (tempLocation == aPlanet){ // tempVIP är vid aPlanet
				  if (anInf.getBoss() == (tempVIP.getBoss())){
					  found.add(tempVIP);
				  }
			  }
		  }
	  }
	  return found;
  }

  /**
   * Check if there are any friendly diplomats (same faction than aGov) on the neutral planet aPlanet
   * @param aGov the gov on planet aPlanet
   * @param aPlanet a neutral planet where aGov are
   * @return true if there is at least one friemdly gov on aPlanet
   */
  protected List<VIP> getAllFriendlyDiplomatOnNeutral(VIP aDip, Planet aPlanet, List<VIP> allDips){
  	Logger.finer("called: " + aDip.getBoss().getGovenorName() + " " + aPlanet.getName());
  	List<VIP> found = new LinkedList<VIP>();
    for (int i = 0; i < allDips.size(); i++){
        VIP tempVIP = (VIP)allDips.get(i);
        if (tempVIP != aDip){ // kolla om tempVIP inte är aDip
        	Planet tempLocation = tempVIP.getPlanetLocation();
        	if (tempLocation == aPlanet){ // tempDip är vid aPlanet
        		Player p1 = aDip.getBoss();
        		Player p2 = tempVIP.getBoss();
        		Faction f1 = p1.getFaction();
        		Faction f2 = p2.getFaction();
        		if ((f1 == f2) & (p1 != p2)){ // same faction but not same player
        			found.add(tempVIP);
        		}
        	}
        }
    }
  	return found;
  }

  /**
   * Check if there are any own diplomats (diplomats from the same player) on the neutral planet aPlanet
   * @param aDip a diplomat on planet aPlanet
   * @param aPlanet a neutral planet where aGov are
   * @return true if there is at least one friemdly gov on aPlanet
   */
  protected List<VIP> getAllOwnDiplomatOnNeutral(VIP aDip, Planet aPlanet, List<VIP> allDips){
  	List<VIP> found = new LinkedList<VIP>();
    for (VIP tempVIP : allDips){
        if (tempVIP != aDip){ // kolla om tempVIP inte är aDip
        	Planet tempLocation = tempVIP.getPlanetLocation();
        	if (tempLocation == aPlanet){ // tempDip är vid aPlanet
        		Player p1 = aDip.getBoss();
        		Player p2 = tempVIP.getBoss();
        		if (p1 == p2){ // same player
        			found.add(tempVIP);
        		}
        	}
        }
    }
  	return found;
  }

  public void shipsJoinGovenor(Planet joiningPlanet, VIP dip){
    List<Spaceship> allss = g.getSpaceships();
    List<Spaceship> removeShips = new LinkedList<Spaceship>();
    List<Spaceship> addShips = new LinkedList<Spaceship>();
    for (Spaceship ss : allss){
      if ((ss.getLocation() == joiningPlanet) & (ss.getOwner() == null)){ // skeppet är neutralt och är vid planeten
          // add new ship instead of the neutral one
          SpaceshipType sstTemp = dip.getBoss().findSpaceshipType(ss.getSpaceshipType().getName());
          if(sstTemp == null){
        	  sstTemp = g.findSpaceshipType(ss.getSpaceshipType().getName());
          }
          
    	  Spaceship ssTemp = sstTemp.getShip(null,0,ss.getTechWhenBuilt());
    	  ssTemp.setCurrentDc(ss.getCurrentDc());
    	  ssTemp.setKills(ss.getKills());
    	  ssTemp.setLocation(joiningPlanet);
    	  ssTemp.setOwner(dip.getBoss());
    	  // add new ship to addList
    	  addShips.add(ssTemp);
    	  // add ship to remove list
    	  removeShips.add(ss);
      }
    }
    for (Spaceship newShip : addShips) {
    	// add new ship belonging to player
    	g.getSpaceships().add(newShip);
	}
    for (Spaceship ss : removeShips){
    	// remove neutral ship
    	g.removeShip(ss);
    }
  }

  public void troopsJoinGovenor(Planet joiningPlanet, VIP dip){
	  List<Troop> allTroops = Functions.cloneList(g.getTroops());
	  List<Troop> removeTroops = new LinkedList<Troop>();
	  for (Troop aTroop : allTroops) {
		  if ((aTroop.getPlanetLocation() == joiningPlanet) & (aTroop.getOwner() == null)){ // 
			  // add new troop instead of the neutral one
			  TroopType ttTemp = dip.getBoss().findTroopType(aTroop.getTroopType().getUniqueName());
			  if(ttTemp == null){
				  ttTemp = g.findTroopType(aTroop.getTroopType().getUniqueName());
			  }
			  Troop troopTemp = ttTemp.getTroop(null,0,aTroop.getTechWhenBuilt());
			  troopTemp.setCurrentDC(aTroop.getCurrentDC());
			  troopTemp.setKills(aTroop.getKills());
			  troopTemp.setPlanetLocation(joiningPlanet);
			  troopTemp.setOwner(dip.getBoss());
			  g.getTroops().add(troopTemp);
			  // add ship to remove vector
			  removeTroops.add(aTroop);
		  }
	  }
	  for (Troop aTroop : removeTroops) {
		  // remove neutral troop
		  g.removeTroop(aTroop);
	  }
  }

  protected void removeNeutralShips(Planet joiningPlanet, VIP inf){
	  List<Spaceship> allss = g.getSpaceships();
	  List<Spaceship> removeShips = new LinkedList<Spaceship>();
	  for (Spaceship ss : allss){
		  if ((ss.getLocation() == joiningPlanet) & (ss.getOwner() == null)){ // skeppet är neutralt och är vid planeten
			  // add ship to remove vector
			  removeShips.add(ss);
		  }
	  }
	  for (Spaceship ss : removeShips){
		  // remove neutral ship
		  g.removeShip(ss);
	  }
  }

  protected void checkNonBesiegedPlanets(){
  	Logger.fine("checkNonBesiegedPlanets called");
    for (int x = 0; x < g.planets.size(); x++){
      Planet temp = (Planet)g.planets.get(x);
      if (!temp.isBesieged()){
        if (temp.getResistance() < 1){  // endast icke-neutrala planeter kan negativ resistance utan att ge upp
          if (temp.getPlayerInControl() != null){
            temp.setRes(1 + temp.getPlayerInControl().getFaction().getResistanceBonus());
          }else{
            temp.setRes(1);
          }
        }
      }
    }
  }

  protected void checkBroke(){
    Logger.fine("checkBroke called");
    for (int x = 0; x < g.players.size(); x++){
      Player temp = (Player)g.players.get(x);
      if (!temp.isDefeated()){
        int tempTreasury = temp.getTreasury();
        if (tempTreasury < 0){
          temp.setTreasury(0);
        }
      }
    }
  }

  protected void updateMapPlanetInfos(){
	  Logger.fine("updateMapPlanetInfos()");
	  for (Player aPlayer : g.getPlayers()) {
		  aPlayer.updateMapInfo();
	  }
  }
  
  protected void updatePlanetInfos(){
	  Logger.fine("updatePlanetInfos() called");
	  for (int x = 0; x < g.players.size(); x++){
		  Player tempPlayer = (Player)g.players.get(x);
		  Logger.finest("tempPlayer: " + tempPlayer.getName());
		  Logger.finest("-----------------------");
		  PlanetInfos pi = tempPlayer.getPlanetInfos();
		  for (int i = 0; i < g.planets.size();i++){
			  Planet p = (Planet)g.planets.get(i);
			  // set last known owner name
			  boolean spy = (g.findVIPSpy(p,tempPlayer) != null);
			  boolean shipInSystem = (g.playerHasShipsInSystem(tempPlayer,p));
			  boolean surveyShip = (g.findSurveyShip(p,tempPlayer) != null);
			  boolean surveyVIP = (g.findSurveyVIPonShip(p,tempPlayer) != null);
			  boolean open = p.isOpen();
			  boolean neutralPlanet = (p.getPlayerInControl() == null);
			  if (open | shipInSystem | spy){
				  if (!neutralPlanet){
					  pi.setLastKnownOwner(p.getName(),p.getPlayerInControl().getName(),g.turn + 1);
					  pi.setRazed(false,p.getName()); // aliens should not be shown as razed
					  pi.setLastKnownMaxShipSize(p.getName(), g.getLargestShipSizeOnPlanet(p, tempPlayer));
					  Logger.finest("setLastKnownMaxShipSize: " + p.getName() + ", " + g.getLargestShipSizeOnPlanet(p, tempPlayer));
				  }else{
					  //            LoggingHandler.finest(this,g,"updatePlanetInfos","g.turn: " + g.turn);
					  //            LoggingHandler.finest(this,g,"updatePlanetInfos","p.getName: " + p.getName());
					  pi.setLastKnownOwner(p.getName(),"Neutral",g.turn + 1);
					  pi.setRazed(p.isRazed(),p.getName());
					  pi.setLastKnownMaxShipSize(p.getName(), g.getLargestShipSizeOnPlanet(p, null, false));
					  Logger.finest("setLastKnownMaxShipSize neutral: " + p.getName() + ", " + g.getLargestShipSizeOnPlanet(p, null, false));
				  }
				  if (open | spy){
					  String buildingsOrbitString = createBuildingString(p.getBuildingsInOrbit());
					  pi.setLastKnownBuildingsInOrbit(p.getName(), buildingsOrbitString);
					  Logger.finest("setLastKnownBuildingsInOrbit: " + p.getName() + ", " + buildingsOrbitString);
					  // store surface buildings in separate field
					  String buildingsSurfaceString = createBuildingString(p.getBuildingsOnSurface());
					  pi.setLastKnownBuildingsOnSurface(p.getName(), buildingsSurfaceString);
					  Logger.finest("setLastKnownSurfaceBuildings: " + p.getName() + ", " + buildingsSurfaceString);
				  }else{ // must be shipInSystem, can only see buildings in orbit 
					  String buildingsOrbitString = createBuildingString(p.getBuildingsInOrbit());
					  pi.setLastKnownBuildingsInOrbit(p.getName(), buildingsOrbitString);
					  Logger.finest("setLastKnownBuildingsInOrbit shipInSystem: " + p.getName() + ", " + buildingsOrbitString);
				  }
			  }
			  // set last known prod and res values
			  if (open | spy | surveyShip | surveyVIP){
				  //        	LoggingHandler.finest(this,g,"updatePlanetInfos","last known res: " + p.getName() + " " + p.getResistance());
				  pi.setLastKnownProdRes(p.getName(),p.getPopulation(),p.getResistance());
				  pi.setLastKnownTroopsNr(p.getName(), g.getTroopsNrOnPlanet(p, tempPlayer));
				  Logger.finest("setLastKnownTroopsNr: " + p.getName() + ", " + g.getTroopsNrOnPlanet(p, tempPlayer));
			  }
		  }
	  }
  }

  protected String createBuildingString(List<Building> buildings){
	  StringBuffer sb = new StringBuffer();
	  Logger.finest("buildings: " + buildings.size());
	  for (Building building : buildings) {
		  Logger.finest("sb: " + sb + " sb.length(): " + sb.length());
		  if (sb.length() > 0){
			  Logger.finest("in if");
			  sb.append(", ");
		  }
		  sb.append(building.getBuildingType().getShortName());
	  }
	  Logger.finer("sb.toString(): " + sb.toString());
	  return sb.toString();
  }
  
  /**
   * Iterate through all planets and check if they are razed and there are aliens
   * present who wish to infestate 
   */
  protected void checkInfestationFromShips(){
      Logger.fine("checkInfestationFromShips called");
	  List<Planet> planets = g.getPlanets(); 
	  for (Planet planet : planets) {
		  if (planet.isRazedAndUninfected()){ 
			  List<Player> aliensPresent = getAliensWithPsychWarfare(planet);
			  if (aliensPresent.size() == 1){
				  Player infestator = aliensPresent.get(0);
				  infestator.addToHighlights(planet.getName(),HighlightType.TYPE_PLANET_INFESTATED);
				  infestator.addToGeneral("You have infected the planet " + planet.getName());
				  planet.setProd(0);
				  planet.setRes(1 + infestator.getFaction().getResistanceBonus());
				  planet.setPlayerInControl(infestator);
				  if (planet.isHasNeverSurrendered()){
					  planet.setHasNeverSurrendered(false);
					  // lägg till en slumpvis VIP till infestator spelaren
					  VIP aVIP = infestator.getGalaxy().maybeAddVIP(infestator);
					  if (aVIP != null){
						  aVIP.setLocation(planet);
						  infestator.addToVIPReport("When you conquered " + planet.getName() + " you have found a " + aVIP.getName() + " who has joined your service.");
						  infestator.addToHighlights(aVIP.getName(),HighlightType.TYPE_VIP_JOINS);
					  }
				  }
			  }else
			  if (aliensPresent.size() > 1){
				  for (Player player : aliensPresent) {
					  player.addToGeneral("You have not infected the planet " + planet.getName() + " because there are other players in the same system who also wants to infect the planet.");
				  }
			  }
		  }
	  }
  }
  
  protected List<Player> getAliensWithPsychWarfare(Planet aPlanet){
	  List<Player> playersPresent = new LinkedList<Player>(); // aliens with troops present
	  List<Player> allPlayers = g.getPlayers();
	  for (Player player : allPlayers) {
		  if (!player.isDefeated() & player.isAlien()){
			  List<Spaceship> playersShipsAtPlanet = g.getPlayersSpaceshipsOnPlanet(player,aPlanet);
			  boolean pw = false;
			  for (Spaceship spaceship : playersShipsAtPlanet) {
				  if (spaceship.getPsychWarfare() > 0){
					  pw = true;
				  }
			  }
			  if (pw){
				  playersPresent.add(player);
			  }
		  }
	  }
	  return playersPresent;
  }
  
/*
  private void moveRetreatingShips(){
    Vector allss = g.getSpaceships();
    for (int x = 0; x < g.players.size(); x++){
      Player tempPlayer = (Player)g.players.elementAt(x);
      int genSize = tempPlayer.getTurnInfo().getGeneralSize();
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss = (Spaceship)allss.elementAt(i);
        if (ss.getOwner() == tempPlayer){
          if (ss.getLocation() == null){ // skeppet är på flykt
            ss.moveShip(ss.getRetreatingTo(),ss.getOwner().getTurnInfo());
            // kolla om skeppet anlänt till en av spelarens planeter
            if (ss.getRetreatingTo().getPlayerInControl() != ss.getOwner()){
              // sätt skeppet att fly vidare om det kan
              boolean planetExistsToRunTo = ss.runAway(true); // returnerar false om skeppet inte har några planeter kvar att fly till (egna, samma faction eller neutrala)
              if (!planetExistsToRunTo){
                // remove ship from game
                g.checkVIPsInScuttledShips(ss,tempPlayer);
                g.spaceships.removeElement(ss);
                ss.getOwner().getTurnInfo().addToLatestGeneralReport("Your ship " + ss.getName() + " has been scuttled by its crew, when retreating retreating from " + ss.getOldLocation().getName() + " last turn, because there was nowhere they could run to.");
              }else{
                ss.getOwner().getTurnInfo().addToLatestGeneralReport("Your ship " + ss.getName() + " has retreated again, this time to " + ss.getRetreatingTo().getName() + ", when trying to reach a friendly system.");
              }
            }else{
              // ships has finished its retreat, clear retreat data
              ss.clearRetreatPlanets();	
            }
          }
        }
      }
      if (genSize < tempPlayer.getTurnInfo().getGeneralSize()){
        tempPlayer.addToGeneral("");
      }
    }
  }
*/
  
  protected void updateSquadronsLocation(){
	  Logger.fine("updateSquadronsLocation called");
	  List<Spaceship> allss = g.getSpaceships();
	  for (Spaceship aSpaceship : allss) {
		aSpaceship.updateSquadronLocation();
	}
	  
  }
  
  protected void moveRetreatingShips(){
  	Logger.fine("moveRetreatingShips called");
    List<Spaceship> allss = g.getSpaceships();
    for (Player tempPlayer : g.players){
      Logger.finer("player: " + tempPlayer);
      int genSize = tempPlayer.getTurnInfo().getGeneralSize();
      // move all who can move on their own, except squadrons in a carrier
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss = (Spaceship)allss.get(i);
        if (ss.getOwner() == tempPlayer){
          if (ss.isRetreating()){ 
        	  if (ss.getRange().canMove()){
        		  if (ss.getCarrierLocation() == null){ // only squadrons can have a carrier location
        			  Logger.finest("moveRetreatingShip: " + ss);
        			  ss.moveShip(ss.getRetreatingTo().getName(),ss.getOwner().getTurnInfo());
        		  }
        	  }
          }
        }
      }
      // move squadrons in carriers (the only ones not moved above)
      for (int i = 0; i < allss.size(); i++){
          Spaceship ss = (Spaceship)allss.get(i);
          if (ss.getOwner() == tempPlayer){
        	  if (ss.isRetreating()){ 
        		  if (ss.getCarrierLocation() != null){ // is in a carrier, only squadron can be in a carrier
        			  ss.moveRetreatingSquadron(ss.getOwner().getTurnInfo());
        		  }
        	  }
          }
      }
      if (genSize < tempPlayer.getTurnInfo().getGeneralSize()){
    	  tempPlayer.addToGeneral("");
      }
    }
  }

  protected void performShipRepairs(){
  	Logger.fine("performShipRepairs called");
    List<Spaceship> allss = g.getSpaceships();
    for (Spaceship ss : allss){  // gå igenom alla rymdskepp
      if (ss.getCurrentDc() < ss.getDamageCapacity()){  // skeppet är skadat
        Planet location = ss.getLocation();
        if (location != null){  // skeppet är ej på flykt
          if (location.getPlayerInControl() == ss.getOwner()){  // skeppet är vid en av spelarens planeter
        	 // int maxResupplySize = location.getMaxWharfsSize();
            int maxRepairTonnage = location.getMaxRepairTonnage();
            if (ss.getTonnage() <= maxRepairTonnage){  // det finns ett skeppsvarv som är tillräckligt stort för att reparera skeppet
              ss.performRepairs();
            }
          }
        }
      }
    }
  }

  protected void performTroopRepairs(){
	  Logger.fine("performTroopRepairs called");
	  for (int i = 0; i < g.getPlayers().size(); i++){
		  Player aPlayer = (Player)g.getPlayers().get(i);
		  boolean repairHasBeenPerformed = false;
		  List<Troop> allTroops = g.getPlayersTroops(aPlayer);
		  for (Troop aTroop : allTroops) {
			  if (aTroop.isDamaged()){
				  if (aTroop.getPlanetLocation() != null){
					  if (aTroop.getPlanetLocation().getPlayerInControl() == aTroop.getOwner() && !g.isOngoingGroundBattle(aTroop.getPlanetLocation(), aTroop.getOwner())){
						  aTroop.performRepairs(0.25);
						  repairHasBeenPerformed = true;
					  }
				  }else{
					  // troop in carrier
					  Spaceship aCarrier = aTroop.getShipLocation();
					  if (aCarrier.getLocation() != null){
						  if (aCarrier.getLocation().getPlayerInControl() == aTroop.getOwner()){
							  // troop in acrrier at own planet
							  aTroop.performRepairs(0.15);
							  repairHasBeenPerformed = true;
						  }else{
							  // troop in acrrier at non-own planet
							  aTroop.performRepairs(0.05);
							  repairHasBeenPerformed = true;
						  }
					  }else{
						  // carrier is retreating
						  aTroop.performRepairs(0.05);
						  repairHasBeenPerformed = true;
					  }
				  }
			  }
		  }
		  if (repairHasBeenPerformed){
			  aPlayer.addToGeneral("");
		  }
	  } // end for loop
  }

  protected void performResupply(){ 
  	Logger.fine("performResupply called");
    List<Spaceship> allss = g.getSpaceships();
    for (Spaceship ss : allss){  // gå igenom alla rymdskepp
      if (ss.getNeedResupply()){  // skeppet är skadat
        Planet location = ss.getLocation();
        if (location != null){  // skeppet är ej på flykt
          if (location.getPlayerInControl() == ss.getOwner()){  // skeppet är vid en av spelarens planeter
          	// max repair at wharfs is same as resupply level
            int maxResupplySize = location.getMaxWharfsSize();
            ss.supplyWeapons(maxResupplySize);
          }
          if (ss.getNeedResupply()){ // skeppet är fortfarande i behov av resupply 
          	// kolla efter supplyships
            int maxResupplySize = g.getMaxResupplyFromShip(location,ss.getOwner());
            ss.supplyWeapons(maxResupplySize);          	
          }
        }
      }
    }
  }

    protected void updateTreasury(){
    	Logger.fine("updateTreasury called");
    	int tempIncome;
    	for (int x = 0; x < g.players.size(); x++){
    		Player temp = (Player)g.players.get(x);
    		if (!temp.isDefeated()){
    			tempIncome = g.getPlayerIncome(temp,false);
    			Logger.finer("Add to treasury: " + tempIncome + " for player " + temp.getGovenorName());
    			temp.addToTreasury(tempIncome);
    		}
    	}
    }

    protected void payUpkeepShips(){
    	Logger.fine("payUpkeepShips called");
    	int tempUpkeep;
    	for (int x = 0; x < g.players.size(); x++){
    		Player temp = (Player)g.players.get(x);
    		if (!temp.isDefeated()){
    			tempUpkeep = g.getPlayerUpkeepShips(temp);
    			temp.removeFromTreasury(tempUpkeep);
    		}
    	}
    }
   
    protected void payUpkeepVIPs(){
    	Logger.fine("payUpkeepVIPs called");
    	int tempUpkeep;
    	for (int x = 0; x < g.players.size(); x++){
    		Player temp = (Player)g.players.get(x);
    		if (!temp.isDefeated()){
    			tempUpkeep = g.getPlayerUpkeepVIPs(temp);
    			temp.removeFromTreasury(tempUpkeep);
    		}
    	}
    }

    protected void payUpkeepTroops(){
        Logger.fine("payUpkeepTroops called");
        int tempUpkeep;
        for (int x = 0; x < g.players.size(); x++){
        	Player aPlayer = (Player)g.players.get(x);
        	if (!aPlayer.isDefeated()){
        		tempUpkeep = g.getPlayerUpkeepTroops(aPlayer);
        		aPlayer.removeFromTreasury(tempUpkeep);
        	}
        }
    }

    protected void writeUpkeepInfo(){
      Logger.fine("writeUpkeepInfo called");
      for (int x = 0; x < g.players.size(); x++){
        Player tempPlayer = (Player)g.players.get(x);
//        Vector allStrings = tempPlayer.getTurnInfo().getLatestGeneralReport().getAllReports();
/*        if (!tempPlayer.isDefeated()){
          int tempUpkeep = g.getPlayerUpkeep(tempPlayer);
          if (tempUpkeep == 0){
            allStrings.insertElementAt("No extra upkeep for spaceships this turn.",5);
          }else{
            allStrings.insertElementAt("Extra upkeep for spaceships this turn: -" + tempUpkeep,5);
          }
          allStrings.insertElementAt("",6);
        }
*/		if (tempPlayer.isBroke()){
//            allStrings.insertElementAt("WARNING: Upkeep exceeds income. You are broke!",6);
//            allStrings.insertElementAt("Until upkeep gets below income you cannot move any ship or VIP, or have any expenses.",7);
            tempPlayer.addToHighlights("",HighlightType.TYPE_BROKE);
        	EconomyReport er = tempPlayer.getTurnInfo().getLatestEconomyReport();
        	er.setBrokeNextTurn(true);
        }
      }
    }

    protected void defeatedPlayers(){
      Logger.fine("defeatedPlayers called");
      for (int x = 0; x < g.players.size(); x++){
        Player tempPlayer = (Player)g.players.get(x);
        if (!tempPlayer.isDefeated()){
          // räkna antalet planeter spelaren har
          boolean noPlanet = g.checkNoPlanet(tempPlayer);
//          boolean noPlanet = true;
//          for (int i = 0; i < g.planets.size();i++){
//            Planet p = (Planet)g.planets.get(i);
//            if (p.getPlayerInControl() == tempPlayer){
//              noPlanet = false;
//            }
//          }
          // kolla att spelaren fortfarande har kvar sin Guvernör
          // eller
          // om spelaren har planeter
          if (noPlanet | g.findVIPGovenor(tempPlayer) == null){
            tempPlayer.defeated(g.findVIPGovenor(tempPlayer) == null,g.getTurn());
            g.removeVIPs(tempPlayer);
            // if the gov has been killed there might exist a lot of ships and planets
            // that should be made neutral or be removed
            if (g.findVIPGovenor(tempPlayer) == null){
            	if (tempPlayer.isAlien()){
            		// remove all ships
            		removeShipsDefeatedAlienPlayer(g,tempPlayer);
            		// set all players planets as razed
            		razePlanetsDefeatedAlienPlayer(g,tempPlayer);
            	}else{
            		// remove all ships not on own planets
            		removeShipsDefeatedPlayer(g,tempPlayer);
            		// make all other ships neutral
            		neutralizeShipsDefeatedPlayer(g,tempPlayer);
            		// make all planets neutral
            		neutralizePlanetsDefeatedPlayer(g,tempPlayer);
            	}
            }
          }
        }
      }
    }
    
    protected void checkAbandonGame(){
        Logger.fine("checkAbandonGame called");
        for (int x = 0; x < g.players.size(); x++){
        	Player tempPlayer = (Player)g.players.get(x);
        	if (!tempPlayer.isDefeated()){
        		if (tempPlayer.getOrders().isAbandonGame()){
        			tempPlayer.abandonGame(g.getTurn());
        			removePlayer(tempPlayer);
        		}
        	}
        }
    }

    protected void checkRepeatedBroke(){
        Logger.fine("checkRepeatedBroke called");
        for (int x = 0; x < g.players.size(); x++){
        	Player tempPlayer = (Player)g.players.get(x);
        	if (!tempPlayer.isDefeated()){
        		if (tempPlayer.isBroke()){
        			tempPlayer.incNrTurnsBroke();
        			if (tempPlayer.getNrTurnsBroke() == 5){
        				tempPlayer.brokeRemovedFromGame(g.getTurn());
        				removePlayer(tempPlayer);
        			}else
        			if (tempPlayer.getNrTurnsBroke() == 4){
        				tempPlayer.brokeRemovedWarning();
        			}
        		}else{
        			tempPlayer.setNrTurnsBroke(0);
        		}
        	}
        }
    }

    protected void removePlayer(Player tempPlayer){
		// remove all vips 
		g.removeVIPs(tempPlayer);
		if (tempPlayer.isAlien()){
			removeShipsDefeatedAlienPlayer(g,tempPlayer);
		//	g.removeWharfsDefeatedAlienPlayer(tempPlayer);
			g.removeBuildingsDefeatedAlienPlayer(tempPlayer);
			razePlanetsDefeatedAlienPlayer(g,tempPlayer);
		}else{
			// remove all ships not on own planets
			removeShipsDefeatedPlayer(g,tempPlayer);
			// make all other ships neutral
			neutralizeShipsDefeatedPlayer(g,tempPlayer);
			// make all planets neutral
			neutralizePlanetsDefeatedPlayer(g,tempPlayer);
		}

    }
    protected void removeShipsDefeatedPlayer(Galaxy g, Player defeatedPlayer){
    	List<Spaceship> allShipsList = g.getPlayersSpaceships(defeatedPlayer);
    	HashSet<Planet> planetsNoLongerBesieged = new HashSet<Planet>();
    	for (Spaceship aShip : allShipsList) {
			Planet location = aShip.getLocation();
			if (location == null){
				// ship is retreating, remove it
				g.removeShip(aShip);
			}else
				if (location.getPlayerInControl() != defeatedPlayer){
					planetsNoLongerBesieged.add(location);
					// ship is not at own planet, remove it
					g.removeShip(aShip);
				}
		}
    	checkBesiegedPlanets(planetsNoLongerBesieged);
    }

    protected void removeShipsDefeatedAlienPlayer(Galaxy g, Player defeatedPlayer){
    	List<Spaceship> allShipsList = g.getPlayersSpaceships(defeatedPlayer);
    	HashSet<Planet> planetsNoLongerBesieged = new HashSet<Planet>();
    	for (Spaceship aShip : allShipsList) {
			Planet location = aShip.getLocation();
			g.removeShip(aShip);
			if (location != null){
				if (location.getPlayerInControl() != defeatedPlayer){
					planetsNoLongerBesieged.add(location);
				}
			}
		}
    	checkBesiegedPlanets(planetsNoLongerBesieged);
    }

    protected void checkBesiegedPlanets(HashSet<Planet> planetsNoLongerBesieged){
    	for (Iterator<Planet> iter = planetsNoLongerBesieged.iterator(); iter.hasNext();) {
			Planet aPlanet = (Planet) iter.next();
			if (aPlanet.isBesieged()){
				aPlanet.setBesieged(false);
				List<Spaceship> hostileShips = g.getHostileShipsAtPlanet(aPlanet);
				if (hostileShips.size() != 0){
					for (Spaceship spaceship : hostileShips) {
						if(spaceship.isCanBlockPlanet()){
							aPlanet.setBesieged(true);
						}
					}
				}
			}
		}
    }

    protected void neutralizeShipsDefeatedPlayer(Galaxy g, Player defeatedPlayer){
    	List<Spaceship> allShipsList = g.getPlayersSpaceships(defeatedPlayer);
    	for (Spaceship aShip : allShipsList) {
			// make ship neutral
			aShip.setOwner(null);
		}
    }

    protected void neutralizePlanetsDefeatedPlayer(Galaxy g, Player defeatedPlayer){
    	List<Planet> playerPlanets = g.getPlayersPlanets(defeatedPlayer);
        PlanetInfos pi = defeatedPlayer.getPlanetInfos();
    	for (Planet aPlanet : playerPlanets) {
			// make planet neutral
			aPlanet.setPlayerInControl(null);
			// remove any buildings that should autodestruct when conquered (or neutralized)
			aPlanet.destroyBuildingsThatCanNotBeOverTaked(null);
            pi.setLastKnownOwner(aPlanet.getName(),"Neutral",g.turn + 1);
		}
    }

    protected void razePlanetsDefeatedAlienPlayer(Galaxy g, Player defeatedPlayer){
    	List<Planet> playerPlanets = g.getPlayersPlanets(defeatedPlayer);
        PlanetInfos pi = defeatedPlayer.getPlanetInfos();
    	for (Planet aPlanet : playerPlanets) {
			aPlanet.setRazed();
            pi.setLastKnownOwner(aPlanet.getName(),"Neutral",g.turn + 1);
		}
    }

    protected void performOrders(){
    	Logger.fine("performOrders called");
    	List<Player> tempPlayers = new ArrayList<Player>();
    	tempPlayers.addAll(g.players);
    	
    	while(tempPlayers.size() > 0){
    		int random = Functions.getRandomInt(0, tempPlayers.size()-1);
    		
    		Player temp = (Player)tempPlayers.get(random);
    		int genSize = temp.getTurnInfo().getGeneralSize();
    		if (!temp.isDefeated()){
    			temp.performOrders();
    		}
    		if (genSize < temp.getTurnInfo().getGeneralSize()){
    			temp.addToGeneral("");
    		}
    		tempPlayers.remove(random);
    	}
    	
    }

    protected void performDiplomacyOrders(){
    	Logger.fine("performDiplomacyOrders called");
    	g.setPostConfList(new LinkedList<DiplomacyState>());
    	for (Player aPlayer : g.players) {
    		int genSize = aPlayer.getTurnInfo().getGeneralSize();
    		if (!aPlayer.isDefeated()){
    			aPlayer.performDiplomacyOrders();
    		}
    		if (genSize < aPlayer.getTurnInfo().getGeneralSize()){
    			aPlayer.addToGeneral("");
    		}
		}
    	for (DiplomacyState aDiplomacyState : g.getPostConfList()) {
    		aDiplomacyState.setCurrentLevel(DiplomacyLevel.CONFEDERACY);
		}
    }

    public boolean allPlanetsRazedAndUninfected(){
      boolean notRazed = false;
      for (int i = 0; i < g.planets.size();i++){
        Planet p = (Planet)g.planets.get(i);
        if (!p.isRazedAndUninfected()){
          notRazed = true;
        }
      }
      return !notRazed;
    }

    
    protected void peaceOnAllPlanets(){
    	for (Planet planet : g.planets) {
    		planet.peace();
		}
    }
    
    protected void checkSpaceBattles(){
    	Logger.fine("checkSpaceBattles called");
    	// leta igenom alla planeter
    	for (int i = 0; i < g.planets.size(); i++){
		    Planet tempPlanet = (Planet)g.planets.get(i);
		    Logger.finest("Planet loop: " + tempPlanet.getName());
		    List<TaskForce> taskforces = new ArrayList<TaskForce>();
		
		    taskforces = g.getTaskForces(tempPlanet,false);
		    // kolla om det blir några konflikter (rymdstrider och belägringar)
		    if (taskforces.size() > 0){
		    	Logger.finer("TaskForces > 0, size: " + taskforces.size() + " " + tempPlanet.getName());
		    	checkConflicts(taskforces,tempPlanet);
		    }
        }
    	Logger.finer("checkSpaceBattles finished");
    }

    protected void checkConflicts(List<TaskForce> taskforces, Planet aPlanet){
  	  Logger.finer("Taskforces.size = " + taskforces.size());
      if (taskforces.size() > 1){ // kan bli rymdstrider
    	  Logger.finer("Taskforces > 2, " + taskforces.size());
    	  Collections.shuffle(taskforces);
    	  defendersFirst(taskforces,aPlanet);
    	  checkSpaceshipBattles(taskforces,aPlanet,0,1);
      }
      // remove abandoned squadrons in taskforces
      checkAbandonedSquadrons(taskforces,aPlanet);
      // check if there are any abanboned troops
      //g.checkAbandonedTroops(aPlanet);
      // if there are at least one TF left at planet
      if (taskforces.size() > 0){
          // check for siege, blockade and landbattles
    	  checkSiege(taskforces,aPlanet);
      }
    }

    protected void checkAbandonedSquadrons(List<TaskForce> taskforces, Planet aPlanet){
        // remove abandoned squadrons in taskforces
        // if a taskforce becomes empty because of abandoned squadrons, remove
        //	the taskforce
        for (int i = taskforces.size()-1; i >= 0; i--){
            TaskForce tmpTF = taskforces.get(0);
            // check the ships in the TF
      	  tmpTF.checkAbandonedSquadrons(aPlanet);
      	  if (tmpTF.getTotalNrNonDestroyedShips() == 0){
      		  taskforces.remove(tmpTF);
      	  }
        }
    }
    
    
    // metod som kollar om det blir några rymdstrider vid en given planet
    protected void checkSpaceshipBattles(List<TaskForce> taskforces,Planet aPlanet,int curtf_low,int curtf_high){
      Logger.finer("checkSpaceshipBattles: taskforces.size(): " + taskforces.size()+" "+curtf_low+" "+curtf_high);
      TaskForce defenderTF = null;
      if (taskforces.get(0).getPlayer() == aPlanet.getPlayerInControl()){ // bryt ut ev. försvarare så den kan slåss sist
    	  defenderTF = taskforces.get(0);
    	  taskforces.remove(defenderTF);
      }
      Collections.shuffle(taskforces);
      for (TaskForce tf1 : taskforces) { // loopa igenom alla TF så att alla får chansen att slåss med varandra
          for (TaskForce tf2 : taskforces) {
          if (tf1 != tf2){ // så flottorna ej slåss mot sig själva :)
          handleSpaceshipBattle(tf1,tf2,aPlanet);
          }
          }
      }
      if (defenderTF != null){
          for (TaskForce tf : taskforces) { 
      handleSpaceshipBattle(tf,defenderTF,aPlanet);
          }
      }
      // ta bort besegrade tf
      int index = 0;
      while (index < taskforces.size()){
    	  if (taskforces.get(index).getTotalNrShips() == 0){
    		  taskforces.remove(index);
    	  }else{
    		  index++;
    	  }
      }
      // ev, lägg tillbaka defender om de finns och ej är besegrade
      if (defenderTF != null){
    	  if (defenderTF.getTotalNrShips() > 0){
    		  taskforces.add(0, defenderTF);
    	  }
      }
      Logger.finer("checkSpaceshipBattles finished");
    }
    
    protected void handleSpaceshipBattle(TaskForce tf1, TaskForce tf2,Planet aPlanet){
    	if ((tf1.getTotalNrShips() > 0) & (tf2.getTotalNrShips() > 0)){ // om någon av flottorna har slut på skepp är den redan besegrad
    		if (hostile(tf1,tf2,aPlanet)){  // at least one of the tf:s want to fight
    			Logger.finest("Hostile!");
    			performCombat(tf1,tf2,aPlanet); // performCombat returnerar den förlorande sidan
    		}          
    	}
    }

	protected int findIndex(List<TaskForce> taskforces,TaskForce losertf){
      int index = -1;
      for (int i = 0; i < taskforces.size(); i++){
        TaskForce temptf = (TaskForce)taskforces.get(i);
        if (temptf == losertf){
          index = i;
        }
      }
      return index;
    }

    protected boolean hostile(TaskForce tf1, TaskForce tf2, Planet aPlanet){
      Logger.finer("hostile: " + aPlanet.getName());
      boolean hostile = false;
      // kolla först om ena tf:n är neutral
      if (tf1.getPlayer() == null){
      	Logger.finer("tf1 är neutral");
      	hostile = tf2.getPlayer().getPlanetOrderStatuses().isAttackIfNeutral(aPlanet.getName());
      	Logger.finer("hostile, attack if neutral:" + hostile);
      }else
      // eller den andra...
      if (tf2.getPlayer() == null){
      	Logger.finer("tf2 är neutral");
        hostile = tf1.getPlayer().getPlanetOrderStatuses().isAttackIfNeutral(aPlanet.getName());
      	Logger.finer("hostile, attack if neutral:" + hostile);
      }else   // bägge flottorna tillhör spelare
//      if (tf1.getPlayer().getFaction() != tf2.getPlayer().getFaction()){ // här skulle man kunna kolla på diplomatiska status istället om de existerade...
      if (g.getDiplomacy().hostileTaskForces(tf1.getPlayer(),tf2.getPlayer(),aPlanet)){ // check diplomaticState..
      	Logger.finer("Diplomacy say fight!");
        hostile = true;
        Logger.finer("Hostile, end of diff fac: " + hostile);
      }
      Logger.finer("Hostile finished: " + hostile);
      return hostile;
    }

    protected boolean hostile(TaskForce tf, Planet aPlanet){
    	Logger.finer("hostile (planet): " + aPlanet.getName());
    	if (tf.getPlayer() != null){
    		Logger.finer("hostile (governor): " + tf.getPlayer().getGovenorName());
    	}else{
    		Logger.finer("Taskforce is neutral");
    	}
    	boolean hostile = false;
    	if (tf.getPlayer() != null && tf.canBesiege()){
    		if (tf.getPlayer() != aPlanet.getPlayerInControl()){
    			Logger.finer("Planet does not belong to player: " + aPlanet.getName());
    			if (aPlanet.getPlayerInControl() == null){  // kolla om den är neutral
    				Logger.finer("Planet is neutral");
    				hostile = tf.getPlayer().getPlanetOrderStatuses().isAttackIfNeutral(aPlanet.getName());
    				Logger.finer("Planet is neutral, hostile = " + hostile);
    			}else  // kolla om det är fred med planetägan.
//    				if (aPlanet.getPlayerInControl().getFaction() != tf.getPlayer().getFaction()){
       				if (g.getDiplomacy().hostileBesiege(aPlanet.getPlayerInControl(),tf.getPlayer())){
    					Logger.finer("Planet belongs to player from another faction");
    					hostile = true;
    					Logger.finer("Hostile = " + hostile);
    				}
    		}
    	}
    	Logger.finer("Hostile finished, returns " + hostile);
    	return hostile;
    }

    protected void defendersFirst(List<TaskForce> taskforces, Planet aPlanet){
      int found = -1;
      TaskForce temptf = null;
      Player tempPlayer = null;
      for (int i = 0; i < taskforces.size(); i++){
        temptf = (TaskForce)taskforces.get(i);
        tempPlayer = temptf.getPlayer();
        if (aPlanet.getPlayerInControl() == tempPlayer){
          found = i;
        }
      }
      if (found > -1){ // flytta försvararna till vectorns första plats
        temptf = (TaskForce)taskforces.get(found);
        taskforces.remove(found);
        taskforces.add(0,temptf);
      }
    }

    /**
     * Called after spaceship battles are finished.
     * There may be more than 1 TF at planet
     * 
     * Here is the complete algorithm
     * ------------------------------
     * 
      // defenders have no TF at planet
        // attackers are more than one
          // blockade
    	// else (only 1 attacking TF)
    	  // check destroy wharfs
          // bombardment
    	  // defender is alien
      	    // siege
    	  // defender have troops
    	  	// troop bombardment
    	  // planet razed
    	    // defender have troops
    	      // remove defending troops
    	    // attacker is alien
    	  	  // attacker have troops ability
    	  	    // planet conquered by alien
    	  // not razed, defender have no troops
    	  	// attacker have troops
    	  	  // defender is alien
    	  	    // attacker is alien
    	  	      // planet conquered by alien
    	  	    // attacker is not alien
    	  	      // planet is razed
    	  	  // defender is not alien
    	  	    // attacker is alien
    	  	      // planet conquered by alien
    	  	    // attacker is not alien
    	  	      // planet conquered
    	    // attacker have no troops
    	  	  // defender is alien
    	        // resistance < 1
    	          // planet is razed
    	  	      // attacker is alien
    	  	        // attacker have troops ability
    	  	          // planet conquered by alien
    	  	    // else
    	  		  // planet under siege but still holding
    	  	  // defender is not alien
    	        // check if resistance have been lowered
    	          // attacker is alien
    	            // production < 1
    	  	          // attacker have troops ability
    	  	            // planet conquered by alien
    	  	          // else
    	  	            // planet is razed
      	  	        // else
    	  		      // planet under siege but still holding
    	          // attacker is not alien
    	            // resistance < 1
     	              // planet conquered
    	            // else
    	  		      // planet under siege but still holding
    	  		// resistance not lowered
    	  // defender have troops
            // attacker have troops
    	      // land battle
    	      // if attacker win (no defending troops left)
	            // planet conquered
    	      // if inconclusive (both have troops left)
                // planet under siege but still holding
	          // if defender won (no attackers left)
    	        // planet under siege but still holding
	        // attacker have no troops
              // planet under siege but still holding

     * 
     * @param taskforces
     * @param aPlanet
     */
    protected void checkSiege(List<TaskForce> taskforces, Planet aPlanet){
      Logger.finer("checkSiege: " + taskforces.size() + " " + aPlanet.getName());
      if (!aPlanet.isRazedAndUninfected()){ // first check that the planet isn't razed and uninhabited. Otherwise there are no siege
    	  // check if defenders have no TF at planet
    	  TaskForce firstTF = taskforces.get(0);
    	  Player attackingPlayer = firstTF.getPlayer();
    	  if (firstTF.getPlayer() != aPlanet.getPlayerInControl()){
    		  Logger.finer("First taskforce does not own the planet.");
    		  // check if attackers are more than one TF
    		  List<TaskForce> tfsWantingToBesiege = countBesiegingTFs(taskforces,aPlanet);
    		  if(tfsWantingToBesiege.size() > 0){
	    		  // logik för cannon fire. Om alla skepp dör så upphör belägringen.
	    		  performCannonDefenceFire(aPlanet,tfsWantingToBesiege);
	    		  // recount and check if any TF can besiege.
	    		  tfsWantingToBesiege = countBesiegingTFs(taskforces,aPlanet);
    		  }
			  if (tfsWantingToBesiege.size() < 1){
    			  // alla flottor är döda
    		  }else
    		  if (tfsWantingToBesiege.size() > 1){  // if more than one TF wants to besiege planet is blocked
    			  Logger.finer("More than one taskforce in orbit = Blockade!");
    			  // blockade
    			  performBlockade(aPlanet,tfsWantingToBesiege);
    			  
    		  }else{ // else (only 1 attacking TF)
    			  Logger.finer("One taskforce in orbit that wants to besiege");
    			  // find besieging taskforce
    	    	  firstTF = tfsWantingToBesiege.get(0);
    	    	  attackingPlayer = firstTF.getPlayer();

    			  g.checkDestroyBuildings(aPlanet,firstTF.getPlayer(),false);

    			  // siege with psywarfare
    			  int resSiege = 0;
   				  // siege, psyWarfare should work the same for both non-aliens and aliens
				  resSiege = aPlanet.besieged(firstTF);

    			  // bombardment
    			  int resBomb = aPlanet.underBombardment(firstTF);

    			  Player defPlayer = aPlanet.getPlayerInControl();
    			  List<Troop> defTroops = g.getTroopsOnPlanet(aPlanet,defPlayer);
    			  if (g.getTroopsOnPlanet(aPlanet, aPlanet.getPlayerInControl()).size() > 0){
    				  // perform bombardment against troops
    				  bombardTroops(defPlayer,defTroops,attackingPlayer,resBomb,aPlanet);
    			  }
    			  // check if planet is razed
    			  boolean infectedByAlien = aPlanet.getInfectedByAlien();
				  Logger.fine("1");
    			  if (((aPlanet.getPopulation() < 1) & !infectedByAlien) | ((aPlanet.getResistance() < 1) & infectedByAlien)){ // planet razed
    				  // remove player on planet and set planet as razed
    				  aPlanet.razed(firstTF.getPlayer());
    				  // check if defender have troops on planet
    				  if (g.getTroopsOnPlanet(aPlanet, aPlanet.getPlayerInControl()).size() == 0){
    					  // TODO Ta bort alla troops. Hindra alltså möjligheten att troops kan finnas på en belägrade planet. Är det inte risk att egna trupper dör då?
    					  // remove defending troops
    				 //     g.checkAbandonedTroops(aPlanet);
    				  }
    				  // check if attacker is alien
    				  if (firstTF.getPlayer().isAlien()){
    					  boolean psychExist = g.getMaxPsychWarfare(aPlanet,firstTF.getPlayer()) > 0;
    					  if (psychExist){ // attacker have psychWarfare ability
    						  // planet conquered by alien
    						  aPlanet.infectedByAttacker(attackingPlayer);
    					  }
    				  }
    			  }else{
    				  Logger.fine("2");
    				  if ((resSiege + resBomb) == 0 && !g.getGameWorld().isTroopGameWorld()){
    					  aPlanet.resistanceNotLowered(firstTF);
    				  }
    				  
    				  List<Troop> allTroopsOnPlanet = g.findAllTroopsOnPlanet(aPlanet);
    				  if (allTroopsOnPlanet.size() == 0){
    	    			  Logger.fine("No defending troops");
    	    			  
						  // check if defender is alien
						  if (aPlanet.getInfectedByAlien()){
							  // check if resistance < 1
							  if (aPlanet.checkSurrender(g)){
								  // planet is razed
								  aPlanet.razed(firstTF.getPlayer());
								  // check if attacker is alien
								  if (firstTF.isAlien()){
			    					  boolean psychExist = g.getMaxPsychWarfare(aPlanet,firstTF.getPlayer()) > 0;
			    					  if (psychExist){ // attacker have psychWarfare ability
										  // planet conquered by alien
										  aPlanet.infectedByAttacker(attackingPlayer);
									  }
								  }
							  }else{ // planet under siege but still holding
								  aPlanet.holding(firstTF);
							  }
						  }else{ // defender is not alien
							  // check if attacker is alien
							  if (firstTF.isAlien()){
								  // check if resistance < 1
								  if (aPlanet.checkSurrender(g)){
			    					  boolean psychExist = g.getMaxPsychWarfare(aPlanet,firstTF.getPlayer()) > 0;
			    					  if (psychExist){ // attacker have psychWarfare ability
										  // planet conquered by alien
										  aPlanet.infectedByAttacker(attackingPlayer);
									  }else{ // no troops
										  // planet is razed
										  aPlanet.razed(firstTF.getPlayer());
									  }
								  }else{ // planet under siege but still holding
									  aPlanet.holding(firstTF);
								  }
							  }else{ // attacker is not alien
								  Logger.fine("attacker is not alien");
								  // check if planet surrenders
								  if (aPlanet.checkSurrender(g)){ 
									  // planet conquered
									  aPlanet.planetSurrenders(firstTF);
								  }else{ // planet under siege but still holding
									  aPlanet.holding(firstTF);
								  }
							  }
						  }
					  }else{
						  aPlanet.setBesieged(true);
					  }
    			  } // end else
    		  } // end defenders have no TF at planet
    	  } // end !aPlanet.isRazedAndUninfected()
      }
      Logger.finer("checkSiege finished.");

//	  performSiege(aPlanet,taskforces.get(0));
    
    }
    
    
    
    protected void troopFight(Planet aPlanet){
        if (!aPlanet.isRazedAndUninfected()){ // first check that the planet isn't razed and uninhabited. Otherwise there are no siege
      	  
        	//Get all non planet owner players that have troops on the planet.
        	// If more than one attacking = blocking attack on defending troops.
        	List<Player> players = g.getAttackingPlayersWithTroopsOnPlanet(aPlanet);
        	if(players.size() > 1){
        	
	        	int index= 0;
	        	while (players.size() > index +1) {
	        		//check i player at index have any hostile troops to fight among the attcking players.
	        		Player attacking = players.get(index);
	        		int i=index + 1;
	        		// if friendly player check next player
	        		while(i < players.size() && !g.getDiplomacy().hostileBesiege(attacking, players.get(i))){
	        			i++;
	        		}
	        		// one of the players i hostile.
	        		if(i < players.size()){
	        			List<Troop> defendingTroops = g.findTroopsOnPlanet(aPlanet,attacking);
						List<Troop> attackingTroops = g.findTroopsOnPlanet(aPlanet,players.get(i));
						Logger.finer("perform land battle between " + attacking.getGovenorName() + " and " + players.get(i).getGovenorName());
						performLandBattle(attacking,defendingTroops,players.get(i),attackingTroops,aPlanet);
						defendingTroops = g.findTroopsOnPlanet(aPlanet,attacking);
						attackingTroops = g.findTroopsOnPlanet(aPlanet,players.get(i));
				      
						addLandbattleHighlights(defendingTroops.size() > 0,attackingTroops.size() > 0,attacking,players.get(i),aPlanet.getName());
						// Remove players that have fight this turn.
						players.remove(i);
						players.remove(index);
	        		}else{
	        			// The attacking player don't have any hostile army.
	        			index++;
	        		}
	        	}
        	}else if (players.size() == 1){
        	// Only one attacking player. Check if any defender and perform a battle or change planet owner to attcking player.	
        		if(g.findTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl()).size() > 0){// Defening troops perform a battle.
		        	Logger.finer("perform land battle aginst defender");
		        		Player attacking = players.get(0);
						// get both defending player/troops and attackning player/troops
						List<Troop> defendingTroops = g.findTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl());
						List<Troop> attackingTroops = g.findTroopsOnPlanet(aPlanet,attacking);
						performLandBattle(aPlanet.getPlayerInControl(),defendingTroops,attacking,attackingTroops,aPlanet);
						defendingTroops = g.findTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl());
						attackingTroops = g.findTroopsOnPlanet(aPlanet,attacking);
				      
						addLandbattleHighlights(defendingTroops.size() > 0,attackingTroops.size() > 0,aPlanet.getPlayerInControl(),attacking,aPlanet.getName());
	        	}
	        }
        	
        	
        	// get all players with troops after the battles.
    		players = g.getAttackingPlayersWithTroopsOnPlanet(aPlanet);
        	if(g.getTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl()).size() == 0){ // Försvarande spelar har inga trupper kvar.
        		if(players.size() == 1){// only one attacker and the planet should change owner.
        			if (players.get(0).isAlien()){
        				Logger.finer("Attacker is alien");
						// planet conquered by alien
						aPlanet.razed(players.get(0));
						aPlanet.infectedByAttacker(players.get(0));
        			}else{ // attacker is not alien
        				Logger.finer("Attacker is not alien");
						// check if defender is alien
						if (aPlanet.getInfectedByAlien()){
						    // planet is razed
							aPlanet.razed(players.get(0));
						}else{ // defender is not alien
							// planet conquered
							
							aPlanet.conqueredByTroops(players.get(0));
							List<TaskForce> taskForces = g.getTaskForces(aPlanet,false);
							List<TaskForce> countBesiegingTFs = countBesiegingTFs(taskForces, aPlanet);
							if(countBesiegingTFs.size() == 0){
								aPlanet.setBesieged(false);
							}else{
								aPlanet.setBesieged(true);
							}
						}
        			}
        		}
        	}else{
        		if(players.size() >= 1){ // at least one player still have troops on the planet.
        			aPlanet.besiegedAfterInconclusiveLandbattle();
        		}
        	}	
	        	
	      } // end !aPlanet.isRazedAndUninfected()
      }
    /*
    private void troopFight(Planet aPlanet){
        if (!aPlanet.isRazedAndUninfected()){ // first check that the planet isn't razed and uninhabited. Otherwise there are no siege
      	  
        	List<Player> players = g.getAttackingPlayersWithTroopsOnPlanet(aPlanet);
        	if(players.size() > 0){
        	
	        	Player attacking = null;
	        	for (Player player : players) {
					if(attacking == null){
						attacking = player;
					}else{
						if(g.getDiplomacy().hostileBesiege(attacking, player)){
							Logger.finer("perform land battle");
							// get both defending player/troops and attackning player/troops
							List<Troop> defendingTroops = g.findTroopsOnPlanet(aPlanet,attacking);
							List<Troop> attackingTroops = g.findTroopsOnPlanet(aPlanet,player);
							performLandBattle(attacking,defendingTroops,player,attackingTroops,aPlanet);
		//			    		  performGroundAssault(defendingPlayer,defendingTroops,attackingPlayer,attackingTroops,firstTF,aPlanet);
							// get troops after battle
							defendingTroops = g.findTroopsOnPlanet(aPlanet,attacking);
							attackingTroops = g.findTroopsOnPlanet(aPlanet,player);
					      
							addLandbattleHighlights(defendingTroops.size() > 0,attackingTroops.size() > 0,attacking,player,aPlanet.getName());
							
							attacking = null;
						}
					}
				}
	        	if(attacking != null && (g.findTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl())).size() > 0){// one attacker have not jet fight.
	        		if(aPlanet.getPlayerInControl() == null || g.getDiplomacy().hostileBesiege(attacking, aPlanet.getPlayerInControl())){
		        		Logger.finer("perform land battle");
						// get both defending player/troops and attackning player/troops
						List<Troop> defendingTroops = g.findTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl());
						List<Troop> attackingTroops = g.findTroopsOnPlanet(aPlanet,attacking);
						performLandBattle(aPlanet.getPlayerInControl(),defendingTroops,attacking,attackingTroops,aPlanet);
		//		    		  performGroundAssault(defendingPlayer,defendingTroops,attackingPlayer,attackingTroops,firstTF,aPlanet);
						// get troops after battle
						defendingTroops = g.findTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl());
						attackingTroops = g.findTroopsOnPlanet(aPlanet,attacking);
				      
						addLandbattleHighlights(defendingTroops.size() > 0,attackingTroops.size() > 0,aPlanet.getPlayerInControl(),attacking,aPlanet.getName());
	        		}
	        	}
	        	
	        	// Försvarande spelar har inga trupper kvar.
        		players = g.getAttackingPlayersWithTroopsOnPlanet(aPlanet);
	        	if(g.getTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl()).size() == 0){
	        		if(players.size() == 1){// only one attacker and the planet should change owner.
	        			if (players.get(0).isAlien()){
	        				Logger.finer("Attacker is alien");
							// planet conquered by alien
							aPlanet.razed(players.get(0));
							aPlanet.infectedByAttacker(players.get(0));
	        			}else{ // attacker is not alien
	        				Logger.finer("Attacker is not alien");
							// check if defender is alien
							if (aPlanet.getInfectedByAlien()){
							    // planet is razed
								aPlanet.razed(players.get(0));
							}else{ // defender is not alien
								// planet conquered	
								aPlanet.conqueredByTroops(players.get(0));
							}
	        			}
	        		}
	        	}else{
	        		if(players.size() >= 1){ // at least one player still have troops on the planet (and should have to have ships in orbit as well)
	        			aPlanet.besiegedAfterInconclusiveLandbattle();
	        		}
	        	}
        		
     
        	}
      	  } // end !aPlanet.isRazedAndUninfected()
      }
    */
    
      
    protected void addLandbattleHighlights(boolean defHaveTroops, boolean attHaveTroops, Player defPlayer, Player attPlayer, String planetName){
    	if (defHaveTroops){
    		if (attHaveTroops){
    			// inconclusive battle
    			if (defPlayer != null){
    				defPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_INCONCLUSIVE);
    			}
				attPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_INCONCLUSIVE);
    		}else{
    			// defender won
    			if (defPlayer != null){
    				defPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_WON);
    			}
				attPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_LOST);
    		}
    	}else{
    		if (attHaveTroops){
    			// attacker won
    			if (defPlayer != null){
    				defPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_LOST);
    			}
				attPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_WON);
    		}else{
    			// both sides destroyed
    			if (defPlayer != null){
    				defPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_BOTH_DESTROYED);
    			}
				attPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_BOTH_DESTROYED);
    		}
    	}    	
    }
    
    protected List<TaskForce> countBesiegingTFs(List<TaskForce> taskforces, Planet aPlanet){
    	List<TaskForce> tfsWantinToBesiege = new LinkedList<TaskForce>();
    	for (int i = 0; i < taskforces.size(); i++){
    		TaskForce temptf = (TaskForce)taskforces.get(i);
    		if (hostile(temptf,aPlanet) && !temptf.getPlayer().getPlanetOrderStatuses().isDoNotBesiege(aPlanet.getName())){ // kolla om de är fientligt inställda
    			tfsWantinToBesiege.add(temptf);
    		}
    	}
    	return tfsWantinToBesiege;
    }

    protected void checkGovOnNeutralPlanet(Planet aPlanet, TaskForce tf){
    	Logger.finer("called");
    	boolean neutralPlanet = (aPlanet.getPlayerInControl() == null);
    	if (neutralPlanet){
        	Logger.finer("Planet is neutral");
        	Logger.finer("tf: " + tf);
        	Logger.finer("tf.getPlayer(): " + tf.getPlayer());
    		Faction aFaction = tf.getPlayer().getFaction();
    		Player aPlayer = tf.getPlayer();
    		Galaxy g = aPlayer.getGalaxy();
    		List<VIP> govs = g.getAllGovsFromFactionOnPlanet(aPlanet,aFaction);
        	Logger.finer("Govs found: " + govs.size());
    		for (VIP aVIP : govs) {
				Player vipPlayer = aVIP.getBoss();
    	    	Logger.finer("In loop, removing VIP from governor: " + vipPlayer.getGovenorName());
				g.removeVIP(aVIP);
				vipPlayer.addToHighlights(aPlanet.getName(),HighlightType.TYPE_GOVENOR_ON_HOSTILE_NEUTRAL);
				vipPlayer.addToGeneral("While visiting the planet " + aPlanet.getName() + " on a diplomatic mission, the planet has been attacked by forces belonging to the same faction as you.");
				vipPlayer.addToGeneral("The population, enraged by this betrayal, immediately attacks your Governor.");
//				vipPlayer.defeated(true,g.getTurn()); detta sätt senade i GalaxyUpdater.defeatedPlayers()
			}
    	}
    }

    protected void performBlockade(Planet aPlanet, List<TaskForce> alltf){
      aPlanet.underBlockade(alltf);
    }
    
    protected void performCannonDefenceFire(Planet aPlanet, List<TaskForce> alltf){
    	for(int i=0; i < aPlanet.getBuildings().size();i++){
    		Building aBuilding = aPlanet.getBuildings().get(i);
    		if(aBuilding.getBuildingType().getCannonDamage() > 0){
    			for(int index=0; index < aBuilding.getBuildingType().getCannonRateOfFire(); index++){
    				// skall ha 50% chans att träffa...
    				int random = Functions.getRandomInt(0, 99) + 1;
					int randomIndex = Functions.getRandomInt(0, alltf.size()-1);
    				TaskForce tf = (TaskForce)alltf.get(randomIndex);
    				if(random < aBuilding.getBuildingType().getCannonHitChance()){// hit
        				tf.incomingCannonFire(aPlanet, aPlanet.getBuildings().get(i));
        				if(tf.getStatus().equalsIgnoreCase("destroyed")){
        					Logger.finer("destroyed");
        					alltf.remove(randomIndex);
        					if(alltf.size() == 0){// no TaskForce and ships left.
        						Logger.finer("return");
        						return;
        					}
        				}
    				}else{
    					String s = tf.getTotalNrShips()>1 ? "s" : "";
    			    	tf.getPlayer().addToGeneral("Your ship"+s+" at " + aPlanet.getName() + " was fired upon by an enemy " + aBuilding.getBuildingType().getName() + " but it misses.");
    			    	if(aPlanet.getPlayerInControl() != null){
    			    		aPlanet.getPlayerInControl().addToGeneral("Your " + aBuilding.getBuildingType().getName() + " at " + aPlanet.getName() + " fires but misses the enemy ships.");
    			    	}
    				}
    			}
    		}
    	}
    }    
    
    /**
     *     	Luftangrepp sker innan striden börjar? Nix.
    		Luftförsvar sker samtidigt? Ja
    		Artilleri sker också först??? Nix.
    		Alla strider kan ge skada åt bägge hållen? Ja.

    		För den större styrkan så används reserven för att anfalla motståndare 2-1. Reserven sorteras slumpvis och en efter en allokeras 
    			trupperna för att anfalla lämpliga motståndare. Detta baseras på anfallsvärden och motståndarens typ (armor/ej armor)
    			Trupper som är i strid med 2 motståndare anfaller slumpvis en av dem vid varje attack.
    		Flankerande trupper tar strid 1-1 slumpvis om de finns på bägge sidor.
    		Om ena sidan har fler flankerare än andra sidan sker något av följande (med de flankers som kommer förbi ev. motståndarens flankers):
    		1. Om motståndaren har SLD-trupp(er) så anfalls en av dem.
    		2. Om motståndaren saknar SLD:
    			2a: Om motståndaren har support troops anfalls slumpvis en av dessa.
    			2b: Annars anfalls en first line troop i ryggen. Den som anfalls i ryggen har svårt att skjuta tillbaka. En trupp kan endast anfallas av 1st flanker i ryggen i en strid.
    		Rymdskepp som hjälper till läggs till i listan över utslumpade attacker, men de kan inte attackeras (dock skjutas tillbaka på av AA).
    		Artilleri läggs också till men får ingen moteld alls.
    		Sedan utförs alla strider/attacker i utslumpad ordning. Om en trupp inte längre har en motståndare så gör den inget mer i denna strid.
    		För varje nytt drag görs en helt ny uppställning. 

     * @param defendingPlayer
     * @param defendingTroops	
     * @param attackingPlayer
     * @param attackingTroops
     * @param attackingTaskForce
     * @param aPlanet
     */
    public void performLandBattle(Player defendingPlayer,List<Troop> defendingTroops, Player attackingPlayer, List<Troop> attackingTroops, Planet aPlanet){
    	LandBattle battle = new LandBattle(defendingPlayer,defendingTroops,attackingPlayer,attackingTroops,aPlanet,g.getTurn(),g);
    	battle.performBattle();
    	// add land battle reports to players
    	attackingPlayer.addToGeneral(battle.getAttackingSummary());
    	attackingPlayer.getTurnInfo().addToLatestLandBattleReports(battle.getAttackingBattleReport());
    	if (defendingPlayer != null){
        	defendingPlayer.addToGeneral(battle.getDefendingSummary());
    		defendingPlayer.getTurnInfo().addToLatestLandBattleReports(battle.getDefendingBattleReport());
    	}
    }    

    protected void bombardTroops(Player defendingPlayer,List<Troop> defendingTroops, Player attackingPlayer, int bombardment, Planet aPlanet){
    	int performedBombardments = 0;
    	while ((performedBombardments < bombardment) & (defendingTroops.size() > 0)){
    		int randomIndex = Functions.getRandomInt(0, defendingTroops.size()-1);
    		
    		// 50% chans to destroy hit a troop (destroy) Gameworld should use bombardmentdamge greater then the best troop have in hit + 50%
    		if(Functions.getRandomInt(0, 100) < 50){
	    		Troop bombardedTroop = defendingTroops.get(randomIndex);
	    		String returnString = bombardedTroop.hit(g.getGameWorld().getBaseBombardmentDamage(), true, true, aPlanet.getResistance());
	    		
	    		if (defendingPlayer !=  null){
	    			defendingPlayer.addToGeneral("While bombarding your planet " + aPlanet.getName() + " Governor " + attackingPlayer.getGovenorName() + "'s bombardment have attacked your troop " + bombardedTroop.getUniqueName() + " with the effect: " + returnString);
	    			attackingPlayer.addToGeneral("While bombarding the planet " + aPlanet.getName() + " belonging to Governor " + defendingPlayer.getGovenorName() + " (" + defendingPlayer.getFaction().getName() + ") your bombardment have attacked his troop " + bombardedTroop.getTroopType().getUniqueName() + " with the effect: " + returnString);
	    		}else{
	    			attackingPlayer.addToGeneral("While bombarding the neutral planet " + aPlanet.getName() + " your bombardment have attacked a troop " + bombardedTroop.getTroopType().getUniqueName() + " with the effect: " + returnString);
	    		}
	    		
	    		if(bombardedTroop.isDestroyed()){
	    			defendingTroops.remove(randomIndex);
	    			g.removeTroop(bombardedTroop);
	    		}
    		}
    		performedBombardments++;
    	}
    }
    
    public TaskForce performCombat(TaskForce tf1, TaskForce tf2, Planet aPlanet){
      Logger.fine(aPlanet.getName());
      boolean neutral = false;
      Player notNeutralPlayer = null;
      TaskForce notNeutralTaskForce = null; 
//      TaskForce neutralTaskForce = null;
      Player player1 = tf1.getPlayer();
      Player player2 = tf2.getPlayer();
      // hantera neutrala tf:s...
      if (player1 == null){
        notNeutralPlayer = player2;
        notNeutralTaskForce = tf2;
//        neutralTaskForce = tf1;
        neutral = true;
      }else
      if (player2 == null){
        notNeutralPlayer = player1;
        notNeutralTaskForce = tf1;
//        neutralTaskForce = tf2;
        neutral = true;
      }
      SpaceBattleReport report = new SpaceBattleReport(aPlanet); 
      // initiala meddelanden, även räkna upp styrkorna eller kanske bara antalet skepp på varje sida?
      if (!neutral){
        player1.addToGeneral("Your forces at " + aPlanet.getName() + " have engaged hostile forces from governor " + player2.getGovenorName() + ".");
        player2.addToGeneral("Your forces at " + aPlanet.getName() + " have engaged hostile forces from player " + player1.getGovenorName() + ".");
      }else{
        notNeutralPlayer.addToGeneral("Your forces have engaged neutral forces at " + aPlanet.getName() + ".");
      }
      report.setInitialForces1(tf1);
      report.setInitialForces2(tf2);
      Logger.finer("Fighting starts");
      String tf1status = "fighting";
      String tf2status = "fighting";
      Random r = Functions.getRandom();
      int shootingSide = 0;
      Spaceship firingShip = null;
//      String oldStatus = "";
      // loopa tills ena sidan är "gone"
      while ((!tf1status.equalsIgnoreCase("destroyed")) & (!tf2status.equalsIgnoreCase("destroyed")) & (!tf1status.equalsIgnoreCase("ran away")) & (!tf2status.equalsIgnoreCase("ran away"))){
      	Logger.finest("In battle loop: " + tf1status + " " + tf2status); 
      	AttackReportSpace attackReport = new AttackReportSpace();
        shootingSide = getShootingSide(tf1,tf2,r);
        if (shootingSide == 1){
          firingShip = tf1.getFiringShip(tf2,r,attackReport); // returnerar null om ett skepp flyr istället för att skjuta
          if (firingShip != null){
        	  Logger.finest("firingShip: " + firingShip.getName() + " ");
          }else{
        	  Logger.finest("firingShip == null ");
          }
          tf1status = tf1.getStatus();
          if (firingShip != null){ // tf2 är beskjutet
            tf2status = tf2.shipHit(tf1,firingShip,r,attackReport);
          }else{   // om inget skepp returneras betyder det att tf1 håller på att retirera
            tf2status = "fighting";
          }
        }else{
          firingShip = tf2.getFiringShip(tf1,r,attackReport);
          tf2status = tf2.getStatus();
          if (firingShip != null){ // tf1 är beskjutet
            tf1status = tf1.shipHit(tf2,firingShip,r,attackReport);
          }else{   // om inget skepp returneras betyder det att tf2 håller på att retirera
            tf1status = "fighting";
          }
          // räkna hur många skepp som kommer undan?
        }
        report.addAttackResult(attackReport);
      }
      // vem vann?
      TaskForce tfWinner = tf2;
      TaskForce tfLoser = tf1;
      Logger.finer("tf2.getNrShips(): " + tf2.getTotalNrShips());
      Logger.finer("tf1.getNrShips(): " + tf1.getTotalNrShips());
      if (tf1.getTotalNrShips() > 0){
      	Logger.finer("tf1.getNrShips(): " + tf1.getTotalNrShips());
        tfWinner = tf1;
        tfLoser = tf2;
      }
      report.setPostBattleForces1(tf1);
      report.setPostBattleForces2(tf2);
      if (tfWinner.getPlayer() != null){
          tfWinner.getPlayer().getTurnInfo().addToLatestSpaceBattleReports(report);
          Logger.finer("tfWinner.getPlayer().getGovenorName(): " + tfWinner.getPlayer().getGovenorName());
      }else{
          Logger.finer("tfWinner is neutral");
      }
      if (tfLoser.getPlayer() != null){
          tfLoser.getPlayer().getTurnInfo().addToLatestSpaceBattleReports(report);
          Logger.finer("tfLoser.getPlayer().getGovenorName(): " + tfLoser.getPlayer().getGovenorName());
      }else{
    	  Logger.finer("tfLoser is neutral");
      }
      tfWinner.addToLatestGeneralReport("Your forces has won a glorious victory.");
      tfLoser.addToLatestGeneralReport("Your forces has lost the battle.");
      if (tfLoser.getRetreatedShips().size() == 0){
      	// the loser dig not retreat with any ships (all destroyed)
      	if (tfWinner.getPlayer() != null){
      		tfWinner.getPlayer().addToHighlights(aPlanet.getName(),HighlightType.TYPE_BATTLE_WON);
      	}
      	if (tfLoser.getPlayer() != null){
      		tfLoser.getPlayer().addToHighlights(aPlanet.getName(),HighlightType.TYPE_BATTLE_LOST);
      	}
      }else
      if (tfLoser.getDestroyedShips().size() == 0){
      	// the loser did not lose any ships (all retreated)
      	if (tfWinner.getPlayer() != null){
      		tfWinner.getPlayer().addToHighlights(aPlanet.getName(),HighlightType.TYPE_RETREAT_IN_COMBAT_ENEMY);
      	}
      	if (tfLoser.getPlayer() != null){
      		tfLoser.getPlayer().addToHighlights(aPlanet.getName(),HighlightType.TYPE_RETREAT_IN_COMBAT_OWN);
      	}
      }else{
      	// else = partial retreat
      	if (tfWinner.getPlayer() != null){
      		tfWinner.getPlayer().addToHighlights(aPlanet.getName(),HighlightType.TYPE_BATTLE_WON_PARTIAL_RETREAT);
      	}
      	if (tfLoser.getPlayer() != null){
      		tfLoser.getPlayer().addToHighlights(aPlanet.getName(),HighlightType.TYPE_BATTLE_LOST_PARTIAL_RETREAT);
      	}
      }
      tfWinner.addToLatestGeneralReport("");
      tfLoser.addToLatestGeneralReport("");
      Logger.finer("tfLoser.getStatus(): " + tfLoser.getStatus());

      // mera detaljerat för battle report
      if (tfLoser.getStatus().equalsIgnoreCase("destroyed")){
    	report.setLastShipDestroyed(true);
      }else{
   	    report.setLastShipDestroyed(false);
      }
      tfWinner.restoreAllShields(); // nollställ även variabler... antal förstörda
      // reload winning sides squadrons if they have a carrierLocation
      tfWinner.reloadSquadrons();
      // check if any governor from same planet as attacker is on planet
      if (neutral){
      	checkGovOnNeutralPlanet(aPlanet,notNeutralTaskForce); 
      }
      Logger.finer("performCombat finished");
      return tfLoser;
    }

//    private String shipNames(List<Spaceship> ships){
//      String allNames = "";
//      for(int i = 0; i < ships.size(); i++){
//        Spaceship tempss = ships.get(i);
//        if (!allNames.equalsIgnoreCase("")){
//          allNames = allNames + ", ";
//        }
//        allNames = allNames + tempss.getName();
//      }
//      return allNames;
//    }

//    private String shiptypeNames(List<Spaceship> ships){
//      String allNames = "";
//      for(int i = 0; i < ships.size(); i++){
//        SpaceshipType tempsst = (ships.get(i)).getSpaceshipType();
//        if (!allNames.equalsIgnoreCase("")){
//          allNames = allNames + ", ";
//        }
//        allNames = allNames + tempsst.getName();
//      }
//      return allNames;
//    }

    // chansen för att den ena tf:en får skjuta baseras på antalet skepp i resp. flotta samt initiativbonus
    // Generaler, Jedis etc kanske kan öka chansen att få skjuta?
    protected int getShootingSide(TaskForce tf1, TaskForce tf2, Random r){
      int returnValue = 0;
//      double tf1ratio = (tf1.getNrFirstLineShips()*1.0) / ((tf1.getNrFirstLineShips() + tf2.getNrFirstLineShips()*1.0));
      double tf1ratio = getInitRatio(tf1,tf2,r);
//      double tf1ratio = (tf1.getRelativeSize()*1.0) / ((tf1.getRelativeSize() + tf2.getRelativeSize()*1.0));
//      int tf1initBonus = tf1.getTotalInitBonus() - tf2.getTotalInitBonus();
      int tf1initBonus = getInitBonusTotal(tf1,tf2);
      Logger.finer("tf1initbonus: " + tf1initBonus + " ratio: " + tf1ratio);
      double randomDouble = r.nextDouble();
      if (tf1initBonus > 0){ // increase chance of initiative
        tf1ratio = tf1ratio + ((1.0-tf1ratio)*(tf1initBonus/100.0));
      }else
      if (tf1initBonus < 0){ // decrease chance of initiative
        tf1ratio = tf1ratio*(1.0+(tf1initBonus/100.0));
      }
      Logger.finer("tf1ratio (inc bonuses) --> " + tf1ratio + " randomDouble: " + randomDouble);
      if (tf1ratio > randomDouble){
        returnValue = 1;
      }
      //return Math.abs(r.nextInt()%2) + 1;
      return returnValue;
    }

    protected double getInitRatio(TaskForce tf1, TaskForce tf2, Random r){
    	InitiativeMethod initMethod = g.getGameWorld().getInitMethod();
    	double tf1ratio = 0.0d;
    	if (initMethod == InitiativeMethod.WEIGHTED){
    		Logger.finer("tf1.getRelativeSize(): " + tf1.getRelativeSize());
    		Logger.finer("tf2.getRelativeSize(): " + tf2.getRelativeSize());
//    		tf1ratio = (tf1.getRelativeSize()*1.0) / ((tf1.getRelativeSize() + tf2.getRelativeSize()*1.0));
    		tf1ratio = getWeightedRatio(0,tf1.getRelativeSize(),tf2.getRelativeSize());
    	}else
       	if (initMethod == InitiativeMethod.WEIGHTED_1){
       		tf1ratio = getWeightedRatio(1,tf1.getRelativeSize(),tf2.getRelativeSize());
       	}else
       	if (initMethod == InitiativeMethod.WEIGHTED_2){
       		tf1ratio = getWeightedRatio(2,tf1.getRelativeSize(),tf2.getRelativeSize());
       	}else
       	if (initMethod == InitiativeMethod.WEIGHTED_3){
       		tf1ratio = getWeightedRatio(3,tf1.getRelativeSize(),tf2.getRelativeSize());
       	}else
    	if (initMethod == InitiativeMethod.LINEAR){
    		tf1ratio = (tf1.getTotalNrFirstLineShips()*1.0) / ((tf1.getTotalNrFirstLineShips() + tf2.getTotalNrFirstLineShips()*1.0));
    	}else
       	if (initMethod == InitiativeMethod.FIFTY_FIFTY){
       		tf1ratio = 0.5d;
       	}
    	return tf1ratio;
    }
    
    protected double getWeightedRatio(double base, double tf1RelSize, double tf2RelSize){
    	double tf1RelSizeMod = tf1RelSize + base;
    	double tf2RelSizeMod = tf2RelSize + base;
		double tf1ratio = tf1RelSizeMod / (tf1RelSizeMod + tf2RelSizeMod);
		return tf1ratio;
    }
    
    protected int getInitBonusTotal(TaskForce tf1, TaskForce tf2){
      int tf1initBonus = tf1.getTotalInitBonus() - tf2.getTotalInitDefence();
      if (tf1initBonus < 0){
      	tf1initBonus = 0;
      }
      int tf2initBonus = tf2.getTotalInitBonus() - tf1.getTotalInitDefence();
      if (tf2initBonus < 0){
      	tf2initBonus = 0;
      }
      return tf1initBonus - tf2initBonus;
    }
    
}