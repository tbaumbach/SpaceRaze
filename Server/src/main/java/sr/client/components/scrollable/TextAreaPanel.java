/*
 * Created on 2005-jan-02
 */
package sr.client.components.scrollable;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;

/**
 * @author WMPABOD
 *
 * Implements a custom textarea component that can show a vertical ScrollList if needed 
 */
@SuppressWarnings("serial")
public class TextAreaPanel extends JPanel {
	Scroller scrollList;
	ScrollableTextArea textarea;
	private final int scrollerWidth = 13;
	
	public TextAreaPanel(){
		scrollList = new Scroller();
		textarea = new ScrollableTextArea(scrollList);
		
		setLayout(null);

	    setBorder(new LineBorder(StyleGuide.colorCurrent));

		add(textarea);
		add(scrollList);
		setOpaque(false);
		scrollList.addScrollListListener(textarea);
	}
	
	public void setBounds(int x, int y, int width, int height){
		super.setBounds(x,y,width,height);
		textarea.setBounds(4,4,width-scrollerWidth-7,height-8);
		scrollList.setBounds(width-scrollerWidth,1,scrollerWidth-1,height-2);
	}
	
	public DefaultListModel getModel(){
		return textarea.getDefaultListModel();
	}
	
	public void updateScrollList(){
		textarea.updateScrollList();
	}

	public void setText(String newText){
		DefaultListModel dlm = textarea.getDefaultListModel();
		dlm.clear();
		textarea.reset();
		if ((newText != null) && (!newText.equals(""))){
			append(newText);
		}
	}
	
	public void append(String textToAppend){
		DefaultListModel dlm = textarea.getDefaultListModel();
		dlm.addElement(textToAppend);
	}

}
