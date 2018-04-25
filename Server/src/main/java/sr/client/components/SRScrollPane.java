package sr.client.components;

import java.awt.Component;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: SpaceRaze with Swing GUI</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRScrollPane extends JScrollPane {

  public SRScrollPane() {
  }

  public SRScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
    super(view, vsbPolicy, hsbPolicy);
  }

  public SRScrollPane(Component view) {
    super(view);
    setBackground(StyleGuide.colorBackground);
    setForeground(StyleGuide.colorCurrent);
    setBorder(new LineBorder(StyleGuide.colorCurrent));
    setHorizontalScrollBar(new SRScrollBar(JScrollBar.HORIZONTAL));
    setVerticalScrollBar(new SRScrollBar(JScrollBar.VERTICAL));
  }

  public SRScrollPane(int vsbPolicy, int hsbPolicy) {
    super(vsbPolicy, hsbPolicy);
  }
}