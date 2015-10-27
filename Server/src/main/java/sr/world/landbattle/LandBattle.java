package sr.world.landbattle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sr.enums.TroopTargetingType;
import sr.enums.TypeOfTroop;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Troop;
import sr.world.VIP;
import sr.world.landbattle.report.LandBattleReport;
import sr.world.spacebattle.ReportLevel;

public class LandBattle {
	private Player defendingPlayer;
	private List<Troop> defendingTroops; 
	private Player attackingPlayer; 
	private List<Troop> attackingTroops; 
	private Planet aPlanet;
	private int currentTurn;
	private Galaxy g;
	private LandBattleReport attackingBattleReport;
	private LandBattleReport defendingBattleReport;
	private int attVipBonus= 0, defVipBonus= 0;
	
	public LandBattle(Player defendingPlayer,List<Troop> defendingTroops, Player attackingPlayer, List<Troop> attackingTroops, Planet aPlanet, int currentTurn, Galaxy aGalaxy){
		this.defendingPlayer = defendingPlayer;
		this.defendingTroops = defendingTroops;
		this.attackingPlayer = attackingPlayer;
		this.attackingTroops = attackingTroops;
		this.aPlanet = aPlanet;
		this.currentTurn = currentTurn;
		this.g = aGalaxy;
		

		// is there any VIPS with group attacks bonus in this Landbattlegroup?
		List<VIP> groupBonusVIPs = new LinkedList<VIP>();
		for (Troop aTroop : attackingTroops) {
			groupBonusVIPs.addAll(g.findLandBattleVIPs(aTroop,true));
		}
		for (VIP vip : groupBonusVIPs) {
			if (vip.getLandBattleGroupAttackBonus() > attVipBonus){
				Logger.info("###(Tobbe)##### Adding VIP bonus = "  + attVipBonus);
				attVipBonus = vip.getLandBattleGroupAttackBonus();
			}
		}
		
		// is there any VIPS with group attacks bonus in this Landbattlegroup?
		groupBonusVIPs = new LinkedList<VIP>();
		for (Troop aTroop : defendingTroops) {
			groupBonusVIPs.addAll(g.findLandBattleVIPs(aTroop,true));
		}
		for (VIP vip : groupBonusVIPs) {
			if (vip.getLandBattleGroupAttackBonus() > defVipBonus){
				defVipBonus = vip.getLandBattleGroupAttackBonus();
			}
		}
	}
	
	public void performBattle(){
		Logger.finer("performBattle() called");
		Logger.finer("Current turn: " + currentTurn);
		// create reports
		attackingBattleReport = new LandBattleReport(false,aPlanet);
		defendingBattleReport = new LandBattleReport(true,aPlanet);
    	// create battle groups
    	LandBattleGroup defBG = new LandBattleGroup(defendingPlayer,defendingTroops,aPlanet);
    	LandBattleGroup attBG = new LandBattleGroup(attackingPlayer,attackingTroops,aPlanet);
    	// perform battle group lineup
		Logger.finer("Starting lineup");
    	defBG.performLineup();
    	attBG.performLineup();
    	defBG.modifyLineup(attBG);
    	attBG.modifyLineup(defBG);
    	defendingBattleReport.addOwnInitialForces(defBG);
    	defendingBattleReport.addEnemyInitialForces(attBG);
    	attackingBattleReport.addOwnInitialForces(attBG);
    	attackingBattleReport.addEnemyInitialForces(defBG);
		Logger.finer("Lineup finished");
    	// set unit opposition
    	OpponentHandler opponentHandler = new OpponentHandler();
    	setOpponents(attBG,defBG,opponentHandler);
    	// create master attack list
		Logger.finer("Creating master attack list");
    	List<LandBattleAttack> attackList = new LinkedList<LandBattleAttack>();
    	// add all close combat & support troops to master attack list
    	int resistance = aPlanet.getResistance();
    	defBG.addToMasterAttackList(attackList,opponentHandler,currentTurn,attBG,true,resistance,g);
    	printMasterAttackList(attackList);
    	attBG.addToMasterAttackList(attackList,opponentHandler,currentTurn,defBG,false,resistance,g);
    	printMasterAttackList(attackList);
		Logger.finer("Master attack list finished");
    	// randomize master attack list
    	Collections.shuffle(attackList);
    	printMasterAttackList(attackList);
		Logger.finer("Master attack list shuffled");
    	// traverse master attack list and perform attacks and counter attacks
    	performAttacks(attackList,opponentHandler,attackingBattleReport,defendingBattleReport, attVipBonus, defVipBonus);
    	// summarize result of battle
    	defendingBattleReport.addOwnPostBattleForces(defBG);
    	defendingBattleReport.addEnemyPostBattleForces(attBG);
    	attackingBattleReport.addOwnPostBattleForces(attBG);
    	attackingBattleReport.addEnemyPostBattleForces(defBG);
    	// test, write result...
    	Logger.finer("");
    	Logger.finer("Attackers battle report:");
    	Logger.finer("------------------------");
    	Logger.finer("");
    	Logger.finer(attackingBattleReport.getAsString(ReportLevel.MEDIUM));
    	Logger.finer("");
    	Logger.finer("Defenders battle report:");
    	Logger.finer("------------------------");
    	Logger.finer("");
    	Logger.finer(defendingBattleReport.getAsString(ReportLevel.MEDIUM));
	}
	
