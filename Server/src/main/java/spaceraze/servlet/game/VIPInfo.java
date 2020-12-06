package spaceraze.servlet.game;

import spaceraze.servlethelper.game.DiplomacyPureFunctions;
import spaceraze.world.Player;
import spaceraze.world.VIP;

public class VIPInfo {

	private String type, owner, name, shortName;
	private int kills = -1;
	private String id;
	
	VIPInfo(VIP aVip, Player player){
		type = aVip.getTypeName();
					
		if(aVip.getBoss() != null){
			
			if(player.getName().equals(aVip.getBoss().getName())){
				owner = aVip.getBoss().getGovernorName();
				name = aVip.getName();
				shortName = aVip.getShortName();
				id = aVip.getUniqueId();
				kills = aVip.getKills();
			}else if(DiplomacyPureFunctions.checkAllianceWithAllInConfederacy(player, aVip.getBoss(), player.getGalaxy())){
				// Ägaren till VIPen är en allierad vilket betyder att spelarn får veta vem som äger VIPen.
				owner = aVip.getBoss().getGovernorName();
			}
		}
		
		
	}

	public String getType() {
		return type;
	}

	public String getOwner() {
		return owner;
	}

	public String getId() {
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
