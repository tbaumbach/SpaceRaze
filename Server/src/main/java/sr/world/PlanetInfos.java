//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger på Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package sr.world;

import java.io.Serializable;
import java.util.List;

public class PlanetInfos implements Serializable {
  static final long serialVersionUID = 1L;
  PlanetInfo allpi[];

  public PlanetInfos(List<Planet> allPlanets, Player aPlayer) {
    allpi = new PlanetInfo[allPlanets.size()];
    for (int i = 0; i < allPlanets.size(); i++){
      allpi[i] = new PlanetInfo(allPlanets.get(i),aPlayer);
    }
  }

  public String getNotes(String planetName){
	  return findPlanetInfo(planetName).getNotes();
  }

  public void setNotes(String planetName, String newNotes){
	  findPlanetInfo(planetName).setNotes(newNotes);
  }

  private PlanetInfo findPlanetInfo(String piname){
	  int i = 0;
	  PlanetInfo found = null;
	  while ((i < allpi.length) & (found == null)){
		  if (allpi[i].getName().equalsIgnoreCase(piname)){
			  found = allpi[i];
		  }else{
			  i++;
		  }
	  }
	  return found;
  }

  public String getLastKnownOwner(String planetName){
    return findPlanetInfo(planetName).getLastKnownOwner();
  }

  public int getLastKnownProd(String planetName){
    return findPlanetInfo(planetName).getProd();
  }

  public int getLastKnownRes(String planetName){
    return findPlanetInfo(planetName).getRes();
  }

  public void setLastKnownOwner(String planetName, String newValue, int lastInfoTurn){
    PlanetInfo temppi = findPlanetInfo(planetName);
    temppi.setLastKnownOwner(newValue);
    temppi.setLastInfoTurn(lastInfoTurn);
  }

  public void setLastKnownProdRes(String planetName, int newProd, int newRes){
    PlanetInfo temppi = findPlanetInfo(planetName);
    temppi.setRes(newRes);
    temppi.setProd(newProd);
  }

    public int getLastInfoTurn(String planetName){
      return findPlanetInfo(planetName).getLastInfoTurn();
    }

    public void setRazed(boolean newvalue, String planetName){
    	findPlanetInfo(planetName).setRazed(newvalue);
    }
    
    public boolean getLastKnownRazed(String planetName){
        return findPlanetInfo(planetName).isRazed();
    }
    
    public int getLastKnownTroopsNr(String planetName){
    	return findPlanetInfo(planetName).getLastKnownTroopsNr();
    }

    public void setLastKnownTroopsNr(String planetName,int lastKnownTroopsNr){
    	PlanetInfo temppi = findPlanetInfo(planetName);
    	temppi.setLastKnownTroopsNr(lastKnownTroopsNr);
    }

    public String getLastKnownMaxShipSize(String planetName){
    	return findPlanetInfo(planetName).getLastKnownMaxShipSize();
    }

    public void setLastKnownMaxShipSize(String planetName,String lastKnownMaxShipSize){
    	PlanetInfo temppi = findPlanetInfo(planetName);
    	temppi.setLastKnownMaxShipSize(lastKnownMaxShipSize);
    }

    /**
     * Get all building, both on surface and in orbit
     * @param planetName
     * @return
     */
    public String getLastKnownBuildings(String planetName){
    	String allBuildingsString = "";
    	String tmp = findPlanetInfo(planetName).getLastKnownBuildingsOnSurface();
    	if ((tmp != null) && (tmp.length() > 0)){
    		allBuildingsString = tmp;
    	}
    	tmp = findPlanetInfo(planetName).getLastKnownBuildingsInOrbit();
    	if ((allBuildingsString.length() > 0) & ((tmp != null) && (tmp.length() > 0))){
    		allBuildingsString += ", ";
    	}
    	if (tmp != null){
    		allBuildingsString += tmp;
    	}
    	return allBuildingsString;
    }

    public String getLastKnownBuildingsInOrbit(String planetName){
    	return findPlanetInfo(planetName).getLastKnownBuildingsInOrbit();
    }

    public void setLastKnownBuildingsInOrbit(String planetName,String lastKnownBuildingsInOrbit){
    	PlanetInfo temppi = findPlanetInfo(planetName);
    	temppi.setLastKnownBuildingsInOrbit(lastKnownBuildingsInOrbit);
    }

    public String getLastKnownBuildingsOnSurface(String planetName){
    	return findPlanetInfo(planetName).getLastKnownBuildingsOnSurface();
    }

    public void setLastKnownBuildingsOnSurface(String planetName,String lastKnownBuildingsOnSurface){
    	PlanetInfo temppi = findPlanetInfo(planetName);
    	temppi.setLastKnownBuildingsOnSurface(lastKnownBuildingsOnSurface);
    }

    public void pruneDroid(){
    	for (PlanetInfo aPlanetInfo : allpi) {
			aPlanetInfo.pruneDroid();
		}
    }
}