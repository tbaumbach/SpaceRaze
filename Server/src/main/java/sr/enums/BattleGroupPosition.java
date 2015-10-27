package sr.enums;

public enum BattleGroupPosition {
	FIRST_LINE("First line","b"),
	RESERVE("Reserve","r"),
	FLANKER("Flanker","f"),
	SUPPORT("Support","s");
	
	private String text;
	private String abbreviation;
	
	private BattleGroupPosition(String aText, String anAbbreviation){
		text = aText;
		abbreviation = anAbbreviation;
	}
	
	@Override
	public String toString(){
		return text;
	}

	public String toShortString(){
		return abbreviation;
	}

	public static BattleGroupPosition getPosition(String positionName){
		System.out.println("positionName: " + positionName);
		BattleGroupPosition tempPosition = null;
		if (positionName.equals(BattleGroupPosition.FIRST_LINE.toString())){
			tempPosition = BattleGroupPosition.FIRST_LINE;
		}else
		if (positionName.equals(BattleGroupPosition.RESERVE.toString())){
			tempPosition = BattleGroupPosition.RESERVE;
		}else
		if (positionName.equals(BattleGroupPosition.FLANKER.toString())){
			tempPosition = BattleGroupPosition.FLANKER;
		}else
		if (positionName.equals(BattleGroupPosition.SUPPORT.toString() )){
			tempPosition = BattleGroupPosition.SUPPORT;
		}
		return tempPosition;
	}
}
