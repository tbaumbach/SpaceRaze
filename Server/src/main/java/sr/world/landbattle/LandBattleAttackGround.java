package sr.world.landbattle;

import java.util.List;

import sr.enums.LandBattleAttackType;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Troop;
import sr.world.landbattle.report.LandBattleReport;

public class LandBattleAttackGround extends LandBattleAttack {
	private Troop attacker;
	private boolean defender; // true if troop is on their own planet

	public LandBattleAttackGround(LandBattleAttackType attackType, Troop anAttacker, List<Troop> aTargetOpponents, boolean defending, int aResistance, Galaxy galaxy){
		super(attackType,aTargetOpponents,aResistance,galaxy);
		attacker = anAttacker;
		this.defender = defending;
	}
 
	@Override
	public void performAttack(LandBattleReport attReport, LandBattleReport defReport, int attVipBonus, int defVipBonus) {
		Logger.finer("performAttack(ground)");
		if (attacker.isDestroyed()){
			Logger.finer("Attacker already destroyed");
		}else{
			Troop targetTroop = getRandomOpponent();
			if (targetTroop == null){
				Logger.finer("All opponents destroyed");
			}else{
				Logger.finer("targetTroop: " + targetTroop.getUniqueName());
				int attMultiplier = Functions.getRandomInt(1, 20);
				Logger.finer("attMultiplier: " + attMultiplier);
				int defMultiplier = 20-attMultiplier-targetTroop.getFiringBackPenalty();
				if (defMultiplier < 0){
					defMultiplier = 0;
				}
				Logger.finer("defMultiplier: " + defMultiplier);
				
				
				int attVIPBonus = attVipBonus;
				int defVIPBonus = defVipBonus;
				if(defender){
					attVIPBonus = defVipBonus;
					defVIPBonus = attVipBonus;
				}
				
				int attackerActualDamage = attacker.getActualDamage(targetTroop.isArmor(),attMultiplier,defender,resistance, attVIPBonus);
				Logger.finer("attackerActualDamage: " + attackerActualDamage);
				int defenderActualDamage = targetTroop.getActualDamage(attacker.isArmor(),defMultiplier,!defender,resistance, defVIPBonus);
				Logger.finer("defenderActualDamage: " + defenderActualDamage);
				String result1 = targetTroop.hit(attackerActualDamage, false, !defender, resistance);
				Logger.finer(targetTroop.getUniqueName() + ": " + result1);
				if (targetTroop.isDestroyed()){
					attacker.addKill();
					g.removeTroop(targetTroop);
					attacker.addToLatestTroopsLostInSpace(targetTroop);
					targetTroop.addToLatestTroopsLostInSpace(targetTroop);
				}
				String result2 = attacker.hit(defenderActualDamage, false, defender, resistance);
				Logger.finer(attacker.getUniqueName() + ": " + result2);
				if (attacker.isDestroyed()){
					targetTroop.addKill();
					g.removeTroop(attacker);
					attacker.addToLatestTroopsLostInSpace(attacker);
					targetTroop.addToLatestTroopsLostInSpace(attacker);
				}
				attReport.addAttackResultGround(attacker,targetTroop,attackerActualDamage,defenderActualDamage,attMultiplier,defMultiplier,!defender);
				defReport.addAttackResultGround(attacker,targetTroop,attackerActualDamage,defenderActualDamage,attMultiplier,defMultiplier,defender);
			}
		}
	}

	@Override
	public String toString(){
		String retStr = "LBAG:";
		retStr = retStr + "att=" + attacker.getUniqueShortName() + " ";
		retStr = retStr + "def=" + defender + " ";
		retStr = retStr + getAsString();
		return retStr;
	}

}
