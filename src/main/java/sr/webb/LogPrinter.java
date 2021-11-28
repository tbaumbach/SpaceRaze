/*
 * Created on 2005-feb-16
 */
package sr.webb;

import spaceraze.util.properties.PropertiesHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author WMPABOD
 *
 * This class can print system/Tomcat logs to webpages...
 */
public class LogPrinter {
	
	public static String getTomcatLog(String filename){
		StringBuffer contents = new StringBuffer();
		String basePath = PropertiesHandler.getProperty("basepath");
		String completePath = basePath + "..\\..\\logs\\" + filename;
		File logFile = new File(completePath);
		List<String> fileContents = readFile(logFile);
		for (String aRow : fileContents) {
			contents.append(aRow).append("<br>");
		}
		return contents.toString();
	}
	
	private static List<String> readFile(File aFile){
		List<String> list = new LinkedList<String>();
		try{
			FileReader fr = new FileReader(aFile);
			BufferedReader br = new BufferedReader(fr);
			String aRow = br.readLine();
			while (aRow != null){
				list.add(aRow);
				aRow = br.readLine();
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
