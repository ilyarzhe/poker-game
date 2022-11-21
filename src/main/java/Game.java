import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game {
    private int pot;
    private ArrayList<Player> players;
    private Deck deck;
    private ArrayList<Card> cardTable;

    public Game(){
        this.pot = 0;
        this.players = new ArrayList<>();
        this.deck = new Deck();
        this.cardTable = new ArrayList<>();


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
    public void genFlop(){
        this.cardTable.add(this.deck.getCards().get(5));
        this.cardTable.add(this.deck.getCards().get(6));
        this.cardTable.add(this.deck.getCards().get(7));
    }
    public void genTurn(){
        this.cardTable.add(this.deck.getCards().get(9));
    }
    public void genRiver(){
        this.cardTable.add(this.deck.getCards().get(11));
    }
    public boolean playOn(){
        return this.getPlayers().get(0).isInGame()&&this.getPlayers().get(1).isInGame();
    }
    public void addToPot(int amount){
        this.pot+=amount;
    }

}
