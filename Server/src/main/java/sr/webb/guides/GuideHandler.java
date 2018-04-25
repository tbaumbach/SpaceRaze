/*
 * Created on 2005-jun-10
 */
package sr.webb.guides;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import sr.general.logging.Logger;
import sr.server.properties.PropertiesHandler;

/**
 * @author niohl
 *
 * Handles guides articles, including saving and reading from file
 */
public class GuideHandler {
	private LinkedList<Guide> allguides;
	private int currentHighestId;
	
	public GuideHandler(){
		Logger.fine("created");
		allguides = loadguides();
		// set current Highest Id
		if (allguides != null){
			if (allguides.size() > 0){
				Guide firstNa = (Guide)allguides.getFirst();
				currentHighestId = firstNa.getId();
			}else{
				currentHighestId = 0;
			}
		}else{
			allguides = new LinkedList<Guide>();
			saveguides();
			currentHighestId = 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	private LinkedList<Guide> loadguides(){
    	LinkedList<Guide> tmpList = null;
    	Logger.info("loadguides called");
		String path = PropertiesHandler.getProperty("basepath") + "guides" + File.separator + "guides.srn";
    	Logger.finer("Complete file path: " + path);
        try{
            FileInputStream fis = new FileInputStream(path);
            GZIPInputStream gzis = new GZIPInputStream(fis); 
            ObjectInputStream ois = new ObjectInputStream(gzis);
            try{
            	tmpList = (LinkedList<Guide>)ois.readObject();
            }catch(ClassNotFoundException e){
            	Logger.fine("ClassNotFoundException: " + e.toString());
            }
            ois.close();
            Logger.finer("guides loaded successfully");
        }
        catch(IOException e){
        	Logger.severe("guides not loaded successfully: " + e);
        }
        return tmpList;
    }

    private void saveguides(){
    	Logger.info("saveguides called");		
    	String basePath = PropertiesHandler.getProperty("basepath");
        String fn = basePath + "guides" + File.separator + "guides.srn";
    	Logger.finer("Complete save guides path: " + fn);
        try{
            FileOutputStream fos = new FileOutputStream(fn);
            GZIPOutputStream gzos = new GZIPOutputStream(fos); 
            ObjectOutputStream oos = new ObjectOutputStream(gzos);
            oos.writeObject(allguides);
            oos.flush();
            oos.close();
            Logger.finer("guides saved successfully");
        }
        catch(IOException e){
        	Logger.severe("guides not saved successfully: " + e);
        }
    }
    
    public void addNewGuide(String title, String content, String creator){
    	currentHighestId++;
    	Guide na = new Guide(title,content,creator,currentHighestId);
    	allguides.addFirst(na);
    	saveguides();
    }
    
    public void modifyGuide(String title, String content, String creator,String published, int id){
    int Publish =0;
    	if ((published != null)&& (published.equals("yes")))
    	{
    		Publish = 1;
		}
    	Guide na = findGuide(id);
    	na.modify(title,content,creator,Publish);
    	saveguides();
    }

    public void deleteGuide(int id){
    	int index = findGuideIndex(id);
    	allguides.remove(index);
    	saveguides();
    }

    public Guide findGuide(int id){
    	int index = findGuideIndex(id);
    	Guide found = (Guide)allguides.get(index);
    	return found;
    }

    /**
     * Find index of a specified guides article.
     * Return -1 if not found
     * @param id id of the article to find
     * @return index of article in allguides
     */
    private int findGuideIndex(int id){
    	boolean found = false;
    	int i = 0;
    	while ((!found) && (i < allguides.size())){
    		Guide tmpNa = (Guide)allguides.get(i);
    		if (tmpNa.isGuide(id)){
    			found = true;
    		}else{
    			i++;
    		}
    	}
    	if (i == allguides.size()){
    		i = -1;
    	}
    	return i;
    }

    public List<Guide> getAllguides(){
    	return allguides;
    }
	
    public String getGuideHTML(int id){
    	String articleHTML = "";
    	Guide na = findGuide(id);
    	articleHTML = "<h3>" + na.getTitle() + "</h3>\n";
    	articleHTML = articleHTML + na.getContent() + "<br>\n";
    	articleHTML = articleHTML + "<i>Posted by: " + na.getCreator() + " - " + na.getCreatedString() + "</i><br>\n";
    	return articleHTML;
    }
    
        public String getGuideHTMLNO(int id){
    	String articleHTML = "";
    	
    	Guide na = findGuide(id);
    	na.addRead();
    	saveguides();
    	//.replace('£','?');
  //  	.replace('£','?');
    //	articleHTML.replace(char(13),"<br>");
    	
    	articleHTML = "<div class=TextArea450>" + na.getTitle() + "</h3>\n";
    	articleHTML = articleHTML + na.getContent() + "<br><br>\n";
    	articleHTML = articleHTML + "<i>Posted by: " + na.getCreator() + " - " + na.getCreatedString() + "</i><br>\n";
    	articleHTML = articleHTML + "</div>\n";
    	return articleHTML;
    }


}
