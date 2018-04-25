//Title:        SpaceRaze Client
//Author:       Paul Bodin
//Description:  Java-klienten för SpazeRaze. Är en Javaapplet.

package sr.client;

import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.interfaces.SRUpdateablePanel;
import sr.world.Player;

@SuppressWarnings("serial")
public class NotesPanel extends SRBasePanel implements SRUpdateablePanel{
    private String id;
	private SRLabel notesLabel;
    private SRTextArea notesArea;
    private SRScrollPane scrollPane;

    public NotesPanel(Player p ,String id){
      this.id = id;
      this.setLayout(null);

      notesLabel = new SRLabel("Notes");
      notesLabel.setBounds(10,10,100,20);
      add(notesLabel);

      notesArea = new SRTextArea();
      notesArea.setEditable(true);
//      notesArea.setBorder(new LineBorder(StyleGuide.colorCurrent));
      
//      notesArea.setCaretColor(StyleGuide.colorCurrent);
      notesArea.setText(p.getNotes());
//      notesArea.setBounds(10,35,500,350);
//      this.add(notesArea);

      scrollPane = new SRScrollPane(notesArea);
//      scrollPane.setBorder(new LineBorder(StyleGuide.colorCurrent));
//    scrollPane.setBorder(null);
      scrollPane.setBounds(10,35,500,350);
      add(scrollPane);
    }

    public String getNotes(){
      return notesArea.getText();
    }

    public String getId(){
      return id;
    }

    public void updateData(){
    }
}
