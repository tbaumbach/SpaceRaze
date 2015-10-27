package sr.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.enums.HighlightType;
import sr.general.logging.Logger;

public class ResearchAdvantage implements Serializable, Cloneable  {
	static final long serialVersionUID = 1L;
	
	private String name, description;
	private boolean developed;
	private int timeToResearch = 1, researchedTurns = 0;
	private int costToResearchOneTurn = 0;
	private int costToResearchOneTurnInProcent = 0;
	private List<ResearchAdvantage> children;
	@JsonIgnore private List<ResearchAdvantage> parents;
	private List<SpaceshipType> ships,replaceShips;
	private int openPlanetBonus = 0,closedPlanetBonus = 0,resistanceBonus = 0;

	private int techBonus = 0; 
	private boolean canReconstruct = false;
	private int reconstructCostBase = 0;
	private int reconstructCostMultiplier = 0;
	private List<ResearchUpgradeShip> researchUpgradeShip;
	// TroopTypes
	private List<TroopType> troops,replaceTroops;
	private List<ResearchUpgradeTroop> researchUpgradeTroop;
	
	private List<VIPType> vips,replaceVIPs;
	private List<BuildingType> buildings,replaceBuildings;
	private List<ResearchUpgradeVIP> researchUpgradeVIP;
	private List<ResearchUpgradeBuilding> researchUpgradeBuilding;
	
	Corruption corruption;
	
	public ResearchAdvantage(String name, String description) {
	    this.name = name;
	    this.description = description;
	    children = new ArrayList<ResearchAdvantage>();
		parents = new ArrayList<ResearchAdvantage>();
		ships = new ArrayList<SpaceshipType>();
		replaceShips = new ArrayList<SpaceshipType>();
		troops = new ArrayList<TroopType>();
		replaceTroops = new ArrayList<TroopType>();
		vips = new ArrayList<VIPType>();
		replaceVIPs = new ArrayList<VIPType>();
		buildings = new ArrayList<BuildingType>();
		replaceBuildings = new ArrayList<BuildingType>();
		//corruption = new Corruption();
		researchUpgradeShip =  new ArrayList<ResearchUpgradeShip>();
		researchUpgradeTroop = new ArrayList<ResearchUpgradeTroop>();
		researchUpgradeVIP = new ArrayList<ResearchUpgradeVIP>();
		researchUpgradeBuilding = new ArrayList<ResearchUpgradeBuilding>();
	}

	public ResearchAdvantage clone(){
		
		try {
			ResearchAdvantage temp = (ResearchAdvantage)super.clone();
			temp.children = new ArrayList<ResearchAdvantage>();
			temp.parents = new ArrayList<ResearchAdvantage>();
			temp.researchUpgradeShip = new ArrayList<ResearchUpgradeShip>(); 
			for(int i=0;i<researchUpgradeShip.size();i++){
				temp.researchUpgradeShip.add(researchUpgradeShip.get(i));
			}
			temp.researchUpgradeTroop = new ArrayList<ResearchUpgradeTroop>(); 
			for(int i=0;i<researchUpgradeTroop.size();i++){
				temp.researchUpgradeTroop.add(researchUpgradeTroop.get(i));
			}
			
			temp.researchUpgradeVIP = new ArrayList<ResearchUpgradeVIP>(); 
			for(int i=0;i<researchUpgradeVIP.size();i++){
				temp.researchUpgradeVIP.add(researchUpgradeVIP.get(i));
			}
			temp.researchUpgradeBuilding = new ArrayList<ResearchUpgradeBuilding>(); 
			for(int i=0;i<researchUpgradeBuilding.size();i++){
				temp.researchUpgradeBuilding.add(researchUpgradeBuilding.get(i));
			}
			
			return temp;
		}
		catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
	}
	
