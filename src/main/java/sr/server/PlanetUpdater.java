package sr.server;

import spaceraze.servlethelper.game.BuildingPureFunctions;
import spaceraze.servlethelper.game.vip.VipMutator;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
import spaceraze.servlethelper.game.planet.PlanetMutator;
import spaceraze.servlethelper.game.planet.PlanetOrderStatusPureFunctions;
import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.battlehandler.spacebattle.TaskForce;

import java.util.ArrayList;
import java.util.List;

//TODO 2020-11-12 snygga till och gör om det senare till en @Service
public class PlanetUpdater {

    public void conqueredByTroops(Planet planet, Player conqueringPlayer, GameWorld gameWorld){
        Logger.finer("conqueredByTroops called");
        planet.setResistance(1 + conqueringPlayer.getResistanceBonus()); // olika typer av spelare för olika res på nyerövrade planeter
        Player playerInControl = planet.getPlayerInControl();
        if (playerInControl != null){
            PlanetPureFunctions.findPlanetInfo(planet.getName(), playerInControl.getPlanetInformations()).setLastKnownOwner(conqueringPlayer.getName());
            PlanetMutator.setLastKnownProductionAndResistance(planet.getName(), planet.getPopulation(), planet.getResistance(), playerInControl.getPlanetInformations());
            playerInControl.addToGeneral("The planet " + planet.getName() + " have no troops and can make no resistance to Governor " + conqueringPlayer.getGovernorName() + " troops.");
            playerInControl.addToGeneral("The planet " + planet.getName() + " has surrendered to Governor " + conqueringPlayer.getGovernorName() + ".");
            playerInControl.addToHighlights(planet.getName(), HighlightType.TYPE_PLANET_LOST);
            conqueringPlayer.addToGeneral("The planet " + planet.getName() + " have no troops and can make no resistance to your troops.");
            conqueringPlayer.addToGeneral("The planet " + planet.getName() + ", formerly belonging to Governor " + playerInControl.getGovernorName() + ", has surrendered to you.");
            conqueringPlayer.addToHighlights(planet.getName(),HighlightType.TYPE_PLANET_CONQUERED);

            planet.setHasNeverSurrendered(false); // should not be needed?

            PlanetMutator.destroyBuildingsThatCanNotBeOverTaken(planet, conqueringPlayer, gameWorld);

        }else{
            conqueringPlayer.addToGeneral("The neutral planet " + planet.getName() + " have no troops and can make no resistance to your troops.");
            conqueringPlayer.addToGeneral("The neutral planet " + planet.getName() + " has surrendered to your troops.");
            conqueringPlayer.addToHighlights(planet.getName(),HighlightType.TYPE_PLANET_CONQUERED);
            if (planet.isHasNeverSurrendered()){
                planet.setHasNeverSurrendered(false);
                // lägg till en slumpvis VIP till denna spelare
                VIP aVIP = VipMutator.maybeAddVIP(conqueringPlayer, conqueringPlayer.getGalaxy());
                if (aVIP != null){
                    VIPType vipType = VipPureFunctions.getVipTypeByUuid(aVIP.getTypeUuid(), conqueringPlayer.getGalaxy().getGameWorld());
                    VipMutator.setShipLocation(aVIP, planet);
                    conqueringPlayer.addToVIPReport("When you conquered " + planet.getName() + " you have found a " + vipType.getName() + " who has joined your service.");
                    conqueringPlayer.addToHighlights(vipType.getName(),HighlightType.TYPE_VIP_JOINS);
                }
            }
        }

        checkVIPsOnConqueredPlanet(planet, conqueringPlayer, conqueringPlayer.getGalaxy());

        Logger.finer("Planet, adding space to general");
        if (playerInControl != null){ // det fanns en spelare som kontrollerade planeten (dvs ej neutral)
            playerInControl.addToGeneral("");
        }
        conqueringPlayer.addToGeneral("");
        // change owner of planet
        planet.setPlayerInControl(conqueringPlayer);
    }

