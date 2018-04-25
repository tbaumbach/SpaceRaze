package sr.server.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import sr.general.logging.Logger;
import sr.server.properties.PropertiesHandler;

/**
 * Own implementation of a simple persistent hashtable. 
 * Primarily to be used with numeric counters. 
 * Serializes data to files. 
 * Keys are strings and are used to create file paths.
 * 
 * Keys used:
 * ----------
 * pageloaded.JSPPAGE - each time the JSPPAGE is loaded
 * pageloaded.JSPPAGE.player.PLAYER_LOGIN - each time the JSPPAGE is loaded
 * battlesim.performed - each time battle sim is started
 * battlesim.performed.gameworld.GAMEWORLD_FILENAME - each time battle sim is started
 * game.finished.total - each time a game is finished
 * game.finished.allrazed - each time a game is finished - and all planets are razed
 * game.finished.nowinner - each time a game is finished and there is no winner
 * game.finished.gameworld.GAMEWORLD_FILENAME - each time a game is finished
 * game.finished.factionwin.GAMEWORLD_FILENAME.FACTION_NAME - each time a game is finished and a faction has won
 * game.finished.singlewin.GAMEWORLD_FILENAME.FACTION_NAME - each time a game is finished and a single player has won
 * game.finished.map.MAP_FILENAME - each time a game is finished
 * game.turn_performed.total - each time a game is updated to a new turn
 * game.turn_performed.player.PLAYER_LOGIN - each time a game is updated to a new turn and the player is not defeated
 * 
 * @author wmpabod
 *
 */
public class PHash {
	private static Hashtable<String,Integer> hash;
	private static String path;
	private static boolean enabled = true; // Phash have to be disabled when code is run on clients and not on the server

	/**
	 * Singleton method to initialize hashtable if not done already
	 * @return
	 */
	private static Hashtable<String,Integer> getHashtable(){
		if (hash == null){
			hash = new Hashtable<String,Integer>();
			readData();
		}
		return hash;
	}
	
	/**
	 * Read all data found in files and add them with correct keys in hashtable
	 *
	 */
	private static void readData(){
		String basePath = PropertiesHandler.getProperty("datapath");
		path = basePath + "persistence";
		readDir(path,"");
	}
	
	/**
	 * Read all datafiles in dirPath and call this method recursively with any directories found in dirPath
	 * @param dirPath Directory to search for datafiles
	 */
	private static void readDir(String dirPath,String keyPrefix){
		Logger.finest("readDir called: " + dirPath + " " + keyPrefix);
		File dir = new File(dirPath);
		File[] filesInDir = dir.listFiles(new PHashFileFilter());
		// add all found files to hashtable
		for (File aDataFile : filesInDir) {
			Integer i = loadFile(aDataFile);
			String keySuffix = aDataFile.getName().substring(0,aDataFile.getName().indexOf("."));
			Logger.finest("keySuffix: " + keySuffix);
			if (keyPrefix.equals("")){
				hash.put(keySuffix,i);
			}else{
				hash.put(keyPrefix + "." + keySuffix,i);
			}
		}
		// find all dirs in dirPath
		filesInDir = dir.listFiles();
		for (File aFileOrDir : filesInDir) {
			if (aFileOrDir.isDirectory()){
				// make recursive call
				String tempDirPath = dirPath + File.separator + aFileOrDir.getName();
				String tempKeyPrefix = null;
				if (keyPrefix.equals("")){
					tempKeyPrefix = aFileOrDir.getName();
				}else{
					tempKeyPrefix = keyPrefix + "." + aFileOrDir.getName();
				}
				readDir(tempDirPath,tempKeyPrefix);
			}
		}
	}
	
	/**
	 * Used to fetch Integer data as int
	 * @return int representation of the data found in key
	 */
	public static int getCounter(String key){
		int retInt = 0;
		Hashtable<String,Integer> ht = getHashtable();
		Integer valueObject = ht.get(key);
		if (valueObject != null){
			retInt = valueObject.intValue();
		}
		return retInt;
	}
	
	/**
	 * Increate a record in the hashtable which must contain an Integer.
	 * @param key key to an Integer record in the hashtable
	 */
	public static void incCounter(String key){
		if (enabled){
			Logger.finest("incCounter: " + key);
			Hashtable<String,Integer> ht = getHashtable();
			Integer valueObject = ht.get(key);
			if (valueObject != null){
				int retInt = valueObject.intValue();
				retInt++;
				putInHashtable(key,new Integer(retInt));
			}else{
				// create new entry
				putInHashtable(key,new Integer(1));
			}
		}
	}
	