	public void research(TurnInfo ti,Player p){
		if(!developed){
			String researchInfoText="";
			Logger.finer("count up researchedTurns from " + researchedTurns);
			researchedTurns++;
			// The research is done and it's time to add the results.
			if(researchedTurns >= timeToResearch){
				researchInfoText="The reaserch on " + getName() + " is fineshed and gives you this:\n";
				// update Player with all new objects and bonus.
				
	
				
				
				if(openPlanetBonus > 0){
					p.setOpenPlanetBonus(p.getOpenPlanetBonus() + openPlanetBonus);
					researchInfoText+="Open planet bonus is now: " + p.getOpenPlanetBonus() + "\n";
				}
				if(closedPlanetBonus > 0){
					p.setClosedPlanetBonus(p.getClosedPlanetBonus() + closedPlanetBonus);
					researchInfoText+="Closed planet bonus is now: " + p.getClosedPlanetBonus() + "\n";
				}
				if(resistanceBonus > 0){
					p.setResistanceBonus(p.getResistanceBonus() + resistanceBonus);
					researchInfoText+="Resistance bonus is now: " + p.getResistanceBonus() + "\n";
				}
			
				if(techBonus > 0){
					p.setTechBonus(p.getTechBonus() + techBonus);
					researchInfoText+="Tech bonus is now: " + p.getTechBonus() + "%\n";
				}
				if(canReconstruct){
					p.setCanReconstruct(canReconstruct);
					researchInfoText+="You can now reconstruct planets\n";
				}
				if(reconstructCostBase > 0){
					p.setReconstructCostBase(p.getReconstructCostBase() - reconstructCostBase);
					researchInfoText+="Reconstruct cost base is now: " + p.getReconstructCostBase() + "\n";
				}
				if(corruption != null){
					
					p.setCorruption(corruption);
					researchInfoText+="Corruption is now: " + p.getCorruptionDescription() + "\n";
				}
				
				// adding ships to the player
				for(int i=0;i < ships.size();i++ ){
					
//					ships.get(i).setAvailableToBuild(true);
					p.findSpaceshipType(ships.get(i).getName()).setAvailableToBuild(true);
				//	p.removeSpaceshipType(p.findSpaceshipType(ships.get(i).getName()));
				//	p.addSpaceshipType(ships.get(i));
					Logger.finer("adding a new ship typ : " + ships.get(i).getName());
					researchInfoText+= "A new ship type: " + ships.get(i).getName() + ".\n";
				}
								
				//	removing old ships models from the player
				for(int i=0;i < replaceShips.size();i++ ){
//					replaceShips.get(i).setAvailableToBuild(false);
					p.findSpaceshipType(replaceShips.get(i).getName()).setAvailableToBuild(false);
					//p.removeSpaceshipType(replaceShips.get(i)); old
					Logger.finer("Removing old ship typ : " + replaceShips.get(i).getName());
					researchInfoText+= "The ship type: " + replaceShips.get(i).getName() + " was removed.\n";
				}

				// adding troops to the player
				for (TroopType aTroopType : troops) {
					p.findTroopType(aTroopType.getUniqueName()).setCanBuild(true);
					Logger.finer("adding a new troop type: " + aTroopType.getUniqueName());
					researchInfoText += "A new troop type: " + aTroopType.getUniqueName() + ".\n";
				}
								
				//	removing old troop types from the player
				for(TroopType aTroopType : replaceTroops){
					p.findTroopType(aTroopType.getUniqueName()).setCanBuild(false);
					Logger.finer("Removing old troop type : " + aTroopType.getUniqueName());
					researchInfoText += "The troop type: " + aTroopType.getUniqueName() + " was removed.\n";
				}
				
				// adding VIPs to the player
				for (VIPType aVIPType : vips) {
					p.findVIPType(aVIPType.getName()).setAvailableToBuild(true);
					Logger.finer("adding a new VIP type: " + aVIPType.getName());
					researchInfoText += "A new VIP type: " + aVIPType.getName() + ".\n";
				}
								
				//	removing old VIPs types from the player
				for (VIPType aVIPType : replaceVIPs) {
					p.findVIPType(aVIPType.getName()).setAvailableToBuild(false);
					Logger.finer("Removing old VIP type: " + aVIPType.getName());
					researchInfoText += "The VIP type: " + aVIPType.getName() + " was removed.\n";
				}
				
				//adding Buildings to the player
				for (BuildingType aBuildingType : buildings) {
					p.findBuildingType(aBuildingType.getName()).setDeveloped(true);
					Logger.finer("adding a new building type: " + aBuildingType.getName());
					researchInfoText += "A new building type: " + aBuildingType.getName() + ".\n";
				}
				
				//removing Buildings to the player
				for (BuildingType aBuildingType : replaceBuildings) {
					p.findBuildingType(aBuildingType.getName()).setDeveloped(false);
					Logger.finer("Removing old building type: " + aBuildingType.getName());
					researchInfoText += "The building type: " + aBuildingType.getName() + " was removed.\n";
				}
				
				
				developed = true;
				
				// check if a childe researchAdvantage have timeToResearch = 0 and ready to be research().
				
				for(int i=0; i < children.size();i++){
					if(children.get(i).getTimeToResearch() == 0 && children.get(i).isReadyToBeResearchedOn()){
						//TODO (Tobbe) add researchText.
						children.get(i).research(ti, p);
					}
				}
				
				for(int i=0;i<researchUpgradeShip.size();i++){
					researchInfoText+= researchUpgradeShip.get(i).doResearch(p.findSpaceshipType(researchUpgradeShip.get(i).getName()));
				}

				for (ResearchUpgradeTroop aResearchUpgradeTroop : researchUpgradeTroop) {
					researchInfoText += aResearchUpgradeTroop.doResearch(p.findTroopType(aResearchUpgradeTroop.getName()));
				}
				for (ResearchUpgradeVIP aResearchUpgradeVIP : researchUpgradeVIP) {
					researchInfoText += aResearchUpgradeVIP.doResearch(p.findVIPType(aResearchUpgradeVIP.getName()));
				}
				for (ResearchUpgradeBuilding aResearchUpgradeBuilding : researchUpgradeBuilding) {
					researchInfoText += aResearchUpgradeBuilding.doResearch(p.findBuildingType(aResearchUpgradeBuilding.getName()));
				}
		    	p.addToHighlights(name,HighlightType.TYPE_RESEARCH_DONE);
			}
			else{
				researchInfoText = "Research have been performed on " + getName() + " and will continue if you don't change the research orders";
			}
			ti.addToLatestResearchReport(researchInfoText);
		}
		
	}
	
