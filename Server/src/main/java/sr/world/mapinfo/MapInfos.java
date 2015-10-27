package sr.world.mapinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sr.general.logging.Logger;
import sr.world.Planet;
import sr.world.Player;

/**
 * Denna klass hanterar (eller ska g�ra det i en framtida version) all information som
 * ska visas p� kartan.
 * Varje spelare har sin egen instans av kartan med hur just vad den spelaren f�r se p� kartan.
 * 
 * F�r att kunna visa information om tidigare drag s� lagras alla gamla PlanetInfos fr�n
 * tidigare drag.
 * 
 * Inga setters eller getters f�r enskilda v�rden finns i denna klass, ist�llet h�mtas det 
 * aktuella MapInfoTurn -> MapPlanetInfo-objektet fram och alla v�rden s�tts direkt i det objektet.
 * 
 * @author Paul Bodin
 */
public class MapInfos implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<MapInfoTurn> allTurns;
	
	public MapInfos() {
		allTurns = new ArrayList<MapInfoTurn>();
	}

	public void createNextTurnMapInfo(Player player) {
		MapInfoTurn mapPlanetInfoTurn = new MapInfoTurn(player,this,allTurns.size()+1); 
		allTurns.add(mapPlanetInfoTurn);
	}
	
	public MapInfoTurn getMapInfoTurn(int turn){
		MapInfoTurn mapInfoTurn = allTurns.get(turn-1);
		return mapInfoTurn;
	}

	public MapInfoTurn getLastMapInfoTurn(){
		MapInfoTurn mapInfoTurn = allTurns.get(allTurns.size()-1);
		return mapInfoTurn;
	}
	
	public MapPlanetInfo getLastMapPlanetInfo(Planet planet){
		MapInfoTurn mapInfoTurn = getLastMapInfoTurn();
		return mapInfoTurn.getMapPlanetInfo(planet);
	}

	public MapPlanetInfo getLastKnownOwnerInfo(Planet planet){
		Logger.finer("getLastKnownOwnerInfo, planet: " + planet.getName());
		MapPlanetInfo lastKnownMapPlanetInfo = null;
		int turn = allTurns.size()-1;
		while((turn >= 0) & (lastKnownMapPlanetInfo == null)){
			Logger.finer("getLastKnownOwnerInfo, turn: " + turn);
			MapInfoTurn mapPlanetInfoTurn = allTurns.get(turn);
			MapPlanetInfo tempMapPlanetInfo = mapPlanetInfoTurn.getMapPlanetInfo(planet);
			if (tempMapPlanetInfo.getOwner() != null){ // om owner inte �r null s� fanns det owner info om den planeten det draget
				Logger.finer("getLastKnownOwnerInfo, tempMapPlanetInfo.getLastKnownOwner(): " + tempMapPlanetInfo.getLastKnownOwner());
				lastKnownMapPlanetInfo = tempMapPlanetInfo;
			}else{
				turn--;
			}
		}
		return lastKnownMapPlanetInfo;
	}

	public MapPlanetInfo getLastKnownProdResInfo(Planet planet){
		MapPlanetInfo lastKnownMapPlanetInfo = null;
		int turn = allTurns.size()-1;
		while((turn >= 0) & (lastKnownMapPlanetInfo == null)){
		//	Logger.info("turn: " + turn);
			MapInfoTurn mapPlanetInfoTurn = allTurns.get(turn);
			MapPlanetInfo tempMapPlanetInfo = mapPlanetInfoTurn.getMapPlanetInfo(planet);
			if (tempMapPlanetInfo.getProd() != null){ // om prod inte �r null s� fanns det prod&res info om den planeten det draget
				Logger.finer("tempMapPlanetInfo.getLastKnownOwner(): " + tempMapPlanetInfo.getLastKnownOwner());
				lastKnownMapPlanetInfo = tempMapPlanetInfo;
			}else{
				turn--;
			}
		}
		return lastKnownMapPlanetInfo;
	}

	public MapPlanetInfo getLastKnownRazedInfo(Planet planet){
		MapPlanetInfo lastKnownMapPlanetInfo = null;
		int turn = allTurns.size()-1;
		while((turn >= 0) & (lastKnownMapPlanetInfo == null)){
//			Logger.finer("turn: " + turn);
			MapInfoTurn mapPlanetInfoTurn = allTurns.get(turn);
			MapPlanetInfo tempMapPlanetInfo = mapPlanetInfoTurn.getMapPlanetInfo(planet);
			if (tempMapPlanetInfo.isRazed()){ // om isRazed �r true s� visste spelaren att denna planet var razead det draget
				Logger.finer("tempMapPlanetInfo.isRazed(): " + tempMapPlanetInfo.isRazed());
				lastKnownMapPlanetInfo = tempMapPlanetInfo;
			}else{
				turn--;
			}
		}
		return lastKnownMapPlanetInfo;
	}

	public void pruneDroid(){
		MapInfoTurn lastMapInfoTurn = getLastMapInfoTurn();
		allTurns.clear();
		allTurns.add(lastMapInfoTurn);
	}

}
