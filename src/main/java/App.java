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
//      TOdo: three lines below are removed for testing purposes
//        Player player1 = generatePlayer(scanner,1);
//        Player player2 = generatePlayer(scanner, 2);
        Player player1 = new Player("Henry",100,true,true);
        Player player2 = new Player("Slava",100,true,false);


        while (player1.getStack()>0&&player2.getStack()>0) {
            player1.drawHand(game);
            player2.drawHand(game);
            int roundCount = 1;
            while (roundCount < 5) {
                System.out.println("This round:"+roundCount);
                startRoundOfBetting(player1, player2, game, scanner, roundCount);
                if (player1.isInGame() && player2.isInGame()) {
                    game.generateTable(roundCount);
                    roundCount++;
                } else {
                    break;
                }

            }
            decideWinner(game, player1, player2);
            gameReset(game, player1, player2);
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
        //TODO:this needs serious simplification, not very DRY!!!!!!
        if ((tempPlayer1.isUnderTheGun()&&roundNumber%2==1)||(!tempPlayer1.isUnderTheGun()&&roundNumber%2==0)){
            starterRoundOfAction(game,tempPlayer1,scanner,roundNumber);
            do {

                responseAction(game, tempPlayer2, tempPlayer1, scanner);
                Player extra = tempPlayer1;
                tempPlayer1 = tempPlayer2;
                tempPlayer2 = extra;
            } while(checkConditionforBetting(tempPlayer1,tempPlayer2));
            //here the response of player 1
            // here we will have the response of player two
        } else if ((tempPlayer2.isUnderTheGun()&&roundNumber%2==1)||(!tempPlayer2.isUnderTheGun()&&roundNumber%2==0)){
            starterRoundOfAction(game,tempPlayer2,scanner,roundNumber);
            do {
                responseAction(game, tempPlayer1, tempPlayer2, scanner);
                Player extra = tempPlayer1;
                tempPlayer1 = tempPlayer2;
                tempPlayer2 = extra;
            } while(checkConditionforBetting(tempPlayer1,tempPlayer2));
            //here the response of player 1
        }
        if (Objects.equals(tempPlayer1.getName(), player1.getName())){
            player1=tempPlayer1;
            player2=tempPlayer2;
        } else {
            player1 = tempPlayer2;
            player2 = tempPlayer1;
        }
        System.out.printf("The pot is %s\n",game.getPot());
        System.out.println("-".repeat(10));
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
        System.out.printf("%s is all in!\n",opponent.getName());
        displayUI(game,you,scanner,false);
        System.out.println("(allin or fold):");
        String action = scanner.nextLine();
        checkPlayAllIn(game,you,action,scanner);
        checkPlayFold(game,you,action,scanner);
    }
    private static void checkInResponse(Game game, Player you, Player opponent,Scanner scanner){
        System.out.printf("%s has checked!\n",opponent.getName());
        displayUI(game,you,scanner,false);
        System.out.println("(bet,fold,check or allin)");
        String action = scanner.nextLine();
        checkPlayAllIn(game,you,action,scanner);
        checkPlayFold(game,you,action,scanner);
        checkPlayBet(game,you,action,scanner);
        checkPlayCheck(game,you,action,scanner);
    }
    private static void foldResponse(Game game,Player you,Player opponent,Scanner scanner){
        System.out.printf("%s has folded!, You won\n",opponent.getName());
        you.winHand(game);
        gameReset(game,you,opponent);

    }
    private static void betResponse(Game game, Player you, Player opponent,Scanner scanner){
        System.out.printf("%s put in a %d bet!\n",opponent.getName(),opponent.getLastBet());
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
        System.out.println("-".repeat(10));
        System.out.printf("%s, your Turn!\n",player.getName());
        if (flop) {
            System.out.println("\nCommunity Cards:");
            System.out.println(game.getCardTable());
        }
        System.out.println("\nYour Hand:");
        System.out.println(player.getHand().toString()+"\n");
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
        System.out.println(player1Score);
        System.out.println(player2Score);
        System.out.println("-".repeat(10));
        if (player1Score>player2Score){
            player1.winHand(game);
            winnerOutput(game,player1);
        }
        if (player2Score>player1Score) {
            player2.winHand(game);
            winnerOutput(game,player2);
        }
    }
    private static void gameReset(Game game, Player  player1, Player player2){
        game.reset();
        player1.reset();
        player2.reset();
        Poker.buildSuitCountTracker();
        Poker.buildCardFrequencyTracker();
    }
    private static void winnerOutput(Game game, Player player){
        int combination = (player.getHandScore()-(player.getHandScore()%14))/14;
        System.out.printf("%s, you have won\n",player.getName());
        System.out.printf("You had : %s\n",Poker.getCombinationsScoreReverse().get(combination));
    }
    private static void fullGameDecision(Player player1,Player player2){
        String winnerName="";
        if (player1.getStack()==0){
            winnerName=player2.getName();
        }
        if (player2.getStack()==0){
            winnerName = player1.getName();
        }
        System.out.printf("Congrats %s. You have won the game\n",winnerName);
    }
    public static boolean checkConditionforBetting(Player player1,Player player2){
        boolean bothInGame = player1.isInGame()&&player2.isInGame();
        boolean sameLastBet = player1.getLastBet()!=player2.getLastBet();
        boolean EitherNoCheck = !player1.isCheckedLastRound()||!player2.isCheckedLastRound();
        return bothInGame&&sameLastBet&&EitherNoCheck;
    }
//    private static void checkPlayBet(Player player, Scanner scanner){}, implement this later
}
