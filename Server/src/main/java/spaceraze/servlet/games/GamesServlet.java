package spaceraze.servlet.games;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.webb.users.User;
import sr.world.Player;

@Path("/games")
public class GamesServlet{
	
	@Context ServletContext context;
	
	@GET
	@Path("/list/{selector}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GameListObject> gameList(@PathParam("selector") String selector, @Context HttpServletRequest req) throws JsonProcessingException {
		
		
		System.out.println("Call aginst games/list/{selector}");
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		SR_Server[] servers = sh.getServers();
		
		List<GameListObject> games = new ArrayList<GameListObject>();
		
		if(selector.equalsIgnoreCase("active")){
			games = getPlayerActiveGames(req, servers);
		}else if(selector.equalsIgnoreCase("open")){
			games = getOpenGames(req, servers);
		}else{
			for (SR_Server aServer : servers) {
				
				games.add(new GameListObject(aServer));
			}
		}
				
		return games;
		
	}
	
	private List<GameListObject> getPlayerActiveGames(HttpServletRequest req, SR_Server[] servers){
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		
		List<GameListObject> games = new ArrayList<GameListObject>();
		
	 
	 	for (SR_Server aServer : servers) {
	 		
	 		if (aServer.isPlayerParticipating(user)&& !aServer.getGalaxy().getsinglePlayer()){
	 			Player tmpPlayer = aServer.getPlayer(user.getLogin(),user.getPassword());
	 			String iconName = null;
				
				boolean addGame = false;
				boolean haveMail = false;
				if (!aServer.getLastUpdateComplete()){
					iconName = "error";
					addGame = true;
				}else
				if (aServer.getGalaxy().gameEnded){
					if (tmpPlayer.isDefeated()){
						iconName = "defeat";
						addGame = true;
					}else{
						iconName = "victory";
						addGame = true;
					}
				}else
				if (tmpPlayer.isDefeated()){
					iconName = "defeat";
					addGame = true;
				}else
				if (tmpPlayer.isFinishedThisTurn()){
					iconName = "check";
					addGame = true;
				}else
				if (tmpPlayer.getUpdatedThisTurn()){
					iconName = "saved";
					addGame = true;
				}else
				if (aServer.getTurn() > 0){
					iconName = "cross";
					addGame = true;
				}
				
				
				haveMail = (aServer.getMessageDatabase().haveNewMessages(tmpPlayer.getName()));
				
				if(addGame){
					games.add(new GameListObject(aServer, iconName, haveMail));
				}
	 		}
		}
	 	
	 	return games;
	}
	
	private List<GameListObject> getOpenGames(HttpServletRequest req, SR_Server[] servers){
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		
		List<GameListObject> games = new ArrayList<GameListObject>();
		
		 
	 	for (SR_Server aServer : servers) {
	 		if (aServer.getTurn() == 0 && !aServer.getGalaxy().getsinglePlayer()){
	 			if (!aServer.isPlayerParticipating(user)){
	 				games.add(new GameListObject(aServer));
	 			}else{
	 				games.add(new GameListObject(aServer, null, Boolean.FALSE));
	 			}
	 		}
	 	}
	 	
	 	return games;
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
