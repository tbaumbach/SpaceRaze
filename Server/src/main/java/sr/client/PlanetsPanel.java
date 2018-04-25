package sr.client;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTable;
import sr.client.components.SRTableHeader;
import sr.client.components.SRTextArea;
import sr.client.interfaces.SRUpdateablePanel;
import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.Troop;
import sr.world.VIP;

/**
 * 
 * @author WMRAKLI
 *
 */
@SuppressWarnings("serial")
public class PlanetsPanel extends SRBasePanel implements SRUpdateablePanel, ListSelectionListener{
	private SRTable shipTable;
	private SRTableHeader tableHeader;
	private SRScrollPane tablePanel;
	private SRLabel title;
	private String id;
	private Player player;
	private Galaxy g;
	private List<Planet> ships;
	private SRTextArea VIPInfoTextArea,TroopInfoTextArea;
	private SRLabel VIPinfoLabel,TroopInfoLabel;
	private Spaceship currentss;
	private JScrollPane scrollPane1,scrollPane2;
	private GameGUIPanel gameGuiPanel;
	
	public PlanetsPanel(Galaxy g, String newId, Player p, GameGUIPanel gameGuiPanel) {
		this.setLayout(null);
		this.id = newId;
		this.player = p;
		this.g = g;
		this.gameGuiPanel = gameGuiPanel;
		fillTableList();
	
	    title = new SRLabel("Planets");
	    title.setBounds(10,10,200,15);
	    add(title);
	    
	    // Troops info textarea
	    TroopInfoLabel = new SRLabel("Troops on this ship");
	    TroopInfoLabel.setBounds(10,320,120,15);
	    TroopInfoLabel.setVisible(false);
	    add(TroopInfoLabel);
	    
	    TroopInfoTextArea = new SRTextArea();
	    TroopInfoTextArea.setEditable(false);
	    TroopInfoTextArea.setVisible(false);
	    
	    scrollPane1 = new SRScrollPane(TroopInfoTextArea);
	    scrollPane1.setBounds(10,340,200,60);
	    scrollPane1.setVisible(false);
	    add(scrollPane1);

	    // VIPs info textarea
	    VIPinfoLabel = new SRLabel("VIPs on this ship");
	    VIPinfoLabel.setBounds(250,320,120,15);
	    VIPinfoLabel.setVisible(false);
	    add(VIPinfoLabel);
	    
	    VIPInfoTextArea = new SRTextArea();
	    VIPInfoTextArea.setEditable(false);
	    VIPInfoTextArea.setVisible(false);
	    
	    scrollPane2 = new SRScrollPane(VIPInfoTextArea);
	    scrollPane2.setBounds(250,340,200,60);
	    scrollPane2.setVisible(false);
	    add(scrollPane2);

	}
	
