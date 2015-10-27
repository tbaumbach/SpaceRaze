package sr.world.mapinfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sr.world.Planet;
import sr.world.PlanetConnection;
import sr.world.Player;

/**
 * H�ller all kart-information f�r planeter f�r en spelare f�r ett specifikt drag
 * 
 * @author Paul Bodin
 *
 */
public class MapInfoTurn implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String,MapPlanetInfo> allPlanetInfos;
	private List<MapConnectionInfo> starportConnections;
	
	public MapInfoTurn(Player player, MapInfos mapPlanetInfos, int turn){
		allPlanetInfos = new HashMap<String,MapPlanetInfo>();
		starportConnections = new LinkedList<MapConnectionInfo>();
		for (Planet planet : player.getGalaxy().getPlanets()) {
			MapPlanetInfo mapPlanetInfo = new MapPlanetInfo(planet, player, mapPlanetInfos, turn);
			allPlanetInfos.put(planet.getName(), mapPlanetInfo);
		}
		for (PlanetConnection aPlanetConnection : player.getGalaxy().getPlanetConnections()) {
			if (MapConnectionInfo.isStarPortConnections(aPlanetConnection,player)){
				MapConnectionInfo mapConnectionInfo = new MapConnectionInfo(aPlanetConnection);
				starportConnections.add(mapConnectionInfo);
			}
		}
	}

	public MapPlanetInfo getMapPlanetInfo(Planet planet) {
		MapPlanetInfo mapPlanetInfo = allPlanetInfos.get(planet.getName());
		return mapPlanetInfo;
	}

	public boolean isStarPortConnection(String aPlanetName1, String aPlanetName2){
		boolean found = false;
		int i = 0;
		while ((!found) & (i < starportConnections.size())){
			MapConnectionInfo aMapConnectionInfo = starportConnections.get(i);
			if (aMapConnectionInfo.isStarPortConnection(aPlanetName1, aPlanetName2)){
				found = true;
			}else{
				i++;
			}
		}
		return found;
	}
	
}
