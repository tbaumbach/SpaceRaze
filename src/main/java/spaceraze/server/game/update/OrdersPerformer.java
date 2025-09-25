package spaceraze.server.game.update;

import spaceraze.map.GalaxyMap;
import spaceraze.map.MapPlanet;
import spaceraze.servlethelper.game.BuildingPureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.servlethelper.game.troop.TroopPureFunctions;
import spaceraze.servlethelper.game.vip.VipMutator;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
import spaceraze.servlethelper.game.planet.PlanetMutator;
import spaceraze.servlethelper.game.planet.PlanetOrderStatusMutator;
import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.orders.*;
import sr.server.SpaceshipHelper;

import java.util.List;

public class OrdersPerformer {

    private OrdersPerformer() {
    }

    public static void performOrders(Orders orders, TurnInfo ti, Player p, Galaxy galaxy, GalaxyMap galaxyMap) {
        for (int i = 0; i < orders.getExpenses().size(); i++) {
            Expense tempExpense = orders.getExpenses().get(i);
            ExpensePerformer.performExpense(tempExpense, ti, p, orders, galaxy, galaxyMap);
        }
        // perform VIP moves
        for (int i = 0; i < orders.getVIPMoves().size(); i++) {
            VIPMovement tempVIPMove = orders.getVIPMoves().get(i);
            performMove(tempVIPMove, ti, galaxy, galaxyMap);
        }
        // perform troop to carrier
        for (TroopToCarrierMovement aTroopToCarrierMovement : orders.getTroopToCarrierMoves()) {
            Logger.finest("aTroopToCarrierMovement: " + aTroopToCarrierMovement.toString());
            performMove(aTroopToCarrierMovement, ti, galaxy, galaxyMap);
        }
        // perform troop to planet moves (was spaceship moves)
        for (TroopToPlanetMovement aTroopToPlanetMovement : orders.getTroopToPlanetMoves()) {
            Logger.finest("aTroopToPlanetMovement: " + aTroopToPlanetMovement.toString());
            performMove(aTroopToPlanetMovement, ti, galaxy, galaxyMap);
        }
        // perform squadrons to carrier moves
        for (ShipToCarrierMovement shipToCarrierMovement : orders.getShipToCarrierMoves()) {
            SpaceshipHelper.performMove(shipToCarrierMovement, ti, galaxy, galaxyMap);
        }
        // perform spaceship moves
        for (int i = 0; i < orders.getShipMoves().size(); i++) {
            Logger.finest("shipMoves.size(): " + orders.getShipMoves().size() + " i: " + i);
            ShipMovement tempShipMove = orders.getShipMoves().get(i);
            SpaceshipHelper.performMove(tempShipMove, ti, p.getGalaxy(), galaxyMap);
        }
        for (String planetUuid : orders.getPlanetVisibilities()) {
            MapPlanet mapPlanet = PlanetPureFunctions.getMapPlanet(galaxyMap, planetUuid);
            Planet planet = PlanetPureFunctions.getPlanet(planetUuid, galaxy);
            PlanetMutator.reverseVisibility(planet);
            String openString = planet.isOpen() ? "open" : "closed";
            ti.addToLatestGeneralReport(mapPlanet.getName() + " is now " + openString + ".");
        }
        // abandon planets
        for (String planetUuid : orders.getAbandonPlanets()) {
            MapPlanet mapPlanet = PlanetPureFunctions.getMapPlanet(galaxyMap, planetUuid);
            Planet planet = PlanetPureFunctions.getPlanet(planetUuid, galaxy);
            Player tempPlayer = planet.getPlayerInControl();
            checkVIPsOnAbandonedPlanet(planet, tempPlayer, galaxy, galaxyMap);
            planet.setPlayerInControl(null);
            PlanetOrderStatusMutator.setAttackIfNeutral(false, planet.getMapPlanetUuid(), tempPlayer.getPlanetOrderStatuses());
            if (GameWorldHandler.getFactionByUuid(p.getFactionUuid(), galaxy.getGameWorld()).isAlien()) {
                PlanetMutator.setRazed(planet);
                galaxy.removeBuildingsOnPlanet(planet);
                PlanetMutator.setLastKnownOwner(planet.getMapPlanetUuid(), "Neutral", tempPlayer.getGalaxy().turn + 1, tempPlayer.getPlanetInformations());
                PlanetMutator.setLastKnownProductionAndResistance(planet.getMapPlanetUuid(), 0, 0, tempPlayer.getPlanetInformations());
                PlanetPureFunctions.findPlanetInfo(planet.getMapPlanetUuid(), tempPlayer.getPlanetInformations()).setRazed(true);
                ti.addToLatestGeneralReport("You have abandoned " + mapPlanet.getName() + ". It is now razed and uninhabited.");
            } else {
                PlanetMutator.setLastKnownOwner(planet.getMapPlanetUuid(), "Neutral", tempPlayer.getGalaxy().turn + 1, tempPlayer.getPlanetInformations());
                PlanetMutator.setLastKnownProductionAndResistance(planet.getMapPlanetUuid(), planet.getPopulation(), planet.getResistance(), tempPlayer.getPlanetInformations());
                ti.addToLatestGeneralReport("You have abandoned " + mapPlanet.getName() + ". It is now neutral.");
            }
        }
        for (int i = 0; i < orders.getShipSelfDestructs().size(); i++) {
            Spaceship tempss = galaxy.findSpaceshipByUuid(orders.getShipSelfDestructs().get(i));
            Logger.finest("shipSelfDestructs: " + orders.getShipSelfDestructs().get(i));
            if (tempss != null) {
                SpaceshipMutator.removeShip(tempss, galaxy);
                VipMutator.checkVIPsInSelfDestroyedShips(tempss, p, galaxy, galaxyMap);
                // remove any troops in selfdestructed ship
                List<Troop> troopsInShip = galaxy.findAllTroopsOnShip(tempss);
                for (Troop troop : troopsInShip) {
                    TroopMutator.removeTroop(troop, galaxy, galaxyMap);
                    ti.addToLatestGeneralReport("When " + tempss.getName() + " was scuttled your troop " + troop.getName() + " has also been destroyed.");
                }
                ti.addToLatestGeneralReport("On your command " + tempss.getName() + " has been scuttled by its crew.");
            }
        }
        for (int i = 0; i < orders.getBuildingSelfDestructs().size(); i++) {
            Building tempBuilding = BuildingPureFunctions.findBuilding(orders.getBuildingSelfDestructs().get(i), p, galaxy);
            if (tempBuilding != null) {
                MapPlanet mapPlanet = PlanetPureFunctions.getMapPlanet(galaxyMap, tempBuilding.getLocation().getMapPlanetUuid());
                PlanetMutator.removeBuilding(tempBuilding.getLocation(), tempBuilding.getUuid());
                ti.addToLatestGeneralReport("On your command " + BuildingPureFunctions.getBuildingTypeByUuid(tempBuilding.getTypeUuid(), galaxy.getGameWorld()).getName() + " at " + mapPlanet.getName() + " has been destroyed.");
            }
        }
        for (int i = 0; i < orders.getVIPSelfDestructs().size(); i++) {
            VIP tempVIP = VipPureFunctions.findVIP(orders.getVIPSelfDestructs().get(i), galaxy);
//        Player tempPlayer = tempow.getLocation().getPlayerInControl();
            galaxy.getAllVIPs().remove(tempVIP);
            MapPlanet mapPlanet = PlanetPureFunctions.getMapPlanet(galaxyMap, VipPureFunctions.getLocation(tempVIP).getMapPlanetUuid());
            ti.addToLatestGeneralReport("On your command " + VipPureFunctions.getVipTypeByUuid(tempVIP.getTypeUuid(), galaxy.getGameWorld()).getName() + " at " + mapPlanet.getName() + " has been retired.");
        }

        for (int i = 0; i < orders.getScreenedShips().size(); i++) {
            Spaceship tempss = galaxy.findSpaceshipByUuid(orders.getScreenedShips().get(i));
            //      Player tempPlayer = tempss.getOwner();
            if (tempss != null) {
                tempss.setScreened(!tempss.isScreened());
                ti.addToLatestGeneralReport("Your ship " + tempss.getName() + " has changed screened status to: " + tempss.isScreened());
            }
        }
        // preform troop selfdestructs
        for (String aTroopId : orders.getTroopSelfDestructs()) {
            Troop aTroop = TroopPureFunctions.findTroop(aTroopId, galaxy);
            if (aTroop != null) {
                TroopMutator.removeTroop(aTroop, galaxy, galaxyMap);
                checkVIPsInSelfDestroyedTroops(aTroop, p, galaxy, galaxyMap);
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

    public static void checkVIPsOnAbandonedPlanet(Planet planet, Player aPlayer, Galaxy galaxy, GalaxyMap galaxyMap) {
        MapPlanet mapPlanet = PlanetPureFunctions.getMapPlanet(galaxyMap, planet.getMapPlanetUuid());
        List<VIP> allVIPsOnPlanet = VipPureFunctions.findAllVIPsOnPlanet(planet, galaxy);
        for (VIP vip : allVIPsOnPlanet) {
            VIPType vipType =VipPureFunctions.getVipTypeByUuid(vip.getTypeUuid(), galaxy.getGameWorld());
            if (vip.getBoss() == aPlayer) {
                if (!vipType.isCanVisitNeutralPlanets()) {
                    galaxy.getAllVIPs().remove(vip);
                    aPlayer.addToVIPReport("Your " + vipType.getName() + " has abandoned your cause when your planet "
                            + mapPlanet.getName() + " was abandoned.");
                    aPlayer.addToHighlights(vipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                }
            }
        }
    }

    public static void checkVIPsInSelfDestroyedTroops(Troop aTroop, Player aPlayer, Galaxy galaxy, GalaxyMap galaxyMap) {
        List<VIP> allVIPsOnTroop = VipPureFunctions.findAllVIPsOnTroop(aTroop, galaxy.getAllVIPs());
        for (VIP aVip : allVIPsOnTroop) {
            VIPType vipType = VipPureFunctions.getVipTypeByUuid(aVip.getTypeUuid(), galaxy.getGameWorld());
            TurnInfo ti = aVip.getBoss().getTurnInfo();
            // troop is aboard ship -> move VIP to ship
            if (aTroop.getShipLocation() != null) {
                ti.addToLatestGeneralReport(vipType.getName() + " has been forced to move when " + aTroop.getName()
                        + " was selfdestructed.");
                VipMutator.moveVIP(aVip, aTroop.getShipLocation(), ti, galaxy.getGameWorld(), galaxyMap);
            } else { // troop is on planet
                Planet planet = aTroop.getPlanetLocation();
                MapPlanet mapPlanet = PlanetPureFunctions.getMapPlanet(galaxyMap, planet.getMapPlanetUuid());
                // own planet -> move VIP to planet
                if (planet.getPlayerInControl() == aVip.getBoss()) {
                    ti.addToLatestGeneralReport(vipType.getName() + " has been forced to move when "
                            + aTroop.getName() + " was selfdestructed.");
                    VipMutator.moveVIP(aVip, planet, ti, galaxy.getGameWorld(), galaxyMap);
                } else if (planet.getPlayerInControl() == null) {
                    // neutral planet
                    if (vipType.isCanVisitNeutralPlanets()) {
                        // VIP can visit neutral planets -> move VIP to planet
                        ti.addToLatestGeneralReport(vipType.getName() + " has moved from " + aTroop.getName()
                                + " to " + mapPlanet.getName());
                        ti.addToLatestVIPReport(
                                vipType.getName() + " has been forced to move to the planet " + mapPlanet.getName()
                                        + " when your troop " + aTroop.getName() + " was selfdestructed.");
                        VipMutator.setShipLocation(aVip, planet);
                    } else {
                        // otherwise VIP is killed
                        galaxy.getAllVIPs().remove(aVip);
                        aPlayer.addToVIPReport("Your " + vipType.getName() + " has been killed when your troop "
                                + aTroop.getName() + " was selfdestructed at " + mapPlanet.getName() + ".");
                        aPlayer.addToHighlights(vipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                    }
                } else {
                    // enemy planet
                    if (vipType.isCanVisitEnemyPlanets()) {
                        // VIP can visit enemy planets -> move VIP to planet
                        ti.addToLatestGeneralReport(vipType.getName() + " has moved from " + aTroop.getName()
                                + " to " + mapPlanet.getName());
                        ti.addToLatestVIPReport(
                                vipType.getName() + " has been forced to move to the planet " + mapPlanet.getName()
                                        + " when your troop " + aTroop.getName() + " was selfdestructed.");
                        VipMutator.setShipLocation(aVip, planet);
                    } else {
                        // otherwise VIP is killed
                        galaxy.getAllVIPs().remove(aVip);
                        aPlayer.addToVIPReport("Your " + vipType.getName() + " has been killed when your troop "
                                + aTroop.getName() + " was selfdestructed at " + mapPlanet.getName() + ".");
                        aPlayer.addToHighlights(vipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                    }
                }
            }
        }
    }

    public static void performMove(TroopToPlanetMovement troopToPlanetMovement, TurnInfo ti, Galaxy aGalaxy, GalaxyMap galaxyMap) {
        Troop aTroop = TroopPureFunctions.findTroop(troopToPlanetMovement.getTroopKey(), aGalaxy);
        Planet planet = aGalaxy.getPlanet(troopToPlanetMovement.getMapPlanetUuid());
        MapPlanet mapPlanet = PlanetPureFunctions.getMapPlanet(galaxyMap, planet.getMapPlanetUuid());
        if (aTroop == null || planet == null) {
            Logger.severe("performMove Error: troopKey= " + troopToPlanetMovement.getTroopKey() + " planetName= " + troopToPlanetMovement.getMapPlanetUuid());
        } else {
            Logger.finest("performMove: " + aTroop.getName() + " destination: " + mapPlanet.getName());
            move(aTroop, planet, ti, galaxyMap);
            aTroop.setLastPlanetMoveTurn(troopToPlanetMovement.getTurn());
        }
    }

    public static void move(Troop troop, Planet destination, TurnInfo ti, GalaxyMap galaxyMap){
        // move troop from ship
        if(troop.getShipLocation() == null || destination == null){
            Logger.severe("Error: shipLocation= " + troop.getShipLocation() + " destination= " + destination);
        }else{
            troop.setOldShipLocation(troop.getShipLocation());
            troop.setShipLocation(null);
            troop.setPlanetLocation(destination);
            ti.addToLatestGeneralReport(troop.getName() + " has moved from " + troop.getOldShipLocation().getName() + " to " + PlanetPureFunctions.getPlanetName(galaxyMap, troop.getPlanetLocation().getMapPlanetUuid()) + ".");
        }
    }

    public static void performMove(TroopToCarrierMovement troopToCarrierMovement, TurnInfo ti, Galaxy aGalaxy, GalaxyMap galaxyMap){
        Troop aTroop = TroopPureFunctions.findTroop(troopToCarrierMovement.getTroopKey(), aGalaxy);
        Spaceship destinationCarrier = aGalaxy.findSpaceshipByUuid(troopToCarrierMovement.getDestinationCarrierKey());
        if(aTroop == null || destinationCarrier == null){
            Logger.severe( "performMove Error: troopId= " + troopToCarrierMovement.getTroopKey() + " destinationCarrierId= " + troopToCarrierMovement.getDestinationCarrierKey());
        }else{
            Logger.finest( "performMove: " + aTroop.getName() + " destination: " + destinationCarrier.getName());
            move(aTroop, destinationCarrier, ti, galaxyMap);
        }

    }

    public static void move(Troop troop, Spaceship destinationCarrier, TurnInfo ti, GalaxyMap galaxyMap) {
        String oldLocString = null;
        if (troop.getPlanetLocation() == null) { // old location is a ship
            troop.setOldShipLocation(troop.getShipLocation());
            oldLocString = troop.getOldShipLocation().getName();
        } else { // old location is a planet
            troop.setOldPlanetLocation(troop.getPlanetLocation());
            oldLocString = PlanetPureFunctions.getPlanetName(galaxyMap, troop.getOldPlanetLocation().getMapPlanetUuid());
            troop.setPlanetLocation(null);
            Logger.finer("New planet location = null!");
        }
        // ; beh�vs denna, eller en motsvarighet till det?
        troop.setShipLocation(destinationCarrier);
        ti.addToLatestGeneralReport(troop.getName() + " has moved from " + oldLocString + " to " + troop.getShipLocation().getName() + ".");
    }

    public static void performMove(VIPMovement vipMovement, TurnInfo ti, Galaxy aGalaxy, GalaxyMap galaxyMap) {
        VIP tempVIP = VipPureFunctions.findVIP(vipMovement.getVipKey(), aGalaxy);
        if (vipMovement.getPlanetDestination() != null) {
            VipMutator.moveVIP(tempVIP, aGalaxy.getPlanet(vipMovement.getPlanetDestination()), ti, aGalaxy.getGameWorld(), galaxyMap);
        } else if (vipMovement.getShipDestination() != null) {
            VipMutator.moveVIP(tempVIP, SpaceshipPureFunctions.findSpaceship(vipMovement.getShipDestination(), aGalaxy), ti, aGalaxy.getGameWorld(), galaxyMap);
        } else { // troop move
            VipMutator.moveVIP(tempVIP, TroopPureFunctions.findTroop(vipMovement.getTroopDestination(), aGalaxy), ti, aGalaxy.getGameWorld(), galaxyMap);
        }
    }

}
