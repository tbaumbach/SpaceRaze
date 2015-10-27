package sr.world;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import sr.enums.SpaceshipTargetingType;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.spacebattle.AttackReportSpace;

/**
 * 
 * @author WMPABOD
 *
 * TaskForce är en tillfällig samling rymdskepp vid en planet och 
 * skapas för att hantera konflikter
 */
public class TaskForce implements Serializable, Cloneable{  // serialiseras denna någonsin??
    static final long serialVersionUID = 1L;
    private List<Spaceship> allss = null, destroyedShips, retreatedShips;
    private Player player;
    private boolean runningAway = false, isDestroyed = false;
    private Galaxy g;

    public TaskForce(Player p, Galaxy g){
      allss = new Vector<Spaceship>();
      destroyedShips = new Vector<Spaceship>();
      retreatedShips = new Vector<Spaceship>();
      player = p;
      this.g = g;
    }
    
    public boolean isDefender(Player aPlayer){
    	boolean defender = false;
    	if (player != null){
    		if (aPlayer != null){
    			defender = aPlayer.getName().equals(player.getName());
    		}    		
    	}else{
    		if (aPlayer == null){
    			defender = true;
    		}
    	}
    	return defender;
    }
    
    /**
     * Makes a shallow clone of this object (TaskForce), but also makes shallow clones of all ships in this taskforce.
     */
    @Override
	public TaskForce clone(){
    	TaskForce clonedTaskForce = null;
		try {
			clonedTaskForce = (TaskForce)super.clone();
			clonedTaskForce.setAllSpaceships(cloneShipList(allss));
			clonedTaskForce.setDestroyedSpaceships(cloneShipList(destroyedShips));
			clonedTaskForce.setRetreatingSpaceships(cloneShipList(retreatedShips));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clonedTaskForce;
	}
    
    private List<Spaceship> cloneShipList(List<Spaceship> originalList){
		List<Spaceship> clonedSpaceships = null;
		if (originalList != null){
			clonedSpaceships = new LinkedList<Spaceship>();
			for (Spaceship aSpaceship : originalList) {
				clonedSpaceships.add(aSpaceship.clone());
			}
		}
		return clonedSpaceships;
    }
    
    private void setAllSpaceships(List<Spaceship> newShipList){
    	allss = newShipList;
    }

    private void setDestroyedSpaceships(List<Spaceship> newShipList){
    	destroyedShips = newShipList;
    }

    private void setRetreatingSpaceships(List<Spaceship> newShipList){
    	retreatedShips = newShipList;
    }
   
    /**
     * removes any squadrons in this taskforce who will not survive
     *
     */
    public void checkAbandonedSquadrons(Planet thePlanet){
      if (player != null){
    	  Logger.finest("checkAbandonedSquadron in TaskForce called: " + player.getName());
      }else{
    	  Logger.finest("checkAbandonedSquadron in TaskForce called: neutral");
      }
  	  boolean sqdSurvive = g.getGameWorld().isSquadronsSurviveOutsideCarriers();
  	  List<Spaceship> tfSpaceships = Functions.cloneList(allss);
  	  Collections.shuffle(tfSpaceships);
  	  List<Spaceship> removeShips = new LinkedList<Spaceship>();
  	  boolean addSpace = false;
  	  for (Spaceship aShip : tfSpaceships) {
  		if (aShip.isSquadron()){
  			if (aShip.getCarrierLocation() == null){ // sqd is not in a carrier
  				if (aShip.getLocation() == null){
  					// should not happen (retreating squadrons??)
  				}else
  				if (player != null && aShip.getLocation().getPlayerInControl() == null){ // neutral planet
  					if (!sqdSurvive){
  						// handle sqds auto moves to carriers
  						List<Spaceship> carriersWithFreeSlots = g.getCarriersWithFreeSlotsInSystem(aShip.getLocation(),player);
  						if (carriersWithFreeSlots.size() > 0){
  							Collections.shuffle(carriersWithFreeSlots);
  							Spaceship aCarrier = carriersWithFreeSlots.get(0);
  							aShip.setCarrierLocation(aCarrier);
  							aShip.setOldLocation(aShip.getLocation());
  							String oldLocString = aShip.getLocation().getName();
  							Logger.finer("TaskForce CarrierLocation: " + aShip.getCarrierLocation());
  							if (aShip.getOwner() != null){
  								aShip.getOwner().addToGeneral("Your sguadron " + aShip.getName() + " has been attached to a nearby carrier (" + aCarrier.getName() + ") in the system " + oldLocString + ".");
  							}
  						}else{
  							removeShips.add(aShip);
  						}
  						addSpace = true;
  					}else
  					// squadrons will survive if at least one carrier exist
  					if (!g.playerHasCarrierAtPlanet(aShip.getOwner(),aShip.getLocation())){
  						// add ship to remove list
  						removeShips.add(aShip);
  						addSpace = true;
  					}
  				}else // planet is not neutral, check if it belongs to another faction
  				if(player != null && aShip.getOwner().getFaction() != aShip.getLocation().getPlayerInControl().getFaction()){
  					if (!sqdSurvive){
  						// handle sqds auto moves to carriers
  						List<Spaceship> carriersWithFreeSlots = g.getCarriersWithFreeSlotsInSystem(aShip.getLocation(),player);
  						if (carriersWithFreeSlots.size() > 0){
  							Collections.shuffle(carriersWithFreeSlots);
  							Spaceship aCarrier = carriersWithFreeSlots.get(0);
  							aShip.setCarrierLocation(aCarrier);
  							aShip.setOldLocation(aShip.getLocation());
  							String oldLocString = aShip.getLocation().getName();
  							Logger.finer("Carrier Location: " + aCarrier);
  							if (aShip.getOwner() != null){
  								aShip.getOwner().addToGeneral("Your sguadron " + aShip.getName() + " has been attached to a nearby carrier (" + aCarrier.getName() + ") in the system " + oldLocString + ".");
  							}
  							addSpace = true;
  						}else{
  							removeShips.add(aShip);
  							addSpace = true;
  						}
  					}else
  					if (!g.playerHasCarrierAtPlanet(aShip.getOwner(),aShip.getLocation())){
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
  		  if (owner != null){
  			  owner.addToGeneral("Your sguadron " + aShip.getName() + " has been scuttled by it's crew because they had no supporting carrier in the system " + aShip.getLocation().getName() + ".");
  	  		  owner.addToShipsLostInSpace(aShip);
  			  g.checkVIPsInDestroyedShips(aShip, owner);
  		  }
  		  Player controllingPlayer = thePlanet.getPlayerInControl();
  		  if (controllingPlayer != null){
  			  if (controllingPlayer != aShip.getOwner()){
  				  if (aShip.getOwner() != null){
  					  controllingPlayer.addToGeneral(Functions.getDeterminedForm(aShip.getTypeName(), true) + " " + aShip.getTypeName() + " belonging to Governor " + aShip.getOwner().getGovenorName() + " has been scuttled in the " + thePlanet.getName() + " system, due to lack of carrier.");
  				  }else{
  					  // neutral forces cannot besiege other planets, this should not happen
  				  }
  			  }
  		  }
  		  g.removeShip(aShip);
  	  }
  	  if (addSpace & (player != null)){
  		  player.addToGeneral("");
  	  }
    }
    
    public void reloadSquadrons(){
    	for (Iterator<Spaceship> iter = allss.iterator(); iter.hasNext();) {
			Spaceship aShip = (Spaceship) iter.next();
			if (aShip.isSquadron()){
				if (aShip.getCarrierLocation() != null){
					aShip.supplyWeapons(4);
				}
			}
		}
    }

    public int getLargestShipSize(){
      int maxsize = 0;
      for (int i = 0; i < allss.size(); i++){
        Spaceship tempss = allss.get(i);
        if (tempss.getSize() > maxsize){
          maxsize = tempss.getSize();
        }
      }
      return maxsize;
    }

    public String getLargestShipSizeString(){
      String maxSizeString = "none";
      int maxSize = -1;
      Spaceship maxss = null;
      for (int i = 0; i < allss.size(); i++){
        Spaceship tempss = allss.get(i);
        if (tempss.getSize() > maxSize){
          maxSize = tempss.getSize();
          maxss = tempss;
        }
      }
      if (maxss != null){
        maxSizeString = maxss.getSizeString();
      }
      return maxSizeString;
    }

    public List<Spaceship> getDefenders(){    // används denna?
      return allss;
    }

    public int getTotalNrShips(){
      return allss.size();
    }

    public int getTotalNrNonDestroyedShips(){
    	int count = 0;
    	for (Spaceship aShip : allss) {
			if (!aShip.isDestroyed()){
				count++;
			}
		}
        return count;
    }

    public int getTotalNrShips(boolean screened){
    	int totalCount = 0;
    	totalCount = totalCount + getNrCapitalShips(screened);
    	totalCount = totalCount + getNrFighters(screened);
    	return totalCount;
    }

    public int getNrCapitalShips(boolean screened){
      int returnValue = 0;
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
        if (ss.getScreened() == screened){
        	if (!ss.isSquadron()){
        		returnValue++;
        	}
        }
      }
      return returnValue;
    }

    public int getNrFighters(boolean screened){
        int returnValue = 0;
        for (int i = 0; i < allss.size(); i++){
          Spaceship ss =allss.get(i);
          if (ss.getScreened() == screened){
          	if (ss.isSquadron()){
          		returnValue++;
          	}
          }
        }
        return returnValue;
      }

    public int getTotalNrFirstLineShips(){
    	return getNrFirstLineCapitalShips() + getNrFirstLineFighters();
    }
    
    public int getNrFirstLineCapitalShips(){
      int firstLineNr = 0;
      if ((getTotalNrShips(false) > 0) & (getTotalNrShips(true) > 0)){
        firstLineNr = getNrCapitalShips(false);
      }else{
        firstLineNr = getNrCapitalShips(true) + getNrCapitalShips(false); // add both, there can only be ships in one of them
      }
      if (this.player != null){
    	  Logger.finer(this.player.getName() + " ffgetNrFirstLineCapitalShips() returns: " + firstLineNr);
      }else{
    	  Logger.finer("neutral ffgetNrFirstLineCapitalShips() returns: " + firstLineNr);
      }
      return firstLineNr;
    }

    public int getNrFirstLineFighters(){
        int firstLineNr = 0;
        if ((getTotalNrShips(false) > 0) & (getTotalNrShips(true) > 0)){
          firstLineNr = getNrFighters(false);
        }else{
          firstLineNr = getNrFighters(false) + getNrFighters(true); // add both, there can only be ships in one of them
        }
        Logger.finer("getNrFirstLineFighters() returns: " + firstLineNr);
        return firstLineNr;
      }

    /**
     * #ships = relative size
     * 1 = 1
     * 2 = 1.8
     * 3 = 2.5
     * 4 = 3
     * 6 = 3.4
     * 9 = 4
     * 16 = 5
     * 25 = 6
     * @return relative size value based on nr of firstline ships
     */
    public double getRelativeSize(){
    	double totalRelativeSize = 0;
    	totalRelativeSize = totalRelativeSize + getRelativeSize(true);
    	totalRelativeSize = totalRelativeSize + getRelativeSize(false);
    	if (getPlayer() != null){
    		Logger.finer(this.getPlayer().getName() + " getRelativeSize(): " + totalRelativeSize + " squadrons: " + getRelativeSize(false) + " capitals: " + getRelativeSize(true));
    	}else{
    		Logger.finer("neutral getRelativeSize(): " + totalRelativeSize + " squadrons: " + getRelativeSize(false) + " capitals: " + getRelativeSize(true));
    	}
    	return totalRelativeSize;
    }
        
    /**
     * 
     * @param capitalShips if false, only count fighters
     * @return
     */
    private double getRelativeSize(boolean capitalShips){
    	int firstLineNr = 0;
    	if (capitalShips){
    		firstLineNr = getNrFirstLineCapitalShips();
    	}else{
    		firstLineNr = getNrFirstLineFighters();
    	}
    	double relativeSize = 0;
    	if (firstLineNr == 0){
    		relativeSize = 0;
    	}else
    	if (firstLineNr == 1){
    		relativeSize = 1;
    	}else
    	if (firstLineNr == 2){
    		relativeSize = 1.8;
    	}else
        if (firstLineNr == 3){
        	relativeSize = 2.5;
        }else{
    		relativeSize = Math.pow(firstLineNr,0.5) + 1;
    	}
    	return relativeSize;
    }

    public String getStatus(){
      String status = "fighting";
      if (isDestroyed){
        status = "destroyed";
      }else
      if (allss.size() == 0){
        status = "ran away";
      }else
      if (runningAway){
        status = "running away";
      }
      return status;
    }

    public Spaceship getFiringShip(TaskForce opponentTF, Random r, AttackReportSpace attackReport){
      Spaceship firingShip = null;
      boolean gotAway = false;
      if (!runningAway){
        runAway(opponentTF);
      }
      Spaceship tempss = null;
      int nr = 0;
      int screennr = 0;
      if ((getTotalNrShips(false) > 0) & (getTotalNrShips(true) > 0) & (!runningAway)){
        int nrFirstLineShips = getTotalNrShips(false);
        Logger.finer("nrFirstLineShips: " + nrFirstLineShips);
        screennr = Math.abs(r.nextInt())%nrFirstLineShips;
        tempss = getShipAt(false,screennr);
        nr = allss.indexOf(tempss);
      }else{
        nr = Math.abs(r.nextInt())%allss.size();
        tempss = allss.get(nr);
      }
      if (runningAway & (tempss.getRange().canMove())){ // tempss försöker fly
    	attackReport.setWantsToRetreat(true);
        if (opponentTF.stopsRetreats()){
//          addToLatestBattleReport("Your ship " + tempss.getName() + " could not retreat due to an enemy ship that stops retreats.");
//          opponentTF.addToLatestBattleReport("An enemy " + tempss.getSpaceshipType().getName() + " was stopped by one of your ships that stops retreats, when trying to flee, and forced to fight instead.");
          firingShip = tempss;
        }else{ // tempss flyr
          gotAway = tempss.retreat();
          allss.remove(nr);
          if (tempss.isCarrier()){
        	  removeSquadronsFromCarrier(tempss);
          }else
          if (tempss.isSquadron()){
        	  tempss.setCarrierLocation(null);
          }
          if (!gotAway){ // skeppet förstördes då det inte fanns någonstans att fly till
        	// check if this is a carrier and if there are any squadrons located at it
        	// If thats the case, null their carrierLocation
            g.removeShip(tempss);
            destroyedShips.add(tempss);
            if (allss.size() == 0){
              isDestroyed = true;
            }
            addToLatestLostInSpace(tempss);
            opponentTF.addToLatestLostInSpace(tempss);
          }else{ // ship has run away
            retreatedShips.add(tempss);
          }
        }
      }else{
        firingShip = tempss;
      }
      attackReport.setAttackingShip(tempss);
      return firingShip;
    }
    
    private void removeSquadronsFromCarrier(Spaceship aCarrier){
    	for (Iterator<Spaceship> iter = allss.iterator(); iter.hasNext();) {
			Spaceship aShip = (Spaceship) iter.next();
			if (aShip.isSquadron()){
				if (aShip.getCarrierLocation() == aCarrier){
					aShip.setCarrierLocation(null);
				}
			}
		}
    }

    private void runAway(TaskForce opponentTF){
    	if (player != null){ // only player taskforces can run away, neutrals never run
    		// kolla om fienden är mer är 4ggr så stort tonnage, försöka fly i så fall
    		if ((4*getStrength()) < (opponentTF.getStrength())){
    			runningAway = true;
    		}
    	}
    }

    private Spaceship getShipAt(boolean screened, int position){
      Logger.finer("getShipAt: " + screened + " " + position);
      Spaceship returnss = null;
      int index = 0;
      int foundnr = 0;
      while (returnss == null){
    	Logger.finer("index: " + index);
        Spaceship tempss = allss.get(index);
        Logger.finer("tempss: " + tempss.getName());
        if (tempss.getScreened() == screened){
          if (foundnr == position){
            returnss = tempss;
          }else{
            foundnr++;
          }
        }
        index++;
      }
      return returnss;
    }

    public String shipHit(TaskForce tfshooting, Spaceship firingShip, Random r, AttackReportSpace attackReport){
      Logger.finest("called, firingShip: " + firingShip.getName());

      // returnera "destroyed" om inga skepp finns kvar i tf:n
      // returnera annars "fighting"
      String statusString = "fighting";
      Spaceship tempss = null;
      int nr = 0;
      boolean screenOnly = false;
      boolean canAttackScreened = firingShip.isCanAttackScreenedShips();
      if (!canAttackScreened){
    	  canAttackScreened = g.findVIPcanAttackScreened(firingShip);
      }
      if ((!canAttackScreened) && ((getTotalNrShips(false) > 0) & (getTotalNrShips(true) > 0))){
    	  screenOnly = true;
      }
      int aimedShotChance = tfshooting.getAimBonus() + 40;
//      int aimedShotChance = tfshooting.getAimBonus();
      boolean aimedShot = Functions.getD100(aimedShotChance);
      if (aimedShot){ // the shot will be aimed at the most damaged enemy ship
    	  // if none is damaged, the shot will be performed as normal
    	  if (noShipDamaged()){
    		  aimedShot = false;
    	  }else{
    		  // there are at least one damaged ship in the hit TF
    		  tempss = getMostDamagedShip(screenOnly);
    		  
    	  }
      }
      // if shot isn't aimed, perform shot as normal, i.e. use target weight etc
      if (!aimedShot){
    	  int targetingWeight = getTotalTargetingWeight(firingShip.getTargetingType(),screenOnly);
    	  int targetIndex = Math.abs(r.nextInt())%targetingWeight;
    	  tempss = getTargetedShip(firingShip.getTargetingType(),targetIndex,screenOnly);
      }
      if (tempss.getScreened() & (getTotalNrShips(false) > 0) & (getTotalNrShips(true) > 0)){
    	  Logger.severe("Screened ship hit!!! " + tempss.getUniqueName() + " aimedShot: " + aimedShot);
      }
	  nr = allss.indexOf(tempss);
      // perform shot
      int multiplier = getMultiplier(0);
      attackReport.setAttMultiplier(multiplier);
      Logger.finest("multiplier: " + multiplier);
      int damageNoArmor = firingShip.getDamageNoArmor(tempss,multiplier);
      attackReport.setDamageNoArmor(damageNoArmor);
      int damageLeftAfterShields = tempss.shipShieldsHit(damageNoArmor);
      attackReport.setDamageLeftAfterShields(damageLeftAfterShields);
      double afterShieldsDamageRatio = (damageLeftAfterShields*1.0d) / damageNoArmor;
      Logger.finer("afterShieldsDamageRatio: " + afterShieldsDamageRatio);
      int actualDamage = firingShip.getActualDamage(tempss,multiplier,afterShieldsDamageRatio);
      attackReport.setActualDamage(actualDamage);
      attackReport.setTargetSpaceship(tempss);
      String damagedStatus = tempss.shipHit(actualDamage,damageLeftAfterShields,damageNoArmor);
      Logger.finest("multiplier=" + multiplier + " damageNoArmor=" + damageNoArmor + " damageLeftAfterShields=" + damageLeftAfterShields + " afterShieldsDamageRatio=" + afterShieldsDamageRatio + " actualDamage=" + actualDamage + " damagedStatus=" + damagedStatus);
      if (tempss.isDestroyed()){
//        addToLatestBattleReport("Your ship " + tempss.getName() + " was destroyed when hit (" + damageNoArmor + ") by an enemy " + firingShip.getSpaceshipType().getName() + " (" + actualDamage + ").");
        addToLatestLostInSpace(tempss);
        firingShip.addToLatestShipsLostInSpace(tempss);
        firingShip.addKill();
        g.removeShip(tempss);
        allss.remove(nr);
        if (allss.size() == 0){
          isDestroyed = true;
        }
        destroyedShips.add(tempss);
      }
      if (allss.size() == 0){
        statusString = "destroyed";
      }
      return statusString;
    }
    
    private boolean noShipDamaged(){
    	boolean noShipDamaged = true;
    	int counter = 0;
    	while ((counter < allss.size()) & noShipDamaged){
    		Spaceship tmpss = allss.get(counter);
    		if (tmpss.isDamaged()){
    			noShipDamaged = false;
    		}else{
    			counter++;
    		}
    	}
    	return noShipDamaged;
    }
    
    private Spaceship getMostDamagedShip(boolean screenOnly){
    	Spaceship mostDamagedShip = null;
    	List<Spaceship> allSsClone = Functions.cloneList(allss);
    	Collections.shuffle(allSsClone);
    	for (Spaceship aSpaceship : allSsClone) {
    		if (!screenOnly | !aSpaceship.getScreened()){ // if screen only, only get ships in screen
				if (mostDamagedShip == null){
					mostDamagedShip = aSpaceship;
				}else{
					if (aSpaceship.getDamageLevel() < mostDamagedShip.getDamageLevel()){
						mostDamagedShip = aSpaceship;
					}
				}
    		}
		}
    	return mostDamagedShip;
    }
    
    public int getAimBonus(){
    	int totalAimBonus = 0;
    	// get aimBonus from ship and vip
    	totalAimBonus += getSpaceshipAimBonus();
    	VIP aimBonusVip = getAimBonusVIP();
    	if (aimBonusVip != null){
    		totalAimBonus += aimBonusVip.getAimBonus();
    	}
    	return totalAimBonus;
    }

    private int getTotalTargetingWeight(SpaceshipTargetingType targetingType, boolean screenOnly){
        Logger.finer("called, targetingType: " + targetingType + " screenOnly: " + screenOnly);
    	int totalWeight = 0;
    	for (Iterator<Spaceship> iter = allss.iterator(); iter.hasNext();) {
			Spaceship aShip = (Spaceship) iter.next();
	        Logger.finest("in for-loop, ship: " + aShip.getName() + " totalWeight: " + totalWeight);
			if (screenOnly){
		        Logger.finer("Screen only!");
				if (!aShip.getScreened()){
					totalWeight = totalWeight + targetingType.getTargetingWeight(aShip);
			        Logger.finer("Ship not in screen - adding weight: " + targetingType.getTargetingWeight(aShip));
				}
			}else{
				totalWeight = totalWeight + targetingType.getTargetingWeight(aShip);
		        Logger.finer("No screen exists - adding weight: " + targetingType.getTargetingWeight(aShip));
			}
		}
        Logger.finer("return totalWeight: " + totalWeight);
    	return totalWeight;
    }
    
    private Spaceship getTargetedShip(SpaceshipTargetingType targetingType, int targetIndex, boolean screenOnly){
        Logger.finer("called, targetingType: " + targetingType + " targetIndex: " + targetIndex + " screenOnly: " + screenOnly);
/*
        Spaceship targetShip = null;
    	int indexCounter = 0;
    	Spaceship currentSpaceship = allss.get(indexCounter);
    	int weightCounter = targetingType.getTargetingWeight(currentSpaceship);
		indexCounter++;
        LoggingHandler.finest(this,g,"getTargetedShip","before while-loop, currentSpaceship: " + currentSpaceship.getName() + " weightCounter: " + weightCounter + " indexCounter: " + indexCounter);
    	while ((indexCounter < allss.size()) && (weightCounter < targetIndex)){
    		currentSpaceship = allss.get(indexCounter);
    		weightCounter = weightCounter + targetingType.getTargetingWeight(currentSpaceship);
    		indexCounter++;
            LoggingHandler.finest(this,g,"getTargetedShip","end of while-loop, currentSpaceship: " + currentSpaceship.getName() + " weightCounter: " + weightCounter + " indexCounter: " + indexCounter);
    	}
    	targetShip = currentSpaceship;
        LoggingHandler.finest(this,g,"getTargetedShip","return targetship: " + targetShip.getName());
*/       
    	Spaceship targetShip = null;
    	int indexCounter = 0;
    	int weightCounter = 0;
        while (targetShip == null){
        	Spaceship currentSpaceship = allss.get(indexCounter);
			Logger.finer("currentSpaceship (" + indexCounter+ "): " + currentSpaceship.getUniqueName() + " (" + indexCounter + ")");
        	if ((!screenOnly) | (!currentSpaceship.getScreened())){ // if all ships, or if the ship is not screened
        		Logger.finer("Ship can be hei");
        		weightCounter = weightCounter + targetingType.getTargetingWeight(currentSpaceship);
        		Logger.finer("weightCounter: " + weightCounter + " targetIndex: " + targetIndex);
        		if (weightCounter > targetIndex){
        			Logger.finer("currentSpaceship targeted: " + currentSpaceship.getUniqueName());
        			targetShip = currentSpaceship;
        		}
        	}
    		indexCounter++;
//            LoggingHandler.finest(this,g,"getTargetedShip","end of while-loop, currentSpaceship: " + currentSpaceship.getName() + " weightCounter: " + weightCounter + " indexCounter: " + indexCounter);
        }        
//        LoggingHandler.finest(this,g,"getTargetedShip","return targetship: " + targetShip.getName());
    	return targetShip;
    }
    
    private int getMultiplier(int base){
    	int tempRandom = Functions.getRandomInt(1,20);
    	if (tempRandom > 18){
    		tempRandom = getMultiplier(base + tempRandom);
    	}else{
    		tempRandom = tempRandom + base;
    	}
        Logger.finest("base: " + base + " returns: " + tempRandom);
    	return tempRandom;
    }

    public void chasedAway(TaskForce chasingTF){
      Logger.finer("chased TF owner: " + player);
      Logger.finer("chasing TF owner: " + chasingTF.getPlayer());
      // Gå igenom alla rymdskepp som ej är skvadroner och gör så de flyr
      for (int i = allss.size()-1; i > -1 ; i--){
        Spaceship tempss = allss.get(i);
        Logger.finest("ship in loop: " + tempss);
        if (tempss.getRange().canMove() & !tempss.isSquadron()){
         
//          boolean planetExistsToRunTo = tempss.runAway(false);
          boolean planetExistsToRunTo = tempss.retreat();
          
          if(planetExistsToRunTo){
        	  retreatedShips.add(tempss);
              allss.remove(i);
          }
          if (!planetExistsToRunTo){
            // remove ship from game
           // tempss.getOwner().getGalaxy().getSpaceships().remove(tempss);
           // tempss.getOwner().getTurnInfo().addToLatestGeneralReport("Your ship " + tempss.getName() + " has been scuttled by its crew, when retreating from " + tempss.getOldLocation().getName() + " last turn, because there was nowhere they could run to.");
           // addToLatestLostInSpace(tempss);
          }
        }
      }
      // iterate through all squadrons and check if they run away in a carrier or by themselves
      for (int i = allss.size()-1; i > -1 ; i--){
          Spaceship tempss = allss.get(i);
          if (tempss.isSquadron()){
          	if (tempss.getCarrierLocation() == null){
          		if (tempss.getRange().canMove()){
          			// squadron is on its own, retreats as a capital ship
          			
              		boolean planetExistsToRunTo = tempss.retreat();
              		if (!planetExistsToRunTo){
              			// försök lägga till sqd till en flyende carrier som har plats.
              			if(tryAddRetreatedSqdToSomeRetreatedCarrierWithEmptySlots(tempss)){
              				tempss.squadronInRetreatingCarrier();
                  			retreatedShips.add(tempss);
                  			allss.remove(i);
              			}
              			
              			// remove ship from game
              		//	tempss.getOwner().getGalaxy().getSpaceships().remove(tempss);
              		//	tempss.getOwner().getTurnInfo().addToLatestGeneralReport("Your ship " + tempss.getName() + " has been scuttled by its crew, when retreating from " + tempss.getOldLocation().getName() + " last turn, because there was nowhere they could run to.");
              		//	addToLatestLostInSpace(tempss);
              		}else{
              			retreatedShips.add(tempss);
                  		allss.remove(i);
              		}
          		}else{
          			// försök lägga till sqd till en flyende carrier som har plats.
          			if(tryAddRetreatedSqdToSomeRetreatedCarrierWithEmptySlots(tempss)){
          				tempss.squadronInRetreatingCarrier();
              			retreatedShips.add(tempss);
              			allss.remove(i);
          			}
          		}
          	}else{
          		if (tempss.getCarrierLocation().isRetreating()){
          			tempss.squadronInRetreatingCarrier();
          			retreatedShips.add(tempss);
          			allss.remove(i);
          		}
          	}
          }
      }
    }
    
    private boolean tryAddRetreatedSqdToSomeRetreatedCarrierWithEmptySlots(Spaceship aSpaceship){
    	for(Spaceship tempSpaceship : retreatedShips){
    		if(tempSpaceship.isCarrier() && tempSpaceship.getSquadronCapacity() > tempSpaceship.getOwner().getGalaxy().getNoSquadronsAssignedToCarrier(tempSpaceship)){
    			aSpaceship.setCarrierLocation(tempSpaceship);
    			return true;
    		}
    	}
    	return false;
   }

    public boolean checkAllCanRetreat(){
      boolean allCanRetreat = true;
      for (int i = 0; i < allss.size() ; i++){
        Spaceship tempss = allss.get(i);
        if (!tempss.getRange().canMove()){
        	if (!tempss.isSquadron()){
        		allCanRetreat = false;
        	}else{
        		if (tempss.getCarrierLocation() == null){
        			allCanRetreat = false;	
        		}
        	}
        }
      }
      return allCanRetreat;
    }

    public boolean checkNoneCanRetreat(){
        boolean noneCanRetreat = true;
        for (int i = 0; i < allss.size() ; i++){
          Spaceship tempss = allss.get(i);
          if (tempss.getRange().canMove()){
            noneCanRetreat = false;
          }
        }
        return noneCanRetreat;
      }

    public Player getPlayer(){
        return player;
    }

    public boolean isPlayer(Player aPlayer){
        return player == aPlayer;
    }

    public void addSpaceship(Spaceship ss){
      allss.add(ss);
    }

    /**
     * använder getStrength istället då det gäller att avgöra om en tf ska fly eller inte
     * @return
     */
    public int getTonnage(){
      int total = 0;
      for (int i = 0; i < allss.size(); i++){
        total = total + ((allss.get(i)).getTonnage());
      }
      return total;
    }

    /**
     * används då det gäller att avgöra om en tf ska fly eller inte
     * @return
     */
    public int getStrength(){
        int total = 0;
        for (int i = 0; i < allss.size(); i++){
        	Spaceship tmpss = allss.get(i);
        	total = total + (tmpss.getCurrentShields());
        	total = total + (tmpss.getCurrentDc()/2);
        	total = total + (tmpss.getActualDamage());          
        }
        return total;
      }

    public void restoreAllShields(){
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
        ss.restoreShields();
      }
      destroyedShips = new Vector<Spaceship>();
      retreatedShips = new Vector<Spaceship>();
    }

    public int getBombardment(){
        int totalBombardment = 0;
    	if (player.getGalaxy().getGameWorld().isCumulativeBombardment()){
            totalBombardment = getCumulativeBombardment();
    	}else{
    		totalBombardment = getMaxBombardment();
    	}
    	if (totalBombardment > 0){
    		// check if bombardment VIP exist in fleet
    		VIP bombVIP = g.findHighestVIPbombardmentBonus(allss);
    		if (bombVIP != null){
    			totalBombardment += bombVIP.getBombardmentBonus();
    		}
    	}
    	return totalBombardment;
    }

    private int getCumulativeBombardment(){
        int totalBombardment = 0;
        for (int i = 0; i < allss.size(); i++){
          Spaceship ss =allss.get(i);
          totalBombardment = totalBombardment + ss.getBombardment();
        }
        return totalBombardment;
    }

    private int getMaxBombardment(){
    	int maxBombardment = 0;
        for (int i = 0; i < allss.size(); i++){
            Spaceship ss =allss.get(i);
            if (ss.getBombardment() > maxBombardment){
            	maxBombardment = ss.getBombardment();
            }
        }
    	return maxBombardment;
    }

    public int getMaxPsychWarfare(){
    	int maxPsychWarfare = 0;
        for (int i = 0; i < allss.size(); i++){
            Spaceship ss =allss.get(i);
            if (ss.getPsychWarfare() > maxPsychWarfare){
            	maxPsychWarfare = ss.getPsychWarfare();
            }
        }
    	return maxPsychWarfare;
    }
/*
    public int getMaxSiegeBonus(){
    	int maxSiegeBonus = 0;
        for (int i = 0; i < allss.size(); i++){
            Spaceship ss =allss.get(i);
            if (ss.getSiegeBonus() > maxSiegeBonus){
            	maxSiegeBonus = ss.getSiegeBonus();
            }
        }
//        LoggingHandler.finer("taskforce getMaxSiegeBonus: " + maxSiegeBonus);
    	return maxSiegeBonus;
    }

    public boolean getTroops(){
      boolean tempTroops = false;
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
        if (ss.getTroops()){
          tempTroops = true;
        }
      }
      return tempTroops;
    }
*/
    
    public List<Spaceship> getSpaceships(){
    	return allss;
    }
    
    // snygga till denna genom att man ser antal på varje typ av skepp istället för att bara räkna upp alla skepp??
    // skicka in true om man vill ret. alla screenade skepp, annars (false) ret. alla icke.screenade skepp
    public String getOwnParticipatingShipsString(){
      String returnString = "";
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
        if (!returnString.equalsIgnoreCase("")){
          returnString = returnString + ", ";
        }
        returnString = returnString + ss.getName();
      }
      return returnString;
    }

    // snygga till denna genom att man ser antal på varje typ av skepp istället för att bara räkna upp alla skepp??
    // skicka in true om man vill ret. alla screenade skepp, annars (false) ret. alla icke.screenade skepp
    public String getOwnParticipatingShipsString(boolean screened){
      String returnString = "";
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
        if (ss.getScreened() == screened){
          if (!returnString.equalsIgnoreCase("")){
            returnString = returnString + ", ";
          }
          returnString = returnString + ss.getName();
        }
      }
      return returnString;
    }

    // snygga till denna genom att man ser antal på varje typ av skepp istället för att bara räkna upp alla skepp??
    public String getEnemyParticipatingShipsString(){
      String returnString = "";
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
        if (!returnString.equalsIgnoreCase("")){
          returnString = returnString + ", ";
        }
        returnString = returnString + ss.getSpaceshipType().getName();
      }
      return returnString;
    }

