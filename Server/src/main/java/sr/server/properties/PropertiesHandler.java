/*
 * Created on 2005-jan-12
 */
package sr.server.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import sr.server.PropfileNameFilter;
import sr.server.ranking.RankingHandler;

/**
 * @author wmpabod
 *
 * Hanterar alla properties
 */
public class PropertiesHandler {
//	private static Properties properties;
	private static final String DEFAULT_PROPERTIES_NAME = "spaceraze";
	
	public static String getProperty(String key){
//		System.out.println("getProperty: key = " + key);
		String retVal = "";
		Properties properties = PropertiesHandler.getInstance();
		String tmpValue = properties.getProperty(key);
		if (tmpValue != null){
			retVal = tmpValue;
		}else{
			retVal = "";
		}
		return retVal;
	}
	
	/**
	 * Used by Map.class
	 * @param propName example "map.wigge3" to load "map.wigge3.properties"
	 * @return
	 */
	public static Properties getInstance(String propFilePrefixName){
//		System.out.println("getInstance: propFilePrefixName = " + propFilePrefixName);
		Properties properties = PropertiesReader.getProperties(propFilePrefixName, false);
		return properties;
	}

	/**
	 * This method always fetches the Properties objects data from loadCashedParams, 
	 * since the data sought after never change during runtime.
	 * @return
	 */
	private static Properties getInstance(){
		Properties properties = null;
		try {
			properties = loadCashedParams(DEFAULT_PROPERTIES_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	

	// used by other classes
	public static Properties loadParams(String file) throws IOException {
		return PropertiesReader.getProperties(file, true);
	}
	
	
	/**
	 * Loads properties through ResourceBundle, which cashes data
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static Properties loadCashedParams(String file) throws IOException {
		// Loads a ResourceBundle and creates Properties from it
		Properties prop = new Properties();
		ResourceBundle bundle = ResourceBundle.getBundle(file);
		Enumeration<String> tmpenum = bundle.getKeys();

		String key = null;
		while (tmpenum.hasMoreElements()) {
			key = tmpenum.nextElement();
//			System.out.println("Reading from bundle: " + key + " = " + (String)bundle.getObject(key));
			prop.put(key, bundle.getObject(key));
		}

		return prop;
	}

	public String getPropertyFiles(){
		String props = "";
		// get path
		String basePath = PropertiesHandler.getProperty("basepath");
		String propPath = basePath + "WEB-INF\\classes";
		props = getProps(propPath);
		return props;
	}

	/**
	 * Return a string containing html links to all properties files
	 * @param folderPath
	 * @return
	 */
	private String getProps(String folderPath){
//		System.out.println("PropertiesHandler.getProps: folderPath=" + folderPath);
		String props = "";
		File propFolder = new File(folderPath);
		File[] propFiles = propFolder.listFiles(new PropfileNameFilter()); 
		for (int i = 0; i < propFiles.length; i++) {
			File file = propFiles[i];
			props = props + "<a href=view_props.jsp?propname=" + file.getName() + ">" + file.getName() + "</a><br>\n";
		}
		return props;
	}
	
	public static String getPropertiesContent(String filename){
//		System.out.println("getPropertiesContent: filename = " + filename);
		String contents = "";
		String basePath = PropertiesHandler.getProperty("basepath");
		String completePath = basePath + "WEB-INF\\classes\\" + filename;
		File logFile = new File(completePath);
		List<String> fileContents = readFile(logFile);
		for (String aRow : fileContents) {
			contents = contents + aRow + "<br>";
		}
		return contents;
	}

	/**
	 * Used for editing contents in a properties file
	 * @param filename
	 * @return
	 */
	public static String getPropertiesContent2(String filename){
//		System.out.println("getPropertiesContent2: filename = " + filename);
		String contents = "";
		String basePath = PropertiesHandler.getProperty("basepath");
		String completePath = basePath + "WEB-INF\\classes\\" + filename;
		File logFile = new File(completePath);
		List<String> fileContents = readFile(logFile);
		for (String aRow : fileContents) {
			contents = contents + aRow + "\n";
		}
		return contents;
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

	public static void saveFile(String fileName, String newContent){
		String basePath = PropertiesHandler.getProperty("basepath");
		String completePath = basePath + "WEB-INF\\classes\\" + fileName;
		File propFile = new File(completePath);		
		writeFile(propFile, newContent);
		if (fileName.equals("ranking.properties")){
			RankingHandler.reloadList();
		}
	}

	private static void writeFile(File aFile, String newContent){
		try{
			FileWriter fw = new FileWriter(aFile);
			PrintWriter pw = new PrintWriter(fw);
//			StringTokenizer st = new StringTokenizer(newContent,"\n");
//			while(st.hasMoreTokens()){
//				pw.println(st.nextToken());
//			}
			pw.print(newContent);
			pw.close();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
