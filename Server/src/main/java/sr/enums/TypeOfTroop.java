package sr.enums;


public enum TypeOfTroop {
	INFANTRY("Infantry"),
	ARMORED("Armored"),
	SUPPORT("Support");
	
	private String retStr;
	
	private TypeOfTroop(String aRetStr){
		this.retStr = aRetStr;
	}
	
	public String toString(){
		return retStr;
	}

}
