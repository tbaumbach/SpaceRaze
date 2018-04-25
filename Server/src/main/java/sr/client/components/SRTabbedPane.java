package sr.client.components;

import java.awt.Insets;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import sr.client.StyleGuide;


@SuppressWarnings("serial")
public class SRTabbedPane extends JTabbedPane{
	
	public SRTabbedPane(String borderType){
		super();
		UIManager.put("TabbedPane.selected", StyleGuide.colorBackground);
	      UIManager.put("TabbedPane.focus", StyleGuide.colorBackground);
	      UIManager.put("TabbedPane.selectedForeground", StyleGuide.colorCurrent);
	      UIManager.put("TabbedPane.selectHighlight", StyleGuide.colorCurrent);
	      
	      UIManager.put("TabbedPane.background", StyleGuide.colorBackground);
	      UIManager.put("TabbedPane.darkShadow",StyleGuide.colorCurrent);
	     
	      UIManager.put("TabbedPane.highlight", StyleGuide.colorCurrent);
	      UIManager.put("TabbedPane.shadow", StyleGuide.colorCurrent.darker().darker().darker());
	      UIManager.put("TabbedPane.light", StyleGuide.colorCurrent.darker());
	      
	      UIManager.put("TabbedPane.contentAreaColor", StyleGuide.colorBackground);
	      UIManager.put("TabbedPane.darkShadow", StyleGuide.colorCurrent);
	      
	      if(borderType.equals("noBottomBorder")){
	    	  UIManager.put("TabbedPane.contentBorderInsets", new Insets( 1, 1, 0, 1) );
		  }else if(borderType.equals("noBorders")){
			  UIManager.put("TabbedPane.contentBorderInsets", new Insets( 1, 0, 0, 0) );
		  }
	      updateUI();
	}

	
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
    }

}
