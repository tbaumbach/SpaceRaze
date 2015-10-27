package sr.world.orders;

import java.io.Serializable;

public class TaxChange implements Serializable {
	static final long serialVersionUID = 1L;
	private int amount;
	private String playerName;
	
	public TaxChange(int amount, String playerName){
		this.amount = amount;
		this.playerName = playerName;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public boolean isThisPlayer(String anotherPlayerName){
		return anotherPlayerName.equals(playerName);
	}
}
