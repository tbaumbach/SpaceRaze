package sr.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

import spaceraze.util.general.Logger;
import spaceraze.util.properties.PropertiesHandler;
import spaceraze.world.Galaxy;

public class GalaxyLoader{
    Galaxy g;

    public Galaxy loadGalaxy(String filename){
    	Logger.info("loadGalaxy called: " + filename);
		String path = PropertiesHandler.getProperty("datapath") + "saves";
    	String fn = path + File.separator + filename + ".srg";
    	Logger.finer("Complete file path: " + fn);
        try{
            FileInputStream fis = new FileInputStream(fn);
            GZIPInputStream gzis = new GZIPInputStream(fis); 
            ObjectInputStream ois = new ObjectInputStream(gzis);
            try{
                g = (Galaxy)ois.readObject();
            }catch(ClassNotFoundException e){
                System.out.println(e);
            }
            ois.close();
            Logger.finer("Galaxy loaded successfully");
        }
        catch(IOException e){
        	Logger.severe("Galaxy not loaded successfully: " + e);
        }
        return g;
    }

}
