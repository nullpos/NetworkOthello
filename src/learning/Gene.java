package learning;

import util.Const;
import application.*;

public class Gene extends AbstructGene {
    /* 
     * 12
     * 00010203
     * 01040506
     * 02050708
     * 03060809
     * weight of diff
     * weight of putable cells
     */
    
    public Gene(int n) {
        super(n);
    }
    public Gene(Gene g) {
        super(g);
    }
    
    @Override
    public Object clone() {
        return new Gene(this);
    }
    
    @Override
    public void calcFitness(Gene g) {
        // 自分以外の相手を選んでリーグ対戦(先攻後攻？)
        // 終了時、fitnessに(自分の石の数-相手の石の数)を加算
        int comLevel = 0;

        int[][] moves = {{Const.BLACK, Const.WHITE}, {Const.WHITE, Const.BLACK}};
        for (int i = 0; i < 2; i++) {
            Othello game = new Othello();
            int now = i;
            int move = moves[i][0];
            int nmove = moves[i][1];
            
            Computer[] com = { new Computer(comLevel, this.chromosome), new Computer(comLevel, g.chromosome) };

            while(!game.isGameFinished()) {
                game.applyAction(com[now].getNextAction(game, (now == 0) ? move : nmove));
                now = (now == 0) ? 1 : 0;
                if(game.getPnum() == 0) {
                    game.applyAction(Const.PASS_STR);
                    now = (now == 0) ? 1 : 0;
                }
            }
            this.fitness += (game.getScore(move) - game.getScore(nmove));
        }
        
    }
}
