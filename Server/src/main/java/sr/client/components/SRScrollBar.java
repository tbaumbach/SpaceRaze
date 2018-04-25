package sr.client.components;

import java.awt.Graphics;

import javax.swing.JScrollBar;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: SpaceRaze with Swing GUI</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRScrollBar extends JScrollBar {

  public SRScrollBar() {
    setValues();
  }

  public SRScrollBar(int orientation, int value, int extent, int min, int max) {
    super(orientation, value, extent, min, max);
    setValues();
  }

  public SRScrollBar(int orientation) {
    super(orientation);
    setValues();
  }

  private void setValues(){
    setBackground(StyleGuide.colorBackground);
    setForeground(StyleGuide.colorCurrent);
    setBorder(new LineBorder(StyleGuide.colorCurrent));
  }

  public void Paint(Graphics g){
    super.paint(g);
  }




}