package sr.world;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.general.Functions;

public class ResearchUpgradeVIP implements Serializable {
	static final long serialVersionUID = 1L;
	private String name;
	
		//Abilities
	  private int assassination; // chance to assassinate
	  private int counterEspionage; // chance to catch other spies and assassins
	  private int exterminator; // chance to catch other spies and assassins
	  private boolean changeSpying; // can spy on planets
	  private boolean spying; // can spy on planets
	  private boolean changeImmuneToCounterEspionage; // can not be caught by counter espionage
	  private boolean immuneToCounterEspionage; // can not be caught by counter espionage
	  private int initBonus; // % bonus
	  private int initSupportBonus; // % bonus (not used - yet?), should not be used together with squadrons
	  private int initDefence; // % bonus, should not be used together with squadrons
	  private int initFighterSquadronBonus; // % bonus
	  private int antiAirBonus; // % bonus to AA damage, not used (yet?)
	  private int psychWarfareBonus; // bonus to resistance decrease of besieged planet
	  private int resistanceBonus; // bonus to resistance on planet
	  private int shipBuildBonus; // decreases build cost of ship
	  private int troopBuildBonus; // decreases build cost of troops
	  @SuppressWarnings("unused")
	  private int vipBuildBonus; // decreases build cost of VIPs. (NOT IN USE)
	  private int buildingBuildBonus; // decreases build cost of new building
	  private int techBonus; // increased shields & weapons for ships and troops built (%)
	  private int openIncBonus; // increase income on open planet
	  private int closedIncBonus; // increase income on closed planet
	  private boolean changeCanVisitEnemyPlanets;
	  private boolean canVisitEnemyPlanets;
	  private boolean changeCanVisitNeutralPlanets;
	  private boolean canVisitNeutralPlanets;
	  private int duellist; // may duel with other duellists, value = skill
	  private boolean changeHardToKill; // cannot be killed by ship destroyed or planet conquered/razed
	  private boolean hardToKill; // cannot be killed by ship destroyed or planet conquered/razed
	  private boolean changeWellGuarded; // cannot be assassinated
	  private boolean wellGuarded; // cannot be assassinated
	  private boolean changeFTLbonus; // enables a short range ship to travel long range
	  private boolean FTLbonus; // enables a short range ship to travel long range - not implemented (yet?)
	  private boolean changeDiplomat; // can persuade neutral planets
	  private boolean diplomat; // can persuade neutral planets
	  private boolean changeInfestate; // can infestate neutral planets
	  private boolean infestate; // can infestate neutral planets
	  private boolean changeShowOnOpenPlanet; // makes this vip visible to other players if on an open planet
	  private boolean showOnOpenPlanet; // makes this vip visible to other players if on an open planet
	  private int aimBonus;
	  private int troopAttacksBonus;
	  private int landBattleGroupAttacksBonus;

	  private int buildCost = 0; 
	  private int supplyCost = 0;
	  
