package sr.world.spacebattle;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import sr.world.Player;

public class SpaceBattleReports implements Serializable{
    static final long serialVersionUID = 1L;
	private List<SpaceBattleReport> allBattles;
	
	public SpaceBattleReports(){
		allBattles = new LinkedList<SpaceBattleReport>();
	}
	
	public void addNewSpaceBattleReport(SpaceBattleReport aSpaceBattleReport){
		allBattles.add(aSpaceBattleReport);
	}
	
	public String getAsString(Player curPlayer, ReportLevel level){
		StringBuffer aBuffer = new StringBuffer();
		for (SpaceBattleReport aSpaceBattleReport : allBattles) {
			aBuffer.append(aSpaceBattleReport.getAsString(curPlayer,level) + "\n");
		}
		return aBuffer.toString();
	}
	
	public boolean battleExist(){
		return allBattles.size() > 0;
	}

}
