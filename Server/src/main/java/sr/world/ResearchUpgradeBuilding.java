package sr.world;

import java.io.Serializable;

import sr.general.Functions;

@SuppressWarnings("unused")
public class ResearchUpgradeBuilding implements Serializable{
	static final long serialVersionUID = 1L;
	private String name;
	
	private int openPlanetBonus = 0,closedPlanetBonus = 0;
	private int techBonus = 0;
	private int wharfSize = 0; // if = cannot build ships
	private int troopSize = 0; // if = cannot build troops
	private int buildCost = 0;
	private boolean changeSpaceport;
	private boolean spaceport;
	private boolean ChangeVisibleOnMap;
	private boolean visibleOnMap;
	private int resistanceBonus = 0, shieldCapacity = 0, cannonDamage, cannonRateOfFire;
	//private int shipTechBonus = 0; // %  on ships bild on planet
	
	// Vänta med dessa tills grunden är klar
	
	private boolean ailienkiller;
	private int counterEspionage = 0;
	private int exterminator = 0;
	
	private int troopHitBonus = 0; // % on troops bild on planet
	private int troopAttackBonus = 0; // % % on troops bild on planet
	private int troopDefanceBonus = 0; // % % on troops bild on planet
	private int troopAirBonus = 0; // % % on troops bild on planet
	private int troopSuportBonus = 0; // % % on troops bild on planet
	private int defenceBonus = 0; // %
	private int airDefanceBonus = 0; // %
	private int suportDefanceBonus = 0; // %
	
	// (Tobbe) Dessa användes inte. Samma egenskaper som VIPar har och skall kanske användas i framtiden. Skall vara i % form.
	private int shipBuildBonus; // decreases build cost of ships
	private int troopBuildBonus; // decreases build cost of troops
	private int vipBuildBonus; // decreases build cost of VIPs
	private int buildingBuildBonus; // decreases build cost of buildings
	

	public String doResearch(BuildingType aBuilding){
		  String text;
		  text = "\nThe building type " + getName() + " has been upgraded";
	    	
		  if(buildCost > 0){
			  aBuilding.setBuildCost(aBuilding.getBuildCost(null) + buildCost);
	    	}
		  
		  	if(wharfSize > 0){
		  		aBuilding.setWharfSize(wharfSize);
	    	}
		  	if(troopSize > 0){
		  		aBuilding.setTroopSize(troopSize);
	    	}
	    	if(exterminator > 0){
	    		aBuilding.setExterminator(aBuilding.getExterminator() + exterminator);
	    	}
	    	if(resistanceBonus > 0){
	    		aBuilding.setResistanceBonus(aBuilding.getResistanceBonus() + resistanceBonus);
	    	}
	    	if(shieldCapacity > 0){
	    		aBuilding.setShieldCapacity(aBuilding.getShieldCapacity() + shieldCapacity);
	    	}
	    	if(cannonDamage > 0){
	    		aBuilding.setCannonDamage(aBuilding.getCannonDamage() + cannonDamage);
	    	}
	    	if(cannonRateOfFire > 0){
	    		aBuilding.setCannonRateOfFire(aBuilding.getCannonRateOfFire() + cannonRateOfFire);
	    	}
	    	/*if(shipBuildBonus > 0){
	    		aBuilding.setShipBuildBonus(aBuilding.getShipBuildBonus() + shipBuildBonus);
	    	}
	    	if(troopBuildBonus > 0){
	    		aBuilding.setTroopBuildBonus(aBuilding.getTroopBuildBonus() + troopBuildBonus);
	    	}
	    	if(vipBuildBonus > 0){
	    		aBuilding.setVipBuildBonus(aBuilding.getVipBuildBonus() + vipBuildBonus);
	    	}
	    	if(buildingBuildBonus > 0){
	    		aBuilding.setBuildingBuildBonus(aBuilding.getBuildingBuildBonus() + buildingBuildBonus);
	    	}*/
	    	if(techBonus > 0){
	    		aBuilding.setTechBonus(aBuilding.getTechBonus() + techBonus);
	    	}
	    	if(openPlanetBonus > 0){
	    		aBuilding.setOpenPlanetBonus(aBuilding.getOpenPlanetBonus() + openPlanetBonus);
	    	}
	    	if(closedPlanetBonus > 0){
	    		aBuilding.setClosedPlanetBonus(aBuilding.getClosedPlanetBonus() + closedPlanetBonus);
	    	}
	    	if(changeSpaceport){
	    		aBuilding.setSpaceport(spaceport);
	    	}
	    	if(ChangeVisibleOnMap){
	    		aBuilding.setVisibleOnMap(visibleOnMap);
	    	}
	    	/*
	    	if(aimBonus > 0){
	    		aBuilding.setAimBonus(aBuilding.getAimBonus() + aimBonus);
	    	}
	    	if(troopAttacksBonus > 0){
	    		aBuilding.setTroopAttacksBonus(aBuilding.getTroopAttacksBonus() + troopAttacksBonus);
	    	}
	    	if(landBattleGroupAttacksBonus > 0){
	    		aBuilding.setLandBattleGroupAttacksBonus(aBuilding.getLandBattleGroupAttacksBonus() + landBattleGroupAttacksBonus);
	    	}*/
	    	
	    	return text;
	    }
	
