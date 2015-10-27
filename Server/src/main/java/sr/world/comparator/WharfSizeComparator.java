package sr.world.comparator;

import java.util.Comparator;

import sr.world.Building;
import sr.world.BuildingType;

/**
 * Sorts building (wharves) in this order:
 * 1. Wharf slots
 * 
 * @author Paul Bodin
 */
public class WharfSizeComparator implements Comparator<Building>{	
	static final long serialVersionUID = 1L;

	public int compare(Building b1, Building b2) {
		BuildingType bt1 = b1.getBuildingType();
		BuildingType bt2 = b2.getBuildingType();
		int slots1 = bt1.getWharfSize();
		int slots2 = bt2.getWharfSize();
		int diff = slots2 - slots1;
		return diff;
	}

}
