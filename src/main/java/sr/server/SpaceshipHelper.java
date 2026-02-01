package sr.server;

import spaceraze.game.Galaxy;
import spaceraze.game.Planet;
import spaceraze.game.Spaceship;
import spaceraze.game.report.old.CanBeLostInSpace;
import spaceraze.game.report.old.Report;
import spaceraze.game.report.old.TurnInfo;
import spaceraze.map.GalaxyMap;
import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.servlethelper.game.vip.VipMutator;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.game.orders.ShipMovement;
import spaceraze.game.orders.ShipToCarrierMovement;

public class SpaceshipHelper {

    public static void performMove(ShipToCarrierMovement shipToCarrierMovement, TurnInfo ti, Galaxy aGalaxy, GalaxyMap galaxyMap){
        Spaceship aSpaceship = aGalaxy.findSpaceshipByUuid(shipToCarrierMovement.getSpaceShipKey());
        Spaceship aSpaceshipCarrier = aGalaxy.findSpaceshipByUuid(shipToCarrierMovement.getDestinationCarrierKey());
        Logger.finest( "performMove: " + aSpaceship.getName() + " destination: " + aSpaceshipCarrier.getName());
        moveShip(aSpaceship, aSpaceshipCarrier, ti, galaxyMap);
    }

    public static void moveShip(Spaceship spaceshipToMove, Spaceship destinationCarrier, TurnInfo ti, GalaxyMap galaxyMap) {
        String oldLocString = null;
        if (spaceshipToMove.getLocation() == null) { // old location is a carrier
            spaceshipToMove.setOldCarrierLocation(spaceshipToMove.getCarrierLocation());
            oldLocString = spaceshipToMove.getOldCarrierLocation().getName();
        } else { // old location is a planet
            spaceshipToMove.setOldLocation(spaceshipToMove.getLocation());
            oldLocString = PlanetPureFunctions.getPlanetName(galaxyMap,spaceshipToMove.getOldLocation().getMapPlanetUuid());
        }
        // behövs denna, eller en motsvarighet till det?
        spaceshipToMove.setCarrierLocation(destinationCarrier);
        ti.addToLatestGeneralReport(spaceshipToMove.getName() + " has moved from " + oldLocString
                + " to " + spaceshipToMove.getCarrierLocation().getName() + ".");
    }

    public static void performMove(ShipMovement shipMovement,  TurnInfo ti, Galaxy aGalaxy, GalaxyMap galaxyMap, GameWorld gameWorld) {
        Spaceship spaceship = aGalaxy.findSpaceshipByUuid(shipMovement.getSpaceshipKey());
        if (spaceship != null) {
            String spaceShipname = spaceship.getName();
            Logger.finest("performMove: " + spaceShipname + " destination: " + shipMovement.getDestination());
            moveShip(spaceship, shipMovement.getDestination(), ti, aGalaxy, galaxyMap, gameWorld);
        }

    }

    public static void moveShip(Spaceship spaceship, String planetUuid, TurnInfo ti, Galaxy galaxy, GalaxyMap galaxyMap, GameWorld gameWorld){
        Planet destination = galaxy.getPlanet(planetUuid);
        if (spaceship.getOwner() != null){
            Logger.finer("Called, spaceship: " + spaceship.toString() + " destination: " + destination + " retreating: " + spaceship.isRetreating());
        }else{
            Logger.finer("moveShip called, spaceship: " + spaceship.toString() + " destination: " + destination + " retreating: " + spaceship.isRetreating());
        }
        if (spaceship.isRetreating()){ // retreating ship given new move order
            if (spaceship.getOwner() != null){
                Logger.finer("retreating == true");
            }else{
                Logger.finer("owner == null, retreating == true");
            }
            // if destination is a planet belonging to the player and not under
            // siege
            if ((destination.getPlayerInControl() != null) && (destination.getPlayerInControl() == spaceship.getOwner()) && !destination.isBesieged()){
                spaceship.setLocation(destination);
                ti.addToLatestGeneralReport("Your ship " + spaceship.getName() + " has arrived to " + PlanetPureFunctions.getPlanetName(galaxyMap, spaceship.getRetreatingTo().getMapPlanetUuid()) + " after retreating from " + PlanetPureFunctions.getPlanetName(galaxyMap, spaceship.getRetreatingFrom().getMapPlanetUuid()) + ".");
                spaceship.setRunningTo(null);
                spaceship.setRunningFrom(null);
                spaceship.setOldLocation(null); // is this one needed?
                spaceship.setRetreating(false);
            }else{ // ship continues to retreat
                // find a planet to run to
                spaceship.setLocation(destination);
                spaceship.setRunningTo(PlanetPureFunctions.getEscapePlanet(spaceship, galaxy, gameWorld));
                if (spaceship.getRetreatingTo() == null){ // there is no planet to retreat to
                    ti.addToLatestGeneralReport("Your ship " + spaceship.getName() + " has been scuttled by it's crew because they had nowhere to retreat to.");
                    if (spaceship.getOwner() != null) {
                        VipMutator.checkVIPsInDestroyedShips(spaceship, spaceship.getOwner(), galaxy, galaxyMap, gameWorld);
                        TroopMutator.checkTroopsInDestroyedShips(spaceship, spaceship.getOwner(), galaxy, galaxyMap, gameWorld);
                        addToLatestShipsLostInSpace(spaceship, spaceship.getOwner().getTurnInfo(), gameWorld);
                    }
                    SpaceshipMutator.removeShip(spaceship, galaxy);
                }else{ // there is a planet to retreat to
                    // running from is unchanged
                    // retreating is still true
                    spaceship.setOldLocation(spaceship.getLocation());
                    spaceship.setLocation(null); // a retreating ship has no location
                    ti.addToLatestGeneralReport("Your ship " + spaceship.getName() + " is retreating and just left " + PlanetPureFunctions.getPlanetName(galaxyMap,spaceship.getOldLocation().getMapPlanetUuid()) + ".");
                }
            }
        }else{
            if (spaceship.getCarrierLocation() != null){ // move ship from carrier
                spaceship.setOldCarrierLocation(spaceship.getCarrierLocation());
                spaceship.setCarrierLocation(null);
                spaceship.setLocation(destination);
                ti.addToLatestGeneralReport(spaceship.getName() + " has moved from " + spaceship.getOldCarrierLocation().getName() + " to " + PlanetPureFunctions.getPlanetName(galaxyMap,spaceship.getLocation().getMapPlanetUuid()) + ".");
            }else{ // move ship from another planet
                spaceship.setOldLocation(spaceship.getLocation());
                spaceship.setLocation(destination);
                ti.addToLatestGeneralReport(spaceship.getName() + " has moved from " + PlanetPureFunctions.getPlanetName(galaxyMap,spaceship.getOldLocation().getMapPlanetUuid()) + " to " + PlanetPureFunctions.getPlanetName(galaxyMap,spaceship.getLocation().getMapPlanetUuid()) + ".");
            }
        }
    }

