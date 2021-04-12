package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;

public class King extends Piece {

    public King(int color, Tile tile) {
        super(color, tile, "src/main/resources/bking.png", "src/main/resources/wking.png");
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
    public boolean move(Tile finTile) {
        boolean b = super.move(finTile);
        return b;
    }

    @Override
    public String toString() {
        return "K";
    }

}
