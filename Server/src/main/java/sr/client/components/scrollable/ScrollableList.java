/*
 * Created on 2005-jan-02
 */
package sr.client.components.scrollable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;

import sr.client.StyleGuide;
import sr.client.components.SRBasePanel;
import sr.client.interfaces.ScrollListListener;
import sr.general.logging.Logger;

/**
 * @author WMPABOD
 *
 * A custom list that is coupled with a ScrollList in a ListPanel 
 * 
 * If none is selected, any click will set selected and create a multipleselected list
 * If ctrl-click, that item will be removed/deleted from mult-sel list
 * If shift-click, only items from selected to will become selected, and all else deselected
 */
@SuppressWarnings("serial")
public class ScrollableList extends SRBasePanel implements ScrollListListener, MouseListener, KeyListener{
	private DefaultListModel model;
	private int selected = -1;
	private List<Integer> multipleSelected = new ArrayList<Integer>(); // if multiple selects, contain selected Integers
	private int offsetY = 0;
	private final int baseY = 13;
	private final int baseX = 4;
	public static final int intervalY = 16;
	private Color rectColor = StyleGuide.colorCurrent.darker().darker().darker();
	private Color rect2Color = StyleGuide.colorCurrent.darker();
	private Color frontColor = StyleGuide.colorCurrent;
	private Scroller scroller;
	private ListPanel lp;
	private boolean multipleSelect = false;
	
	public ScrollableList(Scroller scroller, ListPanel lp){
		super(true);
		
		this.lp = lp;
		
		addMouseListener(this);
		addKeyListener(this);
		
		this.scroller = scroller;
		model = new DefaultListModel();
	}

	public void move(boolean up) {
//		System.out.println("move, up: " + (up==true) + " " + offsetY);
	    if (up){ // move up
	      int diffy = offsetY%intervalY;
	      if (diffy != 0){  // delar av ett rum visas
	        offsetY = offsetY - diffy;
	      }else{  // flytta upp ett helt rum
	        offsetY = offsetY + intervalY;
//	System.out.println("new offsetY: " + offsetY);
	      }
	    }else{ // move down
	      int diffy = offsetY%intervalY;
//System.out.println("down: " + "diffy: " + diffy + " offsetY: " + offsetY);
	      if (diffy != 0){  // delar av ett rum visas
	        offsetY = offsetY - diffy - intervalY;
//System.out.println("diffy != 0, new offsetY: " + offsetY);
	      }else{  // flytta ned ett helt rum
	        offsetY = offsetY - intervalY;
//System.out.println("diffy == 0, new offsetY: " + offsetY);
	      }
	      // kolla att inte offsetY blir för stor(negativ)
	      // offset + höjden på wiewport ska vara <= baseY + interval * antal poster
//System.out.println("this.getSize().height: " + this.getSize().height);
//System.out.println("(intervalY * model.getSize()): " + (intervalY * model.getSize()));
	      if ((-offsetY + this.getSize().height) > (intervalY * model.getSize())){
	      	offsetY = this.getSize().height - (intervalY * model.getSize());
//System.out.println("New offsetY: " + offsetY);
	      }
	    }
	    paint(getGraphics());
	    // uppdatera scrollListen
	    scroller.setHandlePosition(-offsetY/(model.getSize()*intervalY*1.0d));
	    scroller.paintComponent(scroller.getGraphics());
	}

	public void show(double position) {
	    offsetY = (int)Math.round(position * (model.getSize()*-intervalY));
	    paintComponent(getGraphics());
	}
	
	public void setForeground(Color fg) {
		super.setForeground(fg);
		frontColor = fg;
		rectColor = fg.darker().darker().darker();
		rect2Color = fg.darker();
		
		repaint();
    }

	public void paintComponent(Graphics g){
		if (g != null){
			Dimension d = getSize();
			Image bufferImage = createImage(d.width,d.height);
			Graphics bg = bufferImage.getGraphics();
			drawAll(bg);
			g.drawImage(bufferImage,0,0,this);
		}
//		drawAll(g);
	}
	
