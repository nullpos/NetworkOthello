package application;

import util.Const;

public class Computer {
    private int level;
    
    public Computer(int n) {
        level = n;
    }
    
    public String getNextAction(Othello game) {
        Othello nextGame = search(game);

        int[][] b = game.getBoard();
        int[][] nb = nextGame.getBoard();
        
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(b[i][j] != Const.PUTABLE) continue;
                if(nb[i][j] == Const.BLACK || nb[i][j] == Const.WHITE) {
                    System.out.println("("+i+","+j+")");
                    return Integer.toString(i * Const.BSIZE + j);
                }
            }
        }
        
        return Const.PASS_STR;
    }
    
    public Othello search(Othello game) {
        int[][] board = game.getBoard();
        int val = Integer.MIN_VALUE;
        Othello nextGame = game.clone();

        // おける場所を探す
        int[] px = new int[Const.BSIZE * Const.BSIZE];
        int[] py = new int[Const.BSIZE * Const.BSIZE];
        int p = 0;
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.PUTABLE) {
                    px[p] = i;
                    py[p] = j;
                    p++;
                }
            }
        }
        if(p == 0) return game;
        for (int i = 0; i < p; i++) {
            Othello o = game.clone();
            o.applyAction(Integer.toString(px[i] * Const.BSIZE + py[i]));
            int v = minMax(o.clone(), Const.LEVEL_DEPTH[level]);
            
            if(v > val) {
                val = v;
                nextGame = o;
            }
        }
        
        return nextGame;
    }
    
    public int minMax(Othello game, int depth) {
        if(depth == 0) return eval(game);
        
        boolean isMin = (depth % 2 == 0) ? true : false;
        int[][] board = game.getBoard();
        int val = (isMin) ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        // おける場所を探す
        int[] px = new int[Const.BSIZE * Const.BSIZE];
        int[] py = new int[Const.BSIZE * Const.BSIZE];
        int p = 0;
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.PUTABLE) {
                    px[p] = i;
                    py[p] = j;
                    p++;
                }
            }
        }
        
        if(p == 0) return eval(game);
        for (int i = 0; i < p; i++) {
            Othello o = game.clone();
            o.applyAction(Integer.toString(px[i] * Const.BSIZE + py[i]));
            int v = minMax(o.clone(), depth-1);
            
            if(isMin) {
                if(v < val) {
                    val = v;
                }
            } else {
                if(v > val) {
                    val = v;
                }
            }
        }
        return val;
    }
    
    public int eval(Othello game) {
        byte[] g = {1,2,3,4,5,6,7,8,9,10}; // TODO
        int move = game.getIntMove();
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