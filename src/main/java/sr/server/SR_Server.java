package sr.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import spaceraze.server.game.StartGameHandler;
import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.Faction;
import spaceraze.world.Galaxy;
import spaceraze.world.GameWorld;
import spaceraze.world.Player;
import spaceraze.world.StatisticGameType;
import spaceraze.world.enums.DiplomacyGameType;
import sr.message.MessageDataBaseLoader;
import sr.message.MessageDataBaseSaver;
import sr.message.MessageDatabase;
import sr.webb.mail.MailHandler;
import sr.webb.users.User;
import sr.webb.users.UserHandler;

/**
 * 
 * @author WMPABOD
 * 
 *         The SR_Server models one game on the server
 * 
 */
public class SR_Server {
	public static final String version = "4.0.6";
	protected ServerSocket listen_socket;
	private String command, nameOfGame, nameOfMap;
	// private String startedByPlayer;
	private long time = 0;
	private Galaxy g;
	private GalaxyCreator gc = new GalaxyCreator();
	private GalaxyLoader gl = new GalaxyLoader();
	private GalaxySaver gs = new GalaxySaver();
	private InputThread it;
	private UpdateRunner ur;
	// private Vector allsockets;
	// private List allConnections;
	private boolean autoUpdate;
	private int id;
	private int steps;
	// private int maxPlayers;
	// private boolean ranked = false;
	private ServerHandler sh;
	private MessageDatabase messageDatabase;
	private MessageDataBaseLoader aMessageDataBaseLoader = new MessageDataBaseLoader();

	/**
	 * Only used to load old games. Always use auto update. steps is not used when
	 * loading, set to -1 maxPlayers is not used when loading, set to -1
	 *
	 */
	public SR_Server(String nameOfGame, int port, ServerHandler sh) {
		this("load", null, nameOfGame, "not used when loading", port, 0, true, -1, false, -1, null, null, false, false,
				null, sh, null, false, 0, 0, 0, 0, null);
	}

	/**
	 * Use this to start a new game that updates when a set time has passed. Game
	 * will also update when all players are done with their turn.
	 *
	 */
	public SR_Server(String command, GameWorld aGameWorld, String nameOfGame, String nameOfMap, int id, int steps,
			boolean autoBalance, long time, int maxPlayers, String startedByPlayer, String gamePassword,
			boolean groupFaction, boolean randomGame, List<String> selectableFactionNames, ServerHandler sh,
			DiplomacyGameType diplomacyGameType, boolean bRanked, int singleVictory,
			int factionVictory, int endTurn, int numberOfStartPlanet, StatisticGameType statisticGameType) {
		this(command, aGameWorld, nameOfGame, nameOfMap, id, time, true, steps, autoBalance, maxPlayers,
				startedByPlayer, gamePassword, groupFaction, randomGame, selectableFactionNames, sh, diplomacyGameType,
				bRanked, singleVictory, factionVictory, endTurn, numberOfStartPlanet, statisticGameType);
	}

	/**
	 * Use this to start a new game that updates automatically when all players is
	 * finished with a turn Not used any more!?
	 * 
	 * @param command
	 * @param nameOfGame
	 * @param port
	 */
	/*
	 * public SR_Server(String command,String nameOfGame,String nameOfMap,int id,int
	 * steps,boolean autoBalance){
	 * this(command,nameOfGame,nameOfMap,id,0,true,steps,autoBalance); }
	 */

	public int getId() {
		return id;
	}

	public String getStartedByPlayer() {
		return g.getStartedByPlayer();
	}

	public String getStartedByPlayerName() {
		String login = getStartedByPlayer();
		User theUser = UserHandler.findUser(login);
		String tmpName = null;
		if (theUser != null) {
			tmpName = theUser.getName();
		} else {
			tmpName = login;
		}
		return tmpName;
	}

	public void setStartedByPlayer(String startedByPlayer) {
		g.setStartedByPlayer(startedByPlayer);
	}

	public String getMapFileName() {
		return g.getMapFileName();
	}

	public String getLastUpdatedString() {
		return g.getLastUpdatedString();
	}

	public boolean isPlayerParticipating(User aUser) {
		boolean found = false;
		if (aUser != null) {
			Logger.finest("isPlayerParticipating - User: " + aUser.getLogin());
			List<Player> players = g.getPlayers();
			int index = 0;
			while ((!found) && (index < players.size())) {
				Player tmpPlayer = (Player) players.get(index);
				Logger.finest("  tmpPlayer: " + tmpPlayer.getName());
				if (tmpPlayer.getName().equalsIgnoreCase(aUser.getLogin())) {
					found = true;
				} else {
					index++;
				}
			}
		} else {
			Logger.finest("isPlayerParticipating - User: null");
		}
		return found;
	}

