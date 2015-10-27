/*
 * Created on 2005-aug-26
 */
package sr.world.comparator.trooptype;

import java.util.Comparator;

/**
 * @author WMPABOD
 *
 * Compares two strings, and if one of them contains a "*" it is sorted
 * specially, otherwise sorts lexigraphically
 */
public class TroopTypeNameComparator implements Comparator<String> {
    static final long serialVersionUID = 1L;

	public int compare(String tt1, String tt2) {
		if(tt1.contains("*") && !tt2.contains("*")){
			return 1;
		}else if(!tt1.contains("*") && tt2.contains("*")){
			return -1;
		}
		return tt1.compareTo(tt2);
	}

}

/*
public class SpaceshipTypeNameComparator implements Comparator {
    static final long serialVersionUID = 1L;

	public int compare(Object arg0, Object arg1) {
		SpaceshipType sst1 = (SpaceshipType)arg0;
		SpaceshipType sst2 = (SpaceshipType)arg1;
		return sst1.getName().compareTo(sst2.getName());
	}

}
*/