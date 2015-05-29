package test;

import util.Const;
import application.Computer;
import application.Othello;

public class ComputerDriver {
    public static void main(String[] args) throws Exception {
        Othello game = new Othello();
        for (int i = 0; i < Const.LEVEL_DEPTH.length; i++) {
            Computer cpu = new Computer(i);
            System.out.println("レベル"+(i+1)+" evalの結果を表示:" + cpu.eval(game));
            System.out.println("レベル"+(i+1)+" getNextActionの結果を表示:" + cpu.getNextAction(game));
        }
    }
}