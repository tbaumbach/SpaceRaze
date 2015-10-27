package sr.world.mapinfo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Data om alla vippar från en spelare
 * 
 * @author developer
 */
public class VIPData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String playerName;
	private List<String> vipShortNames;	
	
	public VIPData(){
		vipShortNames = new LinkedList<String>();
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public List<String> getVipShortNames() {
		return vipShortNames;
	}

	public void addVipShortName(String vipShortName) {
		vipShortNames.add(vipShortName);
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("vipShortNames, playerName: " + playerName);
		sb.append("\n");
		for (String str : vipShortNames) {
			sb.append("\tvipShortName: " + str);
			sb.append("\n");
		}
		return sb.toString();
	}

}
