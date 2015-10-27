package sr.world;

import java.io.Serializable;

import sr.enums.BattleGroupPosition;
import sr.enums.BlackMarketFrequency;
import sr.enums.TroopTargetingType;
import sr.enums.TypeOfTroop;

public class TroopType implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;
	private UniqueIdCounter uic;
	// names, description etc
	private String uniqueName;
	private String uniqueShortName;
	private String description,history,advantages,disadvantages,shortDescription;
	private int nrProduced; // used to create unique name for a Troop object 
	// attack values
	private int attackInfantry;
	private int attackArmored;
	private int attackArtillery;
	// other abilities
	private int damageCapacity;
	private int dropPenalty = 1; // lowers the number of attacks on the first turn when dropped on a planet
	private int firingBackPenalty = 10;  // lowers the damage roll when firing back when under attack
	private int nrAttacks = 3; // number of attacks each turn
	private TypeOfTroop typeOfTroop = TypeOfTroop.INFANTRY; // infantry, armored or support
	private boolean spaceshipTravel = true; // if false a unit can't leave the planet where it is built
	private BlackMarketFrequency blackMarketFrequency = BlackMarketFrequency.COMMON;
	private int blackmarketFirstTurn = 0;
	
	
//	private boolean attackScreened;  // if true unit can be set to flanker
	private BattleGroupPosition defaultPosition = BattleGroupPosition.FIRST_LINE;
	private boolean canBuild = true; // determines if a player with this trooptype can build this type
	private boolean visible = true; // if false other players cannot see the troop on the map
	private boolean canAppearOnBlackMarket = true;
	private TroopTargetingType targetingType = TroopTargetingType.ALLROUND;
	private int landBattleGroupAttacksBonus;
	// costs
	private int costBuild;
	private int upkeep; // count against each planet's individual upkeep value (=production)
	// used for research
	//private boolean availableToBuild = true;
