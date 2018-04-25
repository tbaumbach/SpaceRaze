/*
 * Created on 2005-jan-30
 */
package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.CheckBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.logging.Logger;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.VIP;

/**
 * @author WMPABOD
 *
 * Shown retreating spaceships
 */
public class RetreatsPanel extends SRBasePanel implements SRUpdateablePanel,ActionListener,ListSelectionListener{
	private static final long serialVersionUID = 1L;
	private String id;
	private SRLabel title;
	private List<Spaceship> retreatingShips;
	private Player player;
	private Spaceship currentss;
	private ListPanel shiplist;
	private CheckBoxPanel selfDestructCheckBox;
	private SRTextArea VIPInfoTextArea;
	private SRLabel dcLabel,killsLabel,VIPinfoLabel;
	private JScrollPane scrollPane2;

	public RetreatsPanel(String id, Player p) {
	    this.id = id;
	    this.setLayout(null);
	    player = p;
	    retreatingShips = p.getGalaxy().getRetreatingShips(p);
	    
	    title = new SRLabel("Retreating spaceships");
	    title.setBounds(10,10,200,15);
	    add(title);

	    selfDestructCheckBox = new CheckBoxPanel("Selfdestruct");
	    selfDestructCheckBox.setBounds(10,340,100,15);
	    selfDestructCheckBox.setSelected(false);
	    selfDestructCheckBox.addActionListener(this);
	    selfDestructCheckBox.setVisible(false);
	    add(selfDestructCheckBox);
	    
	    dcLabel = new SRLabel();
	    dcLabel.setBounds(130,340,90,15);
	    add(dcLabel);
	    
	    killsLabel = new SRLabel();
	    killsLabel.setBounds(130,360,150,15);
	    add(killsLabel);
	    
	    // VIPs info textarea
	    VIPinfoLabel = new SRLabel("VIPs on this ship");
	    VIPinfoLabel.setBounds(250,340,120,15);
	    VIPinfoLabel.setVisible(false);
	    add(VIPinfoLabel);
	    
	    VIPInfoTextArea = new SRTextArea();
	    VIPInfoTextArea.setEditable(false);
	    VIPInfoTextArea.setVisible(false);
	    
	    scrollPane2 = new SRScrollPane(VIPInfoTextArea);
	    scrollPane2.setBounds(250,360,200,60);
	    scrollPane2.setVisible(false);
	    add(scrollPane2);
	    
	    shiplist = new ListPanel();
	    shiplist.setBounds(10,35,500,290);
	    shiplist.setListSelectionListener(this);
        shiplist.setMultipleSelect(false);
        add(shiplist);
        fillList();
	}

    private void fillList(){
        DefaultListModel dlm = (DefaultListModel)shiplist.getModel();
	    for(int i = 0; i < retreatingShips.size(); i++){
        	Spaceship tempss = ((Spaceship)retreatingShips.get(i));
        	String suffix = "";
        	if(player.getShipSelfDestruct(tempss)){
        		suffix = " (will selfdestruct)";
        	}
        	dlm.addElement(tempss.getName() + " is retreating and just left " + tempss.getOldLocation().getName() + suffix);
        }
        shiplist.updateScrollList();
    }

    public void valueChanged(ListSelectionEvent lse){
    	if (lse.getSource() instanceof ListPanel){
    		if (lse.getValueIsAdjusting()){
    			int lastSelection = shiplist.getSelectedIndex();
    			showSpaceship(lastSelection);
    		}
    	}
    }

    public void actionPerformed(ActionEvent ae){
      	Logger.finer("actionPerformed: " + ae.toString());
      	newOrder((CheckBoxPanel)ae.getSource());
    	emptyList();
    	fillList();
    	paintComponent(getGraphics());
    	paintChildren(getGraphics());
    }

    private void newOrder(CheckBoxPanel cb){
    	if (cb.isSelected()){
    		// set up ship for destruction
    		player.addShipSelfDestruct(currentss);
    		// remove any old moveorder for that ship
    		player.addShipMove(currentss,null);
    	}else{
            player.removeShipSelfDestruct(currentss);
    	}
    }

    private void showSpaceship(int index){
    	Spaceship ss = (Spaceship)retreatingShips.get(index);
    	currentss = ss;
    	// show and set selfdestruct cb
    	selfDestructCheckBox.setSelected(player.getShipSelfDestruct(ss));
    	selfDestructCheckBox.setVisible(true);

        dcLabel.setText("Hits: " + ss.getCurrentDc()+ "/" +ss.getDamageCapacity());
        killsLabel.setText("Kills: " + ss.getKills());

        addVIPs();
    	VIPinfoLabel.setVisible(true);
    	VIPInfoTextArea.setVisible(true);
    	scrollPane2.setVisible(true);

        repaint();
      }

    private void addVIPs(){
    	List<VIP> allVIPs = player.getGalaxy().findAllVIPsOnShip(currentss);
    	if (allVIPs.size() == 0){
    		VIPInfoTextArea.setText("None");
    	}else{
    		VIPInfoTextArea.setText("");
    		for (VIP aVIP : allVIPs){
    			VIPInfoTextArea.append(aVIP.getName() + "\n");
    		}
    	}
    }

    private void emptyList(){
        DefaultListModel dlm = (DefaultListModel)shiplist.getModel();
        dlm.removeAllElements();
    }
    
    public String getId(){
    	return id;
    }
    
    public void updateData(){	  	
    }

}
