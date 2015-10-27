package sr.world.comparator;

import java.util.Comparator;

import sr.world.Building;
import sr.world.BuildingType;

/**
 * Sorts building in this order:
 * 1. Shipbuilder
 * 2. VIP builder
 * 3. Name alfabetically
 * 
 * @author Paul Bodin
 */
public class BuildingTypeSizeAndNameComparator implements Comparator<Building>{
	
	static final long serialVersionUID = 1L;

	public int compare(Building b1, Building b2) {
		BuildingType bt1 = b1.getBuildingType();
		BuildingType bt2 = b2.getBuildingType();
		int diff = 0;
		if (bt1.isShipBuilder() & !bt2.isShipBuilder()){
			diff = -1;
		}else
		if (!bt1.isShipBuilder() & bt2.isShipBuilder()){
			diff = 1;
		}else
		if (bt1.isVIPBuilder() & !bt2.isVIPBuilder()){
				diff = -1;
		}else
		if (!bt1.isVIPBuilder() & bt2.isVIPBuilder()){
				diff = 1;
		}else{
			diff = bt1.getName().compareTo(bt2.getName());
		}
		return diff;
	}

}
