import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PlayerTest {
    Player player1;
    Game game;
    Deck deck;

    @BeforeAll
    public static void mainSetUp() {
        Poker.buildCardValues();
        Poker.buildSuits();
        Poker.buildCombinations();
    }

    @BeforeEach
    public void setUp() {
        player1 = new Player("Henry", 100, true, true);
        game = new Game();
        deck = new Deck();
        game.genFlop();
        game.genTurn();
        game.genRiver();
    }

    @Test
    public void checkDrawHand__UTG() {
        player1.drawHand(game);
        Hand playerHand = new Hand(game.getDeck().getCards().get(0), game.getDeck().getCards().get(2));
        assertThat(player1.getHand().getCard1()).isEqualTo(playerHand.getCard1());
        assertThat(player1.getHand().getCard2()).isEqualTo(playerHand.getCard2());
    }

    @Test
    public void checkDrawHand__notUTG() {
        player1.setUnderTheGun(false);
        player1.drawHand(game);
        Hand playerHand = new Hand(game.getDeck().getCards().get(1), game.getDeck().getCards().get(3));
        assertThat(player1.getHand().getCard1()).isEqualTo(playerHand.getCard1());
        assertThat(player1.getHand().getCard2()).isEqualTo(playerHand.getCard2());
    }

    @Test
    public void flush__trueCommunityCardsOnly() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 10, "10"));
        fakeTable.add(new Card("Hearts", 13, "K"));
        fakeTable.add(new Card("Hearts", 2, "2"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Hearts", 8, "8"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Spades", 13, "K"));
        player1.setHand(fakeHand);
        assertThat(player1.hasFlush(game)).isEqualTo(true);
    }

    @Test
    public void flush__trueCommunityCardsand1fromHand() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 10, "10"));
        fakeTable.add(new Card("Hearts", 13, "K"));
        fakeTable.add(new Card("Hearts", 2, "2"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 8, "8"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Hearts", 11, "J"));
        player1.setHand(fakeHand);
        assertThat(player1.hasFlush(game)).isEqualTo(true);
    }

    @Test
    public void flush__trueCommunityCardsand2fromHand() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 10, "10"));
        fakeTable.add(new Card("Hearts", 13, "K"));
        fakeTable.add(new Card("Hearts", 2, "2"));
        fakeTable.add(new Card("Spades", 4, "4"));
        fakeTable.add(new Card("Spades", 8, "8"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Hearts", 11, "J"), new Card("Hearts", 12, "Q"));
        player1.setHand(fakeHand);
        assertThat(player1.hasFlush(game)).isEqualTo(true);
    }

    @Test
    public void flush__false() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 10, "10"));
        fakeTable.add(new Card("Hearts", 13, "K"));
        fakeTable.add(new Card("Hearts", 2, "2"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 8, "8"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Clubs", 11, "J"));
        player1.setHand(fakeHand);
        assertThat(player1.hasFlush(game)).isEqualTo(false);
    }

    @Test
    public void straight__CommunityCardsOnly() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 10, "10"));
        fakeTable.add(new Card("Clubs", 6, "6"));
        fakeTable.add(new Card("Diamonds", 9, "9"));
        fakeTable.add(new Card("Hearts", 7, "7"));
        fakeTable.add(new Card("Spades", 8, "8"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Clubs", 12, "Q"));
        player1.setHand(fakeHand);
        assertThat(player1.hasStraight(game)).isEqualTo(true);
    }

    @Test
    public void straight__1fromHand() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 10, "10"));
        fakeTable.add(new Card("Clubs", 6, "6"));
        fakeTable.add(new Card("Diamonds", 9, "9"));
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Spades", 8, "8"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 7, "7"), new Card("Clubs", 12, "Q"));
        player1.setHand(fakeHand);
        assertThat(player1.hasStraight(game)).isEqualTo(true);
    }

    @Test
    public void straight__2fromHand() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 10, "10"));
        fakeTable.add(new Card("Clubs", 12, "Q"));
        fakeTable.add(new Card("Diamonds", 9, "9"));
        fakeTable.add(new Card("Hearts", 7, "7"));
        fakeTable.add(new Card("Spades", 14, "A"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 8, "8"), new Card("Clubs", 6, "6"));
        player1.setHand(fakeHand);
        assertThat(player1.hasStraight(game)).isEqualTo(true);
    }

    @Test
    public void straight__false() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 10, "10"));
        fakeTable.add(new Card("Clubs", 6, "6"));
        fakeTable.add(new Card("Diamonds", 9, "9"));
        fakeTable.add(new Card("Hearts", 7, "7"));
        fakeTable.add(new Card("Spades", 13, "K"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Clubs", 2, "2"));
        player1.setHand(fakeHand);
        assertThat(player1.hasStraight(game)).isEqualTo(false);
    }

    @Test
    public void straight__A2345() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 13, "K"), new Card("Clubs", 12, "Q"));
        player1.setHand(fakeHand);
        assertThat(player1.hasStraight(game)).isEqualTo(true);
    }
}



