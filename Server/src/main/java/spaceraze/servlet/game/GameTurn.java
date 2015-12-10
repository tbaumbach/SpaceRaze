package spaceraze.servlet.game;

import java.util.ArrayList;
import java.util.List;

import sr.server.SR_Server;
import sr.world.Building;
import sr.world.Planet;
import sr.world.Player;

public class GameTurn {
	
	private List<PlayerInfo> players;
	private List<PlanetInfo> planets; 
	
	//TODO Här måste vi skicka med spelarens ship/troops/buildings typer så att vi ser de är modiferade via forskning.
	//Möjligt att det bara måste göras om spelvärlden har forskning.
	//TODO måste även lösa de unika typernas hantering. Ska alla andra spelare se när faction/world unika typer redan är byggda?
	
	GameTurn(SR_Server server, String playerName, int turn){
		
		addPlayers(server, playerName);
		addPlanets(server, playerName);
		
	}

	private void addPlanets(SR_Server server, String playerName) {
		planets = new ArrayList<PlanetInfo>(server.getGalaxy().getPlanets().size());
		List<Planet> listOfPlanets = server.getGalaxy().getPlanets();
		for (Planet planet : listOfPlanets) {
			planets.add(new PlanetInfo(planet, server.getGalaxy().getPlayer(playerName)));
		}
	}

	private void addPlayers(SR_Server server, String playerName) {
		players = new ArrayList<PlayerInfo>(server.getGalaxy().getActivePlayers().size());
		
		List<Player> activePlayers = server.getGalaxy().getActivePlayers();
		
		for (Player player : activePlayers) {
			if(!player.getName().equalsIgnoreCase(playerName)){
				players.add(new PlayerInfo(player));
			}
		}
	}
	
	
	
	class PlayerInfo{
		
		private String name, faction;
		
		PlayerInfo(Player player){
			name = player.getGovenorName();
			faction = player.getFactionName();
		}

		public String getName() {
			return name;
		}

		public String getFaction() {
			return faction;
		}
		
	}

}
