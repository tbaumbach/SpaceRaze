/*
 * Created on 2005-feb-25
 */
package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import sr.client.components.BasicPopupPanel;
import sr.client.components.ComboBoxPanel;
import sr.client.components.SRLabel;
import sr.client.components.SRTextField;
import sr.general.logging.Logger;
import sr.world.BlackMarketBid;
import sr.world.BlackMarketOffer;
import sr.world.Planet;
import sr.world.Player;
import sr.world.comparator.PlanetNameComparator;

/**
 * Popuppanel used for creation and edits of black market bids
 */
public class BlackMarketPopupPanel extends BasicPopupPanel implements ActionListener, ShowMapPlanet {
	private static final long serialVersionUID = 1L;
	private SRLabel offerSumLabel, offerDestinationLabel, offerLabel, offerNameLabel, mapLabel;
    private SRTextField offerSumTextField;
    private ComboBoxPanel offerDestinationChoice;
    private Player player;
    private MapCanvas mapCanvas;

	public BlackMarketPopupPanel(String title, ActionListener listener, Player p, BlackMarketOffer offer, BlackMarketBid bid){
		this(title,listener,p,offer);
		// set amount
		offerSumTextField.setText(String.valueOf(bid.getCost()));
		// set destination
        if (!offer.isHotStuff() & !offer.isShipBlueprint()){
        	offerDestinationChoice.setSelectedItem(bid.getDestination());
        	Logger.finer("set ok enabled");
        	okBtn.setEnabled(true);
        	mapCanvas.setInitialSetCenter(bid.getDestination());
        	mapCanvas.setChosenCoors(bid.getDestination());
        }
	}
	
	public BlackMarketPopupPanel(String title, ActionListener listener, Player p, BlackMarketOffer offer){
		super(title,listener);
		this.player = p;
		
	    offerLabel = new SRLabel("Item for sale:");
	    offerLabel.setBounds(10,40,100,20);
	    add(offerLabel);

	    offerNameLabel = new SRLabel(offer.getString());
	    offerNameLabel.setBounds(130,40,150,20);
	    add(offerNameLabel);

	    offerSumLabel = new SRLabel("Bid for item:");
	    offerSumLabel.setBounds(10,70,100,20);
	    add(offerSumLabel);

	    offerSumTextField = new SRTextField();
	    offerSumTextField.setBounds(130,70,150,20);
	    add(offerSumTextField);

	    if (!offer.isHotStuff() & !offer.isShipBlueprint()){
	    	offerDestinationLabel = new SRLabel("Destination:");
	    	offerDestinationLabel.setBounds(10,100,100,20);
	    	add(offerDestinationLabel);
	    	
	    	offerDestinationChoice = new ComboBoxPanel();
	    	offerDestinationChoice.setBounds(130,100,150,20);
	    	offerDestinationChoice.addActionListener(this);
	    	addDestinations();
	    	add(offerDestinationChoice);
	    	
            Logger.finer("set ok disabled");
	    	okBtn.setEnabled(false);

	    	mapLabel = new SRLabel("Map (click to select destination):");
	    	mapLabel.setBounds(10,130,380,20);
	    	add(mapLabel);

	    	mapCanvas = new MapCanvas(p,this,true);
	        mapCanvas.setPlanets(p.getGalaxy().getPlanets(),p);
	        mapCanvas.setSpaceships(p.getGalaxy().getPlayersSpaceships(p));
	        mapCanvas.setOwnVips(p.getGalaxy().getPlayersVips(p));
	        mapCanvas.setOwnTroops(p.getGalaxy().getPlayersTroops(p));
	        mapCanvas.setOthersVips(p.getGalaxy().getAllVIPs());
	        mapCanvas.setConnections(p.getGalaxy().getPlanetConnections());
	        mapCanvas.computeNewOrigo();
		    mapCanvas.setBounds(10, 155, 380, 300);
		    mapCanvas.setInitialZoom(-20);
		    mapCanvas.setInitialSetCenter(player.getHomeplanet().getName());
			mapCanvas.setChosenCoors("");
		    add(mapCanvas);

		    setPopupSize(400,500);
	    }else{
	    	setPopupSize(400,200);
	    }
	    
	}
	
	private void addDestinations(){
		offerDestinationChoice.addItem("None");
		List<Planet> playersPlanets = player.getGalaxy().getPlayersPlanets(player);
		Collections.sort(playersPlanets,new PlanetNameComparator<Planet>());
		for (Planet aPlanet : playersPlanets) {
			if (!aPlanet.isBesieged()){
				offerDestinationChoice.addItem(aPlanet.getName());
			}
		}
	}
	  
	public void setSumfieldFocus(){
		offerSumTextField.selectAll();
	    offerSumTextField.requestFocus();
	}
	
	public int getSum(){
		int sum = 0;
		try{
			sum = Integer.parseInt(offerSumTextField.getText());
		}
		catch(NumberFormatException nfe){
			Logger.fine("Could not parse sum, return 0");
		}
		return sum;
	}

	public String getDestination(){
		return offerDestinationChoice.getSelectedItem();
	}
	
	public void actionPerformed(ActionEvent ae){
		Object o = ae.getSource();
		if (o instanceof ComboBoxPanel){
			if ((ComboBoxPanel)ae.getSource() == offerDestinationChoice){
				String dest = getDestination();
				if (dest.equalsIgnoreCase("none")){
					okBtn.setEnabled(false);
					mapCanvas.setChosenCoors("");
				}else{
					okBtn.setEnabled(true);
					mapCanvas.setChosenCoors(dest);
					mapCanvas.doSetCenter(dest);
					mapCanvas.doChange(0,0,-40,0,0,0);
				}
			}
		}else{
			super.actionPerformed(ae);
			//clearPopup();
		}
	}

	public void showPlanet(String aPlanetName) {
//		System.out.println("planet: " + aPlanetName);
		
		if (offerDestinationChoice.exist(aPlanetName)){
			offerDestinationChoice.setSelectedItem(aPlanetName);
			offerDestinationChoice.paintAll(offerDestinationChoice.getGraphics());
			okBtn.setEnabled(true);
		}
		mapCanvas.update(mapCanvas.getGraphics());
	}

	public void showPlanetShips(String aPlanetName) {
		showPlanet(aPlanetName);
	}

}
