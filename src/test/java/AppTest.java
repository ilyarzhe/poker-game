import components.Poker;
import models.Game;
import models.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    private Game game;
    private Player player1;
    private Player player2;

    @BeforeAll public static void setUp(){
        Poker.buildSuits();
        Poker.buildCombinations();
        Poker.buildCardValues();

    }
    @BeforeEach
    public void setUpEach(){
        Poker.buildSuitCountTracker();
        Poker.buildCardFrequencyTracker();
        game = new Game();
        player1 = new Player("Henry",100,true,true);
        player2 = new Player("Carl",100,true,false);
    }
    @Test
    public void firstTest(){
        player1.bet(10,game);
        player2.bet( 10,game);
        assertFalse(App.checkConditionforBetting(player1, player2));
    }
}
