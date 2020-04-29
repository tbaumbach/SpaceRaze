/*
 * Created on 2005-feb-01
 */
package sr.server;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import spaceraze.servlet.game.GameParameters;
import spaceraze.servlethelper.GameData;
import spaceraze.servlethelper.GameListData;
import spaceraze.servlethelper.ReturnGames;
import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.util.general.Logger;
import spaceraze.util.properties.PropertiesHandler;
import spaceraze.world.Faction;
import spaceraze.world.Galaxy;
import spaceraze.world.GameWorld;
import spaceraze.world.Map;
import spaceraze.world.Player;
import spaceraze.world.StatisticGameType;
import spaceraze.world.enums.DiplomacyGameType;
import sr.server.map.MapHandler;
import sr.server.persistence.PHash;
import sr.webb.mail.MailHandler;
import sr.webb.users.User;

/**
 * @author WMPABOD
 * 
 * This class handles multiple servers in a web application framework.
 */
public class ServerHandler {
	private List<SR_Server> allServers;
	private int startId = 1;

	public ServerHandler(){
		allServers = new LinkedList<SR_Server>();
		// load all current games
		String path = PropertiesHandler.getProperty("datapath") + "saves";
		File f = new File(path);
		String[] files = f.list();
		int curId = startId;
		for (int i = 0; i < files.length; i++) {
			String pathAndFile = path + File.separator + files[i]; 
			File tmp = new File(pathAndFile);
			if (!tmp.isDirectory()){
				String nameOfGame = tmp.getName().substring(0,tmp.getName().length()-4);
				SR_Server aServer = new SR_Server(nameOfGame,curId,this);
				Logger.finer("server created: " + aServer.getGameName());
				Logger.finer("server created: " + aServer.getGameName() + " " + aServer.getMapFileName() + " " + aServer.getGalaxy().getNrStartPlanets());
				allServers.add(aServer);
	//			aServer.setStartedByPlayer()
	//			startedByPlayer = aServer.getGalaxy()aServer.getStartedByPlayer();
				curId++;
			}
		}
	}
	
	
	public String getCurrentGamesList(User aUser){
		String retStr = "<table>";
		retStr = retStr + "<tr><td>Game Name&nbsp;&nbsp;&nbsp;</td><td>Map Name&nbsp;&nbsp;&nbsp;</td><td>Current Turn&nbsp;&nbsp;&nbsp;</td><td>Game State&nbsp;&nbsp;&nbsp;</td><td>Started by&nbsp;&nbsp;&nbsp;</td><td></td><td></td></tr>\n";
		retStr = retStr + "<tr><td colspan=\"7\" bgcolor=\"#FFBF00\" height=\"1\"><img src=\"images/pix.gif\" height=\"1\"></td></tr>\n";
		SR_Server[] allSerArr = getServers();
		if (allSerArr.length == 0){
			retStr = retStr + "<td colspan=\"6\">No games found</td></tr>\n";
		}else{
			for (int i = 0; i < allSerArr.length; i++) {
				SR_Server aServer = allSerArr[i];
				retStr = retStr + "<tr valign=\"bottom\"><td>" + aServer.getGameName() + "</td><td>" + aServer.getMapFileName() + "</td><td>" + aServer.getTurn() + "</td><td>" + aServer.getStatus() + "</td><td>" + aServer.getStartedByPlayerName() + " (" + aServer.getStartedByPlayer() + ")</td><td><a href='current_game.jsp?port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=false&returnto=current_games.jsp'>Details</a></td><td></td></tr>\n";
			}
		}
		retStr = retStr + "</table>";
		return retStr;
	}

	public String getCurrentPlayingGamesListNO(User aUser){
		
		String mail ="";
	 String retStr = "<table class='ListTable' cellspacing='0' cellpadding='0' width='100%'><tr height=1 class='ListLine'><td colspan='12'></td>";
	 retStr = retStr + "<tr class='ListheaderRow' height='16' style='width:250'><td class='ListHeader' WIDTH='3'></td><td class='ListHeader' WIDTH='25'></td><td class='ListHeader' nowrap><div class='SolidText'>Game Name&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>GameWorld&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Map&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Players&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Status&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Turn&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Next update&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Started by&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'></td><td class='ListHeader'></td></tr>\n";
	 
	 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 // retStr = retStr + "<tr class='ListheaderRow' height="16" style="width:250">
	  // <td class='ListHeader' WIDTH='3'></td>
	  // <td class='ListHeader' WIDTH='25'></td>
	  // <td class='ListHeader'><div class="SolidText">Game Name&nbsp;&nbsp;&nbsp;</div></td>
	  // <td class='ListHeader'><div class="SolidText">GameWorld&nbsp;&nbsp;&nbsp;</div></td>
	  // <td class='ListHeader'><div class="SolidText">Map Name&nbsp;&nbsp;&nbsp;</div></td>
	  // <td class='ListHeader'><div class="SolidText">Players&nbsp;&nbsp;&nbsp;</div></td>
	  // <td class='ListHeader'><div class="SolidText">Status&nbsp;&nbsp;&nbsp;</div></td>
	  //  <td class='ListHeader'><div class="SolidText">Turn&nbsp;&nbsp;&nbsp;</div></td>
	  // <td class='ListHeader'><div class="SolidText">Next update&nbsp;&nbsp;&nbsp;</div></td>
	  // <td class='ListHeader'><div class="SolidText">Started by&nbsp;&nbsp;&nbsp;</div></td>
	  // <td class='ListHeader'></td><td class='ListHeader'></td>
	  // </tr>\n";
	  
	 retStr = retStr + "<tr><td colspan=\"12\" bgcolor=\"#003902\" height=\"1\"><img src=\"images/pix.gif\" height=\"1\"></td></tr>\n";

	 SR_Server[] allSerArr = getServers();
		if (allSerArr.length == 0){
			retStr = retStr + "<td colspan=\"8\" class='ListText'>No games found</td></tr>\n";
		}else{
			int count = 0;
			for (int i = 0; i < allSerArr.length; i++) {
				
				String RowName = i + "GameListRow";
				
				
				SR_Server aServer = allSerArr[i];
				
				if (aServer.isPlayerParticipating(aUser)){
					Player tmpPlayer = aServer.getPlayer(aUser.getLogin(),aUser.getPassword());
					String nextUpdate = "None";
					UpdateRunner ur = aServer.getUpdateRunner();
					if (ur != null){
						nextUpdate = ur.getNextUpdateShort();
					}
					String iconName = null;
					String sEndTurn = "/" + aServer.getEndTurn();
					boolean addGame = false;
					boolean showJoin = true;
					if (!aServer.getLastUpdateComplete()){
						iconName = "error";
						addGame = true;
						showJoin = false;
					}else
					if (aServer.getGalaxy().gameEnded){
						if (tmpPlayer.isDefeated()){
							iconName = "defeat";
							addGame = true;
						}else{
							iconName = "victory";
							addGame = true;
						}
					}else
					if (tmpPlayer.isDefeated()){
						iconName = "defeat";
						addGame = true;
					}else
					if (tmpPlayer.isFinishedThisTurn()){
						iconName = "check";
						addGame = true;
					}else
					if (tmpPlayer.getUpdatedThisTurn()){
						iconName = "saved";
						addGame = true;
					}else
					if (aServer.getTurn() > 0){
						iconName = "cross";
						addGame = true;
					}
					if (aServer.getEndTurn() == 0){
						sEndTurn = "";

					}
					if (aServer.getMessageDatabase().haveNewMessages(tmpPlayer.getName()))
					{
						mail = "<img src=\"images/mail.jpg\" vspace=\"0\" hspace=\"0\"  border=\"0\">";
					}		
					
					if (addGame){
						GameWorld gw = aServer.getGalaxy().getGameWorld();
						retStr = retStr + "<tr class='ListTextRow' style='height:21px' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',12,1);\" onMouseOut=\"TranparentRow('" + RowName + "',12,0);\"  onclick=\"location.href='Master.jsp?action=current_game&port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'\"  >" +
								"<td id='" + RowName + "1' width='3' class='ListText'></td>" +
								"<td id='" + RowName + "2' width='48' valign='middle' class='ListText'><div class='SolidText'><img src=\"images/" + iconName + ".gif\" vspace=\"0\" hspace=\"0\"  border=\"0\">"+ mail +"</div></td>" +
								"<td id='" + RowName + "3' class='ListText' valign='middle'><div class='SolidText'>" + aServer.getGameName() + "</div></td><td id='" + RowName + "4' class='ListText'><div class='SolidText'>" + gw.getFileName() + "</div></td><td id='" + RowName + "5' class='ListText'><div class='SolidText'>" + aServer.getMapFileName() + "</div></td><td id='" + RowName + "6' class='ListText'><div class='SolidText'>" + aServer.getGalaxy().getNrActivePlayers() + "/" + aServer.getGalaxy().getNrPlayers() + "</div></td><td id='" + RowName + "7' class='ListText'><div class='SolidText'>" + aServer.getStatus() + "</div></td><td id='" + RowName + "8' class='ListText'><div class='SolidText'>" + aServer.getTurn()+"" + sEndTurn + "</div></td><td id='" + RowName + "9' class='ListText'><div class='SolidText'>" + nextUpdate + "</div></td><td id='" + RowName + "10' class='ListText'><div class='SolidText'>" + aServer.getStartedByPlayerName() + "</div></td><td id='" + RowName + "11' class='ListText'><div class='SolidText'></div></td><td id='" + RowName + "12' class='ListText'><div class='SolidText'>&nbsp;";
						if (showJoin){
							retStr = retStr + "&nbsp;";
						}
						retStr = retStr + "</div></td></tr>\n";
						mail="";
						count++;
					}
				}
			}
			if (count == 0){
				retStr = retStr + "<td width='20' class='ListText'></td><td colspan=\"10\" class='ListText'><div class='SolidText' style='padding-bottom:10px;'><b>You are not currently participating in any games</b></div></td></tr>\n";
			}
		}
		retStr = retStr + "</table>";
		return retStr;
	}
		