    public void planetSurrenders(Planet planet, TaskForce attackingTaskForce, Galaxy galaxy){
        Player attackingPlayer = galaxy.getPlayerByGovenorName(attackingTaskForce.getPlayerName());
        Player playerInControl = planet.getPlayerInControl();
        planet.setResistance(1 + attackingPlayer.getResistanceBonus()); // olika typer av spelare för olika res på erövrade planeter
        if (playerInControl != null){
            PlanetMutator.setLastKnownOwner(planet.getName(), attackingPlayer.getName(),galaxy.turn + 1, playerInControl.getPlanetInformations());
            PlanetMutator.setLastKnownProductionAndResistance(planet.getName(), planet.getPopulation(), planet.getResistance(), playerInControl.getPlanetInformations());
            playerInControl.addToGeneral("The planet " + planet.getName() + " has surrendered to Governor " + attackingPlayer.getGovernorName() + ".");
            playerInControl.addToHighlights(planet.getName(),HighlightType.TYPE_PLANET_LOST);
            attackingPlayer.addToGeneral("The planet " + planet.getName() + ", formerly belonging to Governor " + playerInControl.getGovernorName() + ", has surrendered to you.");
            attackingPlayer.addToHighlights(planet.getName(),HighlightType.TYPE_PLANET_CONQUERED);
            checkVIPsOnConqueredPlanet(planet, attackingPlayer, attackingPlayer.getGalaxy());
            planet.setHasNeverSurrendered(false);

            PlanetMutator.destroyBuildingsThatCanNotBeOverTaken(planet, attackingPlayer, galaxy.getGameWorld());
    	/*	if (hasSpaceStation()){
    			attackingTaskForce.getPlayer().addToGeneral("The space station orbiting the planet " + name + " has been destroyed.");
    			playerInControl.addToGeneral("Your space station orbiting the planet " + name + " has been destroyed.");
    			spaceStation = null;
    		}*/
        }else{
            attackingPlayer.addToGeneral("The neutral planet " + planet.getName() + " has surrendered to your forces.");
            attackingPlayer.addToHighlights(planet.getName(),HighlightType.TYPE_PLANET_CONQUERED);
            checkVIPsOnConqueredPlanet(planet, attackingPlayer, attackingPlayer.getGalaxy());


            if (planet.isHasNeverSurrendered()){
                planet.setHasNeverSurrendered(false);
                // lägg till en slumpvis VIP till denna spelare
                VIP aVIP = VipMutator.maybeAddVIP(attackingPlayer, attackingPlayer.getGalaxy());
                if (aVIP != null){
                    VIPType vipType = VipPureFunctions.getVipTypeByUuid(aVIP.getTypeUuid(), galaxy.getGameWorld());
                    VipMutator.setShipLocation(aVIP, planet);
                    attackingPlayer.addToVIPReport("When you conquered " + planet.getName() + " you have found a " + vipType.getName() + " who has joined your service.");
                    attackingPlayer.addToHighlights(vipType.getName(),HighlightType.TYPE_VIP_JOINS);
                }
            }
        }

        planet.setPlayerInControl(attackingPlayer);
    }

    public void razed(Planet planet, Player aPlayer){
        Logger.finer("Planet.razed() called");
        Player playerInControl = planet.getPlayerInControl();
        if (playerInControl != null){
            // razeade planeter skall visas som neutrala... ingen spelare has ju kontrollen...

            PlanetMutator.setLastKnownOwner(planet.getName(), "Neutral",playerInControl.getGalaxy().turn + 1, playerInControl.getPlanetInformations());
            // razed planets does not have any production or resistance
            PlanetMutator.setLastKnownProductionAndResistance(planet.getName(), -1, -1, playerInControl.getPlanetInformations());
        }
        if (playerInControl != null){
            playerInControl.addToGeneral("The planet " + planet.getName() + " has been RAZED by Governor " + aPlayer.getGovernorName() + " forces.");
            playerInControl.addToHighlights(planet.getName(),HighlightType.TYPE_OWN_PLANET_RAZED);
            aPlayer.addToGeneral("The planet " + planet.getName() + ", formerly belonging to " + playerInControl.getGovernorName() + ", has been RAZED by your forces.");
            aPlayer.addToHighlights(planet.getName(),HighlightType.TYPE_ENEMY_PLANET_RAZED);
        }else{
            aPlayer.addToGeneral("The neutral planet " + planet.getName() + " has been RAZED by your forces.");
        }
        if (playerInControl != null){
            checkVIPsOnRazedPlanet(planet,playerInControl, playerInControl.getGalaxy());
        }else{ // check for govs on neutral planets
            checkGovernorsOnRazedPlanet(planet, aPlayer.getGalaxy());
        }
        // razed planets does not have any production or resistance
        planet.setPopulation(0);
        planet.setResistance(0);
        //attackingTF.getPlayer().getGalaxy().checkDestroyWharfs(this,attackingTF,true);
        checkDestroyBuildings(planet,aPlayer,true);
        if (playerInControl != null){ // det fanns en spelare som kontrollerade planeten (dvs ej neutral)
            playerInControl.addToGeneral("");
        }
        planet.setPlayerInControl(null);
    }

