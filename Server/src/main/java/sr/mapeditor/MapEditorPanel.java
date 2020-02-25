/*
 * Created on 2005-jun-16
 */
package sr.mapeditor;

import java.applet.AppletContext;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import sr.client.SpaceRazeApplet;
import sr.general.logging.Logger;
import sr.notifier.NotifierFrame;
import sr.server.properties.PropertiesHandler;
import sr.world.Map;

/**
 * @author WMPABOD
 *
 * Applet class for the map editor
 */
@SuppressWarnings("serial")
public class MapEditorApplet extends JApplet {
	public static final String NEW_MAP = "new";
	public static final String LOAD_DRAFT = "load_draft";
	public static final String LOAD_PUB = "load_pub";
	public static final String SAVE_DRAFT = "save_draft";
	public static final String SAVE_PUB = "save_pub";
	private String mapFileName = null;
	private EditorGUIPanel editorGuiPanel;
	private Map theMap;
	private String userLogin;
	
	private java.util.Map<String,String> applicationParams;
	private NotifierFrame notifierFrame;
	
	public MapEditorApplet(java.util.Map<String,String> applicationParams, NotifierFrame notifierFrame){
		  this.applicationParams = applicationParams;
		  this.notifierFrame = notifierFrame;
	  }
	  
	  public boolean isRunAsApplication(){
		  return applicationParams != null;
	  }
	  
	  
	  @Override
	  public String getParameter(String paramName){
		  Logger.fine("Get parameter: " + paramName);
		  String paramValue = null;
		  if (applicationParams != null){
			  paramValue = applicationParams.get(paramName);
			  Logger.fine("applicationParams, value: " + paramValue);
		  }else{
			  paramValue = super.getParameter(paramName);
			  Logger.fine("super.getParameter, value: " + paramValue);
		  }
		  return paramValue;
	  }
	  
	  @Override
	  public URL getCodeBase(){
		  //Logger.fine("Get codebase");
		  URL codebase = null;
		  if (applicationParams != null){
			  try{
				  codebase = new URL(applicationParams.get("codebase"));
				  Logger.fine("applicationParams, value: " + codebase.toString());
			  }catch(MalformedURLException mue){
				  Logger.severe("felaktig URL: " + applicationParams.get("codebase"));
			  }
		  }else{
			  codebase = super.getCodeBase();
			  //Logger.fine("super.getCodeBase, value: " + codebase.toString());
		  }
		  return codebase;
	  }
	  
	  @Override
	  public Image getImage(URL documentBase, String imageFileName){
		  Logger.fine("getImage overridden called: " + documentBase.toString() + ", " + imageFileName);
		  Image aImage = null;
		  if (isRunAsApplication()){
			  ClassLoader classLoader = this.getClass().getClassLoader();
			  URL url = classLoader.getResource(SpaceRazeApplet.imagesPath + imageFileName);
			  Logger.fine("URL: " + url.toString());
			  aImage = Toolkit.getDefaultToolkit().getImage(url);
		  }else{
			  aImage = super.getImage(documentBase, imageFileName);
		  }
		  return aImage;
	  }

	  @Override
	  public URL getDocumentBase(){
		  Logger.fine("Get documentbase from getCodeBase()");
		  return getCodeBase();
	  }

	public void init() {
	    Logger.info("");
	    Logger.info("MapEditorApplet started");
	    Logger.info("init()");
	    try {
	      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	    }
	    catch (Exception e) {
	      e.getStackTrace();
	    }
		userLogin = getParameter("username");
	    String action = getParameter("action"); // "new", "load_pub" or "load_draft"
	    if (action.equals(LOAD_DRAFT) | action.equals(LOAD_PUB)){
	    	// get map name parameter
	    	mapFileName = getParameter("mapname");
	    	// get the map object from the server...
	    	loadMap(action,mapFileName);
	    	// Use test map
/*	    	Vector planets = new Vector();
	    	Vector conns = new Vector();
	    	createPlanets2(planets,conns);
	    	theMap = new Map(planets,conns,"Testmap wigge 12","A very short description",12);
*/
	    }else{ // new map
	    	// create a new empty map
	    	theMap = new Map();
	    	theMap.setAuthorLogin(userLogin);
	    }

	    showGUI();
		
		Logger.fine("init: " + userLogin + " " + mapFileName + " " + action);
	}

	public void showGUI() {
	  	Logger.finer("showGUI anropad");

	    editorGuiPanel = new EditorGUIPanel(this,theMap);
	    editorGuiPanel.setBounds(0,0,getSize().width,getSize().height);
	    getContentPane().add(editorGuiPanel);
	    update(getGraphics());
	}
	
	public Map getMap(){
		return theMap;
	}
	