	public String getCurrentPlayingGamesListNOShort(User aUser){
				
		 String retStr = "<table class='ListTable' cellspacing='0' cellpadding='0' width='100%'><tr height=1 class='ListLine'><td colspan='12'></td>";
		 retStr = retStr + "<tr class='ListheaderRow' height='16' style='width:250'><td class='ListHeader' WIDTH='3'></td>" +
		 		"<td class='ListHeader' WIDTH='25'></td>" +
		 		"<td class='ListHeader' WIDTH='115'><div class='SolidText' nowrap>Name&nbsp;&nbsp;&nbsp;</div></td>" +
		 		"<td class='ListHeader' WIDTH='20'><div class='SolidText'>Turn&nbsp;&nbsp;&nbsp;</div></td>" +
		 		"<td class='ListHeader' WIDTH='70'><div class='SolidText'>Update&nbsp;&nbsp;&nbsp;</div></td>" +
		 		"<td class='ListHeader'WIDTH='20'></td>" +
		 		"</tr>\n";
		 
		  
		 retStr = retStr + "<tr><td colspan=\"12\" bgcolor=\"#003902\" height=\"1\"><img src=\"images/pix.gif\" height=\"1\"></td></tr>\n";

		 SR_Server[] allSerArr = getServers();
			if (allSerArr.length == 0){
				retStr = retStr + "<td colspan=\"8\" class='ListText'>No games found</td></tr>\n";
			}else{
				int count = 0;
				for (int i = 0; i < allSerArr.length; i++) {
					
					String RowName = i + "GameListRowShort";
					
					SR_Server aServer = allSerArr[i];
					if (aServer.isPlayerParticipating(aUser)){
						Player tmpPlayer = aServer.getPlayer(aUser.getLogin(),aUser.getPassword());
						String nextUpdate = "None";
						UpdateRunner ur = aServer.getUpdateRunner();
						if (ur != null){
							nextUpdate = ur.getNextUpdateShort();
						}
						String iconName = null;
						boolean addGame = false;
						boolean showJoin = true;
						if (!aServer.getLastUpdateComplete()){
							iconName = "error";
							addGame = true;
							showJoin = false;
						}else
						if (aServer.getGalaxy().gameEnded){
							if (tmpPlayer.isDefeated()){
								iconName = "defeat";
								addGame = true;
							}else{
								iconName = "victory";
								addGame = true;
							}
						}else
						if (tmpPlayer.isDefeated()){
							iconName = "defeat";
							addGame = true;
						}else
						if (tmpPlayer.isFinishedThisTurn()){
							iconName = "check";
							addGame = true;
						}else
						if (tmpPlayer.getUpdatedThisTurn()){
							iconName = "saved";
							addGame = true;
						}else
						if (aServer.getTurn() > 0){
							iconName = "cross";
							addGame = true;
						}
						if (addGame){
//							GameWorld gw = aServer.getGalaxy().getGameWorld();
							
							String tempName = aServer.getGameName();
							if (tempName.length() >10)
								{
								tempName = tempName.substring(0,10);
								tempName += "...";
								}
							retStr = retStr + "<tr class='ListTextRow' style='height:21px' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',6,1);\" onMouseOut=\"TranparentRow('" + RowName + "',6,0);\" onclick=\"location.href='Master.jsp?action=current_game&port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'\">" +
									"<td id='" + RowName + "1' class='ListText'></td>" +
									"<td id='" + RowName + "2' valign='middle' class='ListText'><div class='SolidText'><img src=\"images/" + iconName + ".gif\" vspace=\"0\" hspace=\"0\"  border=\"0\"></div></td>" +
									"<td id='" + RowName + "3' class='ListText' valign='middle'><div class='SolidText' nowrap>" + tempName + "</div></td>" +
									"<td id='" + RowName + "4' class='ListText'><div class='SolidText'>" + aServer.getTurn() + "</div></td>" +
									"<td id='" + RowName + "5' class='ListText'><div class='SolidText'>" + nextUpdate + "</div></td>" +
									//"<td id='" + RowName + "6' class='ListText'><div class='SolidText'><a href=''>Details</a></div></td>" +
									"<td id='" + RowName + "6' class='ListText'><div class='SolidText'>&nbsp;";
							retStr = retStr + "</div></td></tr>\n";
							count++;
						}
					}
				}
				if (count == 0){
					retStr = retStr + "<td width='20' class='ListText'></td><td colspan=\"10\" class='ListText'><div class='SolidText' style='padding-bottom:10px;'><b>You are not currently participating in any games</b></div></td></tr>\n";
				}
			}
			retStr = retStr + "</table>";
			return retStr;
		}

	
	
	

