package spaceraze.webb.support.world;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.util.general.Logger;
import spaceraze.world.Faction;
import spaceraze.world.GameWorld;
import spaceraze.world.SpaceshipType;
import spaceraze.servlethelper.comparator.SpaceshipTypeComparator;

public class GameWorldHelper {
	
	private GameWorld gameWorld;
	
	public GameWorldHelper(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	// TODO Remove
		/*
		 * Only used by the battlesim page? Civilian ships are filtered out!
		 */
	public String getSpaceshipTypeOptionsHTML(){
		StringBuffer retHTML = new StringBuffer();
		List<SpaceshipType> militaryShipTypes = removeCivilianShips(gameWorld.getShipTypes());
		Collections.sort(militaryShipTypes,new SpaceshipTypeComparator());
		for (SpaceshipType tmpsst : militaryShipTypes) {
			retHTML.append("<option value=\"");
			retHTML.append(tmpsst.getShortName());
			retHTML.append("\">");
			retHTML.append(tmpsst.getName());
			retHTML.append("</option>\n");
		}
		return retHTML.toString();
	}
	
	private List<SpaceshipType> removeCivilianShips(List<SpaceshipType> allShipTypes){
		List<SpaceshipType> newList = new LinkedList<SpaceshipType>();
		for (SpaceshipType shipType : allShipTypes) {
			if (!shipType.isCivilian()){
				newList.add(shipType);
			}
		}
		return newList;
	}
	
	public String getFactionsTableContentHTML(){
		StringBuffer retHTML = new StringBuffer();
		retHTML.append(Faction.getHTMLHeaderRow());
		for (Faction tmpf : gameWorld.getFactions()) {
			retHTML.append(tmpf.getHTMLTableRow(gameWorld.getFileName()));
		}
		return retHTML.toString();
	}
	
	public String getFactionsTableContentHTMLNO(){
		StringBuffer retHTML = new StringBuffer();
		retHTML.append(Faction.getHTMLHeaderRowNO());
		int i = 0;
		for (Faction tmpf : gameWorld.getFactions()) {
			i = i + 1;
			String RowName = i + "GameWorldListRow";
			retHTML.append(tmpf.getHTMLTableRowNO(gameWorld.getFileName(),RowName,i));
		}
		return retHTML.toString();
	}
	
	public String getFactionsCheckboxesHTML(){
		StringBuffer retHTML = new StringBuffer();
		for (Faction tmpf : gameWorld.getFactions()) {
			retHTML.append(tmpf.getHTMLCheckbox());
		}
		return retHTML.toString();
	}
	
	public List<String> getFactionsNames(){
		List<String> factionNames = new LinkedList<String>();
		for (Faction aFaction : gameWorld.getFactions()) {
			factionNames.add(aFaction.getName());
		}
		return factionNames;
	}

	public static String getHTMLHeaderRow(){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		sb.append("<td>Name</td>");
		sb.append("<td>Filename</td>");
		sb.append("<td>#Factions</td>");
		sb.append("<td>#Shiptypes</td>");
		sb.append("<td>#VipTypes</td>");
		sb.append("<td>Created By</td>");
		sb.append("<td>Created Date</td>");
		sb.append("<td>Last Changed</td>");
		sb.append("</tr>\n");
		return sb.toString();
	}

	public static String getHTMLHeaderRowNO(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr class='ListheaderRow'>");
		sb.append("<td class='ListHeader'></td>");
		sb.append("<td class='ListHeader'><div class='SolidText' style='padding-left:5px;'>Name</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>Filename</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>#Factions</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>#Ship</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>#Vip</div></td>");
		//sb.append("<td class='ListHeader'><div class='SolidText'>Todo</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>Created By</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>Created Date</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>Last Changed</div></td>");
		sb.append("<td class='ListHeader'></td>");
		sb.append("</tr>\n");
		
			return sb.toString();
	}
	
	public String getHTMLTableRowNO(int i, String RowName){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',10,1);\" onMouseOut=\"TranparentRow('" + RowName + "',10,0);\">");
		sb.append("<td id='" + RowName + "1' width='3' class='ListText'></td>");
		sb.append("<td id='" + RowName + "2' class='ListText'><div class='SolidText' style='padding-left:5px;'><a href=\"Master.jsp?action=gameworld&gameworldfilename=" + gameWorld.getFileName() + "\">" + gameWorld.getFullName() + "</a></div></td>");
		sb.append("<td id='" + RowName + "3' class='ListText'><div class='SolidText'>" + gameWorld.getFileName() + "</div></td>");
		sb.append("<td id='" + RowName + "4' class='ListText'><div class='SolidText'>" + gameWorld.getFactions() + "</div></td>");
		sb.append("<td id='" + RowName + "5' class='ListText'><div class='SolidText'>" + gameWorld.getShipTypes() + "</div></td>");
		sb.append("<td id='" + RowName + "6' class='ListText'><div class='SolidText'>" + gameWorld.getVipTypes().size() + "</div></td>");
		sb.append("<td id='" + RowName + "7' class='ListText'><div class='SolidText'>" + gameWorld.getCreatedByUser() + "</div></td>");
		sb.append("<td id='" + RowName + "8' class='ListText'><div class='SolidText'>" + gameWorld.getCreatedDate() + "</div></td>");
		sb.append("<td id='" + RowName + "9' class='ListText'><div class='SolidText'>" + gameWorld.getChangedDate() + "</div></td>");
		sb.append("<td id='" + RowName + "10' width='3' class='ListText'></td>");
		sb.append("</tr>\n");
		return sb.toString();
	}

	public static String getGameWorldsTableContentHTMLNO(){
		StringBuffer retHTML = new StringBuffer();
		List<GameWorld> allGameWorlds = GameWorldHandler.getGameWorldTypes();
		Logger.finest("gw size " + allGameWorlds.size());
		retHTML.append(GameWorldHelper.getHTMLHeaderRowNO());
		int i = 0;
		for (GameWorld tmpgw : allGameWorlds) {
			i = i + 1;
			String rowName = i + "GameWorldListRow";
			retHTML.append(new GameWorldHelper(tmpgw).getHTMLTableRowNO(i,rowName));
		}
		return retHTML.toString();
	}

}
