package sr.world;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.enums.SpaceshipRange;

public class ResearchUpgradeShip implements Serializable{
	static final long serialVersionUID = 1L;
	
	String name;
    int shields,upkeep,buildCost,bombardment,increaseInitiative,initDefence;
//    int siegeBonus;
    private SpaceshipRange range;
    boolean changeTroops =  false,ChangeNoRetreat = false,changeInitSupport = false;
//    boolean troops = false;
    boolean noRetreat = false,initSupport = false;
    private int weaponsStrengthSmall,weaponsStrengthMedium,weaponsStrengthLarge,weaponsStrengthHuge;
    private int weaponsMaxSalvoesMedium,weaponsMaxSalvoesLarge,weaponsMaxSalvoesHuge; // if Integer.MAX then treat as infinite
    private int armorSmall,armorMedium,armorLarge,armorHuge;
    private int supply = 0; // 0 - 4 max size of ship that can be resupplied
    private int weaponsStrengthSquadron,squadronCapacity;
    private String description,history,shortDescription,advantages,disadvantages;
    private int incEnemyClosedBonus,incNeutralClosedBonus,incFrendlyClosedBonus,incOwnClosedBonus;
    private int incEnemyOpenBonus,incNeutralOpenBonus,incFrendlyOpenBonus,incOwnOpenBonus;
    private boolean changePlanetarySurvey,changeCanAttackScreenedShips;
    private boolean planetarySurvey,canAttackScreenedShips;
    private boolean changelookAsCivilian = false;
    private boolean lookAsCivilian = false;
    private boolean changeCanBlockPlanet = false;
    private boolean canBlockPlanet = true;
    private boolean changeVisibleOnMap = false;
    private boolean visibleOnMap = true;
    private int psychWarfare;
    private int troopCarrier;
    
    public ResearchUpgradeShip(String name){
    	this.name = name;
    }
    
