
public class Card {
    private String suit;
    private int value;
    private String cardName;
    public Card(String suit, int value, String cardName){
        this.suit = suit;
        this.value = value;
        this.cardName = cardName;
    }
    public String getSuit(){
        return this.suit;
    }
    public int getValue(){
        return this.value;
    }
    public String getCardName(){
        return this.cardName;
    }
    @Override
    public String toString(){
        return this.cardName + " of " + this.suit;
    }

}
