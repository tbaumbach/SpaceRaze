package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.logging.Logger;
import sr.world.BlackMarket;
import sr.world.BlackMarketBid;
import sr.world.BlackMarketOffer;
import sr.world.Player;

/**
 * @author Paul Bodin
 * 
 * New version of the black market panel using a modal popup
 */

public class BlackMarketPanel2 extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel, ActionListener{
  private static final long serialVersionUID = 1L;
  private SRButton newButton,editButton,deleteButton,detailsButton;
  private String id;
  private Player p;
  private SpaceRazeApplet client;
  private ListPanel allOffersList;
  private BlackMarketPopupPanel popup;
  private List<BlackMarketOffer> offersInList;
  private BlackMarket blackMarket;

  public BlackMarketPanel2(Player p,SpaceRazeApplet client, String id, BlackMarket blackMarket) {
    this.p = p;
    this.client = client;
    this.id = id;
    this.blackMarket = blackMarket;

    offersInList = new ArrayList<BlackMarketOffer>();

    allOffersList = new ListPanel();
    allOffersList.setBounds(10, 10, 350, 350);
    allOffersList.setListSelectionListener(this);
    addOffers();
    allOffersList.updateScrollList();
    add(allOffersList);

    newButton = new SRButton("New Bid");
    newButton.setBounds(380,10,100,20);
    newButton.addActionListener(this);
    add(newButton);
    newButton.setEnabled(false);

    editButton = new SRButton("Edit Bid");
    editButton.setBounds(380,40,100,20);
    editButton.addActionListener(this);
    add(editButton);
    editButton.setEnabled(false);

    deleteButton = new SRButton("Delete Bid");
    deleteButton.setBounds(380,70,100,20);
    deleteButton.addActionListener(this);
    add(deleteButton);
    deleteButton.setEnabled(false);
    
    detailsButton = new SRButton("View Details");
    detailsButton.setBounds(380,100,100,20);
    detailsButton.addActionListener(this);
    detailsButton.setVisible(false);
    add(detailsButton);
  }

  private void addOffers(){
    List<BlackMarketOffer> allOffers = blackMarket.getCurrentOffers();
    DefaultListModel dlm = (DefaultListModel)allOffersList.getModel();
    for (int i = 0; i < allOffers.size(); i++){
      BlackMarketOffer anOffer = (BlackMarketOffer)allOffers.get(i);
      BlackMarketBid tempBid = p.getBidToOffer(anOffer);
      offersInList.add(anOffer);
      if (tempBid != null){
      	if (anOffer.isHotStuff() || anOffer.isShipBlueprint()){
      		dlm.addElement(anOffer.getString() + " (bid: " + tempBid.getCost() + ")");
      	}else{
      		dlm.addElement(anOffer.getString() + " (bid: " + tempBid.getCost() + ", destination:  " + tempBid.getDestinationString() + ")");
      	}
      }else{
      	dlm.addElement(anOffer.getString());
      }
    }
  }
  
  public void valueChanged(ListSelectionEvent lse){
  	showButtons(allOffersList.getSelectedIndex());
  }
  
  private void showButtons(int index){
  	Logger.fine("showButtons: " + index);
  	BlackMarketOffer tempOffer = (BlackMarketOffer)offersInList.get(index);
    BlackMarketBid tempBid = p.getBidToOffer(tempOffer);
	int amount = 0;
	if (tempBid != null){
		amount = tempBid.getCost();
	}
	if (amount > 0){
	  	// if amount is > 0, bid exists
	    newButton.setEnabled(false);
	    editButton.setEnabled(true);
	    deleteButton.setEnabled(true);
	}else{
		if (tempOffer.isVIP()){
			// if amount is = 0, no bid exists
			if (p.getFaction().getAlignment().canHaveVip(tempOffer.getVIPType().getAlignment().getName())){
				newButton.setEnabled(true);
			}else{		
				newButton.setEnabled(false);
			}
		}else{
    		newButton.setEnabled(true);
		}
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}
	if(tempOffer.isShipBlueprint() || tempOffer.isShip() || tempOffer.isVIP() || tempOffer.isTroop()){
		detailsButton.setVisible(true);
	}else{
		detailsButton.setVisible(false);
	}
		
  }

