package spaceraze.servlet.game;

import spaceraze.world.Player;

public class PlayerInfo {
	
	private String name, faction;
	
	PlayerInfo(Player player){
		name = player.getGovenorName();
		faction = player.getFaction().getName();
	}

	public String getName() {
		return name;
	}

	public String getFaction() {
		return faction;
	}

}
