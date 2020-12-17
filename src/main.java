import game.YahtzeeFrame;
import server.SaveServer;

public class main {
    public static void main(String[] args) {
        YahtzeeFrame yahtzeeFrame = new YahtzeeFrame();
        yahtzeeFrame.setAlwaysOnTop(true);
        SaveServer saveServer = new SaveServer();
    }
}