	public String getResearchText(){
    	String text;
    	
    	text= "";
    	
    	if(openPlanetBonus > 0){
			text+="Open planet bonus: " + addplus(openPlanetBonus) + "\n";
		}
		if(closedPlanetBonus > 0){
			text+="Closed planet bonus: " + addplus(closedPlanetBonus) + "\n";
		}
		if(resistanceBonus > 0){
			text+="Resistance bonus: " + addplus(resistanceBonus) + "\n";
		}
		if(techBonus > 0){
			text+="Tech bonus: " + addplus(techBonus) + "%\n";
		}
		if(canReconstruct){
			text+="Can reconstruct planets\n";
		}
		if(reconstructCostBase > 0){
			text+="Reconstruct cost base: " + addplus(reconstructCostBase) + "\n";
		}
		if(reconstructCostMultiplier > 0){
			text+="Reconstruct multiplier cost: " + addplus(reconstructCostMultiplier) + "\n";
		}
		if(corruption != null){
			text+="Corruption: " + corruption.getCorruptionDescription() + "\n";
		}
		
		for(int i=0;i<researchUpgradeShip.size();i++){			
			text+= "\n" + researchUpgradeShip.get(i).getResearchText();
		}
		
		for (ResearchUpgradeTroop aResearchTroopType: researchUpgradeTroop) {
			text += "\n" + aResearchTroopType.getResearchText();
		}
		
		for (ResearchUpgradeVIP aResearchVIPType: researchUpgradeVIP) {
			text += "\n" + aResearchVIPType.getResearchText();
		}
		for (ResearchUpgradeBuilding aResearchBuildingType: researchUpgradeBuilding) {
			text += "\n" + aResearchBuildingType.getResearchText();
		}
		
    	return text;
	}
	
