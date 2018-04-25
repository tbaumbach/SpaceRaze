package sr.client;

import javax.swing.JScrollPane;

import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.world.Faction;


public class FactionOverviewPanel extends SRBasePanel{
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPaneHistory,scrollPaneDescription,scrollPaneHowToPlay;
    private SRTextArea factionHistoryArea,factionDescriptionArea,howToPlayArea;
    private SRLabel descriptionLabel,historyLabel,advantageLabel1,advantageLabel2,disadvantageLabel1,disadvantageLabel2,howToPlayLabel;
	
	public FactionOverviewPanel(){
		
		this.setLayout(null);
        setBackground(StyleGuide.colorBackground);
        
        int y = 3;
		
		advantageLabel1 = new SRLabel();
		advantageLabel1.setBounds(0,y,100,16);
		advantageLabel1.setText("Advantages:");
        add(advantageLabel1);
        advantageLabel2 = new SRLabel();
        advantageLabel2.setBounds(100,y,500,16);
        add(advantageLabel2);
        
        y += 20;

        disadvantageLabel1 = new SRLabel();
        disadvantageLabel1.setBounds(0,y,100,16);
        disadvantageLabel1.setText("Disadvantages:");
        add(disadvantageLabel1);
        disadvantageLabel2 = new SRLabel();
        disadvantageLabel2.setBounds(100,y,500,16);
        add(disadvantageLabel2);
		
        y += 30;

        // description
        
		descriptionLabel = new SRLabel();
		descriptionLabel.setBounds(0,y,200,16);
		descriptionLabel.setText("Description:");
        add(descriptionLabel);
        
        factionDescriptionArea = new SRTextArea();      
        factionDescriptionArea.setEditable(false);

        y += 20;

	    scrollPaneDescription = new SRScrollPane(factionDescriptionArea);
	    scrollPaneDescription.setBounds(0,y,500,130);
	    scrollPaneDescription.setVisible(true);
	    add(scrollPaneDescription);
	    
	    // history
	    
        y += 150;

	    historyLabel = new SRLabel();
	    historyLabel.setBounds(0,y,190,16);
	    historyLabel.setText("History:");
        add(historyLabel);
		
        y += 20;

		factionHistoryArea = new SRTextArea();   
	    factionHistoryArea.setEditable(false);

	    scrollPaneHistory = new SRScrollPane(factionHistoryArea);
	    scrollPaneHistory.setBounds(0,y,500,130);
	    scrollPaneHistory.setVisible(true);
	    add(scrollPaneHistory);

        y += 150;

	    // how to play
	    
	    howToPlayLabel = new SRLabel();
	    howToPlayLabel.setBounds(0,y,190,16);
	    howToPlayLabel.setText("How to play:");
        add(howToPlayLabel);
		
        y += 20;

        howToPlayArea = new SRTextArea();   
		howToPlayArea.setEditable(false);

	    scrollPaneHowToPlay = new SRScrollPane(howToPlayArea);
	    scrollPaneHowToPlay.setBounds(0,y,500,130);
	    scrollPaneHowToPlay.setVisible(true);
	    add(scrollPaneHowToPlay);

	}
	
	public void showFaction(Faction f){
    	
	    if (f != null){
	    	
	    	advantageLabel2.setText(f.getAdvantages());
	    	disadvantageLabel2.setText(f.getDisadvantages());
	    	factionDescriptionArea.setText(f.getDescription());
	    	factionHistoryArea.setText(f.getHistory());
	    	howToPlayArea.setText(f.getHowToPlay());
	    	
	    }
	}
	
}
