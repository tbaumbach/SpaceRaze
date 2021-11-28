package spaceraze.servlet.game;

import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.world.Player;

public class PlayerInfo {
	
	private String name, faction;
	
	PlayerInfo(Player player){
		name = player.getGovernorName();
		faction = GameWorldHandler.getFactionByKey(player.getFactionKey(), player.getGalaxy().getGameWorld()).getName();
	}

	public String getName() {
		return name;
	}

	public String getFaction() {
		return faction;
	}

}
