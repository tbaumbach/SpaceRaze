package spaceraze.server.game.update;

import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.orders.Expense;
import spaceraze.world.orders.Orders;

public class ExpensePerformer {

    private ExpensePerformer(){}

    public static void performExpense(Expense expense, TurnInfo ti, Player p, Orders o, Galaxy galaxy){

        Logger.finer("Expense.performExpense(TurnInfo ti, Player p, Orders o) type: " +  expense.getType());
        Galaxy g = p.getGalaxy();
        Player playerToResive = g.getPlayer(expense.getPlayerName());
        Planet planet = null;
        if(expense.getPlanetName() != null && !expense.getPlanetName().equalsIgnoreCase("")){
            planet = g.getPlanet(expense.getPlanetName());
        }
        if (expense.getType().equalsIgnoreCase("pop")){
            g.getPlayer(expense.getPlayerName()).removeFromTreasury(planet.getPopulation());
            planet.increasePopulation();
            ti.addToLatestExpenseReport(planet.getName() + " has increased its production from " + (planet.getPopulation() - 1) + " to " + planet.getPopulation() + ".");
            ti.addToLatestExpenseReport("Cost to increase production: " + planet.getPopulation() + ".");
        }else
        if (expense.getType().equalsIgnoreCase("res")){
            planet.increaseResistance();
            ti.addToLatestExpenseReport(planet.getName() + " has increased its resistance from " + (planet.getResistance() - 1) + " to " + planet.getResistance() + ".");
            planet.getPlayerInControl().removeFromTreasury(planet.getResistance());
            ti.addToLatestExpenseReport("Cost to increase resistance: " + planet.getResistance() + ".");
        }else
        if (expense.getType().equalsIgnoreCase("building")){

            BuildingType buildingType = g.getPlayer(expense.getPlayerName()).findBuildingType(expense.getBuildingTypeName());

            String uniqueBuildingString="";
            boolean buildBuilding = true;

            VIP tempVIP = p.getGalaxy().findVIPBuildingBuildBonus(planet,p,o);

            if(buildingType.isWorldUnique()){
                if(!buildingType.isWorldUniqueBuild(p.getGalaxy())){
                    uniqueBuildingString = "Congratulations you have build the world unique " + buildingType.getName() + ".";
                }else{// The building can't be build.
                    buildBuilding =  false;
                    uniqueBuildingString = "You can not build the world unique " + buildingType.getName() + " building. Some other organisation was faster then you.";
                }
            }else
            if(buildingType.isFactionUnique()){
                if(!buildingType.isFactionUniqueBuild(p)){
                    uniqueBuildingString = "Congratulations you have build the faction unique " + buildingType.getName() + ".";
                }else{// The building can't be build.
                    buildBuilding =  false;
                    uniqueBuildingString = "You can not build the faction unique " + buildingType.getName() + " building. Some other leader was faster then you.";
                }
            }else
            if(buildingType.isPlayerUnique()){
                if(!buildingType.isPlayerUniqueBuild(p)){
                    uniqueBuildingString = "You have build the player unique " + buildingType.getName() + " and you can not build more of this type.";

                }else{// The building can't be build. Should never happend if the orders is checked then the select box is filled.
                    buildBuilding =  false;
                    uniqueBuildingString = "You can not build the player unique " + buildingType.getName() + " building, you have already the building.";
                }
            }

            if(buildBuilding){
                planet.getPlayerInControl().removeFromTreasury(buildingType.getBuildCost(tempVIP));

                Building tempBuilding = null;
                tempBuilding = buildingType.getBuilding(planet, g);
                // add the building to the planet.
                planet.addBuilding(tempBuilding);
                // if the building have any parent building this is a upgrade and the parent building should be removed
                if(tempBuilding.getBuildingType().getParentBuilding() != null){
                    ti.addToLatestExpenseReport("You have upgraded a " + tempBuilding.getBuildingType().getParentBuilding().getName() + " to a " + tempBuilding.getBuildingType().getName() + " at the planet " + planet.getName() + ".");
                    ti.addToLatestExpenseReport("Cost to upgrade " + tempBuilding.getBuildingType().getName() + ": " + buildingType.getBuildCost(tempVIP) + ".");
                    //planet.removeBuilding(tempBuilding.getBuildingType().getName());
                    planet.removeBuilding(expense.getCurrentBuildingId());
                }else{
                    ti.addToLatestExpenseReport("You have built a new " + tempBuilding.getBuildingType().getName() + ") at the planet " + planet.getName() + ".");
                    ti.addToLatestExpenseReport("Cost to build new " + tempBuilding.getBuildingType().getName() + ": " + buildingType.getBuildCost(tempVIP) + ".");
                }
            }//else{// the building is unique and cant be build.
            if(!uniqueBuildingString.equalsIgnoreCase("")){
                ti.addToLatestExpenseReport(uniqueBuildingString);
            }
        }else
        if (expense.getType().equalsIgnoreCase("buildship")){

            String uniqueBuildingString="";
            boolean buildShip = true;

            SpaceshipType sst = PlayerPureFunctions.findSpaceshipType(expense.getSpaceshipTypeName(), p, galaxy);

            if(sst.isWorldUnique()){
                if(!sst.isWorldUniqueBuild(p.getGalaxy())){
                    uniqueBuildingString = "Congratulations you have build the world unique " + sst.getName() + ".";
                }else{// The building can't be build.
                    buildShip =  false;
                    uniqueBuildingString = "You can not build the world unique " + sst.getName() + " ship. Some other organisation was faster then you.";
                }
            }else
            if(sst.isFactionUnique()){
                if(!sst.isFactionUniqueBuild(p)){
                    uniqueBuildingString = "Congratulations you have build the faction unique " + sst.getName() + ".";
                }else{// The building can't be build.
                    buildShip =  false;
                    uniqueBuildingString = "You can not build the faction unique " + sst.getName() + " ship. Some other leader was faster then you.";
                }
            }else
            if(sst.isPlayerUnique()){
                if(!sst.isPlayerUniqueBuild(p)){
                    uniqueBuildingString = "You have build the player unique " + sst.getName() + " and you can not build more of this type.";

                }else{// The building can't be build. Should never happend if the orders is checked then the select box is filled.
                    buildShip =  false;
                    uniqueBuildingString = "You can not build the player unique " + sst.getName() + " ship, you have already the ship.";
                }
            }

            if(buildShip){
                Spaceship sstemp = null;
                VIP tempVIP = p.getGalaxy().findVIPShipBuildBonus(planet,p,o);
                VIP tempVIP2 = p.getGalaxy().findVIPTechBonus(planet,p,o);
                int factionTechBonus = p.getFaction().getTechBonus();

                sstemp = SpaceshipMutator.createSpaceShip(p, sst, tempVIP2, g, factionTechBonus, planet.getBuildingTechBonus(), g.getUniqueIdCounter("Ship").getUniqueId());
                //sstemp = sst.getShip(tempVIP2,factionTechBonus,planet.getBuildingTechBonus());
                //sstemp = ow.buildShip(sst,tempVIP2,factionTechBonus);
                Logger.finest(" -buildship planet: " + sstemp.getTypeName());
                sstemp.setOwner(planet.getPlayerInControl());
                sstemp.setLocation(planet);
                g.addSpaceship(sstemp);
                ti.addToLatestExpenseReport("You have built a new " + sst.getName() + " (named " + sstemp.getName() + ") at " + planet.getName() + ".");
                // TODO (Tobbe) lägg bonusen för buildings.  Skall bonus addas eller skall den som är störst gälla.
                planet.getPlayerInControl().removeFromTreasury(sst.getBuildCost(tempVIP));
                Logger.finest(" -buildship loc name: " + planet.getName());
                ti.addToLatestExpenseReport("Cost to build new " + sst.getName() + ": " + sst.getBuildCost(tempVIP) + ".");

            } // the ship is unique and cant be build.
            if(!uniqueBuildingString.equalsIgnoreCase("")){
                ti.addToLatestExpenseReport(uniqueBuildingString);
            }

        }else
        if (expense.getType().equalsIgnoreCase("buildtroop")){

            String uniqueBuildingString="";
            boolean buildTroop = true;

            TroopType troopType = PlayerPureFunctions.findOwnTroopType(expense.getTroopTypeName(), g.getPlayer(expense.getPlayerName()), g);

            if(troopType.isWorldUnique()){
                if(!troopType.isWorldUniqueBuild(p.getGalaxy())){
                    uniqueBuildingString = "Congratulations you have build the world unique " + troopType.getUniqueName() + ".";
                }else{// The building can't be build.
                    buildTroop =  false;
                    uniqueBuildingString = "You can not build the world unique " + troopType.getUniqueName() + " troop. Some other organisation was faster then you.";
                }
            }else
            if(troopType.isFactionUnique()){
                if(!troopType.isFactionUniqueBuild(p)){
                    uniqueBuildingString = "Congratulations you have build the faction unique " + troopType.getUniqueName() + ".";
                }else{// The building can't be build.
                    buildTroop =  false;
                    uniqueBuildingString = "You can not build the faction unique " + troopType.getUniqueName() + " troop. Some other leader was faster then you.";
                }
            }else
            if(troopType.isPlayerUnique()){
                if(!troopType.isPlayerUniqueBuild(p)){
                    uniqueBuildingString = "You have build the player unique " + troopType.getUniqueName() + " and you can not build more of this type.";

                }else{// The building can't be build. Should never happend if the orders is checked then the select box is filled.
                    buildTroop =  false;
                    uniqueBuildingString = "You can not build the player unique " + troopType.getUniqueName() + " troop, you have already the troop.";
                }
            }

            if(buildTroop){

                Troop tempTroop = null;

                VIP tempVIP = p.getGalaxy().findVIPTroopBuildBonus(planet,p,o);
                VIP tempVIP2 = p.getGalaxy().findVIPTechBonus(planet,p,o);
                int factionTechBonus = p.getFaction().getTechBonus();

                tempTroop = TroopMutator.createTroop(p, troopType, tempVIP2, factionTechBonus, planet.getBuildingTechBonus(), galaxy.getUniqueIdCounter("Trrop").getUniqueId());
                //sstemp = ow.buildShip(sst,tempVIP2,factionTechBonus);
                Logger.finest(" -buildship planet: " + tempTroop.getUniqueName());
                tempTroop.setOwner(planet.getPlayerInControl());
                tempTroop.setPlanetLocation(planet);
                g.addTroop(tempTroop);
                ti.addToLatestExpenseReport("You have built a new " + troopType.getUniqueName() + " (named " + tempTroop.getUniqueName() + ") at " + planet.getName() + ".");
                // TODO (Tobbe) lägg bonusen för buildings.  Skall bonus addas eller skall den som är störst gälla.
                planet.getPlayerInControl().removeFromTreasury(troopType.getCostBuild(tempVIP));
                Logger.finest(" -buildtroop loc name: " + planet.getName());
                ti.addToLatestExpenseReport("Cost to build new " + troopType.getUniqueName() + ": " + troopType.getCostBuild(tempVIP) + ".");

            }//else{// the ship is unique and cant be build.
            if(!uniqueBuildingString.equalsIgnoreCase("")){
                ti.addToLatestExpenseReport(uniqueBuildingString);
            }


        }else
        if (expense.getType().equalsIgnoreCase("buildVIP")){

            String uniqueVIPString="";
            boolean buildVIP = true;


            // TODO (Tobbe) gör om.  går inte att använda VIPar från bara player om den är worldunique.
            VIPType vipType = p.getGalaxy().findVIPType(expense.getVipTypeName());

            if(vipType.isWorldUnique()){
                if(!p.getGalaxy().findVIPType(expense.getVipTypeName()).isWorldUniqueBuild(p.getGalaxy())){
                    uniqueVIPString = "Congratulations you have build the world unique " + expense.getVipTypeName() + ".";
                }else{// The VIP can't be build.
                    buildVIP =  false;
                    uniqueVIPString = "You can not build the world unique " + expense.getVipTypeName() + " VIP. Some other organisation was faster then you.";
                }
            }

            if(vipType.isWorldUnique()){ // används inte så länge alignment finns kvar. eller alla VIPar ligger i GW
                if(!vipType.isWorldUniqueBuild(p.getGalaxy())){
                    uniqueVIPString = "Congratulations you have build the world unique " + vipType.getName() + ".";
                }else{// The VIP can't be build.
                    buildVIP =  false;
                    uniqueVIPString = "You can not build the world unique " + vipType.getName() + " VIP. Some other organisation was faster then you.";
                }
            }else
            if(vipType.isFactionUnique()){ // anv�nds inte så länge alignment finns kvar. eller alla VIPar ligger i GW
                if(!vipType.isFactionUniqueBuild(p)){

                    uniqueVIPString = "Congratulations you have build the faction unique " + vipType.getName() + ".";
                }else{// The VIP can't be build.
                    buildVIP =  false;
                    uniqueVIPString = "You can not build the faction unique " + vipType.getName() + " VIP. Some other leader was faster then you.";
                }
            }else
            if(vipType.isPlayerUnique()){ // används inte så länge alignment finns kvar. eller alla VIPar ligger i GW
                if(!vipType.isPlayerUniqueBuild(p)){
                    uniqueVIPString = "You have build the player unique " + vipType.getName() + " and you can not build more of this type.";

                }else{// The VIP can't be build. Should never happend if the orders is checked then the select box is filled.
                    buildVIP =  false;
                    uniqueVIPString = "You can not build the player unique " + vipType.getName() + " VIP, you have already the VIP.";
                }
            }

            if(buildVIP){

                VIP vip = null;

                vip = p.getGalaxy().findVIPType(expense.getVipTypeName()).createNewVIP(planet.getPlayerInControl(), planet, false);
                Logger.finest(" -buildVIP planet: " + vip.getTypeName());
                g.getAllVIPs().add(vip);
                ti.addToLatestExpenseReport("You have built a new " + vip.getName() + " (named " + vip.getName() + ") at " + planet.getName() + ".");
                planet.getPlayerInControl().removeFromTreasury(vip.getBuildCost());
                ti.addToLatestExpenseReport("Cost to build new " + vip.getName() + ": " + vip.getBuildCost() + ".");
            }//else{// the VIP is unique and cant be build.
            if(!uniqueVIPString.equalsIgnoreCase("")){
                ti.addToLatestExpenseReport(uniqueVIPString);
            }




        }else
        if (expense.getType().equalsIgnoreCase("transaction")){

            p.addToTreasury(-expense.getSum());
            playerToResive.addToTreasury(expense.getSum());
            p.addToGeneral("You have given " + expense.getSum() + " money to Govenor " + playerToResive.getGovernorName() + ".");
            playerToResive.addToGeneral("You have recieved " + expense.getSum() + " money from Govenor " + p.getGovernorName() + ".");
            playerToResive.addToHighlights(p.getGovernorName() + ";" + expense.getSum(), HighlightType.TYPE_GIFT);
        }else
        if (expense.getType().equalsIgnoreCase("reconstruct")){
            planet.setProd(1);
            planet.setRes(1 + playerToResive.getFaction().getResistanceBonus());
            planet.setPlayerInControl(playerToResive);
            playerToResive.getPlanetInfos().setRazed(false,planet.getName());
            int cost = playerToResive.getFaction().getReconstructCostBase();
            p.removeFromTreasury(cost);
            playerToResive.addToGeneral("You have reconstructed the planet " + planet.getName() + " and it is now under your control with a production of 1.");
            playerToResive.addToHighlights(planet.getName(),HighlightType.TYPE_PLANET_RECONSTRUCTED);
        }else
        if(expense.getType().equalsIgnoreCase("research")){
            p.addToTreasury(-expense.getResearchOrder().getCost());
            ti.addToLatestExpenseReport("You have spend " + expense.getResearchOrder().getCost() + " in research on: " + expense.getResearchOrder().getAdvantageName() + ".");

        }else if(expense.getBlackMarketBid() != null){
//      aBid.addPlayer(player);
            g.addBlackMarketBid(expense.getBlackMarketBid());
        }
    }
}
