/*
 * Created on 2005-jan-08
 */
package sr.client.components;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.StyleGuide;
import sr.client.components.scrollable.ListPanel;
import sr.client.components.scrollable.ScrollableList;
import sr.general.logging.Logger;

/**
 * @author WMPABOD
 *
 * Custom root component for a ComboBox with correct graphics
 */
@SuppressWarnings("serial")
public class ComboBoxPanel extends JPanel implements MouseListener, ListSelectionListener, ItemSelectable{
	private List<String> items;
	private ListPanel lp;
	private JPanel eventPanel;
	private String currentSelection;
	private ActionListener actionListener;
	private ItemListener itemListener;
//	private int currentIndex = -1;
	
	public ComboBoxPanel(){
		items = new ArrayList<String>();
//		setBorder(new LineBorder(StyleGuide.colorCurrent));
		addMouseListener(this);
	}
	
	public void addActionListener(ActionListener al){
		actionListener = al;
	}

	public void addItemListener(ItemListener il){
		itemListener = il;
	}

	public void paintComponent(Graphics g){
/*		g.setColor(StyleGuide.colorBackground);
		g.fillRect(0,0,getSize().width,getSize().height);
		g.setColor(StyleGuide.colorCurrent);
		g.drawString(currentSelection,4,14);
		*/
		paintAll(g);
	}
	
	public void paintAll(Graphics g){
	    if (g != null){
	      int tmpWidth = getSize().width;
	      int tmpHeight = getSize().height;
	      // rita ut bakgrunden
	      g.setColor(StyleGuide.colorBackground);
//	      g.fillRect(0, 0, tmpWidth - 1, tmpHeight - 1);
	      g.fillRect(0, 0, tmpWidth, tmpHeight);
	      // rita ut Border
	      g.setColor(StyleGuide.colorCurrent);
	      g.drawRect(0, 0, tmpWidth - 1, tmpHeight - 1);
	      // rita ut det valda objektet
	      drawSelected(g, tmpWidth, tmpHeight);
	      // rita ut "pilen" till höger
	      g.setColor(StyleGuide.colorBackground);
//	      g.fillRect(tmpWidth-18, 0, 18, tmpHeight);
	      g.fillRect(tmpWidth-18, 1, 17, tmpHeight-2);
	      if (isEnabled()) {
	        g.setColor(StyleGuide.colorCurrent);
	      } else {
	        g.setColor(StyleGuide.colorCurrent.darker().darker().darker());
	      }
	      int[] xPoints = {
	          tmpWidth - 14, tmpWidth - 4, tmpWidth - 9};
	      int[] yPoints = {
	          5, 5, 15};
	      Polygon arrow = new Polygon(xPoints, yPoints, 3);
	      g.fillPolygon(arrow);
	    }
	  }

	  private void drawSelected(Graphics g, int tmpWeight, int tmpHeight){
//	  	System.out.println("drawSelected: " + currentSelection + " isEnabled: " + isEnabled());
	    // rita ut texten
	    if(isEnabled()){
	      g.setColor(StyleGuide.colorCurrent);
	      g.drawString(currentSelection,4,14);
//	      g.drawString(items.get(currentIndex),4,14);
	    }else{
	      g.setColor(StyleGuide.colorCurrent.darker().darker().darker());
	      g.fillRect(3,3,getSize().width-20,getSize().height-6);
	    }
//	    g.setFont(getFont());
	  }

	
	public void addItem(String s){
		items.add(s);
		if (items.size() == 1){
			currentSelection = s;
//			currentIndex = 0;
		}
	}

	public void valueChanged(ListSelectionEvent arg0) {
		Logger.finer("valueChanged: " + arg0.toString());
		currentSelection = lp.getSelectedItem();
//		currentIndex = lp.getSelectedIndex();
		clearPopup();
		if (actionListener != null){
			Logger.finer("actionListener != null");
			actionListener.actionPerformed(new ActionEvent(this,0,currentSelection));
//			actionListener.actionPerformed(new ActionEvent(this,0,items.get(currentIndex)));
		}
		if (itemListener != null){
			Logger.finer("itemListener != null");
			itemListener.itemStateChanged(new ItemEvent(this,ItemEvent.SELECTED,currentSelection,ItemEvent.SELECTED));
//			itemListener.itemStateChanged(new ItemEvent(this,ItemEvent.SELECTED,items.get(currentIndex),ItemEvent.SELECTED));
		}
	}