	public String getCurrentOpenGamesListNO(User aUser){

		String retStr = "<table class='ListTable' cellspacing='0' cellpadding='0' width='716'><tr height=1 class='ListLine'><td colspan=11></td></tr>";
			   retStr = retStr + "<tr class='ListheaderRow' height='16'><td class='ListHeader'></td><td class='ListHeader'></td><td class='ListHeader'><div class='SolidText' nowrap>Game Name&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>GameWorld&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Map&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>#Players&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Status&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Update&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Started by&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'></td><td class='ListHeader'></td></tr>\n";
		
		
		retStr = retStr + "<tr><td colspan=\"11\" bgcolor=\"#003902\" height=\"1\"><img src=\"images/pix.gif\" height=\"1\"></td></tr>\n";
		SR_Server[] allSerArr = getServers();
		if (allSerArr.length == 0){
			retStr = retStr + "<td></td><td colspan=\"10\" class='ListText'><div class='SolidText'>No games found</div></td></tr>\n";
		}else{
			int count = 0;
//			int countAlreadyParticipating = 0;
			
			String joinLink = "";
			
			for (int i = 0; i < allSerArr.length; i++) {
			String RowName = i + "CurrentListRow";
				SR_Server aServer = allSerArr[i];
				GameWorld gw = aServer.getGalaxy().getGameWorld();
				if (aServer.getTurn() == 0){
					count++;
					if (!aServer.isPlayerParticipating(aUser)){
						if (aServer.isPasswordProtected()){
							if (!aUser.isGuest()){
							joinLink = "<a href='Master.jsp?action=password_protected_game&port=" + aServer.getId() + "&autouser=true&returnto=games_list' target=\"mainFrame\">Join</a>";
							}
						}
						String iconString = "questionmark";
						if (aServer.isPasswordProtected()){
							iconString = "unlocked";
						}
						retStr = retStr + "<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',11,1);\" onMouseOut=\"TranparentRow('" + RowName + "',11,0);\"><td id='" + RowName + "1' width='3' class='ListText'></td><td id='" + RowName + "2' width='25' valign='middle' class='ListText'><div class='SolidText'><img src=\"images/" + iconString + ".gif\" width=\"20\" height=\"20\" vspace=\"0\" hspace=\"0\"  border=\"0\"></div></td><td id='" + RowName + "3' width='25' valign='middle' class='ListText'><div class='SolidText'>" + aServer.getGameName() + "</div></td><td id='" + RowName + "4' width='25' valign='middle' class='ListText'><div class='SolidText'>" + gw.getFileName() + "</div></td><td id='" + RowName + "5' width='25' valign='middle' class='ListText'><div class='SolidText'>" + aServer.getMapFileName() + "</div></td><td id='" + RowName + "6' width='25' valign='middle' class='ListText'><div class='SolidText'>" + aServer.getGalaxy().getNrPlayers() + "/" + aServer.getGalaxy().getNrStartPlanets() + "</div></td><td id='" + RowName + "7' width='25' valign='middle' class='ListText'><div class='SolidText'>" + aServer.getStatus() + "</div></td><td id='" + RowName + "8' width='25' valign='middle' class='ListText'><div class='SolidText'>" + UpdateRunner.getShortDescription(aServer.getGalaxy().getTime()) + "</div></td><td id='" + RowName + "9' width='25' valign='middle' class='ListText' nowrap><div class='SolidText' nowrap>" + aServer.getStartedByPlayerName() + "</div></td><td id='" + RowName + "10' width='25' valign='middle' class='ListText'><div class='SolidText'><a href='Master.jsp?action=current_game&port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'>Details</a></div></td><td id='" + RowName + "11' width='25' valign='middle' class='ListText'><div class='SolidText'>&nbsp;&nbsp;" + joinLink + "</td></tr>\n";
					}else{
						String iconString = "exclamationmark";
						if (aServer.isPasswordProtected()){
							iconString = "locked";
						}
						retStr = retStr + "<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',11,1);\" onMouseOut=\"TranparentRow('" + RowName + "',11,0);\"><td id='" + RowName + "1' width='3' class='ListText'></td><td id='" + RowName + "2' width='25' valign='middle' class='ListText'><div class='SolidText'><img src=\"images/" + iconString + ".gif\" width=\"20\" height=\"20\" vspace=\"0\" hspace=\"0\"  border=\"0\"></div></td><td id='" + RowName + "3' width='25' valign='middle' class='ListText'><div class='SolidText'>" + aServer.getGameName() + "</div></td><td id='" + RowName + "4' width='25' valign='middle' class='ListText'><div class='SolidText'>" + gw.getFileName() + "</div></td><td id='" + RowName + "5' width='25' valign='middle' class='ListText'><div class='SolidText'>" + aServer.getMapFileName() + "</div></td><td id='" + RowName + "6' width='25' valign='middle' class='ListText'><div class='SolidText'>" + aServer.getGalaxy().getNrPlayers() + "/" + aServer.getGalaxy().getNrStartPlanets() + "</div></td><td id='" + RowName + "7' width='25' valign='middle' class='ListText'><div class='SolidText'>" + aServer.getStatus() + "</div></td><td id='" + RowName + "8' width='25' valign='middle' class='ListText'><div class='SolidText'>" + UpdateRunner.getShortDescription(aServer.getGalaxy().getTime()) + "</div></td><td id='" + RowName + "9' width='25' valign='middle' class='ListText' nowrap><div class='SolidText' nowrap>" + aServer.getStartedByPlayerName() + "</div></td><td id='" + RowName + "10' width='25' valign='middle' class='ListText'><div class='SolidText'><a href='Master.jsp?action=current_game&port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'>Details</a></div></td><td id='" + RowName + "11' width='25' valign='middle' class='ListText'><div class='SolidText'>&nbsp;</td></tr>\n";
					}
				}
			}
			if (count == 0){
//				if (countAlreadyParticipating == 0){
					retStr = retStr + "<td></td><td colspan=\"10\" class='ListText'><div class='SolidText'>There are no games currently starting up</div></td></tr>\n";
//				}else{
//					retStr = retStr + "<td></td><td colspan=\"10\">There are no games currently starting up (that you have not already joined...)</td></tr>\n";
//				}
			}
		}
		retStr = retStr + "</table>";
		return retStr;
		
	}
		
		
		public String getCurrentGamesListNO(User aUser){
		String retStr = "<table border='0' width='714' cellspacing='0' cellpadding='0' class='MenuMain'><tr height=1 class='ListLine'><td colspan='7'></td></tr>";
		retStr = retStr + "<tr class='ListheaderRow' height='16'><td class='ListHeader'><div class='SolidText' style='padding-left:10px;'>Game Name&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Map Name&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Current Turn&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Status&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'><div class='SolidText'>Started by&nbsp;&nbsp;&nbsp;</div></td><td class='ListHeader'></td><td class='ListHeader'></td></tr>\n";
		retStr = retStr + "<tr><td colspan=\"7\" bgcolor=\"#003902\" height=\"1\"><img src=\"images/pix.gif\" height=\"1\"></td></tr>\n";
		SR_Server[] allSerArr = getServers();
		if (allSerArr.length == 0){
			retStr = retStr + "<td class='ListText' WIDTH='10'></td><td colspan=\"6\" class='ListText'>No games found</td></tr>\n";
		}else{
			for (int i = 0; i < allSerArr.length; i++) {
			
			String RowName = i + "AllGameListRow";
			
				SR_Server aServer = allSerArr[i];
				retStr = retStr + "<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',7,1);\" onMouseOut=\"TranparentRow('" + RowName + "',7,0);\"  onclick=\"location.href='Master.jsp?action=current_game&port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'\"><td id='" + RowName + "1' class='ListText'><div class='SolidText' style='padding-left:10px;height:15px;'>" + aServer.getGameName() + "</div></td><td id='" + RowName + "2' class='ListText'><div class='SolidText'>" + aServer.getMapFileName() + "</div></td><td id='" + RowName + "3' class='ListText'><div class='SolidText'>" + aServer.getTurn() + "</div></td><td id='" + RowName + "4' class='ListText'><div class='SolidText'>" + aServer.getStatus() + "</div></td><td id='" + RowName + "5' class='ListText'><div class='SolidText'>" + aServer.getStartedByPlayerName() + " (" + aServer.getStartedByPlayer() + ")</div></td><td id='" + RowName + "6' class='ListText'><div class='SolidText'></div></td><td id='" + RowName + "7' class='ListText'><div class='SolidText'>";
				retStr = retStr + "</td></tr>\n";
			}
		}
		retStr = retStr + "</table>";
		return retStr;
	}
			
		
	public String getCurrentOpenGamesList(User aUser){
		
	String retStr = "<table>";
	retStr = retStr + "<tr><td>Game Name&nbsp;&nbsp;&nbsp;</td><td>GameWorld&nbsp;&nbsp;&nbsp;</td><td>Map Name&nbsp;&nbsp;&nbsp;</td><td>#Players&nbsp;&nbsp;&nbsp;</td><td>Game State&nbsp;&nbsp;&nbsp;</td><td>Auto updates&nbsp;&nbsp;&nbsp;</td><td>Started by&nbsp;&nbsp;&nbsp;</td><td></td><td></td></tr>\n";
	retStr = retStr + "<tr><td colspan=\"9\" bgcolor=\"#FFBF00\" height=\"1\"><img src=\"images/pix.gif\" height=\"1\"></td></tr>\n";
	SR_Server[] allSerArr = getServers();
	if (allSerArr.length == 0){
		retStr = retStr + "<td colspan=\"7\">No games found</td></tr>\n";
	}else{
		int count = 0;
//		int countAlreadyParticipating = 0;
		for (int i = 0; i < allSerArr.length; i++) {
			SR_Server aServer = allSerArr[i];
			GameWorld gw = aServer.getGalaxy().getGameWorld();
			if (aServer.getTurn() == 0){
				count++;
				if (!aServer.isPlayerParticipating(aUser)){
					String joinLink = "";
					if (aServer.isPasswordProtected()){
						joinLink = "<a href='password_protected_game.jsp?port=" + aServer.getId() + "&autouser=true&returnto=startpage.jsp' target=\"mainFrame\">Join</a>";
					}
					String iconString = "questionmark";
					if (aServer.isPasswordProtected()){
						iconString = "unlocked";
					}
					retStr = retStr + "<tr valign=\"bottom\"><td><img src=\"images/" + iconString + ".gif\" width=\"20\" height=\"20\" vspace=\"0\" hspace=\"0\"  border=\"0\">" + aServer.getGameName() + "</td><td>" + gw.getFileName() + "</td><td>" + aServer.getMapFileName() + "</td><td>" + aServer.getGalaxy().getNrPlayers() + "/" + aServer.getGalaxy().getNrStartPlanets() + "</td><td>" + aServer.getStatus() + "</td><td>" + UpdateRunner.getShortDescription(aServer.getGalaxy().getTime()) + "</td><td>" + aServer.getStartedByPlayerName() + "</td><td><a href='Master.jsp?action=current_game&port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'>Details</a></td><td>&nbsp;&nbsp;" + joinLink + "</td></tr>\n";
				}else{
//					countAlreadyParticipating++;
					String iconString = "exclamationmark";
					if (aServer.isPasswordProtected()){
						iconString = "locked";
					}
					retStr = retStr + "<tr valign=\"bottom\"><td><img src=\"images/" + iconString + ".gif\" width=\"20\" height=\"20\" vspace=\"0\" hspace=\"0\"  border=\"0\">" + aServer.getGameName() + "</td><td>" + gw.getFileName() + "</td><td>" + aServer.getMapFileName() + "</td><td>" + aServer.getGalaxy().getNrPlayers() + "/" + aServer.getGalaxy().getNrStartPlanets() + "</td><td>" + aServer.getStatus() + "</td><td>" + UpdateRunner.getShortDescription(aServer.getGalaxy().getTime()) + "</td><td>" + aServer.getStartedByPlayerName() + "</td><td><a href='Master.jsp?action=current_game.jsp?port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'>Details</a></td><td>&nbsp;</td></tr>\n";
				}
			}
		}
		if (count == 0){
//			if (countAlreadyParticipating == 0){
				retStr = retStr + "<td colspan=\"6\">There are no games currently starting up</td></tr>\n";
//			}else{
//				retStr = retStr + "<td colspan=\"6\">There are no games currently starting up (that you have not already joined...)</td></tr>\n";
//			}
		}
	}
	retStr = retStr + "</table>";
	return retStr;
}

	
	public String getCurrentPlayingGamesListNoShort()
	{
		String retStr = "<table>";
		retStr = retStr + "<tr><td>Game Name&nbsp;&nbsp;&nbsp;</td><td> Turn&nbsp;&nbsp;&nbsp;</td><td>Next update&nbsp;&nbsp;&nbsp;</td><td>Started by&nbsp;&nbsp;&nbsp;</td><td></td><td></td></tr>\n";
		retStr = retStr + "<tr><td colspan=\"10\" bgcolor=\"#FFBF00\" height=\"1\"><img src=\"images/pix.gif\" height=\"1\"></td></tr>\n";
		SR_Server[] allSerArr = getServers();
		if (allSerArr.length == 0)
		{
			retStr = retStr + "<td colspan=\"8\">No games found</td></tr>\n";
		}
		else
		{
			int count = 0;
			String iconName = null;
			boolean addGame = false;
			boolean showJoin = false;
			for (int i = 0; i < allSerArr.length; i++) 
			{
					SR_Server aServer = allSerArr[i];	
					
					addGame = false;
					showJoin = false;
					
					String nextUpdate = "None";
					UpdateRunner ur = aServer.getUpdateRunner();
				
					if (ur != null)
					{
						nextUpdate = ur.getNextUpdateShort();
					}
					
					if (!aServer.getLastUpdateComplete())
					{
						iconName = "error";
						addGame = true;
						showJoin = false;
					}
					else
					{
						if (aServer.getGalaxy().gameEnded)
						{
							iconName = "gameEnded";
							addGame = true;
						}
						else
						{
							iconName = "ongoingGame";
							addGame = true;
						}
					}
					if (addGame)
					{
						GameWorld gw = aServer.getGalaxy().getGameWorld();
						retStr = retStr + "<tr valign=\"bottom\"><td><img src=\"images/" + iconName + ".gif\" width=\"20\" height=\"20\" vspace=\"0\" hspace=\"0\"  border=\"0\">" + aServer.getGameName() + "</td><td>" + gw.getFileName() + "</td><td>" + aServer.getMapFileName() + "</td><td>" + aServer.getGalaxy().getNrActivePlayers() + "/" + aServer.getGalaxy().getNrPlayers() + "</td><td>" + aServer.getStatus() + "</td><td>" + aServer.getTurn() + "</td><td>" + nextUpdate + "</td><td>" + aServer.getStartedByPlayerName() + "</td><td><a href='current_game.jsp?port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'>Details</a></td><td>&nbsp;";
						retStr = retStr + "</td></tr>\n";
						count++;
					}
				}
			
	}
		retStr = retStr + "</table>";
		return retStr;
	}

	
	
	
	
