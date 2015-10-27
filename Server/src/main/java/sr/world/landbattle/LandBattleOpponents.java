package sr.world.landbattle;

import sr.world.Troop;

public class LandBattleOpponents {
	private Troop troop1,troop2;
	
	public LandBattleOpponents(Troop aTroop, Troop bTroop){
		troop1 = aTroop;
		troop2 = bTroop;
	}
	
	public Troop getOpponent(Troop aTroop){
		Troop otherTroop = troop1;
		if (aTroop == troop1){
			otherTroop = troop2;
		}
		return otherTroop;
	}
	
	public boolean getContain(Troop aTroop){
		boolean isTroop = (aTroop == troop1);
		if (!isTroop){
			isTroop = (aTroop == troop2);
		}
		return isTroop;
	}
	
}
