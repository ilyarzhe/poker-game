public class App {
    public static void main(String[] args) {
        Poker.buildCardValues();
        Poker.buildSuits();
        Poker.buildCombinations();
        Deck deck = new Deck();
        deck.build();
        deck.shuffle();

    }
}
