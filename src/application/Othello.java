package application;

import util.Const;

public class Othello {
    String move = null;
    int[][] board = new int[Const.BSIZE][Const.BSIZE];
    int[] opt = new int[Const.OPTSIZE];
    
    public Othello() {
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if((i==3 && j==4) || (i==4 && j==3)) { board[i][j] = Const.WHITE; break;}
                if((i==3 && j==3) || (i==4 && j==4)) { board[i][j] = Const.BLACK; break; }
                board[i][j] = Const.SPACE;
            }
        }
    }
    
    public String whichIsWinner() {
        int n=0;
        int tmp;
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                tmp = board[i][j];
                if(tmp == Const.BLACK || tmp == Const.PBLACK) {
                    n++;
                } else if(tmp == Const.WHITE || tmp == Const.PWHITE) {
                    n--;
                } else {
                    return "ERROR";
                }
            }
        }
        
        if(n == 0) {
            return Const.DRAW_STR;
        } else if(n > 0) {
            return Const.BLACK_STR;
        } else if(n < 0) {
            return Const.WHITE_STR;
        } else {
            return "ERROR";
        }
    }
    
    public boolean isGameFinished() {
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public String getMove() {
        return this.move;
    }
    
    public int[][] getBoard() {
        return this.board;
    }
    
    public void setMove(String move) {
        this.move = move;
    }
    
    public void applyMove(String move) {
        
    }
}