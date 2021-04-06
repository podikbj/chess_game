
package cz.cvut.fel.pjv.chessgame;

public class Player {
    
    private String name;
    private int color;

    public Player(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int isColor() {
        return color;
    }
    
    
    
}