//	worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player.
	private boolean worldUnique=  false, factionUnique = false, playerUnique =  false;
	
    public TroopType(String aUniqueName, String aUniqueShortName, int aDamageCapacity, int aUpkeep, int aCostBuild, UniqueIdCounter aUic, int anAttackInfantry, int anAttackArmor){
    	this.uniqueName = aUniqueName;
    	this.uniqueShortName = aUniqueShortName;
    	this.damageCapacity = aDamageCapacity;
    	this.upkeep = aUpkeep;
    	this.costBuild = aCostBuild;
    	this.uic = aUic;
    	this.attackInfantry = anAttackInfantry;
    	this.attackArmored = anAttackArmor;
    }
    
    @Override
    public TroopType clone(){
    	TroopType tmpTroopType = null;
    	try {
			tmpTroopType = (TroopType)super.clone();
			tmpTroopType.setNrProduced(0);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
    	return tmpTroopType;
    }
    
    @Override
    public String toString(){
    	return uniqueName + " (" + uniqueShortName + ")";
    }
    
    public Troop getTroop(VIP vipWithTechBonus, int factionTechBonus, int buildingTechBonus){
    	nrProduced++;
    	int totalTechBonus = 0;
    	if (vipWithTechBonus != null){
    		totalTechBonus = vipWithTechBonus.getTechBonus();
    	}
    	totalTechBonus += factionTechBonus; 
    	totalTechBonus += buildingTechBonus;
    	Troop tmpTroop = new Troop(this,nrProduced,totalTechBonus,uic.getUniqueId()); 
    	return tmpTroop;
    }

	public String getAdvantages() {
		return advantages;
	}

	public void setAdvantages(String advantages) {
		this.advantages = advantages;
	}

	public TypeOfTroop getTypeOfTroop() {
		return typeOfTroop;
	}
	
	public boolean isArmor(){
		return typeOfTroop == TypeOfTroop.ARMORED;
	}

	public void setTypeOfTroop(TypeOfTroop newType) {
		this.typeOfTroop = newType;
	}

	public int getAttackArmored() {
		return attackArmored;
	}

	public void setAttackArmored(int attackArmored) {
		this.attackArmored = attackArmored;
	}

	public int getAttackArtillery() {
		return attackArtillery;
	}

	public void setAttackArtillery(int attackArtillery) {
		this.attackArtillery = attackArtillery;
	}

	public int getAttackInfantry() {
		return attackInfantry;
	}

	public void setAttackInfantry(int attackInfantry) {
		this.attackInfantry = attackInfantry;
	}
/*
	public boolean isAttackScreened() {
		return attackScreened;
	}

	public void setAttackScreened(boolean attackScreened) {
		this.attackScreened = attackScreened;
	}
*/
	public BlackMarketFrequency getBlackMarketFrequency() {
		return blackMarketFrequency;
	}

	public void setBlackMarketFrequency(BlackMarketFrequency blackMarketFrequency) {
		this.blackMarketFrequency = blackMarketFrequency;
	}
	
	public int getCostBuild(VIP vipWithBonus) {
		int tempBuildCost = costBuild;
		if (vipWithBonus != null){
			int vipBuildbonus = 100 - vipWithBonus.getTroopBuildBonus();
			double tempBuildBonus = vipBuildbonus / 100.0;
			tempBuildCost = (int) Math.round(tempBuildCost * tempBuildBonus);
			if (tempBuildCost < 1){
				tempBuildCost = 1;
			}
		}
		return tempBuildCost;
		
	}

	public void setCostBuild(int costBuild) {
		this.costBuild = costBuild;
	}

	public int getUpkeep() {
		return upkeep;
	}

	public void setUpkeep(int upkeep) {
		this.upkeep = upkeep;
	}

	public int getDamageCapacity() {
		return damageCapacity;
	}

	public void setDamageCapacity(int damageCapacity) {
		this.damageCapacity = damageCapacity;
	}
	
	public String getTotalDescription(){
		String totalDescription = "";
    	
		if(advantages != null && !advantages.equals("")){
    		totalDescription += "Advantages: " + advantages + "\n\n";
        }
    	if(disadvantages != null && !disadvantages.equals("")){
    		totalDescription += "Disadvantages: " + disadvantages + "\n\n";
        }
    	
    	if(shortDescription != null && !shortDescription.equals("")){
    		totalDescription += "Short Description\n";
        	totalDescription += shortDescription + "\n\n";
    	}
    	
    	if(description != null && !description.equals("")){
    		totalDescription +="Description\n";
        	totalDescription += description + "\n\n";
    	}
    	if(history != null && !history.equals("")){
    		totalDescription +="History\n";
        	totalDescription += history;
    	}
    	
    	return totalDescription;
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

	public int getDropPenalty() {
		return dropPenalty;
	}

	public void setDropPenalty(int dropPenalty) {
		this.dropPenalty = dropPenalty;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public int getNrAttacks() {
		return nrAttacks;
	}
	
	public int getBlackmarketFirstTurn() {
		return blackmarketFirstTurn;
	}

	public void setBlackmarketFirstTurn(int blackmarketFirstTurn) {
		this.blackmarketFirstTurn = blackmarketFirstTurn;
	}

/*
	public void setNrAttacks(int nrAttacks) {
		this.nrAttacks = nrAttacks;
	}
*/
	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public boolean isSpaceshipTravel() {
		return spaceshipTravel;
	}

	public void setSpaceshipTravel(boolean spaceshipTravel) {
		this.spaceshipTravel = spaceshipTravel;
	}

	public int getNrProduced() {
		return nrProduced;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public String getUniqueShortName() {
		return uniqueShortName;
	}

    public UniqueIdCounter getUniqueIdCounter(){
    	return uic;
    }

	public void setNrProduced(int nrProduced) {
		this.nrProduced = nrProduced;
	}

	public boolean isCanBuild() {
		return canBuild;
	}

	public void setCanBuild(boolean canBuild) {
		this.canBuild = canBuild;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public BattleGroupPosition getDefaultPosition() {
		return defaultPosition;
	}

	public void setDefaultPosition(BattleGroupPosition defaultPosition) {
		this.defaultPosition = defaultPosition;
	}

	public TroopTargetingType getTargetingType() {
		return targetingType;
	}

	public void setTargetingType(TroopTargetingType targetingType) {
		this.targetingType = targetingType;
	}

	public boolean isCanAppearOnBlackMarket() {
		return canAppearOnBlackMarket;
	}

	public void setCanAppearOnBlackMarket(boolean canAppearOnBlackMarket) {
		this.canAppearOnBlackMarket = canAppearOnBlackMarket;
	}

	public int getFiringBackPenalty() {
		return firingBackPenalty;
	}

	public void setFiringBackPenalty(int firingBackPenalty) {
		this.firingBackPenalty = firingBackPenalty;
	}

	/*public boolean isAvailableToBuild() {
		return availableToBuild;
	}

	public void setAvailableToBuild(boolean availableToBuild) {
		this.availableToBuild = availableToBuild;
	}*/

	public int getLandBattleGroupAttacksBonus() {
		return landBattleGroupAttacksBonus;
	}

	public void setLandBattleGroupAttacksBonus(int landBattleGroupAttacksBonus) {
		this.landBattleGroupAttacksBonus = landBattleGroupAttacksBonus;
	}
	
	public boolean isWorldUnique() {
		return worldUnique;
	}

	public void setWorldUnique(boolean worldUnique) {
		this.worldUnique = worldUnique;
	}

	public boolean isFactionUnique() {
		return factionUnique;
	}

	public void setFactionUnigue(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}

	public boolean isPlayerUnique() {
		return playerUnique;
	}
	
	public String getUniqueString(){
		String uniqueString = "";
  
		if(playerUnique){
			uniqueString = "Player unique";
		}else
		if(factionUnique){
			uniqueString = "Faction unique";
		}else
		if(worldUnique){
			uniqueString = "World unique";
		}
  
		return uniqueString;
	}

	public boolean isFactionUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().troopTypeExist(this, aPlayer.getFaction(), null);
	}

	public boolean isPlayerUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().troopTypeExist(this, null, aPlayer);
	}

	public boolean isWorldUniqueBuild(Galaxy aGalaxy) {
		return aGalaxy.troopTypeExist(this, null, null);
	}
	
	public void setFactionUnique(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}

	public void setPlayerUnique(boolean playerUnique) {
		this.playerUnique = playerUnique;
	}
	
	
	public boolean isConstructible(Player aPlayer){
		boolean constructible =  true;
		if(!isCanBuild()){
			constructible = false;
		}else if((isWorldUnique() && isWorldUniqueBuild(aPlayer.getGalaxy())) || (isFactionUnique() && isFactionUniqueBuild(aPlayer)) || (isPlayerUnique() && isPlayerUniqueBuild(aPlayer))){
			constructible = false;
		}else if(isWorldUnique() || isFactionUnique() || isPlayerUnique()){
			// check if a build order already exist
			if(aPlayer.getOrders().haveTroopTypeBuildOrder(this)){
				constructible = false;
			}
			for (BlackMarketOffer aBlackMarketOffer : aPlayer.getGalaxy().getBlackMarket().getCurrentOffers()) {
				if(aBlackMarketOffer.isTroop() && aBlackMarketOffer.getOfferedTroopType().getUniqueName().equals(uniqueName)){
					constructible = false;
				}
			}
		}
		return constructible;
	}
	
	public boolean isReadyToUseInBlackMarket(Galaxy aGalaxy){
		boolean constructible =  false;
		if(aGalaxy.getTurn() >= getBlackmarketFirstTurn()){
			if (isSpaceshipTravel()){
				if (isCanAppearOnBlackMarket()){
					if(!isPlayerUnique() && !isFactionUnique()){
						if(isWorldUnique() && !isWorldUniqueBuild(aGalaxy)){
							boolean isAlreadyAoffer = false;
							for (BlackMarketOffer aBlackMarketOffer : aGalaxy.getBlackMarket().getCurrentOffers()) {
								if(aBlackMarketOffer.isTroop() && aBlackMarketOffer.getOfferedTroopType().getUniqueName().equals(uniqueName)){
									isAlreadyAoffer = true;
								}
							}
							
							if(!isAlreadyAoffer){
								boolean haveBuildingOrder = false;
								for (Player tempPlayer : aGalaxy.getPlayers()) {
									if(tempPlayer.getOrders().haveTroopTypeBuildOrder(this)){
										haveBuildingOrder = true;
									}
								}
								if(!haveBuildingOrder){
									constructible =  true;
								}
							}
						}else{
							constructible = true;
						}
					}
				}
			}
		}
		return constructible;
	}
	

}
