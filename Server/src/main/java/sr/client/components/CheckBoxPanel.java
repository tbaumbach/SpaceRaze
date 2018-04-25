/*
 * Created on 2005-jan-15
 */
package sr.client.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import sr.client.StyleGuide;

/**
 * @author WMPABOD
 *
 * Custom checkbox with SpaceRaze looks
 */
@SuppressWarnings("serial")
public class CheckBoxPanel extends JPanel implements MouseListener,ItemSelectable{
	private boolean checked = false;
	private boolean enabled = true;
	private String text;
	private ActionListener al;
	private ItemListener il;
	private Color customTextColor;
	
	public CheckBoxPanel(String text){
		this.text = text;
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g){
		if (g != null){
		  int x = 0; 
		  int y = 2;
	      // paint background
	      g.setColor(StyleGuide.colorBackground);
	      g.fillRect(0,0,getSize().width,getSize().height);
	      // set color
	      if (enabled){
	      	g.setColor(StyleGuide.colorCurrent);
	      }else{
	      	g.setColor(StyleGuide.colorCurrent.darker().darker());
	      }
	      // draw square
	      g.drawRect(0+x,0+y,12,12);
	      if (checked){
	      	// draw check
	      	g.drawLine(0+x,3+y,8+x,12+y);
	      	g.drawLine(0+x,4+y,7+x,12+y);
	      	g.drawLine(0+x,5+y,6+x,12+y);
	      	g.drawLine(0+x,6+y,5+x,12+y);
	      	g.drawLine(4+x,11+y,9+x,1+y);
	      	g.drawLine(5+x,11+y,10+x,1+y);
	      	g.drawLine(6+x,11+y,11+x,1+y);
	      	g.drawLine(7+x,11+y,12+x,1+y);
	      }
	      // draw text
	      if (customTextColor != null){
	    	  if (enabled){
	    		  g.setColor(customTextColor);
	    	  }else{
	    		  g.setColor(customTextColor.darker().darker());
	    	  }
	      }
	      g.drawString(text,18+x,11+y);
		}
	}
	
	public void setSelected(boolean selected){
		checked = selected;
		paintComponent(getGraphics());
	}
	
	public boolean isSelected(){
		return checked;
	}

	public void setEnabled(boolean enabled){
		this.enabled = enabled;
		paintComponent(getGraphics());
	}
	
	public void setText(String newText){
		this.text = newText;
	}

	public String getText(){
		return text;
	}

	/**
	 * This is a setter, not an adder ;-)
	 * @param al
	 */
	public void addActionListener(ActionListener al){
		this.al = al;
	}

	/**
	 * This is a setter, not an adder ;-)
	 * @param il
	 */
	public void addItemListener(ItemListener il){
		this.il = il;
	}

	public void mousePressed(MouseEvent arg0) {
		if (enabled){
			checked = !checked;
			paintComponent(getGraphics());
			if (al != null){
				al.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,text));
			}
			if (il != null){
				int state = ItemEvent.DESELECTED;
				if (checked){
					state = ItemEvent.SELECTED;
				}
				il.itemStateChanged(new ItemEvent(this,ItemEvent.ITEM_STATE_CHANGED,this,state));
			}
		}
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void removeItemListener(ItemListener arg0) {}
	public Object[] getSelectedObjects() {return null;}

	public void setCustomTextColor(Color customTextColor) {
		this.customTextColor = customTextColor;
	}

}
