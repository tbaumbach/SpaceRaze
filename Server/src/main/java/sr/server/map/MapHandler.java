/*
 * Created on 2005-maj-12
 */
package sr.server.map;

import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import spaceraze.servlethelper.map.MapImageCreator;
import spaceraze.servlethelper.map.TransferWrapper;
import spaceraze.util.general.Logger;
import spaceraze.world.Map;
import sr.server.MapFileNameFilter;
import sr.server.persistence.PHash;
import sr.server.properties.PropertiesHandler;
import sr.server.properties.PropertiesReader;
import sr.webb.users.User;

/**
 * @author WMPABOD
 *
 * Handles reading of map files and showing data about the maps available
 */
public class MapHandler {
	private static List<Map> allMaps;
	private static String dataPath; // inlagd för att fixa skum bugg, borde egentligen inte behövas...

	/**
	 * 
	 * @return
	 */
	public static List<Map> getAllMaps(){
		Logger.finer("getAllMaps() called");
		if (allMaps == null){
			allMaps = new LinkedList<Map>();
			// read maps from file and create allMaps List
			if (dataPath == null){
				dataPath = PropertiesHandler.getProperty("datapath");
			}
			Logger.finer("basePath: " + dataPath);
			String completePath = dataPath + "maps\\";
			List<String> allMapNames = getProps(completePath);
			for (String mapName : allMapNames) {
				allMaps.add(new Map(mapName));
			}
		}
		return allMaps;
	}

	private static List<Map> getMapDrafts(String playerLogin){
		Logger.finer("getMapDrafts() called");
		List<Map> allDrafts = new LinkedList<Map>();
		// read maps from file and create allMaps List
		if (dataPath == null){
			dataPath = PropertiesHandler.getProperty("datapath");
		}
		Logger.finer("dataPath: " + dataPath);
		String completePath = dataPath + "maps\\" + playerLogin + "\\";
		List<String> allMapNames = getProps(completePath);
		for (String mapName : allMapNames) {
			allDrafts.add(new Map(playerLogin + "." + mapName));
		}
		return allDrafts;
	}

	public static int getNrMapDrafts(String playerLogin){
		Logger.finer("getNrMapDrafts() called");
		// read maps from file and create allMaps List
		if (dataPath == null){
			dataPath = PropertiesHandler.getProperty("datapath");
		}
		Logger.finer("dataPath: " + dataPath);
		String completePath = dataPath + "map\\" + playerLogin + "\\";
		List<String> allMapDrafts = getProps(completePath);
		return allMapDrafts.size();
	}

	public static void reloadMaps(){
		Logger.finer("reloadMaps()");
		allMaps = null;
		getAllMaps();
	}
	
	public static Map getMap(String aMapFileName){
		return getMap(aMapFileName,null);
	}

	public static Map getMap(String aMapFileName, String playerLogin){
		Map found = null;
		List<Map> maps = null;
		if (playerLogin == null){
			maps = getAllMaps();
		}else{
			maps = getMapDrafts(playerLogin);
		}
		int index = 0;
		while ((found == null) & (index < maps.size())){
			Map tmpMap = (Map)maps.get(index);
			if (tmpMap.getFileName().equalsIgnoreCase(aMapFileName)){
				found = tmpMap;
			}else{
				index++;
			}
		}
		return found;
	}
	
	/**
	 * Tries to fetch the full name of the map with mapFileName.
	 * If unsuccessful it returns fileMapName.
	 * @param mapFileName 
	 * @return
	 */
	public static String getMapName(String mapFileName){
		String tmpMapName = mapFileName;
		Map aMap = getMap(mapFileName);
		if (aMap != null){
			tmpMapName = aMap.getNameFull();
		}
		return tmpMapName;
	}

