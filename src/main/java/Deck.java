import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards;
    public Deck(){
        this.cards = new ArrayList<>();
        this.build();
        this.shuffle();
    }
    public ArrayList<Card> getCards(){
        return this.cards;
    }
    //TODO: generate a deck using a forloop and Poker static methods
    public void build(){
        if (getCards().size()!=52) {
            for (String suit : Poker.getSuits()) {
                for (String cardName : Poker.getCardValues().keySet()) {
                    this.cards.add(new Card(suit, Poker.getCardValues().get(cardName), cardName));
                }

            }
        }

    }
    public void shuffle(){
        Random rand = new Random();
        int cardsSize = this.cards.size();
        for(int i = 0; i<cardsSize;i++){
            int r = i + rand.nextInt(cardsSize-i);
            Card temp = this.cards.get(r);
            this.cards.set(r,this.cards.get(i));
            this.cards.set(i,temp);
        }
    }
}
