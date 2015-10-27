package sr.enums;

public enum InitiativeMethod {
	// examples below, 4 capital ships vs 1 capital ship has x% chance of winning the initiative
	LINEAR("Linear"), // x = 80%
	WEIGHTED("Weighted (base 0)"), // x = 75%
	WEIGHTED_1("Weighted (base 1)"), // x = 66.7%
	WEIGHTED_2("Weighted (base 2)"), // x = 62.5%
	WEIGHTED_3("Weighted (base 3)"), // x = 60%
	FIFTY_FIFTY("Fifty-fifty"); // x = 50%
	
	private String desc;
	
	private InitiativeMethod(String aDesc){
		this.desc = aDesc;
	}
	
	@Override
	public String toString(){
		return desc;
	}
	
}

