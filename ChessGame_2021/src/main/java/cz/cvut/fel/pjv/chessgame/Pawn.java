package cz.cvut.fel.pjv.chessgame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Pawn extends Piece {

    private boolean isEnPassant = false;
    //private Tile intermediateTile = null;
    private Tile finLastMoveTile = null;

    public Pawn(int color, Tile tile, boolean wasRemoved) {
        super(color, tile, "src/main/resources/bpawn.png",
                "src/main/resources/wpawn.png", wasRemoved);
    }

    @Override
    public boolean isMoveAllowed(Tile finTile) {
        boolean b = false;
        if (super.isTheSameColor(finTile)) {
            return b;
        }

        if (isAllowedEnPassant(finTile)) {
            b = true;
            return b;

        }

        boolean areOccupiedTiles = true;
        if (isVerticalMove(finTile)) {
            areOccupiedTiles = isAnyTileIsOccupiedVerticalMove(finTile);
        }
        int diffX = finTile.getX() - currentTile.getX();
        int diffY = finTile.getY() - currentTile.getY();

        if (color == 0 && diffY >= 0) {
            return b;
        }
        if (color == 1 && diffY <= 0) {
            return b;
        }

        if (!wasMoved && Math.abs(diffY) < 3 && diffX == 0 && !areOccupiedTiles) {
            b = true;
        }

        if (wasMoved && Math.abs(diffY) == 1 && diffX == 0) {
            b = true;
        }

        if (!finTile.getIsEmpty() && Math.abs(diffY) == 1 && Math.abs(diffX) == 1) {
            b = true;
        }

        return b;
    }

    private boolean isAllowedEnPassant(Tile finTile) {
        boolean b = false;

        Tile[] lastMove = gameManager.getLastMove();

        if (lastMove[0] == null || lastMove[1] == null) {
            return b;
        }
        if (!lastMove[1].getCurrentPiece().toString().equals("P")) {
            return b;
        }

        if (Math.abs(lastMove[1].getY() - lastMove[0].getY()) != 2) {
            return b;
        }

        final int x = lastMove[0].getX();
        final int y = (lastMove[0].getY() + lastMove[1].getY()) / 2;
        Tile intermediateTile = tileList.stream().filter(t -> t.getX() == x
                && t.getY() == y).findAny().get();

        if (Math.abs(intermediateTile.getX() - currentTile.getX()) != 1) {
            return b;
        }

        if (currentTile.getY() != lastMove[1].getY()) {
            return b;
        }

        if (intermediateTile.getY() == 5 && this.getColor() == 1) {
            isEnPassant = true;
            finLastMoveTile = lastMove[1];
            b = true;
        }

        if (intermediateTile.getY() == 2 && this.getColor() == 0) {
            finLastMoveTile = lastMove[1];
            isEnPassant = true;
            b = true;
        }

        return b;
    }

    @Override
    public boolean move(Tile finTile, HashSet<String> tags, List<Piece> removedPieces) {

        if (isEnPassant) {
            if (finLastMoveTile == null) {
                return false;
            }
            tags.add("x");
            Piece removedPiece = finLastMoveTile.getCurrentPiece();
            removedPiece.setCurrentTile(null);
            removedPiece.wasRemoved = true;
            removedPieces.add(removedPiece);
            finLastMoveTile.removePiece();

            currentTile.removePiece();
            finTile.setCurrentPiece(this);
            this.currentTile = finTile;
            addMoveEntry(finTile);
            finLastMoveTile = null;
            isEnPassant = false;

            return true;

        }
        boolean b = super.move(finTile, tags, removedPieces);
        return b;
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public List<Tile> getIntermediateTiles(Tile finTile) {
        List<Tile> tempList = null;

        return tempList;
    }

}
