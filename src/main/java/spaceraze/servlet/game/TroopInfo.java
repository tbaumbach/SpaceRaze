package spaceraze.servlet.game;

import java.util.ArrayList;
import java.util.List;

import spaceraze.servlethelper.game.troop.TroopPureFunctions;
import spaceraze.world.GameWorld;
import spaceraze.world.Troop;
import spaceraze.world.enums.TypeOfTroop;

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

	TroopInfo(Troop aTroop, GameWorld gameWorld) {

		type = TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), gameWorld).getName();
		name = aTroop.getName();
		shortName = aTroop.getShortName();
		id = aTroop.getUniqueId();
		typeOfTroop = TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), gameWorld).getTypeOfTroop();
		kills = aTroop.getKills();
		currentHP = aTroop.getCurrentDamageCapacity();
		maxHP = aTroop.getDamageCapacity();
		position = TroopPureFunctions.getTroopTypeByKey(aTroop.getTypeKey(), gameWorld).getDefaultPosition().toString();
		techWhenBuilt = aTroop.getTechWhenBuilt();
		attackInfantry = TroopPureFunctions.getAttackInfantry(aTroop);
		attackArmored = TroopPureFunctions.getAttackArmored(aTroop);
		attackArtillery = TroopPureFunctions.getAttackArtillery(aTroop);
		vips =  new ArrayList<>();
		
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
