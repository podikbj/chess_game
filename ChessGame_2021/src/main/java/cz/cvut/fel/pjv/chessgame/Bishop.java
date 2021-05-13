package cz.cvut.fel.pjv.chessgame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Represents a bishop piece
 *
 * @author kira
 */
public class Bishop extends Piece {

    /**
     * Constructor for Bishop.
     *
     * @param color piece color
     * @param tile current tile
     * @param blackPiecePath path to black piece picture file
     * @param whitePiecePath path to white piece picture file
     * @param wasRemoved true if the piece was removed from the chess board
     */
    public Bishop(int color, Tile tile, boolean wasRemoved) {
        super(color, tile, "src/main/resources/bbishop.png",
                "src/main/resources/wbishop.png", wasRemoved);

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
        if (isDiagonalMove(finTile)) {
            boolean findOccupiedTiles = isAnyTileIsOccupiedDiagonalMove(finTile);
            b = !findOccupiedTiles;
        }
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
        return b;
    }

    /**
     * Return string that includes unique instance name
     *
     * @return string that includes unique instance name
     */
    @Override
    public String toString() {
        return "B";
    }

    /**
     * Gets intermediate tiles between current tile and finish tile
     *
     * @param finTile current tag finish tile
     * @return list of intermediate tiles
     */
    @Override
    public List<Tile> getIntermediateTiles(Tile finTile) {
        List<Tile> tempList = null;
        if (isDiagonalMove(finTile)) {
            tempList = super.getListOfIntermediateDiagonalTiles(finTile);
        }
        return tempList;
    }

}
