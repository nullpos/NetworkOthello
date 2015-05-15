package application;

import util.Const;

public class Computer {
    private int level;
    
    public Computer(int n) {
        level = n;
    }
    
    public String getNextMove(int[][] board) {
        String next = "";
        
        int[][] nboard = search(board, Const.LEVEL_DEPTH[level]);
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(nboard[i][j] - board[i][j] != 0) {
                    
                }
            }
        }
        
        return next;
    }
    
    public int[][] search(int[][] board, int depth) {
        return board;
    }
}