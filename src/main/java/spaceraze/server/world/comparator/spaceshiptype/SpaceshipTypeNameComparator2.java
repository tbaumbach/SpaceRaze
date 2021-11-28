/*
 * Created on 2005-aug-26
 */
package spaceraze.server.world.comparator.spaceshiptype;

import java.util.Comparator;

import spaceraze.world.SpaceshipType;

/**
 * @author WMPABOD
 *
 * Compares two spaceships lexigraphically on name
 */
public class SpaceshipTypeNameComparator2 implements Comparator<SpaceshipType> {
    static final long serialVersionUID = 1L;

	public int compare(SpaceshipType sst1, SpaceshipType sst2) {
		// sort on name
		int diff = sst1.getName().compareTo(sst2.getName());
		return diff;
	}

}
