package sr.enums;

import sr.world.Troop;

public enum TroopTargetingType {
	ANTIINFANTRY(75,25,0,"Anti Infantry"),
	ALLROUND(50,50,0,"Balanced"),
	ANTITANK(25,75,0,"Anti Tank"),
	ANTISCREENED(25,25,50,"Anti Screened");
	
	private int infantryWeight;
	private int tanksWeight;
	private int screenedWeight;
	private String retStr;
	
	private TroopTargetingType(int anInfantryWeight, int aTanksWeight, int aScreenedWeight, String aRetStr){
		infantryWeight = anInfantryWeight;
		tanksWeight = aTanksWeight;
		screenedWeight = aScreenedWeight;
		this.retStr = aRetStr;
	}
	
	public String toString(){
		return retStr;
	}

	public int getInfantryWeight(){
		return infantryWeight;
	}

	public int getTanksWeight(){
		return tanksWeight;
	}

	public int getScreenedWeight(){
		return screenedWeight;
	}

	public int getTargetingWeight(Troop aTroop){
		int targetingWeight = 0;
		if (aTroop.getPosition() == BattleGroupPosition.SUPPORT){
			targetingWeight = screenedWeight;
		}else
		if (aTroop.isArmor()){
			targetingWeight = tanksWeight;
		}else{
			targetingWeight = infantryWeight;
		}
		return targetingWeight;
	}
}
