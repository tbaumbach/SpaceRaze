package spaceraze.server.game.update;

import spaceraze.servlethelper.game.BuildingPureFunctions;
import spaceraze.servlethelper.game.ResearchPureFunctions;
import spaceraze.servlethelper.game.gameworld.GameWorldPureFunction;
import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.servlethelper.game.troop.TroopPureFunctions;
import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.orders.ResearchOrder;

public class ResearchPerformer {

    private ResearchPerformer(){}

    public static  void researchAdvantage(Faction faction, String advantageName, TurnInfo ti, Player p, Galaxy galaxy){
        ResearchPerformer.research(ResearchPureFunctions.getAdvantage(faction, advantageName), ti, p, galaxy);
    }

    public static  void performResearch(ResearchOrder researchOrder, TurnInfo ti, Player p, Galaxy galaxy){
        Logger.finest( "performResearch: " + researchOrder.getAdvantageName() + " player: " + p.getName());
        ResearchPerformer.researchAdvantage(GameWorldHandler.getFactionByUuid(p.getFactionUuid(), galaxy.getGameWorld()), researchOrder.getAdvantageName(), ti, p, galaxy);
    }

    public static void research(ResearchAdvantage researchAdvantage, TurnInfo ti, Player p, Galaxy galaxy){
        ResearchProgress researchProgress = p.getResearchProgress(researchAdvantage.getName());
        if(!researchAdvantage.isDeveloped(p)){
            String researchInfoText="";
            Logger.finer("count up researchedTurns from " + researchProgress.getResearchedTurns());
            researchProgress.setResearchedTurns(researchProgress.getResearchedTurns() + 1);
            // The research is done and it's time to add the results.
            if(researchProgress.getResearchedTurns() >= researchAdvantage.getTimeToResearch()){
                researchInfoText="The reaserch on " + researchAdvantage.getName() + " is fineshed and gives you this:\n";
                // update Player with all new objects and bonus.

                researchProgress.setDeveloped(true);


                if(researchAdvantage.getOpenPlanetBonus() > 0){
                    p.setOpenPlanetBonus(p.getOpenPlanetBonus() + researchAdvantage.getOpenPlanetBonus());
                    researchInfoText+="Open planet bonus is now: " + p.getOpenPlanetBonus() + "\n";
                }
                if(researchAdvantage.getClosedPlanetBonus() > 0){
                    p.setClosedPlanetBonus(p.getClosedPlanetBonus() + researchAdvantage.getClosedPlanetBonus());
                    researchInfoText+="Closed planet bonus is now: " + p.getClosedPlanetBonus() + "\n";
                }
                if(researchAdvantage.getResistanceBonus() > 0){
                    p.setResistanceBonus(p.getResistanceBonus() + researchAdvantage.getResistanceBonus());
                    researchInfoText+="Resistance bonus is now: " + p.getResistanceBonus() + "\n";
                }

                if(researchAdvantage.getTechBonus() > 0){
                    p.setTechBonus(p.getTechBonus() + researchAdvantage.getTechBonus());
                    researchInfoText+="Tech bonus is now: " + p.getTechBonus() + "%\n";
                }
                if(researchAdvantage.isCanReconstruct()){
                    p.setCanReconstruct(true);
                    researchInfoText+="You can now reconstruct planets\n";
                }
                if(researchAdvantage.getReconstructCostBase() > 0){
                    p.setReconstructCostBase(p.getReconstructCostBase() - researchAdvantage.getReconstructCostBase());
                    researchInfoText+="Reconstruct cost base is now: " + p.getReconstructCostBase() + "\n";
                }
                if(researchAdvantage.getCorruptionPoint() != null){

                    p.setCorruptionPoint(researchAdvantage.getCorruptionPoint());
                    researchInfoText+="Corruption is now: " + p.getCorruptionPoint() != null ? p.getCorruptionPoint().getDescription() : "None" + "\n";
                }

                // adding ships to the player
                for(String uuid : researchAdvantage.getShips()) {
                    SpaceshipType spaceshipType = SpaceshipPureFunctions.getSpaceshipTypeByUuid(uuid, p.getGalaxy().getGameWorld());
                    PlayerPureFunctions.findSpaceshipImprovement(uuid, p).setAvailableToBuild(true);
                    Logger.finer("adding a new ship typ : " + spaceshipType.getName());
                    researchInfoText+= "A new ship type: " + spaceshipType.getName() + ".\n";
                }

                //	removing old ships models from the player
                for(String uuid : researchAdvantage.getReplaceShips()) {
                    SpaceshipType spaceshipType = SpaceshipPureFunctions.getSpaceshipTypeByUuid(uuid, p.getGalaxy().getGameWorld());
                    PlayerPureFunctions.findSpaceshipImprovement(uuid, p).setAvailableToBuild(false);
                    Logger.finer("Removing old ship typ : " + spaceshipType.getName());
                    researchInfoText+= "The ship type: " + spaceshipType.getName() + " was removed.\n";
                }

                // adding troops to the player
                for (String uuid : researchAdvantage.getTroops()) {
                    TroopType troopType = TroopPureFunctions.getTroopTypeByUuid(uuid, p.getGalaxy().getGameWorld());
                    PlayerPureFunctions.findTroopImprovement(uuid, p).setAvailableToBuild(true);
                    Logger.finer("adding a new troop type: " + troopType.getName());
                    researchInfoText += "A new troop type: " + troopType.getName() + ".\n";
                }

                //	removing old troop types from the player
                for(String uuid : researchAdvantage.getReplaceTroops()){
                    TroopType troopType = TroopPureFunctions.getTroopTypeByUuid(uuid, p.getGalaxy().getGameWorld());
                    PlayerPureFunctions.findTroopImprovement(uuid, p).setAvailableToBuild(false);
                    Logger.finer("Removing old troop type : " + troopType.getName());
                    researchInfoText += "The troop type: " + troopType.getName() + " was removed.\n";
                }

                //adding Buildings to the player
                for (String uuid : researchAdvantage.getBuildings()) {
                    BuildingType buildingType = BuildingPureFunctions.getBuildingTypeByUuid(uuid, galaxy.getGameWorld());
                    PlayerPureFunctions.findBuildingImprovementByUuid(uuid, p).setDeveloped(true);
                    Logger.finer("adding a new building type: " + buildingType.getName());
                    researchInfoText += "A new building type: " + buildingType.getName() + ".\n";
                }

                //removing Buildings to the player
                for (String uuid : researchAdvantage.getReplaceBuildings()) {
                    BuildingType buildingType = BuildingPureFunctions.getBuildingTypeByUuid(uuid, galaxy.getGameWorld());
                    PlayerPureFunctions.findBuildingImprovementByUuid(uuid, p).setDeveloped(false);
                    Logger.finer("Removing old building type: " + buildingType.getName());
                    researchInfoText += "The building type: " + buildingType.getName() + " was removed.\n";
                }

                // check if a childe researchAdvantage have timeToResearch = 0 and ready to be research().

                for(String childUuid : researchAdvantage.getChildren()){
                    ResearchAdvantage child = GameWorldPureFunction.getResearchAdvantageByUuid(GameWorldHandler.getFactionByUuid(p.getFactionUuid(), p.getGalaxy().getGameWorld()), childUuid);
                    if(child.getTimeToResearch() == 0 && ResearchPureFunctions.isReadyToBeResearchedOn(childUuid, p,  GameWorldHandler.getFactionByUuid(p.getFactionUuid(), p.getGalaxy().getGameWorld()))){
                        //TODO (Tobbe) add researchText.
                        ResearchPerformer.research(child, ti, p, galaxy);
                    }
                }

                for(ResearchUpgradeShip researchUpgradeShip : researchAdvantage.getResearchUpgradeShip()){
                    researchInfoText+= doResearch(researchUpgradeShip, PlayerPureFunctions.findSpaceshipImprovement(researchUpgradeShip.getTypeUuid(), p), galaxy.getGameWorld());
                }

                for (ResearchUpgradeTroop aResearchUpgradeTroop : researchAdvantage.getResearchUpgradeTroop()) {
                    researchInfoText += doResearch(aResearchUpgradeTroop, PlayerPureFunctions.findTroopImprovement(aResearchUpgradeTroop.getTypeUuid(), p), galaxy.getGameWorld());
                }

                for (ResearchUpgradeBuilding aResearchUpgradeBuilding : researchAdvantage.getResearchUpgradeBuilding()) {
                    researchInfoText += doResearch(aResearchUpgradeBuilding, PlayerPureFunctions.findBuildingImprovementByUuid(aResearchUpgradeBuilding.getTypeUuid(), p), galaxy.getGameWorld());
                }
                p.addToHighlights(researchAdvantage.getName(), HighlightType.TYPE_RESEARCH_DONE);
            }
            else{
                researchInfoText = "Research have been performed on " + researchAdvantage.getName() + " and will continue if you don't change the research orders";
            }
            ti.addToLatestResearchReport(researchInfoText);
        }

    }

