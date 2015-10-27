package sr.world.diplomacy;

import java.io.Serializable;
import java.util.List;

import sr.enums.DiplomacyGameType;
import sr.enums.DiplomacyIconState;
import sr.enums.DiplomacyLevel;
import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Player;

public class DiplomacyState implements Serializable{
	private static final long serialVersionUID = 1L;
	private Player player1,player2,lord;
	private DiplomacyLevel currentLevel;
	private DiplomacyRelation relation;
	private boolean changedThisTurn;
	private int tax; // used by lords to set tax level on vassals
	
	public DiplomacyState(DiplomacyRelation aRelation, Player aPlayer1, Player aPlayer2){
		relation = aRelation;
		player1 = aPlayer1;
		player2 = aPlayer2;
		currentLevel = relation.getStartRelation();
	}
	
	public DiplomacyLevel getStartLevel(){
		return currentLevel;
	}
	
	public DiplomacyLevel getNextLowerLevel(){
		return currentLevel.getNextLowerLevel();
	}
	
	public void setCurrentLevel(DiplomacyLevel newLevel){
		currentLevel = newLevel;
	}
	
	public DiplomacyIconState getIconState(DiplomacyLevel iconLevel, Player thePlayer){
		Logger.finest("getIconState, iconLevel: " + iconLevel + ", thePlayer: " + thePlayer);
		DiplomacyIconState tmpIconState = null;
		Player otherPlayer = getOtherPlayer(thePlayer);
		Logger.finest("otherPlayer: " + otherPlayer);
		DiplomacyOffer ownOffer = thePlayer.getOrders().findDiplomacyOffer(otherPlayer); // offers som man själv har gjort detta drag
		DiplomacyOffer othersOffer = thePlayer.findDiplomacyOffer(otherPlayer); // offers som någon annan gjorde förra draget (och som man kan svara på)
		DiplomacyChange aChange = thePlayer.getOrders().findDiplomacyChange(otherPlayer); // changes som man själv har gjort detta drag
		boolean ownOfferExist = false;
		if (ownOffer != null){
			ownOfferExist = ownOffer.isLevel(iconLevel);
		}
		boolean othersOfferExist = false;
		if (othersOffer != null){
			othersOfferExist = othersOffer.isLevel(iconLevel);
		}
		boolean changeExist = false;
		if (aChange != null){
			changeExist = aChange.isLevel(iconLevel);
		}
		boolean isVassal = thePlayer.getGalaxy().getDiplomacy().isVassal(thePlayer) != null;
		boolean isLord = thePlayer.getGalaxy().getDiplomacy().getVassalPlayers(thePlayer).size() > 0;
		boolean isInConf = thePlayer.getGalaxy().getDiplomacy().getConfederacyPlayers(thePlayer).size() > 0;
		// set icon state
		if (otherPlayer.isDefeated()){
			tmpIconState = DiplomacyIconState.DISABLED;
		}else
		// vid DiplomacyLevel.VASSAL måste man även kolla på om det finnns några
		if (iconLevel == DiplomacyLevel.LORD){
			// Kan påverkas av
			// - ownOffer på VASSAL
			// - aChange till VASSAL
			// - otherOffer på LORD
			boolean ownVassalOfferExist = false;
			if (ownOffer != null){
				ownVassalOfferExist = ownOffer.isLevel(DiplomacyLevel.VASSAL);
			}
			boolean changeVassalExist = false;
			if (aChange != null){
				changeVassalExist = aChange.isLevel(DiplomacyLevel.VASSAL);
			}
			if ((currentLevel == DiplomacyLevel.ETERNAL_WAR) | (currentLevel == DiplomacyLevel.CONFEDERACY)){
				tmpIconState = DiplomacyIconState.DISABLED;
			}else
			if ((currentLevel == DiplomacyLevel.LORD) | (currentLevel == DiplomacyLevel.VASSAL)){
				Logger.fine("iconLevel == currentLevel " + thePlayer.getName() + " " + lord);
				if (thePlayer == lord){
					tmpIconState = DiplomacyIconState.ACTIVE;					
				}else{
					tmpIconState = DiplomacyIconState.DISABLED;
				}
			}else
			if (isInConf | isVassal){
				tmpIconState = DiplomacyIconState.DISABLED;
			}else
			if (othersOfferExist){
				if (changeVassalExist){
					tmpIconState = DiplomacyIconState.PASSIVE_AND_SELECTED_AND_SUGGESTED;
				}else{
					tmpIconState = DiplomacyIconState.PASSIVE_AND_SUGGESTED;
				}
			}else
			if (ownVassalOfferExist){
				tmpIconState = DiplomacyIconState.PASSIVE_AND_SELECTED;
			}else{
				tmpIconState = DiplomacyIconState.PASSIVE;
			}
		}else
		if (iconLevel == DiplomacyLevel.VASSAL){
			// Kan påverkas av
			// - ownOffer på LORD
			// - aChange till LORD
			// - otherOffer på VASSAL
			boolean ownLordOfferExist = false;
			if (ownOffer != null){
				ownLordOfferExist = ownOffer.isLevel(DiplomacyLevel.LORD);
			}
			boolean changeLordExist = false;
			if (aChange != null){
				changeLordExist = aChange.isLevel(DiplomacyLevel.LORD);
			}
			if ((currentLevel == DiplomacyLevel.ETERNAL_WAR) | (currentLevel == DiplomacyLevel.CONFEDERACY)){
				tmpIconState = DiplomacyIconState.DISABLED;
			}else
				if ((currentLevel == DiplomacyLevel.LORD) | (currentLevel == DiplomacyLevel.VASSAL)){
				if (thePlayer != lord){
					tmpIconState = DiplomacyIconState.ACTIVE;					
				}else{
					tmpIconState = DiplomacyIconState.DISABLED;
				}
			}else
			if (isInConf | isVassal | isLord){
				tmpIconState = DiplomacyIconState.DISABLED;
			}else
			if (othersOfferExist){
				if (changeLordExist){
					tmpIconState = DiplomacyIconState.PASSIVE_AND_SELECTED_AND_SUGGESTED;
				}else{
					tmpIconState = DiplomacyIconState.PASSIVE_AND_SUGGESTED;
				}
			}else
			if (ownLordOfferExist){
				tmpIconState = DiplomacyIconState.PASSIVE_AND_SELECTED;
			}else{
				tmpIconState = DiplomacyIconState.PASSIVE;
			}
		}else{ // annan level än lord/vasall
			if (iconLevel == currentLevel){
				tmpIconState = DiplomacyIconState.ACTIVE;
			}else
			if ((currentLevel == DiplomacyLevel.LORD) | (currentLevel == DiplomacyLevel.VASSAL) | (currentLevel == DiplomacyLevel.ETERNAL_WAR) | (currentLevel == DiplomacyLevel.CONFEDERACY)){
				tmpIconState = DiplomacyIconState.DISABLED;
			}else
			if ((iconLevel == DiplomacyLevel.CONFEDERACY) & confNotPossible(thePlayer,otherPlayer)){
				tmpIconState = DiplomacyIconState.DISABLED;
			}else
			if ((iconLevel == DiplomacyLevel.CONFEDERACY) & (isVassal | isLord)){
				tmpIconState = DiplomacyIconState.DISABLED;
			}else
			if (othersOfferExist){
				if (changeExist){
					tmpIconState = DiplomacyIconState.PASSIVE_AND_SELECTED_AND_SUGGESTED;
				}else{
					tmpIconState = DiplomacyIconState.PASSIVE_AND_SUGGESTED;
				}
			}else
			if (ownOfferExist | changeExist){
				tmpIconState = DiplomacyIconState.PASSIVE_AND_SELECTED;
			}else{ // check between...
				if (iconLevel.isBetweenInclusive(relation.getLowestRelation(), relation.getHighestRelation())){
					tmpIconState = DiplomacyIconState.PASSIVE;
				}else{
					tmpIconState = DiplomacyIconState.DISABLED;
				}
			}
		}
		return tmpIconState;
	}
	
