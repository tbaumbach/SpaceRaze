package sr.world.spacebattle;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import sr.general.logging.Logger;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.TaskForce;

/**
 * Stores data from one spaceship battle.
 * Data about the involved forces are kept in clones om the taskforce objects. 
 * The clone only clones the spaceships, not player and galaxy objects.
 * The TaskForce can be a composite TaskForce with ships from several cooperating players.
 * @author bodinp
 *
 */
public class SpaceBattleReport implements Serializable{
    static final long serialVersionUID = 1L;
//	private TaskForce defenderTaskForce; // the taskforce that is defending
	private TaskForce initialTaskForce1;
	private TaskForce initialTaskForce2;
	private TaskForce postBattleTaskForce1;
	private TaskForce postBattleTaskForce2;
	private Planet planet;
	private List<AttackReportSpace> attackReports;
	private boolean lastShipDestroyed = false;
	
	public void setLastShipDestroyed(boolean lastShipDestroyed) {
		this.lastShipDestroyed = lastShipDestroyed;
	}

	public SpaceBattleReport(Planet battlePlanet){
//		defenderTaskForce = theDefender;
		Logger.fine("battlePlanet: " + battlePlanet.getName());
		planet = battlePlanet;
		attackReports = new LinkedList<AttackReportSpace>();
	}
	
	public void setInitialForces1(TaskForce taskForce1){
		initialTaskForce1 = taskForce1.clone();
	}

	public void setInitialForces2(TaskForce taskForce2){
		initialTaskForce2 = taskForce2.clone();
	}

	public void setPostBattleForces1(TaskForce taskForce1){
		postBattleTaskForce1 = taskForce1.clone();;
	}

	public void setPostBattleForces2(TaskForce taskForce2){
		postBattleTaskForce2 = taskForce2.clone();;
	}

//	public void addAttackResult(Spaceship attackingShip,Spaceship targetSpaceship,int damageNoArmor,int attMultiplier,int actualDamage,int damageLeftAfterShields){
//		AttackReportSpace newReport = new AttackReportSpace(attackingShip,targetSpaceship,damageNoArmor,attMultiplier,actualDamage,damageLeftAfterShields);
//		attackReports.add(newReport);
//	}

	public void addAttackResult(AttackReportSpace newReport){
		Logger.finer("adding attackreport");
		attackReports.add(newReport);
	}

