package sr.enums;

public enum BlackMarketFrequency {
	VERY_COMMON (200,"Very Common"), 
	COMMON (100,"Common"),
	SOMEWHAT_UNCOMMON (50,"Somewhat Uncommon"), 
	UNCOMMON (30,"Uncommon"), 
	RARE (10,"rare"), 
	SOMEWHAT_RARE (3,"Somewhat RARE"), 
	VERY_RARE (1,"Very Rare"), 
	NEVER (0,"-"); 
	
	private int frequency;
	private String retStr;

	private BlackMarketFrequency(int aFrequency,String aRetStr){
		this.frequency = aFrequency;
		this.retStr = aRetStr;
	}

	public String toString(){
		return retStr;
	}

	public int getFrequency(){
		return frequency;
	}

}
