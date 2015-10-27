package sr.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import sr.general.logging.Logger;

/**
 * This class handles all statistics within a single game
 * The data is organized like this
 * - type
 * - player
 * - turn
 * @author bodinp
 *
 */
public class StatisticsHandler implements Serializable{
    static final long serialVersionUID = 1L;
	private List<Statistics> allStatistics;
	private StatisticGameType statisticGameType;
	
	public StatisticsHandler (Galaxy g,StatisticGameType statisticGameType){
		allStatistics = new LinkedList<Statistics>();
		this.statisticGameType = statisticGameType;
		Logger.fine("statisticGameType: " + statisticGameType);
		for (StatisticType aStatisticType : StatisticType.values()) {
			allStatistics.add(new Statistics(aStatisticType,g));
		}
	}
	
	public Statistics findStatistics(StatisticType aStatisticType){
		Statistics foundStatistics = null;
		int i = 0;
		while ((foundStatistics == null) & (i < allStatistics.size())){
			Statistics tmpStatistics = allStatistics.get(i);
			if (tmpStatistics.getStatisticType() == aStatisticType){
				foundStatistics = tmpStatistics;
			}else{
				i++;
			}
		}
		return foundStatistics;
	}
	
	/**
	 * Adds a statistics value to the right type.
	 * Turnnumber will be set automatically.
	 * @param aStatisticType
	 * @param aPlayerName
	 * @param value
	 * @param cumulative
	 */
	public void addStatistics(StatisticType aStatisticType, String aPlayerName, int value, boolean cumulative){
		Logger.finest(aStatisticType + " " + aPlayerName + " " + value);
		Statistics foundStatistics = findStatistics(aStatisticType);
		foundStatistics.addStatistics(aPlayerName,value,cumulative);
	}

	public List<Statistics> getStatistics(){
		return allStatistics;
	}

	public StatisticGameType getStatisticGameType() {
		return statisticGameType;
	}
	
}
