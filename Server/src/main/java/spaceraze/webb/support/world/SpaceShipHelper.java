package spaceraze.webb.support.world;

import spaceraze.world.Spaceship;
import spaceraze.world.SpaceshipType;

public class SpaceShipHelper {

	public SpaceShipHelper(Spaceship spaceship) {
	}

	public static String getHTMLHeaderRow() {
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		sb.append("<td>Name</td>");
		sb.append("<td>Short<br>Name</td>");
		sb.append("<td>Size</td>");
		sb.append("<td>Shiptype</td>");
		sb.append("<td>Targeting<br>Type</td>");
		sb.append("<td>range</td>");
		sb.append("<td>Shields</td>");
		sb.append("<td>Hits</td>");
		sb.append("<td>Weapons<br>Strength<br>Squadron</td>");
		sb.append("<td>Weapons<br>Strength<br>Small</td>");
		sb.append("<td>Weapons<br>Strength<br>Medium</td>");
		sb.append("<td>Weapons<br>Strength<br>Large</td>");
		sb.append("<td>Weapons<br>Strength<br>Huge</td>");
		sb.append("<td>Armor<br>Small</td>");
		sb.append("<td>Armor<br>Medium</td>");
		sb.append("<td>Armor<br>Large</td>");
		sb.append("<td>Armor<br>Huge</td>");
		sb.append("<td>Income Open<br>o/f/n/e</td>");
		sb.append("<td>Income Closed<br>o/f/n/e</td>");
		sb.append("<td>Init<br>Bonus</td>");
		sb.append("<td>Init<br>Support<br>Bonus</td>");
		sb.append("<td>Init<br>Defence</td>");
		sb.append("<td>Bombardment</td>");
		sb.append("<td></td>");
		sb.append("<td>PsychWarfare</td>");
		sb.append("<td>Squadron<br>Capacity</td>");
		sb.append("<td>Stops<br>Retreat</td>");
		sb.append("<td>Supply</td>");
		sb.append("<td>Planetary<br>Survey</td>");
		sb.append("<td>Can<br>Appear<br>On<br>Black<br>Market</td>");
		sb.append("<td>Can<br>attack<br>Screened<br>Ships</td>");
		sb.append("<td>Visible<br>on map</td>");
		// TODO sb.append("<td>Screened</td>");
		sb.append("<td>Build<br>Cost</td>");
		sb.append("<td>Upkeep</td>");
		sb.append("</tr>\n");
		return sb.toString();
	}

	public static String getHTMLTableRow(SpaceshipType spaceshipType) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		sb.append("<td>" + spaceshipType.getName() + "</td>");
		sb.append("<td>" + spaceshipType.getShortName() + "</td>");
		sb.append("<td>" + spaceshipType.getSize().getName() + "</td>");
		if (spaceshipType.isSquadron()) {
			sb.append("<td>Squadron</td>");
		} else {
			sb.append("<td>Capital ship</td>");
		}
		sb.append("<td>" + spaceshipType.getTargetingType().toString() + "</td>");
		sb.append("<td>" + spaceshipType.getRange() + "</td>");
		sb.append("<td>" + spaceshipType.getShields() + "</td>");
		sb.append("<td>" + spaceshipType.getHits() + "</td>");
		sb.append("<td>" + spaceshipType.getWeaponsStrengthSquadron() + "</td>");
		sb.append("<td>" + spaceshipType.getWeaponsStrengthSmall() + "</td>");
		sb.append("<td>" + spaceshipType.getWeaponsStrengthMedium() + " (" + spaceshipType.getWeaponsMaxSalvoesMediumString() + ")</td>");
		sb.append("<td>" + spaceshipType.getWeaponsStrengthLarge() + " (" + spaceshipType.getWeaponsMaxSalvoesLargeString() + ")</td>");
		sb.append("<td>" + spaceshipType.getWeaponsStrengthHuge() + " (" + spaceshipType.getWeaponsMaxSalvoesHugeString() + ")</td>");
		sb.append("<td>" + spaceshipType.getArmorSmall() + "</td>");
		sb.append("<td>" + spaceshipType.getArmorMedium() + "</td>");
		sb.append("<td>" + spaceshipType.getArmorLarge() + "</td>");
		sb.append("<td>" + spaceshipType.getArmorHuge() + "</td>");
		sb.append("<td><nobr>" + spaceshipType.getIncomeOpenString() + "</nobr></td>");
		sb.append("<td><nobr>" + spaceshipType.getIncomeClosedString() + "</nobr></td>");
		if (!spaceshipType.isInitSupport()) {
			sb.append("<td>" + spaceshipType.getIncreaseInitiative() + "</td>");
		} else {
			sb.append("<td>0</td>");
		}
		if (spaceshipType.isInitSupport()) {
			sb.append("<td>" + spaceshipType.getIncreaseInitiative() + "</td>");
		} else {
			sb.append("<td>0</td>");
		}
		sb.append("<td>" + spaceshipType.getInitDefence() + "</td>");
		sb.append("<td>" + spaceshipType.getBombardment() + "</td>");
		// sb.append("<td>" + siegeBonus + "</td>");
		// sb.append("<td>" + troops + "</td>");
		sb.append("<td></td>");
		sb.append("<td>" + spaceshipType.getPsychWarfare() + "</td>");
		sb.append("<td>" + spaceshipType.getSquadronCapacity() + "</td>");
		sb.append("<td>" + spaceshipType.isNoRetreat() + "</td>");
		sb.append("<td>" + spaceshipType.getSupply().getName() + "</td>");
		sb.append("<td>" + spaceshipType.isPlanetarySurvey() + "</td>");
		sb.append("<td>" + spaceshipType.isCanAppearOnBlackMarket() + "</td>");
		sb.append("<td>" + spaceshipType.isCanAttackScreenedShips() + "</td>");
		sb.append("<td>" + spaceshipType.isVisibleOnMap() + "</td>");
		// TODO sb.append("<td>" + screened + "</td>");
		sb.append("<td>" + spaceshipType.getBuildCost(null) + "</td>");
		sb.append("<td>" + spaceshipType.getUpkeep() + "</td>");
		sb.append("</tr>\n");
		return sb.toString();
	}

}
