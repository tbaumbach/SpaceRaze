package sr.world.orders;

import java.io.Serializable;

import sr.enums.HighlightType;
import sr.general.logging.Logger;
import sr.world.Player;
import sr.world.TurnInfo;

public class ResearchOrder implements Serializable {
	 static final long serialVersionUID = 1L;
	 
	 private String advantageName;
	 private int cost;

	 public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public ResearchOrder(String name, int cost){
		 setAdvantageName(name);
		 setCost(cost);
	 }

	 public String getAdvantageName() {
		return advantageName;
	}

	public void setAdvantageName(String advantageName) {
		this.advantageName = advantageName;
	}
	
	public String getText(){
	    String returnString = "";
	    returnString = "Research on " + getAdvantageName();
	    return returnString;
	  }

	public void performResearch(TurnInfo ti, Player p){
		Logger.finest( "performResearch: " + advantageName + " player: " + p.getName());
	    p.getResearch().researchAdvantage(advantageName,ti,p);
	}
	
	public void addToHighlights(Player p, HighlightType type){
		p.addToHighlights(getAdvantageName(), type);
	}
	
}
