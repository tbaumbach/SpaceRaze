package sr.world.diplomacy;

import java.io.Serializable;

import sr.enums.DiplomacyLevel;
import sr.world.Faction;

/**
 * Handles diplomacy in gameworld and games
 * 
 * @author WMPABOD
 *
 */
public class DiplomacyRelation implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private Faction faction1,faction2;
	private DiplomacyLevel highestRelation,startRelation,lowestRelation;
	
	/**
	 * Create a default relation between two factions
	 * @param aFaction1 a faction
	 * @param aFaction2 another faction (can be same as faction1)
	 */
	public DiplomacyRelation(Faction aFaction1, Faction aFaction2){
		faction1 = aFaction1;
		faction2 = aFaction2;
		highestRelation = DiplomacyLevel.CONFEDERACY;
		lowestRelation = DiplomacyLevel.ETERNAL_WAR;
		if (faction1 == faction2){
			startRelation = DiplomacyLevel.PEACE;
		}else{
			startRelation = DiplomacyLevel.WAR;
		}
	}
	
	@Override
	public DiplomacyRelation clone(){
		DiplomacyRelation dr = null;
		try {
			dr = (DiplomacyRelation)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return dr;
	}
	
	@Override
	public String toString(){
		return "DiplomacyRelation (" + faction1.getName() + "-" + faction2.getName() + "), highest: " + highestRelation + ", lowest: " + lowestRelation + ", start: " + startRelation;
	}
	
	public boolean isRelation(Faction aFaction1,Faction aFaction2){
		boolean found = false;
		if ((faction1 == aFaction1) & (faction2 == aFaction2)){
			found = true;
		}else
		if ((faction1 == aFaction2) & (faction2 == aFaction1)){
			found = true;
		}
		return found;
	}

	public DiplomacyLevel getHighestRelation() {
		return highestRelation;
	}

	public void setHighestRelation(DiplomacyLevel highestRelation) {
		this.highestRelation = highestRelation;
	}

	public DiplomacyLevel getLowestRelation() {
		return lowestRelation;
	}

	public void setLowestRelation(DiplomacyLevel lowestRelation) {
		this.lowestRelation = lowestRelation;
	}

	public DiplomacyLevel getStartRelation() {
		return startRelation;
	}

	public void setStartRelation(DiplomacyLevel startRelation) {
		this.startRelation = startRelation;
	}

	public Faction getFaction1() {
		return faction1;
	}

	public Faction getFaction2() {
		return faction2;
	}
		
}
