/*
 * Created on 2005-aug-26
 */
package spaceraze.server.world.comparator.spaceshiptype;

import java.util.Comparator;

/**
 * @author WMPABOD
 *
 * Compares two spaceships, biggest/most expensive first
 */
public class SpaceshipTypeNameComparator implements Comparator<String> {
    static final long serialVersionUID = 1L;

	public int compare(String sst1, String sst2) {
		if(sst1.contains("*") && !sst2.contains("*")){
			return 1;
		}else if(!sst1.contains("*") && sst2.contains("*")){
			return -1;
		}
		return sst1.compareTo(sst2);
	}

}