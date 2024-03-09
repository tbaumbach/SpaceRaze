package spaceraze.servlet.gameworld;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.world.GameWorld;

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
	
	//TODO 2020-12-03 create a method getting the serialized GameWorld to be used by Java Client. Change the logic in spaceraze.com.client.startview.NewGamePanel to use this method instead of GameWorldHandler.
	// Try to fix so this method is the same as the method over this, fix the logic around response format: XML, JSON or serialized java.
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public GameWorld getGameWorld(@PathParam("name") String name) {
		
		
		//thelastgreatwar
		GameWorld gameWorld = GameWorldHandler.getGameWorld(name);
		
		return gameWorld;
		
	}
	
}
