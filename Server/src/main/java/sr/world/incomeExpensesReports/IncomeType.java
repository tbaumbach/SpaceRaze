package sr.world.incomeExpensesReports;

import java.io.Serializable;

public enum IncomeType implements Serializable {
	PLANET("Planet"),
	OPEN_BONUS("Planet"),
	CLOSED_BONUS("Planet"),
	SHIP("Ship"),
	BUILDING("Building"),
	VIP("VIP");

	private String desc;
	
	private IncomeType(String desc){
		this.desc = desc;
	}
	
	public String getDesc(){
		return desc;
	}
	
}
