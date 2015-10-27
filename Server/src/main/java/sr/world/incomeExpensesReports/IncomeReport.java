package sr.world.incomeExpensesReports;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains all information about a players incomes for a specific turn
 * @author bodinp
 *
 */
public class IncomeReport implements Serializable {
    static final long serialVersionUID = 1L;
	private List<IncomeReportRow> incomeRows;
	private int counter; 
	
	public IncomeReport (){
		incomeRows = new LinkedList<IncomeReportRow>();
		counter = 1;
	}
	
	public void addNewRow(IncomeType type, String desc, String location, int value){
		IncomeReportRow newRow = new IncomeReportRow(type, desc, location, value, counter);
		incomeRows.add(newRow);
		counter++;
	}
	
	public List<IncomeReportRow> getRows(){
		return incomeRows;
	}
	
}
