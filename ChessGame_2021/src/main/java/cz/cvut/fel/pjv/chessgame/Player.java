
package cz.cvut.fel.pjv.chessgame;

public class Player {
    
    private String name;
    private boolean color;

    public Player(String name, boolean color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public boolean isColor() {
        return color;
    }
    
    
    
}
