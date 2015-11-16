package sr.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.general.logging.Logger;

public class Buildings implements Serializable{
	static final long serialVersionUID = 1L;
	
	private List<BuildingType> buildings;
	
	
	public Buildings(){
		buildings = new ArrayList<BuildingType>();
	}
	
	// kanske inte behövs användas då denna var tänkt att användas vid kloning.  kör functions.deepClone i stället.
	public Buildings(Buildings inObj){
		buildings = new ArrayList<BuildingType>();
		
		for(int i=0;i < inObj.buildings.size();i++){
			buildings.add(inObj.buildings.get(i));
		}
		
		// klona alla Buildings och glöm inte att lista igenom alla relationer mellan bildings.
		// hoppa över relatioener då det bara är en string (men det kanske görs om så att det blir objektet.
	}
	
	public List<BuildingType> getBuildings() {
		return buildings;
	}
	
	public void addBuilding(BuildingType buildingType){
		buildings.add(buildingType);
	}
	
	public BuildingType getBuildingType(String name){
		BuildingType found = null;
		for(int i= 0; i < buildings.size();i++){
			if(buildings.get(i).getName().equalsIgnoreCase(name)){
				found = buildings.get(i);
			}
		}
		return found;
	}
		
	// not in use.
	@Override
	public Buildings clone(){
		Logger.finer("Buildings Clone(): ");
		return new Buildings(this);
	}
	
	public Vector<BuildingType> getAvailableNewBuildings(Planet aPlanet){
		Vector<BuildingType> tempBuildingTypes = new Vector<BuildingType>();     	
    	for(int i=0; i < buildings.size();i++){
    		Logger.finer("buildings.get(i).isConstructible(): " + (buildings.get(i)).isConstructible(aPlanet, -1));
    		Logger.finer("buildings.get(i).getParentBuilding() == null: " + (buildings.get(i).getParentBuilding() == null));
    		if(buildings.get(i).getParentBuilding() == null && buildings.get(i).isConstructible(aPlanet, -1)){
    			tempBuildingTypes.add(buildings.get(i));
    			Logger.finer("tempBuildingTypes.add()= " + (buildings.get(i)).getName());
    		}
    	}
    	return tempBuildingTypes;
    	
    }
	
	@JsonIgnore
	public Vector<BuildingType> getRootBuildings(){
		Vector<BuildingType> tempBuildingTypes = new Vector<BuildingType>(); 
    	
    	for(int i=0; i < buildings.size();i++){
    		if(buildings.get(i).getParentBuilding() == null){
    			tempBuildingTypes.add(buildings.get(i));
    		}
    	}
    	return tempBuildingTypes;
    	
    }
	
	public Vector<String> getRootBuildingsName(){
		Vector<String> tempBuildingTypes = new Vector<String>(); 
    	
    	for(int i=0; i < buildings.size();i++){
    		if(buildings.get(i).getParentBuilding() == null){
    			tempBuildingTypes.add(buildings.get(i).getName());
    		}
    	}
    	return tempBuildingTypes;
    	
    }
	
	public Vector<BuildingType> getNextBuildingSteps(BuildingType aBuildingType){
		Vector<BuildingType> tempBuildingTypes = new Vector<BuildingType>(); 
    	for(int i=0; i < buildings.size();i++){
    		if(buildings.get(i).getParentBuilding() != null && buildings.get(i).getParentBuilding().getName().equalsIgnoreCase(aBuildingType.getName())){
    			tempBuildingTypes.add(buildings.get(i));
    		}
    	}
    	return tempBuildingTypes;
		
	}

	@JsonIgnore
	public Vector<BuildingType> getBuildingsVectorOrderByParent(){
		Vector<BuildingType> tempBuildingTypes = new Vector<BuildingType>(); 
		Logger.fine("Buildings");
    	for(int i=0; i < buildings.size();i++){
    		if(buildings.get(i).getParentBuilding() == null){
    			tempBuildingTypes.add(buildings.get(i));
    			Logger.fine("Buildings"+i);
    		}
    		for(int j=0; j < buildings.size();j++){
        		if(buildings.get(j).getParentBuilding() != null && buildings.get(j).getParentBuilding().getName().equalsIgnoreCase(buildings.get(i).getName())){
        			tempBuildingTypes.add(buildings.get(j));
        			Logger.fine("Buildings"+j);
        		}
        	}
    	
    	}
    	return tempBuildingTypes;
    	
    }
	
	
}