	private boolean confNotPossible(Player thePlayer, Player otherPlayer){
		Logger.finer("thePlayer, otherPlayer: " + thePlayer.getGovenorName() + ", " + otherPlayer.getGovenorName());
		boolean confImpossible = false;
		Galaxy g = thePlayer.getGalaxy();
		Diplomacy d = g.getDiplomacy();
		List<DiplomacyState> confStates1 = d.getConfederacyStates(thePlayer);
		List<DiplomacyState> confStates2 = d.getConfederacyStates(otherPlayer);
		// check if both players are in a confederacy
		if ((confStates1.size() > 0) & (confStates2.size() > 0)){ // both players are in a confederacy
			confImpossible = true;
		}else
		// check if thePlayer is a member of a confederacy, and something hinders that thePlayer joins that confederacy
		if (confStates1.size() > 0){ // thePlayer is in a confederacy
			for (DiplomacyState confState : confStates1) {
				Player confPlayer = confState.getOtherPlayer(thePlayer);
				DiplomacyState otherState = d.getDiplomacyState(confPlayer, otherPlayer);
				if (otherState.getCurrentLevel() == DiplomacyLevel.ETERNAL_WAR){ // confPlayer is in eternal war with otherPlayer and can never join the confederacy
					confImpossible = true;
				}else
				if (otherState.relation.getHighestRelation() != DiplomacyLevel.CONFEDERACY){ // confPlayer can never conf with otherPlayer
					confImpossible = true;
				}
			}
		}else
		// check if otherPlayer is a member of a confederacy, and something hinders that thePlayer joins that confederacy
		if (confStates2.size() > 0){ // otherPlayer is in a confederacy
			Logger.finer("confStates2.size(): " + confStates2.size());
			for (DiplomacyState confState : confStates2) {
				Player confPlayer = confState.getOtherPlayer(otherPlayer);
				DiplomacyState otherState = d.getDiplomacyState(confPlayer, thePlayer);
				if (otherState.getCurrentLevel() == DiplomacyLevel.ETERNAL_WAR){ // confPlayer is in eternal war with thePlayer and can never join the confederacy
					confImpossible = true;
					Logger.finer("otherState.getCurrentLevel() == DiplomacyLevel.ETERNAL_WAR");
				}else
				if (otherState.relation.getHighestRelation() != DiplomacyLevel.CONFEDERACY){ // confPlayer can never conf with thePlayer
					confImpossible = true;
					Logger.finer("otherState.relation.getHighestRelation() != DiplomacyLevel.CONFEDERACY");
				}
			}
		}
		return confImpossible;
	}
	
