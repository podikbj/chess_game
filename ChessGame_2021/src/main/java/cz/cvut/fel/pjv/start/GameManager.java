package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.chessgame.Bishop;
import cz.cvut.fel.pjv.view.BoardPanel;
import cz.cvut.fel.pjv.chessgame.King;
import cz.cvut.fel.pjv.chessgame.Knight;
import cz.cvut.fel.pjv.chessgame.Pawn;
import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Players;
import cz.cvut.fel.pjv.chessgame.Queen;
import cz.cvut.fel.pjv.chessgame.Rook;
import cz.cvut.fel.pjv.chessgame.Tile;
import cz.cvut.fel.pjv.view.StartMenu;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * Represents a game manager. 
 * Performs the function of the MVC controller
 * @author kira
 */
public class GameManager {

    private static GameManager instance = null;

    final private LinkedList<Tile> tileList = new LinkedList<Tile>();

    private List<Tile> tempTileList = new ArrayList<Tile>();
    private List<String> moveSequence = new ArrayList<String>();
    private CheckMatePositionControl checkMatePositionControl;
    private Tile[] lastMove = {null, null};

    private LinkedList<Piece> pieces = new LinkedList<Piece>();

    /**
     *
     * @return single instance of GameManager
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private GameManager() {
    }

    /**
     * Creates the CheckMatePositionControl instance 
     */
    public void initializeCheckMatePositionControl() {

        checkMatePositionControl = new CheckMatePositionControl();

    }

    private Tile createTile(int color, int x, int y) {
        Tile tile = new Tile(color, x, y);
        tileList.add(tile);
        return tile;
    }