	/**
	 * Used when testing
	 * @param attackList
	 */
	private void printMasterAttackList(List<LandBattleAttack> attackList){
		Logger.finer("Master attack printout:");
		for (LandBattleAttack attack : attackList) {
			Logger.finer("  " + attack.toString());
		}
	}
	
	private void performAttacks(List<LandBattleAttack> attackList,OpponentHandler opponentHandler, LandBattleReport attackingBattleReport, LandBattleReport defendingBattleReport, int attVipBonus, int defVipBonus){
		for (LandBattleAttack attack : attackList) {
			Logger.finer("***** " + attack.toString() + " *****");
			attack.performAttack(attackingBattleReport,defendingBattleReport, attVipBonus, defVipBonus);
		}
	}

    private void setOpponents(LandBattleGroup attBG,LandBattleGroup defBG, OpponentHandler opponentHandler){
    	// add smaller firstline to larger first line randomly
    	if (attBG.getNrFirstLine() > defBG.getNrFirstLine()){
    		Logger.finer("Attckar first line > defender first line");
    		defBG.addFirstLineOpponents(attBG,opponentHandler);
    	}else{
    		Logger.finer("Defender first line > attacker first line");
    		attBG.addFirstLineOpponents(defBG,opponentHandler);
    	}
    	// add unopposed firstline in larger force randomly
    	if (attBG.getNrFirstLine() > defBG.getNrFirstLine()){
    		Logger.finer("Attckar first line > defender first line (add second opponent)");
    		attBG.addFirstLineOpponents2(defBG,opponentHandler);
    	}else{
    		Logger.finer("Defender first line > attacker first line (add second opponent)");
    		defBG.addFirstLineOpponents2(attBG,opponentHandler);
    	}
    	// the force with reserves (only one side may have reserves left) may add these "smart" to opponents with only 1 opponent 
    	if (attBG.getNrReserves() > 0){
    		Logger.finer("Attckar reserve");
    		attBG.addReserveOpponents(defBG,opponentHandler);
    	}else{
    		Logger.finer("Defender reserve)");
    		defBG.addReserveOpponents(attBG,opponentHandler);
    	}
    	// if both size have flankers
    	if ((attBG.getNrFlankers() > 0) & (defBG.getNrFlankers() > 0)){
    		// match them up randomly until one side have no unopposed flankers left
    		Logger.finer("Flankers vs flankers");
    		addFlankersVsFlankers(attBG,defBG,opponentHandler);
    	}
    	// if one side have unopposed flankers
    	if ((attBG.getNrUnopposedFlankers(opponentHandler) > 0) & (defBG.getNrSupport() > 0)){
    		// any flankers left attack random support troop
    		Logger.finer("Attckar flanker aginst support");
    		addFlankersVsSupport(attBG,defBG,opponentHandler);
    	}else
    	if ((defBG.getNrUnopposedFlankers(opponentHandler) > 0) & (attBG.getNrSupport() > 0)){
    		// any flankers left attack random support troop
    		Logger.finer("Defender flanker aginst support");
    		addFlankersVsSupport(defBG,attBG,opponentHandler);
    	}
    	// if one side still have unopposed flankers
       	if (attBG.getNrUnopposedFlankers(opponentHandler) > 0){
    		// any flankers left attack random firstline troop in the back
       		Logger.finer("Attckar flanker aginst first line");
       		addFlankersVsFirstLine(attBG,defBG,opponentHandler);
    	}else
    	if (defBG.getNrUnopposedFlankers(opponentHandler) > 0){
    		// any flankers left attack random firstline troop in the back
    		Logger.finer("Defender flanker aginst first line");
       		addFlankersVsFirstLine(defBG,attBG,opponentHandler);
    	}
    }

