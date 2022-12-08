import components.Poker;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PokerTest {
    @Test
    public void buildDeck(){
        Poker.buildCombinations();

        assertThat(Poker.getCombinations().get("High Card")).isEqualTo(0);
        assertThat(Poker.getCombinations().get("Three of a Kind")).isEqualTo(3);
    }
}
