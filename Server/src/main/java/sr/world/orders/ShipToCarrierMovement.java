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
import sr.world.TurnInfo;

public class ShipToCarrierMovement implements Serializable{
  private static final long serialVersionUID = 1L;
  private int ss, destinationCarrier;
  //Spaceship ss;
  //Spaceship destinationCarrier;

  public ShipToCarrierMovement(Spaceship ss, Spaceship destinationCarrier){
    this.ss = ss.getId();
    this.destinationCarrier = destinationCarrier.getId();
  }

  public void performMove(TurnInfo ti, Galaxy aGalaxy){
	  Spaceship aSpaceship = aGalaxy.findSpaceship(ss);
	  Spaceship aSpaceshipCarrier = aGalaxy.findSpaceship(destinationCarrier);
	  Logger.finest( "performMove: " + aSpaceship.getName() + " destination: " + aSpaceshipCarrier.getName());
	  aSpaceship.moveShip(aSpaceshipCarrier,ti);
  }

  public int getDestinationCarrierId(){
    return destinationCarrier;
  }

  public Spaceship getDestinationCarrier(Galaxy aGalaxy){
	  Spaceship aSpaceshipCarrier = aGalaxy.findSpaceship(destinationCarrier);
	  return aSpaceshipCarrier;
  }

  public int getSpaceshipId(){
    return ss;
  }

  /* behövs ej???
  public String getSpaceshipUniqueName(){
    return ss.getUniqueName();
  }
  */

  public String getText(Galaxy aGalaxy){
	  Spaceship aSpaceship = aGalaxy.findSpaceship(ss);
	  Spaceship aSpaceshipCarrier = aGalaxy.findSpaceship(destinationCarrier);
	  
	  String retStr = null;
	  if (aSpaceship.getLocation() != null){
		  retStr = "Move " + aSpaceship.getName() + " from " + aSpaceship.getLocation().getName() + " to " + aSpaceshipCarrier.getName() + ".";
	  }else{
		  retStr = "Move " + aSpaceship.getName() + " from " + aSpaceship.getCarrierLocation().getName() + " to " + aSpaceshipCarrier.getName() + ".";
	  }
    return retStr;
  }

  public boolean isThisShip(Spaceship sSpaceship){
    return sSpaceship.getId() == ss;
  }

  public boolean isThisDestination(Spaceship aCarrier){
	  return aCarrier.getId() == destinationCarrier;
  }
}