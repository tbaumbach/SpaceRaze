package sr.world.comparator;

import java.util.Comparator;

/**
 * @author WMPABOD
 *
 * Compares two viptypes, alfanumerically on name field
 */

public class VIPTypeNameComparator implements Comparator<String> {
    static final long serialVersionUID = 1L;

	public int compare(String v1, String v2) {
		if(v1.contains("*") && !v2.contains("*")){
			return 1;
		}else if(!v1.contains("*") && v2.contains("*")){
			return -1;
		}
		return v1.compareTo(v2);
	}

}
