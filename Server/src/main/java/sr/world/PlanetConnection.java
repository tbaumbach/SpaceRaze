//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger på Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package sr.world;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

public class PlanetConnection implements Serializable{
  static final long serialVersionUID = 1L;
  Planet p1,p2;
  boolean longRange;

  public PlanetConnection(String dataString, List<Planet> allPlanets) {
	StringTokenizer st = new StringTokenizer(dataString,"\t");
	String planetName1 = st.nextToken();
	String planetName2 = st.nextToken();
	longRange = st.nextToken().equalsIgnoreCase("true");
    p1 = findPlanet(planetName1,allPlanets);
    p2 = findPlanet(planetName2,allPlanets);
  }
  
  public PlanetConnection cloneConnection(){
  	return new PlanetConnection(p1,p2,longRange);
  }
  
  private Planet findPlanet(String aPlanetName, List<Planet> allPlanets){
  	Planet found = null;
  	int index = 0;
  	while ((found == null) && (index < allPlanets.size())){
  		Planet tempPlanet = (Planet)allPlanets.get(index);
  		if (tempPlanet.getName().equals(aPlanetName)){
  			found = tempPlanet;
  		}else{
  			index++;
  		}
  	}
  	return found;
  }

  public PlanetConnection(Planet p1, Planet p2, boolean longRange) {
    this.p1 = p1;
    this.p2 = p2;
    this.longRange = longRange;
  }

  public Planet getPlanet1(){
    return p1;
  }

  public Planet getPlanet2(){
    return p2;
  }

  public boolean isLongRange(){
    return longRange;
  }

  public Planet getOtherEnd(Planet aPlanet, boolean isLongRange){
    Planet returnPlanet = null;
    if ((longRange == false) | (isLongRange == longRange)){
      if (aPlanet == p1){
        returnPlanet = p2;
      }else
      if (aPlanet == p2){
        returnPlanet = p1;
      }
    }
    return returnPlanet;
  }

  public String getSaveString(int index){
	String retStr = "connection" + index + " = ";
	retStr = retStr + p1.getName();
	retStr = retStr + "\t" + p2.getName();
	retStr = retStr + "\t" + longRange;
	return retStr;
  }
  
  public boolean isConnection(Planet aPlanet1, Planet aPlanet2){
  	  boolean found = false;
  	  if ((p1 == aPlanet1) & (p2 == aPlanet2)){
  	  	found = true;
  	  }else
   	  if ((p2 == aPlanet1) & (p1 == aPlanet2)){
  	  	found = true;
   	  }
   	  return found;
  }
  
  public String toString(){
	  return p1 + " <--> " + p2;
  }

}