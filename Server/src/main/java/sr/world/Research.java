package sr.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.general.logging.Logger;

public class Research implements Serializable {
	static final long serialVersionUID = 1L;
	
	//private List<ResearchAdvantage> advantages,onGoingResearchedAdvantages;
	private List<ResearchAdvantage> advantages;
	private HashMap<String,ResearchAdvantage> allAdvantage;
	private int numberOfSimultaneouslyResearchAdvantages = 1;
	
	public Research() {
		
		advantages = new ArrayList<ResearchAdvantage>();
	//	onGoingResearchedAdvantages = new ArrayList<ResearchAdvantage>();
		allAdvantage = new HashMap<String, ResearchAdvantage>();
	}
	
	public Research(int numberOfSimultaneouslyResearchAdvantages) {
	
		setNumberOfSimultaneouslyResearchAdvantages(numberOfSimultaneouslyResearchAdvantages);
		advantages = new ArrayList<ResearchAdvantage>();
		//onGoingResearchedAdvantages = new ArrayList<ResearchAdvantage>();
		allAdvantage = new HashMap<String, ResearchAdvantage>();
	}
	
	public Research(Research inObj) {
		 
		advantages = new ArrayList<ResearchAdvantage>();
		allAdvantage = new HashMap<String, ResearchAdvantage>();
		
		setNumberOfSimultaneouslyResearchAdvantages(inObj.numberOfSimultaneouslyResearchAdvantages);
		
		// Clones all the Advantages
		Collection<ResearchAdvantage> tempAllAdvantage = inObj.allAdvantage.values();
		Iterator<ResearchAdvantage> it = tempAllAdvantage.iterator();
		
		int lopNumber = 0;// log skall tas bort
		while(it.hasNext()){
				
			ResearchAdvantage tempResearchAdvantage = it.next();
			allAdvantage.put(tempResearchAdvantage.getName(), tempResearchAdvantage.clone());
			lopNumber++;  // log skall tas bort
		}
		
		// adding the children and parents.
		it = tempAllAdvantage.iterator();
		lopNumber = 0;// log skall tas bort
		while(it.hasNext()){
			ResearchAdvantage tempResearchAdvantage = it.next();
			for(int index= 0;index<tempResearchAdvantage.getChildren().size();index++){
				allAdvantage.get(tempResearchAdvantage.getName()).addChild(allAdvantage.get(tempResearchAdvantage.getChildren().get(index).getName()));
			}
			lopNumber++;  // log skall tas bort
		}
		
		// sets all roots Advantages
		for(int i=0;i < inObj.advantages.size(); i++){
			advantages.add(allAdvantage.get(inObj.advantages.get(i).getName()));
		}
			
	}
	
	@Override
	public Research clone(){
		Logger.finer("Research Clone(): ");
		return new Research(this);
	}
	
	public void researchAdvantage(String advantageName, TurnInfo ti, Player p){
		getAdvantage(advantageName).research(ti,p);
	}
	
	public List<ResearchAdvantage> getAdvantages() {
		return advantages;
	}
	
	@JsonIgnore
	public List<ResearchAdvantage> getAllAdvantagesThatIsReadyToBeResearchOn() {
		
		List<ResearchAdvantage> tempAdvantages = new ArrayList<ResearchAdvantage>();
		
		Collection<ResearchAdvantage> tempAllAdvantage = allAdvantage.values();
		Iterator<ResearchAdvantage> it = tempAllAdvantage.iterator();
		Logger.finer("tempAllAdvantage.size(): " + allAdvantage.size());
		
		while(it.hasNext()){
			ResearchAdvantage tempResearchAdvantage = it.next();
			if(tempResearchAdvantage.isReadyToBeResearchedOn()){
				tempAdvantages.add(tempResearchAdvantage);
			}
		}
		return tempAdvantages;
	}
	
	public List<String> getAllAdvantagesNameThatIsReadyToBeResearchOn() {
		
		List<String> tempAdvantages = new ArrayList<String>();
		
		Collection<ResearchAdvantage> tempAllAdvantage = allAdvantage.values();
		Iterator<ResearchAdvantage> it = tempAllAdvantage.iterator();
		Logger.finer("tempAllAdvantage.size(): " + allAdvantage.size());
		
		while(it.hasNext()){
			ResearchAdvantage tempResearchAdvantage = it.next();
			if(tempResearchAdvantage.isReadyToBeResearchedOn()){
				tempAdvantages.add(tempResearchAdvantage.getName());
			}
		}
		return tempAdvantages;
	}

	public void setAdvantages(List<ResearchAdvantage> advantages) {
		this.advantages = advantages;
	}
	
	public ResearchAdvantage getAdvantage(String name) {
		return allAdvantage.get(name);
	}

	public void addAdvantage(ResearchAdvantage advantages) {
		this.allAdvantage.put(advantages.getName(), advantages);
	}
	
	public void addAdvantagAsRoot(ResearchAdvantage advantages) {
		this.allAdvantage.put(advantages.getName(), advantages);
		this.advantages.add(advantages);
	}

	public int getNumberOfSimultaneouslyResearchAdvantages() {
		return numberOfSimultaneouslyResearchAdvantages;
	}

	public void setNumberOfSimultaneouslyResearchAdvantages(
			int numberOfSimultaneouslyResearchAdvantages) {
		this.numberOfSimultaneouslyResearchAdvantages = numberOfSimultaneouslyResearchAdvantages;
	}
	
}
