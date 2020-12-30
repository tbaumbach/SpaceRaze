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
import java.util.Optional;
import java.util.stream.Collectors;

import spaceraze.battlehandler.spacebattle.SpaceBattlePerformer;
import spaceraze.battlehandler.spacebattle.TaskForceHandler;
import spaceraze.server.game.StartGameHandler;
import spaceraze.server.game.update.BlackMarketPerformer;
import spaceraze.server.game.update.CheckAbandonedSquadrons;
import spaceraze.server.game.update.OrdersPerformer;
import spaceraze.servlethelper.game.DiplomacyMutator;
import spaceraze.servlethelper.game.DiplomacyPureFunctions;
import spaceraze.servlethelper.game.troop.TroopPureFunctions;
import spaceraze.servlethelper.game.vip.VipMutator;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
import spaceraze.servlethelper.game.expenses.ExpensePureFunction;
import spaceraze.servlethelper.game.planet.PlanetMutator;
import spaceraze.servlethelper.game.planet.PlanetOrderStatusPureFunctions;
import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.servlethelper.game.player.CostPureFunctions;
import spaceraze.servlethelper.game.player.IncomePureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.servlethelper.map.MapPureFunctions;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.diplomacy.DiplomacyLevel;
import spaceraze.world.diplomacy.DiplomacyState;
import spaceraze.world.enums.DiplomacyGameType;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.enums.SpaceShipSize;
import spaceraze.world.orders.Orders;
import spaceraze.world.report.PlanetReport;
import spaceraze.world.report.PlayerReport;
import spaceraze.battlehandler.spacebattle.TaskForce;
import sr.message.MessageDatabase;
import sr.server.persistence.PHash;
import spaceraze.util.properties.RankingHandler;
import sr.webb.mail.MailHandler;

public class GalaxyUpdater {
    protected Galaxy g;

    public GalaxyUpdater(Galaxy g) {
        this.g = g;
    }

