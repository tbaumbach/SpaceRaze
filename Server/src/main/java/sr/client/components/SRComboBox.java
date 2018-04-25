package sr.client.components;

import javax.swing.JComboBox;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: Combobox med SpaceRaze GUI.
 * Använder EFComboBoxBase för att rita ut huvudkomponenten
 * Använder en vanlig JComboBox för att rita ut popupen som i sin tur använder EFComboBoxRenderer för kustomiserat utseende.</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRComboBox extends JComboBox {


/*public class EFComboBox extends JPanel {
  JComboBox jChoice;
  EFComboBoxBase ownChoice;

  public EFComboBox() {
    setLayout(null);

    jChoice = new JComboBox();
    jChoice.setRenderer(new EFComboBoxRenderer());

    ownChoice = new EFComboBoxBase(jChoice,"");
    ownChoice.setToolTipText("Decides which side you are on: good, evil or neutral");

    add(ownChoice);
    add(jChoice);

    jChoice.addItemListener(ownChoice);
  }

  public void addItem(String anItem){
    jChoice.addItem(anItem);
    if (jChoice.getItemCount() == 0){
      ownChoice.setChosen(anItem);
    }
  }

  public void select(String selected){
    ownChoice.setChosen(selected);
  }

  public void select(int selectedIndex){
    ownChoice.setChosen((String)jChoice.getItemAt(selectedIndex));
  }

  public JComboBox getJComboBox(){
    return jChoice;
  }

  public void reshape(int x, int y, int width, int height){
    super.reshape(x,y,width,height);
    jChoice.reshape(0,0,width,height);
    ownChoice.reshape(0,0,width,height);
  }

  public void setBounds(int x, int y, int width, int height){
    reshape(x, y, width, height);
  }

  public void setEnabled(boolean enabled){
    ownChoice.setEnabled(enabled);
  }

  public int getSelectedIndex(){
    return ownChoice.getSelectedIndex();
  }

  public String getSelectedItem(){
    return ownChoice.getSelectedItem();
  }

  public void addItemListener(ItemListener anItemListener){
    jChoice.addItemListener(anItemListener);
  }

  public void setVisible(boolean newState){
    super.setVisible(newState);
    ownChoice.setVisible(newState);
    jChoice.setVisible(newState);
  }
*/
}