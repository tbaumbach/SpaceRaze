package sr.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all alignments for a gameworld
 * @author wmpabod
 *
 */
public class Alignments implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Alignment> allAlignments; 

	/**
	 * Create an empty alignments object
	 *
	 */
	public Alignments(){
		allAlignments = new ArrayList<Alignment>();
	}
	
	public Alignments(boolean createDefaultAlignments){
		this();
		if (createDefaultAlignments){
			add("Good");
			add("Neutral");
			add("Evil");
			canHaveVIP("good","neutral");
			canHaveVIP("neutral","good");
			canHaveVIP("neutral","evil");
			canHaveVIP("evil","neutral");
			hateDuellist("good","evil");
			duelOwnAlignment("good",false);
		}
	}
	
	public void add(String newAlignmentName){
		Alignment newAlignment = new Alignment(newAlignmentName);
		allAlignments.add(newAlignment);
	}
	
	public void canHaveVIP(String factionAlignmentName, String vipAlignmentName){
		Alignment factionAlignment = findAlignment(factionAlignmentName); 
		Alignment vipAlignment = findAlignment(vipAlignmentName);
		factionAlignment.addCanHaveVip(vipAlignment.getName());
	}

	public void hateDuellist(String duellistAlignmentName1, String duellistAlignmentName2){
		Alignment duellistAlignment1 = findAlignment(duellistAlignmentName1); 
		Alignment duellistAlignment2 = findAlignment(duellistAlignmentName2);
		duellistAlignment1.addHateDuellist(duellistAlignment2.getName());
		duellistAlignment2.addHateDuellist(duellistAlignment1.getName());
	}

	public void duelOwnAlignment(String duellistAlignmentName, boolean duelOwn){
		Alignment duellistAlignment = findAlignment(duellistAlignmentName); 
		duellistAlignment.setDuelOwnAlignment(duelOwn);
	}

	public Alignment findAlignment(String findName){
		Alignment found = null;
		int index = 0;
		while ((found == null) & (index < allAlignments.size())) {
			Alignment anAlignment = allAlignments.get(index);
			if (anAlignment.isAlignment(findName)){
				found = anAlignment;
			}else{
				index++;
			}
		}
		return found;
	}

	public List<Alignment> getAllAlignments() {
		return allAlignments;
	}
}
