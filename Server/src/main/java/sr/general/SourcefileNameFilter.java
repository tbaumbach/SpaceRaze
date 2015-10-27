/*
 * Created on 2005-feb-19
 */
package sr.general;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author wmpabod
 *
 * Used to filter Java source files
 */
public class SourcefileNameFilter implements FilenameFilter {

	public boolean accept(File notUsed, String filename) {
		int index = filename.lastIndexOf(".");
		boolean found = false;
		if (index > -1){
			String suffix = filename.substring(index);
			if (suffix.equalsIgnoreCase(".java")){
				found = true;
			}
		}
		return found;
	}

}
