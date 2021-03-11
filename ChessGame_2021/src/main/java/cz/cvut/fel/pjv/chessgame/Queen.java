package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;

public class Queen extends Piece {

    public Queen(int color) {
        super(color, "src/main/resources/bqueen.png", "src/main/resources/wqueen.png");
    }

    @Override
    public boolean isMoveAllowed(Tile finTile) {

        boolean b = false;
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
        boolean b = true;
        return b;
    }

    @Override
    public String toString() {
        return "Q";
    }

}
