/*
 * Created on 2005-jun-10
 */
package sr.webb.news;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wmpabod
 *
 * One newsitem to be shown in the web
 */
public class NewsArticle implements Serializable{
	private static final long serialVersionUID = 1L;
	private String title,content,creator;
	private Date created,lastModified;
	private int id;
	
	public NewsArticle(String title, String content, String creator,int id){
		this.title = title;
		this.content = content;
		this.creator = creator;
		this.id = id;
		created = new Date();
	}
	
	public void modify(String title, String content, String creator){
		this.title = title;
		this.content = content;
		this.creator = creator;
		lastModified = new Date();
	}
	
	public String getContent() {
		return content;
	}

	public String getContentShort() {
		String retStr = content;
		if (retStr.length() > 200){
			retStr = retStr.substring(0,99) + "...";
		}
		return retStr;
	}

	public String getCreatedString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(created);
	}

	public String getCreatedShortString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		return sdf.format(created);
	}

	public String getCreator() {
		return creator;
	}
	
	public String getLastModifiedString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(lastModified);
	}
	
	public String getTitleShort() {
		String retStr = title;
		if (retStr.length() > 30){
			retStr = retStr.substring(0,29) + "...";
		}
		return retStr;
	}

	public String getTitle() {
		return title;
	}

	public boolean isArticle(int anId){
		return anId == id;
	}
	
	public int getId(){
		return id;
	}
}
