package sr.world.incomeExpensesReports;

import java.io.Serializable;

import sr.general.logging.Logger;

/**
 * Contains information about one specific income
 * @author bodinp
 *
 */
public class IncomeReportRow implements Serializable {
    static final long serialVersionUID = 1L;
	private IncomeType type;
	private String desc,location;
	private int counter,value;
	
	public IncomeReportRow(IncomeType type, String desc, String location, int value, int counter){
		this.type = type;
		this.desc = desc;
		this.location = location;
		this.value = value;
		this.counter = counter;
		Logger.finer("counter: " + counter);
	}

	public String getDesc() {
		return desc;
	}

	public IncomeType getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public int getCounter() {
		return counter;
	}

	public String getLocation() {
		return location;
	}

}
