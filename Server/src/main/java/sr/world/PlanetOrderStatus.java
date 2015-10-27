package sr.world;

import java.io.Serializable;

/**
 * Denna klass hanterar orderstatus f�r en enskild planet, dvs hur denna planet skall hanteras.
 * Dessa statusar �r bara kr�ngligt att hantera som vanliga ordrar d� de har en l�ngvarig effekt,
 * till skillnad fr�n vanliga ordrar som utf�rs, har sin effekt, och sedan �r klara.
 * 
 * Denna klass skall alltid returneras n�r ett drag sparas.
 * 
 * Notes �r inte med i denna klass d� den hanteras som en order (av god anledning). 
 * 
 * TODO Paul: maxRelativeShipSize �r ej med d� den eventuellt skall tas bort/sluta anv�ndas? 
 * 
 * @author Paul Bodin
 *
 */
public class PlanetOrderStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean attackIfNeutral,destroyOrbitalBuildings,doNotBesiege;
	private int maxBombardment;

    public boolean isAttackIfNeutral() {
		return attackIfNeutral;
	}

    public void setAttackIfNeutral(boolean attackIfNeutral) {
		this.attackIfNeutral = attackIfNeutral;
	}
	
    public boolean isDestroyOrbitalBuildings() {
		return destroyOrbitalBuildings;
	}
	
    public void setDestroyOrbitalBuildings(boolean destroyOrbitalBuildings) {
		this.destroyOrbitalBuildings = destroyOrbitalBuildings;
	}
	
    public boolean isDoNotBesiege() {
		return doNotBesiege;
	}
	
    public void setDoNotBesiege(boolean doNotBesiege) {
		this.doNotBesiege = doNotBesiege;
	}
	
    public int getMaxBombardment() {
		return maxBombardment;
	}
	
    public void setMaxBombardment(int maxBombardment) {
		this.maxBombardment = maxBombardment;
	}
}
