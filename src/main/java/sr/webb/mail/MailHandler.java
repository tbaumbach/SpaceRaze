/*
 * Created on 2005-mar-31
 */
package sr.webb.mail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.util.properties.PropertiesHandler;
import spaceraze.world.*;
import spaceraze.world.spacebattle.ReportLevel;
import sr.server.SR_Server;
import sr.server.UpdateRunner;
import sr.webb.users.User;
import sr.webb.users.UserHandler;

/**
 * @author WMPABOD
 *
 * This class handles all sending of emails, either from an admin using a webb
 * interface, or a game server sending mails to players.
 */
public class MailHandler {
	
	/**
	 * Sends a message to all players who want to recieve messages from admins
	 * @param title
	 * @param content
	 * @param theAdmin
	 */
	public static void sendAdminMessage(String title, String content, User theAdmin){
		// get users who want admin mails
		List<User> users = UserHandler.getUsers(User.WANT_EMAIL_ADMIN);
		String tmpContent = content;
		tmpContent = tmpContent + "\n/From SpaceRaze Administrator: " + theAdmin.getName();
		sendMailToUsers("Admin message: " + title,tmpContent,users);
	}

	/**
	 * Send e-mails to all admins.
	 * Used when a game has an error
	 * @param title Short description of error type
	 * @param errorContent The log showing the error 
	 */
	public static void sendErrorToAdmins(String title, String errorContent){
		// get users who want admin mails
		List<User> users = UserHandler.getAdminUsers();
		String tmpContent = "An exception has occured.\n\n";
		tmpContent = tmpContent + "Last game log:\n\n";
		tmpContent = tmpContent + errorContent;
		sendMailToUsers("Error: " + title,tmpContent,users);
	}

	public static void sendNewGameMessage(SR_Server aNewServer){
		// get users who want new game mails
		List<User> users = UserHandler.getUsers(User.WANT_EMAIL_GAME);
		String title = aNewServer.getGameName() + " is open to join";
		String content = "A new game has been started and is open to join.\n"; 
		content = content + "Game name is: " + aNewServer.getGameName() + "\n";
		content = content + "GameWorld: " + aNewServer.getGalaxy().getGameWorld().getFullName() + "\n";
		content = content + "Game map is: " + aNewServer.getGalaxy().getMapNameFull() + "\n";
		content = content + "Max # players: " + aNewServer.getGalaxy().getNrStartPlanets() + "\n";
		content = content + "Autobalance: " + Functions.getYesNo(aNewServer.getAutoBalance()) + "\n";
		content = content + "Min number of steps: " + aNewServer.getGalaxy().getSteps() + "\n";
		content = content + "Group players from same faction: " + Functions.getYesNo(aNewServer.getGalaxy().isGroupSameFaction()) + "\n";
		content = content + "Random factions: " + Functions.getYesNo(aNewServer.getGalaxy().isRandomFaction()) + "\n";
		content = content + "Open factions: " + aNewServer.getGalaxy().getFactionListString() + "\n";
		content = content + "Scheduled updates: " + UpdateRunner.getUpdateDescription((int)aNewServer.getGalaxy().getTime()) + "\n";
		content = content + "Automated updates: yes\n";
		for (User aUser : users) {
			String content2 = content;
			// print link to site including encrypted password
			String baseurl = PropertiesHandler.getProperty("baseurl");
			// encrypt password
			byte[] passwordBytes = aUser.getPassword().getBytes();
			String encPassword = Base64.encodeBytes(passwordBytes);
			//TODO 2020-02-27  Lösen ord i länken är inte bra, även om det är kryperat, räcker det inte att enbart länka till sidan med användarnamn?
			String completeloginurl = baseurl + "/webb2/Master.jsp?action=login&login=" + aUser.getLogin() + "&password=" + encPassword; 
			content2 = content2 + "\n";
			content2 = content2 + "Link to the SpaceRaze site:\n";
			content2 = content2 + completeloginurl + "\n";
			sendMailToUser("[SR]New Game: " + title,content2,aUser);
		}
//		sendMailToUsers("New Game: " + title,content,users);
	}
	
