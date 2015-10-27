package sr.world.landbattle;

import java.util.List;

import sr.enums.LandBattleAttackType;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Troop;
import sr.world.landbattle.report.LandBattleReport;

public class LandBattleAttackArtillery extends LandBattleAttack {
	private Troop attacker;
	private boolean defender; // true if troop is on their own planet

	public LandBattleAttackArtillery(Troop anAttacker, List<Troop> aTargetOpponents, boolean defending, int aResistance, Galaxy galaxy){
		super(LandBattleAttackType.ARTILLERY,aTargetOpponents,aResistance,galaxy);
		attacker = anAttacker;
		this.defender = defending;
	}

	@Override
	public void performAttack(LandBattleReport attReport, LandBattleReport defReport, int vipBonus, int defVipBonus) {
		if (attacker.isDestroyed()){
			Logger.finer("Attacker already destroyed");
		}else{
			Troop targetTroop = getRandomOpponent();
			if (targetTroop == null){
				Logger.finer("All opponents destroyed");
			}else{
				Logger.finer("targetTroop: " + targetTroop.getUniqueShortName());
				int multiplier = Functions.getRandomInt(1, 20);
				Logger.finer("artMultiplier: " + multiplier);
				
				int attVIPBonus = vipBonus;
				if(defender){
					attVIPBonus = defVipBonus;
				}
				
				int actualDamage = attacker.getArtilleryActualDamage(multiplier,defender,resistance, attVIPBonus);
				String result = targetTroop.hit(actualDamage, true, !defender, resistance);
				Logger.finer(result);
				if (targetTroop.isDestroyed()){
					attacker.addKill();
					g.removeTroop(targetTroop);
					attacker.addToLatestTroopsLostInSpace(targetTroop);
					targetTroop.addToLatestTroopsLostInSpace(targetTroop);
				}
				attReport.addAttackResultArtillery(attacker,targetTroop,actualDamage,multiplier,!defender);
				defReport.addAttackResultArtillery(attacker,targetTroop,actualDamage,multiplier,defender);
			}
		}
	}

	@Override
	public String toString(){
		String retStr = "LBAA:";
		retStr = retStr + "att=" + attacker.getUniqueShortName() + " artDam=" + attacker.getAttackArtillery() + " ";
		retStr = retStr + "def=" + defender + " ";
		retStr = retStr + getAsString();
		return retStr;
	}

}
