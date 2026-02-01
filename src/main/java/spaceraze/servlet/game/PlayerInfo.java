package spaceraze.servlet.game;

import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.game.Player;
import spaceraze.world.GameWorld;

public class PlayerInfo {
	
	private String name, faction;
	
	PlayerInfo(Player player, GameWorld gameWorld){
		name = player.getGovernorName();
		faction = GameWorldHandler.getFactionByUuid(player.getFactionUuid(), gameWorld).getName();
	}

	public String getName() {
		return name;
	}

	public String getFaction() {
		return faction;
	}

}
