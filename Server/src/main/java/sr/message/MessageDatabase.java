package sr.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.Message;
import spaceraze.world.Player;
import spaceraze.world.UniqueIdCounter;

//TODO 2020-11-25 Change this to an entity and save in the database, guess messages should be a stand alone application.
public class MessageDatabase implements Serializable{
	static final long serialVersionUID = 1L;
	public UniqueIdCounter uniqueMessageIDCounter;
	@SuppressWarnings("unused")
	private String nameOfGame;
	private List<Message> allMessages;
	
	/**
	 * @param args
	 */
	public MessageDatabase(String nameOfGame) {
		this.nameOfGame = nameOfGame;
		this.uniqueMessageIDCounter = new UniqueIdCounter();
		this.allMessages = new LinkedList<Message>();

	}
	
	/**
	 * Adding a new message and make a unique message per recipient and one for the sender. 
	 * Saving down the this object to file.
	 * Returns senders all new messages.
	 * @param aMessage
	 * @param aGalaxy
	 * @return
	 */
	public List<Message> addMessage(Message aMessage, Galaxy aGalaxy, int latestReadMessage){
		aMessage.setTurn(aGalaxy.getTurn());
		allMessages.addAll(0, getPlayersUniqueMessages(aMessage, aGalaxy));
		new MessageDataBaseSaver().saveMessageDataBase(aGalaxy.getGameName(), this);
		return  getPlayerNewMessages(aMessage.getSender(), latestReadMessage);
	}

	/**
	 * Only used for single player games
	 * @param aMessage
	 * @param aGalaxy
	 */
	public void addMessage(Message aMessage, Galaxy aGalaxy){
		aMessage.setUniqueId(getUniqueMessageIDCounter().getUniqueId());
		aMessage.setOwner(aMessage.getRecipientPlayer());
		allMessages.add(0,aMessage);
	}

	public List<Message> getAllMessages(){
		return allMessages;
	}
	
	public UniqueIdCounter getUniqueMessageIDCounter() {
		return uniqueMessageIDCounter;
	}
	
	public boolean haveNewMessages(String playerName){
		for (Message aMessage : allMessages) {
			if(aMessage.isOwner(playerName) && !aMessage.isSender(playerName) && !aMessage.isRead()){
				return true;
			}
		}
		return false;		  
	  }
	
	public List<Message> getPlayerSentMessages(String playerName){
		List<Message> sentMessages = new LinkedList<Message>();
		for (Message aMessage : allMessages) {
			if(aMessage.isSender(playerName) && aMessage.isOwner(playerName)){
				sentMessages.add(aMessage);
			}
		}
		
		return sentMessages;
	}
	
	public List<Message> getPlayerMessages(String playerName){
		List<Message> ownMessages = new LinkedList<Message>();
		for (Message aMessage : allMessages) {
			if(aMessage.isOwner(playerName) && !aMessage.isSender(playerName)){
				ownMessages.add(aMessage);
			}
		}
		
		return ownMessages;
	}
	
	public List<Message> getPlayerNewMessages(String playerName, int latestReadMessage){
		Logger.finer("getPlayerNewMessages playerName: " +playerName + " latestReadMessage:" + latestReadMessage);
		List<Message> ownMessages = new LinkedList<Message>();
		for (Message aMessage : allMessages) {
			if(aMessage.isOwner(playerName) && !aMessage.isRead() && !aMessage.isSender(playerName)){
				Logger.finer("latestReadMessage: " +latestReadMessage + " aMessage.getUniqueId():" + aMessage.getUniqueId());
				if(latestReadMessage < aMessage.getUniqueId()){
					ownMessages.add(aMessage);
				}
			}
		}
		
		return ownMessages;
	}
	
	/**
	 * Sets message with messageId to read and returns all message with greater number then latestReadMessage to send back to the client.
	 * @param playerName
	 * @param messageId
	 * @param latestReadMessage
	 * @return
	 */
	public List<Message> setMessagesRead(String playerName, int messageId, int latestReadMessage){
		List<Message> ownMessages = new LinkedList<Message>();
		for (Message aMessage : allMessages) {
			if(aMessage.isOwner(playerName)){
				if(messageId ==  aMessage.getUniqueId()){
					aMessage.setRead(true);
				}else if(latestReadMessage < aMessage.getUniqueId() && !aMessage.isRead()){
					ownMessages.add(aMessage);
				}
			}
		}
		return ownMessages;
	}
	
	/**
	   * Convert the incoming message (from client to server) to a list with an unique message per player. 
	   * @param aGalaxy
	   * @return
	   */
	  public List<Message> getPlayersUniqueMessages(Message message, Galaxy aGalaxy){
		  List<Message> recipientMessages = new ArrayList<Message>();
		  recipientMessages.add(getAsSentMessage(message, getUniqueMessageIDCounter().getUniqueId()));
		  recipientMessages.addAll(getRecipientMessages(message, aGalaxy));
		  
		  return recipientMessages;
	  }
	  
	  /**
	   * Get sending plyers unique message
	   * @return
	   */
	  private Message getAsSentMessage(Message message, int uniqueIdCounter){
		  Message tempMessage = new Message(message, uniqueIdCounter);
		  tempMessage.setOwner(message.getOwner());
		  tempMessage.setRead(true);
		  return tempMessage;
	  }
	  
	  /**
	   * Get all reciptient players unique messages
	   * @param aGalaxy
	   * @return
	   */
	  private List<Message> getRecipientMessages(Message mesage, Galaxy aGalaxy){
		  List<Message> recipientMessages = new ArrayList<Message>();
		  if(mesage.getType().equals("all")){
			  List<Player> players = aGalaxy.getPlayers();
			  for (Player player : players) {
				  if(!player.isPlayer(mesage.getSender())){
					  Logger.finer("Adding unique player mail to: " +player.getName() + " from:" + mesage.getSender());
					  Message tempMessage = new Message(mesage, getUniqueMessageIDCounter().getUniqueId());
					  tempMessage.setOwner(player.getName());
					  recipientMessages.add(tempMessage);
				  }
			  }
			  
		  }else if(mesage.getType().equals("private")){
			  Message tempMessage = new Message(mesage, getUniqueMessageIDCounter().getUniqueId());
			  tempMessage.setOwner(mesage.getRecipientPlayer());
			  recipientMessages.add(tempMessage);
		  }else{ // meddelandet skall till alla i en Faction
			  List<Player> players = aGalaxy.getFactionMember(GameWorldHandler.getFactionByName(mesage.getRecipientFaction(), aGalaxy.getGameWorld()));
			  for (Player player : players) {
				  if(!player.isPlayer(mesage.getSender())){
					  Logger.finer("Adding unique player mail to: " +player.getName() + " from:" + mesage.getSender());
					  Message tempMessage = new Message(mesage, getUniqueMessageIDCounter().getUniqueId());
					  tempMessage.setOwner(player.getName());
					  recipientMessages.add(tempMessage);;
				  }
			  }
		  }
		  return recipientMessages;
	  }

}