    // snygga till denna genom att man ser antal på varje typ av skepp istället för att bara räkna upp alla skepp??
    public String getEnemyParticipatingShipsString(boolean screened){
      String returnString = "";
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
        if (ss.getScreened() == screened){
          if (!returnString.equalsIgnoreCase("")){
            returnString = returnString + ", ";
          }
          returnString = returnString + ss.getSpaceshipType().getName();
        }
      }
      return returnString;
    }

    public List<Spaceship> getRetreatedShips(){
      return retreatedShips;
    }

    public List<Spaceship> getDestroyedShips(){
      return destroyedShips;
    }

//    public void addToLatestBattleReport(String newReportString){
//      if (player != null){
//        player.getTurnInfo().addToLatestBattleReport(newReportString);
//      }
//    }

    public void addToLatestGeneralReport(String newReportString){
      if (player != null){
        player.getTurnInfo().addToLatestGeneralReport(newReportString);
      }
    }

    public void addToLatestLostInSpace(Spaceship aSS){
        if (player != null){
        	player.getTurnInfo().addToLatestShipsLostInSpace(aSS);
        }
    }

    public boolean stopsRetreats(){
      boolean hasInterdictor = false;
      int i = 0;
      while ((i < allss.size()) & (!hasInterdictor)){
        Spaceship ss =allss.get(i);
        if (ss.getSpaceshipType().getNoRetreat()){
          hasInterdictor = true;
        }else{
          i++;
        }
      }
      return hasInterdictor;
    }

    public int getTotalInitBonus(){
    	if (player != null){
    		Logger.finer("getTotalInitBonus " + player.getName() + ": " + getInitBonus() +" " + getVIPInitiativeBonus());
    	}else{
    		Logger.finer("getTotalInitBonus neutral: " + getInitBonus() +" " + getVIPInitiativeBonus());
    	}
    	return getInitBonus() + getVIPInitiativeBonus();
    }

    public int getTotalInitDefence(){
    	if (player != null){
    		Logger.finer("getTotalInitDefence " + player.getName() + ": " + getInitBonus() +" " + getVIPInitiativeBonus());
    	}else{
    		Logger.finer("getTotalInitDefence neutral: " + getInitBonus() + " " + getVIPInitiativeBonus());
    	}
    	return getInitDefence() + getVIPInitDefence();
    }

    public int getInitBonus(){
      int initBonus = 0;
      int initSupportBonus = 0;
      for (int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
        if (ss.getInitSupport()){
        	if (ss.getIncreaseInitiative() > initSupportBonus){
        		initSupportBonus = ss.getIncreaseInitiative();
        	}
        }else{
        	if (ss.getIncreaseInitiative() > initBonus){
        		initBonus = ss.getIncreaseInitiative();
        	}
        }
      }
      return initBonus + initSupportBonus;
    }

    public int getInitDefence(){
        int initDefence = 0;
        for (int i = 0; i < allss.size(); i++){
          Spaceship ss =allss.get(i);
          if (ss.getInitDefence() > initDefence){
          	initDefence = ss.getInitDefence();
          }
        }
        return initDefence;
    }

    public int getSpaceshipAimBonus(){
        int tmpAimBonus = 0;
        for (int i = 0; i < allss.size(); i++){
          Spaceship ss =allss.get(i);
          if (ss.getAimBonus() > tmpAimBonus){
        	  tmpAimBonus = ss.getAimBonus();
          }
        }
        return tmpAimBonus;
      }

    public int getVIPInitiativeBonus(){
      int initBonusCapitalShip = 0;
      for(int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
        if (!ss.isSquadron()){
        	int tmpInitBonusCapitalShip = g.findVIPhighestInitBonusCapitalShip(ss,player);
        	if (tmpInitBonusCapitalShip > initBonusCapitalShip){
        		initBonusCapitalShip = tmpInitBonusCapitalShip;
        	}
        }
      }
      int initBonusSquadron = 0;
      for(int i = 0; i < allss.size(); i++){
        Spaceship ss =allss.get(i);
//        LoggingHandler.finer(ss.getName(),g);
        if (ss.isSquadron()){
//        	LoggingHandler.finer("is squadron",g);
        	if ((getTotalNrShips(true) > 0) & (getTotalNrShips(false) > 0)){
//        		LoggingHandler.finer("screened ships exist!",g);
        		if (!ss.getScreened()){ // screened starfighters don't give bonuses for vips
        			int tmpInitBonusSquadron = g.findVIPhighestInitBonusSquadron(ss,player);
        			if (tmpInitBonusSquadron > initBonusSquadron){
        				initBonusSquadron = tmpInitBonusSquadron;
        			}
        		}        		
        	}else{
//        		LoggingHandler.finer("no screened ships",g);
        		// no screened ships, all ships may have a valid vip
        		int tmpInitBonusSquadron = g.findVIPhighestInitBonusSquadron(ss,player);
//        		LoggingHandler.finer(String.valueOf(tmpInitBonusSquadron),g);
        		if (tmpInitBonusSquadron > initBonusSquadron){
        			initBonusSquadron = tmpInitBonusSquadron;
        		}
        	}
        } 
      }
      Logger.finer("Taskforce.getVIPInitiativeBonus() returning: " + initBonusCapitalShip + " + " + initBonusSquadron);
      return initBonusCapitalShip + initBonusSquadron;
    }

    public int getVIPInitDefence(){
        int initDefence = 0;
        for(int i = 0; i < allss.size(); i++){
          Spaceship ss =allss.get(i);
          int tmpInitDefence = g.findVIPhighestInitDefence(ss,player);
          if (tmpInitDefence > initDefence){
          	initDefence = tmpInitDefence;
          }
        }
        return initDefence;
      }
