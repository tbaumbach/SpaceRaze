/*
 * Created on 2005-jan-02
 */
package sr.client.components.scrollable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import sr.client.StyleGuide;
import sr.client.interfaces.ScrollListListener;

/**
 * @author WMPABOD
 *
 * Implements a vertical scolllist
 */
@SuppressWarnings("serial")
public class Scroller extends JPanel implements MouseListener, MouseMotionListener {
	private ScrollListListener scrollListListener;
	private boolean enabled = false;
	private Color backgroundColor = StyleGuide.colorBackground;
	private Color arrowActiveColor = StyleGuide.colorCurrent;
	private Color arrowInactiveColor = StyleGuide.colorCurrent.darker().darker();
	private Color arrowInactiveEdgeColor = StyleGuide.colorCurrent.darker().darker();
	private Color sliderEdgeColor = StyleGuide.colorCurrent;
	private Color sliderBackgroundColor = StyleGuide.colorCurrent.darker();
	private Color railColor = StyleGuide.colorCurrent;
	private double handlePosition = 0.0d;
	private double handleSize = 1.0d;
	private int oldY = -1;
	private boolean dragging = false;

	public Scroller(){
	    this.addMouseListener(this);
	    this.addMouseMotionListener(this);
	}
	
	public void setForeground(Color fg) {
		super.setForeground(fg);
		arrowActiveColor = fg;
		arrowInactiveColor = fg.darker().darker();
		arrowInactiveEdgeColor = fg.darker().darker();
		sliderEdgeColor = fg;
		sliderBackgroundColor = fg.darker();
		railColor = fg;
		repaint();
    }
	
	public void paintComponent(Graphics g){
		Dimension d = getSize();
		Image bufferImage = createImage(d.width,d.height);
	    Graphics bg = bufferImage.getGraphics();
	    drawAll(bg);
	    g.drawImage(bufferImage,0,0,this);
	}

	  public void drawAll(Graphics g){
	    g.setColor(StyleGuide.colorBackground);
	    g.fillRect(0,0,this.getSize().width,this.getSize().height);
	    if (enabled){
	    	g.setColor(railColor);
	    	g.drawLine(5,0,5,this.getSize().height);
	    	drawArrows(g);
	    	drawHandle(g);
	    }else{
	    	drawDisabled(g);
	    }
	  }
	  
	  private void drawDisabled(Graphics g){
    	g.setColor(arrowInactiveColor);
    	g.drawLine(5,0,5,this.getSize().height);
	  	drawArrow(g,true,0,0);
	  	int y = this.getSize().height-8;
	  	drawArrow(g,false,0,y);
	  }

	  private void drawHandle(Graphics g){
	    int x = 0;
	    int y = 0;
	    int height = 11;
	    int width = 11;

	      int spaceHeight = this.getSize().height-18;
	      x = 0;
	      y = ((int)Math.round(spaceHeight*handlePosition)) + 9;
//	System.out.print(" drawHandle, y: " + y);
	      width = 10;
	      height = (int)Math.round(spaceHeight*handleSize) - 2;

	    // rita ut basrutan av handtaget
	    g.setColor(sliderBackgroundColor);
	    g.fillRect(x,y,width,height);
	    // rita ut tvärstrecken
	    g.setColor(railColor);

	      for(int i = 0; i < height/3; i++){
	        g.drawLine(2,(i*3) + y + 3,9,(i*3) + y + 3);
	      }

	    // rita ut den vita kanten av handtaget
	    g.setColor(sliderEdgeColor);
	    g.drawRect(x,y,width,height);
	  }

	  private void drawArrows(Graphics g){
	  	int x = 0;
	  	int y = 0;
	  	// rita ut den övre pilen
	  	if (handlePosition == 0.0d){
	  		g.setColor(arrowInactiveColor);
	  	}else{
	  		g.setColor(arrowActiveColor);
	  	}
	  	// rita ut den övre
	  	x = 0;
	  	y = 0;
	  	drawArrow(g,true,x,y);
	  	if (backgroundColor == Color.WHITE){
	  		g.setColor(arrowInactiveEdgeColor);
	  		drawArrowEdge(g,true,x,y);
	  	}
	  	// rita ut den nedre pilen
	  	if (handlePosition + handleSize > 0.9999d){
	  		g.setColor(arrowInactiveColor);
	  	}else{
	  		g.setColor(arrowActiveColor);
	  	}
	  	// rita ut den nedre
	  	x = 0;
	  	y = this.getSize().height-8;
	  	drawArrow(g,false,x,y);
	  	if (backgroundColor == Color.WHITE){
	  		g.setColor(arrowInactiveEdgeColor);
	  		drawArrowEdge(g,false,x,y);
	  	}	  	
	}

