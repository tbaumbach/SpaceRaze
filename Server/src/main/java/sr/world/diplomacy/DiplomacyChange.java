package sr.world.diplomacy;

import java.io.Serializable;

import sr.enums.DiplomacyLevel;
import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Player;

/**
 * This class is used when a change should be performed.
 * Either when a player wish to go to a lower state, or they respod to an
 * offer to go to a higher state.
 * @author WMPABOD
 *
 * The new level is the level of the recipient = otherPlayer. For instance if a change to become 
 * a vassal is sent, the new level i LORD, because the otherPlayer would become Lord.
 */
public class DiplomacyChange implements Serializable{
	private static final long serialVersionUID = 1L;
	private String thePlayerName,otherPlayerName;
	private DiplomacyLevel newLevel;
	private boolean responseToPreviousOffer; // behövs för att veta om en change är ett svar på ett erbjudande eller inte
	
	public DiplomacyChange(Player thePlayer, Player otherPlayer, DiplomacyLevel newLevel, boolean responseToPreviousOffer){
		this.thePlayerName = thePlayer.getName();
		Logger.fine("thePlayerName: " + thePlayerName);
		this.otherPlayerName = otherPlayer.getName();
		Logger.fine("otherPlayerName: " + otherPlayerName);
		this.newLevel = newLevel;
		this.responseToPreviousOffer = responseToPreviousOffer;
	}

	public boolean isPlayerAndLevel(Player anOtherPlayer, DiplomacyLevel aNewLevel){
		boolean retVal = false;
		if (otherPlayerName.equalsIgnoreCase(anOtherPlayer.getName()) & (newLevel == aNewLevel)){
			retVal = true;
		}
		return retVal;
	}

	public Player getThePlayer(Galaxy aGalaxy){
		return aGalaxy.getPlayer(thePlayerName);
	}

	public Player getOtherPlayer(Galaxy aGalaxy){
		return aGalaxy.getPlayer(otherPlayerName);
	}

	public String getOtherPlayerName(){
		return otherPlayerName;
	}

	public String getThePlayerName(){
		return thePlayerName;
	}

	public DiplomacyLevel getNewLevel(){
		return newLevel;
	}
	
	public boolean isResponseToPreviousOffer() {
		return responseToPreviousOffer;
	}

	public boolean isLevel(DiplomacyLevel aLevel){
		return aLevel == newLevel;
	}

}
