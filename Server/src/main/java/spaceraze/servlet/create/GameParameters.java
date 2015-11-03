package spaceraze.servlet.create;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.world.StatisticGameType;

public class GameParameters {
	
	
	
	
	private String gameWorldName;
	private String gameName;
	private String mapName;
	private String steps;
	private String autoBalance;
	private String time;
	private String emailPlayers;
	private String maxNrPlayers;
	private String user;
	private String gamePassword;
	private String groupFaction;
	private List<String> selectableFactionNames;
	private String randomFaction;
	private String diplomacy;
	private boolean singelPlayer;
	private String ranked;
	private int singleVictory;
	private int factionVictory;
	private int endTurn;
	private int numberOfStartPlanet;
	private StatisticGameType statisticGameType;
	
	public GameParameters(){};

	public GameParameters(String gameWorldName, String gameName, String mapName, String steps, String autoBalance, 
			String time, String emailPlayers, String maxNrPlayers, String user, String gamePassword, 
			String groupFaction, List<String> selectableFactionNames, String randomFaction, String diplomacy, 
			boolean singelPlayer, String ranked, int singleVictory, int factionVictory, int endTurn, 
			int numberOfStartPlanet, StatisticGameType statisticGameType){
		
		this.gameWorldName = gameWorldName;
		this.gameName = gameName;
		this.mapName = mapName;
		this.steps = steps;
		this.autoBalance = autoBalance;
		this.time = time;
		this.emailPlayers = emailPlayers;
		this.maxNrPlayers = maxNrPlayers;
		this.user = user;
		this.gamePassword = gamePassword;
		this.groupFaction = groupFaction;
		this.selectableFactionNames = selectableFactionNames;
		this.randomFaction = randomFaction;
		this.diplomacy = diplomacy;
		this.singelPlayer = singelPlayer;
		this.ranked = ranked;
		this.singleVictory = singleVictory;
		this.factionVictory = factionVictory;
		this.endTurn = endTurn;
		this.numberOfStartPlanet = numberOfStartPlanet;
		this.statisticGameType = statisticGameType;
	}
	
	
	public GameParameters(String gameWorldName){
		
		this.gameWorldName = gameWorldName;
	}

	public String getGameWorldName() {
		return gameWorldName;
	}

	public String getGameName() {
		return gameName;
	}

	public String getMapName() {
		return mapName;
	}

	public String getSteps() {
		return steps;
	}

	public String getAutoBalance() {
		return autoBalance;
	}

	public String getTime() {
		return time;
	}

	public String getEmailPlayers() {
		return emailPlayers;
	}

	public String getMaxNrPlayers() {
		return maxNrPlayers;
	}

	@JsonIgnore
	public String getUser() {
		return user;
	}

	public String getGamePassword() {
		return gamePassword;
	}

	public String getGroupFaction() {
		return groupFaction;
	}

	public List<String> getSelectableFactionNames() {
		return selectableFactionNames;
	}

	public String getRandomFaction() {
		return randomFaction;
	}

	@JsonIgnore
	public String getDiplomacy() {
		return diplomacy;
	}

	@JsonIgnore
	public boolean isSingelPlayer() {
		return singelPlayer;
	}

	public String getRanked() {
		return ranked;
	}

	public int getSingleVictory() {
		return singleVictory;
	}

	public int getFactionVictory() {
		return factionVictory;
	}

	public int getEndTurn() {
		return endTurn;
	}

	public int getNumberOfStartPlanet() {
		return numberOfStartPlanet;
	}

	public StatisticGameType getStatisticGameType() {
		return statisticGameType;
	}

	public void setGameWorldName(String gameWorldName) {
		this.gameWorldName = gameWorldName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public void setAutoBalance(String autoBalance) {
		this.autoBalance = autoBalance;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setEmailPlayers(String emailPlayers) {
		this.emailPlayers = emailPlayers;
	}

	public void setMaxNrPlayers(String maxNrPlayers) {
		this.maxNrPlayers = maxNrPlayers;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setGamePassword(String gamePassword) {
		this.gamePassword = gamePassword;
	}

	public void setGroupFaction(String groupFaction) {
		this.groupFaction = groupFaction;
	}

	public void setSelectableFactionNames(List<String> selectableFactionNames) {
		this.selectableFactionNames = selectableFactionNames;
	}

	public void setRandomFaction(String randomFaction) {
		this.randomFaction = randomFaction;
	}

	public void setDiplomacy(String diplomacy) {
		this.diplomacy = diplomacy;
	}

	public void setSingelPlayer(boolean singelPlayer) {
		this.singelPlayer = singelPlayer;
	}

	public void setRanked(String ranked) {
		this.ranked = ranked;
	}

	public void setSingleVictory(int singleVictory) {
		this.singleVictory = singleVictory;
	}

	public void setFactionVictory(int factionVictory) {
		this.factionVictory = factionVictory;
	}

	public void setEndTurn(int endTurn) {
		this.endTurn = endTurn;
	}

	public void setNumberOfStartPlanet(int numberOfStartPlanet) {
		this.numberOfStartPlanet = numberOfStartPlanet;
	}

	public void setStatisticGameType(StatisticGameType statisticGameType) {
		this.statisticGameType = statisticGameType;
	}

}
