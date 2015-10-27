/*
 * Created on 2005-aug-26
 */
package sr.world.comparator;

import java.util.Comparator;

import sr.world.Alignment;

/**
 * @author WMPABOD
 *
 * Compares two alignments alfanumerically
 */
public class AlignmentNameComparator<T extends Alignment> implements Comparator<T> {
    static final long serialVersionUID = 1L;

	public int compare(T a1, T a2) {
		return a1.getName().compareTo(a2.getName());
	}

}
