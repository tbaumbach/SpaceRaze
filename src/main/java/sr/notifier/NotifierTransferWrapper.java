package sr.notifier;

import java.io.Serializable;
import java.util.List;

import spaceraze.servlethelper.CreateNewGameData;
import spaceraze.servlethelper.GameListData;
import spaceraze.util.general.RankedPlayer;
import spaceraze.servlethelper.ReturnGames;
import spaceraze.world.Map;

public class NotifierTransferWrapper implements Serializable {
	static final long serialVersionUID = 1L;
	// request data
	private String userLogin;
	private ReturnGames returnGames;
	private int changeTurn; // if set, the game should be advanced/rollbacked this number of turns. 0 = no change.
	private String gameName; // only used if changeTurn != null
	private boolean deleteGame;
	private boolean getAllMaps; // hämta kartor och gameworlds
	private CreateNewGameData createNewGameData;
	private boolean android; // true if caller is the android client
	private int androidVersion; // used if caller is android client. If version is too low returnCode should contain specific error message
	// response data
	private String returnCode;
	private GameListData gameListData;
	private List<Map> allMaps;
	private RankedPlayer rankingData;
	private int gameId; // används av Android för att veta id på ett nyskapat spel

	public GameListData getGameListData() {
		return gameListData;
	}
	
	public void setGameListData(GameListData gameListData) {
		this.gameListData = gameListData;
	}
	
	public String getUserLogin() {
		return userLogin;
	}
	
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public ReturnGames getReturnGames() {
		return returnGames;
	}

	public void setReturnGames(ReturnGames returnGames) {
		this.returnGames = returnGames;
	}

	public int getChangeTurn() {
		return changeTurn;
	}

	public void setChangeTurn(int changeTurn) {
		this.changeTurn = changeTurn;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public boolean isDeleteGame() {
		return deleteGame;
	}

	public void setDeleteGame(boolean deleteGame) {
		this.deleteGame = deleteGame;
	}

	public CreateNewGameData getCreateNewGameData() {
		return createNewGameData;
	}

	public void setCreateNewGameData(CreateNewGameData createNewGameData) {
		this.createNewGameData = createNewGameData;
	}

	public boolean isGetAllMaps() {
		return getAllMaps;
	}

	public void setGetAllMaps(boolean getAllMaps) {
		this.getAllMaps = getAllMaps;
	}

	public List<Map> getAllMaps() {
		return allMaps;
	}

	public void setAllMaps(List<Map> allMaps) {
		this.allMaps = allMaps;
	}
	
	public boolean isAndroid() {
		return android;
	}

	public void setAndroid(boolean android) {
		this.android = android;
	}
	
	public int getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(int androidVersion) {
		this.androidVersion = androidVersion;
	}

	public RankedPlayer getRankingData() {
		return rankingData;
	}

	public void setRankingData(RankedPlayer rankingData) {
		this.rankingData = rankingData;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

}
