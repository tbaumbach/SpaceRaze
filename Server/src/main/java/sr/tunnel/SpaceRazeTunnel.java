/*
 * Created on 2005-mar-04
 */
package sr.tunnel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sr.general.Functions;
import sr.general.logging.Logger;
import sr.server.SR_Server;
import sr.server.ServerHandler;
import sr.world.Faction;
import sr.world.Galaxy;
import sr.world.Message;
import sr.world.Player;

/**
 * @author WMPABOD
 *
 * Used to tunnel calls from clients to the server
 */
@SuppressWarnings("serial")
public class SpaceRazeTunnel extends HttpServlet{
	public static final String NOT_SAVED_ALREADY_UPDATED = "Player not saved by tunnel, server has already updated";
	public static final String SERVER_ERROR = "Player saved, but error occured in server when updating";

	public void init(ServletConfig c) throws ServletException {
		super.init (c);
	}

	public void doGet( HttpServletRequest request,HttpServletResponse response )throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.info("doPost called");
		// used to set back GW is android
		Player tmpPlayer = null;
		
		// testar...
		ServletContext sc = getServletContext();
		ServerHandler sh = (ServerHandler)sc.getAttribute("serverhandler");
		if (sh == null){
			Logger.finest("sh is null...");
			sh = new ServerHandler();
			sc.setAttribute("serverhandler",sh);
			Logger.finest("New ServerHandler created by SpaceRazeTunnel");
		}else{
			Logger.finest("sh exists!");
		}
		
//		BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
        GZIPInputStream gzis = new GZIPInputStream(request.getInputStream()); 
		ObjectInputStream inputStream = new ObjectInputStream(gzis);

//		ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(request.getInputStream()));
		Logger.finest("inputStream created");
		TransferWrapper tw = null;
		//try {
			Logger.finest("Waiting to read...");
			// Alt 1: hämta hela objektet med en gång
//			tw = (TransferWrapper)inputStream.readObject();
			
			// Alt 2: hämta data från en byte array
	        byte[] buf = new byte[15*1000*1024]; 
			
