/*
 * Created on 2005-aug-26
 */
package sr.world.comparator;

import java.util.Comparator;

import sr.world.Planet;


public class PlanetProdComparator implements Comparator<Planet> {
    static final long serialVersionUID = 1L;

	public int compare(Planet p1, Planet p2) {
		int diff = p2.getPopulation() - p1.getPopulation();		
		return diff;
	}

}