    public static String doResearch(ResearchUpgradeShip researchUpgradeShip, PlayerSpaceshipImprovement improvement, GameWorld gameWorld){
        String text;
        text = "\nThe ship model " + SpaceshipPureFunctions.getSpaceshipTypeByUuid(improvement.getTypeUuid(), gameWorld).getName() + " have been upgrade";

        if(researchUpgradeShip.getShields() != 0){
            improvement.setShields(improvement.getShields() + researchUpgradeShip.getShields());
        }
        if(researchUpgradeShip.getUpkeep() != 0){
            improvement.setUpkeep(improvement.getUpkeep() + researchUpgradeShip.getUpkeep());
        }
        if(researchUpgradeShip.getBuildCost() != 0){
            improvement.setBuildCost(improvement.getBuildCost() + researchUpgradeShip.getBuildCost());
        }
        if(researchUpgradeShip.getBombardment() != 0){
            improvement.setBombardment(improvement.getBombardment() + researchUpgradeShip.getBombardment());
        }
        if(researchUpgradeShip.getIncreaseInitiative() > 0){
            improvement.setIncreaseInitiative(improvement.getIncreaseInitiative() + researchUpgradeShip.getIncreaseInitiative());
        }
        if(researchUpgradeShip.getInitDefence() != 0){
            improvement.setInitDefence(improvement.getInitDefence() + researchUpgradeShip.getInitDefence());
        }
        if(researchUpgradeShip.getPsychWarfare() != 0){
            improvement.setPsychWarfare(improvement.getPsychWarfare() + researchUpgradeShip.getPsychWarfare());
        }
        if(researchUpgradeShip.getRange() != null){
            improvement.setRange(researchUpgradeShip.getRange());
        }
        if(researchUpgradeShip.isChangeNoRetreat()){
            improvement.setNoRetreat(researchUpgradeShip.isNoRetreat());
        }
        if(researchUpgradeShip.isChangeInitSupport()){
            improvement.setInitSupport(researchUpgradeShip.isInitSupport());
        }
        if(researchUpgradeShip.getWeaponsStrengthSmall() != 0){
            improvement.setWeaponsStrengthSmall(improvement.getWeaponsStrengthSmall() + researchUpgradeShip.getWeaponsStrengthSmall());
        }
        if(researchUpgradeShip.getWeaponsStrengthMedium() != 0){
            improvement.setWeaponsStrengthMedium(improvement.getWeaponsStrengthMedium() + researchUpgradeShip.getWeaponsStrengthMedium());
        }
        if(researchUpgradeShip.getWeaponsStrengthLarge() != 0){
            improvement.setWeaponsStrengthLarge(improvement.getWeaponsStrengthLarge() + researchUpgradeShip.getWeaponsStrengthLarge());
        }
        if(researchUpgradeShip.getWeaponsStrengthHuge() != 0){
            improvement.setWeaponsStrengthHuge(improvement.getWeaponsStrengthHuge() + researchUpgradeShip.getWeaponsStrengthHuge());
        }
        if(researchUpgradeShip.getWeaponsMaxSalvosMedium() != 0){
            improvement.setWeaponsMaxSalvosMedium(improvement.getWeaponsMaxSalvosMedium() + researchUpgradeShip.getWeaponsMaxSalvosMedium());
        }
        if(researchUpgradeShip.getWeaponsMaxSalvosLarge() != 0){
            improvement.setWeaponsMaxSalvosLarge(improvement.getWeaponsMaxSalvosLarge() + researchUpgradeShip.getWeaponsMaxSalvosLarge());
        }
        if(researchUpgradeShip.getWeaponsMaxSalvosHuge() != 0){
            improvement.setWeaponsMaxSalvosHuge(improvement.getWeaponsMaxSalvosHuge() + researchUpgradeShip.getWeaponsMaxSalvosHuge());
        }
        if(researchUpgradeShip.getArmorSmall() != 0){
            improvement.setArmorSmall(improvement.getArmorSmall() + researchUpgradeShip.getArmorSmall());
        }
        if(researchUpgradeShip.getArmorMedium() != 0){
            improvement.setArmorMedium(improvement.getArmorMedium() + researchUpgradeShip.getArmorMedium());
        }
        if(researchUpgradeShip.getArmorLarge() != 0){
            improvement.setArmorLarge(improvement.getArmorLarge() + researchUpgradeShip.getArmorLarge());
        }
        if(researchUpgradeShip.getArmorHuge() != 0){
            improvement.setArmorHuge(improvement.getArmorHuge() + researchUpgradeShip.getArmorHuge());
        }
        if(researchUpgradeShip.getSupply() != null){
            improvement.setSupply(researchUpgradeShip.getSupply());
        }
        if(researchUpgradeShip.getWeaponsStrengthSquadron() != 0){
            improvement.setWeaponsStrengthSquadron(improvement.getWeaponsStrengthSquadron() + researchUpgradeShip.getWeaponsStrengthSquadron());
        }
        if(researchUpgradeShip.getSquadronCapacity() != 0){
            improvement.setSquadronCapacity(improvement.getSquadronCapacity() + researchUpgradeShip.getSquadronCapacity());
        }
        if(researchUpgradeShip.getIncEnemyClosedBonus() != 0){
            improvement.setIncEnemyClosedBonus(improvement.getIncEnemyClosedBonus() + researchUpgradeShip.getIncEnemyClosedBonus());
        }
        if(researchUpgradeShip.getIncNeutralClosedBonus() != 0){
            improvement.setIncNeutralClosedBonus(improvement.getIncNeutralClosedBonus() + researchUpgradeShip.getIncNeutralClosedBonus());
        }
        if(researchUpgradeShip.getIncFriendlyClosedBonus() != 0){
            improvement.setIncFriendlyClosedBonus(improvement.getIncFriendlyClosedBonus() + researchUpgradeShip.getIncFriendlyClosedBonus());
        }
        if(researchUpgradeShip.getIncOwnClosedBonus() != 0){
            improvement.setIncOwnClosedBonus(improvement.getIncOwnClosedBonus() + researchUpgradeShip.getIncOwnClosedBonus());
        }
        if(researchUpgradeShip.getIncEnemyOpenBonus() != 0){
            improvement.setIncEnemyOpenBonus(improvement.getIncEnemyOpenBonus() + researchUpgradeShip.getIncEnemyOpenBonus());
        }
        if(researchUpgradeShip.getIncNeutralOpenBonus() != 0){
            improvement.setIncNeutralOpenBonus(improvement.getIncNeutralOpenBonus() + researchUpgradeShip.getIncNeutralOpenBonus());
        }
        if(researchUpgradeShip.getIncFriendlyOpenBonus() != 0){
            improvement.setIncFriendlyOpenBonus(improvement.getIncFriendlyOpenBonus() + researchUpgradeShip.getIncFriendlyOpenBonus());
        }
        if(researchUpgradeShip.getIncOwnOpenBonus() != 0){
            improvement.setIncOwnOpenBonus(improvement.getIncOwnOpenBonus() + researchUpgradeShip.getIncOwnOpenBonus());
        }
        if(researchUpgradeShip.getDescription() != null){
            improvement.setDescription(researchUpgradeShip.getDescription());
        }
        if(researchUpgradeShip.getHistory() != null){
            improvement.setHistory(researchUpgradeShip.getHistory());
        }
    /*	if(shortDescription != null){
    		ship.setShortDescription(shortDescription);
    	}
    	if(advantages != null){
    		ship.setAdvantages(advantages);
    	}
    	if(disadvantages != null){
    		ship.setDisadvantages(disadvantages);
    	}*/
        if(researchUpgradeShip.isChangePlanetarySurvey()){
            improvement.setPlanetarySurvey(researchUpgradeShip.isPlanetarySurvey());
        }
        if(researchUpgradeShip.isCanAttackScreenedShips()){
            improvement.setCanAttackScreenedShips(researchUpgradeShip.isCanAttackScreenedShips());
        }
        if(researchUpgradeShip.isLookAsCivilian()){
            improvement.setLookAsCivilian(researchUpgradeShip.isLookAsCivilian());
        }
        if(researchUpgradeShip.isChangeCanBlockPlanet()){
            improvement.setCanBlockPlanet(researchUpgradeShip.isCanBlockPlanet());
        }
        if(researchUpgradeShip.isChangeVisibleOnMap()){
            improvement.setVisibleOnMap(researchUpgradeShip.isVisibleOnMap());
        }
        if(researchUpgradeShip.getTroopCarrier() != 0){
            improvement.setTroopCarrier(improvement.getTroopCarrier() + researchUpgradeShip.getTroopCarrier());
        }

        return text;
    }

