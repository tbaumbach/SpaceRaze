package sr.client;

import javax.swing.JScrollPane;

import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.interfaces.SRUpdateablePanel;
import sr.world.GameWorld;

@SuppressWarnings("serial")
public class GameWorldPanel extends SRBasePanel implements SRUpdateablePanel{
	private SRLabel neutralSize1Lbl,neutralSize2Lbl,neutralSize3Lbl;
    private SRLabel fullNameLbl,descriptionLbl,neutralTroopLbl;
    private SRLabel troopLbl;
	private SRLabel neutralSize1Lbl2,neutralSize2Lbl2,neutralSize3Lbl2, neutralTroopLbl2;
    private SRLabel fullNameLbl2,troopLbl2;
    private SRLabel closedNeutralsLbl,closedNeutralsLbl2,razedLbl,razedLbl2;
    private String id;
    private GameWorld gw;
    
    //TODO baseBombardmentDamage borde visas här om vi inte bestämmer att vi har samma i alla spelen.

    // used for computing components location
    private int yPosition = 5;
    private final int yInterval = 20;
    private int column1X = 10;
    private int column2X = 210;
    private int column3X = 420;
    private int column4X = 630;
    
    private int column1width = 200;
    
    private SRTextArea descriptionTextArea;
    private JScrollPane scrollPane;
    
    public GameWorldPanel(GameWorld gw, String id){
      this.id = id;
      this.gw = gw;

      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
      
      int cHeight = 18;

      fullNameLbl = new SRLabel();
      fullNameLbl.setBounds(column1X,yPosition,190,cHeight);
      add(fullNameLbl);
      fullNameLbl2 = new SRLabel();
      fullNameLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(fullNameLbl2);

      troopLbl = new SRLabel();
      troopLbl.setBounds(column1X,newLine(),column1width,cHeight);
      troopLbl.setToolTipText("Do the game world have troop units");
      add(troopLbl);
      troopLbl2 = new SRLabel();
      troopLbl2.setBounds(column2X,yPosition,180,cHeight);
      troopLbl2.setToolTipText("Do the game world have troop units");
      add(troopLbl2);

      closedNeutralsLbl = new SRLabel();
      closedNeutralsLbl.setBounds(column1X,newLine(),column1width,cHeight);
      closedNeutralsLbl.setToolTipText("Chanse that the plantet is closed in the start of the game");
      add(closedNeutralsLbl);
      closedNeutralsLbl2 = new SRLabel();
      closedNeutralsLbl2.setBounds(column2X,yPosition,200,cHeight);
      closedNeutralsLbl2.setToolTipText("Chanse that the plantet is closed in the start of the game");
      add(closedNeutralsLbl2);

      razedLbl = new SRLabel();
      razedLbl.setBounds(column1X,newLine(),column1width,cHeight);
      razedLbl.setToolTipText("Chanse that the plantet is razed in the start of the game");
      add(razedLbl);
      razedLbl2 = new SRLabel();
      razedLbl2.setBounds(column2X,yPosition,200,cHeight);
      razedLbl2.setToolTipText("Chanse that the plantet is razed in the start of the game");
      add(razedLbl2);
      
      // next colum
      yPosition = 5;
      
      neutralSize1Lbl = new SRLabel();
      neutralSize1Lbl.setBounds(column3X,yPosition,column1width,cHeight);
      neutralSize1Lbl.setToolTipText("ShipType that defends small neutral planets");
      add(neutralSize1Lbl);
      neutralSize1Lbl2 = new SRLabel();
      neutralSize1Lbl2.setBounds(column4X,yPosition,200,cHeight);
      neutralSize1Lbl2.setToolTipText("ShipType that defends small neutral planets");
      add(neutralSize1Lbl2);

      neutralSize2Lbl = new SRLabel();
      neutralSize2Lbl.setBounds(column3X,newLine(),column1width,cHeight);
      neutralSize2Lbl.setToolTipText("ShipType that defends medium neutral planets");
      add(neutralSize2Lbl);
      neutralSize2Lbl2 = new SRLabel();
      neutralSize2Lbl2.setBounds(column4X,yPosition,200,cHeight);
      neutralSize2Lbl2.setToolTipText("ShipType that defends medium neutral planets");
      add(neutralSize2Lbl2);

      neutralSize3Lbl = new SRLabel();
      neutralSize3Lbl.setBounds(column3X,newLine(),column1width,cHeight);
      neutralSize3Lbl.setToolTipText("ShipType that defends large neutral planets");
      add(neutralSize3Lbl);
      neutralSize3Lbl2 = new SRLabel();
      neutralSize3Lbl2.setBounds(column4X,yPosition,200,cHeight);
      neutralSize3Lbl2.setToolTipText("ShipType that defends large neutral planets");
      add(neutralSize3Lbl2);
      
      neutralTroopLbl = new SRLabel();
      neutralTroopLbl.setBounds(column3X,newLine(),column1width,cHeight);
      neutralTroopLbl.setToolTipText("TroopType that defends neutral planets");
      add(neutralTroopLbl);
      neutralTroopLbl2 = new SRLabel();
      neutralTroopLbl2.setBounds(column4X,yPosition,200,cHeight);
      neutralTroopLbl2.setToolTipText("TroopType that defends neutral planets");
      add(neutralTroopLbl2);
      
      
      // GW description textarea
      descriptionLbl = new SRLabel();
      descriptionLbl.setBounds(column1X,120,column1width,cHeight);
      add(descriptionLbl);

      descriptionTextArea = new SRTextArea();
     // descriptionTextArea.setBorder(null);
      descriptionTextArea.setEditable(false);
 
      scrollPane = new SRScrollPane(descriptionTextArea);
      scrollPane.setBounds(column1X,140,830,445);
      add(scrollPane);
      
      showGameWorld();
    }
    
    private int newLine(){
    	yPosition = yPosition + yInterval;
    	return yPosition;
    }

    public void showGameWorld(){
    	fullNameLbl.setText("GameWorld Name: ");
    	descriptionLbl.setText("Description: ");
    	troopLbl.setText("Have troop units: ");
    	neutralSize1Lbl.setText("Neutral small ship: ");
    	neutralSize2Lbl.setText("Neutral medium ship : ");
    	neutralSize3Lbl.setText("Neutral large ship : ");
    	if(gw.isTroopGameWorld()){
    		neutralTroopLbl.setText("Neutral troop: ");
    		neutralTroopLbl2.setText(gw.getNeutralTroopType().getUniqueName());
    	}else{
    		neutralTroopLbl.setVisible(false);
    		neutralTroopLbl2.setVisible(false);
    	}
    	
    	closedNeutralsLbl.setText("Closed neutrals chance: ");
    	razedLbl.setText("Razed neutrals chance: ");
    	// value labels
    	fullNameLbl2.setText(gw.getFullName());
    	if(gw.getTroopTypes().size()> 0){
    		troopLbl2.setText("Yes");
    	}else{
    		troopLbl2.setText("No");
    	}
    	
    	neutralSize1Lbl2.setText(gw.getNeutralSize1().getName());
    	neutralSize2Lbl2.setText(gw.getNeutralSize2().getName());
    	neutralSize3Lbl2.setText(gw.getNeutralSize3().getName());
    	closedNeutralsLbl2.setText(gw.getClosedNeutralPlanetChance() + "%");
    	razedLbl2.setText(gw.getRazedPlanetChance() + "%");

    	
        // description textarea
        descriptionTextArea.setText(gw.getTotalDescription());
        descriptionTextArea.setVisible(true);
    }

    public String getId(){
        return id;
    }

    public void updateData(){
    }
}
