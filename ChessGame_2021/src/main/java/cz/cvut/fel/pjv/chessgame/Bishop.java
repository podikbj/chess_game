package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(int color) {
        super(color);
    }

    @Override
    public boolean isMoveLegal(Tile finTile) {
        boolean b = false;
        if (isDiagonalMove(finTile)) {
            boolean findOccupiedTiles = isAnyTileIsOccupiedDiagonalMove(finTile);
            b = !findOccupiedTiles;
        }
        return b;
    }

    @Override
    public boolean move(Tile finTile) {
        boolean b = true;
        return b;
    }

}
