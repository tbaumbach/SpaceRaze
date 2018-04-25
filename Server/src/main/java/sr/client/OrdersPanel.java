//Title:        SpaceRaze Client
//Author:       Paul Bodin
//Description:  Java-klienten för SpazeRaze.

package sr.client;

import java.util.List;

import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.scrollable.TextAreaPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.enums.DiplomacyLevel;
import sr.world.Building;
import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.VIP;
import sr.world.diplomacy.DiplomacyChange;
import sr.world.diplomacy.DiplomacyOffer;
import sr.world.orders.Expense;
import sr.world.orders.Orders;
import sr.world.orders.PlanetNotesChange;
import sr.world.orders.ResearchOrder;
import sr.world.orders.ShipMovement;
import sr.world.orders.ShipToCarrierMovement;
import sr.world.orders.TaxChange;
import sr.world.orders.TroopToCarrierMovement;
import sr.world.orders.TroopToPlanetMovement;
import sr.world.orders.VIPMovement;

@SuppressWarnings("serial")
public class OrdersPanel extends SRBasePanel implements SRUpdateablePanel{
  private String id;
  private Orders orders;
  private TextAreaPanel infoarea;
//  private JScrollPane scrollPane;
  private SRLabel title;
  private Galaxy g;
  private Player aPlayer;
  private String sepLine = "----------------------\n";

  public OrdersPanel(Orders orders, String id, Player p) {
    this.id = id;
    this.orders = orders;
    this.setLayout(null);
    g = p.getGalaxy();
    this.aPlayer = p;

    title = new SRLabel("Current orders for turn " + p.getGalaxy().getTurn());
    title.setBounds(10,10,200,15);
    add(title);

    infoarea = new TextAreaPanel();
    infoarea.setBounds(10,35,835,590);
    add(infoarea);
    
  }

  public String getId(){
    return id;
  }

