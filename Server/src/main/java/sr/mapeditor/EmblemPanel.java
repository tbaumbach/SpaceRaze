/*
 * Created on 2005-jun-18
 */
package sr.mapeditor;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;

/**
 * @author WMPABOD
 *
 * Shows SpacesRaze map Editor title/emblem in upper left corner
 */
@SuppressWarnings("serial")
public class EmblemPanel extends JPanel {
	
	public EmblemPanel(){
		setBorder(new LineBorder(StyleGuide.colorNeutral));
		setBackground(StyleGuide.colorBackground);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(StyleGuide.colorNeutral.darker().darker());
		g.fillRect(0,0,getSize().width,getSize().height);
		g.setColor(StyleGuide.colorNeutral);
		g.setFont(new Font("Monospaced",0,21));
		g.drawString("SpaceRaze",5,30);
		g.setFont(new Font("Monospaced",0,55));
		g.drawString("Map",16,75);
		g.setFont(new Font("Monospaced",0,32));
		g.drawString("Editor",5,110);
	}

}
