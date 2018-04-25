package sr.client.components;

import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import sr.client.StyleGuide;

@SuppressWarnings("serial")
public class SRTableHeader extends JTableHeader {

	public SRTableHeader() {
		super();
		setColors();
	}

	public SRTableHeader(TableColumnModel arg0) {
		super(arg0);
		setColors();
	}
	
	private void setColors(){
	    setBorder(new LineBorder(StyleGuide.colorCurrent));
		setForeground(StyleGuide.colorCurrent);
//	    setBackground(StyleGuide.colorBackground);
	    setBackground(StyleGuide.colorCurrent.darker().darker().darker());
	}

}