	public static String getMapFilesNO(){
		Logger.finer("getMapFiles called");
		String retStr = "";
		List<Map> allMapNames = MapHandler.getAllMaps();
		Collections.sort(allMapNames);
		Logger.finer("allMapNames.size(): " + allMapNames.size());
		int i=0;
		for (Map aMap : allMapNames) {
			i = i +1;
			String RowName = i + "MapListNORow";
			String editStr = "Denied";
			retStr = retStr + "<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',7,1);\" onMouseOut=\"TranparentRow('" + RowName + "',7,0);\"><td id='" + RowName + "1' class='ListText'><div class='SolidText'></div></td><td id='" + RowName + "2' class='ListText'><div class='SolidText'><a href=\"Master.jsp?action=map_view&mapname=" + aMap.getFileName() + "\">" + aMap.getNameFull() + "</a></div></td><td id='" + RowName + "3' class='ListText'><div class='SolidText'>" + aMap.getFileName() + "</div></td><td id='" + RowName + "4' class='ListText'><div class='SolidText'>" + aMap.getNrPlanets() + "</div></td><td id='" + RowName + "5' class='ListText'><div class='SolidText'>" + aMap.getChangedDate() + "</div></td><td id='" + RowName + "6' class='ListText'><div class='SolidText'>" + aMap.getAuthorName() + "</div></td><td id='" + RowName + "7' class='ListText'><div class='SolidText'>" + editStr + "</div></td></tr>\n";
		}
		return retStr;
	}

	public static String getMapFiles(User aUser){
		Logger.finer("getMapFiles called");
		String retStr = "";
		List<Map> allMapNames = MapHandler.getAllMaps();
		Collections.sort(allMapNames);
		Logger.finer("allMapNames.size(): " + allMapNames.size());
		int i=0;
		for (Map aMap : allMapNames) {
			i = i + 1;
			String RowName = i + "MapListRow";
//			LoggingHandler.finer("in loop: " + aMap.getName());
			String editStr = "";
			if (aMap.getAuthorLogin().equals(aUser.getLogin())){
				editStr = "<a href=\"MapEditor.jsp?action=" + TransferWrapper.LOAD_PUB + "&mapname=" + aMap.getFileName() + "\" target=\"_top\">Edit</a>";
				editStr = editStr + " / ";
//				editStr = editStr + "<a href=\"map_files.jsp?action=delete&mapname=map." + aMap.getFileName() + "\">Delete</a>";
				editStr = editStr + "<a href=\"map_confirm_delete.jsp?mapname=" + aMap.getFileName() + "\">Delete</a>";
			}else{
				editStr = "<a href=\"MapEditor.jsp?action=" + TransferWrapper.LOAD_PUB + "&mapname=" + aMap.getFileName() + "\" target=\"_top\">Copy & Edit</a>";
			}
			retStr = retStr + "<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',7,1);\" onMouseOut=\"TranparentRow('" + RowName + "',7,0);\"><td id='" + RowName + "1' class='ListText'></td><td id='" + RowName + "2' class='ListText'><div class='SolidText'><a href=\"Master.jsp?action=map_view&mapname=" + aMap.getFileName() + "\">" + aMap.getNameFull() + "</a></div></td><td id='" + RowName + "3' class='ListText'><div class='SolidText'>" + aMap.getFileName() + "</div></td><td id='" + RowName + "4' class='ListText'><div class='SolidText'>" + aMap.getNrPlanets() + "</div></td><td id='" + RowName + "5' class='ListText'><div class='SolidText'>" + aMap.getChangedDate() + "</div></td><td id='" + RowName + "6' class='ListText'><div class='SolidText'>" + aMap.getAuthorName() + "</div></td><td id='" + RowName + "7' class='ListText'><div class='SolidText'></div></td></tr>\n";
		}
		return retStr;
	}
	
