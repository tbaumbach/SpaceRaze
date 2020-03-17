/*
 * Created on 2005-aug-26
 */
package spaceraze.server.game;

import java.util.Comparator;

import spaceraze.world.Planet;

public class PlanetProdComparator implements Comparator<Planet> {

	public int compare(Planet p1, Planet p2) {
		return p2.getPopulation() - p1.getPopulation();
	}

}
