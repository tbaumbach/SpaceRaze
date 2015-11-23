package spaceraze.servlet.game;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.webb.users.User;
import sr.world.StatisticGameType;

@Path("/games/game")
public class GameServlet {
	
	@Context ServletContext context;
	
	@GET
	@Path("/{gameId}/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public GameParameters getGame(@PathParam("gameId") int gameId, @PathParam("user") String userName, @Context HttpServletRequest req) throws JsonProcessingException {
		
		
		System.out.println("Call aginst game/{gameId}/{user} @GET");
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		
		SR_Server[] servers = sh.getServers();
		
		for (SR_Server aServer : servers) {
	 		if (aServer.getTurn() == 0 && !aServer.getGalaxy().getsinglePlayer()){
	 			if (aServer.isPlayerParticipating(user) && aServer.getId() == gameId){
	 				return new GameParameters(aServer);
	 			}
	 		}
	 	}
				
		return new GameParameters();
		
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String createNewGame(GameParameters parameters, @Context HttpServletRequest req) throws JsonProcessingException {
		
		
		System.out.println("Call aginst game @PUT: " + parameters.toString());
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		System.out.println("Call aginst creategame/create User.getName: " + user.getName());
		
		
		
		
		parameters.setUser(user.getName());
		
		String report = sh.StartNewGame(parameters);
		
		
		
		return report;
		
	}
	
	
	@GET
	@Path("/contract")
	@Produces(MediaType.APPLICATION_JSON)
	public GameParameters contract() throws JsonProcessingException {
		
		System.out.println("Call aginst game/contract: ");
				
		
		List<String> factions = new ArrayList<String>();
		factions.add("China");
		factions.add("USA");
		
		return new GameParameters("thelastgreatwar", "", -1, "wigge9", "10", "yes", "0", "no", "9", 
				"", "", "yes", factions, "no", "faction", false, 
				"no", 60, 60, 0, 1, StatisticGameType.ALL);
	
	}

}
