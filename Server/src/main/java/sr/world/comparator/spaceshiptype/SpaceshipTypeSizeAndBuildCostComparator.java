/*
 * Created on 2005-aug-26
 */
package sr.world.comparator.spaceshiptype;

import java.util.Comparator;

import sr.world.SpaceshipType;

/**
 * @author WMPABOD
 *
 * Compares two spaceships:
 * 1. size
 * 2. build cost
 * 3. name
 * 
 */
public class SpaceshipTypeSizeAndBuildCostComparator implements Comparator<SpaceshipType> {
    static final long serialVersionUID = 1L;

	public int compare(SpaceshipType sst1, SpaceshipType sst2) {
		int diff = sst2.getTonnage() - sst1.getTonnage();
		if (diff == 0){
			SpaceshipTypeBuildCostComparator stbcc = new SpaceshipTypeBuildCostComparator();
			diff = stbcc.compare(sst1,sst2);
		}
		return diff;
	}

}
