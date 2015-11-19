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

@Path("/maps")
public class MapServlet{
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MapLight> getMaps() throws JsonProcessingException {
		
		
		System.out.println("Call aginst maps");
		
		
		List<MapLight> mapLights = new ArrayList<MapLight>();
		List<Map> maps = MapHandler.getAllMaps();
		for (Map aMap : maps) {
			mapLights.add(new MapLight(aMap));
		}
				
		return mapLights;
		
	}
	
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map getmap(@PathParam("name") String fileName) throws JsonProcessingException {
		
		
		System.out.println("Call aginst maps/name");
		
		
		List<Map> maps = MapHandler.getAllMaps();
		for (Map aMap : maps) {
			if(aMap.getFileName().equalsIgnoreCase(fileName)){
				return aMap;
			}
			
		}
				
		return null;
	}
	
	
}
