package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PawnTest {

    private static GameManager gameManager;
    private static LinkedList<Tile> tileList;
    private static HashSet<String> tags = new HashSet<String>();
    private static LinkedList<Piece> lastRemoved = new LinkedList<>();

    @BeforeAll
    public static void setUp() {

        gameManager = GameManager.getInstance();
        gameManager.initializeTiles();
        tileList = gameManager.getTileList();
//        HashSet<String> tags = new HashSet<String>();
//        List<Piece> lastRemoved = new ArrayList<>();

    }

    @Test
    public void moveTest_IfMovIsEnPassantThenPawnWasCapture_ReturnTrue() {
//        HashSet<String> tags = new HashSet<String>();
//        List<Piece> lastRemoved = new ArrayList<>();

        Tile startWhPawn = tileList.stream().filter(p -> p.getX() == 4 && p.getY() == 3)
                .findAny().get();
        Tile finish1_WhPawn = tileList.stream().filter(p -> p.getX() == 4 && p.getY() == 4)
                .findAny().get();
        Piece wPawn = new Pawn(1, startWhPawn, false);
        startWhPawn.setCurrentPiece(wPawn);
        Tile finish2_WhPawn = tileList.stream().filter(p -> p.getX() == 5 && p.getY() == 5)
                .findAny().get();

        Tile startBlPawn = tileList.stream().filter(p -> p.getX() == 5 && p.getY() == 6)
                .findAny().get();
        Tile finishBlPawn = tileList.stream().filter(p -> p.getX() == 5 && p.getY() == 4)
                .findAny().get();
        Piece bPawn = new Pawn(0, startBlPawn, false);
        startBlPawn.setCurrentPiece(bPawn);

        assertTrue(finish2_WhPawn.getIsEmpty());

        if (wPawn.isMoveAllowed(finish1_WhPawn)) {
            gameManager.move(wPawn, finish1_WhPawn, tags, lastRemoved);
            gameManager.addLastMove(startWhPawn, 0);
            gameManager.addLastMove(finish1_WhPawn, 1);
        }

        if (bPawn.isMoveAllowed(finishBlPawn)) {
            gameManager.move(bPawn, finishBlPawn, tags, lastRemoved);
            gameManager.addLastMove(startBlPawn, 0);
            gameManager.addLastMove(finishBlPawn, 1);
        }
        if (wPawn.isMoveAllowed(finish2_WhPawn)) {
            gameManager.move(wPawn, finish2_WhPawn, tags, lastRemoved);
            gameManager.addLastMove(finish1_WhPawn, 0);
            gameManager.addLastMove(finish2_WhPawn, 1);
        }

        assertEquals(wPawn.getCurrentTile(), finish2_WhPawn);
        assertEquals(finish2_WhPawn.getCurrentPiece(), wPawn);

        assertNull(finishBlPawn.getCurrentPiece());
        assertTrue(bPawn.wasRemoved);

    }

    @Test
    public void moveTest_IfKingIsOnCheck_ThenReturnTrue() {

//        Tile startWhPawn = tileList.stream().filter(p -> p.getX() == 7 && p.getY() == 1)
//                .findAny().get();
//        Piece wPawn = startWhPawn.getCurrentPiece();
//        Tile finish_WhPawn = tileList.stream().filter(p -> p.getX() == 7 && p.getY() == 3)
//                .findAny().get();
//
//        Tile tile = tileList.stream().filter(p -> p.getX() == 8 && p.getY() == 7)
//                .findAny().get();
//        Piece bKing = tile.getCurrentPiece();
//        
//        kingTile.setCurrentPiece(bKing);
//                startWhPawn.setCurrentPiece(wPawn);
//        wPawn.setCurrentTile(startWhPawn);
//
//        if (wPawn.isMoveAllowed(finish_WhPawn)) {
//            gameManager.move(wPawn, finish_WhPawn, tags, lastRemoved);
//        }

    }

}
