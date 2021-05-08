package cz.cvut.fel.pjv.chessgame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Rook extends Piece {

    public Rook(int color, Tile tile, boolean wasRemoved) {
        super(color, tile, "src/main/resources/brook.png",
                "src/main/resources/wrook.png", wasRemoved);
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

        b = !areOccupiedTiles;
        return b;
    }

    @Override
    public boolean move(Tile finTile, HashSet<String> tags, List<Piece> removedPieces) {
        boolean b = super.move(finTile, tags, removedPieces);
        if (wasMoved = false && currentTile.getX() == 11) {
            tags.add("0-0");
        }
        if (wasMoved = false && currentTile.getX() == 4) {
            tags.add("0-0-0");
        }
        wasMoved = true;
        return b;
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public List<Tile> getIntermediateTiles(Tile finTile) {
        List<Tile> tempList = null;
        if (isVerticalMove(finTile)) {
            tempList = super.getListOfIntermediateVerticalTiles(finTile);
        }
        if (isDiagonalMove(finTile)) {
            tempList = super.getListOfIntermediateHorizontalTiles(finTile);
        }

        return tempList;
    }

}
