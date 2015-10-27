/*
 * Created on 2005-aug-26
 */
package sr.world.comparator.spaceship;

import java.util.Comparator;

import sr.world.Spaceship;

/**
 * @author WMPABOD
 *
 * Compares two spaceships, biggest/most expensive first
 */
public class SpaceshipTypeAndBuildCostComparator implements Comparator<Spaceship> {
    static final long serialVersionUID = 1L;

	public int compare(Spaceship ss1, Spaceship ss2) {
		int diff = 0;
		SpaceshipSizeAndBuildCostComparator sbcc = new SpaceshipSizeAndBuildCostComparator();
		if (ss1.isCapitalShip() & !ss2.isCapitalShip()){
			diff = -1;
		}else
		if (!ss1.isCapitalShip() & ss2.isCapitalShip()){
			diff = 1;
		}else
		if (ss1.isCapitalShip() & ss2.isCapitalShip()){
			diff = sbcc.compare(ss1,ss2);
		}else{
			// none of the ships are capital ships
			if (ss1.isSquadron() & !ss2.isSquadron()){
				diff = -1;
			}else
			if (!ss1.isSquadron() & ss2.isSquadron()){
				diff = 1;
			}else
			if (ss1.isSquadron() & ss2.isSquadron()){
				diff = sbcc.compare(ss1,ss2);
			}else{
				// none of the ships are squadrons
				if (ss1.isCivilian() & !ss2.isCivilian()){
					diff = -1;
				}else
				if (!ss1.isCivilian() & ss2.isCivilian()){
					diff = 1;
				}else
				if (ss1.isCivilian() & ss2.isCivilian()){
					diff = sbcc.compare(ss1,ss2);
				}else{
					// both of the ships must be defenders
					diff = sbcc.compare(ss1,ss2);
				}
			}
		}
		return diff;
	}

}
