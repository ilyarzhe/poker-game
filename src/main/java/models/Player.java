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

    private int lastBet;
    private boolean checkedLastRound;
    private boolean allIn;

    public Player(String name,
                  int stack,
                  boolean inGame,
                  boolean underTheGun
    ) {
        this.name = name;
        this.stack = stack;
        this.inGame = inGame;
        this.underTheGun = underTheGun;
        this.lastBet = 0;
        this.checkedLastRound = false;
        this.handScore=0;
        this.allIn=false;
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

    public int getLastBet() {
        return lastBet;
    }

    public void setLastBet(int lastBet) {
        this.lastBet = lastBet;
    }

    public boolean isCheckedLastRound() {
        return checkedLastRound;
    }

    public void setCheckedLastRound(boolean checkedLastRound) {
        this.checkedLastRound = checkedLastRound;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public void setAllIn(boolean allIn) {
        this.allIn = allIn;
    }

    public void bet(int amount, Game game) throws Exception {
        if (amount>this.stack){
            System.out.printf("The stack:%s\n",this.stack);
            throw new Exception("Error, You can't bet more than the stack amount. Try again!");

        }
        this.stack -= amount;
        game.addToPot(amount);
        this.lastBet=amount;
        this.checkedLastRound=false;
        this.allIn=false;
        this.inGame=true;
    }
    public void bet (int raiseAmount, int baseAmount, Game game)throws Exception{
        if (raiseAmount+baseAmount-lastBet>this.stack){
            System.out.printf("The stack:%s\n",this.stack);
            throw new Exception("Error, You can't bet more than the stack amount. Try again!");

        }
        int extra = raiseAmount+baseAmount-lastBet;
        this.stack -= extra;
        game.addToPot(extra);
        this.lastBet=raiseAmount+baseAmount;
        this.checkedLastRound=false;
        this.allIn=false;
        this.inGame=true;
    }

    public void fold() {
        this.inGame = false;
        this.checkedLastRound = false;
        this.lastBet=0;
        this.allIn=false;
    }

    public void check() {
        this.checkedLastRound = true;
        this.lastBet=0;
        this.inGame=true;
        this.allIn=false;
    }

    public void allIn(Game game,int opponentStack,int opponentLastBet) {
        int bet;
        if (this.stack+this.lastBet>opponentStack+opponentLastBet) {
            bet = opponentStack+opponentLastBet;
            game.addToPot(bet-this.lastBet);
        } else {
            bet = this.stack+this.lastBet;
            game.addToPot(this.stack);
        }
        this.stack -= bet-this.lastBet;
        this.lastBet=bet;
        this.checkedLastRound=false;
        this.inGame=true;
        this.allIn = true;
    }
    public void call(Game game, int opponentBet,int opponentStack) throws Exception {
        System.out.println(opponentBet-lastBet);
        bet(opponentBet-lastBet,game);
        lastBet=opponentBet;
        if (stack==0||opponentStack==0){
            this.allIn=true;
        }
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
        ArrayList<Card> fullTable = new ArrayList<>(game.getCardTable());
        if (fullTable.size()==5) {
            fullTable.add(this.hand.getCard1());
            fullTable.add(this.hand.getCard2());
        }
        return fullTable;
    }
    public HashMap<String,Integer> checkSuitCount(ArrayList<Card> fullTable){
        HashMap<String, Integer> suitCount = new HashMap<>(Poker.getSuitCountTracker());
        for (Card card :fullTable) {
            String cardSuit = card.getSuit();
            suitCount.replace(cardSuit, suitCount.get(cardSuit) + 1);
        }
        return suitCount;
    }

    public boolean hasFlush(ArrayList<Card> fullTable) {
        for (Integer suitCount  :
                this.checkSuitCount(fullTable).values()) {
            if (suitCount>=5){
                return true;
            }


        }
        return false;

    }
    public ArrayList<Integer> getUniqueCardValueTable(ArrayList<Card> cardTable){
        ArrayList<Integer> cardValuesOrdered = new ArrayList<>();
        for (Card card :cardTable) {
            cardValuesOrdered.add(card.getValue());

        }
        HashSet<Integer> cardValuesSet = new HashSet<>(cardValuesOrdered);
        ArrayList<Integer> cardValuesOrderedSet = new ArrayList<>(cardValuesSet);
        if (cardValuesOrderedSet.size()>0&&cardValuesOrderedSet.get(cardValuesOrderedSet.size()-1)==14){
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


    public HashMap<String, Integer> checkFrequency(ArrayList<Card> fullTable) {
        HashMap<String,Integer> frequencyTable = new HashMap<>(Poker.getCardFrequencyTracker());
        for (Card card : fullTable) {
            String cardName = card.getCardName();
            frequencyTable.replace(cardName, frequencyTable.get(cardName) + 1);

        }
        return frequencyTable;

    }



    public ArrayList<Integer> countFrequencies(HashMap<String,Integer> frequencyTable) {
        ArrayList<Integer> frequencyCountTable = new ArrayList<>();
        frequencyCountTable.add(Collections.frequency(frequencyTable.values(),2));
        frequencyCountTable.add(Collections.frequency(frequencyTable.values(),3));
        frequencyCountTable.add(Collections.frequency(frequencyTable.values(),4));
        return frequencyCountTable;
    }



    public Integer getMatchingComboCardValue(HashMap<String, Integer> frequencyTable,int target) {
        for (String cardName : frequencyTable.keySet())
            if (frequencyTable.get(cardName)==target){
                return Poker.getCardValues().get(cardName);
            }
        return 0;
    }
    public Integer getFullHouseCardValue(HashMap<String,Integer> frequencyTable){
        int score = 0;
        for (String cardName : frequencyTable.keySet()){
            if (frequencyTable.get(cardName)==3&&score<Poker.getCardValues().get(cardName)){
                score = Poker.getCardValues().get(cardName);
            }
        }
        return score;
    }


    public ArrayList<Card> getListOfCardsInAFlush(ArrayList<Card> fullTable){
        HashMap<String, Integer> suitCount = checkSuitCount(fullTable);
        ArrayList<Card> cardListSameSuit = new ArrayList<>();
        for (int i =0; i<fullTable.size();i++){
            String cardSuit = fullTable.get(i).getSuit();
            if(suitCount.get(cardSuit)>=5){
                cardListSameSuit.add(fullTable.get(i));
            }
        }
        return cardListSameSuit;
    }
    public Integer getHighestCardValueInAFlush(ArrayList<Card> listofCardsInAFlush){
        int max = 0;
        for (Card card : listofCardsInAFlush){
            if (card.getValue()>max){
                max = card.getValue();
            }
        }
        return max;
    }
    public Integer getHighestCardValueInAStraightFlush(ArrayList<Card> fullTable) {
        ArrayList<Card> cardListSameSuit = getListOfCardsInAFlush(fullTable);
        return getHighestCardValueInAStraight(getUniqueCardValueTable(cardListSameSuit));
    }

    public Integer getHandScoreFromTable(Game game){
        ArrayList<Card> table = genFullTable(game);
        HashMap<String,Integer> frequencyTable = checkFrequency(table);
        ArrayList<Integer> frequencyCountTable = countFrequencies(frequencyTable);



        if(getHighestCardValueInAStraightFlush(table)!=0){
            setHandScore(Poker.getCombinations().get("Straight Flush")*14+getHighestCardValueInAStraightFlush(table));
            return getHandScore();
        }
        if(frequencyCountTable.get(2)==1){
            setHandScore(Poker.getCombinations().get("Quads")*14+getMatchingComboCardValue(frequencyTable,4)); //Think of something to get the quad value
            return getHandScore();
        }
        if (frequencyCountTable.get(1)==2||(frequencyCountTable.get(1)==1&&frequencyCountTable.get(0)>=1)){
            setHandScore(Poker.getCombinations().get("Full House")*14+getFullHouseCardValue(frequencyTable));
            return getHandScore();
        }
        if (hasFlush(table)){
            ArrayList<Card> listOfFlushCards = getListOfCardsInAFlush(table);
            int score = getHighestCardValueInAFlush(listOfFlushCards);
            setHandScore(Poker.getCombinations().get("Flush")*14+score);
            return getHandScore();
        }
        if (getHighestCardValueInAStraight(getUniqueCardValueTable(table))>0){
            int score = getHighestCardValueInAStraight(getUniqueCardValueTable(table));
            setHandScore(Poker.getCombinations().get("Straight")*14+score);
            return getHandScore();
        }
        if (frequencyCountTable.get(1)==1){
            int score = getMatchingComboCardValue(frequencyTable,3);
            setHandScore(Poker.getCombinations().get("Three of a Kind")*14+score);
            return getHandScore();
        }
        if (frequencyCountTable.get(0)>=2){
            int score = getMatchingComboCardValue(frequencyTable,2);
            setHandScore(Poker.getCombinations().get("Two Pair")*14+score);
            return getHandScore();
        }
        if (frequencyCountTable.get(0)==1){
            int score = getMatchingComboCardValue(frequencyTable,2);
            setHandScore(Poker.getCombinations().get("Pair")*14+score);
            return getHandScore();
        }
        if (frequencyCountTable.get(0)==0){
            int score = getMatchingComboCardValue(frequencyTable,1);
            setHandScore(Poker.getCombinations().get("High Card")*14+score);
            return getHandScore();
        }
        return 0;
    }
    public void reset(){
        this.underTheGun=!underTheGun;
        this.lastBet=0;
        this.allIn=false;
        this.checkedLastRound=false;
        this.handScore=0;
    }


}
