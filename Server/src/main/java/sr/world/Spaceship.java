package sr.world;

import java.io.Serializable;

import sr.enums.SpaceshipRange;
import sr.enums.SpaceshipTargetingType;
import sr.general.logging.Logger;

public class Spaceship implements Serializable, Comparable<Spaceship>, ShortNameable, CanBeLostInSpace, Cloneable {
	static final long serialVersionUID = 1L;
	private SpaceshipType sst;
	private Player owner;
	private Planet location, oldLocation, runningTo;
	private Spaceship carrierLocation, oldCarrierLocation;
	private Planet runningFrom; // original planet where the ship was forced to
								// start running
	// private String statusx = "ok"; // används endast av runAway()
	private String name, uniqueName, uniqueShortName;
	private int shields, damagecapacity, currentdc, currentshields, uniqueId, kills;
//	private boolean movedThisTurnx = false; // används för att kolla om ett
											// retirerande skepp skall återvända
											// till sin oldLocation eller hitta
											// en ny planet att fly till
	private boolean screened = false;
	private boolean retreating = false;
	private int weaponsStrengthSmall, weaponsStrengthMedium, weaponsStrengthLarge, weaponsStrengthHuge, weaponsStrengthSquadron;
    private int weaponsAirToGround;
	private int weaponsSalvoesMedium, weaponsSalvoesLarge, weaponsSalvoesHuge; // if Integer.MAX then treat as infinite
	private double armorSmall, armorMedium, armorLarge, armorHuge;
	private int techWhenBuilt;

	// construktorn skall ej anropas direkt, utan spaceshiptype.getShip skall
	// användas istället
	public Spaceship(SpaceshipType sst, String name, int nrProduced,
			int uniqueIdCounter, VIP vipWithBonus, int factionTechBonus,
			int buildingBonus) {
		this.sst = sst;
		uniqueName = sst.getName() + " - " + nrProduced;
		uniqueShortName = sst.getShortName() + " - " + nrProduced;
		this.uniqueId = uniqueIdCounter;
		kills = 0;
		if (name != null) {
			this.name = name;
		} else {
			this.name = uniqueName;
		}
		this.screened = sst.isScreened();
		setData(vipWithBonus, factionTechBonus, buildingBonus);
	}

	private void setData(VIP vipWithBonus, int factionTechBonus, int buildingBonus) {
		int tech = 100 + factionTechBonus;
		tech = tech + buildingBonus;
		if (vipWithBonus != null) {
			tech = tech + vipWithBonus.getTechBonus();
//			LoggingHandler.fine( this, null, "setData","vipWithBonus.getTechBonus(): " + vipWithBonus.getTechBonus());
		}
		techWhenBuilt = tech - 100;
		double techBonus = tech / 100.0;
		// int tonnage = sst.getTonnage();
//		LoggingHandler.fine( this, null, "setData", techBonus + "");
		// weapons =
		// (int)Math.round(powTonnage(tonnage,2.2)*sst.getWeapons()/sst.getTotal()/10);
		weaponsStrengthSmall = (int) Math.round(sst.getWeaponsStrengthSmall() * techBonus);
		weaponsStrengthMedium = (int) Math.round(sst.getWeaponsStrengthMedium()	* techBonus);
		weaponsStrengthLarge = (int) Math.round(sst.getWeaponsStrengthLarge() * techBonus);
		weaponsStrengthHuge = (int) Math.round(sst.getWeaponsStrengthHuge()	* techBonus);
		weaponsStrengthSquadron = (int) Math.round(sst.getWeaponsStrengthSquadron() * techBonus);

//		LoggingHandler.fine( this, null, "setData", "weaponsStrengthSmall: " + weaponsStrengthSmall);

		weaponsSalvoesMedium = sst.getWeaponsMaxSalvoesMedium();
		weaponsSalvoesLarge = sst.getWeaponsMaxSalvoesLarge();
		weaponsSalvoesHuge = sst.getWeaponsMaxSalvoesHuge();

		shields = (int) Math.round(sst.getShields() * techBonus);

		currentshields = shields;
		Logger.finest( "currentshields & shields: " + currentshields + " " + shields);
		damagecapacity = sst.getHits();

		currentdc = damagecapacity;

		armorSmall = sst.getArmorSmall() / 100.0d;
		armorMedium = sst.getArmorMedium() / 100.0d;
		armorLarge = sst.getArmorLarge() / 100.0d;
		armorHuge = sst.getArmorHuge() / 100.0d;
	}

