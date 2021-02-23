package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;

public class Rook extends Piece {
    
    private boolean wasMoved = false;

    public Rook(int color) {
        super(color);
    }

    @Override
    public boolean isMoveLegal(Tile finTile) {
        boolean b = false;
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

    @Override
    public boolean move(Tile finTile) {
        boolean b = true;
        wasMoved = true;
        return b;
    }

}