    public String doResearch(SpaceshipType ship){
    	String text;
    	text = "\nThe ship model " + getName() + " have been upgrade";
    	
    	if(shields > 0){
    		ship.setShields(ship.getShields() + shields);
    	}
    	if(upkeep > 0){
    		ship.setUpkeep(ship.getUpkeep() + upkeep);
    	}
    	if(buildCost > 0){
    		ship.setBuildCost(ship.getBuildCost(null) + buildCost);
    	}
    	if(bombardment > 0){
    		ship.setBombardment(ship.getBombardment() + bombardment);
    	}
    	if(increaseInitiative > 0){
    		ship.setIncreaseInitiative(ship.getIncreaseInitiative() + increaseInitiative);
    	}
    	if(initDefence > 0){
    		ship.setInitDefence(ship.getInitDefence() + initDefence);
    	}
    	if(psychWarfare > 0){
    		ship.setPsychWarfare(ship.getPsychWarfare() + psychWarfare);
    	}
    	if(range != null){
    		ship.setSpaceshipRange(range);
    	}
/*    	if(changeTroops){
    		ship.setTroops(troops);
    	}*/
    	if(ChangeNoRetreat){
    		ship.setNoRetreat(noRetreat);
    	}
    	if(changeInitSupport){
    		ship.setInitSupport(initSupport);
    	}
    	if(weaponsStrengthSmall > 0){
    		ship.setWeaponsStrengthSmall(ship.getWeaponsStrengthSmall() + weaponsStrengthSmall);
    	}
    	if(weaponsStrengthMedium > 0){
    		ship.setWeaponsStrengthMedium(ship.getWeaponsStrengthMedium() + weaponsStrengthMedium);
    	}
    	if(weaponsStrengthLarge > 0){
    		ship.setWeaponsStrengthLarge(ship.getWeaponsStrengthLarge() + weaponsStrengthLarge);
    	}
    	if(weaponsStrengthHuge > 0){
    		ship.setWeaponsStrengthHuge(ship.getWeaponsStrengthHuge() + weaponsStrengthHuge);
    	}
    	if(weaponsMaxSalvoesMedium > 0){
    		ship.setWeaponsMaxSalvoesMedium(ship.getWeaponsMaxSalvoesMedium() + weaponsMaxSalvoesMedium);
    	}
    	if(weaponsMaxSalvoesLarge > 0){
    		ship.setWeaponsMaxSalvoesLarge(ship.getWeaponsMaxSalvoesLarge() + weaponsMaxSalvoesLarge);
    	}
    	if(weaponsMaxSalvoesHuge > 0){
    		ship.setWeaponsMaxSalvoesHuge(ship.getWeaponsMaxSalvoesHuge() + weaponsMaxSalvoesHuge);
    	}
    	if(armorSmall > 0){
    		ship.setArmorSmall(ship.getArmorSmall() + armorSmall);
    	}
    	if(armorMedium > 0){
    		ship.setArmorMedium(ship.getArmorMedium() + armorMedium);
    	}
    	if(armorLarge > 0){
    		ship.setArmorLarge(ship.getArmorLarge() + armorLarge);
    	}
    	if(armorHuge > 0){
    		ship.setArmorHuge(ship.getArmorHuge() + armorHuge);
    	}
    	if(supply > 0){
    		ship.setSupply(supply);
    	}
    	if(weaponsStrengthSquadron > 0){
    		ship.setWeaponsStrengthSquadron(ship.getWeaponsStrengthSquadron() + weaponsMaxSalvoesHuge);
    	}
    	if(squadronCapacity > 0){
    		ship.setSquadronCapacity(ship.getSquadronCapacity() + squadronCapacity);
    	}
    	if(incEnemyClosedBonus > 0){
    		ship.setIncEnemyClosedBonus(ship.getIncEnemyClosedBonus() + incEnemyClosedBonus);
    	}
    	if(incNeutralClosedBonus > 0){
    		ship.setIncNeutralClosedBonus(ship.getIncNeutralClosedBonus() + incNeutralClosedBonus);
    	}
    	if(incFrendlyClosedBonus > 0){
    		ship.setIncFrendlyClosedBonus(ship.getIncFrendlyClosedBonus() + incFrendlyClosedBonus);
    	}
    	if(incOwnClosedBonus > 0){
    		ship.setIncOwnClosedBonus(ship.getIncOwnClosedBonus() + incOwnClosedBonus);
    	}
    	if(incEnemyOpenBonus > 0){
    		ship.setIncEnemyOpenBonus(ship.getIncEnemyOpenBonus() + incEnemyOpenBonus);
    	}
    	if(incNeutralOpenBonus > 0){
    		ship.setIncNeutralOpenBonus(ship.getIncNeutralOpenBonus() + incNeutralOpenBonus);
    	}
    	if(incFrendlyOpenBonus > 0){
    		ship.setIncFrendlyOpenBonus(ship.getIncFrendlyOpenBonus() + incFrendlyOpenBonus);
    	}
    	if(incOwnOpenBonus > 0){
    		ship.setIncOwnOpenBonus(ship.getIncOwnOpenBonus() + incOwnOpenBonus);
    	}
    	if(description != null){
    		ship.setDescription(description);
    	}
    	if(history != null){
    		ship.setHistory(history);
    	}
    /*	if(shortDescription != null){
    		ship.setShortDescription(shortDescription);
    	}
    	if(advantages != null){
    		ship.setAdvantages(advantages);
    	}
    	if(disadvantages != null){
    		ship.setDisadvantages(disadvantages);
    	}*/
    	if(changePlanetarySurvey){
    		ship.setPlanetarySurvey(planetarySurvey);
    	}
    	if(changeCanAttackScreenedShips){
    		ship.setCanAttackScreenedShips(canAttackScreenedShips);
    	}
    	if(changelookAsCivilian){
    		ship.setLookAsCivilian(lookAsCivilian);
    	}
    	if(changeCanBlockPlanet){
    		ship.setCanBlockPlanet(canBlockPlanet);
    	}
    	if(changeVisibleOnMap){
    		ship.setVisibleOnMap(visibleOnMap);
    	}
    	if(troopCarrier > 0){
    		ship.setTroopCapacity(ship.getTroopCapacity() + troopCarrier);
    	}
    /*	if(troopDownload > 0){
    		ship.setTroopLaunchCapacity(ship.getTroopLaunchCapacity() +troopDownload);
    	}*/
    	
    	return text;
    }
    
