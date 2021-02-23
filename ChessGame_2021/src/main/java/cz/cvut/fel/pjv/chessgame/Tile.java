package cz.cvut.fel.pjv.chessgame;

import java.util.HashMap;
import java.util.Map;

public class Tile {

    private final int color; // 1 - white; 0 - black
    private final int x;
    private final int y;
    private Piece currentPiece = null;
    private boolean isEmpty = true;
    private HashMap<Integer, String> chMap = new HashMap<>();
    
//    public Tile(Piece currentPiece, int color, int x, int y) {
//        this.currentPiece = this.currentPiece;
//        this.color = color;
//        this.x = x;
//        this.y = y;
//        this.isEmpty = false;
//                
//    }

    public Tile(int color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
        isEmpty = true;
    }

    public int getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public boolean getIsEmpty() {
        return isEmpty;
    }

    public void removePiece(Piece piece) {
        this.currentPiece = null;
    }

    public void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
        isEmpty = false;
    }

    public String coordinatesString() {
        chMap.clear();
        chMap.put(1, "a");
        chMap.put(2, "b");
        chMap.put(3, "c");
        chMap.put(4, "d");
        chMap.put(5, "e");
        chMap.put(6, "f");
        chMap.put(7, "g");
        chMap.put(8, "h");
           
        return chMap.get(x) + Integer.toString(y);
    }

}
