package sr.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.general.Functions;

/**
 * Represents one alignment, with a text identifier/name
 * @author wmpabod
 *
 */
public class Alignment implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name,description;
	private List<String> canHaveVipList = new LinkedList<String>(); // factions from this alignments can have vips from these alignments
	private boolean duelOwnAlignment = true; // duellists with false will never fight another duellist with the same alignment
	private List<String> hateDuellistsList = new LinkedList<String>(); // will fight duellists from these alignments even on same side
	
	@JsonIgnore
	  public String getHTMLTableContent(){
		  StringBuffer sb = new StringBuffer();
		  sb.append("<table border=\"0\" cellspacing=\"4\" cellpadding=\"0\" class=\"sr\">\n");
//		  sb.append("<tr><td>&nbsp;</td></tr>\n");
		  sb.append("<tr>");
		  sb.append("<td>Name</td>");
		  sb.append("<td>" + name + "</td>");
		  sb.append("</tr>\n");
		  sb.append("<tr>");
		  sb.append("<td>Duel own alignment</td>");
		  sb.append("<td>" + Functions.getYesNo(duelOwnAlignment) + "</td>");
		  sb.append("</tr>\n");
		  if (description != null){
			  sb.append("<tr>");
			  sb.append("<td>Description:</td>");
			  sb.append("<td colspan=\"4\">" + description + "</td>");
			  sb.append("</tr>\n");
		  }
		  sb.append("<tr>");
		  sb.append("<td>Can have VIPs from<br>these alignments:</td>");
		  sb.append("<td colspan=\"4\">" + getCanHaveVIPStringList() + "</td>");
		  sb.append("</tr>\n");
		  if (hateDuellistsList.size() > 0){
			  sb.append("<tr>");
			  sb.append("<td>Hates duellists from<br>these alignments:</td>");
			  sb.append("<td colspan=\"4\">" + getHatesDuellistsStringList() + "</td>");
			  sb.append("</tr>\n");
		  }
		  sb.append("</table>");
		  return sb.toString();
	  }
	  
	private String getCanHaveVIPStringList(){
		StringBuffer sb = new StringBuffer();
		for (String alignment : canHaveVipList) {
			if (sb.length() > 0){
				sb.append(", ");
			}
			sb.append(alignment);
		}
		return sb.toString();
	}

	private String getHatesDuellistsStringList(){
		StringBuffer sb = new StringBuffer();
		for (String alignment : hateDuellistsList) {
			if (sb.length() > 0){
				sb.append(", ");
			}
			sb.append(alignment);
		}
		return sb.toString();
	}

	public Alignment(String aName){
		this.name = aName;
		name = name.substring(0,1).toUpperCase() + name.substring(1);
		addCanHaveVip(this.name); // can always have vips of the same alignment
	}
	
	public boolean equals(Alignment anotherAlignment){
//		return this == anotherAlignment; detta borde funka, men testar att jämföra på namn istället
		return getName().equalsIgnoreCase(anotherAlignment.getName());
	}
	
	public boolean isAlignment(String aName){
		return name.equalsIgnoreCase(aName);
	}
	
	public void addCanHaveVip(String anAlignment){
		canHaveVipList.add(anAlignment);
	}

	public void addHateDuellist(String anAlignment){
		hateDuellistsList.add(anAlignment);
	}

	public boolean canHaveVip(String aVipAlignment){
		return findAlignment(aVipAlignment,canHaveVipList);
	}

	public List<String> getCanHaveVipList(){
		return canHaveVipList;
	}

	public List<String> getHateDuellistList(){
		return hateDuellistsList;
	}

	public boolean hateDuellist(String findAlignment){
		return findAlignment(findAlignment,hateDuellistsList);
	}
	
	/**
	 * Search the listToSearch for a specified alignment
	 * @param findAlignment 
	 * @return
	 */
	private boolean findAlignment(String findAlignment, List<String> listToSearch){
		boolean found = false;
		int index = 0;
		while ((!found) & (index < listToSearch.size())) {
			String anAlignment = listToSearch.get(index);
			//LoggingHandler.finer(anAlignment.getName() + ", alignment=" + findAlignment.getName());
			if (anAlignment.equals(findAlignment)){
				found = true;
			}else{
				index++;
			}
		}
		return found;
	}

	public boolean isDuelOwnAlignment() {
		return duelOwnAlignment;
	}

	public void setDuelOwnAlignment(boolean duelOwnAlignment) {
		this.duelOwnAlignment = duelOwnAlignment;
	}

	@Override
	public String toString(){
		return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName(){
		return name;
	}

	@JsonIgnore
	public String getAlignmentInfoDroid() {
		  StringBuffer sb = new StringBuffer();
		  sb.append("<h4>Alignment: ");
		  sb.append(name);
		  sb.append("</h4>");
		  sb.append(description);
		  sb.append("<p>");
		  sb.append("A faction of this alignment can have VIPs of these alignments: </b>");
		  sb.append("<br>");
		  for (String aAlignment : canHaveVipList) {
			  sb.append(aAlignment + " alignment");
			  sb.append("<br>");
		  }
		  sb.append("<br>");
		  sb.append("All VIPs with " + name + " alignment:<br>");
		  sb.append("[=getviptypesalignment]");
		  sb.append("<br>");
		  sb.append("All factions with " + name + " alignment:<br>");
		  sb.append("[=getfactionsalignment]");
		  return sb.toString();
	}

}
