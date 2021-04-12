package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Tile {

    private final int color; // 1 - white; 0 - black
    private final int x;
    private final int y;
    private Piece currentPiece = null;
    private boolean isEmpty = true;
    private HashMap<Integer, String> chMap = new HashMap<>();

    public Tile(int color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
        isEmpty = true;
    }

    public boolean isOnStrike(int color) {
        boolean b = false;
        LinkedList<Piece> pieces = null;
        GameManager gameManager = GameManager.getInstance();

        pieces = gameManager.getPieces(color);
        
        if (pieces == null) { return b; }
        
        for (Piece p : pieces) {
            b = p.isMoveAllowed(this);
            if (!b) {
                return b;
            }
        }
        return b;
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

    public void removePiece() {
        this.currentPiece = null;
        isEmpty = true;

    }

    public void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
        isEmpty = false;
    }

    public String coordinatesString() {
        chMap.clear();
        chMap.put(4, "a");
        chMap.put(5, "b");
        chMap.put(6, "c");
        chMap.put(7, "d");
        chMap.put(8, "e");
        chMap.put(9, "f");
        chMap.put(10, "g");
        chMap.put(11, "h");

        return chMap.get(x) + Integer.toString(y + 1);
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
    
    

}
