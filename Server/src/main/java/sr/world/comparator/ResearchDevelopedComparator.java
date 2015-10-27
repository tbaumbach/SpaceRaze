/*
 * Created on 2005-aug-26
 */
package sr.world.comparator;

import java.util.Comparator;

import sr.world.ResearchAdvantage;


public class ResearchDevelopedComparator implements Comparator<ResearchAdvantage> {
    static final long serialVersionUID = 1L;

	public int compare(ResearchAdvantage ra1, ResearchAdvantage ra2) {
		int v1 = ra1.isDeveloped() ? 1 : 0;
		int v2 = ra2.isDeveloped() ? 1 : 0;
		int diff = v1 - v2;
		return diff;
	}

}
