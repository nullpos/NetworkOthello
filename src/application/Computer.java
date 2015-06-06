package application;

import java.util.Random;

import util.Const;

public class Computer extends Thread {
    private int level;
    private byte[] g = new byte[Const.CHROMO_NUM];
    private Client client = null;
    private int opt;
    private boolean flag = true;
    private String move;

    public Computer(int n, Client client, String move) {
        level = n;
        opt = client.getOption()[0];
        this.client = client;
        for(int i=0; i<g.length; i++) {
            g[i] = Const.GENE_ANS[n][i];
        }
        this.move = move;
    }

    public Computer(int n, Client client, byte[] chromosome, String move) {
        level = n;
        opt = Integer.MAX_VALUE;
        this.client = client;
        for(int i=0; i<g.length; i++) {
            g[i] = chromosome[i];
        }
        this.move = move;
    }
    
    public Computer(int n, byte[] chromosome) {
        level = n;
        for(int i=0; i<g.length; i++) {
            g[i] = chromosome[i];
        }
    }
    
    public void run() {
        Othello o;
        while (flag) {
            o = this.client.getGame();
            if(o.getMove().equals(this.move)) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {}
                String action = getNextAction(o, o.getIntMove());
                this.client.receiveMessage(action);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }
    }
    
    public void stopRun() {
        flag = false;
    }
    public boolean isRunning() {
        return flag;
    }
    
    public String getNextAction(Othello game, int move) {
        Othello nextGame = search(game, move);
        
        int[][] nb = nextGame.getBoard();
        int[] px = game.getPx();
        int[] py = game.getPy();
        int p = game.getPnum();
        
        for(int i=0; i < p; i++) {
            if(nb[px[i]][py[i]] == Const.BLACK || nb[px[i]][py[i]] == Const.WHITE) {
                int n = px[i] * Const.BSIZE + py[i];
                if(opt == 1) {
                    Random r = new Random();
                    if(r.nextDouble() < 0.4) {
                        n += 64;
                        opt--;
                    }
                }
                return Integer.toString(n);
            }
        }
        
        return Const.PASS_STR;
    }
    
    public Othello search(Othello game, int move) {
        int val = Integer.MIN_VALUE;
        Othello nextGame = game.clone();
        
        int[] px = game.getPx();
        int[] py = game.getPy();
        int p = game.getPnum();
        
        if(p == 0) return game;
        for (int i = 0; i < p; i++) {
            Othello o = game.clone();
            o.applyAction(Integer.toString(px[i] * Const.BSIZE + py[i]));
            int v = minMax(o.clone(), move, Const.LEVEL_DEPTH[level], val);
            
            if(v > val) {
                val = v;
                nextGame = o;
            }
        }
        
        return nextGame;
    }
    
    // TODO 完全読み、勝利読み
    public int minMax(Othello game, int move, int depth, int cut) {
        if(depth == 0) {
            return eval(game);
        }
        
        boolean isMin = (move == game.getIntMove()) ? false : true;
        int val = (isMin) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        
        int[] px = game.getPx();
        int[] py = game.getPy();
        int p = game.getPnum();
        
        if(p == 0) {
            Othello o = game.clone();
            o.applyAction(Const.PASS_STR);
            int v = minMax(o.clone(), move, depth-1, cut);
            return v;
        }
        
        for (int i = 0; i < p; i++) {
            Othello o = game.clone();
            o.applyAction(Integer.toString(px[i] * Const.BSIZE + py[i]));
            int v = minMax(o.clone(), move, depth-1, val);
            
            if(isMin) {
                if(v < val) val = v;
                if(cut > v) return v;
            } else {
                if(v > val) val = v;
                if(cut < v) return v;
            }
        }
        
        return val;
    }
    
    public int eval(Othello game) {
        int move = game.getIntMove();
        int nmove = (move == Const.BLACK) ? Const.WHITE : Const.BLACK;
        int[][] board = game.getBoard();
        int value = 0;
        int turn = game.getTurn();
        int index;
        
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                switch (board[i][j]) {
                case Const.BLACK:
                case Const.PBLACK:
                    if(turn < 20) {
                        index = Const.WEIGHT[i][j];
                    } else if(turn < 40) {
                        index = Const.WEIGHT[i][j] + 12;
                    } else {
                        index = Const.WEIGHT[i][j] + 24;
                    }
                    
                    if (move == Const.BLACK) {
                        value += g[index];
                    } else {
                        value -= g[index];
                    }
                    break;
                    
                case Const.WHITE:
                case Const.PWHITE:
                    if(turn < 20) {
                        index = Const.WEIGHT[i][j];
                    } else if(turn < 40) {
                        index = Const.WEIGHT[i][j] + 12;
                    } else {
                        index = Const.WEIGHT[i][j] + 24;
                    }
                    
                    if (move == Const.BLACK) {
                        value -= g[index];
                    } else {
                        value += g[index];
                    }
                    break;
                    
                case Const.SPACE:
                case Const.PUTABLE:
                default:
                    break;
                }
            }
        }
        

        if(turn < 20) {
            index = 10;
        } else if(turn < 40) {
            index = 10 + 12;
        } else {
            index = 10 + 24;
        }
        
        value += (game.getScore(move) - game.getScore(nmove)) * g[index];
        value += game.checkPutable(move) * g[index+1];
        return value;
    }
}