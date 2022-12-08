package models;

import components.Poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Player {
    private String name;
    private Hand hand;
    private int stack;
    private boolean inGame;
    private int handScore;
    private boolean underTheGun;

    public Player(String name,
                  int stack,
                  boolean inGame,
                  boolean underTheGun
    ) {
        this.name = name;
        this.stack = stack;
        this.inGame = inGame;
        this.underTheGun = underTheGun;
    }

    public boolean isUnderTheGun() {
        return this.underTheGun;
    }

    public String getName() {
        return this.name;
    }

    public Hand getHand() {
        return this.hand;
    }

    public int getStack() {
        return this.stack;
    }

    public boolean isInGame() {
        return this.inGame;
    }

    public int getHandScore() {
        return this.handScore;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setStack(int amount) {
        this.stack = amount;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void setHandScore(int score) {
        this.handScore = score;
    }

    public void setUnderTheGun(boolean underTheGun) {
        this.underTheGun = underTheGun;
    }

    public void bet(int amount, Game game) {
        this.stack -= amount;
        game.addToPot(amount);
    }

    public void fold() {
        this.inGame = false;
    }

    public boolean check() {
        return true;
    }

    public void allIn(Game game) {
        this.stack -= this.stack;
        game.addToPot(this.stack);
    }

    public void winHand(Game game) {
        this.stack += game.getPot();
    }

    public void drawHand(Game game) {
        Hand dealtHand;
        if (this.underTheGun) {
            dealtHand = new Hand(game.getDeck().getCards().get(0), game.getDeck().getCards().get(2));
        } else {
            dealtHand = new Hand(game.getDeck().getCards().get(1), game.getDeck().getCards().get(3));
        }
        this.hand = dealtHand;


    }

    public ArrayList<Card> genFullTable(Game game) {
        ArrayList<Card> fullTable = game.getCardTable();
        fullTable.add(this.getHand().getCard1());
        fullTable.add(this.getHand().getCard2());
        return fullTable;
    }
    public HashMap<String,Integer> checkSuitCount(Game game){
        HashMap<String, Integer> suitCount = Poker.getSuitCountTracker();
        for (Card card :
                this.genFullTable(game)) {
            String cardSuit = card.getSuit();
            suitCount.replace(cardSuit, suitCount.get(cardSuit) + 1);
        }
        return suitCount;
    }

    public boolean hasFlush(Game game) {
        for (Integer suitCount  :
                this.checkSuitCount(game).values()) {
            if (suitCount>=5){
                return true;
            }


        }
        return false;

    }
    public ArrayList<Integer> getUniqueCardValueTable(Game game ,ArrayList<Card> cardTable){
        ArrayList<Integer> cardValuesOrdered = new ArrayList<>();
        for (Card card :cardTable) {
            cardValuesOrdered.add(card.getValue());

        }
        HashSet<Integer> cardValuesSet = new HashSet<>(cardValuesOrdered);
        ArrayList<Integer> cardValuesOrderedSet = new ArrayList<>(cardValuesSet);
        if (cardValuesOrderedSet.get(cardValuesOrderedSet.size()-1)==14){
            cardValuesOrderedSet.add(0,1);
        }
        return cardValuesOrderedSet;
    }
    public Integer getHighestCardValueInAStraight(ArrayList<Integer> cardValuesOrderedSet){
        for(int i = cardValuesOrderedSet.size()-1;i>=4;i--){
            if (cardValuesOrderedSet.get(i)-cardValuesOrderedSet.get(i-4)==4){
                return cardValuesOrderedSet.get(i);
            }
        }
        return 0;
    }

    public boolean hasStraight(Game game,ArrayList<Card> cardList) {
        return getHighestCardValueInAStraight(getUniqueCardValueTable(game,cardList))>0;
    }

    public HashMap<String, Integer> checkFrequency(Game game) {
        HashMap<String,Integer> frequencyTable = Poker.getCardFrequencyTracker();
        ArrayList<Card> fullTable = this.genFullTable(game);
        for (Card card : fullTable) {
            String cardName = card.getCardName();
            frequencyTable.replace(cardName, frequencyTable.get(cardName) + 1);

        }
        return frequencyTable;

    }

    public boolean hasPair(HashMap<String, Integer> frequencyTable) {
        for (int cardCount : frequencyTable.values()) {
            if (cardCount >= 2) {
                return true;
            }
        }
        return false;

    }

    public Integer countFrequencies(HashMap<String, Integer> frequencyTable, Integer targetFrequency) {
        return Collections.frequency(frequencyTable.values(), targetFrequency);
    }

    public boolean hasTwoPair(HashMap<String, Integer> frequencyTable) {
        return countFrequencies(frequencyTable, 2) + countFrequencies(frequencyTable, 3) + countFrequencies(frequencyTable, 4) >= 2;
    }

    public boolean hasSet(HashMap<String, Integer> frequencyTable) {
        return countFrequencies(frequencyTable, 3) + countFrequencies(frequencyTable, 4) >= 1;
    }

    public boolean hasQuads(HashMap<String, Integer> frequencyTable) {
        return countFrequencies(frequencyTable, 4) >= 1;
    }

    public boolean hasFullHouse(HashMap<String, Integer> frequencyTable) {
        int count2 = countFrequencies(frequencyTable, 2);
        int count3 = countFrequencies(frequencyTable, 3);
        int count4 = countFrequencies(frequencyTable, 4);
        return  (count3>=1&&count2>=1)||(count2>=1&&count4>=1)||(count3>=1&&count4>=1);
    }
    public boolean hasStraightFlush(Game game){
        if (!hasFlush(game)||!hasStraight(game,genFullTable(game))){
            return false;
        }
        return getHighestCardValueInAStraightFlush(game)>0;

    }
    public ArrayList<Card> getListOfCardsInAFlush(Game game){
        HashMap<String, Integer> suitCount = checkSuitCount(game);
        ArrayList<Card> fullTable = genFullTable(game);
        ArrayList<Card> cardListSameSuit = new ArrayList<>();
        for (int i =0; i<fullTable.size();i++){
            String cardSuit = fullTable.get(i).getSuit();
            if(suitCount.get(cardSuit)>=5){
                cardListSameSuit.add(fullTable.get(i));
            }
        }
        return cardListSameSuit;
    }
    public Integer getHighestCardValueInAStraightFlush(Game game) {
        ArrayList<Card> cardListSameSuit = getListOfCardsInAFlush(game);
        return getHighestCardValueInAStraight(getUniqueCardValueTable(game, cardListSameSuit));
    }
    //TODO:HandScore function
    public Integer getHandScoreFromTable(Game game){
        ArrayList<Card> table = genFullTable(game);
        HashMap<String,Integer> frequencyTable = checkFrequency(game);

        if(getHighestCardValueInAStraightFlush(game)!=0){
            setHandScore(Poker.getCombinations().get("Straight Flush")*14+getHighestCardValueInAStraightFlush(game));
            return getHandScore();
        }
        return 0;

        // Start generate highest card in a straight, highest card in flush and count frequencies
        // Think of the best order to check combinations
        // add a score to the combination based on the highest card

    }


}
