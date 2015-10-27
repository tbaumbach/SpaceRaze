package sr.world.landbattle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sr.enums.BattleGroupPosition;
import sr.enums.LandBattleAttackType;
import sr.enums.TroopTargetingType;
import sr.enums.TypeOfTroop;
import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Troop;

public class LandBattleGroup {
	private List<Troop> troops;
	// sub-lists
	private List<Troop> firstLine;
	private List<Troop> reserve;
	private List<Troop> flankers;
	private List<Troop> support;
	
	public LandBattleGroup(Player aPlayer,List<Troop> someTroops, Planet aPlanet){
		troops = someTroops;
		firstLine = new LinkedList<Troop>();
		reserve = new LinkedList<Troop>();
		flankers = new LinkedList<Troop>();
		support = new LinkedList<Troop>();
		
	      
	}
	
	public void addToMasterAttackList(List<LandBattleAttack> attackList,OpponentHandler opponentHandler,int currentTurn, LandBattleGroup otherBattleGroup, boolean defending, int aResistance, Galaxy g){
		// add all troops
		for (Troop aTroop : troops) {
			List<Troop> opponents = opponentHandler.getOpponents(aTroop);
			BattleGroupPosition aPosition = getPosition(aTroop);
			Logger.finer("addToMaster: " + aTroop.getUniqueShortName() + " " + (aPosition == BattleGroupPosition.SUPPORT) + " " + aTroop.getAttackArtillery() + " " + opponents.size());
			if (opponents.size() > 0){
				if (aPosition == BattleGroupPosition.FIRST_LINE){
					int nrAttacks = getNrAttacks(aTroop,currentTurn,g);
					List<Troop> eligbleOpponents = getFirstLineOpponentsOnly(opponents,otherBattleGroup);
					LandBattleAttackGround groundAttack = new LandBattleAttackGround(LandBattleAttackType.FIRSTLINE_VS_FIRSTLINE,aTroop,eligbleOpponents,defending,aResistance,g);
					createAttacks(groundAttack,nrAttacks,attackList);
				}else
				if (aPosition == BattleGroupPosition.FLANKER){
					int nrAttacks = getNrAttacks(aTroop,currentTurn,g);
					LandBattleAttackType attackType = opponentHandler.getFlankerAttackType(aTroop,aPosition,opponents,otherBattleGroup);
					LandBattleAttackGround groundAttack = new LandBattleAttackGround(attackType,aTroop,opponents,defending,aResistance,g);
					createAttacks(groundAttack,nrAttacks,attackList);
				}else
				if (aPosition == BattleGroupPosition.SUPPORT){ // support attacked by flankers perform ground attacks against their attackers, instead of using their artillery attack
					int nrAttacks = getNrAttacks(aTroop,currentTurn,g);
					LandBattleAttackGround groundAttack = new LandBattleAttackGround(LandBattleAttackType.SUPPORT_VS_FLANKER,aTroop,opponents,defending,aResistance,g);
					createAttacks(groundAttack,nrAttacks,attackList);
				}
				// Note: reserve never have an opponent and defence cannot attack, only do counter-fire
			}else
			if ((aPosition == BattleGroupPosition.SUPPORT) & (aTroop.getAttackArtillery() > 0)){
				Logger.finer("-> addingToMaster");
				// add artillery
				int nrAttacks = getNrAttacks(aTroop,currentTurn,g);
				List<Troop> eligbleOpponents = otherBattleGroup.getTroops();
				LandBattleAttackArtillery artilleryAttack = new LandBattleAttackArtillery(aTroop,eligbleOpponents,defending,aResistance,g);
				createAttacks(artilleryAttack,nrAttacks,attackList);
			}	
		}		
	}
/*	
	public List<Troop> getAllTroops(){
		return troops;
	}
	*/
	private List<Troop> getFirstLineOpponentsOnly(List<Troop> opponents, LandBattleGroup otherBattleGroup){
		List<Troop> eligbleOpponents = new LinkedList<Troop>();
		for (Troop troop : opponents) {
			if (otherBattleGroup.isFirstLine(troop)){
				eligbleOpponents.add(troop);
			}
		}
		return eligbleOpponents;
	}
	
	private void createAttacks(LandBattleAttack anAttack, int nrAttacks, List<LandBattleAttack> attackList){
		for (int i = 0; i < nrAttacks; i++){
			attackList.add(anAttack);
		}
	}
	