	private void clearPopup(){
		JRootPane rootPane = getRootPane();
		JLayeredPane layeredPane = rootPane.getLayeredPane();
		lp.setVisible(false);
		layeredPane.remove(lp);
		eventPanel.setVisible(false);
		layeredPane.remove(eventPanel);
		paintComponent(getGraphics());
		paintBorder(getGraphics());
	}
	
	public String getSelectedItem(){
		//return items.get(currentIndex);
		return currentSelection;
	}
	

	public int getSelectedIndex(){
		return findSelected();
//		return currentIndex;
	}
	
	public Object getItemAt(int i){
		return items.get(i);
	}
	
	public void removeAllItems(){
		items.clear();
	}
	
	private int findSelected(){
		int found = -1;
		int counter = 0;
		while ((found == -1) & (counter < items.size())){
			if (((String)items.get(counter)).equalsIgnoreCase(currentSelection)){
				found = counter;
			}else{
				counter++;
			}
		}
		return found;
	}

	public int getItemCount(){
		return items.size();
	}
	
	public void setSelectedIndex(int newSelectedIndex){
		currentSelection = items.get(newSelectedIndex);
//		currentIndex = newSelectedIndex;
	}
	
	public boolean contains(String anItem){
		return items.indexOf(anItem) > -1;
	}

	public void setSelectedItem(String newSelectedItem){
		currentSelection = newSelectedItem;
/*		int newIndex = items.indexOf(newSelectedItem);
		if (newIndex == -1){
			Logger.severe("Index == -1: " + newSelectedItem);
			for (String anItem : items) {
				Logger.severe("anItem: " + anItem);
			}
		}
		currentIndex = newIndex;*/
	}

	public void mousePressed(MouseEvent arg0) {
		Object source = arg0.getSource();
		if (source instanceof ComboBoxPanel){
			if (isEnabled()){
				requestFocus();
				JRootPane rootPane = getRootPane();
				JLayeredPane layeredPane = rootPane.getLayeredPane();
				lp = new ListPanel();
				DefaultListModel dlm = (DefaultListModel)lp.getModel();
				for (int i = 0; i < items.size(); i++){
					dlm.addElement(items.get(i));
				}
				int preferredHeight = lp.getPreferredHeight();
				Point absLocation = getAbsoluteLocation();
				int availableDepth = rootPane.getHeight() - absLocation.y - getSize().height;
				int popupHeight = preferredHeight+1;
				if (popupHeight > availableDepth){
					popupHeight = (availableDepth/ScrollableList.intervalY) * ScrollableList.intervalY + 1;
				}
				lp.setBounds(absLocation.x,absLocation.y + getSize().height,getSize().width,popupHeight);
				lp.setListSelectionListener(this);
				lp.updateScrollList();
				// add an opaque eventstopper
				eventPanel = new JPanel();
				eventPanel.setBounds(0,0,rootPane.getSize().width,rootPane.getSize().height);
				eventPanel.addMouseListener(this);
				eventPanel.setOpaque(false);
				layeredPane.add(eventPanel,JLayeredPane.POPUP_LAYER);
//				layeredPane.add(eventPanel,JLayeredPane.MODAL_LAYER);
				// add the popup
//				layeredPane.add(lp,JLayeredPane.POPUP_LAYER);
				layeredPane.add(lp,JLayeredPane.DRAG_LAYER);
			}
		}else{
			// är eventpanel
//System.out.println("eventpanel event");
			clearPopup();
		}
	}
	
	public Point getAbsoluteLocation(){
		int tmpX = getLocation().x;
		int tmpY = getLocation().y;
		Container tmpParent = getParent();
		while (tmpParent != null){
			Point tmpLocation = tmpParent.getLocation();
			Logger.finest("x,y: " + tmpX + "," + tmpY + " - " + tmpParent.getClass().getName() + " (" + tmpLocation.x + "," + tmpLocation.y + ")");
			if (!tmpParent.getClass().getName().equals("sr.notifier.NotifierFrame")){
				tmpX = tmpX + tmpLocation.x;
				tmpY = tmpY + tmpLocation.y;
			}else{ // kompensera för listen i överkanten på applikationsfönstret
				tmpX -= 4;
				tmpY -= 30;
			}
			tmpParent = tmpParent.getParent();
		}
		Logger.finest("Final x,y: " + tmpX + "," + tmpY);
		return new Point(tmpX,tmpY);
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

	public void removeItemListener(ItemListener arg0) {}

	public Object[] getSelectedObjects() {
		return null;
	}
	
	public boolean exist(String value){
		for (Object item : items) {
			if(value.equalsIgnoreCase((String)item)){
				return true;
			}
		}
		
		return false;
	}

}