	public int getSize() {
		int sizeValue = 1;
		if ((sst.getTonnage() > 300) & (sst.getTonnage() <= 600)) {
			sizeValue = 2;
		} else if ((sst.getTonnage() > 600) & (sst.getTonnage() <= 900)) {
			sizeValue = 3;
		} else if (sst.getTonnage() > 900) {
			sizeValue = 4;
		}
		return sizeValue;
	}

	public void moveShip(String inPlanet, TurnInfo ti){
		Planet destination = owner.getGalaxy().getPlanet(inPlanet);
    	if (owner != null){
    		Logger.finer("Called, spaceship: " + toString() + " destination: " + destination + " retreating: " + retreating);
    	}else{
    		Logger.finer("moveShip called, spaceship: " + toString() + " destination: " + destination + " retreating: " + retreating);
    	}
        if (retreating){ // retreating ship given new move order
          if (owner != null){
        	  Logger.finer("retreating == true");
          }else{
        	  Logger.finer("owner == null, retreating == true");
          }
          // if destination is a planet belonging to the player and not under
		  // siege
          if ((destination.getPlayerInControl() != null) && (destination.getPlayerInControl() == owner) && !destination.isBesieged()){
            location = destination;
            ti.addToLatestGeneralReport("Your ship " + name + " has arrived to " + runningTo.getName() + " after retreating from " + runningFrom.getName() + ".");
            runningTo = null;
            runningFrom = null;
            oldLocation = runningFrom; // is this one needed?
            retreating = false;
          }else{ // ship continues to retreat
          	// find a planet to run to
            location = destination;
          	runningTo = getRunToPlanet();
          	if (runningTo == null){ // there is no planet to retreat to
                ti.addToLatestGeneralReport("Your ship " + name + " has been scuttled by it's crew because they had nowhere to retreat to.");
      			if (owner != null) {
      				owner.getGalaxy().checkVIPsInDestroyedShips(this, owner);
      				owner.getGalaxy().checkTroopsInDestroyedShips(this, owner);
					owner.getTurnInfo().addToLatestShipsLostInSpace(this);
      			}
                owner.getGalaxy().removeShip(this);
          	}else{ // there is a planet to retreat to
          		// running from is unchanged
          		// retreating is still true
                oldLocation = location;
          		location = null; // a retreating ship has no location
                ti.addToLatestGeneralReport("Your ship " + name + " is retreating and just left " + oldLocation.getName() + ".");
          	}
          }
        }else{
        	if (carrierLocation != null){ // move ship from carrier
        		oldCarrierLocation = carrierLocation;
        		carrierLocation = null;
        		location = destination;
        		ti.addToLatestGeneralReport(name + " has moved from " + oldCarrierLocation.getName() + " to " + location.getName() + ".");
        	}else{ // move ship from another planet
        		oldLocation = location;
        		location = destination;
        		ti.addToLatestGeneralReport(name + " has moved from " + oldLocation.getName() + " to " + location.getName() + ".");
        	}
        }
	}	
	
	public void moveShip(Spaceship destinationCarrier, TurnInfo ti) {
		String oldLocString = null;
		if (location == null) { // old location is a carrier
			oldCarrierLocation = carrierLocation;
			oldLocString = oldCarrierLocation.getName();
		} else { // old location is a planet
			oldLocation = location;
			oldLocString = oldLocation.getName();
		}
		// ; behövs denna, eller en motsvarighet till det?
		carrierLocation = destinationCarrier;
		ti.addToLatestGeneralReport(name + " has moved from " + oldLocString
				+ " to " + carrierLocation.getName() + ".");
	}

