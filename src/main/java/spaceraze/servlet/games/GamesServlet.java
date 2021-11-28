package spaceraze.servlet.games;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

import spaceraze.servlet.game.GameParameters;
import spaceraze.world.Player;
import spaceraze.world.StatisticGameType;
import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.webb.users.User;

@Path("/games")
public class GamesServlet{
	
	@Context ServletContext context;
	
	@GET
	@Path("/{selector}")
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
	 		
	 		if (aServer.isPlayerParticipating(user)){
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
	 		if (aServer.getTurn() == 0){
	 			if (!aServer.isPlayerParticipating(user)){
	 				games.add(new GameListObject(aServer));
	 			}else{
	 				games.add(new GameListObject(aServer, null, Boolean.FALSE));
	 			}
	 		}
	 	}
	 	
	 	return games;
	}
	
	
	// Join a game
	@PUT
	@Path("/{gameName}/users/{faction}/{player}/{govenor}")
	@Produces(MediaType.APPLICATION_JSON)
	public String join(@PathParam("gameName") int gameId, @PathParam("faction") String factionName, @PathParam("player") String playerName, 
			@PathParam("govenor") String govenorName, @Context HttpServletRequest req) throws JsonProcessingException {
		
		String message ="Somthing went wrong in joining the game";
		
		System.out.println(new StringJoiner(" ").add("Call aginst game/user/join/{game}/{faction}/{player}/{govenor}:").
				add(Integer.toString(gameId)).add(factionName).add(playerName).add(govenorName));
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		// TODO We are not using players name from the URL. Going FB, Google logins = most likely no session.
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		
		
		if(user.isPlayerOrAdmin()){
			SR_Server aServer = sh.findGame(gameId);
			if(aServer != null){
				message = aServer.join(user.getName(), govenorName, factionName);
			}else{
				message = "Can't find a game with id: " + gameId;
			}
	    	
		}else{
			message = "Player are not logd in.";
		}
    	
	    return message;
		
	}

}