	private SR_Server(String command, GameWorld aGameWorld, String nameOfGame, String nameOfMap, int id, long aTime,
			boolean autoUpdate, int steps, boolean autoBalance, int maxPlayers, String startedByPlayer,
			String gamePassword, boolean groupFaction, boolean randomGame, List<String> selectableFactionNames,
			ServerHandler sh, DiplomacyGameType diplomacyGameType,boolean bRanked,
			int singleVictory, int factionVictory, int endTurn, int numberOfStartPlanet,
			StatisticGameType statisticGameType) {
		Logger.finer("new SR_Server");
		this.command = command;
		this.nameOfGame = nameOfGame;
		this.nameOfMap = nameOfMap;
		this.id = id;
		this.time = aTime;
		this.autoUpdate = autoUpdate;
		this.steps = steps;
		// this.maxPlayers = maxPlayers;
		this.sh = sh;
		// this.ranked = bRanked;
		// this.startedByPlayer = startedByPlayer;
		Logger.finer("autoBalance: " + autoBalance);
		parseCommand(autoBalance, maxPlayers, startedByPlayer, aGameWorld, gamePassword, groupFaction, randomGame,
				selectableFactionNames, diplomacyGameType, bRanked, singleVictory, factionVictory,
				endTurn, numberOfStartPlanet, statisticGameType);
	}

	public String getServerHTMLRow() {
		String retStr = "";
		retStr = retStr + nameOfGame + " " + id + " " + autoUpdate;
		return retStr;
	}

	public void startRunning() {
		Logger.fine("startRunning() called");
		if (ur == null) {
			ur = new UpdateRunner(time, this);
			ur.start();
		}
	}

	private void parseCommand(boolean autoBalance, int maxPlayers, String startedByPlayer, GameWorld aGameWorld,
			String gamePassword, boolean groupFaction, boolean randomGame, List<String> selectableFactionNames,
			DiplomacyGameType diplomacyGameType, boolean ranked, int singleVictory,
			int factionVictory, int endTurn, int numberOfStartPlanet, StatisticGameType statisticGameType) {
		if (command.equalsIgnoreCase("create")) {
			createGalaxy(autoBalance, maxPlayers, startedByPlayer, aGameWorld, gamePassword, groupFaction, randomGame,
					selectableFactionNames, diplomacyGameType, ranked, singleVictory, factionVictory,
					endTurn, numberOfStartPlanet, statisticGameType);
			messageDatabase = new MessageDatabase(nameOfGame);
			new MessageDataBaseSaver().saveMessageDataBase(nameOfGame, messageDatabase);
		} else if (command.equalsIgnoreCase("load")) {
			loadGalaxy();
			messageDatabase = aMessageDataBaseLoader.loadMessageDatabase(nameOfGame);
			if (messageDatabase == null) {
				messageDatabase = new MessageDatabase(nameOfGame);
				new MessageDataBaseSaver().saveMessageDataBase(nameOfGame, messageDatabase);
			}
		} else if (command.equalsIgnoreCase("update")) { // never used anymore???
			try {
				updateGalaxy(true);
			} catch (Exception e) {
				Logger.fine("Exception: " + e.toString());
			}
		}
	}

	// private void createMessageDataBase(String nameOfGame){
	//
	// }

	private void createGalaxy(boolean autoBalance, int maxPlayers, String startedByPlayer, GameWorld aGameWorld,
			String gamePassword, boolean groupFaction, boolean randomGame, List<String> selectableFactionNames,
			DiplomacyGameType diplomacyGameType, boolean ranked, int singleVictory,
			int factionVictory, int endTurn, int numberOfStartPlanet, StatisticGameType statisticGameType) {
		// when planetlist is in a file, send the filename as the second parameter
		// filename can be a parameter to the .bat file
		g = gc.createGalaxy(nameOfGame, nameOfMap, steps, aGameWorld, singleVictory, factionVictory, endTurn,
				numberOfStartPlanet, statisticGameType);
		g.setAutoBalance(autoBalance);
		g.setTime(time);
		g.setMaxNrStartPlanets(maxPlayers);
		g.setStartedByPlayer(startedByPlayer);
		g.setGroupSameFaction(groupFaction);
		Logger.finest("gamePassword: " + gamePassword);
		g.setPassword(gamePassword);
		g.setRandomFaction(randomGame);
		g.setRanked(ranked);
		if (selectableFactionNames == null) {
			// all factions should be selectable
			g.setAllFactionsSelectable();
		} else {
			g.setSelectableFactionNames(selectableFactionNames);
		}
		g.setDiplomacyGameType(diplomacyGameType);
		gs.saveGalaxy(nameOfGame, "saves", g);
		gs.saveGalaxy(nameOfGame + "_" + g.getTurn(), "saves/previous", g);
	}