	public static void sendNewTurnMessage(SR_Server aServer){
		Logger.fine("sendNewTurnMessage: " + aServer.getGameName());
		List<Player> players = aServer.getGalaxy().getPlayers();
		List<User> users = new LinkedList<User>();
		int currentTurn = aServer.getGalaxy().getTurn();
		for (Player aPlayer : players) {
			Logger.finer("aPlayer: " + aPlayer.getName());
			User aUser = UserHandler.findUser(aPlayer.getName());
			Logger.finer("aUser: " + aUser.getName());
			if ((aUser != null) && (aUser.getRecieveMail(User.WANT_EMAIL_TURN))){
				if (aServer.getGalaxy().gameEnded || (!aPlayer.isDefeated() || (aPlayer.getTurnDefeated() + 1) == currentTurn)){
					users.add(aUser);
					Logger.finer("User added");
				}
			}
		}
		String title = null;
		String content = null;
		int time = (int)aServer.getGalaxy().getTime();
		if (aServer.getTurn() == 1){
			// first turn messages
			title = aServer.getGameName() + " has started!";
			content = aServer.getGameName() + " has been started and has updated to the first turn.\n";
		}else{
			// not first turn messages
//			title = aServer.getGameName() + " = turn " + aServer.getTurn();
			title = aServer.getGameName() + " updated";
			content = aServer.getGameName() + " has been updated to turn " + aServer.getTurn() + "\n";
		}
		if (time > 0){ // TODO (Paul) nullPointer om drag 0, ur inte skapad än?
			UpdateRunner ur = aServer.getUpdateRunner();
			if (ur != null){
				content = content + "Next automatic update: " + ur.getNextUpdate() + "\n";
			}
		}
		// generate turn info & highlights for each individual player
		// also generate url to loginpage
		// then send the mail
		for (User aUser : users) {
			String content2 = content;
			Player aPlayer = aServer.getPlayer(aUser.getLogin(),aUser.getPassword());
			// print link to site including encrypted password
			String baseurl = PropertiesHandler.getProperty("baseurl");
			// encrypt password
			byte[] passwordBytes = aUser.getPassword().getBytes();
			String encPassword = Base64.encodeBytes(passwordBytes);
			String completeloginurl = baseurl + "/webb2/Master.jsp?action=login&login=" + aUser.getLogin() + "&password=" + encPassword;
			content2 = content2 + "\n";
			content2 = content2 + "Link to the SpaceRaze site:\n";
			content2 = content2 + completeloginurl + "\n";
			// add highlights
			content2 = content2 + "\n";
			content2 = content2 + "----------\n";
			content2 = content2 + "Highlights\n";
			content2 = content2 + "----------\n";
			content2 = content2 + "\n";
			content2 = content2 + getHighlights(aPlayer);
			// add Lost In Space
			content2 = content2 + "\n";
			content2 = content2 + "-------------\n";
			content2 = content2 + "Lost In Space\n";
			content2 = content2 + "-------------\n";
			content2 = content2 + "\n";
			content2 = content2 + getLostInSpace(aPlayer, aServer.getGalaxy());
			// add turn info to content
			content2 = content2 + "\n";
			content2 = content2 + "---------\n";
			content2 = content2 + "Turn Info\n";
			content2 = content2 + "---------\n";
			content2 = content2 + "\n";
			content2 = content2 + getTurnInfo(aPlayer);
			sendMailToUser("[SR]" + title,content2,aUser);
		}
	}
	
	  /**
	   * Returns a spaceship list with all ships from a certain faction
	   * @param allShips
	   * @param aFaction
	   * @return
	   */
/*	private static List<Spaceship> getShips(List<Spaceship> allShips, String aFactionName){
	  	List<Spaceship> tmpShips = new LinkedList<Spaceship>();
	  	for (Spaceship aShip : allShips) {
			if (aShip.getOwner() != null){
				if (aShip.getOwner().getFaction().getName().equalsIgnoreCase(aFactionName)){
					tmpShips.add(aShip);
				}
			}else
			if (aFactionName == null){
				tmpShips.add(aShip);
			}
		}
	  	return tmpShips;
	}
*/

	  /**
	   * Returns a list with all LiS from a certain faction.
	   * Same as in HighlightPanel.
	   */
	private static List<CanBeLostInSpace> getLostInSpaceByFaction(List<CanBeLostInSpace> allLostInSpace, String aFactionName, Galaxy galaxy){
		return allLostInSpace.stream().filter(canBeLostInSpace -> isOwnedByFaction(canBeLostInSpace, aFactionName, galaxy)).collect(Collectors.toList());
	}

	private static boolean isOwnedByFaction(CanBeLostInSpace canBeLostInSpace, String aFactionName, Galaxy galaxy) {
		if (canBeLostInSpace.getOwner() != null){
			if (GameWorldHandler.getFactionByKey(galaxy.getPlayerByGovenorName(canBeLostInSpace.getOwner()).getFactionKey(), galaxy.getGameWorld()).getName().equalsIgnoreCase(aFactionName)){
				return true;
			}
		}else
		if (aFactionName == null){
			return true;
		}
		return false;
	}