	public String getCurrentPlayingGamesListNoUser()
	{
		String retStr = "<table>";
		retStr = retStr + "<tr><td>Game Name&nbsp;&nbsp;&nbsp;</td><td>GameWorld&nbsp;&nbsp;&nbsp;</td><td>Map Name&nbsp;&nbsp;&nbsp;</td><td>#Players&nbsp;&nbsp;&nbsp;</td><td>Game State&nbsp;&nbsp;&nbsp;</td><td>Current Turn&nbsp;&nbsp;&nbsp;</td><td>Next update&nbsp;&nbsp;&nbsp;</td><td>Started by&nbsp;&nbsp;&nbsp;</td><td></td><td></td></tr>\n";
		retStr = retStr + "<tr><td colspan=\"10\" bgcolor=\"#FFBF00\" height=\"1\"><img src=\"images/pix.gif\" height=\"1\"></td></tr>\n";
		SR_Server[] allSerArr = getServers();
		if (allSerArr.length == 0)
		{
			retStr = retStr + "<td colspan=\"8\">No games found</td></tr>\n";
		}
		else
		{
			int count = 0;
			String iconName = null;
			boolean addGame = false;
			boolean showJoin = false;
			for (int i = 0; i < allSerArr.length; i++) 
			{
					SR_Server aServer = allSerArr[i];	
					
					addGame = false;
					showJoin = false;
					
					String nextUpdate = "None";
					UpdateRunner ur = aServer.getUpdateRunner();
				
					if (ur != null)
					{
						nextUpdate = ur.getNextUpdateShort();
					}
					
					if (!aServer.getLastUpdateComplete())
					{
						iconName = "error";
						addGame = true;
						showJoin = false;
					}
					else
					{
						if (aServer.getGalaxy().gameEnded)
						{
							iconName = "gameEnded";
							addGame = true;
						}
						else
						{
							iconName = "ongoingGame";
							addGame = true;
						}
					}
					if (addGame)
					{
						GameWorld gw = aServer.getGalaxy().getGameWorld();
						retStr = retStr + "<tr valign=\"bottom\"><td><img src=\"images/" + iconName + ".gif\" width=\"20\" height=\"20\" vspace=\"0\" hspace=\"0\"  border=\"0\">" + aServer.getGameName() + "</td><td>" + gw.getFileName() + "</td><td>" + aServer.getMapFileName() + "</td><td>" + aServer.getGalaxy().getNrActivePlayers() + "/" + aServer.getGalaxy().getNrPlayers() + "</td><td>" + aServer.getStatus() + "</td><td>" + aServer.getTurn() + "</td><td>" + nextUpdate + "</td><td>" + aServer.getStartedByPlayerName() + "</td><td><a href='current_game.jsp?port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'>Details</a></td><td>&nbsp;";
						retStr = retStr + "</td></tr>\n";
						count++;
					}
				}
			
	}
		retStr = retStr + "</table>";
		return retStr;
	}

