package sr.notifier;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class GameListData implements Serializable {
	static final long serialVersionUID = 1L;
	private List<GameData> games;
	
	public GameListData() {
		games = new LinkedList<GameData>();
	}
	
	public void addGame(GameData aGameData){
		games.add(aGameData);
	}
	
	public List<GameData> getGames(){
		return games;
	}

}
