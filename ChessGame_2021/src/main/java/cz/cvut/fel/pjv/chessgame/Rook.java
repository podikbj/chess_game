package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;

public class Rook extends Piece {

    public Rook(int color) {
        super(color, "src/main/resources/brook.png", "src/main/resources/wrook.png");
    }

    @Override
    public boolean isMoveAllowed(Tile finTile) {
        boolean b = false;
        if (super.isTheSameColor(currentTile, finTile)) {return b; }
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
        boolean b = super.move(finTile);
        wasMoved = true;
        return b;
    }

    @Override
    public String toString() {
        return "R";
    }


    
    

}
