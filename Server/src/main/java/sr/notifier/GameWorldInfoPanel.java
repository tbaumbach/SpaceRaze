package sr.notifier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.border.LineBorder;

import sr.client.StyleGuide;
import sr.client.color.ColorConverter;
import sr.client.components.CheckBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.scrollable.TextAreaPanel;
import sr.world.Faction;
import sr.world.GameWorld;

@SuppressWarnings("serial")
public class GameWorldInfoPanel extends SRBasePanel implements ActionListener {
	private SRLabel titleLbl,nrShipsLbl,nrShipsLbl2,nrVIPsLbl,nrVIPsLbl2;
	private SRLabel  advFeaturesLbl,advFeaturesLbl2,descriptionLbl;
	private TextAreaPanel descriptionTA;
	private CheckBoxPanel[] factionsCB;
	private SRLabel[] factionsLbl;

	public GameWorldInfoPanel(GameWorld aGameWorld, boolean showAdvanced){
		setLayout(null);
		setSize(260, 610);                         
		setBorder(new LineBorder(StyleGuide.colorNeutral));
		
		titleLbl = new SRLabel();
		titleLbl.setSize(150, 20);
		titleLbl.setForeground(titleLbl.getForeground().brighter());
		addUpperLeft(titleLbl);

		nrShipsLbl = new SRLabel("# Ship Types:");
		nrShipsLbl.setSize(150, 20);
		addBelow(nrShipsLbl,titleLbl);
		nrShipsLbl2 = new SRLabel();
		nrShipsLbl2.setSize(70, 20);
		addRight(nrShipsLbl2,nrShipsLbl);

		nrVIPsLbl = new SRLabel("# VIP Types:");
		nrVIPsLbl.setSize(150, 20);
		addBelow(nrVIPsLbl,nrShipsLbl);
		nrVIPsLbl2 = new SRLabel();
		nrVIPsLbl2.setSize(70, 20);
		addRight(nrVIPsLbl2,nrVIPsLbl);

		advFeaturesLbl = new SRLabel("Advanced features:");
		advFeaturesLbl.setSize(150, 20);
		addBelow(advFeaturesLbl,nrVIPsLbl);
		advFeaturesLbl2 = new SRLabel();
		advFeaturesLbl2.setSize(150, 20);
		advFeaturesLbl2.setForeground(StyleGuide.colorNeutral.darker());
		addBelow(advFeaturesLbl2,advFeaturesLbl,0);

		descriptionLbl = new SRLabel("Description:");
		descriptionLbl.setSize(130, 20);
		addBelow(descriptionLbl,advFeaturesLbl2);
		descriptionTA = new TextAreaPanel();
		descriptionTA.setSize(240, 60);
		addBelow(descriptionTA,descriptionLbl);

		showGameWorld(aGameWorld,showAdvanced);
	}
	
    public void showGameWorld(GameWorld aGameWorld, boolean showAdvanced){
		titleLbl.setText(aGameWorld.getFullName());
		nrShipsLbl2.setText(aGameWorld.getShipTypes().size());
		nrVIPsLbl2.setText(aGameWorld.getVipTypes().size());
		String advFeaturesStr = "None";
		if (aGameWorld.getTroopTypes().size() > 0){
			advFeaturesStr = "Troop units";
		}
		if (aGameWorld.getFactions().get(0).getResearch().getAdvantages().size() > 0){
			if (!advFeaturesStr.equals("")){
				advFeaturesStr += ", ";
			}
			advFeaturesStr += "Research";
		}
		advFeaturesLbl2.setText("    " + advFeaturesStr);
		descriptionTA.setText(aGameWorld.getDescription());
		// remove old factions
		if (factionsCB != null){
			for (CheckBoxPanel aCheckBoxPanel : factionsCB) {
				remove(aCheckBoxPanel);
			}
			factionsCB = null;
		}
		if (factionsLbl != null){
			for (SRLabel aSRLabel : factionsLbl) {
				remove(aSRLabel);
			}
			factionsLbl = null;
		}
		// show factions
		List<Faction> factions = aGameWorld.getFactions();
		int ySpace = 5;
		if (factions.size() > 14){
			ySpace = 0;
		}
		if (showAdvanced){
			factionsCB = new CheckBoxPanel[factions.size()];
			int counter = 0;
			for (Faction aFaction : factions) {
				factionsCB[counter] = new CheckBoxPanel(aFaction.getName());
				factionsCB[counter].setSize(150, 20);
				factionsCB[counter].setSelected(true);
				factionsCB[counter].setCustomTextColor(ColorConverter.getColorFromHexString(aFaction.getPlanetHexColor()));
				if (counter == 0){
					addBelow(factionsCB[counter], descriptionTA, 15);
				}else{
					addBelow(factionsCB[counter], factionsCB[counter-1], ySpace);
				}
				counter++;
			}
		}else{
			factionsLbl = new SRLabel[factions.size()];
			int counter = 0;
			for (Faction aFaction : factions) {
				factionsLbl[counter] = new SRLabel(aFaction.getName());
				factionsLbl[counter].setSize(150, 20);
				factionsLbl[counter].setForeground(ColorConverter.getColorFromHexString(aFaction.getPlanetHexColor()));
				if (counter == 0){
					addBelow(factionsLbl[counter], descriptionTA, 15, 20);
				}else{
					addBelow(factionsLbl[counter], factionsLbl[counter-1], ySpace);
				}
				counter++;
			}
		}
    }

    public List<String> getFactionNames(){
    	List<String> factionNames = new LinkedList<String>();
    	if (factionsCB != null){
    		for (CheckBoxPanel aCheckBoxPanel : factionsCB) {
    			if (aCheckBoxPanel.isSelected()){
    				factionNames.add(aCheckBoxPanel.getText());
    			}
			}
    	}else{
    		for (SRLabel aSRLabel : factionsLbl) {
				factionNames.add(aSRLabel.getText());
			}
    	}
    	return factionNames;
    }
    
    public void actionPerformed(ActionEvent ae){
    	// not used
    }
    
}
