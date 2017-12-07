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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;

import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.webb.users.User;
import sr.world.EconomyReport;
import sr.world.Player;
import sr.world.Report;
import sr.world.StatisticGameType;
import sr.world.TurnInfo;
import sr.world.orders.Expense;
import sr.world.orders.Orders;

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
		
		SR_Server server = sh.findGame(gameId);
		
		if(server != null){
			if(server.isPlayerParticipating(user)){
				return new GameParameters(server);
			}
		}
		
				
		return new GameParameters();
		
	}
	
	@GET
	@Path("/{gameId}/{user}/{turn}")
	@Produces(MediaType.APPLICATION_JSON)
	@JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
	public GameTurn getTurn(@PathParam("gameId") int gameId, @PathParam("user") String userName, 
			@PathParam("turn") int turn, @Context HttpServletRequest req) throws JsonProcessingException {
		
		
		System.out.println("Call aginst games/game/{gameId}/{user}/{turn} @GET");
		System.out.println("Call aginst games/game/"+ gameId +"/"+ userName +"/"+ turn +" @GET");
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		
		SR_Server server = sh.findGame(gameId);
		
		if(server != null){
			if(server.isPlayerParticipating(user)){
				
				GameTurn turnInfo = new GameTurn(server, user.getLogin(), turn);
				
				return turnInfo;
			}
		}
		
				
		return null;
		
	}
	
	@GET
	@Path("/orders/{gameId}/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	@JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
	public Orders getOrders(@PathParam("gameId") int gameId, @PathParam("user") String userName, 
			@Context HttpServletRequest req) throws JsonProcessingException {
		
		
		System.out.println("Call aginst games/game/orders/{gameId}/{user} @GET getOrders");
		System.out.println("Call aginst games/game/orders/"+ gameId +"/"+ userName +" @GET getOrders");
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		
		SR_Server server = sh.findGame(gameId);
		
		if(server != null){
			if(server.isPlayerParticipating(user)){
				
				Orders orders = server.getGalaxy().getPlayer(user.getName()).getOrders();
				
				return orders;
			}
		}
		
				
		return null;
		
	}
	
	@PUT
	@Path("/orders/{gameId}/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
	public String saveOrders(Orders newOrders, @PathParam("gameId") int gameId, @PathParam("user") String userName, 
			@Context HttpServletRequest req) throws JsonProcessingException {
		
		
		System.out.println("Call aginst games/game/orders/{gameId}/{user} @PUT saveOrders");
		System.out.println("Call aginst games/game/orders/"+ gameId +"/"+ userName +" @PUT saveOrders");
		
		ServerHandler sh = (ServerHandler)context.getAttribute("serverhandler");
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		
		SR_Server server = sh.findGame(gameId);
		
		if(server != null){
			if(server.isPlayerParticipating(user)){
				
				
				//TODO Blackmarker har problem vid bud på skepp eller blueprints då stora delar av objektmodellen åker med och det är ju inte meningen.
				// Skriv om Blackmarket bid så att bara namn på skepp/blueprints skickas med, detta medför omskrivning av serven och vi väntar med det så länge vi är beroende av det gamla projektet.
				// Finns risk att hela Orders måste skrivas om, dock är dagens Orders skriver just för att undervika att skicka med hela galaxy(serven).
				// Fortsätter och kodar mot dagens Orders då vi klara oss undan t.ex. BlackMarket.
				server.getGalaxy().getPlayer(user.getName()).setOrders(newOrders);
				
				
				return "Ok";
			}
		}
		
				
		return "somthing went wrong";
		
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