    /**
     * Creats chess board tiles
     */
    public void initializeTiles() {

        for (int j = 7; j >= 0; j--) {
            //     for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 12; i++) {
                if (i % 2 != 0) {
                    if (j % 2 != 0) {
                        createTile(0, i, j); //
                    } else {
                        createTile(1, i, j); //
                    }
                }

                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        createTile(0, i, j); //
                    } else {
                        createTile(1, i, j); //
                    }
                }
            }
        }
    }

    private void addTilesToList(int color) {

        if (!StartMenu.isManual && color == 1) {
            tempTileList = tileList.stream()
                    .filter(p -> p.getY() == 0)
                    .sorted(Comparator.comparing(Tile::getX))
                    .collect(toList());
        }
        if (!StartMenu.isManual && color == 0) {
            tempTileList = tileList.stream()
                    .filter(p -> p.getY() == 7)
                    .sorted(Comparator.comparing(Tile::getX))
                    .collect(toList());
        }
        if (StartMenu.isManual && color == 0) {
            tempTileList = tileList.stream()
                    .filter(p -> p.getX() == 2)
                    .sorted(Comparator.comparing(Tile::getY))
                    .collect(toList());
        }
        if (StartMenu.isManual && color == 1) {
            tempTileList = tileList.stream()
                    .filter(p -> p.getX() == 0)
                    .sorted(Comparator.comparing(Tile::getY))
                    .collect(toList());
        }

    }

    /**
     * Invokes move method on piece instance.
     * 
     * @param currentPiece current piece
     * @param finTile finish tile
     * @param tags set of special string symbols like "+, x, 0-0, 0-0-0"
     * @param removedPieces list of removed pieces
     */
    public void move(Piece currentPiece, Tile finTile, HashSet<String> tags, List<Piece> removedPieces) {
        currentPiece.move(finTile, tags, removedPieces);
    }

    /**
     * Invokes doCastling method on checkMatePositionControl instance.
     * @param finTile finish tile
     * @param tags set of special string symbols like "+, x, 0-0, 0-0-0"
     * @param removedPieces list of removed pieces
     * @return finish king position tile after castling
     */
    public Tile doCastling(Tile finTile, HashSet<String> tags, List<Piece> removedPieces) {
        return checkMatePositionControl.doCastling(finTile, tags, removedPieces);
    }

    /**
     * Return true if move is allowed according to the piece rules and
     * if move is allowed according to the chech mate rules
     * @param currentPiece current piece
     * @param finTile finish tile
     * @param startTile start tile
     * @param castling flag if it is castling
     * @return true if move is allowed
     */
    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, Tile startTile, int castling) {
        return currentPiece.isMoveAllowed(finTile)
                && checkMatePositionControl.isMoveAllowed(currentPiece, finTile, startTile, castling);
    }

    private void setPieceOnTile(int color) {
        int k = 0;
        boolean wasRemoved = (!StartMenu.isManual) ? false : true;
        Piece[] tempPieceses = new Piece[8];
        for (Tile el : tempTileList) {

            k = (!StartMenu.isManual) ? el.getX() - 4 : el.getY();

            switch (k) {
                case (0):
                    tempPieceses[k] = new Rook(color, el, wasRemoved);
                    el.setCurrentPiece(tempPieceses[k]);
                    pieces.add(tempPieceses[k]);
                    break;
                case (1):
                    tempPieceses[k] = new Knight(color, el, wasRemoved);
                    el.setCurrentPiece(tempPieceses[k]);
                    pieces.add(tempPieceses[k]);
                    break;
                case (2):
                    tempPieceses[k] = new Bishop(color, el, wasRemoved);
                    el.setCurrentPiece(tempPieceses[k]);
                    pieces.add(tempPieceses[k]);
                    break;
                case (3):
                    tempPieceses[k] = new Queen(color, el, wasRemoved);
                    el.setCurrentPiece(tempPieceses[k]);
                    pieces.add(tempPieceses[k]);
                    break;
                case (4):
                    tempPieceses[k] = new King(color, el, wasRemoved);
                    el.setCurrentPiece(tempPieceses[k]);
                    pieces.add(tempPieceses[k]);
                    break;
                case (5):
                    tempPieceses[k] = new Bishop(color, el, wasRemoved);
                    el.setCurrentPiece(tempPieceses[k]);
                    pieces.add(tempPieceses[k]);
                    break;
                case (6):
                    tempPieceses[k] = new Knight(color, el, wasRemoved);
                    el.setCurrentPiece(tempPieceses[k]);
                    pieces.add(tempPieceses[k]);
                    break;
                case (7):
                    tempPieceses[k] = new Rook(color, el, wasRemoved);
                    el.setCurrentPiece(tempPieceses[k]);
                    pieces.add(tempPieceses[k]);
                    break;
            }

        }

    }

    /**
     * Creates pieces and plases them on tiles
     */
    public void initializePieces() {

        final int black = 0;
        if (!StartMenu.isManual) {
            tileList.stream().filter(t -> t.getY() == 6 && t.getX() > 3).forEach((t)
                    -> {
                t.setCurrentPiece(new Pawn(black, t, false));
                pieces.add(t.getCurrentPiece());
            }
            );
        } else {
            tileList.stream().filter(t -> t.getX() == 3).forEach((t)
                    -> {
                t.setCurrentPiece(new Pawn(black, t, true));
            }
            );
        }

        addTilesToList(black);
        setPieceOnTile(black);

        final int white = 1;
        if (!StartMenu.isManual) {
            tileList.stream().filter(t -> t.getY() == 1 && t.getX() > 3).forEach((t)
                    -> {
                t.setCurrentPiece(new Pawn(white, t, false));
                pieces.add(t.getCurrentPiece());
            }
            );
        } else {
            tileList.stream().filter(t -> t.getX() == 1).forEach((t)
                    -> {
                t.setCurrentPiece(new Pawn(white, t, true));
                pieces.add(t.getCurrentPiece());
            }
            );
        }

        tempTileList.clear();
        addTilesToList(white);
        setPieceOnTile(white);

    }

    /**
     *
     * @return list of all the tiles
     */
    public LinkedList<Tile> getTileList() {
        return tileList;
    }