			// sedan läs från inströmmen till arrayen 
	        int readSoFar = 0;
	        final int BLOCK_SIZE = 100 * 1024; 
	        try {
	            int lengthRead = inputStream.read(buf,readSoFar,BLOCK_SIZE);
            	Logger.finest("lengthRead: " + lengthRead);
	            while (lengthRead > -1) {
	            	readSoFar += lengthRead;
//	            	System.out.println("readSoFar: " + readSoFar);
	            	lengthRead = inputStream.read(buf,readSoFar,BLOCK_SIZE);
//	            	System.out.println("lengthRead: " + lengthRead);
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        
	        // trim array to size read
	        byte[] buf2 = new byte[readSoFar]; 
	        for (int i = 0; i < readSoFar; i++) {
				buf2[i] = buf[i];
			}
	        buf = buf2;
        	Logger.finest("trimmed to: " + readSoFar);

			// hämta ut objekten från arrayen
			try {
		        // Deserialize from a byte array
				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf));
		        tw = (TransferWrapper) in.readObject();
		        in.close();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
			
			Logger.finest("tw read");
			Logger.finest("tw read: port=" + tw.getPort());
			if(tw != null){ 
				if(tw.isGetTurn()){
					// client started
					//tw.setTurn(getTurn(tw.getPort()));
					SR_Server aServer = getTurn2(tw.getPort(),sh);
//					tw.setTurn(getTurn2(tw.getPort(),sh));
					int turn = aServer.getTurn();
					tw.setTurn(turn);
					Logger.finest("turn: " + turn);
					// set content size
			        byte[] buf0 = null;
			        try {
			            // Serialize to a byte array
			            ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
			            ObjectOutputStream out = new ObjectOutputStream(bos) ;
			            out.writeObject(aServer.getGalaxy());
			            out.close();
			        
			            // Get the bytes of the serialized object
			            buf0 = bos.toByteArray();
			            Logger.finest("Buffer length in getTurn: " + buf0.length);
			            tw.setContentSize(buf0.length);
			        } catch (IOException e) {
			        	e.printStackTrace();
			        }

					Logger.finest("autobalance: " + aServer.getAutoBalance());
					if (tw.isAndroid()){ // om klient är droid, ändra så att returnobjekt är factionnames
						List<String> factionNames = new LinkedList<String>();
						for (Faction aFaction : aServer.getOpenSelectableFactions()) {
							factionNames.add(aFaction.getName());
						}
						tw.setReturnObject(factionNames);
						Logger.finest("droid tw openFactions names set: ");
					}else
					if ((turn == 0) & aServer.getAutoBalance()){
						tw.setReturnObject(aServer.getOpenSelectableFactions());
						Logger.finest("tw openFactions set: " + tw.getReturnObject().toString());
					}else
					if ((turn == 0) & !aServer.getAutoBalance()){
						tw.setReturnObject(aServer.getSelectableFactions());
						Logger.finest("tw allFactions set: " + tw.getReturnObject().toString());
					}
					// set message
					if (aServer.getGalaxy().isRandomFaction()){
						tw.setMessage("randomfaction");
					}else{
						tw.setMessage("choosefaction");
					}
					
					// set gameWorld
					if (!tw.isAndroid()){ // is client is android, gw should not be included
						tw.setGameWorld(aServer.getGalaxy().getGameWorld());
					}
				}else if(tw.getMessage().equals("setMessagesRead")){
					List<Message> newMessages = setMessagesRead(tw, sh);
					tw.setReturnObject(newMessages);
				}else if(tw.getMessage().equals("getPlayerNewMessages")){
					List<Message> newMessages = getPlayerNewMessages(tw, sh);
					tw.setReturnObject(newMessages);
				}else if(tw.getMessage().equals("getPlayerMessages")){
					List<Message> newMessages = getPlayerMessages(tw, sh);
					tw.setReturnObject(newMessages);
				}else if(tw.getMessage().equals("getPlayerSentMessages")){
					List<Message> newMessages = getPlayerSentMessages(tw, sh);
					tw.setReturnObject(newMessages);
				}else if(tw.getMessage().equals("addMessage")){
					List<Message> newMessages = addMessage(tw, sh);
					tw.setReturnObject(newMessages);
				}else if(tw.isGetPlayer()){
					// client logging in
					String message = tw.getMessage();
					Logger.fine(message);
					//tw.setReturnObject(getPlayer(message,tw.getPort()));
					tmpPlayer = getPlayer2(message,tw.getPort(),sh);
					if (tw.isAndroid()){ // is client is android, remove some unnessesary stuff from the player and galaxy objects
						tmpPlayer = prunePlayer(tmpPlayer);
					}
					tw.setReturnObject(tmpPlayer);
				}else{
					// client saving a turn/player
					String saveMessage = savePlayer2(tw,sh);
					tw.setReturnObject(saveMessage);
				}
			}
		/*} catch( ClassNotFoundException ex ) {
			ex.printStackTrace();
		}*/
		// send response
		response.setStatus(HttpServletResponse.SC_OK);
		Logger.fine("Creating output");
//		BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
        GZIPOutputStream gzos = new GZIPOutputStream(response.getOutputStream());
		ObjectOutputStream oos = new ObjectOutputStream(gzos);

//		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(response.getOutputStream()));
		Logger.fine("Writing output");
//		oos.writeObject(tw);
		
		// Alt 2: skicka data bit för bit
		// först skapa en array med all data
		Logger.fine("Alt 2: skicka data byte för byte");
        byte[] buffer = null;
        try {
            // Serialize to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
            ObjectOutputStream out = new ObjectOutputStream(bos) ;
            out.writeObject(tw);
            out.close();
        
            // Get the bytes of the serialized object
            buffer = bos.toByteArray();
            Logger.fine("Buffer length: " + buffer.length);
        } catch (IOException e) {
        	e.printStackTrace();
        }
		
		// sedan skriv från arrayen till utströmmen
        int writtenSoFar = 0;
        int arrSize = buffer.length;
        final int BLOCK_SIZE_2 = 10 * 1024; 
        try {
            int lengthToWrite = 0;
            while (writtenSoFar < arrSize) {
            	if ((arrSize - writtenSoFar) < BLOCK_SIZE_2){
            		lengthToWrite = arrSize - writtenSoFar;
            	}else{
            		lengthToWrite = BLOCK_SIZE_2;
            	}
            	Logger.fine("lengthToWrite: " + lengthToWrite);
            	oos.write(buffer, writtenSoFar, lengthToWrite);
            	writtenSoFar += lengthToWrite;
            	Logger.fine("writtenSoFar: " + writtenSoFar);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        // slut Alt 2
		
		Logger.fine("Output written");
		oos.flush();
		Logger.fine("Output flushed");
		oos.close();
		Logger.fine("Output closed");
		// maybe set back GW if android
//		if (tw.isAndroid()){ // is client is android, gw should not be included in the galaxy object (there is only one droid GW and that will be set on the client)
//			tmpPlayer.getGalaxy().setGameWorld(oldGW);
//			Logger.info("Android: GW restored");
//			int index = 0;
//			for (Faction aFaction : tmpPlayer.getGalaxy().getFactions()) {
//				aFaction.setPlanetColor(factionColors.get(index));
//				index++;
//			}
//		}
	}
	
	/**
	 * Lots of stuff do not need to be sent to the Android client:
	 * -Factions
	 * -GW
	 * -enemy and neutral ships, VIPs and buildings
	 * -old turn infos and map infos
	 * -only current turn for the current player
	 * 
	 * This method will deepclone the player object and remove all that is not needed.
	 * 
	 * @param player the player to be sent to the android client
	 * @return a deepcloned and pruned player object
	 */
	private Player prunePlayer(Player player){
		Logger.info("Android: pruning Player");
		// clone player
		Player tmpPlayer = Functions.deepClone(player);
		// prune galaxy
		// ------------
		Galaxy g = tmpPlayer.getGalaxy();
		// remove gameworld
		g.setGameWorld(null);
		// remove factions
		g.removeFactions();
		// remove all ships,buildings,VIPs not belonging to the player
		// remove alot from other players
		g.pruneDroid(tmpPlayer);
		// prune player
		// ------------
		// remove faction, but save faction name first
		tmpPlayer.setFactionName(tmpPlayer.getFaction().getName());
		tmpPlayer.setFaction(null);
		// remove old turns in TurnInfo
		if (tmpPlayer.getTurnInfo() != null){
			tmpPlayer.getTurnInfo().pruneDroid();
		}
		// prune PlanetInfos
		if (tmpPlayer.getPlanetInfos() != null){
			tmpPlayer.getPlanetInfos().pruneDroid();
		}
		// remove Research
		tmpPlayer.setResearch(null);
		// remove old turns in MapInfos
		if ((tmpPlayer.getMapInfos() != null) & (g.getTurn() > 0)){
			tmpPlayer.getMapInfos().pruneDroid();
		}
		// finished, return pruned clone
		Logger.info("Android: pruning finished");
		return tmpPlayer;
	}

	private Player getPlayer2(String message,int port,ServerHandler sh){
		Logger.fine(message);
		Player tmpPlayer = null;
	    try {
//	    	boolean simpleLogin;
	    	Logger.finest("");
	    	Logger.info("Tunnel - getPlayer2: " + port);
	    	SR_Server aServer = sh.findGame(port);
	    	
	    	Logger.finest("aServer.getGameName(): " + aServer.getGameName());
	    	tmpPlayer = aServer.getPlayer(message);
	    	Logger.finest("tmpPlayer.getName(): " + tmpPlayer.getName());
	    	int turn = aServer.getTurn();
	    	// if turn = 0, immediately save new player
	    	if (turn == 0){
	    		aServer.updatePlayer(tmpPlayer);
	    		Logger.finest("aServer.updatePlayer(tmpPlayer): ");
	    	}
		} catch (Exception e) {
			Logger.fine("Exception: " + e.toString());
			e.printStackTrace();
		}
	    return tmpPlayer;
	}

	private String savePlayer2(TransferWrapper tw,ServerHandler sh){
		String saveMessage = null;
		int gameid = tw.getPort();
		
    	try {
    		Logger.finest("");
    		Logger.info("Tunnel - savePlayer2: " + gameid);
    		SR_Server aServer = sh.findGame(gameid);
    		int serverTurn = aServer.getTurn();
    		int playerTurn = tw.getTurn();
    		Logger.finest("serverTurn: " + serverTurn + " playerTurn: " + playerTurn);
    		if (serverTurn == playerTurn){
    			Logger.finest("Player Name: " + tw.getPlayerName());
    			Player tempPlayer = aServer.getGalaxy().getPlayer(tw.getPlayerName());
    			tempPlayer.setOrders(tw.getOrders());
//    			tempPlayer.setPlanetInfos(tw.getPi()); // TODO Paul 100701: denna bör tas bort då alla orders ligger i Orders-objektet
    			tempPlayer.setPlanetOrderStatuses(tw.getPlanetOrderStatuses());
    			tempPlayer.setNotes(tw.getNotes());
    			tempPlayer.setFinishedThisTurn(tw.isFinishedThisTurn());
    			tempPlayer.setReportLevel(tw.getReportLevel());
    			aServer.updatePlayer(tempPlayer);
    			Logger.finest("Player saved by tunnel");
    			saveMessage = "Player recieved and saved by tunnel";
    		}else{
    			Logger.fine(NOT_SAVED_ALREADY_UPDATED);
    			saveMessage = NOT_SAVED_ALREADY_UPDATED;
    		}
		} catch (Exception e) {
			Logger.severe("savePlayer2 " + SERVER_ERROR);
			saveMessage = SERVER_ERROR;
			e.printStackTrace();
		}
	    return saveMessage;
	}
	
	private List<Message> setMessagesRead(TransferWrapper tw,ServerHandler sh){
		int gameid = tw.getPort();
		SR_Server aServer = sh.findGame(gameid);
		return aServer.getMessageDatabase().setMessagesRead(tw.getPlayerName(), tw.getMessageId(), tw.getLatestReadMessage());
	}
	
	private List<Message> getPlayerNewMessages(TransferWrapper tw,ServerHandler sh){
		int gameid = tw.getPort();
		SR_Server aServer = sh.findGame(gameid);
		return aServer.getMessageDatabase().getPlayerNewMessages(tw.getPlayerName(), tw.getLatestReadMessage());
	}
	
	private List<Message> getPlayerMessages(TransferWrapper tw,ServerHandler sh){
		int gameid = tw.getPort();
		SR_Server aServer = sh.findGame(gameid);
		return aServer.getMessageDatabase().getPlayerMessages(tw.getPlayerName());
	}
	
	private List<Message> getPlayerSentMessages(TransferWrapper tw,ServerHandler sh){
		int gameid = tw.getPort();
		SR_Server aServer = sh.findGame(gameid);
		return aServer.getMessageDatabase().getPlayerSentMessages(tw.getPlayerName());
	}
	
	private List<Message> addMessage(TransferWrapper tw,ServerHandler sh){
		int gameid = tw.getPort();
		SR_Server aServer = sh.findGame(gameid);
		return aServer.getMessageDatabase().addMessage(tw.getMailMessage(), aServer.getGalaxy(), tw.getLatestReadMessage());
	}
	
	
	private SR_Server getTurn2(int port,ServerHandler sh){
		Logger.fine("");
		Logger.fine("Tunnel - getTurn2: " + port);
	    SR_Server aServer = sh.findGame(port);
//	    int turn = aServer.getTurn();
//	    Galaxy g = aServer.getGalaxy();
	    return aServer;
	}

}
