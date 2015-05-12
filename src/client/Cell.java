package client;

public class Cell {
    private int x;
    private int y;
    private int state;

    public Cell() {
        this.x = 0;
        this.y = 0;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Cell(int x, int y, int state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }
    
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}