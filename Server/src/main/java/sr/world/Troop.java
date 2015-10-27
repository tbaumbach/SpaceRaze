package sr.world;

import java.io.Serializable;

import sr.enums.BattleGroupPosition;
import sr.enums.TroopTargetingType;
import sr.general.logging.Logger;

public class Troop implements Serializable, CanBeLostInSpace, Cloneable{
	private static final long serialVersionUID = 1L;
	private TroopType troopType;
	private String uniqueName;
	private String uniqueShortName;
	private int currentDC,maxDC;
	private BattleGroupPosition position;
	private Player owner = null;
	private Planet planetLocation;
	private int lastPlanetMoveTurn;
	private Spaceship shipLocation;
	private int kills;
	private int uniqueId;
	private int techWhenBuilt; // needed for land battle sim
	// attack values
	private int attackInfantry;
	private int attackArmored;
	private int attackArtillery;
	// used when moving
	private Planet oldPlanetLocation;
	private Spaceship oldCarrierLocation;

	public Troop(TroopType aTroopType, int aNrProduced, int aTotalTechBonus, int anUniqueId ){
		this.troopType = aTroopType;
		this.uniqueName = troopType.getUniqueName() + " - " + aNrProduced;
		this.uniqueShortName = troopType.getUniqueShortName() + " - " + aNrProduced;
		this.uniqueId = anUniqueId;
		setData(aTotalTechBonus);	
	}
	
