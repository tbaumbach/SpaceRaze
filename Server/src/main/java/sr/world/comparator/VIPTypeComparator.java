/*
 * Created on 2005-aug-26
 */
package sr.world.comparator;

import java.util.Comparator;

import sr.world.VIPType;

/**
 * @author WMPABOD
 *
 * Compares two viptypes, alfanumerically on name field
 */
public class VIPTypeComparator implements Comparator<VIPType> {
    static final long serialVersionUID = 1L;

	public int compare(VIPType vt1, VIPType vt2) {
		return vt1.getName().compareToIgnoreCase(vt2.getName());
	}

}
