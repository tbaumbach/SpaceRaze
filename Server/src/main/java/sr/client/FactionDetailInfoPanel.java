package sr.client;

import javax.swing.JScrollPane;

import sr.client.color.ColorConverter;
import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.general.Functions;
import sr.world.Faction;
import sr.world.Player;

public class FactionDetailInfoPanel extends SRBasePanel{
	private static final long serialVersionUID = 1L;
	
	private SRLabel nameLbl,openPlanetBonusLbl,closedPlanetBonusLbl,resistanceBonusLbl;
	private SRLabel alignmentLbl,governorVIPTypeLbl,nrStartingRandomVIPsLbl;
    private SRLabel techBonusLbl, techBonusLbl2;
    private SRLabel xenomorphLbl,xenomorphLbl2;
    private SRLabel nameLbl2,openPlanetBonusLbl2,closedPlanetBonusLbl2,resistanceBonusLbl2;
    private SRLabel alignmentLbl2,governorVIPTypeLbl2,nrStartingRandomVIPsLbl2;
    private SRLabel reconstructLbl,reconstructLbl2;
    private SRLabel corrLbl,corrLbl2;
    private JScrollPane scrollPaneTotalDescription;
    private SRTextArea factionTotalDescriptionArea;
    private SRLabel descriptionLabel;
    
    private int column2X = 190;
    private int column1width = 170;
    private int yPosition = 3;
    private final int yInterval = 15;
    
    
    
