package sr.enums;

import sr.world.Spaceship;

public enum SpaceshipTargetingType {
	ANTIAIR(75,25,"Anti Squadron"),
	ALLROUND(50,50,"Balanced"),
	ANTIMBU(25,75,"Anti Capital");
	
	private int squadronWeight;
	private int capitalWeight;
	private String retStr;
	
	private SpaceshipTargetingType(int squadronWeight, int capitalkWeight, String aRetStr){
		this.squadronWeight = squadronWeight;
		this.capitalWeight = capitalkWeight;
		this.retStr = aRetStr;
	}
	
	public String toString(){
		return retStr;
	}

	public int getSquadronWeight(){
		return squadronWeight;
	}

	public int getCapitalWeight(){
		return capitalWeight;
	}

	public int getTargetingWeight(Spaceship aShip){
		int targetingWeight = 0;
		if (aShip.isSquadron()){
			targetingWeight = squadronWeight;
		}else{
			targetingWeight = capitalWeight;
		}
		return targetingWeight;
	}
}
