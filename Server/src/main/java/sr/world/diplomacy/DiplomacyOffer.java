package sr.world.diplomacy;

import java.io.Serializable;

import sr.enums.DiplomacyLevel;
import sr.general.logging.Logger;
import sr.world.Galaxy;
import sr.world.Player;

/**
 * This class is used when a player wish to send an offer to another player
 * to enter a higher level. the other player must respond with a DiplomacyChange
 * the next turn or send an offer the same turn for the change to be performed.
 * 
 * The suggested level is the level of the recipient = otherPlayer. For instance if a offer to become 
 * a vassal is sent, the suggested level i LORD, because the otherPlayer would become Lord.
 * 
 * @author WMPABOD
 */
public class DiplomacyOffer implements Serializable {
	private static final long serialVersionUID = 1L;
	private String thePlayerName,otherPlayerName;
	private DiplomacyLevel suggestedLevel;
//	private boolean responseToPreviousOffer; // används denna? Tror att changes används istället (se text ovan...)
	private boolean offerPerformed;
	
	public DiplomacyOffer(Player thePlayer, Player otherPlayer, DiplomacyLevel suggestedLevel){
		this.thePlayerName = thePlayer.getName();
		this.otherPlayerName = otherPlayer.getName();
		this.suggestedLevel = suggestedLevel;
//		this.responseToPreviousOffer = responseToPreviousOffer;
	}

	public boolean isPlayerAndLevel(Player anOtherPlayer, DiplomacyLevel aNewLevel){
		Logger.fine("otherPlayerName: " + otherPlayerName + " anOtherPlayer.getName(): " + anOtherPlayer.getName());
		boolean retVal = false;
		if (otherPlayerName.equalsIgnoreCase(anOtherPlayer.getName()) & (suggestedLevel == aNewLevel)){
			retVal = true;
		}
		return retVal;
	}
	
	public boolean isLevel(DiplomacyLevel aLevel){
		return aLevel == suggestedLevel;
	}
	
	/**
	 * Find if one of the players is aPlayer
	 * @param aPlayer
	 * @return
	 */
	public boolean isOtherPlayer(Player aPlayer){
		boolean isOtherPlayer = false;
		if (aPlayer.getName().equalsIgnoreCase(otherPlayerName)){
			isOtherPlayer = true;
		}else{
			if (aPlayer.getName().equalsIgnoreCase(thePlayerName)){
				isOtherPlayer = true;
			}
		}
		return isOtherPlayer;
	}
	
	public Player getThePlayer(Galaxy aGalaxy){
		return aGalaxy.getPlayer(thePlayerName);
	}

	public Player getOtherPlayer(Galaxy aGalaxy){
		return aGalaxy.getPlayer(otherPlayerName);
	}

	public DiplomacyLevel getSuggestedLevel(){
		return suggestedLevel;
	}

//	public boolean isResponseToPreviousOffer() {
//		return responseToPreviousOffer;
//	}

	public boolean isOfferPerformed() {
		return offerPerformed;
	}

	public void setOfferPerformed(boolean offerPerformed) {
		this.offerPerformed = offerPerformed;
	}

	public String getOtherPlayerName() {
		return otherPlayerName;
	}

	public String getThePlayerName() {
		return thePlayerName;
	}
	
}