    public static void addToLatestShipsLostInSpace(Spaceship ss, TurnInfo turnInfo, GameWorld gameWorld) {
        Report r = turnInfo.getGeneralReports().get( turnInfo.getGeneralReports().size() - 1);
        addShipLostInSpace(ss, r, gameWorld);
    }

    private static void addShipLostInSpace(Spaceship ss, Report report, GameWorld gameWorld) {
        report.getShipsLostInSpace().add(CanBeLostInSpace.builder().lostInSpaceString(SpaceshipPureFunctions.getSpaceshipTypeByUuid(ss.getTypeUuid(), gameWorld).getName()).owner(ss.getOwner() != null ? ss.getOwner().getGovernorName() : null).build()); // TODO 2020-11-28 This should be replaced by EvenReport logic. So add the lost ships to the new specific created Report (for the typ of event) extending EvenReport. Try to reuse the EnemySpaceship and OwnSpaceship
    }

    public static void moveRetreatingSquadron(Spaceship spaceship, TurnInfo ti, Galaxy galaxy, GalaxyMap galaxyMap, GameWorld gameWorld) {
        // The only ships without range who can be retreating are squadrons
        // in a retreating carrier, and they move to where the carrier has
        // moved. And is destroyed if the cartrier is destroyed.

        // First check if carrier is destroyed
        if (spaceship.getCarrierLocation().isDestroyed()) {
            ti.addToLatestGeneralReport("Your squadron "
                    + spaceship.getName()
                    + " has been destroyed when it's retreating carrier "
                    + spaceship.getCarrierLocation().getName()
                    + " was scuttled by it's crew.");
            if (spaceship.getOwner() != null) {
                VipMutator.checkVIPsInDestroyedShips(spaceship, spaceship.getOwner(), galaxy, galaxyMap, gameWorld);
                TroopMutator.checkTroopsInDestroyedShips(spaceship, spaceship.getOwner(), galaxy, galaxyMap, gameWorld);
                addToLatestShipsLostInSpace(spaceship, spaceship.getOwner().getTurnInfo(), gameWorld);
            }
            SpaceshipMutator.removeShip(spaceship, galaxy);
        } else if (spaceship.getCarrierLocation().isRetreating()) { // carrier is still
            // retreating
            // carrier is still in retreat
            // location is still null
            // retreat is still true
            // runningFrom is unchanged
            spaceship.setOldLocation(spaceship.getCarrierLocation().getOldLocation());
            ti.addToLatestGeneralReport("Your squadron " + spaceship.getName()
                    + " is retreating with it's carrier "
                    + spaceship.getCarrierLocation().getName() + " and just left "
                    + PlanetPureFunctions.getPlanetName(galaxyMap,spaceship.getOldLocation().getMapPlanetUuid()) + ".");
        } else {
            // carrier is no longer retreating
            // location = destination;
            spaceship.setLocation(spaceship.getCarrierLocation().getLocation());
            ti.addToLatestGeneralReport("Your squadron " + spaceship.getName()
                    + " has arrived to " + PlanetPureFunctions.getPlanetName(galaxyMap,spaceship.getLocation().getMapPlanetUuid())
                    + " after retreating with "
                    + spaceship.getCarrierLocation().getName() + ".");
            spaceship.setRunningTo(null);
            spaceship.setRunningFrom(null);
            spaceship.setOldLocation(null);
            spaceship.setRetreating(false);
        }
    }
}
