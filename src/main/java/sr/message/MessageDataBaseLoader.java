package sr.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

import spaceraze.util.general.Logger;
import spaceraze.util.properties.PropertiesHandler;

public class MessageDataBaseLoader{
	MessageDatabase aMessageDatabase;

    public MessageDatabase loadMessageDatabase(String filename){
    	Logger.info("loadMessageDatabase called: " + filename);
		String path = PropertiesHandler.getProperty("datapath") + "messageDatabase";
    	String fn = path + File.separator + filename + ".srg";
    	Logger.finer("Complete file path: " + fn);
        try{
            FileInputStream fis = new FileInputStream(fn);
            GZIPInputStream gzis = new GZIPInputStream(fis); 
            ObjectInputStream ois = new ObjectInputStream(gzis);
            try{
            	aMessageDatabase = (MessageDatabase)ois.readObject();
            }catch(ClassNotFoundException e){
                System.out.println(e);
            }
            ois.close();
            Logger.finer("MessageDatabase loaded successfully");
        }
        catch(IOException e){
        	Logger.severe("MessageDatabase not loaded successfully: " + e);
        }
        return aMessageDatabase;
    }
}
