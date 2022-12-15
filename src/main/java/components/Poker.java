package components;

import java.util.ArrayList;
import java.util.HashMap;

public class Poker {
    private static HashMap<String,Integer> combinationsScore = new HashMap<>();
    private static HashMap<Integer,String> combinationsScoreReverse = new HashMap<>();
    private static ArrayList<String> suits = new ArrayList<>();
    private static HashMap<String,Integer> cardValues = new HashMap<>();
    private static HashMap<String,Integer> suitCountTracker = new HashMap<>();
    private static HashMap<String,Integer> cardFrequencyTracker = new HashMap<>();

    public static HashMap<String, Integer> getCardFrequencyTracker() {
        return cardFrequencyTracker;
    }

    // Getters
    public static HashMap<String,Integer> getCombinations(){
        return combinationsScore;
    }
    public static ArrayList<String> getSuits(){
        return suits;
    }
    public static HashMap<String, Integer> getCardValues(){
        return cardValues;
    }

    public static HashMap<String, Integer> getSuitCountTracker() {
        return suitCountTracker;
    }

    public static HashMap<Integer, String> getCombinationsScoreReverse() {
        return combinationsScoreReverse;
    }

    public static void buildCombinations(){
        ArrayList<String> combinations = new ArrayList<>();
        combinations.add("High Card");
        combinations.add("Pair");
        combinations.add("Two Pair");
        combinations.add("Three of a Kind");
        combinations.add("Straight");
        combinations.add("Flush");
        combinations.add("Full House");
        combinations.add("Quads");
        combinations.add("Straight Flush");
        for (int i = 0; i< combinations.size(); i++){
            combinationsScore.put(combinations.get(i),i);
            combinationsScoreReverse.put(i, combinations.get(i));
        }
    }
    public static void buildSuits(){
        suits.add("Hearts");
        suits.add("Diamonds");
        suits.add("Clubs");
        suits.add("Spades");
    }

    public static void buildCardValues(){
        HashMap<String,Integer> cardValuesExtra= new HashMap<>();
        for ( int i =0; i<13;i++){
            switch (i){
                case 9:
                    cardValuesExtra.put("J",i+2);
                    break;
                case 10:
                    cardValuesExtra.put("Q",i+2);
                    break;
                case 11:
                    cardValuesExtra.put("K",i+2);
                    break;
                case 12:
                    cardValuesExtra.put("A",i+2);
                    break;
                default:
                    cardValuesExtra.put(Integer.toString(i+2),i+2);
                    break;
            }
        }
        cardValues = cardValuesExtra;
    }
    public static void buildSuitCountTracker(){
        for (String key : getSuits()){
            suitCountTracker.put(key,0);
        }
    }
    public static void buildCardFrequencyTracker(){
        for(String cardName: getCardValues().keySet()){
            cardFrequencyTracker.put(cardName,0);
        }
    }


}
