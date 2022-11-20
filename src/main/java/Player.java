public class Player {
    private String name;
    private Hand hand;
    private int stack;
    private boolean inGame;
    private int handScore;
    public Player(String name,
                  int stack,
                  boolean inGame
                  ){
        this.name = name;
        this.stack = stack;
        this.inGame = inGame;
    }
    public String getName(){
        return this.name;
    }
    public Hand getHand(){
        return this.hand;
    }
    public int getStack(){
        return this.stack;
    }
    public boolean isInGame(){
        return this.inGame;
    }
    public int getHandScore(){
        return this.handScore;
    }
    public void setHand(Hand hand){
        this.hand = hand;
    }
    public void setStack(int amount){
        this.stack = amount;
    }
    public void setInGame(boolean inGame){
        this.inGame = inGame;
    }
    public void setHandScore(int score){
        this.handScore = score;
    }
}