	private static String getLostInSpace(Player aPlayer, Galaxy galaxy){
		StringBuffer sb = new StringBuffer();
		boolean lisExist = false;
		Report lastReport = aPlayer.getTurnInfo().getLatestGeneralReport();
		List<CanBeLostInSpace> lostShips = lastReport.getLostShips();
		List<CanBeLostInSpace> lostTrops = lastReport.getLostTroops();
		// print players own losses
  		String playerFactionName = GameWorldHandler.getFactionByKey(aPlayer.getFactionKey(), galaxy.getGameWorld()).getName();
  		List<CanBeLostInSpace> tmpList = getLostInSpaceByFaction(lostShips, playerFactionName, galaxy);
  		if (tmpList.size() > 0){
  			sb.append(drawFactionLis(tmpList,"Own ships lost"));
  			lisExist = true;
  		}
  		tmpList = getLostInSpaceByFaction(lostTrops, playerFactionName, galaxy);
  		if (tmpList.size() > 0){
  			sb.append(drawFactionLis(tmpList,"Own troop lost"));
  			lisExist = true;
  		}
  		// print neutral ships destroyed
  		tmpList = getLostInSpaceByFaction(lostShips,null, galaxy);
  		if (tmpList.size() > 0){
  			sb.append(drawFactionLis(tmpList,"Neutral ships destroyed"));
  			lisExist = true;
  		}
  		tmpList = getLostInSpaceByFaction(lostTrops,null, galaxy);
  		if (tmpList.size() > 0){
  			sb.append(drawFactionLis(tmpList,"Neutral troops destroyed"));
  			lisExist = true;
  		}
  		// print ships from other factions
  		List<Faction> allFactions = galaxy.getActiveFactions(GameWorldHandler.getFactionByKey(aPlayer.getFactionKey(), galaxy.getGameWorld()));
  		for (Faction aFaction : allFactions) {

  			tmpList = getLostInSpaceByFaction(lostShips,aFaction.getName(), galaxy);
  	  		if (tmpList.size() > 0){
  	  			sb.append(drawFactionLis(tmpList,aFaction.getName() + " ships destroyed"));
  	  			lisExist = true;
  	  		}
  	  	tmpList = getLostInSpaceByFaction(lostTrops,aFaction.getName(), galaxy);
	  		if (tmpList.size() > 0){
	  			sb.append(drawFactionLis(tmpList,aFaction.getName() + " troops destroyed"));
	  			lisExist = true;
	  		}
		}
  		
  		// if no ships is lost in space, print "None"
  		if (!lisExist){
  			sb.append("None\n");
  		}
  		
  		return sb.toString();
	}
	
	private static String drawFactionLis(List<CanBeLostInSpace> lisList, String title){
		StringBuffer sb = new StringBuffer();
		sb.append(title + ": ");
		if (lisList.size() > 0){
			for (CanBeLostInSpace aLIS : lisList) {		
				sb.append(aLIS.getLostInSpaceString());
				sb.append(" ");
			}
			sb.setLength(sb.length()-1);
		}
		sb.append("\n");
		return sb.toString();
	}

	private static String getHighlights(Player aPlayer){
		StringBuffer highlightsText = new StringBuffer();
	    Report lastReport = aPlayer.getTurnInfo().getLatestGeneralReport();
	    List<Highlight> highlightsList = lastReport.getHighlights();
	    if (highlightsList.size() == 0){
	    	// add highlight that there are no special highlights this turn
	    	highlightsText.append("Nothing special to report" + "\r\n");
	    }
	    for (Highlight hlTmp : highlightsList) {
			highlightsText.append(hlTmp.getMessage() + " \n");
			Logger.finest("appending highlight: " + hlTmp.getMessage() + "\n");
		}
	    return highlightsText.toString();
	}
	
	private static String getTurnInfo(Player aPlayer){
		String turnInfoText = "";
		turnInfoText = aPlayer.getTurnInfoText(aPlayer.getGalaxy().getTurn(),ReportLevel.SHORT);
		return turnInfoText;
	}

	public static void sendNewPlayerMessage(User aUser){
		String tmpContent = "Welcome to SpaceRaze!\n";
		String roleString = " Player";
		if (aUser.isAdmin()){
			roleString = "n Administrator";
		}
		tmpContent = tmpContent + "You have been added as a" + roleString + " to the SpaceRaze game site.\n";
		tmpContent = tmpContent + "You have the following login: " + aUser.getLogin() + "\n";
		tmpContent = tmpContent + "You have the following password: " + aUser.getPassword() + "\n";
		tmpContent = tmpContent + "To activate your account you need to change your passeword.\n";
		tmpContent = tmpContent + "The URL to change the password is:\n";
		String baseurl = PropertiesHandler.getProperty("baseurl");
		String completeloginurl = baseurl + "/webb2/Master.jsp?action=change_password"; 
		tmpContent = tmpContent + completeloginurl + " \n";
		sendMailToUser("[SR]Welcome to SpaceRaze!",tmpContent,aUser);
	}

