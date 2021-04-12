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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import static java.util.stream.Collectors.toList;

public class GameManager {

    private static GameManager instance = null;

    private LinkedList<Tile> tileList = new LinkedList<Tile>();
    private LinkedList<Tile> leftSideTileList = new LinkedList<Tile>();

    private List<Tile> tempTileList = new ArrayList<Tile>();
    private List<String> moveSequence = new ArrayList<String>();
    private CheckMatePositionControl checkMatePositionControl;

    private LinkedList<Piece> wPieceses = new LinkedList<Piece>();
    private LinkedList<Piece> bPieceses = new LinkedList<Piece>();

    private LinkedList<Piece> wRemovedPieceses = new LinkedList<Piece>();
    private LinkedList<Piece> bRemovedPieceses = new LinkedList<Piece>();

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private GameManager() {
        initializeTiles();
        initializePieces();
    }

    public void initializeCheckMatePositionControl() {
        checkMatePositionControl = new CheckMatePositionControl();

    }

    private Tile createTile(int color, int x, int y) {
        Tile tile = new Tile(color, x, y);
        tileList.add(tile);
        return tile;
    }

    private void initializeTiles() {

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

    public void move(Piece currentPiece, Tile finTile) {
        currentPiece.move(finTile);
    }

    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, Tile startTile, boolean whiteIsActive, int castling) {
        return checkMatePositionControl.isMoveAllowed(currentPiece, finTile, startTile, whiteIsActive, castling)
                && currentPiece.isMoveAllowed(finTile);
    }

    private void setPieceOnTile(int color) {
        int k = 0;
        Piece[] tempPieceses = new Piece[8];
        for (Tile el : tempTileList) {

            k = (!StartMenu.isManual) ? el.getX() - 4 : el.getY();

            switch (k) {
                case (0):
                    tempPieceses[k] = new Rook(color, el);
                    el.setCurrentPiece(tempPieceses[k]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[k]);
                    } else {
                        bPieceses.add(tempPieceses[k]);
                    }
                    break;
                case (1):
                    tempPieceses[k] = new Knight(color, el);
                    el.setCurrentPiece(tempPieceses[k]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[k]);
                    } else {
                        bPieceses.add(tempPieceses[k]);
                    }
                    break;
                case (2):
                    tempPieceses[k] = new Bishop(color, el);
                    el.setCurrentPiece(tempPieceses[k]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[k]);
                    } else {
                        bPieceses.add(tempPieceses[k]);
                    }
                    break;
                case (3):
                    tempPieceses[k] = new Queen(color, el);
                    el.setCurrentPiece(tempPieceses[k]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[k]);
                    } else {
                        bPieceses.add(tempPieceses[k]);
                    }
                    break;
                case (4):
                    tempPieceses[k] = new King(color, el);
                    el.setCurrentPiece(tempPieceses[k]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[k]);
                    } else {
                        bPieceses.add(tempPieceses[k]);
                    }
                    break;
                case (5):
                    tempPieceses[k] = new Bishop(color, el);
                    el.setCurrentPiece(tempPieceses[k]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[k]);
                    } else {
                        bPieceses.add(tempPieceses[k]);
                    }
                    break;
                case (6):
                    tempPieceses[k] = new Knight(color, el);
                    el.setCurrentPiece(tempPieceses[k]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[k]);
                    } else {
                        bPieceses.add(tempPieceses[k]);
                    }
                    break;
                case (7):
                    tempPieceses[k] = new Rook(color, el);
                    el.setCurrentPiece(tempPieceses[k]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[k]);
                    } else {
                        bPieceses.add(tempPieceses[k]);
                    }
                    break;
            }
        }

    }

    private void initializePieces() {

        final int black = 0;
        if (!StartMenu.isManual) {
            tileList.stream().filter(t -> t.getY() == 6 && t.getX() > 3).forEach((t)
                    -> {
                t.setCurrentPiece(new Pawn(black, t));
                bPieceses.add(t.getCurrentPiece());
            }
            );
        } else {
            tileList.stream().filter(t -> t.getX() == 3).forEach((t)
                    -> {
                t.setCurrentPiece(new Pawn(black, t));
                bRemovedPieceses.add(t.getCurrentPiece());
            }
            );
        }

        addTilesToList(black);
        setPieceOnTile(black);

        final int white = 1;
        if (!StartMenu.isManual) {
            tileList.stream().filter(t -> t.getY() == 1 && t.getX() > 3).forEach((t)
                    -> {
                t.setCurrentPiece(new Pawn(white, t));
                wPieceses.add(t.getCurrentPiece());
            }
            );
        } else {
            tileList.stream().filter(t -> t.getX() == 1).forEach((t)
                    -> {
                t.setCurrentPiece(new Pawn(white, t));
                wRemovedPieceses.add(t.getCurrentPiece());
            }
            );
        }

        tempTileList.clear();
        addTilesToList(white);
        setPieceOnTile(white);

    }

    public LinkedList<Tile> getTileList() {
        return tileList;
    }

    public List<String> getMoveSequence() {
        return moveSequence;
    }

    public void changePawn(Piece currentPiece, Tile currentTile, boolean whiteIsActive, int x) {
        int color = (whiteIsActive == true) ? 1 : 0;
        Piece newPiece = null;
        switch (x) {
            case 0:
                newPiece = new Queen(color, currentTile);
                break;
            case 1:
                newPiece = new Rook(color, currentTile);
                break;
            case 2:
                newPiece = new Bishop(color, currentTile);
                break;
            case 3:
                newPiece = new Knight(color, currentTile);
                break;
            case 4:

                break;
        }
        currentTile.setCurrentPiece(newPiece);
    }

    public CheckMatePositionControl getCheckMatePositionControl() {
        return checkMatePositionControl;
    }

    public LinkedList<Piece> getPieces(int color) {
        LinkedList<Piece> pieceses = null;
        pieceses = (color == 0) ? bPieceses : wPieceses;
        return pieceses;
    }

    public LinkedList<Piece> getRemovedPieceses(int color) {
        LinkedList<Piece> removedPieceses = null;
        removedPieceses = (color == 0) ? bRemovedPieceses : wRemovedPieceses;
        return removedPieceses;
    }

    public LinkedList<Tile> getLeftSideTileList() {
        return leftSideTileList;
    }

}
