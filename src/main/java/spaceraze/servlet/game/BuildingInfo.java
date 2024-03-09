package spaceraze.servlet.game;

import spaceraze.world.Building;

public class BuildingInfo {

//	private String name;
	private String typeKey;
	private String key;
	
	BuildingInfo(Building building){
		typeKey = building.getTypeUuid();
		key = building.getUuid();
//		name = building.getName();
	}

	public String getTypeKey() {
		return typeKey;
	}

	public String getKey() {
		return key;
	}
}