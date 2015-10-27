/*
 * Created on 2005-maj-12
 */
package sr.world;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import sr.general.logging.Logger;
import sr.server.properties.PropertiesHandler;

/**
 * @author WMPABOD
 *
 * Encapsulates all data for a map instance
 */
public class Map implements Serializable, Comparable<Map>{
	private static final long serialVersionUID = 1L;
	private List<PlanetConnection> connections;
	private List<Planet> planets;
	private int maxNrStartPlanets;
	private String createdDate,changedDate,description;
	private String authorName; // real name of author
	private String authorLogin; // authors unique spaceraze login
	private String fileName; // unique name of map used in filename of properties file
	private String name; // real name of map
	private long versionId = 1; // should be increased every time a new version of a map is published
	public Map(String playerLogin,String mapFileName){
		this(playerLogin + "." + mapFileName);
		Logger.finer("new Map from draft, mapname: " + mapFileName + " playerLogin" + playerLogin);
	}
	
	// creating a map from a published map
	public Map(String mapFileName){
		Logger.finest("New Map from mapName: " + mapFileName);
		Properties props = PropertiesHandler.getInstance("map." + mapFileName);
		initMap(mapFileName,props);
	}

	/** 
	 * Creating a map from a properties object.
	 * Used by SpaceRazeDroid.
	 * 
	 * @param mapFileName
	 * @param props
	 */
	public Map(String mapFileName, Properties props){
		initMap(mapFileName, props);
	}
	
	// creating a map from a published map
	public void initMap(String mapFileName, Properties props){
		Logger.finest("Init Map from mapName: " + mapFileName);
		planets = getPlanets(props);
		connections = getConnections(props,planets);
//		distanceTable = getDistanceTable(props);
		name = props.getProperty("mapName");
		String tmpVersion = props.getProperty("version");
		if (tmpVersion != null){
			versionId = Long.parseLong(props.getProperty("version"));
		}
		fileName = props.getProperty("mapFileName");
		authorLogin = props.getProperty("authorLogin");
		authorName = props.getProperty("author");
		description = props.getProperty("description");
		Logger.finest("description: " + description);
		createdDate = props.getProperty("createddate");		
		changedDate = props.getProperty("changeddate");
		Logger.finest("changeddate: " + changedDate);
		maxNrStartPlanets = Integer.parseInt(props.getProperty("maxNrStartPlanets"));
	}


	/**
	 * This constructor is only used during testing
	 * @param planets
	 * @param conns
	 * @param name
	 * @param maxNrPlayers
	 */
	public Map(List<Planet> planets, List<PlanetConnection> conns, String name, String desc, int maxNrPlayers){
		this.planets = planets;
		this.connections = conns;
		this.name = name;
		this.description = desc;
//		createdDate = props.getProperty("createddate");		
//		changedDate = props.getProperty("changeddate");
		this.maxNrStartPlanets = maxNrPlayers;
	}
	
	public Map(){
		planets = new LinkedList<Planet>(); // behövs denna?
		connections = new LinkedList<PlanetConnection>(); // behövs denna?
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		createdDate = sdf.format(new Date());
	}
	
	/**
	 * Creates a new Map object from file of a published map
	 * @return
	 */
	public Map getCopyFromFile(){
		return new Map(fileName);
	}

	private List<Planet> getPlanets(Properties props){
		List<Planet> tmpPlanets = new LinkedList<Planet>();
		int index = 0;
		String tmpValue = props.getProperty("planet" + index);
		while (tmpValue != null){
			Planet tmpPlanet = new Planet(tmpValue);
			tmpPlanets.add(tmpPlanet);
			index++;
			tmpValue = props.getProperty("planet" + index);
		}
		return tmpPlanets;
	}
	
	private List<PlanetConnection> getConnections(Properties props, List<Planet> allPlanets){
		List<PlanetConnection> tmpConnections = new LinkedList<PlanetConnection>();
		int index = 0;
		String tmpValue = props.getProperty("connection" + index);
		while (tmpValue != null){
			PlanetConnection tmpConnection = new PlanetConnection(tmpValue,allPlanets);
			tmpConnections.add(tmpConnection);
			index++;
			tmpValue = props.getProperty("connection" + index);
		}
		return tmpConnections;
	}


	public String getAuthorName() {
		return authorName;
	}

	public String getAuthorLogin() {
		return authorLogin;
	}
	
