package sr.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class defines how much statistics should be shown to players during a game.
 *
 * @author bodinp
 */
public enum StatisticGameType implements Serializable{
	NONE("None"),
	PRODUCTION_ONLY("Production only"),
	ALL("All");

    static final long serialVersionUID = 1L;
	private String text;
	
	private StatisticGameType(String text){
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public static List<String> getTypeTexts(){
		List<String> texts = new LinkedList<String>();
		for (StatisticGameType aStatisticGameType : values()) {
			texts.add(aStatisticGameType.getText());
		}
		return texts;
	}
	
	public static StatisticGameType findStatisticGameType(String aText){
		StatisticGameType foundStatisticGameType = null;
		for (StatisticGameType aStatisticGameType : values()) {
			if (aStatisticGameType.toString().equals(aText)){
				foundStatisticGameType = aStatisticGameType;
			}
		}
		return foundStatisticGameType;
	}
}