	private void drawArrowEdge(Graphics g, boolean highEnd, int x, int y){
		if (highEnd){
			int[] xCoors = {x+5,x+11,x+11,x+0,x+0};
			int[] yCoors = {y+0,y+5,y+7,y+7,y+5};
			g.drawPolygon(xCoors,yCoors,5);
		}else{
			int[] xCoors = {x+0,x+11,x+11,x+5,x+0};
			int[] yCoors = {y+0,y+0,y+2,y+7,y+2};
			g.drawPolygon(xCoors,yCoors,5);
		}
	}

	private void drawArrow(Graphics g, boolean highEnd, int x, int y){
		if (highEnd){
			// top
			int[] xCoors = {x+5,x+11,x+11,x+0,x+0};
			int[] yCoors = {y+0,y+5,y+7,y+7,y+5};
			g.fillPolygon(xCoors,yCoors,5);
		}else{
			// bottom
			int[] xCoors = {x+0,x+11,x+11,x+5,x+0};
			int[] yCoors = {y+0,y+0,y+2,y+7,y+2};
			g.fillPolygon(xCoors,yCoors,5);
		}
	}

	public void addScrollListListener(ScrollListListener scrollListListener){
		this.scrollListListener = scrollListListener;
	}

	public void setEnabled(boolean showScroller){
		enabled = showScroller;
	}
	
	/**
	 * Kontrollerar var "handtaget/slidern" är
	 * @param handlePosition Kan vara 0.0 - 1.0. Om exakt 0 eller 1 skall upp resp. ned-pilarna "gråas"
	 */
	public void setHandlePosition(double handlePosition){
		this.handlePosition = handlePosition;
	}
	
	/**
	 * Styr hur stor del av scrollerna längd handtaget skall uppta.
	 * @param handleSize Ska vara mellan 0.0-1.0. Avgör hur stor del av sliderns/handtaget utrymme slidern/handtaget ska uppta (1.0 = 100%)
	 */
	public void setHandleRatio(double handleSize){
		this.handleSize = handleSize;
	}
	
	public void mouseDragged(MouseEvent me) {
//	  System.out.println("mouseDragged: " + me);
	      if (enabled){
//	  System.out.print("enabled " );
	        if(dragging){
//	  System.out.print("dragging");
//	  System.out.print("vertical");
	            int diffY = me.getY() - oldY;
	            if(diffY > 0){ // om diffY > 0 så har musen dragits nedåt
	              if(handlePosition + handleSize < 0.9999d){ // handtaget är inte redan nederst
	                double tmpPosition = handlePosition + (1.0d * diffY) / (this.getSize().height - 18);
	                if((tmpPosition + handleSize) >= 1.0d){
	                  handlePosition = 1.0d - handleSize;
	                }else{
	                  handlePosition = tmpPosition;
	                }
	                scrollListListener.show(handlePosition);
	                paint(getGraphics());
	              }
	            }
	            else{
	              if(handlePosition > 0.0d){ // handtaget är inte redan överst
	                double tmpPosition = handlePosition + (1.0d * diffY) / (this.getSize().height - 18);
	                if(tmpPosition <= 0.0d){
	                  handlePosition = 0.0d;
	                }else{
	                  handlePosition = tmpPosition;
	                }
	                scrollListListener.show(handlePosition);
	                paint(getGraphics());
	              }
	          }
	        }
	        oldY = me.getY();
	      }
	    }

	    public void mouseMoved(MouseEvent me) {}

	    public void mouseClicked(MouseEvent me) {}

	    public void mouseEntered(MouseEvent me) {}

	    public void mouseExited(MouseEvent me) {}

	    public void mousePressed(MouseEvent me) {
//	  System.out.println("mousePressed: " + me);
	        if(enabled){
	            if(me.getY() < 8){ // kolla om y-coor uppe på övre pilen
	              if(handlePosition > 0.0d){ // Om övre pilen är aktiv
	                // anropa lyssnarens flyttametod
	                scrollListListener.move(true);
	              }
	            }else
	            if(me.getY() > (this.getSize().height - 8)){ // kolla om y-coor är nere på nedre pilen
	              if(handlePosition + handleSize < 0.9999d){
	                // anropa lyssnarens flyttametod
//	  System.out.println("handlePosition + handleSize < 0.9999d");
	                scrollListListener.move(false);
	              }
	            }else{
	              // kolla om musklicket är på handtaget
	              int spaceHeight = this.getSize().height - 18;
	              int tmpY = ((int)Math.round(spaceHeight * handlePosition)) + 9;
	              int tmpHeight = (int)Math.round(spaceHeight * handleSize) - 1;
	              if((me.getY() > tmpY) & (me.getY() < (tmpY + tmpHeight))){
	                dragging = true;
	                oldY = me.getY();
	              }
	            }
	      }
	    }

	    public void mouseReleased(MouseEvent me) {
//	  System.out.println("mouseReleased: " + me);
	      if (dragging){
	        dragging = false;
	        oldY = -1;
	      }
	    }

}
