import components.Poker;
import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

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
        Poker.buildSuitCountTracker();
        Poker.buildCardFrequencyTracker();
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
        assertThat(player1.hasFlush(player1.genFullTable(game))).isEqualTo(true);
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
        assertThat(player1.hasFlush(player1.genFullTable(game))).isEqualTo(true);
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
        assertThat(player1.getHandScoreFromTable(game)).isEqualTo(5 * 14 + 13);
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
        ArrayList<Card> table = player1.genFullTable(game);
        assertThat(player1.hasFlush(table)).isEqualTo(false);
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
        assertThat(player1.hasStraight(player1.genFullTable(game))).isEqualTo(true);
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
        assertThat(player1.hasStraight(player1.genFullTable(game))).isEqualTo(true);
    }

    @Test
    public void straight__2fromHand() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 10, "10"));
        fakeTable.add(new Card("Clubs", 7, "7"));
        fakeTable.add(new Card("Diamonds", 9, "9"));
        fakeTable.add(new Card("Hearts", 7, "7"));
        fakeTable.add(new Card("Spades", 6, "6"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 8, "8"), new Card("Clubs", 6, "6"));
        player1.setHand(fakeHand);
        assertThat(player1.hasStraight(player1.genFullTable(game))).isEqualTo(true);
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
        assertThat(player1.hasStraight(player1.genFullTable(game))).isEqualTo(false);
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
        assertThat(player1.hasStraight(player1.genFullTable(game))).isEqualTo(true);
    }

    @Test
    public void ReturnCardFrequency_all_1() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Clubs", 7, "7"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 13, "K"), new Card("Clubs", 12, "Q"));
        player1.setHand(fakeHand);
        assertThat(player1.getHandScoreFromTable(game)).isEqualTo(14);
    }

    @Test
    public void ReturnCardFrequency__Pair() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Clubs", 12, "Q"));
        player1.setHand(fakeHand);
        assertThat(player1.checkFrequency(player1.genFullTable(game)).get("A")).isEqualTo(2);
    }

    @Test
    public void ReturnCardFrequency__Set() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Clubs", 14, "A"));
        player1.setHand(fakeHand);
        assertThat(player1.checkFrequency(player1.genFullTable(game)).get("A")).isEqualTo(3);
    }

    @Test
    public void ReturnCardFrequency__TwoPair() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Clubs", 3, "3"));
        player1.setHand(fakeHand);
        HashMap<String, Integer> actual = player1.checkFrequency( player1.genFullTable(game));
        assertThat(actual.get("A")).isEqualTo(2);
        assertThat(actual.get("3")).isEqualTo(2);
    }

    @Test
    public void ReturnCardFrequency__FullHouse() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 2, "2"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Clubs", 14, "A"));
        player1.setHand(fakeHand);
        HashMap<String, Integer> actual = player1.checkFrequency(player1.genFullTable(game));
        assertThat(actual.get("A")).isEqualTo(3);
        assertThat(actual.get("2")).isEqualTo(2);
    }

    @Test
    public void ReturnCardFrequency_Quad() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 14, "A"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Clubs", 14, "A"));
        player1.setHand(fakeHand);
        assertThat(player1.getHandScoreFromTable(game)).isEqualTo(7 * 14 + 14);
    }

    @Test
    public void CheckPair__true() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 14, "A"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 6, "6"), new Card("Clubs", 7, "7"));
        player1.setHand(fakeHand);
        assertThat(player1.getHandScoreFromTable(game)).isEqualTo(14+14);
    }

    @Test
    public void CheckPair__false() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 14, "A"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 13, "K"), new Card("Clubs", 12, "Q"));
        player1.setHand(fakeHand);
        assertThat(player1.hasPair(player1.checkFrequency(player1.genFullTable(game)))).isEqualTo(false);
    }

    @Test
    public void CheckTwoPair__false() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 13, "K"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 13, "K"), new Card("Clubs", 12, "Q"));
        player1.setHand(fakeHand);
    }

    @Test
    public void CheckTwoPair__true() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 13, "K"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 13, "K"), new Card("Clubs", 3, "3"));
        player1.setHand(fakeHand);
        assertThat(player1.getHandScoreFromTable(game)).isEqualTo(2*14+13);
    }

    @Test
    public void CheckTwoPair__sneaky() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 3, "3"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 13, "K"), new Card("Clubs", 3, "3"));
        player1.setHand(fakeHand);

    }

    @Test
    public void CheckSet__true() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 3, "3"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 13, "K"), new Card("Clubs", 3, "3"));
        player1.setHand(fakeHand);
        assertThat(player1.getHandScoreFromTable(game)).isEqualTo(3 * 14 + 3);
    }

    @Test
    public void CheckSet__false() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 3, "3"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 13, "K"), new Card("Clubs", 4, "4"));
        player1.setHand(fakeHand);
    }

    @Test
    public void CheckFullHouse__true() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 3, "3"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 2, "2"), new Card("Clubs", 3, "3"));
        player1.setHand(fakeHand);
        assertThat(player1.getHandScoreFromTable(game)).isEqualTo(6 * 14 + 3);
    }

    @Test
    public void CheckFullHouse__false() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 3, "3"));
        fakeTable.add(new Card("Clubs", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Spades", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Spades", 14, "A"), new Card("Clubs", 3, "3"));
        player1.setHand(fakeHand);
