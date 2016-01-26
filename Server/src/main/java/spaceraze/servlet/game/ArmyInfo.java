package spaceraze.servlet.game;

/**
 * Information om fientliga/neutral armer på en planet.
 * Visar vem som är ägaren och hur många trupper som ingår i armen.
 * Här finns möjlighet att ge mera info genom att t.ex. 
 * dela in antalet trupper i de olika trupp typerna(Infantry, Armored, Support).
 * @author Tobbe
 *
 */
public class ArmyInfo {
	
	private String owner;
	private int nrOfTroops;
	
	ArmyInfo(String owner, int nrOfTroops){
		this.owner = owner;
		this.nrOfTroops = nrOfTroops;
	}

	public String getOwner() {
		return owner;
	}

	public int getNrOfTroops() {
		return nrOfTroops;
	}
}
