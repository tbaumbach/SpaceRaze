package sr.world.landbattle.report;

import java.io.Serializable;

import sr.general.Functions;
import sr.world.Troop;

public class AttackReportArtillery extends AttackReport implements Serializable{
    static final long serialVersionUID = 1L;
	private Troop attackingTroop;
	
	public AttackReportArtillery(Troop attackingTroop,Troop targetTroop,int attackerActualDamage,int attMultiplier,boolean attacker){
		super(targetTroop,attackerActualDamage,attMultiplier,attacker);
		this.attackingTroop = attackingTroop.clone();
	}
	
	public String getAsString(){
		StringBuffer sb = new StringBuffer();
		String attDesc = getAttackLevelDescription(attMultiplier);
		if (attacker){
			sb.append("Your artillery troop " + attackingTroop.getUniqueShortName() + " have made " + Functions.getDeterminedForm(attDesc) + " " + attDesc + " attack (" + attackerActualDamage + " damage) against an enemy " + targetTroop.getTroopType().getUniqueShortName());
			if (targetTroop.isDestroyed()){
				sb.append(" (destroyed).\n");
			}else{
				sb.append(" (" + targetTroop.getTroopStrength() + "%).\n");
			}
		}else{
			sb.append("An enemy artillery troop " + attackingTroop.getTroopType().getUniqueShortName() + " have made " + Functions.getDeterminedForm(attDesc) + " " + attDesc + " attack (" + attackerActualDamage + " damage) against your " + targetTroop.getUniqueShortName());
			if (targetTroop.isDestroyed()){
				sb.append(" (destroyed).\n");
			}else{
				sb.append(" (" + targetTroop.getTroopStrength() + "%).\n");
			}
		}
		return sb.toString();
	}
}