	private String getTunnelURL(){
		URL codeBase = getCodeBase();
		
		String appletPort = null;
	  	if (isRunAsApplication()){
	  		appletPort = getParameter("appletport");
	  	}else{
	  		appletPort = PropertiesHandler.getProperty("port");
	  	}
	  	
	 	Logger.info("Port read from properties file: \"" + appletPort + "\"");
		if (appletPort.equals("")){
		 	Logger.info("Port not found, using default port = 80: ");
			appletPort = "80";
		}
	  	String tunnelUrlString = codeBase.getProtocol() + "://" +  codeBase.getHost() + ":" + appletPort;
	  	// the code below which computes the path relies on that the client exist in a sub folder to the spaceraze root folder
	  	System.out.println("codeBase.getPath(): " +  codeBase.getPath());
	  	int webbIndex = codeBase.getPath().substring(0,codeBase.getPath().length()-1).lastIndexOf("/");
	  	System.out.println("webbIndex: " + webbIndex);
	  	String tmpPath = codeBase.getPath().substring(0,webbIndex);
	  	System.out.println("tmpPath: " +  tmpPath);
	  	tunnelUrlString = tunnelUrlString + tmpPath + "/map/sr.mapeditor.MapEditorTunnel";
		
		//String tunnelUrlString = codeBase.getProtocol() + "://" +  codeBase.getHost() + ":" + codeBase.getPort();
		//String tmpPath = codeBase.getPath().substring(0,codeBase.getPath().length()-8);
		//System.out.println("getTunnelURL: " + tunnelUrlString);
		//tunnelUrlString = tunnelUrlString + tmpPath + "/servlet/sr.mapeditor.MapEditorTunnel";
		return tunnelUrlString;
	}
	
	public void exit(){
		if (notifierFrame != null){
			notifierFrame.hideMapApplet();
		}else{
			URL codeBase = getCodeBase();
			String newUrl = codeBase.toString() + "redirectMAP.jsp";
			AppletContext ac = this.getAppletContext();
			Logger.finer("newUrl: " + newUrl);
			try {
				ac.showDocument(new URL(newUrl));
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void loadMap(String anAction, String aMapFileName){
		// create transfer object
		Logger.fine("creating tw: " + anAction + " " + userLogin + " " + aMapFileName);
		TransferWrapper tw = new TransferWrapper(anAction,userLogin,aMapFileName);
		// call server...
		tw = getResponse(tw);
		Logger.fine("recieving tw: " + tw.getMessage() + " " + tw.getMap());
		theMap = tw.getMap();
		Logger.fine("map loaded from server");
	}
	
	public void saveMap(String saveAction, String saveFileName){
		// create transfer object
		Logger.fine("aFileName: " + theMap.getFileName());
		TransferWrapper tw = new TransferWrapper(saveAction,userLogin,theMap,saveFileName);
		// call server...
		tw = getResponse(tw);
		if (tw.getMessage().equals(MapEditorTunnel.COMFIRM_NEEDED)){
//			int confirmDelete = JOptionPane.showConfirmDialog(this,"File exists, overwrite?","Confirmation needed",JOptionPane.YES_NO_OPTION);
			String[] opts = new String[2];
			opts[0] = "Yes";
			opts[1] = "No";
			int confirmDelete = JOptionPane.showOptionDialog(this,"File exists, overwrite?","Confirmation needed",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,opts,opts[0]);
			if (confirmDelete == JOptionPane.YES_OPTION){
				tw = new TransferWrapper(saveAction,userLogin,theMap,saveFileName);
				tw.setOwerwriteConfirm(true);
				tw = getResponse(tw);
				if (saveAction.equals(SAVE_PUB) & tw.getMessage().equals(TransferWrapper.MAP_SAVED)){
					theMap.incVersionId();
					editorGuiPanel.updateVersion();
				}
				JOptionPane.showMessageDialog(this,tw.getMessage(),"Message from server",JOptionPane.INFORMATION_MESSAGE);
			}
		}else{
			if (saveAction.equals(SAVE_PUB) & tw.getMessage().equals(TransferWrapper.MAP_SAVED)){
				theMap.incVersionId();
				editorGuiPanel.updateVersion();
			}
			JOptionPane.showMessageDialog(this,tw.getMessage(),"Message from server",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void updateMapData(){
		editorGuiPanel.updateMapData();
	}
	
	private TransferWrapper getResponse(TransferWrapper sendTW){
	  	Logger.info("getResponse called");
	  	URL server = null;
	  	TransferWrapper responseTW = null;
	  	try {
//	  		String tpath = PropertiesHandler.getProperty("mapeditortunnelpath");
	  		String tpath = getTunnelURL();
	  	  	Logger.info("Tunnel path: " + tpath);
	  		server = new URL(tpath);
	  	} catch(MalformedURLException e) {}
	  	ObjectInputStream response = null;
	  	Object result = null;
	  	URLConnection con;
		try {
			con = server.openConnection();
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/octet-stream");

			ObjectOutputStream request = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
			request.writeObject(sendTW);
		  	Logger.info("tw written");
			request.flush();
			request.close();
		  	Logger.info("request flush & close ");
			// get the result input stream
			response = new ObjectInputStream(new BufferedInputStream(con.getInputStream()));
		  	Logger.info("objectInputStream created: ");
			// read response back from the server
			result = response.readObject();
		  	Logger.info("result == null: " + (result == null));
		  	responseTW = (TransferWrapper)result;
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return responseTW;
	}
	
	public String getMapFileName(){
		return mapFileName;
	}
	
	public void setMapFileName(String newFileName){
		mapFileName = newFileName;
		theMap.setFileName(newFileName);
		editorGuiPanel.setMapFileName(newFileName);
	}

}
