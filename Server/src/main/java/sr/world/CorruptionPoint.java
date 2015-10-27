package sr.world;

import java.io.Serializable;

/**
 * Handles one breakpoint for corruption.
 * Breakpoint are not cumulative
 * @author WMPABOD
 *
 */
public class CorruptionPoint implements Serializable{
	private static final long serialVersionUID = 1L;
	private int incomeLimit;
	private int corrutionPercentage;
	private double incomePercentage;
	private CorruptionPoint nextBreakpoint;

	public CorruptionPoint(int anIncomeLimit,int aCorrutionPercentage){
		incomeLimit = anIncomeLimit;
		corrutionPercentage = aCorrutionPercentage;
		incomePercentage = (100 - aCorrutionPercentage)/100.0;
	}
	
	public String getDescription(){
		StringBuffer sb = new StringBuffer();
		sb.append(incomeLimit);
		sb.append("=");
		sb.append(corrutionPercentage);
		sb.append("%");
		if (nextBreakpoint != null){
			sb.append(",");
			sb.append(nextBreakpoint.getDescription());
		}
		return sb.toString();
	}

	public int getCorrutionPercentage() {
		return corrutionPercentage;
	}

	public int getIncomeLimit() {
		return incomeLimit;
	}

	public void setNextBreakpoint(CorruptionPoint tmpNextBreakpoint){
		nextBreakpoint = tmpNextBreakpoint;
	}
	
	/**
	 * Compute income after corruption between this and the next breakpoint.
	 * If there is another breakpoint, call it and add it's income to the returned result.
	 * @param income
	 * @return
	 */
	public int getIncomeAfterCorruption(int anIncome){
		int incomeAfterCorruption = 0;
		if (nextBreakpoint != null){
			int nextLimit = nextBreakpoint.getIncomeLimit();
			if (anIncome < nextLimit){
				double tmpInc = (anIncome - incomeLimit) * incomePercentage;
				incomeAfterCorruption = (int)Math.round(tmpInc);
			}else{
				double tmpInc = (nextLimit - incomeLimit) * incomePercentage;
				incomeAfterCorruption = (int)Math.round(tmpInc);
				incomeAfterCorruption += nextBreakpoint.getIncomeAfterCorruption(anIncome);
			}
		}else{
			double tmpInc = (anIncome - incomeLimit) * incomePercentage;
			incomeAfterCorruption = (int)Math.round(tmpInc);
		}
		return incomeAfterCorruption;
	}

	public CorruptionPoint getNextBreakpoint() {
		return nextBreakpoint;
	}
}
