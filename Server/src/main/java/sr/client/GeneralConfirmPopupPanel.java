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
 * Popuppanel for simple messages and confirm buttons (cancel/ok)
 */
@SuppressWarnings("serial")
public class GeneralConfirmPopupPanel extends BasicPopupPanel {
    private SRLabel messageLabel;

	public GeneralConfirmPopupPanel(String title, ActionListener listener, String message){
		this(title,listener);
		// set message text
		messageLabel.setText(message);
	}
	
	public GeneralConfirmPopupPanel(String title, ActionListener listener){
		super(title,listener);
		
		messageLabel = new SRLabel();
		messageLabel.setBounds(10,40,400,20);
	    add(messageLabel);
	}
	
	public void setPopupSize(int width, int height){
		super.setPopupSize(width, height);
		messageLabel.setSize(width,20);
	}

}
