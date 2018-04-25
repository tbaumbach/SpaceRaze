package sr.notifier;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import sr.world.Player;

/**
 * Contains all data for a single game to be shown in a list in the client application
 * @author bodinp
 *
 */
public class GameData implements Serializable {
	static final long serialVersionUID = 1L;
	private String gameName,mapName,status,nextUpdate,gameWorldName,updatesWeek,password,gameOverStatus;
	private String scenarioFileName; // used in droid client for single player games
	private Calendar nextUpdateDate;
	private int turn,maxTurn;
	private int nrPlayers,nrPlayersMax; 
	private int gameId;
	private int mapMaxPlayers;
	private String[][] players;
	
	public String[] getColumnData(){
		String[] data = new String[8];
		data[0] = gameName;
		data[1] = mapName;
		data[2] = gameWorldName;
		data[3] = nrPlayers + "/" + nrPlayersMax;
		data[4] = status;
		data[5] = turn + (maxTurn>0 ? ("/" + maxTurn) : "");
		data[6] = updatesWeek;
		data[7] = nextUpdate;
		return data;
	}

	public static String[] getColumnHeaders(){
		String[] data = new String[8];
		data[0] = "Game Name";
		data[1] = "Map Name";
		data[2] = "GameWorld Name";
		data[3] = "Players";
		data[4] = "Status";
		data[5] = "Turn";
		data[6] = "Updates";
		data[7] = "Next Update";
		return data;
	}

	public static int[] getColumnsWidths(){
		int[] data = new int[8];
		data[0] = 120;
		data[1] = 120;
		data[2] = 150;
		data[3] = 50;
		data[4] = 80;
		data[5] = 50;
		data[6] = 70;
		data[7] = 100;
		return data;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public String getGameName() {
		return gameName;
	}
	
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public String getGameWorldName() {
		return gameWorldName;
	}
	
	public void setGameWorldName(String gameWorldName) {
		this.gameWorldName = gameWorldName;
	}
	
	public String getMapName() {
		return mapName;
	}
	
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	public int getMaxTurn() {
		return maxTurn;
	}
	
	public void setMaxTurn(int maxTurn) {
		this.maxTurn = maxTurn;
	}
	
	public String getNextUpdate() {
		return nextUpdate;
	}
	
	public void setNextUpdate(String nextUpdate) {
		this.nextUpdate = nextUpdate;
	}
	
	public int getNrPlayers() {
		return nrPlayers;
	}
	
	public void setNrPlayers(int nrPlayers) {
		this.nrPlayers = nrPlayers;
	}
	
	public int getNrPlayersMax() {
		return nrPlayersMax;
	}
	
	public void setNrPlayersMax(int nrPlayersMax) {
		this.nrPlayersMax = nrPlayersMax;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}
	
	public String getUpdatesWeek() {
		return updatesWeek;
	}
	
	public void setUpdatesWeek(String updatesWeek) {
		this.updatesWeek = updatesWeek;
	}
	
	public void setPlayers(List<Player> playersList, boolean isAndroid){
		players = new String[playersList.size()][4];
		int i = 0;
		for (Player aPlayer : playersList) {
			players[i][0] = aPlayer.getName();
			if (isAndroid){ // för Androidklienten så skall inte lösenordet returneras, utan istället vill Androidklienten veta vad varje spelare är för faction
				players[i][1] = aPlayer.getFactionName();
			}else{
				players[i][1] = aPlayer.getPassword();
			}
			String statusChar = "x";
			if (aPlayer.isFinishedThisTurn() | aPlayer.isDefeated()){
				statusChar = "n";
			}else
			if (aPlayer.getUpdatedThisTurn()){
				statusChar = "s";
			}
			players[i][2] = statusChar;
			players[i][3] = aPlayer.getFaction().getColorValues();
			i++;
		}
	}
	
	public boolean containsPlayer(String playerName){
		boolean containsPlayer = false;
		int index = 0;
		while((!containsPlayer) & (index < players.length)){
			if (players[index][0].equals(playerName)){
				containsPlayer = true;
			}else{
				index++;
			}
		}
		return containsPlayer;
	}
	
	public String[][] getPlayers(){
		return players;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getGameOverStatus() {
		return gameOverStatus;
	}

	public void setGameOverStatus(String gameOverStatus) {
		this.gameOverStatus = gameOverStatus;
	}

	public Calendar getNextUpdateCalendar() {
		return nextUpdateDate;
	}

	public void setNextUpdateCalendar(Calendar nextUpdateDate) {
		this.nextUpdateDate = nextUpdateDate;
	}

	public int getMapMaxPlayers() {
		return mapMaxPlayers;
	}

	public void setMapMaxPlayers(int mapMaxPlayers) {
		this.mapMaxPlayers = mapMaxPlayers;
	}

	public String getScenarioFileName() {
		return scenarioFileName;
	}

	public void setScenarioFileName(String scenarioFileName) {
		this.scenarioFileName = scenarioFileName;
	}

}