	private void loadGalaxy() {
		g = gl.loadGalaxy(nameOfGame);
		time = g.getTime();
		Logger.finer("loadGalaxy, time=" + time + " g.gameEnded=" + g.gameEnded + " Gamename=" + nameOfGame);
		if (!g.gameEnded) {
			if (g.getTurn() > 0) {
				if (time > 0) {
					// start the update scheduler
					Logger.fine("Time > 0, starting ur");
					startRunning();
				}
			}
		}
	}

	public void setGalaxy(Galaxy newGalaxy) {
		this.g = newGalaxy;
	}

	public void updateGalaxy(boolean hasAutoUpdated) throws Exception {
		g = gl.loadGalaxy(nameOfGame);
		Logger.info("Galaxy loaded. Turn is " + g.getTurn());
		updateGalaxy(g);
		g.setHasAutoUpdated(hasAutoUpdated);
		gs.saveGalaxy(nameOfGame, "saves", g);
		gs.saveGalaxy(nameOfGame + "_" + g.getTurn(), "saves/previous", g);
	}

	private void updateGalaxy(Galaxy g) throws Exception {
		GalaxyUpdater gu = new GalaxyUpdater(g);
		gu.performUpdate(this);
	}

	/**
	 * Check if game should be removed
	 * 
	 * @return true if game hasn't been updated for 2 weeks
	 */
	public boolean tooOld() {
		boolean isTooOld = false;
		// create last updated calendar object
		Date lastUpdatedDate = g.getLastUpdated();
		Calendar lastUpdatedCal = Calendar.getInstance();
		lastUpdatedCal.setTimeInMillis(lastUpdatedDate.getTime());
		// calculate when a game is too old
		Calendar oldCal = (Calendar) lastUpdatedCal.clone();
		oldCal.add(Calendar.DATE, 14);
		// create now calendar instance
		Calendar nowCal = Calendar.getInstance();
		if (oldCal.before(nowCal)) {
			isTooOld = true;
		}
		return isTooOld;
	}

	public int getTurn() {
		return g.getTurn();
	}

	public int getEndTurn() {
		return g.getEndTurn();
	}

	public String getGameName() {
		return nameOfGame;
	}

	public String getStatus() {
		return g.getStatus();
	}

	public boolean canBeDeletedByPlayer() {
		boolean okToDelete = false;
		Calendar gameCal = Calendar.getInstance();
		gameCal.setTime(g.getLastUpdated());
		gameCal.roll(Calendar.DATE, 7);
		Calendar nowCal = Calendar.getInstance();
		if (nowCal.after(gameCal)) {
			okToDelete = true;
		}
		return okToDelete;
	}

	/*
	 * Used then a player joins a game(this). The password is removed and set to "".
	 */
	public String join(String name, String govenorName, String factionName) {

		String message = "";

		if (g.getTurn() > 0) {
			message = "Game has already begun. No more players can join.";
		} else { // turn == 0
			if ((g.getPlayer(name, "")).getErrorMessage() == null) {
				Logger.fine("Player already exists.");
				message = "Player already exists.";
			} else if (!factionIsOpenAndSelectable(factionName)) {
				message = "All slots in the " + factionName + " faction have just been taken. Choose another faction.";
			} else {
				Player player = (new StartGameHandler()).getNewPlayer(name, "", govenorName, factionName, g);

				if (player.getErrorMessage() != null) {
					message = player.getErrorMessage();
				}
			}
		}
		return message;
	}

	public Player getPlayer(String pName, String pPassword) {
		return g.getPlayer(pName, pPassword);
	}

