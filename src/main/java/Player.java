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
        if (this.underTheGun) {
            Hand dealtHand = new Hand(game.getDeck().getCards().get(0), game.getDeck().getCards().get(2));
            this.hand = dealtHand;
        } else {
            Hand dealtHand = new Hand(game.getDeck().getCards().get(1), game.getDeck().getCards().get(3));
            this.hand = dealtHand;
        }


    }

    public ArrayList<Card> genFullTable(Game game) {
        ArrayList<Card> fullTable = game.getCardTable();
        fullTable.add(this.getHand().getCard1());
        fullTable.add(this.getHand().getCard2());
        return fullTable;
    }

    public boolean hasFlush(Game game) {
        HashMap<String, Integer> suitCount = new HashMap<>();
        for (String suit :
                Poker.getSuits()) {
            suitCount.put(suit, 0);
        }
        for (Card card :
                this.genFullTable(game)) {
            suitCount.replace(card.getSuit(), suitCount.get(card.getSuit()) + 1);
        }
        for (Integer count :
                suitCount.values()) {
            if (count >= 5) {
                return true;
            }
        }
        return false;

    }

    public boolean hasStraight(Game game) {
        ArrayList<Integer> cardValuesOrdered = new ArrayList<>();
        for (Card card :
                this.genFullTable(game)) {
            cardValuesOrdered.add(card.getValue());

        }
        HashSet<Integer> cardValuesSet = new HashSet<>(cardValuesOrdered);
        Collections.sort(cardValuesOrdered);
        int setSize = cardValuesOrdered.size();
        if (cardValuesSet.size() >= 5) {
            for (int i = 0; i < setSize - 5; i++) {
                if (cardValuesOrdered.get(i + 4) - cardValuesOrdered.get(i) == 4) {
                    return true;
                }
            }
            if (cardValuesOrdered.get(setSize - 1) - cardValuesOrdered.get(0) == 12 &&
                    cardValuesOrdered.get(3) - cardValuesOrdered.get(0) == 3) {
                return true;
            }

        }
        return false;
    }

    public HashMap<String, Integer> checkFrequency(Game game) {
        HashMap<String, Integer> frequencyTable = Poker.getCardValues();
        for (String cardName : frequencyTable.keySet()) {
            frequencyTable.put(cardName, 0);
        }
        ArrayList<Card> fullTable = this.genFullTable(game);
        for (Card card : fullTable) {
            String cardName = card.getCardName();
            frequencyTable.put(cardName, frequencyTable.get(cardName) + 1);

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

    public Integer CountFrequencies(HashMap<String, Integer> frequencyTable, Integer targetFrequency) {
        return Collections.frequency(frequencyTable.values(), targetFrequency);
    }

    public boolean hasTwoPair(HashMap<String, Integer> frequencyTable) {
        return CountFrequencies(frequencyTable, 2) + CountFrequencies(frequencyTable, 3) + CountFrequencies(frequencyTable, 4) >= 2;
    }

    public boolean hasSet(HashMap<String, Integer> frequencyTable) {
        return CountFrequencies(frequencyTable, 3) + CountFrequencies(frequencyTable, 4) >= 1;
    }

    public boolean hasQuads(HashMap<String, Integer> frequencyTable) {
        return CountFrequencies(frequencyTable, 4) >= 1;
    }

    public boolean hasFullHouse(HashMap<String, Integer> frequencyTable) {
        int count2 = CountFrequencies(frequencyTable, 2);
        int count3 = CountFrequencies(frequencyTable, 3);
        int count4 = CountFrequencies(frequencyTable, 4);
        return  (count3>=1&&count2>=1)||(count2>=1&&count3>=1)||(count3>=1&&count4>=1);
    }
    public boolean hasStraightFlush(Game game){
        if (!hasStraight(game)||!hasFlush(game)){
            return false;
        }
        ArrayList<Card> fullTable = genFullTable(game);
        //Todo: implement a check for a straight and flush together! Check that we have the same cards for both flush and straight
        
    }


}
