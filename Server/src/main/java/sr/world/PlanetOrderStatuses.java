package sr.world;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Innehåller planet orderstatus för alla planeter i ett parti
 * 
 * @author Paul Bodin
 *
 */
public class PlanetOrderStatuses implements Serializable{
	private static final long serialVersionUID = 1L;
	private HashMap<String,PlanetOrderStatus> planetOrderStatusMap;
	
	public PlanetOrderStatuses (List<Planet> planets){
		planetOrderStatusMap = new LinkedHashMap<String, PlanetOrderStatus>();
		for (Planet planet : planets) {
			planetOrderStatusMap.put(planet.getName(), new PlanetOrderStatus());
		}
	}
	
	public PlanetOrderStatus getPlanetOrderStatus(String aPlanetName){
		return planetOrderStatusMap.get(aPlanetName);
	}

	// easy to use setters
    public void setAttackIfNeutral(boolean attackIfNeutral,String aPlanetName) {
		getPlanetOrderStatus(aPlanetName).setAttackIfNeutral(attackIfNeutral);
	}
	
    public void setDestroyOrbitalBuildings(boolean destroyOrbitalBuildings, String aPlanetName) {
    	getPlanetOrderStatus(aPlanetName).setDestroyOrbitalBuildings(destroyOrbitalBuildings);
	}
	
    public void setDoNotBesiege(boolean doNotBesiege, String aPlanetName) {
    	getPlanetOrderStatus(aPlanetName).setDoNotBesiege(doNotBesiege);
	}
	
    public void setMaxBombardment(int maxBombardment, String aPlanetName) {
    	getPlanetOrderStatus(aPlanetName).setMaxBombardment(maxBombardment);
	}
    
    // easy to use getters
    public boolean isAttackIfNeutral(String aPlanetName) {
		return getPlanetOrderStatus(aPlanetName).isAttackIfNeutral();
	}

    public boolean isDestroyOrbitalBuildings(String aPlanetName) {
		return getPlanetOrderStatus(aPlanetName).isDestroyOrbitalBuildings();
	}
	
    public boolean isDoNotBesiege(String aPlanetName) {
		return getPlanetOrderStatus(aPlanetName).isDoNotBesiege();
	}
	
    public int getMaxBombardment(String aPlanetName) {
		return getPlanetOrderStatus(aPlanetName).getMaxBombardment();
	}

}