	public void drawAll(Graphics g){
	    g.setColor(StyleGuide.colorBackground);
	    g.fillRect(0,0,this.getSize().width,this.getSize().height);
	    for (int i = 0; i < model.getSize(); i++){
//	        current = (efapplet.getCurrentRoom() == preMeasurement.getRoom(i));
//	        preMeasurement.getRoom(i).drawAsMenu(g,current,9,(i*21)+5+offsetY,hasFocus);
	    	if (isSelected(i)){
//	    	if (i == selected){
	    		// draw as selected
//	    		FontMetrics fm = g.getFontMetrics();
	    		int tmpX = 1;
	    		int tmpY = (i*intervalY)+baseY+offsetY-13;
	    		int tmpWidth = this.getSize().width - 3;
//	    		int tmpWidth = fm.stringWidth((String)model.elementAt(i) + 6);
	    		int tmpHeight = 16;
//	    		int tmpArc = 5;
	    		g.setColor(rectColor);
	    		g.fillRect(tmpX,tmpY,tmpWidth,tmpHeight);
	    		g.setColor(rect2Color);
	    		g.drawRect(tmpX,tmpY,tmpWidth,tmpHeight);
	    	}
		    String textToDraw = (String)model.elementAt(i);
		    int tmpX = baseX;
		    int tmpY = (i*intervalY)+baseY+offsetY;
		    if (textToDraw.contains("¤")){
		    	String tmpText = textToDraw.substring(1);
		    	int endIndex = tmpText.indexOf("¤");
		    	textToDraw = tmpText.substring(endIndex+1);
		    	tmpText = tmpText.substring(0, endIndex);
//		    	Logger.fine("tmpText: " + tmpText);
		    	StringTokenizer st = new StringTokenizer(tmpText,";");
		    	if (st.countTokens() == 4){ // must be spaceship data: hits/medAmmo/largeAmmo/hugeAmmo
		    		int hits = Integer.valueOf(st.nextToken());
			    	//Logger.finer("ship hits: " + hits);
		    		int ammoM = Integer.valueOf(st.nextToken());
		    		int ammoL = Integer.valueOf(st.nextToken());
		    		int ammoH = Integer.valueOf(st.nextToken());
		    		// hits
		    		g.setColor(getColor(hits));
		    		g.drawLine(tmpX, tmpY-9, tmpX+hits, tmpY-9);
		    		g.drawLine(tmpX, tmpY-8, tmpX+hits, tmpY-8);
		    		g.drawLine(tmpX, tmpY-7, tmpX+hits, tmpY-7);
		    		g.drawLine(tmpX, tmpY-6, tmpX+hits, tmpY-6);
		    		// ammoM
		    		g.setColor(getColor(ammoM));
		    		g.drawLine(tmpX, tmpY-4, tmpX + ((ammoM > -1) ? ammoM : 10), tmpY-4);
		    		// ammoL
		    		g.setColor(getColor(ammoL));
		    		g.drawLine(tmpX, tmpY-2, tmpX + ((ammoL > -1) ? ammoL : 10), tmpY-2);
		    		// ammoH
		    		g.setColor(getColor(ammoH));
		    		g.drawLine(tmpX, tmpY, tmpX + ((ammoH > -1) ? ammoH : 10), tmpY);
		    	}
		    	if (st.countTokens() == 1){ // must be troop unit data: hits
		    		int hits = Integer.valueOf(st.nextToken());
			    	Logger.finer("troop hits: " + hits);
		    		// hits
		    		g.setColor(getColor(hits));
		    		g.drawLine(tmpX, tmpY-6, tmpX+hits, tmpY-6);
		    		g.drawLine(tmpX, tmpY-5, tmpX+hits, tmpY-5);
		    		g.drawLine(tmpX, tmpY-4, tmpX+hits, tmpY-4);
		    		g.drawLine(tmpX, tmpY-3, tmpX+hits, tmpY-3);
		    	}
		    	tmpX += 15;
		    }
		    g.setColor(frontColor);
	    	g.drawString(textToDraw,tmpX,tmpY);
	    }
	}
	
	private Color getColor(int value){
		Color theColor = null;
		switch(value){
		case -1: theColor = Color.DARK_GRAY;
		break;
		case 1: case 2: case 3: theColor = Color.RED;
		break;
		case 4:  case 5: case 6: theColor = Color.ORANGE;
		break;
		case 7:  case 8: case 9: case 10: theColor = Color.GREEN;
		}
    	//Logger.finest("value: " + value + " theColor: " + theColor.toString());
		return theColor;
	}
	
	public DefaultListModel getDefaultListModel(){
		return model;
	}
	