	  public String doResearch(VIPType aVIP){
		  String text;
		  text = "\nThe VIP type " + getName() + " has been upgraded";
	    	
		  if(buildCost > 0){
	    		aVIP.setBuildCost(aVIP.getBuildCost() + buildCost);
	    	}
		  if(supplyCost > 0){
	    		aVIP.setUpkeep(aVIP.getUpkeep() + supplyCost);
	    	}
		  	if(assassination > 0){
	    		aVIP.setAssassination(aVIP.getAssassination() + assassination);
	    	}
	    	if(counterEspionage > 0){
	    		aVIP.setCounterEspionage(aVIP.getCounterEspionage() + counterEspionage);
	    	}
	    	if(exterminator > 0){
	    		aVIP.setExterminator(aVIP.getExterminator() + exterminator);
	    	}
	    	if(changeSpying){
	    		aVIP.setSpying(spying);
	    	}
	    	if(changeImmuneToCounterEspionage){
	    		aVIP.setImmuneToCounterEspionage(immuneToCounterEspionage);
	    	}
	    	if(initBonus > 0){
	    		aVIP.setInitBonus(aVIP.getInitBonus() + initBonus);
	    	}
	    	if(initDefence > 0){
	    		aVIP.setInitDefence(aVIP.getInitDefence() + initDefence);
	    	}
	    	if(initFighterSquadronBonus > 0){
	    		aVIP.setInitFighterSquadronBonus(aVIP.getInitFighterSquadronBonus() + initFighterSquadronBonus);
	    	}
	    	if(antiAirBonus > 0){
	    		aVIP.setAntiAirBonus(aVIP.getAntiAirBonus() + antiAirBonus);
	    	}
	    	if(psychWarfareBonus > 0){
	    		aVIP.setPsychWarfareBonus(aVIP.getPsychWarfareBonus() + psychWarfareBonus);
	    	}
	    	if(resistanceBonus > 0){
	    		aVIP.setResistanceBonus(aVIP.getResistanceBonus() + resistanceBonus);
	    	}
	    	if(shipBuildBonus > 0){
	    		aVIP.setShipBuildBonus(aVIP.getShipBuildBonus() + shipBuildBonus);
	    	}
	    	if(troopBuildBonus > 0){
	    		aVIP.setTroopBuildBonus(aVIP.getTroopBuildBonus() + troopBuildBonus);
	    	}
	    	if(buildingBuildBonus > 0){
	    		aVIP.setBuildingBuildBonus(aVIP.getBuildingBuildBonus() + buildingBuildBonus);
	    	}
	    	if(techBonus > 0){
	    		aVIP.setTechBonus(aVIP.getTechBonus() + techBonus);
	    	}
	    	if(openIncBonus > 0){
	    		aVIP.setOpenIncBonus(aVIP.getOpenIncBonus() + openIncBonus);
	    	}
	    	if(closedIncBonus > 0){
	    		aVIP.setClosedIncBonus(aVIP.getClosedIncBonus() + closedIncBonus);
	    	}
	    	if(duellist > 0){
	    		aVIP.setDuellistSkill(aVIP.getDuellistSkill() + duellist);
	    	}
	    	if(aimBonus > 0){
	    		aVIP.setAimBonus(aVIP.getAimBonus() + aimBonus);
	    	}
//	    	if(troopAttacksBonus > 0){
//	    		aVIP.setTroopAttacksBonus(aVIP.getTroopAttacksBonus() + troopAttacksBonus);
//	    	}
	    	if(landBattleGroupAttacksBonus > 0){
	    		aVIP.setLandBattleGroupAttackBonus(aVIP.getLandBattleGroupAttackBonus() + landBattleGroupAttacksBonus);
	    	}
	    	if(changeCanVisitEnemyPlanets){
	    		aVIP.setCanVisitEnemyPlanets(canVisitEnemyPlanets);
	    	}
	    	if(changeCanVisitNeutralPlanets){
	    		aVIP.setCanVisitNeutralPlanets(canVisitNeutralPlanets);
	    	}
	    	if(changeHardToKill){
	    		aVIP.setHardToKill(hardToKill);
	    	}
	    	if(changeWellGuarded){
	    		aVIP.setWellGuarded(wellGuarded);
	    	}
	    	if(changeFTLbonus){
	    		aVIP.setFTLbonus(FTLbonus);
	    	}
	    	if(changeDiplomat){
	    		aVIP.setDiplomat(diplomat);
	    	}
	    	if(changeInfestate){
	    		aVIP.setInfestate(infestate);
	    	}
	    	if(changeShowOnOpenPlanet){
	    		aVIP.setShowOnOpenPlanet(showOnOpenPlanet);
	    	}
	    	
	    	return text;
	    }
	  
