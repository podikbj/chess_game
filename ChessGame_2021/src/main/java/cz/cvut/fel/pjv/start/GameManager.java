package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.chessgame.Bishop;
import cz.cvut.fel.pjv.view.BoardPanel;
import cz.cvut.fel.pjv.chessgame.King;
import cz.cvut.fel.pjv.chessgame.Knight;
import cz.cvut.fel.pjv.chessgame.Pawn;
import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Player;
import cz.cvut.fel.pjv.chessgame.Queen;
import cz.cvut.fel.pjv.chessgame.Rook;
import cz.cvut.fel.pjv.chessgame.Tile;
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
    private List<Tile> tempTileList = new ArrayList<Tile>();
    private List<String> moveSequence = new ArrayList<String>();
    private CheckMatePositionControl checkMatePositionControl;

    private LinkedList<Piece> wPieceses = new LinkedList<Piece>();
    private LinkedList<Piece> bPieceses = new LinkedList<Piece>();

    //private Tile[][] tileArray = new Tile[8][8];
    //private static Player activePlayer;
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
            // activePlayer = player;
        }
        return instance;
    }

    private GameManager() {
        initializeTiles();
        initializePieces();
<<<<<<< HEAD
        //initializeCheckMatePositionControl();
    }

    public void initializeCheckMatePositionControl() {
<<<<<<< HEAD
        checkMatePositionControl = new CheckMatePositionControl();

=======
=======
        initializeCheckMatePositionControl();
    }
    
    private void initializeCheckMatePositionControl() {
>>>>>>> 12bb173c7ad0468d60ebd2210041c3a060a0878f
       checkMatePositionControl = new CheckMatePositionControl(); 
       
>>>>>>> 7daedc57f25617f4a43287d75ee9d715665b29be
    }

    private Tile createTile(int color, int x, int y) {
        Tile tile = new Tile(color, x, y);
        tileList.add(tile);
        //tileArray[x][y] = tile;
        return tile;
    }

    private void initializeTiles() {
        // int color = 0;
        for (int j = 7; j >= 0; j--) {
            for (int i = 0; i < 8; i++) {
                if (i % 2 != 0) {
                    if (j % 2 != 0) {
                        createTile(0, i, j);
                    } else {
                        createTile(1, i, j);
                    }
                }

                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        createTile(0, i, j);
                    } else {
                        createTile(1, i, j);
                    }
                }
            }
        }
    }

    private void addTilesToList(int color) {

        if (color == 0) {
            tempTileList = tileList.stream()
                    .filter(p -> p.getY() == 7)
                    .sorted(Comparator.comparing(Tile::getX))
                    .collect(toList());
        }

        if (color == 1) {
            tempTileList = tileList.stream()
                    .filter(p -> p.getY() == 0)
                    .sorted(Comparator.comparing(Tile::getX))
                    .collect(toList());
        }
    }

    public void move(Piece currentPiece, Tile finTile) {
        currentPiece.move(finTile);
    }

<<<<<<< HEAD
    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, Tile startTile, boolean whiteIsActive, int castling) {
        return currentPiece.isMoveAllowed(finTile)
                && checkMatePositionControl.isMoveAllowed(currentPiece, finTile, startTile, whiteIsActive, castling);
=======
<<<<<<< HEAD
    public boolean isMoveAllowed(Piece currentPiece, Tile finTile, boolean whiteIsActive) {
        return currentPiece.isMoveAllowed(finTile) && checkMatePositionControl.isMoveAllowed(currentPiece, finTile, whiteIsActive);
=======
    public boolean isMoveAllowed(Piece currentPiece, Tile finTile) {
        return currentPiece.isMoveAllowed(finTile);
>>>>>>> 12bb173c7ad0468d60ebd2210041c3a060a0878f
>>>>>>> 7daedc57f25617f4a43287d75ee9d715665b29be
    }

//    public boolean isChecked(Piece currentPiece, Tile finTile, boolean whiteIsActive) {
//        return CheckMatePositionControl.isChecked(currentPiece, finTile, whiteIsActive);
//    }
    private void setPieceOnTile(int color) {
        int x = 0;
        Piece[] tempPieceses = new Piece[8];
        for (Tile el : tempTileList) {
            x = el.getX();
            switch (x) {
                case (0):
                    tempPieceses[x] = new Rook(color);
                    el.setCurrentPiece(tempPieceses[x]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[x]);
                    } else {
                        bPieceses.add(tempPieceses[x]);
                    }
                    break;
                case (1):
                    tempPieceses[x] = new Knight(color);
                    el.setCurrentPiece(tempPieceses[x]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[x]);
                    } else {
                        bPieceses.add(tempPieceses[x]);
                    }
                    break;
                case (2):
                    tempPieceses[x] = new Bishop(color);
                    el.setCurrentPiece(tempPieceses[x]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[x]);
                    } else {
                        bPieceses.add(tempPieceses[x]);
                    }
                    break;
                case (3):
                    tempPieceses[x] = new Queen(color);
                    el.setCurrentPiece(tempPieceses[x]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[x]);
                    } else {
                        bPieceses.add(tempPieceses[x]);
                    }
                    break;
                case (4):
                    tempPieceses[x] = new King(color);
                    el.setCurrentPiece(tempPieceses[x]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[x]);
                    } else {
                        bPieceses.add(tempPieceses[x]);
                    }
                    break;
                case (5):
                    tempPieceses[x] = new Bishop(color);
                    el.setCurrentPiece(tempPieceses[x]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[x]);
                    } else {
                        bPieceses.add(tempPieceses[x]);
                    }
                    break;
                case (6):
                    tempPieceses[x] = new Knight(color);
                    el.setCurrentPiece(tempPieceses[x]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[x]);
                    } else {
                        bPieceses.add(tempPieceses[x]);
                    }
                    break;
                case (7):
                    tempPieceses[x] = new Rook(color);
                    el.setCurrentPiece(tempPieceses[x]);
                    if (color == 1) {
                        wPieceses.add(tempPieceses[x]);
                    } else {
                        bPieceses.add(tempPieceses[x]);
                    }
                    break;
            }
        }