	private static void sendMailToUsers(String title, String content, List<User> recipientUsers){
		for (User aUser : recipientUsers){
			sendMailToUser("[SR]" + title,content,aUser);
		}
	}
	
	private static void sendMailToUser(String title, String content, User recipientUser){
		String eMailAddresses = recipientUser.getEmails();
		if (!eMailAddresses.equals("")){
			StringTokenizer st = new StringTokenizer(eMailAddresses);
			while(st.hasMoreTokens()){
				//TODO 2020-11-30 Test and activate this.
				//sendAMail(title,content,st.nextToken());
			}
		}
	}

	private static void sendAMail(String subject, String content, String eMailAddress){
		Logger.info("Send mail to: " + eMailAddress + " Title: " + subject);
		Logger.finest("Content:");
		Logger.finest(content);
		// get mail properties
		String smtpServer = PropertiesHandler.getProperty("mailsmtp");
		String login = PropertiesHandler.getProperty("maillogin");
		String password = PropertiesHandler.getProperty("mailpassword");
		String from = PropertiesHandler.getProperty("mailfrom");
		// send the mail
		sendSocket(smtpServer,eMailAddress,from,subject,content,login,password);
	}

	/**
	    * "send" method to send the message.
	    */
		public static void sendSocket(String smtpServer, String to, String from, String subject, String body, String login, String password){
			Logger.finer("sendSocket, smtp: " + smtpServer + " Subject: " + subject + " To: " + to + " From: " + from + " login: " + login + " password: " + password);
			boolean noMail = PropertiesHandler.getProperty("mail").equals("false");
			if (!noMail){
		      try{
				Socket s = new Socket(InetAddress.getByName(smtpServer),25);
				PrintWriter pw = new PrintWriter(s.getOutputStream());
				BufferedReader br  = new BufferedReader(new InputStreamReader(s.getInputStream()));
				// -- Create a new message --
				String send = "";
				String recieved = br.readLine();
				Logger.finest("Anwser: " + recieved);

				if (login != null){
					// encode login/password in base64
//					byte[] login64 = login.getBytes();
//					String loginStr64 = Base64.encodeBytes(login64);
//					byte[] pass64 = password.getBytes();
//					String passStr64 = Base64.encodeBytes(pass64);
					byte[] lp64 = (login + "\0" + login + "\0" + password).getBytes();
					String lpStr64 = Base64.encodeBytes(lp64);
					
					// Auth
					send = "AUTH PLAIN";
					pw.println(send);
					pw.flush();
					Logger.finest("Send:   " + send);
					recieved = br.readLine();
					Logger.finest("Anwser: " + recieved);
				
					send = lpStr64;
					pw.println(send);
					pw.flush();
					Logger.finest("Send:   " + send);
					recieved = br.readLine();
					Logger.finest("Anwser: " + recieved);
				}
				
				// -- Set the FROM field --
				send = "MAIL FROM:" + from;
				pw.println(send);
				pw.flush();
				Logger.finest("Send:   " + send);
				recieved = br.readLine();
				Logger.finest("Anwser: " + recieved);

				// -- Set the TO field --
				send = "RCPT TO:" + to;
				pw.println(send);
				pw.flush();
				Logger.finest("Send:   " + send);
				recieved = br.readLine();
				Logger.finest("Anwser: " + recieved);
				
				// -- Set the subject and body text --
				send = "DATA";
				pw.println(send);
				pw.flush();
				Logger.finest("Send:   " + send);
				send = "To: " + to;
				pw.println(send);
				pw.flush();
				Logger.finest("Send:   " + send);
				send = "From: " + from;
				pw.println(send);
				pw.flush();
				Logger.finest("Send:   " + send);
				send = "Subject: " + subject + "\r\n";
				pw.println(send);
				pw.flush();
				Logger.finest("Send:   " + send);
				send = body + "\r\n.\r\n";
				pw.println(send);
				pw.flush();
				Logger.finest("Send:   " + send);
				recieved = br.readLine();
				Logger.finest("Anwser: " + recieved);
				
				// -- Send the message --
				send = "SAML";
				pw.println(send);
				pw.flush();
				Logger.finest("Send:   " + send);
				recieved = br.readLine();
				Logger.finest("Anwser: " + recieved);
				
				// close socket connection
				s.close();
		      }
		      catch (Exception ex){
		    	Logger.warning("Exception in MailHandler, cannot connect to mail server?");
		    	Logger.info("Exception: " + ex.toString());
//		    	ex.printStackTrace();
		      }
		    }
		    Logger.finest("sendSocket finished.");
		}
/*		
		public static void main(String[] args){
			sendSocket("smtp.hemmanet.se","paul.bodin@gmail.com","spaceraze@hemmanet.se","Testmessage SR","Body text...","spacerazemail","overlord");
		}
*/
}
