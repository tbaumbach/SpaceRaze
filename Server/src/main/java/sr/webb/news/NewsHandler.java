/*
 * Created on 2005-jun-10
 */
package sr.webb.news;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import spaceraze.util.general.Logger;
import spaceraze.util.properties.PropertiesHandler;

/**
 * @author wmpabod
 *
 * Handles news articles, including saving and reading from file
 */
public class NewsHandler {
	private LinkedList<NewsArticle> allNews;
	private int currentHighestId;
	
	public NewsHandler(){
		Logger.fine("created");
		allNews = loadNews();
		// set current Highest Id
		if (allNews != null){
			if (allNews.size() > 0){
				NewsArticle firstNa = (NewsArticle)allNews.getFirst();
				currentHighestId = firstNa.getId();
			}else{
				currentHighestId = 0;
			}
		}else{
			allNews = new LinkedList<NewsArticle>();
			saveNews();
			currentHighestId = 0;
		}
	}
	
    @SuppressWarnings("unchecked")
	private LinkedList<NewsArticle> loadNews(){
    	LinkedList<NewsArticle> tmpList = null;
    	Logger.info("loadNews called");
		String path = PropertiesHandler.getProperty("datapath") + "news" + File.separator + "articles.srn";
    	Logger.finer("Complete file path: " + path);
        try{
            FileInputStream fis = new FileInputStream(path);
            GZIPInputStream gzis = new GZIPInputStream(fis); 
            ObjectInputStream ois = new ObjectInputStream(gzis);
            try{
            	tmpList = (LinkedList<NewsArticle>)ois.readObject();
            }catch(ClassNotFoundException e){
            	Logger.fine("ClassNotFoundException: " + e.toString());
            }
            ois.close();
            Logger.finer("News loaded successfully");
        }
        catch(IOException e){
        	Logger.severe("News not loaded successfully: " + e);
        }
        return tmpList;
    }

    private void saveNews(){
    	Logger.info("saveNews called");		
    	String dataPath = PropertiesHandler.getProperty("datapath");
        String fn = dataPath + "news" + File.separator + "articles.srn";
    	Logger.finer("Complete save news path: " + fn);
        try{
            FileOutputStream fos = new FileOutputStream(fn);
            GZIPOutputStream gzos = new GZIPOutputStream(fos); 
            ObjectOutputStream oos = new ObjectOutputStream(gzos);
            oos.writeObject(allNews);
            oos.flush();
            oos.close();
            Logger.finer("News saved successfully");
        }
        catch(IOException e){
        	Logger.severe("News not saved successfully: " + e);
        }
    }
    
    public void addNewArticle(String title, String content, String creator){
    	currentHighestId++;
    	NewsArticle na = new NewsArticle(title,content,creator,currentHighestId);
    	allNews.addFirst(na);
    	saveNews();
    }
    
    public void modifyArticle(String title, String content, String creator, int id){
    	NewsArticle na = findNewsArticle(id);
    	na.modify(title,content,creator);
    	saveNews();
    }

    public void deleteArticle(int id){
    	int index = findNewsArticleIndex(id);
    	allNews.remove(index);
    	saveNews();
    }

    public NewsArticle findNewsArticle(int id){
    	int index = findNewsArticleIndex(id);
    	NewsArticle found = (NewsArticle)allNews.get(index);
    	return found;
    }

    /**
     * Find index of a specified news article.
     * Return -1 if not found
     * @param id id of the article to find
     * @return index of article in allNews
     */
    private int findNewsArticleIndex(int id){
    	boolean found = false;
    	int i = 0;
    	while ((!found) && (i < allNews.size())){
    		NewsArticle tmpNa = (NewsArticle)allNews.get(i);
    		if (tmpNa.isArticle(id)){
    			found = true;
    		}else{
    			i++;
    		}
    	}
    	if (i == allNews.size()){
    		i = -1;
    	}
    	return i;
    }

    public LinkedList<NewsArticle> getAllNews(){
    	return allNews;
    }
	
    public String getNewsArticleHTML(int id){
    	String articleHTML = "";
    	NewsArticle na = findNewsArticle(id);
    	articleHTML = "<h3>" + na.getTitle() + "</h3>\n";
    	articleHTML = articleHTML + na.getContent() + "<br>\n";
    	articleHTML = articleHTML + "<i>Posted by: " + na.getCreator() + " - " + na.getCreatedString() + "</i><br>\n";
    	return articleHTML;
    }
    
        public String getNewsArticleHTMLNO(int id){
    	String articleHTML = "";
    	NewsArticle na = findNewsArticle(id);
    	articleHTML = "<div class=TextArea450>" + na.getTitle() + "</h3>\n";
    	articleHTML = articleHTML + na.getContent() + "<br>\n";
    	articleHTML = articleHTML + "<i>Posted by: " + na.getCreator() + " - " + na.getCreatedString() + "</i><br>\n";
    	articleHTML = articleHTML + "</div>\n";
    	return articleHTML;
    }

}
