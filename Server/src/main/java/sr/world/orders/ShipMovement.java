//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger p� Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package sr.world.orders;

import java.io.Serializable;

import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Spaceship;
import sr.world.TurnInfo;

public class ShipMovement implements Serializable {
	static final long serialVersionUID = 1L;
	private String planetName, owner;
	private int spaceShipID;
	// Planet destination;
	// Spaceship ss;

	
	public ShipMovement(){}              
	
	public ShipMovement(Spaceship ss, Planet destination) {
		this.spaceShipID = ss.getId();
		this.planetName = destination.getName();
		this.owner = ss.getOwner().getName();
	}

	/*
	 * // anropas vid uppdatering av en spelares orders public
	 * ShipMovement(ShipMovement oldShipMove,Galaxy newGalaxy){ destination =
	 * newGalaxy.findPlanet(oldShipMove.getDestinationName()); ss =
	 * newGalaxy.findSpaceship(oldShipMove.getSpaceshipId()); }
	 */

	public void performMove(TurnInfo ti, Galaxy aGalaxy) {
		Spaceship spaceship = aGalaxy.findSpaceship(spaceShipID);
		if (spaceship != null) {
			String spaceShipname = spaceship.getName();
			Logger.finest("performMove: " + spaceShipname + " destination: " + planetName);
			aGalaxy.findSpaceship(spaceShipname, aGalaxy.getPlayer(owner)).moveShip(planetName, ti);
		}

	}

	/*
	 * public int getSpaceshipId(){ return ss.getId(); }
	 */

	/*
	 * beh�vs ej??? public String getSpaceshipUniqueName(){ return
	 * ss.getUniqueName(); }
	 */

	public String getText(Galaxy aGalaxy) {
		String spaceShipname = aGalaxy.findSpaceship(spaceShipID).getName();
		return "Move " + spaceShipname + " from "
				+ aGalaxy.findSpaceship(spaceShipname, aGalaxy.getPlayer(owner)).getLocation().getName() + " to "
				+ planetName + ".";
	}

	public boolean isThisShip(Spaceship sSpaceship) {
		return sSpaceship.getId() == spaceShipID;
	}

	public String getDestinationName() {
		return planetName;
	}

	public String setDestinationName(String planetName) {
		return this.planetName = planetName;
	}

	public int getSpaceShipID() {
		return spaceShipID;
	}

	public int setSpaceShipID(int spaceShipID) {
		return this.spaceShipID = spaceShipID;
	}

	public String getOwner() {
		return owner;
	}

	public String setOwner(String owner) {
		return this.owner = owner;
	}

}