	/**
	 * Anropas direkt efter att listans innehåll har skapats
	 *
	 */
	public void updateScrollList(){
		if (((model.getSize()*intervalY)) > this.getSize().getHeight()){
			scroller.setEnabled(true);
			double size = this.getSize().getHeight()*1.0d/(intervalY*model.getSize());
			scroller.setHandleRatio(size);
			scroller.setHandlePosition(0);
		}else{
			scroller.setEnabled(false);
		}
	}
	
	public void mouseClicked(MouseEvent me) {}
	
	public void mouseEntered(MouseEvent me) {}
	
	public void mouseExited(MouseEvent me) {}
	
	public void mousePressed(MouseEvent me) {
		Logger.finer(me.toString());
		// kolla om y-coor är på listan
		if((me.getY() > 0) & (me.getY() < (model.getSize() * intervalY))){
			// ta fram vilket spaceship det är
			int itemNr = ((me.getY() - offsetY - 3) / intervalY);
			if (multipleSelect){
				System.out.println("Multiple select = true");
				System.out.println(me.getModifiers());
				int mod = me.getModifiers(); // 16 = normal, 17 = shift, 18 = ctrl
				if (mod == 16){ // normal mouse-click
					selected = itemNr;
					multipleSelected.clear();
					multipleSelected.add(new Integer(itemNr));
				}else
				if(mod == 17){ // shift left-click
					if (selected == -1){
						System.out.println("17... selected == -1");
						selected = itemNr;
						multipleSelected.add(new Integer(itemNr));					
					}else{
						multipleSelected.clear();
						int top = itemNr;
						int bottom = selected;
						if (top > bottom){
							top = selected;
							bottom = itemNr;
						}
						for(int i = top; i <= bottom; i++){
							multipleSelected.add(new Integer(i));
						}
					}
				}else{ // mod == 18, ctrl
					if (selected == -1){
						selected = itemNr;
					}
					int findIndex = findMultiple(itemNr); 
					if (findIndex > -1){
						// the item was already selected
						// deselect it, remove from multSel
						multipleSelected.remove(findIndex);
					}else{
						// item was not selected
						// select it
						multipleSelected.add(new Integer(itemNr));					
					}
				}
			}else{ // multiple selection not allowed
				selected = itemNr;
				multipleSelected.clear();
				multipleSelected.add(new Integer(itemNr));
			}
			lp.newSelection(me.getClickCount());
		}
		paintComponent(getGraphics());
		Logger.finer("MultselList: " + selected);
		for (int j = 0; j < multipleSelected.size(); j++){
			Logger.finer(String.valueOf(((Integer)multipleSelected.get(j)).intValue()));
		}
	}
	
	private boolean isSelected(int itemNr){
		return findMultiple(itemNr) > -1;
	}
	
	/**
	 * returns the index in the multipleSelected list of a certain item
	 * in the component
	 * @param itemNr index of an item in the componet
	 * @return index in multipleSelected
	 */
	private int findMultiple(int itemNr){
		boolean found = false;
		int foundIndex = -1;
		int i = 0;
		while ((!found) && (i < multipleSelected.size())){
			Integer anItem = (Integer)multipleSelected.get(i);
			if (anItem.intValue() == itemNr){
				found = true;
				foundIndex = i;
			}else{
				i++;
			}
		}
		return foundIndex;
	}
	
	public void mouseReleased(MouseEvent me) {}

	public int getSelected(){
		return selected;
	}

	public String getSelectedItem(){
		return (String)model.elementAt(selected);
	}

	public List<Integer> getSelectedItems(){
		return multipleSelected;
	}

	public int getPreferredHeight(){
		int pHeight = 0;
		pHeight = model.getSize()*intervalY + 1;
		return pHeight;
	}

	/**
	 * TODO (Paul) får ej detta att funka, pil upp/ned...
	 */
    public void keyPressed(KeyEvent ke){
    	Logger.finer("kp" + ke.getKeyCode());    	
    	switch(ke.getKeyCode()){
    		case 127: // delete
    			break;
    		default:
    			break;
    	}
	}

    public void keyReleased(KeyEvent ke){}

    public void keyTyped(KeyEvent ke){}

    public void setMultipleSelect(boolean newValue){
    	multipleSelect = newValue;
    }
    
    public void clearSelected(){
    	selected = -1;
    	multipleSelected.clear();
		paintComponent(getGraphics());
    }
    
    public void setSelected(int newSelected){
    	clearSelected();
    	selected = newSelected;
		multipleSelected.add(new Integer(newSelected));
		paintComponent(getGraphics());
    }
    
}
