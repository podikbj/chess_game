package cz.cvut.fel.pjv.chessgame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Represents a Knight piece
 *
 * @author kira
 */
public class Knight extends Piece {

    /**
     * Constructor for Knight.
     *
     * @param color piece color
     * @param tile current tile
     * @param blackPiecePath path to black piece picture file
     * @param whitePiecePath path to white piece picture file
     * @param wasRemoved true if the piece was removed from the chess board
     */
    public Knight(int color, Tile tile, boolean wasRemoved) {

        super(color, tile, "src/main/resources/bknight.png",
                "src/main/resources/wknight.png", wasRemoved);

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
        if (isVerticalMove(finTile) || isHorizontalMove(finTile)) {
            b = true;
        }
        return b;
    }

    /**
     *
     * @param finTile
     * @return
     */
    @Override
    public boolean isVerticalMove(Tile finTile) {
        return (Math.abs(finTile.getX() - currentTile.getX()) == 2
                && Math.abs(finTile.getY() - currentTile.getY()) == 1);
    }

    /**
     *
     * @param finTile
     * @return
     */
    @Override
    public boolean isHorizontalMove(Tile finTile) { // x const
        return (Math.abs(finTile.getX() - currentTile.getX()) == 1
                && Math.abs(finTile.getY() - currentTile.getY()) == 2);
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
        return "N";
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
