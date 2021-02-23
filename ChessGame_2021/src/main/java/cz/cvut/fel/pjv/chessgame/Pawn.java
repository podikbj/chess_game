package cz.cvut.fel.pjv.chessgame;

import java.util.LinkedList;
import java.util.List;

public class Pawn extends Piece {

    private boolean wasMoved = false;

    public Pawn(int color) {
        super(color);
    }

    @Override
    public boolean isMoveLegal(Tile finTile) {

        boolean b = false;
        
        int diffX = finTile.getX() - currentTile.getX();
        int diffY = finTile.getY() - currentTile.getY();

        if (color == 0 && diffY >= 0) {
            return b;
        }
        if (color == 1 && diffY <= 0) {
            return b;
        }

        if (!wasMoved && Math.abs(diffY) < 3 && diffX == 0) {
            b = true;
        }

        if (wasMoved && Math.abs(diffY) == 1 && diffX == 0) {
            b = true;
        }

        if (!finTile.getIsEmpty()&& Math.abs(diffY) == 1 && Math.abs(diffX) == 1) {
            b = true;
        }
        return b;
    }

    @Override
    public boolean move(Tile finTile) {
        boolean b = super.move(finTile);
        wasMoved = true;
        return b;
    }

}
