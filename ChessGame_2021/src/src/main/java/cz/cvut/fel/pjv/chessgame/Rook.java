package cz.cvut.fel.pjv.chessgame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Represents a Rook piece
 * @author kira
 */
public class Rook extends Piece {

    /**
     * Constructor for Rook.
     * @param color piece color
     * @param tile current tile
     * @param blackPiecePath path to black piece picture file
     * @param whitePiecePath path to white piece picture file
     * @param wasRemoved true if the piece was removed from the chess board
     */
    public Rook(int color, Tile tile, boolean wasRemoved) {
        super(color, tile, "src/main/resources/brook.png",
                "src/main/resources/wrook.png", wasRemoved);
    }

    /**
     * Checks if the piece is allowed to move to finish tile
     * @param finTile current tag finish tile
     * @return true if move is allowed by chess rules
     */
    @Override
    public boolean isMoveAllowed(Tile finTile) {
        boolean b = false;
        if (super.isTheSameColor(finTile)) {
            return b;
        }
        boolean areOccupiedTiles = true;

        if (isHorizontalMove(finTile)) {
            areOccupiedTiles = isAnyTileIsOccupiedHorizontalMove(finTile);
        }

        if (isVerticalMove(finTile)) {
            areOccupiedTiles = isAnyTileIsOccupiedVerticalMove(finTile);
        }

        b = !areOccupiedTiles;
        return b;
    }

    /**
     * Move piece to finish tile. Marks captured piece as Removed. Adds captured piece to list
     *
     * @param finTile current tag finish tile
     * @param tags set of special string symbols like "+, x, 0-0, 0-0-0"
     * @param lastRemoved list of removed pieces
     * @return true always 
     */
    @Override
    public boolean move(Tile finTile, HashSet<String> tags, LinkedList<Piece> removedPieces) {
        boolean b = super.move(finTile, tags, removedPieces);
        if (wasMoved = false && currentTile.getX() == 11) {
            tags.add("0-0");
        }
        if (wasMoved = false && currentTile.getX() == 4) {
            tags.add("0-0-0");
        }
        wasMoved = true;
        return b;
    }

    /**
     * Return string that includes unique instance name
     * @return string that includes unique instance name
     */
    @Override
    public String toString() {
        return "R";
    }

    /**
     * Gets intermediate tiles between current tile and finish tile
     * @param finTile current tag finish tile
     * @return list of intermediate tiles
     */
    @Override
    public List<Tile> getIntermediateTiles(Tile finTile) {
        List<Tile> tempList = null;
        if (isVerticalMove(finTile)) {
            tempList = super.getListOfIntermediateVerticalTiles(finTile);
        }
        if (isHorizontalMove(finTile)) {
            tempList = super.getListOfIntermediateHorizontalTiles(finTile);
        }

        return tempList;
    }

}
