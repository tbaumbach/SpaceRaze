package spaceraze.servlet.user;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

import sr.webb.users.User;
import sr.webb.users.UserHandler;

@Path("/user")
public class UserServlet{
	
	@Context ServletContext context;
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String create(UserParameters parameters) throws JsonProcessingException {
		
		System.out.println("Call aginst user/create: " + parameters.toString());
		
		//TODO H�r f�r bara players skapas om det kommer in en role=admin s� m�ste en koll g�ras f�r att se om en admin �r inloggad = bara admin f�r skapa admins.
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
	
	//TODO  detta ska �ndras s� att inte session anv�nds.
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
	
	//TODO  detta ska �ndras s� att inte session anv�nds. H�r m�ste sedan anv�ndan skicka in, nu finns den i session s� det �r on�digt.
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public String logout(@Context HttpServletResponse response, @Context HttpServletRequest request) throws JsonProcessingException {
		
		System.out.println("Call aginst user/logout");
		
		
		String message = UserHandler.logoutUser(request, response);
				
		request.getSession().removeAttribute("user");
		
		return message;
		
	}
	
	//TODO  detta ska �ndras s� att inte session anv�nds.
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
