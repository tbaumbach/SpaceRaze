package spaceraze.servlet.create;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

import sr.server.ServerHandler;
import sr.webb.users.User;
import sr.world.StatisticGameType;

@Path("/creategame")
public class GameCreater{
	
	@Context ServletContext context;
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String create(GameParameters parameters, @Context HttpServletRequest req) throws JsonProcessingException {
		
		
		System.out.println("Call aginst creategame/create: " + parameters.toString());
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		System.out.println("Call aginst creategame/create User.getName: " + user.getName());
		
		
		
		
		parameters.setUser(user.getName());
		
		String report = sh.StartNewGame(parameters);
		
		
		
		return report;
		
	}
	
	@GET
	@Path("/create/contract")
	@Produces(MediaType.APPLICATION_JSON)
	public GameParameters contract() throws JsonProcessingException {
		
		System.out.println("Call aginst creategame/create/contract: ");
				
		
		List<String> factions = new ArrayList<String>();
		factions.add("China");
		factions.add("USA");
		
		return new GameParameters("thelastgreatwar", "", "wigge9", "10", "yes", "0", "no", "9", 
				"", "", "yes", factions, "no", "faction", false, 
				"no", 60, 60, 0, 1, StatisticGameType.ALL);
	
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
