package sr.client;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.logging.Logger;
import sr.server.properties.PropertiesHandler;
import sr.tunnel.TransferWrapper;
import sr.world.Faction;
import sr.world.Galaxy;
import sr.world.Message;
import sr.world.Player;

/**
 * @author Paul Bodin
 * 
 *  New version of the messagepanel using a modal popup
 */

public class MessagePanel2 extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel, ActionListener, Runnable{
	private static final long serialVersionUID = 1L;
	private SRButton newButton,showRecievedButton,showSentButton;
	private SRLabel sentMessagesLbl,newMessagesLbl,recievedMessagesLbl;
	private String id;
	private Player p;
	private SpaceRazeApplet client;
	private ListPanel allMessagesList,recievedMessagesList,sentMessagesList;
	private MessagePopupPanel popup;
	private MessageShowPopupPanel popupShow;
//	private final String DELIMITER = "-";
	private List<String> originalRecipientStrings;
	private List<Message> sentMessages,recievedMessages;
	private Thread aThread;

  public MessagePanel2(Player p,SpaceRazeApplet client, String id) {
    this.p = p;
    this.client = client;
    this.id = id;
    
    originalRecipientStrings = new Vector<String>();
    sentMessages = client.getSentMessage();
    recievedMessages = client.getReceivedMessage();
    
    int buttonX = 640;
    int buttomWidth = 120;
    int listWidth = 620;
    
    // New messages / recipients list
    // ------------------------------
    
    newMessagesLbl = new SRLabel("Send new messages:");
    newMessagesLbl.setBounds(10,15,200,20);
    add(newMessagesLbl);
    
    allMessagesList = new ListPanel();
    allMessagesList.setBounds(10, 40, listWidth, 170);
    allMessagesList.setListSelectionListener(this);
    addRecipients(p.getGalaxy());
    allMessagesList.updateScrollList();
    add(allMessagesList);

    newButton = new SRButton("Create Message");
    newButton.setBounds(buttonX,40,buttomWidth,20);
    newButton.addActionListener(this);
    add(newButton);
    newButton.setEnabled(false);

    
    // Recieved messages
    // -----------------
    
    recievedMessagesLbl = new SRLabel("Recieved messages:");
    recievedMessagesLbl.setBounds(10,225,200,20);
    add(recievedMessagesLbl);

    recievedMessagesList = new ListPanel();
    recievedMessagesList.setBounds(10, 250, listWidth, 170);
    recievedMessagesList.setListSelectionListener(this);
    addRecievedMessages(recievedMessages, p.getGalaxy());
    recievedMessagesList.updateScrollList();
    add(recievedMessagesList);

    showRecievedButton = new SRButton("Show Message");
    showRecievedButton.setBounds(buttonX,250,buttomWidth,20);
    showRecievedButton.addActionListener(this);
    add(showRecievedButton);
    showRecievedButton.setEnabled(false);

    // Sent messages
    // -------------
    
    sentMessagesLbl = new SRLabel("Sent messages:");
    sentMessagesLbl.setBounds(10,435,200,20);
    add(sentMessagesLbl);

    sentMessagesList = new ListPanel();
    sentMessagesList.setBounds(10, 460, listWidth, 170);
    sentMessagesList.setListSelectionListener(this);
    addSentMessages();
    sentMessagesList.updateScrollList();
    add(sentMessagesList);

    showSentButton = new SRButton("Show Message");
    showSentButton.setBounds(buttonX,460,buttomWidth,20);
    showSentButton.addActionListener(this);
    add(showSentButton);
    showSentButton.setEnabled(false);
    
    aThread = new Thread(this);
    aThread.start();
  }
  
  /*
  private String getRecipientRow(String recipient){
    String tmp = recipient;
    String messageTmp = getMessage(tmp);
    if (!messageTmp.equalsIgnoreCase("")){
    	if (messageTmp.length() > 52){
    		messageTmp = messageTmp.substring(0,50) + "...";
    	}
    	tmp = tmp + " - " + messageTmp;
    }
    return tmp;
  }*/

  private void addRecipients(Galaxy g){
    DefaultListModel dlm = (DefaultListModel)allMessagesList.getModel();
    dlm.addElement("Public Statement");
    originalRecipientStrings.add("Public Statement");
	for (int i = 0; i < g.getFactions().size(); i++){
      Faction tempFaction = (Faction)g.getFactions().get(i);
      int tempNr = g.getFactionLivingMemberNr(tempFaction);
      if(tempFaction == p.getFaction()){
    	  tempNr--;
      }
      if(tempNr > 0){
	      dlm.addElement("All " + tempFaction.getName() + " governors");
	      originalRecipientStrings.add("All " + tempFaction.getName() + " governors");
      }
    }
    for (int i = 0; i < g.getPlayers().size(); i++){
      Player tempPlayer = (Player)g.getPlayers().get(i);
      if ((!tempPlayer.isDefeated()) & (tempPlayer != p)){
      	dlm.addElement("Governor " + tempPlayer.getGovenorName() + " (" + tempPlayer.getFaction().getName() + ")");
      	originalRecipientStrings.add("Governor " + tempPlayer.getGovenorName() + " (" + tempPlayer.getFaction().getName() + ")");
      }
    }
  }
  
