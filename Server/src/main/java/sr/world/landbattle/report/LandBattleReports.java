package sr.world.landbattle.report;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import sr.world.spacebattle.ReportLevel;

public class LandBattleReports implements Serializable{
    static final long serialVersionUID = 1L;
	private List<LandBattleReport> allBattles;
	
	public LandBattleReports(){
		allBattles = new LinkedList<LandBattleReport>();
	}
	
	public void addNewLandBattleReport(LandBattleReport aLandBattleReport){
		allBattles.add(aLandBattleReport);
	}
	
	public String getAsString(ReportLevel level){
		StringBuffer aBuffer = new StringBuffer();
		for (LandBattleReport aLandBattleReport : allBattles) {
			aBuffer.append(aLandBattleReport.getAsString(level) + "\n");
		}
		return aBuffer.toString();
	}
	
	public boolean battleExist(){
		return allBattles.size() > 0;
	}

}