	public static String getMapDraftFiles(User aUser){
		Logger.finer("getMapDraftFiles called, user: " + aUser.getLogin());
		String retStr = "";
		List<Map> tmpMaps = MapHandler.getMapDrafts(aUser.getLogin());
		Collections.sort(tmpMaps);
		Logger.finer("tmpMaps.size(): " + tmpMaps.size());
		for (Map aMap : tmpMaps) {
//			LoggingHandler.finer("in loop: " + aMap.getName());
			retStr = retStr + "<tr><td></td><td>" + aMap.getNameFull() + "</td><td>" + aMap.getFileName() + "</td><td>" + aMap.getNrPlanets() + "</td><td>" + aMap.getChangedDate() + "</td><td><a href=\"MapEditor.jsp?action=" + TransferWrapper.LOAD_DRAFT + "&mapname=" + aMap.getFileName() + "\" target=\"_top\">Edit</a> / <a href=\"map_files.jsp?action=delete&mapname=map." + aMap.getAuthorLogin() + "." + aMap.getFileName() + "\">Delete</a></td></tr>\n";
		}
		return retStr;
	}

	public static String getMapDraftFilesNO(User aUser){
		Logger.finer("getMapDraftFiles called, user: " + aUser.getLogin());
		String retStr = "";
		List<Map> tmpMaps = MapHandler.getMapDrafts(aUser.getLogin());
		Collections.sort(tmpMaps);
		Logger.finer("tmpMaps.size(): " + tmpMaps.size());
		int i=0;
		for (Map aMap : tmpMaps) {
			i = i + 1;
			String RowName = i + "MapDraftListRow";
			String editStr = "";
			if (aMap.getAuthorLogin().equals(aUser.getLogin())){
				editStr = "<a href=\"MapEditor.jsp?action=" + TransferWrapper.LOAD_DRAFT + "&mapname=" + aMap.getFileName() + "\" target=\"_top\">Edit</a>";
				editStr = editStr + " / ";
				editStr = editStr + "<a href=\"map_confirm_delete.jsp?mapname=" + aMap.getFileName() + "\">Delete</a>";
			}else{
				editStr = "<a href=\"MapEditor.jsp?action=" + TransferWrapper.LOAD_DRAFT + "&mapname=" + aMap.getFileName() + "\" target=\"_top\">Copy & Edit</a>";
			}
			
			retStr = retStr + "<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',7,1);\" onMouseOut=\"TranparentRow('" + RowName + "',7,0);\"><td id='" + RowName + "1' class='ListText'><div class='SolidText'></div></td><td id='" + RowName + "2' class='ListText'><div class='SolidText'><a href=\"Master.jsp?action=map_view&mapname=" + aMap.getFileName() + "\">" + aMap.getNameFull() + "</a></div></td><td id='" + RowName + "3' class='ListText'><div class='SolidText'>" + aMap.getFileName() + "</div></td><td id='" + RowName + "4' class='ListText'><div class='SolidText'>" + aMap.getNrPlanets() + "</div></td><td id='" + RowName + "5' class='ListText'><div class='SolidText'>" + aMap.getChangedDate() + "</div></td><td id='" + RowName + "6' class='ListText'><div class='SolidText'>" + aMap.getAuthorName() + "</div></td><td id='" + RowName + "7' class='ListText'><div class='SolidText'></div></td></tr>\n";
			//retStr = retStr + "<tr><td></td><td>" + aMap.getNameFull() + "</td><td>" + aMap.getFileName() + "</td><td>" + aMap.getNrPlanets() + "</td><td>" + aMap.getChangedDate() + "</td><td><a href=\"MapEditor.jsp?action=" + MapEditorPanel.LOAD_DRAFT + "&mapname=" + aMap.getFileName() + "\" target=\"_top\">Edit</a> / <a href=\"map_files.jsp?action=delete&mapname=map." + aMap.getAuthorLogin() + "." + aMap.getFileName() + "\">Delete</a></td></tr>\n";
		}
		return retStr;
	}
	
	
	public static String getMapHTML(){
		Logger.finer("getMapHTML called");
		String retStr = "";
		List<Map> allMapNames = MapHandler.getAllMaps();
		Collections.sort(allMapNames);
		Logger.finer("allMapNames.size(): " + allMapNames.size());
		for (Map aMap : allMapNames) {
			retStr = retStr + "<option value=\"" + aMap.getFileName() + "\">" + aMap.getNameFull() + " (" + aMap.getFileName() + ")</option>\n";
		}
		return retStr;
	}

