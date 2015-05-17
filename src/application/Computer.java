package application;

import util.Const;

public class Computer {
    private int level;
    
    public Computer(int n) {
        level = n;
    }
    
    public String getNextAction(Othello game) {
        String next = "";
        int[][] board = game.getBoard();
        
        int[][] nboard = search(game, Const.LEVEL_DEPTH[level]);
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(nboard[i][j] - board[i][j] != 0) {
                    
                }
            }
        }
        
        return next;
    }
    
    public int[][] search(Othello game, int depth) {
        int move = Const.WHITE; // TODO
        boolean isMini = (depth % 2 == 0) ? false : true;
        
        game.checkPutable(move);
        int[][] board = game.getBoard();

        int[] px = new int[Const.BSIZE * Const.BSIZE];
        int[] py = new int[Const.BSIZE * Const.BSIZE];
        int p = 0;
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.PUTABLE) {
                    px[i*j] = i;
                    py[i*j] = j;
                    p++;
                }
            }
        }
        
        for (int i = 0; i < p; i++) {
            Othello o = game.clone();
            o.applyAction(Integer.toString(px[i] * py[i]));
            int value = eval(game);
            //TODO
        }
        
        return board;
    }
    
    public int eval(Othello game) {
        byte[] g = {1,2,3,4,5,6,7,8,9,10};
        int move = (game.getMove().equals(Const.BLACK)) ? Const.BLACK : Const.WHITE;
        int[][] board = game.getBoard();
        int value = 0;
        
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                switch (board[i][j]) {
                case Const.BLACK:
                case Const.PBLACK:
                    if (move == Const.BLACK) {
                        value += g[Const.WEIGHT[i][j]];
                    } else {
                        value -= g[Const.WEIGHT[i][j]];
                    }
                    break;
                    
                case Const.WHITE:
                case Const.PWHITE:
                    if (move == Const.BLACK) {
                        value -= g[Const.WEIGHT[i][j]];
                    } else {
                        value += g[Const.WEIGHT[i][j]];
                    }
                    break;
                    
                case Const.SPACE:
                case Const.PUTABLE:
                default:
                    break;
                }
            }
        }
        return value;
    }
}