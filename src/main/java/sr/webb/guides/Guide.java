/*
 * Created on 2005-jun-10
 */
package sr.webb.guides;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author niohl
 *
 * One Guideitem to be shown in the web
 */
public class Guide implements Serializable{
	private static final long serialVersionUID = 1L;
	private String title,content,creator;
	private Date created,lastModified;
	private int id,reads,published;

	
	public Guide(String title, String content, String creator,int id){
		this.title = title;
		this.content = content;
		this.creator = creator;
		this.id = id;
		this.published = 0;
		this.reads = 0;
		created = new Date();
	}
	
	public void modify(String title, String content, String creator,int published){
		this.title = title;
		this.content = content;
		this.creator = creator;
		this.published = published;
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
		if (lastModified != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(lastModified);
		}
		else
		{
			return "Not Modified";
		}
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

	public boolean isGuide(int anId){
		return anId == id;
	}
	
	public int getId(){
		return id;
	}
	public int getPublished(){
		return published;
	}
	public int getReads(){
		return reads;
	}
	public void addRead(){
		reads = reads + 1;
		
	}
}