    @JsonIgnore
    public String getResearchText(){
    	String text;
    	
    	text= "Change propertys for the ship model: " + name;
    	
    	if(shields > 0){
    		text+="\n-Shields: " + addplus(shields);
    	}
    	if(upkeep > 0){
    		text+="\n-Upkeep: " + addplus(upkeep);
    	}
    	if(buildCost > 0){
    		text+="\n-Build cost: " + addplus(buildCost);
    	}
    	if(bombardment > 0){
    		text+="\n-Bombardment: " + addplus(bombardment);
    	}
    	if(increaseInitiative > 0){
    		text+="\n-IncreaseInitiative: " + addplus(increaseInitiative);
    	}
    	if(initDefence > 0){
    		text+="\n-InitDefence: " + addplus(initDefence);
    	}
    	if(psychWarfare > 0){
    		text+="\n-psychWarfare: " + addplus(psychWarfare);
    	}
    	if(range != null){
    		text+="\n-Range: " + range.toString();
    	}
/*    	if(changeTroops){
    		text+="\n-Troops: " + addYesOrNo(troops);
    	}
*/    	if(ChangeNoRetreat){
    		text+="\n-No Retreat: " + addYesOrNo(noRetreat);
    	}
    	if(changeInitSupport){
    		text+="\n-InitSupport: " + addYesOrNo(initSupport);
    	}
    	if(weaponsStrengthSmall > 0){
    		text+="\n-Weapons strength small: " + addplus(weaponsStrengthSmall);
    	}
    	if(weaponsStrengthMedium > 0){
    		text+="\n-Weapons strength medium: " + addplus(weaponsStrengthMedium);
    	}
    	if(weaponsStrengthLarge > 0){
    		text+="\n-Weapons strength large: " + addplus(weaponsStrengthLarge);
    	}
    	if(weaponsStrengthHuge > 0){
    		text+="\n-Weapons strength huge: " + addplus(weaponsStrengthHuge);
    	}
    	if(armorSmall > 0){
    		text+="\n-Armor Small: " + addplus(armorSmall);
    	}
    	if(armorMedium > 0){
    		text+="\n-Armor Medium: " + addplus(armorMedium);
    	}
    	if(armorLarge > 0){
    		text+="\n-Armor Large: " + addplus(armorLarge);
    	}
    	if(armorHuge > 0){
    		text+="\n-Armor Huge: " + addplus(armorHuge);
    	}
    	if(supply > 0){
    		text+="\n-Supply ships size: " + getShipSize(supply);
    	}
    	if(weaponsStrengthSquadron > 0){
    		text+="\n-Weapons strength squadron: " + addplus(weaponsStrengthSquadron);
    	}
    	if(squadronCapacity > 0){
    		text+="\n-Squadron carrier capacity: " + addplus(squadronCapacity);
    	}
    	if(incEnemyClosedBonus > 0){
    		text+="\n-Incom enemy closed bonus: " + addplus(incEnemyClosedBonus);
    	}
    	if(incNeutralClosedBonus > 0){
    		text+="\n-Incom neutral closed bonus: " + addplus(incNeutralClosedBonus);
    	}
    	if(incFrendlyClosedBonus > 0){
    		text+="\n-Incom frendly closed bonus: " + addplus(incFrendlyClosedBonus);
    	}
    	if(incOwnClosedBonus > 0){
    		text+="\n-Incom own closed bonus: " + addplus(incOwnClosedBonus);
    	}
    	if(incEnemyOpenBonus > 0){
    		text+="\n-Incom enemy open bonus: " + addplus(incEnemyOpenBonus);
    	}
    	if(incNeutralOpenBonus > 0){
    		text+="\n-Incom neutral open bonus: " + addplus(incNeutralOpenBonus);
    	}
    	if(incFrendlyOpenBonus > 0){
    		text+="\n-Incom frendly open Bbnus: " + addplus(incFrendlyOpenBonus);
    	}
    	if(incOwnOpenBonus > 0){
    		text+="\n-Incom own open bonus: " + addplus(incOwnOpenBonus);
    	}
    	if(changePlanetarySurvey){
    		text+="\n-Planetary survey: " + addYesOrNo(planetarySurvey);
    	}
    	if(changeCanAttackScreenedShips){
    		text+="\n-Can attack screened ships: " + addYesOrNo(canAttackScreenedShips);
    	}
    	if(changelookAsCivilian){
    		text+="\n-Look as civilian: " + addYesOrNo(lookAsCivilian);
    	}
    	if(changeCanBlockPlanet){
    		text+="\n-Can block planet: " + addYesOrNo(canBlockPlanet);
    	}
    	if(changeVisibleOnMap){
    		text+="\n-Visible on nap: " + addYesOrNo(visibleOnMap);
    	}
    	if(troopCarrier > 0){
    		text+="\n-Troop Capacity: " + addplus(troopCarrier);
    	}
    /*	if(troopDownload > 0){
    		text+="\n-Troop download: " + addplus(troopDownload);
    	}*/
    	
    	
    	return text;
    }
    
    private String addplus(int number){
    	if(number > 0){
    		return "+" +number;
    	}
    	return new Integer(number).toString();
    }
    private String addYesOrNo(boolean test){
    	if(test){
    		return "Yes";
    	}
    	return "No";
    }
    
