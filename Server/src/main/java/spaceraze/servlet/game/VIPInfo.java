package spaceraze.servlet.game;

import spaceraze.servlethelper.game.DiplomacyPureFunctions;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
import spaceraze.world.Player;
import spaceraze.world.VIP;
import spaceraze.world.VIPType;

public class VIPInfo {

	private String type, owner, name, shortName;
	private int kills = -1;
	private String key;
	
	VIPInfo(VIP aVip, Player player){
		VIPType vipType = VipPureFunctions.getVipTypeByKey(aVip.getTypeKey(), player.getGalaxy().getGameWorld());
		type = vipType.getName();
					
		if(aVip.getBoss() != null){
			
			if(player.getName().equals(aVip.getBoss().getName())){
				owner = aVip.getBoss().getGovernorName();
				name = vipType.getName();
				shortName = vipType.getShortName();
				key = aVip.getKey();
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
