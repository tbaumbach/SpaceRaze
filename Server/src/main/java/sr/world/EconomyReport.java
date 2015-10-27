/*
 * Created on 2005-jun-02
 */
package sr.world;

import java.io.Serializable;

/**
 * @author WMPABOD
 *
 * This class encapsulates information about economy key numbers for one turn
 */
public class EconomyReport implements Serializable{
	private static final long serialVersionUID = 1L;
	private int savedLastTurn,incomeLastTurn,expensesLastTurn,supportLastTurn,savedNextTurn,incomeNextTurn,supportNextTurn;
	private int corruptionUpkeepLastTurn,corruptionIncomeLastTurn,corruptionUpkeepNextTurn,corruptionIncomeNextTurn; // lost to corruption
	private int supportTroopsLastTurn,supportTroopsNextTurn;
	private int supportVIPsLastTurn,supportVIPsNextTurn;
	
	private boolean broke = false;
	
	public int getExpensesLastTurn() {
		return expensesLastTurn;
	}
	
	public void setExpensesLastTurn(int expensesLastTurn) {
		this.expensesLastTurn = expensesLastTurn;
	}
	
	public int getIncomeLastTurn() {
		return incomeLastTurn;
	}
	
	public void setIncomeLastTurn(int incomeLastTurn) {
		this.incomeLastTurn = incomeLastTurn;
	}
	
	public int getIncomeNextTurn() {
		return incomeNextTurn;
	}
	
	public void setIncomeNextTurn(int incomeNextTurn) {
		this.incomeNextTurn = incomeNextTurn;
	}
	
	public int getSavedLastTurn() {
		return savedLastTurn;
	}
	
	public void setSavedLastTurn(int savedLastTurn) {
		this.savedLastTurn = savedLastTurn;
	}
	
	public int getSavedNextTurn() {
		return savedNextTurn;
	}
	
	public void setSavedNextTurn(int savedNextTurn) {
		this.savedNextTurn = savedNextTurn;
	}
	
	public int getSupportShipsLastTurn() {
		return supportLastTurn;
	}
	
	public void setSupportShipsLastTurn(int supportLastTurn) {
		this.supportLastTurn = supportLastTurn;
	}
	
	public int getSupportShipsNextTurn() {
		return supportNextTurn;
	}
	
	public void setSupportShipsNextTurn(int supportNextTurn) {
		this.supportNextTurn = supportNextTurn;
	}

	public boolean getBrokeNextTurn() {
		return broke;
	}
	
	public void setBrokeNextTurn(boolean broke) {
		this.broke = broke;
	}

	public int getCorruptionIncomeLastTurn() {
		return corruptionIncomeLastTurn;
	}

	public void setCorruptionIncomeLastTurn(int corruptionIncomeLastTurn) {
		this.corruptionIncomeLastTurn = corruptionIncomeLastTurn;
	}

	public int getCorruptionIncomeNextTurn() {
		return corruptionIncomeNextTurn;
	}

	public void setCorruptionIncomeNextTurn(int corruptionIncomeNextTurn) {
		this.corruptionIncomeNextTurn = corruptionIncomeNextTurn;
	}

	public int getCorruptionUpkeepShipsLastTurn() {
		return corruptionUpkeepLastTurn;
	}

	public void setCorruptionUpkeepShipsLastTurn(int corruptionUpkeepLastTurn) {
		this.corruptionUpkeepLastTurn = corruptionUpkeepLastTurn;
	}

	public int getCorruptionUpkeepShipsNextTurn() {
		return corruptionUpkeepNextTurn;
	}

	public void setCorruptionUpkeepShipsNextTurn(int corruptionUpkeepNextTurn) {
		this.corruptionUpkeepNextTurn = corruptionUpkeepNextTurn;
	}

	public int getSupportTroopsLastTurn() {
		return supportTroopsLastTurn;
	}

	public void setSupportTroopsLastTurn(int supportTroopsLastTurn) {
		this.supportTroopsLastTurn = supportTroopsLastTurn;
	}

	public int getSupportTroopsNextTurn() {
		return supportTroopsNextTurn;
	}

	public void setSupportTroopsNextTurn(int supportTroopsNextTurn) {
		this.supportTroopsNextTurn = supportTroopsNextTurn;
	}
	
	public int getSupportVIPsLastTurn() {
		return supportVIPsLastTurn;
	}
	
	public void setSupportVIPsLastTurn(int supportLastTurn) {
		this.supportVIPsLastTurn = supportLastTurn;
	}
	
	public int getSupportVIPsNextTurn() {
		return supportVIPsNextTurn;
	}

	public void setSupportVIPsNextTurn(int supportVIPsNextTurn) {
		this.supportVIPsNextTurn = supportVIPsNextTurn;
	}
	
}