    private String getShipSize(int size){
    	if(size==1){
    		return "Samll";
    	}else if(size==2){
    		return "Medium";
    	}else if(size==3){
    		return "Large";
    	}else if(size==4){
    		return "Huge";
    	}
    	return "Non";
    }
    
	public String getAdvantages() {
		return advantages;
	}
	public void setAdvantages(String advantages) {
		this.advantages = advantages;
	}
	public int getArmorHuge() {
		return armorHuge;
	}
	public void setArmorHuge(int armorHuge) {
		this.armorHuge = armorHuge;
	}
	public int getArmorLarge() {
		return armorLarge;
	}
	public void setArmorLarge(int armorLarge) {
		this.armorLarge = armorLarge;
	}
	public int getArmorMedium() {
		return armorMedium;
	}
	public void setArmorMedium(int armorMedium) {
		this.armorMedium = armorMedium;
	}
	public int getArmorSmall() {
		return armorSmall;
	}
	public void setArmorSmall(int armorSmall) {
		this.armorSmall = armorSmall;
	}
	public int getBombardment() {
		return bombardment;
	}
	public void setBombardment(int bombardment) {
		this.bombardment = bombardment;
	}
	public int getBuildCost() {
		return buildCost;
	}
	public void setBuildCost(int buildCost) {
		this.buildCost = buildCost;
	}
	public boolean isCanAttackScreenedShips() {
		return canAttackScreenedShips;
	}
	public void setCanAttackScreenedShips(boolean canAttackScreenedShips) {
		this.changeCanAttackScreenedShips = true;
		this.canAttackScreenedShips = canAttackScreenedShips;
	}
	public boolean isCanBlockPlanet() {
		return canBlockPlanet;
	}
	public void setCanBlockPlanet(boolean canBlockPlanet) {
		this.changeCanBlockPlanet =  true;
		this.canBlockPlanet = canBlockPlanet;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisadvantages() {
		return disadvantages;
	}
	public void setDisadvantages(String disadvantages) {
		this.disadvantages = disadvantages;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public int getIncEnemyClosedBonus() {
		return incEnemyClosedBonus;
	}
	public void setIncEnemyClosedBonus(int incEnemyClosedBonus) {
		this.incEnemyClosedBonus = incEnemyClosedBonus;
	}
	public int getIncEnemyOpenBonus() {
		return incEnemyOpenBonus;
	}
	public void setIncEnemyOpenBonus(int incEnemyOpenBonus) {
		this.incEnemyOpenBonus = incEnemyOpenBonus;
	}
	public int getIncFrendlyClosedBonus() {
		return incFrendlyClosedBonus;
	}
	public void setIncFrendlyClosedBonus(int incFrendlyClosedBonus) {
		this.incFrendlyClosedBonus = incFrendlyClosedBonus;
	}
	public int getIncFrendlyOpenBonus() {
		return incFrendlyOpenBonus;
	}
	public void setIncFrendlyOpenBonus(int incFrendlyOpenBonus) {
		this.incFrendlyOpenBonus = incFrendlyOpenBonus;
	}
	public int getIncNeutralClosedBonus() {
		return incNeutralClosedBonus;
	}
	public void setIncNeutralClosedBonus(int incNeutralClosedBonus) {
		this.incNeutralClosedBonus = incNeutralClosedBonus;
	}
	public int getIncNeutralOpenBonus() {
		return incNeutralOpenBonus;
	}
	public void setIncNeutralOpenBonus(int incNeutralOpenBonus) {
		this.incNeutralOpenBonus = incNeutralOpenBonus;
	}
	public int getIncOwnClosedBonus() {
		return incOwnClosedBonus;
	}
	public void setIncOwnClosedBonus(int incOwnClosedBonus) {
		this.incOwnClosedBonus = incOwnClosedBonus;
	}
	public int getIncOwnOpenBonus() {
		return incOwnOpenBonus;
	}
	public void setIncOwnOpenBonus(int incOwnOpenBonus) {
		this.incOwnOpenBonus = incOwnOpenBonus;
	}
	public int getIncreaseInitiative() {
		return increaseInitiative;
	}
	public void setIncreaseInitiative(int increaseInitiative) {
		this.increaseInitiative = increaseInitiative;
	}
	public int getInitDefence() {
		return initDefence;
	}
	public void setInitDefence(int initDefence) {
		this.initDefence = initDefence;
	}
	public boolean isInitSupport() {
		return initSupport;
	}
	public void setInitSupport(boolean initSupport) {
		this.changeInitSupport = true;
		this.initSupport = initSupport;
	}
	public boolean isLookAsCivilian() {
		return lookAsCivilian;
	}
	public void setLookAsCivilian(boolean lookAsCivilian) {
		this.changelookAsCivilian = true;
		this.lookAsCivilian = lookAsCivilian;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isNoRetreat() {
		return noRetreat;
	}
	public void setNoRetreat(boolean noRetreat) {
		this.ChangeNoRetreat = true;
		this.noRetreat = noRetreat;
	}
	public boolean isPlanetarySurvey() {
		return planetarySurvey;
	}
	public void setPlanetarySurvey(boolean planetarySurvey) {
		this.changePlanetarySurvey =  true;
		this.planetarySurvey = planetarySurvey;
	}
	public SpaceshipRange getRange() {
		return range;
	}
	public void setRange(SpaceshipRange range) {
		this.range = range;
	}
	public int getShields() {
		return shields;
	}
	public void setShields(int shields) {
		this.shields = shields;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public int getPsychWarfare(){
		return psychWarfare;
	}
	public void setPsychWarfare(int newPsychWarfare) {
		this.psychWarfare = newPsychWarfare;
	}
/*	public int getSiegeBonus() {
		return siegeBonus;
	}
	public void setSiegeBonus(int siegeBonus) {
		this.siegeBonus = siegeBonus;
	}
*/	public int getSquadronCapacity() {
		return squadronCapacity;
	}
	public void setSquadronCapacity(int squadronCapacity) {
		this.squadronCapacity = squadronCapacity;
	}
	public int getSupply() {
		return supply;
	}
	public void setSupply(int supply) {
		this.supply = supply;
	}
	public int getUpkeep() {
		return upkeep;
	}
	public void setUpkeep(int upkeep) {
		this.upkeep = upkeep;
	}
/*	public boolean isTroops() {
		return troops;
	}
	public void setTroops(boolean troops) {
		this.changeTroops = true;
		this.troops = troops;
	}
*/	public int getWeaponsMaxSalvoesHuge() {
		return weaponsMaxSalvoesHuge;
	}
	public void setWeaponsMaxSalvoesHuge(int weaponsMaxSalvoesHuge) {
		this.weaponsMaxSalvoesHuge = weaponsMaxSalvoesHuge;
	}
	public int getWeaponsMaxSalvoesLarge() {
		return weaponsMaxSalvoesLarge;
	}
	public void setWeaponsMaxSalvoesLarge(int weaponsMaxSalvoesLarge) {
		this.weaponsMaxSalvoesLarge = weaponsMaxSalvoesLarge;
	}
	public int getWeaponsMaxSalvoesMedium() {
		return weaponsMaxSalvoesMedium;
	}
	public void setWeaponsMaxSalvoesMedium(int weaponsMaxSalvoesMedium) {
		this.weaponsMaxSalvoesMedium = weaponsMaxSalvoesMedium;
	}
	public int getWeaponsStrengthHuge() {
		return weaponsStrengthHuge;
	}
	public void setWeaponsStrengthHuge(int weaponsStrengthHuge) {
		this.weaponsStrengthHuge = weaponsStrengthHuge;
	}
	public int getWeaponsStrengthLarge() {
		return weaponsStrengthLarge;
	}
	public void setWeaponsStrengthLarge(int weaponsStrengthLarge) {
		this.weaponsStrengthLarge = weaponsStrengthLarge;
	}
	public int getWeaponsStrengthMedium() {
		return weaponsStrengthMedium;
	}
	public void setWeaponsStrengthMedium(int weaponsStrengthMedium) {
		this.weaponsStrengthMedium = weaponsStrengthMedium;
	}
	public int getWeaponsStrengthSmall() {
		return weaponsStrengthSmall;
	}
	public void setWeaponsStrengthSmall(int weaponsStrengthSmall) {
		this.weaponsStrengthSmall = weaponsStrengthSmall;
	}
	public int getWeaponsStrengthSquadron() {
		return weaponsStrengthSquadron;
	}
	public void setWeaponsStrengthSquadron(int weaponsStrengthSquadron) {
		this.weaponsStrengthSquadron = weaponsStrengthSquadron;
	}
	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}
	public void setVisibleOnMap(boolean visibleOnMap) {
		this.changeVisibleOnMap = true;
		this.visibleOnMap = visibleOnMap;
	}

	public int getTroopCarrier() {
		return troopCarrier;
	}

	public void setTroopCarrier(int troopCarrier) {
		this.troopCarrier = troopCarrier;
	}
/*
	public int getTroopDownload() {
		return troopDownload;
	}

	public void setTroopDownload(int troopDownload) {
		this.troopDownload = troopDownload;
	}*/

}