	public void setAuthorLogin(String newAuthorLogin){
		authorLogin = newAuthorLogin;
	}

	public String getChangedDate() {
		return changedDate;
	}
	
	public List<PlanetConnection> getConnections() {
		return connections;
	}

	public int getNrConnections() {
		return connections.size();
	}

	public int getNrConnectionsLong() {
		int nrLongConnections = 0;
		for (PlanetConnection aConnection : connections) {
			if (aConnection.isLongRange()){
				nrLongConnections++;
			}
		}
		return nrLongConnections;
	}

	public int getNrConnectionsShort() {
		int nrShortConnections = 0;
		for (PlanetConnection aConnection : connections) {
			if (!aConnection.isLongRange()){
				nrShortConnections++;
			}
		}
		return nrShortConnections;
	}
	
	public double getNrPlanetConnectionsRatio(){
		double ratio = getNrPlanets()/getNrConnections();
		return ratio;
	}

	public double getAverageNrConnections(){
		double ratio = (2.0*getNrConnections())/getNrPlanets();
		return ratio;
	}

	public String getAverageNrConnectionsString(){
		double ratio = getAverageNrConnections();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		return nf.format(ratio);
	}

	public String getCreatedDate() {
		return createdDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getMaxNrStartPlanets() {
		return maxNrStartPlanets;
	}
	
	public String getNameFull() {
		return name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String newFileName) {
		fileName = newFileName;
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public int getNrPlanets() {
		return planets.size();
	}

	/**
	 * used to create propertiesfiles from the old GalaxyCreator class
	 * @param args not used
	 
	public static void main(String[] args){
		String mapNameArr[] = {"test","wigge3","wigge6","wigge9","wigge12","solen15"};
		for (int i = 0; i < mapNameArr.length; i++) {
			final String mapName = mapNameArr[i];
			GalaxyCreator gc = new GalaxyCreator();
			Galaxy g = gc.createGalaxy("battleSim",mapName,3);
			List allPlanets = g.getPlanets();
			List allConnections = g.getPlanetConnections();
			String basePath = PropertiesHandler.getProperty("basepath");
			String completePath = basePath + "WEB-INF\\classes\\map." + mapName + ".properties";
			//		String path = "c:\\map." + mapName + ".properties";
			File mapFile = new File(completePath);
			try{
				FileWriter fw = new FileWriter(mapFile);
				PrintWriter pw = new PrintWriter(fw);
				pw.println("# File created date:" + (new Date()));
				pw.println("# File created by: main in Map class");
				pw.println("");
				pw.println("author = ?");
				pw.println("description = none");
				pw.println("");
				pw.println("# Last date the map was changed");
				pw.println("changeddate = ?");
				pw.println("");
				pw.println("# The first time the map was available for SpaceRaze games");
				pw.println("createddate = ?");
				pw.println("");
				// write name
				pw.println("mapName = " + mapName);
				pw.println("");
				// write max nr start planets
				pw.println("maxNrStartPlanets = " + g.getNrStartPlanets());
				pw.println("");
				// write planets
				int counter = 0;
				for (Iterator iter = allPlanets.iterator(); iter.hasNext();) {
					Planet aPlanet = (Planet) iter.next();
					pw.println(aPlanet.getSaveString(counter));
					counter++;
				}
				pw.println("");
				// write connections
				counter = 0;
				for (Iterator iter = allConnections.iterator(); iter.hasNext();) {
					PlanetConnection aConnection = (PlanetConnection) iter.next();
					pw.println(aConnection.getSaveString(counter));
					counter++;
				}
				pw.close();
				LoggingHandler.info("Map saved: " + mapNameArr[i]);
			}
			catch(IOException ioe){
				LoggingHandler.severe("Error while saving map");
				ioe.printStackTrace();
			}
		
		}
	}
*/
	public int compareTo(Map anotherMap) {
		int sortOrder = 1;
		if (anotherMap.getNrPlanets() > getNrPlanets()){
			sortOrder = -1;
		}
		return sortOrder;
	}
	
	/**
	 * Returns html for select options for max nr of players in a new game.
	 * Min is always 2
	 * Selected is always maxNrStartPlanets
	 * Max is always planets.size()
	 * @return html for select options
	 */
	public String getNrPlayersHTML(){
		String retStr = "";
		List<Planet> starPlanets = getStarPlanets();
		for (int i = 2; i <= starPlanets.size(); i++){
			String selected = "";
			String recText = "";
			if (i == maxNrStartPlanets){
				selected = " selected";
				recText = " (default)";
			}
			retStr = retStr + "<option value=\"" + i + "\"" + selected + ">" + i + recText + "</option>\n";
		}
		return retStr;
	}
	
	public String getNrStartingPlanetsHTML(){
		
		int numbersOfStartPlanets = planets.size() / maxNrStartPlanets;
		String retStr = "";
		for (int i = 1; i <= numbersOfStartPlanets; i++){
			retStr = retStr + "<option value=\"" + i + "\">" + i + "</option>\n";
		}
		return retStr;
	}
	
	private List<Planet> getStarPlanets() {
		List<Planet>  tempList = new ArrayList<Planet>();
		for (Planet aPlanet : planets) {
		  if(aPlanet.isPosssibleStartplanet()){
			  tempList.add(aPlanet);
		  }
		}  
		// TODO Auto-generated method stub
		return tempList;
	}
	
				  
	public String getPlanetsHTML(){
		String retStr = "";
		int j = 0;
		j = planets.size()/maxNrStartPlanets;
		Logger.info("Get Planets: maxNrStartPlanets="+ maxNrStartPlanets);
		Logger.info("Get Planets: planets.size()="+ planets.size());
		Logger.info("Get Planets: j="+ j);
		
		
		for (int i = 0; i <= j; i++){
			
			String selected = "";
			String recText = "";
			
			if (i == 1){
				selected = " selected";
				recText = " (default)";
				j++;
				
			}
			retStr = retStr + "<option value=\"" + i + "\"" + selected + ">" + i + recText + "</option>\n";
				
		}
		
		return retStr;
	}
	
	
	
	public Planet createNewPlanet(int x, int y){
		String newName = getFreeName("Unnamed - ");
		Planet newPlanet = new Planet(x,y,0,newName,-1,-1,false, true);
		planets.add(newPlanet);
		return newPlanet;
	}
	
	public String getFreeName(String namePrefix){
		int index = 1;
		boolean freeFound = false;
		String curName = null;
		while (!freeFound){
			curName = namePrefix + index;
			boolean exists = false;
			int i = 0;
			while (!exists & (i < planets.size())){
				Planet tmpPlanet = (Planet)planets.get(i);
				if (tmpPlanet.getName().equals(curName)){
					exists = true;
				}else{
					i++;
				}
			}
			if (exists){
				index++;
			}else{
				freeFound = true;
			}
		}
		return curName;
	}
	
	public Planet findClosestPlanet(int xLY,int yLY){
		Planet closestPlanet = null;
		double closest = Double.MAX_VALUE;
		for (Planet aPlanet : planets) {
			double xDiff = aPlanet.getXcoor() - xLY;
			double yDiff = aPlanet.getYcoor() - yLY;
			double distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff,2));
			if (distance < closest){
				closest = distance;
				closestPlanet = aPlanet;
			}
		}
		return closestPlanet;
	}
	