    public void performUpdate(SR_Server aSR_Server) throws Exception {
        try {
            Logger.info("Update started");
            g.setLastUpdateComplete(false);
            Logger.info("g.setLastUpdateComplete(false);");
            if (g.gameEnded) {
                // spelet skall ej uppdateras
                Logger.info("Game is over. Galaxy not updated.");
            } else
                // om det är det första draget, innan spelet har börjat
                if (g.turn == 0) {
                    // antal startplaneter
                    StartGameHandler startGameHandler = new StartGameHandler();
                    startGameHandler.setStartPlanets(this);
                    Logger.info("First turn.");
                    DiplomacyMutator.setPlayerDiplomacy(g.getDiplomacyGameType(), g.getDiplomacyStates());
                    for (Player player : g.players) {
                        player.setOrders(new Orders());
                        player.getPlayerReports().add(new PlayerReport());
                        //TODO 2020-12-01 addFirstTurnMessages should be put the message in to the PlayerReport
                        addFirstTurnMessages(player, aSR_Server.getMessageDatabase(), g.getGameWorld());
                        player.setPlanetInformations(PlanetMutator.createPlayerStartPlanetInformations(g.getPlanets(), player));
                        // add start income to income report
                        IncomePureFunctions.getPlayerIncomeWithoutCorruption(player, true, player.getGalaxy());
                    }
                    updateMapPlanetInfos();
                    // add statistics for first turn
                    StatisticsUpdater.performStatistics(g);
                    g.turn++;
                    g.setLastUpdated(new Date());
                } else { // update galaxy
                    Logger.info("Update galaxy");
                    // update reports
                    for (Player player : g.players) {
                        // 2019-12-30 new logic reports
                        player.getPlayerReports().add(new PlayerReport());

                        player.updateTurnInfo();
                        player.setUpdatedThisTurn(false);
                        player.setFinishedThisTurn(false);
                        player.addToGeneral("Game has been updated to turn " + (g.turn + 1) + ".");
                        player.addToGeneral("");
                    }
                    // add last turn economy data to economy report
                    updateEconomyReport1();
                    // check if any players wish to abandon the game
                    checkAbandonGame();
                    if (g.playersLeft()) {
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
                        (new CheckAbandonedSquadrons(g)).checkAbandonedSquadrons();
                        // check destruction of civilian ships
                        checkCivilianShips();
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
                        DiplomacyMutator.resetDiplomacyStates(g.getDiplomacyStates());
                        // check if govenor are on a retreating ship
                        checkRetreatingGovenor();
                        // add next turn economy data to economy report
                        updateEconomyReport2();
                        // check if any players lose the game due to being broke for more than 5 turns
                        checkRepeatedBroke();
                        // statistics
                        StatisticsUpdater.performStatistics(g);
                        Logger.info("Galaxy updated.");

                        // check if game is over
                        if (allPlanetsRazedAndUninfected()) {
                            Logger.info("... all planets RAZED!!!");
                            for (int x = 0; x < g.players.size(); x++) {
                                Player temp = (Player) g.players.get(x);
                                Logger.info("Player defeated due to razed planets");
                                rankingLoss(temp.getName(), false);
                                temp.addToGeneralAt("All planets in this sector Razed!", 4);
                                temp.addToGeneralAt("There is no winner.", 5);
                                temp.addToGeneralAt("Game have ended.", 6);
                                temp.addToGeneralAt("", 7);
                                temp.addToHighlights("All planets in this sector Razed!", HighlightType.TYPE_SPECIAL_1);
                                temp.addToHighlights("There is no winner.", HighlightType.TYPE_SPECIAL_2);
                                temp.addToHighlights("Game have ended", HighlightType.TYPE_SPECIAL_3);
                            }
                            g.gameEnded = true;
                            PHash.incCounter("game.finished.total");
                            PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
                            PHash.incCounter("game.finished.map." + g.getMapFileName());
                            PHash.incCounter("game.finished.allrazed");
                        } else   // one player remains and is the winner of the game
                            if (g.checkSoloPlayerWinner() != null) {
                                Logger.info("... and we have a (solo) WINNER!");
                                Player winner = g.checkSoloPlayerWinner();
                                for (int x = 0; x < g.players.size(); x++) {
                                    Player temp = (Player) g.players.get(x);
                                    Logger.info("Solo win, loop player: " + temp.getName());
                                    if (temp != winner) {
                                        Logger.info("Player defeated due to solo remaining player");
                                        rankingLoss(temp.getName(), false);
                                        temp.addToGeneralAt("Player " + winner.getName() + " (Governor " + winner.getGovernorName() + ") is the only player left and has won the game.", 4);
                                        temp.addToGeneralAt("Game have ended.", 5);
                                        temp.addToGeneralAt("", 6);
                                        temp.addToHighlights(winner.getName() + " have won the game!", HighlightType.TYPE_SPECIAL_1);
                                        temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                    } else {
                                        temp.setWin(true);
                                        updateWinRanking(temp, true);
                                        temp.addToGeneralAt("You have won the game!!!!", 4);
                                        temp.addToGeneralAt("Only you remain in the game and are the ruler of this sector (or what remains of it...).", 5);
                                        temp.addToGeneralAt("Game have ended.", 6);
                                        temp.addToGeneralAt("", 7);
                                        temp.addToHighlights("You have won the game!", HighlightType.TYPE_SPECIAL_1);
                                        temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                    }
                                }
                                g.gameEnded = true;
                                PHash.incCounter("game.finished.total");
                                PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
                                PHash.incCounter("game.finished.map." + g.getMapFileName());
                                PHash.incCounter("game.finished.singlewin." + g.getGameWorld().getFileName() + "." + winner.getFaction().getName());
                            } else // only one faction remains and wins the game
//				if (g.checkSoloFactionWinner() != null){
                                if (checkSoloConfederacyWinner(g)) {
                                    Logger.info("... and we have a winning confederacy!");
//					Faction faction = g.checkSoloFactionWinner();
                                    List<Player> confPlayers = g.getSoloConfederacyWinner();
                                    for (int x = 0; x < g.players.size(); x++) {
                                        Player temp = (Player) g.players.get(x);
                                        Logger.info("Single confederacy win, loop player: " + temp.getName());
//						if (!faction.isFaction(temp.getFaction().getName())){
                                        if (temp.isDefeated()) {
                                            Logger.info("Player defeated due to only remaining confederacy");
                                            rankingLoss(temp.getName(), false);
                                            temp.addToGeneralAt("You have lost the game!", 4);
                                            temp.addToGeneralAt("Only one victorious confederacy remains, and they have defeated all other players and won the game.", 5);
                                            temp.addToGeneralAt("Game have ended.", 6);
                                            temp.addToGeneralAt("", 7);
                                            temp.addToHighlights("Last remaining confederacy have won the game!", HighlightType.TYPE_SPECIAL_1);
                                            temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                        } else {
                                            temp.setWin(true);
                                            updateWinRankingConfederacy(temp, confPlayers);
                                            temp.addToGeneralAt("Your confederacy have won the game!!!!", 4);
                                            temp.addToGeneralAt("Only your confederacy remain in the game and are the ruler of this sector (or what remains of it...).", 5);
                                            temp.addToGeneralAt("Game have ended.", 6);
                                            temp.addToGeneralAt("", 7);
                                            temp.addToHighlights("Your confederacy have won the game!", HighlightType.TYPE_SPECIAL_1);
                                            temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                        }
                                    }
                                    g.gameEnded = true;
                                    PHash.incCounter("game.finished.total");
                                    PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
                                    PHash.incCounter("game.finished.map." + g.getMapFileName());
                                    PHash.incCounter("game.finished.confederacywin." + g.getGameWorld().getFileName());
                                } else // one player has at least XX% of all pop in the sector and wins the game
                                    if (g.checkWinningPlayer() != null) {
                                        Logger.info("... and we have a (XX% prod) WINNER!");
                                        Player winner = g.checkWinningPlayer();
                                        for (int x = 0; x < g.players.size(); x++) {
                                            Player temp = (Player) g.players.get(x);
                                            if (temp != winner) {
                                                if ((temp.getFaction() == winner.getFaction()) & !temp.isDefeated()) {
                                                    // since player is on the same faction as the winner he gets a faction win without any
                                                    // bonus ranking points for defeated opponents
                                                    Logger.info("Player on same faction as " + g.getSingleVictory() + "% win player");
                                                    temp.setWin(true);
                                                    rankingWin(0, temp.getName(), false);
                                                    temp.addToGeneralAt(winner.getName() + " have won the game!", 4);
                                                    temp.addToGeneralAt(winner.getName() + " control more than 65% of the total population in this sector", 5);
                                                    temp.addToGeneralAt("Since you are on the same faction you are awarded a cooperative victory!", 6);
                                                    temp.addToGeneralAt("Game have ended.", 7);
                                                    temp.addToGeneralAt("", 8);
                                                    temp.addToHighlights("You are on the winning side!", HighlightType.TYPE_SPECIAL_1);
                                                    temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                                } else {
                                                    Logger.info("Player defeated due to " + g.getSingleVictory() + "% domination player");
                                                    rankingLoss(temp.getName(), !temp.isDefeated());
                                                    temp.setDefeated(true);
                                                    temp.addToGeneralAt("You have lost the game!", 4);
                                                    temp.addToGeneralAt("Player " + winner.getName() + " (Governor " + winner.getGovernorName() + ") has control over more than " + g.getSingleVictory() + "% of all population in this sector and has won the game.", 5);
                                                    temp.addToGeneralAt("Game have ended.", 6);
                                                    temp.addToGeneralAt("", 7);
                                                    temp.addToHighlights(winner.getName() + " have won the game!", HighlightType.TYPE_SPECIAL_1);
                                                    temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                                }
                                            } else {
                                                temp.setWin(true);
                                                updateWinRanking(temp, true);
                                                temp.addToGeneralAt("You have won the game!!!!", 4);
                                                temp.addToGeneralAt("You control more than " + g.getSingleVictory() + "% of the total population in this sector and are proclaimed ruler of this sector (or what remains of it...).", 5);
                                                temp.addToGeneralAt("Game have ended.", 6);
                                                temp.addToGeneralAt("", 7);
                                                temp.addToHighlights("You have won the game!", HighlightType.TYPE_SPECIAL_1);
                                                temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                            }
                                        }
                                        g.gameEnded = true;
                                        PHash.incCounter("game.finished.total");
                                        PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
                                        PHash.incCounter("game.finished.map." + g.getMapFileName());
                                        PHash.incCounter("game.finished.singlewin." + g.getGameWorld().getFileName() + "." + winner.getFaction().getName());
                                    } else // one faction controls at least XX% of all pop in sector and wins the game
                                        if ((g.getDiplomacyGameType() == DiplomacyGameType.FACTION) && (g.checkWinningFaction() != null)) {
                                            Logger.info("... and we have a (faction) WINNER!");
                                            Faction winner = g.checkWinningFaction();
                                            for (int x = 0; x < g.players.size(); x++) {
                                                Player temp = (Player) g.players.get(x);
                                                if (temp.getFaction() != winner) {
                                                    Logger.info("Player defeated due to " + g.getFactionVictory() + "% domination faction");
                                                    rankingLoss(temp.getName(), !temp.isDefeated());
                                                    temp.setDefeated(true);
                                                    temp.addToGeneralAt("You have lost the game!", 4);
                                                    temp.addToGeneralAt("The " + winner.getName() + " faction has control over more than " + g.getFactionVictory() + "% of the total population in this sector and the", 5);
                                                    temp.addToGeneralAt("members of the " + winner.getName() + " faction has won the game together.", 6);
                                                    temp.addToGeneralAt("Game have ended.", 7);
                                                    temp.addToGeneralAt("", 8);
                                                } else {
                                                    temp.setWin(true);
                                                    updateWinRanking(temp, false);
                                                    temp.addToGeneralAt("Shared Victory!", 4);
                                                    temp.addToGeneralAt("The " + winner.getName() + " faction has control over more than " + g.getFactionVictory() + "% of the total population in this sector and you", 5);
                                                    temp.addToGeneralAt("and the other members of the " + winner.getName() + " faction has won the game together.", 6);
                                                    temp.addToGeneralAt("Game have ended.", 7);
                                                    temp.addToGeneralAt("", 8);
                                                }
                                                temp.addToHighlights("The " + winner.getName() + " faction have won the game!", HighlightType.TYPE_SPECIAL_1);
                                                temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                            }
                                            g.gameEnded = true;
                                            PHash.incCounter("game.finished.total");
                                            PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
                                            PHash.incCounter("game.finished.map." + g.getMapFileName());
                                            PHash.incCounter("game.finished.factionwin." + g.getGameWorld().getFileName() + "." + winner.getName());
                                        } else // one confederacy have at least XX% production
                                            if (((g.getDiplomacyGameType() == DiplomacyGameType.OPEN) | (g.getDiplomacyGameType() == DiplomacyGameType.GAMEWORLD)) && (checkWinningConfederacy(g) != null)) {
                                                Logger.info("... and we have a (confederacy) WINNER!");
                                                List<Player> winnerConf = checkWinningConfederacy(g);
                                                for (Player aPlayer : g.players) {
                                                    if (!winnerConf.contains(aPlayer) | aPlayer.isDefeated()) {
                                                        Logger.info("Player defeated due to " + g.getFactionVictory() + "% domination confederacy");
                                                        rankingLoss(aPlayer.getName(), !aPlayer.isDefeated());
                                                        aPlayer.setDefeated(true);
                                                        aPlayer.addToGeneralAt("You have lost the game!", 4);
                                                        aPlayer.addToGeneralAt("A confederacy has control over more than " + g.getFactionVictory() + "% of the total production in this sector and the", 5);
                                                        aPlayer.addToGeneralAt("members of the confederacy has won the game together.", 6);
                                                        aPlayer.addToGeneralAt("Game have ended.", 7);
                                                        aPlayer.addToGeneralAt("", 8);
                                                    } else {
                                                        aPlayer.setWin(true);
                                                        updateWinRankingConfederacy(aPlayer, winnerConf);
                                                        aPlayer.addToGeneralAt("Shared Victory!", 4);
                                                        aPlayer.addToGeneralAt("Your confederacy have control over more than " + g.getFactionVictory() + "% of the total production in this sector and you", 5);
                                                        aPlayer.addToGeneralAt("and the other members of the confederacy have won the game together.", 6);
                                                        aPlayer.addToGeneralAt("Game have ended.", 7);
                                                        aPlayer.addToGeneralAt("", 8);
                                                    }
                                                    aPlayer.addToHighlights("A confederacy of players have won the game!", HighlightType.TYPE_SPECIAL_1);
                                                    aPlayer.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                                }
                                                g.gameEnded = true;
                                                PHash.incCounter("game.finished.total");
                                                PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
                                                PHash.incCounter("game.finished.map." + g.getMapFileName());
                                                PHash.incCounter("game.finished.confederacywin." + g.getGameWorld().getFileName());
                                            } else // one Lord controls at least XX% of all pop in sector and wins the game
                                                if (((g.getDiplomacyGameType() == DiplomacyGameType.OPEN) | (g.getDiplomacyGameType() == DiplomacyGameType.GAMEWORLD)) && (checkWinningLord(g) != null)) {
                                                    Logger.info("... and we have a (Lord) WINNER!");
                                                    List<Player> winnerLord = checkWinningLord(g);
                                                    for (Player aPlayer : g.players) {
                                                        if (!winnerLord.contains(aPlayer) | aPlayer.isDefeated()) {
                                                            Logger.info("Player defeated due to " + g.getFactionVictory() + "% domination Lord");
                                                            rankingLoss(aPlayer.getName(), !aPlayer.isDefeated());
                                                            aPlayer.setDefeated(true);
                                                            aPlayer.addToGeneralAt("You have lost the game!", 4);
                                                            aPlayer.addToGeneralAt("A Lord has control over more than " + g.getFactionVictory() + "% of the total production in this sector and", 5);
                                                            aPlayer.addToGeneralAt("have won the game.", 6);
                                                            aPlayer.addToGeneralAt("Game have ended.", 7);
                                                            aPlayer.addToGeneralAt("", 8);
                                                        } else {
                                                            if (winnerLord.get(0) == aPlayer) {
                                                                aPlayer.setWin(true);
                                                                updateWinRankingLord(aPlayer);
                                                                aPlayer.addToGeneralAt("You have won the game!", 4);
                                                                aPlayer.addToGeneralAt("Your and your vassals have control over more than " + g.getFactionVictory() + "% of the total production in this sector and you", 5);
                                                                aPlayer.addToGeneralAt("have won the game.", 6);
                                                                aPlayer.addToGeneralAt("Game have ended.", 7);
                                                                aPlayer.addToGeneralAt("", 8);
                                                            } else { // vassal on the winning side
                                                                rankingWinVassal(aPlayer.getName());
                                                                aPlayer.addToGeneralAt("Your Lord have won the game!", 4);
                                                                aPlayer.addToGeneralAt("Your lord and his vassals have control over more than " + g.getFactionVictory() + "% of the total production in this sector and you", 5);
                                                                aPlayer.addToGeneralAt("are on the winning side.", 6);
                                                                aPlayer.addToGeneralAt("Game have ended.", 7);
                                                                aPlayer.addToGeneralAt("", 8);
                                                            }
                                                        }
                                                        aPlayer.addToHighlights("A confederacy of players have won the game!", HighlightType.TYPE_SPECIAL_1);
                                                        aPlayer.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                                    }
                                                    g.gameEnded = true;
                                                    PHash.incCounter("game.finished.total");
                                                    PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
                                                    PHash.incCounter("game.finished.map." + g.getMapFileName());
                                                    PHash.incCounter("game.finished.confederacywin." + g.getGameWorld().getFileName());
                                                } else if (g.getEndTurn() > 0 && g.getEndTurn() <= g.getTurn()) { //maximum turns.
                                                    List<Faction> largestFactions = g.getLargestFactions();
                                                    String winnerString = "";

                                                    if (largestFactions.size() > 1) { // Shared Victory.
                                                        for (Faction faction : largestFactions) {
                                                            if (winnerString.equals("")) {
                                                                winnerString = faction.getName();
                                                            } else {
                                                                winnerString += ", " + faction.getName();
                                                            }
                                                        }

                                                        for (int x = 0; x < g.players.size(); x++) {
                                                            Player temp = (Player) g.players.get(x);
                                                            if (!largestFactions.contains(temp.getFaction())) {
                                                                Logger.info("Player defeated due to max");
                                                                rankingLoss(temp.getName(), !temp.isDefeated());
                                                                temp.setDefeated(true);
                                                                temp.addToGeneralAt("You have lost the game!", 4);
                                                                temp.addToGeneralAt("The " + winnerString + " factions has control most of the total population in this sector and the", 5);
                                                                temp.addToGeneralAt("members of the " + winnerString + " factions has won the game together.", 6);
                                                                temp.addToGeneralAt("Game have ended.", 7);
                                                                temp.addToGeneralAt("", 8);
                                                            } else {
                                                                temp.setWin(true);
                                                                updateWinRanking(temp, false);
                                                                temp.addToGeneralAt("Shared Victory!", 4);
                                                                temp.addToGeneralAt("The " + winnerString + " factions has control most of the total population in this sector and you", 5);
                                                                temp.addToGeneralAt("and the other members of the " + winnerString + " factions has won the game together.", 6);
                                                                temp.addToGeneralAt("Game have ended.", 7);
                                                                temp.addToGeneralAt("", 8);
                                                            }
                                                            temp.addToHighlights("The " + winnerString + " factions have won the game!", HighlightType.TYPE_SPECIAL_1);
                                                            temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                                        }
                                                        g.gameEnded = true;
                                                        PHash.incCounter("game.finished.total");
                                                        PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
                                                        PHash.incCounter("game.finished.map." + g.getMapFileName());
                                                        PHash.incCounter("game.finished.factionwin." + g.getGameWorld().getFileName() + "." + winnerString);

                                                    } else {// one faction win.  checking if singel player win or fatcion win.
                                                        if (g.getFactionMember(largestFactions.get(0)).size() > 1) {// Shared Victory.
                                                            for (int x = 0; x < g.players.size(); x++) {
                                                                Player temp = (Player) g.players.get(x);
                                                                if (!largestFactions.contains(temp.getFaction())) {
                                                                    Logger.info("Player defeated due to max turn");
                                                                    rankingLoss(temp.getName(), !temp.isDefeated());
                                                                    temp.setDefeated(true);
                                                                    temp.addToGeneralAt("You have lost the game!", 4);
                                                                    temp.addToGeneralAt("The " + winnerString + " faction has control most of the total population in this sector and the", 5);
                                                                    temp.addToGeneralAt("members of the " + largestFactions.get(0).getName() + " faction has won the game together.", 6);
                                                                    temp.addToGeneralAt("Game have ended.", 7);
                                                                    temp.addToGeneralAt("", 8);
                                                                } else {
                                                                    temp.setWin(true);
                                                                    updateWinRanking(temp, false);
                                                                    temp.addToGeneralAt("Shared Victory!", 4);
                                                                    temp.addToGeneralAt("The " + largestFactions.get(0).getName() + " faction has control most of the total population in this sector and you", 5);
                                                                    temp.addToGeneralAt("and the other members of the " + largestFactions.get(0).getName() + " faction has won the game together.", 6);
                                                                    temp.addToGeneralAt("Game have ended.", 7);
                                                                    temp.addToGeneralAt("", 8);
                                                                }
                                                                temp.addToHighlights("The " + largestFactions.get(0).getName() + " faction have won the game!", HighlightType.TYPE_SPECIAL_1);
                                                                temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                                            }
                                                            g.gameEnded = true;
                                                            PHash.incCounter("game.finished.total");
                                                            PHash.incCounter("game.finished.gameworld." + g.getGameWorld().getFileName());
                                                            PHash.incCounter("game.finished.map." + g.getMapFileName());
                                                            PHash.incCounter("game.finished.factionwin." + g.getGameWorld().getFileName() + "." + largestFactions.get(0).getName());
                                                        } else {// one player win
                                                            Player winner = g.getFactionMember(largestFactions.get(0)).get(0);
                                                            for (int x = 0; x < g.players.size(); x++) {
                                                                Player temp = (Player) g.players.get(x);
                                                                if (!largestFactions.contains(temp.getFaction())) {

                                                                    Logger.info("Player defeated due to max turn");
                                                                    rankingLoss(temp.getName(), !temp.isDefeated());
                                                                    temp.setDefeated(true);
                                                                    temp.addToGeneralAt("You have lost the game!", 4);
                                                                    temp.addToGeneralAt("The player " + winnerString + "(Governor " + winner.getGovernorName() + ") control most of the total population in this sector", 5);
                                                                    temp.addToGeneralAt("and has won the game.", 6);
                                                                    temp.addToGeneralAt("Game have ended.", 7);
                                                                    temp.addToGeneralAt("", 8);
                                                                    temp.addToHighlights(winner.getName() + " have won the game!", HighlightType.TYPE_SPECIAL_1);
                                                                    temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
                                                                } else {
                                                                    temp.setWin(true);
                                                                    updateWinRanking(temp, true);
                                                                    temp.addToGeneralAt("You have won the game!!!!", 4);
                                                                    temp.addToGeneralAt("You control most of the total population in this sector and are proclaimed ruler of this sector (or what remains of it...).", 5);
                                                                    temp.addToGeneralAt("Game have ended.", 6);
                                                                    temp.addToGeneralAt("", 7);
                                                                    temp.addToHighlights("You have won the game!", HighlightType.TYPE_SPECIAL_1);
                                                                    temp.addToHighlights("Game have ended.", HighlightType.TYPE_SPECIAL_2);
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
                    } else { // no players left, game is over
                        Logger.info("No players left, game is over");
                        for (int x = 0; x < g.players.size(); x++) {
                            Player temp = (Player) g.players.get(x);
                            Logger.info("No players left");
                            rankingLoss(temp.getName(), false);
                            temp.addToGeneralAt("No players left.", 4);
                            temp.addToGeneralAt("There is no winner.", 5);
                            temp.addToGeneralAt("Game have ended.", 6);
                            temp.addToGeneralAt("", 7);
                            temp.addToHighlights("No players left.", HighlightType.TYPE_SPECIAL_1);
                            temp.addToHighlights("There is no winner.", HighlightType.TYPE_SPECIAL_2);
                            temp.addToHighlights("Game have ended", HighlightType.TYPE_SPECIAL_3);
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
                        if (!aPlayer.isDefeated()) {
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
        } catch (Exception e) {
            Logger.severe("Exception caught: " + e.toString());
            StackTraceElement[] stackArray = e.getStackTrace();
            for (int i = 0; i < stackArray.length; i++) {
                Logger.severe(stackArray[i].toString());
            }
            MailHandler.sendErrorToAdmins("Update - " + g.getGameName(), g.getLastLog());
            throw e;
        }
    }

    protected void removeTroopsOnAlliedPlanets() {
        if (g.getGameWorld().isTroopGameWorld()) {
            List<Player> players = g.getPlayers();
            for (Player player : players) {
                List<Troop> troops = TroopPureFunctions.getPlayersTroops(player, g);
                for (Troop troop : troops) {
                    if (troop.getPlanetLocation() != null && troop.getPlanetLocation().isPlayerPlanet() && !troop.getPlanetLocation().getPlayerInControl().equals(player)) {
                        DiplomacyState diplomacyState = DiplomacyPureFunctions.getDiplomacyState(player, troop.getPlanetLocation().getPlayerInControl(), g.getDiplomacyStates());
                        if (diplomacyState.getCurrentLevel().isHigher(DiplomacyLevel.WAR)) {//friendly
                            //remove the troop
                            TroopMutator.addToLatestTroopsLostInSpace(troop, player.getTurnInfo(), g.getGameWorld());
                            player.getTurnInfo().addToLatestGeneralReport("The troop " + troop.getName() + "have be dismissed to avoid conlict with our ally on the planet " + troop.getPlanetLocation().getName() + ".");
                            TroopMutator.removeTroop(troop, g);
                        }

                    }
                }
            }
        }


    }

    protected void checkGroundBattles() {
        if (g.getGameWorld().isTroopGameWorld()) {
            Logger.fine("checkGroundBattles called");
            // leta igenom alla planeter
            List<Planet> planets = g.getPlanets();
            for (Planet planet : planets) {
                LandBattleHelper.troopFight(planet, g);
            }
            Logger.finer("checkGroundBattles finished");
        }
    }

    protected void rankingLoss(String playerLogin, boolean survived) {
        if (g.getranked()) {
            RankingHandler.addPlayerLoss(playerLogin, survived);
        }
    }

    protected void rankingWin(int nrDefeatedOpp, String playerLogin, boolean soloWin) {
        if (g.getranked()) {
            RankingHandler.addPlayerWin(nrDefeatedOpp, playerLogin, soloWin);
        }
    }

    protected void rankingWinVassal(String playerLogin) {
        if (g.getranked()) {
            RankingHandler.addVassalWin(playerLogin);
        }
    }

    /**
     * Check if there are any unguarded civilian ships in the same system as enemy
     * military ships, and if so they are destroyed.
     */
    protected void checkCivilianShips() {
        Logger.finer("checkCivilianShips called");
        // loop through all planets
        for (Planet aPlanet : g.getPlanets()) {
            Logger.finer("aPlanet: " + aPlanet.getName());
            // find all civilian ships at the current planet
            List<Spaceship> civsAtPlanet = SpaceshipPureFunctions.getShips(aPlanet, true, g);
            // for each civilian ship
            for (Spaceship aSpaceship : civsAtPlanet) {
                Logger.finer("aSpaceship: " + aSpaceship.getName());
                // find if there are any friendly military ships in the system
                List<Spaceship> militaryAtPlanet = SpaceshipPureFunctions.getShips(aPlanet, false, g);
                List<Spaceship> friendlyMilitarys = getFriendlyMilitaryShips(aSpaceship, militaryAtPlanet);
                Logger.finest("militaryAtPlanet.size(): " + militaryAtPlanet.size());
                Logger.finest("friendlyMilitarys.size(): " + friendlyMilitarys.size());
                // if there are no friendly ships
                if (friendlyMilitarys.size() == 0) {
                    // get a list over all enemy military ships in the system
                    List<Spaceship> enemyMilitarys = getEnemyMilitaryShips(aSpaceship, militaryAtPlanet);
                    Logger.finer("enemyMilitarys.size(): " + enemyMilitarys.size());
                    // if there are at least one enemy military ship
                    if (enemyMilitarys.size() > 0) {
                        boolean stopRetreats = getStopRetreats(enemyMilitarys);
                        List<Player> enemyPlayers = getEnemyPlayers(enemyMilitarys);
                        if (SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), g.getGameWorld()).isAlwaysRetreat() & !stopRetreats) {
                            boolean gotAway = aSpaceship.isRetreating();
                            Logger.finer("gotAway: " + gotAway);
                            if (gotAway) { // ship have retreated
                                for (Player player : enemyPlayers) {
                                    if (aSpaceship.getOwner() != null) {
                                        player.getTurnInfo().addToLatestCivilianReport("A civilian ship of the type " + SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), getGalaxy().getGameWorld()).getName() + " from govenor " + aSpaceship.getOwner().getName() + " have retreated in the " + aPlanet.getName() + " system.");
                                    } else { // civ ship is neutral
                                        player.getTurnInfo().addToLatestCivilianReport("A neutral civilian ship of the type " + SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), getGalaxy().getGameWorld()).getName() + " have retreated in the " + aPlanet.getName() + " system.");
                                    }
                                    player.getTurnInfo().addToLatestHighlights(aPlanet.getName(), HighlightType.TYPE_ENEMY_CIVILIAN_SHIP_RETREATED);
                                }
                                if (aSpaceship.getOwner() != null) {
                                    aSpaceship.getOwner().getTurnInfo().addToLatestCivilianReport("Your civilian ship " + aSpaceship.getName() + " has retreated from the planet " + aPlanet.getName() + ".");
                                    aSpaceship.getOwner().getTurnInfo().addToLatestHighlights(aPlanet.getName(), HighlightType.TYPE_OWN_CIVILIAN_SHIP_RETREATED);
                                }
                            } else { // ship had nowhere to retreat to, is scuttled
                                for (Player player : enemyPlayers) {
                                    if (aSpaceship.getOwner() != null) {
                                        player.getTurnInfo().addToLatestCivilianReport("A civilian ship of the type " + SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), g.getGameWorld()).getName() + " from govenor " + aSpaceship.getOwner().getName() + " in the " + aSpaceship.getLocation().getName() + " system have been scuttled by it's own crew, when it had nowhere to retreat to.");
                                    } else { // civ ship is neutral
                                        player.getTurnInfo().addToLatestCivilianReport("A neutral civilian ship of the type " + SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), g.getGameWorld()).getName() + " in the " + aSpaceship.getLocation().getName() + " system have been scuttled by it's own crew, when it had nowhere to retreat to.");
                                    }
                                    SpaceshipHelper.addToLatestShipsLostInSpace(aSpaceship, player.getTurnInfo(), g.getGameWorld());
                                    player.getTurnInfo().addToLatestHighlights(SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), getGalaxy().getGameWorld()).getName(), HighlightType.TYPE_ENEMY_CIVILIAN_SHIP_DESTROYED);
                                }
                                if (aSpaceship.getOwner() != null) {
                                    aSpaceship.getOwner().getTurnInfo().addToLatestCivilianReport("Your civilian ship " + aSpaceship.getName() + " has been scuttled in the system " + aSpaceship.getLocation().getName() + " when it had nowhere to retreat to.");
                                    SpaceshipHelper.addToLatestShipsLostInSpace(aSpaceship, aSpaceship.getOwner().getTurnInfo(), g.getGameWorld());
                                    aSpaceship.getOwner().getTurnInfo().addToLatestHighlights(aSpaceship.getName(), HighlightType.TYPE_OWN_CIVILIAN_SHIP_DESTROYED);
                                    VipMutator.checkVIPsInDestroyedShips(aSpaceship, aSpaceship.getOwner(), g);
                                    TroopMutator.checkTroopsInDestroyedShips(aSpaceship, aSpaceship.getOwner(), g);
                                }
                                SpaceshipMutator.removeShip(aSpaceship, g);
                            }
                        } else { // ship is destroyed
                            // add a general message to owner of civilian ship that the ship has been destroyed
                            if (aSpaceship.getOwner() != null) {
                                if (SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), g.getGameWorld()).isAlwaysRetreat() & stopRetreats) {
                                    aSpaceship.getOwner().getTurnInfo().addToLatestCivilianReport("Your civilian ship " + aSpaceship.getName() + " has been destroyed in the system " + aSpaceship.getLocation().getName() + ". It tried to retreat but was stopped by an enemy ship with the stop retreats ability.");
                                } else {
                                    aSpaceship.getOwner().getTurnInfo().addToLatestCivilianReport("Your civilian ship " + aSpaceship.getName() + " has been destroyed in the system " + aSpaceship.getLocation().getName() + ".");
                                }
                                SpaceshipHelper.addToLatestShipsLostInSpace(aSpaceship, aSpaceship.getOwner().getTurnInfo(), g.getGameWorld());
                                aSpaceship.getOwner().getTurnInfo().addToLatestHighlights(aSpaceship.getName(), HighlightType.TYPE_OWN_CIVILIAN_SHIP_DESTROYED);
                            }
                            // add a general message to each enemy player about the destruction of the civilian ship
//						List<Player> enemyPlayers = getEnemyPlayers(enemyMilitarys);
                            Logger.finest("enemyPlayers: " + enemyPlayers.size());
                            for (Player player : enemyPlayers) {
                                if (aSpaceship.getOwner() != null) {
                                    Logger.finest("addToLatestCivilianReport");
                                    if (SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), g.getGameWorld()).isAlwaysRetreat() & stopRetreats) {
                                        player.getTurnInfo().addToLatestCivilianReport("A civilian ship of the type " + SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), getGalaxy().getGameWorld()).getName() + " from govenor " + aSpaceship.getOwner().getName() + " couldn't retreat and has been destroyed in the " + aSpaceship.getLocation().getName() + " system.");
                                    } else {
                                        player.getTurnInfo().addToLatestCivilianReport("A civilian ship of the type " + SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), getGalaxy().getGameWorld()).getName() + " from govenor " + aSpaceship.getOwner().getName() + " has been destroyed in the " + aSpaceship.getLocation().getName() + " system.");
                                    }
                                } else { // civ ship is neutral
                                    player.getTurnInfo().addToLatestCivilianReport("A neutral civilian ship of the type " + SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), getGalaxy().getGameWorld()).getName() + " has been destroyed in the " + aSpaceship.getLocation().getName() + " system.");
                                }
                                SpaceshipHelper.addToLatestShipsLostInSpace(aSpaceship, player.getTurnInfo(), g.getGameWorld());
                                player.getTurnInfo().addToLatestHighlights(SpaceshipPureFunctions.getSpaceshipTypeByKey(aSpaceship.getTypeKey(), g.getGameWorld()).getName(), HighlightType.TYPE_ENEMY_CIVILIAN_SHIP_DESTROYED);
                            }
                            // destroy the civilian ship
                            VipMutator.checkVIPsInDestroyedShips(aSpaceship, aSpaceship.getOwner(), g);
                            TroopMutator.checkTroopsInDestroyedShips(aSpaceship, aSpaceship.getOwner(), g);
                            SpaceshipMutator.removeShip(aSpaceship, g);
                        }
                    }
                }
            }
        }
    }

    protected boolean getStopRetreats(List<Spaceship> enemyMilitarys) {
        boolean stopRetreats = false;
        for (Spaceship spaceship : enemyMilitarys) {
            if (spaceship.isNoRetreat()) {
                stopRetreats = true;
            }
        }
        return stopRetreats;
    }

    protected List<Player> getEnemyPlayers(List<Spaceship> enemyMilitaryShips) {
        List<Player> enemyPlayers = new LinkedList<Player>();
        for (Spaceship anEnemyShip : enemyMilitaryShips) {
            Player tmpPlayer = anEnemyShip.getOwner();
            if (tmpPlayer != null) {
                if (!enemyPlayers.contains(tmpPlayer)) {
                    enemyPlayers.add(tmpPlayer);
                }
            }
        }
        return enemyPlayers;
    }

    protected List<Spaceship> getFriendlyMilitaryShips(Spaceship aSpaceship, List<Spaceship> militaryAtPlanet) {
        List<Spaceship> ships = new LinkedList<Spaceship>();
        Player owner = aSpaceship.getOwner(); // civ owner
        for (Spaceship tmpSpaceship : militaryAtPlanet) {
            if (tmpSpaceship.getOwner() != null) {
                if (owner != null) {
                    if (DiplomacyPureFunctions.friendlyCivilians(tmpSpaceship.getOwner(), owner, g.getDiplomacyStates())) {
                        ships.add(tmpSpaceship);
                    }
                }
            } else {
                if (owner == null) {
                    // neutral civilian & neutral military -> add!
                    ships.add(tmpSpaceship);
                }
            }
        }
        return ships;
    }

    protected List<Spaceship> getEnemyMilitaryShips(Spaceship aSpaceship, List<Spaceship> militaryAtPlanet) {
        List<Spaceship> ships = new LinkedList<Spaceship>();
        Player owner = aSpaceship.getOwner();
        for (Spaceship tmpSpaceship : militaryAtPlanet) {
            // only military ships belonging to players can be considered enemy
            if (tmpSpaceship.getOwner() != null) {
                if (owner == null) {
                    // civilian ship is neutral, check i tmpSpaceship is hosrile
                    //   towards this neutral planet
                    boolean hostile = PlanetOrderStatusPureFunctions.isAttackIfNeutral(aSpaceship.getLocation().getName(), tmpSpaceship.getOwner().getPlanetOrderStatuses());
                    if (hostile) {
                        ships.add(tmpSpaceship);
                    }
                } else if (DiplomacyPureFunctions.hostileCivilians(tmpSpaceship.getOwner(), owner, g.getDiplomacyStates())) {
                    ships.add(tmpSpaceship);
                }
            }
        }
        return ships;
    }

    protected void initGeneralReports() {
        Logger.fine("initGeneralReports called");
        for (int x = 0; x < g.players.size(); x++) {
            Player temp = (Player) g.players.get(x);
            temp.addToGeneral("General Reports");
            temp.addToGeneral("--------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    protected void checkRetreatingGovenor() {
        Logger.fine("called");
        for (int x = 0; x < g.players.size(); x++) {
            Player aPlayer = (Player) g.players.get(x);
            if (!aPlayer.isDefeated()) {
                VIP theGov = VipPureFunctions.findVIPGovernor(aPlayer, g);
                if ((theGov != null) && theGov.getShipLocation() != null) { // must check if theGov is null, because that can happen in single player tutorial
                    Spaceship tempShip = theGov.getShipLocation();
                    if (tempShip.isRetreating()) {
                        aPlayer.setRetreatingGovernor(true);
                        aPlayer.addToHighlights(tempShip.getName(), HighlightType.TYPE_GOVENOR_RETREATING);
                        aPlayer.addToGeneral("Your governor is currently on a retreating ship (" + tempShip.getName() + ").");
                        aPlayer.addToGeneral("As long as your governor is on a retreating ship he cannot give any order to ships or VIPs.");
                        aPlayer.addToGeneral("");
                    } else {
                        aPlayer.setRetreatingGovernor(false);
                    }
                } else {
                    aPlayer.setRetreatingGovernor(false);
                }
            } else {
                aPlayer.setRetreatingGovernor(false);
            }
        }
    }

    protected void clearOrders() {
        Logger.fine("clear orders");
        for (int x = 0; x < g.players.size(); x++) {
            Player temp = (Player) g.players.get(x);

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
    protected void updateEconomyReport1() {
        Logger.fine("updateEconomyReport1 called");
        for (int x = 0; x < g.players.size(); x++) {
            Player aPlayer = (Player) g.players.get(x);
            if (!aPlayer.isDefeated()) {
                EconomyReport er = aPlayer.getTurnInfo().getLatestEconomyReport();
                // actual upkeep cost for ships last turn
                int upkeep = CostPureFunctions.getPlayerUpkeepShips(aPlayer, g.getPlanets(), g.getSpaceships());
                er.setSupportShipsLastTurn(upkeep);
                // actual upkeep cost for troops
                int upkeepTroops = CostPureFunctions.getPlayerUpkeepTroops(aPlayer, g.getPlanets(), g.getTroops());
                er.setSupportTroopsLastTurn(upkeepTroops);
                // actual upkeep cost for VIPs
                int upkeepVIPs = CostPureFunctions.getPlayerUpkeepVIPs(aPlayer, g.getAllVIPs());
                er.setSupportVIPsLastTurn(upkeepVIPs);
                // upkeep lost to corruption last turn
                int upkeepTmp = CostPureFunctions.getPlayerFreeUpkeepWithoutCorruption(aPlayer, g.getPlanets());
                int upkeepLostToCorr = IncomePureFunctions.getLostToCorruption(upkeepTmp, aPlayer.getCorruptionPoint());
                er.setCorruptionUpkeepShipsLastTurn(IncomePureFunctions.getLostToCorruption(upkeepLostToCorr, aPlayer.getCorruptionPoint()));
                // expenses last turn
                er.setExpensesLastTurn(ExpensePureFunction.getExpensesCost(g, aPlayer));
                // actual income last turn
                int income = IncomePureFunctions.getPlayerIncomeWithoutCorruption(aPlayer, false, g);
                er.setIncomeLastTurn(income);
                // income lost to corruption last turn
                int incomeLostToCorr = IncomePureFunctions.getLostToCorruption(income, aPlayer.getCorruptionPoint());
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
    protected void updateEconomyReport2() {
        Logger.fine("updateEconomyReport2 called");
        for (int x = 0; x < g.players.size(); x++) {
            Player aPlayer = (Player) g.players.get(x);
            if (!aPlayer.isDefeated()) {
                EconomyReport er = aPlayer.getTurnInfo().getLatestEconomyReport();
                // actual upkeep cost for ships next turn
                int upkeep = CostPureFunctions.getPlayerUpkeepShips(aPlayer, g.getPlanets(), g.getSpaceships());
                er.setSupportShipsNextTurn(upkeep);
                // actual troops upkeep cost next turn
                int upkeepTroops = CostPureFunctions.getPlayerUpkeepTroops(aPlayer, g.getPlanets(), g.getTroops());
                er.setSupportTroopsNextTurn(upkeepTroops);
//        	int upkeepVIPs = g.getPlayerUpkeepVIPs(aPlayer);
                er.setSupportVIPsNextTurn(upkeepTroops);
                // upkeep lost to corruption next turn
                int upkeepTmp = CostPureFunctions.getPlayerFreeUpkeepWithoutCorruption(aPlayer, g.getPlanets());
                int upkeepLostToCorr = IncomePureFunctions.getLostToCorruption(upkeepTmp, aPlayer.getCorruptionPoint());
                er.setCorruptionUpkeepShipsNextTurn(IncomePureFunctions.getLostToCorruption(upkeepLostToCorr, aPlayer.getCorruptionPoint()));
                // actual income next turn
                int income = IncomePureFunctions.getPlayerIncomeWithoutCorruption(aPlayer, true, g);
                er.setIncomeNextTurn(income);
                // income lost to corruption next turn
//            int incomeTmp = g.getPlayerIncomeWithoutCorruption(aPlayer,false);
                int incomeLostToCorr = IncomePureFunctions.getLostToCorruption(income, aPlayer.getCorruptionPoint());
                er.setCorruptionIncomeNextTurn(incomeLostToCorr);
                // saved to next turn
                er.setSavedNextTurn(aPlayer.getTreasury());
            }
        }
    }

    protected void updateWinRanking(Player p, boolean soloWin) {
        Logger.info("updateWinRanking called: " + p.getName() + " " + p.getFaction().getName() + " " + soloWin);
        int nrDefeatedOpp = 0;
        int nrFaction = g.getFactionMemberNr(p.getFaction());
        int nrHostile = g.players.size() - nrFaction;
        Logger.finer(nrFaction + " " + nrHostile);
        if (soloWin) {
            nrDefeatedOpp = nrHostile;
            Logger.finer(nrFaction + "SoloWin: " + nrDefeatedOpp);
        } else {
            int nrFactionUndefeated = g.getUndefeatedFactionMemberNr(p.getFaction());
            double average = (double) nrHostile / (double) nrFactionUndefeated;
            nrDefeatedOpp = (int) Math.round(Math.ceil(average));
            Logger.finer(nrFaction + "Not solo win: " + nrDefeatedOpp + " " + average);
        }
        rankingWin(nrDefeatedOpp, p.getName(), soloWin);
    }

    protected void updateWinRankingConfederacy(Player p, List<Player> winnerConf) {
        Logger.info("updateWinRankingConfederacy called: " + p.getName() + " " + winnerConf.size());
        int nrDefeatedOpp = 0;
        int nrConf = winnerConf.size();
        int nrHostile = g.players.size() - nrConf;
        Logger.finer(nrConf + " " + nrHostile);
        double average = (double) nrHostile / (double) nrConf;
        nrDefeatedOpp = (int) Math.round(Math.ceil(average));
        Logger.finer(nrConf + "Not solo win: " + nrDefeatedOpp + " " + average);
        rankingWin(nrDefeatedOpp, p.getName(), false);
    }

    protected void updateWinRankingLord(Player p) {
        Logger.info("updateWinRankingLord called: " + p.getName());
        int nrDefeatedOpp = g.players.size() - 1;
        Logger.finer("LordWin: " + nrDefeatedOpp);
        rankingWin(nrDefeatedOpp, p.getName(), false);
    }

    /**
     * Used for single player games
     *
     * @param aPlayer
     */
    protected void addFirstTurnMessages(Player aPlayer) {
        aPlayer.updateTurnInfo();
        aPlayer.addToHighlights("Game has started.", HighlightType.TYPE_SPECIAL_1);
        aPlayer.addToGeneral("Game has started.");
        aPlayer.addToGeneral("");
    }

    //  protected void addFirstTurnMessages(Player aPlayer, SR_Server aSR_Server){
    protected void addFirstTurnMessages(Player aPlayer, MessageDatabase aMessageDatabase, GameWorld gameWorld) {
        aPlayer.updateTurnInfo();
        List<VIP> vips = VipPureFunctions.findPlayersVIPsOnPlanetOrShipsOrTroops(aPlayer.getHomePlanet(), aPlayer, g);
        int nrFaction = g.getFactionMemberNr(aPlayer.getFaction());
        aPlayer.addToGeneral("Game has started.");
        aPlayer.addToHighlights("Game has started.", HighlightType.TYPE_SPECIAL_1);
//  	aPlayer.addToGeneral("Welcome, Governor " + aPlayer.getGovenorName() + ".");
        aPlayer.addToGeneral("");
        String planetsStr = "";
        List<Planet> playerPlanets = g.getPlayersPlanets(aPlayer);
        if (playerPlanets.size() == 1) {
            planetsStr += "You have one planet under your control - the planet " + aPlayer.getHomePlanet().getName() + ".\n";
// 		aPlayer.addToGeneral("You have one planet under your control - the planet " + aPlayer.getHomeplanet().getName() + ".");
        } else {
            planetsStr += "Your homeplanet and main base is the planet " + aPlayer.getHomePlanet().getName() + ".\n";
            for (Planet aPlanet : playerPlanets) {
                if (aPlanet != aPlayer.getHomePlanet()) {
                    planetsStr += "You also control the planet " + aPlanet.getName() + ".\n";
                }
            }

        }
        String unitsStr = "";
        Map<String, Integer> map = new HashMap<>();
        for (VIP aVIP : vips) {
            VIPType vipType = VipPureFunctions.getVipTypeByKey(aVIP.getTypeKey(), gameWorld);
            if (!vipType.isGovernor()) {
                int sum = 1;
                if (map.containsKey(vipType.getName())) {
                    sum += map.get(vipType.getName());
                }
                map.put(vipType.getName(), sum);

            }
        }
        if (map.size() > 0) {
            unitsStr += "\nVIPs under your command.\n";
        }
        for (VIP aVIP : vips) {
            VIPType vipType = VipPureFunctions.getVipTypeByKey(aVIP.getTypeKey(), gameWorld);
            if (map.containsKey(vipType.getName())) {
                if (map.get(vipType.getName()) > 1) {
                    unitsStr += map.get(vipType.getName()) + " " + vipType.getName() + ".\n";
                } else {
                    unitsStr += vipType.getName() + ".\n";
                }
                map.remove(vipType.getName());
            }
        }
        map.clear();


        // add text about starting ships
        List<Spaceship> playerShips = aPlayer.getGalaxy().getPlayersSpaceships(aPlayer);

        for (Spaceship ss : playerShips) {
            int sum = 1;
            if (map.containsKey(SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getName())) {
                sum += map.get(SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getName());
            }
            map.put(SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getName(), sum);
        }
        if (map.size() > 0) {
            unitsStr += "\nShips under your command.\n";
        }
        for (Spaceship ss : playerShips) {
            if (map.containsKey(SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getName())) {
                if (map.get(SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getName()) > 1) {
                    unitsStr += map.get(SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getName()) + " " + SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getName() + ".\n";
                } else {
                    unitsStr += SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getName() + ".\n";
                }
                map.remove(SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getName());
            }
        }
        map.clear();

        // add text about starting troops
        List<Troop> playerTroops = TroopPureFunctions.getPlayersTroops(aPlayer, aPlayer.getGalaxy());

        for (Troop aTroop : playerTroops) {
            int sum = 1;
            if (map.containsKey(TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName())) {
                sum += map.get(TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName());
            }
            map.put(TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName(), sum);
        }
        if (map.size() > 0) {
            unitsStr += "\nTroops under your command.\n";
        }
        for (Troop aTroop : playerTroops) {
            if (map.containsKey(TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName())) {
                if (map.get(TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName()) > 1) {
                    unitsStr += map.get(TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName()) + " " + TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName() + ".\n";
                } else {
                    unitsStr += TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName() + ".\n";
                }
                map.remove(TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName());
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

        tmpText = "Greetings, Governor " + aPlayer.getGovernorName();
// 	aPlayer.addToGeneral(tmpText);
        messageText += tmpText + "\n";

        tmpText = "We welcome you to the " + f.getName() + " faction.";
// 	aPlayer.addToGeneral(tmpText);
        messageText += tmpText + "\n";
        messageText += "\n";
        messageText += planetsStr + "\n";

        messageText += unitsStr + "\n";

        if (aPlayer.getFaction().getAdvantages() != null && !aPlayer.getFaction().getAdvantages().equals("")) {
            messageText += "Faction advantages: \n";
            messageText += aPlayer.getFaction().getAdvantages() + "\n\n";
        }

        if (aPlayer.getFaction().getDisadvantages() != null && !aPlayer.getFaction().getDisadvantages().equals("")) {
            messageText += "Faction disadvantages: \n";
            messageText += aPlayer.getFaction().getDisadvantages() + "\n\n";
        }

        if (aPlayer.getFaction().getHowToPlay() != null && !aPlayer.getFaction().getHowToPlay().equals("")) {
            messageText += "How to play your faction: \n";
            messageText += aPlayer.getFaction().getHowToPlay() + "\n\n";
        }

        if (g.getGameWorld().getHowToPlay() != null && !g.getGameWorld().getHowToPlay().equals("")) {
            messageText += "The way to play this world: \n";
            messageText += g.getGameWorld().getHowToPlay() + "\n\n";
        }


        int nrHostile = g.players.size();
        if (g.getDiplomacyGameType() == DiplomacyGameType.FACTION) {
            nrHostile -= nrFaction;
        } else { // in all other diplomacy types
            nrHostile -= 1;
        }

        if (g.getDiplomacyGameType() == DiplomacyGameType.FACTION) {
            tmpText = "Be careful, according to our information there are " + nrHostile + " hostile governors in this quadrant.";
        } else if (g.getDiplomacyGameType() == DiplomacyGameType.DEATHMATCH) {
            tmpText = "Be careful, according to our information there are " + nrHostile + " hostile governors in this quadrant.";
        } else { // OPEN & GAMEWORLD
            tmpText = "Be careful, according to our information there are " + nrHostile + " potentially hostile governors in this quadrant.";
        }

        // 	aPlayer.addToGeneral(tmpText);
        messageText += tmpText + "\n";

        tmpText = addEnemyGovMessage(nrFaction, aPlayer);
        if (tmpText != null) {
            messageText += tmpText + "\n";
        }

        messageText += "\n";
        tmpText = "Good luck, Governor";
// 	aPlayer.addToGeneral(tmpText);
        messageText += tmpText + "\n";

        Message newMessage = new Message(messageText, aPlayer, new Player(f.getName() + " Headquarters", "password", g, "govname", f.getName(), new Planet(0, 0, 0, "name", 0, 0, true, true), new ArrayList<>()));

        aMessageDatabase.addMessage(newMessage, aPlayer.getGalaxy(), 0);

        // add to highlights
// 	if(g.getNrStartPlanets() == 0){
//	 	playerShips = aPlayer.getGalaxy().getPlayersSpaceships(aPlayer);
//	 	for (Iterator iter = playerShips.iterator(); iter.hasNext();) {
//			Spaceship ss = (Spaceship) iter.next();
//			aPlayer.addToHighlights("You have a " + ss.getSpaceshipType().getName() + " under your command",1);
//		}
// 	}

        aPlayer.addToHighlights("You have recieved a message from " + f.getName() + " Command", HighlightType.TYPE_SPECIAL_LAST);
    }

    protected String addEnemyGovMessage(int nrFaction, Player aPlayer) {
        String retText = null;
        if (g.getDiplomacyGameType() != DiplomacyGameType.DEATHMATCH) {
            if (nrFaction == 2) {
                retText = "There are also one other " + aPlayer.getFaction().getName() + " governor in this quadrant of the same faction as you.";
            } else {
                if (nrFaction > 2) {
                    retText = "There are also " + (nrFaction - 1) + " other " + aPlayer.getFaction().getName() + " governors in this quadrant of the same faction as you.";
                } else {
                    retText = "";
                }
            }
        }
        return retText;
    }

    protected void performBlackMarket() {
        Logger.fine("performBlackMarket called");
        // perform all bids on current offers
        BlackMarketPerformer.performBlackMarket(g);
        // add new offers
        if (g.getTurn() > 0) {
            BlackMarketPerformer.newTurn(g);
        }
    }

    protected void checkVIPConflicts() {
        Logger.fine("checkVIPConflicts called");
        // check conflicts between duellists
        checkDuels(g);
        // check if spies catch any visiting VIPs that isn't immune
        checkCounterEspionage(g);
        // check if any Assassins kill other VIPs that isn't well guarded
        checkAssassins(g);
        // check if any exterminators catch any infestators
        checkExtermination(g);
    }

    protected void checkDiplomatsOnNeutrals() {
        Logger.fine("checkDiplomatsOnNeutrals called");
        List<VIP> allDips = getAllDiplomatsOnNeutralPlanets();
        for (VIP tempDip : allDips) {
            VIPType vipType = VipPureFunctions.getVipTypeByKey(tempDip.getTypeKey(), g.getGameWorld());
            Planet tempLocation = tempDip.getPlanetLocation();
            List<VIP> hostileDips = getAllHostileDiplomatOnNeutral(tempDip, tempLocation, allDips);
            List<VIP> friendlyDips = getAllFriendlyDiplomatOnNeutral(tempDip, tempLocation, allDips);
            Player aPlayer = tempDip.getBoss();
            int total = hostileDips.size() + friendlyDips.size();
            if ((hostileDips.size() > 0) | (friendlyDips.size() > 0)) {
                aPlayer.addToGeneral("Your " + vipType.getName() + " tries to convince the neutral planet " + tempLocation.getName() + " to join you.");
                for (VIP aFriendlyDip : friendlyDips) {
                    aPlayer.addToGeneral("A " + VipPureFunctions.getVipTypeByKey(aFriendlyDip.getTypeKey(), g.getGameWorld()).getName() + " from your own faction is also present at the neutral planet " + tempLocation.getName());
                }
                for (VIP aHostileDip : hostileDips) {
                    aPlayer.addToGeneral("A " + VipPureFunctions.getVipTypeByKey(aHostileDip.getTypeKey(), g.getGameWorld()).getName() + " from the " + aHostileDip.getBoss().getFaction().getName() + " faction is also present at the neutral planet " + tempLocation.getName());
                }
                String pluralS = "";
                if (total > 1) {
                    pluralS = "s";
                }
                if (tempDip.getGovCounter() >= tempLocation.getResistance()) {
                    // join has been blocked
                    aPlayer.addToHighlights(pluralS, HighlightType.TYPE_GOVENOR_NEUTRAL_JOIN_BLOCKED);
                    if (total > 1) {
                        aPlayer.addToGeneral("The presense of other Diplomats has blocked your effort to convince the planet to join you. If the other Diplomats leave the planet the planet will join you immediately.");
                    } else {
                        aPlayer.addToGeneral("The presense of another Diplomat has blocked your effort to convince the planet to join you. If the other Diplomat leave the planet the planet will join you immediately.");
                    }
                } else {
                    if (hostileDips.size() > 0) {
                        // persuation has been blocked
                        aPlayer.addToHighlights(pluralS, HighlightType.TYPE_GOVENOR_NEUTRAL_PERSUATION_BLOCKED);
                        if (total > 1) {
                            aPlayer.addToGeneral("The presense of hostile Diplomats has blocked your effort to persuade the planet to join you. As long as the hostile Diplomats remain on the planet you will not get any closer to persuading it.");
                        } else {
                            aPlayer.addToGeneral("The presense of a hostile Diplomat has blocked your effort to persuade the planet to join you. As long as the hostile Diplomat remain on the planet you will not get any closer to persuading it.");
                        }
                    } else {
                        // can persuade, but will not join unless...
                        tempDip.setGovCounter(tempDip.getGovCounter() + 1);
                        if (tempDip.getGovCounter() == tempLocation.getResistance()) {  // one turn left to join
                            aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will soon join you, if you are the lone Diplomat left at this planet.");
                        } else {
                            aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will need at least one more turn of negotiations, but will not join you unless you are the lone Diplomat left on this planet.");
                        }
                    }
                }
            } else { // dip can persuades to join (no dips from other players)
                if (tempDip.getGovLastTurn() < g.getTurn()) {
                    Logger.fine("Persuation for planet " + tempLocation.getName());
                    aPlayer.addToGeneral("Your " + vipType.getName() + " tries to convince the neutral planet " + tempLocation.getName() + " to join you.");
                    tempDip.setGovCounter(tempDip.getGovCounter() + 1);
                    tempDip.setLastTurn(g.getTurn());
                    List<VIP> ownDips = getAllOwnDiplomatOnNeutral(tempDip, tempLocation, allDips);
                    for (VIP anotherDip : ownDips) {
                        aPlayer.addToGeneral("Your " + VipPureFunctions.getVipTypeByKey(anotherDip.getTypeKey(), g.getGameWorld()).getName() + " tries to convince the neutral planet " + tempLocation.getName() + " to join you.");
                        tempDip.setGovCounter(tempDip.getGovCounter() + 1);
                        anotherDip.setLastTurn(g.getTurn());
                        anotherDip.setGovCounter(anotherDip.getGovCounter() + ownDips.size() + 1);
                    }
                    if (tempDip.getGovCounter() >= tempLocation.getResistance()) {
                        // the planet joins
                        PlanetMutator.joinsVisitingDiplomat(tempLocation, tempDip, true, g.getGameWorld());
                        shipsJoinGovenor(tempLocation, tempDip);
                        troopsJoinGovenor(tempLocation, tempDip);
                        PlanetUpdater.checkVIPsOnConqueredPlanet(tempLocation, aPlayer, g);
                        //            	tempDip.clearGovCounter(); not needed, done later for all vips
                    } else {
                        // not join yet
                        if (tempDip.getGovCounter() == (tempLocation.getResistance() - 1)) {
                            // one turn left to join
                            aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will soon join you.");
                        } else {
                            aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will need at least one more turn of negotiations.");
                        }
                    }
                } else {
                    Logger.fine("Persuation for planet " + tempLocation.getName() + " is already handled");
                }
            }
            aPlayer.addToGeneral("");
        }
        clearDiplomatCounters();
    }

    private List<VIP> getAllDiplomatsOnNeutralPlanets() {
        List<VIP> allDiplomats = new LinkedList<VIP>();
        for (int i = 0; i < g.getAllVIPs().size(); i++) {
            VIP tempVIP = g.getAllVIPs().get(i);
            if (VipPureFunctions.getVipTypeByKey(tempVIP.getTypeKey(), g.getGameWorld()).isDiplomat() & !tempVIP.getBoss().isAlien()) { // aliens can not use diplomacy
                Planet tempLocation = tempVIP.getPlanetLocation();
                if (tempLocation != null) { // tempVIP �r vid en planet
                    if ((tempLocation.getPlayerInControl() == null) & !tempLocation.isRazed()) { // planeten �r neutral
                        allDiplomats.add(tempVIP);
                    }
                }
            }
        }
        return allDiplomats;
    }

    protected void clearDiplomatCounters() {
        // clear counters from all diplomats not on neutral planets
        List<VIP> allDips = getAllDiplomatsNotOnNeutralPlanets();
        for (VIP aDip : allDips) {
            aDip.setGovCounter(0);
            aDip.setGovLastTurn(-1);
        }
    }

    public List<VIP> getAllDiplomatsNotOnNeutralPlanets() {
        List<VIP> allDips = new LinkedList<VIP>();
        for (int i = 0; i < g.getAllVIPs().size(); i++) {
            VIP tempVIP = (VIP) g.getAllVIPs().get(i);
            if (VipPureFunctions.getVipTypeByKey(tempVIP.getTypeKey(), g.getGameWorld()).isDiplomat()) {
                Planet tempLocation = tempVIP.getPlanetLocation();
                if (tempLocation != null) { // Gov �r vid en planet
                    if (tempLocation.getPlayerInControl() != null) { // planeten �r inte neutral
                        allDips.add(tempVIP);
                    }
                } else {
                    allDips.add(tempVIP);
                }
            }
        }
        return allDips;
    }

    protected void clearInfestatorCounters() {
        // clear counters from all infestators not in action
        List<VIP> allInfs = getAllInfestatorsNotOnActionPlanets();
        for (VIP vip : allInfs) {
            vip.setGovCounter(0);
            vip.setGovLastTurn(-1);
        }
    }

    private List<VIP> getAllInfestatorsNotOnActionPlanets() {
        List<VIP> allInfs = new LinkedList<VIP>();
        for (int i = 0; i < g.getAllVIPs().size(); i++) {
            VIP tempVIP = g.getAllVIPs().get(i);
            if (VipPureFunctions.getVipTypeByKey(tempVIP.getTypeKey(), g.getGameWorld()).isInfestate()) {
                Planet tempLocation = tempVIP.getPlanetLocation();
                if (tempLocation == null) { // inf is not at a planet
                    allInfs.add(tempVIP);
                } else if (tempLocation.getPopulation() == 0) { // razed cannot be infed
                    allInfs.add(tempVIP);
                } else {
                    Player owner = tempLocation.getPlayerInControl();
                    if (owner == null) { // can inf neutral planet!
                        // do nothing, ok to infestate!
                    } else if (owner == tempVIP.getBoss()) { // cannot inf own planet
                        allInfs.add(tempVIP);
                    } else if (owner.isAlien()) { // cannot inf alien planets
                        allInfs.add(tempVIP);
                    } else if (owner.getFaction().equals(tempVIP.getBoss().getFaction())) { // cannot inf same factions
                        // planets
                        allInfs.add(tempVIP);
                    }
                }
            }
        }
        return allInfs;
    }

    protected void checkInfestationFromVIPs() {
        Logger.fine("checkInfestationFromVIPs called");
        List<VIP> allInfs = getAllInfestatorsOnPlanets();
        for (VIP tempInf : allInfs) {
            VIPType vipType = VipPureFunctions.getVipTypeByKey(tempInf.getTypeKey(), g.getGameWorld());
            Logger.finer("tempInf: " + vipType.getName());
            Planet tempLocation = tempInf.getPlanetLocation();
            Player aPlayer = tempInf.getBoss();
            if (DiplomacyPureFunctions.hostileInfestator(aPlayer, tempLocation, g)) {
                List<VIP> otherInfs = getAllOtherInfestators(tempInf, tempLocation, allInfs);
                if (otherInfs.size() > 0) {
                    aPlayer.addToGeneral("Your " + vipType.getName() + " tries to infect the planet " + tempLocation.getName() + " to join you.");
                    for (VIP anotherInf : otherInfs) {
                        aPlayer.addToGeneral(Functions.getDeterminedForm(VipPureFunctions.getVipTypeByKey(anotherInf.getTypeKey(), g.getGameWorld()).getName(), true) + " " + VipPureFunctions.getVipTypeByKey(anotherInf.getTypeKey(), g.getGameWorld()).getTypeName() + " from the " + anotherInf.getBoss().getFaction().getName() + " faction is also present at the planet " + tempLocation.getName());
                    }
                    String pluralS = "";
                    if (otherInfs.size() > 1) {
                        pluralS = "s";
                    }
                    if (otherInfs.size() > 0) {
                        // infestation has been blocked
                        aPlayer.addToHighlights(pluralS, HighlightType.TYPE_INFESTATION_BLOCKED);
                        if (otherInfs.size() > 1) {
                            aPlayer.addToGeneral("The presense of other infestators has blocked your effort to infect the planet to join you. As long as the other infestators remain on the planet you will not get any closer to infecting it.");
                        } else {
                            aPlayer.addToGeneral("The presense of another infestator has blocked your effort to infect the planet to join you. As long as the other infestator remain on the planet you will not get any closer to infecting it.");
                        }
                    }
                    if (tempLocation.getPlayerInControl() != null) {
                        tempLocation.getPlayerInControl().addToHighlights(tempLocation.getName(), HighlightType.TYPE_OWN_PLANET_INFESTATION_IN_PROGRESS);
                        tempLocation.getPlayerInControl().addToGeneral("Your planet " + tempLocation.getName() + " are being infected by aliens.");
                    }
                } else { // no infs from other players present, persuades to join
                    if (tempInf.getGovLastTurn() < g.getTurn()) {
                        Logger.finer("inf is alone, persuades to join");
                        Logger.fine("Infestation for planet " + tempLocation.getName());
                        aPlayer.addToGeneral("Your " + vipType.getName() + " tries to infect the planet " + tempLocation.getName() + " to join you.");
                        tempInf.setGovCounter(tempInf.getGovCounter() + 1);
                        tempInf.setLastTurn(g.getTurn());
                        List<VIP> ownInfs = getAllOwnInfestators(tempInf, tempLocation, allInfs);
                        for (VIP anotherInf : ownInfs) {
                            aPlayer.addToGeneral("Your " + VipPureFunctions.getVipTypeByKey(anotherInf.getTypeKey(), g.getGameWorld()).getName() + " tries to infect the planet " + tempLocation.getName() + " to join you.");
                            tempInf.setGovCounter(tempInf.getGovCounter() + 1);
                            anotherInf.setLastTurn(g.getTurn());
                            anotherInf.setGovCounter(anotherInf.getGovCounter() + ownInfs.size() + 1);
                        }
                        Logger.finer("tempInf.getGovCounter(): " + tempInf.getGovCounter());
                        Logger.finer("tempLocation.getPopulation(): " + tempLocation.getPopulation());
                        if (tempInf.getGovCounter() >= tempLocation.getPopulation()) {
                            // the planet joins
                            if (tempLocation.getPlayerInControl() == null) {
                                removeNeutralShips(tempLocation, tempInf);
                            }
                            checkTroopsOnInfestedPlanet(tempLocation, aPlayer);
                            PlanetMutator.joinsVisitingInfector(tempLocation, tempInf, g.getGameWorld());
                            // check for diplomats/other vips killed on infestated planets
                            PlanetUpdater.checkVIPsOnConqueredPlanet(tempLocation, aPlayer, g);
                        } else {
                            // not join yet
                            if (tempInf.getGovCounter() == (tempLocation.getPopulation() - 1)) {
                                // one turn left to join
                                aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will soon join you.");
                            } else {
                                aPlayer.addToGeneral("The planet " + tempLocation.getName() + " will need at least one more turn of infection.");
                            }
                            if (tempLocation.getPlayerInControl() != null) {
                                tempLocation.getPlayerInControl().addToHighlights(tempLocation.getName(), HighlightType.TYPE_OWN_PLANET_INFESTATION_IN_PROGRESS);
                                tempLocation.getPlayerInControl().addToGeneral("Your planet " + tempLocation.getName() + " is being infected with by aliens.");
                            }
                        }
                    } else {
                        Logger.fine("Infestation for planet " + tempLocation.getName() + " is already handled");
                    }
                }
            }
            aPlayer.addToGeneral("");
        }
        clearInfestatorCounters();
    }

    private List<VIP> getAllInfestatorsOnPlanets() {
        Logger.finer("getAllInfestatorsOnPlanets() called");
        List<VIP> allInfestators = new LinkedList<VIP>();
        Logger.finest("allVIPs.size(): " + g.getAllVIPs().size());
        for (VIP tempVIP : g.getAllVIPs()) {
            VIPType vipType = VipPureFunctions.getVipTypeByKey(tempVIP.getTypeKey(), g.getGameWorld());
            Logger.finest("tempVIP: " + vipType.getName());
            Logger.finest("tempVIP.isInfestator(): " + vipType.isInfestate());
            Logger.finest("tempVIP.getBoss().isAlien(): " + tempVIP.getBoss().isAlien());
            Logger.finest("tempVIP.getAlignment(): " + vipType.getAlignment());
            Logger.finest(
                    "tempVIP.getBoss().getFaction().getAlignment(): " + tempVIP.getBoss().getFaction().getAlignment());
            if (vipType.isInfestate() & tempVIP.getBoss().isAlien()
                    & vipType.getAlignment().equals(tempVIP.getBoss().getFaction().getAlignment())) { // only
                // infestators
                // with the same
                // alignment as
                // a player can
                // use
                // infestation
                Planet tempLocation = tempVIP.getPlanetLocation();
                if (tempLocation != null) { // tempVIP �r vid en planet
                    Logger.finest("tempLocation != null ");
                    Logger.finest("tempLocation: " + tempLocation.getName());
                    Player owner = tempLocation.getPlayerInControl();
                    if ((owner == null) || ((owner != tempVIP.getBoss()) & !owner.isAlien()
                            & !(owner.getFaction().equals(tempVIP.getBoss().getFaction())))) { // planet is neutral or
                        // belongs to another
                        // non-alien player from
                        // another faction
                        Logger.finest("can infest");
                        if (tempLocation.getPopulation() > 0) { // can not infestate razed planets, troops are needed
                            // for that
                            Logger.finest("tempLocation.getPopulation() > 0");
                            allInfestators.add(tempVIP);
                        }
                    }
                }
            }
        }
        return allInfestators;
    }

    public void checkTroopsOnInfestedPlanet(Planet aPlanet, Player aPlayer) {
        List<Troop> allTroopsOnPlanet = TroopPureFunctions.findAllTroopsOnPlanet(g.getTroops(), aPlanet);
        for (Troop aTroop : allTroopsOnPlanet) {
            if (aTroop.getOwner() == aPlanet.getPlayerInControl()) { // if troop belongs to the same player that
                // controls the planet (or is neutral)
                g.getTroops().remove(aTroop);
                if (aTroop.getOwner() != null) {
                    aTroop.getOwner().addToGeneral("Your " + aTroop.getName()
                            + " have been destroyed when the planet " + aPlanet.getName() + " was infested.");
                }
                // aTroop.getOwner().addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
                aPlayer.addToGeneral("An enemy " + TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName()
                        + " have been killed when you infested the planet " + aPlanet.getName() + ".");
                // aPlayer.addToHighlights(tempVIP.getName(),Highlight.TYPE_ENEMY_VIP_KILLED);
            }
        }
    }

    /**
     * Check if there are any hostile diplomats (other faction than aGov) on the neutral planet aPlanet
     *
     * @param aDip    the gov on planet aPlanet
     * @param aPlanet a neutral planet where aGov are
     * @return true if there is at least one hostile gov on aPlanet
     */
    protected List<VIP> getAllHostileDiplomatOnNeutral(VIP aDip, Planet aPlanet, List<VIP> allDips) {
        Logger.finer("called: " + aDip.getBoss().getGovernorName() + " " + aPlanet.getName());
        List<VIP> found = new LinkedList<VIP>();
        for (int i = 0; i < allDips.size(); i++) {
            VIP tempVIP = (VIP) allDips.get(i);
            if (tempVIP != aDip) { // kolla om tempVIP inte �r aGov
                Planet tempLocation = tempVIP.getPlanetLocation();
                if (tempLocation == aPlanet) { // Dip �r vid aPlanet
                    Faction f1 = aDip.getBoss().getFaction();
                    Faction f2 = tempVIP.getBoss().getFaction();
                    if (f1 != f2) {
                        found.add(tempVIP);
                    }
                }
            }
        }
        return found;
    }

    /**
     * Check if there are any other infestators (other players than aGov) on the planet aPlanet
     *
     * @param anInf   the inf on planet aPlanet
     * @param aPlanet the planet where anInf are
     * @return true if there is at least one other inf on aPlanet
     */
    protected List<VIP> getAllOtherInfestators(VIP anInf, Planet aPlanet, List<VIP> allInfs) {
        Logger.fine("called: " + anInf.getBoss().getGovernorName() + " " + aPlanet.getName());
        List<VIP> found = new LinkedList<VIP>();
        for (VIP tempVIP : allInfs) {
            if (tempVIP != anInf) {
                Planet tempLocation = tempVIP.getPlanetLocation();
                if (tempLocation == aPlanet) { // tempVIP �r vid aPlanet
                    if (anInf.getBoss() != (tempVIP.getBoss())) {
                        found.add(tempVIP);
                    }
                }
            }
        }
        return found;
    }

    /**
     * Find all other own infestators
     *
     * @param anInf
     * @param aPlanet
     * @param allInfs
     * @return
     */
    protected List<VIP> getAllOwnInfestators(VIP anInf, Planet aPlanet, List<VIP> allInfs) {
        Logger.fine("called: " + anInf.getBoss().getGovernorName() + " " + aPlanet.getName());
        List<VIP> found = new LinkedList<VIP>();
        for (VIP tempVIP : allInfs) {
            if (tempVIP != anInf) {
                Planet tempLocation = tempVIP.getPlanetLocation();
                if (tempLocation == aPlanet) { // tempVIP �r vid aPlanet
                    if (anInf.getBoss() == (tempVIP.getBoss())) {
                        found.add(tempVIP);
                    }
                }
            }
        }
        return found;
    }

    /**
     * Check if there are any friendly diplomats (same faction than aGov) on the neutral planet aPlanet
     *
     * @param aDip    the gov on planet aPlanet
     * @param aPlanet a neutral planet where aGov are
     * @return true if there is at least one friemdly gov on aPlanet
     */
    protected List<VIP> getAllFriendlyDiplomatOnNeutral(VIP aDip, Planet aPlanet, List<VIP> allDips) {
        Logger.finer("called: " + aDip.getBoss().getGovernorName() + " " + aPlanet.getName());
        List<VIP> found = new LinkedList<VIP>();
        for (int i = 0; i < allDips.size(); i++) {
            VIP tempVIP = (VIP) allDips.get(i);
            if (tempVIP != aDip) { // kolla om tempVIP inte �r aDip
                Planet tempLocation = tempVIP.getPlanetLocation();
                if (tempLocation == aPlanet) { // tempDip �r vid aPlanet
                    Player p1 = aDip.getBoss();
                    Player p2 = tempVIP.getBoss();
                    Faction f1 = p1.getFaction();
                    Faction f2 = p2.getFaction();
                    if ((f1 == f2) & (p1 != p2)) { // same faction but not same player
                        found.add(tempVIP);
                    }
                }
            }
        }
        return found;
    }

    /**
     * Check if there are any own diplomats (diplomats from the same player) on the neutral planet aPlanet
     *
     * @param aDip    a diplomat on planet aPlanet
     * @param aPlanet a neutral planet where aGov are
     * @return true if there is at least one friemdly gov on aPlanet
     */
    protected List<VIP> getAllOwnDiplomatOnNeutral(VIP aDip, Planet aPlanet, List<VIP> allDips) {
        List<VIP> found = new LinkedList<VIP>();
        for (VIP tempVIP : allDips) {
            if (tempVIP != aDip) { // kolla om tempVIP inte �r aDip
                Planet tempLocation = tempVIP.getPlanetLocation();
                if (tempLocation == aPlanet) { // tempDip �r vid aPlanet
                    Player p1 = aDip.getBoss();
                    Player p2 = tempVIP.getBoss();
                    if (p1 == p2) { // same player
                        found.add(tempVIP);
                    }
                }
            }
        }
        return found;
    }

    public void shipsJoinGovenor(Planet joiningPlanet, VIP dip) {
        List<Spaceship> allss = g.getSpaceships();
        List<Spaceship> removeShips = new LinkedList<>();
        List<Spaceship> addShips = new LinkedList<>();
        for (Spaceship ss : allss) {
            if ((ss.getLocation() == joiningPlanet) & (ss.getOwner() == null)) { // skeppet är neutralt och är vid planeten
                // add new ship instead of the neutral one
                //TODO 2020-04-22 No need to get players SpaceshipType(should not use the upgrades from the new owner), check why we are creating a nwe ship instead of just changing the owner. Possible name conflict?
                //SpaceshipType sstTemp = PlayerPureFunctions.findSpaceshipType(ss.getSpaceshipType().getName(), dip.getBoss(), g);
                //if(sstTemp == null){
                SpaceshipType sstTemp = SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld());
                //}

                Spaceship ssTemp = SpaceshipMutator.createSpaceShip(dip.getBoss(), sstTemp, 0, 0, ss.getTechWhenBuilt());
                //Spaceship ssTemp = sstTemp.getShip(null,0,ss.getTechWhenBuilt());
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
        for (Spaceship ss : removeShips) {
            // remove neutral ship
            SpaceshipMutator.removeShip(ss, g);
        }
    }

    public void troopsJoinGovenor(Planet joiningPlanet, VIP dip) {
        List<Troop> allTroops = g.getTroops().stream().collect(Collectors.toList());
        List<Troop> removeTroops = new LinkedList<Troop>();
        for (Troop aTroop : allTroops) {
            if ((aTroop.getPlanetLocation() == joiningPlanet) & (aTroop.getOwner() == null)) { //
                // add new troop instead of the neutral one
                //TODO 2020-05-07 No need to get players TroopType(should not use the upgrades from the new owner), check why we are creating a nwe ship instead of just changing the owner. Possible name conflict?
                //TroopType ttTemp = PlayerPureFunctions.findOwnTroopType(aTroop.getTroopType().getUniqueName(), dip.getBoss(), g);
                //if(ttTemp == null){
                //  ttTemp = g.findTroopType(aTroop.getTroopType().getUniqueName());
                //}
                TroopType ttTemp = g.findTroopType(TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), g.getGameWorld()).getName());
                Troop troopTemp = TroopMutator.createTroop(ttTemp, g);
                troopTemp.setCurrentDamageCapacity(aTroop.getCurrentDamageCapacity());
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
            TroopMutator.removeTroop(aTroop, g);
        }
    }

    protected void removeNeutralShips(Planet joiningPlanet, VIP inf) {
        List<Spaceship> allss = g.getSpaceships();
        List<Spaceship> removeShips = new LinkedList<Spaceship>();
        for (Spaceship ss : allss) {
            if ((ss.getLocation() == joiningPlanet) & (ss.getOwner() == null)) { // skeppet �r neutralt och �r vid planeten
                // add ship to remove vector
                removeShips.add(ss);
            }
        }
        for (Spaceship ss : removeShips) {
            // remove neutral ship
            SpaceshipMutator.removeShip(ss, g);
        }
    }

    protected void checkNonBesiegedPlanets() {
        Logger.fine("checkNonBesiegedPlanets called");
        for (int x = 0; x < g.planets.size(); x++) {
            Planet temp = (Planet) g.planets.get(x);
            if (!temp.isBesieged()) {
                if (temp.getResistance() < 1) {  // endast icke-neutrala planeter kan negativ resistance utan att ge upp
                    if (temp.getPlayerInControl() != null) {
                        temp.setResistance(1 + temp.getPlayerInControl().getFaction().getResistanceBonus());
                    } else {
                        temp.setResistance(1);
                    }
                }
            }
        }
    }

    protected void checkBroke() {
        Logger.fine("checkBroke called");
        for (int x = 0; x < g.players.size(); x++) {
            Player temp = (Player) g.players.get(x);
            if (!temp.isDefeated()) {
                int tempTreasury = temp.getTreasury();
                if (tempTreasury < 0) {
                    temp.setTreasury(0);
                }
            }
        }
    }

    protected void updateMapPlanetInfos() {
        Logger.fine("updateMapPlanetInfos()");
        for (Player aPlayer : g.getPlayers()) {
            aPlayer.getMapPlanetInfos().getAllTurns().add(MapPureFunctions.createMapInfoTurn(aPlayer, aPlayer.getMapPlanetInfos(), aPlayer.getMapPlanetInfos().getAllTurns().size() + 1));
        }
    }

    protected void updatePlanetInfos() {
        Logger.fine("updatePlanetInfos() called");
        for (int x = 0; x < g.players.size(); x++) {
            Player tempPlayer = (Player) g.players.get(x);
            Logger.finest("tempPlayer: " + tempPlayer.getName());
            Logger.finest("-----------------------");
            for (int i = 0; i < g.planets.size(); i++) {
                Planet p = g.planets.get(i);
                // set last known owner name
                boolean spy = VipPureFunctions.findVIPSpy(p, tempPlayer, g) != null;
                boolean shipInSystem = (g.playerHasShipsInSystem(tempPlayer, p));
                boolean surveyShip = SpaceshipPureFunctions.findSurveyShip(p, tempPlayer, g.getSpaceships(), g.getGameWorld()) != null;
                boolean surveyVIP = VipPureFunctions.findSurveyVIPonShip(p, tempPlayer, g) != null;
                boolean open = p.isOpen();
                boolean neutralPlanet = (p.getPlayerInControl() == null);
                if (open | shipInSystem | spy) {
                    if (!neutralPlanet) {
                        PlanetMutator.setLastKnownOwner(p.getName(), p.getPlayerInControl().getName(), g.turn + 1, tempPlayer.getPlanetInformations());
                        PlanetPureFunctions.findPlanetInfo(p.getName(), tempPlayer.getPlanetInformations()).setRazed(false);
                        PlanetPureFunctions.findPlanetInfo(p.getName(), tempPlayer.getPlanetInformations()).setLastKnownMaxShipSize(MapPureFunctions.getLargestShipSizeOnPlanet(p, tempPlayer, g));
                        Logger.finest("setLastKnownMaxShipSize: " + p.getName() + ", " + MapPureFunctions.getLargestShipSizeOnPlanet(p, tempPlayer, g));
                    } else {
                        //            LoggingHandler.finest(this,g,"updatePlanetInfos","g.turn: " + g.turn);
                        //            LoggingHandler.finest(this,g,"updatePlanetInfos","p.getName: " + p.getName());
                        PlanetMutator.setLastKnownOwner(p.getName(), "Neutral", g.turn + 1, tempPlayer.getPlanetInformations());
                        PlanetPureFunctions.findPlanetInfo(p.getName(), tempPlayer.getPlanetInformations()).setRazed(p.isRazed());
                        PlanetPureFunctions.findPlanetInfo(p.getName(), tempPlayer.getPlanetInformations()).setLastKnownMaxShipSize(MapPureFunctions.getLargestShipSizeOnPlanet(p, null, false, g));
                        Logger.finest("setLastKnownMaxShipSize neutral: " + p.getName() + ", " + MapPureFunctions.getLargestShipSizeOnPlanet(p, null, false, g));
                    }
                    if (open | spy) {
                        String buildingsOrbitString = createBuildingString(p.getBuildingsInOrbit());
                        PlanetPureFunctions.findPlanetInfo(p.getName(), tempPlayer.getPlanetInformations()).setLastKnownBuildingsInOrbit(buildingsOrbitString);
                        Logger.finest("setLastKnownBuildingsInOrbit: " + p.getName() + ", " + buildingsOrbitString);
                        // store surface buildings in separate field
                        String buildingsSurfaceString = createBuildingString(p.getBuildingsOnSurface());
                        PlanetPureFunctions.findPlanetInfo(p.getName(), tempPlayer.getPlanetInformations()).setLastKnownBuildingsOnSurface(buildingsSurfaceString);
                        Logger.finest("setLastKnownSurfaceBuildings: " + p.getName() + ", " + buildingsSurfaceString);
                    } else { // must be shipInSystem, can only see buildings in orbit
                        String buildingsOrbitString = createBuildingString(p.getBuildingsInOrbit());
                        PlanetPureFunctions.findPlanetInfo(p.getName(), tempPlayer.getPlanetInformations()).setLastKnownBuildingsInOrbit(buildingsOrbitString);
                        Logger.finest("setLastKnownBuildingsInOrbit shipInSystem: " + p.getName() + ", " + buildingsOrbitString);
                    }
                }
                // set last known prod and res values
                if (open | spy | surveyShip | surveyVIP) {
                    //        	LoggingHandler.finest(this,g,"updatePlanetInfos","last known res: " + p.getName() + " " + p.getResistance());
                    PlanetMutator.setLastKnownProductionAndResistance(p.getName(), p.getPopulation(), p.getResistance(), tempPlayer.getPlanetInformations());
                    PlanetPureFunctions.findPlanetInfo(p.getName(), tempPlayer.getPlanetInformations()).setLastKnownTroopsNr(g.getTroopsNrOnPlanet(p, tempPlayer));
                    Logger.finest("setLastKnownTroopsNr: " + p.getName() + ", " + g.getTroopsNrOnPlanet(p, tempPlayer));
                }
            }
        }
    }

    protected String createBuildingString(List<Building> buildings) {
        StringBuffer sb = new StringBuffer();
        Logger.finest("buildings: " + buildings.size());
        for (Building building : buildings) {
            Logger.finest("sb: " + sb + " sb.length(): " + sb.length());
            if (sb.length() > 0) {
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
    protected void checkInfestationFromShips() {
        Logger.fine("checkInfestationFromShips called");
        List<Planet> planets = g.getPlanets();
        for (Planet planet : planets) {
            if (planet.isRazedAndUninfected()) {
                List<Player> aliensPresent = getAliensWithPsychWarfare(planet);
                if (aliensPresent.size() == 1) {
                    Player infestator = aliensPresent.get(0);
                    infestator.addToHighlights(planet.getName(), HighlightType.TYPE_PLANET_INFESTATED);
                    infestator.addToGeneral("You have infected the planet " + planet.getName());
                    planet.setProd(0);
                    planet.setResistance(1 + infestator.getFaction().getResistanceBonus());
                    planet.setPlayerInControl(infestator);
                    if (planet.isHasNeverSurrendered()) {
                        planet.setHasNeverSurrendered(false);
                        // l�gg till en slumpvis VIP till infestator spelaren
                        VIP aVIP = VipMutator.maybeAddVIP(infestator, infestator.getGalaxy());
                        if (aVIP != null) {
                            VipMutator.setShipLocation(aVIP, planet);
                            infestator.addToVIPReport("When you conquered " + planet.getName() + " you have found a " + VipPureFunctions.getVipTypeByKey(aVIP.getTypeKey(), g.getGameWorld()).getName() + " who has joined your service.");
                            infestator.addToHighlights(VipPureFunctions.getVipTypeByKey(aVIP.getTypeKey(), g.getGameWorld()).getName(), HighlightType.TYPE_VIP_JOINS);
                        }
                    }
                } else if (aliensPresent.size() > 1) {
                    for (Player player : aliensPresent) {
                        player.addToGeneral("You have not infected the planet " + planet.getName() + " because there are other players in the same system who also wants to infect the planet.");
                    }
                }
            }
        }
    }

    protected List<Player> getAliensWithPsychWarfare(Planet aPlanet) {
        List<Player> playersPresent = new LinkedList<Player>(); // aliens with troops present
        List<Player> allPlayers = g.getPlayers();
        for (Player player : allPlayers) {
            if (!player.isDefeated() & player.isAlien()) {
                List<Spaceship> playersShipsAtPlanet = SpaceshipPureFunctions.getPlayersSpaceshipsOnPlanet(player, aPlanet, g.getSpaceships());
                boolean pw = false;
                for (Spaceship spaceship : playersShipsAtPlanet) {
                    if (SpaceshipPureFunctions.getSpaceshipTypeByKey(spaceship.getTypeKey(), g.getGameWorld()).getPsychWarfare() > 0) {
                        pw = true;
                    }
                }
                if (pw) {
                    playersPresent.add(player);
                }
            }
        }
        return playersPresent;
    }

    protected void updateSquadronsLocation() {
        Logger.fine("updateSquadronsLocation called");
        List<Spaceship> allss = g.getSpaceships();
        for (Spaceship aSpaceship : allss) {
            updateSquadronLocation(aSpaceship);
        }

    }

    private void updateSquadronLocation(Spaceship spaceship) {
        if (spaceship.getSize() == SpaceShipSize.SQUADRON) {
            if (spaceship.getCarrierLocation() != null) {
                spaceship.setLocation(spaceship.getCarrierLocation().getLocation());
            }
        }
    }

    protected void moveRetreatingShips() {
        Logger.fine("moveRetreatingShips called");
        List<Spaceship> allss = g.getSpaceships();
        for (Player tempPlayer : g.players) {
            Logger.finer("player: " + tempPlayer);
            int genSize = tempPlayer.getTurnInfo().getGeneralSize();
            // move all who can move on their own, except squadrons in a carrier
            for (int i = 0; i < allss.size(); i++) {
                Spaceship ss = allss.get(i);
                if (ss.getOwner() == tempPlayer) {
                    if (ss.isRetreating()) {
                        if (SpaceshipPureFunctions.getRange(ss, g).canMove()) {
                            if (ss.getCarrierLocation() == null) { // only squadrons can have a carrier location
                                Logger.finest("moveRetreatingShip: " + ss);
                                SpaceshipHelper.moveShip(ss, ss.getRetreatingTo().getName(), ss.getOwner().getTurnInfo(), g);
                            }
                        }
                    }
                }
            }
            // move squadrons in carriers (the only ones not moved above)
            for (int i = 0; i < allss.size(); i++) {
                Spaceship ss = (Spaceship) allss.get(i);
                if (ss.getOwner() == tempPlayer) {
                    if (ss.isRetreating()) {
                        if (ss.getCarrierLocation() != null) { // is in a carrier, only squadron can be in a carrier
                            SpaceshipHelper.moveRetreatingSquadron(ss, ss.getOwner().getTurnInfo(), g);
                        }
                    }
                }
            }
            if (genSize < tempPlayer.getTurnInfo().getGeneralSize()) {
                tempPlayer.addToGeneral("");
            }
        }
    }

    protected void performShipRepairs() {
        Logger.fine("performShipRepairs called");
        List<Spaceship> allss = g.getSpaceships();
        for (Spaceship ss : allss) {  // gå igenom alla rymdskepp
            if (ss.getCurrentDc() < ss.getDamageCapacity()) {  // skeppet är skadat
                Planet location = ss.getLocation();
                if (location != null) {  // skeppet är ej på flykt
                    if (location.getPlayerInControl() == ss.getOwner()) {  // skeppet är vid en av spelarens planeter
                        if (SpaceshipPureFunctions.getSpaceshipTypeByKey(ss.getTypeKey(), g.getGameWorld()).getSize().getSlots() <= location.getMaxWharfsSize()) {  // det finns ett skeppsvarv som är tillräckligt stort för att reparera skeppet
                            SpaceshipMutator.performRepairs(ss);
                        }
                    }
                }
            }
        }
    }

    protected void performTroopRepairs() {
        Logger.fine("performTroopRepairs called");
        for (int i = 0; i < g.getPlayers().size(); i++) {
            Player aPlayer = (Player) g.getPlayers().get(i);
            boolean repairHasBeenPerformed = false;
            List<Troop> allTroops = TroopPureFunctions.getPlayersTroops(aPlayer, g);
            for (Troop aTroop : allTroops) {
                if (aTroop.getCurrentDamageCapacity() < aTroop.getDamageCapacity()) {
                    if (aTroop.getPlanetLocation() != null) {
                        if (aTroop.getPlanetLocation().getPlayerInControl() == aTroop.getOwner() && !g.isOngoingGroundBattle(aTroop.getPlanetLocation(), aTroop.getOwner())) {
                            TroopMutator.performRepairs(aTroop, 0.25);
                            repairHasBeenPerformed = true;
                        }
                    } else {
                        // troop in carrier
                        Spaceship aCarrier = aTroop.getShipLocation();
                        if (aCarrier.getLocation() != null) {
                            if (aCarrier.getLocation().getPlayerInControl() == aTroop.getOwner()) {
                                // troop in acrrier at own planet
                                TroopMutator.performRepairs(aTroop, 0.15);
                                repairHasBeenPerformed = true;
                            } else {
                                // troop in acrrier at non-own planet
                                TroopMutator.performRepairs(aTroop, 0.05);
                                repairHasBeenPerformed = true;
                            }
                        } else {
                            // carrier is retreating
                            TroopMutator.performRepairs(aTroop, 0.05);
                            repairHasBeenPerformed = true;
                        }
                    }
                }
            }
            if (repairHasBeenPerformed) {
                aPlayer.addToGeneral("");
            }
        } // end for loop
    }

    protected void performResupply() {
        Logger.fine("performResupply called");
        List<Spaceship> allss = g.getSpaceships();
        for (Spaceship ss : allss) {  // gå igenom alla rymdskepp
            if (SpaceshipPureFunctions.getNeedResupply(ss)) {  // skeppet är skadat
                Planet location = ss.getLocation();
                if (location != null) {  // skeppet är ej på flykt
                    if (location.getPlayerInControl() == ss.getOwner()) {  // skeppet är vid en av spelarens planeter
                        // max repair at wharfs is same as resupply level
                        int maxResupplySize = location.getMaxWharfsSize();
                        SpaceshipMutator.supplyWeapons(ss, SpaceShipSize.createFromSlots(maxResupplySize));
                    }
                    if (SpaceshipPureFunctions.getNeedResupply(ss)) { // skeppet är fortfarande i behov av resupply
                        // kolla efter supplyships
                        SpaceshipMutator.supplyWeapons(ss, SpaceshipPureFunctions.getMaxResupplyFromShip(location, ss.getOwner(), g));
                    }
                }
            }
        }
    }

    protected void updateTreasury() {
        Logger.fine("updateTreasury called");
        int tempIncome;
        for (int x = 0; x < g.players.size(); x++) {
            Player temp = (Player) g.players.get(x);
            if (!temp.isDefeated()) {
                tempIncome = IncomePureFunctions.getPlayerIncome(temp, false);
                Logger.finer("Add to treasury: " + tempIncome + " for player " + temp.getGovernorName());
                temp.addToTreasury(tempIncome);
            }
        }
    }

    protected void payUpkeepShips() {
        Logger.fine("payUpkeepShips called");
        int tempUpkeep;
        for (int x = 0; x < g.players.size(); x++) {
            Player temp = (Player) g.players.get(x);
            if (!temp.isDefeated()) {
                tempUpkeep = CostPureFunctions.getPlayerUpkeepShips(temp, g.getPlanets(), g.getSpaceships());
                temp.removeFromTreasury(tempUpkeep);
            }
        }
    }

    protected void payUpkeepVIPs() {
        Logger.fine("payUpkeepVIPs called");
        int tempUpkeep;
        for (int x = 0; x < g.players.size(); x++) {
            Player temp = (Player) g.players.get(x);
            if (!temp.isDefeated()) {
                tempUpkeep = CostPureFunctions.getPlayerUpkeepVIPs(temp, g.getAllVIPs());
                temp.removeFromTreasury(tempUpkeep);
            }
        }
    }

    protected void payUpkeepTroops() {
        Logger.fine("payUpkeepTroops called");
        int tempUpkeep;
        for (int x = 0; x < g.players.size(); x++) {
            Player aPlayer = (Player) g.players.get(x);
            if (!aPlayer.isDefeated()) {
                tempUpkeep = CostPureFunctions.getPlayerUpkeepTroops(aPlayer, g.getPlanets(), g.getTroops());
                aPlayer.removeFromTreasury(tempUpkeep);
            }
        }
    }

    protected void writeUpkeepInfo() {
        Logger.fine("writeUpkeepInfo called");
        for (int x = 0; x < g.players.size(); x++) {
            Player tempPlayer = (Player) g.players.get(x);
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
*/
            if (CostPureFunctions.isBroke(tempPlayer, g)) {
//            allStrings.insertElementAt("WARNING: Upkeep exceeds income. You are broke!",6);
//            allStrings.insertElementAt("Until upkeep gets below income you cannot move any ship or VIP, or have any expenses.",7);
                tempPlayer.addToHighlights("", HighlightType.TYPE_BROKE);
                EconomyReport er = tempPlayer.getTurnInfo().getLatestEconomyReport();
                er.setBrokeNextTurn(true);
            }
        }
    }

    protected void defeatedPlayers() {
        Logger.fine("defeatedPlayers called");
        for (int x = 0; x < g.players.size(); x++) {
            Player tempPlayer = (Player) g.players.get(x);
            if (!tempPlayer.isDefeated()) {
                // r�kna antalet planeter spelaren har
                boolean noPlanet = checkNoPlanet(tempPlayer, g);
//          boolean noPlanet = true;
//          for (int i = 0; i < g.planets.size();i++){
//            Planet p = (Planet)g.planets.get(i);
//            if (p.getPlayerInControl() == tempPlayer){
//              noPlanet = false;
//            }
//          }
                // kolla att spelaren fortfarande har kvar sin Guvern�r
                // eller
                // om spelaren har planeter
                if (noPlanet | VipPureFunctions.findVIPGovernor(tempPlayer, g) == null) {
                    tempPlayer.defeated(VipPureFunctions.findVIPGovernor(tempPlayer, g) == null, g.getTurn());
                    g.removeVIPs(tempPlayer);
                    // if the gov has been killed there might exist a lot of ships and planets
                    // that should be made neutral or be removed
                    if (VipPureFunctions.findVIPGovernor(tempPlayer, g) == null) {
                        if (tempPlayer.isAlien()) {
                            // remove all ships
                            removeShipsDefeatedAlienPlayer(g, tempPlayer);
                            // set all players planets as razed
                            razePlanetsDefeatedAlienPlayer(g, tempPlayer);
                        } else {
                            // remove all ships not on own planets
                            removeShipsDefeatedPlayer(g, tempPlayer);
                            // make all other ships neutral
                            neutralizeShipsDefeatedPlayer(g, tempPlayer);
                            // make all planets neutral
                            neutralizePlanetsDefeatedPlayer(g, tempPlayer);
                        }
                    }
                }
            }
        }
    }

    public boolean checkNoPlanet(Player aPlayer, Galaxy galaxy) {
        boolean noPlanet = true;
        for (int i = 0; i < galaxy.getPlanets().size(); i++) {
            Planet p = (Planet) galaxy.getPlanets().get(i);
            if (p.getPlayerInControl() == aPlayer) {
                noPlanet = false;
            }
        }
        return noPlanet;
    }

    protected void checkAbandonGame() {
        Logger.fine("checkAbandonGame called");
        for (int x = 0; x < g.players.size(); x++) {
            Player tempPlayer = (Player) g.players.get(x);
            if (!tempPlayer.isDefeated()) {
                if (tempPlayer.getOrders().isAbandonGame()) {
                    tempPlayer.abandonGame(g.getTurn());
                    removePlayer(tempPlayer);
                }
            }
        }
    }

    protected void checkRepeatedBroke() {
        Logger.fine("checkRepeatedBroke called");
        for (int x = 0; x < g.players.size(); x++) {
            Player tempPlayer = (Player) g.players.get(x);
            if (!tempPlayer.isDefeated()) {
                if (CostPureFunctions.isBroke(tempPlayer, g)) {
                    tempPlayer.incNrTurnsBroke();
                    if (tempPlayer.getNrTurnsBroke() == 5) {
                        tempPlayer.brokeRemovedFromGame(g.getTurn());
                        removePlayer(tempPlayer);
                    } else if (tempPlayer.getNrTurnsBroke() == 4) {
                        tempPlayer.brokeRemovedWarning();
                    }
                } else {
                    tempPlayer.setNrTurnsBroke(0);
                }
            }
        }
    }

    protected void removePlayer(Player tempPlayer) {
        // remove all vips
        g.removeVIPs(tempPlayer);
        if (tempPlayer.isAlien()) {
            removeShipsDefeatedAlienPlayer(g, tempPlayer);
            //	g.removeWharfsDefeatedAlienPlayer(tempPlayer);
            g.removeBuildingsDefeatedAlienPlayer(tempPlayer);
            razePlanetsDefeatedAlienPlayer(g, tempPlayer);
        } else {
            // remove all ships not on own planets
            removeShipsDefeatedPlayer(g, tempPlayer);
            // make all other ships neutral
            neutralizeShipsDefeatedPlayer(g, tempPlayer);
            // make all planets neutral
            neutralizePlanetsDefeatedPlayer(g, tempPlayer);
        }

    }

    protected void removeShipsDefeatedPlayer(Galaxy g, Player defeatedPlayer) {
        List<Spaceship> allShipsList = g.getPlayersSpaceships(defeatedPlayer);
        HashSet<Planet> planetsNoLongerBesieged = new HashSet<Planet>();
        for (Spaceship aShip : allShipsList) {
            Planet location = aShip.getLocation();
            if (location == null) {
                // ship is retreating, remove it
                SpaceshipMutator.removeShip(aShip, g);
            } else if (location.getPlayerInControl() != defeatedPlayer) {
                planetsNoLongerBesieged.add(location);
                // ship is not at own planet, remove it
                SpaceshipMutator.removeShip(aShip, g);
            }
        }
        checkBesiegedPlanets(planetsNoLongerBesieged);
    }

    protected void removeShipsDefeatedAlienPlayer(Galaxy g, Player defeatedPlayer) {
        List<Spaceship> allShipsList = g.getPlayersSpaceships(defeatedPlayer);
        HashSet<Planet> planetsNoLongerBesieged = new HashSet<Planet>();
        for (Spaceship aShip : allShipsList) {
            Planet location = aShip.getLocation();
            SpaceshipMutator.removeShip(aShip, g);
            if (location != null) {
                if (location.getPlayerInControl() != defeatedPlayer) {
                    planetsNoLongerBesieged.add(location);
                }
            }
        }
        checkBesiegedPlanets(planetsNoLongerBesieged);
    }

    protected void checkBesiegedPlanets(HashSet<Planet> planetsNoLongerBesieged) {
        for (Iterator<Planet> iter = planetsNoLongerBesieged.iterator(); iter.hasNext(); ) {
            Planet aPlanet = (Planet) iter.next();
            if (aPlanet.isBesieged()) {
                aPlanet.setBesieged(false);
                List<Spaceship> hostileShips = g.getHostileShipsAtPlanet(aPlanet);
                if (hostileShips.size() != 0) {
                    for (Spaceship spaceship : hostileShips) {
                        if (spaceship.isCanBlockPlanet()) {
                            aPlanet.setBesieged(true);
                        }
                    }
                }
            }
        }
    }

    protected void neutralizeShipsDefeatedPlayer(Galaxy g, Player defeatedPlayer) {
        List<Spaceship> allShipsList = g.getPlayersSpaceships(defeatedPlayer);
        for (Spaceship aShip : allShipsList) {
            // make ship neutral
            aShip.setOwner(null);
        }
    }

    protected void neutralizePlanetsDefeatedPlayer(Galaxy g, Player defeatedPlayer) {
        List<Planet> playerPlanets = g.getPlayersPlanets(defeatedPlayer);
        for (Planet aPlanet : playerPlanets) {
            // make planet neutral
            aPlanet.setPlayerInControl(null);
            // remove any buildings that should autodestruct when conquered (or neutralized)
            aPlanet.destroyBuildingsThatCanNotBeOverTaked(null);
            PlanetMutator.setLastKnownOwner(aPlanet.getName(), "Neutral", g.turn + 1, defeatedPlayer.getPlanetInformations());
        }
    }

    protected void razePlanetsDefeatedAlienPlayer(Galaxy g, Player defeatedPlayer) {
        List<Planet> playerPlanets = g.getPlayersPlanets(defeatedPlayer);
        for (Planet aPlanet : playerPlanets) {
            aPlanet.setRazed();
            PlanetMutator.setLastKnownOwner(aPlanet.getName(), "Neutral", g.turn + 1, defeatedPlayer.getPlanetInformations());
        }
    }

    protected void performOrders() {
        Logger.fine("performOrders called");
        List<Player> tempPlayers = new ArrayList<Player>();
        tempPlayers.addAll(g.players);

        while (tempPlayers.size() > 0) {
            int random = Functions.getRandomInt(0, tempPlayers.size() - 1);

            Player temp = (Player) tempPlayers.get(random);
            int genSize = temp.getTurnInfo().getGeneralSize();
            if (!temp.isDefeated()) {
                OrdersPerformer.performOrders(temp.getOrders(), temp.getTurnInfo(), temp, g);
            }
            if (genSize < temp.getTurnInfo().getGeneralSize()) {
                temp.addToGeneral("");
            }
            tempPlayers.remove(random);
        }

    }

    public boolean allPlanetsRazedAndUninfected() {
        boolean notRazed = false;
        for (int i = 0; i < g.planets.size(); i++) {
            Planet p = (Planet) g.planets.get(i);
            if (!p.isRazedAndUninfected()) {
                notRazed = true;
            }
        }
        return !notRazed;
    }


    protected void peaceOnAllPlanets() {
        for (Planet planet : g.planets) {
            planet.peace();
        }
    }

    protected void checkSpaceBattles() {
        Logger.fine("checkSpaceBattles called");
        // leta igenom alla planeter
        for (int i = 0; i < g.planets.size(); i++) {
            Planet tempPlanet = g.planets.get(i);
            Logger.finest("Planet loop: " + tempPlanet.getName());
            List<TaskForce> taskforces = new ArrayList<TaskForce>();

            taskforces = TaskForceHandler.getTaskForces(tempPlanet, false, g);
            // kolla om det blir några konflikter (rymdstrider och belägringar)
            if (taskforces.size() > 0) {
                Logger.finer("TaskForces > 0, size: " + taskforces.size() + " " + tempPlanet.getName());
                checkConflicts(taskforces, tempPlanet);
            }
        }
        Logger.finer("checkSpaceBattles finished");
    }

    protected void checkConflicts(List<TaskForce> taskforces, Planet aPlanet) {
        Logger.finer("Taskforces.size = " + taskforces.size());
        if (taskforces.size() > 1) { // kan bli rymdstrider
            Logger.finer("Taskforces > 2, " + taskforces.size());
            Collections.shuffle(taskforces);
            defendersFirst(taskforces, aPlanet);
            checkSpaceshipBattles(taskforces, aPlanet, 0, 1);
        }
        // remove abandoned squadrons in taskforces
        checkAbandonedSquadrons(taskforces, aPlanet);
        // check if there are any abandoned troops
        //g.checkAbandonedTroops(aPlanet);
        // if there are at least one TF left at planet
        if (taskforces.size() > 0) {
            // check for siege, blockade and landbattles
            checkSiege(taskforces, aPlanet);
        }
    }

    protected void checkAbandonedSquadrons(List<TaskForce> taskforces, Planet aPlanet) {
        // remove abandoned squadrons in taskforces
        // if a taskforce becomes empty because of abandoned squadrons, remove
        //	the taskforce
        for (int i = taskforces.size() - 1; i >= 0; i--) {
            TaskForce tmpTF = taskforces.get(0);
            // check the ships in the TF
            (new CheckAbandonedSquadrons(g)).checkAbandonedSquadrons(tmpTF, aPlanet);
            if (tmpTF.getTotalNrNonDestroyedShips() == 0) {
                taskforces.remove(tmpTF);
            }
        }
    }


    // metod som kollar om det blir några rymdstrider vid en given planet
    protected void checkSpaceshipBattles(List<TaskForce> taskforces, Planet aPlanet, int curtf_low, int curtf_high) {
        Logger.finer("checkSpaceshipBattles: taskforces.size(): " + taskforces.size() + " " + curtf_low + " " + curtf_high);
        TaskForce defenderTF = null;
        if (g.getPlayerByGovenorName(taskforces.get(0).getPlayerName()) == aPlanet.getPlayerInControl()) { // bryt ut ev. försvarare så den kan slåss sist
            defenderTF = taskforces.get(0);
            taskforces.remove(defenderTF);
        }
        Collections.shuffle(taskforces);
        for (TaskForce tf1 : taskforces) { // loopa igenom alla TF så att alla får chansen att slåss med varandra
            for (TaskForce tf2 : taskforces) {
                if (tf1 != tf2) { // så flottorna ej slåss mot sig själva :)
                    handleSpaceshipBattle(tf1, tf2, aPlanet);
                }
            }
        }
        if (defenderTF != null) {
            for (TaskForce tf : taskforces) {
                handleSpaceshipBattle(tf, defenderTF, aPlanet);
            }
        }
        // ta bort besegrade tf
        int index = 0;
        while (index < taskforces.size()) {
            if (taskforces.get(index).getTotalNrShips() == 0) {
                taskforces.remove(index);
            } else {
                index++;
            }
        }
        // ev, lägg tillbaka defender om de finns och ej är besegrade
        if (defenderTF != null) {
            if (defenderTF.getTotalNrShips() > 0) {
                taskforces.add(0, defenderTF);
            }
        }
        Logger.finer("checkSpaceshipBattles finished");
    }

    protected void handleSpaceshipBattle(TaskForce tf1, TaskForce tf2, Planet aPlanet) {
        if ((tf1.getTotalNrShips() > 0) & (tf2.getTotalNrShips() > 0)) { // om någon av flottorna har slut på skepp är den redan besegrad
            if (hostile(tf1, tf2, aPlanet)) {  // at least one of the tf:s want to fight
                Logger.finest("Hostile!");


                (new SpaceBattlePerformer()).performCombat(tf1, tf2, g.getGameWorld().getInitMethod(), aPlanet.getName(), g.getGameWorld(), g);

                // 2019-12-26 Hantera detta, behöver vi detta? eller kan vi räkna ihop alla skepp nu när de ligger i SpaceBattleAttack. Får vara kvar ett tag till då enheter i listan används både av servern och klienten.
                if (tf1.getPlayerName() != null) {
                    tf1.getDestroyedShips().stream().map(ship -> ship.getSpaceship()).forEach(destroyedShip -> SpaceshipHelper.addToLatestShipsLostInSpace(destroyedShip, g.getPlayerByGovenorName(tf1.getPlayerName()).getTurnInfo(), g.getGameWorld()));
                    tf2.getDestroyedShips().stream().map(ship -> ship.getSpaceship()).forEach(destroyedShip -> SpaceshipHelper.addToLatestShipsLostInSpace(destroyedShip, g.getPlayerByGovenorName(tf1.getPlayerName()).getTurnInfo(), g.getGameWorld()));
                }

                if (tf2.getPlayerName() != null) {
                    tf1.getDestroyedShips().stream().map(ship -> ship.getSpaceship()).forEach(destroyedShip -> SpaceshipHelper.addToLatestShipsLostInSpace(destroyedShip, g.getPlayerByGovenorName(tf2.getPlayerName()).getTurnInfo(), g.getGameWorld()));
                    tf2.getDestroyedShips().stream().map(ship -> ship.getSpaceship()).forEach(destroyedShip -> SpaceshipHelper.addToLatestShipsLostInSpace(destroyedShip, g.getPlayerByGovenorName(tf2.getPlayerName()).getTurnInfo(), g.getGameWorld()));
                }

                highlightsSpaceBattle(tf1.getTotalNrShips() > 0 ? tf1 : tf2, tf1.getTotalNrShips() > 0 ? tf2 : tf1, aPlanet);

                //2019-12-30 Removing destroyed ships.
                tf1.getDestroyedShips().stream().map(ship -> ship.getSpaceship()).forEach(destroyedShip -> SpaceshipMutator.removeShip(destroyedShip, g));
                tf2.getDestroyedShips().stream().map(ship -> ship.getSpaceship()).forEach(destroyedShip -> SpaceshipMutator.removeShip(destroyedShip, g));

                //TODO 2019-12-26 Flyttad från SpaceBattlePerformer Undersök även möjligheten om skölderna ska återställas när alla strider är genomförda d.v.s. om en flotta slåss fler än en gång så kommer den inte få ladda om sin sköld i mellan. Är det bra eller dåligt? den vinnande flottan kommer då vara svagare i nästa strid. Flytta till checkSpaceshipBattles, metoden som anroppar den här, lägg i så fall logiken när alla strider på planeten är genomförda.
                //TODO 2019-12-26 Dock ska troligen förstörda och skepp som har flytt nollställas om samma TF kan användas igen.
                tf1.restoreShieldsAndCleanDestroyedAndRetreatedLists(getGalaxy().getGameWorld());
                tf2.restoreShieldsAndCleanDestroyedAndRetreatedLists(getGalaxy().getGameWorld());

                // reload winning sides squadrons if they have a carrierLocation
                if (tf1.getTotalNrShips() > 0) {
                    tf1.reloadSquadrons();
                }
                if (tf2.getTotalNrShips() > 0) {
                    tf2.reloadSquadrons();
                }

                addSpaceBattleReport(tf1, aPlanet);
                addSpaceBattleReport(tf2, aPlanet);

            }
        }
    }

    private void highlightsSpaceBattle(TaskForce tfWinner, TaskForce tfLoser, Planet aPlanet) {
        if (tfLoser.getRetreatedShips().size() == 0) {
            // the loser dig not retreat with any ships (all destroyed)
            if (tfWinner.getPlayerName() != null) {
                g.getPlayerByGovenorName(tfWinner.getPlayerName()).addToHighlights(aPlanet.getName(), HighlightType.TYPE_BATTLE_WON);
            }
            if (tfLoser.getPlayerName() != null) {
                g.getPlayerByGovenorName(tfLoser.getPlayerName()).addToHighlights(aPlanet.getName(), HighlightType.TYPE_BATTLE_LOST);
            }
        } else if (tfLoser.getDestroyedShips().size() == 0) {
            // the loser did not lose any ships (all retreated)
            if (tfWinner.getPlayerName() != null) {
                g.getPlayerByGovenorName(tfWinner.getPlayerName()).addToHighlights(aPlanet.getName(), HighlightType.TYPE_RETREAT_IN_COMBAT_ENEMY);
            }
            if (tfLoser.getPlayerName() != null) {
                g.getPlayerByGovenorName(tfLoser.getPlayerName()).addToHighlights(aPlanet.getName(), HighlightType.TYPE_RETREAT_IN_COMBAT_OWN);
            }
        } else {
            // else = partial retreat
            if (tfWinner.getPlayerName() != null) {
                g.getPlayerByGovenorName(tfWinner.getPlayerName()).addToHighlights(aPlanet.getName(), HighlightType.TYPE_BATTLE_WON_PARTIAL_RETREAT);
            }
            if (tfLoser.getPlayerName() != null) {
                g.getPlayerByGovenorName(tfLoser.getPlayerName()).addToHighlights(aPlanet.getName(), HighlightType.TYPE_BATTLE_LOST_PARTIAL_RETREAT);
            }
        }

    }

    private void addSpaceBattleReport(TaskForce taskForce, Planet planet) {
        if (taskForce.getPlayerName() != null) {
            Player player = g.getPlayerByGovenorName(taskForce.getPlayerName());
            Optional<PlanetReport> optional = player.getPlayerReports().get(g.getTurn() - 1).getChildReportsOfType(PlanetReport.class).stream()
                    .filter(planetReport -> planetReport.getPlanetName().equals(planet.getName())).findAny();
            if (optional.isPresent()) {
                optional.get().getSpaceBattleReports().add(taskForce.getSpaceBattleReport());
            } else {
                PlanetReport planetReport = new PlanetReport(planet.getName());
                planetReport.getSpaceBattleReports().add(taskForce.getSpaceBattleReport());
                player.getPlayerReports().get(g.getTurn() - 1).getPlanetReports().add(planetReport);

            }
        }
    }

    protected boolean hostile(TaskForce tf1, TaskForce tf2, Planet aPlanet) {
        Logger.finer("hostile: " + aPlanet.getName());
        boolean hostile = false;
        // kolla först om ena tf:n är neutral
        if (tf1.getPlayerName() == null) {
            Logger.finer("tf1 är neutral");
            hostile = PlanetOrderStatusPureFunctions.isAttackIfNeutral(aPlanet.getName(), g.getPlayerByGovenorName(tf2.getPlayerName()).getPlanetOrderStatuses());
            Logger.finer("hostile, attack if neutral:" + hostile);
        } else
            // eller den andra...
            if (tf2.getPlayerName() == null) {
                Logger.finer("tf2 är neutral");
                hostile = PlanetOrderStatusPureFunctions.isAttackIfNeutral(aPlanet.getName(), g.getPlayerByGovenorName(tf1.getPlayerName()).getPlanetOrderStatuses());
                Logger.finer("hostile, attack if neutral:" + hostile);
            } else   // bägge flottorna tillhör spelare
//      if (tf1.getPlayer().getFaction() != tf2.getPlayer().getFaction()){ // här skulle man kunna kolla på diplomatiska status istället om de existerade...
                if (DiplomacyPureFunctions.hostileDuelists(g.getPlayerByGovenorName(tf1.getPlayerName()), g.getPlayerByGovenorName(tf2.getPlayerName()), aPlanet, g.getDiplomacyStates())) { // check diplomaticState..
                    Logger.finer("Diplomacy say fight!");
                    hostile = true;
                    Logger.finer("Hostile, end of diff fac: " + hostile);
                }
        Logger.finer("Hostile finished: " + hostile);
        return hostile;
    }

    protected void defendersFirst(List<TaskForce> taskforces, Planet aPlanet) {
        int found = -1;
        TaskForce temptf = null;
        Player tempPlayer = null;
        for (int i = 0; i < taskforces.size(); i++) {
            temptf = (TaskForce) taskforces.get(i);
            tempPlayer = g.getPlayerByGovenorName(temptf.getPlayerName());
            if (aPlanet.getPlayerInControl() == tempPlayer) {
                found = i;
            }
        }
        if (found > -1) { // flytta försvararna till vectorns första plats
            temptf = (TaskForce) taskforces.get(found);
            taskforces.remove(found);
            taskforces.add(0, temptf);
        }
    }

    /**
     * Called after spaceship battles are finished.
     * There may be more than 1 TF at planet
     * <p>
     * Here is the complete algorithm
     * ------------------------------
     * <p>
     * // defenders have no TF at planet
     * // attackers are more than one
     * // blockade
     * // else (only 1 attacking TF)
     * // check destroy wharfs
     * // bombardment
     * // defender is alien
     * // siege
     * // defender have troops
     * // troop bombardment
     * // planet razed
     * // defender have troops
     * // remove defending troops
     * // attacker is alien
     * // attacker have troops ability
     * // planet conquered by alien
     * // not razed, defender have no troops
     * // attacker have troops
     * // defender is alien
     * // attacker is alien
     * // planet conquered by alien
     * // attacker is not alien
     * // planet is razed
     * // defender is not alien
     * // attacker is alien
     * // planet conquered by alien
     * // attacker is not alien
     * // planet conquered
     * // attacker have no troops
     * // defender is alien
     * // resistance < 1
     * // planet is razed
     * // attacker is alien
     * // attacker have troops ability
     * // planet conquered by alien
     * // else
     * // planet under siege but still holding
     * // defender is not alien
     * // check if resistance have been lowered
     * // attacker is alien
     * // production < 1
     * // attacker have troops ability
     * // planet conquered by alien
     * // else
     * // planet is razed
     * // else
     * // planet under siege but still holding
     * // attacker is not alien
     * // resistance < 1
     * // planet conquered
     * // else
     * // planet under siege but still holding
     * // resistance not lowered
     * // defender have troops
     * // attacker have troops
     * // land battle
     * // if attacker win (no defending troops left)
     * // planet conquered
     * // if inconclusive (both have troops left)
     * // planet under siege but still holding
     * // if defender won (no attackers left)
     * // planet under siege but still holding
     * // attacker have no troops
     * // planet under siege but still holding
     *
     * @param taskforces
     * @param aPlanet
     */
    protected void checkSiege(List<TaskForce> taskforces, Planet aPlanet) {
        Logger.finer("checkSiege: " + taskforces.size() + " " + aPlanet.getName());
        if (!aPlanet.isRazedAndUninfected()) { // first check that the planet isn't razed and uninhabited. Otherwise there are no siege
            // check if defenders have no TF at planet
            TaskForce firstTF = taskforces.get(0);
            Player attackingPlayer = g.getPlayerByGovenorName(firstTF.getPlayerName());
            if (g.getPlayerByGovenorName(firstTF.getPlayerName()) != aPlanet.getPlayerInControl()) {
                Logger.finer("First taskforce does not own the planet.");
                // check if attackers are more than one TF
                List<TaskForce> tfsWantingToBesiege = LandBattleHelper.countBesiegingTFs(taskforces, aPlanet, g);
                if (tfsWantingToBesiege.size() > 0) {
                    // logik f�r cannon fire. Om alla skepp d�r s� upph�r bel�gringen.
                    performCannonDefenceFire(aPlanet, tfsWantingToBesiege);
                    // recount and check if any TF can besiege.
                    tfsWantingToBesiege = LandBattleHelper.countBesiegingTFs(taskforces, aPlanet, g);
                }
                if (tfsWantingToBesiege.size() < 1) {
                    // alla flottor �r d�da
                } else if (tfsWantingToBesiege.size() > 1) {  // if more than one TF wants to besiege planet is blocked
                    Logger.finer("More than one taskforce in orbit = Blockade!");
                    // blockade
                    underBlockade(aPlanet, tfsWantingToBesiege, g);

                } else { // else (only 1 attacking TF)
                    Logger.finer("One taskforce in orbit that wants to besiege");
                    // find besieging taskforce
                    firstTF = tfsWantingToBesiege.get(0);
                    attackingPlayer = g.getPlayerByGovenorName(firstTF.getPlayerName());

                    (new PlanetUpdater()).checkDestroyBuildings(aPlanet, g.getPlayerByGovenorName(firstTF.getPlayerName()), false);

                    // siege with psywarfare
                    int resSiege = 0;
                    // siege, psyWarfare should work the same for both non-aliens and aliens
                    resSiege = besieged(aPlanet, firstTF, g);

                    // bombardment
                    int resBomb = underBombardment(aPlanet, firstTF, g);

                    Player defPlayer = aPlanet.getPlayerInControl();
                    List<Troop> defTroops = TroopPureFunctions.getTroopsOnPlanet(aPlanet, defPlayer, g.getTroops());
                    if (TroopPureFunctions.getTroopsOnPlanet(aPlanet, aPlanet.getPlayerInControl(), g.getTroops()).size() > 0) {
                        // perform bombardment against troops
                        bombardTroops(defPlayer, defTroops, attackingPlayer, resBomb, aPlanet);
                    }
                    // check if planet is razed
                    boolean infectedByAlien = aPlanet.getInfectedByAlien();
                    Logger.fine("1");
                    if ((aPlanet.getPopulation() < 1 && !infectedByAlien) || (aPlanet.getResistance() < 1 && infectedByAlien)) { // planet razed
                        // remove player on planet and set planet as razed
                        (new PlanetUpdater()).razed(aPlanet, g.getPlayerByGovenorName(firstTF.getPlayerName()));
                        // check if defender have troops on planet
                        if (TroopPureFunctions.getTroopsOnPlanet(aPlanet, aPlanet.getPlayerInControl(), g.getTroops()).size() == 0) {
                            //TODO Remove defending troops, no troops can survive on razed planets
                        }
                        // check if attacker is alien
                        if (g.getPlayerByGovenorName(firstTF.getPlayerName()).isAlien()) {
                            boolean psychExist = getMaxPsychWarfare(aPlanet, g.getPlayerByGovenorName(firstTF.getPlayerName()), g.getSpaceships(), g.getGameWorld()) > 0;
                            if (psychExist) { // attacker have psychWarfare ability
                                // planet conquered by alien
                                PlanetMutator.infectedByAttacker(aPlanet, attackingPlayer, g.getGameWorld());
                            }
                        }
                    } else {
                        Logger.fine("2");
                        if ((resSiege + resBomb) == 0 && !g.getGameWorld().isTroopGameWorld()) {
                            resistanceNotLowered(aPlanet, firstTF, g);
                        }

                        List<Troop> allTroopsOnPlanet = TroopPureFunctions.findAllTroopsOnPlanet(g.getTroops(), aPlanet);
                        if (allTroopsOnPlanet.size() == 0) {
                            Logger.fine("No defending troops");

                            // check if defender is alien
                            if (aPlanet.getInfectedByAlien()) {
                                // check if resistance < 1
                                if (PlanetPureFunctions.checkSurrender(aPlanet, g)) {
                                    // planet is razed
                                    (new PlanetUpdater()).razed(aPlanet, g.getPlayerByGovenorName(firstTF.getPlayerName()));
                                    // check if attacker is alien
                                    if (g.getPlayerByGovenorName(firstTF.getPlayerName()).isAlien()) {
                                        boolean psychExist = getMaxPsychWarfare(aPlanet, g.getPlayerByGovenorName(firstTF.getPlayerName()), g.getSpaceships(), g.getGameWorld()) > 0;
                                        if (psychExist) { // attacker have psychWarfare ability
                                            // planet conquered by alien
                                            PlanetMutator.infectedByAttacker(aPlanet, attackingPlayer, g.getGameWorld());
                                        }
                                    }
                                } else { // planet under siege but still holding
                                    holding(aPlanet, firstTF, g);
                                }
                            } else { // defender is not alien
                                // check if attacker is alien
                                if (g.getPlayerByGovenorName(firstTF.getPlayerName()).isAlien()) {
                                    // check if resistance < 1
                                    if (PlanetPureFunctions.checkSurrender(aPlanet, g)) {
                                        boolean psychExist = getMaxPsychWarfare(aPlanet, g.getPlayerByGovenorName(firstTF.getPlayerName()), g.getSpaceships(), g.getGameWorld()) > 0;
                                        if (psychExist) { // attacker have psychWarfare ability
                                            // planet conquered by alien
                                            PlanetMutator.infectedByAttacker(aPlanet, attackingPlayer, g.getGameWorld());
                                        } else { // no troops
                                            // planet is razed
                                            (new PlanetUpdater()).razed(aPlanet, g.getPlayerByGovenorName(firstTF.getPlayerName()));
                                        }
                                    } else { // planet under siege but still holding
                                        holding(aPlanet, firstTF, g);
                                    }
                                } else { // attacker is not alien
                                    Logger.fine("attacker is not alien");
                                    // check if planet surrenders
                                    if (PlanetPureFunctions.checkSurrender(aPlanet, g)) {
                                        // planet conquered
                                        (new PlanetUpdater()).planetSurrenders(aPlanet, firstTF, g);
                                    } else { // planet under siege but still holding
                                        holding(aPlanet, firstTF, g);
                                    }
                                }
                            }
                        } else {
                            aPlanet.setBesieged(true);
                        }
                    } // end else
                } // end defenders have no TF at planet
            } // end !aPlanet.isRazedAndUninfected()
        }
        Logger.finer("checkSiege finished.");

//	  performSiege(aPlanet,taskforces.get(0));

    }

    // blockad är som en belägring men utan att planeten tar skada eller kan ge sig. Den bara blockeras.
    public void underBlockade(Planet planet, List<TaskForce> alltf, Galaxy galaxy){
        planet.setBesieged(true);
        planet.setOpen(false);
        if (canBesiege(alltf)){
            if (planet.getPlayerInControl() != null){
                planet.getPlayerInControl().addToGeneral("The planet " + planet.getName() + " are being blockad by hostile forces.");
                for (int i = 0; i < alltf.size(); i++){
                    TaskForce temptf = alltf.get(i);
                    // kolla vilka som vill belägra?
                    galaxy.getPlayerByGovenorName(temptf.getPlayerName()).addToGeneral("The planet " + planet.getName() + ", are being blocked by you and ships from other factions.");
                }
            }else{
                for (int i = 0; i < alltf.size(); i++){
                    TaskForce temptf = alltf.get(i);
                    // kolla vilka som vill belägra?
                    galaxy.getPlayerByGovenorName(temptf.getPlayerName()).addToGeneral("The neutral planet " + planet.getName() + " are being blocked by you and ships from other factions.");
                }
            }
        }
    }

    public int besieged(Planet planet, TaskForce tf, Galaxy galaxy){
//    	Player oldPlayerInControl = null;
//        boolean hasFallen = false;
        planet.setOpen(false);
        if (planet.getPlayerInControl() != null){
            planet.getPlayerInControl().addToGeneral("The planet " + planet.getName() + " is under siege by the forces of Governor " + tf.getPlayerName() + ".");
            galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral("You are laying siege to the planet " + planet.getName() + ", belonging to Governor " + planet.getPlayerInControl().getGovernorName() + ".");
        }else{
            galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral("You are laying siege to the neutral planet " + planet.getName() + ".");
        }
        int oldRes = planet.getResistance();
//        boolean infectedByAlien = getInfectedByAlien();
        int psychWarfare = getMaxPsychWarfare(planet, galaxy.getPlayerByGovenorName(tf.getPlayerName()), galaxy.getSpaceships(), galaxy.getGameWorld());
        if (psychWarfare > 0){
            // Detta var nog en bugg:  res -= psychWarfare; stog två gånger.
            //  res -= psychWarfare;
            // TaskForce psychWarfare
            Logger.finer("psychWarfare: " + psychWarfare);
            planet.setResistance(planet.getResistance() - psychWarfare);
            if (planet.getPlayerInControl() != null){
                planet.getPlayerInControl().addToGeneral("While besieging your planet " + planet.getName() + " the psych warfare bonus in Governor " + tf.getPlayerName() + " (" + galaxy.getPlayerByGovenorName(tf.getPlayerName()).getFaction().getName() + ") fleet have lowered " + planet.getName() + "'s resistance by " + psychWarfare + ".");
                galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral("While besieging the planet " + planet.getName() + " belonging to Governor " + planet.getPlayerInControl().getGovernorName() + " (" + planet.getPlayerInControl().getFaction().getName() + ") the psych warfare bonus of your fleets ships have lowered its resistance by " + psychWarfare + ".");
            }else{
                galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral("While besieging the neutral planet " + planet.getName() + " the psych warfare bonus of the ships in your fleet have lowered its resistance by " + psychWarfare + ".");
            }
            // VIP psychWarfare bonus
            VIP psychWarfareBonusVIP = getPsychWarfareBonusVIPs(planet, galaxy.getPlayerByGovenorName(tf.getPlayerName()), galaxy);
            if (psychWarfareBonusVIP != null){
                VIPType vipType = VipPureFunctions.getVipTypeByKey(psychWarfareBonusVIP.getTypeKey(), g.getGameWorld());
                planet.setResistance(planet.getResistance() - vipType.getPsychWarfareBonus());
                if (planet.getPlayerInControl() != null){
                    planet.getPlayerInControl().addToGeneral("While besieging your planet " + planet.getName() + " the precence of a " + vipType.getName() + " in Governor " + tf.getPlayerName() + " (" + galaxy.getPlayerByGovenorName(tf.getPlayerName()).getFaction().getName() + ") fleet have lowered " + planet.getName() + "'s resistance by " + vipType.getPsychWarfareBonus() + ".");
                    galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral("While besieging the planet " + planet.getName() + " belonging to Governor " + planet.getPlayerInControl().getGovernorName() + " (" + planet.getPlayerInControl().getFaction().getName() + ") your " + vipType.getName() + " have lowered its resistance by " + vipType.getPsychWarfareBonus() + ".");
                }else{
                    galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral("While besieging the neutral planet " + planet.getName() + " your " + vipType.getName() + " have lowered its resistance by " + vipType.getPsychWarfareBonus() + ".");
                }
            }
        }
        return oldRes - planet.getResistance();
    }

    private boolean canBesiege(List<TaskForce> alltf){
        boolean canBesiege = false;
        int counter = 0;
        while ((counter < alltf.size()) & (!canBesiege)){
            TaskForce tf = alltf.get(counter);
            if (tf.canBesiege()){
                canBesiege = true;
            }else{
                counter++;
            }
        }
        return canBesiege;
    }

    private void resistanceNotLowered(Planet planet, TaskForce tf, Galaxy galaxy){
        // resistance has not been lowered, write feedback...
        galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral("Your forces has not lowered the resistance of the planet " + planet.getName() + " in any way.");
        if (planet.getPlayerInControl() != null){
            planet.getPlayerInControl().addToGeneral("The emeny forces has not lowered the planets resistance in any way.");
        }
    }


    private void holding(Planet planet, TaskForce tf, Galaxy galaxy){
        Logger.fine("holding");
        planet.setBesieged(true);
        if (planet.getPlayerInControl() != null){
            galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral(planet.getName() + " have not surrendered yet.");
            planet.getPlayerInControl().addToGeneral(planet.getName() + " is holding out and has " + (planet.getResistance() + VipPureFunctions.findHighestVIPResistanceBonus(planet, planet.getPlayerInControl(), galaxy)) + " left in resistance.");
        }else{
            galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral(planet.getName() + " have not surrenderad yet.");
        }
    }

    private void unchallengedDefendingTroopsBesieged(Planet planet, TaskForce tf, Galaxy galaxy){
        Logger.fine("unchallengedDefendingTroopsBesieged");
        planet.setBesieged(true);
        galaxy.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral("You are besieging " + planet.getName() + " but it will never surrender as long as it have defending troops left.");
        if (planet.getPlayerInControl() != null){
            planet.getPlayerInControl().addToGeneral(planet.getName() + " is holding out and has " + (planet.getResistance() + VipPureFunctions.findHighestVIPResistanceBonus(planet, planet.getPlayerInControl(), galaxy)) + " left in resistance.");
            planet.getPlayerInControl().addToGeneral("Since there are no attacking troops " + planet.getName() + " will never surrender as long as it have defending troops left.");
        }
    }

    protected void performCannonDefenceFire(Planet aPlanet, List<TaskForce> alltf) {
        for (int i = 0; i < aPlanet.getBuildings().size(); i++) {
            Building aBuilding = aPlanet.getBuildings().get(i);
            if (aBuilding.getBuildingType().getCannonDamage() > 0) {
                for (int index = 0; index < aBuilding.getBuildingType().getCannonRateOfFire(); index++) {
                    // skall ha 50% chans att träffa...
                    int random = Functions.getRandomInt(0, 99) + 1;
                    int randomIndex = Functions.getRandomInt(0, alltf.size() - 1);
                    TaskForce tf = (TaskForce) alltf.get(randomIndex);
                    if (random < aBuilding.getBuildingType().getCannonHitChance()) {// hit
                        tf.incomingCannonFire(aPlanet, aPlanet.getBuildings().get(i), g);
                        if (tf.getStatus().equalsIgnoreCase("destroyed")) {
                            Logger.finer("destroyed");
                            alltf.remove(randomIndex);
                            if (alltf.size() == 0) {// no TaskForce and ships left.
                                Logger.finer("return");
                                return;
                            }
                        }
                    } else {
                        String s = tf.getTotalNrShips() > 1 ? "s" : "";
                        g.getPlayerByGovenorName(tf.getPlayerName()).addToGeneral("Your ship" + s + " at " + aPlanet.getName() + " was fired upon by an enemy " + aBuilding.getBuildingType().getName() + " but it misses.");
                        if (aPlanet.getPlayerInControl() != null) {
                            aPlanet.getPlayerInControl().addToGeneral("Your " + aBuilding.getBuildingType().getName() + " at " + aPlanet.getName() + " fires but misses the enemy ships.");
                        }
                    }
                }
            }
        }
    }

    protected void bombardTroops(Player defendingPlayer, List<Troop> defendingTroops, Player attackingPlayer, int bombardment, Planet aPlanet) {
        int performedBombardments = 0;
        while ((performedBombardments < bombardment) & (defendingTroops.size() > 0)) {
            int randomIndex = Functions.getRandomInt(0, defendingTroops.size() - 1);

            // 50% chans to destroy hit a troop (destroy) Gameworld should use bombardmentdamge greater then the best troop have in hit + 50%
            if (Functions.getRandomInt(0, 100) < 50) {
                Troop bombardedTroop = defendingTroops.get(randomIndex);
                String returnString = TroopMutator.hit(bombardedTroop, g.getGameWorld().getBaseBombardmentDamage(), true, true, aPlanet.getResistance());

                if (defendingPlayer != null) {
                    defendingPlayer.addToGeneral("While bombarding your planet " + aPlanet.getName() + " Governor " + attackingPlayer.getGovernorName() + "'s bombardment have attacked your troop " + bombardedTroop.getName() + " with the effect: " + returnString);
                    attackingPlayer.addToGeneral("While bombarding the planet " + aPlanet.getName() + " belonging to Governor " + defendingPlayer.getGovernorName() + " (" + defendingPlayer.getFaction().getName() + ") your bombardment have attacked his troop " + TroopPureFunctions.getTroopTypeByKey(bombardedTroop.getTypeKey(), g.getGameWorld()).getName() + " with the effect: " + returnString);
                } else {
                    attackingPlayer.addToGeneral("While bombarding the neutral planet " + aPlanet.getName() + " your bombardment have attacked a troop " + TroopPureFunctions.getTroopTypeByKey(bombardedTroop.getTypeKey(), g.getGameWorld()).getName() + " with the effect: " + returnString);
                }

                if (TroopPureFunctions.isDestroyed(bombardedTroop)) {
                    defendingTroops.remove(randomIndex);
                    TroopMutator.removeTroop(bombardedTroop, g);
                }
            }
            performedBombardments++;
        }
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


    public Galaxy getGalaxy() {
        return g;
    }

    public void removeNeutralShips(Planet homeplanet) {
        for (int i = g.getSpaceships().size() - 1; i >= 0; i--) {
            Spaceship tempShip = g.getSpaceships().get(i);
            if ((tempShip.getLocation() == homeplanet) & (tempShip.getOwner() == null)) {
                g.getSpaceships().remove(tempShip);
            }
        }
    }

	// check conflicts between good and evil duellists on the same faction or player
	public void checkDuels(Galaxy galaxy) {
		for (Planet planet : galaxy.getPlanets()) {
			List<VIP> vipsAtPlanet = VipPureFunctions.findAllVIPsOnPlanet(planet, galaxy);
			if (vipsAtPlanet.size() > 1) {
				Logger.finest("Check vips at planet " + planet.getName());
                checkDuelAtPlanet(planet, vipsAtPlanet, 0, vipsAtPlanet.size() - 1, galaxy);
			}
		}
	}

    // check conflicts between good and evil duellists on the same faction or
    // player, at a specific planet
    public void checkDuelAtPlanet(Planet aPlanet, List<VIP> vipsAtPlanet, int lowVIPindex, int highVIPindex, Galaxy galaxy) {
        Logger.finer("checkDuelAtPlanet: (l/h) " + lowVIPindex + " " + highVIPindex);
        VIP lowVIP = vipsAtPlanet.get(lowVIPindex);
        VIP highVIP = vipsAtPlanet.get(highVIPindex);
        // check if the current VIP will fight
        if (VipPureFunctions.isDuellistConflict(aPlanet, lowVIP, highVIP, galaxy)) { // Fight!
            // compute who wins
            int lowChanceToWin = 50;
            lowChanceToWin = lowChanceToWin + VipPureFunctions.getDuellistSkill(lowVIP, galaxy.getGameWorld());
            lowChanceToWin = lowChanceToWin - VipPureFunctions.getDuellistSkill(highVIP, galaxy.getGameWorld());
            if (lowChanceToWin > 95) {
                lowChanceToWin = 95;
            } else if (lowChanceToWin < 5) {
                lowChanceToWin = 5;
            }
            boolean lowWon = Functions.getD100(lowChanceToWin);
            int loserIndex = -1, winnerIndex = -1;
            if (lowWon) { // low wins
                loserIndex = highVIPindex;
                winnerIndex = lowVIPindex;
            } else { // high wins
                loserIndex = lowVIPindex;
                winnerIndex = highVIPindex;
            }
            VIP losingVIP = vipsAtPlanet.get(loserIndex);
            VIPType losingVipType = VipPureFunctions.getVipTypeByKey(losingVIP.getTypeKey(), galaxy.getGameWorld());
            VIP winningVIP = vipsAtPlanet.get(winnerIndex);
            VIPType winningVIPType = VipPureFunctions.getVipTypeByKey(winningVIP.getTypeKey(), galaxy.getGameWorld());
            winningVIP.setKills(winningVIP.getKills() + 1);
            galaxy.getAllVIPs().remove(losingVIP);
            if (losingVIP.getBoss() == winningVIP.getBoss()) {
                losingVIP.getBoss().addToHighlights(losingVipType.getName(), HighlightType.TYPE_ACCIDENTAL_DUEL);
                losingVIP.getBoss().addToVIPReport(
                        "Your " + losingVipType.getName() + " has been killed by your own " + winningVIPType.getName() + ".");
                winningVIP.getBoss().addToVIPReport(
                        "Your " + winningVIPType.getName() + " has killed your own " + losingVipType.getName() + ".");
            } else if (losingVIP.getBoss().getFaction() == winningVIP.getBoss().getFaction()) {
                losingVIP.getBoss().addToHighlights(losingVipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                winningVIP.getBoss().addToHighlights(losingVipType.getName(), HighlightType.TYPE_FRIENDLY_VIP_KILLED);
                losingVIP.getBoss().addToVIPReport(
                        "Your " + losingVipType.getName() + " has been killed by a friendly " + winningVIPType.getName()
                                + " belonging to Governor " + winningVIP.getBoss().getGovernorName() + ".");
                winningVIP.getBoss().addToVIPReport("Your " + winningVIPType.getName() + " has killed a friendly "
                        + losingVipType.getName() + " belonging to Governor " + losingVIP.getBoss().getGovernorName() + ".");
            } else { // different factions => enemies
                losingVIP.getBoss().addToHighlights(losingVipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                winningVIP.getBoss().addToHighlights(losingVipType.getName(), HighlightType.TYPE_ENEMY_VIP_KILLED);
                losingVIP.getBoss().addToVIPReport(
                        "Your " + losingVipType.getName() + " has been killed by an enemy " + winningVIPType.getName() + ".");
                winningVIP.getBoss().addToVIPReport(
                        "Your " + winningVIPType.getName() + " has killed an enemy " + losingVipType.getName() + ".");
            }
            if (loserIndex == highVIPindex) {
                lowVIPindex = 0;
            }
            highVIPindex = highVIPindex - 1;
            if (lowVIPindex < highVIPindex) {
                checkDuelAtPlanet(aPlanet, vipsAtPlanet, lowVIPindex, highVIPindex, galaxy);
            }
        } else { // no fight...
            lowVIPindex = lowVIPindex + 1;
            if (lowVIPindex == highVIPindex) {
                lowVIPindex = 0;
                highVIPindex = highVIPindex - 1;
            }
            Logger.finest(lowVIPindex + " " + highVIPindex);
            if (highVIPindex > 0) {
                checkDuelAtPlanet(aPlanet, vipsAtPlanet, lowVIPindex, highVIPindex, galaxy);
            }
        }
    }

    // check if spies catch any (normal) spies or assassins
    public void checkCounterEspionage(Galaxy galaxy) {
        for (Planet planet : galaxy.getPlanets()) {
            List<VIP> vipsAtPlanet = VipPureFunctions.findAllVIPsOnPlanetOrShipsOrTroops(planet, galaxy);
            if (vipsAtPlanet.size() > 1) {
                checkCounterEspionageAtPlanet(planet, vipsAtPlanet, 0, vipsAtPlanet.size() - 1, galaxy);
            }
        }
    }

    // check if any Assassins kill other VIPs that isn't well guarded
    public void checkAssassins(Galaxy galaxy) {
        for (Planet planet : galaxy.getPlanets()) {
            List<VIP> vipsAtPlanet = VipPureFunctions.findAllVIPsOnPlanetOrShipsOrTroops(planet, galaxy);
            List<VIP> v = vipsAtPlanet.stream().collect(Collectors.toList());
            Collections.shuffle(v);
            if (v.size() > 1) {
                checkAssassins(planet, 0, v.size() - 1, v, galaxy);
            }
        }
        // remove hasKilled from all assassins
        for (int i = 0; i < galaxy.getAllVIPs().size(); i++) {
            VIP aVIP = galaxy.getAllVIPs().get(i);
            if (VipPureFunctions.getVipTypeByKey(aVIP.getTypeKey(), galaxy.getGameWorld()).getAssassination() > 0) {
                aVIP.setHasKilled(false);
            }
        }
    }

    // check if spies catch enemy VIPs on their planets
    public void checkCounterEspionageAtPlanet(Planet aPlanet, List<VIP> vipsAtPlanet, int lowVIP, int highVIP, Galaxy galaxy) {
        // check if the current VIP will fight
        if (VipPureFunctions.isSpiesConflict(aPlanet, vipsAtPlanet.get(lowVIP), vipsAtPlanet.get(highVIP), galaxy)) { // Conflict!
            // kolla vilken av Spionerna som är på en egen planet
            VIP aHighVIP = vipsAtPlanet.get(highVIP);
            VIP aLowVIP = vipsAtPlanet.get(lowVIP);
            boolean highIsHome = false;
            if (VipPureFunctions.getVipTypeByKey(aHighVIP.getTypeKey(), galaxy.getGameWorld()).isCounterSpy()) {
                Planet planetLocation = aHighVIP.getPlanetLocation();
                if (planetLocation != null) {
                    if (planetLocation.getPlayerInControl() == aHighVIP.getBoss()) {
                        if (!VipPureFunctions.getVipTypeByKey(aLowVIP.getTypeKey(), galaxy.getGameWorld()).isImmuneToCounterEspionage()) {
                            highIsHome = true;
                        }
                    }
                }
            }
            // slumpa om den andra blir upptäckt
            int counterEspionageSkill = 0;
            if (highIsHome) {
                counterEspionageSkill = VipPureFunctions.getCounterEspionage(aHighVIP, galaxy.getGameWorld());
            } else {
                counterEspionageSkill = VipPureFunctions.getCounterEspionage(aLowVIP, galaxy.getGameWorld());
            }
            boolean discovered = Functions.getD100(counterEspionageSkill);
            int loserIndex = -1, winnerIndex = -1;
            if (discovered) { // the other VIP is discovered
                if (highIsHome) {
                    winnerIndex = highVIP;
                    loserIndex = lowVIP;
                } else {
                    loserIndex = highVIP;
                    winnerIndex = lowVIP;
                }
                VIP losingVIP = vipsAtPlanet.get(loserIndex);
                VIPType losingVipType = VipPureFunctions.getVipTypeByKey(losingVIP.getTypeKey(), galaxy.getGameWorld());
                VIP winningVIP = vipsAtPlanet.get(winnerIndex);
                VIPType winningVipType = VipPureFunctions.getVipTypeByKey(winningVIP.getTypeKey(), galaxy.getGameWorld());
                galaxy.getAllVIPs().remove(losingVIP);
                losingVIP.getBoss().addToVIPReport(
                        "Your " + losingVipType.getName() + " has been discovered by an enemy counter-spy at "
                                + aPlanet.getName() + " and has been killed.");
                winningVIP.getBoss().addToVIPReport("Your " + winningVipType.getName() + " has discovered an enemy "
                        + losingVipType.getName() + " at " + aPlanet.getName() + " and has captured and killed him.");
                losingVIP.getBoss().addToHighlights(losingVipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                winningVIP.getBoss().addToHighlights(losingVipType.getName(), HighlightType.TYPE_ENEMY_VIP_KILLED);
                // update counters etc
                if (loserIndex == highVIP) {
                    lowVIP = 0;
                }
                highVIP = highVIP - 1;
                if (lowVIP < highVIP) {
                    checkCounterEspionageAtPlanet(aPlanet, vipsAtPlanet, lowVIP, highVIP, galaxy);
                }
            } else { // update counters as in no fight
                lowVIP = lowVIP + 1;
                if (lowVIP == highVIP) {
                    lowVIP = 0;
                    highVIP = highVIP - 1;
                }
                if (highVIP > 0) {
                    checkCounterEspionageAtPlanet(aPlanet, vipsAtPlanet, lowVIP, highVIP, galaxy);
                }
            }
        } else { // no fight...
            lowVIP = lowVIP + 1;
            if (lowVIP == highVIP) {
                lowVIP = 0;
                highVIP = highVIP - 1;
            }
            if (highVIP > 0) {
                checkCounterEspionageAtPlanet(aPlanet, vipsAtPlanet, lowVIP, highVIP, galaxy);
            }
        }
    }

    // check if exterminatorsdestroy any alien infestators
    private void checkExtermination(Galaxy galaxy) {
        List<VIP> exterminators = null;
        List<VIP> infestators = null;
        for (Planet planet : galaxy.getPlanets()) {
            exterminators = VipPureFunctions.getExterminators(planet, galaxy.getAllVIPs(), galaxy.getGameWorld());
            infestators = VipPureFunctions.getInfestators(planet, galaxy.getAllVIPs(), galaxy.getGameWorld());
            if ((exterminators.size() > 0) & (infestators.size() > 0)) {
                checkExterminationAtPlanet(planet, infestators, exterminators, galaxy);
            }
        }
    }



    private void checkExterminationAtPlanet(Planet aPlanet, List<VIP> infestators, List<VIP> exterminators, Galaxy galaxy) {
        List<VIP> enemyInfestators;
        Collections.shuffle(infestators);
        for (VIP anExt : exterminators) {
            enemyInfestators = new LinkedList<VIP>();
            // copy all enemy infestators to enemy list
            for (VIP anInf : infestators) {
                // if (anInf.getBoss().getFaction() != anExt.getBoss().getFaction()){
                if (DiplomacyPureFunctions.hostileExterminator(anInf.getBoss(), anExt.getBoss(), galaxy)) {
                    enemyInfestators.add(anInf);
                }
            }
            if (enemyInfestators.size() > 0) { // all infs may already be killed or are friendly
                int randomNr = Functions.getRandomInt(1, 100);
                if (randomNr <= VipPureFunctions.getExterminatorSkill(anExt, galaxy.getGameWorld())) { // the inf is killed
                    int randomIndex = Functions.getRandomInt(0, enemyInfestators.size() - 1);
                    VIP anInf = enemyInfestators.get(randomIndex);
                    VIPType vipType = VipPureFunctions.getVipTypeByKey(anInf.getTypeKey(), galaxy.getGameWorld());
                    anInf.getBoss().addToVIPReport(
                            "Your " + vipType.getName() + " has been discovered by an enemy exterminator at "
                                    + aPlanet.getName() + " and has been killed.");
                    anExt.getBoss().addToVIPReport("Your " + VipPureFunctions.getVipTypeByKey(anExt.getTypeKey(), galaxy.getGameWorld()).getName() + " has discovered an enemy "
                            + vipType.getName() + " at " + aPlanet.getName() + " and has killed him.");
                    anInf.getBoss().addToHighlights(vipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                    anExt.getBoss().addToHighlights(vipType.getName(), HighlightType.TYPE_ENEMY_VIP_KILLED);
                    galaxy.getAllVIPs().remove(anInf);
                    infestators.remove(randomIndex);
                }
            }
        }
    }

    // check if assassins kill any enemy VIPs
    private void checkAssassins(Planet aPlanet, int lowVIP, int highVIP, List<VIP> allVIPsOnPlanetRandomized, Galaxy galaxy) {
        // check if the current VIP will fight
        if (VipPureFunctions.isPossibleAssassinationConflict(aPlanet, (VIP) allVIPsOnPlanetRandomized.get(lowVIP),
                (VIP) allVIPsOnPlanetRandomized.get(highVIP), galaxy)) { // Conflict!
            VIP aHighVIP = allVIPsOnPlanetRandomized.get(highVIP);
            VIP aLowVIP = allVIPsOnPlanetRandomized.get(lowVIP);
            boolean highIsAssassin = false;
            if (VipPureFunctions.getVipTypeByKey(aHighVIP.getTypeKey(), galaxy.getGameWorld()).getAssassination() > 0 && VipPureFunctions.getLocation(aHighVIP) == aPlanet && VipPureFunctions.getLocation(aLowVIP) == aPlanet
                    && !VipPureFunctions.getVipTypeByKey(aLowVIP.getTypeKey(), galaxy.getGameWorld()).isWellGuarded()) {
                highIsAssassin = true;
            }
            // slumpa om den andra blir m�rdad
            int assassinationSkill = 0;
            if (highIsAssassin) {
                assassinationSkill = VipPureFunctions.getAssassinationSkill(aHighVIP, galaxy.getGameWorld());
            } else {
                assassinationSkill = VipPureFunctions.getAssassinationSkill(aLowVIP, galaxy.getGameWorld());
            }
            if (assassinationSkill > 95) {
                assassinationSkill = 95;
            }
            boolean discovered = Functions.getD100(assassinationSkill);
            int loserIndex = -1, winnerIndex = -1;
            if (discovered) { // the other is murdered
                if (highIsAssassin) {
                    winnerIndex = highVIP;
                    loserIndex = lowVIP;
                } else {
                    loserIndex = highVIP;
                    winnerIndex = lowVIP;
                }
                VIP losingVIP = allVIPsOnPlanetRandomized.get(loserIndex);
                VIPType losingVipType = VipPureFunctions.getVipTypeByKey(losingVIP.getTypeKey(), galaxy.getGameWorld());
                VIP winningVIP = allVIPsOnPlanetRandomized.get(winnerIndex);
                VIPType winningVipType = VipPureFunctions.getVipTypeByKey(winningVIP.getTypeKey(), galaxy.getGameWorld());
                winningVIP.setKills(winningVIP.getKills() + 1);
                galaxy.getAllVIPs().remove(losingVIP);
                allVIPsOnPlanetRandomized.remove(loserIndex);
                winningVIP.setHasKilled(true);
                losingVIP.getBoss().addToVIPReport("Your " + losingVipType.getName()
                        + " has been assassinated by an enemy assassin at " + aPlanet.getName() + ".");
                winningVIP.getBoss().addToVIPReport("Your " + winningVipType.getName() + " has assassinated an enemy "
                        + losingVipType.getName() + " at " + aPlanet.getName() + ".");
                losingVIP.getBoss().addToHighlights(losingVipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                winningVIP.getBoss().addToHighlights(losingVipType.getName(), HighlightType.TYPE_ENEMY_VIP_KILLED);
                // update counters etc
                if (loserIndex == highVIP) {
                    lowVIP = 0;
                }
                highVIP = highVIP - 1;
                if (lowVIP < highVIP) {
                    checkAssassins(aPlanet, lowVIP, highVIP, allVIPsOnPlanetRandomized, galaxy);
                }
            } else { // update counters as in no fight
                lowVIP = lowVIP + 1;
                if (lowVIP == highVIP) {
                    lowVIP = 0;
                    highVIP = highVIP - 1;
                }
                if (highVIP > 0) {
                    checkAssassins(aPlanet, lowVIP, highVIP, allVIPsOnPlanetRandomized, galaxy);
                }
            }
        } else { // no fight...
            lowVIP = lowVIP + 1;
            if (lowVIP == highVIP) {
                lowVIP = 0;
                highVIP = highVIP - 1;
            }
            if (highVIP > 0) {
                checkAssassins(aPlanet, lowVIP, highVIP, allVIPsOnPlanetRandomized, galaxy);
            }
        }
    }

    public int underBombardment(Planet planet , TaskForce bombardingTaskForce, Galaxy galaxy){
        Logger.fine("(Planet.java)  underBombardment  ");
        int bombardment = bombardingTaskForce.getBombardment(galaxy.getGameWorld());
        int maxBombardment = Integer.MAX_VALUE;
        Player bombardmentPlayer = galaxy.getPlayerByGovenorName(bombardingTaskForce.getPlayerName());
        if (!bombardmentPlayer.isAlien()){
            maxBombardment = PlanetOrderStatusPureFunctions.getMaxBombardment(planet.getName(), bombardmentPlayer.getPlanetOrderStatuses());
        }
        if (bombardment > maxBombardment){
            bombardment = maxBombardment;
        }
        if (bombardment > 0){
            Logger.fine("bombardment  " + bombardment);
            int shield = planet.getShield();
            if(shield > 0){
                Logger.fine("shield  " + shield);
                if(shield >= bombardment){
                    bombardment=0;
                    if (planet.getPlayerInControl() != null){
                        planet.getPlayerInControl().addToGeneral("While besieging your planet " + planet.getName() + " Governor " + bombardmentPlayer.getGovernorName() + " attampt to bombardment your planet but your planet shields stopped his attampt.");
                    }
                    bombardmentPlayer.addToGeneral("While besieging the planet " + planet.getName() + " belonging to Governor " + planet.getPlayerInControl().getGovernorName() + " (" + planet.getPlayerInControl().getFaction().getName() + ") your bombardment was stopped by planet defence shields.");
                }else{
                    bombardment-= shield;
                    if (planet.getPlayerInControl() != null){
                        planet.getPlayerInControl().addToGeneral("While besieging your planet " + planet.getName() + " Governor " + bombardmentPlayer.getGovernorName() + " bombardment your planet, your planet shields reduced the bombardment with " + shield + ".");
                    }
                    bombardmentPlayer.addToGeneral("While besieging the planet " + planet.getName() + " belonging to Governor " + planet.getPlayerInControl().getGovernorName() + " (" + planet.getPlayerInControl().getFaction().getName() + ") your bombardment was reduced  with " + shield + " by planet defence shields.");
                }
            }
        }
        if (bombardment > 0){
            Logger.fine("bombardment left after shield  " + bombardment);
            if (!planet.getInfectedByAlien()){
                planet.setPopulation(planet.getPopulation() - bombardment);
            }
            planet.setResistance(planet.getResistance() - bombardment);
            if (planet.getPlayerInControl() != null){
                planet.getPlayerInControl().addToGeneral("While besieging your planet " + planet.getName() + " Governor " + bombardmentPlayer.getGovernorName() + "'s bombardment have lowered " + planet.getName() + "'s resistance and population by " + bombardment + ".");
                bombardmentPlayer.addToGeneral("While besieging the planet " + planet.getName() + " belonging to Governor " + planet.getPlayerInControl().getGovernorName() + " (" + planet.getPlayerInControl().getFaction().getName() + ") your bombardment have lowered its resistance and population by " + bombardment + ".");

                // 10% chans att bomba s�nder en byggnad/bombv�rde.
                for(int bombardmentIndex = 0; bombardmentIndex < bombardment; bombardmentIndex++){
                    if(Functions.getD100(10)){// 10% to hit a ground building.

                        List<Building> groundBuildings = new ArrayList<>();
                        for(Building building : planet.getBuildings()){
                            if(building.getBuildingType().isInOrbit()){// ground buiding
                                groundBuildings.add(building);
                            }
                        }
                        if(groundBuildings.size() > 0){
                            int randomIndex = Functions.getRandomInt(0, groundBuildings.size()-1);
                            Building destroyedBuilding = groundBuildings.get(randomIndex);
                            planet.getPlayerInControl().addToGeneral("While besieging your planet " + planet.getName() + " Governor " + bombardmentPlayer.getGovernorName() + "'s bombardment have destoyed the building " + destroyedBuilding.getBuildingType().getName() + ".");


                            if(TroopPureFunctions.getTroopsOnPlanet(planet, bombardmentPlayer, galaxy.getTroops()).size() > 0){
                                // The attacking player have troops on the planet that can report which typ of building that was destroeyd.
                                bombardmentPlayer.addToGeneral("While besieging the planet " + planet.getName() + " belonging to Governor " + planet.getPlayerInControl().getGovernorName() + " (" + planet.getPlayerInControl().getFaction().getName() + ") your bombardment have destroyed a " + destroyedBuilding.getBuildingType().getName() + " building.");
                            }else{
                                // No troops and no report about the destoeyd buiding, just the explosion that tells about a destroyed building.
                                bombardmentPlayer.addToGeneral("While besieging the planet " + planet.getName() + " belonging to Governor " + planet.getPlayerInControl().getGovernorName() + " (" + planet.getPlayerInControl().getFaction().getName() + ") your bombardment have destroyed a building.");
                            }
                            planet.removeBuilding(destroyedBuilding.getUniqueId());
                        }

                    }
                }
            }else{
                bombardmentPlayer.addToGeneral("While besieging the neutral planet " + planet.getName() + " your bombardment have lowered its resistance and population by " + bombardment + ".");
            }




        }
        return bombardment; // return the number of points that were bombarded
    }

    // check if one confederacy has at least factionVictory XX% of the total pop of
    // all planets in the game
    public List<Player> checkWinningLord(Galaxy galaxy) {
        List<Player> winner = null;
        List<List<Player>> allLordships = getAllLordships(galaxy);
        if (allLordships.size() > 0) {
            // array for total prod for all conf
            int[] lordProdTotal = new int[allLordships.size()];
            int otherProd = 0; // räkna popen på alla neutrala planeter
            // r�kna popen p� alla factioner
            for (Planet aPlanet : galaxy.getPlanets()) {
                if (aPlanet.getPlayerInControl() != null) {
                    int lordIndex = findPlayerLordship(aPlanet.getPlayerInControl(), allLordships);
                    if (lordIndex > -1) {
                        if (aPlanet.getPlayerInControl().isAlien()) {
                            lordProdTotal[lordIndex] += aPlanet.getResistance();
                        } else {
                            lordProdTotal[lordIndex] += aPlanet.getPopulation();
                        }
                    } else {
                        otherProd += aPlanet.getPopulation();
                    }
                } else {
                    otherProd += aPlanet.getPopulation();
                }
            }
            // summera all pop i sectorn
            int totalProd = otherProd;
            for (int aConfProd : lordProdTotal) {
                totalProd += aConfProd;
            }
            // check for a winner
            int index = 0;
            while ((winner == null) & (index < lordProdTotal.length)) {
                if (((lordProdTotal[index] * 1.0) / totalProd * 1.0) > (galaxy.getFactionVictory() / 100.0)) {
                    winner = allLordships.get(index);
                } else {
                    index++;
                }
            }
        }
        return winner;
    }

    private int findPlayerLordship(Player aPlayer, List<List<Player>> allLordships) {
        int foundIndex = -1;
        int tempIndex = 0;
        while ((foundIndex == -1) & (tempIndex < allLordships.size())) {
            List<Player> aLord = allLordships.get(tempIndex);
            if (aLord.contains(aPlayer)) {
                foundIndex = tempIndex;
            } else {
                tempIndex++;
            }
        }
        return foundIndex;
    }

    private List<List<Player>> getAllLordships(Galaxy galaxy) {
        List<List<Player>> lordList = new LinkedList<List<Player>>();
        List<Player> allFoundLordPlayers = new LinkedList<Player>(); // all players in lord/vassal relations
        for (Player aPlayer1 : galaxy.getPlayers()) {
            if (!allFoundLordPlayers.contains(aPlayer1)) {
                // find all other vassal players with whom this player is in a Lord
                List<Player> tempLordPlayers = DiplomacyPureFunctions.getVassalPlayers(aPlayer1, galaxy);
                // List<Player> tempLordPlayers = new LinkedList<Player>(); // all players in
                // conf with player1
                // for (Player aPlayer2 : players) {
                // if (aPlayer1 != aPlayer2){
                // DiplomacyState aState = getDiplomacyState(aPlayer1,aPlayer2);
                // if ((aState.getCurrentLevel() == DiplomacyLevel.LORD) && (aState.getLord() ==
                // aPlayer1)){
                // tempLordPlayers.add(aPlayer2);
                // }
                // }
                // }
                if (tempLordPlayers.size() > 0) {
                    tempLordPlayers.add(0, aPlayer1);
                    allFoundLordPlayers.addAll(tempLordPlayers);
                    lordList.add(tempLordPlayers);
                }
            }
        }
        return lordList;
    }

    private boolean checkSoloConfederacyWinner(Galaxy galaxy) {
        boolean singleConfederacyFound = true;
        for (Player aPlayer1 : galaxy.getPlayers()) {
            if (!aPlayer1.isDefeated()) {
                for (Player aPlayer2 : galaxy.getPlayers()) {
                    if (!aPlayer2.isDefeated()) {
                        if (aPlayer1 != aPlayer2) {
                            if (DiplomacyPureFunctions.getDiplomacyState(aPlayer1, aPlayer2, galaxy.getDiplomacyStates()).getCurrentLevel() != DiplomacyLevel.CONFEDERACY) {
                                singleConfederacyFound = false;
                            }
                        }
                    }
                }
            }
        }
        return singleConfederacyFound;
    }

    // check if one confederacy has at least factionVictory XX% of the total pop of
    // all planets in the game
    public List<Player> checkWinningConfederacy(Galaxy galaxy) {
        List<Player> winner = null;
        List<List<Player>> allConfederacies = getAllConfederacies(galaxy);
        if (allConfederacies.size() > 0) {
            // array for total prod for all conf
            int[] confProdTotal = new int[allConfederacies.size()];
            int otherProd = 0; // r�kna popen p� alla neutrala planeter
            // räkna popen på alla factioner
            for (Planet aPlanet : galaxy.getPlanets()) {
                if (aPlanet.getPlayerInControl() != null) {
                    int confIndex = galaxy.findPlayerConfederacy(aPlanet.getPlayerInControl(), allConfederacies);
                    if (confIndex > -1) {
                        if (aPlanet.getPlayerInControl().isAlien()) {
                            confProdTotal[confIndex] += aPlanet.getResistance();
                        } else {
                            confProdTotal[confIndex] += aPlanet.getPopulation();
                        }
                    } else {
                        otherProd += aPlanet.getPopulation();
                    }
                } else {
                    otherProd += aPlanet.getPopulation();
                }
            }
            // summera all pop i sectorn
            int totalProd = otherProd;
            for (int aConfProd : confProdTotal) {
                totalProd += aConfProd;
            }
            // check for a winner
            int index = 0;
            while ((winner == null) & (index < confProdTotal.length)) {
                if (((confProdTotal[index] * 1.0) / totalProd * 1.0) > (galaxy.getFactionVictory() / 100.0)) {
                    winner = allConfederacies.get(index);
                } else {
                    index++;
                }
            }
        }
        return winner;
    }

    public List<List<Player>> getAllConfederacies(Galaxy galaxy) {
        List<List<Player>> confList = new LinkedList<List<Player>>();
        List<Player> allFoundConfPlayers = new LinkedList<Player>(); // all players in conf
        for (Player aPlayer1 : galaxy.getPlayers()) {
            if (!aPlayer1.isDefeated() & !allFoundConfPlayers.contains(aPlayer1)) {
                // find all other players with whom this player is in a confederacy
                List<Player> tempConfPlayers = new LinkedList<Player>(); // all players in conf with player1
                for (Player aPlayer2 : galaxy.getPlayers()) {
                    if (aPlayer1 != aPlayer2) {
                        if (DiplomacyPureFunctions.getDiplomacyState(aPlayer1, aPlayer2, galaxy.getDiplomacyStates()).getCurrentLevel() == DiplomacyLevel.CONFEDERACY) {
                            if (!aPlayer2.isDefeated()) {
                                tempConfPlayers.add(aPlayer2);
                            }
                        }
                    }
                }
                if (tempConfPlayers.size() > 0) {
                    tempConfPlayers.add(aPlayer1);
                    allFoundConfPlayers.addAll(tempConfPlayers);
                    confList.add(tempConfPlayers);
                }
            }
        }
        return confList;
    }

    public int getMaxPsychWarfare(Planet aPlanet, Player aPlayer, List<Spaceship> spaceships, GameWorld gameWorld) {
        List<Spaceship> shipsAtPlanet = SpaceshipPureFunctions.getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet, spaceships);
        int maxPW = 0;
        for (Spaceship ss : shipsAtPlanet) {
            if (ss.getPsychWarfare() > maxPW) {
                maxPW = ss.getPsychWarfare();
            }
        }
        return maxPW;
    }

    private VIP getPsychWarfareBonusVIPs(Planet aPlanet, Player aPlayer, Galaxy galaxy) {
        List<Spaceship> shipsAtPlanet = SpaceshipPureFunctions.getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet, galaxy.getSpaceships());
        VIP highestPsychWarfareVIP = null;
        for (Spaceship ss : shipsAtPlanet) {
            VIP aVIP = findHighestVIPPsychWarfareBonus(ss, aPlayer, galaxy);
            if (aVIP != null) {
                if (highestPsychWarfareVIP == null) {
                    highestPsychWarfareVIP = aVIP;
                } else if (VipPureFunctions.getVipTypeByKey(aVIP.getTypeKey(), galaxy.getGameWorld()).getPsychWarfareBonus() > VipPureFunctions.getVipTypeByKey(highestPsychWarfareVIP.getTypeKey(), galaxy.getGameWorld()).getPsychWarfareBonus()) {
                    highestPsychWarfareVIP = aVIP;
                }
            }
        }
        return highestPsychWarfareVIP;
    }

    private static VIP findHighestVIPPsychWarfareBonus(Spaceship aShip, Player aPlayer, Galaxy galaxy) {
        VIP foundVIP = null;
        int highestPsychWarfareBonus = 0;
        for (int i = 0; i < galaxy.getAllVIPs().size(); i++) {
            VIP tempVIP = galaxy.getAllVIPs().get(i);
            VIPType vipType = VipPureFunctions.getVipTypeByKey(tempVIP.getTypeKey(), galaxy.getGameWorld());
            if (vipType.getPsychWarfareBonus() > 0 && tempVIP.getBoss() == aPlayer
                    && tempVIP.getShipLocation() == aShip) {
                if (vipType.getPsychWarfareBonus() > highestPsychWarfareBonus) {
                    highestPsychWarfareBonus = vipType.getPsychWarfareBonus();
                    foundVIP = tempVIP;
                }
            }
        }
        return foundVIP;
    }

}