package sr.client.components;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: SpaceRaze with Swing GUI</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRList extends JList {

  public SRList() {
    setBackground(StyleGuide.colorBackground);
    setForeground(StyleGuide.colorCurrent);
    setSelectionForeground(StyleGuide.colorCurrent);
    setSelectionBackground(StyleGuide.colorCurrent.darker().darker().darker());
    setBorder(new LineBorder(StyleGuide.colorCurrent));
    setModel(new DefaultListModel());
  }

}