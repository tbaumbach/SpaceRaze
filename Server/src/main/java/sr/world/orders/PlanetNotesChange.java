package sr.world.orders;

import java.io.Serializable;

import sr.world.PlanetInfos;
import sr.world.Player;

/**
 * Handle an order to change the notes text for a planet
 * 
 * @author Paul Bodin
 *
 */
public class PlanetNotesChange implements Serializable{
	  static final long serialVersionUID = 1L;
	  private String planetName,notesText;

	public String getNotesText() {
		return notesText;
	}

	public PlanetNotesChange(String planetName, String notesText){
		  this.planetName = planetName;
		  this.notesText = notesText;
	  }
	  
	  public void performPlanetNotes(Player aPlayer){
		  PlanetInfos pi = aPlayer.getPlanetInfos();
		  pi.setNotes(planetName, notesText);
	  }
	  
	  public String getText(){
		  String text = null;
		  if ("".equals(notesText)){
			  text = "Empty notes for planet " + planetName + ".";
		  }else{
			  text = "Set notes for planet " + planetName + " to \"" + notesText + "\".";
		  }
		  return text;
	  }

	  public String getPlanetName(){
		  return planetName;
	  }
	  
	  public void setNotesText(String notesText) {
		  this.notesText = notesText;
	  }

}
