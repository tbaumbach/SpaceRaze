package spaceraze.server.game.update;

import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
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
                SpaceshipMutator.removeShip(tempss, galaxy);
                VipMutator.checkVIPsInSelfDestroyedShips(tempss, p, galaxy);
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
                tempss.setScreened(!tempss.isScreened());
                ti.addToLatestGeneralReport("Your ship " + tempss.getName() + " has changed screened status to: " + tempss.isScreened());
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
