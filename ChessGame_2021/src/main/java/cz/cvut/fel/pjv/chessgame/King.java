package cz.cvut.fel.pjv.chessgame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class King extends Piece {

    public King(int color, Tile tile, boolean wasRemoved) {
        super(color, tile, "src/main/resources/bking.png",
                "src/main/resources/wking.png", wasRemoved);
    }

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

        if (finTile.isOnStrike(this.color)) {
            return b;
        }

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

    @Override
    public boolean move(Tile finTile, HashSet<String> tags, List<Piece> removedPieces) {
        boolean b = super.move(finTile, tags, removedPieces);
        wasMoved = true;
        return b;
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public List<Tile> getIntermediateTiles(Tile finTile) {
        List<Tile> tempList = null;

        return tempList;
    }
}
