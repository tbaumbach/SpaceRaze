package sr.notifier;

import java.io.Serializable;
import java.util.List;

import sr.world.StatisticGameType;

public class CreateNewGameData implements Serializable {
	static final long serialVersionUID = 1L;
	private String gameWorldFileName, gameName, mapName, autoBalanceString, timeString, emailPlayers, maxNrPlayers, userLogin, gamePassword, groupFaction, randomFactionString, diplomacy;
	private List<String> selectableFactionNames;
	private StatisticGameType statisticGameType;
	private int endTurn, numberOfStartPlanet;
	
	public String getGameName() {
		return gameName;
	}
	
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public String getGameWorldFileName() {
		return gameWorldFileName;
	}
	
	public void setGameWorldFileName(String gameWorldFileName) {
		this.gameWorldFileName = gameWorldFileName;
	}
	
	public String getMapName() {
		return mapName;
	}
	
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getMaxNrPlayers() {
		return maxNrPlayers;
	}

	public void setMaxNrPlayers(String maxNrPlayers) {
		this.maxNrPlayers = maxNrPlayers;
	}

	public String getAutoBalanceString() {
		return autoBalanceString;
	}

	public void setAutoBalanceString(String autoBalanceString) {
		this.autoBalanceString = autoBalanceString;
	}

	public String getDiplomacy() {
		return diplomacy;
	}

	public void setDiplomacy(String diplomacy) {
		this.diplomacy = diplomacy;
	}

	public String getEmailPlayers() {
		return emailPlayers;
	}

	public void setEmailPlayers(String emailPlayers) {
		this.emailPlayers = emailPlayers;
	}

	public String getGamePassword() {
		return gamePassword;
	}

	public void setGamePassword(String gamePassword) {
		this.gamePassword = gamePassword;
	}

	public String getGroupFaction() {
		return groupFaction;
	}

	public void setGroupFaction(String groupFaction) {
		this.groupFaction = groupFaction;
	}

	public String getRandomFactionString() {
		return randomFactionString;
	}

	public void setRandomFactionString(String randomFactionString) {
		this.randomFactionString = randomFactionString;
	}

	public List<String> getSelectableFactionNames() {
		return selectableFactionNames;
	}

	public void setSelectableFactionNames(List<String> selectableFactionNames) {
		this.selectableFactionNames = selectableFactionNames;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public int getEndTurn() {
		return endTurn;
	}

	public void setEndTurn(int endTurn) {
		this.endTurn = endTurn;
	}

	public int getNumberOfStartPlanet() {
		return numberOfStartPlanet;
	}

	public void setNumberOfStartPlanet(int numberOfStartPlanet) {
		this.numberOfStartPlanet = numberOfStartPlanet;
	}

	public StatisticGameType getStatisticGameType() {
		return statisticGameType;
	}

	public void setStatisticGameType(StatisticGameType statisticGameType) {
		this.statisticGameType = statisticGameType;
	}

}
