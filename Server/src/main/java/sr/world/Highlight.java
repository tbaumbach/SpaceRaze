/*
 * Created on 2005-jan-17
 */
package sr.world;

import java.io.Serializable;

import sr.enums.HighlightType;
import sr.general.Functions;

/**
 * @author WMPABOD
 *
 * Represents one extra important news item that can be shown at startup in a client
 */
public class Highlight implements Comparable<Highlight>, Serializable{
	static final long serialVersionUID = 1L;
	private String text,text2;
	private HighlightType type;
	
	public Highlight(String text, HighlightType type){
		if (text != null){
			int sepIndex = text.indexOf(";"); 
			if (sepIndex > -1){
				this.text = text.substring(0,sepIndex);
				this.text2 = text.substring(sepIndex+1);			
			}else{
				this.text = text;
			}
		}else{
			this.text = "";
			this.text2 = "";
		}
		this.type = type;
	}
	
	public String getMessage(){
		String message = "";
		switch (type){
		case TYPE_GAME_WON: 	
			message = "You have won the game: " + text; // text = type of win
			break;
		case TYPE_GAME_OVER:
			message = "You have lost the game " + text; // type of loss
			break;
		case TYPE_GAME_ABANDONED:
			message = "You have abandoned this game.";
			break;
		case TYPE_GAME_BROKE_REMOVED:
			message = "You have been broke for 5 turns and have lost the game.";
			break;
		case TYPE_GAME_BROKE_WARNING:
			message = "You will lost the game if you are broke one more turn.";
			break;
		case TYPE_BROKE:
			message = "You are broke!"; // text = not used
			break;
		case TYPE_DIPLOMACY_CHANGE_LORD_VASSAL:
			message = "Gov " + text + " is now your " + text2; // text = gov name, text2 = level
			break;
		case TYPE_DIPLOMACY_CHANGE_OTHER:
			message = "Gov " + text + " have declared " + text2; // text = gov name, text2 = level
			break;
		case TYPE_DIPLOMACY_CHANGE_OWN:
			message = "You have declared " + text2 + " to Gov " + text; // text = gov name, text2 = level
			break;
		case TYPE_DIPLOMACY_CHANGE_BOTH:
			message = "You and Gov " + text + " now have " + text2; // text = gov name, text2 = level
			break;
		case TYPE_DIPLOMACY_INCOMPLETE_CONF_CHANGE:
			message = "Incomplete change to Confederacy " + text; // text = from/to gov name
			break;
		case TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER:
			message = "Incomplete offer for Confederacy " + text; // text = from/to gov name
			break;
		case TYPE_DIPLOMACY_CHANGE_OFFER:
			message = text + " send you an offer for " + text2; // text = gov name, text2 = level
			break;
		case TYPE_DEFEATED:
			message = "You have been defeated"; // text = not used
			break;
		case TYPE_GOVENOR_KILLED:
			message = "Your governor have been killed"; // text = not used
			break;
		case TYPE_GOVENOR_ON_HOSTILE_NEUTRAL:
			message = "Governor attacked by enraged population on planet " + text; // text = planet name
			break;
		case TYPE_GOVENOR_NEUTRAL_PERSUATION_BLOCKED:
			message = "Neutral planet persuation blocked by other diplomat" + text; // text = s for plural
			break;
		case TYPE_INFESTATION_BLOCKED:
			message = "Planet infection blocked by other infestator" + text; // text = s for plural
			break;
		case TYPE_GOVENOR_NEUTRAL_JOIN_BLOCKED:
			message = "Neutral planet join blocked by other diplomat" + text; // text = s for plural
			break;
		case TYPE_GOVENOR_RETREATING:
			message = "Your governor is on a retreating ship (" + text + ")"; // text = retreating ship name
			break;
		case TYPE_NO_SHIPS_NO_PLANETS:
			message = "Governor " + text + " has no ships or planets left"; // text = gov name
			break;
		case TYPE_DEFEATED_OTHER_PLAYER:
			message = "Governor " + text + " have been defeated"; // text = gov name
			break;
		case TYPE_GAME_BROKE_REMOVED_OTHER_PLAYER:
			message = "Governor " + text + " have been defeated due to being broke."; // text = gov name
			break;
		case TYPE_GAME_ABANDONED_OTHER_PLAYER:
			message = "Governor " + text + " have abandoned this game."; // text = gov name
			break;
		case TYPE_GOVENOR_KILLED_OTHER_PLAYER:
			message = "Governor " + text + " has been killed"; // text = gov name
			break;
		case TYPE_NO_SHIPS_NO_PLANETS_OTHER_PLAYER:
			message = "No ships or planets left"; // text = not used
			break;
		case TYPE_OWN_PLANET_RAZED:
			message = "Your planet " + text + " have been RAZED"; // text = planet name
			break;
		case TYPE_PLANET_LOST:
			message = "You have lost the planet " + text;
			break;
		case TYPE_PLANET_CONQUERED:
			message = "You have conquered the planet " + text;
			break;
		case TYPE_PLANET_INFESTATED:
			message = "You have colonized the planet " + text;
			break;
		case TYPE_OWN_PLANET_INFESTATED:
			message = "Your planet " + text + " has been infected with aliens";
			break;
		case TYPE_PLANET_JOINS:
			message = "The planet " + text + " have joined your forces";
			break;
		case TYPE_PLANET_RECONSTRUCTED:
			message = "You have reconstructed the planet " + text;
			break;
		case TYPE_ENEMY_PLANET_RAZED:
			message = "You have RAZED the planet " + text;
			break;
		case TYPE_OWN_PLANET_INFESTATION_IN_PROGRESS:
			message = "Your planet " + text + " are being infected by aliens";
			break;
		case TYPE_VIP_JOINS:
			message = Functions.getDeterminedForm(text,true) + " " + text + " have joined your forces"; // from conquered neutral planet
			break;
		case TYPE_VIP_BOUGHT:
			message = "You have bought " + Functions.getDeterminedForm(text) + " " + text; // bm
			break;
		case TYPE_OWN_VIP_KILLED:
			message = "Your " + text + " have been killed";
			break;
		case TYPE_ENEMY_VIP_KILLED:
			message = "An enemy " + text + " has been killed";
			break;
		case TYPE_FRIENDLY_VIP_KILLED:
			message = "A friendly " + text + " has been killed";
			break;
		case TYPE_OWN_JEDI_LEAVES:
			message = "Your Light Jedi have been forces by " + text + " to leave this quadrant";
			break;
		case TYPE_ACCIDENTAL_DUEL:
			message = "Your " + text + " have been killed by one of your other VIPs";
			break;		
		case TYPE_ENEMY_JEDI_LEAVES:
			message = "You have forced a Light Jedi of governor " + text + " to leave this quadrant";
			break;
		case TYPE_BATTLE_WON:
			message = "You have won a battle at the planet " + text;
			break;
		case TYPE_BATTLE_LOST:
			message = "You have lost a battle at the planet " + text;
			break;
		case TYPE_RETREAT_IN_COMBAT_OWN:
			message = "Your forces at the planet " + text + " have retreated";
			break;
		case TYPE_RETREAT_IN_COMBAT_ENEMY:
			message = "Enemy forces at the planet " + text + " have run away";
			break;
		case TYPE_BATTLE_LOST_PARTIAL_RETREAT:
			message = "You have lost and retreated from a battle at " + text;
			break;
		case TYPE_BATTLE_WON_PARTIAL_RETREAT:
			message = "You have won a battle at " + text + " but some enemies ran away";
			break;
		case TYPE_RETREAT_BEFORE_COMBAT_OWN:
			message = "Your forces at " + text + " retreated before combat could commence";
			break;
		case TYPE_RETREAT_BEFORE_COMBAT_ENEMY:
			message = "Enemy forces at " + text + " ran away before combat could commence";
			break;
		case TYPE_LANDBATTLE_WON:
			message = "You have won a landbattle at the planet " + text;
			break;
		case TYPE_LANDBATTLE_INCONCLUSIVE:
			message = "You have fought an inconclusive landbattle at " + text;
			break;
		case TYPE_LANDBATTLE_BOTH_DESTROYED:
			message = "Both sides destroyed in inconclusive landbattle at " + text;
			break;
		case TYPE_LANDBATTLE_LOST:
			message = "You have lost a landbattle at the planet " + text;
			break;
		case TYPE_SHIP_WON: // bm
			message = "You have won the bidding for " + Functions.getDeterminedForm(text) + " " + text; 
			break;
		case TYPE_SHIPTYPE_WON: // bm
			message = "You have won the bidding of blueprints for " + text; 
			break;
		case TYPE_TROOP_WON: // bm
			message = "You have won the bidding for " + Functions.getDeterminedForm(text) + " " + text; 
			break;
		case TYPE_HOT_STUFF_WON:
			message = "You have won the bidding for a Hot Stuff worth " + text;
			break;
		case TYPE_GIFT:
			message = "You have recieved a gift of " + text2 + " from Governor " + text;
			break;
		case TYPE_SUPPORT:
			message = "You have recieved a gift of " + text2 + " from " + text;
			break;
		case TYPE_MESSAGE_PRIVATE:
			message = "You have recieved a private message from Governor " + text;
			break;
		case TYPE_MESSAGE_FACTION:
			message = "Governor " + text + " has sent a message to all " + text2 + " Govenors";
			break;
		case TYPE_MESSAGE_PUBLIC:
			message = "Governor " + text + " has made a public statement";
			break;
		case TYPE_OWN_TROOP_DESTROYED:
			message = "Your troop " + text + " has been destroyed";
			break;
		case TYPE_ENEMY_TROOP_DESTROYED:
			message = "An enemy troop " + text + " has been destroyed";
			break;
		case TYPE_OWN_CIVILIAN_SHIP_DESTROYED:
			message = "Your civilian ship " + text + " has been destroyed";
			break;
		case TYPE_OWN_CIVILIAN_SHIP_RETREATED:
			message = "One of your civilian ships have retreated at " + text;
			break;
		case TYPE_ENEMY_CIVILIAN_SHIP_DESTROYED:
			message = "Enemy civilian ship " + text + " has been destroyed";
			break;
		case TYPE_ENEMY_CIVILIAN_SHIP_RETREATED:
			message = "An enemy civilian ship have retreated at " + text;
			break;
		case TYPE_NOTHING_TO_REPORT:
			message = "Nothing special to report";
			break;
		case TYPE_RESEARCH_DONE:
			message = "Research on " + text + " is done";
			break;
		default: // custom level
			message = text;
			break;
		}
		return message;
	}

	public HighlightType getType(){
		return type;
	}

	public int compareTo(Highlight tmpH) {
		return type.ordinal() - tmpH.getType().ordinal();
	}
}
