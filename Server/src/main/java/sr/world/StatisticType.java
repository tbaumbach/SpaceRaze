package sr.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class defines the different types of statistics that is collected each trun of a game.
 *
 * @author bodinp
 * 
 */
public enum StatisticType implements Serializable{
	PRODUCTION_PLAYER("Player planet production"),
	PRODUCTION_FACTION("Faction planet production"),
	NET_INCOME("Net income"), // income after support, 
	SHIP_SIZE("Total ship size"),
	SHIP_NUMBER("# ships"),
	PLANETS("# planets"),
	VIPS("# VIPs"),
	TROOPS_NUMBER("# troop units"),
	SHIPS_KILLED("Ships destroyed"),
	SHIPS_LOST("Ships lost"),
	SHIPS_MOST_KILLS("Ships with most kills");

    static final long serialVersionUID = 1L;
	private String text;
	
	private StatisticType(String text){
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public static List<String> getTypeTexts(){
		List<String> texts = new LinkedList<String>();
		for (StatisticType aStatisticType : values()) {
			texts.add(aStatisticType.getText());
		}
		return texts;
	}
	
	public static StatisticType findStatisticType(String aText){
		StatisticType foundStatisticType = null;
		for (StatisticType aStatisticType : values()) {
			if (aStatisticType.getText().equals(aText)){
				foundStatisticType = aStatisticType;
			}
		}
		return foundStatisticType;
	}
}