  public void valueChanged(ListSelectionEvent lse){
	  if (lse.getSource() == allMessagesList && !p.isDefeated()){
		  newButton.setEnabled(true);
		  if (lse.getFirstIndex() > 1){ // assume doubleclick
		    	openPopup("Create Message");
		  }
	  }else
	  if (lse.getSource() == recievedMessagesList){
		  showRecievedButton.setEnabled(true);
		  if (lse.getFirstIndex() > 1){ // assume doubleclick
			  Message tmpMessage = recievedMessages.get(recievedMessagesList.getSelectedIndex());
			  openShowPopup(tmpMessage);
		  }
	  }else{ // must be sentMessagesList
		  showSentButton.setEnabled(true);
		  if (lse.getFirstIndex() > 1){ // assume doubleclick
			  Message tmpMessage = sentMessages.get(sentMessagesList.getSelectedIndex());
			  openShowPopup(tmpMessage);
		  }
	  }
  }
  
  public void actionPerformed(ActionEvent ae){
  	Logger.fine("actionPerformed: " + ae.getActionCommand() + " " + ae.getSource().getClass().getName());
  	String action = ae.getActionCommand();
  	if (ae.getSource() == showSentButton){
  		Message tmpMessage = sentMessages.get(sentMessagesList.getSelectedIndex());
  		openShowPopup(tmpMessage);
  	}else
  	if (ae.getSource() == showRecievedButton){
  		Message tmpMessage = recievedMessages.get(recievedMessagesList.getSelectedIndex());
  		openShowPopup(tmpMessage);
  	}else
  	if (action.equalsIgnoreCase("close")){
  		// do nothing
  	}else
  	if (action.equalsIgnoreCase("cancel")){
  		// do nothing
  	}else
    if (action.equalsIgnoreCase("ok")){
    	// perform action
    	performPopupAction();
  	}else{
    	openPopup(action);
  	}
  }
 
  /*
  private void deleteMessage(){
  	LoggingHandler.fine("deletemessage called");

  	// set message to "" to remove it...
  	setMessage("");

  	updateMessagePanel();
  }*/

  private void performPopupAction(){
  	Logger.fine("performPopupAction called");

  	setMessage(popup.getMessageText());

  	updateMessagePanel();
}
  
  /**
   * Also used for removing messages by setting text to ""
   *
   */
  private void setMessage(String messageText){
	  Logger.finest("setMessage called: messageText=" + messageText);
	  Message tempMessage = null;
	  String selectedString = allMessagesList.getSelectedItem();
	  Logger.finest("selectedString=" + selectedString);
	  int firstSpaceIndex = selectedString.indexOf(" ");
	  String firstWord = selectedString.substring(0,firstSpaceIndex);
	  if (firstWord.equalsIgnoreCase("Public")){  // skicka till alla spelare
		  Logger.finest("firstWord.equalsIgnoreCase(\"Public\")");
		  tempMessage = new Message(messageText,null,p);
	  }else
	  if (firstWord.equalsIgnoreCase("All")){  // skicka till alla spelare i en viss faction
		  Logger.finest("firstWord.equalsIgnoreCase(\"All\")");
		  int secondSpaceIndex = selectedString.indexOf(" governors",firstSpaceIndex+1);
		  Faction recipient = p.getGalaxy().getFaction(selectedString.substring(firstSpaceIndex+1,secondSpaceIndex));
		  tempMessage = new Message(messageText,recipient,p);
	  }else
	  if (firstWord.equalsIgnoreCase("Governor")){  // skicka till en separat spelare
		  Logger.finest("firstWord.equalsIgnoreCase(\"Governor\")");
		  int secondSpaceIndex = selectedString.indexOf("(") - 1;
		  Player recipient = p.getGalaxy().getPlayerByGovenorName(selectedString.substring(firstSpaceIndex+1,secondSpaceIndex));
		  tempMessage = new Message(messageText,recipient,p);
	  }
	  
	  // call addMessge and add message to sent message.
	  if(tempMessage != null){
		  addMessage(tempMessage);
	  }
	  
  }