//    public List<String> getMoveSequence() {
//        return moveSequence;
//    }
    /**
     * Exchanges the pawn for a selected piece 
     * @param currentTile current piece tile
     * @param whiteIsActive white is on tag
     * @param x int index of the piece that player wants to get in exchange for passed pawn
     */
    //public void changePawn(Piece currentPiece, Tile currentTile,
    public void changePawn(Tile currentTile,
            boolean whiteIsActive, int x, HashSet<String> tags) {

        int color = (whiteIsActive == true) ? 1 : 0;
        Piece newPiece = null;
        switch (x) {
            case 0:
                newPiece = new Queen(color, currentTile, false);
                break;
            case 1:
                newPiece = new Rook(color, currentTile, false);
                break;
            case 2:
                newPiece = new Bishop(color, currentTile, false);
                break;
            case 3:
                newPiece = new Knight(color, currentTile, false);
                break;
            case 4:

                break;
        }
        if (newPiece != null) {
            tags.add(newPiece.toString());
        }

        //Piece newPiece = currentTile.getCurrentPiece();
        Tile currentKingPosition = (whiteIsActive == true)
                ? checkMatePositionControl.getbKing().getCurrentTile()
                : checkMatePositionControl.getwKing().getCurrentTile();
        if (newPiece.isMoveAllowed(currentKingPosition)) {
            checkMatePositionControl.setCheckFlag(true, newPiece);
            tags.add("+");
            int a = 0;
        }

        currentTile.removePiece();
        //currentPiece.setCurrentTile(null);
        //pieces.remove(currentPiece);
        //currentPiece = null;

        newPiece.setCurrentTile(currentTile);
        currentTile.setCurrentPiece(newPiece);
        pieces.add(newPiece);

        //return newPiece;
    }

    /**
     *
     * @return instance of checkMatePositionControl
     */
    public CheckMatePositionControl getCheckMatePositionControl() {
        return checkMatePositionControl;
    }

    /**
     *
     * @return list of all pieces that wasn't removed
     */
    public List<Piece> getPieces() {
        List<Piece> p = null;
        p = pieces.stream()
                .filter(s -> !s.isWasRemoved())
                .collect(toList());
        return p;
    }

    /**
     *
     * @param color
     * @return list of one color pieces that wasn't removed
     */
    public List<Piece> getPieces(int color) {
        List<Piece> p = null;
        p = pieces.stream()
                .filter(s -> !s.isWasRemoved())
                .filter(s -> s.getColor() == color)
                .collect(toList());
        return p;
    }

    /**
     *
     * @return list of removed pieces from chess board
     */
    public List<Piece> getRemovedPieces() {
        List<Piece> removedPieceses = null;
        removedPieceses = pieces.stream()
                .filter(p -> p.isWasRemoved())
                .collect(toList());
        //removedPieceses = (color == 0) ? bRemovedPieceses : wRemovedPieceses;
        return removedPieceses;
    }

    /**
     *
     * @param p
     * @return
     */
    public List<Tile> getAllowedTiles(Piece p) {

        List<Tile> l = new ArrayList<Tile>();
        final int currentColor = p.getColor();

        List<Tile> tempTList = tileList.stream()
                .filter(t -> t.getX() > 3)
                .collect(toList());

        boolean b = false;
        for (Tile t : tempTList) {
            if (!t.getIsEmpty() && t.getCurrentPiece().getColor() == currentColor) {
                continue;
            }
            b = isMoveAllowed(p, t, p.getCurrentTile(), 1);
            if (b) {
                l.add(t);
            }
        }

        return l;
    }

    /**
     *
     * @return last move start tile and finish tile array
     */
    public Tile[] getLastMove() {
        return lastMove;
    }

    /**
     * Adds tiles to array
     * @param t last mova start/finish tile
     * @param ind int index: 0 - start tile, 1 - finish tile
     */
    public void addLastMove(Tile t, int ind) {
        lastMove[ind] = t;
    }

}