    public static String doResearch(ResearchUpgradeTroop researchUpgradeTroop, PlayerTroopImprovement improvement, GameWorld gameWorld){
        String text;
        text = "\nThe troop type " + TroopPureFunctions.getTroopTypeByUuid(researchUpgradeTroop.getTypeUuid(), gameWorld).getName() + " has been upgraded";

        if(researchUpgradeTroop.getAttackInfantry() != 0){
            improvement.setAttackInfantry(improvement.getAttackInfantry() + researchUpgradeTroop.getAttackInfantry());
        }
        if(researchUpgradeTroop.getAttackArmored() != 0){
            improvement.setAttackArmored(improvement.getAttackArmored() + researchUpgradeTroop.getAttackArmored());
        }
        if(researchUpgradeTroop.getAttackArtillery() != 0){
            improvement.setAttackArtillery(improvement.getAttackArtillery() + researchUpgradeTroop.getAttackArtillery());
        }
        if(researchUpgradeTroop.getDamageCapacity() != 0){
            improvement.setDamageCapacity(improvement.getDamageCapacity() + researchUpgradeTroop.getDamageCapacity());
        }
        if(researchUpgradeTroop.getCostBuild() != 0){
            improvement.setCostBuild(improvement.getCostBuild() + researchUpgradeTroop.getCostBuild());
        }
        if(researchUpgradeTroop.getCostSupport() != 0){
            improvement.setCostSupport(improvement.getCostSupport() + researchUpgradeTroop.getCostSupport());
        }
        if (researchUpgradeTroop.isChangeSpaceshipTravel()){
            improvement.setSpaceshipTravel(researchUpgradeTroop.isSpaceshipTravel());
        }
    /*	if (changeAttackScreened){
    		aTroop.setAttackScreened(attackScreened);
    	}*/
        if (researchUpgradeTroop.isChangeVisible()){
            improvement.setVisible(researchUpgradeTroop.isVisible());
        }
        return text;
    }

