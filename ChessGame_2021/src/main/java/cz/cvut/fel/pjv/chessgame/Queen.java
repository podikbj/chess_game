package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Queen extends Piece {

    public Queen(int color, Tile tile, boolean wasRemoved) {
        super(color, tile, "src/main/resources/bqueen.png",
                "src/main/resources/wqueen.png", wasRemoved);
    }

    @Override
    public boolean isMoveAllowed(Tile finTile) {

        boolean b = false;
        if (super.isTheSameColor(finTile)) {
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
    public boolean move(Tile finTile, Stack<String> tagType) {
        boolean b = super.move(finTile, tagType);
        return b;
    }

    @Override
    public String toString() {
        return "Q";
    }

    @Override
    public List<Tile> getIntermediateTiles(Tile finTile) {
        List<Tile> tempList = null;
        if (isVerticalMove(finTile)) {
            tempList = super.getListOfIntermediateVerticalTiles(finTile);
        }
        if (isHorizontalMove(finTile)) {
            tempList = super.getListOfIntermediateHorizontalTiles(finTile);
        }
        if (isDiagonalMove(finTile)) {
            tempList = super.getListOfIntermediateDiagonalTiles(finTile);
        }

        return tempList;
    }

}
