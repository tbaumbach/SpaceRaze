package sr.world.diplomacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sr.world.Faction;

/**
 * Handles "GameWorld Diplomacy" relations in a gameworld
 * 
 * @author WMPABOD
 *
 */
public class GameWorldDiplomacy implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<DiplomacyRelation> allRelations;
	
	public GameWorldDiplomacy (){
		allRelations = new ArrayList<DiplomacyRelation>();
	}
	
	public List<DiplomacyRelation> getAllRelations(){
		return allRelations;
	}
	
	public void addDefaultRelation(Faction aFaction1, Faction aFaction2){
		DiplomacyRelation tmpDR = new DiplomacyRelation(aFaction1,aFaction2);
		allRelations.add(tmpDR);
	}
	
	public DiplomacyRelation getRelation(Faction aFaction1, Faction aFaction2){
		DiplomacyRelation foundDR = null;
		int counter = 0;
		while ((counter < allRelations.size()) & (foundDR == null)) {
			DiplomacyRelation tmpDR = allRelations.get(counter);
			if (tmpDR.isRelation(aFaction1,aFaction2)){
				foundDR = tmpDR;
			}else{
				counter++;
			}
		}
		return foundDR;
	}
	
}
