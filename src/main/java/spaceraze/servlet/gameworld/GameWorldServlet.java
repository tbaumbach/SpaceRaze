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
