package spaceraze.server.game.update;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Player;
import spaceraze.world.Spaceship;
import spaceraze.world.spacebattle.TaskForce;

public class CheckAbandonedSquadrons {
	
	Galaxy galaxy;
	
	public CheckAbandonedSquadrons(Galaxy galaxy) {
		this.galaxy = galaxy;
	}
	
	/**
	   * Destroy all squadrons who are on a neutral or enemy planet without
	   * at least one carrier from the same player at the same planet
	   *
	   */
	  public void checkAbandonedSquadrons(){
	      Logger.fine("checkAbandonedSquadrons called");
		  for (Player aPlayer : galaxy.getPlayers()) {
			  checkAbandonedSquadrons(aPlayer);
		  }
	  }
	
	  
	/**
	 * removes any squadrons in this TaskForce who will not survive
	 *
	 */
	public void checkAbandonedSquadrons(TaskForce taskforce, Planet thePlanet) {
		Player player = galaxy.getPlayerByGovenorName(taskforce.getPlayerName());
		if (player != null) {
			Logger.finest("checkAbandonedSquadron in TaskForce called: " + player.getName());
		} else {
			Logger.finest("checkAbandonedSquadron in TaskForce called: neutral");
		}
		boolean sqdSurvive = galaxy.getGameWorld().isSquadronsSurviveOutsideCarriers();
		List<Spaceship> tfSpaceships = taskforce.getAllSpaceShips().stream().map(ship -> ship.getSpaceship()).collect(Collectors.toList());
		Collections.shuffle(tfSpaceships);
		List<Spaceship> removeShips = new LinkedList<Spaceship>();
		boolean addSpace = false;
		for (Spaceship aShip : tfSpaceships) {
			if (aShip.isSquadron()) {
				if (aShip.getCarrierLocation() == null) { // sqd is not in a carrier
					if (aShip.getLocation() == null) {
						// should not happen (retreating squadrons??)
					} else if (player != null && aShip.getLocation().getPlayerInControl() == null) { // neutral planet
						if (!sqdSurvive) {
							// handle sqds auto moves to carriers
							List<Spaceship> carriersWithFreeSlots = galaxy
									.getCarriersWithFreeSlotsInSystem(aShip.getLocation(), player);
							if (carriersWithFreeSlots.size() > 0) {
								Collections.shuffle(carriersWithFreeSlots);
								Spaceship aCarrier = carriersWithFreeSlots.get(0);
								aShip.setCarrierLocation(aCarrier);
								aShip.setOldLocation(aShip.getLocation());
								String oldLocString = aShip.getLocation().getName();
								Logger.finer("TaskForce CarrierLocation: " + aShip.getCarrierLocation());
								if (aShip.getOwner() != null) {
									aShip.getOwner()
											.addToGeneral("Your sguadron " + aShip.getName()
													+ " has been attached to a nearby carrier (" + aCarrier.getName()
													+ ") in the system " + oldLocString + ".");
								}
							} else {
								removeShips.add(aShip);
							}
							addSpace = true;
						} else
						// squadrons will survive if at least one carrier exist
						if (!galaxy.playerHasCarrierAtPlanet(aShip.getOwner(), aShip.getLocation())) {
							// add ship to remove list
							removeShips.add(aShip);
							addSpace = true;
						}
					} else // planet is not neutral, check if it belongs to another faction
					if (player != null
							&& aShip.getOwner().getFaction() != aShip.getLocation().getPlayerInControl().getFaction()) {
						if (!sqdSurvive) {
							// handle sqds auto moves to carriers
							List<Spaceship> carriersWithFreeSlots = galaxy
									.getCarriersWithFreeSlotsInSystem(aShip.getLocation(), player);
							if (carriersWithFreeSlots.size() > 0) {
								Collections.shuffle(carriersWithFreeSlots);
								Spaceship aCarrier = carriersWithFreeSlots.get(0);
								aShip.setCarrierLocation(aCarrier);
								aShip.setOldLocation(aShip.getLocation());
								String oldLocString = aShip.getLocation().getName();
								Logger.finer("Carrier Location: " + aCarrier);
								if (aShip.getOwner() != null) {
									aShip.getOwner()
											.addToGeneral("Your sguadron " + aShip.getName()
													+ " has been attached to a nearby carrier (" + aCarrier.getName()
													+ ") in the system " + oldLocString + ".");
								}
								addSpace = true;
							} else {
								removeShips.add(aShip);
								addSpace = true;
							}
						} else if (!galaxy.playerHasCarrierAtPlanet(aShip.getOwner(), aShip.getLocation())) {
							// add ship to remove list
							removeShips.add(aShip);
							addSpace = true;
						}
					}
				}
			}
		}
		for (Spaceship aShip : removeShips) {
			// destroy ship
			Player owner = aShip.getOwner();
			if (owner != null) {
				owner.addToGeneral("Your sguadron " + aShip.getName()
						+ " has been scuttled by it's crew because they had no supporting carrier in the system "
						+ aShip.getLocation().getName() + ".");
				owner.addToShipsLostInSpace(aShip);
				galaxy.checkVIPsInDestroyedShips(aShip, owner);
			}
			Player controllingPlayer = thePlanet.getPlayerInControl();
			if (controllingPlayer != null) {
				if (controllingPlayer != aShip.getOwner()) {
					if (aShip.getOwner() != null) {
						controllingPlayer.addToGeneral(Functions.getDeterminedForm(aShip.getTypeName(), true) + " "
								+ aShip.getTypeName() + " belonging to Governor " + aShip.getOwner().getGovenorName()
								+ " has been scuttled in the " + thePlanet.getName()
								+ " system, due to lack of carrier.");
					} else {
						// neutral forces cannot besiege other planets, this should not happen
					}
				}
			}
			galaxy.removeShip(aShip);
		}
		if (addSpace & (player != null)) {
			player.addToGeneral("");
		}
	}

