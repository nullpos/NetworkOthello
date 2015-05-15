package application;

public class Player {
    private String name;
    private String move;
    
    public Player() {
        
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setMove(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}