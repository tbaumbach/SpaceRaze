package sr.client;

import java.awt.Font;

import javax.swing.border.LineBorder;

import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: SpaceRaze with Swing GUI</p>
 * @author Paul Bodin
 * @version 3.0
 */

public class GeneralMessagePanel extends SRBasePanel {
	private static final long serialVersionUID = 1L;
	private SRLabel titleLabel,messageLabel;

	public GeneralMessagePanel(String title, String message) {
		setLayout(null);
		setBackground(StyleGuide.colorBackground);
		setForeground(StyleGuide.colorCurrent);
		setBorder(new LineBorder(StyleGuide.colorCurrent));
		
		titleLabel = new SRLabel(title);
		titleLabel.setBounds(20,160,400,20);
		titleLabel.setFont(new Font("Helvetica",1,14));
		add(titleLabel);
		
		messageLabel = new SRLabel(message);
		messageLabel.setBounds(20,200,550,20);
		add(messageLabel);
	}

}