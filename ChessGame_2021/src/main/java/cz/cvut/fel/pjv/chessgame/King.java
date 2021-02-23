package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;

public class King extends Piece {

    private boolean wasMoved = false;
    
    public King(int color) {
        super(color);
    }

    @Override
    public boolean isMoveLegal(Tile finTile) {
        // check position for another king, they can not be in adjacent tiles
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
        wasMoved = true;
        return b;
    }

}
