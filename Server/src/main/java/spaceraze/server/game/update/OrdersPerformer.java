package spaceraze.server.game.update;

import spaceraze.servlethelper.game.DiplomacyPureFunctions;
import spaceraze.servlethelper.game.VipPureFunctions;
import spaceraze.servlethelper.game.planet.PlanetMutator;
import spaceraze.servlethelper.game.planet.PlanetOrderStatusMutator;
import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.diplomacy.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.orders.*;
import sr.server.SpaceshipHelper;

import java.util.Collections;
import java.util.List;

public class OrdersPerformer {

    private OrdersPerformer() {
    }

    public static void performOrders(Orders orders, TurnInfo ti, Player p, Galaxy galaxy) {
        for (int i = 0; i < orders.getExpenses().size(); i++) {
            Expense tempExpense = orders.getExpenses().get(i);
            ExpensePerformer.performExpense(tempExpense, ti, p, orders, galaxy);
        }
        // perform VIP moves
        for (int i = 0; i < orders.getVIPMoves().size(); i++) {
            VIPMovement tempVIPMove = orders.getVIPMoves().get(i);
            tempVIPMove.performMove(ti, galaxy);
        }
        // perform troop to carrier
        for (TroopToCarrierMovement aTroopToCarrierMovement : orders.getTroopToCarrierMoves()) {
            Logger.finest("aTroopToCarrierMovement: " + aTroopToCarrierMovement.toString());
            aTroopToCarrierMovement.performMove(ti, galaxy);
        }
        // perform troop to planet moves (was spaceship moves)
        for (TroopToPlanetMovement aTroopToPlanetMovement : orders.getTroopToPlanetMoves()) {
            Logger.finest("aTroopToPlanetMovement: " + aTroopToPlanetMovement.toString());
            aTroopToPlanetMovement.performMove(ti, galaxy);
        }
        // perform squadrons to carrier moves
        for (int i = 0; i < orders.getShipToCarrierMoves().size(); i++) {
            Logger.finest("shipMoves.size(): " + orders.getShipToCarrierMoves().size() + " i: " + i);
            ShipToCarrierMovement tempShipToCarrierMove = orders.getShipToCarrierMoves().get(i);
            SpaceshipHelper.performMove(tempShipToCarrierMove, ti, galaxy);
        }
        // perform spaceship moves
        for (int i = 0; i < orders.getShipMoves().size(); i++) {
            Logger.finest("shipMoves.size(): " + orders.getShipMoves().size() + " i: " + i);
            ShipMovement tempShipMove = orders.getShipMoves().get(i);
            SpaceshipHelper.performMove(tempShipMove, ti, p.getGalaxy());
        }
        for (int i = 0; i < orders.getPlanetVisibilities().size(); i++) {
            Planet temp = galaxy.getPlanet(orders.getPlanetVisibilities().get(i));
            temp.reverseVisibility();
            String openString = "closed";
            if (temp.isOpen()) {
                openString = "open";
            }
            ti.addToLatestGeneralReport(temp.getName() + " is now " + openString + ".");
        }
        // abandon planets
        for (int i = 0; i < orders.getAbandonPlanets().size(); i++) {
            Planet tempPlanet = galaxy.getPlanet(orders.getAbandonPlanets().get(i));
            Player tempPlayer = tempPlanet.getPlayerInControl();
            checkVIPsOnAbandonedPlanet(tempPlanet, tempPlayer, galaxy);
            tempPlanet.setPlayerInControl(null);
            PlanetOrderStatusMutator.setAttackIfNeutral(false, tempPlanet.getName(), tempPlayer.getPlanetOrderStatuses());
            if (p.isAlien()) {
                tempPlanet.setRazed();
                galaxy.removeBuildingsOnPlanet(tempPlanet);
                PlanetMutator.setLastKnownOwner(tempPlanet.getName(), "Neutral", tempPlayer.getGalaxy().turn + 1, tempPlayer.getPlanetInformations());
                PlanetMutator.setLastKnownProductionAndResistance(tempPlanet.getName(), 0, 0, tempPlayer.getPlanetInformations());
                PlanetPureFunctions.findPlanetInfo(tempPlanet.getName(), tempPlayer.getPlanetInformations()).setRazed(true);
                ti.addToLatestGeneralReport("You have abandoned " + tempPlanet.getName() + ". It is now razed and uninhabited.");
            } else {
                PlanetMutator.setLastKnownOwner(tempPlanet.getName(), "Neutral", tempPlayer.getGalaxy().turn + 1, tempPlayer.getPlanetInformations());
                PlanetMutator.setLastKnownProductionAndResistance(tempPlanet.getName(), tempPlanet.getPopulation(), tempPlanet.getResistance(), tempPlayer.getPlanetInformations());
                ti.addToLatestGeneralReport("You have abandoned " + tempPlanet.getName() + ". It is now neutral.");
            }
        }
        for (int i = 0; i < orders.getShipSelfDestructs().size(); i++) {
            Spaceship tempss = galaxy.findSpaceshipByUniqueId(orders.getShipSelfDestructs().get(i));
            Logger.finest("shipSelfDestructs: " + orders.getShipSelfDestructs().get(i));
            if (tempss != null) {
                galaxy.removeShip(tempss);
                galaxy.checkVIPsInSelfDestroyedShips(tempss, p);
                // remove any troops in selfdestructed ship
                List<Troop> troopsInShip = galaxy.findAllTroopsOnShip(tempss);
                for (Troop troop : troopsInShip) {
                    galaxy.removeTroop(troop);
                    ti.addToLatestGeneralReport("When " + tempss.getName() + " was scuttled your troop " + troop.getUniqueName() + " has also been destroyed.");
                }
                ti.addToLatestGeneralReport("On your command " + tempss.getName() + " has been scuttled by its crew.");
            }
        }
        for (int i = 0; i < orders.getBuildingSelfDestructs().size(); i++) {
            Building tempBuilding = galaxy.findBuilding(orders.getBuildingSelfDestructs().get(i), p);
            if (tempBuilding != null) {
                tempBuilding.getLocation().removeBuilding(tempBuilding.getUniqueId());
                ti.addToLatestGeneralReport("On your command " + tempBuilding.getBuildingType().getName() + " at " + tempBuilding.getLocation().getName() + " has been destroyed.");
            }
        }
        for (int i = 0; i < orders.getVIPSelfDestructs().size(); i++) {
            VIP tempVIP = galaxy.findVIP(orders.getVIPSelfDestructs().get(i));
//        Player tempPlayer = tempow.getLocation().getPlayerInControl();
            galaxy.getAllVIPs().remove(tempVIP);
            ti.addToLatestGeneralReport("On your command " + tempVIP.getName() + " at " + tempVIP.getLocation().getName() + " has been retired.");
        }

        for (int i = 0; i < orders.getScreenedShips().size(); i++) {
            Spaceship tempss = galaxy.findSpaceshipByUniqueId(orders.getScreenedShips().get(i));
            //      Player tempPlayer = tempss.getOwner();
            if (tempss != null) {
                tempss.setScreened(!tempss.getScreened());
                ti.addToLatestGeneralReport("Your ship " + tempss.getName() + " has changed screened status to: " + tempss.getScreened());
            }
        }
        // preform troop selfdestructs
        for (int aTroopId : orders.getTroopSelfDestructs()) {
            Troop aTroop = galaxy.findTroop(aTroopId);
            if (aTroop != null) {
                galaxy.removeTroop(aTroop);
                galaxy.checkVIPsInSelfDestroyedTroops(aTroop, p);
                ti.addToLatestGeneralReport("On your command " + aTroop.getUniqueName() + " has been disbanded.");
            }
        }

        // perform research
        for (int i = 0; i < orders.getResearchOrders().size(); i++) {
            ResearchOrder tempReserachOrder = orders.getResearchOrders().get(i);
            Logger.fine("(orders.java) researchOrder.size() " + orders.getResearchOrders().size() + " tempReserachOrder.getAdvantageName() " + tempReserachOrder.getAdvantageName());
            ResearchPerformer.performResearch(tempReserachOrder, ti, p, galaxy);
//    	tempReserachOrder.addToHighlights(p,HighlightType.TYPE_RESEARCH_DONE);
        }
        // perform new notes text changes
        for (PlanetNotesChange aPlanetNotesChange : orders.getPlanetNotesChanges()) {
            PlanetPureFunctions.findPlanetInfo(aPlanetNotesChange.getPlanetName(), p.getPlanetInformations()).setNotes(aPlanetNotesChange.getNotesText());
        }

    }

