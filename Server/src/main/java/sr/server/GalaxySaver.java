package sr.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import sr.general.logging.Logger;
import sr.server.properties.PropertiesHandler;
import sr.world.Galaxy;

public class GalaxySaver{ 

    public void saveGalaxy(String filename, String path, Galaxy g){
    	Logger.info("saveGalaxy called: " + filename + " " + path);		
    	String dataPath = PropertiesHandler.getProperty("datapath");
        String fn = dataPath + path + "/" + filename + ".srg";
    	Logger.finer("Complete save file path: " + fn);
        try{
            FileOutputStream fos = new FileOutputStream(fn);
            GZIPOutputStream gzos = new GZIPOutputStream(fos); 
            ObjectOutputStream oos = new ObjectOutputStream(gzos);
            oos.writeObject(g);
            oos.flush();
            oos.close();
            Logger.finer("Galaxy saved successfully");
        }
        catch(IOException e){
        	Logger.severe("Galaxy not saved successfully: " + e);
        }
    }
}
