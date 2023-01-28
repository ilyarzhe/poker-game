import components.Actions;
import components.Poker;
import models.Game;
import models.Player;

import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        startGame();
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
//      two lines below are removed for testing purposes
//        Player player1 = generatePlayer(scanner,1);
//        Player player2 = generatePlayer(scanner, 2);
        Player player1 = new Player("Henry",100,true,true);
        Player player2 = new Player("Slava",90,true,false);

        while (player1.getStack()>0&&player2.getStack()>0) {
            player1.drawHand(game);
            player2.drawHand(game);
            int roundCount = 1;
            while (roundCount < 5) {
                System.out.println("This round:"+roundCount);
                if (!player1.isAllIn()&&!player2.isAllIn()) {
                    startRoundOfBetting(player1, player2, game, scanner, roundCount);
                }
                if (player1.isInGame() && player2.isInGame()) {
                    game.generateTable(roundCount);
                    System.out.println(game.getCardTable());
                    roundCount++;
                } else {
                    break;
                }

            }
            decideWinner(game, player1, player2);
            gameReset(game, player1, player2);
        }
        decideEntireGameWinner(player1,player2);



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
    private static void startRoundOfBetting(Player player1,Player player2, Game game,Scanner scanner,int roundNumber) throws Exception {
        Player tempPlayer1;
        Player tempPlayer2;
        if ((player1.isUnderTheGun()&&roundNumber%2==1)||(!player1.isUnderTheGun()&&roundNumber%2==0)){
            tempPlayer1 = player1;
            tempPlayer2 = player2;
        } else {
            tempPlayer2=player1;
            tempPlayer1=player2;
        }
        starterRoundOfAction(game,tempPlayer1,scanner,roundNumber,tempPlayer2);
        do {
            responseAction(game, tempPlayer2, tempPlayer1, scanner,roundNumber);
            Player extra = tempPlayer1;
            tempPlayer1 = tempPlayer2;
            tempPlayer2 = extra;
        } while(checkConditionContinue(tempPlayer1,tempPlayer2));
        if (Objects.equals(tempPlayer1.getName(), player1.getName())){
            player1=tempPlayer1;
            player2=tempPlayer2;
        } else {
            player1 = tempPlayer2;
            player2 = tempPlayer1;
        }
        player1.setLastBet(0);
        player2.setLastBet(0);
        System.out.printf("The pot is %s\n",game.getPot());
        System.out.println("-".repeat(10));
    }
    private static void starterRoundOfAction(Game game,Player player,Scanner scanner,int roundNumber,Player opponent){
        displayUI(game,player, roundNumber>1);
        System.out.println("(bet,fold,check,allin):");
        String action = scanner.nextLine();
        checkPlayBet(game,player,action,scanner);
        checkPlayCheck(player,action);
        checkPlayFold(player,action);
        checkPlayAllIn(game,player,action,opponent);
    }
    private static void responseAction(Game game, Player you,Player opponent,Scanner scanner,int roundNumber) throws Exception {
        // check what was previous player's action
        // construct a response function based on the action
        if (opponent.isAllIn()){
            allInResponse(game,you,opponent,scanner,roundNumber);
        }
        else if (opponent.isCheckedLastRound()){
            checkResponse(game,you,opponent,scanner,roundNumber);
        }
        else if (!opponent.isInGame()){
            foldResponse(game,you,opponent);
        }
        else if (opponent.getLastBet()>0){
            betResponse(game,you,opponent,scanner,roundNumber);
        }
    }

    private static void allInResponse(Game game, Player you,Player opponent,Scanner scanner,int roundNumber) throws Exception {
        System.out.printf("%s is all in! The amount to call is %s!\n",opponent.getName(),opponent.getLastBet());
        displayUI(game,you, roundNumber>1);
        System.out.println("(call or fold):");
        String action = scanner.nextLine();
        checkPlayCall(game,you,opponent,action);
        checkPlayFold(you,action);
    }
    private static void checkResponse(Game game, Player you, Player opponent,Scanner scanner, int roundNumber){
        System.out.printf("%s has checked!\n",opponent.getName());
        displayUI(game,you, roundNumber>1);
        System.out.println("(bet,fold,check or allin)");
        String action = scanner.nextLine();
        checkPlayAllIn(game,you,action,opponent);
        checkPlayFold(you,action);
        checkPlayBet(game,you,action,scanner);
        checkPlayCheck(you,action);
    }
    private static void foldResponse(Game game,Player you,Player opponent){
        System.out.printf("%s has folded!, You won\n",opponent.getName());
        you.winHand(game);
        gameReset(game,you,opponent);

    }
    private static void betResponse(Game game, Player you, Player opponent,Scanner scanner,int roundNumber) throws Exception {
        System.out.printf("%s put in a %d bet!\n",opponent.getName(),opponent.getLastBet());
        displayUI(game,you, roundNumber>1);
        System.out.println("(call, raise,allin or fold):");
        String action = scanner.nextLine();
        checkPlayCall(game,you,opponent,action);
        checkPlayRaise(game,you,action,scanner,opponent);
        checkPlayAllIn(game,you,action,opponent);
        checkPlayFold(you,action);
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
    private static void displayUI(Game game, Player player, boolean flop) {
        System.out.println("-".repeat(10));
        System.out.printf("%s, your Turn!\n",player.getName());
        if (flop) {
            System.out.println("\nCommunity Cards:");
            System.out.println(game.getCardTable());
        }
        System.out.println("\nYour Hand:");
        System.out.println(player.getHand().toString()+"\n");
        System.out.println("Your Stack:");
        System.out.println(player.getStack()+"\n");
        System.out.println("Your turn for action!");
    }
    private static void checkPlayBet(Game game,Player player, String action,Scanner scanner){
        if (action.equalsIgnoreCase(Actions.BET.toString())){
            boolean betGoneThrough = false;
            while(!betGoneThrough) {
                try {
                    int betSize = inputNumber(scanner);
                    player.bet(betSize, game);
                    betGoneThrough=true;
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    private static void checkPlayFold(Player player, String action){
        if (action.equalsIgnoreCase(Actions.FOLD.toString())){
            player.fold();
        }
    }
    private static void checkPlayRaise(Game game,Player player, String action, Scanner scanner, Player opponent){
        if (action.equalsIgnoreCase(Actions.RAISE.toString())){
            boolean betGoneThrough = false;
            while(!betGoneThrough) {
                try {
                    int betSize = inputNumber(scanner);
                    player.bet(betSize,opponent.getLastBet(), game);
                    betGoneThrough=true;
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    private static void checkPlayCheck(Player player, String action){
        if (action.equalsIgnoreCase(Actions.CHECK.toString())){
            player.check();
        }
    }
    private static void checkPlayAllIn(Game game,Player player, String action, Player opponent){
        if (action.equalsIgnoreCase(Actions.ALLIN.toString())){
            player.allIn(game,opponent.getStack(),opponent.getLastBet());
        }
    }
    private static void checkPlayCall(Game game,Player player,Player opponent, String action) throws Exception {
        if (action.equalsIgnoreCase(Actions.CALL.toString())){
            player.call(game,opponent.getLastBet(), player.getLastBet());
        }
    }
    private static void decideWinner(Game game, Player player1, Player player2){
        int player1Score = player1.getHandScoreFromTable(game);
        int player2Score = player2.getHandScoreFromTable(game);
        System.out.println(player1Score);
        System.out.println(player2Score);
        System.out.println("-".repeat(10));
        if (player1Score>player2Score||!player2.isInGame()){
            player1.winHand(game);
            winnerOutput(player1);
        } else if (player2Score>player1Score||!player1.isInGame()) {
            player2.winHand(game);
            winnerOutput(player2);
        } else if (player1Score==player2Score){
            game.tie(player1,player2);
            System.out.println("It's a tie. Pot is split!");
        }
    }
    private static void gameReset(Game game, Player  player1, Player player2){
        game.reset();
        player1.reset();
        player2.reset();
        Poker.buildSuitCountTracker();
        Poker.buildCardFrequencyTracker();
    }
    private static void winnerOutput(Player player){
        int combination = (player.getHandScore()-(player.getHandScore()%14))/14;
        System.out.printf("%s, you have won\n",player.getName());
        System.out.printf("You had : %s\n",Poker.getCombinationsScoreReverse().get(combination));
    }

    public static boolean checkConditionContinue(Player player1, Player player2){
        boolean bothInGame = player1.isInGame()&&player2.isInGame();
        boolean differentLastBet = player1.getLastBet()!=player2.getLastBet();
        boolean EitherNoCheck = !player1.isCheckedLastRound()||!player2.isCheckedLastRound();
        boolean bothNotAllIn=!(player1.isAllIn()&&player2.isAllIn());
        return bothInGame&&differentLastBet&&EitherNoCheck&&bothNotAllIn;
    }
    private static void decideEntireGameWinner(Player player1, Player player2){
        Player winner = player1.getStack()==0?player2:player1;
        System.out.println("The Game ended!");
        System.out.printf("Congratulations, %s, you won the Game!",winner.getName());
    }
}
