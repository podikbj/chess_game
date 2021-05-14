package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.view.StartMenu;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author kira
 */
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
    private int c = 0;

    /**
     *
     */
    public CheckMatePositionControl() {
        setKing(1);
        setKing(0);
        this.tileList = gameManager.getTileList();
    }

    /**
     *
     * @return
     */
    public boolean isChecked() {
        return (whiteIsActive == true) ? whiteIsOnChech : blackIsOnCheck;
    }

    /**
     *
     * @param currentPiece current piece
     * @param finTile
     * @return
     */
    public boolean doesMoveBlockCheck(Piece currentPiece, Tile finTile, boolean setBlockFlag) {

        boolean doesMoveBlockCheck = false;
        if (finTile == attaker.getCurrentTile()) {
            if (setBlockFlag) {
                //attaker = null;
                setCheckFlag(false, null);
            }
            return true;
        }
        boolean wia = (attaker.getColor() == 1) ? false : true;
        setCurrentKingPosition(wia);
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
            if (setBlockFlag) {
                // attaker = null;
                setCheckFlag(false, null);
            }
        }
        return doesMoveBlockCheck;
    }

    /**
     *
     * @param currentPiece current piece
     * @param finTile
     * @param startTile
     * @param self
     * @return
     */
    public boolean doesMoveCauseCheck(Piece currentPiece, Tile finTile, Tile startTile, boolean self) {

        //can put on check current king because of free start tile
        if (self) {
            if (currentPiece.toString().equals("K")) {
                return kingMoveCauseCheck(finTile, currentPiece, false);
            }
            boolean wia = whiteIsActive;
            if ((whiteIsActive && blackIsOnCheck) || (!whiteIsActive && whiteIsOnChech)) {
                wia = !whiteIsActive;
            }
            setCurrentKingPosition(wia);

            int color = (wia == true) ? 0 : 1;
            List<Piece> pieces = gameManager.getPieces(color);
            if (pieces.size() <= 1) {
                return false;
            }

            Piece tempPiece = null;
            if (!finTile.getIsEmpty()) {
                tempPiece = finTile.getCurrentPiece();
                finTile.removePiece();
            }

            startTile.removePiece();

            finTile.setCurrentPiece(currentPiece);
            currentPiece.setCurrentTile(finTile);

            for (Piece p : pieces) {
                if (p == tempPiece) {
                    continue;
                }
                if (p.isMoveAllowed(currentKingPosition)) {
//                    if (finTile == p.getCurrentTile()) {
//                        break;
//                    }
                    List<Tile> tempList = p.getIntermediateTiles(currentKingPosition);
                    if (tempList != null && tempList.contains(startTile)) {
                        return true;
                    }
                }
            }

            finTile.removePiece();
            if (tempPiece != null) {
                finTile.setCurrentPiece(tempPiece);
                tempPiece.setCurrentTile(finTile);
            }
            currentPiece.setCurrentTile(startTile);
            startTile.setCurrentPiece(currentPiece);
            return false;
        }

        setCurrentKingPosition(!whiteIsActive);

//can put on check opposite king by current piece because of moving on finish tile
        if (currentPiece.isMoveAllowed(currentKingPosition)) {
            //attaker = currentPiece;
            setCheckFlag(true, currentPiece);
            return true;
        }

// can put on check opposite king by any other piece because of free start tile
        int color = (whiteIsActive == true) ? 1 : 0;
        List<Piece> pieces = gameManager.getPieces(color);
        if (pieces.size() <= 2) {
            return false;
        }

        for (Piece p : pieces) {
            if (p == currentPiece) {
                continue;
            }
            if (p.isMoveAllowed(currentKingPosition)) {
                List<Tile> tempList = p.getIntermediateTiles(currentKingPosition);
                if (tempList != null && tempList.contains(startTile)) {
                    //attaker = p;
                    setCheckFlag(true, p);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *
     * @param currentPiece current piece
     * @param finTile
     * @param startTile
     * @param castling
     * @return
     */
    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, Tile startTile, int castling) {
        //it's not allowed to capture a king
        boolean isKingTile = isKingTile(finTile);
        if (isKingTile) {
            return false;
        }

        //it's not allowed to make some moves causing to check
        boolean doesMoveCauseCheck = false;
        doesMoveCauseCheck = doesMoveCauseCheck(currentPiece, finTile, startTile, true);
        if (doesMoveCauseCheck) {
            return false;
        }

        //it's not allowed to make some moves being under check 
        boolean doesMoveBlockCheck = true;
        boolean doesKingCanEscapeCheck = true;
        if (isChecked()) {
            if (currentPiece.toString().equals("K")) {
                doesKingCanEscapeCheck = !kingMoveCauseCheck(finTile, currentPiece, false);
                if (doesKingCanEscapeCheck) {
                    setCheckFlag(false, null);
                }
            } else {
                doesMoveBlockCheck = doesMoveBlockCheck(currentPiece, finTile, true);
                if (doesMoveBlockCheck) {
                    setCheckFlag(false, null);
                }
            }
        }
        if (!doesMoveBlockCheck || !doesKingCanEscapeCheck) {
            return false;
        }

        //it's not allowed do castling being under check and if piece was moved
        boolean castlingIsAllowed = true;
        //System.out.println("castling2 :" + castling);
        if (castling == 0) {
            castlingIsAllowed = castlingIsAllowed(currentPiece, finTile);
        }
        if (!castlingIsAllowed) {
            return false;
        }

        return true;
    }

    // King's stuff
    /**
     *
     * @param finTile
     * @return
     */
    public boolean isKingTile(Tile finTile) {

        boolean isKingTile = false;

        setCurrentKingPosition(!whiteIsActive);
        if (currentKingPosition == finTile) {
            isKingTile = true;
        }
        return isKingTile;

    }

    /**
     *
     * @param color
     */
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

    private List<Tile> getTilesAroundKing() {
        setCurrentKingPosition(!whiteIsActive);
        List<Tile> tempList = null;
        Piece king = (whiteIsActive == true) ? bKing : wKing;
        tempList = tileList.stream()
                .filter((p) -> {
                    boolean x = false;
                    x = Math.abs(p.getX() - currentKingPosition.getX()) <= 1
                            && Math.abs(p.getY() - currentKingPosition.getY()) <= 1;
                    return x;
                })
                .filter(p -> !(p.getX() == currentKingPosition.getX() && p.getY() == currentKingPosition.getY()))
                .collect(Collectors.toList());

        return tempList;
    }

    private boolean kingCanEscapeCheck() {
        boolean b = true;
        List<Tile> tempList = getTilesAroundKing();
        Piece king = (whiteIsActive == true) ? bKing : wKing;

        for (Tile t : tempList) {
            if (king.isMoveAllowed(t) && !kingMoveCauseCheck(t, king, false)) {
                //setCheckFlag(false);
                return b;
            }
        }
        return false;
    }

    private boolean checkCanBeBlocked() {
        //boolean b = true;
        int color = -1;
        if (whiteIsActive && blackIsOnCheck) {
            color = 0;
        }
        if (!whiteIsActive && whiteIsOnChech) {
            color = 1;
        }
        if (color == -1) {
            return false;
        }

        Piece king = (whiteIsActive == true) ? bKing : wKing;
        setCurrentKingPosition(!whiteIsActive);
        //int color = (whiteIsActive == true) ? 0 : 1;
        List<Piece> pieces = (gameManager.getPieces(color));
        if (pieces.size() <= 1) {
            //setCheckFlag(true);
            return false;
        }

        if (attaker.getColor() == color) {
            return false;
        }

        List<Tile> tempList = attaker.getIntermediateTiles(currentKingPosition);
        if (tempList == null) {
            return false;
        }

        tempList.add(attaker.getCurrentTile());

        for (Piece p : pieces) {
            if (p == king) {
                continue;
            }
            for (Tile t : tempList) {
                if (p.isMoveAllowed(t) && doesMoveBlockCheck(p, t, false)) {
                    if (doesMoveCauseCheck(p, t, p.getCurrentTile(), true)) {
//                        attaker = p;
//                        setCheckFlag(true);
                        return false;
                    } else {
                        //setCheckFlag(false);
                        return true;
                    }
                }

            }
        }
        // setCheckFlag(false);
        return false;

    }

    /**
     *
     * @return
     */
    public boolean canEscapeCheck() {

        boolean b = true;
        boolean kingCanEscapeCheck = kingCanEscapeCheck();
        if (kingCanEscapeCheck) {
            return true;
        }

        boolean checkCanBeBlocked = checkCanBeBlocked();
        if (checkCanBeBlocked) {
            return true;
        }

        return false;

    }

    //Castling stuff
    /**
     *
     * @param currentPiece current piece
     * @param finTile
     * @return
     */
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

        if (currentPiece.isWasMoved()) {
            return b = false;
        }

        if (finTile.getX() != 7 && finTile.getX() != 9) {
            return b = false;
        }

        if (kingMoveCauseCheck(finTile, king, true)) {
            return b = false;
        }

        return b;

    }

    private boolean kingMoveCauseCheck(Tile finTile, Piece king, boolean isCastling) {

        boolean b = false;
        List<Piece> pieces = (king.getColor() == 1) ? gameManager.getPieces(0) : gameManager.getPieces(1);

        if (pieces.size() <= 1) {
            return false;
        }

        Tile finishTile = finTile;
        if (isCastling) {
            final int ad = (finTile.getX() == 9) ? 1 : -1;
            Tile kingPositionAfterCastling = tileList.stream()
                    .filter(t -> t.getX() == finTile.getX() + ad && t.getY() == finTile.getY())
                    .findAny()
                    .get();

            finishTile = (kingPositionAfterCastling != null) ? kingPositionAfterCastling : null;
        }

        if (finishTile == null) {
            return false;
        }

        Piece tempPiece = null;
        if (!finishTile.getIsEmpty()) {

            tempPiece = finishTile.getCurrentPiece();
            tempPiece.setCurrentTile(null);
            finishTile.removePiece();

            for (Piece p : pieces) {
                if (p == attaker) {
                    continue;
                }
                if (p == tempPiece) {
                    continue;
                }
                if (p.isMoveAllowed(finishTile)) {
                    //b = true;
                    finishTile.setCurrentPiece(tempPiece);
                    tempPiece.setCurrentTile(finishTile);
                    return true;
                }
            }
            finishTile.setCurrentPiece(tempPiece);
            tempPiece.setCurrentTile(finishTile);

            return b;
        }

        for (Piece p : pieces) {
//            if (p == attaker) {
//                continue;
//            }
            if (p.isMoveAllowed(finishTile)) {
                b = true;
                break;
            }
        }

        return b;

    }

    /**
     *
     * @param finTile
     * @param tags
     * @param removedPieces
     * @return
     */
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
            king.move(finKingTile, tags, removedPieces);
            if (tags.equals("R")) {
                king.setWasMoved(false);
            }
        }

        return finKingTile;

    }

    public void setCheckFlag(boolean flag, Piece attaker) {

        this.attaker = attaker;

        if (flag) {
            this.attaker = attaker;
            if (whiteIsActive) {
                blackIsOnCheck = true;
                whiteIsOnChech = false;
            } else {
                whiteIsOnChech = true;
                blackIsOnCheck = false;

            }
            return;
        }

        if (blackIsOnCheck) {
            blackIsOnCheck = !blackIsOnCheck;
        }
        if (whiteIsOnChech) {
            whiteIsOnChech = !whiteIsOnChech;
        }
    }

    /**
     *
     * @param whiteIsActive
     */
    public void setWhiteIsActive(boolean whiteIsActive) {
        this.whiteIsActive = whiteIsActive;
    }

    public Piece getwKing() {
        return wKing;
    }

    public Piece getbKing() {
        return bKing;
    }

    public void setKing(Piece king) {
        if (king.getColor() == 1 ) {
            this.wKing = king;
        }
        if (king.getColor() == 0 ) {
            this.bKing = king;
        }
    }

}
