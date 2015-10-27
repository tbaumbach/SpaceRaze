/*
 * Created on 2005-jun-29
 */
package sr.mapeditor;

import java.io.Serializable;

import sr.world.Map;

/**
 * @author WMPABOD
 *
 * Wrapper for data transferred to and from map editor tunnel servlet.
 * Also contains some logic for that data and the tunnel.
 */
public class TransferWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map aMap;
	private String playerLogin;
	private String mapFileName; // name of map to be loaded or saved
	private String action; // see MapEditorApplet for valid actions
	private String message; // used by server to indicate success or failiure to save a map
	private boolean owerwriteConfirm = false; // used by client to confirm an overwrite
	public static final String MAP_SAVED = "Map saved";
	public static final String MAP_NOT_SAVED = "Error on server, map was not saved";

	// Used when fetching a map from the server
	public TransferWrapper(String action, String aPlayerLogin, String aMapFileName){
		this.action = action;
		this.playerLogin = aPlayerLogin;
		this.mapFileName = aMapFileName;
	}

	// Used when saving a map to the server
	public TransferWrapper(String action, String aPlayerLogin, Map aMap, String aMapFileName){
		this.action = action;
		this.playerLogin = aPlayerLogin;
		this.aMap = aMap;
		this.mapFileName = aMapFileName;
	}

	// used bu server to return a map
	public void setMap(Map aMap){
		this.aMap = aMap;
	}
	
	public Map getMap(){
		return aMap;
	}

	public boolean isOwerwriteConfirm() {
		return owerwriteConfirm;
	}
	
	public void setOwerwriteConfirm(boolean owerwriteConfirm) {
		this.owerwriteConfirm = owerwriteConfirm;
	}
	
	public String getPlayerLogin(){
		return playerLogin;
	}

	public String getMessage(){
		return message;
	}

	/**
	 * Used by server when responding to a save map action.
	 * Also removes map from transfer object
	 * @param aMessage
	 */
	public void setMessage(String aMessage){
		this.message = aMessage;
		aMap = null;
	}

	public String getAction(){
		return action;
	}

	public String getMapFileName() {
		return mapFileName;
	}
}