  public void actionPerformed(ActionEvent ae){
  	Logger.fine("actionPerformed: " + ae.getActionCommand() + " " + ae.getSource().getClass().getName());
  	String action = ae.getActionCommand();
  	if (action.equalsIgnoreCase("View Details")){
  		BlackMarketOffer currentOffer = (BlackMarketOffer)offersInList.get(allOffersList.getSelectedIndex());
  		if(currentOffer != null && (currentOffer.isShip() | currentOffer.isShipBlueprint())){
  			client.showShiptypeDetails(currentOffer.getShipType().getName(), "All (sort by name)");
  		}else if(currentOffer != null && currentOffer.isVIP()){
  			client.showVIPTypeDetails(currentOffer.getVIPType().getName(), "All");
  		}else if(currentOffer != null && currentOffer.isTroop()){
  			client.showTroopTypeDetails(currentOffer.getString(), "All (sort by name)");
  		}
  	}else
  	if (action.equalsIgnoreCase("cancel")){
  	}else
    if (action.equalsIgnoreCase("ok")){
    	// perform action
    	performPopupAction();
  	}else
    if (action.equalsIgnoreCase("delete bid")){
    	deleteBid();
    }else{
    	openPopup(action);
  	}
  }
  
  private void deleteBid(){
  	Logger.fine("deleteBid called");
  	BlackMarketOffer currentOffer = (BlackMarketOffer)offersInList.get(allOffersList.getSelectedIndex());
  	// set bid to zero to remove it...
    p.getOrders().addNewBlackMarketBid(0,currentOffer,null,p);
    client.updateTreasuryLabel();
  	updateGiftPanel();
  }

  private void performPopupAction(){
  	Logger.fine("performPopupAction called");
  	BlackMarketOffer currentOffer = (BlackMarketOffer)offersInList.get(allOffersList.getSelectedIndex());
  	int tempSum = popup.getSum();
  	String tmpDestination = "";
    if (currentOffer.isHotStuff() | currentOffer.isShipBlueprint()){
        p.getOrders().addNewBlackMarketBid(tempSum,currentOffer,null,p);
    }else{
      	tmpDestination = popup.getDestination();
        p.getOrders().addNewBlackMarketBid(tempSum,currentOffer,p.getGalaxy().findPlanet(tmpDestination),p);
    }
    client.updateTreasuryLabel();
  	updateGiftPanel();
  }

  private void updateGiftPanel(){
  	int index = allOffersList.getSelectedIndex();
  	emptyList();
  	addOffers();
  	update(getGraphics());
  	allOffersList.update(allOffersList.getGraphics());
  	showButtons(index);
  }
  
  private void openPopup(String actionCommand){
  	Logger.fine("openPopup called: " + actionCommand);
  	BlackMarketOffer tempOffer = (BlackMarketOffer)offersInList.get(allOffersList.getSelectedIndex());
    BlackMarketBid tempBid = p.getBidToOffer(tempOffer);

    if (actionCommand.equalsIgnoreCase("edit bid")){
    	popup = new BlackMarketPopupPanel(actionCommand,this,p,tempOffer,tempBid);
    }else{
    	popup = new BlackMarketPopupPanel(actionCommand,this,p,tempOffer);
    }
//	popup.setPopupSize(400,200);
	popup.open(this);
	popup.setSumfieldFocus();
  }

  private void emptyList(){
    DefaultListModel dlm = (DefaultListModel)allOffersList.getModel();
    dlm.removeAllElements();
    offersInList = new ArrayList<BlackMarketOffer>();
  }

  public String getId(){
    return id;
  }

  public void updateData(){
  }

}