	private String addplus(int number){
		if(number > 0){
			return "+" +number;
	    }
	    return new Integer(number).toString();
	}
	
	@SuppressWarnings("unused")
	private String addYesOrNo(boolean test){
	   	if(test){
	   		return "Yes";
	   	}
	   	return "No";
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public boolean isDeveloped(){
		return developed;
	}
	
	public void setDeveloped(boolean developed){
		this.developed = developed;
	}
	
	public int getTimeToResearch(){
		return timeToResearch;
	}
	
	public void setTimeToResearch(int timeToResearch){
		this.timeToResearch = timeToResearch;
	}
	
	public int getResearchedTurns(){
		return researchedTurns;
	}
	
	public void setResearchedTurns(int researchedTurns){
		this.researchedTurns = researchedTurns;
	}
	
	public List<ResearchAdvantage> getChildren(){
		
		return this.children;
	}
	
	public ResearchAdvantage getChild(String key){
		Iterator<ResearchAdvantage> it = children.iterator();
		while(it.hasNext()){
			ResearchAdvantage temp = it.next();
			if(temp.getName().equals(key)){
				return temp;
			}
		}
		return null;
	}
	
	public void addChild(ResearchAdvantage child){		
		addChild(child,true);
	}

	public void addParent(ResearchAdvantage parent){
		addParent(parent,true);
	}

	private void addChild(ResearchAdvantage child, boolean addParentToChild){		
		this.children.add(child);
		if (addParentToChild){
			child.addParent(this,false);
		}
	}

	private void addParent(ResearchAdvantage parent, boolean addChildToParent){
		this.parents.add(parent);
		if (addChildToParent){
			parent.addChild(this,false);
		}
	}

	public List<ResearchAdvantage> getParents(){
		return parents;
	}
	
	public void setParents(List<ResearchAdvantage> parants){
		this.parents = parants;
	}
		
	public SpaceshipType getShip(String name){
		Iterator<SpaceshipType> it = ships.iterator();
		while(it.hasNext()){
			SpaceshipType tempShip = it.next();
			if(tempShip.getName().equals(name)){
				return tempShip;
			}
		}
		return null;
	}

	public void addShip(SpaceshipType ship){
		this.ships.add(ship);
	}
	
	public List<SpaceshipType> getShips(){
		return ships;
	}
	
	public void setShips(List<SpaceshipType> ships){
		this.ships = ships;
	}
	
	public TroopType getTroopType(String name){
		TroopType found = null;
		int counter = 0;
		while((found == null) & (counter < troops.size())){
			TroopType tempTroopType = troops.get(counter);
			if(tempTroopType.getUniqueName().equals(name)){
				found = tempTroopType;
			}
		}
		return found;
	}

	public void addTroopType(TroopType aTroopType){
		troops.add(aTroopType);
	}
	
	public List<TroopType> getTroopTypes(){
		return troops;
	}
	
	public void setTroopTypes(List<TroopType> newTroops){
		troops = newTroops;
	}
	
	public VIPType getVIPType(String name){
		VIPType found = null;
		int counter = 0;
		while((found == null) & (counter < vips.size())){
			VIPType tempVIPType = vips.get(counter);
			if(tempVIPType.getName().equals(name)){
				found = tempVIPType;
			}
		}
		return found;
	}

	public void addVIPType(VIPType aVIPType){
		vips.add(aVIPType);
	}
	
	public List<VIPType> getVIPTypes(){
		return vips;
	}
	
	public void setVIPTypes(List<VIPType> newVIPs){
		vips = newVIPs;
	}
	
	public BuildingType getBuildingType(String name){
		BuildingType found = null;
		int counter = 0;
		while((found == null) & (counter < buildings.size())){
			BuildingType tempBuildingType = buildings.get(counter);
			if(tempBuildingType.getName().equals(name)){
				found = tempBuildingType;
			}
		}
		return found;
	}

	public void addBuildingType(BuildingType aBuildingType){
		buildings.add(aBuildingType);
	}
	
	public List<BuildingType> getBuildingTypes(){
		return buildings;
	}
	
	public void setBuildingTypes(List<BuildingType> newBuildings){
		buildings = newBuildings;
	}
	
	public SpaceshipType getReplaceShip(String name){
		Iterator<SpaceshipType> it = replaceShips.iterator();
		while(it.hasNext()){
			SpaceshipType tempShip = it.next();
			if(tempShip.getName().equals(name)){
				return tempShip;
			}
		}
		return null;
	}

	public void addReplaceShip(SpaceshipType ship){
		this.replaceShips.add(ship);
	}

	public List<SpaceshipType> getReplaceShips() {
		return replaceShips;
	}
	
	public void setReplaceShips(List<SpaceshipType> replaceShips) {
		this.replaceShips = replaceShips;
	}
	
	public TroopType getReplaceTroopType(String name){
		TroopType found = null;
		int counter = 0;
		while((found == null) & (counter < replaceTroops.size())){
			TroopType tempTroopType = replaceTroops.get(counter);
			if(tempTroopType.getUniqueName().equals(name)){
				found = tempTroopType;
			}
		}
		return found;
	}

	public void addReplaceTroopTypes(TroopType aTroopType){
		this.replaceTroops.add(aTroopType);
	}

	public List<TroopType> getReplaceTroopTypes() {
		return replaceTroops;
	}
	
	public void setReplaceTroopTypes(List<TroopType> replaceTroopTypes) {
		this.replaceTroops = replaceTroopTypes;
	}
	
	public VIPType getReplaceVIPType(String name){
		VIPType found = null;
		int counter = 0;
		while((found == null) & (counter < replaceVIPs.size())){
			VIPType tempVIPType = replaceVIPs.get(counter);
			if(tempVIPType.getName().equals(name)){
				found = tempVIPType;
			}
		}
		return found;
	}

	public void addReplaceVIPType(VIPType aVIPType){
		replaceVIPs.add(aVIPType);
	}
	
	public List<VIPType> getReplaceVIPTypes(){
		return replaceVIPs;
	}
	
	public void setReplaceVIPTypes(List<VIPType> replaceVIPTypes){
		replaceVIPs = replaceVIPTypes;
	}
	
	public BuildingType getReplaceBuildingType(String name){
		BuildingType found = null;
		int counter = 0;
		while((found == null) & (counter < replaceBuildings.size())){
			BuildingType tempBuildingType = replaceBuildings.get(counter);
			if(tempBuildingType.getName().equals(name)){
				found = tempBuildingType;
			}
		}
		return found;
	}

	public void addReplaceType(BuildingType aBuildingType){
		replaceBuildings.add(aBuildingType);
	}
	
	public List<BuildingType> getReplaceBuildingTypes(){
		return replaceBuildings;
	}
	
	public void setReplaceBuildingTypes(List<BuildingType> newBuildings){
		replaceBuildings = newBuildings;
	}
	/*
	public int getBuildOrbitalStructureCostBase() {
		return buildOrbitalStructureCostBase;
	}

	public void setBuildOrbitalStructureCostBase(int buildOrbitalStructureCostBase) {
		this.buildOrbitalStructureCostBase = buildOrbitalStructureCostBase;
	}*/

	public boolean isCanReconstruct() {
		return canReconstruct;
	}

	public void setCanReconstruct(boolean canReconstruct) {
		this.canReconstruct = canReconstruct;
	}

	public int getClosedPlanetBonus() {
		return closedPlanetBonus;
	}

	public void setClosedPlanetBonus(int closedPlanetBonus) {
		this.closedPlanetBonus = closedPlanetBonus;
	}

	public int getOpenPlanetBonus() {
		return openPlanetBonus;
	}

	public void setOpenPlanetBonus(int openPlanetBonus) {
		this.openPlanetBonus = openPlanetBonus;
	}

	public int getReconstructCostBase() {
		return reconstructCostBase;
	}

	public void setReconstructCostBase(int reconstructCostBase) {
		this.reconstructCostBase = reconstructCostBase;
	}

	public int getResistanceBonus() {
		return resistanceBonus;
	}

	public void setResistanceBonus(int resistanceBonus) {
		this.resistanceBonus = resistanceBonus;
	}
	
	public int getTechBonus() {
		return techBonus;
	}

	public void setTechBonus(int techBonus) {
		this.techBonus = techBonus;
	}
/*
	public int getWharfBuildCost() {
		return wharfBuildCost;
	}

	public void setWharfBuildCost(int wharfBuildCost) {
		this.wharfBuildCost = wharfBuildCost;
	}

	public int getWharfUpgradeCostHuge() {
		return wharfUpgradeCostHuge;
	}

	public void setWharfUpgradeCostHuge(int wharfUpgradeCostHuge) {
		this.wharfUpgradeCostHuge = wharfUpgradeCostHuge;
	}

	public int getWharfUpgradeCostLarge() {
		return wharfUpgradeCostLarge;
	}

	public void setWharfUpgradeCostLarge(int wharfUpgradeCostLarge) {
		this.wharfUpgradeCostLarge = wharfUpgradeCostLarge;
	}

	public int getWharfUpgradeCostMedium() {
		return wharfUpgradeCostMedium;
	}

	public void setWharfUpgradeCostMedium(int wharfUpgradeCostMedium) {
		this.wharfUpgradeCostMedium = wharfUpgradeCostMedium;
	}
*/
	public int getCostToResearchOneTurn() {
		return costToResearchOneTurn;
	}

	public void setCostToResearchOneTurn(int costToResearchOneTurn) {
		this.costToResearchOneTurn = costToResearchOneTurn;
	}

	public int getCostToResearchOneTurnInProcent() {
		return costToResearchOneTurnInProcent;
	}

	public void setCostToResearchOneTurnInProcent(int costToResearchOneTurnInProcent) {
		this.costToResearchOneTurnInProcent = costToResearchOneTurnInProcent;
	}
	
	public boolean isReadyToBeResearchedOn(){
		for(int i=0;i < parents.size();i++){
			if(!parents.get(i).isDeveloped()){
				return false;
			}
		}
		return true;	
	}/*
	public int getBuildOrbitalStructureCostMulitplier() {
		return buildOrbitalStructureCostMulitplier;
	}
	public void setBuildOrbitalStructureCostMulitplier(
		int buildOrbitalStructureCostMulitplier) {
		this.buildOrbitalStructureCostMulitplier = buildOrbitalStructureCostMulitplier;
	}*/
	public int getReconstructCostMultiplier() {
		return reconstructCostMultiplier;
	}
	public void setReconstructCostMultiplier(int reconstructCostMultiplier) {
		this.reconstructCostMultiplier = reconstructCostMultiplier;
	}
	public List<ResearchUpgradeShip> getResearchUpgradeShip() {
		return researchUpgradeShip;
	}
	public List<ResearchUpgradeTroop> getResearchUpgradeTroop() {
		return researchUpgradeTroop;
	}
	public List<ResearchUpgradeVIP> getResearchUpgradeVIP() {
		return researchUpgradeVIP;
	}
	public List<ResearchUpgradeBuilding> getResearchUpgradeBuilding() {
		return researchUpgradeBuilding;
	}
	public void addResearchUpgradeShip(ResearchUpgradeShip researchUpgradeShip) {
		this.researchUpgradeShip.add(researchUpgradeShip);
	}
	public void addResearchUpgradeTroop(ResearchUpgradeTroop researchUpgradeTroop) {
		this.researchUpgradeTroop.add(researchUpgradeTroop);
	}
	public void addResearchUpgradeBuilding(ResearchUpgradeBuilding researchUpgradeBuilding) {
		this.researchUpgradeBuilding.add(researchUpgradeBuilding);
	}

	public Corruption getCorruption() {
		return corruption;
	}

	public void setCorruption(Corruption corruption) {
		this.corruption = corruption;
	}
	
}
