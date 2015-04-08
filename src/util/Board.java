package util;

public class Board {
    final private int N = 8;
    private int[][] board = new int[N][N];

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Board() {
        this.init();
    }
    
    public Board(int[][] b) {
        setBoard(b);
    }
    
    public void init() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
                    this.setStone(i, j, Const.WHITE);
                } else if ((i == 3 && j == 4) || (i == 3 && j == 4)) {
                    this.setStone(i, j, Const.BLACK);
                } else {
                    this.setStone(i, j, Const.SPACE);
                }
            }
        }
    }
    
    public void setStone(int x, int y, int c) {
        board[x][y] = c;
    }
}