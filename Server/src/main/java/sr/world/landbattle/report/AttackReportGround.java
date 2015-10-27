package sr.world.landbattle.report;

import java.io.Serializable;

import sr.general.Functions;
import sr.world.Troop;

public class AttackReportGround extends AttackReport implements Serializable{
    static final long serialVersionUID = 1L;
	private Troop attackingTroop;
	private int defenderActualDamage;
	private int defMultiplier;
	
	public AttackReportGround(Troop attackingTroop,Troop targetTroop,int attackerActualDamage,int defenderActualDamage,int attMultiplier,int defMultiplier,boolean attacker){
		super(targetTroop,attackerActualDamage,attMultiplier,attacker);
		this.attackingTroop = attackingTroop.clone();
		this.defenderActualDamage = defenderActualDamage;
		this.defMultiplier = defMultiplier;
	}
	
	public String getAsString(){
		StringBuffer sb = new StringBuffer();
		String attDesc = getAttackLevelDescription(attMultiplier);
		String defDesc = getAttackLevelDescription(defMultiplier);
		if (attacker){
			sb.append("Your troop " + attackingTroop.getUniqueName() + " have made " + Functions.getDeterminedForm(attDesc)+ " " + attDesc + " attack (" + attackerActualDamage + " damage) against an enemy " + targetTroop.getTroopType().getUniqueName());
			if (targetTroop.isDestroyed()){
				sb.append(" (destroyed).\n");
			}else{
				sb.append(" (" + targetTroop.getTroopStrength() + "%).\n");
			}
			if (defMultiplier > 0){
				sb.append("   The enemy troop perform " + defDesc + " counterfire (" + defenderActualDamage + " damage) against your troop");
				if (attackingTroop.isDestroyed()){
					sb.append(" (destroyed).\n");
				}else{
					sb.append(" (" + attackingTroop.getTroopStrength() + "%).\n");
				}
			}else{
				sb.append("   The enemy troop does not procuce any significant counterfire.\n");
			}
		}else{
			sb.append("Enemy troop " + attackingTroop.getTroopType().getUniqueName() + " have made " + Functions.getDeterminedForm(attDesc)+ " " + attDesc + " attack (" + attackerActualDamage + " damage) against your " + targetTroop.getUniqueName());
			if (targetTroop.isDestroyed()){
				sb.append(" (destroyed).\n");
			}else{
				sb.append(" (" + targetTroop.getTroopStrength() + "%).\n");
			}
			if (defMultiplier > 0){
				sb.append("   Your troop perform " + defDesc + " counterfire (" + defenderActualDamage + " damage) against the enemy troop");
				if (attackingTroop.isDestroyed()){
					sb.append(" (destroyed).\n");
				}else{
					sb.append(" (" + attackingTroop.getTroopStrength() + "%).\n");
				}
			}else{
				sb.append("   Your troop does not procuce any significant counterfire.\n");
			}
		}
		return sb.toString();
	}
}
