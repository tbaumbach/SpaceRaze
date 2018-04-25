package sr.client.components;

import javax.swing.JTextArea;
import javax.swing.text.Document;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: SpaceRaze with Swing GUI</p>
 * @author Paul Bodin
 * @version 3.0
 */

public class SRTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;

	public SRTextArea() {
		setBackground(StyleGuide.colorBackground);
		setForeground(StyleGuide.colorCurrent);
		//setBorder(new LineBorder(StyleGuide.colorCurrent));
		setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setLineWrap(true);
		setWrapStyleWord(true);
		setCaretColor(StyleGuide.colorCurrent);
		setSelectedTextColor(StyleGuide.colorCurrent.darker());
		setSelectionColor(StyleGuide.colorCurrent.brighter());
	}
	
	public SRTextArea(String text) {
		this();
		setText(text);
	}
	
	public SRTextArea(int rows, int columns) {
		super(rows, columns);
	}
	
	public SRTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
	}
	
	public SRTextArea(Document doc) {
		super(doc);
	}
	
	public SRTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
	}
}