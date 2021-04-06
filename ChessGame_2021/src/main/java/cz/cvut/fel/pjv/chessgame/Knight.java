package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;

public class Knight extends Piece {

    public Knight(int color, Tile tile) {

        super(color, tile,"src/main/resources/bknight.png", "src/main/resources/wknight.png");
     
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
    public boolean move(Tile finTile) {
        boolean b = super.move(finTile);
        return b;
    }
    
        @Override
    public String toString() {
        return "N";
    }

}
