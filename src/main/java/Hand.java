public class Hand {
    private Card card1;
    private Card card2;

    public Hand(Card card1,Card card2){
        this.card1 = card1;
        this.card2 = card2;
    }
    public Card getCard1(){
        return this.card1;
    }
    public Card getCard2(){
        return this.card2;
    }
}