	public Troop clone(){
		Troop aTroop = null;
		try {
			aTroop = (Troop)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return aTroop;
	}
	
	private void setData(int aTotalTechBonus) {
		techWhenBuilt = aTotalTechBonus;
		int tech = 100 + aTotalTechBonus;
		double techBonus = tech / 100.0;

		attackArmored = (int) Math.round(troopType.getAttackArmored() * techBonus);
		attackArtillery = (int) Math.round(troopType.getAttackArtillery() * techBonus);
		//Logger.finer("******** attackArtillery: " + attackArtillery + " " + troopType.getAttackArtillery());
		attackInfantry = (int) Math.round(troopType.getAttackInfantry() * techBonus);

		maxDC = (int) Math.round(troopType.getDamageCapacity() * techBonus);;
		currentDC = maxDC;
		
		position = troopType.getDefaultPosition();
	}
	
	@Override
	public String toString(){
		return "Troop: " + uniqueShortName + " (" + troopType + ")";
	}

	/**
	 * Get a value (higher is better) of how suitable this troop is as a victim of
	 * anAttackers attack
	 * @param anAttacker
	 * @return
	 */
	public double getSuitableWeight(Troop anAttacker){
		double weight = 0;
		if (isArmor()){
			weight = anAttacker.getAttackArmored();
		}else{
			weight = anAttacker.getAttackInfantry();
		}
		if (anAttacker.isArmor()){
			weight -= getAttackArmored();
		}else{
			weight -= getAttackInfantry();
		}
		return weight;
	}	

	public int getCurrentDC() {
		return currentDC;
	}
	
	public void setCurrentDC(int currentDC) {
		this.currentDC = currentDC;
	}
	
	public int getKills() {
		return kills;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public int getMaxDC() {
		return maxDC;
	}
	
	public void setMaxDC(int maxDC) {
		this.maxDC = maxDC;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public Planet getPlanetLocation() {
		return planetLocation;
	}
	
	public String getLocationString(){
		String retStr = "";
		if (planetLocation != null){
			retStr = planetLocation.getName();
		}else{
			retStr = shipLocation.getName();
		}
		return retStr;
	}

	/**
	 * Check if a Troop is at a planet, either on the planet itself or on a ship at the planet
	 * @param aPlanet
	 * @return true if troop is on planet or on ship at planet
	 */
	public boolean isAtPlanet(Planet aPlanet){
		boolean atPlanet = false;
		if (planetLocation != null){
			if (planetLocation == aPlanet){
				atPlanet = true;
			}
		}else{
			if (shipLocation.getLocation() == aPlanet){
				atPlanet = true;
			}
		}
		return atPlanet;
	}

	public void setPlanetLocation(Planet planetLocation) {
		this.planetLocation = planetLocation;
	}
	
	public BattleGroupPosition getPosition() {
		return position;
	}
	/* not in use. Allways the default value
	public void setPosition(BattleGroupPosition position) {
		this.position = position;
	}
	*/
	public Spaceship getShipLocation() {
		return shipLocation;
	}
	/*
	public void setShipLocation(Spaceship shipLocation) {
		this.shipLocation = shipLocation;
	}*/
	
	public TroopType getTroopType() {
		return troopType;
	}
	
	public void setTroopType(TroopType troopType) {
		this.troopType = troopType;
	}

	public int getTechWhenBuilt() {
		return techWhenBuilt;
	}

	public void setTechWhenBuilt(int techWhenBuilt) {
		this.techWhenBuilt = techWhenBuilt;
	}

	public int getAttackArmored() {
		double temp = attackArmored * ((kills + 10.0) / 10.0);
		return (int)Math.round(temp);
	}

	public void setAttackArmored(int attackArmored) {
		this.attackArmored = attackArmored;
	}

	public int getAttackArtillery() {
		double temp = attackArtillery * ((kills + 10.0) / 10.0);
		return (int)Math.round(temp);
	}

	public void setAttackArtillery(int attackArtillery) {
		this.attackArtillery = attackArtillery;
	}

	public int getAttackInfantry() {
		double temp = attackInfantry * ((kills + 10.0) / 10.0);
		return (int)Math.round(temp);
	}

	public void setAttackInfantry(int attackInfantry) {
		this.attackInfantry = attackInfantry;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getUniqueShortName() {
		return uniqueShortName;
	}

	public void setUniqueShortName(String uniqueShortName) {
		this.uniqueShortName = uniqueShortName;
	}

	public boolean isArmor(){
		return troopType.isArmor();
	}
	
	public void performRepairs(double amountOfRepair) {
		currentDC = (int)Math.round(currentDC + (int)Math.round(amountOfRepair*maxDC));
		if (currentDC > maxDC){
			currentDC = maxDC;
		}
		if (owner != null) {
			if (currentDC == maxDC){
				owner.addToGeneral("Your troop " + uniqueName + " at "	+ getLocationString() + " has been repaired " + (int)Math.round(amountOfRepair*100) + "% to full damage capacity.");
			}else{
				owner.addToGeneral("Your troop " + uniqueName + " at "	+ getLocationString() + " has been repaired " + (int)Math.round(amountOfRepair*100) + "% to " + (int)Math.round((currentDC * 100)/maxDC)  + "% of full damage capacity.");
			}
		}
	}

	public boolean isDamaged(){
		return currentDC < maxDC;
	}
	
	public boolean isSpaceshipTravel(){
		return troopType.isSpaceshipTravel();
	}
	
	public int getId() {
		return uniqueId;
	}

	public void move(Spaceship destinationCarrier, TurnInfo ti) {
		String oldLocString = null;
		if (planetLocation == null) { // old location is a carrier
			oldCarrierLocation = shipLocation;
			oldLocString = oldCarrierLocation.getName();
		} else { // old location is a planet
			oldPlanetLocation = planetLocation;
			oldLocString = oldPlanetLocation.getName();
			planetLocation = null;
			Logger.finer("New planet location = null!");
		}
		// ; behövs denna, eller en motsvarighet till det?
		shipLocation = destinationCarrier;
		ti.addToLatestGeneralReport(uniqueName + " has moved from " + oldLocString + " to " + shipLocation.getName() + ".");
	}

	public void move(Planet destination, TurnInfo ti){
    	// move ship from carrier
		if(shipLocation == null || destination == null){
			Logger.severe("Error: shipLocation= " + shipLocation + " destination= " + destination);
		}else{
			oldCarrierLocation = shipLocation;
			shipLocation = null;
			planetLocation = destination;
			ti.addToLatestGeneralReport(uniqueName + " has moved from " + oldCarrierLocation.getName() + " to " + planetLocation.getName() + ".");
		}
	}	

	public void setDestroyed() {
		currentDC = 0;
	}
	
	public int getUpkeep(){
		return troopType.getUpkeep();
	}

	public String getLostInSpaceString() {
		return troopType.getUniqueName();
	}
	
	public String hit(int damage, boolean artillery, boolean defending, int resistance){
		Logger.finer("hit: damage=" + damage + " art=" + artillery + " def=" + defending + " res=" + resistance);
		String returnString = "";
		double remainingDamage = damage; 
		Logger.finer("art remainingDamage: " + remainingDamage);
		// add resistance effect
		if (defending){
			remainingDamage = remainingDamage * ((20.0 - resistance) / 20.0);
		}
		Logger.finer("res remainingDamage: " + remainingDamage);
		int actualDamage = (int)Math.round(remainingDamage);
		if (actualDamage < 1){
			actualDamage = 1;
		}
		Logger.finer("actualDamage: " + actualDamage);
		if (actualDamage >= currentDC){
			currentDC = 0;
			if (owner != null){
				owner.getGalaxy().checkVIPsInDestroyedTroop(this);
			}
			returnString = "Troop destroyed";
		}else{
			Logger.finer("currentDC before: " + currentDC);
			currentDC = currentDC - actualDamage;
			Logger.finer("currentDC after: " + currentDC);
			int troopStrength = getTroopStrength();
			Logger.finer("troopStrength: " + troopStrength);
			returnString = "Troop damaged, strength: " + String.valueOf(troopStrength) + "%";
		}
		return returnString;
	}
	
	/**
	 * Returns a percentage of remaning DC
	 * @return a percentage of remaning DC
	 */
	public int getTroopStrength(){
//		System.out.println(currentDC + " " + maxDC);
		return (int) Math.round((100.0 * currentDC) / maxDC);
	}
	
	public int getFiringBackPenalty() {
		return troopType.getFiringBackPenalty();
	}
/*
	public boolean isAttackScreened(){
		return troopType.isAttackScreened();
	}
*/
	public int getLastPlanetMoveTurn() {
		return lastPlanetMoveTurn;
	}

	public void setLastPlanetMoveTurn(int lastPlanetMoveTurn) {
		this.lastPlanetMoveTurn = lastPlanetMoveTurn;
	}
	
	public int getActualDamage(boolean armoredTarget, int multiplier, boolean defender, int resistance, int vipBonus){
		int baseDamage = 0;
		if (armoredTarget){
			baseDamage = getAttackArmored();
		}else{
			baseDamage = getAttackInfantry();
		}
		Logger.finer("baseDamage: " + baseDamage + " Mult=" + multiplier);
		return getModifiedActualDamage(baseDamage, multiplier, defender, resistance, vipBonus);
	}

	public int getArtilleryActualDamage(int multiplier, boolean defender, int resistance, int vipBonus){
		int baseDamage = getAttackArtillery();
		Logger.finer("getAttackArtillery(): " + baseDamage);
		return getModifiedActualDamage(baseDamage, multiplier, defender, resistance, vipBonus);
	}
	
	private int getModifiedActualDamage(int baseDamage, int multiplier, boolean defender, int resistance, int vipBonus){
		double tmpDamage = baseDamage * ((100.0 + vipBonus) / 100.0);
		Logger.finer("vipBonus: " + tmpDamage);
		// add resistance effect
		Logger.finer("resistance: " + resistance);
		if (defender){
			tmpDamage = tmpDamage * ((resistance + 20.0) / 20.0);
		}//else{
			//tmpDamage = tmpDamage * ((20.0 - resistance) / 20.0);
		//}
		Logger.finer("defender=" + defender + ":" + tmpDamage);
		// add damage mod
		tmpDamage = tmpDamage * ((getCurrentDC() * 1.0) / getMaxDC());
		Logger.finer("damaged: " + tmpDamage);
		// randomize damage
		int actualDamage = (int) Math.round(tmpDamage * (multiplier / 10.0));
		if (actualDamage < 0) {
			actualDamage = 1; // en attack måste alltid göra minst 1 i skada.
		}
		Logger.finer("multiplied: " + actualDamage);
		return actualDamage;
	}
	
	public boolean isDestroyed(){
		return currentDC <= 0;
	}
	
	public void addKill(){
		kills++;
	}
	
	public void addToLatestShipsLostInSpace(Spaceship ss) {
		if (owner != null) {
			owner.getTurnInfo().addToLatestShipsLostInSpace(ss);
		}
	}

	public void addToLatestTroopsLostInSpace(Troop aTroop) {
		if (owner != null) {
			owner.getTurnInfo().addToLatestTroopsLostInSpace(aTroop);
		}
	}

	public String getBattleSimAbilities(){
		StringBuffer sb = new StringBuffer();
		if (kills > 0){
			sb.append("k:");
			sb.append(String.valueOf(kills));
		}
		if (techWhenBuilt > 0){
			if (sb.length() > 0){
				sb.append(",");
			}
			sb.append("t:");
			sb.append(String.valueOf(techWhenBuilt));
		}
		if (currentDC < maxDC){
			if (sb.length() > 0){
				sb.append(",");
			}
			sb.append("d:");
			double tmpDc1 = 1 - ((currentDC*1.0)/maxDC);
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
	
	public TroopTargetingType getTargetingType(){
		return troopType.getTargetingType();
	}

}