	public Player getPlayer(String message) throws Exception {
		Logger.fine("message: " + message);
		StringTokenizer st = new StringTokenizer(message);
		Logger.fine("st " + st.toString());
		Player p;
		Logger.fine("st.countTokens() " + st.countTokens());
		if (st.countTokens() >= 3) {
			String command = st.nextToken();
			String name = st.nextToken();
			Logger.fine("command " + command);
			String password = st.nextToken();
			String govenorName = "";
			String factionName = "";
			try {
				govenorName = st.nextToken().replace('#', ' ');
				factionName = st.nextToken();
				Logger.fine("factionName " + factionName);
				while (st.hasMoreTokens()) {
					factionName = factionName + " " + st.nextToken();
					Logger.fine("factionName loop" + factionName);
				}
			} catch (NoSuchElementException nsee) {
				Logger.fine("NoSuchElementException. No more tokens found (normal handling).");
				// Thread.dumpStack();
			}
			if (command.equalsIgnoreCase("newplayer")) {
				if (g.getTurn() > 0) {
					p = new Player("Game has already begun. No more players can join.");
				} else { // turn == 0
					if ((g.getPlayer(name, password)).getErrorMessage() == null) {
						Logger.fine("Player already exists.");
						p = new Player("Player already exists.");
					} else if (!factionIsOpenAndSelectable(factionName)) {
						p = new Player("All slots in the " + factionName
								+ " faction have just been taken. Choose another faction.");
					} else {
						p = (new StartGameHandler()).getNewPlayer(name, password, govenorName, factionName, g);
					}
				}
			} else {
				p = g.getPlayer(name, password);
			}
		} else if ((st.countTokens() == 1) && st.nextToken().equals("checkStatus")) {
			p = new Player("Returning status.", g);
		} else if ((st.countTokens() == 2) && st.nextToken().equals("update")) {
			p = new Player("Updating server.", g);
			// update server
			int turns = Integer.parseInt(st.nextToken());
			for (int i = 0; i < turns; i++) {
				updateGalaxy(true);
			}
		} else {
			p = new Player("Player name or password missing.");
		}
		if (!autoUpdate) {

			//TODO 2020-12-11 this value is never used, remove or shoudl we set this value in a other object? p.setNextUpdate(ur.getNextUpdate());
		}
		return p;
	}

	public void setHasAutoUpdated(boolean newValue) {
		g.setHasAutoUpdated(newValue);
	}

	public boolean hasAutoUpdated() {
		return g.hasAutoUpdated();
	}

	public String updatePlayer(Player p) throws Exception {
		if (p.getName() != null) {
			Logger.info("updatePlayer(server): " + p.getName());
		}
		String msg = g.replacePlayer(p);
		if (msg.equalsIgnoreCase("Player data inserted successfully.")) {
			gs.saveGalaxy(nameOfGame, "saves", g);
			gs.saveGalaxy(nameOfGame + "_" + g.getTurn(), "saves/previous", g);
			if (g.gameEnded) {
				Logger.info("Game is ended, no update or mails will be performed.");
			} else if (g.turn > 0) {
				int nrActive = PlayerPureFunctions.getActivePlayers(g).size();
				int nrUpdated = g.getNrFinishedPlayers();
				Logger.info(nrUpdated + "/" + nrActive + " players updated.");
				if (nrActive == nrUpdated) {
					// skriv ut i konsolen...
					Logger.info("***************************************");
					Logger.info("All players updated (at least once...).");
					Logger.info("***************************************");
					if (autoUpdate) {
						updateGalaxy(true);
						MailHandler.sendNewTurnMessage(this);
					}
				}
			} else {
				// ta reda på antalet spelare
				int nrPlayers = g.getNrPlayers();
				// ta reda på max antalet spelare
				int maxNrStartPlanets = g.getNrStartPlanets();
				Logger.info(nrPlayers + "/" + maxNrStartPlanets + " players created.");
				// om alla har skapat sina guvenörer..

				if (nrPlayers == maxNrStartPlanets) {
					Logger.info("********************");
					Logger.info("All players created.");
					Logger.info("********************");
					boolean sendMail = false;
					if (autoUpdate) {
						updateGalaxy(true);
						// g.setHasAutoUpdated(true);
						sendMail = true;
					}
					if (time > 0) {
						// start the update scheduler
						Logger.fine("All players created, and time > 0, starting ur!");
						startRunning();
					}
					if (sendMail) {
						MailHandler.sendNewTurnMessage(this);
					}
				}
			}
		}
		return msg;
	}

