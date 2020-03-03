/*
 * Created on 2004-dec-28
 */
package sr.webb;

import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.Player;
import sr.server.SR_Server;
import sr.server.UpdateRunner;

/**
 * @author WMPABOD
 */
public class ServerStatus {

	public static String getServerStatus(int port){
		String msg = null;
		CheckServer cs = new CheckServer(port);
		if (!cs.isRunning()){
			msg = "Server is not online at the moment";
		}else
		if (cs.getTurn() == 0){
			msg = "New game is starting. Click the Client/Login link in the menu to log in and join the game.";
		}else{
			msg = "Server is online (current game turn number is " + cs.getTurn() + ").<br> Click <a href=\"current_game.jsp\">Game Status</a> to see more information about the game.";
		}
		return msg;
	}
	
	public static String getStartingText(Galaxy g){
		String retStr = "";
		if (g.getTurn() == 0){
			retStr = "New game is starting.<p>";
            String[] playerInfos = getPlayerStrings(g,false);
			int freeSlots = g.getNrStartPlanets() - playerInfos.length; 
			if (freeSlots > 0){
				retStr = retStr + " There are " + freeSlots + " player slots not taken (yet).<p>";
			}
		}
		return retStr;
	}

	public static String getUpdateText(SR_Server aServer){
		Logger.finer("getUpdateText: " + aServer.getGameName());
		String retStr = "";
		if (!aServer.getGalaxy().gameEnded){
			long time = aServer.getGalaxy().getTime();
			int turn = aServer.getGalaxy().getTurn();
			if (time > 0){
				Logger.finer("Time > 0: " + time + " aServer.getGalaxy().getTurn(): " + turn);
				retStr = retStr + UpdateRunner.getUpdateDescription((int)time) + "<br>";
				if (turn == 0){
					retStr = retStr + "Update scheduler has not started (yet).<br>";
				}else{
					UpdateRunner ur = aServer.getUpdateRunner();
					retStr = retStr + "Next scheduled update are: " + ur.getNextUpdate() + "<br>";
					retStr = retStr + "Game will also update immediately when all players are finished with their turn.<p>";
				}
			}else{
				retStr = retStr + "Game will update immediately when all players are finished with their turn.<p>";
			}
		}
		Logger.finer("retStr2: " + retStr);
		return retStr;
	}

	public static String getGameOverText(Galaxy g){
		String retStr = "";
		if (g.gameEnded){
			retStr = "Game has ended.<p>";
		}
		return retStr;
	}
	
    private static String[] getPlayerStrings(Galaxy g,boolean showUser){
    	String[] allPlayerStrings = new String[g.getNrStartPlanets()];
    	Logger.fine("getPlayerList: " + allPlayerStrings.length);
		Logger.finer("getPlayerList (boolean): " + showUser);
    	int longestName = g.getLongestGovenorName();
        for (int i = 0; i < g.getPlayers().size(); i++){
        	Player temp = (Player)g.getPlayers().get(i);
        	String color = temp.getFaction().getColorHexValue();
        	String aPlayerStr = "<font color=\"" + color + "\">" + temp.getGovenorName();
        	if (showUser){
            	aPlayerStr = aPlayerStr + " (" + temp.getName() + ")"; 
        	}
        	aPlayerStr = aPlayerStr + "</font><span></span>";
        	// pad with space
        	for (int j = temp.getGovenorName().length(); j < (longestName + 3); j++){
        		aPlayerStr = aPlayerStr + " ";
        	}
        	if (g.getTurn() > 0){
        		if (temp.isDefeated()){
        			aPlayerStr = aPlayerStr + "Defeated";
        		}else
            	if (g.gameEnded){
            		aPlayerStr = aPlayerStr + "Victory";
            	}else
        		if (temp.isFinishedThisTurn()){
        			aPlayerStr = aPlayerStr + "Finished";
        		}else
            	if (temp.getUpdatedThisTurn()){
            		aPlayerStr = aPlayerStr + "Saved";
            	}else{
        			aPlayerStr = aPlayerStr + "Not finished";
        		}
        	}else{
        		aPlayerStr = aPlayerStr + "Created";
        	}
        	allPlayerStrings[i] = aPlayerStr;
        }
        if (g.getPlayers().size() < g.getNrStartPlanets()){
        	for(int i = g.getPlayers().size(); i < g.getNrStartPlanets(); i++){
        		allPlayerStrings[i] = "Unnamed<span></span>Free slot";
        	}
        }
        return allPlayerStrings;
    }

