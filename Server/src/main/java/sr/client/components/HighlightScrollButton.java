/*
 * Created on 2005-jan-18
 */
package sr.client.components;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import sr.client.HighlightsPanel;
import sr.client.StyleGuide;

/**
 * @author WMPABOD
 *
 * Up and down buttons for highlights list
 */
@SuppressWarnings("serial")
public class HighlightScrollButton extends JPanel implements MouseListener{
	private boolean up, enabled = true;
	private HighlightsPanel hlp;
	
	public HighlightScrollButton(boolean up, HighlightsPanel hlp){
		this.up = up;
		this.hlp = hlp;
		
//		setBorder(new LineBorder(StyleGuide.colorCurrent,1));

		this.addMouseListener(this);
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}

	public void paintComponent(Graphics g){
		g.setColor(StyleGuide.colorBackground);
		g.fillRect(0,0,getSize().width,getSize().height);
		if (enabled){
			g.setColor(StyleGuide.colorCurrent);
		}else{
			g.setColor(StyleGuide.colorCurrent.darker().darker());
		}
		Polygon p = new Polygon();
		int width = getSize().width;
		int height = getSize().height;
		if (up){
			p.addPoint(0,height);
			p.addPoint(width/2,0);
			p.addPoint(width,height);
		}else{
			p.addPoint(0,0);
			p.addPoint(width/2,height);
			p.addPoint(width,0);
		}
		g.fillPolygon(p);
	}

	public void mousePressed(MouseEvent arg0) {
		if (enabled){
			hlp.move(up);
		}
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

}
