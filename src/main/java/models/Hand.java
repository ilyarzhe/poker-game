package models;

import models.Card;

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
    @Override
    public String toString(){
        return String.format("%s %s,%s %s",this.card1.getCardName(),this.card1.getSuit(),this.card2.getCardName(),this.card2.getSuit());
    }
}

