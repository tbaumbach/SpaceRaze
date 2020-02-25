package spaceraze.servlet.game;

import spaceraze.world.Player;
import spaceraze.world.VIP;

public class VIPInfo {

	private String type, owner, name, shortName;
	private int id = -1, kills = -1;
	
	VIPInfo(VIP aVip, Player player){
		type = aVip.getTypeName();
					
		if(aVip.getBoss() != null){
			
			if(player.getName().equals(aVip.getBoss().getName())){
				owner = aVip.getBoss().getGovenorName();
				name = aVip.getName();
				shortName = aVip.getShortName();
				id = aVip.getId();
				kills = aVip.getKills();
			}else if(player.getGalaxy().getDiplomacy().checkAllianceWithAllInConfederacy(player, aVip.getBoss())){
				// Ägaren till VIPen är en allierad vilket betyder att spelarn får veta vem som äger VIPen.
				owner = aVip.getBoss().getGovenorName();
			}
		}
		
		
	}

	public String getType() {
		return type;
	}

	public String getOwner() {
		return owner;
	}

	public int getId() {
		return id;
	}

	public int getKills() {
		return kills;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}
}
