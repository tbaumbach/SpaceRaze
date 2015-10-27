//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger på Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package sr.world.orders;
import java.io.Serializable;

import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Troop;
import sr.world.TurnInfo;

public class TroopToPlanetMovement implements Serializable{
  static final long serialVersionUID = 1L;
  private int troopId;
  private String planetName;
  private int turn;

  public TroopToPlanetMovement(Troop theTroop, Planet destination, int turn){
    this.troopId = theTroop.getId();
    this.planetName = destination.getName();
    this.turn = turn;
  }

  
  public void performMove(TurnInfo ti, Galaxy aGalaxy){
	  Troop aTroop = aGalaxy.findTroop(troopId);
	  Planet aPlanet = aGalaxy.getPlanet(planetName);
	  if(aTroop == null || aPlanet == null){
		  Logger.severe( "performMove Error: troopId= " + troopId + " planetName= " + planetName);
	  }else{
		  Logger.finest( "performMove: " + aTroop.getUniqueName() + " destination: " + aPlanet.getName());
		  aTroop.move(aPlanet,ti);
		  aTroop.setLastPlanetMoveTurn(turn);
	  }
  }

  public String getDestinationName(){
	  return planetName;
  }

  public Planet getDestination(Galaxy aGalaxy){
	  return aGalaxy.getPlanet(planetName);
  }

  public int getTroopId(){
	  return troopId;
  }

  public Troop getTroop(Galaxy aGalaxy){
	  return aGalaxy.findTroop(troopId);
  }

  public String getText(Galaxy aGalaxy){
	  Troop aTroop = aGalaxy.findTroop(troopId);
	  return "Move " + aTroop.getUniqueName() + " from " + aTroop.getShipLocation().getName() + " to " + getDestinationName() + ".";
  }

  public boolean isThisTroop(Troop aTroop){
	  return aTroop.getId() == troopId;
  }

  public boolean isThisDestination(Planet aPlanet){
	  return aPlanet.getName().equalsIgnoreCase(planetName);
  }

public int getTurn() {
	return turn;
}

public void setTurn(int turn) {
	this.turn = turn;
}
}