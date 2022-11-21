import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DeckTest {
    private Deck deck;
    @BeforeAll
    public static void beforeAllSetup(){
        Poker.buildCardValues();
        Poker.buildCombinations();
        Poker.buildSuits();
    }

    @BeforeEach
    public void setUp(){
        deck = new Deck();
    }
    @Test
    public void deckBuild(){
        assertThat(deck.getCards().size()).isEqualTo(52);
    }
}
