/*
 * Created on 20056-oct-09
 */
package sr.notifier;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import spaceraze.servlethelper.CreateNewGameData;
import spaceraze.servlethelper.ReturnGames;
import spaceraze.util.general.Logger;
import sr.server.ServerHandler;
import sr.server.map.MapHandler;
import sr.server.persistence.PHash;
import sr.webb.users.User;
import sr.webb.users.UserHandler;

/**
 * @author WMPABOD
 *
 * Used to tunnel calls from notifier to the server
 */
public class NotifierTunnel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig c) throws ServletException {
		super.init (c);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response )throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.info("NotifierTunnel doPost called");

		// check if serverhandler is initialized?
		ServletContext sc = getServletContext();
		ServerHandler sh = (ServerHandler)sc.getAttribute("serverhandler");
		if (sh == null){
			sh = new ServerHandler();
			sc.setAttribute("serverhandler", sh);
			Logger.info("Serverhandler created by NotifierTunnel");
//			PHash.dumpHashtable();
		}

		ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(request.getInputStream()));
		Logger.finest("inputStream created");
		spaceraze.servlethelper.NotifierTransferWrapper transferWrapper = null;
		try {
			Logger.finest("Waiting to read...");
			transferWrapper = (spaceraze.servlethelper.NotifierTransferWrapper)inputStream.readObject();
			Logger.finest("transfer login: " + transferWrapper.getUserLogin());
			Logger.finest("getallmaps: " + transferWrapper.isGetAllMaps());
			if(transferWrapper != null){ 
				if (transferWrapper.getCreateNewGameData() != null){
					Logger.fine("Create new game in NotifierTunnel");
					CreateNewGameData cngd = transferWrapper.getCreateNewGameData();
					// create new game
					int steps = MapHandler.getSteps(cngd.getMapName(), cngd.getMaxNrPlayers());
					String tmpGameName = null;
					if(cngd.getGameName().equals("?")){
						tmpGameName = sh.getFirstFreeDefaultName();
					}else{
						tmpGameName = cngd.getGameName(); 
					}
					int winLimit = 75;
					String status = sh.startNewGame(cngd.getGameWorldFileName(), tmpGameName, cngd.getMapName(), String.valueOf(steps), cngd.getAutoBalanceString(), cngd.getTimeString(), cngd.getEmailPlayers(), cngd.getMaxNrPlayers(), cngd.getUserLogin(), cngd.getGamePassword(), cngd.getGroupFaction(), cngd.getSelectableFactionNames(), cngd.getRandomFactionString(), cngd.getDiplomacy(), "yes", winLimit, winLimit, cngd.getEndTurn(), cngd.getNumberOfStartPlanet(), cngd.getStatisticGameType());

					if (status.equalsIgnoreCase("game started") & cngd.getGameName().equals("?")){
						status += " " + tmpGameName; 
					}
					Logger.fine("Create new game status: " + status);
					// return status in returncode
					transferWrapper.setReturnCode(status);
				}else
				if (transferWrapper.isGetAllMaps()){
					transferWrapper.setAllMaps(MapHandler.getAllMaps());
				}else
				if (transferWrapper.isDeleteGame()){
					sh.deleteGame(transferWrapper.getGameName());
				}else
				if (transferWrapper.getChangeTurn() != 0){
					try{
						if (transferWrapper.getChangeTurn() > 0){
							sh.updateGame(transferWrapper.getGameName(), transferWrapper.getChangeTurn());
						}else{ // transferWrapper.getChangeTurn() < 0
							sh.rollbackGame(transferWrapper.getGameName(), transferWrapper.getChangeTurn());
						}
						transferWrapper.setReturnCode("ok");
					}catch(Exception e){
						// set returnvalue
						String returnCode = "Error while updating:\n\n";
						returnCode += e.toString() + "\n\n";
						StackTraceElement[] stackTrace = e.getStackTrace();
						for (StackTraceElement element : stackTrace) {
							returnCode += element.toString() + "\n";
						}
						transferWrapper.setReturnCode(returnCode);
						// print error
						e.printStackTrace();
					}
				}else{
					User aUser = UserHandler.findUser(transferWrapper.getUserLogin());
					if (aUser == null){
						PHash.incCounter("notifier.nouserfound");
						transferWrapper.setReturnCode("u");
					}else{
						PHash.incCounter("notifier." + transferWrapper.getUserLogin());
						boolean turnToPerform = sh.getPlayerHasTurnToPerform(aUser);
						if (turnToPerform){
							boolean savedOnly = sh.getPlayerHasSavedOnlyToPerform(aUser);
							if (savedOnly){
								transferWrapper.setReturnCode("s");
							}else{
								transferWrapper.setReturnCode("x");
							}
						}else{
							transferWrapper.setReturnCode("n");
						}
						// maybe include games data
						if (transferWrapper.getReturnGames() != ReturnGames.NONE){
							transferWrapper.setGameListData(sh.getGamesData(aUser,transferWrapper.getReturnGames()));
						}
					}
				}
			}
		} catch( ClassNotFoundException ex ) {
			ex.printStackTrace();
		}
		// send response
		response.setStatus(HttpServletResponse.SC_OK);

		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(response.getOutputStream()));
		oos.writeObject(transferWrapper);
		oos.flush();
	}


}