	public void removePlanet(Planet aPlanet){
		// remove all connections
		List<PlanetConnection> removeConns = new LinkedList<PlanetConnection>();
		for (PlanetConnection aConn : connections) {
			if ((aConn.getPlanet1() == aPlanet) | (aConn.getPlanet2() == aPlanet)){
				removeConns.add(aConn);
			}
		}
		// remove all removeConns
		for (PlanetConnection aConn2 : removeConns) {
			connections.remove(aConn2);
		}
		// remove the planet
		planets.remove(aPlanet);
	}

	public void addNewConnection(Planet newP1, Planet newP2, boolean longRange){
		PlanetConnection newConn = new PlanetConnection(newP1,newP2,longRange);
		connections.add(newConn);
	}
	
	public void addNewPlanet(Planet aPlanet){
		planets.add(aPlanet);
	}

	/**
	 * copy of method i Galaxy
	 * @param findname
	 * @return
	 */
    public Planet findPlanet(String findname){
        Planet p = null;
        int i = 0;
        while ((p == null) & (i<planets.size())){
          Planet temp = (Planet)planets.get(i);
          if (temp.getName().equalsIgnoreCase(findname)){
            p = temp;
          }else{
            i++;
          }
        }
        return p;
    }
    
    public void deleteConnection(Planet aPlanet1, Planet aPlanet2){
    	PlanetConnection found = findConnection(aPlanet1,aPlanet2);
    	connections.remove(found);
    }
    
