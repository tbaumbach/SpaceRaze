package sr.enums;

import sr.general.logging.Logger;

public enum DiplomacyLevel {
	ETERNAL_WAR("Eternal War","Permanent","When at eternal war units between your and the other player will always attack each other, and neither of you can ever change diplomacy toward each other again."),
	WAR("War","Stable","When at war units between your and the other player will always attack each other."),
	CEASE_FIRE("Cease Fire","Unstable","During a cease fire your units will only attack the other player at your own planets (and vice versa). If such a combat occurs diplomacy between you and that player will be changed to war the next turn."),
	PEACE("Peace","Unstable","In peace you and the other players units will not attack each other. But if any of your ships travel to one of the other players planets (and vice versa) diplomacy between you and that player will be changed to cease fire the next turn."),
	ALLIANCE("Alliance","Stable","In an alliance you and the other players units will never attack each other."),
	CONFEDERACY("Confederacy","Permanent","In a confederacy you and the other players units will never attack each other, and neither of you can ever change diplomacy toward each other again."),
	VASSAL("Vassal","Permanent","As a vassal you and the lord's units will never attack each other, and neither of you can ever change diplomacy toward each other again. The Lord player can set a tax that will be deducted from your income each turn and given to the lord player."),
	LORD("Lord","Permanent","As a Lord you and the vassal's units will never attack each other, and neither of you can ever change diplomacy toward each other again. You can set a tax that will be deducted from the vassal's income each turn and given to you.");
	
	private String name,durability,desc;
	
	private DiplomacyLevel(String aName, String aDurability, String aDesc){
		this.name = aName;
		this.durability = aDurability;
		this.desc = aDesc;
	}
	
	// if ewar return ewar
	public DiplomacyLevel getNextLowerLevel(){
		int nextLower = ordinal() - 1;
		if (nextLower < 0){
			nextLower = 0;
		}
		return values()[nextLower];
	}

	@Override
	public String toString(){
		return name;
	}
	
	public boolean isBetweenInclusive(DiplomacyLevel low, DiplomacyLevel high){
		boolean between = false;
		int lowIndex = low.ordinal();
		int highIndex = high.ordinal();
		if ((ordinal() >= lowIndex) & (ordinal() <= highIndex)){
			between = true;
		}
		return between;
	}
	
	/**
	 * Retruns true if the wo levels are adjacent on the list
	 * @param anotherLevel
	 * @return
	 */
	public boolean isAdjacent(DiplomacyLevel anotherLevel){
		return (ordinal()+1 == anotherLevel.ordinal()) | ordinal()-1 == anotherLevel.ordinal();
	}

	public boolean isHigher(DiplomacyLevel anotherLevel){
		Logger.finest(ordinal() + " > " + anotherLevel.ordinal());
		return ordinal() > anotherLevel.ordinal();
	}

	public boolean isHigherOrEqual(DiplomacyLevel anotherLevel){
		Logger.finest(ordinal() + " >= " + anotherLevel.ordinal());
		return ordinal() >= anotherLevel.ordinal();
	}

	public boolean isLower(DiplomacyLevel anotherLevel){
		return ordinal() < anotherLevel.ordinal();
	}

	public boolean isLowerOrEqual(DiplomacyLevel anotherLevel){
		Logger.fine(ordinal() + " " + anotherLevel.ordinal());
		return ordinal() <= anotherLevel.ordinal();
	}

	public String getDesc() {
		return desc;
	}

	public String getName() {
		return name;
	}

	public String getDurability() {
		return durability;
	}
	
}

