/*
 * Created on 2005-jan-02
 */
package sr.client.components.scrollable;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;

import sr.client.StyleGuide;
import sr.client.components.SRBasePanel;
import sr.client.interfaces.ScrollListListener;
import sr.general.StringTokenizerPlusPlus;

/**
 * @author WMPABOD
 *
 * A custom textarea that is coupled with a ScrollList in a TextAreaPanel 
 */
@SuppressWarnings("serial")
public class ScrollableTextArea extends SRBasePanel implements ScrollListListener{
	private DefaultListModel model,wrappedModel;
	private int offsetY = 0;
	private int baseY = 13;
	public final int leftPadding = 4;
	public final int rightPadding = 4;
	private int intervalY = 16;
	private Scroller scroller;
	
	public ScrollableTextArea(Scroller scroller){
		super(true);
		this.scroller = scroller;
		model = new DefaultListModel();
	}
	
	public void reset(){
		wrappedModel = null;
		offsetY = 0;
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
	      if ((-offsetY + getSize().height) > (intervalY * wrappedModel.getSize())){
	      	offsetY = this.getSize().height - (intervalY * wrappedModel.getSize());
//System.out.println("New offsetY: " + offsetY);
	      }
	    }
	    paintComponent(getGraphics());
	    // uppdatera scrollListen
	    scroller.setHandlePosition(-offsetY/(wrappedModel.getSize()*intervalY*1.0d));
	    scroller.paintComponent(scroller.getGraphics());
	}

	public void show(double position) {
	    offsetY = (int)Math.round(position * (wrappedModel.getSize()*-intervalY));
	    paintComponent(getGraphics());
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
	    g.setFont(new Font("serif",0,11));
	    if (wrappedModel == null){
		    createWrappedModel(g);
		    updateScrollList();	    	
	    }
	    for (int i = 0; i < wrappedModel.getSize(); i++){
		    g.setColor(StyleGuide.colorCurrent);
		    g.drawString((String)wrappedModel.elementAt(i),leftPadding,(i*intervalY)+baseY+offsetY);
	    }
	}
	
	private void createWrappedModel(Graphics g){
		wrappedModel = new DefaultListModel();
		// add all appends in the model to one long string
		String totalModelString = "";
		for (int i = 0; i < model.getSize(); i++){
			String rowString = (String)model.elementAt(i);
			totalModelString = totalModelString + rowString;
		}
		// split the string on newlines
		StringTokenizerPlusPlus stpp = new StringTokenizerPlusPlus(totalModelString,"\n");
		int textSpace = getSize().width - leftPadding - rightPadding;
		FontMetrics fm = g.getFontMetrics();
		String[] rows = null;
		while (stpp.hasMoreTokens()){
			rows = getRowsWhenWrapping(stpp.nextToken(),fm,textSpace);
			for (int j = 0; j < rows.length; j++){
				wrappedModel.addElement(rows[j]);
			}
		}
	}
	
	private String[] getRowsWhenWrapping(String text,FontMetrics fm,int maxWidth){
		StringTokenizer st = new StringTokenizer(text);
		List<String> tmpRows = new ArrayList<String>();
		String tmpString = "";
		String tmpRow = "";
		while (st.hasMoreTokens()){
			tmpString = st.nextToken();
			if (fm.stringWidth(tmpString + tmpRow) > maxWidth){
				tmpRows.add(tmpRow);
				tmpRow = tmpString;
				tmpString = "";
			}else{
				if (tmpRow.equals("")){
					tmpRow = tmpString;
				}else{
					tmpRow = tmpRow + " " + tmpString;
				}
			}
		}
		tmpRows.add(tmpRow);
		String[] rowsArray = new String[tmpRows.size()];
		for (int i = 0; i < tmpRows.size(); i++){
			rowsArray[i] = (String)tmpRows.get(i);
		}
		return rowsArray;
	}

	public DefaultListModel getDefaultListModel(){
		return model;
	}
	
	/**
	 * Anropas direkt efter att listans innehåll har skapats
	 *
	 */
	public void updateScrollList(){
//		Thread.dumpStack();
//		Logger.fine("wrappedModel.getSize(): " + wrappedModel.getSize());
//		Logger.fine("(wrappedModel.getSize()*intervalY)+baseY): " + ((wrappedModel.getSize()*intervalY)+baseY));
//		Logger.fine("getSize().height: " + getSize().height);
		if (((wrappedModel.getSize()*intervalY)+baseY) > getSize().height){
			scroller.setEnabled(true);
			double size = getSize().height*1.0d/(intervalY*wrappedModel.getSize());
//System.out.println("size: " + size);
//	      double size = 13.0d/model.getSize();
			scroller.setHandleRatio(size);
//			scroller.setHandlePosition(1.0d - size);
			scroller.setHandlePosition(0);
			// sätt så att det sista / nya rummet visas nederst i listan
			//	      offsetY = -21*(preMeasurement.getNumberOfRooms() - 13);
			//	System.out.println("  ->  offsetY: " + offsetY);
		}else{
			scroller.setEnabled(false);
		}
		scroller.repaint();
//		paint(getGraphics());
//		scrollList.paint(scrollList.getGraphics());
//		scroller.paintComponent(scroller.getGraphics());
	}

}