	private int getNrAttacks(Troop aTroop, int currentTurn, Galaxy g){
		Logger.finer("getNrAttacks; " + aTroop.getUniqueShortName());
		// get basic nr of attacks
		int attacks = aTroop.getTroopType().getNrAttacks();
		// is there any VIPs with attack bonus on this troop?
//		List<VIP> bonusVIPs = getLandBattleVIPs(aTroop, g);
//		int maxBonus = 0;
//		for (VIP vip : bonusVIPs) {
//			Logger.finer("bonusVIPs; " + vip.getTroopAttacksBonus());
//			if (vip.getTroopAttacksBonus() > maxBonus){
//				maxBonus = vip.getTroopAttacksBonus();
//			}
//		}
//		Logger.finer("maxBonus; " + maxBonus);
//		attacks += maxBonus;
		// is there any VIPS with group attacks bonus in this Landbattlegroup?
	/*	List<VIP> groupBonusVIPs = getLandBattleVIPs(g);
		int maxGroupBonus = 0;
		for (VIP vip : groupBonusVIPs) {
			LoggingHandler.finer("groupBonusVIPs; " + vip.getLandBattleGroupAttacksBonus());
			if (vip.getLandBattleGroupAttacksBonus() > maxGroupBonus){
				maxGroupBonus = vip.getLandBattleGroupAttacksBonus();
			}
		}
		LoggingHandler.finer("maxGroupBonus; " + maxGroupBonus);
		attacks += maxGroupBonus;*/
		// drop penalty?
		if (aTroop.getLastPlanetMoveTurn() == currentTurn){
			attacks -= aTroop.getTroopType().getDropPenalty();
			if (attacks < 0){ // is this one needed?
				attacks = 0; 
			}
		}
		return attacks;
	}
	
	/**
	 * Find all land battle VIPs on aTroop
	 * @param aTroop
	 * @param g
	 * @return
	 */
//	private List<VIP> getLandBattleVIPs(Troop aTroop, Galaxy g){
//		List<VIP> VIPs = new LinkedList<VIP>();
//		VIPs = g.findLandBattleVIPs(aTroop,false);
//		return VIPs;
//	}

	/**
	 * Find all land battle VIPs on troops in this battle group
	 * @param g
	 * @return
	 */
//	private List<VIP> getLandBattleVIPs(Galaxy g){
//		List<VIP> VIPs = new LinkedList<VIP>();
//		for (Troop aTroop : troops) {
//			VIPs.addAll(g.findLandBattleVIPs(aTroop,true));
//		}
//		return VIPs;
//	}

	public BattleGroupPosition getPosition(Troop aTroop){
		BattleGroupPosition aPosition = BattleGroupPosition.FIRST_LINE;
		if (findTroop(aTroop, reserve)){
			aPosition = BattleGroupPosition.RESERVE;
		}else
		if (findTroop(aTroop, flankers)){
			aPosition = BattleGroupPosition.FLANKER;
		}else
		if (findTroop(aTroop, support)){
			aPosition = BattleGroupPosition.SUPPORT;
		}
		return aPosition;
	}
	
	public void addFirstLineOpponents(LandBattleGroup largerBG, OpponentHandler opponentHandler){
		Collections.shuffle(firstLine);
		for (Troop aTroop : firstLine) {
			largerBG.addAsOpposingFirstLine(aTroop,opponentHandler);
		}
	}
	
	/**
	 * Find opponents for troops in the reserve
	 * @param smallerBG
	 * @param opponents
	 */
	public void addReserveOpponents(LandBattleGroup smallerBG, OpponentHandler opponentHandler){
		List<Troop> possibeOpponents = smallerBG.getPossibleOpponents(opponentHandler);
		while ((reserve.size() > 0) & (possibeOpponents.size() > 0)){
			Collections.shuffle(reserve);
			Troop anAttacker = reserve.get(0);
			Troop anOpponent = smallerBG.findOpponent(possibeOpponents,anAttacker);
			reserve.remove(0);
			firstLine.add(anAttacker); // ska verkligen truppen flyttas till FL??
			opponentHandler.addOpponents(anAttacker,anOpponent);
			possibeOpponents = smallerBG.getPossibleOpponents(opponentHandler);
		}
	}
	
