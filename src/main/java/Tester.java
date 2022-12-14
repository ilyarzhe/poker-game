import models.Game;
import models.Player;

public class Tester {
    public static void main(String[] args) {
        Game game = new Game();
        Player player = new Player("Henry",100,true,true);
        player.bet(5,game);
        Player help = player;
        System.out.println(help.getStack());
    }
}
