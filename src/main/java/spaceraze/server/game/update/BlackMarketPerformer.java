package spaceraze.server.game.update;

import spaceraze.game.*;
import spaceraze.servlethelper.game.AlignmentPureFunctions;
import spaceraze.servlethelper.game.BlackMarketPureFunctions;
import spaceraze.servlethelper.game.blackMarket.BlackMarketMutator;
import spaceraze.servlethelper.game.orders.OrderPureFunctions;
import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.servlethelper.game.troop.TroopPureFunctions;
import spaceraze.servlethelper.game.vip.VipMutator;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.BlackMarketFrequency;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.enums.SpaceShipSize;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class BlackMarketPerformer {

    private static final int shipIndex = 3;
    private static final int hotStuffIndex = 5;
    private static final int vipIndex = 8;
    private static final int bluePrintShipIndex = 9;
    private static final int troopIndex = 10;
    private static final int duelGame = 2;
    private static final int smallGame = 5;

    private BlackMarketPerformer(){}

    public static void newTurn(Galaxy g, GameWorld gameWorld){
        int nrOffers;
        int nrPlayers = PlayerPureFunctions.getActivePlayers(g).size();
        if (nrPlayers == duelGame){
            int tmpRdm = Functions.getRandomInt(1,4);
            if (tmpRdm == 4){ // 25% of 2 offers
                nrOffers = 2;
            }else{ // otherwise 1 offer
                nrOffers = 1;
            }
        }else if (smallGame > 5){ // in big games, can be up to 3 offers
            nrOffers = Functions.getRandomInt(1,3);
        }else{ // otherwise 1-2 offers
            nrOffers = Functions.getRandomInt(1,2);
        }
        for (int i = 0; i < nrOffers; i++){
            BlackMarketOffer tempOffer = BlackMarketPerformer.createBlackMarketOffer(g, gameWorld, UUID.randomUUID().toString());
            g.getCurrentOffers().add(tempOffer);
            BlackMarketMutator.addBlackMarketMessages(g,null,"New item for sale: a " + BlackMarketPureFunctions.getDescription(tempOffer, gameWorld) + " is for sale at the Black Market.");
        }
    }

    public static BlackMarketOffer createBlackMarketOffer(Galaxy galaxy, GameWorld gameWorld, String uuid) {
        BlackMarketOffer blackMarketOffer = new BlackMarketOffer(uuid);
        int randomType = galaxy.hasTroops(gameWorld) ? Functions.getRandomInt(1,12) : Functions.getRandomInt(1,9);
        blackMarketOffer.setLastTurnAction(galaxy.getTurn());
        if (randomType <= shipIndex){ // ship
            blackMarketOffer.setSpaceshipTypeUuid(getRandomCommonSpaceshipType(galaxy, gameWorld).getUuid());
        }else
        if (randomType <= hotStuffIndex){ // hot stuff
            blackMarketOffer.setHotStuffAmount(createHotStuffBid());
        }else
        if (randomType <= vipIndex){ // VIP
            //TODO 2020-05-02 rewrite this.
            boolean canBeUsed = false;
            int tries = 0;
            while (!canBeUsed & (tries < 100)){
                blackMarketOffer.setVipTypeUuid(VipPureFunctions.getRandomVIPType(galaxy, gameWorld).getUuid());
                tries++;
                canBeUsed = vipCanBeUsed(VipPureFunctions.getVipTypeByUuid(blackMarketOffer.getVipTypeUuid(), gameWorld), galaxy, gameWorld);
            }
            if(!canBeUsed){ // if no vip was found use a hot stuff instead
                blackMarketOffer.setVipTypeUuid(null);
                blackMarketOffer.setHotStuffAmount(createHotStuffBid());
            }
        }else
        if (randomType == bluePrintShipIndex){ // shiptype blueprint
            blackMarketOffer.setBlueprint(BlackMarketPerformer.getRandomShipBlueprint(galaxy, gameWorld).getUuid());
            if (blackMarketOffer.getBlueprint() == null){ // if no shiptype was found use a hot stuff instead
                blackMarketOffer.setHotStuffAmount(createHotStuffBid());
            }
        }else{ //TroopIndex 10-12
            blackMarketOffer.setTroopTypeUuid(getRandomCommonTroopType(galaxy, gameWorld).getUuid());
        }

        return blackMarketOffer;
    }

    public static boolean vipCanBeUsed(VIPType aVIPType, Galaxy galaxy, GameWorld gameWorld) {
        boolean found = false;
        int index = 0;
        List<Player> activePlayers = PlayerPureFunctions.getActivePlayers(galaxy);
        while (!found & (index < activePlayers.size())) {
            Player aPlayer = activePlayers.get(index);
            if (AlignmentPureFunctions.canHaveVip(aVIPType.getAlignment(), AlignmentPureFunctions.getPlayerAlignment(aPlayer, gameWorld))) {
                found = true;
            } else {
                index++;
            }
        }
        return found;
    }

    private static TroopType getRandomCommonTroopType(Galaxy galaxy, GameWorld gameWorld) {
        TroopType aTroopType = null;
        TroopType tempTroopType = null;
        List<TroopType> allAvailableTroopTypes = getTroopTypesToBlackMarket(galaxy, gameWorld);
        int totalFrequencypoint = 0;
        for (TroopType troopType : allAvailableTroopTypes) {
            totalFrequencypoint += troopType.getBlackMarketFrequency().getFrequency();
        }

        int freqValue = Functions.getRandomInt(0, totalFrequencypoint - 1);
        int counter = 0;
        int tmpFreqSum = 0;
        while (aTroopType == null) {
            tempTroopType = allAvailableTroopTypes.get(counter);
            tmpFreqSum = tmpFreqSum + tempTroopType.getBlackMarketFrequency().getFrequency();
            if (tmpFreqSum > freqValue) {
                aTroopType = tempTroopType;
            }
            counter++;
        }

        return aTroopType;
    }

    /**
     * Searches through all trooptype lists for all factions
     *
     * @return list containing trooptypes. If a trooptype can be build by several
     *         factions it will appear several times in the list
     */
    private static List<TroopType> getTroopTypesToBlackMarket(Galaxy galaxy, GameWorld gameWorld) {
        Logger.fine("getTroopTypesToBlackMarket() called");
        List<TroopType> troopTypes = new LinkedList<>();
        for (Faction aFaction : gameWorld.getFactions()) {
            Logger.finer("Faction: " + aFaction.getName());
            List<TroopType> factionTroopTypes = aFaction.getTroopTypes().stream().map(uuid -> TroopPureFunctions.getTroopTypeByUuid(uuid, gameWorld)).collect(Collectors.toList());
            Logger.finer("TroopTypes #: " + factionTroopTypes.size());
            for (TroopType aTroopType : factionTroopTypes) {
                Logger.finer("TT: " + aTroopType.getName());

                if (isReadyToUseInBlackMarket(galaxy, aTroopType, gameWorld)) {
                    if (!troopTypes.contains(aTroopType)) {
                        troopTypes.add(aTroopType);
                    }
                }
            }
        }
        return troopTypes;
    }

    public static boolean isReadyToUseInBlackMarket(Galaxy aGalaxy, TroopType troopType, GameWorld gameWorld){
        boolean isConstructable =  false;
        if(aGalaxy.getTurn() >= troopType.getBlackmarketFirstTurn()){
            if (troopType.isSpaceshipTravel()){
                if (troopType.isCanAppearOnBlackMarket()){
                    if(!troopType.isPlayerUnique() && !troopType.isFactionUnique()){
                        if(troopType.isWorldUnique() && !TroopPureFunctions.isWorldUniqueBuild(aGalaxy, troopType, gameWorld)){
                            boolean isAlreadyAoffer = false;
                            for (BlackMarketOffer aBlackMarketOffer : aGalaxy.getCurrentOffers()) {
                                if(aBlackMarketOffer.isTroop() && aBlackMarketOffer.getTroopTypeUuid().equals(troopType.getUuid())){
                                    isAlreadyAoffer = true;
                                }
                            }

                            if(!isAlreadyAoffer){
                                boolean haveBuildingOrder = false;
                                for (Player tempPlayer : aGalaxy.getPlayers()) {
                                    if(OrderPureFunctions.haveTroopTypeBuildOrder(tempPlayer.getOrders(), troopType)){
                                        haveBuildingOrder = true;
                                    }
                                }
                                if(!haveBuildingOrder){
                                    isConstructable =  true;
                                }
                            }
                        }else{
                            isConstructable = true;
                        }
                    }
                }
            }
        }
        return isConstructable;
    }

    private static int createHotStuffBid(){
        return Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6);
    }

    public static void performBlackMarket(Galaxy galaxy, GameWorld gameWorld){
        galaxy.getCurrentOffers().removeIf(blackMarketOffer -> BlackMarketPerformer.performSelling(blackMarketOffer, galaxy, gameWorld) || removeTooOldAndSendMessage(galaxy, gameWorld, blackMarketOffer));
    }

    public static boolean removeTooOldAndSendMessage(Galaxy galaxy, GameWorld gameWorld, BlackMarketOffer blackMarketOffer){
        boolean tooOld = blackMarketOfferTooOld(galaxy, blackMarketOffer);
        if(tooOld){
            BlackMarketMutator.createRemovedOldMessage(galaxy, gameWorld, blackMarketOffer);
        }
        return tooOld;
    }

    private static boolean blackMarketOfferTooOld(Galaxy galaxy, BlackMarketOffer blackMarketOffer){
        boolean old = false;
        if ((blackMarketOffer.getLastTurnAction() + 2) < galaxy.getTurn()){
            old = true;
        }
        return old;
    }

    public static boolean performSelling(BlackMarketOffer blackMarketOffer, Galaxy galaxy, GameWorld gameWorld){
        boolean sold = false;
        if (blackMarketOffer.getBlackMarketBids().size() > 0){
            Logger.finest( "performSelling: bids.size(): " + blackMarketOffer.getBlackMarketBids().size());
            BlackMarketBid winningBid = blackMarketOffer.getHighestBidder(galaxy);
            if (winningBid == null){ // no-one won the offer
                BlackMarketMutator.sendDrawMessages(galaxy, blackMarketOffer, gameWorld);
                // send messages who failed to win this bidding
                sendRefundingMessages(blackMarketOffer, winningBid.getUuid(), galaxy);
                blackMarketOffer.resetBids();
            }else{
                sold = true;
                Planet destinationPlanet = galaxy.getPlanet(winningBid.getDestination());
                Player winningPlayer = PlayerPureFunctions.getPlayer(galaxy, winningBid.getPlayerUuid());
                Logger.finest( "performSelling: winningBid: " + BlackMarketPureFunctions.getBiddingText(blackMarketOffer, winningBid, gameWorld) + winningBid.getPlayerUuid());
                Logger.finest( "performSelling: winningPlayer: " + winningPlayer.getName());
                winningPlayer.addToLatestBlackMarketMessages("You have won the bidding for a " + BlackMarketPureFunctions.getDescription(blackMarketOffer, gameWorld) + " at the cost of " + winningBid.getCost() + ".");
                if (blackMarketOffer.isHotStuff()){
                    Logger.finest( "performSelling: hotStuff");
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToTreasury(blackMarketOffer.getHotStuffAmount());
                    winningPlayer.addToLatestBlackMarketMessages("The Hot Stuff have given you +" + blackMarketOffer.getHotStuffAmount() + " extra income this turn.");
                    winningPlayer.addToHighlights(String.valueOf(blackMarketOffer.getHotStuffAmount()), HighlightType.TYPE_HOT_STUFF_WON);
                }else
                if (blackMarketOffer.getVipTypeUuid() != null){ // is vip
                    performSellingVip(blackMarketOffer, galaxy, gameWorld, winningPlayer, destinationPlanet, winningBid);
                }else
                if (blackMarketOffer.getSpaceshipTypeUuid() != null){ // is spaceship
                    performSellingSpaceship(blackMarketOffer, galaxy, gameWorld, winningPlayer, destinationPlanet, winningBid);
                }else
                if (blackMarketOffer.getBlueprint() != null){ // is spaceship blueprints
                    performSellingBluePrint(blackMarketOffer, gameWorld, winningPlayer, winningBid);
                }else{ // is troop
                    Logger.finest( "performSelling: troop: " + blackMarketOffer.getTroopTypeUuid());
                    //Create troop without player bonus/research
                    Troop newTroop = TroopMutator.createTroop(TroopPureFunctions.getTroopTypeByUuid(blackMarketOffer.getTroopTypeUuid(), gameWorld), galaxy, gameWorld);
                    newTroop.setOwner(winningPlayer);
                    newTroop.setPlanetLocation(destinationPlanet);
                    galaxy.addTroop(newTroop);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + newTroop.getName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(TroopPureFunctions.getTroopTypeByUuid(blackMarketOffer.getTroopTypeUuid(), gameWorld).getName(), HighlightType.TYPE_TROOP_WON);
                }
                // send messages to everyone except the winner
                BlackMarketMutator.addBlackMarketMessages(galaxy, winningPlayer.getUuid(),BlackMarketPureFunctions.getDescription(blackMarketOffer, gameWorld) + " sold to Govenor " + winningPlayer.getGovernorName() + " for cost: " + winningBid.getCost() + ".");
                // send messages who failed to win this bidding
                sendRefundingMessages(blackMarketOffer, winningBid.getUuid(), galaxy);
            }
        }else{
            BlackMarketMutator.addBlackMarketMessages(galaxy, null,BlackMarketPureFunctions.getDescription(blackMarketOffer, gameWorld) + " not sold - not bids yet.");
        }
        return sold;
    }

    private static void performSellingVip(BlackMarketOffer blackMarketOffer, Galaxy galaxy, GameWorld gameWorld, Player winningPlayer, Planet destinationPlanet, BlackMarketBid winningBid) {
        Logger.finest( "performSelling: vip: ");
        VIP newVIP = VipMutator.createNewVIP(VipPureFunctions.getVipTypeByUuid(blackMarketOffer.getVipTypeUuid(), gameWorld), winningPlayer, destinationPlanet, true);
        galaxy.allVIPs.add(newVIP);
        winningPlayer.removeFromTreasury(winningBid.getCost());
        winningPlayer.addToLatestBlackMarketMessages("Your new " + VipPureFunctions.getVipTypeByUuid(blackMarketOffer.getVipTypeUuid(), gameWorld).getName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
        winningPlayer.addToHighlights(String.valueOf(VipPureFunctions.getVipTypeByUuid(blackMarketOffer.getVipTypeUuid(), gameWorld).getName()),HighlightType.TYPE_VIP_BOUGHT);
    }

    private static void performSellingSpaceship(BlackMarketOffer blackMarketOffer, Galaxy galaxy, GameWorld gameWorld, Player winningPlayer, Planet destinationPlanet, BlackMarketBid winningBid) {
        Logger.finest("performSelling: ship: ");
        Spaceship newShip = SpaceshipMutator.createSpaceShip(SpaceshipPureFunctions.getSpaceshipTypeByUuid(blackMarketOffer.getSpaceshipTypeUuid(), gameWorld));
        newShip.setOwner(winningPlayer);
        newShip.setLocation(destinationPlanet);
        galaxy.addSpaceship(newShip);
        winningPlayer.removeFromTreasury(winningBid.getCost());
        winningPlayer.addToLatestBlackMarketMessages("Your new " + newShip.getName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
        winningPlayer.addToHighlights(SpaceshipPureFunctions.getSpaceshipTypeByUuid(blackMarketOffer.getSpaceshipTypeUuid(), gameWorld).getName(), HighlightType.TYPE_SHIP_WON);
    }

    private static void performSellingBluePrint(BlackMarketOffer blackMarketOffer, GameWorld gameWorld, Player winningPlayer, BlackMarketBid winningBid) {
        Logger.finest("performSelling: shiptype blueprints: ");
        winningPlayer.removeFromTreasury(winningBid.getCost());
        //SpaceshipType aSST = winningPlayer.findOwnSpaceshipType(offeredShiptypeBlueprint.getName());
        PlayerSpaceshipImprovement ownPlayerSpaceshipImprovement = PlayerPureFunctions.findSpaceshipImprovement(blackMarketOffer.getBlueprint(), winningPlayer);
        if (ownPlayerSpaceshipImprovement != null){
            if (ownPlayerSpaceshipImprovement.isAvailableToBuild()){ // check if the player already have the shiptype
                winningPlayer.addToLatestBlackMarketMessages("You already could build ships of the type " + SpaceshipPureFunctions.getSpaceshipTypeByUuid(blackMarketOffer.getBlueprint(), gameWorld).getName() + ".");
            }else{
                ownPlayerSpaceshipImprovement.setAvailableToBuild(true);
                winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + SpaceshipPureFunctions.getSpaceshipTypeByUuid(blackMarketOffer.getBlueprint(), gameWorld).getName() + ".");
            }
            winningPlayer.addToHighlights(SpaceshipPureFunctions.getSpaceshipTypeByUuid(blackMarketOffer.getBlueprint(), gameWorld).getName(), HighlightType.TYPE_SHIPTYPE_WON);
        }else{
            winningPlayer.getSpaceshipImprovements().add(new PlayerSpaceshipImprovement(blackMarketOffer.getBlueprint(), true));
            winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + SpaceshipPureFunctions.getSpaceshipTypeByUuid(blackMarketOffer.getBlueprint(), gameWorld).getName() + ".");
            winningPlayer.addToHighlights(SpaceshipPureFunctions.getSpaceshipTypeByUuid(blackMarketOffer.getBlueprint(), gameWorld).getName(),HighlightType.TYPE_SHIPTYPE_WON);
        }
    }

    public static SpaceshipType getRandomShipBlueprint(Galaxy galaxy, GameWorld gameWorld) {
        List<SpaceshipType> possibleShipTypes = new LinkedList<>();
        for (SpaceshipType aSpaceshipType : gameWorld.getShipTypes()) {
            boolean allhaveType = true;
            for (Player aPlayer : PlayerPureFunctions.getActivePlayers(galaxy)) {
                if (PlayerPureFunctions.findSpaceshipImprovement(aSpaceshipType.getUuid(), aPlayer) == null) {
                    allhaveType = false;
                }
            }
            if (!allhaveType) {
                if (isBluePrintReadyToUseInBlackMarket(aSpaceshipType, galaxy.getTurn())) {
                    possibleShipTypes.add(aSpaceshipType);
                }
            }
        }

        SpaceshipType randomShiptype = null;
        SpaceshipType tempShipType;

        if (possibleShipTypes.size() > 0) {
            int totalFrequencypoint = 0;
            for (SpaceshipType spaceshipType : possibleShipTypes) {
                totalFrequencypoint += spaceshipType.getBluePrintFrequency().getFrequency();
            }

            int freqValue = Functions.getRandomInt(0, totalFrequencypoint - 1);
            int counter = 0;
            int tmpFreqSum = 0;
            while (randomShiptype == null) {
                tempShipType = possibleShipTypes.get(counter);
                tmpFreqSum = tmpFreqSum + tempShipType.getBluePrintFrequency().getFrequency();
                if (tmpFreqSum > freqValue) {
                    randomShiptype = tempShipType;
                }
                counter++;
            }
        }
        return randomShiptype;
    }

    public static void addBlackMarketBid(BlackMarketBid aBid, Galaxy galaxy){
        Logger.finer("BlackMarketBid: " + aBid.getOfferUuid());
        BlackMarketOffer aOffer = BlackMarketPureFunctions.findBlackMarketOffer(aBid.getOfferUuid(), galaxy);
        if(aOffer != null){
            aOffer.addBid(aBid);
        }

    }

    public static  SpaceshipType getRandomCommonSpaceshipType(Galaxy galaxy, GameWorld gameWorld) {
        SpaceshipType returnType = null;
        SpaceshipType tempShipType = null;
        List<SpaceshipType> allAvailableTypes = getSpaceshipTypesToBlackMarket(galaxy, gameWorld);
        int totalFrequencypoint = 0;
        for (SpaceshipType spaceshipType : allAvailableTypes) {
            totalFrequencypoint += spaceshipType.getBlackMarketFrequency().getFrequency();
        }

        int freqValue = Functions.getRandomInt(0, totalFrequencypoint - 1);
        int counter = 0;
        int tmpFreqSum = 0;
        while (returnType == null) {
            tempShipType = allAvailableTypes.get(counter);
            tmpFreqSum = tmpFreqSum + tempShipType.getBlackMarketFrequency().getFrequency();
            if (tmpFreqSum > freqValue) {
                returnType = tempShipType;
            }
            counter++;
        }
        return returnType;
    }

    /**
     * Searches through all sst lists for all factions, limited by the turn number
     * (no medium+ on turn 1 etc)
     *
     * @return
     */
    private static List<SpaceshipType> getSpaceshipTypesToBlackMarket(Galaxy galaxy, GameWorld gameWorld) {
        Logger.fine("getSpaceshipTypesToBlackMarket() called");
        List<SpaceshipType> ssTypes = new LinkedList<SpaceshipType>();
        // LoggingHandler.fine("Faction: " + aFaction.getName(),this);
        // LoggingHandler.fine("Ships nr: " + tmpSsTypes.size(),this);
        for (SpaceshipType spaceshipType : gameWorld.getShipTypes()) {
            if (isReadyToUseInBlackMarket(spaceshipType, galaxy, gameWorld)) {
                ssTypes.add(spaceshipType);
            }
        }
        return ssTypes;
    }

    private static boolean isReadyToUseInBlackMarket(SpaceshipType spaceshipType, Galaxy aGalaxy, GameWorld gameWorld){
        boolean constructible =  false;

        if (aGalaxy.getTurn() >= spaceshipType.getBlackmarketFirstTurn()){
            if (spaceshipType.getRange().canMove() || spaceshipType.getSize() == SpaceShipSize.SQUADRON){
                if (spaceshipType.isCanAppearOnBlackMarket()){
                    if(!spaceshipType.isPlayerUnique() && !spaceshipType.isFactionUnique()){
                        if(spaceshipType.isWorldUnique() && !SpaceshipPureFunctions.isWorldUniqueBuild(aGalaxy, spaceshipType, gameWorld)){
                            boolean isAlreadyAoffer = false;
                            for (BlackMarketOffer aBlackMarketOffer : aGalaxy.getCurrentOffers()) {
                                if(aBlackMarketOffer.isShip() && SpaceshipPureFunctions.getSpaceshipTypeByUuid(aBlackMarketOffer.getSpaceshipTypeUuid(), gameWorld).getName().equals(spaceshipType.getName())){
                                    isAlreadyAoffer = true;
                                }
                            }
                            if(!isAlreadyAoffer){
                                boolean haveBuildingOrder = false;
                                for (Player tempPlayer : aGalaxy.getPlayers()) {
                                    if(OrderPureFunctions.haveSpaceshipTypeBuildOrder(tempPlayer.getOrders(), spaceshipType)){
                                        haveBuildingOrder = true;
                                    }
                                }
                                if(!haveBuildingOrder){
                                    constructible =  true;
                                }
                            }
                        }else{
                            constructible = true;
                        }
                    }
                }
            }
        }
        return constructible;
    }

    public static boolean isBluePrintReadyToUseInBlackMarket(SpaceshipType spaceshipType, int turn){
        boolean constructible =  false;

        if (turn >= spaceshipType.getBluePrintFirstTurn()){
            if (spaceshipType.getBluePrintFrequency() != BlackMarketFrequency.NEVER){
                if(!spaceshipType.isPlayerUnique() && !spaceshipType.isFactionUnique() && !spaceshipType.isWorldUnique()){
                    constructible = true;
                }
            }
        }

        return constructible;
    }

    public static void sendRefundingMessages(BlackMarketOffer blackMarketOffer, String winningBidUuid, Galaxy galaxy){
        blackMarketOffer.getBlackMarketBids().stream()
                .filter(blackMarketBid -> !blackMarketBid.getUuid().equalsIgnoreCase(winningBidUuid))
                .forEach(blackMarketBid -> PlayerPureFunctions.getPlayer(galaxy, blackMarketBid.getUuid()).addToLatestBlackMarketMessages("Your bid of " + blackMarketBid.getCost() + " have been refunded."));
    }
}