	/**
	 * Find the most effective to attack against
	 * @param possibeOpponents
	 * @param anAttacker
	 * @return
	 */
	private Troop findOpponent(List<Troop> possibeOpponents,Troop anAttacker){
		Troop bestVictim = null;
		Collections.shuffle(possibeOpponents);
		for (Troop aTroop : possibeOpponents) {
			if (bestVictim == null){
				bestVictim = aTroop;
			}else{
				if (aTroop.getSuitableWeight(anAttacker) > bestVictim.getSuitableWeight(anAttacker)){
					bestVictim = aTroop;
				}
			}
		}
		return bestVictim;
	}
	
	/**
	 * Find all troops that have only 1 opponent
	 * @return
	 */
	public List<Troop> getPossibleOpponents(OpponentHandler opponentHandler){
		List<Troop> foundTroops = new LinkedList<Troop>();
		for (Troop aTroop : firstLine) {
			if (opponentHandler.maxOneOpponent(aTroop)){
				foundTroops.add(aTroop);
			}
		}
		return foundTroops;
	}

	/**
	 * Used to allocate the larger bg:s firstline troops to opponents
	 * @param smallerBG
	 * @param opponents
	 */
	public void addFirstLineOpponents2(LandBattleGroup smallerBG, OpponentHandler opponentHandler){
		Logger.finer("addFirstLineOpponents2");
		Collections.shuffle(firstLine);
		for (Troop aTroop : firstLine) {
			Logger.finer("aTroop: " + aTroop.getUniqueShortName());
			if (opponentHandler.noOpponent(aTroop)){
				Logger.finer("aTroop has no opponent");
				Troop newOpponent = smallerBG.findSmallerOpponent(opponentHandler, aTroop.getTargetingType());
				if (newOpponent != null){
					Logger.finer("aTroop new opponent: " + newOpponent.getUniqueShortName());
					opponentHandler.addOpponents(newOpponent,aTroop);
				}
			}
		}
	}
	
