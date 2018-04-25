package sr.client.components;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: Wrapper around JPanel to add SpaceRaze looks</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRLabel extends JLabel {

  public SRLabel() {
	  this("");
  }

  public SRLabel(String text, int width, int height) {
	  this(text);
	  setSize(width, height);
  }

  public SRLabel(int width, int height) {
	  this();
	  setSize(width, height);
  }

  public SRLabel(String text, Icon icon, int horizontalAlignment) {
    super(text, icon, horizontalAlignment);
    setForeground(StyleGuide.colorCurrent);
  }

  public SRLabel(String text, int horizontalAlignment) {
    super(text, horizontalAlignment);
    setForeground(StyleGuide.colorCurrent);
  }

  public SRLabel(String text) {
    super(text);
    setForeground(StyleGuide.colorCurrent);
  }
  
  public SRLabel(String text, Color tempColor) {
	  super(text);
	  setForeground(tempColor);
  }

  public SRLabel(Icon image, int horizontalAlignment) {
    super(image, horizontalAlignment);
    setForeground(StyleGuide.colorCurrent);
  }

  public SRLabel(Icon image) {
    super(image);
    setForeground(StyleGuide.colorCurrent);
  }
  
  public void setText(int aTextNumber){
	  setText(String.valueOf(aTextNumber));
  }

/*  public JToolTip createToolTip(){
    JToolTip tmpToolTip = new JToolTip();
    tmpToolTip.setTipText("xxx " + getToolTipText());
    return tmpToolTip;
  }*/
}