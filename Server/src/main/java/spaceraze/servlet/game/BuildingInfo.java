package spaceraze.servlet.game;

import spaceraze.world.Building;

public class BuildingInfo {

	private String name, type;
	private int id;
	
	BuildingInfo(Building building){
		type = building.getBuildingType().getName();
		id = building.getUniqueId();
		name = building.getUniqueName();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getId() {
		return id;
	}
}