/*
    public VIP getSiegeBonusVIP(){
      VIP highestSiegeVIP = null;
      for (int i = 0;i < allss.size();i++){
        Spaceship ss =allss.get(i);
      	VIP aVIP = g.findHighestVIPSiegeBonus(ss,player);
      	if (aVIP != null){
      		if (highestSiegeVIP == null){
      			highestSiegeVIP = aVIP;
      		}else
      		if (aVIP.getSiegeBonus() > highestSiegeVIP.getSiegeBonus()){
      			highestSiegeVIP = aVIP;
      		}
      	}
      }
      return highestSiegeVIP;
    }
*/
    public VIP getAimBonusVIP(){
        VIP highestAimVIP = null;
        for (int i = 0;i < allss.size();i++){
          Spaceship ss =allss.get(i);
          VIP aVIP = g.findHighestVIPAimBonus(ss,player);
          if (aVIP != null){
        	  if (highestAimVIP == null){
        		  highestAimVIP = aVIP;
        	  }else
        		  if (aVIP.getAimBonus() > highestAimVIP.getAimBonus()){
        			  highestAimVIP = aVIP;
        		  }
          }
        }
        return highestAimVIP;
      }

    public int getTotalCostSupply(){
    	int tmpSupply = 0;
    	for (Iterator<Spaceship> iter = allss.iterator(); iter.hasNext();) {
			Spaceship aShip = (Spaceship) iter.next();
			tmpSupply = tmpSupply + aShip.getUpkeep();
		}
    	return tmpSupply;
    }
    
    public int getTotalCostBuy(){
    	int tmpBuy = 0;
    	for (Iterator<Spaceship> iter = allss.iterator(); iter.hasNext();) {
			Spaceship aShip = (Spaceship) iter.next();
			tmpBuy = tmpBuy + aShip.getSpaceshipType().getBuildCost(null);
		}
    	return tmpBuy;
    }
    
    /**
     * Returns true if at least one ship in this fleet can besiege
     * @return true if at least one ship in this fleet can besiege
     */
    public boolean canBesiege(){
    	boolean canBesiege = false;
    	int counter = 0;
    	while ((counter < allss.size()) & (!canBesiege)){
    		Spaceship ss = allss.get(counter);
    		if (ss.isCanBlockPlanet() & !ss.isDestroyed()){
    			canBesiege = true;
    		}else{
    			counter++;
    		}
    	}
    	return canBesiege;
    }
    
    public boolean isAlien(){
    	return player.isAlien();
    }
    
    public void removeDestroyedShips(){
    	if (allss != null){
    		List<Spaceship> removeShips = new LinkedList<Spaceship>();
    		for (Iterator<Spaceship> iter = allss.iterator(); iter.hasNext();) {
    			Spaceship aShip = (Spaceship) iter.next();
    			if (aShip.isDestroyed()){
    				destroyedShips.add(aShip);
    				removeShips.add(aShip);
    			}
    		}
    		for (Spaceship spaceship : removeShips) {
				allss.remove(spaceship);
			}
    	}
    }
    
    public void incomingCannonFire(Planet aPlanet, Building aBuilding){   	
    	if (allss != null){
    		List<Spaceship> shipsPossibleToHit = new LinkedList<Spaceship>();
    		for (Iterator<Spaceship> iter = allss.iterator(); iter.hasNext();) {
    			Spaceship aShip = (Spaceship) iter.next();
    			if (aShip.isCapitalShip()){
    				shipsPossibleToHit.add(aShip);
    			}
    		}
    		Logger.finer("shipsPossibleToHit.size(): " + shipsPossibleToHit.size());
    		int randomIndex = Functions.getRandomInt(0, shipsPossibleToHit.size()-1);
    		Spaceship shipToBeHit = shipsPossibleToHit.get(randomIndex);
    	
		    int nr = 0;
		    nr = allss.indexOf(shipToBeHit);
		    // perform shot
		    int multiplier = getMultiplier(0);
		    Logger.finest("multiplier: " + multiplier);
		      
		    // randomize damage
	  		int damageNoArmor = (int) Math.round(aBuilding.getBuildingType().getCannonDamage() * (multiplier / 10.0));
	  		if (damageNoArmor < 1) {
	  			damageNoArmor = 1;
	  		}
		  	// Use 	totalDamage to show the damage after armor.
		  	int totalDamage = shipToBeHit.getCurrentShields();
		    int damageLeftAfterShields = shipToBeHit.shipShieldsHit(damageNoArmor);
		    double afterShieldsDamageRatio = (damageLeftAfterShields*1.0d) / damageNoArmor;
		    Logger.finer("afterShieldsDamageRatio: " + afterShieldsDamageRatio);
		    //  gör en sådan funktion och plocka ut skadan.  är bara small skada som kanonen gör.
		    int actualDamage = getActualDamage(shipToBeHit,multiplier,afterShieldsDamageRatio,aBuilding);
		    // Anväda denna funktion. o skicka i skadan. damageLeftAfterShields är skadan kvar efter att skölden har tagit första smällen. Är alltså 0 om skölde klarade av hela skadan. om damageLeftAfterShields är = 0 så skall damageNoArmor dras av skölden. annars stts skölden till 0 och actualDamage dras av hullet.
		    String damagedStatus = shipToBeHit.shipHit(actualDamage,damageLeftAfterShields,damageNoArmor);
		    totalDamage += actualDamage;
		    Logger.finer("multiplier=" + multiplier + " damageNoArmor=" + damageNoArmor + " damageLeftAfterShields=" + damageLeftAfterShields + " afterShieldsDamageRatio=" + afterShieldsDamageRatio + " actualDamage=" + actualDamage + " damagedStatus=" + damagedStatus);
		    if (shipToBeHit.isDestroyed()){
		    	getPlayer().addToGeneral("Your ship " + shipToBeHit.getName() + " on " + aPlanet.getName() + " was destroyed when hit (" + damageNoArmor + ") by an enemy " + aBuilding.getBuildingType().getName() + ".");
		    	//		    	 Är detta rätt? ser skumt ut
		    	addToLatestLostInSpace(shipToBeHit);
		    	if(aPlanet.getPlayerInControl() != null){
			        aPlanet.getPlayerInControl().addToGeneral("Your " + aBuilding.getBuildingType().getName() + " at " + aPlanet.getName() + "hit (" + damageNoArmor + ") and destroyed an enemy " + shipToBeHit.getSpaceshipType().getName()+ ".");
			        // Är detta rätt? ser skumt ut
			        aPlanet.getPlayerInControl().getTurnInfo().addToLatestShipsLostInSpace(shipToBeHit);
		    	}
		        // check for destroyed squadrons in the carrier hit
		        List<Spaceship> squadronsDestroyed = new LinkedList<Spaceship>();
		        for (Spaceship aShip : allss) {
		        	Logger.finer("sqd loc: " + aShip.getCarrierLocation());
					if (aShip.isSquadron() & (aShip.getCarrierLocation() == shipToBeHit)){ // squadron in a destroyed carrier
						squadronsDestroyed.add(aShip);
					}
				}
		        // remove ship
		        g.removeShip(shipToBeHit);
		        allss.remove(nr);
		        destroyedShips.add(shipToBeHit);
		        for (Spaceship aSquadron : squadronsDestroyed) {
		        	Logger.finer("sqd destroyed!");
			        g.removeShip(aSquadron);
			        allss.remove(aSquadron);
			        destroyedShips.add(aSquadron);
			    	getPlayer().addToGeneral("Your squadron " + aSquadron.getName() + " carried inside " + shipToBeHit.getName() + " was also destroyed when " + shipToBeHit.getName() + " was lost.");
			    	addToLatestLostInSpace(aSquadron);
			    	// squadrons destroyed are not added to planets owners lostInSpace or addToGeneral, he don't know if a carrier carried any squadrons
				}
		        Logger.finer("allss.size(): " + allss.size());
		        // is all ships in the tf destroyed?
		        if (allss.size() == 0){
		        	isDestroyed = true;
		        }
		    }else{
		    	getPlayer().addToGeneral("Your ship " + shipToBeHit.getName() + " on " + aPlanet.getName() + " was hit by an enemy " + aBuilding.getBuildingType().getName() + " and the damage (" + damageNoArmor + ") " + damagedStatus + ".");
		    	if(aPlanet.getPlayerInControl() != null){
		    		aPlanet.getPlayerInControl().addToGeneral("Your " + aBuilding.getBuildingType().getName() + " at " + aPlanet.getName() + " hit an enemy " + shipToBeHit.getSpaceshipType().getName() + " and the damage (" + damageNoArmor + ") " + damagedStatus + ".");
		    	}
		   	}
    	}    	
    }
    
    public int getActualDamage(Spaceship targetShip, int multiplier, double shieldsMultiplier, Building aBuilding) {
		double tmpDamage = 0;
		
		tmpDamage = aBuilding.getBuildingType().getCannonDamage() * (1.0 - targetShip.getArmorSmall());
			
		Logger.finer( "Damage before shieldsmodifier: " + tmpDamage);
		tmpDamage = tmpDamage * shieldsMultiplier;
		Logger.finer( "Damage after shieldsmodifier: " + tmpDamage);
//		double baseDamage = tmpDamage * ((targetShip.getCurrentDc() * 1.0) / targetShip.getDamageCapacity());
		double baseDamage = tmpDamage; // Paul: tar bort raden ovan, gör att skepp tar mindre skada om de är skadade
		Logger.finer( "Damage after hull damage effect: " + baseDamage);
		// randomize damage
		int actualDamage = (int) Math.round(baseDamage * (multiplier / 10.0));
		Logger.finest("Damage after multiplier: " + actualDamage + " ship hit: " + targetShip.getName() + " firing Building (cannon): " + aBuilding.getBuildingType().getName());
		if (actualDamage < 1) {
			actualDamage = 1;
		}
		return actualDamage;
	}
    
    public int getTroopCapacity(){
    	int capacity= 0;
    	for (Spaceship ship : allss) {
    		capacity += ship.getTroopCapacity();
		}
    	return capacity;
    }
}