    public void checkDestroyBuildings(Planet aPlanet, Player aPlayer, boolean autoDestroy) {
        if (autoDestroy || PlanetOrderStatusPureFunctions.isDestroyOrbitalBuildings(aPlanet.getName(), aPlayer.getPlanetOrderStatuses())) {
            List<Building> removeBuildings = new ArrayList<Building>();
            List<Building> buildings = aPlanet.getBuildings();
            for (Building building : buildings) {
                if (BuildingPureFunctions.getBuildingTypeByUuid(building.getTypeUuid(), aPlayer.getGalaxy().getGameWorld()).isInOrbit()) {
                    removeBuildings.add(building);
                }
            }
            for (Building building : removeBuildings) {
                // skriva meddelanden...
                aPlayer.getTurnInfo().addToLatestGeneralReport("You have destroyed a "
                        + BuildingPureFunctions.getBuildingTypeByUuid(building.getTypeUuid(), aPlayer.getGalaxy().getGameWorld()).getName() + " on " + aPlanet.getName() + ".");
                if (aPlanet.getPlayerInControl() != null) {
                    aPlanet.getPlayerInControl().getTurnInfo()
                            .addToLatestGeneralReport("Your " + BuildingPureFunctions.getBuildingTypeByUuid(building.getTypeUuid(), aPlayer.getGalaxy().getGameWorld()).getName() + " on the "
                                    + aPlanet.getName() + " has been destroyed.");
                }
                PlanetMutator.removeBuilding(aPlanet, building.getUuid());
            }
        }
    }

    public void checkVIPsOnRazedPlanet(Planet aPlanet, Player aPlayer, Galaxy galaxy) {
        List<VIP> allVIPsOnPlanet = VipPureFunctions.findAllVIPsOnPlanet(aPlanet, galaxy);
        for (int i = 0; i < allVIPsOnPlanet.size(); i++) {
            VIP tempVIP = allVIPsOnPlanet.get(i);
            if (tempVIP.getBoss() == aPlayer) {
                VIPType vipType = VipPureFunctions.getVipTypeByUuid(tempVIP.getTypeUuid(), galaxy.getGameWorld());
                if (!vipType.isCanVisitEnemyPlanets()) {
                    galaxy.getAllVIPs().remove(tempVIP);
                    aPlayer.addToVIPReport("Your " + vipType.getName() + " has been killed when the planet "
                            + aPlanet.getName() + " was RAZED.");
                    aPlayer.addToHighlights(vipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                }
            }
        }
    }

    public void checkGovernorsOnRazedPlanet(Planet aPlanet, Galaxy galaxy) {
        List<VIP> allVIPsOnPlanet = VipPureFunctions.findAllVIPsOnPlanet(aPlanet, galaxy);
        for (int i = 0; i < allVIPsOnPlanet.size(); i++) {
            VIP tempVIP = allVIPsOnPlanet.get(i);
            VIPType vipType = VipPureFunctions.getVipTypeByUuid(tempVIP.getTypeUuid(), galaxy.getGameWorld());
            if (vipType.isGovernor()) {
                galaxy.getAllVIPs().remove(tempVIP);
                tempVIP.getBoss().addToVIPReport("Your " + vipType.getName() + " has been killed when the planet "
                        + aPlanet.getName() + " was RAZED.");
                // aPlayer.addToHighlights(tempVIP.getName(),Highlight.TYPE_OWN_VIP_KILLED);
            }
        }
    }

    public static void checkVIPsOnConqueredPlanet(Planet aPlanet, Player aPlayer, Galaxy galaxy) {
        List<VIP> allVIPsOnPlanet = VipPureFunctions.findAllVIPsOnPlanet(aPlanet, galaxy);
        for (int i = 0; i < allVIPsOnPlanet.size(); i++) {
            VIP tempVIP = allVIPsOnPlanet.get(i);
            if (tempVIP.getBoss() != aPlayer) {
                VIPType vipType = VipPureFunctions.getVipTypeByUuid(tempVIP.getTypeUuid(), galaxy.getGameWorld());
                if (!vipType.isCanVisitEnemyPlanets()) {
                    galaxy.getAllVIPs().remove(tempVIP);
                    tempVIP.getBoss().addToVIPReport("Your " + vipType.getName() + " have been killed when the planet "
                            + aPlanet.getName() + " was conquered.");
                    tempVIP.getBoss().addToHighlights(vipType.getName(), HighlightType.TYPE_OWN_VIP_KILLED);
                    aPlayer.addToVIPReport("An enemy " + vipType.getName()
                            + " have been killed when you conquered the planet " + aPlanet.getName() + ".");
                    aPlayer.addToHighlights(vipType.getName(), HighlightType.TYPE_ENEMY_VIP_KILLED);
                }
            }
        }
    }
}