    private void addFlankersVsFirstLine(LandBattleGroup flankingBG,LandBattleGroup stabbedBG,OpponentHandler opponentHandler){
    	List<Troop> tmpStabbed = stabbedBG.getUnstabbedFirstLine(flankingBG,opponentHandler);
    	List<Troop> tmpFlankers = flankingBG.getUnopposedFlankers(opponentHandler);
    	
    	while ((tmpStabbed.size()) > 0 & (tmpFlankers.size() > 0)){
			
			Collections.shuffle(tmpStabbed);
			Collections.shuffle(tmpFlankers);
			
			
			Troop tempTroop = null;
			Troop troop;
			Troop offTroop;
			
			offTroop = tmpFlankers.get(0);
			int index = 0;
			while (tempTroop == null && tmpStabbed.size() > index) {
				troop = tmpStabbed.get(index);
				if(offTroop.getTargetingType().equals(TroopTargetingType.ALLROUND)){
					tempTroop = troop;
					opponentHandler.addOpponents(offTroop, troop);
				} else if (offTroop.getTargetingType().equals(TroopTargetingType.ANTIINFANTRY)){
					if(troop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.INFANTRY)){
						tempTroop = troop;
					}
				}else{// ANTITANK
					if(troop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.ARMORED)){
						tempTroop = troop;
					}
				}
				index++;
			}
			if(tempTroop == null){
				tempTroop = tmpStabbed.get(0);
			}
			
			
			opponentHandler.addOpponents(offTroop,tempTroop);
			
