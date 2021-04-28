package cz.cvut.fel.pjv.chessgame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Knight extends Piece {

    public Knight(int color, Tile tile, boolean wasRemoved) {

        super(color, tile,"src/main/resources/bknight.png",
                "src/main/resources/wknight.png", wasRemoved);
     
    }

    @Override
    public boolean isMoveAllowed(Tile finTile) {

        boolean b = false;
        if (super.isTheSameColor(finTile)) {return b; }
        if ( isVerticalMove(finTile) || isHorizontalMove(finTile) ) {
            b = true;
        }
        return b;
    }

    @Override
    public boolean isVerticalMove(Tile finTile) {
        return (Math.abs(finTile.getX() - currentTile.getX()) == 2 
                && Math.abs(finTile.getY() - currentTile.getY()) == 1);
    }

    @Override
    public boolean isHorizontalMove(Tile finTile) { // x const
        return (Math.abs(finTile.getX() - currentTile.getX()) == 1 
                && Math.abs(finTile.getY() - currentTile.getY()) == 2);
    }

    @Override
    public boolean move(Tile finTile, HashSet<String> tags, List<Piece> removedPieces) {
        boolean b = super.move(finTile, tags, removedPieces);
        return b;
    }
    
        @Override
    public String toString() {
        return "N";
    }
    
        @Override
    public List<Tile> getIntermediateTiles(Tile finTile) {
        List<Tile> tempList = null;

        return tempList;
    }

}
