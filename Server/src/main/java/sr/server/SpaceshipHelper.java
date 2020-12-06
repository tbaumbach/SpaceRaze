package sr.server;

import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.orders.ShipMovement;
import spaceraze.world.orders.ShipToCarrierMovement;

public class SpaceshipHelper {

    public static void performMove(ShipToCarrierMovement shipToCarrierMovement, TurnInfo ti, Galaxy aGalaxy){
        Spaceship aSpaceship = aGalaxy.findSpaceshipByUniqueId(shipToCarrierMovement.getSpaceshipId());
        Spaceship aSpaceshipCarrier = aGalaxy.findSpaceshipByUniqueId(shipToCarrierMovement.getDestinationCarrierId());
        Logger.finest( "performMove: " + aSpaceship.getName() + " destination: " + aSpaceshipCarrier.getName());
        moveShip(aSpaceship, aSpaceshipCarrier, ti);
    }

    public static void moveShip(Spaceship spaceshipToMove, Spaceship destinationCarrier, TurnInfo ti) {
        String oldLocString = null;
        if (spaceshipToMove.getLocation() == null) { // old location is a carrier
            spaceshipToMove.setOldCarrierLocation(spaceshipToMove.getCarrierLocation());
            oldLocString = spaceshipToMove.getOldCarrierLocation().getName();
        } else { // old location is a planet
            spaceshipToMove.setOldLocation(spaceshipToMove.getLocation());
            oldLocString = spaceshipToMove.getOldLocation().getName();
        }
        // behövs denna, eller en motsvarighet till det?
        spaceshipToMove.setCarrierLocation(destinationCarrier);
        ti.addToLatestGeneralReport(spaceshipToMove.getName() + " has moved from " + oldLocString
                + " to " + spaceshipToMove.getCarrierLocation().getName() + ".");
    }

    public static void performMove(ShipMovement shipMovement,  TurnInfo ti, Galaxy aGalaxy) {
        Spaceship spaceship = aGalaxy.findSpaceshipByUniqueId(shipMovement.getSpaceShipID());
        if (spaceship != null) {
            String spaceShipname = spaceship.getName();
            Logger.finest("performMove: " + spaceShipname + " destination: " + shipMovement.getDestinationName());
            moveShip(spaceship, shipMovement.getDestinationName(), ti, aGalaxy);
        }

    }

    public static void moveShip(Spaceship spaceship, String inPlanet, TurnInfo ti, Galaxy galaxy){
        Planet destination = galaxy.getPlanet(inPlanet);
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
                ti.addToLatestGeneralReport("Your ship " + spaceship.getName() + " has arrived to " + spaceship.getRetreatingTo().getName() + " after retreating from " + spaceship.getRetreatingFrom().getName() + ".");
                spaceship.setRunningTo(null);
                spaceship.setRunningFrom(null);
                spaceship.setOldLocation(null); // is this one needed?
                spaceship.setRetreating(false);
            }else{ // ship continues to retreat
                // find a planet to run to
                spaceship.setLocation(destination);
                spaceship.setRunningTo(PlanetPureFunctions.getEscapePlanet(spaceship, galaxy));
                if (spaceship.getRetreatingTo() == null){ // there is no planet to retreat to
                    ti.addToLatestGeneralReport("Your ship " + spaceship.getName() + " has been scuttled by it's crew because they had nowhere to retreat to.");
                    if (spaceship.getOwner() != null) {
                        galaxy.checkVIPsInDestroyedShips(spaceship, spaceship.getOwner());
                        galaxy.checkTroopsInDestroyedShips(spaceship, spaceship.getOwner());
                        spaceship.getOwner().getTurnInfo().addToLatestShipsLostInSpace(spaceship);
                    }
                    galaxy.removeShip(spaceship);
                }else{ // there is a planet to retreat to
                    // running from is unchanged
                    // retreating is still true
                    spaceship.setOldLocation(spaceship.getLocation());
                    spaceship.setLocation(null); // a retreating ship has no location
                    ti.addToLatestGeneralReport("Your ship " + spaceship.getName() + " is retreating and just left " + spaceship.getOldLocation().getName() + ".");
                }
            }
        }else{
            if (spaceship.getCarrierLocation() != null){ // move ship from carrier
                spaceship.setOldCarrierLocation(spaceship.getCarrierLocation());
                spaceship.setCarrierLocation(null);
                spaceship.setLocation(destination);
                ti.addToLatestGeneralReport(spaceship.getName() + " has moved from " + spaceship.getOldCarrierLocation().getName() + " to " + spaceship.getLocation().getName() + ".");
            }else{ // move ship from another planet
                spaceship.setOldLocation(spaceship.getLocation());
                spaceship.setLocation(destination);
                ti.addToLatestGeneralReport(spaceship.getName() + " has moved from " + spaceship.getOldLocation().getName() + " to " + spaceship.getLocation().getName() + ".");
            }
        }
    }
}