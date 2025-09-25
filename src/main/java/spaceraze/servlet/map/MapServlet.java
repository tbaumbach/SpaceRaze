package spaceraze.servlet.map;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import spaceraze.map.GalaxyMap;
import sr.server.map.MapHandler;

@Path("/maps")
public class MapServlet{
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MapLight> getMaps() throws JsonProcessingException {
		
		
		System.out.println("Call aginst maps");
		
		
		List<MapLight> mapLights = new ArrayList<MapLight>();
		List<GalaxyMap> maps = MapHandler.getAllMaps();
		for (GalaxyMap aMap : maps) {
			mapLights.add(new MapLight(aMap));
		}
				
		return mapLights;
		
	}
	
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public GalaxyMap getmap(@PathParam("name") String fileName) throws JsonProcessingException {
		
		
		System.out.println("Call aginst maps/name");
		
		
		List<GalaxyMap> maps = MapHandler.getAllMaps();
		for (GalaxyMap aMap : maps) {
			if(aMap.getFileName().equalsIgnoreCase(fileName)){
				return aMap;
			}
			
		}
				
		return null;
	}
	
	
}
