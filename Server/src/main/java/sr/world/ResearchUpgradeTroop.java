package sr.world;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.general.Functions;

public class ResearchUpgradeTroop implements Serializable{
	static final long serialVersionUID = 1L;
	private String name;
	private int attackInfantry;
	private int attackArmored;
	private int attackArtillery;
	private int damageCapacity;
	private int dropPenalty;
	private boolean changeSpaceshipTravel;
	private boolean spaceshipTravel;
	private boolean changeVisible;
	private boolean visible;
	private int costBuild;
	private int costSupport;

    public ResearchUpgradeTroop(String name){
    	this.name = name;
    }
    
    public String doResearch(TroopType aTroop){
    	String text;
    	text = "\nThe troop type " + getName() + " has been upgraded";
    	
    	if(attackInfantry > 0){
    		aTroop.setAttackInfantry(aTroop.getAttackInfantry() + attackInfantry);
    	}
    	if(attackArmored > 0){
    		aTroop.setAttackArmored(aTroop.getAttackArmored() + attackArmored);
    	}
    	if(attackArtillery > 0){
    		aTroop.setAttackArtillery(aTroop.getAttackArtillery() + attackArtillery);
    	}
    	if(damageCapacity > 0){
    		aTroop.setDamageCapacity(aTroop.getDamageCapacity() + damageCapacity);
    	}
    	if(dropPenalty > 0){
    		aTroop.setDropPenalty(aTroop.getDropPenalty() - dropPenalty);
    	}
    	if(costBuild > 0){
    		aTroop.setCostBuild(aTroop.getCostBuild(null) + costBuild);
    	}
    	if(costSupport > 0){
    		aTroop.setUpkeep(aTroop.getUpkeep() + costSupport);
    	}
    	if (changeSpaceshipTravel){
    		aTroop.setSpaceshipTravel(spaceshipTravel);
    	}
    /*	if (changeAttackScreened){
    		aTroop.setAttackScreened(attackScreened);
    	}*/
    	if (changeVisible){
    		aTroop.setVisible(visible);
    	}    	
    	return text;
    }
    
    @JsonIgnore
    public String getResearchText(){
    	String text;
    	
    	text= "Change properties for the troop type: " + name;
    	
    	if(attackInfantry > 0){
    		text += "\n-Attack Infantry: " + addPlus(attackInfantry);
    	}
    	if(attackArmored > 0){
    		text += "\n-Attack Armored: " + addPlus(attackArmored);
    	}
    	if(attackArtillery > 0){
    		text += "\n-Attack Artillery: " + addPlus(attackArtillery);
    	}
    	if(damageCapacity > 0){
    		text += "\n-Damage Capacity: " + addPlus(damageCapacity);
    	}
    	if(dropPenalty > 0){
    		text += "\n-Drop Penalty: -" + dropPenalty;
    	}
    	if(costBuild > 0){
    		text += "\n-Cost Build: " + addPlus(costBuild);
    	}
    	if(costSupport > 0){
    		text += "\n-Cost Support: " + addPlus(costSupport);
    	}
    	if (changeSpaceshipTravel){
    		text += "\n-Spaceship Travel: " + Functions.getYesNo(spaceshipTravel);
    	}
    /*	if (changeAttackScreened){
    		text += "\n-Attack Screened: " + Functions.getYesNo(attackScreened);
    	}*/
    	if (changeVisible){
    		text += "\n-Visible on map: " + Functions.getYesNo(visible);
    	}    	

    	return text;
    }
    
    private String addPlus(int number){
    	StringBuffer retVal = new StringBuffer();
    	if(number > 0){
    		retVal.append("+");
    	}
    	retVal.append(number);
    	return retVal.toString();
    }

	public String getName() {
		return name;
	}

	public int getAttackArmored() {
		return attackArmored;
	}

	public void setAttackArmored(int attackArmored) {
		this.attackArmored = attackArmored;
	}

	public int getAttackArtillery() {
		return attackArtillery;
	}

	public void setAttackArtillery(int attackArtillery) {
		this.attackArtillery = attackArtillery;
	}

	public int getAttackInfantry() {
		return attackInfantry;
	}

	public void setAttackInfantry(int attackInfantry) {
		this.attackInfantry = attackInfantry;
	}
/*
	public boolean isAttackScreened() {
		return attackScreened;
	}

	public void setAttackScreened(boolean attackScreened) {
		this.attackScreened = attackScreened;
		changeAttackScreened = true;
	}

	public boolean isChangeAttackScreened() {
		return changeAttackScreened;
	}

	public void setChangeAttackScreened(boolean changeAttackScreened) {
		this.changeAttackScreened = changeAttackScreened;
	}
*/
	public boolean isChangeSpaceshipTravel() {
		return changeSpaceshipTravel;
	}

	public void setChangeSpaceshipTravel(boolean changeSpaceshipTravel) {
		this.changeSpaceshipTravel = changeSpaceshipTravel;
	}

	public boolean isChangeVisible() {
		return changeVisible;
	}

	public void setChangeVisible(boolean changeVisible) {
		this.changeVisible = changeVisible;
	}

	public int getCostBuild() {
		return costBuild;
	}

	public void setCostBuild(int costBuild) {
		this.costBuild = costBuild;
	}

	public int getCostSupport() {
		return costSupport;
	}

	public void setCostSupport(int costSupport) {
		this.costSupport = costSupport;
	}

	public int getDamageCapacity() {
		return damageCapacity;
	}

	public void setDamageCapacity(int damageCapacity) {
		this.damageCapacity = damageCapacity;
	}
	
	public int getDropPenalty() {
		return dropPenalty;
	}

	public void setDropPenalty(int dropPenalty) {
		this.dropPenalty = dropPenalty;
	}

	public boolean isSpaceshipTravel() {
		return spaceshipTravel;
	}

	public void setSpaceshipTravel(boolean spaceshipTravel) {
		this.spaceshipTravel = spaceshipTravel;
		changeSpaceshipTravel = true;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		changeVisible = true;
	}

}
