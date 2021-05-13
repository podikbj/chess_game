package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;

/**
 * Represents a chess board tile
 * @author kira
 */
public class Tile {

    private final int color; // 1 - white; 0 - black
    private final int x;
    private final int y;
    private Piece currentPiece = null;
    private boolean isEmpty = true;
    private HashMap<Integer, String> chMap = new HashMap<>();

    /**
     * Constructor for Tile instance
     * @param color tile color
     * @param x horizontal tile coordinate
     * @param y vertical tile coordinate
     */
    public Tile(int color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
        isEmpty = true;
    }

//    /**
//     *
//     * @param color
//     * @return
//     */
//    protected boolean isOnStrike(int color) {
//        boolean b = false;
//
//        GameManager gameManager = GameManager.getInstance();
//        List<Piece> pieces = gameManager.getPieces();
//
//        if (pieces.size() <= 0) {
//            return b;
//        }
//
//        for (Piece p : pieces) {
//            b = p.isMoveAllowed(this);
//            if (!b) {
//                return b;
//            }
//        }
//        return b;
//    }

    /**
     * Getter for tile color
     * @return
     */
    public int getColor() {
        return color;
    }

    /**
     * Getter for horizontal tile coordinate
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for vertical tile coordinate
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Getter for current piece
     * @return piece is placed on this tile
     */
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    /**
     *
     * @return true if no one piece is placed on this tile
     */
    public boolean getIsEmpty() {
        return isEmpty;
    }

    /**
     * Remove piece from tile
     */
    public void removePiece() {
        this.currentPiece = null;
        isEmpty = true;

    }

    /**
     * Place piece on tile
     * @param currentPiece piece placing on tile
     */
    public void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
        isEmpty = false;
    }

    /**
     * Return tile coordinate in a pgn notation 
     * @return String tile coordinate
     */
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

    /**
     * Sets true if tile is empty, false if not
     * @param isEmpty 
     */
    protected void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

}
