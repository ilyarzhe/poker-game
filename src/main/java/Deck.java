import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards;
    public Deck(){
        this.cards = new ArrayList<>();
    }
    public ArrayList<Card> getCards(){
        return this.cards;
    }
    //TODO: generate a deck using a forloop and Poker static methods
    public void build(){
        for (String suit:
             Poker.getSuits()) {
            for (String cardName :
                    Poker.getCardValues().keySet()) {
                cards.add(new Card(suit,Poker.getCardValues().get(cardName),cardName));
            }

        }

    }
    public void shuffle(){
        Random rand = new Random();
        for(int i = 0; i<this.cards.size();i++){
            int r = i + rand.nextInt(52-i);
            Card temp = this.cards.get(r);
            this.cards.set(r,this.cards.get(i));
            this.cards.set(i,temp);
        }
    }
}
