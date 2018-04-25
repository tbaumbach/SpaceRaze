package sr.client;

import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.Functions;
import sr.world.Alignment;
import sr.world.Player;
import sr.world.comparator.AlignmentNameComparator;

public class AlignmentsPanel extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel{
	private static final long serialVersionUID = 1L;
	private List<Alignment> alignments;
    private ListPanel alignmentsList;
    private SRLabel nameLbl, duelOwnAlignmentLbl;
    private SRLabel nameLbl2, duelOwnAlignmentLbl2;
    // right column
    private SRLabel descLbl;
    private SRTextArea descTextArea;
    private JScrollPane descScrollPane;
    private SRLabel canHaveVIPLbl;
    private SRTextArea canHaveVIPTextArea;
    private JScrollPane canHaveVIPScrollPane;
    private SRLabel hatesDuellistLbl;
    private SRTextArea hatesDuellistTextArea;
    private JScrollPane hatesDuellistScrollPane;
    private String id;
    // used for computing components location
    private int column1X = 205;
    private int column2X = 345;
    private int yPosition = 10;
    private int textAreaHeight = 80;
    private final int yInterval = 20;
    
    public AlignmentsPanel(Player p, String id){
      alignments = Functions.cloneList(p.getGalaxy().getGameWorld().getAlignments().getAllAlignments());
	  Collections.sort(alignments,new AlignmentNameComparator<Alignment>());
      this.id = id;

      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
      
      int cHeight = 18;

      alignmentsList = new ListPanel();
      alignmentsList.setBounds(10,10,170,590);
      alignmentsList.setListSelectionListener(this);
      alignmentsList.setForeground(StyleGuide.colorCurrent);
      alignmentsList.setBackground(StyleGuide.colorBackground);
      alignmentsList.setBorder(new LineBorder(StyleGuide.colorCurrent));
      // fill list
      DefaultListModel dlm = (DefaultListModel)alignmentsList.getModel();
      for(int i = 0; i < alignments.size(); i++){
        dlm.addElement(alignments.get(i).getName());
      }
      alignmentsList.updateScrollList();
      add(alignmentsList);


      nameLbl = new SRLabel();
      nameLbl.setBounds(column1X,yPosition,190,cHeight);
      add(nameLbl);
      nameLbl2 = new SRLabel();
      nameLbl2.setBounds(column2X,yPosition,190,cHeight);
      add(nameLbl2);

      duelOwnAlignmentLbl = new SRLabel();
      duelOwnAlignmentLbl.setBounds(column1X,newLine(),190,cHeight);
      add(duelOwnAlignmentLbl);
      duelOwnAlignmentLbl2 = new SRLabel();
      duelOwnAlignmentLbl2.setBounds(column2X,yPosition,190,cHeight);
      add(duelOwnAlignmentLbl2);

      // description textarea
      descLbl = new SRLabel();
      descLbl.setBounds(column1X,newLine(),120,cHeight);
      add(descLbl);

      descTextArea = new SRTextArea();
      
      descTextArea.setEditable(false);
 
      descScrollPane = new SRScrollPane(descTextArea);
      descScrollPane.setBounds(column1X,newLine(),350,textAreaHeight);
      descScrollPane.setVisible(false);
      add(descScrollPane);

      yPosition = yPosition + textAreaHeight;
      
      // can have vip textarea
      canHaveVIPLbl = new SRLabel();
      canHaveVIPLbl.setBounds(column1X,newLine(),320,cHeight);
      add(canHaveVIPLbl);

      canHaveVIPTextArea = new SRTextArea();
      
      canHaveVIPTextArea.setEditable(false);
 
      canHaveVIPScrollPane = new SRScrollPane(canHaveVIPTextArea);
      canHaveVIPScrollPane.setBounds(column1X,newLine(),350,textAreaHeight);
      canHaveVIPScrollPane.setVisible(false);
      add(canHaveVIPScrollPane);

      yPosition = yPosition + textAreaHeight;

      // hates duellist textarea
      hatesDuellistLbl = new SRLabel();
      hatesDuellistLbl.setBounds(column1X,newLine(),320,cHeight);
      add(hatesDuellistLbl);

      hatesDuellistTextArea = new SRTextArea();
      
      hatesDuellistTextArea.setEditable(false);
 
      hatesDuellistScrollPane = new SRScrollPane(hatesDuellistTextArea);
      hatesDuellistScrollPane.setBounds(column1X,newLine(),350,textAreaHeight);
      hatesDuellistScrollPane.setVisible(false);
      add(hatesDuellistScrollPane);
    }
    
    private int newLine(){
    	yPosition = yPosition + yInterval;
    	return yPosition;
    }

    public void valueChanged(ListSelectionEvent lse){
      if (lse.getSource() instanceof ListPanel){
        showAlignment(alignmentsList.getSelectedItem());
      }
    }

    private Alignment findAlignment(String findname){
    	Alignment vt = null;
    	int i = 0;
    	while ((vt == null) & (i<alignments.size())){
    		Alignment temp = alignments.get(i);
    		if (temp.getName().equalsIgnoreCase(findname)){
    			vt = temp;
    		}
    		i++;
    	}
    	return vt;
    }

    public void showAlignment(String name){
      Alignment anAlignment = findAlignment(name);
      nameLbl.setText("Name: ");
      duelOwnAlignmentLbl.setText("Duel own alignment: ");
      descLbl.setText("Description: ");
      canHaveVIPLbl.setText("Can have VIPs from these alignments: ");
      hatesDuellistLbl.setText("Hates duellists from these alignments: ");
      if (anAlignment != null){
          nameLbl2.setText(anAlignment.getName());
          duelOwnAlignmentLbl2.setText(Functions.getYesNo(anAlignment.isDuelOwnAlignment()));
          // abilities textarea
          descTextArea.setText(anAlignment.getDescription());
          descTextArea.setVisible(true);
          descScrollPane.setVisible(true);
          // can have vip textarea
          canHaveVIPTextArea.setText("");
          List<String> canHaveVipList = anAlignment.getCanHaveVipList();
//    	  canHaveVIPTextArea.append(anAlignment.getName() + "\n");
          for (String tmpAlignment : canHaveVipList) {
        	  canHaveVIPTextArea.append(tmpAlignment + "\n");
          }
          canHaveVIPTextArea.setVisible(true);
          canHaveVIPScrollPane.setVisible(true);
          // hate duellist textarea
          hatesDuellistTextArea.setText("");
          List<String> hatesDuellistList = anAlignment.getHateDuellistList();
          for (String tmpAlignment : hatesDuellistList) {
        	  hatesDuellistTextArea.append(tmpAlignment + "\n");
          }
          hatesDuellistTextArea.setVisible(true);
          hatesDuellistScrollPane.setVisible(true);
      }
    }

    public String getId(){
        return id;
    }

    public void updateData(){
    }
}