	public Troop findSmallerOpponent(OpponentHandler opponentHandler, TroopTargetingType targetType){
    	Logger.finer("findSmallerOpponent");
		Troop found = null;
		Troop firstTroopWithNoOpponentButWrongTypeOfTroop = null;
		Collections.shuffle(firstLine);
		int counter = 0;
		while ((found == null) & (counter < firstLine.size())){
	    	Logger.finer("counter: " + counter);
			Troop firstLineTroop = firstLine.get(counter);
	    	Logger.finer("firstLineTroop: " + firstLineTroop.getUniqueShortName());
			if (opponentHandler.maxOneOpponent(firstLineTroop)){
		    	Logger.finer("maxOneOpponent=true ");
		    	if(targetType.equals(TroopTargetingType.ALLROUND)){
		    		found = firstLineTroop;
				} else if (targetType.equals(TroopTargetingType.ANTIINFANTRY)){
					if(firstLineTroop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.INFANTRY)){
						found = firstLineTroop;
					}else{
						if(firstTroopWithNoOpponentButWrongTypeOfTroop == null){
							firstTroopWithNoOpponentButWrongTypeOfTroop = firstLineTroop;
						}
					}
				}else{// ANTITANK
					if(firstLineTroop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.ARMORED)){
						found = firstLineTroop;
					}else{
						if(firstTroopWithNoOpponentButWrongTypeOfTroop == null){
							firstTroopWithNoOpponentButWrongTypeOfTroop = firstLineTroop;
						}
					}
				}
		    	
			}
			counter++;
			
		}
		
		if(found == null && firstTroopWithNoOpponentButWrongTypeOfTroop != null){
			found = firstTroopWithNoOpponentButWrongTypeOfTroop;
		}
		
		return found;
	}

	private boolean findTroop(Troop aTroop, List<Troop> aList){
		boolean found = false;
		int counter = 0;
		while (!found & (counter < aList.size())){
			Troop listTroop = aList.get(counter);
			if (listTroop == aTroop){
				found = true;
			}else{
				counter++;
			}
		}
		return found;
	}

	public void addAsOpposingFirstLine(Troop aTroop, OpponentHandler opponentHandler){
		Collections.shuffle(firstLine);
		boolean found = false;
		int counter = 0;
		Troop firstTroopWithNoOpponentButWrongTypeOfTroop = null;
		while (!found & (counter < firstLine.size())){
			Troop firstLineTroop = firstLine.get(counter);
			if (opponentHandler.noOpponent(firstLineTroop)){
				if(aTroop.getTargetingType().equals(TroopTargetingType.ALLROUND)){
					opponentHandler.addOpponents(aTroop, firstLineTroop);
					found = true;
				} else if (aTroop.getTargetingType().equals(TroopTargetingType.ANTIINFANTRY)){
					if(firstLineTroop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.INFANTRY)){
						opponentHandler.addOpponents(aTroop, firstLineTroop);
						found = true;
					}else{
						if(firstTroopWithNoOpponentButWrongTypeOfTroop == null){
							firstTroopWithNoOpponentButWrongTypeOfTroop = firstLineTroop;
						}
					}
				}else{// ANTITANK
					if(firstLineTroop.getTroopType().getTypeOfTroop().equals(TypeOfTroop.ARMORED)){
						opponentHandler.addOpponents(aTroop, firstLineTroop);
						found = true;
					}else{
						if(firstTroopWithNoOpponentButWrongTypeOfTroop == null){
							firstTroopWithNoOpponentButWrongTypeOfTroop = firstLineTroop;
						}
					}
				}
			}
			counter++;
		}
		if(!found && firstTroopWithNoOpponentButWrongTypeOfTroop != null){
			opponentHandler.addOpponents(aTroop, firstTroopWithNoOpponentButWrongTypeOfTroop);
		}
	}
	
	public List<Troop> getSupportWithMaxOneOpponent(OpponentHandler opponentHandler){
		return getTroopsWithMaxOneOpponent(support, opponentHandler);
	}

	public List<Troop> getTroopsWithMaxOneOpponent(List<Troop> troopList, OpponentHandler opponentHandler){
		List<Troop> maxOneList = new LinkedList<Troop>();
		for (Troop aTroop : troopList) {
			if (opponentHandler.maxOneOpponent(aTroop)){
				maxOneList.add(aTroop);
			}
		}
		return maxOneList;
	}

	/**		
	 *     Först ställs trupperna upp:
    		Explicita order till enheter:
    		-first line
    		-reserve (kan tilldelas slumpvis om styrkan är större än motståndaren, överblivna firstline enheter kan inte slåss mot support eller flankers om de inte är i dubbla antal.)
    		-flanker (anfaller motståndans fria falnkers o annars försöker de anfalla motståndarens support) (kan ha default sann)
    		-support/artillery/AA (skyddas av first line + flankers mot andra ) (kan ha default sann)
    		Saknas order så har trupperna ev. defaultorder.

	 */
	public void performLineup(){
		for (Troop troop : troops) {
			if (troop.getPosition() == BattleGroupPosition.FIRST_LINE){
				Logger.finer(troop.getUniqueShortName() + ": BattleGroupPosition.FIRST_LINE");
				firstLine.add(troop);
			}else
			if (troop.getPosition() == BattleGroupPosition.FLANKER){
				flankers.add(troop);
			}else
			if (troop.getPosition() == BattleGroupPosition.RESERVE){
				reserve.add(troop);
			}else{
				support.add(troop);
			}
		}
	}

	/**		
    		Om en styrka är mindre än hälften i first line än motståndaren så används ev. flankers för att fylla på first line.
    		Om ena sidan efter detta fortfarande har mindre än hälften så många first line så används support troops för att fylla på first line.

	 * @param otherBattleGroup
	 */
	public void modifyLineup(LandBattleGroup otherBattleGroup){
		int nrToMove;
		if (otherBattleGroup.getNrFirstLine() > getNrFirstLine()){
			nrToMove = otherBattleGroup.getNrFirstLine() - getNrFirstLine();
			// use reserves to strengthen first line
			moveToFirstLine(reserve,nrToMove);
		}
		if ((otherBattleGroup.getNrFirstLine() == getNrFirstLine()) & (otherBattleGroup.getNrReserves() > 0) & (getNrReserves() > 0)){
			// move an equal nr of reserves to first line until (at least) one BG have no reserves left
			int moveNr = Math.min(otherBattleGroup.getNrReserves(),getNrReserves());
			otherBattleGroup.moveReservesToFirstLine(moveNr);
			moveReservesToFirstLine(moveNr);
		}else
		if (otherBattleGroup.getNrFirstLine() > (getNrFirstLine()*2)){
			nrToMove = getNrToMove(otherBattleGroup.getNrFirstLine(),getNrFirstLine());
			// use flankers to strengthen first line
			moveToFirstLine(flankers,nrToMove);
			if (otherBattleGroup.getNrFirstLine() > (getNrFirstLine()*2)){
				nrToMove = getNrToMove(otherBattleGroup.getNrFirstLine(),getNrFirstLine());
				// use support to strengthen first line
				moveToFirstLine(support,nrToMove);
				
			}
		}
	}
	
	public List<Troop> getUnopposedFlankers(OpponentHandler opponentHandler){
		return getUnopposedTroops(flankers, opponentHandler);
	}

	public List<Troop> getUnopposedSupport(OpponentHandler opponentHandler){
		return getUnopposedTroops(support, opponentHandler);
	}

	private List<Troop> getUnopposedTroops(List<Troop> troopList, OpponentHandler opponentHandler){
		List<Troop> unopposedFlankers = new LinkedList<Troop>();
		for (Troop aTroop : troopList) {
			if (opponentHandler.noOpponent(aTroop)){
				unopposedFlankers.add(aTroop);
			}
		}
		return unopposedFlankers;
	}

	public List<Troop> getUnstabbedFirstLine(LandBattleGroup flankingBG, OpponentHandler opponentHandler){
		List<Troop> unstabbedFirstLine = new LinkedList<Troop>();
		for (Troop aTroop : firstLine) {
			if (getNrStabbers(aTroop, flankingBG, opponentHandler) == 0){
				unstabbedFirstLine.add(aTroop);
			}
		}
		return unstabbedFirstLine;
	}

	public List<Troop> getMaxOneStabberFirstLine(LandBattleGroup flankingBG, OpponentHandler opponentHandler){
		List<Troop> maxOneStabberFirstLine = new LinkedList<Troop>();
		for (Troop aTroop : firstLine) {
			if (getNrStabbers(aTroop, flankingBG, opponentHandler) <= 1){
				maxOneStabberFirstLine.add(aTroop);
			}
		}
		return maxOneStabberFirstLine;
	}

	private int getNrStabbers(Troop aTroop, LandBattleGroup flankingBG, OpponentHandler opponentHandler){
		int found = 0;
		List<Troop> opponents = opponentHandler.getOpponents(aTroop);
		for (Troop otherTroop : opponents) {
			if (flankingBG.isFlanker(otherTroop)){
				found++;
			}
		}
		return found;
	}
	
	private boolean isFlanker(Troop aTroop){
		return flankers.contains(aTroop);
	}

	private boolean isFirstLine(Troop aTroop){
		return firstLine.contains(aTroop);
	}

	/**
	 * If smaller is less than half than larger, return larger-(smaller*2)/2 rounded up.
	 * @param larger
	 * @param smaller
	 * @return
	 */
	private int getNrToMove(int larger, int smaller){
		int nrToMove = 0;
		int temp = larger - (smaller*2);
		if (temp > 0){
			if (temp%2 > 0){
				nrToMove = temp/2 + 1;
			}else{
				nrToMove = temp/2;
			}
		}
		return nrToMove;
	}

	public void moveReservesToFirstLine(int nrToMove){
		moveToFirstLine(reserve, nrToMove);
	}
	
	private void moveToFirstLine(List<Troop> someTroops, int nrToMove){
		int counter = nrToMove;
		while ((someTroops.size() > 0) & (counter > 0)){
			Collections.shuffle(someTroops);
			Troop troopToMove = someTroops.get(0);
			firstLine.add(troopToMove);
			someTroops.remove(troopToMove);
			counter--;
		}
	}

	public int getNrFirstLine(){
		return firstLine.size();
	}

	public int getNrFlankers(){
		return flankers.size();
	}

	public int getNrSupport(){
		return support.size();
	}

	public int getTotalNrTroops(){
		return troops.size();
	}

	public int getNrUnopposedFlankers(OpponentHandler opponentHandler){
		return getUnopposedFlankers(opponentHandler).size();
	}

	public int getNrReserves(){
		return reserve.size();
	}

	public List<Troop> getFirstLine() {
		return firstLine;
	}

	public List<Troop> getFlankers() {
		return flankers;
	}

	public List<Troop> getReserve() {
		return reserve;
	}

	public List<Troop> getSupport() {
		return support;
	}

	public List<Troop> getTroops() {
		return troops;
	}
}
