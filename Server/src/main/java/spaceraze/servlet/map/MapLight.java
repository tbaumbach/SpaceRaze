package spaceraze.servlet.map;

import spaceraze.world.Map;

public class MapLight {
	
	private String fileName = null;
	private String name = null;
	private int nrOfPlanets;
	private String description = null;
	private int maxNrStartPlanets;
	private long version;
	
	public MapLight(){}
	
	public MapLight(Map map){
		
		fileName = map.getFileName();
		name = map.getNameFull();
		nrOfPlanets = map.getNrPlanets();
		description = map.getDescription();
		maxNrStartPlanets = map.getMaxNrStartPlanets();
		version = map.getVersionId();
	}

	public String getFileName() {
		return fileName;
	}

	public String getName() {
		return name;
	}

	public int getNrOfPlanets() {
		return nrOfPlanets;
	}

	public String getDescription() {
		return description;
	}

	public int getMaxNrStartPlanets() {
		return maxNrStartPlanets;
	}

	public long getVersion() {
		return version;
	}
	

}
