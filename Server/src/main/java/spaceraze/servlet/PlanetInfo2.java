package spaceraze.servlet;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.world.Planet;

@Path("/PlanetInfo2")
public class PlanetInfo2{
	
	@Context ServletContext context;
	
	
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

}
