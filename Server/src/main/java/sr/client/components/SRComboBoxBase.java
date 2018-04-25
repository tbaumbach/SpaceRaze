package sr.client.components;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description:Visar en enkel kombobox utan popup med SpaceRaze utseende. Ej editernar text.</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRComboBoxBase extends JComponent implements ItemListener,MouseListener,PopupMenuListener{
  private List<String> listItems = new ArrayList<String>();
  private int selected = 0;
  private boolean showPopup = false;
  private JComboBox theRealJComboBox;
  private String chosen;

  public SRComboBoxBase(JComboBox aRealJComboBox, String initialChosen){
    chosen = initialChosen;
    theRealJComboBox = aRealJComboBox;
    addMouseListener(this);
  }

  public void addItem(String anItem){
    listItems.add(anItem);
  }

  public int getSelectedIndex(){
    return selected;
  }

  public void setChosen(String newChosen){
    chosen = newChosen;
  }

  public String getSelectedItem(){
    return chosen;
  }

  public void paint(Graphics g){
    if (g != null){
      int tmpWidth = getSize().width;
      int tmpHeight = getSize().height;
      // rita ut bakgrunden
      g.setColor(StyleGuide.colorBackground);
      g.fillRect(0, 0, tmpWidth - 1, tmpHeight - 1);
      // rita ut Border
      g.setColor(StyleGuide.colorCurrent);
      g.drawRect(0, 0, tmpWidth - 1, tmpHeight - 1);
      // rita ut "pilen" till höger
      if (isEnabled()) {
        g.setColor(StyleGuide.colorCurrent);
      }
      else {
        g.setColor(StyleGuide.colorCurrent.darker().darker());
      }
      int[] xPoints = {
          tmpWidth - 14, tmpWidth - 4, tmpWidth - 9};
      int[] yPoints = {
          5, 5, 15};
      Polygon arrow = new Polygon(xPoints, yPoints, 3);
      g.fillPolygon(arrow);
      // rita ut det valda objektet
      drawSelected(g, tmpWidth, tmpHeight);
    }
  }

  private void drawSelected(Graphics g, int tmpWeight, int tmpHeight){
    // rita ut texten
    if(isEnabled()){
      g.setColor(StyleGuide.colorCurrent);
    }else{
      g.setColor(StyleGuide.colorCurrent.darker().darker());
    }
    g.setFont(getFont());
    g.drawString(chosen,3,14);
  }

  public void mousePressed(MouseEvent me){
    if (isEnabled()){
      if(showPopup){
        showPopup = false;
        // stäng popup
        theRealJComboBox.hidePopup();
      }else{
        showPopup = true;
        // öppna popup
        theRealJComboBox.showPopup();
      }
    }
  }

  public void itemStateChanged(ItemEvent ie){
    showPopup = false;
    chosen = (String)ie.getItem();
    update(getGraphics());
  }

/*  private void createPopupMenu(){
    popupMenu = new JPopupMenu();
    popupMenu.setLocation((int)this.getLocationOnScreen().getX(),(int)this.getLocationOnScreen().getY() + this.getSize().height);
    popupMenu.setForeground(StyleGuide.colorCurrent);
    popupMenu.setBackground(StyleGuide.colorBackground);
    popupMenu.setBorder(new LineBorder(StyleGuide.colorCurrent));
    JMenuItem tmpMenuItem = null;
    for (int i = 0; i < listItems.size(); i++){
      tmpMenuItem = new JMenuItem(listItems.getStringAt(i));
      tmpMenuItem.setForeground(StyleGuide.colorCurrent);
      tmpMenuItem.setBackground(StyleGuide.colorBackground);
      tmpMenuItem.addActionListener(this);
      popupMenu.add(tmpMenuItem);
    }
  }


  public void actionPerformed(ActionEvent ae){
    selected = findSelected(ae.getActionCommand());
    update(this.getGraphics());
//    System.out.println(ae.getActionCommand() + " " + ae.getModifiers());
    showPopup = false;
    // stäng popup
    popupMenu.setVisible(false);
  }

  private int findSelected(String chosen){
    int returnValue = -1;
    int i = 0;
    boolean found = false;
    while (!found & (i < listItems.size())){
      if (listItems.getStringAt(i).equals(chosen)){
        found = true;
        returnValue = i;
      }else{
        i++;
      }
    }
    return returnValue;
  }
*/
  public void mouseClicked(MouseEvent me){
  }

  public void mouseReleased(MouseEvent me){
  }

  public void mouseExited(MouseEvent me){
  }

  public void mouseEntered(MouseEvent me){
  }

  public void popupMenuCanceled(PopupMenuEvent pme){
  }

  public void popupMenuWillBecomeInvisible(PopupMenuEvent pme){
  }

  public void popupMenuWillBecomeVisible(PopupMenuEvent pme){
  }
/*
    public EFComboBox() {
      setBorder(new LineBorder(StyleGuide.colorCurrent));
      //setForeground(StyleGuide.colorCurrent);
      //setBackground(StyleGuide.colorBackground);
      setRenderer(new EFComboBoxRenderer());
      //setOpaque(false);
    }

  public EFComboBox(ComboBoxModel aModel) {
    super(aModel);
  }

  public EFComboBox(Object[] items) {
    super(items);
  }

  public EFComboBox(Vector items) {
    super(items);
  }

  public void paint(Graphics g){
    g.setColor(StyleGuide.colorBackground);
    g.fillRect(0,0,getSize().width-1,getSize().height-1);
    g.setColor(StyleGuide.colorCurrent);
    g.drawRect(0,0,getSize().width-1,getSize().height-1);
    JList dataList = new JList();
    for (int i = 0; i > this.getItemCount(); i++){
      dataList.add(new JLabel());
    }
    Image buffer = createImage(getSize().width-20,getSize().height-4);
    Graphics bg = buffer.getGraphics();
    Component tmpLabel = getRenderer().getListCellRendererComponent(dataList,getSelectedItem(),-1,false,!isEnabled());
    tmpLabel.paint(bg);
    g.drawImage(buffer,2,2,this);
    // rita ut "pilen" till höger
    if (isEnabled()){
      g.setColor(StyleGuide.colorCurrent);
    }else{
      g.setColor(StyleGuide.colorCurrent.darker().darker());
    }
    g.fillRect(getSize().width-14,4,10,10);
  }
  */
}