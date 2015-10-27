package sr.world.comparator;

import java.util.Comparator;

import sr.world.BuildingType;

public class BuildingTypeBuildCostAndNameComparator implements Comparator<BuildingType>{
	
	static final long serialVersionUID = 1L;

	public int compare(BuildingType arg0, BuildingType arg1) {
		int diff = arg1.getBuildCost(null) - arg0.getBuildCost(null);
		if (diff == 0){
			diff = arg0.getName().compareTo(arg1.getName());
		}
		return diff;
	}

}
