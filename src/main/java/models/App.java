package models;

import components.Poker;

import java.util.ArrayList;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        Poker.buildCardValues();
        Poker.buildSuits();
        Poker.buildCombinations();
        Deck deck = new Deck();
        System.out.println(deck.getCards().size());
        System.out.println("___________");
        deck = new Deck();
        System.out.println(deck.getCards().size());


    }
}
