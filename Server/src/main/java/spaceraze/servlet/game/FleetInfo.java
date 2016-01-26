package spaceraze.servlet.game;

public class FleetInfo {
	
	private String owner;
	private int shipSize;
	private boolean civ;
	
	FleetInfo(String owner, int size, boolean isCivilan){
		this.owner = owner;
		this.shipSize = size;
		this.civ = isCivilan;
	}

	public String getOwner() {
		return owner;
	}

	public int getShipSize() {
		return shipSize;
	}

	public boolean isCiv() {
		return civ;
	}

}
