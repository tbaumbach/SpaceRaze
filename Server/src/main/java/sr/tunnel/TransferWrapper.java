/*
 * Created on 2005-mar-05
 */
package sr.tunnel;

import java.io.Serializable;

import sr.world.GameWorld;
import sr.world.Message;
import sr.world.PlanetInfos;
import sr.world.PlanetOrderStatuses;
import sr.world.Player;
import sr.world.orders.Orders;
import sr.world.spacebattle.ReportLevel;

/**
 * @author WMPABOD
 *
 * Wrapper for data transferred to and from tunnel servlet.
 * Also contains some logic for that data and the tunnel.
 */
public class TransferWrapper implements Serializable{
	static final long serialVersionUID = 1L;
	private Object returnObject;
	private int port; // = gameid
//	private boolean getPlayer; // false = savePlayer
	private String message;
	private int turn;
	private int contentSize; // when getTurn, server should return the byte size of the game specified in port
	private GameWorld gameWorld;
	
	// ny kod för att koppla loss Player objektet när ett drag görs.
	private PlanetInfos pi;
	private PlanetOrderStatuses planetOrderStatuses;
	private Orders orders;
	private String playerName = null;
	private String notes;
	private Message mailMessage;
	boolean finishedThisTurn = false;
	private int messageId;
	private int latestReadMessage;
	
	private ReportLevel reportLevel;
	
	private boolean android; // true if caller is the android client. Default is that the client is not the Android client.
	private int mapInfoFromTurn; // used if caller only wants to have MapInfoTurn-objects from a certain turn. Default will return all turns

	public ReportLevel getReportLevel() {
		return reportLevel;
	}

	public TransferWrapper(Player aPlayer,String message,int port){
		//this.sendPlayer = aPlayer;
		this.port = port;
//		this.getPlayer = getPlayer;
		this.message = message;
		
		// ny kod för att koppla loss Player objektet när ett drag görs.
		if(aPlayer != null){
//			pi = aPlayer.getPlanetInfos(); // TODO Paul ta bort detta när du har hittat buggen / Tobbe
			orders = aPlayer.getOrders();
			playerName = aPlayer.getName();
			notes = aPlayer.getNotes();
			finishedThisTurn = aPlayer.isFinishedThisTurn();
			turn = aPlayer.getGalaxy().getTurn();
			latestReadMessage = aPlayer.getLatestMessageIdFromServer();
			messageId = aPlayer.getMessageId();
			reportLevel = aPlayer.getReportLevel();
			planetOrderStatuses = aPlayer.getPlanetOrderStatuses();
		}
	}
	
	public TransferWrapper(Message aMessage, Player aPlayer, String message,int port){
		this(aPlayer,message,port);
		this.mailMessage = aMessage;
		
	}
	
	
	public void setReturnObject(Object returnObject){
		this.returnObject = returnObject;
	}
	
	public Object getReturnObject(){
		return returnObject;
	}
/*
	public Player getSendPlayer(){
		return sendPlayer;
	}*/
	
	public String getMessage(){
		return message;
	}

	public void setMessage(String aMessage){
		message = aMessage;
	}

	public int getPort(){
		return port;
	}
	
	public boolean isGetPlayer(){
		return (playerName == null);
	}
	
	public boolean isGetTurn(){
		return (playerName == null) & (message == null);
	}
	
	public void setTurn(int turn){
		this.turn = turn;
	}
	
	public int getTurn(){
		return turn;
	}

	public int getContentSize() {
		return contentSize;
	}

	public void setContentSize(int contentSize) {
		this.contentSize = contentSize;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	public PlanetInfos getPi() {
		return pi;
	}

	public void setPi(PlanetInfos pi) {
		this.pi = pi;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isFinishedThisTurn() {
		return finishedThisTurn;
	}

	public void setFinishedThisTurn(boolean finishedThisTurn) {
		this.finishedThisTurn = finishedThisTurn;
	}

	public Message getMailMessage() {
		return mailMessage;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getLatestReadMessage() {
		return latestReadMessage;
	}

	public void setLatestReadMessage(int latestReadMessage) {
		this.latestReadMessage = latestReadMessage;
	}

	public PlanetOrderStatuses getPlanetOrderStatuses() {
		return planetOrderStatuses;
	}

	public void setPlanetOrderStatuses(PlanetOrderStatuses planetOrderStatuses) {
		this.planetOrderStatuses = planetOrderStatuses;
	}

	public boolean isAndroid() {
		return android;
	}

	public void setAndroid(boolean android) {
		this.android = android;
	}

	public int getMapInfoFromTurn() {
		return mapInfoFromTurn;
	}

	public void setMapInfoFromTurn(int mapInfoFromTurn) {
		this.mapInfoFromTurn = mapInfoFromTurn;
	}

}