	/**
	 * If aUser has at least one turn to perform in a game (inc. saved)
	 * @param aUser
	 * @return
	 */
	public boolean getPlayerHasTurnToPerform(User aUser){
		boolean hasTurn = false;
		SR_Server[] allSerArr = getServers();
		if (allSerArr.length > 0){
			for (int i = 0; i < allSerArr.length; i++) {
				SR_Server aServer = allSerArr[i];
				if (aServer.isPlayerParticipating(aUser)){
					Player tmpPlayer = aServer.getPlayer(aUser.getLogin(),aUser.getPassword());
					if (!aServer.getLastUpdateComplete()){
						// do nothing
					}else
					if (aServer.getGalaxy().gameEnded){
						// do nothing
					}else
					if (tmpPlayer.isDefeated()){
						// do nothing
					}else
					if (tmpPlayer.isFinishedThisTurn()){
						// do nothing
					}else
					if (tmpPlayer.getUpdatedThisTurn()){
						hasTurn = true;
					}else
					if (aServer.getTurn() > 0){
						hasTurn = true;
					}
				}
			}
		}
		return hasTurn;
	}

	public GameListData getGamesData(User aUser, ReturnGames returnGames){
		GameListData gameListData = new GameListData();
		SR_Server[] allSerArr = getServers();
		if (allSerArr.length > 0){
			for (int i = 0; i < allSerArr.length; i++) {
				SR_Server aServer = allSerArr[i];
				if (returnGames == ReturnGames.ALL){
					gameListData.addGame(getGameData(aServer,aUser));
				}else
				if (((returnGames == ReturnGames.OWN) | (returnGames == ReturnGames.OWN_AND_OPEN)) & aServer.isPlayerParticipating(aUser)){
					gameListData.addGame(getGameData(aServer,aUser));
				}else
				if (((returnGames == ReturnGames.OPEN) | (returnGames == ReturnGames.OWN_AND_OPEN)) & !aServer.isPlayerParticipating(aUser) & (aServer.getTurn() == 0)){
					gameListData.addGame(getGameData(aServer,aUser));
				}
			}
		}
		return gameListData;
	}

