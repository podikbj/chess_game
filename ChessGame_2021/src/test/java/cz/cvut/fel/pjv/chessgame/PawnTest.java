package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import java.util.HashSet;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.LinkedList;
import java.util.Stack;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class PawnTest {

    Piece wPawn_d2 = null;
    Piece wPawn_e2 = null;
    Piece bPawn_d7 = null;
    Piece bPawn_e7 = null;

    Tile startTile_d2 = new Tile(0, 3, 1);
    Tile startTile_e2 = new Tile(1, 4, 1);
    Tile startTile_d7 = new Tile(1, 3, 6);
    Tile startTile_e7 = new Tile(0, 4, 6);

    Tile finTile_d3 = new Tile(1, 3, 2);
    Tile finTile_e4 = new Tile(1, 4, 3);
    Tile finTile_e5 = new Tile(0, 4, 4);
    Tile finTile_d5 = new Tile(1, 3, 4);
    
    private HashSet<String> tags = new HashSet<>();

    @Mock
    Piece piece;

    @BeforeEach
    public void setUp() {

        wPawn_d2 = new Pawn(1, startTile_d2, false);
        wPawn_e2 = new Pawn(1, startTile_e2, false);
        bPawn_d7 = new Pawn(0, startTile_d7, false);
        bPawn_e7 = new Pawn(0, startTile_e7, false);

        startTile_d2.setCurrentPiece(wPawn_d2);
        startTile_e2.setCurrentPiece(wPawn_e2);
        startTile_d7.setCurrentPiece(bPawn_d7);
        startTile_e7.setCurrentPiece(bPawn_e7);

    }

//    @Test
//    public void isTheSameColorTest_IfPieceTryTakePieceSameColor_ReturnTrue() {
//
//        wPawn_e2.move(finTile_e4, tags);
//        bPawn_e7.move(finTile_e5, tags);
//        wPawn_d2.move(finTile_d3, tags);
//        bPawn_d7.move(finTile_d5, tags);
//
//        boolean result = wPawn_d2.isTheSameColor(finTile_e4);
//
//        assertTrue(result);

    }

//    @Test
//    public void isTheSameColorTest_IfPieceTryTakePieceSameColor_ReturnFalse() {
//
//        wPawn_e2.move(finTile_e4, tags);
//        bPawn_d7.move(finTile_d5, tags);
//
//        boolean result = wPawn_e2.isTheSameColor(finTile_d5);
//
//        assertFalse(result);
//    }

    @Test
//    public void removePieceFromArrayTest_ReturtTrueIfPieceNotFoundInArray() {

//        GameManager gameManager = GameManager.getInstance();
//        LinkedList<Tile> tileList = gameManager.getTileList();
//        LinkedList<Piece> pieces = gameManager.getPieces(1);
//
//        
//        Tile finTile = null;
//        Piece currentPiece = null;
//
//        for (Tile t : tileList) {
//            if (t.getX() == 4 && t.getY() == 1) {
//                finTile = t;
//                currentPiece = t.getCurrentPiece();
//            }
//        }
//
//        currentPiece.move(finTile);
//        boolean result = currentPiece.removePieceFromArray(finTile);
//
//        boolean expectedResult = pieces.contains(currentPiece);
//
//        assertEquals(expectedResult, result);
 //   }

    @Test
 //   public void removePieceFromArrayTest_ReturtFalseIfTileIsEmpty() {

//        GameManager gameManager = GameManager.getInstance();
//
//        LinkedList<Tile> tileList = gameManager.getTileList();
//        LinkedList<Piece> pieces = gameManager.getPieces(1);
//
//        Tile finTile = null;
//        Piece currentPiece = null;
//
//        for (Tile t : tileList) {
//            if (t.getX() == 4 && t.getY() == 1) {
//                currentPiece = t.getCurrentPiece();
//            }
//            if (t.getX() == 4 && t.getY() == 2) {
//                finTile = t;
//            }
//        }
//
//        boolean result = currentPiece.removePieceFromArray(finTile);
//        boolean expectedResult = pieces.contains(currentPiece);
//
//        assertEquals(expectedResult, result);
//    }
//
//    @Test
//    public void isMoveAllowedTest_e2e4_AllTilesBetweenAreEmptyWasNotMovedReturnTrue() {
//
//        boolean result = wPawn_e2.isMoveAllowed(finTile_e4);
//
//        assertTrue(result);
//
//    }
//
//    @Test
//    public void isMoveAllowedTest_e2e4_AllTilesBetweenAreEmptyWasMovedReturnFalse() {
//        wPawn_e2.wasMoved = true;
//        boolean result = wPawn_e2.isMoveAllowed(finTile_e4);
//
//        assertFalse(result);
//
//    }

//    @Test
//    public void isMoveAllowedTest_d2d4_TilesBetweenNotEmptyWasNotMovedReturnFalse() {
//
//        Tile finTile_d4 = new Tile(0, 3, 3);
//        finTile_d3.setCurrentPiece(piece);
//        boolean result = wPawn_d2.isMoveAllowed(finTile_d4);
//
//        assertFalse(result);
//
//    }
//}
