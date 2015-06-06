package learning;

import java.util.ResourceBundle;

import application.Client;
import application.Computer;
import application.Othello;
import application.Player;
import util.Const;
import util.Images;

public class View {
    private static final byte[][] genes = {
        {-103, -104, -99, -81, -94, -58, 21, -49, 85, 42, -104, 79, 125, -8, -14, 74, -120, -66, -8, 25, 74, 24, -73, 47, 99, 104, -28, -5, -20, -120, -113, -5, -75, 2, 74, 126},
        {22, -35, 56, -99, -118, -13, 50, -74, 25, 75, -76, 32, -99, -75, -16, 98, -109, -19, 38, 80, 127, 119, -118, 27, 89, 105, 11, -15, -64, -109, -107, -56, -106, -70, 122, 108}
    };
    
    public static void main(String[] args) {
        int comLevel = 0;
        Images images = new Images();
        try {
            ResourceBundle rb = ResourceBundle.getBundle(Const.SETTINGS_NAME);
            images.loadImages(rb.getString("Preset"));
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-1);
        }
        
        Othello game = new Othello();
        Player player1 = new Player();
        player1.setMove(Const.BLACK_STR);
        player1.setName(genes[0].toString());
        Player player2 = new Player();
        player2.setMove(Const.WHITE_STR);
        player2.setName(genes[1].toString());
        Client client = new Client(game, player1, images);
        Computer[] com = { new Computer(comLevel, client, genes[0], player1.getMove()), new Computer(comLevel, client, genes[1], player2.getMove()) };

        client.receiveMessage(Const.PLAYER_NAME_ID + player2.getName());
        
        Thread[] comThreads = {new Thread(com[0]), new Thread(com[1])};
        comThreads[0].start();
        comThreads[1].start();
        client.setVisible(true);
        while(!game.isGameFinished()){
            
        }

        if(com[0].isRunning())com[0].stopRun();
        if(com[1].isRunning())com[1].stopRun();
    }
}