    public PlanetConnection findConnection(Planet aPlanet1, Planet aPlanet2){
    	int i = 0;
    	PlanetConnection found = null;
    	while ((found == null) & (i < connections.size())){
    		PlanetConnection tmpConn = (PlanetConnection)connections.get(i);
    		if (tmpConn.isConnection(aPlanet1,aPlanet2)){
    			found = tmpConn;
    		}else{
    			i++;
    		}
    	}
    	return found;
    }

	// returns a String object that can be written to a file to create a Map file
	public String getMapData(){
		String s = "";
		List<Planet> allPlanets = getPlanets();
		List<PlanetConnection> allConnections = getConnections();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		s = s + "# File created date: " + sdf.format(new Date()) + "\n";
		s = s + "# File created by: MapHandler.getMapData(Map)\n";
		s = s + "\n";
		s = s + "author = " + getAuthorName() + "\n";
		s = s + "authorLogin = " + getAuthorLogin() + "\n";
		s = s + "\n";
		s = s + "description = " + getDescription() + "\n";
		s = s + "\n";
		s = s + "# Last date the map was changed (saved)\n";
		s = s + "changeddate = " + sdf.format(new Date()) + "\n";
		s = s + "\n";
		s = s + "# When the map was first created\n";
		s = s + "createddate = " + getCreatedDate() + "\n";
		s = s + "\n";
		// write name
		s = s + "mapName = " + getNameFull() + "\n";
		s = s + "mapFileName = " + getFileName() + "\n";
		s = s + "\n";
		// write version
		s = s + "version = " + getVersionId() + "\n";
		s = s + "\n";
		// write max nr start planets
		s = s + "maxNrStartPlanets = " + getMaxNrStartPlanets() + "\n";
		s = s + "\n";
		// write planets
		int counter = 0;
		for (Planet aPlanet : allPlanets) {
			s = s + aPlanet.getSaveString(counter) + "\n";
			counter++;
		}
		s = s + "\n";
		// write connections
		counter = 0;
		for (PlanetConnection aConnection : allConnections) {
			s = s + aConnection.getSaveString(counter) + "\n";
			counter++;
		}
		s = s + "\n";
		return s; 	
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setMaxNrStartPlanets(int maxNrStartPlanets) {
		this.maxNrStartPlanets = maxNrStartPlanets;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public boolean checkPlanetNamesNoEmpty(){
		boolean noEmptyName = true;
		int counter = 0;
		while (noEmptyName & (counter < planets.size())) {
			Planet aPlanet = (Planet)planets.get(counter);
			if (aPlanet.getName().trim().equals("")){
				noEmptyName = false;
			}else{
				counter++;
			}
		}
		return noEmptyName;
	}

	public boolean checkPlanetNamesUnique(){
		boolean allNamesUnique = true;
		for (Planet aPlanet1 : planets) {
			if (allNamesUnique){
				int counter = 0;
				for (Planet aPlanet2 : planets) {
					if (aPlanet1.getName().equalsIgnoreCase(aPlanet2.getName())){
						counter++;
					}
				}
				if (counter > 1){
					allNamesUnique = false;
				}
			}
		}
		return allNamesUnique;
	}
	
	/**
	 * Called to validate a map that there are no planets without connections
	 * Returns the first planet without any connections. If all have 
	 * connections, null is returned.
	 */
	public Planet checkConnectionToPlanets(){
		Planet foundPlanet = null;
		int i = 0;
		while ((foundPlanet == null) && (i < planets.size())){
			Planet tmpPlanet = (Planet)planets.get(i);
			int j = 0;
			boolean foundConn = false;
			while ((!foundConn) && (j < connections.size())){
				PlanetConnection tmpConn = (PlanetConnection)connections.get(j);
				Logger.finest( tmpPlanet.getName() + " " + tmpConn.getPlanet1().getName() + " " + tmpConn.getPlanet2().getName());
				if (tmpConn.getPlanet1().equals(tmpPlanet) | tmpConn.getPlanet2().equals(tmpPlanet)){
					foundConn = true;
				}else{
					j++;
				}
			}
			if (foundConn){
				i++;
			}else{
				foundPlanet = tmpPlanet;
			}
		}
		return foundPlanet;
	}

	public boolean checkPlanetsAllConnected(){
		boolean allComnected = true;
		List<Planet> searchedPlanets = new LinkedList<Planet>(); // add all serched planets + startplanet
		if (planets.size() > 1){ // 0 or 1 planets are by definition connected
			List<Planet> edgePlanets = new LinkedList<Planet>();
			List<Planet> newEdgePlanets = new LinkedList<Planet>();
			searchedPlanets.add(planets.get(0));
			edgePlanets.add(planets.get(0));
			List<Planet> allNeighbours = new LinkedList<Planet>();
			
	      	// Gå igenom alla edgePlanets (dvs bara startplaneten initialt)
	        for (int i = 0; i < edgePlanets.size(); i++){
	          Planet tempPlanet = (Planet)edgePlanets.get(i);
	          // Hämta alla grannar till tempPlanet
	          allNeighbours = getAllDestinations(tempPlanet,true);
	          // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
	          for (int j = 0; j < allNeighbours.size(); j++){
	            Planet tempNeighbourPlanet = (Planet)allNeighbours.get(j);
	            // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
	            if ((!searchedPlanets.contains(tempNeighbourPlanet)) & (!newEdgePlanets.contains(tempNeighbourPlanet))){
	              // lägg i newEdgePlanets
	              newEdgePlanets.add(tempNeighbourPlanet);
	            }
	          }
	        }
	        
	        while (newEdgePlanets.size() > 0){
	            // töm edgePlanets
	            edgePlanets.clear();
	            for (int l = 0; l < newEdgePlanets.size(); l++){
	              // kopiera över newEdgePlanets till edgePlanets
	              edgePlanets.add((Planet)newEdgePlanets.get(l));
	              // kopiera över newEdgePlanets till searchedPlanets
	              searchedPlanets.add((Planet)newEdgePlanets.get(l));
	            }
	            // töm newEdgePlanets
	            newEdgePlanets.clear();
	            // töm allNeighbours
	            allNeighbours.clear();

		      	// Gå igenom alla edgePlanets (dvs bara startplaneten initialt)
		        for (int i = 0; i < edgePlanets.size(); i++){
		          Planet tempPlanet = (Planet)edgePlanets.get(i);
		          // Hämta alla grannar till tempPlanet
		          allNeighbours = getAllDestinations(tempPlanet,true);
		          // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
		          for (int j = 0; j < allNeighbours.size(); j++){
		            Planet tempNeighbourPlanet = (Planet)allNeighbours.get(j);
		            // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
		            if ((!searchedPlanets.contains(tempNeighbourPlanet)) & (!newEdgePlanets.contains(tempNeighbourPlanet))){
		              // lägg i newEdgePlanets
		              newEdgePlanets.add(tempNeighbourPlanet);
		            }
		          }
		        }
	        }
		}
		allComnected = planets.size() == searchedPlanets.size();
		return allComnected;
	}
	
    public List<Planet> getAllDestinations(Planet location, boolean longRange){
        List<Planet> alldest = new LinkedList<Planet>();
        Planet tempPlanet = null;
        for (PlanetConnection temppc : connections){
          tempPlanet = temppc.getOtherEnd(location,longRange);
          if (tempPlanet != null){  // there is a connection within range
            alldest.add(tempPlanet);
          }
        }
        return alldest;
      }

	public long getVersionId() {
		return versionId;
	}

	public void setVersionId(long versionId) {
		this.versionId = versionId;
	}

	public void incVersionId() {
		versionId++;
	}

	public String getMapInfoDroid() {
		  StringBuffer sb = new StringBuffer();
		  sb.append("<h4>Map: ");
		  sb.append(name);
		  sb.append("</h4>");
		  sb.append("<p>");
		  sb.append(description);
		  sb.append("</p>");
		  
		  sb.append("<b>Number of players: </b>");
		  sb.append(maxNrStartPlanets);
		  sb.append("<br>");

		  sb.append("<b>Number of planets: </b>");
		  sb.append(planets.size());
		  sb.append("<br>");

		  sb.append("<b>Average nr connections: </b>");
		  sb.append(getAverageNrConnectionsString());
		  sb.append("<br>");
		  sb.append("<br>");
		  
		  // visa bild på kartan
		  sb.append("<img src=\"map_" + fileName + "\" alt=\"map_" + fileName + "\" />");
		  
		  return sb.toString();
	}


}
