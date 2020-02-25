/*
 * Created on 2005-aug-26
 */
package sr.world.comparator;

import java.util.Comparator;

import sr.world.Planet;

/**
 * @author WMPABOD
 *
 * Compares two planets, lowest range to nearest planet from same faction first
 */
public class PlanetRangeComparator implements Comparator<Planet> {
    static final long serialVersionUID = 1L;

	public int compare(Planet p1, Planet p2) {
		int diff = p1.getRangeToClosestFriendly() - p2.getRangeToClosestFriendly();
		return diff;
	}

}