	private GameData getGameData(SR_Server aServer, User aUser){
		GameData gameData = new GameData();
		gameData.setGameId((int)aServer.getId());
		Logger.finer("aServer.getGameId(): " + aServer.getId());
		gameData.setGameName(aServer.getGameName());
		Logger.finer("Notifier returning game: " + aServer.getGameName());
		gameData.setGameWorldName(aServer.getGalaxy().getGameWorld().getFullName());
		gameData.setMapName(aServer.getGalaxy().getMapNameFull());
		gameData.setMaxTurn(aServer.getEndTurn());
		String nextUpdate = "None";
		UpdateRunner ur = aServer.getUpdateRunner();
		if (ur != null){
			nextUpdate = ur.getNextUpdateShort();
			gameData.setNextUpdateCalendar(ur.getNextUpdateCalendar());
		}
		gameData.setNextUpdate(nextUpdate);
		gameData.setNrPlayers(aServer.getGalaxy().getNrActivePlayers());
		Map map = MapHandler.getMap(aServer.getMapFileName());
		gameData.setMapMaxPlayers(map.getMaxNrStartPlanets());
		if (aServer.getTurn() > 0){
			gameData.setNrPlayersMax(aServer.getGalaxy().getNrPlayers());
		}else{
			gameData.setNrPlayersMax(aServer.getGalaxy().getNrStartPlanets());
		}
		gameData.setStatus(aServer.getStatus());
		if (aServer.getGalaxy().isGameOver()){
			Player player = aServer.getPlayer(aUser.getLogin(), aUser.getPassword());
			if (player.isDefeated()){
				gameData.setGameOverStatus("defeated");
			}else{
				if (player.isWin()){
					gameData.setGameOverStatus("win");
				}else{ // assumes survival or equivalent end game status
					gameData.setGameOverStatus("survival");
				}
			}
		}
		gameData.setTurn(aServer.getTurn());
		gameData.setUpdatesWeek(UpdateRunner.getShortDescription(aServer.getGalaxy().getTime()));
		gameData.setPlayers(aServer.getGalaxy().getPlayers());
		gameData.setPassword(aServer.getGalaxy().getPassword());
		return gameData;
	}
	
	/**
	 * Check if a player has any unsaved games
	 * @param aUser
	 * @return
	 */
	public boolean getPlayerHasSavedOnlyToPerform(User aUser){
		boolean savedOnly = true; // is true until an unsaved game is found
		SR_Server[] allSerArr = getServers();
		if (allSerArr.length > 0){
			for (int i = 0; i < allSerArr.length; i++) {
				SR_Server aServer = allSerArr[i];
				if (aServer.isPlayerParticipating(aUser)){
					Player tmpPlayer = aServer.getPlayer(aUser.getLogin(),aUser.getPassword());
					if (!aServer.getLastUpdateComplete()){
						// do nothing
					}else
					if (aServer.getGalaxy().gameEnded){
						// do nothing
					}else
					if (tmpPlayer.isDefeated()){
						// do nothing
					}else
					if (tmpPlayer.isFinishedThisTurn()){
						// do nothing
					}else
					if (tmpPlayer.getUpdatedThisTurn()){
						// do nothing
					}else
					if (aServer.getTurn() > 0){
						savedOnly = false;
					}
				}
			}
		}
		return savedOnly;
	}

	public String getCurrentPlayingGamesList(User aUser){
		String retStr = "<table>";
		retStr = retStr + "<tr><td>Game Name&nbsp;&nbsp;&nbsp;</td><td>GameWorld&nbsp;&nbsp;&nbsp;</td><td>Map Name&nbsp;&nbsp;&nbsp;</td><td>#Players&nbsp;&nbsp;&nbsp;</td><td>Game State&nbsp;&nbsp;&nbsp;</td><td>Current Turn&nbsp;&nbsp;&nbsp;</td><td>Next update&nbsp;&nbsp;&nbsp;</td><td>Started by&nbsp;&nbsp;&nbsp;</td><td></td><td></td></tr>\n";
		retStr = retStr + "<tr><td colspan=\"10\" bgcolor=\"#FFBF00\" height=\"1\"><img src=\"images/pix.gif\" height=\"1\"></td></tr>\n";
		SR_Server[] allSerArr = getServers();
		if (allSerArr.length == 0){
			retStr = retStr + "<td colspan=\"8\">No games found</td></tr>\n";
		}else{
			int count = 0;
			for (int i = 0; i < allSerArr.length; i++) {
				SR_Server aServer = allSerArr[i];
				if (aServer.isPlayerParticipating(aUser)){
					Player tmpPlayer = aServer.getPlayer(aUser.getLogin(),aUser.getPassword());
					String nextUpdate = "None";
					UpdateRunner ur = aServer.getUpdateRunner();
					if (ur != null){
						nextUpdate = ur.getNextUpdateShort();
					}
					String iconName = null;
					boolean addGame = false;
					boolean showJoin = true;
					if (!aServer.getLastUpdateComplete()){
						iconName = "error";
						addGame = true;
						showJoin = false;
					}else
					if (aServer.getGalaxy().gameEnded){
						if (tmpPlayer.isDefeated()){
							iconName = "defeat";
							addGame = true;
						}else{
							iconName = "victory";
							addGame = true;
						}
					}else
					if (tmpPlayer.isDefeated()){
						iconName = "defeat";
						addGame = true;
					}else
					if (tmpPlayer.isFinishedThisTurn()){
						iconName = "check";
						addGame = true;
					}else
					if (tmpPlayer.getUpdatedThisTurn()){
						iconName = "saved";
						addGame = true;
					}else
					if (aServer.getTurn() > 0){
						iconName = "cross";
						addGame = true;
					}
					if (addGame){
						GameWorld gw = aServer.getGalaxy().getGameWorld();
						retStr = retStr + "<tr valign=\"bottom\"><td><img src=\"images/" + iconName + ".gif\" width=\"20\" height=\"20\" vspace=\"0\" hspace=\"0\"  border=\"0\">" + aServer.getGameName() + "</td><td>" + gw.getFileName() + "</td><td>" + aServer.getMapFileName() + "</td><td>" + aServer.getGalaxy().getNrActivePlayers() + "/" + aServer.getGalaxy().getNrPlayers() + "</td><td>" + aServer.getStatus() + "</td><td>" + aServer.getTurn() + "</td><td>" + nextUpdate + "</td><td>" + aServer.getStartedByPlayerName() + "</td><td><a href='current_game.jsp?port=" + aServer.getId() + "&gamename=" + aServer.getGameName() + "&autouser=true&returnto=startpage.jsp'>Details</a></td><td>&nbsp;";
						retStr = retStr + "</td></tr>\n";
						count++;
					}
				}
			}
			if (count == 0){
				retStr = retStr + "<td colspan=\"6\">You are not currently participating in any games</td></tr>\n";
			}
		}
		retStr = retStr + "</table>";
		return retStr;
	}
	
	public String getServersList(){
		String retStr = "";
		SR_Server[] allSerArr = getServers();
		for (int i = 0; i < allSerArr.length; i++) {
			SR_Server aServer = allSerArr[i];
			retStr = aServer.getServerHTMLRow() + "<BR>\n";
		}
		return retStr;
	}
	
	public SR_Server[] getServers(){
		SR_Server[] allServersArray = new SR_Server[allServers.size()];
		int i = 0;
		for (SR_Server aServer : allServers) {
			allServersArray[i] = aServer;
			i++;
		}
		return allServersArray;
	}
	
