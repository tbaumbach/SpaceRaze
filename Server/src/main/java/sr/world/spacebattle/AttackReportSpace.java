package sr.world.spacebattle;

import java.io.Serializable;

import sr.general.logging.Logger;
import sr.world.Player;
import sr.world.Spaceship;

public class AttackReportSpace implements Serializable{
    static final long serialVersionUID = 1L;
	private Spaceship targetSpaceship,attackingShip;
	private int damageNoArmor,actualDamage,damageLeftAfterShields;
	@SuppressWarnings("unused")
	private int attMultiplier; // not used (yet)
	private boolean wantsToRetreat;
	
	public String getAsString(Player curPlayer){
		StringBuffer buffer = new StringBuffer();
		boolean ownShip = attackingShip.getOwner() == curPlayer;
		if (wantsToRetreat){
			if (targetSpaceship != null){ // ship could not retreat and fired instead
				if (ownShip){
					buffer.append("Your ship " + attackingShip.getName() + " could not retreat due to an enemy ship that stops retreats.");
				}else{
					buffer.append("An enemy " + attackingShip.getSpaceshipType().getName() + " was stopped by one of your ships that stops retreats, when trying to flee, and forced to fight instead.");
				}
			}else{
				if (attackingShip.isDestroyed()){ // ship selfdestructed
					if (ownShip){
						buffer.append("Your ship " + attackingShip.getName() + " has been destroyed by its own crew, because there was nowhere they could run to.");
					}else{
						buffer.append("An enemy " + attackingShip.getSpaceshipType().getName() + " has been scuttled by its own crew instead of fighting us.");
					}
				}else{ // ship retreated
					if (ownShip){
						Logger.fine("attackingShip: " + attackingShip);
						Logger.fine("attackingShip.getRetreatingTo(): " + attackingShip.getRetreatingTo());
						buffer.append("Your ship " + attackingShip.getName() + " has run away, and is heading for " + attackingShip.getRetreatingTo().getName() + ".");
					}else{
						buffer.append("An enemy " + attackingShip.getSpaceshipType().getName() + " has run away from the battle.");
					}
				}
			}
		}
		if (targetSpaceship != null){ // if ship have retreated of self destructed there are no target ship
			if (targetSpaceship.getOwner() == curPlayer){ // own ship hit
				if (targetSpaceship.getCurrentDc() <= actualDamage){ // own ship destroyed
//				if (targetSpaceship.getCurrentDc() == 0){ // own ship destroyed
					buffer.append("Your ship " + targetSpaceship.getName() + " was destroyed when hit (" + damageNoArmor + ") by an enemy " + attackingShip.getSpaceshipType().getName() + ".");
				}else{ // own ship damaged
					buffer.append("Your ship " + targetSpaceship.getName() + " was hit by an enemy " + attackingShip.getSpaceshipType().getName() + " and the damage (" + damageNoArmor + ") " + getDamageStatus() + ".");
				}
			}else{ // enemy ship hit
				if (targetSpaceship.getCurrentDc() <= actualDamage){ // enemy ship destroyed
					buffer.append("Your ship " + attackingShip.getName() + " hit (" + damageNoArmor + ") and destroyed an enemy " + targetSpaceship.getSpaceshipType().getName()+ ".");
				}else{
					buffer.append("Your ship " + attackingShip.getName() + " hit an enemy " + targetSpaceship.getSpaceshipType().getName() + " and the damage (" + damageNoArmor + ") " + getDamageStatus() + ".");
				}
			}
		}
		buffer.append("\n");
		return buffer.toString();
	}

	private String getDamageStatus(){
		String returnString = "";
		int currentshields = targetSpaceship.getCurrentShields();
		int shieldsIncKillsFactor = targetSpaceship.killsFactor(targetSpaceship.getShields());
		int currentdc = targetSpaceship.getCurrentDc();
		int damagecapacity = targetSpaceship.getDamageCapacity();
		if (damageLeftAfterShields == 0) { // all damage was absorbed by the shields
			currentshields = currentshields - damageNoArmor;
			int shieldStrength = (int) Math.round((100.0 * currentshields) / shieldsIncKillsFactor);
			returnString = "was absorbed by the shields (shield strength: " + String.valueOf(shieldStrength) + "%).";
		} else {
			// damage = damage - currentshields;
//			currentshields = 0;
			if (currentdc > actualDamage) {
				currentdc = currentdc - actualDamage;
				int hullStrength = (int) Math.round((100.0 * currentdc)	/ damagecapacity);
				returnString = "damaged the ship (hull strength:" + String.valueOf(hullStrength) + "%).";
			}else{
				returnString = " cdc: " + currentdc + " adam: " + actualDamage + " damcap: " + damagecapacity; 
			}
		}
		return returnString;
	}

	public void setTargetSpaceship(Spaceship targetSpaceship) {
		this.targetSpaceship = targetSpaceship.clone();
	}

	public void setAttackingShip(Spaceship attackingShip) {
		this.attackingShip = attackingShip.clone();
	}

	public void setDamageNoArmor(int damageNoArmor) {
		this.damageNoArmor = damageNoArmor;
	}

	public void setActualDamage(int actualDamage) {
		this.actualDamage = actualDamage;
	}

	public void setDamageLeftAfterShields(int damageLeftAfterShields) {
		this.damageLeftAfterShields = damageLeftAfterShields;
	}

	public void setAttMultiplier(int attMultiplier) {
		this.attMultiplier = attMultiplier;
	}

	public void setWantsToRetreat(boolean wantsToRetreat) {
		this.wantsToRetreat = wantsToRetreat;
	}

}
