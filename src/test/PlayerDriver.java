package test;

import util.Const;
import application.Player;

public class PlayerDriver {
    public static void main(String[] args) throws Exception {
      Player player = new Player();
      System.out.println("setNameで「電情太郎」を入力します");
      player.setName("電情太郎");
      System.out.println("getName出力: " + player.getName());
      System.out.println("setNameで「"+Const.BLACK_STR+"」を入力します");
      player.setMove(Const.BLACK_STR);
      System.out.println("getColor出力: " + player.getMove());
    }
}