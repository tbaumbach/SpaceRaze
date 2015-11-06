package spaceraze.servlet.map;

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
import com.sun.jersey.spi.container.servlet.PerSession;

import sr.server.GameWorldHandler;
import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.server.map.MapHandler;
import sr.webb.users.User;
import sr.world.GameWorld;
import sr.world.Map;
import sr.world.Planet;
import sr.world.StatisticGameType;

@Path("/map")
public class MapServlet{
	
	@Context ServletContext context;
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MapLight> gameworldList() throws JsonProcessingException {
		
		
		System.out.println("Call aginst map/list");
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		List<MapLight> mapLights = new ArrayList<MapLight>();
		List<Map> maps = MapHandler.getAllMaps();
		for (Map aMap : maps) {
			mapLights.add(new MapLight(aMap));
		}
				
		return mapLights;
		
	}
	/*
	@GET
	@Path("/create/contract")
	@Produces(MediaType.APPLICATION_JSON)
	public GameParameters contract(@Context HttpServletRequest req) throws JsonProcessingException {
		
		System.out.println("Call aginst creategame/create/contract: ");
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		
		System.out.println("Call aginst creategame/create User.getName: " + user.getName());
		System.out.println("Call aginst creategame/create User.getPassword: " + user.getPassword());
		System.out.println("Call aginst creategame/create User.getRole: " + user.getRole());
		System.out.println("Call aginst creategame/create User.getEmails: " + user.getEmails());
		
		List<String> factions = new ArrayList<String>();
		factions.add("China");
		factions.add("USA");
		
		return new GameParameters("thelastgreatwar", "", "wigge9", "10", "yes", "0", "no", "9", 
				"", "", "yes", factions, "no", "faction", false, 
				"no", 60, 60, 0, 1, StatisticGameType.ALL);
				
		
	//	ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		//ObjectMapper mapper = new ObjectMapper();
		
		//GameWorld gameWorld = TheLastGreatWar.getGameWorld();
		
	//	SR_Server aGame = sh.findGame(gameName);
		
		//GameWorld gameWorld = aGame.getGalaxy().getGameWorld();
		
		//Planet planet1 = aGame.getGalaxy().getPlanets().get(0);
		
	//	return aGame.getGalaxy().getPlanets().get(new Integer(planet));
	}
	*/
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
