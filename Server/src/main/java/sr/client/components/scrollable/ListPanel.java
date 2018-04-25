/*
 * Created on 2005-jan-02
 */
package sr.client.components.scrollable;

import java.awt.Color;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.StyleGuide;
import sr.general.logging.Logger;

/**
 * @author WMPABOD
 *
 * Implements a custom list component that can show a vertical ScrollList if needed 
 */
@SuppressWarnings("serial")
public class ListPanel extends JPanel {
	private ScrollableList list;
	private Scroller scrollList;
	private ListSelectionListener listSelectionListener;
//	private int selectedIndex;
	private final int scrollerWidth = 13;
	private boolean showScroller = true;

	public ListPanel(boolean showScroller){
		this();
		this.showScroller = showScroller;
	}

	public ListPanel(){
		scrollList = new Scroller();
		list = new ScrollableList(scrollList,this);
		
		setLayout(null);

	    setBorder(new LineBorder(StyleGuide.colorCurrent));
	    setOpaque(true);

		add(list);
		add(scrollList);
		
		scrollList.addScrollListListener(list);
	}
	
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if(scrollList != null){
			scrollList.setForeground(fg);
		}
		if(list != null){
			list.setForeground(fg);
		}
		setBorder(new LineBorder(fg));
		repaint();
    }
	
	public void setBounds(int x, int y, int width, int height){
		super.setBounds(x,y,width,height);
		int moveScroller = 0;
		if (!showScroller){
			moveScroller = scrollerWidth-1;
		}
		list.setBounds(1,1,width-scrollerWidth-1+moveScroller,height-2);
		scrollList.setBounds(width-scrollerWidth+(moveScroller*2),1,scrollerWidth-1,height-2);
	}
/*	
	public void paintComponent(Graphics g){
		Dimension d = getSize();
		g.setColor(Color.GREEN);
		g.fillRect(0,0,d.width,d.height);
	}
*/
	public void setListSelectionListener(ListSelectionListener lsl){
		listSelectionListener = lsl;
	}
	
	public void newSelection(int clickCount){
		if (listSelectionListener != null){
			ListSelectionEvent lse = new ListSelectionEvent(this, clickCount, 0, true); // fuskar lite och använder andra parametern till clickcount...
			listSelectionListener.valueChanged(lse);
		}
	}
	
	public int getSelectedIndex(){
		return list.getSelected();
	}
	
	public String getSelectedItem(){
		return list.getSelectedItem();
	}
	
	public List<Integer> getSelectedItems(){
		return list.getSelectedItems();
	}
	
	public DefaultListModel getModel(){
		return list.getDefaultListModel();
	}

	public void updateScrollList(){
		list.updateScrollList();
	}
	
	public int getPreferredHeight(){
		return list.getPreferredHeight();
	}
	
    public void setMultipleSelect(boolean newValue){
    	list.setMultipleSelect(newValue);
    }

    public void clearSelected(){
    	list.clearSelected();
    }
    
    /**
     * Selects a row in the list
     * @param index Index of the row that should be selected
     */
    public void setSelected(int index){
    	list.setSelected(index);
    	Logger.finer("list.getSelected(): " + list.getSelected());
    }

}