    // diplomacy changes
    public static void performDiplomacyOrders(Orders orders, Player p) {
        Logger.fine("performDiplomacyOrders: " + p.getGovernorName());
        Galaxy g = p.getGalaxy();
        // first sort changes so that lower (ex. ewar) comes before higher (ex. conf) levels
        Collections.sort(orders.getDiplomacyChanges(), new DiplomacyChangeLevelComparator<DiplomacyChange>());
        List<Player> confPlayers = DiplomacyPureFunctions.getConfederacyPlayers(p, g);
        for (DiplomacyChange aChange : orders.getDiplomacyChanges()) {
//    	Player thePlayer = aChange.getThePlayer(g);
            Player otherPlayer = aChange.getOtherPlayer(g);
            DiplomacyState aState = DiplomacyPureFunctions.getDiplomacyState(p, otherPlayer, g.getDiplomacyStates());
            if (aState.isChangedThisTurn()) { // state have already changed due to conflicts, offer is no longer valid
                aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to the changed diplomatic state between you and Governor " + aChange.getOtherPlayer(g).getGovernorName() + " the change to " + aChange.getNewLevel() + " is no longer valid.");
                aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to the changed diplomatic state between you and Governor " + aChange.getThePlayer(g).getGovernorName() + " the change to " + aChange.getNewLevel() + " is no longer valid.");
            } else if (aChange.isResponseToPreviousOffer()) { // if the change is a response to an earlier offer
                if (aChange.getNewLevel() == DiplomacyLevel.LORD) {
                    aState.setCurrentLevel(DiplomacyLevel.LORD);
                    Player aPlayer = aChange.getOtherPlayer(g);
                    Logger.fine("aChange.getOtherPlayerName(): " + aChange.getOtherPlayerName() + " aPlayer: " + aPlayer);
                    aState.setLord(aPlayer); // newLevel g�ller alltid den andra spelaren
                    aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovernorName() + " offer for vassalship and he is now your lord");
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovernorName() + " have accepted your offer for vassalship and is now your vassal");
                    aChange.getThePlayer(g).getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovernorName() + ";" + DiplomacyLevel.LORD, HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovernorName() + ";" + DiplomacyLevel.VASSAL, HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                } else if (aChange.getNewLevel() == DiplomacyLevel.VASSAL) {
                    aState.setCurrentLevel(DiplomacyLevel.LORD);
                    Player aPlayer = aChange.getThePlayer(g);
                    Logger.fine("aChange.getThePlayerName(): " + aChange.getThePlayerName() + " aPlayer: " + aPlayer);
                    aState.setLord(aPlayer);  // newLevel g�ller alltid den andra spelaren, och i detta fall skall thePlayer vara lord om den andra �r vasall
                    aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovernorName() + " offer for lordship and he is now your vassal");
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovernorName() + " have accepted your offer for lordship and is now your lord");
                    aChange.getThePlayer(g).getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovernorName() + ";" + DiplomacyLevel.VASSAL, HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovernorName() + ";" + DiplomacyLevel.LORD, HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                } else if (aChange.getNewLevel() == DiplomacyLevel.CONFEDERACY) {
                    // check if p is in a confederacy
                    if (confPlayers.size() > 0) { // p is in a confederacy
                        // check (in Galaxy) if all other players in the confederacy also have a change with p
                        boolean allInConf = g.checkAllInConfederacyOrder(otherPlayer, confPlayers);
                        if (allInConf) { // all have change
                            // perform change
                            Logger.fine("aChange.getNewLevel()" + aChange.getNewLevel());
                            aState.setCurrentLevel(aChange.getNewLevel());
                            p.getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovernorName() + " offer for " + aChange.getNewLevel());
                            otherPlayer.getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovernorName() + " have accepted your offer for " + aChange.getNewLevel());
                            p.getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovernorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                            otherPlayer.getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovernorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                        } else { // not all have change
                            // do not perform change, message about incomplete answer
                            p.getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovernorName() + " offer for " + aChange.getNewLevel() + ", but since not all members of your confederacy have done so the acceptance is incomplete and have no effect.");
                            otherPlayer.getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovernorName() + " have accepted your offer for " + aChange.getNewLevel() + ", but since not all members of his confederacy have done so the acceptance is incomplete and have no effect.");
                            p.getTurnInfo().addToLatestHighlights(" to " + otherPlayer.getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_CHANGE);
                            otherPlayer.getTurnInfo().addToLatestHighlights(" from " + p.getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_CHANGE);
                        }
                    } else {// p is not in a confederacy
                        // check if otherPlayer is in a confederacy
                        // otherPlayer is in a conf
                        // perform change
                        // otherPlayer is not in a conf
                        // perform change
                        Logger.fine("aChange.getNewLevel()" + aChange.getNewLevel());
                        aState.setCurrentLevel(aChange.getNewLevel());
                        p.getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovernorName() + " offer for " + aChange.getNewLevel());
                        otherPlayer.getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovernorName() + " have accepted your offer for " + aChange.getNewLevel());
                        p.getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovernorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                        otherPlayer.getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovernorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                    }
                } else { // response to other offer (not lord/vassal/conf)
                    Logger.fine("aChange.getNewLevel()" + aChange.getNewLevel());
                    aState.setCurrentLevel(aChange.getNewLevel());
                    aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovernorName() + " offer for " + aChange.getNewLevel());
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovernorName() + " have accepted your offer for " + aChange.getNewLevel());
                    aChange.getThePlayer(g).getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovernorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovernorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                }
            } else { // if it isn't a response
                Logger.fine("aChange.getNewLevel()" + aChange.getNewLevel());
                aState.setCurrentLevel(aChange.getNewLevel());
                aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have changed your diplomatic status to Governor " + aChange.getOtherPlayer(g).getGovernorName() + " to " + aChange.getNewLevel());
                aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovernorName() + " have changed his diplomatic status to you to " + aChange.getNewLevel());
                aChange.getThePlayer(g).getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovernorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OWN);
                aChange.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovernorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OTHER);
            }
            aState.setChangedThisTurn(true);
        }
        // diplomacy offers
        for (DiplomacyOffer anOffer : orders.getDiplomacyOffers()) {
            Player otherPlayer = g.getPlayer(anOffer.getOtherPlayerName());
            DiplomacyState aState = DiplomacyPureFunctions.getDiplomacyState(g.getPlayer(anOffer.getThePlayerName()), g.getPlayer(anOffer.getOtherPlayerName()), g.getDiplomacyStates());
            if (aState.isChangedThisTurn()) { // state have already changed due to conflicts, offer is no longer valid
                g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to the changed diplomatic state between you and Governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " your offer for " + anOffer.getSuggestedLevel() + " is no longer valid.");
                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " have made an offer for " + anOffer.getSuggestedLevel() + ", but it is no longer valid since the change in diplomatic state between you.");
                anOffer.setOfferPerformed(true);
            } else if (!anOffer.isOfferPerformed()) {
                // first check if the other player also have made an offer
                DiplomacyOffer otherPlayersOffer = g.getPlayer(anOffer.getOtherPlayerName()).getDiplomacyOffer(p);
                if (otherPlayersOffer != null) { // if there is an offer
                    boolean lordVassall = checkLordVassall(anOffer.getSuggestedLevel(), otherPlayersOffer.getSuggestedLevel());
                    if (lordVassall) { // players are now lord and vassall
                        if (anOffer.getSuggestedLevel() == DiplomacyLevel.LORD) {
                            Logger.fine("anOffer.getSuggestedLevel()" + anOffer.getSuggestedLevel());
                            aState.setCurrentLevel(anOffer.getSuggestedLevel());
                            aState.setLord(g.getPlayer(anOffer.getThePlayerName()));
                            g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " is now your vassal!");
                            g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " is now your lord!");
                            g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + ";" + otherPlayersOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                            g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                            anOffer.setOfferPerformed(true);
                            otherPlayersOffer.setOfferPerformed(true);
                        } else {
                            Logger.fine("anOffer.getSuggestedLevel()" + anOffer.getSuggestedLevel());
                            aState.setCurrentLevel(anOffer.getSuggestedLevel());
                            aState.setLord(g.getPlayer(anOffer.getOtherPlayerName()));
                            g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " is now your vassal!");
                            g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " is now your lord!");
                            g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                            g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + ";" + otherPlayersOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                            anOffer.setOfferPerformed(true);
                            otherPlayersOffer.setOfferPerformed(true);
                        }
                    } else {
                        if (anOffer.getSuggestedLevel() == otherPlayersOffer.getSuggestedLevel()) { // both players suggest the same non-lord/vassall diplomacy level
                            List<Player> confPlayers2 = DiplomacyPureFunctions.getConfederacyPlayers(otherPlayer, g);
                            if ((anOffer.getSuggestedLevel() == DiplomacyLevel.CONFEDERACY) & ((confPlayers.size() > 0) | (confPlayers2.size() > 0))) { // that both are in conf can not happen
                                if (confPlayers.size() > 0) { // player is in a conf, check if all members of the conf have made offers
                                    boolean allOfferConf = g.checkAllInConfederacyOffer(otherPlayer, confPlayers);
                                    if (allOfferConf) {
                                        // perform change
                                        Logger.fine("multiple anOffer.getSuggestedLevel(): " + anOffer.getSuggestedLevel());
//    		    					aState.setCurrentLevel(anOffer.getSuggestedLevel());
                                        g.addPostConfList(aState);
                                        g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " now have " + anOffer.getSuggestedLevel());
                                        g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " now have " + anOffer.getSuggestedLevel());
                                        g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                        g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                        anOffer.setOfferPerformed(true);
                                        otherPlayersOffer.setOfferPerformed(true);
                                    } else { // offer invalid
                                        // not all members in conf have made offers, offer is not performed
                                        g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("You and governor " + otherPlayer.getGovernorName() + " have made simultaneous offers for confederacy, but since not all members of your confederacy have made offers for confederacy, the offers are invalid and no change is performed.");
                                        g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("You and governor " + p.getGovernorName() + " have made simultaneous offers for confederacy, but since not all members of governor " + p.getGovernorName() + " confederacy have made offers for confederacy, the offers are invalid and no change is performed.");
                                        g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestHighlights(" with " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                        g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(" with " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                        anOffer.setOfferPerformed(true);
                                        otherPlayersOffer.setOfferPerformed(true);
                                    }
                                } else { // otherPlayer is in a conf, check if all members of otherPlayers conf have made offers
                                    boolean allOfferConf = g.checkAllInConfederacyOffer(p, confPlayers2);
                                    if (allOfferConf) {
                                        // perform change
                                        Logger.fine("multiple anOffer.getSuggestedLevel(): " + anOffer.getSuggestedLevel());
//    		    					aState.setCurrentLevel(anOffer.getSuggestedLevel());
                                        g.addPostConfList(aState);
                                        g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " now have " + anOffer.getSuggestedLevel());
                                        g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " now have " + anOffer.getSuggestedLevel());
                                        g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                        g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                        anOffer.setOfferPerformed(true);
                                        otherPlayersOffer.setOfferPerformed(true);
                                    } else { // offer invalid
                                        // not all members in conf have made offers, offer is not performed
                                        g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("You and governor " + otherPlayer.getGovernorName() + " have made simultaneous offers for confederacy, but since not all members of governor " + p.getGovernorName() + " confederacy have made offers for confederacy, the offers are invalid and no change is performed.");
                                        g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("You and governor " + p.getGovernorName() + " have made simultaneous offers for confederacy, but since not all members of your confederacy have made offers for confederacy, the offers are invalid and no change is performed.");
                                        g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestHighlights(" with " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                        g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(" with " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                        anOffer.setOfferPerformed(true);
                                        otherPlayersOffer.setOfferPerformed(true);
                                    }
                                }
                            } else {
                                // perform change
                                Logger.fine("anOffer.getSuggestedLevel(): " + anOffer.getSuggestedLevel());
                                aState.setCurrentLevel(anOffer.getSuggestedLevel());
                                g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " now have " + anOffer.getSuggestedLevel());
                                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " now have " + anOffer.getSuggestedLevel());
                                g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                anOffer.setOfferPerformed(true);
                                otherPlayersOffer.setOfferPerformed(true);
                            }
                        } else {
                            // different suggested levels, no change is performed
                            g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous different offers no change is performed between you and governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName());
                            g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous different offers no change is performed between you and governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName());
                            anOffer.setOfferPerformed(true);
                            otherPlayersOffer.setOfferPerformed(true);
                        }
                    }
                } else { // if there is only one offer
                    // check if the other player have made a change
                    DiplomacyChange otherPlayersChange = g.getPlayer(anOffer.getOtherPlayerName()).getDiplomacyChange(p);
                    if (otherPlayersChange != null) { // if there is a change
                        // the offer is not performed...
                        g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("Due to governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " change in status to you your offer for " + anOffer.getSuggestedLevel() + " is no longer valid.");
                        g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " have made an offer for " + anOffer.getSuggestedLevel() + " but it is no longer valid since you have changed your status to him.");
                        anOffer.setOfferPerformed(true);
                    } else { // if there is no change
                        if (anOffer.getSuggestedLevel() == DiplomacyLevel.CONFEDERACY) {
                            List<Player> confPlayers2 = DiplomacyPureFunctions.getConfederacyPlayers(otherPlayer, g);
                            Logger.fine("confPlayers2: " + confPlayers2.size());
                            boolean otherConfOffer = false;
                            if (confPlayers2.size() > 0) {
                                otherConfOffer = g.checkConfederacyOfferExist(p, confPlayers2);
                            }
                            Logger.fine("otherConfOffer: " + otherConfOffer);
                            // check if p is in a conf
                            if (confPlayers.size() > 0) { // p is in a confederacy
                                // check (in Galaxy) if all other players in the confederacy also have an offer with otherplayer
                                boolean allInConf = g.checkAllInConfederacyOffer(otherPlayer, confPlayers);
                                if (allInConf) { // all have change
                                    // add offer to other player
                                    g.getPlayer(anOffer.getOtherPlayerName()).addDiplomacyOffer(anOffer);
                                    g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OFFER);
                                    g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " for " + anOffer.getSuggestedLevel());
                                    g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " have sent you an offer for " + anOffer.getSuggestedLevel());
                                    anOffer.setOfferPerformed(true);
                                } else {
                                    // offer incomplete
                                    g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestHighlights(" to " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                    g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(" from " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                    g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " for " + anOffer.getSuggestedLevel() + ", but since not all members of your confederacy have done so the offer is incomplete and is discarded.");
                                    g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " have sent you an offer for " + anOffer.getSuggestedLevel() + ", but since not all members of his confederacy have done so the offer is incomplete and is discarded.");
                                    anOffer.setOfferPerformed(true);
                                }
                            } else if ((confPlayers2.size() > 0) & otherConfOffer) { // otherPlayer is in a confederacy and there exist at least one offer from another member of his conf
                                // not all members in conf have made offers, offer is not performed
                                g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("You have made an offer for confederacy to governor " + otherPlayer.getGovernorName() + " but since only some members of their confederacy have made offers to you, the offers are invalid and no change is performed.");
                                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + p.getGovernorName() + " have made an offer for confederacy to you, but since only some members of your confederacy have made offers to governor " + p.getGovernorName() + ", the offers are invalid and no change is performed.");
                                g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestHighlights(" to " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(" from " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                anOffer.setOfferPerformed(true);
                            } else { // neither is in a conf, add offer
                                g.getPlayer(anOffer.getOtherPlayerName()).addDiplomacyOffer(anOffer);
                                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OFFER);
                                g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " for " + anOffer.getSuggestedLevel());
                                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " have sent you an offer for " + anOffer.getSuggestedLevel());
                                anOffer.setOfferPerformed(true);
                            }
                        } else {
                            // add offer to other player
                            g.getPlayer(anOffer.getOtherPlayerName()).addDiplomacyOffer(anOffer);
                            g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestHighlights(g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OFFER);
                            if (anOffer.getSuggestedLevel() == DiplomacyLevel.LORD) {
                                g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " for him to become your Lord and you his vassal");
                                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " have sent you an offer to become his lord and he your vassal");
                            } else if (anOffer.getSuggestedLevel() == DiplomacyLevel.VASSAL) {
                                g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " for him to become your vassal and you his lord" + anOffer.getSuggestedLevel());
                                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " have sent you an offer to become his vassal and he your lord");
                            } else {
                                g.getPlayer(anOffer.getThePlayerName()).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + g.getPlayer(anOffer.getOtherPlayerName()).getGovernorName() + " for " + anOffer.getSuggestedLevel());
                                g.getPlayer(anOffer.getOtherPlayerName()).getTurnInfo().addToLatestDiplomacyReport("Governor " + g.getPlayer(anOffer.getThePlayerName()).getGovernorName() + " have sent you an offer for " + anOffer.getSuggestedLevel());
                            }
                            anOffer.setOfferPerformed(true);
                        }
                    }
                }
            }
        }
    }

    private static boolean checkLordVassall(DiplomacyLevel level1, DiplomacyLevel level2) {
        boolean lordVassall = false;
        if ((level1 == DiplomacyLevel.LORD) & (level2 == DiplomacyLevel.VASSAL)) {
            lordVassall = true;
        } else if ((level2 == DiplomacyLevel.LORD) & (level1 == DiplomacyLevel.VASSAL)) {
            lordVassall = true;
        }
        return lordVassall;
    }

    public static void checkVIPsOnAbandonedPlanet(Planet aPlanet, Player aPlayer, Galaxy galaxy) {
        List<VIP> allVIPsOnPlanet = VipPureFunctions.findAllVIPsOnPlanet(aPlanet, galaxy);
        for (int i = 0; i < allVIPsOnPlanet.size(); i++) {
            VIP tempVIP = allVIPsOnPlanet.get(i);
            if (tempVIP.getBoss() == aPlayer) {
                if (!tempVIP.canVisitNeutralPlanets()) {
                    galaxy.getAllVIPs().remove(tempVIP);
                    aPlayer.addToVIPReport("Your " + tempVIP.getName() + " has abandoned your cause when your planet "
                            + aPlanet.getName() + " was abandoned.");
                    aPlayer.addToHighlights(tempVIP.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                }
            }
        }
    }

}
