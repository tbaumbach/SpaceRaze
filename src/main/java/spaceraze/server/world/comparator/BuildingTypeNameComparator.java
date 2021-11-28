package spaceraze.server.world.comparator;

import java.util.Comparator;

import spaceraze.world.BuildingType;

/**
 * Sorts building on name alfabetically
 * 
 * @author Paul Bodin
 */
public class BuildingTypeNameComparator implements Comparator<BuildingType>{
	
	static final long serialVersionUID = 1L;

	public int compare(BuildingType bt1, BuildingType bt2) {
		int diff = bt1.getName().compareTo(bt2.getName());
		return diff;
	}

}
