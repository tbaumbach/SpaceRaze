package sr.world.landbattle.report;

import java.io.Serializable;

import sr.world.Troop;

public abstract class AttackReport implements Serializable{
    static final long serialVersionUID = 1L;
	protected Troop targetTroop;
	protected int attackerActualDamage;
	protected int attMultiplier;
	protected boolean attacker;
	
	public AttackReport(Troop targetTroop,int attackerActualDamage,int attMultiplier,boolean attacker){
		this.targetTroop = targetTroop.clone();
		this.attackerActualDamage = attackerActualDamage;
		this.attMultiplier = attMultiplier;
		this.attacker = attacker;
	}
	
	public abstract String getAsString();
	
	protected String getAttackLevelDescription(int aMultiplier){
		String desc = "";
		if (aMultiplier <= 5){
			desc = "very weak";
		}else
		if ((aMultiplier >= 6) & (aMultiplier <= 10)){
			desc = "weak";
		}else
		if ((aMultiplier >= 11) & (aMultiplier <= 15)){
			desc = "good";
		}else
		if (aMultiplier >= 16){
			desc = "excellent";
		}
		return desc;
	}
}
