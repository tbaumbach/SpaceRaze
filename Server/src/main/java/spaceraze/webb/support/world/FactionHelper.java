package spaceraze.webb.support.world;

import java.util.Collections;

import spaceraze.world.Faction;
import spaceraze.world.SpaceshipType;
import spaceraze.world.VIPType;
import spaceraze.world.comparator.SpaceshipTypeComparator;

public class FactionHelper {
	
	private Faction faction;
	
	public FactionHelper(Faction faction) {
		this.faction = faction;
	}
	
	public String getSpaceshipTypesTableContentHTML(){
		StringBuffer retHTML = new StringBuffer();
		retHTML.append(SpaceShipHelper.getHTMLHeaderRow());
		Collections.sort(faction.getSpaceshipTypes(),new SpaceshipTypeComparator());
		for (SpaceshipType sst : faction.getSpaceshipTypes()) {
			retHTML.append(SpaceShipHelper.getHTMLTableRow(sst));
		}
		return retHTML.toString();
	}

	public String getStartingSpaceshipsHTML(){
		StringBuffer retHTML = new StringBuffer();
		for (SpaceshipType sst : faction.getStartingShipTypes()) {
			retHTML.append(sst.getName() + "<br>");
		}
		return retHTML.toString();
	}

	public String getStartVIPTypesTableContentHTML(){
		StringBuffer retHTML = new StringBuffer();
		retHTML.append(faction.getGovernorVIPType().getHTMLTableContent());
		for (VIPType vt : faction.getStartingVIPTypes()) {
			retHTML.append(vt.getHTMLTableContent());
		}
		return retHTML.toString();
	}

}