//        if (color == 1) {
//            wPieceses = Arrays.copyOf(tempPieceses, tempPieceses.length);
//        } else {
//            bPieceses = Arrays.copyOf(tempPieceses, tempPieceses.length);
//        }
    }

    private void initializePieces() {

        final int black = 0;
        tileList.stream().filter(p -> p.getY() == 6).forEach(p -> p.setCurrentPiece(new Pawn(black)));
        addTilesToList(black);
        setPieceOnTile(black);

        final int white = 1;
        tileList.stream().filter(p -> p.getY() == 1).forEach(p -> p.setCurrentPiece(new Pawn(white)));
        tempTileList.clear();
        addTilesToList(white);
        setPieceOnTile(white);

        tileList.stream().filter(p -> p.getIsEmpty() == false).forEach(p -> p.getCurrentPiece().setCurrentTile(p));

    }

    private void createMoves(Tile currentTile, Tile finTile) {
        Piece currentPiece = currentTile.getCurrentPiece();
        if (currentPiece.getColor() == 1) {
            //whiteInCheck();
            //whiteCheckMated();
        }
        if (currentPiece.getColor() == 0) {
            //blackInCheck();
            //blackCheckMated();
        }

        if (currentPiece.isMoveAllowed(finTile)) {
            currentPiece.move(finTile);
        }
    }

    public LinkedList<Tile> getTileList() {
        return tileList;
    }

    public List<String> getMoveSequence() {
        return moveSequence;
    }

<<<<<<< HEAD
    public void changePawn(Piece currentPiece, Tile currentTile, boolean whiteIsActive, int x) { 
        int color = (whiteIsActive == true) ? 1 : 0;
        Piece newPiece = null;
        switch (x) {
            case 0:
                newPiece = new Queen(color);
                break;
            case 1:
                newPiece = new Rook(color);
                break;
            case 2:
                newPiece = new Bishop(color);
                break;
            case 3:
                newPiece = new Knight(color);
                break;
            case 4:
 
                break;
        }
        currentTile.setCurrentPiece(newPiece);
    }
    

=======
//    public Piece getwKing() {
//        return wKing;
//    }
//
//    public Piece getbKing() {
//        return bKing;
//    }
>>>>>>> 7daedc57f25617f4a43287d75ee9d715665b29be

//    private void createReport() throws IOException {
//        Date dateNow = new Date();
//        SimpleDateFormat formatDate = new SimpleDateFormat("YYYY.MM.DD");
//
//        File myFile = new File(formatDate + "Game.txt");
//
//        if (ReportIsFirstInUse == false) {
//            try {
//                PrintWriter writer = new PrintWriter(myFile);
//                logger.info("Clearing the contents of the Reports.txt file starts");
//                writer.print("");
//                writer.close();
//            } catch (Exception e) {
//                logger.log(Level.SEVERE, "Error occur in file cleaning", e);
//            }
//            ReportIsFirstInUse = true;
//        }
//
//        try {
//            PrintWriter writer = new PrintWriter(myFile);
//            logger.info("Printing GameReport to file starts");
//
////            writer.println("============================================================");
////            writer.println("HOUSE CONFIGURATION REPORT");
////            writer.println("============================================================");
//            int counter = 1;
//            for (String str : moveSequence) {
//                str = counter + ". " + str;
//                writer.println(str);
//                counter += 1;
//            }
//            writer.flush();
//            writer.close();
//        } catch (IOException ex) {
//            logger.log(Level.SEVERE, "Error occur in printing GameReport", ex);
//            ex.printStackTrace();
//        }
//
//    }
//    public Tile[][] getTileArray() {
//        return tileArray;
//    }
    public CheckMatePositionControl getCheckMatePositionControl() {
        return checkMatePositionControl;
    }

    public LinkedList<Piece> getwPieceses() {
        return wPieceses;
    }

    public LinkedList<Piece> getbPieceses() {
        return bPieceses;
    }

}
