package spaceraze.server.game.update;

import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.orders.ResearchOrder;

public class ResearchPerformer {

    private ResearchPerformer(){}

    public static  void researchAdvantage(Research research, String advantageName, TurnInfo ti, Player p, Galaxy galaxy){
        ResearchPerformer.research(research.getAdvantage(advantageName), ti, p, galaxy);
    }

    public static  void performResearch(ResearchOrder researchOrder, TurnInfo ti, Player p, Galaxy galaxy){
        Logger.finest( "performResearch: " + researchOrder.getAdvantageName() + " player: " + p.getName());
        ResearchPerformer.researchAdvantage(p.getResearch(), researchOrder.getAdvantageName(), ti, p, galaxy);
    }

    public static void research(ResearchAdvantage researchAdvantage, TurnInfo ti, Player p, Galaxy galaxy){
        if(!researchAdvantage.isDeveloped()){
            String researchInfoText="";
            Logger.finer("count up researchedTurns from " + researchAdvantage.getResearchedTurns());
            researchAdvantage.setResearchedTurns(researchAdvantage.getResearchedTurns() + 1);
            // The research is done and it's time to add the results.
            if(researchAdvantage.getResearchedTurns() >= researchAdvantage.getTimeToResearch()){
                researchInfoText="The reaserch on " + researchAdvantage.getName() + " is fineshed and gives you this:\n";
                // update Player with all new objects and bonus.

                researchAdvantage.setDeveloped(true);


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
                if(researchAdvantage.getCorruption() != null){

                    p.setCorruption(researchAdvantage.getCorruption());
                    researchInfoText+="Corruption is now: " + p.getCorruptionDescription() + "\n";
                }

                // adding ships to the player
                for(int i=0;i < researchAdvantage.getShips().size();i++ ){
                    PlayerPureFunctions.findOwnSpaceshipType(researchAdvantage.getShips().get(i).getName(), p, galaxy).setAvailableToBuild(true);
                    Logger.finer("adding a new ship typ : " + researchAdvantage.getShips().get(i).getName());
                    researchInfoText+= "A new ship type: " + researchAdvantage.getShips().get(i).getName() + ".\n";
                }

                //	removing old ships models from the player
                for(int i=0;i < researchAdvantage.getReplaceShips().size();i++ ){
                    PlayerPureFunctions.findOwnSpaceshipType(researchAdvantage.getShips().get(i).getName(), p, galaxy).setAvailableToBuild(false);
                    Logger.finer("Removing old ship typ : " + researchAdvantage.getReplaceShips().get(i).getName());
                    researchInfoText+= "The ship type: " + researchAdvantage.getReplaceShips().get(i).getName() + " was removed.\n";
                }

                // adding troops to the player
                for (TroopType aTroopType : researchAdvantage.getTroopTypes()) {
                    p.findTroopType(aTroopType.getUniqueName()).setCanBuild(true);
                    Logger.finer("adding a new troop type: " + aTroopType.getUniqueName());
                    researchInfoText += "A new troop type: " + aTroopType.getUniqueName() + ".\n";
                }

                //	removing old troop types from the player
                for(TroopType aTroopType : researchAdvantage.getReplaceTroopTypes()){
                    p.findTroopType(aTroopType.getUniqueName()).setCanBuild(false);
                    Logger.finer("Removing old troop type : " + aTroopType.getUniqueName());
                    researchInfoText += "The troop type: " + aTroopType.getUniqueName() + " was removed.\n";
                }

                // adding VIPs to the player
                for (VIPType aVIPType : researchAdvantage.getVIPTypes()) {
                    p.findVIPType(aVIPType.getName()).setAvailableToBuild(true);
                    Logger.finer("adding a new VIP type: " + aVIPType.getName());
                    researchInfoText += "A new VIP type: " + aVIPType.getName() + ".\n";
                }

                //	removing old VIPs types from the player
                for (VIPType aVIPType : researchAdvantage.getReplaceVIPTypes()) {
                    p.findVIPType(aVIPType.getName()).setAvailableToBuild(false);
                    Logger.finer("Removing old VIP type: " + aVIPType.getName());
                    researchInfoText += "The VIP type: " + aVIPType.getName() + " was removed.\n";
                }

                //adding Buildings to the player
                for (BuildingType aBuildingType : researchAdvantage.getBuildingTypes()) {
                    p.findBuildingType(aBuildingType.getName()).setDeveloped(true);
                    Logger.finer("adding a new building type: " + aBuildingType.getName());
                    researchInfoText += "A new building type: " + aBuildingType.getName() + ".\n";
                }

                //removing Buildings to the player
                for (BuildingType aBuildingType : researchAdvantage.getReplaceBuildingTypes()) {
                    p.findBuildingType(aBuildingType.getName()).setDeveloped(false);
                    Logger.finer("Removing old building type: " + aBuildingType.getName());
                    researchInfoText += "The building type: " + aBuildingType.getName() + " was removed.\n";
                }

                // check if a childe researchAdvantage have timeToResearch = 0 and ready to be research().

                for(ResearchAdvantage advantage : researchAdvantage.getChildren()){
                    if(advantage.getTimeToResearch() == 0 && advantage.isReadyToBeResearchedOn()){
                        //TODO (Tobbe) add researchText.
                        ResearchPerformer.research(advantage, ti, p, galaxy);
                    }
                }

                for(ResearchUpgradeShip researchUpgradeShip : researchAdvantage.getResearchUpgradeShip()){
                    researchInfoText+= researchUpgradeShip.doResearch(PlayerPureFunctions.findOwnSpaceshipType(researchUpgradeShip.getTypeId(), p, galaxy));
                }

                for (ResearchUpgradeTroop aResearchUpgradeTroop : researchAdvantage.getResearchUpgradeTroop()) {
                    researchInfoText += aResearchUpgradeTroop.doResearch(p.findTroopType(aResearchUpgradeTroop.getName()));
                }
                for (ResearchUpgradeVIP aResearchUpgradeVIP : researchAdvantage.getResearchUpgradeVIP()) {
                    researchInfoText += aResearchUpgradeVIP.doResearch(p.findVIPType(aResearchUpgradeVIP.getName()));
                }
                for (ResearchUpgradeBuilding aResearchUpgradeBuilding : researchAdvantage.getResearchUpgradeBuilding()) {
                    researchInfoText += aResearchUpgradeBuilding.doResearch(p.findBuildingType(aResearchUpgradeBuilding.getName()));
                }
                p.addToHighlights(researchAdvantage.getName(), HighlightType.TYPE_RESEARCH_DONE);
            }
            else{
                researchInfoText = "Research have been performed on " + researchAdvantage.getName() + " and will continue if you don't change the research orders";
            }
            ti.addToLatestResearchReport(researchInfoText);
        }

    }
}
