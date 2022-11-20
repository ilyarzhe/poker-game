import java.util.ArrayList;
import java.util.HashMap;

public class Poker {
    private static HashMap<String,Integer> combinationsScore = new HashMap<>();
    private static ArrayList<String> suits = new ArrayList<>();
    private static HashMap<String,Integer> cardValues = new HashMap<>();

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
        }
    }
    public static void buildSuits(){
        suits.add("Hearts");
        suits.add("Diamonds");
        suits.add("Clubs");
        suits.add("Spades");
    }

    public static void buildCardValues(){
        for ( int i =0; i<13;i++){
            switch (i){
                case 9:
                    cardValues.put("J",i+2);
                    break;
                case 10:
                    cardValues.put("Q",i+2);
                    break;
                case 11:
                    cardValues.put("K",i+2);
                    break;
                case 12:
                    cardValues.put("A",i+2);
                    break;
                default:
                    cardValues.put(Integer.toString(i+2),i+2);
                    break;
            }
        }
    }



}
