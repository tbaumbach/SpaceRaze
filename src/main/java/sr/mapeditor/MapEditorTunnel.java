/*
 * Created on 2005-jun-29
 */
package sr.mapeditor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import spaceraze.servlethelper.map.TransferWrapper;
import spaceraze.util.general.Logger;
import spaceraze.util.properties.PropertiesHandler;
import spaceraze.world.Map;
import sr.server.map.MapHandler;
import sr.webb.users.User;
import sr.webb.users.UserHandler;

/**
 * @author WMPABOD
 *
 * Used to tunnel calls from map editors to the server
 */
@SuppressWarnings("serial")
public class MapEditorTunnel extends HttpServlet {

	public void init(ServletConfig c) throws ServletException {
		super.init (c);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response )throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.fine("MapEditorTunnel doPost called");
		
		ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(request.getInputStream()));
		Logger.fine("inputStream created");
		spaceraze.servlethelper.map.TransferWrapper tw = null;
		try {
			Logger.fine("Waiting to read...");
			tw = (spaceraze.servlethelper.map.TransferWrapper)inputStream.readObject();
			Logger.fine("tw read: " + tw.getAction() + " " + tw.getPlayerLogin());
			if(tw != null){ 
				String action = tw.getAction();
				if(action.equals(TransferWrapper.LOAD_DRAFT)){
					tw.setMessage(TransferWrapper.LOAD_DRAFT);
					tw.setMap(MapHandler.getMap(tw.getMapFileName(),tw.getPlayerLogin()));
				}else
				if(action.equals(TransferWrapper.LOAD_PUB)){
					tw.setMessage(TransferWrapper.LOAD_PUB);
					Map aMap = MapHandler.getMap(tw.getMapFileName(),null).getCopyFromFile();
					// always set author, since the map can be a "copy" of another players published map
					User curUser = UserHandler.findUser(tw.getPlayerLogin());
					aMap.setAuthor(curUser.getLogin());
					aMap.setAuthorName(curUser.getName());
					// set map
					tw.setMap(aMap);
				}else
				if(action.equals(TransferWrapper.SAVE_DRAFT)){
					// if needed, create player map folder
					String basePath = PropertiesHandler.getProperty("datapath");
					String folderPath = basePath + "maps\\" + tw.getPlayerLogin();
					System.out.println("folderPath: " + folderPath);
					File folderFile = new File(folderPath);
					if (!folderFile.exists()){
						Logger.fine("!folderFile.exists(), creating folder");
						folderFile.mkdir();
					}
					// create success string variable
					String success = null;
					// check if file exists with same name
					Map existingMap = MapHandler.getMap(tw.getMapFileName(),tw.getPlayerLogin());
					if ((existingMap == null) || tw.isOwerwriteConfirm()){
						System.out.println("existingMap: " + existingMap);
						System.out.println("tw.isOwerwriteConfirm(): " + tw.isOwerwriteConfirm());
						// save map
						String path = tw.getPlayerLogin() + File.separator + tw.getMapFileName() + ".properties";
						// set user name
						Map theMap = tw.getMap();
						User curUser = UserHandler.findUser(theMap.getAuthor());
						theMap.setAuthorName(curUser.getName());
						// save the map
						success = MapHandler.saveMapToFile(theMap,path);
					}else{
						success = TransferWrapper.COMFIRM_NEEDED;
					}
					tw.setMessage(success);
				}else
				if(action.equals(TransferWrapper.SAVE_PUB)){
					// set user name
					Map theMap = tw.getMap();
					User curUser = UserHandler.findUser(theMap.getAuthor());
					String success = null;
					// check that there are not already a published map from another player with the same name
					Map anotherMap = MapHandler.getMap(tw.getMapFileName());
					if (anotherMap == null){ // ok to save map
						theMap.setAuthorName(curUser.getName());
						theMap.incVersionId();
						// save map
						String path = tw.getMapFileName() + ".properties";
						success = MapHandler.saveMapToFile(theMap,path);
					}else
					if (anotherMap.getAuthor().equals(curUser.getLogin())){ // ok to save map, if confirm overwrite exists
						// check if file exists with same name
						if (tw.isOwerwriteConfirm()){
							System.out.println("pub existingMap: " + anotherMap);
							System.out.println("pub tw.isOwerwriteConfirm(): " + tw.isOwerwriteConfirm());
							// save map
							String path = tw.getMapFileName() + ".properties";
							// set user name
//							Map theMap = tw.getMap();
//							User curUser = UserHandler.findUser(theMap.getAuthorLogin());
							theMap.setAuthorName(curUser.getName());
							theMap.incVersionId();
							// save the map
							success = MapHandler.saveMapToFile(theMap,path);
						}else{
							success = TransferWrapper.COMFIRM_NEEDED;
						}
					}else{
						success = "Cannot publish map with this filename, another player\nalready has a published map with the name " + tw.getMapFileName();
					}
					tw.setMessage(success);
				}
			}
		} catch( ClassNotFoundException ex ) {
			ex.printStackTrace();
		}
		// send response
		response.setStatus(HttpServletResponse.SC_OK);

		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(response.getOutputStream()));
		oos.writeObject(tw);
		oos.flush();
	}


}
