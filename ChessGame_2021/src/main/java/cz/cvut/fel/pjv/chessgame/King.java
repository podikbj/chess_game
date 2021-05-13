package cz.cvut.fel.pjv.chessgame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Represents a King piece
 *
 * @author kira
 */
public class King extends Piece {

    /**
     * Constructor for King.
     *
     * @param color piece color
     * @param tile current tile
     * @param blackPiecePath path to black piece picture file
     * @param whitePiecePath path to white piece picture file
     * @param wasRemoved true if the piece was removed from the chess board
     */
    public King(int color, Tile tile, boolean wasRemoved) {
        super(color, tile, "src/main/resources/bking.png",
                "src/main/resources/wking.png", wasRemoved);
    }

    /**
     * Checks if the piece is allowed to move to finish tile
     *
     * @param finTile current tag finish tile
     * @return true if move is allowed by chess rules
     */
    @Override
    public boolean isMoveAllowed(Tile finTile) {

        boolean b = false;
        if (super.isTheSameColor(finTile)) {
            return b;
        }
        //check position for another king, they can not be in adjacent tiles
        if (!finTile.getIsEmpty() && finTile.getCurrentPiece().toString().equals("K")) {
            return b;
        }

        int diffX = finTile.getX() - currentTile.getX();
        int diffY = finTile.getY() - currentTile.getY();

        if (Math.abs(diffX) > 1 || Math.abs(diffY) > 1) {
            return b;
        }

//        if (finTile.isOnStrike(this.color)) {
//            return b;
//        }

        boolean areOccupiedTiles = true;

        if (isHorizontalMove(finTile)) {
            areOccupiedTiles = isAnyTileIsOccupiedHorizontalMove(finTile);
        }

        if (isVerticalMove(finTile)) {
            areOccupiedTiles = isAnyTileIsOccupiedVerticalMove(finTile);
        }

        if (isDiagonalMove(finTile)) {
            areOccupiedTiles = isAnyTileIsOccupiedDiagonalMove(finTile);
        }
        b = !areOccupiedTiles;
        return b;
    }

    /**
     *
     * @param finTile
     * @param tags
     * @param removedPieces
     * @return
     */
    @Override
    public boolean move(Tile finTile, HashSet<String> tags, List<Piece> removedPieces) {
        boolean b = super.move(finTile, tags, removedPieces);
        wasMoved = true;
        return b;
    }

    /**
     * Returns string that includes instance name
     *
     * @return string that includes unique piece name
     */
    @Override
    public String toString() {
        return "K";
    }

    /**
     * Gets intermediate tiles between current tile and finish tile
     *
     * @param finTile current tag finish tile
     * @return null
     */
    @Override
    public List<Tile> getIntermediateTiles(Tile finTile) {
        List<Tile> tempList = null;

        return tempList;
    }
}
