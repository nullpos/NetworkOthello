package application;

import util.Const;

public class Othello {
    private String move = null;
    private int[][] board = new int[Const.BSIZE][Const.BSIZE];
    private int[] opt = new int[Const.OPTSIZE];
    
    public Othello() {
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                board[i][j] = Const.INIT_BOARD[i][j];
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
        Othello tmp = this.clone();
        // 両者ともに置く場所がない
        if(tmp.checkPutable(Const.BLACK) == 0 &&
                tmp.checkPutable(Const.WHITE) == 0) {
            return true;
        } else {
            return false;
        }
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
    
    // 返り値はtrueならば相手のターンに移る
    public boolean applyAction(String action) {
        if(action.equals(Const.PASS_STR)) {
            switchTurn();
            checkPutable(this.getIntMove());
            //System.out.println(this.getMove()+" "+Const.PASS_STR);
            return true;
        }
        
        int iact = Integer.parseInt(action);
        boolean p;
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
        if(p) {
            board[x][y] = (move == Const.BLACK) ? Const.PBLACK: Const.PWHITE;
        } else {
            board[x][y] = move;
        }

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
    
    public boolean flip(int move, int nx, int ny, boolean b) {
        boolean flag = false;
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
                    x += dx;
                    y += dy;
                    if(x<0 || x>7 || y<0 || y>7) break;
                }
                if(x<0 || x>7 || y<0 || y>7) continue;
                
                if(board[x][y] == Const.SPACE || board[x][y] == Const.PUTABLE) {
                    continue;
                }
                
                x -= dx;
                y -= dy;
                while(x != nx || y != ny) {
                    if(b) {
                        if(!(board[x][y] == Const.PBLACK || board[x][y] == Const.PWHITE))
                            board[x][y] = move;
                        x -= dx;
                        y -= dy;
                        flag = true;
                    } else {
                        board[nx][ny] = Const.PUTABLE;
                        return true;
                    }
                }
            }
        }
        return flag;
    }
    
    public int checkPutable(int move) {
        int p = 0;
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.PUTABLE || board[i][j] == Const.SPACE) {
                    board[i][j] = Const.SPACE;
                    if(flip(move, i, j, false)) p++;
                }
            }
        }
        return p;
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

    public int getScore(int c) {
        int n=0;
        if(c == Const.BLACK) {
            for(int i=0; i<Const.BSIZE; i++) {
                for(int j=0; j<Const.BSIZE; j++) {
                    if(this.board[i][j] == Const.BLACK || this.board[i][j] == Const.PBLACK) {
                        n++;
                    }
                }
            }
        } else {
            for(int i=0; i<Const.BSIZE; i++) {
                for(int j=0; j<Const.BSIZE; j++) {
                    if(this.board[i][j] == Const.WHITE || this.board[i][j] == Const.PWHITE) {
                        n++;
                    }
                }
            }
        }
        return n;
    }
    
    public int getTurn() {
        int n = 0;
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.SPACE || board[i][j] == Const.PUTABLE)
                    continue;
                n++;
            }
        }
        return n - 4;
    }
}