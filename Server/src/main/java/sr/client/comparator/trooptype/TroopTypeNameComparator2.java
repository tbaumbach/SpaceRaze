/*
 * Created on 2005-aug-26
 */
package sr.world.comparator.trooptype;

import java.util.Comparator;

import sr.world.TroopType;

/**
 * @author WMPABOD
 *
 * Compares two troop types and sorts them lexigraphically
 */
public class TroopTypeNameComparator2 implements Comparator<TroopType> {
    static final long serialVersionUID = 1L;

	public int compare(TroopType tt1, TroopType tt2) {
		// sort on name
		int diff = tt1.getUniqueName().compareTo(tt2.getUniqueName());
		return diff;
	}

}
