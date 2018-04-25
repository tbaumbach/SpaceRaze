/*
 * Created on 2005-jun-18
 */
package sr.client;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import sr.server.SR_Server;

/**
 * @author WMPABOD
 *
 * Shows SpacesRaze map Editor title/emblem in upper left corner
 */
public class EmblemPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public EmblemPanel(){
		setBorder(new LineBorder(StyleGuide.colorCurrent));
		setBackground(StyleGuide.colorBackground);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(StyleGuide.colorCurrent.darker().darker());
		g.fillRect(0,0,getSize().width,getSize().height);
		g.setColor(StyleGuide.colorCurrent);
		g.setFont(new Font("Monospaced",1,20));
		g.drawString("SpaceRaze",6,29);
		String version = "v " + SR_Server.version;
		Font tmpFont = new Font("Monospaced",0,10);
		FontMetrics fm = g.getFontMetrics(tmpFont);
		g.setFont(tmpFont);
		int width = fm.stringWidth(version);
		g.drawString(version,117-width,41);
	}

}