			tmpStabbed = stabbedBG.getUnstabbedFirstLine(flankingBG,opponentHandler);
	    	tmpFlankers = flankingBG.getUnopposedFlankers(opponentHandler);
		}
    	
    	if (flankingBG.getNrUnopposedFlankers(opponentHandler) > 0){
    		// add up to two flankers against each firstline
        	tmpStabbed = stabbedBG.getMaxOneStabberFirstLine(flankingBG, opponentHandler);
        	tmpFlankers = flankingBG.getUnopposedFlankers(opponentHandler);
        	while ((tmpStabbed.size()) > 0 & (tmpFlankers.size() > 0)){
    			
    			Collections.shuffle(tmpStabbed);
    			Collections.shuffle(tmpFlankers);
    			
    			
    			Troop tempTroop = null;
    			Troop troop;
    			Troop offTroop;
    			
    			offTroop = tmpFlankers.get(0);
    			int index = 0;
    			while (tempTroop == null && tmpStabbed.size() > index) {
    				troop = tmpStabbed.get(index);
    				if(offTroop.getTargetingType().equals(TroopTargetingType.ALLROUND)){
    					tempTroop = troop;
    					opponentHandler.addOpponents(offTroop, troop);
    				} else if (offTroop.getTargetingType().equals(TroopTargetingType.ANTIINFANTRY)){
    					if(troop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.INFANTRY)){
    						tempTroop = troop;
    					}
    				}else{// ANTITANK
    					if(troop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.ARMORED)){
    						tempTroop = troop;
    					}
    				}
    				index++;
    			}
    			if(tempTroop == null){
    				tempTroop = tmpStabbed.get(0);
    			}
        		opponentHandler.addOpponents(tmpFlankers.get(0),tmpStabbed.get(0));
            	tmpStabbed = stabbedBG.getMaxOneStabberFirstLine(flankingBG, opponentHandler);
            	tmpFlankers = flankingBG.getUnopposedFlankers(opponentHandler);
        	}
    	}
    }

    private void addFlankersVsSupport(LandBattleGroup flankingBG,LandBattleGroup supportBG,OpponentHandler opponentHandler){
    	List<Troop> tmpSupport = supportBG.getUnopposedSupport(opponentHandler);
    	List<Troop> tmpFlankers = flankingBG.getUnopposedFlankers(opponentHandler);
    	while ((tmpSupport.size()) > 0 & (tmpFlankers.size() > 0)){
        	Collections.shuffle(tmpSupport);
        	Collections.shuffle(tmpFlankers);
        	opponentHandler.addOpponents(tmpFlankers.get(0),tmpSupport.get(0));
        	tmpSupport = supportBG.getUnopposedSupport(opponentHandler);
        	tmpFlankers = flankingBG.getUnopposedFlankers(opponentHandler);
    	}
    	if (flankingBG.getNrUnopposedFlankers(opponentHandler) > 0){
    		// add up to two flankers against each support
        	tmpSupport = supportBG.getSupportWithMaxOneOpponent(opponentHandler);
        	tmpFlankers = flankingBG.getUnopposedFlankers(opponentHandler);
        	while ((tmpSupport.size()) > 0 & (tmpFlankers.size() > 0)){
            	Collections.shuffle(tmpSupport);
            	Collections.shuffle(tmpFlankers);
        		opponentHandler.addOpponents(tmpFlankers.get(0),tmpSupport.get(0));
            	tmpSupport = supportBG.getSupportWithMaxOneOpponent(opponentHandler);
            	tmpFlankers = flankingBG.getUnopposedFlankers(opponentHandler);
        	}
    	}
    }

	private void addFlankersVsFlankers(LandBattleGroup attBG,LandBattleGroup defBG, OpponentHandler opponentHandler){
		while ((attBG.getNrUnopposedFlankers(opponentHandler) > 0) & (defBG.getNrUnopposedFlankers(opponentHandler) > 0)){
			List<Troop> attFlankers = attBG.getUnopposedFlankers(opponentHandler);
			List<Troop> defFlankers = defBG.getUnopposedFlankers(opponentHandler);
			Collections.shuffle(attFlankers);
			Collections.shuffle(defFlankers);
			
			Logger.finer("Number of attacking flankers: " +  attFlankers.size());
			Logger.finer("Number of defending flankers: " +  defFlankers.size());
			
			Troop tempTroop = null;
			Troop troop;
			Troop offTroop;
			int randomInt = Functions.getRandomInt(1, 2);
			if(randomInt == 1){
				offTroop = attFlankers.get(0);
				int index = 0;
				while (tempTroop == null && defFlankers.size() > index) {
					troop = defFlankers.get(index);
					if(offTroop.getTargetingType().equals(TroopTargetingType.ALLROUND)){
						tempTroop = troop;
						opponentHandler.addOpponents(offTroop, troop);
					} else if (offTroop.getTargetingType().equals(TroopTargetingType.ANTIINFANTRY)){
						if(troop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.INFANTRY)){
							tempTroop = troop;
						}
					}else{// ANTITANK
						if(troop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.ARMORED)){
							tempTroop = troop;
						}
					}
					index++;
				}
				if(tempTroop == null){
					tempTroop = defFlankers.get(0);
				}
			}else{
				offTroop = defFlankers.get(0);
				int index = 0;
				while (tempTroop == null && attFlankers.size() > index) {
					troop = attFlankers.get(index);
					if(offTroop.getTargetingType().equals(TroopTargetingType.ALLROUND)){
						tempTroop = troop;
						opponentHandler.addOpponents(offTroop, troop);
					} else if (offTroop.getTargetingType().equals(TroopTargetingType.ANTIINFANTRY)){
						if(troop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.INFANTRY)){
							tempTroop = troop;
						}
					}else{// ANTITANK
						if(troop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.ARMORED)){
							tempTroop = troop;
						}
					}
					index++;
				}
				if(tempTroop == null){
					tempTroop = attFlankers.get(0);
				}
			}
			
			opponentHandler.addOpponents(offTroop,tempTroop);
		}
	}

	public LandBattleReport getAttackingBattleReport() {
		return attackingBattleReport;
	}
	
	public String getAttackingSummary(){
		return getAttackingBattleReport().getSummary();
	}

	public String getDefendingSummary(){
		return getDefendingBattleReport().getSummary();
	}

	public void setAttackingBattleReport(LandBattleReport attackingBattleReport) {
		this.attackingBattleReport = attackingBattleReport;
	}

	public LandBattleReport getDefendingBattleReport() {
		return defendingBattleReport;
	}

	public void setDefendingBattleReport(LandBattleReport defendingBattleReport) {
		this.defendingBattleReport = defendingBattleReport;
	}

}
