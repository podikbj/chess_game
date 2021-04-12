package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.view.BoardPanel;
import cz.cvut.fel.pjv.chessgame.King;
import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.view.StartMenu;
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
        setKing(1);
        setKing(0);
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
    }

    public boolean isKingTile(Tile finTile, boolean whiteIsActive) {

        boolean isKingTile = false;

        Tile currentKingPosition = getCurrentKingPosition(!whiteIsActive);
        if (currentKingPosition == finTile) {
            isKingTile = true;
        }
        return isKingTile;

    }

    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, Tile startTile, boolean whiteIsActive, int castling) {
        //it's not allowed to capture a king
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

    public void setKing(int color) {
        GameManager gameManager = GameManager.getInstance();
        
        LinkedList<Piece> pieces = (!StartMenu.isManual) 
                ?  gameManager.getPieces(color) : gameManager.getRemovedPieceses(color);

        if (pieces == null) { return; }
        Piece king = pieces.stream()
                .filter(p -> p.toString()
                .equals("K"))
                .findAny().get();
        if (color == 1) {
            wKing = king;
        } else {
            bKing = king;
        }

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
        LinkedList<Piece> pieceses = (whiteIsActive == true) ? gameManager.getPieces(0) : gameManager.getPieces(1);
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
        if (currentPiece != startTile.getCurrentPiece()) {
            return b;
        }
        Tile currentKingPosition = getCurrentKingPosition(whiteIsActive);
        GameManager gameManager = GameManager.getInstance();

        startTile.removePiece();
        currentPiece.setCurrentTile(new Tile(1, 8, 8));
        //finTile.setCurrentPiece(currentPiece);     

        LinkedList<Piece> pieceses = (whiteIsActive == true) ? gameManager.getPieces(0) : gameManager.getPieces(1);
        for (Piece p : pieceses) {
            if (p.isMoveAllowed(currentKingPosition)) {
                b = true;
                break;
            }
        }
        
        //finTile.removePiece();
        currentPiece.setCurrentTile(startTile);
        startTile.setCurrentPiece(currentPiece);
        

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
