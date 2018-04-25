package sr.client.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: SpaceRaze with Swing GUI</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRCheckBox extends JCheckBox {
  static Image empty,checked;
  Component c;

  public SRCheckBox(Component c) {
    this("",c);
  }


  public SRCheckBox(String text, Component c){
	  this(text);
	  this.c = c;

	  empty = c.createImage(13,13);
      checked = c.createImage(13,13);
      Graphics eg = empty.getGraphics();
      Graphics cg = checked.getGraphics();
      // rita ut bakgrunden på bägge
      eg.setColor(StyleGuide.colorBackground);
      cg.setColor(StyleGuide.colorBackground);
      eg.fillRect(0,0,13,13);
      cg.fillRect(0,0,13,13);
      // rita ut rutan
      eg.setColor(StyleGuide.colorCurrent);
      cg.setColor(StyleGuide.colorCurrent);
      eg.drawRect(0,0,12,12);
      cg.drawRect(0,0,12,12);
      // rita ut bocken (check)
      cg.drawLine(0,3,8,12);
      cg.drawLine(0,4,7,12);
      cg.drawLine(0,5,6,12);
      cg.drawLine(0,6,5,12);
      cg.drawLine(4,11,9,1);
      cg.drawLine(5,11,10,1);
      cg.drawLine(6,11,11,1);
      cg.drawLine(7,11,12,1);

      setSelectedIcon(new ImageIcon(checked));
      setIcon(new ImageIcon(empty));
  }

  public SRCheckBox(String text) {
    super(text);
    setBackground(StyleGuide.colorBackground);
    setForeground(StyleGuide.colorCurrent);
    setBorder(new LineBorder(StyleGuide.colorCurrent));
//    setSelectedIcon(new ImageIcon(SpaceRazeApplet.imageHandler.getImage("neutral_check")));
    setContentAreaFilled(false);
    setDoubleBuffered(true);
//    setIcon(new ImageIcon(SpaceRazeApplet.imageHandler.getImage("neutral_no_check")));
    setOpaque(true);
  }


}