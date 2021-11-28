package sr.server.persistence;

import java.io.File;
import java.io.FilenameFilter;

import spaceraze.util.general.Logger;

/**
 * File filter used to find datafiles for PHash class
 * @author wmpabod
 *
 */
public class PHashFileFilter implements FilenameFilter {

	/**
	 * Will return true for all files with filesuffix ".srp" (= SpaceRaze Persistence)
	 */
	public boolean accept(File dir, String fileName) {
		Logger.finest("PHashFileFilter.accept called: " + dir.getName() + " " + fileName);
		boolean fileOk = false;
		int index = fileName.lastIndexOf(".");
		if (index > -1){
			String suffix = fileName.substring(index+1);
			if (suffix.equalsIgnoreCase("srp")){
				fileOk = true;
				Logger.finest("ok");
			}
		}
		return fileOk;
	}

}