	public void moveRetreatingSquadron(TurnInfo ti) {
		// The only ships without range who can be retreating are squadrons
		// in a retreating carrier, and they move to where the carrier has
		// moved. And is destroyed if the cartrier is destroyed.

		// First check if carrier is destroyed
		if (carrierLocation.isDestroyed()) {
			ti.addToLatestGeneralReport("Your squadron "
							+ name
							+ " has been destroyed when it's retreating carrier "
							+ carrierLocation.getName()
							+ " was scuttled by it's crew.");
  			if (owner != null) {
  				owner.getGalaxy().checkVIPsInDestroyedShips(this, owner);
  				owner.getGalaxy().checkTroopsInDestroyedShips(this, owner);
				owner.getTurnInfo().addToLatestShipsLostInSpace(this);
  			}
			owner.getGalaxy().removeShip(this);
		} else if (carrierLocation.isRetreating()) { // carrier is still
														// retreating
			// carrier is still in retreat
			// location is still null
			// retreat is still true
			// runningFrom is unchanged
			oldLocation = getCarrierLocation().getOldLocation();
			ti.addToLatestGeneralReport("Your squadron " + name
					+ " is retreating with it's carrier "
					+ getCarrierLocation().getName() + " and just left "
					+ oldLocation.getName() + ".");
		} else {
			// carrier is no longer retreating
			// location = destination;
			location = getCarrierLocation().getLocation();
			ti.addToLatestGeneralReport("Your squadron " + name
					+ " has arrived to " + location.getName()
					+ " after retreating with "
					+ getCarrierLocation().getName() + ".");
			runningTo = null;
			runningFrom = null;
			oldLocation = runningFrom; // is this one needed?
			retreating = false;
		}
	}

	/*
	 * public void clearRetreatPlanets(){ runningFrom = null; runningTo = null; }
	 */

	// return part of shot that will penetrate the shields
	public int shipShieldsHit(int rawDamage) {
		int penetrating = 0;
		if (currentshields < rawDamage) {
			penetrating = rawDamage - currentshields;
		}
		Logger.finer( "rawDamage: " + rawDamage + " penetrating: " + penetrating);
		return penetrating;
	}

	// returnera sträng om skottets effekt, förstört, skadat, togs upp av
	// sköldarna
	public String shipHit(int damage, int damageAfterShields, int damageNoArmor) {
		String returnString = "";
		Logger.finer( "shipHit currentshields "
				+ currentshields + " damage " + damage + " currentdc "
				+ currentdc + " damageAfterShields: " + damageAfterShields
				+ " damageNoArmor: " + damageNoArmor);
		// if (currentshields > damage){
		if (damageAfterShields == 0) { // all damage was absorbed by the shields
			currentshields = currentshields - damageNoArmor;
			int shieldStrength = (int) Math.round((100.0 * currentshields) / killsFactor(shields));
			returnString = "was absorbed by the shields (shield strength: " + String.valueOf(shieldStrength) + "%).";
		} else {
			// damage = damage - currentshields;
			currentshields = 0;
			if (currentdc > damage) {
				currentdc = currentdc - damage;
				int hullStrength = (int) Math.round((100.0 * currentdc)	/ damagecapacity);
				returnString = "damaged the ship (hull strength:" + String.valueOf(hullStrength) + "%).";
			} else {
				currentdc = 0;
				if (owner != null) {
					owner.getGalaxy().checkVIPsInDestroyedShips(this, owner);
					owner.getGalaxy().checkTroopsInDestroyedShips(this, owner);
				}
			}
		}
		return returnString;
	}

	public String hitByAntiAir(int damage){
		String returnString = "";
		if (damage >= currentshields){
			// shields depleted
			int tmpDamage = damage - currentshields;
			currentshields = 0;
			if (tmpDamage > 0){
				currentdc -= tmpDamage;
				if (currentdc <= 0){
					currentdc = 0;
					returnString = "Ship destroyed by anti-air fire.";
				}else{
					int hullStrength = (int) Math.round((100.0 * currentdc)	/ damagecapacity);
					returnString = "Anti-Air damaged the ship (hull strength:" + String.valueOf(hullStrength) + "%).";
				}
			}else{
				returnString = "Anti-Air was absorbed by the shields (shield strength: 0%).";
			}
		}else{
			currentshields = currentshields - damage;
			int shieldStrength = (int) Math.round((100.0 * currentshields) / killsFactor(shields));
			returnString = "Anti-Air was absorbed by the shields (shield strength: " + String.valueOf(shieldStrength) + "%).";
		}
		return returnString;
	}
	