	@SuppressWarnings("deprecation")
	public void stopThreads() {
		try {
			if (listen_socket != null) {
				listen_socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// stop();
		if (it != null) {
			it.stop();
		}
		if (ur != null) {
			ur.stop();
		}
		/*
		 * for (Iterator iter = allConnections.iterator(); iter.hasNext();) { Connection
		 * aConn = (Connection) iter.next(); aConn.stopThreads(); }
		 */
	}

	public Galaxy getGalaxy() {
		return g;
	}

	private boolean factionIsOpenAndSelectable(String factionName) {
		boolean ok = true;
		boolean autoBalance = getAutoBalance();
		if (autoBalance) {
			List<Faction> openFactions = getOpenSelectableFactions();
			boolean found = false;
			for (Faction aFaction : openFactions) {
				if (aFaction.getName().equalsIgnoreCase(factionName)) {
					found = true;
				}
			}
			ok = found;
		}
		return ok;
	}

	/**
	 * Returns a list of the factions a new player can join
	 * 
	 * @return open factions list
	 */
	public List<Faction> getOpenSelectableFactions() {
		List<Faction> openFactions = new LinkedList<Faction>();
		List<Faction> allFactions = g.getFactions();
		// List<Player> allPlayers = g.getPlayers();
		int maxPlayers = g.getNrStartPlanets();
		int nrSelectableFactions = g.getSelectableFactionNames().size();
		Logger.finer("maxPlayers: " + maxPlayers);
		int maxFactionNr = (int) Math.ceil((1.0 * maxPlayers) / nrSelectableFactions);
		Logger.finer("maxFactionNr: " + maxFactionNr);
		for (Faction aFaction : allFactions) {
			Logger.finer("Faction found: " + aFaction.getName());
			int factionNr = g.getFactionMemberNr(aFaction);
			Logger.finer("factionNr: " + factionNr);
			if ((factionNr < maxFactionNr) & g.isFactionSelectable(aFaction)) {
				openFactions.add(aFaction);
				Logger.finest("faction added: " + aFaction.getName());
			}
		}
		return openFactions;
	}

	/**
	 * Returns a list of the factions a new player can join
	 * 
	 * @return open factions list
	 */
	public List<Faction> getSelectableFactions() {
		List<Faction> selectableFactions = new LinkedList<Faction>();
		for (String factionName : g.getSelectableFactionNames()) {
			selectableFactions.add(g.findFaction(factionName));
		}
		return selectableFactions;
	}

	public boolean getAutoBalance() {
		return g.getAutoBalance();
	}

	public UpdateRunner getUpdateRunner() {
		return ur;
	}

	public void newPlayerPassword(String login, String newPassword) {
		g.newPlayerPassword(login, newPassword);
		Logger.info("Saving Galaxy after changing password");
		gs.saveGalaxy(nameOfGame, "saves", g);
		gs.saveGalaxy(nameOfGame + "_" + g.getTurn(), "saves/previous", g);

	}

	public boolean getLastUpdateComplete() {
		return g.getLastUpdateComplete();
	}

	public String getLastLog() {
		String lastLog = g.getLastLog();
		lastLog = lastLog.replaceAll("\n", "<br>");
		return lastLog;
	}

	/*
	 * // testing... public static void main (String[] args){ boolean okToDelete =
	 * false; Calendar gameCal = Calendar.getInstance();
	 * gameCal.roll(Calendar.DATE,7); SimpleDateFormat sdf = new
	 * SimpleDateFormat("yy-MM-dd HH:mm:ss");
	 * System.out.println(sdf.format(gameCal.getTime())); Calendar nowCal =
	 * Calendar.getInstance(); System.out.println(sdf.format(nowCal.getTime())); if
	 * (nowCal.after(gameCal)){ okToDelete = true;
	 * System.out.println("now after game"); }else{
	 * System.out.println("now before game"); }
	 * 
	 * }
	 */
	public boolean isPasswordProtected() {
		boolean passwordProtected = false;
		if ((g.getPassword() != null) && (!g.getPassword().equals(""))) {
			passwordProtected = true;
		}
		return passwordProtected;
	}

	public int getSoloWin() {
		return g.getSingleVictory();
	}

	public int getNumberOfStartPlanet() {
		return g.getNumberOfStartPlanet();
	}

	public int getFactionWin() {
		return g.getFactionVictory();
	}

	public boolean checkGamePassword(String aPassword) {
		boolean passwordOk = false;
		if (g.getPassword().equals(aPassword)) {
			passwordOk = true;
		}
		return passwordOk;
	}

	public void removeGame() {
		sh.removeGame(this);
	}

	public MessageDatabase getMessageDatabase() {
		if (messageDatabase == null) {
			messageDatabase = new MessageDatabase(nameOfGame);
			new MessageDataBaseSaver().saveMessageDataBase(nameOfGame, messageDatabase);
		}
		return messageDatabase;
	}
}
