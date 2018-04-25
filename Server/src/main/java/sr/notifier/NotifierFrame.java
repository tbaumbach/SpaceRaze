package sr.notifier;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessControlException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import sr.client.SpaceRazeApplet;
import sr.client.StyleGuide;
import sr.client.components.SRButton;
import sr.client.components.SRTextArea;
import sr.client.components.SRTextField;
import sr.general.logging.Logger;
import sr.mapeditor.MapEditorApplet;
import sr.server.GameWorldHandler;
import sr.world.GameWorld;

// nya : pabod 5by5 http://localhost:8080/SpaceRaze/ 8080
// Paul: gamla tunnelurl parametern: http://localhost:8080/SpaceRaze/servlet/sr.notifier.NotifierTunnel
// f�r att g� mot prod: pabod 5by5 http://www.spaceraze.com/

@SuppressWarnings("serial")
public class NotifierFrame extends JFrame implements Runnable,ActionListener{
//	private JLabel userLbl;
	private Image red,yellow,green,question,current;
	private JLabel statusLabel;
	private SRButton updateBtn,moreInfoBtn,minimizeBtn,newGameBtn, editMapBtn;
	private SRTextField mapName;
	private GameListPanel gameListPanel;	
	private Thread t;
	private String user,password;
	private String tunnelPath;
	private boolean moreInfo = false;
	private TrayIcon trayIcon;
	private ReturnGames returnGames;
	private SpaceRazeApplet applet;
	private MapEditorApplet mapApplet;
	private String port;
	private List<sr.world.Map> maps;
	private NewGamePanel newGamePanel;
	private MapInfoPanel mapInfoPanel;
	private GameWorldInfoPanel gameWorldInfoPanel;
	private JScrollPane scrollPane;
	private String imagesPath = "resources/images/";

