package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(int color, Tile tile) {
        super(color, tile, "src/main/resources/bpawn.png", "src/main/resources/wpawn.png");
    }

    @Override
    public boolean isMoveAllowed(Tile finTile) {
        boolean b = false;
        if (super.isTheSameColor(finTile)) {
            return b;
        }
        boolean areOccupiedTiles = true;
        if (isVerticalMove(finTile)) {
            areOccupiedTiles = isAnyTileIsOccupiedVerticalMove(finTile);
        }
        int diffX = finTile.getX() - currentTile.getX();
        int diffY = finTile.getY() - currentTile.getY();

        if (color == 0 && diffY >= 0) {
            return b;
        }
        if (color == 1 && diffY <= 0) {
            return b;
        }

        if (!wasMoved && Math.abs(diffY) < 3 && diffX == 0 && !areOccupiedTiles) {
            b = true;
        }

        if (wasMoved && Math.abs(diffY) == 1 && diffX == 0) {
            b = true;
        }

        if (!finTile.getIsEmpty() && Math.abs(diffY) == 1 && Math.abs(diffX) == 1) {
            b = true;
        }
        return b;
    }

    @Override
    public boolean move(Tile finTile) {
        boolean b = super.move(finTile);
        return b;
    }

    @Override
    public String toString() {
        return "P";
    }

}
