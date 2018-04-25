package sr.client.components;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: Används för att få rätt utseende på popupen på EFComboBox</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRComboBoxRenderer extends JLabel implements ListCellRenderer {
  private int tmpIndex;
  private JList tmpList;
  private boolean tmpNotEnabled;

  public SRComboBoxRenderer(){
  }

  public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean notEnabled) {
    tmpIndex = index;
    tmpList = list;
    tmpNotEnabled = notEnabled;
    this.setText((String)value);
    setForeground(StyleGuide.colorCurrent);
    if (isSelected){
      setBackground(StyleGuide.colorCurrent.darker().darker());
    }else{
      setBackground(StyleGuide.colorBackground);
    }
    return this;
  }

  public void paint(Graphics g){
    int tmpWidth = getSize().width;
    int tmpHeight = getSize().height;
    // rita ut bakgrunden
    if (tmpIndex == -1){
      g.setColor(StyleGuide.colorBackground);
//System.out.println("x " + g.getColor());
    }else{
      g.setColor(getBackground());
//System.out.println(g.getColor());
    }
//g.setColor(Color.red);
    g.fillRect(0,0,tmpWidth,tmpHeight);
    // rita ut texten
    if (tmpIndex == -1){
      if(tmpNotEnabled){
        g.setColor(StyleGuide.colorCurrent.darker().darker());
      }else{
        g.setColor(StyleGuide.colorCurrent);
      }
    }else{
      g.setColor(getForeground());
    }
    g.setFont(getFont());
    g.drawString(getText(),3,12);
    if (tmpIndex > -1){
      // kanten till vänster
      g.drawLine(0, 0, 0, tmpHeight);
      // kanten till höger
      g.drawLine(tmpWidth - 1, 0, tmpWidth - 1, tmpHeight);
      // kanten högst upp
      if (tmpIndex == 0) {
        g.drawLine(0, 0, tmpWidth, 0);
      }
      // kanten nederst
      if ( (tmpList.getModel().getSize() - 1) == tmpIndex) {
        g.drawLine(0, tmpHeight - 1, tmpWidth, tmpHeight - 1);
      }
    }
  }
}