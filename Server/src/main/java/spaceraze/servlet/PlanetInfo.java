package spaceraze.servlet;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spaceraze.world.GameWorld;
import spaceraze.world.Planet;
import spaceraze.servlethelper.gameworlds.TheLastGreatWar;
import sr.server.SR_Server;
import sr.server.ServerHandler;

@Path("/PlanetInfo")
public class PlanetInfo{
	
	@Context ServletContext context;
	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyRESTService(@QueryParam("gamename") String gameName, @QueryParam("planet") int planet) throws NumberFormatException, JsonProcessingException {
		
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		// START testar att skapa en gameWorld JSON f�r klienten.
		System.out.println("######### Startar testningen ########");
		
		GameWorld gameWorld = TheLastGreatWar.getGameWorld();
		
		System.out.println("GameWorld name: " + gameWorld.getFullName());
		
		String gameWorldAsJSON = mapper.writeValueAsString(gameWorld.getVipTypes());
		
		
		//sh.startNewGame(gameWorldFileName, gameName, mapName, stepsString, autoBalanceString, timeString, emailPlayers, maxNrPlayers, userLogin, gamePassword, groupFaction, selectableFactionNames, randomFactionString, diplomacy, singlePlayer, ranked, singleVictory, factionVictory, endTurn, numberOfStartPlanet, statisticGameType)
		
		System.out.println("######### Slutar testningen ########");
		// SLUT testar att skapa en gameWorld JSON f�r klienten.
		
		System.out.println("TEST");
		
		System.out.println("gameName: " + gameName);
		
		
		SR_Server aGame = sh.findGame(gameName);
		
		//GameWorld gameWorld = aGame.getGalaxy().getGameWorld();
		
		Planet planet1 = aGame.getGalaxy().getPlanets().get(0);
		
		System.out.println("Planet1 name: " + planet1.getName());
		
		
		
		System.out.println(mapper.writeValueAsString(aGame.getGalaxy().getPlanets().get(new Integer(planet))));
		
		/*
		String aPlanet = mapper.writeValueAsString(aGame.getGalaxy().getPlanets().get(new Integer(planet)));
		
		try {
			Planet javaPlanet = mapper.readValue(aPlanet, Planet.class);
			System.out.println("Planet name: " + javaPlanet.getName() +  " planet pop: " + javaPlanet.getPopulation());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
 
		// return HTTP response 200 in case of success
		//return Response.ok(mapper.writeValueAsString(aGame.getGalaxy().getPlanets().get(new Integer(planet)))).build();
		//return Response.status(200).entity(gameWorldAsJSON).build();
		
		//return Response.status(404).entity(mapper.writeValueAsString(aGame.getGalaxy().getPlanets().get(new Integer(planet)))).build();
		
		String jsonReturnString = mapper.writeValueAsString(aGame.getGalaxy().getPlanets().get(new Integer(planet)));
		
		return Response.ok(jsonReturnString, "application/json; charset=UTF-8").build();
		
		
	}

}
