//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger på Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package sr.world.orders;

import java.io.Serializable;

import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Spaceship;
import sr.world.Troop;
import sr.world.TurnInfo;
import sr.world.VIP;

public class VIPMovement implements Serializable{
  static final long serialVersionUID = 1L;
  private int aVIP;
  private String planetDestination = null;
  private String shipDestination = null;
  private int troopDestination;

  public VIPMovement(VIP aVIP, Planet planetDestination){
    this.aVIP = aVIP.getId();
    this.planetDestination = planetDestination.getName();
  }

  public VIPMovement(VIP aVIP, Spaceship shipDestination){
    this.aVIP = aVIP.getId();
    this.shipDestination = shipDestination.getName();
  }

  public VIPMovement(VIP aVIP, Troop troopDestination){
	  this.aVIP = aVIP.getId();
	  this.troopDestination = troopDestination.getUniqueId();
  }


  public String getPlanetDestinationName(){
    return planetDestination;
  }

  public boolean isPlanetMove(){
    return planetDestination != null;
  }

  public boolean isShipMove(){
	  return shipDestination != null;
  }

  public void performMove(TurnInfo ti, Galaxy aGalaxy){
	VIP tempVIP = aGalaxy.findVIP(aVIP);
    if (isPlanetMove()){
    	tempVIP.moveVIP(aGalaxy.getPlanet(planetDestination),ti);
    }else
    if (isShipMove()){
    	tempVIP.moveVIP(aGalaxy.findSpaceship(shipDestination, tempVIP.getBoss()),ti);
    }else{ // troop move
    	tempVIP.moveVIP(aGalaxy.findTroop(troopDestination), ti);
    }
  }

  public String getDestinationName(boolean longName, Galaxy aGalaxy){
    String returnValue = "";
    if (isPlanetMove()){
      returnValue = planetDestination;
    }else
    if (isShipMove()){
    	if (longName){
    		returnValue =  aGalaxy.findSpaceship(shipDestination,aGalaxy.findVIP(aVIP).getBoss()).getUniqueName();
    	}else{
    		returnValue = aGalaxy.findSpaceship(shipDestination,aGalaxy.findVIP(aVIP).getBoss()).getShortName();
    	}
    }else{ // troop
    	if (longName){
    		returnValue = aGalaxy.findTroop(troopDestination).getUniqueName();
    	}else{
    		returnValue = aGalaxy.findTroop(troopDestination).getUniqueShortName();
    	}
    	
    }
    return returnValue;
  }
/*
  public int getShipDestinationId(){
    return shipDestination.getId();
  }
*/
  public int getVIPId(){
    return aVIP;
  }

  public int getTroopId(){
	  return troopDestination;
  }

  public String getText(Galaxy aGalaxy){
    return "Move " + aGalaxy.findVIP(aVIP).getName() + " from " + aGalaxy.findVIP(aVIP).getLocationString() + " to " + getDestinationName(true, aGalaxy) + ".";
  }

  public boolean isThisVIP(VIP tempVIP){
    return this.aVIP == tempVIP.getId();
  }
}
