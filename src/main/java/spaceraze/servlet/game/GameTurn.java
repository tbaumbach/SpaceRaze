package spaceraze.servlet.game;

import java.util.ArrayList;
import java.util.List;

import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import spaceraze.world.Planet;
import spaceraze.world.Player;
import sr.server.SR_Server;

public class GameTurn {
	
	private List<PlayerInfo> players;
	private List<PlanetInfo> planets; 
	
	//TODO Här måste vi skicka med spelarens ship/troops/buildings typer så att vi ser de är modiferade via forskning.
	//Möjligt att det bara måste göras om spelvärlden har forskning.
	//TODO måste även lösa de unika typernas hantering. Ska alla andra spelare se när faction/world unika typer redan är byggda?
	
	//TODO lägg till BlackMarket.
	
	GameTurn(){};
	
	GameTurn(SR_Server server, String playerName, int turn){
		
		addPlayers(server, playerName);
		addPlanets(server, playerName);
		
	}

	private void addPlanets(SR_Server server, String playerName) {
		planets = new ArrayList<PlanetInfo>(server.getGalaxy().getPlanets().size());
		List<Planet> listOfPlanets = server.getGalaxy().getPlanets();
		for (Planet planet : listOfPlanets) {
			planets.add(new PlanetInfo(planet, server.getGalaxy().getPlayerByUserName(playerName)));
		}
	}

	private void addPlayers(SR_Server server, String playerName) {
		players = new ArrayList<PlayerInfo>(PlayerPureFunctions.getActivePlayers(server.getGalaxy()).size());
		
		List<Player> activePlayers = PlayerPureFunctions.getActivePlayers(server.getGalaxy());
		
		for (Player player : activePlayers) {
			if(!player.getName().equalsIgnoreCase(playerName)){
				players.add(new PlayerInfo(player));
			}
		}
	}
	



	public List<PlayerInfo> getPlayers() {
		return players;
	}

	public List<PlanetInfo> getPlanets() {
		return planets;
	}

}
