package sr.enums;

import sr.general.logging.Logger;

public enum DiplomacyGameType {
	GAMEWORLD("gameworld","Gameworld"),
	OPEN("Open","Open"),
	FACTION("Faction","Faction Deathmatch"),
	DEATHMATCH("Deathmatch","Deathmatch");
	
	private String shortText;
	private String longText;
	
	private DiplomacyGameType(String aShortText, String aLongText){
		shortText = aShortText;
		longText = aLongText;
	}
	
	@Override
	public String toString(){
		return longText + "  diplomacy";
	}
	
	public String getLongText(){
		return longText;
	}

	public String getShortText(){
		return shortText;
	}

	public boolean equalsString(String aTypeName){
		return aTypeName.equalsIgnoreCase(shortText);
	}
	
	public static DiplomacyGameType get(int index){
		DiplomacyGameType[] values = values();
		return values[index];
	}

	public static DiplomacyGameType getType(String typeName){
		Logger.finer("typeName: " + typeName);
		DiplomacyGameType tempType = null;
		if (DiplomacyGameType.GAMEWORLD.equalsString(typeName)){
			tempType = DiplomacyGameType.GAMEWORLD;
		}else
		if (DiplomacyGameType.OPEN.equalsString(typeName)){
			tempType = DiplomacyGameType.OPEN;
		}else
		if (DiplomacyGameType.FACTION.equalsString(typeName)){
			tempType = DiplomacyGameType.FACTION;
		}else
		if (DiplomacyGameType.DEATHMATCH.equalsString(typeName)){
			tempType = DiplomacyGameType.DEATHMATCH;
		}
		return tempType;
	}
	
	public String checkMinimumNumberFactions(int nrSelectableFactionNames){
		String retVal = "";
		if (((this == DiplomacyGameType.FACTION) | (this == DiplomacyGameType.GAMEWORLD)) & nrSelectableFactionNames < 2){
			retVal = "At least 2 factions must be selectable";
		}else
		if (nrSelectableFactionNames < 1){
			retVal = "At least 1 faction must be selectable";
		}
		return retVal;
	}

}
