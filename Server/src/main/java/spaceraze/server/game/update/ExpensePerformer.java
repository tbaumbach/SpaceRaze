package spaceraze.server.game.update;

import spaceraze.servlethelper.game.BuildingPureFunctions;
import spaceraze.servlethelper.game.UniqueIdHandler;
import spaceraze.servlethelper.game.planet.PlanetMutator;
import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.servlethelper.game.troop.TroopPureFunctions;
import spaceraze.servlethelper.game.vip.VipMutator;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
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
            planet.setPopulation(planet.getPopulation() + 1);
            ti.addToLatestExpenseReport(planet.getName() + " has increased its production from " + (planet.getPopulation() - 1) + " to " + planet.getPopulation() + ".");
            ti.addToLatestExpenseReport("Cost to increase production: " + planet.getPopulation() + ".");
        }else
        if (expense.getType().equalsIgnoreCase("res")){
            planet.setResistance(planet.getResistance() + 1);
            ti.addToLatestExpenseReport(planet.getName() + " has increased its resistance from " + (planet.getResistance() - 1) + " to " + planet.getResistance() + ".");
            planet.getPlayerInControl().removeFromTreasury(planet.getResistance());
            ti.addToLatestExpenseReport("Cost to increase resistance: " + planet.getResistance() + ".");
        }else
        if (expense.getType().equalsIgnoreCase("building")){

            BuildingType buildingType = PlayerPureFunctions.findOwnBuildingType(expense.getBuildingTypeName(), g.getPlayer(expense.getPlayerName()));

            String uniqueBuildingString="";
            boolean buildBuilding = true;

            VIP tempVIP = VipPureFunctions.findVIPBuildingBuildBonus(planet,p,o, p.getGalaxy());

            if(buildingType.isWorldUnique()){
                if(!BuildingPureFunctions.isWorldUniqueBuild(p.getGalaxy(), buildingType)){
                    uniqueBuildingString = "Congratulations you have build the world unique " + buildingType.getName() + ".";
                }else{// The building can't be build.
                    buildBuilding =  false;
                    uniqueBuildingString = "You can not build the world unique " + buildingType.getName() + " building. Some other organisation was faster then you.";
                }
            }else
            if(buildingType.isFactionUnique()){
                if(!BuildingPureFunctions.isFactionUniqueBuild(p, galaxy, buildingType)){
                    uniqueBuildingString = "Congratulations you have build the faction unique " + buildingType.getName() + ".";
                }else{// The building can't be build.
                    buildBuilding =  false;
                    uniqueBuildingString = "You can not build the faction unique " + buildingType.getName() + " building. Some other leader was faster then you.";
                }
            }else
            if(buildingType.isPlanetUnique()){
                if(buildingType.isPlanetUnique() && BuildingPureFunctions.hasBuilding(planet, buildingType.getKey())){
                    buildBuilding =  false;
                    uniqueBuildingString = "You can not build an already existing planet unique building: " + buildingType.getName() + ".";
                }
            }else
            if(buildingType.isPlayerUnique()){
                if(!BuildingPureFunctions.isPlayerUniqueBuild(p, galaxy,buildingType)){
                    uniqueBuildingString = "You have build the player unique " + buildingType.getName() + " and you can not build more of this type.";

                }else{// The building can't be build. Should never happend if the orders is checked then the select box is filled.
                    buildBuilding =  false;
                    uniqueBuildingString = "You can not build the player unique " + buildingType.getName() + " building, you have already the building.";
                }
            }

            if(buildBuilding){
                int vipBuildBonus = tempVIP == null ? 0 : VipPureFunctions.getVipTypeByKey(tempVIP.getTypeKey(), p.getGalaxy().getGameWorld()).getBuildingBuildBonus();
                planet.getPlayerInControl().removeFromTreasury(BuildingPureFunctions.getBuildCost(buildingType, vipBuildBonus));

                Building tempBuilding = null;
                tempBuilding = new Building(buildingType, UniqueIdHandler.getUniqueIdCounter(g, CounterType.BUILDING).getUniqueId(), planet);
                // add the building to the planet.
                planet.getBuildings().add(tempBuilding);
                // if the building have any parent building this is a upgrade and the parent building should be removed
                if(buildingType.getParentBuildingName() != null){
                    ti.addToLatestExpenseReport("You have upgraded a " + buildingType.getParentBuildingName() + " to a " + buildingType.getName() + " at the planet " + planet.getName() + ".");
                    ti.addToLatestExpenseReport("Cost to upgrade " + buildingType.getName() + ": " + BuildingPureFunctions.getBuildCost(buildingType, vipBuildBonus) + ".");
                    //planet.removeBuilding(tempBuilding.getBuildingType().getName());
                    PlanetMutator.removeBuilding(planet, expense.getBuildingKey());
                }else{
                    ti.addToLatestExpenseReport("You have built a new " + buildingType.getName() + ") at the planet " + planet.getName() + ".");
                    ti.addToLatestExpenseReport("Cost to build new " + buildingType.getName() + ": " + BuildingPureFunctions.getBuildCost(buildingType, vipBuildBonus) + ".");
                }
            }//else{// the building is unique and cant be build.
            if(!uniqueBuildingString.equalsIgnoreCase("")){
                ti.addToLatestExpenseReport(uniqueBuildingString);
            }
        }else
        if (expense.getType().equalsIgnoreCase("buildship")){

            String uniqueBuildingString="";
            boolean buildShip = true;

            SpaceshipType sst = PlayerPureFunctions.findOwnSpaceshipType(expense.getSpaceshipTypeName(), p, galaxy);

            if(sst.isWorldUnique()){
                if(!SpaceshipPureFunctions.isWorldUniqueBuild(p.getGalaxy(), sst)){
                    uniqueBuildingString = "Congratulations you have build the world unique " + sst.getName() + ".";
                }else{// The building can't be build.
                    buildShip =  false;
                    uniqueBuildingString = "You can not build the world unique " + sst.getName() + " ship. Some other organisation was faster then you.";
                }
            }else
            if(sst.isFactionUnique()){
                if(!SpaceshipPureFunctions.isFactionUniqueBuild(p, sst, galaxy)){
                    uniqueBuildingString = "Congratulations you have build the faction unique " + sst.getName() + ".";
                }else{// The building can't be build.
                    buildShip =  false;
                    uniqueBuildingString = "You can not build the faction unique " + sst.getName() + " ship. Some other leader was faster then you.";
                }
            }else
            if(sst.isPlayerUnique()){
                if(!SpaceshipPureFunctions.isPlayerUniqueBuild(p, sst, galaxy)){
                    uniqueBuildingString = "You have build the player unique " + sst.getName() + " and you can not build more of this type.";

                }else{// The building can't be build. Should never happend if the orders is checked then the select box is filled.
                    buildShip =  false;
                    uniqueBuildingString = "You can not build the player unique " + sst.getName() + " ship, you have already the ship.";
                }
            }

            if(buildShip){
                Spaceship sstemp = null;
                VIP tempVIP = VipPureFunctions.findVIPShipBuildBonus(planet,p,o, p.getGalaxy());
                int vipTechBonus = tempVIP == null ? 0 : VipPureFunctions.getVipTypeByKey(tempVIP.getTypeKey(), p.getGalaxy().getGameWorld()).getTechBonus();
                int factionTechBonus = p.getTechBonus();

                sstemp = SpaceshipMutator.createSpaceShip(p, sst, vipTechBonus, factionTechBonus, PlanetPureFunctions.getBuildingTechBonus(planet, g.getGameWorld()));
                //sstemp = sst.getShip(tempVIP2,factionTechBonus,planet.getBuildingTechBonus());
                //sstemp = ow.buildShip(sst,tempVIP2,factionTechBonus);
                Logger.finest(" -buildship planet: " + SpaceshipPureFunctions.getSpaceshipTypeByKey(sstemp.getTypeKey(), galaxy.getGameWorld()).getName());
                sstemp.setOwner(planet.getPlayerInControl());
                sstemp.setLocation(planet);
                g.addSpaceship(sstemp);
                ti.addToLatestExpenseReport("You have built a new " + sst.getName() + " (named " + sstemp.getName() + ") at " + planet.getName() + ".");
                // TODO (Tobbe) lägg bonusen för buildings.  Skall bonus addas eller skall den som är störst gälla.
                int vipBuildBonus = tempVIP == null ? 0 : VipPureFunctions.getVipTypeByKey(tempVIP.getTypeKey(), p.getGalaxy().getGameWorld()).getShipBuildBonus();
                planet.getPlayerInControl().removeFromTreasury(SpaceshipPureFunctions.getBuildCost(sst, vipBuildBonus));
                Logger.finest(" -buildship loc name: " + planet.getName());
                ti.addToLatestExpenseReport("Cost to build new " + sst.getName() + ": " + SpaceshipPureFunctions.getBuildCost(sst, vipBuildBonus) + ".");

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
                if(!TroopPureFunctions.isWorldUniqueBuild(p.getGalaxy(), troopType)){
                    uniqueBuildingString = "Congratulations you have build the world unique " + troopType.getName() + ".";
                }else{// The building can't be build.
                    buildTroop =  false;
                    uniqueBuildingString = "You can not build the world unique " + troopType.getName() + " troop. Some other organisation was faster then you.";
                }
            }else
            if(troopType.isFactionUnique()){
                if(!TroopPureFunctions.isFactionUniqueBuild(p, troopType)){
                    uniqueBuildingString = "Congratulations you have build the faction unique " + troopType.getName() + ".";
                }else{// The building can't be build.
                    buildTroop =  false;
                    uniqueBuildingString = "You can not build the faction unique " + troopType.getName() + " troop. Some other leader was faster then you.";
                }
            }else
            if(troopType.isPlayerUnique()){
                if(!TroopPureFunctions.isPlayerUniqueBuild(p, troopType)){
                    uniqueBuildingString = "You have build the player unique " + troopType.getName() + " and you can not build more of this type.";

                }else{// The building can't be build. Should never happend if the orders is checked then the select box is filled.
                    buildTroop =  false;
                    uniqueBuildingString = "You can not build the player unique " + troopType.getName() + " troop, you have already the troop.";
                }
            }

            if(buildTroop){

                Troop tempTroop = null;

                VIP tempVIP = VipPureFunctions.findVIPTroopBuildBonus(planet,p,o, p.getGalaxy());
                VIP vipWithTechBonus = VipPureFunctions.findVIPTechBonus(planet, p, o, p.getGalaxy());
                int vipTechBonus = vipWithTechBonus != null ? VipPureFunctions.getVipTypeByKey(vipWithTechBonus.getTypeKey(), p.getGalaxy().getGameWorld()).getTechBonus() : 0;
                int factionTechBonus = p.getTechBonus();

                tempTroop = TroopMutator.createTroop(p, troopType, vipTechBonus, factionTechBonus, PlanetPureFunctions.getBuildingTechBonus(planet, g.getGameWorld()), UniqueIdHandler.getUniqueIdCounter(galaxy, CounterType.TROOP).getUniqueId(), galaxy.getGameWorld());
                //sstemp = ow.buildShip(sst,tempVIP2,factionTechBonus);
                Logger.finest(" -buildship planet: " + tempTroop.getName());
                tempTroop.setOwner(planet.getPlayerInControl());
                tempTroop.setPlanetLocation(planet);
                g.addTroop(tempTroop);
                ti.addToLatestExpenseReport("You have built a new " + troopType.getName() + " (named " + tempTroop.getName() + ") at " + planet.getName() + ".");
                // TODO (Tobbe) lägg bonusen för buildings.  Skall bonus addas eller skall den som är störst gälla.
                int vipBuildBonus = tempVIP == null ? 0 : VipPureFunctions.getVipTypeByKey(tempVIP.getTypeKey(), p.getGalaxy().getGameWorld()).getTroopBuildBonus();
                planet.getPlayerInControl().removeFromTreasury(TroopPureFunctions.getCostBuild(troopType, vipBuildBonus));
                Logger.finest(" -buildtroop loc name: " + planet.getName());
                ti.addToLatestExpenseReport("Cost to build new " + troopType.getName() + ": " + TroopPureFunctions.getCostBuild(troopType, vipBuildBonus) + ".");

            }//else{// the ship is unique and cant be build.
            if(!uniqueBuildingString.equalsIgnoreCase("")){
                ti.addToLatestExpenseReport(uniqueBuildingString);
            }


        }else
        if (expense.getType().equalsIgnoreCase("buildVIP")){

            String uniqueVIPString="";
            boolean buildVIP = true;

            VIPType vipType = VipPureFunctions.getVipTypeByKey(expense.getTypeVIPKey(), p.getGalaxy().getGameWorld());

            if(vipType.isWorldUnique()){
                if(!VipPureFunctions.isWorldUniqueBuild(vipType, p.getGalaxy())){
                    uniqueVIPString = "Congratulations you have build the world unique " + expense.getTypeVIPName() + ".";
                }else{// The VIP can't be build.
                    buildVIP =  false;
                    uniqueVIPString = "You can not build the world unique " + expense.getTypeVIPName() + " VIP. Some other organisation was faster then you.";
                }
            }else
            if(vipType.isFactionUnique()){ // används inte så länge alignment finns kvar. eller alla VIPar ligger i GW
                if(!VipPureFunctions.isFactionUniqueBuild(vipType, p, p.getGalaxy())){

                    uniqueVIPString = "Congratulations you have build the faction unique " + vipType.getName() + ".";
                }else{// The VIP can't be build.
                    buildVIP =  false;
                    uniqueVIPString = "You can not build the faction unique " + vipType.getName() + " VIP. Some other leader was faster then you.";
                }
            }else
            if(vipType.isPlayerUnique()){ // används inte så länge alignment finns kvar. eller alla VIPar ligger i GW
                if(!VipPureFunctions.isPlayerUniqueBuild(vipType, p, galaxy)){
                    uniqueVIPString = "You have build the player unique " + vipType.getName() + " and you can not build more of this type.";

                }else{// The VIP can't be build. Should never happend if the orders is checked then the select box is filled.
                    buildVIP =  false;
                    uniqueVIPString = "You can not build the player unique " + vipType.getName() + " VIP, you have already the VIP.";
                }
            }

            if(buildVIP){

                VIP vip = null;

                vip = VipMutator.createNewVIP(vipType, planet.getPlayerInControl(), planet, false);
                Logger.finest(" -buildVIP planet: " + VipPureFunctions.getVipTypeByKey(vip.getTypeKey(), galaxy.getGameWorld()).getName());
                g.getAllVIPs().add(vip);
                ti.addToLatestExpenseReport("You have recruited a new " + vipType.getName() + " at " + planet.getName() + ".");
                planet.getPlayerInControl().removeFromTreasury(vip.getBuildCost());
                ti.addToLatestExpenseReport("Cost to recruited a new " + vipType.getName() + ": " + vip.getBuildCost() + ".");
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
            planet.setResistance(1 + playerToResive.getResistanceBonus());
            planet.setPlayerInControl(playerToResive);
            PlanetPureFunctions.findPlanetInfo(planet.getName(), p.getPlanetInformations()).setRazed(false);
            int cost = playerToResive.getReconstructCostBase();
            p.removeFromTreasury(cost);
            playerToResive.addToGeneral("You have reconstructed the planet " + planet.getName() + " and it is now under your control with a production of 1.");
            playerToResive.addToHighlights(planet.getName(),HighlightType.TYPE_PLANET_RECONSTRUCTED);
        }else
        if(expense.getType().equalsIgnoreCase("research")){
            p.addToTreasury(-expense.getResearchOrder().getCost());
            ti.addToLatestExpenseReport("You have spend " + expense.getResearchOrder().getCost() + " in research on: " + expense.getResearchOrder().getAdvantageName() + ".");

        }else if(expense.getBlackMarketBid() != null){
//      aBid.addPlayer(player);
            BlackMarketPerformer.addBlackMarketBid(expense.getBlackMarketBid(), g);
        }
    }
}
