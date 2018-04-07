package spaceraze.servlet.gameworld;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import sr.server.GameWorldHandler;
import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.webb.users.User;
import sr.world.GameWorld;
import sr.world.Planet;
import sr.world.StatisticGameType;

@Path("/gameworlds")
public class GameWorldServlet{
	
	//@Context ServletContext context;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<GameWorldLight> gameworldList() throws JsonProcessingException {
		
		
		System.out.println("Call aginst gameworlds @GET");
		
		List<GameWorldLight> gameWorldsLights = new ArrayList<GameWorldLight>();
		List<GameWorld> gameWorldTypes = GameWorldHandler.getGameWorldTypes();
		for (GameWorld gameWorld : gameWorldTypes) {
			gameWorldsLights.add(new GameWorldLight(gameWorld));
		}
				
		return gameWorldsLights;
		
	}
	
	
	@GET
	@Path("/{name}/{version}")
	@Produces(MediaType.APPLICATION_JSON)
	public GameWorld getGameWorld(@PathParam("name") String name, @PathParam("version") int version) throws NumberFormatException, JsonProcessingException {
		
		
		//thelastgreatwar
		GameWorld gameWorld = GameWorldHandler.getGameWorld(name);
		
		return gameWorld;
		
	}
	
}
