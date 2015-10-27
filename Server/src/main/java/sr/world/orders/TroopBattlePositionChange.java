package sr.world.orders;

import sr.enums.BattleGroupPosition;
import sr.world.Galaxy;
import sr.world.Troop;

public class TroopBattlePositionChange {
	private int troop;
	private BattleGroupPosition newPosition;
	
	public TroopBattlePositionChange(Troop aTroop, BattleGroupPosition aNewPosition){
		this.troop = aTroop.getId();
		this.newPosition = aNewPosition;
	}
	
	public Troop getTroop(Galaxy aGalaxy){
		return aGalaxy.findTroop(troop);
	}
	
	public int getTroopID(){
		return troop;
	}
	
	public boolean isTroop(Troop aTroop){
		return aTroop.getId() == troop;
	}
	
	public BattleGroupPosition getNewPosition(){
		return newPosition;
	}

	public String getText(Galaxy aGalaxy){
		return "Change battle position for troop " + aGalaxy.findTroop(troop).getUniqueName() + " from " +  aGalaxy.findTroop(troop).getPosition() + " to " + newPosition + ".";
	}

}
