package spaceraze.server.game.update;

import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.servlethelper.game.vip.VipMutator;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
import spaceraze.servlethelper.game.planet.PlanetMutator;
import spaceraze.servlethelper.game.planet.PlanetOrderStatusMutator;
import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.orders.*;
import sr.server.SpaceshipHelper;

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
            performMove(aTroopToCarrierMovement, ti, galaxy);
        }
        // perform troop to planet moves (was spaceship moves)
        for (TroopToPlanetMovement aTroopToPlanetMovement : orders.getTroopToPlanetMoves()) {
            Logger.finest("aTroopToPlanetMovement: " + aTroopToPlanetMovement.toString());
            performMove(aTroopToPlanetMovement, ti, galaxy);
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
                SpaceshipMutator.removeShip(tempss, galaxy);
                VipMutator.checkVIPsInSelfDestroyedShips(tempss, p, galaxy);
                // remove any troops in selfdestructed ship
                List<Troop> troopsInShip = galaxy.findAllTroopsOnShip(tempss);
                for (Troop troop : troopsInShip) {
                    TroopMutator.removeTroop(troop, galaxy);
                    ti.addToLatestGeneralReport("When " + tempss.getName() + " was scuttled your troop " + troop.getName() + " has also been destroyed.");
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
                tempss.setScreened(!tempss.isScreened());
                ti.addToLatestGeneralReport("Your ship " + tempss.getName() + " has changed screened status to: " + tempss.isScreened());
            }
        }
        // preform troop selfdestructs
        for (int aTroopId : orders.getTroopSelfDestructs()) {
            Troop aTroop = galaxy.findTroop(aTroopId);
            if (aTroop != null) {
                TroopMutator.removeTroop(aTroop, galaxy);
                checkVIPsInSelfDestroyedTroops(aTroop, p, galaxy);
                ti.addToLatestGeneralReport("On your command " + aTroop.getName() + " has been disbanded.");
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

    public static void checkVIPsInSelfDestroyedTroops(Troop aTroop, Player aPlayer, Galaxy galaxy) {
        List<VIP> allVIPsOnTroop = VipPureFunctions.findAllVIPsOnTroop(aTroop, galaxy.getAllVIPs());
        for (VIP aVip : allVIPsOnTroop) {
            TurnInfo ti = aVip.getBoss().getTurnInfo();
            // troop is aboard ship -> move VIP to ship
            if (aTroop.getShipLocation() != null) {
                ti.addToLatestGeneralReport(aVip.getName() + " has been forced to move when " + aTroop.getName()
                        + " was selfdestructed.");
                aVip.moveVIP(aTroop.getShipLocation(), ti);
            } else { // troop is on planet
                Planet thePlanet = aTroop.getPlanetLocation();
                // own planet -> move VIP to planet
                if (thePlanet.getPlayerInControl() == aVip.getBoss()) {
                    ti.addToLatestGeneralReport(aVip.getName() + " has been forced to move when "
                            + aTroop.getName() + " was selfdestructed.");
                    aVip.moveVIP(thePlanet, ti);
                } else if (thePlanet.getPlayerInControl() == null) {
                    // neutral planet
                    if (aVip.canVisitNeutralPlanets()) {
                        // VIP can visit neutral planets -> move VIP to planet
                        ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getName()
                                + " to " + thePlanet.getName());
                        ti.addToLatestVIPReport(
                                aVip.getName() + " has been forced to move to the planet " + thePlanet.getName()
                                        + " when your troop " + aTroop.getName() + " was selfdestructed.");
                        aVip.setLocation(thePlanet);
                    } else {
                        // otherwise VIP is killed
                        galaxy.getAllVIPs().remove(aVip);
                        aPlayer.addToVIPReport("Your " + aVip.getName() + " has been killed when your troop "
                                + aTroop.getName() + " was selfdestructed at " + thePlanet.getName() + ".");
                        aPlayer.addToHighlights(aVip.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                    }
                } else {
                    // enemy planet
                    if (aVip.canVisitEnemyPlanets()) {
                        // VIP can visit enemy planets -> move VIP to planet
                        ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getName()
                                + " to " + thePlanet.getName());
                        ti.addToLatestVIPReport(
                                aVip.getName() + " has been forced to move to the planet " + thePlanet.getName()
                                        + " when your troop " + aTroop.getName() + " was selfdestructed.");
                        aVip.setLocation(thePlanet);
                    } else {
                        // otherwise VIP is killed
                        galaxy.getAllVIPs().remove(aVip);
                        aPlayer.addToVIPReport("Your " + aVip.getName() + " has been killed when your troop "
                                + aTroop.getName() + " was selfdestructed at " + thePlanet.getName() + ".");
                        aPlayer.addToHighlights(aVip.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                    }
                }
            }
        }
    }

    public static void performMove(TroopToPlanetMovement troopToPlanetMovement, TurnInfo ti, Galaxy aGalaxy) {
        Troop aTroop = aGalaxy.findTroop(troopToPlanetMovement.getTroopId());
        Planet aPlanet = aGalaxy.getPlanet(troopToPlanetMovement.getPlanetName());
        if (aTroop == null || aPlanet == null) {
            Logger.severe("performMove Error: troopId= " + troopToPlanetMovement.getTroopId() + " planetName= " + troopToPlanetMovement.getPlanetName());
        } else {
            Logger.finest("performMove: " + aTroop.getName() + " destination: " + aPlanet.getName());
            move(aTroop, aPlanet, ti);
            aTroop.setLastPlanetMoveTurn(troopToPlanetMovement.getTurn());
        }
    }

    public static void move(Troop troop, Planet destination, TurnInfo ti){
        // move troop from ship
        if(troop.getShipLocation() == null || destination == null){
            Logger.severe("Error: shipLocation= " + troop.getShipLocation() + " destination= " + destination);
        }else{
            troop.setOldShipLocation(troop.getShipLocation());
            troop.setShipLocation(null);
            troop.setPlanetLocation(destination);
            ti.addToLatestGeneralReport(troop.getName() + " has moved from " + troop.getOldShipLocation().getName() + " to " + troop.getPlanetLocation().getName() + ".");
        }
    }

    public static void performMove(TroopToCarrierMovement troopToCarrierMovement, TurnInfo ti, Galaxy aGalaxy){
        Troop aTroop = aGalaxy.findTroop(troopToCarrierMovement.getTroopId());
        Spaceship destinationCarrier = aGalaxy.findSpaceshipByUniqueId(troopToCarrierMovement.getDestinationCarrierId());
        if(aTroop == null || destinationCarrier == null){
            Logger.severe( "performMove Error: troopId= " + troopToCarrierMovement.getTroopId() + " destinationCarrierId= " + troopToCarrierMovement.getDestinationCarrierId());
        }else{
            Logger.finest( "performMove: " + aTroop.getName() + " destination: " + destinationCarrier.getName());
            move(aTroop, destinationCarrier, ti);
        }

    }

    public static void move(Troop troop, Spaceship destinationCarrier, TurnInfo ti) {
        String oldLocString = null;
        if (troop.getPlanetLocation() == null) { // old location is a ship
            troop.setOldShipLocation(troop.getShipLocation());
            oldLocString = troop.getOldShipLocation().getName();
        } else { // old location is a planet
            troop.setOldPlanetLocation(troop.getPlanetLocation());
            oldLocString = troop.getOldPlanetLocation().getName();
            troop.setPlanetLocation(null);
            Logger.finer("New planet location = null!");
        }
        // ; beh�vs denna, eller en motsvarighet till det?
        troop.setShipLocation(destinationCarrier);
        ti.addToLatestGeneralReport(troop.getName() + " has moved from " + oldLocString + " to " + troop.getShipLocation().getName() + ".");
    }

}
