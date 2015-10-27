package sr.world.landbattle.report;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import sr.world.Planet;
import sr.world.Player;
import sr.world.Troop;
import sr.world.landbattle.LandBattleGroup;
import sr.world.spacebattle.ReportLevel;

public class LandBattleReport implements Serializable{
    static final long serialVersionUID = 1L;
	private boolean defender; // own forces are defending
	private List<Troop> initialOwnFirstLine;
	private List<Troop> initialOwnReserve;
	private List<Troop> initialOwnFlankers;
	private List<Troop> initialOwnSupport;
	private List<Troop> initialEnemyFirstLine;
	private List<Troop> initialEnemyReserve;
	private List<Troop> initialEnemyFlankers;
	private List<Troop> initialEnemySupport;
	private List<Troop> postBattleSurvivingOwnTroops;
	private List<Troop> postBattleDestroyedOwnTroops;
	private List<Troop> postBattleSurvivingEnemyTroops;
	private List<Troop> postBattleDestroyedEnemyTroops;
	private Planet planet;
	private List<AttackReport> attackReports;
	
	public LandBattleReport(boolean isDefender, Planet battlePlanet){
//		allStrings = new LinkedList<String>();
		defender = isDefender;
		planet = battlePlanet;
		attackReports = new LinkedList<AttackReport>();
	}
	
	public void addOwnInitialForces(LandBattleGroup ownBattleGroup){
		initialOwnFirstLine = cloneTroopList(ownBattleGroup.getFirstLine());
		initialOwnReserve = cloneTroopList(ownBattleGroup.getReserve());
		initialOwnFlankers = cloneTroopList(ownBattleGroup.getFlankers());
		initialOwnSupport = cloneTroopList(ownBattleGroup.getSupport());
	}

	public void addEnemyInitialForces(LandBattleGroup enemyBattleGroup){
		initialEnemyFirstLine = cloneTroopList(enemyBattleGroup.getFirstLine());
		initialEnemyReserve = cloneTroopList(enemyBattleGroup.getReserve());
		initialEnemyFlankers = cloneTroopList(enemyBattleGroup.getFlankers());
		initialEnemySupport = cloneTroopList(enemyBattleGroup.getSupport());
	}

	public void addOwnPostBattleForces(LandBattleGroup ownBattleGroup){
		postBattleSurvivingOwnTroops = cloneSurvivingTroopList(ownBattleGroup.getTroops(),true);
		postBattleDestroyedOwnTroops = cloneSurvivingTroopList(ownBattleGroup.getTroops(),false);
	}

	public void addEnemyPostBattleForces(LandBattleGroup enemyBattleGroup){
		postBattleSurvivingEnemyTroops = cloneSurvivingTroopList(enemyBattleGroup.getTroops(),true);
		postBattleDestroyedEnemyTroops = cloneSurvivingTroopList(enemyBattleGroup.getTroops(),false);
	}

	private List<Troop> cloneTroopList(List<Troop> aList){
		List<Troop> newList = null;
		if (aList != null){
			newList = new LinkedList<Troop>();
			for (Troop aTroop : aList) {
				newList.add(aTroop.clone());
			}
		}
		return newList;
	}

	private List<Troop> cloneSurvivingTroopList(List<Troop> aList, boolean getSurvivors){
		List<Troop> survivorList = null;
		List<Troop> destroyedList = null;
		if (aList != null){
			survivorList = new LinkedList<Troop>();
			destroyedList = new LinkedList<Troop>();
			for (Troop aTroop : aList) {
				if (aTroop.isDestroyed()){
					destroyedList.add(aTroop.clone());
				}else{
					survivorList.add(aTroop.clone());
				}
			}
		}
		List<Troop> retList = null;
		if (getSurvivors){
			retList = survivorList;
		}else{
			retList = destroyedList;
		}
		return retList;
	}