	public int getGamesNrStarting(){
		int nr = 0;
		for (SR_Server aServer : allServers) {
			if ((aServer.getTurn() == 0) & (!aServer.getGalaxy().isGameOver())){
				nr++;
			}
		}
		return nr;
	}

	public int getGamesNrRunning(){
		int nr = 0;
		for (SR_Server aServer : allServers) {
			if ((aServer.getTurn() > 0) & (!aServer.getGalaxy().isGameOver())){
				nr++;
			}
		}
		return nr;
	}
	
	public String StartNewGame(GameParameters gameParameters){
		
		return startNewGame(gameParameters.getGameWorldName(), gameParameters.getGameName(), gameParameters.getMapName(), 
				gameParameters.getSteps(), gameParameters.getAutoBalance(), 
				gameParameters.getTime(), gameParameters.getEmailPlayers(), gameParameters.getMaxNrPlayers(), gameParameters.getUser(), 
				gameParameters.getGamePassword(), gameParameters.getGroupFaction(), gameParameters.getSelectableFactionNames(), 
				gameParameters.getRandomFaction(), gameParameters.getDiplomacy(), gameParameters.getRanked(),
				gameParameters.getSingleVictory(), gameParameters.getFactionVictory(), gameParameters.getEndTurn(), 
				gameParameters.getNumberOfStartPlanet(), gameParameters.getStatisticGameType());
	}

	/**
	 * Create new game, initially it always update automatically
	 */
	public String startNewGame(String gameWorldFileName, String gameName, String mapName, String stepsString, String autoBalanceString, String timeString, String emailPlayers, String maxNrPlayers, String userLogin, String gamePassword, String groupFaction, List<String> selectableFactionNames, String randomFactionString, String diplomacy, String ranked, int singleVictory, int factionVictory, int endTurn, int numberOfStartPlanet, StatisticGameType statisticGameType){
		Logger.fine(gameWorldFileName);
		Logger.fine(gameName);
		Logger.fine(mapName);
		Logger.fine(stepsString);
		Logger.fine(autoBalanceString);
		Logger.fine(timeString);
		Logger.fine(emailPlayers);
		Logger.fine(maxNrPlayers);
		Logger.fine(userLogin);
		Logger.fine(gamePassword);
		Logger.fine(groupFaction);
		Logger.fine(randomFactionString);
		Logger.fine(diplomacy);
		Logger.fine(ranked);
		String retVal = "";
		String errorMessage = checkGameName(gameName);
		if (errorMessage.equals("ok")){
			// set diplomacy type
			DiplomacyGameType diplomacyType = DiplomacyGameType.getType(diplomacy);
			Logger.finer("diplomacyType set to " + diplomacyType.toString());
			retVal = diplomacyType.checkMinimumNumberFactions(selectableFactionNames.size());
//			if ((selectableFactionNames != null) && (selectableFactionNames.size() < 2)){
//				retVal = "At least 2 factions must be selectable";
			if (retVal.equals("")){
				Logger.finer("gameWorldFileName: " + gameWorldFileName);
				GameWorld gw = GameWorldHandler.getGameWorld(gameWorldFileName);
				Logger.fine("gameWorld: " + gw);
				int tmpId = getFreeId();
				int steps = Integer.parseInt(stepsString);
				int maxPlayers = Integer.parseInt(maxNrPlayers);
				long time = Long.parseLong(timeString);
				boolean autoBalance = false;
				boolean bRanked = false;
				Logger.finer("autoBalanceString: " + autoBalanceString);
				if ((autoBalanceString != null) && (autoBalanceString.equals("yes"))){
					autoBalance = true;
					Logger.finer("autoBalance set to true");
				}
				
				if ((ranked.equals("yes"))){
					bRanked = true;
					Logger.finer("ranked set to true");
				}
				
				boolean groupFactionValue = false;
				if ((groupFaction != null) && (groupFaction.equals("yes"))){
					groupFactionValue = true;
				}
				// set random faction
				boolean randomFaction = false;
				if ((randomFactionString != null) && (randomFactionString.equals("yes"))){
					randomFaction = true;
					Logger.finer("autoBalance set to true");
				}
				// create game instance
				SR_Server newServer = new SR_Server("create",gw,gameName,mapName,tmpId,steps,autoBalance,time,maxPlayers,userLogin,gamePassword,groupFactionValue,randomFaction,selectableFactionNames,this, diplomacyType, bRanked, singleVictory, factionVictory, endTurn, numberOfStartPlanet, statisticGameType);
				allServers.add(newServer);
//				newServer.setStartedByPlayer(userLogin);
/*
				// set selectable factions
				Galaxy g = newServer.getGalaxy();
				System.out.println("selectableFactionNames: " + selectableFactionNames);
				if (selectableFactionNames == null){
					// all factions should be selectable
					g.setAllFactionsSelectable();
				}else{
					g.setSelectableFactionNames(selectableFactionNames);
				}
*/
/*
				// set random faction
				boolean randomFaction = false;
				if ((randomFactionString != null) && (randomFactionString.equals("yes"))){
					randomFaction = true;
					LoggingHandler.finer("autoBalance set to true");
				}
				g.setRandomFaction(randomFaction);
*/
				// send e-mails about new game
				if ((emailPlayers != null) && (emailPlayers.equals("yes"))){
					MailHandler.sendNewGameMessage(newServer);
				}
				// test
				Logger.finer("Selectable factions: ");
				List<Faction> allFactions = gw.getFactions();
				for (Faction faction : allFactions) {
					Logger.finer("faction: " + faction);
				}
				retVal = "Game started";
			}
		}else{
			retVal = errorMessage;
		}
		return retVal;
	}
	
	private String checkGameName(String aGameName){
		String message = "ok";
		if (aGameName == null){
			message = "Game name is required";
		}else
		if (aGameName.equals("")){
			message = "Game name is required";
		}else
		if (aGameName.length() > 20){
			message = "Game name can not be longar than 20 characters";
		}else{
			boolean nameOk = checkString(aGameName);
			if (!nameOk){
				message = "Game name can only contain characters, numbers, blanks and minus";
			}else{
				SR_Server aServer = findGame(aGameName);
				if (aServer != null){
					message = "Game name \"" + aGameName + "\" is already in use. Please try again with another game name.";
				}
			}
		}
		System.out.println("m: " + message);
		return message;
	}
	
	private boolean checkString(String aGameName){
		boolean ok = true;
    	for (int i = 0; i < aGameName.length(); i++) {
			char c = aGameName.charAt(i);
			if (!Character.isLetter(c) & !Character.isWhitespace(c) & !Character.isDigit(c) & !(String.valueOf(c).equals("-"))){
				ok = false;
			}
		}
		return ok;
	}
	
	public String getNameOptions(){
		String retStr = "";
		SR_Server[] allSerArr = getServers();
		for (int i = 0; i < allSerArr.length; i++) {
			SR_Server aServer = allSerArr[i];
			String gameOver = "";
			if (aServer.getGalaxy().gameEnded){
				gameOver = " (game over)";
			}
			retStr = retStr + "<option value=\"" + aServer.getGameName() + "\">" + aServer.getGameName() + gameOver + "</option>\n";
		}
		return retStr;
	}
	
