import components.Actions;
import components.Poker;
import models.Deck;
import models.Game;
import models.Player;

import javax.swing.*;
import java.sql.SQLOutput;
import java.util.*;

public class App {
    public static void main(String[] args) {
        startGame();
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        Player player1 = generatePlayer(scanner,1);
        Player player2 = generatePlayer(scanner, 2);
        player1.drawHand(game);
        player2.drawHand(game);
        int roundCount = 1;
        while (roundCount<5){
            if (player1.isInGame()&&player2.isInGame()) {
                startRoundOfBetting(player1, player2, game, scanner, roundCount);
                game.generateTable(roundCount);
                roundCount++;
            }
        }



    }
    private static void startGame(){
        System.out.println("Welcome two H2H Poker!");
        Poker.buildCardValues();
        Poker.buildSuits();
        Poker.buildCombinations();
        Poker.buildCardFrequencyTracker();
        Poker.buildSuitCountTracker();
        System.out.println("The game has successfully loaded");
        System.out.println("-".repeat(10));
    }
    private static Player generatePlayer(Scanner scanner,int number){
        System.out.println("Time to input player " + number + " details!");
        System.out.println("Please input player name:");
        String playerName = scanner.nextLine();
        System.out.println("Please input the stack amount: ");
        int playerStack= 0;
        while(playerStack==0) {
            try {
                playerStack = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You can only input number value for this! Try Again!");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        return new Player(playerName,playerStack,true,number==1);
    }
    private static void startRoundOfBetting(Player player1,Player player2, Game game,Scanner scanner,int roundNumber){
        Player tempPlayer1 = player1;
        Player tempPlayer2 = player2;
        if ((tempPlayer1.isUnderTheGun()&&roundNumber%2==1)||(!tempPlayer1.isUnderTheGun()&&roundNumber%2==0)){
            starterRoundOfAction(game,tempPlayer1,scanner,roundNumber);
            do {

                responseAction(game, tempPlayer2, tempPlayer1, scanner);
                Player extra = tempPlayer1;
                tempPlayer1 = tempPlayer2;
                tempPlayer2 = extra;
            } while(tempPlayer2.getLastBet()!=tempPlayer1.getLastBet()&&tempPlayer1.isInGame()&&tempPlayer2.isInGame());
            //here the response of player 1
            // here we will have the response of player two
        }
        if ((tempPlayer2.isUnderTheGun()&&roundNumber%2==1)||(!tempPlayer2.isUnderTheGun()&&roundNumber%2==0)){
            starterRoundOfAction(game,tempPlayer2,scanner,roundNumber);
            do {
                responseAction(game, tempPlayer1, tempPlayer2, scanner);
                Player extra = tempPlayer1;
                tempPlayer1 = tempPlayer2;
                tempPlayer2 = extra;
            } while(tempPlayer2.getLastBet()!=tempPlayer1.getLastBet()&&tempPlayer1.isInGame()&&tempPlayer2.isInGame());
            //here the response of player 1
        }
        if (Objects.equals(tempPlayer1.getName(), player1.getName())){
            player1=tempPlayer1;
            player2=tempPlayer2;
        } else {
            player1 = tempPlayer2;
            player2 = tempPlayer1;
        }
        System.out.println(player1.getName());
        System.out.println(player2.getName());
        System.out.printf("The pot is %s",game.getPot());
    }
    private static void starterRoundOfAction(Game game,Player player,Scanner scanner,int roundNumber){
        displayUI(game,player,scanner,roundNumber>1);
        System.out.println("(bet,fold,check,allin):");
        String action = scanner.nextLine();
        checkPlayBet(game,player,action,scanner);
        checkPlayCheck(game,player,action,scanner);
        checkPlayFold(game,player,action,scanner);
        checkPlayAllIn(game,player,action,scanner);




    }
    private static void responseAction(Game game, Player you,Player opponent,Scanner scanner){
        // check what was previous player's action
        // construct a response function based on the action
        if (opponent.isAllIn()){
            allInResponse(game,you,opponent,scanner);
        }
        if (opponent.isCheckedLastRound()){
            checkInResponse(game,you,opponent,scanner);
        }
        if (!opponent.isInGame()){
            foldResponse(game,you,opponent,scanner);
        }
        if (opponent.getLastBet()>0){
            betResponse(game,you,opponent,scanner);
        }
    }

    private static void allInResponse(Game game, Player you,Player opponent,Scanner scanner){
        System.out.printf("%s is all in!",opponent.getName());
        displayUI(game,you,scanner,false);
        System.out.println("(allin or fold):");
        String action = scanner.nextLine();
        checkPlayAllIn(game,you,action,scanner);
        checkPlayFold(game,you,action,scanner);
    }
    private static void checkInResponse(Game game, Player you, Player opponent,Scanner scanner){
        System.out.printf("%s has checked!",opponent.getName());
        displayUI(game,you,scanner,false);
        System.out.println("(bet,fold,allin or call)");
        String action = scanner.nextLine();
        checkPlayAllIn(game,you,action,scanner);
        checkPlayFold(game,you,action,scanner);
        checkPlayBet(game,you,action,scanner);
        checkPlayCheck(game,you,action,scanner);
    }
    private static void foldResponse(Game game,Player you,Player opponent,Scanner scanner){
        System.out.printf("%s has folded!, You won",opponent.getName());
        you.winHand(game);
        game.reset();
    }
    private static void betResponse(Game game, Player you, Player opponent,Scanner scanner){
        System.out.printf("%s put in a %d bet!",opponent.getName(),opponent.getLastBet());
        displayUI(game,you,scanner,false);
        System.out.println("(call or fold):");
        String action = scanner.nextLine();
        checkPlayCall(game,you,opponent,action,scanner);
        checkPlayFold(game,you,action,scanner);
    }

    private static int inputNumber(Scanner scanner){
        System.out.println("Input bet Amount:");
        int num = 0;
        while (num==0){
            try {
                num = scanner.nextInt();
            }catch (InputMismatchException e){
                System.out.println("Invalid input! Please try again!");
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        return num;
    }
    private static void displayUI(Game game,Player player,Scanner scanner,boolean flop) {
        if (flop) {
            System.out.println("\nCommunity Cards:");
            System.out.println(game.getCardTable());
        }
        System.out.println("Your Hand:");
        System.out.println(player.getHand().toString());
        System.out.println("Your turn for action!");
    }
    private static void checkPlayBet(Game game,Player player, String action,Scanner scanner){
        if (action.equalsIgnoreCase(Actions.BET.toString())){
            int betSize = inputNumber(scanner);
            player.bet(betSize,game);
        }
    }
    private static void checkPlayFold(Game game,Player player, String action,Scanner scanner){
        if (action.equalsIgnoreCase(Actions.FOLD.toString())){
            player.fold();
        }
    }
    private static void checkPlayCheck(Game game,Player player, String action,Scanner scanner){
        if (action.equalsIgnoreCase(Actions.CHECK.toString())){
            player.check();
        }
    }
    private static void checkPlayAllIn(Game game,Player player, String action,Scanner scanner){
        if (action.equalsIgnoreCase(Actions.ALLIN.toString())){
            player.allIn(game);
        }
    }
    private static void checkPlayCall(Game game,Player player,Player opponent, String action,Scanner scanner){
        if (action.equalsIgnoreCase(Actions.CALL.toString())){
            player.bet(opponent.getLastBet(),game);
        }
    }
    private static void decideWinner(Game game, Player player1, Player player2){
        int player1Score = player1.getHandScoreFromTable(game);
        int player2Score = player2.getHandScoreFromTable(game);
        if (player1Score>player2Score){
            player1.winHand(game);
            game.reset();
            player1.reset();
            player2.reset();
            System.out.printf("%s, you have won!",player1.getName());
        }
    }
//    private static void checkPlayBet(Player player, Scanner scanner){}, implement this later
}
