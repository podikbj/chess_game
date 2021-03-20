package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.view.BoardPanel;
import cz.cvut.fel.pjv.chessgame.King;
import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import java.util.Comparator;
import java.util.LinkedList;
<<<<<<< HEAD
import java.util.List;
import java.util.stream.Collectors;
=======
>>>>>>> 12bb173c7ad0468d60ebd2210041c3a060a0878f
import static java.util.stream.Collectors.toList;

public class CheckMatePositionControl {

    private boolean whiteIsOnChech = false;
    private boolean blackIsOnCheck = false;

    private Piece wKing;
    private Piece bKing;

<<<<<<< HEAD
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
            if (blackIsOnCheck) {
                blackIsOnCheck = !blackIsOnCheck;
            }
            if (whiteIsOnChech) {
                whiteIsOnChech = !whiteIsOnChech;
            }
        }
        return doesMoveBlockCheck;
=======
    public CheckMatePositionControl() {
    }

    public boolean isChecked(Piece currentPiece, Tile finTile, boolean whiteIsActive) {

        boolean isChecked = false;
        //GameManager gameManager = GameManager.getInstance();

        Piece king = (whiteIsActive == true) ? wKing : bKing;
        Tile currentKingPosition = king.getCurrentTile();
        if (currentPiece.isMoveAllowed(currentKingPosition)) {
            isChecked = true;
            if (whiteIsActive) {
                whiteIsOnChech = true;
            } else {
                blackIsOnCheck = true;
            }
            System.out.println("king " + king.getColor() + " isChecked");
        }

        return isChecked;
>>>>>>> 12bb173c7ad0468d60ebd2210041c3a060a0878f
    }

    public boolean isKingTile(Tile finTile, boolean whiteIsActive) {

        boolean isKingTile = false;
<<<<<<< HEAD

        Tile currentKingPosition = getCurrentKingPosition(!whiteIsActive);
=======
        //GameManager gameManager = GameManager.getInstance();
        Piece king = (whiteIsActive == true) ? bKing : wKing;
        Tile currentKingPosition = king.getCurrentTile();
>>>>>>> 12bb173c7ad0468d60ebd2210041c3a060a0878f
        if (currentKingPosition == finTile) {
            isKingTile = true;
        }
        return isKingTile;

    }

