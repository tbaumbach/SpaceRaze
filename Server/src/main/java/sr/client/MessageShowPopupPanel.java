/*
 * Created on 2005-feb-25
 */
package sr.client;

import java.awt.event.ActionListener;

import javax.swing.JScrollPane;

import sr.client.components.BasicPopupPanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.world.Galaxy;
import sr.world.Message;

/**
 * @author wmpabod
 *
 * Popuppanel used for showing old messages
 */
public class MessageShowPopupPanel extends BasicPopupPanel {
	private static final long serialVersionUID = 1L;
	private SRLabel messageTextLabel, messageRecipientLabel, messageRecipientNameLabel;
    private SRLabel turnLabel, turn2Label;
    private SRLabel senderLabel, sender2Label;
    private SRTextArea contentArea;
    private JScrollPane scrollPane;

	public MessageShowPopupPanel(String title, ActionListener listener, Message aMessage, Galaxy aGalaxy){
		super(title,listener);
		okBtn.setText("Close");
		cancelBtn.setVisible(false);
		
	    turnLabel = new SRLabel("Turn:"); // change to a better label text?
	    turnLabel.setBounds(10,40,100,20);
	    add(turnLabel);

	    turn2Label = new SRLabel(String.valueOf(aMessage.getTurn()));
	    turn2Label.setBounds(130,40,300,20);
	    add(turn2Label);

	    senderLabel = new SRLabel("Sender:"); // change to a better label text?
	    senderLabel.setBounds(10,70,100,20);
	    add(senderLabel);

	    String senderText = null;
	    if (aMessage.getSender(aGalaxy) != null){
	    	senderText = "Governor " + aMessage.getSender(aGalaxy).getGovenorName(); 
	    }else{
	    	senderText = aMessage.getSender();
	    }
	    sender2Label = new SRLabel(senderText);
	    sender2Label.setBounds(130,70,300,20);
	    add(sender2Label);

	    messageRecipientLabel = new SRLabel("Recipient:"); // change to a better label text?
	    messageRecipientLabel.setBounds(10,100,100,20);
	    add(messageRecipientLabel);

	    messageRecipientNameLabel = new SRLabel(aMessage.getRecipientString(aGalaxy));
	    messageRecipientNameLabel.setBounds(130,100,300,20);
	    add(messageRecipientNameLabel);

	    messageTextLabel = new SRLabel("Message text:");
	    messageTextLabel.setBounds(10,130,100,20);
	    add(messageTextLabel);

	    contentArea = new SRTextArea(aMessage.getContent());
	    contentArea.setEditable(false);
	    
	    add(contentArea);

	    scrollPane = new SRScrollPane(contentArea);
	    scrollPane.setBounds(10,150,675,460);
	    add(scrollPane);
	    
	    contentArea.setCaretPosition(0);
	}

	public void setButtonLocation(){
		okBtn.setLocation(560,620);
	}
	
}