//        assertThat(player1.getHandScoreFromTable(game)).isEqualTo();
    }

    @Test
    public void straightFlush__true() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 3, "7"));
        fakeTable.add(new Card("Hearts", 2, "2"));
        fakeTable.add(new Card("Diamonds", 3, "3"));
        fakeTable.add(new Card("Hearts", 4, "4"));
        fakeTable.add(new Card("Hearts", 5, "5"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Hearts", 14, "A"), new Card("Clubs", 3, "3"));
        player1.setHand(fakeHand);
        assertThat(player1.getHandScoreFromTable(game)).isEqualTo(8 * 14 + 5);
    }

    @Test
    public void straightFlush__false() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 5, "5"));
        fakeTable.add(new Card("Hearts", 6, "6"));
        fakeTable.add(new Card("Spades", 14, "A"));
        fakeTable.add(new Card("Hearts", 7, "7"));
        fakeTable.add(new Card("Hearts", 11, "J"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Hearts", 9, "9"), new Card("Clubs", 8, "8"));
        player1.setHand(fakeHand);
        System.out.println(player1.genFullTable(game));
        System.out.println(player1.getHandScoreFromTable(game));
        Player player2 = new Player("Harry",100,true,false);
        player2.setHand(new Hand(new Card("Clubs",10,"10"),new Card("Clubs",9,"9")));
        System.out.println(player2.getHand());
        System.out.println(player2.genFullTable(game));
        System.out.println(player2.getHandScoreFromTable(game));
    }

    //    @Disabled
    @Test
    public void StraightFlushScore() {
        ArrayList<Card> fakeTable = new ArrayList<>();
        fakeTable.add(new Card("Hearts", 5, "5"));
        fakeTable.add(new Card("Hearts", 6, "6"));
        fakeTable.add(new Card("Spades", 14, "A"));
        fakeTable.add(new Card("Hearts", 7, "7"));
        fakeTable.add(new Card("Hearts", 11, "J"));
        game.setCardTable(fakeTable);
        Hand fakeHand = new Hand(new Card("Hearts", 9, "9"), new Card("Hearts", 8, "8"));
        player1.setHand(fakeHand);
        assertThat(player1.getHandScoreFromTable(game)).isEqualTo(8 * 14 + 9);
    }

    @Test
    public void Another() {
        assertThat(Poker.getCombinations().get("Straight Flush")).isEqualTo(8);
    }


}