  private void updateMessagePanel(){
  	emptyList();
    
	addSentMessages();  
	addRecievedMessages(recievedMessages, p.getGalaxy()); 
	  
  	update(getGraphics());
  	Graphics aGraphics = sentMessagesList.getGraphics();
  	if(aGraphics != null){
  		sentMessagesList.update(aGraphics);
  	}
  	
  	aGraphics = recievedMessagesList.getGraphics();
  	if(aGraphics != null){
  		recievedMessagesList.update(aGraphics);
  	}
  	
  	showNewMailImage();
  	
  }
  
  private void showNewMailImage(){
	  boolean haveNew = false;
	  for (Message aMessage : recievedMessages) {
		if(!aMessage.isRead()){
			haveNew = true;
		}
	  }
	  client.showNewMailImage(haveNew);
  }
  
  private void openShowPopup(Message aMessage){
	  Logger.fine("openShowPopup called: " + aMessage);
	  popupShow = new MessageShowPopupPanel("Show Message",this,aMessage, p.getGalaxy());
	  popupShow.setPopupSize(700,650);
	  popupShow.setButtonLocation();
	  popupShow.open(this);
	  
	  if(!aMessage.isRead()){
		  aMessage.setRead(true);
		  setMessagesRead(aMessage.getUniqueId());
		  
		//  DefaultListModel dlm = (DefaultListModel)recievedMessagesList.getModel();
		//  int index = recievedMessagesList.getSelectedIndex();
		//  dlm.remove(index);
		//  dlm.add(index, aMessage.getRecievedMessageListString(p.getGalaxy()));
		  
	  }
	  
  }

  private void openPopup(String actionCommand){
  	Logger.fine("openPopup called: " + actionCommand);
    String recipientString = (String)originalRecipientStrings.get(allMessagesList.getSelectedIndex());

    popup = new MessagePopupPanel(actionCommand,this,recipientString,p);
    popup.setPopupSize(700,650);
	popup.open(this);
	popup.setSumfieldFocus();
  }
  /*
  private String getMessage(String selectedString){
  	String messageText = "";
    int firstSpaceIndex = selectedString.indexOf(" ");
    String firstWord = selectedString.substring(0,firstSpaceIndex);
    Message tempm = null;
    if (firstWord.equalsIgnoreCase("Public")){  // hämta meddelande till alla spelare
      tempm = p.getMessageTo(null);
      if (tempm != null){
      	messageText = tempm.getContent();
      }
    }else
    if (firstWord.equalsIgnoreCase("All")){  // hämta meddelande till alla spelare i en viss faction
      int secondSpaceIndex = selectedString.indexOf(" governors",firstSpaceIndex+1);
      Faction recipient = p.getGalaxy().getFaction(selectedString.substring(firstSpaceIndex+1,secondSpaceIndex));
      tempm = p.getMessageTo(recipient);
      if (tempm != null){
      	messageText = tempm.getContent();
      }
    }else
    if (firstWord.equalsIgnoreCase("Governor")){  // hämta meddelande till en separat spelare
      int secondSpaceIndex = selectedString.indexOf("(") - 1;
      Player recipient = p.getGalaxy().getPlayerByGovenorName(selectedString.substring(firstSpaceIndex+1,secondSpaceIndex));
      tempm = p.getMessageTo(recipient);
      if (tempm != null){
        messageText = tempm.getContent();
      }
    }
    return messageText;
  }*/

  private void emptyList(){
    DefaultListModel dlm = (DefaultListModel)recievedMessagesList.getModel();
    dlm.removeAllElements();
    
    dlm = (DefaultListModel)sentMessagesList.getModel();
    dlm.removeAllElements();
    
  }

  /**
   * Searches messages list for all messages sent by current player
   * and adds them to sentMessages
   */
  /*
  private List<Message> getAllSentMessages(List<Message> messages){
	  List<Message> tmpList = new ArrayList<Message>();
	  for (Message aMessage : messages) {
		  if (aMessage.getSender(p.getGalaxy()).equals(p)){
			  tmpList.add(aMessage);
		  }
	  }
	  Collections.sort(tmpList);
	  return tmpList;
  }
  */
  /**
   * Searches messages list for all messages recieved by current player
   * and adds them to sentMessages
   */
  /*
  private List<Message> getAllRecievedMessages(List<Message> messages){
	  List<Message> tmpList = new ArrayList<Message>();
	  for (Message aMessage : messages) {
		  if (aMessage.getSender(p.getGalaxy()) != p){
			  if (aMessage.getType().equalsIgnoreCase("all")){
				  tmpList.add(aMessage);
			  }else
			  if (aMessage.getType().equalsIgnoreCase("private")){
				  if (aMessage.getRecipientPlayer(p.getGalaxy()).equals(p)){
					  tmpList.add(aMessage);
				  }
			  }else{ // type == "faction"
				  if (aMessage.getRecipientFaction().equals(p.getFaction())){
					  tmpList.add(aMessage);
				  }
			  }
		  }
	  }
	  Collections.sort(tmpList);
	  return tmpList;
  }*/
  