  public void updateData(){
    infoarea.setText("");
    // add expenses
    List<Expense> temp = orders.getExpenses();
    if (temp.size() > 0){
	    infoarea.append("Expenses\n");
	    infoarea.append(sepLine);
	    for (int i = 0; i < temp.size(); i++){
	      Expense tempExpense = temp.get(i);
	      infoarea.append(tempExpense.getText(g,aPlayer.getOrders()) + "\n");
	    }
	    infoarea.append("\n");
    }

    // add ship movements
	List<ShipMovement> shipMovements = orders.getShipMoves();
    if (shipMovements.size() > 0){
    	infoarea.append("Ship movements\n");
    	infoarea.append(sepLine);
    	for (int j = 0; j < shipMovements.size(); j++){
    		ShipMovement tempShipMovement = shipMovements.get(j);
    		infoarea.append(tempShipMovement.getText(g) + "\n");
    	}
    	List<ShipToCarrierMovement> stcmList = orders.getShipToCarrierMoves();
    	for (ShipToCarrierMovement movement : stcmList) {
    		infoarea.append(movement.getText(g) + "\n");
    	}
    	infoarea.append("\n");
    }

    if (g.hasTroops()){
    	List<TroopToCarrierMovement> ttcm = orders.getTroopToCarrierMoves();
    	List<TroopToPlanetMovement> ttpm = orders.getTroopToPlanetMoves();
        if ((ttcm.size() > 0) | (ttpm.size() > 0)){
        	// add troop movements
        	infoarea.append("Troop movements\n");
        	infoarea.append(sepLine);
        	for (TroopToCarrierMovement troopToCarrierMovement : ttcm) {
        		infoarea.append(troopToCarrierMovement.getText(g) + "\n");
        	}
        	for (TroopToPlanetMovement troopToPlanetMovement : ttpm) {
        		infoarea.append(troopToPlanetMovement.getText(g) + "\n");
        	}
        	infoarea.append("\n");
        }
    }
    
    // add changes in planet visibility
	List<String> planetNames = orders.getPlanetVisibilities();
    if (planetNames.size() > 0){
    	infoarea.append("Open/closed planets" + "\n");
    	infoarea.append(sepLine);
    	for (int k = 0; k < planetNames.size(); k++){    	
    		Planet tempPlanet = g.findPlanet(planetNames.get(k));
    		infoarea.append("Change planet " + tempPlanet.getName() + " to ");
    		if (tempPlanet.isOpen()){  // change to closed
    			infoarea.append("closed");
    		}else{ // change to open
    			infoarea.append("open");
    		}
    		infoarea.append(" status." + "\n");
    	}
    	infoarea.append("\n");
    }

    // add changes in abandoning planets
	planetNames = orders.getAbandonPlanets();
    if (planetNames.size() > 0){
    	infoarea.append("Abandon planets" + "\n");
    	infoarea.append(sepLine);
    	for (int l = 0; l < planetNames.size(); l++){
    		Planet tempPlanet = g.findPlanet(planetNames.get(l));
    		infoarea.append("Planet " + tempPlanet.getName() + " is to be abandoned." + "\n");
    	}
    	infoarea.append("\n");
    }

    // add ships do be selfdestroyed
	List<Integer> shipIds = orders.getShipSelfDestructs();
    if (shipIds.size() > 0){
    	infoarea.append("Selfdestruct ships" + "\n");
    	infoarea.append(sepLine);
    	for (int m = 0; m < shipIds.size(); m++){
    		Spaceship tempss = g.findSpaceship(shipIds.get(m));
    		infoarea.append("Spaceship " + tempss.getName() + " is to be destroyed." + "\n");
    	}
    	infoarea.append("\n");
    }
    
    //  add VIPs do be selfdestroyed
	List<Integer> tempVIPs = orders.getVIPSelfDestructs();
    if (tempVIPs.size() > 0){
    	infoarea.append("Selfdestruct VIPs" + "\n");
    	infoarea.append(sepLine);
    	for (int v = 0; v < tempVIPs.size(); v++){
    		VIP tempVIP = g.findVIP( tempVIPs.get(v));
    		infoarea.append("VIP " + tempVIP.getName() + " is to be retired." + "\n");
    	}
    	infoarea.append("\n");
    }

    // add ships do be selfdestroyed
	List<Integer> tempBuildings = orders.getBuildingSelfDestructs();
    if (tempBuildings.size() > 0){
    	infoarea.append("Selfdestruct Buildings" + "\n");
    	infoarea.append(sepLine);
    	for (int n = 0; n < tempBuildings.size(); n++){
    		Building tempBuilding = g.findBuilding(tempBuildings.get(n), aPlayer);
    		infoarea.append("Building " + tempBuilding.getBuildingType().getName() + " at " + tempBuilding.getLocation().getName() + " is to be destroyed." + "\n");
    	}
    	infoarea.append("\n");
    }

    // add ships do change screened status
	shipIds = orders.getScreenedShips();
    if (shipIds.size() > 0){
    	infoarea.append("Screen spaceships" + "\n");
    	infoarea.append(sepLine);
    	for (int p = 0; p < shipIds.size(); p++){
    		Spaceship tempss = g.findSpaceship(shipIds.get(p));
    		if (tempss.getLocation() != null){
    			infoarea.append("Your ship " + tempss.getName() + " at " + tempss.getLocation().getName() + "  is to change its screened status to " + !tempss.getScreened() + "\n");
    		}else{
    			infoarea.append("Your ship " + tempss.getName() + " in deep space is to change its screened status to " + !tempss.getScreened() + "\n");
    		}
    	}
    	infoarea.append("\n");
    }

    // add ship movements
	List<VIPMovement> vipMovementa = orders.getVIPMoves();
    if (vipMovementa.size() > 0){
    	infoarea.append("VIP movements\n");
    	infoarea.append(sepLine);
    	for (int s = 0; s < vipMovementa.size(); s++){
    		VIPMovement tempVIPMovement = vipMovementa.get(s);
    		infoarea.append(tempVIPMovement.getText(g) + "\n");
    	}
    	infoarea.append("\n");
    }
    
    // add ReserchOrders
	List<ResearchOrder> researchOrders = orders.getResearchOrders();
    if (researchOrders.size() > 0){
    	infoarea.append("Researchs\n");
    	infoarea.append(sepLine);
    	for (int i = 0; i < researchOrders.size(); i++){
    		ResearchOrder tempResearch = researchOrders.get(i);
    		infoarea.append(tempResearch.getText() + "\n");
    	}
    	infoarea.append("\n");
    }

    // diplomacy orders
	List<DiplomacyOffer> offersList = orders.getDiplomacyOffers();
	List<DiplomacyChange> changesList = orders.getDiplomacyChanges();
    if ((offersList.size() > 0) | (changesList.size() > 0)){
    	infoarea.append("Diplomacy orders\n");
    	infoarea.append(sepLine);
    	for (DiplomacyOffer anOffer : offersList) {
    		if (anOffer.getSuggestedLevel() == DiplomacyLevel.VASSAL){
    			infoarea.append("Make offer to " + anOffer.getOtherPlayer(g).getGovenorName() + " for you to become his lord and he your vassal\n");
    		}else
    		if (anOffer.getSuggestedLevel() == DiplomacyLevel.LORD){
    			infoarea.append("Make offer to " + anOffer.getOtherPlayer(g).getGovenorName() + " for him to become your lord and you his vassal\n");
    		}else{ // other offer
    			infoarea.append("Make offer to " + anOffer.getOtherPlayer(g).getGovenorName() + " for " + anOffer.getSuggestedLevel().toString() + "\n");
    		}
    	}
        if (offersList.size() > 0){
        	infoarea.append("\n");
        }
    	for (DiplomacyChange aChange : changesList) {
    		if (aChange.getNewLevel() == DiplomacyLevel.VASSAL){
    			infoarea.append("Make change for " + aChange.getOtherPlayer(g).getGovenorName() + " that you become his lord and he your vassal\n");
    		}else
    		if (aChange.getNewLevel() == DiplomacyLevel.LORD){
    			infoarea.append("Make change for " + aChange.getOtherPlayer(g).getGovenorName() + " to become your lord and you his vassal\n");
    		}else{ // other change
    			infoarea.append("Make change for " + aChange.getOtherPlayer(g).getGovenorName() + " to " + aChange.getNewLevel().toString() + "\n");
    		}
    	}
    	infoarea.append("\n");
    }

    // Vassal tax changes
    List<TaxChange> taxChanges = orders.getTaxChanges();
    if (taxChanges.size() > 0){
    	infoarea.append("Tax Changes\n");
    	infoarea.append(sepLine);
    	for (TaxChange change : taxChanges) {
        	infoarea.append("Change tax from " + change.getPlayerName() + " to " + change.getAmount() + "\n");
		}
    	infoarea.append("\n");
    }

    // Change planet notes
    List<PlanetNotesChange> notesChanges = orders.getPlanetNotesChanges();
    if (notesChanges.size() > 0){
    	infoarea.append("Planet notes Changes\n");
    	infoarea.append(sepLine);
    	for (PlanetNotesChange change : notesChanges) {
        	infoarea.append(change.getText() + "\n");
		}
    	infoarea.append("\n");
    }

    // other orders (i.e. abandon game)
    if (orders.isAbandonGame()){
    	infoarea.append("Other orders\n");
    	infoarea.append(sepLine);
    	if (orders.isAbandonGame()){
    		infoarea.append("Abandon game\n");
    	}
    	infoarea.append("\n");
    }

    if (infoarea.getModel().size() == 0){
    	infoarea.append("\n");
    	infoarea.append("No orders exist\n");
    }

    if (isVisible()){
    	update(getGraphics());
    	paintChildren(getGraphics());
    }
  }

}