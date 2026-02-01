package spaceraze.servlet.game;

import spaceraze.game.Galaxy;
import spaceraze.servlethelper.game.DiplomacyPureFunctions;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
import spaceraze.game.Player;
import spaceraze.game.VIP;
import spaceraze.world.GameWorld;
import spaceraze.world.VIPType;

public class VIPInfo {

	private String type, owner, name, shortName;
	private int kills = -1;
	private String key;
	
	VIPInfo(VIP aVip, Player player, Galaxy galaxy, GameWorld gameWorld){
		VIPType vipType = VipPureFunctions.getVipTypeByUuid(aVip.getTypeUuid(), gameWorld);
		type = vipType.getName();
					
		if(aVip.getBoss() != null){
			
			if(player.getName().equals(aVip.getBoss().getName())){
				owner = aVip.getBoss().getGovernorName();
				name = vipType.getName();
				shortName = vipType.getShortName();
				key = aVip.getUuid();
				kills = aVip.getKills();
			}else if(DiplomacyPureFunctions.checkAllianceWithAllInConfederacy(player, aVip.getBoss(), galaxy)){
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

	public String getKey() {
		return key;
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