	private void checkAbandonedSquadrons(Player aPlayer){
	  	  Logger.finest("checkAbandonedSquadron called, player: " + aPlayer.getName());
	  	  boolean sqdSurvive = galaxy.getGameWorld().isSquadronsSurviveOutsideCarriers();
	  	  List<Spaceship> playerSpaceships = Functions.cloneList(galaxy.getPlayersSpaceships(aPlayer));
	  	  Collections.shuffle(playerSpaceships);
	  	  List<Spaceship> removeShips = new LinkedList<Spaceship>();
	  	  boolean addSpace = false;
	  	  for (Spaceship aShip : playerSpaceships) {
	  		if (aShip.isSquadron()){
	  			if (aShip.getCarrierLocation() == null){ // sqd is not in a carrier
	  				if (aShip.getLocation() == null){
	  					// not at a planet, do nothing (attached to carrier?)
	  				}else
	  				if (aShip.getLocation().getPlayerInControl() == null){
	  					if (!sqdSurvive){
	  						// handle sqds auto moves to carriers
	  						List<Spaceship> carriersWithFreeSlots = galaxy.getCarriersWithFreeSlotsInSystem(aShip.getLocation(),aPlayer);
	  						if (carriersWithFreeSlots.size() > 0){
	  							Collections.shuffle(carriersWithFreeSlots);
	  							Spaceship aCarrier = carriersWithFreeSlots.get(0);
	  							aShip.setCarrierLocation(aCarrier);
	  							aShip.setOldLocation(aShip.getLocation());
	  							String oldLocString = aShip.getLocation().getName();
	  							Logger.finer("CarrierLocation: " + aShip.getCarrierLocation());
	  							if (aShip.getOwner() != null){
	  								aPlayer.addToGeneral("Your sguadron " + aShip.getName() + " has been attached to a nearby carrier (" + aCarrier.getName() + ") in the system " + oldLocString + ".");
	  							}
	  						}else{
	  							removeShips.add(aShip);
	  						}
	  						addSpace = true;
	  					}else
	  					// planet is neutral
	  					if (!galaxy.playerHasCarrierAtPlanet(aShip.getOwner(),aShip.getLocation())){
	  						// add ship to remove list
	  						removeShips.add(aShip);
	  						addSpace = true;
	  					}
	  				}else // planet is not neutral, check if it belongs to another faction
	  				if(aShip.getOwner().getFaction() != aShip.getLocation().getPlayerInControl().getFaction()){
	  					if (!sqdSurvive){
	  						// handle sqds auto moves to carriers
	  						List<Spaceship> carriersWithFreeSlots = galaxy.getCarriersWithFreeSlotsInSystem(aShip.getLocation(),aPlayer);
	  						if (carriersWithFreeSlots.size() > 0){
	  							Collections.shuffle(carriersWithFreeSlots);
	  							Spaceship aCarrier = carriersWithFreeSlots.get(0);
	  							aShip.setCarrierLocation(aCarrier);
	  							aShip.setOldLocation(aShip.getLocation());
	  							String oldLocString = aShip.getLocation().getName();
	  							Logger.finer("Carrier Location: " + aCarrier);
	  							if (aShip.getOwner() != null){
	  								aPlayer.addToGeneral("Your sguadron " + aShip.getName() + " has been attached to a nearby carrier (" + aCarrier.getName() + ") in the system " + oldLocString + ".");
	  							}
	  							addSpace = true;
	  						}else{
	  							removeShips.add(aShip);
	  							addSpace = true;
	  						}
	  					}else
	  					if (!galaxy.playerHasCarrierAtPlanet(aShip.getOwner(),aShip.getLocation())){
	  						// add ship to remove list
	  						removeShips.add(aShip);
	  						addSpace = true;
	  					}
	  				}
	  			}
	  		}
	  	  }
	  	  for (Spaceship aShip : removeShips) {
	  			// destroy ship
	  			if (aShip.getOwner() != null){
	  				aPlayer.addToGeneral("Your sguadron " + aShip.getName() + " has been scuttled by it's crew because they had no supporting carrier in the system " + aShip.getLocation().getName() + ".");
	  			}
	  			Player owner = aShip.getOwner();
	  			if (owner != null) {
	  				galaxy.checkVIPsInDestroyedShips(aShip, owner);
	  			}
	  	        galaxy.removeShip(aShip);
	  	  }
	  	  if (addSpace){
	  		  aPlayer.addToGeneral("");
	  	  }
	    }
}
