import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GameTest {
    private Game table;

    @BeforeAll
    public static void setUpBeforeAll(){
        Poker.buildCombinations();
        Poker.buildSuits();
        Poker.buildCardValues();

    }

    @BeforeEach
    public void setUp(){
        table = new Game();
        Player player1 = new Player("Henry",100,true,true);
        Player player2 = new Player("Ivan",100,true,false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        table.setPlayers(players);
    }
    @Test
    public void flopTestSize(){
        table.genFlop();
        assertThat(table.getCardTable().size()).isEqualTo(3);
    }
    @Test
    public void flopTestRightCards(){
        System.out.println(table.getDeck().getCards().subList(0,10));
        table.genFlop();
        ArrayList<Card> expected = new ArrayList<>();
        expected.add(table.getDeck().getCards().get(5));
        expected.add(table.getDeck().getCards().get(6));
        expected.add(table.getDeck().getCards().get(7));
        assertThat(table.getCardTable()).isEqualTo(expected);
    }
    @Test
    public void turnTestSize(){
        table.genFlop();
        table.genTurn();
        assertThat(table.getCardTable().size()).isEqualTo(4);
    }
    @Test
    public void turnTestRightCards(){
        table.genFlop();
        table.genTurn();
        ArrayList<Card> expected = new ArrayList<>();
        expected.add(table.getDeck().getCards().get(5));
        expected.add(table.getDeck().getCards().get(6));
        expected.add(table.getDeck().getCards().get(7));
        expected.add(table.getDeck().getCards().get(9));

        assertThat(table.getCardTable()).isEqualTo(expected);
    }
    @Test
    public void testPlayOn__true(){
        assertThat(table.playOn()).isEqualTo(true);
    }
    @Test
    public void testPlayOn__false(){
        table.getPlayers().get(1).setInGame(false);
        assertThat(table.playOn()).isEqualTo(false);

    }

}