	public void addAttackResultGround(Troop attackingTroop,Troop targetTroop,int attackerActualDamage,int defenderActualDamage,int attMultiplier,int defMultiplier,boolean attacker){
		AttackReportGround newReport = new AttackReportGround(attackingTroop,targetTroop,attackerActualDamage,defenderActualDamage,attMultiplier,defMultiplier,attacker);
		attackReports.add(newReport);
	}

	public void addAttackResultArtillery(Troop attackingTroop,Troop targetTroop,int attackerActualDamage,int attMultiplier,boolean attacker){
		AttackReportArtillery newReport = new AttackReportArtillery(attackingTroop,targetTroop,attackerActualDamage,attMultiplier,attacker);
		attackReports.add(newReport);
	}

	public String getAsString(ReportLevel level){
		StringBuffer aBuffer = new StringBuffer();
		aBuffer.append("Land battle report for the planet " + planet.getName() + "\n");
		if (defender){
			aBuffer.append("Your forces are defending\n");
		}else{
			aBuffer.append("Your forces are attacking\n");
		}
		aBuffer.append("\n");
		aBuffer.append("Your forces are:\n");
		if ((initialOwnFirstLine != null) && initialOwnFirstLine.size() > 0){
			aBuffer.append("First-line: " + getOwnTroopsAsString(initialOwnFirstLine,level) + "\n");
		}
		if (initialOwnReserve.size() > 0){
			aBuffer.append("Reserve: " + getOwnTroopsAsString(initialOwnReserve,level) + "\n");
		}
		if (initialOwnFlankers.size() > 0){
			aBuffer.append("Flankers: " + getOwnTroopsAsString(initialOwnFlankers,level) + "\n");
		}
		if (initialOwnSupport.size() > 0){
			aBuffer.append("Support: " + getOwnTroopsAsString(initialOwnSupport,level) + "\n");
		}
		aBuffer.append("\n");
		aBuffer.append("Enemy forces owned by " + getEnemyNameAndFaction() + " are:\n");
		if (initialEnemyFirstLine.size() > 0){
			aBuffer.append("First-line: " + getEnemyTroopsAsString(initialEnemyFirstLine,level) + "\n");
		}
		if (initialEnemyReserve.size() > 0){
			aBuffer.append("Reserve: " + getEnemyTroopsAsString(initialEnemyReserve,level) + "\n");
		}
		if (initialEnemyFlankers.size() > 0){
			aBuffer.append("Flankers: " + getEnemyTroopsAsString(initialEnemyFlankers,level) + "\n");
		}
		if (initialEnemySupport.size() > 0){
			aBuffer.append("Support: " + getEnemyTroopsAsString(initialEnemySupport,level) + "\n");
		}
		aBuffer.append("\n");
		if (level.ordinal() >= ReportLevel.MEDIUM.ordinal()){
			// attack results here
			for (AttackReport anAttackReport : attackReports) {
				aBuffer.append(anAttackReport.getAsString());
			}
		}else{ // ReportLevel.LOW
			aBuffer.append("The battle lasted " + attackReports.size() + " rounds.\n");
		}
		aBuffer.append("\n");
		// post battle information

		aBuffer.append("Own survuving forces are:\n");
		if (postBattleSurvivingOwnTroops.size() > 0){
			aBuffer.append("Troops: " + getOwnTroopsAsString(postBattleSurvivingOwnTroops, level) + "\n");
		}
		aBuffer.append("Enemy survuving forces are:\n");
		if (postBattleSurvivingEnemyTroops.size() > 0){
			aBuffer.append("Troops: " + getEnemyTroopsAsString(postBattleSurvivingEnemyTroops, level) + "\n");
		}
		aBuffer.append("\n");

		aBuffer.append("Own forces lost:\n");
		boolean none = true;
		if (postBattleDestroyedOwnTroops.size() > 0){
			ReportLevel tempLeve;
			if(level.equals(ReportLevel.LONG)){
				tempLeve = ReportLevel.MEDIUM;
			}else{
				tempLeve = level;
			}
			aBuffer.append("Troops: " + getOwnTroopsAsString(postBattleDestroyedOwnTroops,tempLeve) + "\n");
			none = false;
		}
		if (none){
			aBuffer.append("None\n");
		}
		aBuffer.append("Enemy forces destroyed:\n");
		none = true;
		if (postBattleDestroyedEnemyTroops.size() > 0){
			ReportLevel tempLeve;
			if(level.equals(ReportLevel.LONG)){
				tempLeve = ReportLevel.MEDIUM;
			}else{
				tempLeve = level;
			}
			aBuffer.append("Troops: " + getEnemyTroopsAsString(postBattleDestroyedEnemyTroops,tempLeve) + "\n");
			none = false;
		}
		if (none){
			aBuffer.append("None\n");
		}
		aBuffer.append("\n");
		// battle summation (who won?)
		if (postBattleSurvivingEnemyTroops.size() == 0){
			if (postBattleSurvivingOwnTroops.size() == 0){
				aBuffer.append("Both forces have been destroyed\n");
			}else{
				aBuffer.append("Battle won!\n");
			}
		}else{
			if (postBattleSurvivingOwnTroops.size() == 0){
				aBuffer.append("Battle lost!\n");
			}else{
				aBuffer.append("Battle was inconclusive, both sides still have forces on planet\n");
			}
		}
		return aBuffer.toString();
	}

