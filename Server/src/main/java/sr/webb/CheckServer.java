/*
 * Created on 2004-dec-28
 */
package sr.webb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Player;

/**
 * @author WMPABOD
 */
public class CheckServer {
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
//	private Player p;
	private int turn = -1;
	private String[] playerInfos;
	private Galaxy g;
	
	public CheckServer(int portnr){
	    int port = -1;
//	    boolean simpleLogin;
	    System.out.println("");
	    System.out.println("init - starting login sequence");
	    port = portnr;
	    // Koppla upp till servern
	    try {
	      System.out.println(InetAddress.getLocalHost());
	      s = new Socket(InetAddress.getLocalHost(), port);
	      System.out.println(s.toString());
	      ois = new ObjectInputStream(s.getInputStream());
	      oos = new ObjectOutputStream(s.getOutputStream());
	      System.out.println("Connected to " + s.getInetAddress().getHostName() + ":" + s.getPort());
	      turn = ( (Integer) ois.readObject()).intValue();
	      System.out.println("Turn data recieved successfully, turn is: " + turn);
          oos.writeObject("checkStatus");
          Player p = (Player)ois.readObject();
          System.out.println("Player recieved, errmsg: " + p.getErrorMessage());
          g = p.getGalaxy();
          playerInfos = getPlayerList(g);
          oos.writeObject(new Player("StatusCheck finished. Bye."));
          String msg = (String)ois.readObject();
          System.out.println("Server sais: " + msg);
          System.out.println("Status check finished");
	    }
	    catch (IOException e) {
	      System.out.println("Error while connecting to Server, CAUSE OF ERROR: " + e.toString());
	      System.out.println("IOException: " + e.toString());
	    }
	    catch (ClassNotFoundException cnfe) {
	      System.out.println(cnfe.toString());
	    }
	}
	
    private String[] getPlayerList(Galaxy g){
    	String[] allPlayerStrings = new String[g.getNrStartPlanets()];
    	Logger.fine("getPlayerList: " + allPlayerStrings.length);
    	int longestName = g.getLongestGovenorName();
        for (int i = 0; i < g.getPlayers().size(); i++){
        	Player temp = (Player)g.getPlayers().get(i);
        	String color = "#FF0000";
        	if (temp.getFaction().getName().equalsIgnoreCase("rebel")){
        		color = "#00FF00";
        	}else
        	if (temp.getFaction().getName().equalsIgnoreCase("league")){
        		color = "#0000FF";
        	}
        	String aPlayerStr = "<font color=\"" + color + "\">" + temp.getGovenorName() + "</font><span></span>";
        	// pad with space
        	for (int j = temp.getGovenorName().length(); j < (longestName + 3); j++){
        		aPlayerStr = aPlayerStr + " ";
        	}
        	if (getTurn() > 0){
        		if (temp.isDefeated()){
        			aPlayerStr = aPlayerStr + "Defeated";
        		}else
            	if (g.gameEnded){
            		aPlayerStr = aPlayerStr + "Victory";
            	}else
        		if (temp.getUpdatedThisTurn()){
        			aPlayerStr = aPlayerStr + "Finished";
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
	
	public String[] getPlayerList(){
		return playerInfos;
	}
	
	public boolean isRunning(){
		return turn > -1;
	}
	
	public int getTurn(){
		return turn;
	}
	
	public String getLastUpdatedString(){
		return g.getLastUpdatedString();
	}
	
	public String getStartingText(){
		String retStr = "";
		if (turn == 0){
			retStr = "New game is starting.<p>";
			int freeSlots = g.getNrStartPlanets() - playerInfos.length; 
			if (freeSlots > 0){
				retStr = retStr + " There are " + freeSlots + " player slots not taken (yet).<p>";
			}
		}
		return retStr;
	}

	public String getGameOverText(){
		String retStr = "";
		if (g.gameEnded){
			retStr = "Game has ended.<p>";
		}
		return retStr;
	}

	public static void main(String[] args) {
		new CheckServer(6793);
	}
}