    private static String[] getPlayerStringsNO(Galaxy g,boolean showUser){
    	String[] allPlayerStrings = new String[g.getNrStartPlanets()];
    	Logger.fine("getPlayerList: " + allPlayerStrings.length);
		Logger.finer("getPlayerList (boolean): " + showUser);
    	int longestName = g.getLongestGovenorName();
        for (int i = 0; i < g.getPlayers().size(); i++){
        	Player temp = (Player)g.getPlayers().get(i);
        	String color = temp.getFaction().getColorHexValue();
        	String aPlayerStr = "<font color=\"" + color + "\">" + temp.getGovenorName();
        	if (showUser){
            	aPlayerStr = aPlayerStr + " (" + temp.getName() + ")"; 
        	}
        	aPlayerStr = aPlayerStr + "</font><span></span>";
        	// pad with space
        	for (int j = temp.getGovenorName().length(); j < (longestName + 3); j++){
        		aPlayerStr = aPlayerStr + " ";
        	}
        	if (g.getTurn() > 0){
        		if (temp.isDefeated()){
        			aPlayerStr = aPlayerStr + "Defeated";
        		}else
            	if (g.gameEnded){
            		aPlayerStr = aPlayerStr + "Victory";
            	}else
        		if (temp.isFinishedThisTurn()){
        			aPlayerStr = aPlayerStr + "Finished";
        		}else
            	if (temp.getUpdatedThisTurn()){
            		aPlayerStr = aPlayerStr + "Saved";
            	}else{
        			aPlayerStr = aPlayerStr + "Not finished";
        		}
        	}else{
        		aPlayerStr = aPlayerStr + "Created";
        	}
        	allPlayerStrings[i] = aPlayerStr;
        }
        if (g.getPlayers().size() < g.getNrStartPlanets()){
        	for(int i = g.getPlayers().size(); i < g.getNrStartPlanets(); i++){
        		allPlayerStrings[i] = "Unnamed<span></span>Free slot";
        	}
        }
        return allPlayerStrings;
    }
    
    
	public static String getPlayerList(Galaxy g, String showUserStr){
		Logger.finer("getPlayerList: " + showUserStr);
		boolean showUser = showUserStr.equalsIgnoreCase("true");
		Logger.finer("getPlayerList: " + showUser);
		String retStr = "<h3>Players:</h3>";
		retStr = retStr + "<table>";
		String[] playerList = getPlayerStringsNO(g,!showUser);
		for (int i = 0; i < playerList.length; i++) {
			if ((g.getTurn() == 0) || !playerList[i].equals("Unnamed<span></span>Free slot")){
				retStr = retStr + "<tr>";
				int index = playerList[i].indexOf("<span></span>");
				retStr = retStr + "<td>" + playerList[i].substring(0,index) + "&nbsp;&nbsp;&nbsp;</td>";
				retStr = retStr + "<td>" + playerList[i].substring(index+13) + "</td>";
				retStr = retStr + "</tr>";
			}
		}
		retStr = retStr + "</table>";
		return retStr;
	}

	public static String getPlayerListNO(Galaxy g, String showUserStr){
		Logger.finer("getPlayerList: " + showUserStr);
		boolean showUser = showUserStr.equalsIgnoreCase("true");
		Logger.finer("getPlayerList: " + showUser);
		String retStr = "";
		retStr = retStr + "<table>";
		String[] playerList = getPlayerStrings(g,!showUser);
		for (int i = 0; i < playerList.length; i++) {
			if ((g.getTurn() == 0) || !playerList[i].equals("Unnamed<span></span>Free slot")){
				retStr = retStr + "<tr>";
				int index = playerList[i].indexOf("<span></span>");
				retStr = retStr + "<td>" + playerList[i].substring(0,index) + "&nbsp;&nbsp;&nbsp;</td>";
				retStr = retStr + "<td>" + playerList[i].substring(index+13) + "</td>";
				retStr = retStr + "</tr>";
			}
		}
		retStr = retStr + "</table>";
		return retStr;
	}

	
}