	public String getResearchText(){
    	String text;
    	
    	text= "Change properties for the Building type: " + name;
    	
    	if(buildCost > 0){
    		text+="\n-Build cost: " + addPlus(buildCost);
    	}
    	if(wharfSize > 0){
    		text+="\n-Wharf size: " + wharfSize;
	  	}
	  	if(troopSize > 0){
	  		text+="\n-Troop size: " + troopSize;
	  	}
    	if(counterEspionage > 0){
    		text+="\n-CounterEspionage: " + addPlus(counterEspionage) + "%";
    	}
    	if(exterminator > 0){
    		text+="\n-Exterminator: " + addPlus(exterminator) + "%";
    	}
    	if(shieldCapacity > 0){
    		text+="\n-Shield Capacity: " + addPlus(shieldCapacity);
    	}
    	if(cannonDamage > 0){
    		text+="\n-Cannon Damage: " + addPlus(cannonDamage);
    	}
    	if(cannonRateOfFire > 0){
    		text+="\n-Cannon Rate Of Fire: " + addPlus(cannonRateOfFire);
    	}
    	if(resistanceBonus > 0){
    		text+="\n-Resistance Bonus: " + addPlus(resistanceBonus);
    	}
    	if(techBonus > 0){
    		text+="\n-Tech Bonus: " + addPlus(techBonus) + "%";
    	}
    	if(openPlanetBonus > 0){
    		text+="\n-Open Planet Income Bonus: " + addPlus(openPlanetBonus);
    	}
    	if(closedPlanetBonus > 0){
    		text+="\n-Closed Planet Income Bonus: " + addPlus(closedPlanetBonus);
    	}
    	if(ChangeVisibleOnMap){
    		text+="\n-Visible On Map: " + Functions.getYesNo(visibleOnMap);
    	}
    	if(changeSpaceport){
    		text+="\n-Spaceport: " + Functions.getYesNo(spaceport);
    	}
    	/*
    	if(aimBonus > 0){
    		text+="\n-Aim Bonus: " + addPlus(aimBonus);
    	}
    	if(troopAttacksBonus > 0){
    		text+="\n-Troop Attacks Bonus: " + addPlus(troopAttacksBonus);
    	}
    	if(landBattleGroupAttacksBonus > 0){
    		text+="\n-Land Battle Group Attacks Bonus: " + addPlus(landBattleGroupAttacksBonus) + "%";
    	}*/
    	
    	return text;
    }
	
	private String addPlus(int number){
    	StringBuffer retVal = new StringBuffer();
    	if(number > 0){
    		retVal.append("+");
    	}
    	retVal.append(number);
    	return retVal.toString();
    }

	public int getBuildCost() {
		return buildCost;
	}

	public void setBuildCost(int buildCost) {
		this.buildCost = buildCost;
	}

	public int getCannonDamage() {
		return cannonDamage;
	}

	public void setCannonDamage(int canonDamage) {
		this.cannonDamage = canonDamage;
	}

	public int getCannonRateOfFire() {
		return cannonRateOfFire;
	}

	public void setCannonRateOfFire(int cannonRateOfFire) {
		this.cannonRateOfFire = cannonRateOfFire;
	}

	public int getClosedPlanetBonus() {
		return closedPlanetBonus;
	}

	public void setClosedPlanetBonus(int closedPlanetBonus) {
		this.closedPlanetBonus = closedPlanetBonus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOpenPlanetBonus() {
		return openPlanetBonus;
	}

	public void setOpenPlanetBonus(int openPlanetBonus) {
		this.openPlanetBonus = openPlanetBonus;
	}

	public int getResistanceBonus() {
		return resistanceBonus;
	}

	public void setResistanceBonus(int resistanceBonus) {
		this.resistanceBonus = resistanceBonus;
	}

	public int getShieldCapacity() {
		return shieldCapacity;
	}

	public void setShieldCapacity(int shieldCapacity) {
		this.shieldCapacity = shieldCapacity;
	}

	public boolean isSpaceport() {
		return spaceport;
	}

	public void setSpaceport(boolean spaceport) {
		this.changeSpaceport = true;
		this.spaceport = spaceport;
	}

	public int getTechBonus() {
		return techBonus;
	}

	public void setTechBonus(int techBonus) {
		this.techBonus = techBonus;
	}

	public int getWharfSize() {
		return wharfSize;
	}

	public void setWharfSize(int wharfSize) {
		this.wharfSize = wharfSize;
	}

	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}

	public void setVisibleOnMap(boolean visibleOnMap) {
		this.ChangeVisibleOnMap = true;
		this.visibleOnMap = visibleOnMap;
	}

	public int getTroopSize() {
		return troopSize;
	}

	public void setTroopSize(int troopSize) {
		this.troopSize = troopSize;
	}
}