	public NotifierFrame(String aUser, String aPassword, String tunnelPath, boolean showFrameOnStartup, String port){
		super("SpaceRaze Notifier - " + aUser);
		Logger.fine("constructor called: " + aUser + ", " + showFrameOnStartup + ", " + tunnelPath + ", " + port);
		this.port = port;
		if(showFrameOnStartup){
			returnGames = ReturnGames.ALL; // om den �r startad via utv-milj�, visa alla partier
		}else{
			returnGames = ReturnGames.OWN_AND_OPEN;
		}

		loadImages();
		current = question;
		
		setIconImage(question);
		setLocation(100,100);
		setSize(360,110);
		setLayout(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		Container c = getContentPane();
		c.setBackground(new Color(0,0,0));
		
		ImageIcon ii = new ImageIcon(question); 
		
		statusLabel = new JLabel("Starting...",ii,JLabel.LEFT);
		statusLabel.setBounds(7,10,220,20);
		setColorAndFont(statusLabel);
		c.add(statusLabel);
		
		updateBtn = new SRButton("Update Now");
		updateBtn.setBounds(10,40,110,20);
		updateBtn.addActionListener(this);		
		c.add(updateBtn);

		moreInfoBtn = new SRButton("Show Games");
		moreInfoBtn.setBounds(130,40,100,20);
		moreInfoBtn.addActionListener(this);
		c.add(moreInfoBtn);

		if (SystemTray.isSupported()){
			try{
				SystemTray.getSystemTray(); // testa om s�kerhetsinst�llningarna till�ter att man har tillg�ng till system tray
				minimizeBtn = new SRButton("Minimize");
				minimizeBtn.setBounds(240,40,100,20);
				minimizeBtn.addActionListener(this);
				c.add(minimizeBtn);
			}catch(AccessControlException ace){
				Logger.info("Security does not allow access to system tray");
			}
		}

		newGameBtn = new SRButton("New Game");
		newGameBtn.setBounds(400,40,100,20);
		newGameBtn.addActionListener(this);
		c.add(newGameBtn);
		
		editMapBtn = new SRButton("Edit Map");
		editMapBtn.setBounds(520,40,100,20);
		editMapBtn.addActionListener(this);
		c.add(editMapBtn);
		
		mapName = new SRTextField("Map name");
		mapName.setBounds(630,40,100,20);
		c.add(mapName);

		user = aUser;
		Logger.info("User set to: " + user);

		password = aPassword;
		Logger.info("Password set to: " + password);

		this.tunnelPath = tunnelPath;

		if (SystemTray.isSupported()){
			if (showFrameOnStartup){
				moreInfo = true;
				setSize(1208,744);
				moreInfoBtn.setText("Hide Games");
				setVisible(true);
			}else{
				setVisible(true);
			}
		}else{
			setVisible(true);
		}
		
		t = new Thread(this);
		t.start();
	}

	private void setColorAndFont(Component c){
		c.setForeground(new Color(255,191,0));
		c.setBackground(new Color(0,0,0));
		c.setFont(new Font("Dialog",1,12));
	}
	
	private void loadImages(){
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL url = classLoader.getResource(SpaceRazeApplet.imagesPath + "questionmark.gif");
		Logger.fine("url questionmark.gif: " + url.toString());
		question = Toolkit.getDefaultToolkit().getImage(url);
		url = classLoader.getResource(imagesPath + "check.gif");
		green = Toolkit.getDefaultToolkit().getImage(url);
		url = classLoader.getResource(imagesPath + "saved.gif");
		yellow = Toolkit.getDefaultToolkit().getImage(url);
		url = classLoader.getResource(imagesPath + "cross.gif");
		red = Toolkit.getDefaultToolkit().getImage(url);
	}
	
	/**
	 * Perform infinite loop with pause, and update status
	 */
	public void run(){
		while(true){
			checkWithServer();
			update(getGraphics());
			try {
				if (returnGames == ReturnGames.ALL){ // when running in testing, no automatic updates are required
					Thread.sleep(Integer.MAX_VALUE);
				}else{
					// sleep 5 minutes before next update
					Thread.sleep(5*60*1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}

	public String deleteGame(String gameName){
		NotifierTransferWrapper wrapper = getResponse(null,0,gameName,true,false,null); 
		String status = wrapper.getReturnCode();
		return status;
	}

	public String updateGame(int changeTurns, String gameName){
		NotifierTransferWrapper wrapper = getResponse(null,changeTurns,gameName,false,false,null); 
		String status = wrapper.getReturnCode();
		return status;
	}
		
	public void checkWithServer(){
		NotifierTransferWrapper wrapper = getResponse(user,0,null,false,false,null); 
		String status = wrapper.getReturnCode();
		Logger.info("Reply from server: " + status);
		if (status.equalsIgnoreCase("error")){
			// can not contact server
			statusLabel.setText("Can't contact server");
			statusLabel.setIcon(new ImageIcon(question));
			setIconImage(question);
		}else
		if (status.equalsIgnoreCase("u")){
			// user not found. Show red X icon
			statusLabel.setText("User not found on server");
			statusLabel.setIcon(new ImageIcon(question));
			setIcon(question);
		}else
		if (status.equalsIgnoreCase("x")){
			// at least one game is not saved or finished. Show red X icon
			statusLabel.setText("Unfinished game(s) exist");
			statusLabel.setIcon(new ImageIcon(red));
			setIcon(red);
		}else
		if (status.equalsIgnoreCase("s")){
			// no game is not finished, and at leats one game is saved. Show yellow check icon 
			statusLabel.setText("Saved game(s) exist");
			statusLabel.setIcon(new ImageIcon(yellow));
			setIcon(yellow);
		}else{ // return "n"
			// no turns to perform, show check green icon
			statusLabel.setText("No games to perform");
			statusLabel.setIcon(new ImageIcon(green));
			setIcon(green);
		}
		if (wrapper.getGameListData() != null){
			Logger.fine("wrapper.getGameListData(), size: " + wrapper.getGameListData().getGames().size());
			updateGameList(wrapper.getGameListData().getGames());
		}
	}
	
	public List<sr.world.Map> getMaps(){
		if (maps == null){
			NotifierTransferWrapper wrapper = getResponse(null,0,null,false,true,null); 
			maps = wrapper.getAllMaps();
		}
		return maps;
	}
	
	private void updateGameList(List<GameData> games){
		if (gameListPanel != null){
			remove(gameListPanel);
		}
		if (scrollPane != null){
			remove(scrollPane);
		}
		gameListPanel = new GameListPanel(games,this);
		if (games.size() > 24){
//			gameListPanel = new GameListPanel(games,this);
			// create scrollpane
			scrollPane = new JScrollPane(gameListPanel);
			scrollPane.setLocation(10, 80);
			scrollPane.setSize(1185, 25*25);
			add(scrollPane);
			// workaround to get ViewportView to show
			setVisible(false);
			repaint();
			setVisible(true);
		}else{// less or equal to 24 games
			gameListPanel.setLocation(10, 80);
			add(gameListPanel);
		}
		// always repaint the frame
		repaint();
	}
	
	private void setIcon(Image anImage){
		current = anImage;
		setIconImage(anImage);
		if (trayIcon != null){
			trayIcon.setImage(anImage);
		}
	}

	public String createNewGame(CreateNewGameData createNewGameData){
		Logger.fine("createNewGameData, new game name: " + createNewGameData.getGameName());
		NotifierTransferWrapper wrapper = getResponse(null,0,null,false,false,createNewGameData); 
		String status = wrapper.getReturnCode();
		Logger.fine("Status: " + status);
		return status;
	}

	private NotifierTransferWrapper getResponse(String userLogin, int changeTurns, String gameName,boolean deleteGame, boolean getAllMaps, CreateNewGameData createNewGameData){
	  	Logger.fine("getResponse called");
	  	URL server = null;
	  	NotifierTransferWrapper responseWrapper = new NotifierTransferWrapper();
	  	responseWrapper.setReturnCode("error"); // default if nothing else is returned
	  	try {
	  		String tmpTunnelPath = tunnelPath + "notifier/sr.notifier.NotifierTunnel";
	  		Logger.fine("Tunnel path: " + tmpTunnelPath);
	  		server = new URL(tmpTunnelPath);
	  		ObjectInputStream response = null;
	  		Object result = null;
	  		URLConnection con;
			con = server.openConnection();
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/octet-stream");

			ObjectOutputStream request = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
			NotifierTransferWrapper wrapper = new NotifierTransferWrapper();
			if (createNewGameData != null){
				wrapper.setCreateNewGameData(createNewGameData);
			}else
			if (getAllMaps){
				wrapper.setGetAllMaps(true);
			}else
			if (deleteGame){
				wrapper.setDeleteGame(true);
				wrapper.setGameName(gameName);
			}else
			if (userLogin != null){
				wrapper.setUserLogin(userLogin);
				if (isVisible() & moreInfoBtn.getText().equals("Hide Games")){
					wrapper.setReturnGames(returnGames);
				}
			}else{ // update game
				wrapper.setChangeTurn(changeTurns);
				wrapper.setGameName(gameName);
			}
			request.writeObject(wrapper);
			Logger.fine("userName written");
			request.flush();
			request.close();
			Logger.fine("request flush & close ");
			// get the result input stream
			response = new ObjectInputStream(new BufferedInputStream(con.getInputStream()));
			Logger.fine("objectInputStream created: ");
			// read response back from the server
			result = response.readObject();
		  	responseWrapper = (NotifierTransferWrapper)result;
			Logger.fine("returnCode: " + responseWrapper.getReturnCode());
	  	} catch(ConnectException e) {
	  		Logger.warning("ConnectException: " + e.toString());
			e.printStackTrace();
	  	} catch(MalformedURLException e) {
	  		Logger.warning("MalformedURLException: " + tunnelPath + " -> " + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Logger.warning("IOException: " + e.toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Logger.warning("ClassNotFoundException: " + e.toString());
			e.printStackTrace();
		}
		return responseWrapper;
	}

	public static void main(String[] args) {
		if (args.length == 4){ // argument som f�s via Eclipse, t.ex. "pabod 5by5 http://localhost:8080/SpaceRaze/ 8080"
			new NotifierFrame(args[0],args[1],args[2],true,args[3]);
		}else{ // antas vara 3 argunent via jnlp-filen
			String port = "80";
			int colonIndex = args[2].substring(6).indexOf(":");
			if (colonIndex > -1){ // h�mta ut portnummret
				String tmpPortStr = args[2].substring(6).substring(colonIndex+1);
				Logger.fine("tmpPortStr: " + tmpPortStr);
				tmpPortStr = tmpPortStr.substring(0, tmpPortStr.indexOf("/"));
				Logger.fine("tmpPortStr: " + tmpPortStr);
				port = tmpPortStr;
				Logger.fine("Port: " + port);
			}
			new NotifierFrame(args[0],args[1],args[2],false,port);
		}
	}
	
	public void removeNewGamePanel(){
		remove(newGamePanel);
		newGamePanel = null;
		if (mapInfoPanel != null){
			remove(mapInfoPanel);
			mapInfoPanel = null;
		}
		if (gameWorldInfoPanel != null){
			remove(gameWorldInfoPanel);
			gameWorldInfoPanel = null;
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == newGameBtn){
			if (newGamePanel == null){
//				if (gameListPanel != null){
//					gameListPanel.setVisible(false);
//					remove(gameListPanel);
//				}
				if (scrollPane != null){
					scrollPane.setVisible(false);
					remove(scrollPane);
				}
				if (gameListPanel != null){
					gameListPanel.setVisible(false);
					remove(gameListPanel);
				}
				newGamePanel = new NewGamePanel(this);
				newGamePanel.setLocation(10, 80);
				add(newGamePanel);
				paintAll(getGraphics());
			}
		}else
		if (arg0.getSource() == editMapBtn){
			if (newGamePanel != null){
				removeNewGamePanel();
			}
			if (scrollPane != null){
				scrollPane.setVisible(false);
				remove(scrollPane);
			}
			if (gameListPanel != null){
				gameListPanel.setVisible(false);
				remove(gameListPanel);
			}
			showMapApplet(mapName.getText(), "load_pub", getUser());
			
		}else
		if (arg0.getSource() == updateBtn){
			if (newGamePanel != null){
				removeNewGamePanel();
			}
			checkWithServer();
		}else
		if (arg0.getSource() == moreInfoBtn){
			if (!moreInfo){
				moreInfo = true;
				if (returnGames == ReturnGames.OWN_AND_OPEN){
					setSize(1208,764);
				}else{
					setSize(1208,744);
				}
				moreInfoBtn.setText("Hide Games");
			}else{
				moreInfo = false;
				setSize(360,110);
				moreInfoBtn.setText("Show Games");
			}
			paintAll(getGraphics());
		}else
		if(arg0.getSource() == minimizeBtn){
			minimizeToTray();
		}else
		if (arg0.getSource() == trayIcon){
			this.setVisible(true);
	        SystemTray tray = SystemTray.getSystemTray();
	        tray.remove(trayIcon);
		}else
		if (arg0.getSource() instanceof MenuItem){
			MenuItem aMenuItem = (MenuItem)arg0.getSource();
			if (aMenuItem.getLabel().equals("Open")){
				this.setVisible(true);
		        SystemTray tray = SystemTray.getSystemTray();
		        tray.remove(trayIcon);
			}else
			if (aMenuItem.getLabel().equals("Exit")){
		        SystemTray tray = SystemTray.getSystemTray();
		        tray.remove(trayIcon);
		        System.exit(0);
			}else
			if (aMenuItem.getLabel().equals("Open www.spaceraze.com")){
				openWebBrowser();
			}else
			if (aMenuItem.getLabel().equals("Update Now")){
				checkWithServer();
			}
		}else{
			Logger.severe("Unknown action source: " + arg0.getSource());
		}
	}
	
	private void openWebBrowser(){
		try {
//			URL anURL = new URL("http://www.spaceraze.com/webb/login.jsp?login=" + user + "&password=" + password);
			URL anURL = new URL("http://www.spaceraze.com/");
			Logger.fine("URL to open: " + anURL.toString());
			// Lookup the javax.jnlp.BasicService object
			BasicService bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
			// Invoke the showDocument method
			bs.showDocument(anURL);
		} catch(UnavailableServiceException ue) {
			// Service is not supported
			Logger.fine("UnavailableServiceException: " + ue);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void minimizeToTray(){
		// get the SystemTray instance
        SystemTray tray = SystemTray.getSystemTray();
        // load an image
        Image image = current;
        // create a popup menu
        PopupMenu popup = new PopupMenu();
        // create menu items

        MenuItem menuFrame = new MenuItem("Open");
        menuFrame.addActionListener(this);
        popup.add(menuFrame);

        MenuItem menuUpdate = new MenuItem("Update Now");
        menuUpdate.addActionListener(this);
        popup.add(menuUpdate);

        MenuItem menuWeb = new MenuItem("Open www.spaceraze.com");
        menuWeb.addActionListener(this);
        popup.add(menuWeb);

        MenuItem menuExit = new MenuItem("Exit");
        menuExit.addActionListener(this);
        popup.add(menuExit);
        // construct a TrayIcon
        trayIcon = new TrayIcon(image, "SpaceRaze Notifier", popup);
        // set the TrayIcon properties
        trayIcon.addActionListener(this);
        // add the tray image
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }
        this.setVisible(false);
	}

	private void setVisibleComponents(boolean show){
//		userLbl.setVisible(show);
		statusLabel.setVisible(show);
		updateBtn.setVisible(show);
		moreInfoBtn.setVisible(show);
		if (minimizeBtn != null){
			minimizeBtn.setVisible(show);
		}
		gameListPanel.setVisible(show);
		if (scrollPane != null){
			scrollPane.setVisible(show);
		}
		newGameBtn.setVisible(show);
		editMapBtn.setVisible(show);
	}
	
	public void showApplet(int gameId, String playerName, String playerPassword){
		if (applet != null){
			remove(applet);
		}
		setVisibleComponents(false);
		Map<String,String> applicationParams = new HashMap<String,String>();
		applicationParams.put("port", String.valueOf(gameId));
		applicationParams.put("returnto", "Not Used");
		applicationParams.put("returnto_delete", "Not Used");
		String autouser = null;
		Logger.info("returnGames: " + returnGames);
		if (returnGames == ReturnGames.OWN_AND_OPEN){ // startad via jnlp
			autouser = "true";
			applicationParams.put("username", user);
			applicationParams.put("userpassword", password);
		}else{ // startad som vanlig applikation
			if (playerName != null){ // autologin med playerName/playerPassword
				autouser = "true";
				applicationParams.put("username", playerName);
				applicationParams.put("userpassword", playerPassword);
			}else{
				// ej autologin, detta m�ste vara ett parti som har status "Starting"
				autouser = "false";
			}
		}
		Logger.info("autouser: " + autouser);
		applicationParams.put("autouser", autouser);
		applicationParams.put("codebase", tunnelPath + "webb2/");
		applicationParams.put("appletport", port);
		if (returnGames == ReturnGames.ALL){
			applicationParams.put("messagesleeptime","6000000"); // startad via utv-milj�, h�gt v�rde f�r att inte st�ra loggarna...
		}else{
			applicationParams.put("messagesleeptime","10000"); // startad via jnlp, var 10:e sekund...
		}
		applet = new SpaceRazeApplet(applicationParams,this);
		applet.setBounds(0, 0, 1200, 710);
		applet.init();
		applet.start();
		add(applet);
	}
	
	public void showMapApplet(String mapName, String action, String playerName){
		if (mapApplet != null){
			remove(mapApplet);
		}
		setVisibleComponents(false);
		Map<String,String> applicationParams = new HashMap<String,String>();
		applicationParams.put("mapname", mapName);
		Logger.info("returnGames: " + returnGames);
		if (returnGames == ReturnGames.OWN_AND_OPEN){ // startad via jnlp
			applicationParams.put("username", user);
			//applicationParams.put("userpassword", password);
		}else{ // startad som vanlig applikation
			if (playerName != null){ // autologin med playerName/playerPassword
				applicationParams.put("username", playerName);
				
				//applicationParams.put("userpassword", playerPassword);
			}
		}
		applicationParams.put("action", action);
		applicationParams.put("codebase", tunnelPath + "webb2/");
		applicationParams.put("appletport", port);
		
		mapApplet = new MapEditorApplet(applicationParams,this);
		mapApplet.setBounds(0, 0, 930, 560);
		mapApplet.init();
		mapApplet.start();
		add(mapApplet);
	}
	
	public void hideApplet(){
		applet.setVisible(false);
		applet = null;
		StyleGuide.colorCurrent = StyleGuide.colorNeutral;
		setVisibleComponents(true);
		checkWithServer();
	}
	
	public void hideMapApplet(){
		mapApplet.setVisible(false);
		mapApplet = null;
		StyleGuide.colorCurrent = StyleGuide.colorNeutral;
		setVisibleComponents(true);
		checkWithServer();
	}

	public String getPassword() {
		return password;
	}

	public String getUser() {
		return user;
	}

	public ReturnGames getReturnGames() {
		return returnGames;
	}

	public Image getGreen() {
		return green;
	}

	public Image getRed() {
		return red;
	}

	public Image getYellow() {
		return yellow;
	}
	
    public sr.world.Map findMap(String aMapName){
    	sr.world.Map found = null;
    	int i = 0;
    	while ((found == null) & (i < maps.size())){
    		sr.world.Map aMap = maps.get(i);
    		if (aMap.getNameFull().equals(aMapName)){
    			found = aMap;
    		}else{
    			i++;
    		}
    	}
    	return found;
    }   
    
    public int getMaxPlayers(){
    	return mapInfoPanel.getMaxPlayers();
    }
    
    public int getNrStartPlanets(){
    	return mapInfoPanel.getNrStartPlanets();
    }

    public List<String> getFactionNames(){
    	return gameWorldInfoPanel.getFactionNames();
    }

    public MapInfoPanel showMapInfo(String aMapName, boolean showAdvanced){
    	Logger.fine("aMapName: " + aMapName);
    	sr.world.Map theMap = findMap(aMapName);
    	if (aMapName == null){ // hide map panel
    		mapInfoPanel.setVisible(false);
    	}else
    	if (mapInfoPanel == null){    		
    		mapInfoPanel = new MapInfoPanel(theMap,showAdvanced);
    		mapInfoPanel.setLocation(650, 80);
    		add(mapInfoPanel);
    	}else{
    		mapInfoPanel.setVisible(true);
    		mapInfoPanel.showMap(theMap,showAdvanced);
    	}
    	repaint();
    	return mapInfoPanel;
    }

    public GameWorldInfoPanel showGameWorldInfo(String aGameWorldName, boolean showAdvanced){
    	Logger.fine("aGameWorldName: " + aGameWorldName);
    	GameWorld foundGameWorld = GameWorldHandler.findGameWorld(aGameWorldName);
    	if (aGameWorldName == null){ // hide gw panel
    		gameWorldInfoPanel.setVisible(false);
    	}else
    	if (gameWorldInfoPanel == null){    		
    		gameWorldInfoPanel = new GameWorldInfoPanel(foundGameWorld,showAdvanced);
    		gameWorldInfoPanel.setLocation(380, 80);
    		add(gameWorldInfoPanel);
    	}else{
    		gameWorldInfoPanel.setVisible(true);
    		gameWorldInfoPanel.showGameWorld(foundGameWorld,showAdvanced);
    	}
    	repaint();
    	return gameWorldInfoPanel;
    }
    
}
