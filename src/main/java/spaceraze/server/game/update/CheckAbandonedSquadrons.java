package spaceraze.server.game.update;

import java.util.*;
import java.util.stream.Collectors;

import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.servlethelper.game.vip.VipMutator;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Player;
import spaceraze.world.Spaceship;
import spaceraze.battlehandler.spacebattle.TaskForce;
import spaceraze.world.enums.SpaceShipSize;
import spaceraze.world.orders.Orders;
import sr.server.SpaceshipHelper;

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
			if (aShip.getSize() == SpaceShipSize.SQUADRON) {
				if (aShip.getCarrierLocation() == null) { // sqd is not in a carrier
					if (aShip.getLocation() == null) {
						// should not happen (retreating squadrons??)
					} else if (player != null && aShip.getLocation().getPlayerInControl() == null) { // neutral planet
						if (!sqdSurvive) {
							// handle sqds auto moves to carriers
							List<Spaceship> carriersWithFreeSlots = getOtherCarriersWithFreeSlotsInSystem(aShip.getLocation(), player, null, galaxy);
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
						if (!playerHasCarrierAtPlanet(aShip.getOwner(), aShip.getLocation(), galaxy)) {
							// add ship to remove list
							removeShips.add(aShip);
							addSpace = true;
						}
					} else // planet is not neutral, check if it belongs to another faction
					if (player != null
							&& !aShip.getOwner().getFactionUuid().equals(aShip.getLocation().getPlayerInControl().getFactionUuid())) {
						if (!sqdSurvive) {
							// handle sqds auto moves to carriers
							List<Spaceship> carriersWithFreeSlots = getOtherCarriersWithFreeSlotsInSystem(aShip.getLocation(), player, null, galaxy);
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
						} else if (!playerHasCarrierAtPlanet(aShip.getOwner(), aShip.getLocation(), galaxy)) {
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
				SpaceshipHelper.addToLatestShipsLostInSpace(aShip, owner.getTurnInfo(), galaxy.getGameWorld());
				VipMutator.checkVIPsInDestroyedShips(aShip, owner, galaxy);
			}
			Player controllingPlayer = thePlanet.getPlayerInControl();
			if (controllingPlayer != null) {
				if (controllingPlayer != aShip.getOwner()) {
					if (aShip.getOwner() != null) {
						controllingPlayer.addToGeneral(Functions.getDeterminedForm(SpaceshipPureFunctions.getSpaceshipTypeByUuid(aShip.getTypeUuid(), galaxy.getGameWorld()).getName(), true) + " "
								+ SpaceshipPureFunctions.getSpaceshipTypeByUuid(aShip.getTypeUuid(), galaxy.getGameWorld()).getName() + " belonging to Governor " + aShip.getOwner().getGovernorName()
								+ " has been scuttled in the " + thePlanet.getName()
								+ " system, due to lack of carrier.");
					} else {
						// neutral forces cannot besiege other planets, this should not happen
					}
				}
			}
			SpaceshipMutator.removeShip(aShip, galaxy);
		}
		if (addSpace & (player != null)) {
			player.addToGeneral("");
		}
	}

	private void checkAbandonedSquadrons(Player aPlayer){
	  	  Logger.finest("checkAbandonedSquadron called, player: " + aPlayer.getName());
	  	  boolean sqdSurvive = galaxy.getGameWorld().isSquadronsSurviveOutsideCarriers();
	  	  List<Spaceship> playerSpaceships = SpaceshipPureFunctions.getPlayersSpaceships(aPlayer, galaxy).stream().collect(Collectors.toList());
	  	  Collections.shuffle(playerSpaceships);
	  	  List<Spaceship> removeShips = new LinkedList<Spaceship>();
	  	  boolean addSpace = false;
	  	  for (Spaceship aShip : playerSpaceships) {
	  		if (aShip.getSize() == SpaceShipSize.SQUADRON){
	  			if (aShip.getCarrierLocation() == null){ // sqd is not in a carrier
	  				if (aShip.getLocation() == null){
	  					// not at a planet, do nothing (attached to carrier?)
	  				}else
	  				if (aShip.getLocation().getPlayerInControl() == null){
	  					if (!sqdSurvive){
	  						// handle sqds auto moves to carriers
	  						List<Spaceship> carriersWithFreeSlots = getOtherCarriersWithFreeSlotsInSystem(aShip.getLocation(),aPlayer, null, galaxy);
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
	  					if (!playerHasCarrierAtPlanet(aShip.getOwner(),aShip.getLocation(), galaxy)){
	  						// add ship to remove list
	  						removeShips.add(aShip);
	  						addSpace = true;
	  					}
	  				}else // planet is not neutral, check if it belongs to another faction
	  				if(!aShip.getOwner().getFactionUuid().equals(aShip.getLocation().getPlayerInControl().getFactionUuid())){
	  					if (!sqdSurvive){
	  						// handle sqds auto moves to carriers
	  						List<Spaceship> carriersWithFreeSlots = getOtherCarriersWithFreeSlotsInSystem(aShip.getLocation(),aPlayer, null, galaxy);
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
	  					if (!playerHasCarrierAtPlanet(aShip.getOwner(),aShip.getLocation(), galaxy)){
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
					VipMutator.checkVIPsInDestroyedShips(aShip, owner, galaxy);
	  			}
			  SpaceshipMutator.removeShip(aShip, galaxy);
	  	  }
	  	  if (addSpace){
	  		  aPlayer.addToGeneral("");
	  	  }
	    }

	public List<Spaceship> getOtherCarriersWithFreeSlotsInSystem(Planet aLocation, Player aPlayer, Spaceship aCarrier, Galaxy galaxy) {
		List<Spaceship> carriersWithFreeSlots = new ArrayList<Spaceship>();
		List<Spaceship> shipsAtPlanet = SpaceshipPureFunctions.getPlayersSpaceshipsOnPlanet(aPlayer, aLocation, galaxy.getSpaceships());
		for (Spaceship spaceship : shipsAtPlanet) {
			if (SpaceshipPureFunctions.isCarrier(spaceship)) {
				int maxSlots = spaceship.getSquadronCapacity();
				int slotsFull = SpaceshipPureFunctions.getNoSquadronsAssignedToCarrier(spaceship, galaxy.getSpaceships());
				int sqdMovingToCarrier = getNoSquadronsMovingToCarrier(spaceship, galaxy.getSpaceships());
				if ((slotsFull + sqdMovingToCarrier) < maxSlots) {
					carriersWithFreeSlots.add(spaceship);
				}
			}
		}
		return carriersWithFreeSlots;
	}

	private static int getNoSquadronsMovingToCarrier(Spaceship aCarrier, List<Spaceship> spaceships) {
		int count = 0;
		Player aPlayer = aCarrier.getOwner();
		List<Spaceship> shipsAtPlanet = SpaceshipPureFunctions.getPlayersSpaceshipsOnPlanet(aPlayer, aCarrier.getLocation(), spaceships);
		for (Spaceship aSpaceship : shipsAtPlanet) {
			if (aSpaceship.getSize() == SpaceShipSize.SQUADRON) {
				// check if sstemp has a move order to the carrier
				if (aPlayer != null) {
					boolean moveToCarrierOrder = checkShipToCarrierMove(aSpaceship, aCarrier, aPlayer.getOrders());
					if (moveToCarrierOrder) {
						count++;
					}
				}
			}
		}
		return count;
	}

	private static boolean playerHasCarrierAtPlanet(Player aPlayer, Planet aPlanet, Galaxy galaxy) {
		boolean found = false;
		for (Iterator<Spaceship> iter = galaxy.getSpaceships().iterator(); iter.hasNext();) {
			Spaceship aShip = iter.next();
			if (aShip.getOwner() == aPlayer) {
				if (aShip.getLocation() == aPlanet) {
					if (SpaceshipPureFunctions.isCarrier(aShip)) {
						found = true;
					}
				}
			}
		}
		return found;
	}

	// kolla om det finns en gammal order för detta skepp
	private static boolean checkShipToCarrierMove(Spaceship aSqd, Spaceship aCarrier, Orders orders) {
		return orders.getShipToCarrierMoves().stream().filter(move -> aSqd.getUuid().equalsIgnoreCase(move.getSpaceShipKey())).anyMatch(move -> aCarrier.getUuid().equalsIgnoreCase(move.getDestinationCarrierKey()));
	}
}
