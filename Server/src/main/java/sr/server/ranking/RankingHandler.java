/*
 * Created on 2005-feb-20
 */
package sr.server.ranking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import sr.general.logging.Logger;
import sr.server.properties.PropertiesHandler;

/**
 * @author wmpabod
 *
 * Handles ranking of players
 */
public class RankingHandler {
	private static Properties properties;
	private static final String DEFAULT_PROPERTIES_NAME = "ranking";
	private static List<RankedPlayer> allRankedPlayers;
	
	public static void addPlayerWin(int nrDefeatedOpp, String playerLogin, boolean soloWin){
		Logger.info("RankingHandler.addPlayerWin called: " + playerLogin + " " + soloWin + " " + nrDefeatedOpp);
		getList();
		RankedPlayer player = findPlayer(playerLogin);
		if (player == null){
			// player does not exist, create a new player
			player = addNewPlayer(playerLogin);
		}
		player.newWin(nrDefeatedOpp,soloWin);
		player.addToNrDefeatedPlayers(2); // survived and is on winning side
		// save changes
		saveUsers();
	}

	public static void addVassalWin(String playerLogin){
		Logger.info("RankingHandler.addVassalWin called: " + playerLogin);
		getList();
		RankedPlayer player = findPlayer(playerLogin);
		if (player == null){
			// player does not exist, create a new player
			player = addNewPlayer(playerLogin);
		}
		player.newWin(1,false);
		// save changes
		saveUsers();
	}

	public static void addSurvival(String playerLogin){
		Logger.info("RankingHandler.addSurvival called: " + playerLogin);
		getList();
		RankedPlayer player = findPlayer(playerLogin);
		if (player == null){
			// player does not exist, create a new player
			player = addNewPlayer(playerLogin);
		}
		player.newSurvival();
		// save changes
		saveUsers();
	}

	public static void addPlayerLoss(String playerLogin, boolean survived){
		Logger.info("RankingHandler.addPlayerLoss called: " + playerLogin);
		getList();
		RankedPlayer player = findPlayer(playerLogin);
		if (player == null){
			// player does not exist, create a new player
			player = addNewPlayer(playerLogin);
		}
		player.newLoss();
		if (survived){
			player.addToNrDefeatedPlayers(1);
			Logger.fine("player.newSurvival()");
			player.newSurvival();
		}
		// save changes
		saveUsers();
	}
	
	private static RankedPlayer addNewPlayer(String playerLogin){
		RankedPlayer newPlayer = new RankedPlayer(playerLogin);
		allRankedPlayers.add(newPlayer);
//		saveUsers();
		return newPlayer;
	}
	
	private static RankedPlayer findPlayer(String playerLogin){
		RankedPlayer foundPlayer = null;
		List<RankedPlayer> users = getList();
		for (RankedPlayer aPlayer : users) {
			if (aPlayer.isPlayer(playerLogin)){
				foundPlayer = aPlayer;
			}
		}
		return foundPlayer;
	}
	
	/**
	 * Used to return all data about a players ranking to the Android client
	 * @param playerLogin
	 * @return
	 */
	public static RankedPlayer findPlayerRanking(String playerLogin){
		RankedPlayer playerRanking = findPlayer(playerLogin);
		playerRanking.setRankedPlayersNr(allRankedPlayers.size());
		playerRanking.setRanking(allRankedPlayers.indexOf(playerRanking)+1);
		return playerRanking;
	}

	/**
	 * Returns a sorted array with all ranked players
	 * @return a sorted array of players
	 */
	public static RankedPlayer[] getRanking(){
		getList();
		RankedPlayer[] rankingArray = new RankedPlayer[allRankedPlayers.size()];
		// fill the array
		int index = 0;
		for (RankedPlayer aPlayer : allRankedPlayers) {
			rankingArray[index] = aPlayer;
			index++;
		}
		// sort the array
		Arrays.sort(rankingArray);
		return rankingArray;
	}

	private static void saveUsers(){
		String dataPath = PropertiesHandler.getProperty("datapath");
		String completePath = dataPath + "users\\ranking.properties";
		File playersFile = new File(completePath);
		try{
			FileWriter fw = new FileWriter(playersFile);
			PrintWriter pw = new PrintWriter(fw);
			pw.println("# File created " + (new Date()));
			int counter = 0;
			for (RankedPlayer aPlayer : allRankedPlayers) {
				pw.println(aPlayer.getSaveString(counter));
				counter++;
			}
			pw.close();
			Logger.info("Ranking saved");
		}
		catch(IOException ioe){
			Logger.severe("Error while saving users");
			ioe.printStackTrace();
		}

	}
	
	/**
	 * Used by PropertiesHandler when rankings have been changed on file
	 *
	 */
	public static void reloadList(){
		allRankedPlayers = null;
		getList();
	}

	private static List<RankedPlayer> getList(){
		if (allRankedPlayers == null){
			allRankedPlayers = new ArrayList<RankedPlayer>();
			int index = 0;
			boolean continueLoop = true;
			while (continueLoop){
				String tmpStr = RankingHandler.getProperty("user" + index);
				if (!tmpStr.equals("")){
					Logger.fine("adding ranked player: " + tmpStr);
					RankedPlayer tmpPlayer = new RankedPlayer(tmpStr);
					allRankedPlayers.add(tmpPlayer);
					index++;
				}else{
					continueLoop = false;
				}
			}
			Collections.sort(allRankedPlayers);
		}
		return allRankedPlayers;
	}

	private static String getProperty(String key){
		String retVal = "";
//		properties = RankingHandler.getInstance();
		properties = PropertiesHandler.getInstance("users." +DEFAULT_PROPERTIES_NAME);
		String tmpValue = properties.getProperty(key);
		if (tmpValue != null){
			retVal = tmpValue;
		}else{
			retVal = "";
		}
		return retVal;
	}
	

}
