package spaceraze.servlet.gameworld;

import java.util.ArrayList;
import java.util.List;

import sr.world.Faction;
import sr.world.GameWorld;

public class GameWorldLight {
	
	
	private String name, shortDescription, description, history, howToPlay;
	private String id;
	private List<String> factions;
	
	public GameWorldLight(){};
	
	public GameWorldLight(GameWorld gameWorld){
		
		name = gameWorld.getFullName();
		id = gameWorld.getFileName();
		shortDescription = gameWorld.getShortDescription();
		description = gameWorld.getDescription();
		history = gameWorld.getHistory();
		howToPlay = gameWorld.getHowToPlay();
		
		
		factions = new ArrayList<String>();
		for (Faction faction : gameWorld.getFactions()) {
			factions.add(faction.getName());
			
		}
		
	}

	public String getName() {
		return name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public String getHistory() {
		return history;
	}

	public String getHowToPlay() {
		return howToPlay;
	}

	public String getId() {
		return id;
	}

	public List<String> getFactions() {
		return factions;
	}

}
