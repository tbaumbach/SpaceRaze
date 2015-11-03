package spaceraze.servlet;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;

import spaceraze.servlet.create.GameParameters;
import sr.server.GameWorldHandler;
import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.world.GameWorld;
import sr.world.Planet;
import sr.world.StatisticGameType;

@Path("/creategame")
public class GameCreater{
	
	@Context ServletContext context;
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(GameParameters parameters) throws JsonProcessingException {
		
		//System.out.println("Call aginst creategame/create: " + parameters);
		
		System.out.println("Call aginst creategame/create: " + parameters.getGameName());
		System.out.println("Call aginst creategame/create: " + parameters.getMaxNrPlayers());
		System.out.println("Call aginst creategame/create: " + parameters.getGameWorldName());
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		
		
		//sh.startNewGame(gameWorldFileName, gameName, mapName, stepsString, autoBalanceString, timeString, emailPlayers, maxNrPlayers, userLogin, gamePassword, groupFaction, selectableFactionNames, randomFactionString, diplomacy, singlePlayer, ranked, singleVictory, factionVictory, endTurn, numberOfStartPlanet, statisticGameType)
		
		//Hämta användaren från context
		
		// Läg till retur objekt om så önskas. Om så är fallet ska nog denna servelet retunera den typen av objekt i stället och inte ett Response objekt.
		return Response.ok().build();
		
	}
	
	@GET
	@Path("/create/contract")
	@Produces(MediaType.APPLICATION_JSON)
	public GameParameters contract() throws JsonProcessingException {
		
		System.out.println("Call aginst creategame/create/contract: ");
		
		return new GameParameters("thelastgreatwar", "", "wigge9", "10", "yes", "0", "no", "9", 
				"", "", "yes", new ArrayList<String>(), "no", "", false, 
				"no", 60, 60, 0, 1, StatisticGameType.ALL);
		
	//	ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		//ObjectMapper mapper = new ObjectMapper();
		
		//GameWorld gameWorld = TheLastGreatWar.getGameWorld();
		
	//	SR_Server aGame = sh.findGame(gameName);
		
		//GameWorld gameWorld = aGame.getGalaxy().getGameWorld();
		
		//Planet planet1 = aGame.getGalaxy().getPlanets().get(0);
		
	//	return aGame.getGalaxy().getPlanets().get(new Integer(planet));
	}
	
	/*
	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Planet verifyRESTService(@QueryParam("gamename") String gameName, @QueryParam("planet") int planet) throws NumberFormatException, JsonProcessingException {
		
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		//ObjectMapper mapper = new ObjectMapper();
		
		//GameWorld gameWorld = TheLastGreatWar.getGameWorld();
		
		SR_Server aGame = sh.findGame(gameName);
		
		//GameWorld gameWorld = aGame.getGalaxy().getGameWorld();
		
		//Planet planet1 = aGame.getGalaxy().getPlanets().get(0);
		
		return aGame.getGalaxy().getPlanets().get(new Integer(planet));
	}
	
	@GET
	@Path("/gameworld/{name}/{version}")
	@Produces(MediaType.APPLICATION_JSON)
	public GameWorld getGameWorld(@PathParam("name") String name, @PathParam("version") int version) throws NumberFormatException, JsonProcessingException {
		
		
		//thelastgreatwar
		GameWorld gameWorld = GameWorldHandler.getGameWorld(name);
		
		return gameWorld;
		
		
		
		//ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		//ObjectMapper mapper = new ObjectMapper();
		
		//GameWorld gameWorld = TheLastGreatWar.getGameWorld();
		
		
		//GameWorld gameWorld = aGame.getGalaxy().getGameWorld();
		
		//Planet planet1 = aGame.getGalaxy().getPlanets().get(0);
		
		//return aGame.getGalaxy().getPlanets().get(new Integer(planet));
	}
*/
}
