package sr.message;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.MissingResourceException;
import java.util.zip.GZIPOutputStream;

import spaceraze.util.general.Logger;
import spaceraze.util.properties.PropertiesHandler;

public class MessageDataBaseSaver{ 

    public void saveMessageDataBase(String filename, MessageDatabase aMessageDatabase){
    	Logger.info("saveMessageDataBase called: " + filename);		
        try{
	    	String basePath = PropertiesHandler.getProperty("datapath");
	        String fn = basePath + "messageDatabase" + "/" + filename + ".srg";
	    	Logger.finer("Complete save file path: " + fn);
	        try{
	            FileOutputStream fos = new FileOutputStream(fn);
	            GZIPOutputStream gzos = new GZIPOutputStream(fos); 
	            ObjectOutputStream oos = new ObjectOutputStream(gzos);
	            oos.writeObject(aMessageDatabase);
	            oos.flush();
	            oos.close();
	            Logger.finer("MessageDataBase saved successfully");
	        }
	        catch(IOException e){
	        	Logger.severe("MessageDataBase not saved successfully: " + e);
	        }
        }
        catch(MissingResourceException e){
        	Logger.severe("MessageDataBase not saved successfully: " + e);
        	Logger.severe("This is probably because the MessageDataBaseSaver isn't running on the server");
        }
    }
}
