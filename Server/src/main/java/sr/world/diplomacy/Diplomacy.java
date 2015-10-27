package sr.world.diplomacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sr.enums.DiplomacyGameType;
import sr.enums.DiplomacyLevel;
import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.GameWorld;
import sr.world.Planet;
import sr.world.Player;

/**
 * Handles all diplomacy in a game
 * @author bodinp
 *
 */
public class Diplomacy implements Serializable{
	private static final long serialVersionUID = 1L;
    private List<DiplomacyState> diplomacyStates; // current states between all players
    private DiplomacyGameType diplomacyGameType;
    private Galaxy g;

    public Diplomacy(Galaxy g){
    	this.g = g;
        diplomacyStates = new ArrayList<DiplomacyState>();
    }
    
    public void resetDiplomacyStates(){
    	for (DiplomacyState aState : diplomacyStates) {
			aState.setChangedThisTurn(false);
		}
    }
    
    public void createInitialDiplomaticRelations(Player p, GameWorld gw){
        // create new diplomacy states to all other players (that have already joined this game)
        GameWorldDiplomacy diplomacy = gw.getDiplomacy();
        for (Player aPlayer : g.getPlayers()) {
			DiplomacyRelation tmpRelation = diplomacy.getRelation(aPlayer.getFaction(), p.getFaction());
			DiplomacyState tmpState = new DiplomacyState(tmpRelation.clone(),aPlayer,p);
			diplomacyStates.add(tmpState);
		}

    }

    public void logDiplomacyStates(){
    	for (DiplomacyState aState : diplomacyStates) {
			Logger.finest( "State: " + aState);
		}
    }

	public List<DiplomacyState> getDiplomacyStates(Player aPlayer){
		List<DiplomacyState> foundStates = new LinkedList<DiplomacyState>();
		for (DiplomacyState aState : diplomacyStates) {
			if (aState.isPlayer(aPlayer)){
				foundStates.add(aState);
			}
		}
		return foundStates;
	}

	public DiplomacyGameType getDiplomacyGameType() {
		return diplomacyGameType;
	}
	
	public void setPlayerDiplomacy(){
		for (DiplomacyState dipState : diplomacyStates) {
			dipState.modifyDueToGameType(diplomacyGameType);
		}
	}
	
	public void setDiplomacyGameType(DiplomacyGameType diplomacyGameType) {
		this.diplomacyGameType = diplomacyGameType;
	}

	public List<DiplomacyState> getDiplomacyStates() {
		return diplomacyStates;
	}

	public DiplomacyState getDiplomacyState(Player player1, Player player2){
		DiplomacyState foundState = null;
		Logger.finer("getDiplomacyState: " + player1 + " & " + player2);
		int counter = 0;
		while ((foundState == null) & (counter < diplomacyStates.size())){
			DiplomacyState tmpState = diplomacyStates.get(counter);
			Logger.finer("tmpState: " + tmpState);
			if (tmpState.isPlayers(player1,player2)){
				foundState = tmpState;
			}else{
				counter++;
			}
		}
		Logger.finer("player1, player2: " + player1 + ", " + player2 + " foundState: " + foundState);
		return foundState;
	}

