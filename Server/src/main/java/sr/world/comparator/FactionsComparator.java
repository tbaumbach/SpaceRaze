/*
 * Created on 2005-aug-26
 */
package sr.world.comparator;

import java.util.Comparator;

import sr.world.Faction;

/**
 * @author WMPABOD
 *
 * Compares two factions alfanumerically
 */
public class FactionsComparator implements Comparator<Faction> {
    static final long serialVersionUID = 1L;

	public int compare(Faction f1, Faction f2) {
		return f1.getName().compareToIgnoreCase(f2.getName());
	}

}
