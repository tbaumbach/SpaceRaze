/*
 * Created on 2005-aug-26
 */
package spaceraze.server.world.comparator.spaceshiptype;

import java.util.Comparator;

import spaceraze.world.SpaceshipType;

/**
 * @author WMPABOD
 *
 * Compares two spaceships, biggest/most expensive first
 */
public class SpaceshipTypeSizeComparator implements Comparator<SpaceshipType> {
    static final long serialVersionUID = 1L;

	public int compare(SpaceshipType sst1, SpaceshipType sst2) {
		int diff = 0;
		// squadrons first
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
