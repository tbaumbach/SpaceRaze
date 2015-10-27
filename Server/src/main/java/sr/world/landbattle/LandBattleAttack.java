package sr.world.landbattle;

import java.util.LinkedList;
import java.util.List;

import sr.enums.LandBattleAttackType;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Troop;
import sr.world.landbattle.report.LandBattleReport;

public abstract class LandBattleAttack {
	protected LandBattleAttackType attackType;
	private List<Troop> targetOpponents;
	protected int resistance; // resistance on the planet where the battle is fought
	protected Galaxy g;
	
	public LandBattleAttack(LandBattleAttackType anAttackType, List<Troop> aTargetOpponents, int aResistance, Galaxy aGalaxy){
		attackType = anAttackType;
		targetOpponents = aTargetOpponents;
		resistance = aResistance;
		g = aGalaxy;
	}

	public LandBattleAttackType getAttackType(){
		return attackType;
	}
	
	public abstract void performAttack(LandBattleReport attReport, LandBattleReport defReport, int attVipBonus, int defVipBonus);
	
	protected Troop getRandomOpponent(){
		List<Troop> okOpponents = getNonDestroyedOpponents();
		Troop foundTroop = null;
		if (okOpponents.size() > 0){
			int randomIndex = Functions.getRandomInt(0, okOpponents.size()-1);
			foundTroop = okOpponents.get(randomIndex);
		}
		return foundTroop;
	}
	
	private List<Troop> getNonDestroyedOpponents(){
		List<Troop> okTroops = new LinkedList<Troop>();
		for (Troop aTroop : targetOpponents) {
			if (!aTroop.isDestroyed()){
				Logger.finer(aTroop.getUniqueShortName() + " not destroyed: " + aTroop.getCurrentDC());
				okTroops.add(aTroop);
			}else{
				Logger.finer(aTroop.getUniqueShortName() + " destroyed: " + aTroop.getCurrentDC());
			}
		}
		Logger.finer("okTroops size: " + okTroops.size());
		return okTroops;
	}
	
	protected String getAsString(){
		String retStr = "";
		retStr = retStr + "attType=" + attackType.toString() + " ";
		retStr = retStr + "res=" + resistance + " ";
		retStr = retStr + "opp=" + resistance + " ";
		for (Troop aTroop : targetOpponents) {
			retStr = retStr + aTroop.getUniqueShortName() + ";";
		}
		return retStr;
	}
}