	/**
	 * Return a List containing map names of all maps on disc
	 * @param folderPath
	 * @return List with map name Strings
	 */
	private static List<String> getProps(String folderPath){
		Logger.fine("MapHandler.getProps: folderPath=" + folderPath);
		List<String> allMapNames = new LinkedList<String>();
		File propFolder = new File(folderPath);
		if (propFolder.exists()){
			File[] propFiles = propFolder.listFiles(new MapFileNameFilter()); 
			for (int i = 0; i < propFiles.length; i++) {
				File file = propFiles[i];
				String mapName = extractMapName(file.getName());
				allMapNames.add(mapName);
			}
		}
		return allMapNames;
	}
	
	private static String extractMapName(String propFileName){
//		int index1 = propFileName.indexOf(".");
		int index2 = propFileName.lastIndexOf(".");
//		String mapName = propFileName.substring(index1+1,index2);
		String mapName = propFileName.substring(0,index2);
		return mapName;
	}

	public static void main(String[] args){
		List<Map> aList = getAllMaps();
		for (Map aMap : aList) {
			System.out.println(aMap.getFileName());
		}
//		double sqrt = Math.sqrt(16);
//		int floor = (int)Math.round(Math.floor(sqrt));
//		System.out.println(floor + 1);
	}

	public static String showMapNO(String mapName){
		MapImageCreator mic = new MapImageCreator();
		Map aMap = getMap(mapName,null);
//		Dimension d = mic.createGifAndGetSize(mapName,aMap); verkar inte som om d beh�vs?
		mic.createGifAndGetSize(mapName,aMap);
		String retStr = "<img src=\"images/maps/" + mapName + ".gif\" width=\"700\">";
		return retStr;
	}

	
	public static String showMap(String mapName){
		MapImageCreator mic = new MapImageCreator();
		Map aMap = getMap(mapName,null);
		Dimension d = mic.createGifAndGetSize(mapName,aMap);
		String retStr = "<img src=\"images/maps/" + mapName + ".gif\" width=\"" + d.width + "\" height=\"" + d.height + "\">";
		return retStr;
	}
	
	/**
	 * 
	 * @param aMap
	 * @param path
	 * @return
	 */
	public static String saveMapToFile(Map aMap, String path){
		Logger.fine("saveMapToFile called(): " + path + " " + aMap.getFileName());
		Logger.fine("playerLogin:: " + aMap.getAuthorLogin());
		String success = null;
		// create complete path to file to save
		if (dataPath == null){
			dataPath = PropertiesHandler.getProperty("datapath");
		}
		Logger.finer("dataPath: " + dataPath);
		String completePath = dataPath + "maps\\" + path;
		Logger.finer("completePath: " + completePath);
		// check if file can be created
		File aFile = new File(completePath);
		if (!aFile.exists() || aFile.canWrite()){
			// create file content (String for textfile)
			String mapData = aMap.getMapData();
			// save file
			writeFile(aFile,mapData);
			success = TransferWrapper.MAP_SAVED;
			deleteMapImageFile(aMap.getFileName());
			reloadMaps();
		}else{
			success = TransferWrapper.MAP_NOT_SAVED;
		}
		return success;
	}
	
	private static void deleteMapImageFile(String mapName){
		System.out.println("deleteMapImageFile called, mapName:" + mapName);
		String basePath = PropertiesHandler.getProperty("basepath");
		String exactPath = basePath + "webb2\\images\\maps\\" + mapName + ".gif";
		File picFile = new File(exactPath);
		if (picFile.exists()){
			System.out.println("map image deleted: " + exactPath);
			picFile.delete();
		}
	}

