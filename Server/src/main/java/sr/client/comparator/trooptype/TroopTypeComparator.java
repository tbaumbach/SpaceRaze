/*
 * Created on 2005-aug-26
 */
package sr.world.comparator.trooptype;

import java.util.Comparator;

import sr.world.TroopType;

/**
 * @author WMPABOD
 *
 * Compares two spaceships, biggest/most expensive first
 */
public class TroopTypeComparator implements Comparator<TroopType> {
    static final long serialVersionUID = 1L;

	public int compare(TroopType tt1, TroopType tt2) {
		int diff = 0;
		// lowest build cost first
		if (diff == 0){
			diff = tt2.getCostBuild(null) - tt1.getCostBuild(null);
		}
		// else lowest support cost first
		if (diff == 0){
			diff = tt2.getUpkeep() - tt1.getUpkeep();
		}
		// else sort on name
		if (diff == 0){
			diff = tt2.getUniqueName().compareTo(tt1.getUniqueName());
		}
		return diff;
	}

}
