package spaceraze.server.game.update;

import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.diplomacy.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.orders.*;

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
            tempShipToCarrierMove.performMove(ti, galaxy);
        }
        // perform spaceship moves
        for (int i = 0; i < orders.getShipMoves().size(); i++) {
            Logger.finest("shipMoves.size(): " + orders.getShipMoves().size() + " i: " + i);
            ShipMovement tempShipMove = orders.getShipMoves().get(i);
            tempShipMove.performMove(ti, p.getGalaxy());
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
            galaxy.checkVIPsOnAbandonedPlanet(tempPlanet, tempPlayer);
            tempPlanet.setPlayerInControl(null);
            tempPlayer.getPlanetOrderStatuses().setAttackIfNeutral(false, tempPlanet.getName());
            if (p.isAlien()) {
                tempPlanet.setRazed();
                galaxy.removeBuildingsOnPlanet(tempPlanet);
                tempPlayer.getPlanetInfos().setLastKnownOwner(tempPlanet.getName(), "Neutral", tempPlayer.getGalaxy().turn + 1);
                tempPlayer.getPlanetInfos().setLastKnownProdRes(tempPlanet.getName(), 0, 0);
                tempPlayer.getPlanetInfos().setRazed(true, tempPlanet.getName());
                ti.addToLatestGeneralReport("You have abandoned " + tempPlanet.getName() + ". It is now razed and uninhabited.");
            } else {
                tempPlayer.getPlanetInfos().setLastKnownOwner(tempPlanet.getName(), "Neutral", tempPlayer.getGalaxy().turn + 1);
                tempPlayer.getPlanetInfos().setLastKnownProdRes(tempPlanet.getName(), tempPlanet.getPopulation(), tempPlanet.getResistance());
                ti.addToLatestGeneralReport("You have abandoned " + tempPlanet.getName() + ". It is now neutral.");
            }
        }
        for (int i = 0; i < orders.getShipSelfDestructs().size(); i++) {
            Spaceship tempss = galaxy.findSpaceship(orders.getShipSelfDestructs().get(i));
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
            Spaceship tempss = galaxy.findSpaceship(orders.getScreenedShips().get(i));
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

        // perform tax changes
        for (TaxChange aTaxChange : orders.getTaxChanges()) {
            // first find the other player
            Player vassal = galaxy.getPlayer(aTaxChange.getPlayerName());
            // perform tax change
            DiplomacyState state = galaxy.getDiplomacyState(vassal, p);
            state.setTax(aTaxChange.getAmount());
        }
        // perform new notes text changes
        for (PlanetNotesChange aPlanetNotesChange : orders.getPlanetNotesChanges()) {
            aPlanetNotesChange.performPlanetNotes(p);
        }

    }

    // diplomacy changes
    public static void performDiplomacyOrders(Orders orders, Player p) {
        Logger.fine("performDiplomacyOrders: " + p.getGovenorName());
        Galaxy g = p.getGalaxy();
        // first sort changes so that lower (ex. ewar) comes before higher (ex. conf) levels
        Collections.sort(orders.getDiplomacyChanges(), new DiplomacyChangeLevelComparator<DiplomacyChange>());
        List<Player> confPlayers = g.getDiplomacy().getConfederacyPlayers(p);
        for (DiplomacyChange aChange : orders.getDiplomacyChanges()) {
//    	Player thePlayer = aChange.getThePlayer(g);
            Player otherPlayer = aChange.getOtherPlayer(g);
            DiplomacyState aState = g.getDiplomacyState(p, otherPlayer);
            if (aState.isChangedThisTurn()) { // state have already changed due to conflicts, offer is no longer valid
                aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to the changed diplomatic state between you and Governor " + aChange.getOtherPlayer(g).getGovenorName() + " the change to " + aChange.getNewLevel() + " is no longer valid.");
                aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to the changed diplomatic state between you and Governor " + aChange.getThePlayer(g).getGovenorName() + " the change to " + aChange.getNewLevel() + " is no longer valid.");
            } else if (aChange.isResponseToPreviousOffer()) { // if the change is a response to an earlier offer
                if (aChange.getNewLevel() == DiplomacyLevel.LORD) {
                    aState.setCurrentLevel(DiplomacyLevel.LORD);
                    Player aPlayer = aChange.getOtherPlayer(g);
                    Logger.fine("aChange.getOtherPlayerName(): " + aChange.getOtherPlayerName() + " aPlayer: " + aPlayer);
                    aState.setLord(aPlayer); // newLevel g�ller alltid den andra spelaren
                    aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovenorName() + " offer for vassalship and he is now your lord");
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovenorName() + " have accepted your offer for vassalship and is now your vassal");
                    aChange.getThePlayer(g).getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovenorName() + ";" + DiplomacyLevel.LORD, HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovenorName() + ";" + DiplomacyLevel.VASSAL, HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                } else if (aChange.getNewLevel() == DiplomacyLevel.VASSAL) {
                    aState.setCurrentLevel(DiplomacyLevel.LORD);
                    Player aPlayer = aChange.getThePlayer(g);
                    Logger.fine("aChange.getThePlayerName(): " + aChange.getThePlayerName() + " aPlayer: " + aPlayer);
                    aState.setLord(aPlayer);  // newLevel g�ller alltid den andra spelaren, och i detta fall skall thePlayer vara lord om den andra �r vasall
                    aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovenorName() + " offer for lordship and he is now your vassal");
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovenorName() + " have accepted your offer for lordship and is now your lord");
                    aChange.getThePlayer(g).getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovenorName() + ";" + DiplomacyLevel.VASSAL, HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovenorName() + ";" + DiplomacyLevel.LORD, HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                } else if (aChange.getNewLevel() == DiplomacyLevel.CONFEDERACY) {
                    // check if p is in a confederacy
//				List<Player> confPlayers = g.getDiplomacy().getConfederacyPlayers(p);
                    if (confPlayers.size() > 0) { // p is in a confederacy
                        // check (in Galaxy) if all other players in the confederacy also have a change with p
                        boolean allInConf = g.checkAllInConfederacyOrder(otherPlayer, confPlayers);
                        if (allInConf) { // all have change
                            // perform change
                            Logger.fine("aChange.getNewLevel()" + aChange.getNewLevel());
                            aState.setCurrentLevel(aChange.getNewLevel());
                            p.getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovenorName() + " offer for " + aChange.getNewLevel());
                            otherPlayer.getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovenorName() + " have accepted your offer for " + aChange.getNewLevel());
                            p.getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovenorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                            otherPlayer.getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovenorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                        } else { // not all have change
                            // do not perform change, message about incomplete answer
                            p.getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovenorName() + " offer for " + aChange.getNewLevel() + ", but since not all members of your confederacy have done so the acceptance is incomplete and have no effect.");
                            otherPlayer.getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovenorName() + " have accepted your offer for " + aChange.getNewLevel() + ", but since not all members of his confederacy have done so the acceptance is incomplete and have no effect.");
                            p.getTurnInfo().addToLatestHighlights(" to " + otherPlayer.getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_CHANGE);
                            otherPlayer.getTurnInfo().addToLatestHighlights(" from " + p.getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_CHANGE);
                        }
                    } else {// p is not in a confederacy
                        // check if otherPlayer is in a confederacy
                        // otherPlayer is in a conf
                        // perform change
                        // otherPlayer is not in a conf
                        // perform change
                        Logger.fine("aChange.getNewLevel()" + aChange.getNewLevel());
                        aState.setCurrentLevel(aChange.getNewLevel());
                        p.getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovenorName() + " offer for " + aChange.getNewLevel());
                        otherPlayer.getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovenorName() + " have accepted your offer for " + aChange.getNewLevel());
                        p.getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovenorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                        otherPlayer.getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovenorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                    }
                } else { // response to other offer (not lord/vassal/conf)
                    Logger.fine("aChange.getNewLevel()" + aChange.getNewLevel());
                    aState.setCurrentLevel(aChange.getNewLevel());
                    aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have accepted Governor " + aChange.getOtherPlayer(g).getGovenorName() + " offer for " + aChange.getNewLevel());
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovenorName() + " have accepted your offer for " + aChange.getNewLevel());
                    aChange.getThePlayer(g).getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovenorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                    aChange.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovenorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                }
            } else { // if it isn't a response
                Logger.fine("aChange.getNewLevel()" + aChange.getNewLevel());
                aState.setCurrentLevel(aChange.getNewLevel());
                aChange.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have changed your diplomatic status to Governor " + aChange.getOtherPlayer(g).getGovenorName() + " to " + aChange.getNewLevel());
                aChange.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + aChange.getThePlayer(g).getGovenorName() + " have changed his diplomatic status to you to " + aChange.getNewLevel());
                aChange.getThePlayer(g).getTurnInfo().addToLatestHighlights(aChange.getOtherPlayer(g).getGovenorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OWN);
                aChange.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(aChange.getThePlayer(g).getGovenorName() + ";" + aChange.getNewLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OTHER);
            }
            aState.setChangedThisTurn(true);
        }
        // diplomacy offers
        for (DiplomacyOffer anOffer : orders.getDiplomacyOffers()) {
            Player otherPlayer = anOffer.getOtherPlayer(g);
            DiplomacyState aState = g.getDiplomacyState(anOffer.getThePlayer(g), anOffer.getOtherPlayer(g));
            if (aState.isChangedThisTurn()) { // state have already changed due to conflicts, offer is no longer valid
                anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to the changed diplomatic state between you and Governor " + anOffer.getOtherPlayer(g).getGovenorName() + " your offer for " + anOffer.getSuggestedLevel() + " is no longer valid.");
                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " have made an offer for " + anOffer.getSuggestedLevel() + ", but it is no longer valid since the change in diplomatic state between you.");
                anOffer.setOfferPerformed(true);
            } else if (!anOffer.isOfferPerformed()) {
                // first check if the other player also have made an offer
                DiplomacyOffer otherPlayersOffer = anOffer.getOtherPlayer(g).getDiplomacyOffer(p);
                if (otherPlayersOffer != null) { // if there is an offer
                    boolean lordVassall = checkLordVassall(anOffer.getSuggestedLevel(), otherPlayersOffer.getSuggestedLevel());
                    if (lordVassall) { // players are now lord and vassall
                        if (anOffer.getSuggestedLevel() == DiplomacyLevel.LORD) {
                            Logger.fine("anOffer.getSuggestedLevel()" + anOffer.getSuggestedLevel());
                            aState.setCurrentLevel(anOffer.getSuggestedLevel());
                            aState.setLord(anOffer.getThePlayer(g));
                            anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getOtherPlayer(g).getGovenorName() + " is now your vassal!");
                            anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " is now your lord!");
                            anOffer.getThePlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getOtherPlayer(g).getGovenorName() + ";" + otherPlayersOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                            anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getThePlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                            anOffer.setOfferPerformed(true);
                            otherPlayersOffer.setOfferPerformed(true);
                        } else {
                            Logger.fine("anOffer.getSuggestedLevel()" + anOffer.getSuggestedLevel());
                            aState.setCurrentLevel(anOffer.getSuggestedLevel());
                            aState.setLord(anOffer.getOtherPlayer(g));
                            anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " is now your vassal!");
                            anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getOtherPlayer(g).getGovenorName() + " is now your lord!");
                            anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getThePlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                            anOffer.getThePlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getOtherPlayer(g).getGovenorName() + ";" + otherPlayersOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_LORD_VASSAL);
                            anOffer.setOfferPerformed(true);
                            otherPlayersOffer.setOfferPerformed(true);
                        }
                    } else {
                        if (anOffer.getSuggestedLevel() == otherPlayersOffer.getSuggestedLevel()) { // both players suggest the same non-lord/vassall diplomacy level
                            List<Player> confPlayers2 = g.getDiplomacy().getConfederacyPlayers(otherPlayer);
                            if ((anOffer.getSuggestedLevel() == DiplomacyLevel.CONFEDERACY) & ((confPlayers.size() > 0) | (confPlayers2.size() > 0))) { // that both are in conf can not happen
                                if (confPlayers.size() > 0) { // player is in a conf, check if all members of the conf have made offers
                                    boolean allOfferConf = g.checkAllInConfederacyOffer(otherPlayer, confPlayers);
                                    if (allOfferConf) {
                                        // perform change
                                        Logger.fine("multiple anOffer.getSuggestedLevel(): " + anOffer.getSuggestedLevel());
//    		    					aState.setCurrentLevel(anOffer.getSuggestedLevel());
                                        g.addPostConfList(aState);
                                        anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + anOffer.getOtherPlayer(g).getGovenorName() + " now have " + anOffer.getSuggestedLevel());
                                        anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + anOffer.getThePlayer(g).getGovenorName() + " now have " + anOffer.getSuggestedLevel());
                                        anOffer.getThePlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getOtherPlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                        anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getThePlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                        anOffer.setOfferPerformed(true);
                                        otherPlayersOffer.setOfferPerformed(true);
                                    } else { // offer invalid
                                        // not all members in conf have made offers, offer is not performed
                                        anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You and governor " + otherPlayer.getGovenorName() + " have made simultaneous offers for confederacy, but since not all members of your confederacy have made offers for confederacy, the offers are invalid and no change is performed.");
                                        anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("You and governor " + p.getGovenorName() + " have made simultaneous offers for confederacy, but since not all members of governor " + p.getGovenorName() + " confederacy have made offers for confederacy, the offers are invalid and no change is performed.");
                                        anOffer.getThePlayer(g).getTurnInfo().addToLatestHighlights(" with " + anOffer.getOtherPlayer(g).getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                        anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(" with " + anOffer.getThePlayer(g).getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
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
                                        anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + anOffer.getOtherPlayer(g).getGovenorName() + " now have " + anOffer.getSuggestedLevel());
                                        anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + anOffer.getThePlayer(g).getGovenorName() + " now have " + anOffer.getSuggestedLevel());
                                        anOffer.getThePlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getOtherPlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                        anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getThePlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                        anOffer.setOfferPerformed(true);
                                        otherPlayersOffer.setOfferPerformed(true);
                                    } else { // offer invalid
                                        // not all members in conf have made offers, offer is not performed
                                        anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You and governor " + otherPlayer.getGovenorName() + " have made simultaneous offers for confederacy, but since not all members of governor " + p.getGovenorName() + " confederacy have made offers for confederacy, the offers are invalid and no change is performed.");
                                        anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("You and governor " + p.getGovenorName() + " have made simultaneous offers for confederacy, but since not all members of your confederacy have made offers for confederacy, the offers are invalid and no change is performed.");
                                        anOffer.getThePlayer(g).getTurnInfo().addToLatestHighlights(" with " + anOffer.getOtherPlayer(g).getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                        anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(" with " + anOffer.getThePlayer(g).getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                        anOffer.setOfferPerformed(true);
                                        otherPlayersOffer.setOfferPerformed(true);
                                    }
                                }
                            } else {
                                // perform change
                                Logger.fine("anOffer.getSuggestedLevel(): " + anOffer.getSuggestedLevel());
                                aState.setCurrentLevel(anOffer.getSuggestedLevel());
                                anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + anOffer.getOtherPlayer(g).getGovenorName() + " now have " + anOffer.getSuggestedLevel());
                                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous offers you and governor " + anOffer.getThePlayer(g).getGovenorName() + " now have " + anOffer.getSuggestedLevel());
                                anOffer.getThePlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getOtherPlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getThePlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_BOTH);
                                anOffer.setOfferPerformed(true);
                                otherPlayersOffer.setOfferPerformed(true);
                            }
                        } else {
                            // different suggested levels, no change is performed
                            anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous different offers no change is performed between you and governor " + anOffer.getOtherPlayer(g).getGovenorName());
                            anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to simultaneous different offers no change is performed between you and governor " + anOffer.getThePlayer(g).getGovenorName());
                            anOffer.setOfferPerformed(true);
                            otherPlayersOffer.setOfferPerformed(true);
                        }
                    }
                } else { // if there is only one offer
                    // check if the other player have made a change
                    DiplomacyChange otherPlayersChange = anOffer.getOtherPlayer(g).getDiplomacyChange(p);
                    if (otherPlayersChange != null) { // if there is a change
                        // the offer is not performed...
                        anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("Due to governor " + anOffer.getOtherPlayer(g).getGovenorName() + " change in status to you your offer for " + anOffer.getSuggestedLevel() + " is no longer valid.");
                        anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " have made an offer for " + anOffer.getSuggestedLevel() + " but it is no longer valid since you have changed your status to him.");
                        anOffer.setOfferPerformed(true);
                    } else { // if there is no change
                        if (anOffer.getSuggestedLevel() == DiplomacyLevel.CONFEDERACY) {
                            List<Player> confPlayers2 = g.getDiplomacy().getConfederacyPlayers(otherPlayer);
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
                                    anOffer.getOtherPlayer(g).addDiplomacyOffer(anOffer);
                                    anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getThePlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OFFER);
                                    anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + anOffer.getOtherPlayer(g).getGovenorName() + " for " + anOffer.getSuggestedLevel());
                                    anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " have sent you an offer for " + anOffer.getSuggestedLevel());
                                    anOffer.setOfferPerformed(true);
                                } else {
                                    // offer incomplete
                                    anOffer.getThePlayer(g).getTurnInfo().addToLatestHighlights(" to " + anOffer.getOtherPlayer(g).getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                    anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(" from " + anOffer.getThePlayer(g).getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                    anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + anOffer.getOtherPlayer(g).getGovenorName() + " for " + anOffer.getSuggestedLevel() + ", but since not all members of your confederacy have done so the offer is incomplete and is discarded.");
                                    anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " have sent you an offer for " + anOffer.getSuggestedLevel() + ", but since not all members of his confederacy have done so the offer is incomplete and is discarded.");
                                    anOffer.setOfferPerformed(true);
                                }
                            } else if ((confPlayers2.size() > 0) & otherConfOffer) { // otherPlayer is in a confederacy and there exist at least one offer from another member of his conf
                                // not all members in conf have made offers, offer is not performed
                                anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have made an offer for confederacy to governor " + otherPlayer.getGovenorName() + " but since only some members of their confederacy have made offers to you, the offers are invalid and no change is performed.");
                                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + p.getGovenorName() + " have made an offer for confederacy to you, but since only some members of your confederacy have made offers to governor " + p.getGovenorName() + ", the offers are invalid and no change is performed.");
                                anOffer.getThePlayer(g).getTurnInfo().addToLatestHighlights(" to " + anOffer.getOtherPlayer(g).getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(" from " + anOffer.getThePlayer(g).getGovenorName(), HighlightType.TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER);
                                anOffer.setOfferPerformed(true);
                            } else { // neither is in a conf, add offer
                                anOffer.getOtherPlayer(g).addDiplomacyOffer(anOffer);
                                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getThePlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OFFER);
                                anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + anOffer.getOtherPlayer(g).getGovenorName() + " for " + anOffer.getSuggestedLevel());
                                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " have sent you an offer for " + anOffer.getSuggestedLevel());
                                anOffer.setOfferPerformed(true);
                            }
                        } else {
                            // add offer to other player
                            anOffer.getOtherPlayer(g).addDiplomacyOffer(anOffer);
                            anOffer.getOtherPlayer(g).getTurnInfo().addToLatestHighlights(anOffer.getThePlayer(g).getGovenorName() + ";" + anOffer.getSuggestedLevel(), HighlightType.TYPE_DIPLOMACY_CHANGE_OFFER);
                            if (anOffer.getSuggestedLevel() == DiplomacyLevel.LORD) {
                                anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + anOffer.getOtherPlayer(g).getGovenorName() + " for him to become your Lord and you his vassal");
                                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " have sent you an offer to become his lord and he your vassal");
                            } else if (anOffer.getSuggestedLevel() == DiplomacyLevel.VASSAL) {
                                anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + anOffer.getOtherPlayer(g).getGovenorName() + " for him to become your vassal and you his lord" + anOffer.getSuggestedLevel());
                                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " have sent you an offer to become his vassal and he your lord");
                            } else {
                                anOffer.getThePlayer(g).getTurnInfo().addToLatestDiplomacyReport("You have sent an offer to Governor " + anOffer.getOtherPlayer(g).getGovenorName() + " for " + anOffer.getSuggestedLevel());
                                anOffer.getOtherPlayer(g).getTurnInfo().addToLatestDiplomacyReport("Governor " + anOffer.getThePlayer(g).getGovenorName() + " have sent you an offer for " + anOffer.getSuggestedLevel());
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
}