    public static String doResearch(ResearchUpgradeBuilding researchUpgradeBuilding, PlayerBuildingImprovement improvement, GameWorld gameWorld){
        String text;
        text = "\nThe building type " + BuildingPureFunctions.getBuildingTypeByUuid(researchUpgradeBuilding.getTypeUuid(), gameWorld).getName() + " has been upgraded";

        if(researchUpgradeBuilding.getBuildCost() > 0){
            improvement.setBuildCost(improvement.getBuildCost() + researchUpgradeBuilding.getBuildCost());
        }

        if(researchUpgradeBuilding.getWharfSize() > 0){
            improvement.setWharfSize(researchUpgradeBuilding.getWharfSize());
        }
        if(researchUpgradeBuilding.getTroopSize() > 0){
            improvement.setTroopSize(researchUpgradeBuilding.getTroopSize());
        }
        if(researchUpgradeBuilding.getExterminator() > 0){
            improvement.setExterminator(improvement.getExterminator() + researchUpgradeBuilding.getExterminator());
        }
        if(researchUpgradeBuilding.getResistanceBonus() > 0){
            improvement.setResistanceBonus(improvement.getResistanceBonus() + researchUpgradeBuilding.getResistanceBonus());
        }
        if(researchUpgradeBuilding.getShieldCapacity() > 0){
            improvement.setShieldCapacity(improvement.getShieldCapacity() + researchUpgradeBuilding.getShieldCapacity());
        }
        if(researchUpgradeBuilding.getCannonDamage() > 0){
            improvement.setCannonDamage(improvement.getCannonDamage() + researchUpgradeBuilding.getCannonDamage());
        }
        if(researchUpgradeBuilding.getCannonRateOfFire() > 0){
            improvement.setCannonRateOfFire(improvement.getCannonRateOfFire() + researchUpgradeBuilding.getCannonRateOfFire());
        }
	    	/*if(shipBuildBonus > 0){
	    		improvement.setShipBuildBonus(improvement.getShipBuildBonus() + shipBuildBonus);
	    	}
	    	if(troopBuildBonus > 0){
	    		improvement.setTroopBuildBonus(improvement.getTroopBuildBonus() + troopBuildBonus);
	    	}
	    	if(vipBuildBonus > 0){
	    		improvement.setVipBuildBonus(improvement.getVipBuildBonus() + vipBuildBonus);
	    	}
	    	if(buildingBuildBonus > 0){
	    		improvement.setBuildingBuildBonus(improvement.getBuildingBuildBonus() + buildingBuildBonus);
	    	}*/
        if(researchUpgradeBuilding.getTechBonus() > 0){
            improvement.setTechBonus(improvement.getTechBonus() + researchUpgradeBuilding.getTechBonus());
        }
        if(researchUpgradeBuilding.getOpenPlanetBonus() > 0){
            improvement.setOpenPlanetBonus(improvement.getOpenPlanetBonus() + researchUpgradeBuilding.getOpenPlanetBonus());
        }
        if(researchUpgradeBuilding.getClosedPlanetBonus() > 0){
            improvement.setClosedPlanetBonus(improvement.getClosedPlanetBonus() + researchUpgradeBuilding.getClosedPlanetBonus());
        }
        if(researchUpgradeBuilding.isChangeSpaceport()){
            improvement.setSpaceport(researchUpgradeBuilding.isSpaceport());
        }
        if(researchUpgradeBuilding.isChangeVisibleOnMap()){
            improvement.setVisibleOnMap(researchUpgradeBuilding.isVisibleOnMap());
        }
	    	/*
	    	if(aimBonus > 0){
	    		improvement.setAimBonus(improvement.getAimBonus() + aimBonus);
	    	}
	    	if(troopAttacksBonus > 0){
	    		improvement.setTroopAttacksBonus(improvement.getTroopAttacksBonus() + troopAttacksBonus);
	    	}
	    	if(landBattleGroupAttacksBonus > 0){
	    		improvement.setLandBattleGroupAttacksBonus(improvement.getLandBattleGroupAttacksBonus() + landBattleGroupAttacksBonus);
	    	}*/

        return text;
    }
}
