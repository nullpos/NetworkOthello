package application;

import util.Const;

public class Othello {
    private String move = null;
    private int[][] board = new int[Const.BSIZE][Const.BSIZE];
    private int[] opt = new int[Const.OPTSIZE];
    
    public Othello() {
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if((i==3 && j==4) || (i==4 && j==3)) { board[i][j] = Const.WHITE; continue;}
                if((i==3 && j==3) || (i==4 && j==4)) { board[i][j] = Const.BLACK; continue;}
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
                if(board[i][j] == Const.SPACE) {
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
    
    public boolean applyAction(String action) {
        int iact = Integer.parseInt(action);
        boolean p; // TODO PBLACK
        if(iact > 63) {
            p = true;
            iact -= 64;
        } else {
            p = false;
        }
        
        int x,y;
        x = iact / Const.BSIZE;
        y = iact % Const.BSIZE;
        int move = this.getIntMove();
        
        if(board[x][y] != Const.PUTABLE) {System.err.println("action: " + action + " error"); return false;}
        board[x][y] = move;

        flip(move, x, y, true);
        switchTurn();
        checkPutable(this.getIntMove());
        return true;
    }
    
    public int getIntMove() {
        return (this.move.equals(Const.BLACK_STR)) ? Const.BLACK : Const.WHITE;
    }
    
    public void switchTurn() {
        this.move = (this.move.equals(Const.BLACK_STR)) ? Const.WHITE_STR : Const.BLACK_STR;
    }
    
    public void flip(int move, int nx, int ny, boolean b) {
        for(int i=-1; i<2; i++) {
            for(int j=-1; j<2; j++) {
                if(i==0 && j==0) continue;

                int dx = i;
                int dy = j;
                int nmove = (move == Const.BLACK) ? Const.WHITE : Const.BLACK;
                int x,y;
                
                x = nx + dx;
                y = ny + dy;
                if(x<0 || x>7 || y<0 || y>7) continue;
                while(board[x][y] == nmove) {
                    if(x==0 || x==7 || y==0 || y==7) break;
                    x += dx;
                    y += dy;
                }
                
                if(board[x][y] == Const.SPACE || board[x][y] == Const.PUTABLE) {
                    continue;
                }
                
                x -= dx;
                y -= dy;
                while(x != nx || y != ny) {
                    if(b) {
                        board[x][y] = move;
                        x -= dx;
                        y -= dy;
                    } else {
                        board[nx][ny] = Const.PUTABLE;
                        return;
                    }
                }
            }
        }
    }
    
    public void checkPutable(int move) {
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.PUTABLE || board[i][j] == Const.SPACE) {
                    board[i][j] = Const.SPACE;
                    flip(move, i, j, false);
                }
            }
        }
    }
    
    public Othello clone() {
        Othello o = new Othello();
        int[][] b = o.getBoard();
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                b[i][j] = this.board[i][j];
            }
        }
        o.move = this.move;
        for(int i=0; i<Const.OPTSIZE; i++) {
            o.opt[i] = this.opt[i];
        }
        return o;
    }
    
    public void print() {
        int[][] board = this.getBoard();
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                System.out.print(Const.PRINT_BOARD[board[i][j]]);
            }
            System.out.println();
        }
    }
}