	  @JsonIgnore
	  public String getResearchText(){
	    	String text;
	    	
	    	text= "Change properties for the VIP type: " + name;
	    	
	    	if(supplyCost > 0){
	    		text+="\n-Supply cost: " + addPlus(supplyCost);
	    	}
	    	if(buildCost > 0){
	    		text+="\n-Build cost: " + addPlus(buildCost);
	    	}
	    	if(initDefence > 0){
	    		text+="\n-InitDefence: " + addPlus(initDefence) + "%";
	    	}
	    	if(psychWarfareBonus > 0){
	    		text+="\n-PsychWarfareBonus: " + addPlus(psychWarfareBonus) + "%";
	    	}
	    	if(assassination > 0){
	    		text+="\n-Assassination: " + addPlus(assassination) + "%";
	    	}
	    	if(counterEspionage > 0){
	    		text+="\n-CounterEspionage: " + addPlus(counterEspionage) + "%";
	    	}
	    	if(exterminator > 0){
	    		text+="\n-Exterminator: " + addPlus(exterminator) + "%";
	    	}
	    	if(changeSpying){
	    		text+="\n-Spay: " + Functions.getYesNo(spying);
	    	}
	    	if(changeImmuneToCounterEspionage){
	    		text+="\n-Immune To Counter Espionage: " + Functions.getYesNo(immuneToCounterEspionage);
	    	}
	    	if(initBonus > 0){
	    		text+="\n-Initbonus: " + addPlus(initBonus) + "%";
	    	}
	    	if(initDefence > 0){
	    		text+="\n-Initdefence: " + addPlus(initDefence) + "%";
	    	}
	    	if(initFighterSquadronBonus > 0){
	    		text+="\n-InitFighterSquadronBonus: " + addPlus(initFighterSquadronBonus) + "%";
	    	}
	    	if(antiAirBonus > 0){
	    		text+="\n-Anti Air Bonus: " + addPlus(antiAirBonus) + "%";
	    	}
	    	if(psychWarfareBonus > 0){
	    		text+="\n-PsychWarfareBonus: " + addPlus(psychWarfareBonus);
	    	}
	    	if(resistanceBonus > 0){
	    		text+="\n-Resistance Bonus: " + addPlus(resistanceBonus);
	    	}
	    	if(shipBuildBonus > 0){
	    		text+="\n-Ship Build Bonus: " + addPlus(shipBuildBonus);
	    	}
	    	if(troopBuildBonus > 0){
	    		text+="\n-Troop Build Bonus: " + addPlus(troopBuildBonus);
	    	}
	    	if(buildingBuildBonus > 0){
	    		text+="\n-Building Build Bonus: " + addPlus(buildingBuildBonus) + "%";
	    	}
	    	if(techBonus > 0){
	    		text+="\n-Tech Bonus: " + addPlus(techBonus) + "%";
	    	}
	    	if(openIncBonus > 0){
	    		text+="\n-Open Planet Income Bonus: " + addPlus(openIncBonus);
	    	}
	    	if(closedIncBonus > 0){
	    		text+="\n-Closed Planet Income Bonus: " + addPlus(closedIncBonus);
	    	}
	    	if(duellist > 0){
	    		text+="\n-Duellist: " + addPlus(duellist) + "%";
	    	}
	    	if(aimBonus > 0){
	    		text+="\n-Aim Bonus: " + addPlus(aimBonus);
	    	}
	    	if(troopAttacksBonus > 0){
	    		text+="\n-Troop Attacks Bonus: " + addPlus(troopAttacksBonus);
	    	}
	    	if(landBattleGroupAttacksBonus > 0){
	    		text+="\n-Land Battle Group Attacks Bonus: " + addPlus(landBattleGroupAttacksBonus) + "%";
	    	}
	    	if(changeCanVisitEnemyPlanets){
	    		text+="\n-Can Visit Enemy Planets: " + Functions.getYesNo(canVisitEnemyPlanets);
	    	}
	    	if(changeCanVisitNeutralPlanets){
	    		text+="\n-Can Visit Neutral Planets: " + Functions.getYesNo(canVisitNeutralPlanets);
	    	}
	    	if(changeHardToKill){
	    		text+="\n-Hard To Kill: " + Functions.getYesNo(hardToKill);
	    	}
	    	if(changeWellGuarded){
	    		text+="\n-Well Guarded: " + Functions.getYesNo(wellGuarded);
	    	}
	    	if(changeFTLbonus){
	    		text+="\n-Boost Range Of Ship: " + Functions.getYesNo(FTLbonus);
	    	}
	    	if(changeDiplomat){
	    		text+="\n-Diplomat: " + Functions.getYesNo(diplomat);
	    	}
	    	if(changeInfestate){
	    		text+="\n-Infestate: " + Functions.getYesNo(infestate);
	    	}
	    	if(changeShowOnOpenPlanet){
	    		text+="\n-Show On Open Planet: " + Functions.getYesNo(showOnOpenPlanet);
	    	}
	    	
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
	  
	public int getAimBonus() {
		return aimBonus;
	}
	public void setAimBonus(int aimBonus) {
		this.aimBonus = aimBonus;
	}
	public int getAntiAirBonus() {
		return antiAirBonus;
	}
	public void setAntiAirBonus(int antiAirBonus) {
		this.antiAirBonus = antiAirBonus;
	}
	public int getAssassination() {
		return assassination;
	}
	public void setAssassination(int assassination) {
		this.assassination = assassination;
	}
	public int getBuildCost() {
		return buildCost;
	}
	public void setBuildCost(int buildCost) {
		this.buildCost = buildCost;
	}
	public int getBuildingBuildBonus() {
		return buildingBuildBonus;
	}
	public void setBuildingBuildBonus(int buildingBuildBonus) {
		this.buildingBuildBonus = buildingBuildBonus;
	}
	public boolean isCanVisitEnemyPlanets() {
		return canVisitEnemyPlanets;
	}
	public void setCanVisitEnemyPlanets(boolean canVisitEnemyPlanets) {
		this.changeCanVisitEnemyPlanets=true;
		this.canVisitEnemyPlanets = canVisitEnemyPlanets;
	}
	public boolean isCanVisitNeutralPlanets() {
		return canVisitNeutralPlanets;
	}
	public void setCanVisitNeutralPlanets(boolean canVisitNeutralPlanets) {
		this.changeCanVisitNeutralPlanets=true;
		this.canVisitNeutralPlanets = canVisitNeutralPlanets;
	}
	public int getClosedIncBonus() {
		return closedIncBonus;
	}
	public void setClosedIncBonus(int closedIncBonus) {
		this.closedIncBonus = closedIncBonus;
	}
	public int getCounterEspionage() {
		return counterEspionage;
	}
	public void setCounterEspionage(int counterEspionage) {
		this.counterEspionage = counterEspionage;
	}
	public boolean isDiplomat() {
		return diplomat;
	}
	public void setDiplomat(boolean diplomat) {
		this.changeDiplomat=true;
		this.diplomat = diplomat;
	}
	public int getDuellist() {
		return duellist;
	}
	public void setDuellist(int duellist) {
		this.duellist = duellist;
	}
	public int getExterminator() {
		return exterminator;
	}
	public void setExterminator(int exterminator) {
		this.exterminator = exterminator;
	}
	public boolean isFTLbonus() {
		return FTLbonus;
	}
	public void setFTLbonus(boolean lbonus) {
		this.changeFTLbonus=true;
		FTLbonus = lbonus;
	}
	
	public boolean isHardToKill() {
		return hardToKill;
	}
	public void setHardToKill(boolean hardToKill) {
		this.changeHardToKill=true;
		this.hardToKill = hardToKill;
	}
	
	public boolean isImmuneToCounterEspionage() {
		return immuneToCounterEspionage;
	}
	public void setImmuneToCounterEspionage(boolean immuneToCounterEspionage) {
		this.changeImmuneToCounterEspionage=true;
		this.immuneToCounterEspionage = immuneToCounterEspionage;
	}
	public boolean isInfestate() {
		return infestate;
	}
	public void setInfestate(boolean infestate) {
		this.changeInfestate=true;
		this.infestate = infestate;
	}
	public int getInitBonus() {
		return initBonus;
	}
	public void setInitBonus(int initBonus) {
		this.initBonus = initBonus;
	}
	public int getInitDefence() {
		return initDefence;
	}
	public void setInitDefence(int initDefence) {
		this.initDefence = initDefence;
	}
	public int getInitFighterSquadronBonus() {
		return initFighterSquadronBonus;
	}
	public void setInitFighterSquadronBonus(int initFighterSquadronBonus) {
		this.initFighterSquadronBonus = initFighterSquadronBonus;
	}
	public int getInitSupportBonus() {
		return initSupportBonus;
	}
	public void setInitSupportBonus(int initSupportBonus) {
		this.initSupportBonus = initSupportBonus;
	}
	public int getLandBattleGroupAttacksBonus() {
		return landBattleGroupAttacksBonus;
	}
	public void setLandBattleGroupAttacksBonus(int landBattleGroupAttacksBonus) {
		this.landBattleGroupAttacksBonus = landBattleGroupAttacksBonus;
	}
	
	public int getOpenIncBonus() {
		return openIncBonus;
	}
	public void setOpenIncBonus(int openIncBonus) {
		this.openIncBonus = openIncBonus;
	}
	public int getResistanceBonus() {
		return resistanceBonus;
	}
	public void setResistanceBonus(int resistanceBonus) {
		this.resistanceBonus = resistanceBonus;
	}
	public boolean isShowOnOpenPlanet() {
		return showOnOpenPlanet;
	}
	public void setShowOnOpenPlanet(boolean showOnOpenPlanet) {
		this.changeShowOnOpenPlanet=true;
		this.showOnOpenPlanet = showOnOpenPlanet;
	}
	public int getPsychWarfareBonus() {
		return psychWarfareBonus;
	}
	public void setPsychWarfareBonus(int psychWarfareBonus) {
		this.psychWarfareBonus = psychWarfareBonus;
	}
	
	public boolean isSpying() {
		return spying;
	}
	public void setSpying(boolean spying) {
		this.changeSpying=true;
		this.spying = spying;
	}
	
	public int getSupplyCost() {
		return supplyCost;
	}
	public void setSupplyCost(int supplyCost) {
		this.supplyCost = supplyCost;
	}
	public int getTechBonus() {
		return techBonus;
	}
	public void setTechBonus(int techBonus) {
		this.techBonus = techBonus;
	}
	public int getTroopAttacksBonus() {
		return troopAttacksBonus;
	}
	public void setTroopAttacksBonus(int troopAttacksBonus) {
		this.troopAttacksBonus = troopAttacksBonus;
	}
	public boolean isWellGuarded() {
		return wellGuarded;
	}
	public void setWellGuarded(boolean wellGuarded) {
		this.changeWellGuarded=true;
		this.wellGuarded = wellGuarded;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getShipBuildBonus() {
		return shipBuildBonus;
	}

	public void setShipBuildBonus(int shipBuildBonus) {
		this.shipBuildBonus = shipBuildBonus;
	}

	public int getTroopBuildBonus() {
		return troopBuildBonus;
	}

	public void setTroopBuildBonus(int troopBuildBonus) {
		this.troopBuildBonus = troopBuildBonus;
	}


	

}
