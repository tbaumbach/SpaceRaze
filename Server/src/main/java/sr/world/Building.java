package sr.world;

import java.io.Serializable;

public class Building implements Serializable, Cloneable{
	static final long serialVersionUID = 1L;
	
	private BuildingType buildingType;
	private String uniqueName;
	// vet ej om dessa behövs
	private Planet location;
	
	//  borde kanske ligga i en klass typ warBuilding. Skall troligen inte vara med alls.
	private int uniqueId; 
	@SuppressWarnings("unused")
	private int kills; // not used (yet)
    
    //  construktorn skall ej anropas direkt, utan BuildingType.getBuilding skall
	// användas istället    
    public Building(BuildingType buildingType,String name,int nrProduced,Galaxy g, Planet location){
    	this.buildingType = buildingType;
    	uniqueName = buildingType.getName() + " - " + nrProduced;
//    	uniqueShortName = buildingType.getShortName() + " - " + nrProduced;
    	this.uniqueId = g.uniqueBuildingIdCounter.getUniqueId();
		this.setLocation(location);
//		if (name != null) {
//			this.name = name;
//		} else {
//			this.name = uniqueName;
//		}
    	
    }
    
    @Override
	public Building clone(){
		Building clonedBuilding = null;
		try {
			clonedBuilding = (Building)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clonedBuilding;
	}
    
    /*
	public Building (BuildingType buildingType){
		
	}
	*/
    
    public BuildingType getBuildingType(){
    	return buildingType;
    }

	public Planet getLocation() {
		return location;
	}

	public void setLocation(Planet location) {
		this.location = location;
	}
/*
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
*/
	public int getUniqueId() {
		return uniqueId;
	}

	public String getUniqueName() {
		return uniqueName;
	}

/*	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}*/
}
