package spaceraze.servlet.game;

import java.util.StringJoiner;

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

@Path("/game/player")
public class PlayerServlet{
	
	@Context ServletContext context;
	
	
	// Do not use this. Use @Path("/{gameName}/users/{faction}/{player}/{govenor}") in GamesServlet
	@GET
	@Path("/join/{game}/{faction}/{player}/{govenor}")
	@Produces(MediaType.APPLICATION_JSON)
	public String join(@PathParam("game") int gameId, @PathParam("faction") String factionName, @PathParam("player") String playerName, 
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
	
	/*
	 * Abandone a game are made through game orders and will be done in the next turn.
	@GET
	@Path("/abandone/{game}/{player}/")
	@Consumes(MediaType.APPLICATION_JSON)
	public String abandone(@PathParam("name") int gameId, @PathParam("player") String playerName, @Context HttpServletRequest req) throws JsonProcessingException {
		
		String message ="Somthing went wrong in abandone the game";
		
		System.out.println(new StringJoiner(" ").add("Call aginst game/user/abandone/{game}/{player}/:").
				add(Integer.toString(gameId)).add(playerName));
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		// TODO We are not using players name from the URL. Going FB, Google logins = most likely no session.
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		
		
		if(user.isPlayerOrAdmin()){
			SR_Server aServer = sh.findGame(gameId);
			aServer.
	    	message = aServer.join(user.getName(), govenorName, factionName);
		}else{
			message = "Player are not logd in.";
		}
    	
	    return message;
		
	}
	*/
}
