import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DeckTest {
    private Deck deck;
    @BeforeEach
    public void setUp(){
        deck = new Deck();
        Poker.buildCardValues();
        Poker.buildCombinations();
        Poker.buildSuits();
        deck.build();
    }
    @Test
    public void deckBuild(){
        assertThat(deck.getCards().get(51).toString()).isEqualTo("10 of Spades");
    }
    @Test
    public void checkSize(){
        assertThat(deck.getCards().size()).isEqualTo(52);
    }

}
