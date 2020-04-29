package spaceraze.server.game.update;

import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.HighlightType;

import java.util.LinkedList;

public class BlackMarketPerformer {

    private BlackMarketPerformer(){}

    public static void performBlackMarket(BlackMarket blackMarket, Galaxy galaxy){
        //TODO 2020-04-22 kolla att detta fungerar
        blackMarket.getCurrentOffers().removeIf(blackMarketOffer -> BlackMarketPerformer.performSelling(blackMarketOffer, galaxy) || blackMarketOffer.removeTooOldAndSendMessage(galaxy));

        /*
        for (int i = blackMarket.getCurrentOffers().size()-1; i >= 0; i--){
            BlackMarketOffer tempOffer = blackMarket.getCurrentOffers().get(i);
            boolean sold = tempOffer.performSelling(galaxy);
            if (sold){
                currentOffers.removeElementAt(i);
            }else
            if (tempOffer.tooOld(galaxy)){
                tempOffer.createRemovedOldMessage(galaxy);
                currentOffers.removeElementAt(i);
            }
        }*/
    }

    public static boolean performSelling(BlackMarketOffer blackMarketOffer, Galaxy galaxy){
        boolean sold = false;
        if (blackMarketOffer.getBids().size() > 0){
            Logger.finest( "performSelling: bids.size(): " + blackMarketOffer.getBids().size());
            BlackMarketBid winningBid = blackMarketOffer.getHighestBidder(galaxy);
            if (winningBid == null){ // no-one won the offer
                blackMarketOffer.sendDrawMessages(galaxy);
                // send messages who failed to win this bidding
                blackMarketOffer.sendRefundingMessages(winningBid, galaxy);
                blackMarketOffer.resetBids();
            }else{
                sold = true;
                Planet destinationPlanet = galaxy.getPlanet(winningBid.getDestination());
                Logger.finest( "performSelling: winningBid: " + winningBid.getText() + winningBid.getPlayerName());
                Player winningPlayer = galaxy.getPlayer(winningBid.getPlayerName());
                Logger.finest( "performSelling: winningPlayer: " + winningPlayer.getName());
                winningPlayer.addToLatestBlackMarketMessages("You have won the bidding for a " + blackMarketOffer.getString() + " at the cost of " + winningBid.getCost() + ".");
                if (blackMarketOffer.isHotStuff()){
                    Logger.finest( "performSelling: hotStuff");
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToTreasury(blackMarketOffer.getHotStuffAmount());
                    winningPlayer.addToLatestBlackMarketMessages("The Hot Stuff have given you +" + blackMarketOffer.getHotStuffAmount() + " extra income this turn.");
                    winningPlayer.addToHighlights(String.valueOf(blackMarketOffer.getHotStuffAmount()), HighlightType.TYPE_HOT_STUFF_WON);
                }else
                if (blackMarketOffer.getOfferedVIPType() != null){ // is vip
                    Logger.finest( "performSelling: vip: ");
                    VIP newVIP = blackMarketOffer.getOfferedVIPType().createNewVIP(winningPlayer,destinationPlanet, true);
                    galaxy.allVIPs.add(newVIP);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + blackMarketOffer.getOfferedVIPType().getTypeName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(String.valueOf(blackMarketOffer.getOfferedVIPType().getTypeName()),HighlightType.TYPE_VIP_BOUGHT);
                }else
                if (blackMarketOffer.getOfferedShiptype() != null){ // is spaceship
                    Logger.finest("performSelling: ship: ");
                    Spaceship newShip = SpaceshipMutator.createSpaceShip(blackMarketOffer.getOfferedShiptype(), galaxy);
                    newShip.setOwner(winningPlayer);
                    newShip.setLocation(destinationPlanet);
                    galaxy.addSpaceship(newShip);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + newShip.getName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(blackMarketOffer.getOfferedShiptype().getName(),HighlightType.TYPE_SHIP_WON);
                }else
                if (blackMarketOffer.getOfferedShiptypeBlueprint() != null){ // is spaceship blueprints
                    Logger.finest("performSelling: shiptype blueprints: ");
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    //SpaceshipType aSST = winningPlayer.findOwnSpaceshipType(offeredShiptypeBlueprint.getName());
                    PlayerSpaceshipType ownPlayerSpaceshipType = winningPlayer.findOwnPlayerSpaceshipType(blackMarketOffer.getOfferedShiptypeBlueprint().getName());
                    if (ownPlayerSpaceshipType != null){
                        if (ownPlayerSpaceshipType.isAvailableToBuild()){ // check if the player already have the shiptype
                            winningPlayer.addToLatestBlackMarketMessages("You already could build ships of the type " + blackMarketOffer.getOfferedShiptypeBlueprint().getName() + ".");
                        }else{
                            ownPlayerSpaceshipType.setAvailableToBuild(true);
                            winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + blackMarketOffer.getOfferedShiptypeBlueprint().getName() + ".");
                        }
                        winningPlayer.addToHighlights(blackMarketOffer.getOfferedShiptypeBlueprint().getName(),HighlightType.TYPE_SHIPTYPE_WON);
                    }else{
                        winningPlayer.addPlayerSpaceshipType(new PlayerSpaceshipType(blackMarketOffer.getOfferedShiptypeBlueprint().getName(), true));
                        winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + blackMarketOffer.getOfferedShiptypeBlueprint().getName() + ".");
                        winningPlayer.addToHighlights(blackMarketOffer.getOfferedShiptypeBlueprint().getName(),HighlightType.TYPE_SHIPTYPE_WON);
                    }
                }else{ // is troop
                    Logger.finest( "performSelling: troop: " + blackMarketOffer.getOfferedTroopType().getUniqueName());
                    Troop newTroop = winningPlayer.getGalaxy().findTroopType(blackMarketOffer.getOfferedTroopType().getUniqueName()).getTroop(null,0,0);
                    newTroop.setOwner(winningPlayer);
                    newTroop.setPlanetLocation(destinationPlanet);
                    galaxy.addTroop(newTroop);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + newTroop.getUniqueName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(blackMarketOffer.getOfferedTroopType().getUniqueName(),HighlightType.TYPE_TROOP_WON);
                }
                // send messages to everyone except the winner
                galaxy.addBlackMarketMessages(winningPlayer,blackMarketOffer.getString() + " sold to Govenor " + winningPlayer.getGovenorName() + " for cost: " + winningBid.getCost() + ".");
                // send messages who failed to win this bidding
                blackMarketOffer.sendRefundingMessages(winningBid, galaxy);
            }
        }else{
            galaxy.addBlackMarketMessages(null,blackMarketOffer.getString() + " not sold - not bids yet.");
        }
        return sold;
    }
}
