//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger på Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package sr.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import sr.enums.HighlightType;
import sr.general.Functions;
import sr.general.logging.Logger;

public class BlackMarketOffer implements Serializable {
  static final long serialVersionUID = 1L;
  VIPType offeredVIPType;
  SpaceshipType offeredShiptype;
  SpaceshipType offeredShiptypeBlueprint;
  TroopType offeredTroopType;
  boolean hotStuff;
  int uniqueId,hotStuffAmount;
  List<BlackMarketBid> bids;
  //Galaxy g;
  private int lastTurnAction = 0;

  public BlackMarketOffer(Galaxy g, int uniqueId) {
    this.uniqueId = uniqueId;
    int r = 0;
    if (g.hasTroops()){
    	r = Functions.getRandomInt(1,12);
    }else{
    	r = Functions.getRandomInt(1,9);
    }
    lastTurnAction = g.getTurn();
    if (r <= 3){ // ship
    	offeredShiptype = g.getRandomCommonShiptype();
    }else
    if ((r >= 4) & (r <= 5)){ // hot stuff
        hotStuff = true;
        hotStuffAmount = Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6);
    }else
    if ((r >= 6) & (r <= 8)){ // VIP
    	boolean canBeUsed = false;
    	int tries = 0;
    	while (!canBeUsed & (tries < 100)){
    		offeredVIPType = g.getRandomVIPType();
    		tries++;
    		canBeUsed = g.vipCanBeUsed(offeredVIPType);
    	}
    	if (!canBeUsed){ // if no vip was found use a hot stuff instead
    		hotStuffAmount = Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6);
    	}
    }else
    if (r == 9){ // shiptype blueprint
    	offeredShiptypeBlueprint = g.getRandomShipBlueprint();
    	if (offeredShiptypeBlueprint == null){ // if no shiptype was found use a hot stuff instead
    		hotStuffAmount = Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6);
    	}
    }else{ // 10-12 troop
        offeredTroopType = g.getRandomCommonTroopType();
    }
    bids = new LinkedList<BlackMarketBid>();
  }

  public String getString(){
    String returnString = "Hot stuff";
    if (offeredVIPType != null){
      returnString = offeredVIPType.getTypeName();
    }else
    if (offeredShiptype != null){
      returnString = offeredShiptype.getName();
    }else
    if (offeredTroopType != null){
        returnString = offeredTroopType.getUniqueName();
    }else
    if (offeredShiptypeBlueprint != null){
        returnString = "Blueprint: " + offeredShiptypeBlueprint.getName();
    }
    return returnString;
  }

  public String getTypeString(){
	  String returnString = "Illegal goods for sale";
	  if (offeredVIPType != null){
		  returnString = "VIP for hire";
	  }else
	  if (offeredShiptype != null){
		  returnString = "Spaceship for sale";
	  }else
	  if (offeredTroopType != null){
		  returnString = "Troop unit for hire";
	  }else
	  if (offeredShiptypeBlueprint != null){
		  returnString = "Ship blueprint for sale";
	  }
	  return returnString;
  }

  public int getUniqueId(){
    return uniqueId;
  }

  public void addBid(BlackMarketBid aBid){
    bids.add(aBid);
  }
  
  public boolean tooOld(Galaxy galaxy){
	  boolean old = false;
	  if ((lastTurnAction + 2) < galaxy.getTurn()){
		  old = true;
	  }
	  return old;
  }
  
  public void createRemovedOldMessage(Galaxy galaxy){
	  // create turn info row for all undefeated players 
      galaxy.addBlackMarketMessages(null,getString() + " removed from Black market due to lack of bids for 3 turns.");
  }

  public boolean performSelling(Galaxy galaxy){
    boolean sold = false;
    if (bids.size() > 0){
	  Logger.finest( "performSelling: bids.size(): " + bids.size());
      BlackMarketBid winningBid = getHighestBidder(galaxy);
      if (winningBid == null){ // no-one won the offer
        sendDrawMessages(galaxy);
        // send messages who failed to win this bidding
        sendRefundingMessages(winningBid, galaxy);
        // delete old bids
        bids = new LinkedList<BlackMarketBid>();
      }else{
        sold = true;
        Planet destinationPlanet = galaxy.getPlanet(winningBid.getDestination());
		Logger.finest( "performSelling: winningBid: " + winningBid.getText() + winningBid.getPlayerName());
        Player winningPlayer = galaxy.getPlayer(winningBid.getPlayerName());
		Logger.finest( "performSelling: winningPlayer: " + winningPlayer.getName());
        winningPlayer.addToLatestBlackMarketMessages("You have won the bidding for a " + getString() + " at the cost of " + winningBid.getCost() + ".");
        if (hotStuff){
   		  Logger.finest( "performSelling: hotStuff");
          winningPlayer.removeFromTreasury(winningBid.getCost());
          winningPlayer.addToTreasury(hotStuffAmount);
          winningPlayer.addToLatestBlackMarketMessages("The Hot Stuff have given you +" + hotStuffAmount + " extra income this turn.");
          winningPlayer.addToHighlights(String.valueOf(hotStuffAmount),HighlightType.TYPE_HOT_STUFF_WON);
        }else
        if (offeredVIPType != null){ // is vip
    	  Logger.finest( "performSelling: vip: ");
          VIP newVIP = offeredVIPType.createNewVIP(winningPlayer,destinationPlanet, true);
          galaxy.allVIPs.add(newVIP);
          winningPlayer.removeFromTreasury(winningBid.getCost());
          winningPlayer.addToLatestBlackMarketMessages("Your new " + offeredVIPType.getTypeName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
          winningPlayer.addToHighlights(String.valueOf(offeredVIPType.getTypeName()),HighlightType.TYPE_VIP_BOUGHT);
        }else
        if (offeredShiptype != null){ // is spaceship
    	  Logger.finest("performSelling: ship: ");
//          Spaceship newShip = g.getShipType(offeredShiptype.getName()).getShip(null,0,0);
          Spaceship newShip = winningPlayer.findSpaceshipType(offeredShiptype.getName()).getShip(null,0,0);
          newShip.setOwner(winningPlayer);
          newShip.setLocation(destinationPlanet);
          galaxy.addSpaceship(newShip);
          winningPlayer.removeFromTreasury(winningBid.getCost());
          winningPlayer.addToLatestBlackMarketMessages("Your new " + newShip.getName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
          winningPlayer.addToHighlights(offeredShiptype.getName(),HighlightType.TYPE_SHIP_WON);
        }else
        if (offeredShiptypeBlueprint != null){ // is spaceship blueprints
        	Logger.finest("performSelling: shiptype blueprints: ");
    		winningPlayer.removeFromTreasury(winningBid.getCost());
    		SpaceshipType aSST = winningPlayer.findOwnSpaceshipType(offeredShiptypeBlueprint.getName()); 
        	if (aSST != null){
        		if (aSST.isAvailableToBuild()){ // check if the player already have the shiptype
        			winningPlayer.addToLatestBlackMarketMessages("You already could build ships of the type " + offeredShiptypeBlueprint.getName() + ".");
        		}else{
        			aSST.setAvailableToBuild(true);
        			winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + offeredShiptypeBlueprint.getName() + ".");
        		}
        		winningPlayer.addToHighlights(offeredShiptypeBlueprint.getName(),HighlightType.TYPE_SHIPTYPE_WON);
        	}else{
        		SpaceshipType newShipType = new SpaceshipType(offeredShiptypeBlueprint);
        		newShipType.setAvailableToBuild(true); // sätter för säkerhets skull
        		winningPlayer.addSpaceshipType(newShipType);
        		winningPlayer.removeOtherSpaceshipType(offeredShiptypeBlueprint);
        		winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + offeredShiptypeBlueprint.getName() + ".");
        		winningPlayer.addToHighlights(offeredShiptypeBlueprint.getName(),HighlightType.TYPE_SHIPTYPE_WON);
        	}
        }else{ // is troop
        	Logger.finest( "performSelling: troop: " + offeredTroopType.getUniqueName());
        	Troop newTroop = winningPlayer.getGalaxy().findTroopType(offeredTroopType.getUniqueName()).getTroop(null,0,0);
        	newTroop.setOwner(winningPlayer);
        	newTroop.setPlanetLocation(destinationPlanet);
        	galaxy.addTroop(newTroop);
        	winningPlayer.removeFromTreasury(winningBid.getCost());
        	winningPlayer.addToLatestBlackMarketMessages("Your new " + newTroop.getUniqueName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
        	winningPlayer.addToHighlights(offeredTroopType.getUniqueName(),HighlightType.TYPE_TROOP_WON);
        }
        // send messages to everyone except the winner
        galaxy.addBlackMarketMessages(winningPlayer,getString() + " sold to Govenor " + winningPlayer.getGovenorName() + " for cost: " + winningBid.getCost() + ".");
        // send messages who failed to win this bidding
        sendRefundingMessages(winningBid, galaxy);
      }
    }else{
    	galaxy.addBlackMarketMessages(null,getString() + " not sold - not bids yet.");
    }
    return sold;
  }

  private void sendRefundingMessages(BlackMarketBid winningBid, Galaxy galaxy){
    for (int i = 0; i < bids.size(); i++){
      BlackMarketBid aBid = bids.get(i);
      if (aBid != winningBid){
    	  galaxy.getPlayer(aBid.getPlayerName()).addToLatestBlackMarketMessages("Your bid of " + aBid.getCost() + " have been refunded.");
      }
    }
  }

  private void sendDrawMessages(Galaxy galaxy){
    for (int i = 0; i < bids.size(); i++){
      BlackMarketBid aBid = bids.get(i);
      galaxy.getPlayer(aBid.getPlayerName()).addToLatestBlackMarketMessages("The bidding for a " + getString() + " ended in a draw at the cost of " + getHighestBid() + ".");
      galaxy.getPlayer(aBid.getPlayerName()).addToLatestBlackMarketMessages("The " + getString() + " will remain for sale.");
    }
  }

  private BlackMarketBid getHighestBidder(Galaxy galaxy){
    BlackMarketBid maxBid = null;
    boolean draw = false;
    for (int i = 0; i < bids.size(); i++){
      BlackMarketBid aBid = bids.get(i);
      if (maxBid == null){
        maxBid = aBid;
        lastTurnAction = galaxy.getTurn();
      }else
      if (aBid.getCost() > maxBid.getCost()){
        maxBid = aBid;
        draw = false;
      }else
      if (aBid.getCost() == maxBid.getCost()){
        draw = true;
      }
    }
    if (draw){ // if the bidding was a draw, no-one wins...
      maxBid = null;
    }
    return maxBid;
  }

  private int getHighestBid(){
    int maxBid = 0;
    for (int i = 0; i < bids.size(); i++){
      BlackMarketBid aBid = bids.get(i);
      if (maxBid == 0){
        maxBid = aBid.getCost();
      }else
      if (aBid.getCost() > maxBid){
        maxBid = aBid.getCost();
      }
    }
    return maxBid;
  }

  public boolean isHotStuff(){
    return hotStuff;
  }
  
  public boolean isShip(){
	  return offeredShiptype != null;
  }

  public boolean isShipBlueprint(){
	  return offeredShiptypeBlueprint != null;
  }

  public boolean isVIP(){
	return offeredVIPType != null;
  }

  public boolean isTroop(){
	  return offeredTroopType != null;
  }

  public VIPType getVIPType(){
	  return offeredVIPType;
  }

  public VIPType getOfferedVIPType() {
	  return offeredVIPType;
  }

  public SpaceshipType getOfferedShiptype() {
	  return offeredShiptype;
  }

  public SpaceshipType getOfferedShiptypeBlueprint() {
	  return offeredShiptypeBlueprint;
  }
  
  public SpaceshipType getShipType(){
	  SpaceshipType aSST = offeredShiptype;
	  if (aSST == null){
		  aSST = offeredShiptypeBlueprint;
	  }
	  return aSST;
  }

  public TroopType getOfferedTroopType() {
	  return offeredTroopType;
  }
  
  public boolean destinationRequired(){
	  return isVIP() | isShip() | isTroop();
  }

}