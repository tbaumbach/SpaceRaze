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
 * Used to filter SpaceRaze files
 */
public class SavefileNameFilter implements FilenameFilter {

	public boolean accept(File notUsed, String filename) {
		Logger.finest("SavefileNameFilter, filename: " + filename);
		int index = filename.lastIndexOf(".");
		boolean found = false;
		if (index > -1){
			String suffix = filename.substring(index);
			Logger.finest("suffix: " + suffix);
			if (suffix.equalsIgnoreCase(".srg")){
				found = true;
			}
		}
		return found;
	}

}