    public FactionDetailInfoPanel(){
        
        this.setLayout(null);
        setBackground(StyleGuide.colorBackground);
        
        int cHeight = 15;
        
        nameLbl = new SRLabel();
        nameLbl.setBounds(0,yPosition,190,cHeight);
        add(nameLbl);
        nameLbl2 = new SRLabel();
        nameLbl2.setBounds(column2X,yPosition,190,cHeight);
        add(nameLbl2);

        openPlanetBonusLbl = new SRLabel();
        openPlanetBonusLbl.setBounds(0,newLine(),190,cHeight);
        add(openPlanetBonusLbl);
        openPlanetBonusLbl2 = new SRLabel();
        openPlanetBonusLbl2.setBounds(column2X,yPosition,190,cHeight);
        add(openPlanetBonusLbl2);

        closedPlanetBonusLbl = new SRLabel();
        closedPlanetBonusLbl.setBounds(0,newLine(),column1width,cHeight);
        add(closedPlanetBonusLbl);
        closedPlanetBonusLbl2 = new SRLabel();
        closedPlanetBonusLbl2.setBounds(column2X,yPosition,100,cHeight);
        add(closedPlanetBonusLbl2);

        resistanceBonusLbl = new SRLabel();
        resistanceBonusLbl.setBounds(0,newLine(),column1width,cHeight);
        add(resistanceBonusLbl);
        resistanceBonusLbl2 = new SRLabel();
        resistanceBonusLbl2.setBounds(column2X,yPosition,150,cHeight);
        add(resistanceBonusLbl2);

        alignmentLbl = new SRLabel();
        alignmentLbl.setBounds(0,newLine(),column1width,cHeight);
        add(alignmentLbl);
        alignmentLbl2 = new SRLabel();
        alignmentLbl2.setBounds(column2X,yPosition,200,cHeight);
        add(alignmentLbl2);

        corrLbl = new SRLabel();
        corrLbl.setBounds(0,newLine(),column1width,cHeight);
        add(corrLbl);
        corrLbl2 = new SRLabel();
        corrLbl2.setBounds(column2X,yPosition,380,cHeight);
        add(corrLbl2);
        
        techBonusLbl = new SRLabel();
        techBonusLbl.setBounds(0,newLine(),column1width,cHeight);
        add(techBonusLbl);
        techBonusLbl2 = new SRLabel();
        techBonusLbl2.setBounds(column2X,yPosition,100,cHeight);
        add(techBonusLbl2);

        xenomorphLbl = new SRLabel();
        xenomorphLbl.setBounds(0,newLine(),column1width,cHeight);
        add(xenomorphLbl);
        xenomorphLbl2 = new SRLabel();
        xenomorphLbl2.setBounds(column2X,yPosition,100,cHeight);
        add(xenomorphLbl2);

        reconstructLbl = new SRLabel();
        reconstructLbl.setBounds(0,newLine(),column1width,cHeight);
        add(reconstructLbl);
        reconstructLbl2 = new SRLabel();
        reconstructLbl2.setBounds(column2X,yPosition,200,cHeight);
        add(reconstructLbl2);

        governorVIPTypeLbl = new SRLabel();
        governorVIPTypeLbl.setBounds(0,newLine(),column1width,cHeight);
        add(governorVIPTypeLbl);
        governorVIPTypeLbl2 = new SRLabel();
        governorVIPTypeLbl2.setBounds(column2X,yPosition,300,cHeight);
        add(governorVIPTypeLbl2);

        nrStartingRandomVIPsLbl = new SRLabel();
        nrStartingRandomVIPsLbl.setBounds(0,newLine(),column1width,cHeight);
        add(nrStartingRandomVIPsLbl);
        nrStartingRandomVIPsLbl2 = new SRLabel();
        nrStartingRandomVIPsLbl2.setBounds(column2X,yPosition,100,cHeight);
        add(nrStartingRandomVIPsLbl2);
        
        newLine();
        
        descriptionLabel = new SRLabel();
		descriptionLabel.setBounds(0,newLine(),200,16);
		descriptionLabel.setText("Description:");
        add(descriptionLabel);
        
        factionTotalDescriptionArea = new SRTextArea();      
        factionTotalDescriptionArea.setEditable(false);

        scrollPaneTotalDescription = new SRScrollPane(factionTotalDescriptionArea);
        scrollPaneTotalDescription.setBounds(0,newLine(),645,390);
	    scrollPaneTotalDescription.setVisible(true);
	    add(scrollPaneTotalDescription);
	    
        nameLbl.setText("Name: ");
        openPlanetBonusLbl.setText("Open planet bonus: ");
        closedPlanetBonusLbl.setText("Closed planet bonus: ");
        resistanceBonusLbl.setText("Resistance bonus: ");
        alignmentLbl.setText("Alignment: ");
        corrLbl.setText("Corruption: ");
        governorVIPTypeLbl.setText("Governor type: ");
        nrStartingRandomVIPsLbl.setText("# starting random VIPs: ");
        techBonusLbl.setText("Tech bonus: ");
        xenomorphLbl.setText("Alien: ");
        reconstructLbl.setText("Reconstruct: ");
    }
    
public void showFaction(Faction f){
    	
    if (f != null){
          nameLbl2.setText(String.valueOf(f.getName()));
          nameLbl2.setForeground(ColorConverter.getColorFromHexString(f.getPlanetHexColor()));
          openPlanetBonusLbl2.setText(String.valueOf(f.getOpenPlanetBonus()));
          closedPlanetBonusLbl2.setText(String.valueOf(f.getClosedPlanetBonus()));
          resistanceBonusLbl2.setText(String.valueOf(f.getResistanceBonus()));
          alignmentLbl2.setText(f.getAlignment().toString());
          corrLbl2.setText(f.getCorruptionDescription());
          governorVIPTypeLbl2.setText(f.getGovernorVIPType().getName());
          nrStartingRandomVIPsLbl2.setText(String.valueOf(f.getNrStartingRandomVIPs()));
          techBonusLbl2.setText(String.valueOf(f.getTechBonus()));
          xenomorphLbl2.setText(Functions.getYesNo(f.isAlien()));
          String recStr = Functions.getYesNo(f.isCanReconstruct());
          if (f.isCanReconstruct()){
        	  recStr += " (" + f.getReconstructCostBase() + " + planet start production)";
          }
          reconstructLbl2.setText(recStr);

          factionTotalDescriptionArea.setText(f.getTotalDescription());
      }
    }

public void showFaction(Faction f, Player p){
	
    if (f != null){
          nameLbl2.setText(String.valueOf(f.getName()));
          nameLbl2.setForeground(ColorConverter.getColorFromHexString(p.getFaction().getPlanetHexColor()));
          openPlanetBonusLbl2.setText(String.valueOf(p.getOpenPlanetBonus()));
          closedPlanetBonusLbl2.setText(String.valueOf(p.getClosedPlanetBonus()));
          resistanceBonusLbl2.setText(String.valueOf(p.getResistanceBonus()));
          alignmentLbl2.setText(f.getAlignment().toString());
          corrLbl2.setText(p.getCorruptionDescription());
          governorVIPTypeLbl2.setText(f.getGovernorVIPType().getName());
          nrStartingRandomVIPsLbl2.setText(String.valueOf(f.getNrStartingRandomVIPs()));
          techBonusLbl2.setText(String.valueOf(p.getTechBonus()));
          xenomorphLbl2.setText(Functions.getYesNo(f.isAlien()));
          String recStr = Functions.getYesNo(p.isCanReconstruct());
          if (f.isCanReconstruct()){
        	  recStr += " (" + f.getReconstructCostBase() + " + planet start production)";
          }
          reconstructLbl2.setText(recStr);
          factionTotalDescriptionArea.setText(f.getTotalDescription());
      }
    }
    
    private int newLine(){
    	yPosition = yPosition + yInterval;
    	return yPosition;
    }
}
