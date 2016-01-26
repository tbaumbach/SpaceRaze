package spaceraze.servlet.game;

import java.util.ArrayList;
import java.util.List;

import sr.enums.TypeOfTroop;
import sr.world.Troop;

public class TroopInfo {
	
	private String type;
	private TypeOfTroop typeOfTroop;
	private String name;
	private String shortName;
	private int currentHP, maxHP;
	private String position;
	private int kills;
	private int id;
	private int techWhenBuilt; // needed for land battle sim
	// attack values
	private int attackInfantry;
	private int attackArmored;
	private int attackArtillery;

	
	
	private List<VIPInfo> vips;

	TroopInfo(Troop aTroop) {

		type = aTroop.getTroopType().getUniqueName();
		name = aTroop.getUniqueName();
		shortName = aTroop.getUniqueShortName();
		id = aTroop.getId();
		typeOfTroop = aTroop.getTroopType().getTypeOfTroop(); 
		kills = aTroop.getKills();
		currentHP = aTroop.getCurrentDC();
		maxHP = aTroop.getMaxDC();
		position = aTroop.getTroopType().getDefaultPosition().toString();
		techWhenBuilt = aTroop.getTechWhenBuilt();
		attackInfantry = aTroop.getAttackInfantry();
		attackArmored = aTroop.getAttackArmored();
		attackArtillery = aTroop.getAttackArtillery();
		vips =  new ArrayList<VIPInfo>();
		
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortName;
	}

	public int getKills() {
		return kills;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public int getTechWhenBuilt() {
		return techWhenBuilt;
	}
	
	public TypeOfTroop getTypeOfTroop() {
		return typeOfTroop;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public String getPosition() {
		return position;
	}

	//TODO Tror att den ska tas bort. Den avslöjare nämligen vilken ordningar truppen byggeds i. Spelar behöver inte se detta o behövs det inte i orders så ska den tas bort
	public int getId() {
		return id;
	}

	public int getAttackInfantry() {
		return attackInfantry;
	}

	public int getAttackArmored() {
		return attackArmored;
	}

	public int getAttackArtillery() {
		return attackArtillery;
	}

	public List<VIPInfo> getVips() {
		return vips;
	}

	public void addVIP(VIPInfo aVIP) {
		vips.add(aVIP);
		
	}
}
