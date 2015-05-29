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
        Othello game = new Othello();
        Computer[] com = { new Computer(comLevel, this.chromosome), new Computer(comLevel, g.chromosome) };

        // 先攻
        int move = Const.BLACK;
        int nmove = Const.WHITE;
        int i = 0;
        
        while(!game.isGameFinished()) {
            game.applyAction(com[i].getNextAction(game));
            i = (i == 0) ? 1 : 0;
            if(game.checkPutable(game.getIntMove()) == 0) {
                game.applyAction(Const.PASS_STR);
                i = (i == 0) ? 1 : 0;
            }
        }
        this.fitness += (game.getScore(move) - game.getScore(nmove));
        
        // 後攻
        move = Const.WHITE;
        nmove = Const.BLACK;
        i = 1;
        
        game = new Othello();
        while(!game.isGameFinished()) {
            game.applyAction(com[i].getNextAction(game));
            i = (i == 0) ? 1 : 0;
            if(game.checkPutable(game.getIntMove()) == 0) {
                game.applyAction(Const.PASS_STR);
                i = (i == 0) ? 1 : 0;
            }
        }
        this.fitness += (game.getScore(move) - game.getScore(nmove));
    }
}
