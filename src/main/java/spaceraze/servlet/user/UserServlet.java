package spaceraze.servlet.user;


import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import sr.webb.users.User;
import sr.webb.users.UserHandler;

@Path("/user")
public class UserServlet{
	
	@Context
	ServletContext context;
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String create(UserParameters parameters) throws JsonProcessingException {
		
		System.out.println("Call aginst user/create: " + parameters.toString());
		
		//TODO Här får bara players skapas, om det kommer in en role=admin så måste en koll göras för att se om en admin är inloggad = bara admin får skapa admins.
		String message = UserHandler.addUser(parameters.getName(), parameters.getLogin(), parameters.getRole(), parameters.getEmail(), 
				parameters.getTurnEmail(), parameters.getGameEmail(), parameters.getAdminEmail(), parameters.isRulesOk());
				
		return message;
		
	}
	
	@GET
	@Path("/contract")
	@Produces(MediaType.APPLICATION_JSON)
	public UserParameters contract() throws JsonProcessingException {
		
		System.out.println("Call aginst user/contract: ");
		
		return new UserParameters();
	
	}
	
	//TODO  detta ska ändras så att inte session används.
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String login(UserParameters parameters, @Context HttpServletResponse response, @Context HttpServletRequest request) throws JsonProcessingException {
		
		System.out.println("Call aginst user/login: " + parameters.toString());
		

		String message = UserHandler.loginUser(parameters.getLogin(), parameters.getPassword(), response);
		
		if(message.equalsIgnoreCase("ok")){
			User aUser= UserHandler.getUser(parameters.getLogin(), parameters.getPassword());
			request.getSession().setAttribute("user",aUser);
		}
		
		return message;
		
	}
	
	//TODO  detta ska ändras så att inte session används. Här måste sedan användan skicka in, nu finns den i session så det är onödigt.
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public String logout(@Context HttpServletResponse response, @Context HttpServletRequest request) throws JsonProcessingException {
		
		System.out.println("Call aginst user/logout");
		
		
		String message = UserHandler.logoutUser(request, response);
				
		request.getSession().removeAttribute("user");
		
		return message;
		
	}
	
	//TODO  detta ska ändras så att inte session används.
	@POST
	@Path("/activate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String activate(UserParameters parameters, @Context HttpServletResponse response, @Context HttpServletRequest request) throws JsonProcessingException {
		
		System.out.println("Call aginst user/activate: " + parameters.toString());
		
		String message = "ok";
		
		message = UserHandler.activateUser(parameters.getLogin(), parameters.getPassword(), parameters.getRepeatedPassword());
		if(message.equalsIgnoreCase("ok")){
			
			message = UserHandler.loginUser(parameters.getLogin(), parameters.getPassword(), response);
			
			if(message.equalsIgnoreCase("ok")){
				
				message = UserHandler.loginUser(parameters.getLogin(), parameters.getPassword(), response);
				if(message.equalsIgnoreCase("ok")){
					User aUser= UserHandler.getUser(parameters.getLogin(), parameters.getPassword());
					request.getSession().setAttribute("user",aUser);
				}
			}
		}
		return message;
		
	}
	

}