	public String getAsString(Player curPlayer, ReportLevel level){
		StringBuffer aBuffer = new StringBuffer();
		boolean curIsTF1 = initialTaskForce1.isPlayer(curPlayer);
		// rename as own/enemy forces
		TaskForce initialOwnTaskForce = curIsTF1 ? initialTaskForce1 : initialTaskForce2;
		TaskForce initialEnemyTaskForce = !curIsTF1 ? initialTaskForce1 : initialTaskForce2;
		TaskForce postBattleOwnTaskForce = curIsTF1 ? postBattleTaskForce1 : postBattleTaskForce2;
		TaskForce postBattleEnemyTaskForce = !curIsTF1 ? postBattleTaskForce1 : postBattleTaskForce2;
		boolean won = postBattleOwnTaskForce.getTotalNrShips() > 0;
		aBuffer.append("Report from spacebattle at " + planet.getName() + "\n");
		aBuffer.append("--------------------------------------------\n");
		aBuffer.append("\n");
        if ((initialOwnTaskForce.getTotalNrShips(true) > 0) & (initialOwnTaskForce.getTotalNrShips(false) > 0)){
        	aBuffer.append("Your initial first line forces consisted of: " + initialOwnTaskForce.getOwnParticipatingShipsString(false) + "\n");
        	aBuffer.append("Your initial screened forces consisted of: " + initialOwnTaskForce.getOwnParticipatingShipsString(true) + "\n");
        }else{
        	aBuffer.append("Your initial forces consisted of: " + initialOwnTaskForce.getOwnParticipatingShipsString() + "\n");
        }
        if ((initialEnemyTaskForce.getTotalNrShips(true) > 0) & (initialEnemyTaskForce.getTotalNrShips(false) > 0)){
        	aBuffer.append(initialEnemyTaskForce.getPlayer().getGovenorName() + "'s (" + initialEnemyTaskForce.getPlayer().getFaction().getName() + ") initial first line forces consisted of: " + initialEnemyTaskForce.getEnemyParticipatingShipsString(false) + "\n");
        	aBuffer.append(initialEnemyTaskForce.getPlayer().getGovenorName() + "'s (" + initialEnemyTaskForce.getPlayer().getFaction().getName() + ") initial screened forces consisted of: " + initialEnemyTaskForce.getEnemyParticipatingShipsString(true) + "\n");
        }else{
        	if (initialEnemyTaskForce.getPlayer() !=  null){
        		aBuffer.append(initialEnemyTaskForce.getPlayer().getGovenorName() + "'s (" + initialEnemyTaskForce.getPlayer().getFaction().getName() + ") initial forces consisted of: " + initialEnemyTaskForce.getEnemyParticipatingShipsString() + "\n");
        	}else{ // neutral opponent
        		aBuffer.append(planet.getName() + "'s (neutral) initial forces consisted of: " + initialEnemyTaskForce.getEnemyParticipatingShipsString() + "\n");
        	}
        }
		aBuffer.append("\n");
		
		if (level.ordinal() >= ReportLevel.MEDIUM.ordinal()){
			// attack results here
			for (AttackReportSpace anAttackReport : attackReports) {
				aBuffer.append(anAttackReport.getAsString(curPlayer));
			}
			if (won){
				if (lastShipDestroyed){
					aBuffer.append("The last of your opponents ships has been destroyed.");
				}else{
					aBuffer.append("The last of your opponents ships has run away like cowards.");
				}
			}else{ // lost battle
				if (lastShipDestroyed){
					aBuffer.append("The last of your ships has been destroyed.");
				}else{
					aBuffer.append("The last of your ships has made a tactical retreat.");
				}
			}
			aBuffer.append("\n");
		}else{ // ReportLevel.LOW
			aBuffer.append("The battle lasted " + attackReports.size() + " rounds.\n");
			if (won){
				aBuffer.append("You won the battle.\n");
			}else{
				aBuffer.append("You lost the battle.\n");
			}
		}
		aBuffer.append("\n");
		
		// post battle information
		// own ships
		if (postBattleOwnTaskForce.getDestroyedShips().size() > 0){
			aBuffer.append("You lost the following ships in the battle: " + getOwnShipsAsString(postBattleOwnTaskForce.getDestroyedShips()) + "\n");
		}else{
			aBuffer.append("You lost no ships in the battle\n");
		}
		// enemy ships
		if (postBattleEnemyTaskForce.getDestroyedShips().size() > 0){
			aBuffer.append("Your opponent lost the following ships in the battle: " + getEnemyShipsAsString(postBattleEnemyTaskForce.getDestroyedShips()) + "\n");
		}else{
			aBuffer.append("Your opponent lost no ships in the battle\n");
		}
		if (won){
			if (postBattleEnemyTaskForce.getRetreatedShips().size() > 0){
				aBuffer.append("The following of your opponents ships fled the battle: " + getEnemyShipsAsString(postBattleEnemyTaskForce.getRetreatedShips()) + "\n");
			}
		}else{
			if (postBattleOwnTaskForce.getRetreatedShips().size() > 0){
				aBuffer.append("The following of your ships retreated from the battle: " + getOwnShipsAsString(postBattleOwnTaskForce.getRetreatedShips()) + "\n");
			}
		}
		// print surviving ships data
		if (level.ordinal() >= ReportLevel.LONG.ordinal()){
			List<Spaceship> survivingShips = postBattleOwnTaskForce.getSpaceships(); 
			if (survivingShips.size() > 0){
				aBuffer.append("\n");
				aBuffer.append("Damage status of your surviving ships:\n");
				aBuffer.append("------------------------\n");
				for (Spaceship aShip : survivingShips) {
					aBuffer.append(aShip.getUniqueName() + ": " + getDamageStatus(aShip) + "\n");
				}
			}
		}

		Logger.finest("aBuffer.toString():\n" + aBuffer.toString());

		return aBuffer.toString();
	}

	private String getDamageStatus(Spaceship aShip){
		String returnString = "";
		int currentshields = aShip.getCurrentShields();
		int shieldsIncKillsFactor = aShip.killsFactor(aShip.getShields());
		int currentdc = aShip.getCurrentDc();
		int damagecapacity = aShip.getDamageCapacity();
		// dc
		int hullStrength = (int) Math.round((100.0 * currentdc)	/ damagecapacity);
		returnString += "Hull strength: " + String.valueOf(hullStrength) + "% ";
		// shields
		int shieldStrength = (int) Math.round((100.0 * currentshields) / shieldsIncKillsFactor);
		returnString += "Shield strength: " + String.valueOf(shieldStrength) + "%";
		// return string
		return returnString;
	}

	private String getOwnShipsAsString(List<Spaceship> ships){
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Spaceship ship : ships) {
			if (!first){
				sb.append(", ");
			}
			first = false;
			sb.append(ship.getUniqueName());
		}
		return sb.toString();
	}

	private String getEnemyShipsAsString(List<Spaceship> ships){
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Spaceship ship : ships) {
			if (!first){
				sb.append(", ");
			}
			first = false;
			sb.append(ship.getSpaceshipType().getName());
		}
		return sb.toString();
	}

}
