package sr.enums;

public enum SpaceshipRange {
	NONE,
	SHORT,
	LONG;
	
	public String toString(){
		String retVal = "-";
		if (this == SHORT){
			retVal = "Short";
		}else
		if (this == LONG){
			retVal = "Long";
		}
		return retVal;
	}
	
	public String toLowercaseString(){
		return this.toString().toLowerCase();
	}
	
	public boolean greaterThan(SpaceshipRange otherRange){
		return this.compareTo(otherRange) > 0;
	}

	public boolean lesserThan(SpaceshipRange otherRange){
		return this.compareTo(otherRange) < 0;
	}
	
	public boolean canMove(){
		return this != NONE;
	}
	
}
