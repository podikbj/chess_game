package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Bishop extends Piece {

    public Bishop(int color, Tile tile, boolean wasRemoved) {
        super(color, tile, "src/main/resources/bbishop.png",
                "src/main/resources/wbishop.png", wasRemoved);

    }

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

    @Override
    public boolean move(Tile finTile, Stack<String> tagType) {
        boolean b = super.move(finTile, tagType);
        return b;
    }

    @Override
    public String toString() {
        return "B";
    }

    @Override
    public List<Tile> getIntermediateTiles(Tile finTile) {
        List<Tile> tempList = null;
        if (isDiagonalMove(finTile)) {
            tempList = super.getListOfIntermediateDiagonalTiles(finTile);
        }
        return tempList;
    }

}