	/**
	 * Find a free port to use for new game
	 * @return
	 */
	private int getFreeId(){
		int idIndex = startId;
		int found = -1;
		if (allServers.size() > 0){			
			while (found == -1){
				boolean alreadyTaken = false;
				for (SR_Server aServer : allServers) {
					int tmpId = aServer.getId();
					if (tmpId == idIndex){
						alreadyTaken = true;
					}
				}
				if (alreadyTaken){
					idIndex++;
				}else{
					found = idIndex;
				}
			}
		}else{
			found = idIndex;
		}
		return found;
	}
	
	public String getFirstFreeDefaultName(){
		String key = "autogamenamecounter";
		int counter = PHash.getCounter(key);
		counter++;
		String freeName = null;
		while (freeName == null){
			String tmpName = "Game-" + counter;
			SR_Server tmpGame = findGame(tmpName);
			if (tmpGame != null){
				counter++;
			}else{
				freeName = tmpName;
				PHash.setValue(key, counter);
			}
		}
		return freeName;
	}
	
	public int updateGame(String gameName, int nrTurns) throws Exception{
		Logger.fine("updateGame called: " + gameName + " " + nrTurns);
		int newTurn = -1;
		SR_Server foundGame = findGame(gameName);
		Logger.fine("Game found: " + foundGame);
		int oldTurn = foundGame.getTurn();
		for (int i = 0; i < nrTurns; i++){
			foundGame.updateGalaxy(true);
			foundGame.setHasAutoUpdated(true);
		}
		newTurn = foundGame.getTurn();
		Logger.fine("New turn: " + newTurn);
		if (oldTurn == 0){
			if (foundGame.getGalaxy().getTime() > 0){
				foundGame.startRunning();
			}
		}
		Logger.fine("running...");
		MailHandler.sendNewTurnMessage(foundGame);
		Logger.fine("updateGame finished");
		return newTurn;
	}

	public int rollbackGame(String gameName, int nrTurns) throws Exception{
		Logger.fine("rollbackGame called: " + gameName + " " + nrTurns);
		int newTurn = -1;
		SR_Server foundGame = findGame(gameName);
		Logger.fine("Game found: " + foundGame);
		int currentTurn = foundGame.getTurn();
		newTurn = currentTurn + nrTurns;
		if (newTurn < 1){
			newTurn = 1;
		}
		Logger.fine("New turn: " + newTurn);
		// load previous version of the galaxy
	    GalaxyLoader gl = new GalaxyLoader();
        Galaxy g = gl.loadGalaxy("previous" + File.separator + gameName + "_" + newTurn);
        Logger.info("Galaxy loaded. Turn is " + g.getTurn());
        // save previous version of galaxy as current version
        GalaxySaver gs = new GalaxySaver();
        gs.saveGalaxy(gameName,"saves",g);
        // update the SR_Server with the new current galaxy
        foundGame.setGalaxy(g);
		Logger.fine("updateGame finished");
		return newTurn;
	}

	public SR_Server findGame(String gameName){
		SR_Server found = null;
		int index = 0;
		while ((found == null) && (index < allServers.size())){
			SR_Server  tmp = (SR_Server)allServers.get(index);
			if (tmp.getGameName().equals(gameName)){
				found = tmp;
			}else{
				index++;
			}
		}
		return found;
	}
	
	public SR_Server findGame(int id){
		SR_Server foundGame = null;
		for (SR_Server aServer : allServers) {
			Logger.fine("aServer.getId(): " + aServer.getId() + " aServer.getGameName()" + aServer.getGameName());
			if (aServer.getId() == id){
				foundGame = aServer;
			}
		}
		return foundGame;
	}
	
	public void deleteGame(String gameName){
		System.out.println("deleteGame: " + gameName);
		// find game
		SR_Server srs = findGame(gameName);
		// kill game threads
		srs.stopThreads();
		// remove game from list
/*		for (Iterator iter = allServers.iterator(); iter.hasNext();) {
			SR_Server aServer = (SR_Server) iter.next();
			LoggingHandler.fine("aServer before: " + aServer.getName());
		}
*/		allServers.remove(srs);
/*		for (Iterator iter = allServers.iterator(); iter.hasNext();) {
			SR_Server aServer = (SR_Server) iter.next();
			LoggingHandler.fine("aServer before: " + aServer.getName());
		}
*/		// delete game file		
		String path = PropertiesHandler.getProperty("datapath") + "saves";
		String pathAndFile = path + File.separator + gameName + ".srg";
		Logger.fine("Path to savefile to remove: " + pathAndFile);
		File tmp = new File(pathAndFile);
		tmp.delete();
		
		path = PropertiesHandler.getProperty("datapath") + "messageDatabase";
		pathAndFile = path + File.separator + gameName + ".srg";
		Logger.fine("Path to savefile to remove: " + pathAndFile);
		tmp = new File(pathAndFile);
		tmp.delete();
	}
	
	/**
	 * Used for testing
	 * @param args
	 
	public static void main(String[] args){
		ServerHandler sh = new ServerHandler();
//		System.out.println(sh.getFreePort());
		sh.startNewGame("test","wigge3","3","yes","0","yes","2","pabod");
		sh.startNewGame("test2","wigge3","3","yes","0","yes","4","pabod");
		try {
			System.out.println(sh.updateGame("test",1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
*/
	
	//TODO varför måste password finns för spelaren i pågående spel? borde inte det tas bort så behövs inte detta göras?
	/**
	 * Called when a player has changed his password.
	 * Update all games so that the players new password will be used.
	 * @param login
	 * @param newPassword
	 */
	public void newPlayerPassword(String login, String newPassword){
		for (SR_Server aServer : allServers) {
			aServer.newPlayerPassword(login,newPassword);
		}
	}

	// called to find if a certain player already has a game started
	// returns null if no game was found, otherwise return the first game found
	public SR_Server playerHasGameStarted(User curUser){
		SR_Server found = null;
		int index = 0;
		while ((found == null) && (index < allServers.size())){
			SR_Server aGame = (SR_Server)allServers.get(index);
			if (aGame.getStartedByPlayer().equals(curUser.getLogin())){
				found = aGame;
			}else{
				index++;
			}
		}
		return found;
	}
	
	public boolean checkProtectedGamePassword(String password, String gameId){
		int gameIdNr = Integer.parseInt(gameId);
		SR_Server theGame = findGame(gameIdNr);
		boolean passwordOk = theGame.checkGamePassword(password);
		return passwordOk;
	}
	
	public void removeGame(SR_Server aGame){
		// remove from serverhandler
		allServers.remove(aGame);
		// remove file
    	String dataPath = PropertiesHandler.getProperty("datapath");
        String filePath = dataPath + "saves" + "/" + aGame.getGameName() + ".srg";
        Logger.info("Deleting file = too old, path: " + filePath);
        File gameFile = new File(filePath);
        gameFile.delete();
        
        filePath = dataPath + "messageDatabase" + "/" + aGame.getGameName() + ".srg";
        Logger.info("Deleting file = too old, path: " + filePath);
        gameFile = new File(filePath);
        gameFile.delete();
        
	}
}