	public Player getOtherPlayer(Player aPlayer){
		Player otherPlayer = player1;
		if (aPlayer == player1){
			otherPlayer = player2;
		}
		return otherPlayer;
	}
	
	public boolean isPlayers(Player aPlayer1,Player aPlayer2){
		boolean found = false;
		if ((player1 == aPlayer1) & (player2 == aPlayer2)){
			found = true;
		}else
		if ((player1 == aPlayer2) & (player2 == aPlayer1)){
			found = true;
		}
		return found;
	}

	public boolean isPlayer(Player aPlayer){
		boolean found = false;
		if ((player1 == aPlayer) | (player2 == aPlayer)){
			found = true;
		}
		return found;
	}

	@Override
	public String toString(){
		String retVal = "Diplomacy state: " + player1.getName() + " vs " + player2.getName() + ", current: " + currentLevel.toString() + ", relation: " + relation;
		if (lord != null){
			retVal += ", Lord: " + lord.getName();
		}
		if (tax > 0){
			retVal += ", Tax: " + tax;
		}
		return retVal;
	}
	
	public void modifyDueToGameType(DiplomacyGameType diplomacyGameType){
		if (diplomacyGameType == DiplomacyGameType.GAMEWORLD){
			// do nothing, kepp all settings
		}else
		if (diplomacyGameType == DiplomacyGameType.OPEN){
			relation.setHighestRelation(DiplomacyLevel.CONFEDERACY);
			relation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);
			if (relation.getFaction1() == relation.getFaction2()){
				relation.setStartRelation(DiplomacyLevel.PEACE);
				currentLevel = DiplomacyLevel.PEACE;
			}else{
				relation.setStartRelation(DiplomacyLevel.CEASE_FIRE);
				currentLevel = DiplomacyLevel.CEASE_FIRE;
			}
		}else
		if (diplomacyGameType == DiplomacyGameType.FACTION){
			if (relation.getFaction1() == relation.getFaction2()){
				relation.setHighestRelation(DiplomacyLevel.CONFEDERACY);
				relation.setLowestRelation(DiplomacyLevel.CONFEDERACY);
				relation.setStartRelation(DiplomacyLevel.CONFEDERACY);
				currentLevel = DiplomacyLevel.CONFEDERACY;
			}else{
				relation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
				relation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);
				relation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);
				currentLevel = DiplomacyLevel.ETERNAL_WAR;
			}
		}else
		if (diplomacyGameType == DiplomacyGameType.DEATHMATCH){
			relation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
			relation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);
			relation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);
			currentLevel = DiplomacyLevel.ETERNAL_WAR;
		}
	}

	public boolean isChangedThisTurn() {
		return changedThisTurn;
	}

	public void setChangedThisTurn(boolean changedThisTurn) {
		this.changedThisTurn = changedThisTurn;
	}

	public Player getLord() {
		return lord;
	}

	public void setLord(Player lord) {
		this.lord = lord;
	}

	public DiplomacyLevel getCurrentLevel() {
		return currentLevel;
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}
}