	/**
	 * @return a percentage of remaning DC
	 */
	public int getHullStrength(){
		int hullStrength = (int) Math.round((100.0 * currentdc)	/ damagecapacity);
		return hullStrength;
	}

	/**
	 * @return a percentage of remaning shields
	 */
	public int getShieldStrength(){
		int shieldStrength = (int) Math.round((100.0 * currentshields) / killsFactor(shields));
		return shieldStrength;
	}

	// Check if ship gets away, and set new destination if it does
	// returnera true om flykten lyckades
	// returnera false om skeppet förstörs
	public boolean retreat() {
		Logger.finer( "retreat() called");
		boolean gotAway = true;
		runningTo = getRunToPlanet();
		Logger.finer( "runningTo: " + runningTo);
		if (runningTo == null) {
			Logger.finer( "runningTo == null");
			oldLocation = location;
			gotAway = false;
		} else {
			Logger.finer( "runningTo != null: " + runningTo.getName());
			oldLocation = location;
			runningFrom = location;
			location = null;
			retreating = true;
			restoreShields();
		}
		Logger.finer( "return: " + gotAway);
		return gotAway;
	}

	public void squadronInRetreatingCarrier() {
		Logger.finer( "squadronInRetreatingCarrier() called");
		oldLocation = location;
		runningFrom = location;
		location = null;
		retreating = true;
		restoreShields();
	}

	// leta reda på planet att fly till
	private Planet getRunToPlanet() {
		return owner.getGalaxy().getRunToPlanet(this);
	}

	public boolean isDestroyed() {
		return currentdc == 0;
	}

	public void clearRunningFrom() {
		runningFrom = null;
	}

	public void setLocation(Planet newLocation) {
		location = newLocation;
	}

	public int getUpkeep() {
		return sst.getUpkeep();
	}

	public int getBombardment() {
		return sst.getBombardment();
	}
/*
	public boolean getTroops() {
		return sst.getTroops();
	}
*/
	public String getName() {
		return name;
	}

