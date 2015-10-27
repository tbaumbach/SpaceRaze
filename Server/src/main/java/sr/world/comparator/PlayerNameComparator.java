/*
 * Created on 2005-aug-26
 */
package sr.world.comparator;

import java.util.Comparator;

import sr.world.Player;

/**
 * @author WMPABOD
 *
 * Compares two players names alfanumerically
 */
public class PlayerNameComparator<T extends Player> implements Comparator<T> {

	public int compare(T p1, T p2) {
		return p1.getGovenorName().compareTo(p2.getGovenorName());
	}

}
