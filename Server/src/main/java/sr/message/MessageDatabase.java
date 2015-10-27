package sr.message;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Message;
import sr.world.UniqueIdCounter;

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
		allMessages.addAll(0,aMessage.getPlayersUniqueMessages(this, aGalaxy));
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

}