<<<<<<< HEAD
    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, Tile startTile, boolean whiteIsActive, int castling) {
        //it's not allowed to capture a king
=======
<<<<<<< HEAD
    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, boolean whiteIsActive) {
>>>>>>> 7daedc57f25617f4a43287d75ee9d715665b29be
        boolean isKingTile = isKingTile(finTile, whiteIsActive);
        //it's not allowed to make some moves being under check 
        boolean doesMoveBlockCheck = true;
        boolean canEscapeCheck = true;
        if (isChecked(whiteIsActive)) {

            if (currentPiece.toString().equals("K")) {
                canEscapeCheck = canEscapeCheck(whiteIsActive);
            } else {
                doesMoveBlockCheck = doesMoveBlockCheck(currentPiece, finTile, whiteIsActive);
            }

        }
        //it's not allowed to make some moves causing to check
        boolean doesMoveCauseCheck = false;
        doesMoveCauseCheck = doesMoveCauseCheck(currentPiece, finTile, startTile, whiteIsActive);
        //it's not allowed castling being under check and if piece was moved
        boolean castlingIsAllowed = true;
        if (castling == 1) {
            castlingIsAllowed = castlingIsAllowed(finTile, whiteIsActive);
        }
        // kings are not allowed to occupy adjacent tiles
//        boolean kingsAreOccupiedAdjacentTiles = false;
//        if (currentPiece.toString().equals("K")) {
//            kingsAreOccupiedAdjacentTiles = kingsAreOccupiedAdjacentTiles(finTile, whiteIsActive);
//        }

        return !isKingTile && doesMoveBlockCheck && !doesMoveCauseCheck
                && canEscapeCheck
                && castlingIsAllowed;
    }

    private Tile getCurrentKingPosition(boolean whiteIsActive) {
        Piece king = (whiteIsActive == true) ? wKing : bKing;
        Tile currentKingPosition = king.getCurrentTile();
        return currentKingPosition;
    }

    public void setwKing() {
=======

    public void setwKing(Piece wKing) {
>>>>>>> 12bb173c7ad0468d60ebd2210041c3a060a0878f
        GameManager gameManager = GameManager.getInstance();
        LinkedList<Piece> wPieceses = gameManager.getwPieceses();

        wKing = wPieceses.stream()
                .filter(p -> p.toString()
                .equals("K"))
                .findAny().get();

    }

<<<<<<< HEAD
    public void setbKing() {
=======
    public void setbKing(Piece bKing) {
>>>>>>> 12bb173c7ad0468d60ebd2210041c3a060a0878f
        GameManager gameManager = GameManager.getInstance();

        LinkedList<Piece> bPieceses = gameManager.getbPieceses();

        bKing = bPieceses.stream()
                .filter(p -> p.toString()
                .equals("K"))
                .findAny().get();

    }

    private List<Tile> getEmptyTilesAroundKing(boolean whiteIsActive) {
        Tile currentKingPosition = getCurrentKingPosition(whiteIsActive);
        GameManager gameManager = GameManager.getInstance();
        LinkedList<Tile> tempList = gameManager.getTileList();

        List<Tile> newList = tempList.stream()
                .filter(p -> Math.abs(p.getX() - currentKingPosition.getX()) == 1)
                .filter(p -> Math.abs(p.getY() - currentKingPosition.getY()) == 1)
                .filter(p -> p.getIsEmpty())
                .collect(Collectors.toList());

        return newList;
    }

    private boolean canEscapeCheck(boolean whiteIsActive) {
        boolean b = true;
        Tile currentKingPosition = getCurrentKingPosition(whiteIsActive);
        List<Tile> tempList = getEmptyTilesAroundKing(whiteIsActive);
        GameManager gameManager = GameManager.getInstance();
        LinkedList<Piece> pieceses = (whiteIsActive == true) ? gameManager.getbPieceses() : gameManager.getwPieceses();
        for (Piece p : pieceses) {
            for (Tile t : tempList) {
                if (p.isMoveAllowed(t)) {
                    b = false;
                    break;
                }
            }
        }
        return b;
    }

    private boolean canBlockCheck() {
        boolean b = true;
        return b;
    }

    private boolean doesMoveCauseCheck(Piece currentPiece, Tile finTile, Tile startTile, boolean whiteIsActive) {
        boolean b = false;
        Tile currentKingPosition = getCurrentKingPosition(whiteIsActive);
        GameManager gameManager = GameManager.getInstance();
        if (currentPiece == startTile.getCurrentPiece()) {
            startTile.removePiece();
            finTile.setCurrentPiece(currentPiece);
        }

        LinkedList<Piece> pieceses = (whiteIsActive == true) ? gameManager.getbPieceses() : gameManager.getwPieceses();
        for (Piece p : pieceses) {
            if (p.isMoveAllowed(currentKingPosition)) {
                b = true;
                break;
            }
        }
        startTile.setCurrentPiece(currentPiece);
        finTile.removePiece();

        return b;
    }

    private boolean kingsAreOccupiedAdjacentTiles(Tile finTile, boolean whiteIsActive) {
        boolean b = false;
        Tile currentKingPosition = getCurrentKingPosition(!whiteIsActive);
        List<Tile> emptyTilesAroundKing = getEmptyTilesAroundKing(!whiteIsActive);
        return emptyTilesAroundKing.contains(finTile);

    }

    public boolean castlingIsAllowed(Tile finTile, boolean whiteIsActive) {
        boolean b = true;
        if (isChecked(whiteIsActive)) {
            return b = false;
        }
        Piece king = (whiteIsActive == true) ? wKing : bKing;
        if (king.isWasMoved()) {
            return b = false;
        }

        if (finTile.getX() != 5 || finTile.getX() != 3) {
            return b = false;
        }

        return b;

    }

    public Tile doCastling(Tile finTile, boolean whiteIsActive) {

        Piece king = (whiteIsActive == true) ? wKing : bKing;
        Tile currentKingPosition = getCurrentKingPosition(whiteIsActive);
        currentKingPosition.removePiece();
        Tile finKingTile = null;

        GameManager gameManager = GameManager.getInstance();
        LinkedList<Tile> tempList = gameManager.getTileList();

        if (finTile.getX() == 5 && whiteIsActive) {
            finKingTile = tempList.stream()
                    .filter(p -> p.getX() == 6 && p.getY() == 0).findAny().get();
        }
        if (finTile.getX() == 3 && whiteIsActive) {
            finKingTile = tempList.stream()
                    .filter(p -> p.getX() == 2 && p.getY() == 0).findAny().get();
        }
        if (finTile.getX() == 3 && !whiteIsActive) {
            finKingTile = tempList.stream()
                    .filter(p -> p.getX() == 2 && p.getY() == 7).findAny().get();
        }
        if (finTile.getX() == 5 && !whiteIsActive) {
            finKingTile = tempList.stream()
                    .filter(p -> p.getX() == 6 && p.getY() == 7).findAny().get();
        }

        if (finKingTile != null) {
            king.move(finKingTile);
        }

        return finKingTile;

    }

}
