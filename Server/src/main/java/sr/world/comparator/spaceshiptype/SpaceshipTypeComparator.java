/*
 * Created on 2005-aug-26
 */
package sr.world.comparator.spaceshiptype;

import java.util.Comparator;

import sr.world.SpaceshipType;

/**
 * @author WMPABOD
 *
 * Compares two spaceships, biggest/most expensive first
 */
public class SpaceshipTypeComparator implements Comparator<SpaceshipType> {
    static final long serialVersionUID = 1L;

	public int compare(SpaceshipType sst1, SpaceshipType sst2) {
		int diff = 0;
		// defence stations first
		if (diff == 0){
			if (sst1.isDefenceShip() & !sst2.isDefenceShip()){
				diff = -1;
			}else
			if (!sst1.isDefenceShip() & sst2.isDefenceShip()){
				diff = 1;
			}
		}
		// else civilian ships first
		if (diff == 0){
			if (sst1.isCivilian() & !sst2.isCivilian()){
				diff = -1;
			}else
			if (!sst1.isCivilian() & sst2.isCivilian()){
				diff = 1;
			}
		}
		// else squadrons first
		if (diff == 0){
			if (sst1.isSquadron() & !sst2.isSquadron()){
				diff = -1;
			}else
			if (!sst1.isSquadron() & sst2.isSquadron()){
				diff = 1;
			}
		}
		// else smallest first
		if (diff == 0){
			diff = sst1.getTonnage() - sst2.getTonnage();
		}
		// else lowest build cost first
		if (diff == 0){
			diff = sst1.getBuildCost(null) - sst2.getBuildCost(null);
		}
		// else lowest support cost first
		if (diff == 0){
			diff = sst1.getUpkeep() - sst2.getUpkeep();
		}
		// else sort on name
		if (diff == 0){
			diff = sst1.getName().compareTo(sst2.getName());
		}
		return diff;
	}

}
