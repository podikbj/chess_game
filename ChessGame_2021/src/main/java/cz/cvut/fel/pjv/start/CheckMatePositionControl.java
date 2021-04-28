package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.view.BoardPanel;
import cz.cvut.fel.pjv.chessgame.King;
import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.view.StartMenu;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.imageio.plugins.tiff.ExifGPSTagSet;

public class CheckMatePositionControl {

    private boolean whiteIsOnChech = false;
    private boolean blackIsOnCheck = false;

    private Piece wKing;
    private Piece bKing;

    private Piece attaker = null;
    private List<Tile> tileList = null;
    private GameManager gameManager = GameManager.getInstance();
    private Tile currentKingPosition = null;
    private boolean whiteIsActive = true;

    public CheckMatePositionControl() {
        setKing(1);
        setKing(0);
        this.tileList = gameManager.getTileList();
    }

    public boolean isChecked() {
        return (whiteIsActive == true) ? whiteIsOnChech : blackIsOnCheck;
    }

    public boolean isItCheck(Piece currentPiece) {

        boolean isItCheck = false;

        if (currentPiece.isWasRemoved()) {
            return isItCheck;
        }
        setCurrentKingPosition(!whiteIsActive);
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

    public boolean doesMoveBlockCheck(Piece currentPiece, Tile finTile) {

        boolean doesMoveBlockCheck = false;
        if (finTile == attaker.getCurrentTile()) {
            attaker = null;
            if (blackIsOnCheck) {
                blackIsOnCheck = !blackIsOnCheck;
            }
            if (whiteIsOnChech) {
                whiteIsOnChech = !whiteIsOnChech;
            }
            return true;
        }
        setCurrentKingPosition(whiteIsActive);
        List<Tile> tempList = null;
        if (attaker.isVerticalMove(currentKingPosition)) {
            tempList = attaker.getListOfIntermediateVerticalTiles(currentKingPosition);
        }
        if (attaker.isHorizontalMove(currentKingPosition)) {
            tempList = attaker.getListOfIntermediateHorizontalTiles(currentKingPosition);
        }
        if (attaker.isDiagonalMove(currentKingPosition)) {
            tempList = attaker.getListOfIntermediateDiagonalTiles(currentKingPosition);
        }

        if (tempList != null && tempList.contains(finTile)) {
            doesMoveBlockCheck = true;
            attaker = null;
            if (blackIsOnCheck) {
                blackIsOnCheck = !blackIsOnCheck;
            }
            if (whiteIsOnChech) {
                whiteIsOnChech = !whiteIsOnChech;
            }

        }
        return doesMoveBlockCheck;
    }

    private boolean doesMoveCauseCheck(Piece currentPiece, Tile finTile, Tile startTile) {
        boolean b = false;
        if (currentPiece != startTile.getCurrentPiece()) {
            return b;
        }
        setCurrentKingPosition(whiteIsActive);
        startTile.removePiece();

        List<Piece> pieces = null;
        pieces = (whiteIsActive == true) ? gameManager.getPieces(0) : gameManager.getPieces(1);
        if (pieces == null) {
            return b;
        }
        for (Piece p : pieces) {
            if (p.isMoveAllowed(currentKingPosition)) {
                if (finTile == p.getCurrentTile()) {
                    break;
                }
                List<Tile> tempList = p.getIntermediateTiles(currentKingPosition);
                if (tempList != null && tempList.contains(startTile)) {
                    b = true;
                    break;
                }
            }
        }

        currentPiece.setCurrentTile(startTile);
        startTile.setCurrentPiece(currentPiece);

        return b;
    }

    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, Tile startTile, int castling) {
        //it's not allowed to capture a king
        boolean isKingTile = isKingTile(finTile);
        if (isKingTile) {
            return false;
        }

        //it's not allowed to make some moves being under check 
        boolean doesMoveBlockCheck = true;
        boolean doesMoveEscapeCheck = true;
        if (isChecked()) {
            if (currentPiece.toString().equals("K")) {
                doesMoveEscapeCheck = doesMoveEscapeCheck(finTile);
            } else {
                doesMoveBlockCheck = doesMoveBlockCheck(currentPiece, finTile);
            }
        }
        if (!doesMoveBlockCheck || !doesMoveEscapeCheck) {
            return false;
        }

        //it's not allowed to make some moves causing to check
        boolean doesMoveCauseCheck = false;
        doesMoveCauseCheck = doesMoveCauseCheck(currentPiece, finTile, startTile);
        if (doesMoveCauseCheck) {
            return false;
        }

        //it's not allowed do castling being under check and if piece was moved
        boolean castlingIsAllowed = true;
        //System.out.println("castling2 :" + castling);
        if (castling == 1) {
            castlingIsAllowed = castlingIsAllowed(currentPiece, finTile);
        }
        if (!castlingIsAllowed) {
            return false;
        }

        // kings are not allowed to occupy adjacent tiles
//        boolean kingsAreOccupiedAdjacentTiles = false;
//        if (currentPiece.toString().equals("K")) {
//            kingsAreOccupiedAdjacentTiles = kingsAreOccupiedAdjacentTiles(finTile, whiteIsActive);
//        }
//        System.out.println("============");
//        System.out.println("currentPiece :" + currentPiece.toString());
//        System.out.println("doesMoveBlockCheck: " + doesMoveBlockCheck);
//        System.out.println("doesMoveCauseCheck: " + doesMoveCauseCheck);
//        System.out.println("doesMoveEscapeCheck: " + doesMoveEscapeCheck);
//        System.out.println("castlingIsAllowed:" + castlingIsAllowed);
//        System.out.println("============");
        return true;
    }

