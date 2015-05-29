package test;

import java.io.*;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import util.Const;
import util.Images;
import application.*;

public class ClientDriver {
    public static void main(String[] args) throws Exception {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in), 1);
        Player player = new Player(); //プレイヤオブジェクトの用意
        player.setName("test user"); //名前を受付
        Othello game = new Othello(); //オセロオブジェクトを用意
        
        Images images = new Images();
        try {
            ResourceBundle rb = ResourceBundle.getBundle(Const.SETTINGS_NAME);
            images.loadImages(rb.getString("Preset"));
        } catch (MissingResourceException e) {
            JOptionPane.showMessageDialog(null, "Please check "+Const.SETTINGS_NAME+".propaties." + e, "Failed to load images.", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-1);
        }
        
        Client oclient = new Client(game, player, images);
        oclient.setVisible(true);
        
        System.out.println("テスト用サーバに接続します");
        oclient.connectServer("localhost", 10000);
        System.out.println("受信用テストメッセージを入力してください");
        while(true){
            String s = r.readLine();
            oclient.receiveMessage(s);
            System.out.println("テストメッセージ「" + s + "」を受信しました");
            System.out.println("テスト操作を行った後、受信用テストメッセージを入力してください");
        }
    }
}