	public String getSummary(){
		String text = null;
		if (postBattleSurvivingEnemyTroops.size() == 0){
			if (postBattleSurvivingOwnTroops.size() == 0){
				text = "Both forces have been destroyed";
			}else{
				text = "Landbattle won!";
			}
		}else{
			if (postBattleSurvivingOwnTroops.size() == 0){
				text = "Landbattle lost!";
			}else{
				text = "Battle was inconclusive, both sides still have forces on planet";
			}
		}
		return text;
	}
	
	private String getOwnTroopsAsString(List<Troop> troops, ReportLevel level){
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Troop troop : troops) {
			if (!first){
				sb.append(",");
			}
			first = false;
			if (level == ReportLevel.SHORT){
				sb.append(troop.getUniqueShortName());
			}else if(level == ReportLevel.MEDIUM){
				sb.append(troop.getUniqueName());
			}else{ // add dc info in paranthesis
				sb.append(troop.getUniqueName());
				sb.append("(");
				sb.append(troop.getTroopStrength() + "%");
				sb.append(")");
			}
		}
		return sb.toString();
	}

	private String getEnemyTroopsAsString(List<Troop> troops, ReportLevel level){
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Troop troop : troops) {
			if (!first){
				sb.append(",");
			}
			first = false;
			if (level == ReportLevel.SHORT){
				sb.append(troop.getUniqueShortName());
			}else if(level == ReportLevel.MEDIUM){
				sb.append(troop.getUniqueName());
			}else{ // add dc info in paranthesis
				sb.append(troop.getUniqueName());
				sb.append("(");
				sb.append(troop.getTroopStrength() + "%");
				sb.append(")");
			}
		}
		return sb.toString();
	}
	
	private String getEnemyNameAndFaction(){
		Player aPlyer = null;
		if (initialEnemyFirstLine.size() > 0){
			aPlyer = initialEnemyFirstLine.get(0).getOwner();
		}else
		if (initialEnemyReserve.size() > 0){
			aPlyer = initialEnemyReserve.get(0).getOwner();
		}else
		if (initialEnemyFlankers.size() > 0){
			aPlyer = initialEnemyFlankers.get(0).getOwner();
		}else
		if (initialEnemySupport.size() > 0){
			aPlyer = initialEnemySupport.get(0).getOwner();
		}
		
		if(aPlyer != null){
			return aPlyer.getGovenorName() + " (" + aPlyer.getFaction().getName() + ")";
		}else{
			return "neutrals";
		}
		
	}

}
