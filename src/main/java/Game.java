import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game {
    private int pot;
    private ArrayList<Player> players;
    private Deck deck;
    private ArrayList<Card> cardTable;

    public Game(int pot,
                ArrayList<Player> players,
                Deck deck,
                ArrayList<Card> cardTable){
        this.pot =pot;
        this.players = players;
        this.deck = deck;
        this.cardTable = cardTable;

    }
    public int getPot(){
        return this.pot;
    }
    public ArrayList<Player> getPlayers(){
        return this.players;
    }
    public Deck getDeck(){
        return this.deck;
    }
    public ArrayList<Card> getCardTable(){
        return this.cardTable;
    }
    public void setPot(int amount){
        this.pot = amount;
    }
    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }
    public void setDeck(Deck deck){
        this.deck = deck;
    }
    public void setCardTable(ArrayList<Card> cards){
        this.cardTable = cards;
    }
}