    // King's stuff
    public boolean isKingTile(Tile finTile) {

        boolean isKingTile = false;

        setCurrentKingPosition(!whiteIsActive);
        if (currentKingPosition == finTile) {
            isKingTile = true;
        }
        return isKingTile;

    }

    public void setKing(int color) {

        List<Piece> pieces = null;
        pieces = (!StartMenu.isManual)
                ? gameManager.getPieces(color) : gameManager.getRemovedPieces();

        if (pieces == null) {
            return;
        }

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

    private Tile getCurrentKingPosition() {
        return this.currentKingPosition;
    }

    private void setCurrentKingPosition(boolean wia) {
        Piece king = (wia == true) ? wKing : bKing;
        this.currentKingPosition = king.getCurrentTile();
    }

    private List<Tile> getEmptyTilesAroundKing(boolean wia) {
        setCurrentKingPosition(wia);
        List<Tile> tempList = null;
        tempList = tileList.stream()
                .filter(p -> Math.abs(p.getX() - currentKingPosition.getX()) == 1)
                .filter(p -> Math.abs(p.getY() - currentKingPosition.getY()) == 1)
                .filter(p -> p.getIsEmpty())
                .collect(Collectors.toList());

        return tempList;
    }

    private boolean kingsAreOccupiedAdjacentTiles(Tile finTile, boolean wia) {
        List<Tile> emptyTilesAroundKing = getEmptyTilesAroundKing(!wia);
        return emptyTilesAroundKing.contains(finTile);

    }

    private boolean canEscapeCheck() {
        boolean b = true;
        List<Tile> tempList = getEmptyTilesAroundKing(whiteIsActive);
        List<Piece> pieces = null;
        pieces = (whiteIsActive == true) ? gameManager.getPieces(0) : gameManager.getPieces(1);
        if (pieces == null) {
            return b;
        }
        for (Piece p : pieces) {
            for (Tile t : tempList) {
                if (p.isMoveAllowed(t)) {
                    b = false;
                    break;
                }
            }
        }
        return b;
    }

    private boolean doesMoveEscapeCheck(Tile finTile) {
        boolean b = true;
        List<Piece> pieces = null;
        pieces = (whiteIsActive == true) ? gameManager.getPieces(0) : gameManager.getPieces(1);
        if (pieces == null) {
            return b;
        }
        for (Piece p : pieces) {
            if (p.isMoveAllowed(finTile)) {
                b = false;
                break;
            }
        }
        return b;
    }

    //Castling stuff
    public boolean castlingIsAllowed(Piece currentPiece, Tile finTile) {
        boolean b = true;
        if (isChecked()) {
            return b = false;
        }
        Piece king = (whiteIsActive == true) ? wKing : bKing;
        if (king.isWasMoved()) {
            return b = false;
        }

        if (!currentPiece.toString().equals("R")) {
            return b = false;
        }

        if (!currentPiece.isWasMoved()) {
            return b = false;
        }

        if (finTile.getX() != 7 || finTile.getX() != 9) {
            return b = false;
        }

        return b;

    }

    public Tile doCastling(Tile finTile, HashSet<String> tags, List<Piece> removedPieces) {

        Piece king = (whiteIsActive == true) ? wKing : bKing;
        setCurrentKingPosition(whiteIsActive);
        currentKingPosition.removePiece();
        Tile finKingTile = null;

        if (finTile.getX() == 9 && whiteIsActive) {
            finKingTile = tileList.stream()
                    .filter(p -> p.getX() == 10 && p.getY() == 0).findAny().get();
            tags.add("O-O");
        }
        if (finTile.getX() == 7 && whiteIsActive) {
            finKingTile = tileList.stream()
                    .filter(p -> p.getX() == 6 && p.getY() == 0).findAny().get();
            tags.add("O-O-O");
        }
        if (finTile.getX() == 7 && !whiteIsActive) {
            finKingTile = tileList.stream()
                    .filter(p -> p.getX() == 6 && p.getY() == 7).findAny().get();
            tags.add("O-O-O");
        }
        if (finTile.getX() == 9 && !whiteIsActive) {
            finKingTile = tileList.stream()
                    .filter(p -> p.getX() == 10 && p.getY() == 7).findAny().get();
            tags.add("O-O");
        }
        // reversed castling
        if ((finTile.getX() == 11 || finTile.getX() == 4) && whiteIsActive) {
            finKingTile = tileList.stream()
                    .filter(p -> p.getX() == 8 && p.getY() == 0).findAny().get();
            //tagType.add("O-O");
        }

        if ((finTile.getX() == 4 || finTile.getX() == 4) && !whiteIsActive) {
            finKingTile = tileList.stream()
                    .filter(p -> p.getX() == 8 && p.getY() == 7).findAny().get();
            //tagType.add("O-O-O");
        }

        if (finKingTile != null) {
            king.move(finKingTile, tags,removedPieces);
            if (tags.equals("R")) {
                king.setWasMoved(false);
            }
        }

        return finKingTile;

    }

    public void setWhiteIsActive(boolean whiteIsActive) {
        this.whiteIsActive = whiteIsActive;
    }

}