	/**
	 * Put the value object in the hashtable.
	 * Will overwrite any previus value in the hashtable with the same key.
	 * @param key A String key
	 * @param value A serializable object
	 */
	public static void setValue(String key,Object value){
		putInHashtable(key,value);
	}

	/**
	 * Perform a put on the value object to the hashtable, and save the object to a datafile (.srp)
	 * @param key A String key
	 * @param value A serializable object
	 */
	@SuppressWarnings(value={"unchecked"})
	private static synchronized void putInHashtable(String key,Object value){
		Logger.finest("putInHashtable: " + key + " " + value);
		Hashtable ht = getHashtable();
		ht.put(key,value);
		// save file
		saveFile(key,value);
	}
	
	/**
	 * Loads a specified data file and de-serialises it into a Object.
	 * @param aDataFile A .srp data file containing a zipped serialized object
	 * @return The object in the data file
	 */
    private static Integer loadFile(File aDataFile){
    	Logger.finest("loadGalaxy called: " + aDataFile.getAbsolutePath());
    	Integer i = null;
        try{
            FileInputStream fis = new FileInputStream(aDataFile);
            GZIPInputStream gzis = new GZIPInputStream(fis); 
            ObjectInputStream ois = new ObjectInputStream(gzis);
            try{
                i = (Integer)ois.readObject();
            }catch(ClassNotFoundException e){
        		Logger.severe(e.toString());
            }
            ois.close();
            Logger.finest("Object loaded successfully");
        }
        catch(IOException e){
        	Logger.info("aDataFile: " + aDataFile.getName());
        	Logger.severe("Object not loaded successfully: " + e);
        }
        return i;
    }
	
    private static void saveFile(String key,Object value){
    	Logger.finest("saveFile called: " + key + " " + value.toString());
    	getHashtable(); // called to ensure that path has been set
        String fn = path + File.separator + key.replace(".",File.separator) + ".srp";
    	Logger.finest("Complete save file path: " + fn);
    	// check if dir exists
    	String dirPath = fn.substring(0,fn.lastIndexOf(File.separator));
    	Logger.finest("Save dir path: " + dirPath);
    	File dirFile = new File(dirPath); 
    	// if necessary, create the directories needed
    	if (!dirFile.exists()){
    		dirFile.mkdirs();
    	}
        try{
            FileOutputStream fos = new FileOutputStream(fn);
            GZIPOutputStream gzos = new GZIPOutputStream(fos); 
            ObjectOutputStream oos = new ObjectOutputStream(gzos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            Logger.finest("Value saved successfully");
        }
        catch(IOException e){
        	Logger.severe("Value not saved successfully: " + e);
        }
    }
    
    /**
     * Print the entire hashtable in a html table
     * @return String containing a HTML table with all records in the hashtable
     */
    public static String getHashtableHTML(){
    	String retVal = "<table>\n";
    	Hashtable<String,Integer> ht = getHashtable();
    	List<String> keyList = new LinkedList<String>();
    	for (Enumeration<String> e = ht.keys() ; e.hasMoreElements() ;) {
            String tmpKey = e.nextElement();
            keyList.add(tmpKey);
        }
    	Collections.sort(keyList);
    	for (String aKey : keyList) {
        	retVal += "<tr>\n";
        	retVal += "<td>\n";
        	retVal += aKey + "\n";
        	retVal += "</td>\n";
        	retVal += "<td>\n";
        	retVal += ht.get(aKey).toString() + "\n";
        	retVal += "</td>\n";
        	retVal += "</tr>\n";
		}
    	retVal += "</table>\n";
    	return retVal;
    }

    /**
     * Dumps all content of the hashtable on System.out
     * @return
     */
    public static void dumpHashtable(){
    	Logger.finer("dumpHashtable");
    	Hashtable<String,Integer> ht = getHashtable();
    	List<String> keyList = new LinkedList<String>();
    	for (Enumeration<String> e = ht.keys() ; e.hasMoreElements() ;) {
            String tmpKey = (String)e.nextElement();
            keyList.add(tmpKey);
        }
    	Collections.sort(keyList);
    	for (String aKey : keyList) {
    		Logger.finer(aKey + " " + ht.get(aKey).toString());
		}
    }

	public static boolean isEnabled() {
		return enabled;
	}

	public static void setEnabled(boolean enabled) {
		PHash.enabled = enabled;
	}

}
