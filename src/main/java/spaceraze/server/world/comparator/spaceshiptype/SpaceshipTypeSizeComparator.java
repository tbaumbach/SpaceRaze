/*
 * Created on 2005-aug-26
 */
package spaceraze.server.world.comparator.spaceshiptype;

import java.util.Comparator;

import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
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
		// smallest first
		if (diff == 0){
			diff = sst1.getSize().getCompareSize() - sst2.getSize().getCompareSize();
		}
		// else lowest build cost first
		if (diff == 0){
			diff = SpaceshipPureFunctions.getBuildCost(sst1, 0) - SpaceshipPureFunctions.getBuildCost(sst2, 0);
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
