package sr.world;

import java.io.Serializable;

/**
 * Denna klass hanterar orderstatus för en enskild planet, dvs hur denna planet skall hanteras.
 * Dessa statusar är bara krångligt att hantera som vanliga ordrar då de har en långvarig effekt,
 * till skillnad från vanliga ordrar som utförs, har sin effekt, och sedan är klara.
 * 
 * Denna klass skall alltid returneras när ett drag sparas.
 * 
 * Notes är inte med i denna klass då den hanteras som en order (av god anledning). 
 * 
 * TODO Paul: maxRelativeShipSize är ej med då den eventuellt skall tas bort/sluta användas? 
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