	private static void writeFile(File aFile, String content){
		try{
			FileWriter fw = new FileWriter(aFile);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(content);
			pw.close();
			Logger.info("Map written to file: " + aFile.getPath());
		}
		catch(IOException ioe){
			Logger.severe("Error while saving map");
			ioe.printStackTrace();
		}

	}
	
	public static String deleteMap(String mapFileName){
		System.out.println("deleteMap called, mapFileName: " + mapFileName);
		String exactPath = PropertiesReader.getExactPath(null,mapFileName + ".properties", false);
		System.out.println("exactPath: " + exactPath);
		String message = null;
		if (exactPath != null){
			File remFile = new File(exactPath);
			if (remFile.canWrite()){
				remFile.delete();
				if (remFile.exists()){
					message = "<font color=\"#FF0000\">Map was not deleted</font>";
				}else{
					int index = mapFileName.indexOf(".");
					String tmpMapName = mapFileName.substring(index+1);
					deleteMapImageFile(tmpMapName);
					reloadMaps();
					// TODO (Paul) should aso remove old image/gif of the map if it exists
					message = "<font color=\"#00FF00\">Map has been deleted</font>";
				}
			}else{
				message = "<font color=\"#FF0000\">Can not delete map</font>";
			}
		}else{
			message = "<font color=\"#FF0000\">Can not find map to remove</font>";
		}
		return message;
	}
	
	public static int getMapsNr(){
		int nr = 0;
		List<Map> maps = getAllMaps();
		if (maps != null){
			nr = maps.size();
		}
		return nr;
	}

	/**
	 * Returns the first of the maps with the highest counter 
	 * @return
	 */
	public static Map getMostUsedMap(){
		List<Map> maps = getAllMaps();
		Map foundMap = null;
		int foundCounter = -1;
		for (Map aMap : maps) {
			int tmpCounter = PHash.getCounter("game.finished.map." + aMap.getFileName());
			if (foundMap == null){
				foundMap = aMap;
				foundCounter = tmpCounter;
			}else
			if(tmpCounter > foundCounter){
				foundMap = aMap;
				foundCounter = tmpCounter;
			}
		}
		return foundMap;
	}

	public static String getMostUsedMapName(){
		Map foundMap = getMostUsedMap();
		String mapName = "No maps found";
		if (foundMap != null){
			mapName = foundMap.getNameFull();
		}
		return mapName;
	}
	
	public static int getSteps(String mapName, String maxNrPlayersString){
		int steps = 0;
		int maxNrPlayers = Integer.parseInt(maxNrPlayersString);
    	Map theMap = MapHandler.getMap(mapName);
    	int nrPlanets = theMap.getNrPlanets();
    	double planetsPerPlayerRatio = nrPlanets*1.0/maxNrPlayers;
    	if (planetsPerPlayerRatio < 2){
    		steps = 0;
    	}else
    	if (planetsPerPlayerRatio < 4){
    		steps = 1;
    	}else
    	if (planetsPerPlayerRatio < 6){
    		steps = 2;
    	}else
    	if (planetsPerPlayerRatio < 8){
    		steps = 3;
    	}else
    	if (planetsPerPlayerRatio < 16){
    		steps = 4;
    	}else{ // planetsPerPlayerRatio >= 16
    		double sqrt = Math.sqrt(planetsPerPlayerRatio);
    		int floor = (int)Math.round(Math.floor(sqrt));
    		steps = floor + 1;
    	}
		return steps;
	}
	
	public static String getSizeText(String mapName){
		return getSizeText(MapHandler.getMap(mapName));
	}

	public static String getSizeText(Map map){
		String sizeText = null;
		int nrPlayersMax = map.getMaxNrStartPlanets();
		switch (nrPlayersMax){
			case 2: 						sizeText = "duel"; break;
			case 3: case 4: 				sizeText = "small"; break;
			case 5: case 6: 				sizeText = "medium"; break;
			case 7: case 8: case 9: case 10: sizeText = "large"; break;
			default: 						sizeText = "huge";
		}
		return sizeText;
	}

}
