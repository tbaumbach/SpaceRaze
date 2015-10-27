//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger på Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package sr.world.orders;
import java.io.Serializable;

import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Spaceship;
import sr.world.Troop;
import sr.world.TurnInfo;

public class TroopToCarrierMovement implements Serializable{
  private static final long serialVersionUID = 1L;
  
  int troopId;
  int destinationCarrierId;
  
  //Troop troop;
  //Spaceship destinationCarrier;

  public TroopToCarrierMovement(Troop theTroop, Spaceship destinationCarrier){
    this.troopId = theTroop.getId();
    this.destinationCarrierId = destinationCarrier.getId();
  }

 
  public void performMove(TurnInfo ti, Galaxy aGalaxy){
	  Troop aTroop = aGalaxy.findTroop(troopId);
	  Spaceship destinationCarrier = aGalaxy.findSpaceship(destinationCarrierId);
	  if(aTroop == null || destinationCarrier == null){
		  Logger.severe( "performMove Error: troopId= " + troopId + " destinationCarrierId= " + destinationCarrierId);
	  }else{
		  Logger.finest( "performMove: " + aTroop.getUniqueName() + " destination: " + destinationCarrier.getName());
		  aTroop.move(destinationCarrier,ti);
	  }
	  
  }

  public int getDestinationCarrierId(){
    return destinationCarrierId;
  }

  public Spaceship getDestinationCarrier(Galaxy aGalaxy){
	  return aGalaxy.findSpaceship(destinationCarrierId);
  }

  public int getTroopId(){
	  return troopId;
  }

  public String getText(Galaxy aGalaxy){
	  Troop aTroop = aGalaxy.findTroop(troopId);
	  Spaceship destinationCarrier = aGalaxy.findSpaceship(destinationCarrierId);
	  String retStr = null;
	  if (aTroop.getPlanetLocation() != null){
		  retStr = "Move " + aTroop.getUniqueName() + " from " + aTroop.getPlanetLocation().getName() + " to " + destinationCarrier.getName() + ".";
	  }else{ // move from ship to ship
		  retStr = "Move " + aTroop.getUniqueName() + " from " + aTroop.getShipLocation().getName() + " to " + destinationCarrier.getName() + ".";
	  }
    return retStr;
  }

  public boolean isThisTroop(Troop aTroop){
    return aTroop.getId() == troopId;
  }

  public boolean isThisDestination(Spaceship aCarrier){
	  return aCarrier.getId() == destinationCarrierId;
  }
}