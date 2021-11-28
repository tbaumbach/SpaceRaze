/*
 * Created on 2005-feb-19
 */
package sr.server;

import java.io.File;
import java.io.FilenameFilter;

import spaceraze.util.general.Logger;

/**
 * @author wmpabod
 *
 * Used to filter SpaceRaze properties files
 */
public class MapFileNameFilter implements FilenameFilter {

	public boolean accept(File notUsed, String filename) {
		Logger.finest("MapFileNameFilter, filename: " + filename);
		// first check ends with ".properties"
		int index = filename.lastIndexOf(".");
		boolean found = false;
		if (index > -1){
			String suffix = filename.substring(index);
//			LoggingHandler.finest("suffix: " + suffix);
			if (suffix.equalsIgnoreCase(".properties")){
				found = true;
			}
		}
		/*
		// first check starts with "map."
		if (found){
			index = filename.indexOf(".");
			if (index > -1){
				String suffix = filename.substring(0,index);
				if (suffix.equalsIgnoreCase("map")){
					found = true;
				}else{
					found = false;
				}
			}else{
				found = false;
			}
		}
		*/
		return found;
	}

}
