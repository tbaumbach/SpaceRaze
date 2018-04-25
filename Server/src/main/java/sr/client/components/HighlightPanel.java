/*
 * Created on 2005-jan-17
 */
package sr.client.components;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;
import sr.world.Highlight;

/**
 * @author WMPABOD
 *
 * This panel displays a specific highlight item
 */
@SuppressWarnings("serial")
public class HighlightPanel extends JPanel {
	private Highlight hl;

	public HighlightPanel(Highlight hl){
		this.hl = hl;
		setBorder(new LineBorder(StyleGuide.colorCurrent,1));
	}
	
	public void paintComponent(Graphics g){
		g.setColor(StyleGuide.colorBackground);
		g.fillRect(0,0,getSize().width,getSize().height);
		g.setColor(StyleGuide.colorCurrent);
//		g.setFont(new Font("SansSerif",0,15));
		g.setFont(new Font("SansSerif",0,12));
//		g.drawString(hl.getMessage(),5,21);
		g.drawString(hl.getMessage(),5,20);
	}
}
