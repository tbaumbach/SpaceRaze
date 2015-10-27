/*
 * Created on 2005-feb-19
 */
package sr.server;

import java.io.File;
import java.io.FilenameFilter;

import sr.general.logging.Logger;

/**
 * @author wmpabod
 *
 * Used to filter SpaceRaze properties files
 */
public class PropfileNameFilter implements FilenameFilter {

	public boolean accept(File notUsed, String filename) {
		Logger.finest("PropfileNameFilter, filename: " + filename);
		int index = filename.lastIndexOf(".");
		boolean found = false;
		if (index > -1){
			String suffix = filename.substring(index);
			Logger.finest("suffix: " + suffix);
			if (suffix.equalsIgnoreCase(".properties")){
				found = true;
			}
		}
		return found;
	}

}