	private void fillTableList(){
		ships = this.g.getPlayersPlanets(player);
		int nrShips = ships.size();
		shipTable = new SRTable(nrShips, 7);
		shipTable.setAutoResizeMode(1);
		shipTable.setRowHeight(18);
		shipTable.setColumnSelectionAllowed(false);
		shipTable.setRowSelectionAllowed(true);
		shipTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shipTable.getSelectionModel().addListSelectionListener(this);
		
		for (int i = 0; i < nrShips; i++) {
			Planet aPlanet = ships.get(i);
			String tmpOpen ="No";
			String tmpBesieged ="No";
			
			if (aPlanet.isOpen())
				tmpOpen = "Yes";
			
			if(aPlanet.isBesieged())
				tmpBesieged = "Yes";
			
			shipTable.setValueAt(aPlanet.getName(), i, 0);
			shipTable.setValueAt(aPlanet.getPopulation(), i, 1);
			shipTable.setValueAt(aPlanet.getResistance(), i, 2);
			shipTable.setValueAt(tmpOpen , i, 3);
			shipTable.setValueAt(tmpBesieged, i, 4);
			shipTable.setValueAt(aPlanet.getBuildings().size(), i, 5);
			shipTable.setValueAt(aPlanet.hasSpacePort(), i, 6);
		}
		
		
	/*	for (int i = 0; i < nrShips; i++) {
			Spaceship aShip = ((Spaceship)ships.get(i));
			shipTable.setValueAt(aShip.getName(), i, 0);
			
			if (aShip.getCarrierLocation() != null){
				shipTable.setValueAt(aShip.getCarrierLocation().getName(), i, 1);
			}else
			if (aShip.getLocation()!= null) {
				Planet planet = aShip.getLocation();
				shipTable.setValueAt(planet.getName(), i, 1);
			}else{
				shipTable.setValueAt("Retreating", i, 1);
			}

			if (!aShip.isCanMove()){
				shipTable.setValueAt("-", i, 2);
			}else
			if (player.checkShipMove(aShip)){
				shipTable.setValueAt(player.getShipDestinationName(aShip), i, 2);
			}
			
			shipTable.setValueAt(aShip.getHullStrength() + "%", i, 3);
			
		}
		*/
		TableColumnModel model = shipTable.getColumnModel();
		model.getColumn(0).setHeaderValue("Name");
		model.getColumn(0).setPreferredWidth(250);
		model.getColumn(1).setHeaderValue("Production");
		model.getColumn(2).setHeaderValue("Resistance");
		model.getColumn(3).setHeaderValue("Open");
		model.getColumn(4).setHeaderValue("Besieged");
		model.getColumn(5).setHeaderValue("Buildings");
		model.getColumn(6).setHeaderValue("Spaceport");
		
		shipTable.setAutoCreateRowSorter(true);
		
		tableHeader = new SRTableHeader(model);
		tableHeader.setOpaque(false);
		tableHeader.setReorderingAllowed(true);
		tableHeader.setTable(shipTable);
		tableHeader.setVisible(true);
		shipTable.setTableHeader(tableHeader);

		tablePanel = new SRScrollPane(shipTable);
		int rowHeight = 18;
		int tableHeight = 15*rowHeight;
		if (nrShips < 14){
			tableHeight = (nrShips + 1)*rowHeight;
		}
		tablePanel.setBounds(10,40,800,tableHeight);
		tablePanel.setBackground(StyleGuide.colorBackground);
		tablePanel.setForeground(StyleGuide.colorBackground);
		tablePanel.setVisible(true);
		add(tablePanel);  
	}
	
	// TODO (Paul) ej använd, ska vi visa trupper i planetlistan?
    @SuppressWarnings("unused")
	private void addTroops(){
    	List<Troop> allTroops = player.getGalaxy().findAllTroopsOnShip(currentss);
    	if (allTroops.size() == 0){
    		TroopInfoTextArea.setText("None");
    	}else{
    		TroopInfoTextArea.setText("");
    		for (Troop aTroop : allTroops) {
    			TroopInfoTextArea.append(aTroop.getUniqueName() + "\n");
			}
    	}
    }

	// TODO (Paul) ej använd, ska vi visa VIP:ar i planetlistan?
    @SuppressWarnings("unused")
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
	
    private void showSpaceship(int rowIndex){
    	String shipName = (String)shipTable.getValueAt(rowIndex, 0);
    	//Planet pp = g.findPlanet(shipName);
    	gameGuiPanel.showPlanet(shipName);
    	
    	
    	//Spaceship ss = g.findSpaceship(shipName, player);
    	//currentss = ss;
    	
    	// is repaint needed?
        //repaint();
        //if (true){ // chk...
        //	Planet planetlocation = currentss.getLocation();
        //	if (planetlocation == null){
        //		planetlocation = currentss.getCarrierLocation().getLocation();
        //	}
        //	gameGuiPanel.showShipOnPlanet(planetlocation.getName(),currentss);
        //}
    }

	public void valueChanged(ListSelectionEvent e) {
		showSpaceship(shipTable.getSelectedRow());
	}

	public String getId(){
		return id;
	}
	
	private void clearTable(){
		shipTable.removeAll();
		shipTable = null;
		remove(tablePanel);
		tablePanel = null;
        // hide components
    	TroopInfoLabel.setVisible(false);
    	TroopInfoTextArea.setVisible(false);
    	scrollPane1.setVisible(false);
    	VIPinfoLabel.setVisible(false);
    	VIPInfoTextArea.setVisible(false);
    	scrollPane2.setVisible(false);
	}
	
	public void updateData(){
		clearTable();
		fillTableList();
	}
	
}