  /**
   * Adds all messages in recievedMessages to the recievedMessagesList
   * component
   */
  private void addRecievedMessages(List<Message> inList, Galaxy aGalaxy){
	  DefaultListModel dlm = (DefaultListModel)recievedMessagesList.getModel();
	  for (Message aMessage : inList) {
		  dlm.addElement(aMessage.getRecievedMessageListString(aGalaxy));
	  }
  }
  
  /**
   * Adds all messages in sentMessages to the sentMessagesList
   * component
   */
  private void addSentMessages(){
	  DefaultListModel dlm = (DefaultListModel)sentMessagesList.getModel();
	  for (Message aMessage : sentMessages) {
		  dlm.addElement(aMessage.getSentMessageListString(p.getGalaxy()));
	  }
  }

  public String getId(){
    return id;
  }

  public void updateData(){
  }
  
  @SuppressWarnings("unchecked")
  private void getPlayerNewMessages(){
	  //Logger.info("getPlayerNewMessages new called: ");
	  p.setLatestMessageIdFromServer(getLatestMessageIdFromServer());
	  TransferWrapper tw = client.getResponse(p,"getPlayerNewMessages",null, null);
	  List<Message> returnMessage = (List<Message>)tw.getReturnObject();
	  if(returnMessage.size() > 0){
		  recievedMessages.addAll(0,returnMessage);
		  updateMessagePanel();
	  }	  
  }
  
  @SuppressWarnings("unchecked")
  private void setMessagesRead(int messageId){
	  Logger.info("setMessagesRead new called: ");
	  p.setMessageId(messageId);
	  p.setLatestMessageIdFromServer(getLatestMessageIdFromServer());
	  TransferWrapper tw = client.getResponse(p,"setMessagesRead",null, null);
	  List<Message> returnMessage = (List<Message>)tw.getReturnObject(); // fungerar detta?
	  if(returnMessage.size() > 0){
		  recievedMessages.addAll(0,returnMessage);
	  }
	  updateMessagePanel();
  }
  
  @SuppressWarnings("unchecked")
  private void addMessage(Message aMessage){
	  Logger.info("addMessage new called: ");
	  DefaultListModel dlm = (DefaultListModel)sentMessagesList.getModel();
	  dlm.add(0,aMessage.getSentMessageListString(p.getGalaxy()));
	  sentMessages.add(0, aMessage);
	  p.setLatestMessageIdFromServer(getLatestMessageIdFromServer());
	  TransferWrapper tw = client.getResponse(p,"addMessage",null, aMessage);
	  List<Message> returnMessage = (List<Message>)tw.getReturnObject(); // fungerar detta?
	  if(returnMessage.size() > 0){
		  recievedMessages.addAll(0,returnMessage);
	  }
	  updateMessagePanel();
  }
  
  
  
  private int getLatestMessageIdFromServer(){
	  int tempMassageId=0;
	  if(recievedMessages.size() > 0){
		  tempMassageId = recievedMessages.get(0).getUniqueId();
	  }
	 
	  return tempMassageId;
  }
  
  /**
	 * Perform infinite loop with pause, and update status
	 */
	public void run(){
		int messageSleepTime = 10000; // sleep for 10 sec
		String messageSleepTimeProperty = null;
		if (client.isRunAsApplication()){
			messageSleepTimeProperty = client.getParameter("messagesleeptime");
	  	}else{
			messageSleepTimeProperty = PropertiesHandler.getProperty("messagesleeptime");
	  	}
//		messageSleepTimeProperty = PropertiesHandler.getProperty("messagesleeptime");
		if ((messageSleepTimeProperty != null) && (messageSleepTimeProperty.length() > 0)){
			try{
				int tmpSleepTime = Integer.parseInt(messageSleepTimeProperty);
				messageSleepTime = tmpSleepTime;
				Logger.info("Message sleep time set to " + tmpSleepTime + " millis");
			} catch(NumberFormatException nfe){
				Logger.info("spaceraze.properties messagesleeptime property does not contain an integer");
			}
		}else{
			Logger.info("spaceraze.properties does not contain messagesleeptime property, defaults to " + messageSleepTime + " millis");
		}
		while(true){
			getPlayerNewMessages();
			try {
				// sleep before next update
				Thread.sleep(messageSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}

	public void showFirstUnreadMessage() {
		Logger.info("Open first message");
		Message firstUnreadMessage = null;
		int index = 0;
		for (Message message : recievedMessages) {
			if(!message.isRead()){
				if (firstUnreadMessage == null){
					firstUnreadMessage = message;
				}
			}
			index++;
		}
		recievedMessagesList.setSelected(index);
		openShowPopup(firstUnreadMessage);
	}

}