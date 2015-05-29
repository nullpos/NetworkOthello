package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import util.Const;
import application.Othello;

public class OthelloDriver {
    public static void main(String[] args) throws Exception{
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in), 1);
        Othello game = new Othello();
        
        System.out.println("テスト１：Othelloクラスのオブジェクトを初期化した結果：");
        printStatus(game);
        game.print();
        
        while(true){
            System.out.println("石を置く場所(数字またはpass)をキーボードで入力してください。");
            System.out.println("不動の駒を使う際は、64を足した数字を入力してください。");
            String s = r.readLine();//文字列の入力
            System.out.println(s + " が入力されました。手番は " + game.getTurn() + " です。");
            game.applyAction(s);
            printStatus(game);
            game.print();
        }
    }
    
    public static void printStatus(Othello game) {
        System.out.println("checkPutable出力:" + game.checkPutable(game.getIntMove()));
        System.out.println("getIntMove出力:" + game.getIntMove());
        System.out.println("getMove出力:" + game.getMove());
        System.out.println("getScore出力(BLACK):" + game.getScore(Const.BLACK));
        System.out.println("getScore出力(WHITE):" + game.getScore(Const.WHITE));
        System.out.println("getTurn出力:" + game.getTurn());
        System.out.println("isGameFinished出力:" + game.isGameFinished());
    }
}