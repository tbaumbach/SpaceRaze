package sr.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.world.GameWorld;
import sr.world.Planet;

@SuppressWarnings("serial")
public class JSONAndHTMLServlet extends HttpServlet{
	
	
	public void doGet( HttpServletRequest request,HttpServletResponse response )throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		
		
		//response.setContentType("text/html");
		
		//response.getWriter().print("<html><body><h1>Testing</h1></body></html>");
		//response.getWriter().write("test");
	
		ServletContext servletContext = getServletContext();
		
		ServerHandler sh = (ServerHandler)servletContext.getAttribute("serverhandler");
		
		System.out.println("TEST");
		
		String gameName = "default";
		gameName = request.getParameter("gamename");
		
		System.out.println("gameName: " + gameName);
		
		String planet = request.getParameter("planet");
		
		
		//SR_Server aGame = sh.findGame("test");
		SR_Server aGame = sh.findGame(gameName);
		
		GameWorld gameWorld = aGame.getGalaxy().getGameWorld();
		
		Planet planet1 = aGame.getGalaxy().getPlanets().get(0);
		
		System.out.println("Planet1 name: " + planet1.getName());
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		//mapper.writeValue(new File("c:\\gameworld.json"), aGame.getGalaxy().getPlanets().get(8));
		
		System.out.println(mapper.writeValueAsString(aGame.getGalaxy().getPlanets().get(new Integer(planet))));
		
		
		// TODO fel n�r vi retunerar hela listan. fungerar n�r det bara �r planet 8
		response.getWriter().print(mapper.writeValueAsString(aGame.getGalaxy().getPlanets().get(new Integer(planet))));
		
		//request.getAttribute("");
		
		//request.getSession();
		
	}

}
