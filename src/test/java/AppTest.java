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
        player2 = new Player("Carl",10,true,false);
    }
    @Test
    public void firstTest() throws Exception {
        player1.allIn(game);
        player2.allIn(game);
        System.out.println(player1.getLastBet());
        System.out.println(player2.getLastBet());
        assertFalse(App.checkConditionContinue(player1, player2));
    }
}
