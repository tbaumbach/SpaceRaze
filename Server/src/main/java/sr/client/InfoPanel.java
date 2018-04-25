package sr.client;

import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.scrollable.TextAreaPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.logging.Logger;
import sr.world.Player;
import sr.world.PublicInfo;
import sr.world.spacebattle.ReportLevel;

public class InfoPanel extends SRBasePanel implements SRUpdateablePanel, ItemListener{
	private static final long serialVersionUID = 1L;
	private SRLabel turnlbl,reportLevelLbl;
	private String id;
//	private PublicInfo pi;
	private TextAreaPanel infoarea;
	private Player p;
	private ComboBoxPanel turnChoice,reportLevelChoice;
//	private JScrollPane scrollPane;

    public InfoPanel(PublicInfo pi, Player p ,String id){
//      this.pi = pi;
      this.id = id;
      this.p = p;
      this.setLayout(null);

      turnlbl = new SRLabel();
      turnlbl.setBounds(10,10,100,20);
      add(turnlbl);

      turnChoice = new ComboBoxPanel();
      turnChoice.setBounds(130,10,120,20);
      for (int i = p.getGalaxy().getTurn(); i > 0; i--){
        turnChoice.addItem(String.valueOf(i));
      }
      turnChoice.addItemListener(this);
      if (p.getGalaxy().getTurn() == 0){
        turnChoice.setEnabled(false);
      }
      add(turnChoice);

      reportLevelLbl = new SRLabel();
      reportLevelLbl.setBounds(300,10,100,20);
      add(reportLevelLbl);

      reportLevelChoice = new ComboBoxPanel();
      reportLevelChoice.setBounds(430,10,120,20);
      for (ReportLevel aReportLevel : ReportLevel.values()) {
    	  reportLevelChoice.addItem(aReportLevel.toString());
      }
      reportLevelChoice.addItemListener(this);
      if (p.getGalaxy().getTurn() == 0){
    	  reportLevelChoice.setEnabled(false);
      }
      if (p.getReportLevel() == null){
    	  p.setReportLevel(ReportLevel.LONG);
      }
      reportLevelChoice.setSelectedItem(p.getReportLevel().toString());
      add(reportLevelChoice);

      infoarea = new TextAreaPanel();
      infoarea.setBounds(10,35,715,535);
      setFields(p.getGalaxy().getTurn());
      add(infoarea);
    }
    
    public void paint(Graphics g){
    	paintChildren(g);
    	paintChildren(g); // den andra omritningen behövs för att scrollern på textarean skall visas korrekt
    }

    private void setFields(int turn){
    	Logger.finer(reportLevelChoice.getSelectedItem());
    	infoarea.setVisible(false);
    	turnlbl.setText("Choose turn: ");
    	reportLevelLbl.setText("Report level: ");
    	ReportLevel selectedReportLevel = ReportLevel.getReportLevel(reportLevelChoice.getSelectedItem());
    	infoarea.setText(p.getTurnInfoText(turn,selectedReportLevel));
    	infoarea.setVisible(true);
    }

    public void itemStateChanged(ItemEvent ie){
    	if (ie.getStateChange() == ItemEvent.SELECTED){
    		try{
    			if ((ComboBoxPanel)ie.getItemSelectable() == turnChoice){
    				setFields(Integer.parseInt((String)turnChoice.getSelectedItem()));
    			}else
    			if ((ComboBoxPanel)ie.getItemSelectable() == reportLevelChoice){
    				setFields(Integer.parseInt((String)turnChoice.getSelectedItem()));
    				p.setReportLevel(ReportLevel.getReportLevel(reportLevelChoice.getSelectedItem())); 
    			}
    		}
    		catch(NumberFormatException nfe){
    		}
    	}
    }

    public String getId(){
        return id;
    }

    public void updateData(){
    }
}
