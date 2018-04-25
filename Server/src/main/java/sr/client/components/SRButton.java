package sr.client.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: SpaceRaze with Swing GUI</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRButton extends JButton implements MouseMotionListener, MouseListener{
	private int type = 0;
	private Color oldForeground;
	
  public SRButton() {
    this("");
  }

  public SRButton(String text) {
    super(text);
    setBorder(new LineBorder(StyleGuide.colorCurrent));
    setForeground(StyleGuide.colorCurrent);
    setBackground(StyleGuide.colorBackground);
    setFont(StyleGuide.buttonFont);
    addMouseMotionListener(this);
    addMouseListener(this);
    setContentAreaFilled( false );
	setOpaque(true);
  }
  
  public SRButton(String text,int iType) {
	    super(text);
	    setBorder(new LineBorder(StyleGuide.colorCurrent));
	    setForeground(StyleGuide.colorCurrent);
	    setBackground(StyleGuide.colorBackground);
	    setFont(StyleGuide.buttonFont);
	    addMouseMotionListener(this);
	    addMouseListener(this);
	    setContentAreaFilled( false );
		setOpaque(true);
		type=iType;
	  }

  public void setGreen(){
	  
	  	setBorder(new LineBorder(StyleGuide.colorGreenBorder));
	    setBackground(StyleGuide.colorGreenBackground9);
	    setForeground(StyleGuide.colorGreenForeground9);
	  }
  
  public void setNavButton1(){
	  
	  	setBorder(new LineBorder(StyleGuide.colorNAVBorder));
	    setBackground(StyleGuide.colorNAVBackground2);
	    setForeground(StyleGuide.colorNAVForeground2);
	  }
  public void setNavButton2(){
	  	setBorder(new LineBorder(StyleGuide.colorNAVBorder));
	    setBackground(StyleGuide.colorNAVBackground2);
	    setForeground(StyleGuide.colorNAVForeground2);
	  }
  public void setNavButton3(){
	  	setBorder(new LineBorder(StyleGuide.colorNAVBorder));
	    setBackground(StyleGuide.colorNAVBackground3);
	    setForeground(StyleGuide.colorNAVForeground3);
	  }
  public void setNavButton4(){
	  setBorder(new LineBorder(StyleGuide.colorNAVBorder));
	    setBackground(StyleGuide.colorNAVBackground4);
	    setForeground(StyleGuide.colorNAVForeground5);
	  }
  public void setNavButton5(){
	  setBorder(new LineBorder(StyleGuide.colorNAVBorder));
	    setBackground(StyleGuide.colorNAVBackground5);
	    setForeground(StyleGuide.colorNAVForeground5);
	  }
  public void setNavButton6(){
	  setBorder(new LineBorder(StyleGuide.colorNAVBorder));
	    setBackground(StyleGuide.colorNAVBackground6);
	    setForeground(StyleGuide.colorNAVForeground6);
	  }
  public void setNavButton7(){
	  setBorder(new LineBorder(StyleGuide.colorNAVBorder));
	    setBackground(StyleGuide.colorNAVBackground7);
	    setForeground(StyleGuide.colorNAVForeground7);
	  }
  public void setNavButton8(){
	  setBorder(new LineBorder(StyleGuide.colorNAVBorder));
	    setBackground(StyleGuide.colorNAVBackground8);
	    setForeground(StyleGuide.colorNAVForeground8);
	  }
  public void setNavButton9(){
	  setBorder(new LineBorder(StyleGuide.colorNAVBorder));
	    setBackground(StyleGuide.colorNAVBackground9);
	    setForeground(StyleGuide.colorNAVForeground9);
	  }
  
  public void mousePressed(MouseEvent me){
	  	if (type==1){
		    setBackground(StyleGuide.colorNAVBackground9.darker().darker().darker().darker());
		    setForeground(StyleGuide.colorNAVForeground9.darker());
  		}
	  	else if (type==1){
		    setBackground(StyleGuide.colorGreenBackground9.darker().darker().darker().darker());
		    setForeground(StyleGuide.colorGreenForeground9.darker());
  		}
  		else
  		{	
  			oldForeground = getForeground();
  			setBackground(getForeground().darker().darker().darker().darker());
    		setForeground(getForeground().darker());
  		}
  }

  public void mouseClicked(MouseEvent me){
  }

  public void mouseReleased(MouseEvent me){
    if (isEnabled()){
    	if (type==1){
    	    setBackground(StyleGuide.colorNAVBackground9);
    	    setForeground(StyleGuide.colorNAVForeground9);
    	}
    	else if (type==2){
    		setBackground(StyleGuide.colorGreenBackground9);
    	    setForeground(StyleGuide.colorGreenForeground9);
    	}
    	else
    	{	
    		setBackground(StyleGuide.colorBackground);
    		setForeground(oldForeground);
    	}
      update(getGraphics());
    }
  }

  public void mouseExited(MouseEvent me){
    if (isEnabled()){
    	if (type==1){
    		setBackground(StyleGuide.colorNAVBackground9);
    	}else if (type==2){
    		setBackground(StyleGuide.colorGreenBackground9);
    	}
    	
    	else
    	{	
    		setBackground(StyleGuide.colorBackground);
    	}
    }
  }

  public void mouseEntered(MouseEvent me){
    if (isEnabled()){
    	if (type==1){
    		setBackground(StyleGuide.colorNAVBackground9.darker().darker().darker());
    	}
    	else if (type==2){
    		setBackground(StyleGuide.colorGreenBackground9.darker().darker().darker());
    	}
    	else
    	{	
//    		setBackground(StyleGuide.colorCurrent.darker().darker().darker());
    		setBackground(getForeground().darker().darker().darker());
    	}
    }
  }

  public void mouseDragged(MouseEvent me){
  }

  public void mouseMoved(MouseEvent me){
  }

  public void setEnabled(boolean enabled){
	  
    super.setEnabled(enabled);
    if (!enabled){
    	if (type==2){
    		   setBackground(StyleGuide.colorGreenBackground9.darker().darker().darker().darker());
    		    setForeground(StyleGuide.colorGreenForeground9.darker());
    	}
    	else{
    	      setBackground(StyleGuide.colorCurrent.darker().darker().darker().darker());
    	      setForeground(StyleGuide.colorCurrent.darker());
    	}
    }else{
    	if (type==2){
    		   setBackground(StyleGuide.colorGreenBackground9);
    		    setForeground(StyleGuide.colorGreenForeground9);
    	}
    	else{
    	      setForeground(StyleGuide.colorCurrent);
    	      setBackground(StyleGuide.colorBackground);	
    	}
    }
  }

  public void paint(Graphics g){
  	if (g != null){
  		super.paint(g);
  		Dimension d = getSize();
  		g.setColor(getBackground());
  		g.fillRect(1,1,d.width-2,d.height-2);
  		g.setColor(getForeground());
  		FontMetrics fm = getFontMetrics(getFont());
  		int xoffset = (d.width - fm.stringWidth(getText()))/2;
  		int yoffset = (d.height - fm.getHeight() - fm.getMaxAscent())/2 + fm.getHeight() + 2;
  		g.drawString(getText(),xoffset,yoffset);
  	}
  }


}