	public boolean hostileTaskForces(Player player1, Player player2, Planet planet){
		boolean hostile = false;
		DiplomacyState state = getDiplomacyState(player1, player2);
		Logger.fine("planet: " + planet.getName());
		Logger.fine(state.getCurrentLevel().toString());
		Logger.fine(String.valueOf(planet.isPlayerPlanet()));
		if (planet.isPlanetOwner(player1) | planet.isPlanetOwner(player2)){
			if (state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.CEASE_FIRE)){ // ewar, war, cease fire
				hostile = true;
			}
			if (state.getCurrentLevel().isBetweenInclusive(DiplomacyLevel.CEASE_FIRE,DiplomacyLevel.PEACE)){
				// create change to one step lower level
				Logger.finer("lower level one step! " + planet.getName() + " " + player1.getName());
				Player planetOwner = planet.getPlayerInControl();
				DiplomacyChange newChange = new DiplomacyChange(planetOwner,state.getOtherPlayer(planetOwner),state.getNextLowerLevel(),false);
				planetOwner.getOrders().addDiplomacyChange(newChange);
			}
		}else{ // annan spelares planet (samma som neutral?!)
			if (state.getCurrentLevel().isLower(DiplomacyLevel.CEASE_FIRE)){ // ewar, war
				hostile = true;
			}
		}
		return hostile;
	}

	public boolean hostileCivilians(Player player1, Player player2){
		boolean hostile = false;
		if (player1 != player2){
			DiplomacyState state = getDiplomacyState(player1, player2);
			if (state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.WAR)){ // ewar, war
				hostile = true;
			}
		}
		return hostile;
	}

	public boolean friendlyCivilians(Player player1, Player player2){
		boolean friendly = false;
		if (player1 != player2){
			DiplomacyState state = getDiplomacyState(player1, player2);
			if (state.getCurrentLevel().isHigherOrEqual(DiplomacyLevel.ALLIANCE)){ // conf, lord, vassal, alliance
				friendly = true;
			}
		}
		return friendly;
	}

	public boolean friendlyTraders(Player player1, Player player2){
		boolean friendly = false;
		if (player1 != player2){
			DiplomacyState state = getDiplomacyState(player1, player2);
			if (state.getCurrentLevel().isHigherOrEqual(DiplomacyLevel.PEACE)){ // conf, lord, vassal, alliance, peace
				friendly = true;
			}
		}
		return friendly;
	}

	public boolean friendlySpaceports(Player player1, Player player2){
		boolean friendly = false;
		if (player1 != player2){
			DiplomacyState state = getDiplomacyState(player1, player2);
			if (state.getCurrentLevel().isHigherOrEqual(DiplomacyLevel.ALLIANCE)){ // conf, lord, vassal, alliance
				friendly = true;
			}
		}else{ // always friendly to same player
			friendly = true;
		}
		return friendly;
	}

	public boolean hostileCapitals(Player player1, Player player2){
		boolean hostile = false;
		if (player1 != player2){
			DiplomacyState state = getDiplomacyState(player1, player2);
			if (state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.WAR)){ // ewar, war
				hostile = true;
			}
		}
		return hostile;
	}

	public boolean hostileBesiege(Player playerPlanet, Player playerTaskForce){
		Logger.finer("hostileBesiege playerPlanet: " + playerPlanet.getName() + " playerTaskForce: " + playerTaskForce.getName());
		boolean hostile = false;
		DiplomacyState state = getDiplomacyState(playerPlanet, playerTaskForce);
		Logger.finer("hostile: " + state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.WAR));
		if (state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.WAR)){ // ewar, war
			hostile = true;
		}
		return hostile;
	}

	public boolean hostileDuelists(Player player1, Player player2, Planet planet){
		boolean hostile = false;
		if (player1 != player2){
			DiplomacyState state = getDiplomacyState(player1, player2);
			if (planet.isPlanetOwner(player1) | planet.isPlanetOwner(player2)){
				if (state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.CEASE_FIRE)){ // ewar, war, cease fire
					hostile = true;
				}
			}else{ // annan spelares eller neutral planet
				if (state.getCurrentLevel().isLower(DiplomacyLevel.CEASE_FIRE)){ // ewar, war
					hostile = true;
				}
			}
		}
		return hostile;
	}

	public boolean hostileInfestator(Player infPlayer, Planet planet){
		boolean hostile = false;
		if (!planet.isPlayerPlanet()){ // neutral planet
			hostile = true;
		}else{
			if (infPlayer != planet.getPlayerInControl()){
				DiplomacyState state = getDiplomacyState(infPlayer, planet.getPlayerInControl());
				if (state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.CEASE_FIRE)){ // ewar, war, cease fire
					hostile = true;
				}
			}
		}
		return hostile;
	}

	public boolean hostileCounterSpies(Player player1, Player player2){
		boolean hostile = false;
		if (player1 != player2){
			DiplomacyState state = getDiplomacyState(player1, player2);
			Logger.fine("state: " + state);
			if (state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.CEASE_FIRE)){ // ewar, war, cease fire
				hostile = true;
			}
		}
		return hostile;
	}

	public boolean hostileAssassin(Player player1, Player player2){
		boolean hostile = false;
		if (player1 != player2){
			DiplomacyState state = getDiplomacyState(player1, player2);
			if (state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.CEASE_FIRE)){ // ewar, war, cease fire
				hostile = true;
			}
		}
		return hostile;
	}

	public boolean hostileExterminator(Player player1, Player player2){
		boolean hostile = false;
		if (player1 != player2){
			DiplomacyState state = getDiplomacyState(player1, player2);
			if (state.getCurrentLevel().isLowerOrEqual(DiplomacyLevel.CEASE_FIRE)){ // ewar, war, cease fire
				hostile = true;
			}
		}
		return hostile;
	}

	/**
	 * Returns null if maybeVassalPlayer is not a vassal
	 * @param maybeVassalPlayer
	 * @return
	 */
	public DiplomacyState isVassal(Player maybeVassalPlayer){
		DiplomacyState lordState = null;
		for (Player aPlayer : g.players) {
			if (maybeVassalPlayer != aPlayer){
				DiplomacyState aState = getDiplomacyState(maybeVassalPlayer,aPlayer);
				if ((aState.getCurrentLevel() == DiplomacyLevel.LORD) && (aState.getLord() == aPlayer)){
					lordState = aState;
				}
			}
		}
		return lordState;
	}

	public List<Player> getVassalPlayers(Player lordPlayer){
		List<Player> tempVassalPlayers = new LinkedList<Player>(); // all players in conf with player1
		for (Player aPlayer : g.players) {
			if (lordPlayer != aPlayer){
				DiplomacyState aState = getDiplomacyState(lordPlayer,aPlayer);
				if ((aState.getCurrentLevel() == DiplomacyLevel.LORD) && (aState.getLord() == lordPlayer)){
					tempVassalPlayers.add(aPlayer);
				}
			}
		}
		return tempVassalPlayers;
	}

	public List<DiplomacyState> getVassalStates(Player lordPlayer){
		List<DiplomacyState> vassalStates = null;
		List<Player> tempVassalPlayers = getVassalPlayers(lordPlayer);
		if (tempVassalPlayers.size() > 0){
			vassalStates = new LinkedList<DiplomacyState>();
			for (Player vassalPlayer : tempVassalPlayers) {
				vassalStates.add(g.getDiplomacyState(lordPlayer, vassalPlayer));
			}
		}
		return vassalStates;
	}

	public List<Player> getConfederacyPlayers(Player thePlayer){
		List<Player> tempConfPlayers = new LinkedList<Player>(); // all players in conf with player1
		for (Player aPlayer : g.players) {
			if (thePlayer != aPlayer){
				DiplomacyState aState = getDiplomacyState(thePlayer,aPlayer);
				if (aState.getCurrentLevel() == DiplomacyLevel.CONFEDERACY){
					tempConfPlayers.add(aPlayer);
				}
			}
		}
		return tempConfPlayers;
	}

	public List<DiplomacyState> getConfederacyStates(Player thePlayer){
		List<DiplomacyState> confStates = new LinkedList<DiplomacyState>();
		List<Player> tempConfPlayers = getConfederacyPlayers(thePlayer);
		if (tempConfPlayers.size() > 0){
			for (Player confPlayer : tempConfPlayers) {
				confStates.add(g.getDiplomacyState(thePlayer, confPlayer));
			}
		}
		return confStates;
	}

	public boolean checkAllianceWithAllInConfederacy(Player thePlayer, Player otherPlayer){
		boolean allAlliance = true;
		List<Player> confPlayers = getConfederacyPlayers(otherPlayer);
		if (confPlayers.size() > 0){
			Logger.fine("confPlayers.size(): " + confPlayers.size());
			for (Player confPlayer : confPlayers) {
				Logger.fine("confPlayer: " + confPlayer);
				DiplomacyState state = getDiplomacyState(confPlayer, thePlayer);
				if (state.getCurrentLevel() != DiplomacyLevel.ALLIANCE){
					Logger.fine("Not alliance");
					allAlliance = false;
				}
			}
		}
		return allAlliance;
	}
	
	/**
	 * Returns true if the players have one of the levels in their current state
	 * @param player1
	 * @param player2
	 * @param levels
	 * @return
	 */
	public boolean isDiplomacyLevel(Player player1, Player player2, DiplomacyLevel... levels){
		boolean found = false;
    	DiplomacyState state = g.getDiplomacy().getDiplomacyState(player1,player2);
    	DiplomacyLevel level = state.getCurrentLevel();
		for (DiplomacyLevel aLevel : levels) {
	        if (level == aLevel){
	        	found = true;
	        }
		}
		return found;
	}
}
