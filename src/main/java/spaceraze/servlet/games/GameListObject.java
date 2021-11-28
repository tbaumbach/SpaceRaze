package spaceraze.servlet.games;

import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import sr.server.SR_Server;
import sr.server.UpdateRunner;

public class GameListObject {
	
	private String gameName, mapName, status, startedByPlayerName, startedByPlayerId, nextUpdate, playerGameStatus;
	private int gameId, turn, endTurn, numberOfStartPlanets, maxNrOfPlayers, nrActivePlayers;
	private boolean playerHaveMail = Boolean.FALSE, participate = Boolean.FALSE, passwordProteced;
	
	public GameListObject(){}

	public GameListObject(SR_Server aServer) {
		
		gameName = aServer.getGameName();
		mapName = aServer.getGalaxy().getMapNameFull();
		turn = aServer.getTurn();
		status = aServer.getStatus();
		startedByPlayerName = aServer.getStartedByPlayerName();
		startedByPlayerId = aServer.getStartedByPlayer();
		gameId = aServer.getId();
		numberOfStartPlanets = aServer.getNumberOfStartPlanet();
		endTurn = aServer.getEndTurn();
		maxNrOfPlayers = aServer.getGalaxy().getNrStartPlanets();
		nrActivePlayers = PlayerPureFunctions.getActivePlayers(aServer.getGalaxy()).size();
		passwordProteced = aServer.isPasswordProtected();
		nextUpdate = "None";
		
		
	}

	public GameListObject(SR_Server aServer, String playerGameStatus, boolean haveMail) {
		this(aServer);
		
		this.playerGameStatus = playerGameStatus;
		this.playerHaveMail = haveMail;
		this.participate = Boolean.TRUE;
		UpdateRunner ur = aServer.getUpdateRunner();
		if (ur != null){
			nextUpdate = ur.getNextUpdateShort();
		}
		
	}

	public String getGameName() {
		return gameName;
	}

	public String getMapName() {
		return mapName;
	}

	public String getStatus() {
		return status;
	}

	public String getStartedByPlayerName() {
		return startedByPlayerName;
	}

	public String getStartedByPlayerId() {
		return startedByPlayerId;
	}

	public int getGameId() {
		return gameId;
	}

	public int getTurn() {
		return turn;
	}

	public int getNumberOfStartPlanets() {
		return numberOfStartPlanets;
	}

	public String getNextUpdate() {
		return nextUpdate;
	}

	public String getPlayerGameStatus() {
		return playerGameStatus;
	}

	public int getEndTurn() {
		return endTurn;
	}

	public int getMaxNrOfPlayers() {
		return maxNrOfPlayers;
	}

	public int getNrActivePlayers() {
		return nrActivePlayers;
	}

	public boolean isPlayerHaveMail() {
		return playerHaveMail;
	}

	public boolean isParticipate() {
		return participate;
	}

	public boolean isPasswordProteced() {
		return passwordProteced;
	};
	
	

}
