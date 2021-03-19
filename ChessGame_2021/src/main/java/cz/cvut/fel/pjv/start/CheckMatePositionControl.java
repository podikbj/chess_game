package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.view.BoardPanel;
import cz.cvut.fel.pjv.chessgame.King;
import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

public class CheckMatePositionControl {

    private boolean whiteIsOnChech = false;
    private boolean blackIsOnCheck = false;

    private Piece wKing;
    private Piece bKing;

    private Piece attaker = null;

    public CheckMatePositionControl() {
        setbKing();
        setwKing();
    }

    public boolean isChecked(boolean whiteIsActive) {
        return (whiteIsActive == true) ? whiteIsOnChech : blackIsOnCheck;
    }

    public boolean isItCheck(Piece currentPiece, boolean whiteIsActive) {

        boolean isItCheck = false;

        Tile currentKingPosition = getCurrentKingPosition(!whiteIsActive);
        if (currentPiece.isMoveAllowed(currentKingPosition)) {
            isItCheck = true;
            attaker = currentPiece;
            if (whiteIsActive) {
                blackIsOnCheck = true;
                whiteIsOnChech = false;
            } else {
                whiteIsOnChech = true;
                blackIsOnCheck = false;

            }
            System.out.println("king " + currentKingPosition.getColor() + " isChecked");
        }

        return isItCheck;
    }

    public boolean doesMoveBlockCheck(Piece currentPiece, Tile finTile, boolean whiteIsActive) {

        boolean doesMoveBlockCheck = false;
        Tile currentKingPosition = getCurrentKingPosition(whiteIsActive);
        List<Tile> tempList = null;
        GameManager gameManager = GameManager.getInstance();
        List<Tile> tileList = gameManager.getTileList();
        if (attaker.isVerticalMove(currentKingPosition)) {
            tempList = tileList.stream()
                    .filter(p -> p.getX() == currentKingPosition.getX())
                    .filter((p)
                            -> {
                        boolean y = false;
                        if (currentKingPosition.getY() > attaker.getCurrentTile().getY()) {
                            y = (p.getY() > attaker.getCurrentTile().getY() && p.getY() < currentKingPosition.getY());
                        }
                        if (currentKingPosition.getY() < attaker.getCurrentTile().getY()) {
                            y = (p.getY() < attaker.getCurrentTile().getY() && p.getY() > currentKingPosition.getY());
                        }
                        return y;
                    }
                    )
                    .collect(Collectors.toList());

        }
        if (attaker.isHorizontalMove(currentKingPosition)) {
            tempList = tileList.stream()
                    .filter(p -> p.getY() == currentKingPosition.getY())
                    .filter((p)
                            -> {
                        boolean x = false;
                        if (currentKingPosition.getX() > attaker.getCurrentTile().getX()) {
                            x = (p.getX() > attaker.getCurrentTile().getX() && p.getX() < currentKingPosition.getX());
                        }
                        if (currentKingPosition.getX() < attaker.getCurrentTile().getX()) {
                            x = (p.getX() < attaker.getCurrentTile().getX() && p.getX() > currentKingPosition.getX());
                        }
                        return x;
                    }
                    )
                    .collect(Collectors.toList());
        }
        if (attaker.isDiagonalMove(currentKingPosition)) {
            tempList = tileList.stream()
                    .filter(p -> Math.abs(p.getX() - currentKingPosition.getX()) == Math.abs(p.getY() - currentKingPosition.getY()))
                    .filter(p -> p.getX() > currentKingPosition.getX())
                    .filter(p -> p.getY() > currentKingPosition.getY())
                    .filter(p -> p.getX() < attaker.getCurrentTile().getX())
                    .filter(p -> p.getY() < attaker.getCurrentTile().getY())
                    .collect(Collectors.toList());
        }

        if (tempList != null && tempList.contains(finTile)) {
            doesMoveBlockCheck = true;
            if (blackIsOnCheck) blackIsOnCheck = !blackIsOnCheck;
            if (whiteIsOnChech) whiteIsOnChech = !whiteIsOnChech;
        }
        return doesMoveBlockCheck;
    }

    public boolean isKingTile(Tile finTile, boolean whiteIsActive) {

        boolean isKingTile = false;

        Tile currentKingPosition = getCurrentKingPosition(!whiteIsActive);
        if (currentKingPosition == finTile) {
            isKingTile = true;
        }
        return isKingTile;

    }

    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, boolean whiteIsActive) {
        boolean isKingTile = isKingTile(finTile, whiteIsActive);
        boolean doesMoveBlockCheck = true;
        if (isChecked(whiteIsActive)) {
            doesMoveBlockCheck = doesMoveBlockCheck(currentPiece, finTile, whiteIsActive);
        }
        return !isKingTile && doesMoveBlockCheck;
    }

    private Tile getCurrentKingPosition(boolean whiteIsActive) {
        Piece king = (whiteIsActive == true) ? wKing : bKing;
        Tile currentKingPosition = king.getCurrentTile();
        return currentKingPosition;
    }

    public void setwKing() {
        GameManager gameManager = GameManager.getInstance();
        LinkedList<Piece> wPieceses = gameManager.getwPieceses();

        wKing = wPieceses.stream()
                .filter(p -> p.toString()
                .equals("K"))
                .findAny().get();

    }

    public void setbKing() {
        GameManager gameManager = GameManager.getInstance();

        LinkedList<Piece> bPieceses = gameManager.getbPieceses();

        bKing = bPieceses.stream()
                .filter(p -> p.toString()
                .equals("K"))
                .findAny().get();

    }
}
