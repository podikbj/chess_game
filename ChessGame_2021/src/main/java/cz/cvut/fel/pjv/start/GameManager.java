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
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import static java.util.stream.Collectors.toList;

public class GameManager {

    private static GameManager instance = null;

    private List<Tile> tileList = new ArrayList<Tile>();
    private List<Tile> tempTileList = new ArrayList<Tile>();
    private List<String> moveSequence = new ArrayList<String>();
    private static Logger logger = Logger.getLogger(GameManager.class.getName());
    private static Handler fileHandler = null;
    private Piece[] wPieceses = new Piece[8];
    private Piece[] bPieceses = new Piece[8];
    private boolean ReportIsFirstInUse = false;
    private Tile[][] tileArray = new Tile[8][8];
    //private static Player activePlayer;

    public static GameManager getInstance() throws IOException {
        if (instance == null) {
            instance = new GameManager();
            // activePlayer = player;
        }
        return instance;
    }

    private Tile createTile(int color, int x, int y) {
        Tile tile = new Tile(color, x, y);
        tileList.add(tile);
        tileArray[x][y] = tile;
        return tile;
    }

    private void initializeTiles() {
        int color = 0;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
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

    private void addPiecesToList(int color) {

        if (color == 0) {
            tempTileList = tileList.stream()
                    .filter(p -> p.getY() == 8)
                    .sorted(Comparator.comparing(Tile::getX))
                    .collect(toList());
        }

        if (color == 1) {
            tempTileList = tileList.stream()
                    .filter(p -> p.getY() == 1)
                    .sorted(Comparator.comparing(Tile::getX))
                    .collect(toList());
        }
    }

    private void setPieceOnTile(int color) {
        int x = 1;
        Piece[] tempPieceses = new Piece[8];
        for (Tile el : tempTileList) {
            x = el.getX();
            switch (x) {
                case (1):
                    tempPieceses[x - 1] = new Rook(color);
                    el.setCurrentPiece(tempPieceses[x - 1]);
                    break;
                case (2):
                    tempPieceses[x - 1] = new Knight(color);
                    el.setCurrentPiece(tempPieceses[x - 1]);
                    break;
                case (3):
                    tempPieceses[x - 1] = new Bishop(color);
                    el.setCurrentPiece(tempPieceses[x - 1]);
                    break;
                case (4):
                    tempPieceses[x - 1] = new King(color);
                    el.setCurrentPiece(tempPieceses[x - 1]);
                    break;
                case (5):
                    tempPieceses[x - 1] = new Queen(color);
                    el.setCurrentPiece(tempPieceses[x - 1]);
                    break;
                case (6):
                    tempPieceses[x - 1] = new Bishop(color);
                    el.setCurrentPiece(tempPieceses[x - 1]);
                    break;
                case (7):
                    tempPieceses[x - 1] = new Knight(color);
                    el.setCurrentPiece(tempPieceses[x - 1]);
                    break;
                case (8):
                    tempPieceses[x - 1] = new Rook(color);
                    el.setCurrentPiece(tempPieceses[x - 1]);
                    break;
            }
        }

        if (color == 1) {
            wPieceses = Arrays.copyOf(tempPieceses, tempPieceses.length);
        } else {
            bPieceses = Arrays.copyOf(tempPieceses, tempPieceses.length);
        }

    }

    private void initializePieces() {

        final int black = 0;
        tileList.stream().filter(p -> p.getY() == 7).forEach(p -> p.setCurrentPiece(new Pawn(black)));
        addPiecesToList(black);
        setPieceOnTile(black);

        final int white = 1;
        tileList.stream().filter(p -> p.getY() == 2).forEach(p -> p.setCurrentPiece(new Pawn(white)));
        tempTileList.clear();
        setPieceOnTile(white);

    }

    private void createMoves(Tile currentTile, Tile finTile) {
        Piece currentPiece = currentTile.getCurrentPiece();
        if (currentPiece.getColor() == 1) {
            //whiteInCheck();
            //whiteCheckMated();
        }
        if (currentPiece.getColor() == 1) {
            //blackInCheck();
            //blackCheckMated();
        }

        if (currentPiece.isMoveLegal(finTile)) {
            currentPiece.move(finTile);
        }
    }

    public List<Tile> getTileList() {
        return tileList;
    }

    public List<String> getMoveSequence() {
        return moveSequence;
    }

    private void createReport() throws IOException {
        Date dateNow = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("YYYY.MM.DD");

        File myFile = new File(formatDate + "Game.txt");

        if (ReportIsFirstInUse == false) {
            try {
                PrintWriter writer = new PrintWriter(myFile);
                logger.info("Clearing the contents of the Reports.txt file starts");
                writer.print("");
                writer.close();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error occur in file cleaning", e);
            }
            ReportIsFirstInUse = true;
        }

        try {
            PrintWriter writer = new PrintWriter(myFile);
            logger.info("Printing GameReport to file starts");

            writer.println("============================================================");
            writer.println("HOUSE CONFIGURATION REPORT");
            writer.println("============================================================");
            int counter = 1;
            for (String str : moveSequence) {
                str = counter + ". " + str;
                writer.println(str);
                counter += 1;
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error occur in printing GameReport", ex);
            ex.printStackTrace();
        }

    }

}
