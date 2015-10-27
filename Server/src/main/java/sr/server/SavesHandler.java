/*
 * Created on 2005-feb-19
 */
package sr.server;

import java.io.File;

import sr.general.logging.Logger;
import sr.server.properties.PropertiesHandler;

/**
 * @author wmpabod
 *
 * Enable admins to download savefiles 
 */
public class SavesHandler {
	
	public String getActiveSaves(){
		String saves = "";
		// get path
		String basePath = PropertiesHandler.getProperty("basepath");
		String savesPath = basePath + "saves";
		saves = getSaves(savesPath,"..\\saves\\");
		return saves;
	}

	public String getPreviousSaves(){
		String saves = "";
		// get path
		String basePath = PropertiesHandler.getProperty("basepath");
		String savesPath = basePath + "saves\\previous";
		saves = getSaves(savesPath,"..\\saves\\previous\\");
		return saves;
	}

	/**
	 * 
	 * @param folderPath
	 * @return
	 */
	private String getSaves(String folderPath, String folderUrl){
		Logger.fine("SavesHandler.getSaves: folderPath=" + folderPath + ", folderUrl=" + folderUrl);
		String saves = "";
		File savesFolder = new File(folderPath);
		File[] saveFiles = savesFolder.listFiles(new SavefileNameFilter()); 
		for (int i = 0; i < saveFiles.length; i++) {
			File file = saveFiles[i];
			saves = saves + "<a href=" + folderUrl + file.getName() + ">" + file.getName() + "</a><br>\n";
		}
		return saves;
	}

}
