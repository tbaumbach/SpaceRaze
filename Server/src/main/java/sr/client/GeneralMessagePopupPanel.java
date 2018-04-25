/*
 * Created on 2005-feb-25
 */
package sr.client;

import java.awt.event.ActionListener;

import sr.client.components.BasicPopupPanel;
import sr.client.components.SRLabel;

/**
 * @author wmpabod
 *
 * Popuppanel for simple messages
 */
@SuppressWarnings("serial")
public class GeneralMessagePopupPanel extends BasicPopupPanel {
    private SRLabel messageLabel;

	public GeneralMessagePopupPanel(String title, ActionListener listener, String message){
		this(title,listener);
		// set message text
		messageLabel.setText(message);
		
		// remove cancel button
		cancelBtn.setVisible(false);
	}
	
	private GeneralMessagePopupPanel(String title, ActionListener listener){
		super(title,listener);
		
		messageLabel = new SRLabel();
		messageLabel.setBounds(10,40,400,20);
	    add(messageLabel);
	}

	public GeneralMessagePopupPanel(String title, ActionListener listener, int width, String... messageRow){
		this(title,listener);
		
		int rowCounter = 1;
		for (String aMessageRow : messageRow) {
			messageLabel = new SRLabel(aMessageRow);
			messageLabel.setBounds(10,20 + rowCounter*20,width-20,20);
			messageLabel.setVisible(true);
			add(messageLabel);
			rowCounter++;
		}

		// remove cancel button
		cancelBtn.setVisible(false);
		// set size
		setPopupSize(width, 100 + messageRow.length*20);
	}

	public void setPopupSize(int width, int height){
		super.setPopupSize(width, height);
		messageLabel.setSize(width,20); // denna rad har endast effekt på enradsmeddelanden
		okBtn.setBounds((width/2)-50,height-35,100,20);
	}

}
