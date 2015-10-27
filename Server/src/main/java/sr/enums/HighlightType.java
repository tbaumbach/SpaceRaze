package sr.enums;

/**
 * Denna enum specificerar vilka olika typer av highlights det finns, och i vilken ordning de skall visas i highlightlistan.
 * 
 * @author Paul Bodin
 *
 */
public enum HighlightType {
	TYPE_SPECIAL_1, // text is shown "as is"
	TYPE_SPECIAL_2, // text is shown "as is"
	TYPE_SPECIAL_3, // text is shown "as is"
	TYPE_GAME_WON, // text = type of win
	TYPE_GAME_OVER, // text = type of loss
	TYPE_GAME_BROKE_REMOVED, // text = not used
	TYPE_GAME_ABANDONED, // text = not used
	TYPE_DEFEATED, // text = ?
	TYPE_DEFEATED_OTHER_PLAYER, // text = ?
	TYPE_GAME_BROKE_REMOVED_OTHER_PLAYER, // text = gov name
	TYPE_GAME_ABANDONED_OTHER_PLAYER, // text = gov name
	TYPE_GOVENOR_KILLED, // text = ?
	TYPE_GOVENOR_ON_HOSTILE_NEUTRAL, // text = planet name
	TYPE_GOVENOR_KILLED_OTHER_PLAYER, // text = ?
	TYPE_NO_SHIPS_NO_PLANETS, // text = ?
	TYPE_NO_SHIPS_NO_PLANETS_OTHER_PLAYER, // text = ?
	TYPE_BROKE, // text = not used
	TYPE_GAME_BROKE_WARNING, // text = not used
	TYPE_DIPLOMACY_CHANGE_LORD_VASSAL, // text = govname ; level
	TYPE_DIPLOMACY_CHANGE_OTHER, // text = govname ; level
	TYPE_DIPLOMACY_CHANGE_OWN, // text = govname ; level
	TYPE_DIPLOMACY_CHANGE_BOTH, // text = govname ; level
	TYPE_DIPLOMACY_CHANGE_OFFER, // text = govname ; level
	TYPE_DIPLOMACY_INCOMPLETE_CONF_CHANGE, // text = govname
	TYPE_DIPLOMACY_INCOMPLETE_CONF_OFFER, // text = govname
	TYPE_GOVENOR_RETREATING, // text = retreating ship name
	TYPE_GOVENOR_NEUTRAL_JOIN_BLOCKED, // text = s for plural
	TYPE_GOVENOR_NEUTRAL_PERSUATION_BLOCKED, // text = s for plural
	TYPE_INFESTATION_BLOCKED, // text = s for plural
	TYPE_OWN_PLANET_RAZED, // text = planetname OK
	TYPE_PLANET_LOST, // text = planetname OK
	TYPE_OWN_PLANET_INFESTATED, // text = planetname OK OK
	TYPE_PLANET_CONQUERED, // text = planetname OK OK
	TYPE_PLANET_INFESTATED, // text = planetname OK OK
	TYPE_PLANET_JOINS, // text = planetname OK
	TYPE_PLANET_RECONSTRUCTED, // text = planetname OK
	TYPE_ENEMY_PLANET_RAZED, // text = planetname OK
	TYPE_OWN_PLANET_INFESTATION_IN_PROGRESS, // text = planetname OK
	TYPE_VIP_JOINS,  // text = vip type OK OK
	TYPE_VIP_BOUGHT, // text = vip type OK
	TYPE_ENEMY_JEDI_LEAVES, // text = enemy gov OK
	TYPE_ENEMY_VIP_KILLED, // text = vip type OK OK OK OK
	TYPE_FRIENDLY_VIP_KILLED, // text = killed vip type OK OK OK OK
	TYPE_ACCIDENTAL_DUEL, // text = killed vip type OK OK OK OK OK OK
	TYPE_OWN_JEDI_LEAVES, // text = enemy gov OK
	TYPE_OWN_VIP_KILLED, // text = vip type OK OK OK OK OK OK
	TYPE_BATTLE_WON, // text = planetname
	TYPE_BATTLE_LOST, // text = planetname
	TYPE_BATTLE_WON_PARTIAL_RETREAT, // text = planetname
	TYPE_BATTLE_LOST_PARTIAL_RETREAT, // text = planetname
	TYPE_RETREAT_IN_COMBAT_OWN, // text = planetname
	TYPE_RETREAT_IN_COMBAT_ENEMY, // text = planetname
	TYPE_RETREAT_BEFORE_COMBAT_OWN, // text = planetname
	TYPE_RETREAT_BEFORE_COMBAT_ENEMY, // text = planetname
	TYPE_LANDBATTLE_WON, // text = planetname
	TYPE_LANDBATTLE_INCONCLUSIVE, // text = planetname
	TYPE_LANDBATTLE_BOTH_DESTROYED, // text = planetname
	TYPE_LANDBATTLE_LOST, // text = planetname
	TYPE_SHIP_WON, // text = ship type OK
	TYPE_SHIPTYPE_WON, // text = ship type OK
	TYPE_TROOP_WON, // text = troop type OK
	TYPE_HOT_STUFF_WON, // text = hot stuff amount OK
	TYPE_GIFT, // text = sender name ";" amount
	TYPE_SUPPORT, // text = sender name ";" amount
	TYPE_MESSAGE_PRIVATE, // text = govenor name
	TYPE_MESSAGE_FACTION, // text = sender name ";" faction
	TYPE_MESSAGE_PUBLIC, // text = govenor name
	TYPE_OWN_TROOP_DESTROYED, // text = troop name
	TYPE_ENEMY_TROOP_DESTROYED, // text = troop name
	TYPE_OWN_CIVILIAN_SHIP_DESTROYED, // text = ship name
	TYPE_OWN_CIVILIAN_SHIP_RETREATED, // text = planet name
	TYPE_ENEMY_CIVILIAN_SHIP_DESTROYED, // text = ship name
	TYPE_ENEMY_CIVILIAN_SHIP_RETREATED, // text = planet name
	TYPE_RESEARCH_DONE, // text = Research advantage name
	TYPE_NOTHING_TO_REPORT, // text = none
	TYPE_SPECIAL_LAST; // text is shown "as is"
	
}