	public String getShortName() {
		return uniqueShortName;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public Planet getLocation() {
		return location;
	}

	public int getTonnage() {
		return sst.getTonnage();
	}

	/*
	 * public int getWeapons(){ return weapons; }
	 */

	public int getMediumSalvoe(boolean fireASalvoe) {
		int salvoeStrength = 0;
		if (weaponsStrengthMedium > 0) {
			if (weaponsSalvoesMedium > 0) {
				salvoeStrength = getWeaponsStrengthMedium();
				if (fireASalvoe) {
					weaponsSalvoesMedium--;
				}
			}
		}
		return salvoeStrength;
	}

	public int getLargeSalvoe(boolean fireASalvoe) {
		int salvoeStrength = 0;
		if (weaponsStrengthLarge > 0) {
			if (weaponsSalvoesLarge > 0) {
				salvoeStrength = getWeaponsStrengthLarge();
				if (fireASalvoe) {
					weaponsSalvoesLarge--;
				}
			}
		}
		return salvoeStrength;
	}

	public int getHugeSalvoe(boolean fireASalvoe) {
		int salvoeStrength = 0;
		if (weaponsStrengthHuge > 0) {
			if (weaponsSalvoesHuge > 0) {
				salvoeStrength = getWeaponsStrengthHuge();
				if (fireASalvoe) {
					weaponsSalvoesHuge--;
				}
			}
		}
		return salvoeStrength;
	}

	public int getWeaponsStrengthHuge() {
		return killsFactor(weaponsStrengthHuge);
	}

	public int getWeaponsStrengthLarge() {
		return killsFactor(weaponsStrengthLarge);
	}

	public int getWeaponsStrengthMedium() {
		return killsFactor(weaponsStrengthMedium);
	}

	public int getWeaponsStrengthSmall() {
		return killsFactor(weaponsStrengthSmall);
	}
	
	/**
	 * Used to add experience (kills) to certain values (weapons, shields...)
	 * @param aValue
	 * @return
	 */
	public int killsFactor(int aValue){
		double temp = aValue * (((kills/sst.getSlots())+10.0) / 10.0);
		return (int)Math.round(temp);
	}

	/**
	 * Return a sum of all current weapon strengths, counting with
	 * the following factors:
	 * -no # salvoes left
	 * -kills
	 * -damage to the ship
	 * @return the computed strength of the ship
	 */
	public int getActualDamage() {
		int totalDamage = getWeaponsStrengthSmall();
		totalDamage = totalDamage + getMediumSalvoe(false);
		totalDamage = totalDamage + getLargeSalvoe(false);
		totalDamage = totalDamage + getHugeSalvoe(false);
		totalDamage = totalDamage + getWeaponsStrengthSquadron();
		totalDamage = (int) Math.round(totalDamage * ((currentdc * 1.0) / damagecapacity));
		if (totalDamage < 1){
			totalDamage = 1;
		}
		return totalDamage;
	}

	public int getDamageNoArmor(Spaceship ss, int multiplier) {
		double tmpDamage = 0;
		if (ss.isSquadron()) {
			tmpDamage = getWeaponsStrengthSquadron();
		} else {
			tmpDamage = getWeaponsStrengthSmall();
			if (ss.getSize() >= 2) {
				tmpDamage = tmpDamage + getMediumSalvoe(false);
			}
			if (ss.getSize() >= 3) {
				tmpDamage = tmpDamage + getLargeSalvoe(false);
			}
			if (ss.getSize() == 4) {
				tmpDamage = tmpDamage + getHugeSalvoe(false);
			}
		}
		double baseDamage = tmpDamage * ((currentdc * 1.0) / damagecapacity);
		// randomize damage
		int actualDamage = (int) Math.round(baseDamage * (multiplier / 10.0));
		if (actualDamage < 1) {
			actualDamage = 1;
		}
		Logger.finest("actualDamage=" + actualDamage + " baseDamage=" + baseDamage + " ship hit: " + ss.getName() + " firing ship: " + name);
		return actualDamage;
	}

	public boolean isSquadron() {
		return sst.isSquadron();
	}

	public int getActualDamage(Spaceship targetShip, int multiplier, double shieldsMultiplier) {
		double tmpDamage = 0;
		if (targetShip.isSquadron()) {
			tmpDamage = getWeaponsStrengthSquadron() * (1.0 - targetShip.getArmorSmall());
		} else {
			tmpDamage = getWeaponsStrengthSmall() * (1.0 - targetShip.getArmorSmall());
			if (targetShip.getSize() >= 2) {
				tmpDamage = tmpDamage + getMediumSalvoe(true) * (1.0 - targetShip.getArmorMedium());
			}
			if (targetShip.getSize() >= 3) {
				tmpDamage = tmpDamage + getLargeSalvoe(true) * (1.0 - targetShip.getArmorLarge());
			}
			if (targetShip.getSize() == 4) {
				tmpDamage = tmpDamage + getHugeSalvoe(true) * (1.0 - targetShip.getArmorHuge());
			}
		}
		Logger.finer( "Damage before shieldsmodifier: " + tmpDamage);
		tmpDamage = tmpDamage * shieldsMultiplier;
		Logger.finer( "Damage after shieldsmodifier: " + tmpDamage);
		double baseDamage = tmpDamage * ((currentdc * 1.0) / damagecapacity);
		Logger.finer( "Damage after hull damage effect: " + baseDamage);
		// randomize damage
		int actualDamage = (int) Math.round(baseDamage * (multiplier / 10.0));
		Logger.finest("Damage after multiplier: " + actualDamage + " ship hit: " + targetShip.getName() + " firing ship: " + name);
		if (actualDamage < 1) {
			actualDamage = 1;
		}
		return actualDamage;
	}
/*
	public int getAirToGroundActualDamage(int multiplier, int resistance) {
		int tmpDamage = sst.getWeaponsAirToGround();
		// add kills factor
		double baseDamage = tmpDamage * ((kills + 10.0) / 10.0);
		// add resistance effect
		baseDamage = baseDamage * ((20.0 - resistance) / 20.0);
		// add damage mod
		baseDamage = baseDamage * ((currentdc * 1.0) / damagecapacity);
		// randomize damage
		int actualDamage = (int) Math.round(baseDamage * (multiplier / 10.0));
		if (actualDamage < 0) {
			actualDamage = 0; // ok with 0 in damage
		}
		return actualDamage;
	}
*/
	public int getShields() {
		return killsFactor(shields);
	}

	public int getDamageCapacity() {
		return damagecapacity;
	}

	public String getTypeName() { // hette innan getType
		return sst.getName();
	}

	public SpaceshipRange getRange() {
		SpaceshipRange range = sst.getRange();
		if (owner != null){ // only need to check presense of vip if not neutral
			if (range == SpaceshipRange.SHORT) {
				boolean ftlMasterOnShip = owner.getGalaxy().isFTLMasterOnShip(this);
				if (ftlMasterOnShip) {
					range = SpaceshipRange.LONG;
				}
			}
		}
		return range;
	}

	public String getRangeString() {
		return sst.getRangeString();
	}

	public int getCurrentDc() {
		return currentdc;
	}
	
	public int setCurrentDc(int inCurrentdc) {
		return currentdc = inCurrentdc;
	}

	public int getCurrentShields() {
		return currentshields;
	}

	public void restoreShields() {
		currentshields = getShields();
	}

	public void setOwner(Player newOwner) {
		this.owner = newOwner;
	}

	public Player getOwner() {
		return owner;
	}

	/*
	 * public String getStatus(){ return status; }
	 * 
	 * public void setStatus(String newStatus){ status = newStatus; }
	 * 
	 * public void removeMovedThisTurn(){ movedThisTurn = false; }
	 * 
	 * public boolean getMovedThisTurn(){ return movedThisTurn; }
	 * 
	 * public void setMovedThisTurn(boolean newValue){ movedThisTurn = newValue; }
	 */
	public int getId() {
		return uniqueId;
	}

	public SpaceshipType getSpaceshipType() {
		return sst;
	}

	public void addKill() {
		kills++;
	}

	public void setKills(int newKills) {
		kills = newKills;
	}

	public int getKills() {
		return kills;
	}

	public Planet getOldLocation() {
		return oldLocation;
	}

//	public void addToLatestBattleReport(String battleReport) {
//		if (owner != null) {
//			owner.getTurnInfo().addToLatestBattleReport(battleReport);
//		}
//	}

	public void addToLatestShipsLostInSpace(Spaceship ss) {
		if (owner != null) {
//			Logger.fine(ss.getUniqueName());
//			Thread.dumpStack();
			owner.getTurnInfo().addToLatestShipsLostInSpace(ss);
		}
	}

	public void addToLatestTroopsLostInSpace(Troop aTroop) {
		if (owner != null) {
			owner.getTurnInfo().addToLatestTroopsLostInSpace(aTroop);
		}
	}

	public Planet getRetreatingTo() {
		return runningTo;
	}

	public Planet getRetreatingFrom() {
		return runningFrom;
	}

	public void setOldLocation(Planet newOldLocation) {
		oldLocation = newOldLocation;
	}

	public String getNoRetreatString() {
		return sst.getNoRetreatString();
	}

	public String getSizeString() {
		return sst.getSizeString();
	}

	public int getSlots() {
		return sst.getSlots();
	}

	public void performRepairs() {
		currentdc = damagecapacity;
		if (owner != null) {
			owner.addToGeneral("Your ship " + name + " at "	+ location.getName() + " has been repaired up to full damage capacity.");
		}
	}

	public int getIncreaseInitiative() {
		int tmpInitBouns = 0;
		if (getInitSupport()) {
			tmpInitBouns = sst.getInitSupportBonus();
		} else {
			tmpInitBouns = sst.getInitiativeBonus();
		}
		return tmpInitBouns;
	}

	public int getInitDefence() {
		return sst.getInitDefence();
	}

	public boolean getScreened() {
		return screened;
	}

	public void setScreened(boolean newValue) {
		screened = newValue;
	}

	public int compareTo(Spaceship ss) {
		return getTonnage() - ss.getTonnage();
	}

	public boolean getInitSupport() {
		return sst.getInitSupport();
	}

	public boolean isRetreating() {
		return retreating;
	}

	public int getWeaponsSalvoesHuge() {
		return weaponsSalvoesHuge;
	}

	public int getWeaponsSalvoesLarge() {
		return weaponsSalvoesLarge;
	}

	public int getWeaponsSalvoesMedium() {
		return weaponsSalvoesMedium;
	}

	public int getWeaponsStrengthSquadron() {
		return killsFactor(weaponsStrengthSquadron);
	}

	public int getMaxWeaponsSalvoesHuge() {
		return sst.getWeaponsMaxSalvoesHuge();
	}

	public int getMaxWeaponsSalvoesLarge() {
		return sst.getWeaponsMaxSalvoesLarge();
	}

	public int getMaxWeaponsSalvoesMedium() {
		return sst.getWeaponsMaxSalvoesMedium();
	}

	public void supplyWeapons(int size) {
		if (getMaxWeaponsSalvoesMedium() < Integer.MAX_VALUE) {
			if (size >= 2) {
				weaponsSalvoesMedium = getMaxWeaponsSalvoesMedium();
			}
		}
		if (getMaxWeaponsSalvoesLarge() < Integer.MAX_VALUE) {
			if (size >= 3) {
				weaponsSalvoesLarge = getMaxWeaponsSalvoesLarge();
			}
		}
		if (getMaxWeaponsSalvoesHuge() < Integer.MAX_VALUE) {
			if (size > 3) {
				weaponsSalvoesHuge = getMaxWeaponsSalvoesHuge();
			}
		}
	}

	public boolean getNeedResupply() {
		boolean needSupplies = false;
		if (weaponsSalvoesMedium < getMaxWeaponsSalvoesMedium()) {
			needSupplies = true;
		}
		if (weaponsSalvoesLarge < getMaxWeaponsSalvoesLarge()) {
			needSupplies = true;
		}
		if (weaponsSalvoesHuge < getMaxWeaponsSalvoesHuge()) {
			needSupplies = true;
		}
		return needSupplies;
	}

	public int getMaxResupply() {
		return sst.getMaxResupply();
	}

	public String getMaxResupplyString() {
		return sst.getMaxResupplyString();
	}

	public double getArmorHuge() {
		return armorHuge;
	}

	public double getArmorLarge() {
		return armorLarge;
	}

	public double getArmorMedium() {
		return armorMedium;
	}

	public double getArmorSmall() {
		return armorSmall;
	}

	public boolean isPlanetarySurvey() {
		return sst.isPlanetarySurvey();
	}

	public int getPsychWarfare() {
//		LoggingHandler.finer( "getSiegeBonus: " + name + " - " + sst.getSiegeBonus());
		return sst.getPsychWarfare();
	}

	public boolean isCarrier() {
		return getSquadronCapacity() > 0;
	}

	public boolean isTroopCarrier() {
		return getTroopCapacity() > 0;
	}

	public Spaceship getCarrierLocation() {
		return carrierLocation;
	}

	public void setCarrierLocation(Spaceship carrier) {
		this.carrierLocation = carrier;
	}

	public int getSquadronCapacity() {
		return sst.getSquadronCapacity();
	}

	public void updateSquadronLocation() {
		if (isSquadron()) {
			if (carrierLocation != null) {
				location = carrierLocation.getLocation();
			}
		}
	}

	public SpaceshipTargetingType getTargetingType() {
		return sst.getTargetingType();
	}

	public void setDestroyed() {
		currentdc = 0;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ShortName: " + uniqueShortName);
		sb.append(" Location: " + location);
		sb.append(" Carrier location: " + carrierLocation);
		sb.append(" Owner: " + owner);
		return sb.toString();
	}

	public int getIncEnemyClosedBonus() {
		return sst.getIncEnemyClosedBonus();
	}

	public int getIncEnemyOpenBonus() {
		return sst.getIncEnemyOpenBonus();
	}

	public int getIncFrendlyClosedBonus() {
		return sst.getIncFrendlyClosedBonus();
	}

	public int getIncFrendlyOpenBonus() {
		return sst.getIncFrendlyOpenBonus();
	}

	public int getIncNeutralClosedBonus() {
		return sst.getIncNeutralClosedBonus();
	}

	public int getIncNeutralOpenBonus() {
		return sst.getIncNeutralOpenBonus();
	}

	public int getIncOwnClosedBonus() {
		return sst.getIncOwnClosedBonus();
	}

	public int getIncOwnOpenBonus() {
		return sst.getIncOwnOpenBonus();
	}

	public boolean isCanAttackScreenedShips() {
		return sst.isCanAttackScreenedShips();
	}

	public boolean isCanMove() {
		return getRange() != SpaceshipRange.NONE;
	}

	public boolean isCanBlockPlanet() {
		return sst.isCanBlockPlanet();
	}
	
	public boolean isCivilian() {
		return sst.isCivilian();
	}

	public boolean isLookAsCivilian() {
		return sst.isLookAsCivilian();
	}

	public boolean isVisibleOnMap() {
		return sst.isVisibleOnMap();
	}

	/**
	 * A capital ship is not civilian, not a squadron and can move
	 * @return
	 */
	public boolean isCapitalShip(){
		return !sst.isCivilian() & !sst.isSquadron() & (sst.getRange() != SpaceshipRange.NONE);
	}
	
	/**
	 * A defender ship is not civilian, not a squadron and can't move
	 * @return
	 */
	public boolean isDefender(){
		return !sst.isCivilian() & !sst.isSquadron() & (sst.getRange() == SpaceshipRange.NONE);
	}
	
	/**
	 * Used by BattleSim for client
	 * @param damagePercent
	 */
	public void setDamage(int damagePercent){
		currentdc = (int) Math.round(currentdc * ((100.0-damagePercent)/100.0));
	}
	
	public String getBattleSimAbilities(){
		StringBuffer sb = new StringBuffer();
		if (kills > 0){
			sb.append("k:");
			sb.append(String.valueOf(kills));
		}
		if (screened){
			if (sb.length() > 0){
				sb.append(",");
			}
			sb.append("s");
		}
		if (techWhenBuilt > 0){
			if (sb.length() > 0){
				sb.append(",");
			}
			sb.append("t:");
			sb.append(String.valueOf(techWhenBuilt));
		}
		if (currentdc < damagecapacity){
			if (sb.length() > 0){
				sb.append(",");
			}
			sb.append("d:");
			double tmpDc1 = 1 - ((currentdc*1.0)/damagecapacity);
			int tmpDc2 = (int)Math.round(tmpDc1*100);
			if (tmpDc2 > 99){
				tmpDc2 = 99;
			}else
			if (tmpDc2 < 1){
				tmpDc2 = 1;
			}
			sb.append(String.valueOf(tmpDc2));
		}
		return sb.toString();
	}
	
	/**
	 * return total percentage of dc and armor
	 * Example 1: if a ship have 100% dc and shields, it returns 200
	 * Example 2: if a ship have 50% left ofshields and 75% left of dc it returns 125
	 * @return added percentage of dc and shields
	 */
	public int getDamageLevel(){
		double curDcLevel = currentdc*1.0/damagecapacity;
		double curShieldsLevel = currentshields*1.0/shields;
		int curDcPercentage = (int)Math.round(curDcLevel*100);
		int curShieldsPercentage = (int)Math.round(curShieldsLevel*100);
		int damageLevel = curDcPercentage + curShieldsPercentage; 
		Logger.finest( "getDamageLevel: " + damageLevel);
		return damageLevel;
	}
	
	public boolean isDamaged(){
		boolean damaged = getDamageLevel() < 200;
		Logger.finest( "isDamaged: " + damaged);
		return damaged;
	}
	
	public int getAimBonus() {
		return sst.getAimBonus();
	}
	
	public int getTroopCapacity(){
		return sst.getTroopCapacity();
	}
	
	public int getTroopLaunchCapacity(){
		return sst.getTroopLaunchCapacity();
	}

	public String getLostInSpaceString() {
		return sst.getName();
	}

	public int getWeaponsAirToGround() {
		return weaponsAirToGround;
	}
	
	@Override
	public Spaceship clone(){
		Spaceship clonedShip = null;
		try {
			clonedShip = (Spaceship)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clonedShip;
	}

	public int getTechWhenBuilt() {
		return techWhenBuilt;
	}

	public SpaceshipType getType() {
		return sst;
	}
	
	public boolean isAlwaysRetreat(){
		return sst.isAlwaysRetreat();
	}
	
    public boolean getNoRetreat(){
        return sst.getNoRetreat();
    }
    
    public boolean isSmuggler(){
    	return sst.isSmuggler();
    }

    public boolean isTrader(){
    	return sst.isTrader();
    }
    
    /**
     * @return true if small capital psych warfare ship
     */
    public boolean isSmallPsychWarfareShip(){
    	return sst.isSmallPsychWarfareShiptype();
    }

}
