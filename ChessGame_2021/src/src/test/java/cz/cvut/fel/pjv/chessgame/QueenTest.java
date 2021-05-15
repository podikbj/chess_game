package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.chessgame.*;
import cz.cvut.fel.pjv.start.*;

import net.bytebuddy.build.Plugin;
import org.junit.After;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class QueenTest {

    @Mock
    Tile tile;

    @Mock
    Tile currentTile;

    @Mock
    Tile finTile;

    private static GameManager gameManager;
    private static LinkedList<Tile> tileList;
    private static Tile startTile;
    private static Tile finishTile;

    @BeforeAll
    public static void setUp() {
        gameManager = GameManager.getInstance();
        gameManager.initializeTiles();
        tileList = gameManager.getTileList();
        startTile = tileList.stream().filter(p -> p.getX() == 4 && p.getY() == 2)
                .findAny().get();
        finishTile = tileList.stream().filter(p -> p.getX() == 11 && p.getY() == 2)
                .findAny().get();
    }

    @Test
    public void mockedConstructorQueen() {
        try (MockedConstruction mocked = mockConstruction(Queen.class)) {
            Tile tile = new Tile(1, 3, 0);
            Piece queen = new Queen(1, tile, false);
            when(queen.toString()).thenReturn("Q");
            assertEquals("Q", queen.toString());
            // verify(queen).toString();
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    public void isDiagonalMoveTest_ReturnTrue() {
        tile = mock(Tile.class);
        currentTile = mock(Tile.class);
        Piece queen = new Queen(1, currentTile, false);
        when(currentTile.getX()).thenReturn(7);
        when(currentTile.getY()).thenReturn(0);

        when(tile.getX()).thenReturn(11);
        when(tile.getY()).thenReturn(4);

        assertTrue(queen.isDiagonalMove(tile));
    }

    @Test
    public void isDiagonalMoveTest_ReturnFalse() {
        tile = mock(Tile.class);
        currentTile = mock(Tile.class);
        Piece queen = new Queen(1, currentTile, false);
        when(currentTile.getX()).thenReturn(7);
        when(currentTile.getY()).thenReturn(0);

        when(tile.getX()).thenReturn(11);
        when(tile.getY()).thenReturn(3);

        assertFalse(queen.isDiagonalMove(tile));
    }

    @Test
    public void isVerticalMoveTest_ReturnTrue() {

        Tile currentTile = new Tile(1, 7, 0);
        Tile finTile = new Tile(0, 7, 7);
        Piece queen = new Queen(1, currentTile, false);

        boolean expectedResult = true;

        assertEquals(expectedResult, queen.isVerticalMove(finTile));
    }

    @Test
    public void isVerticalMoveTest_ReturnFalse() {

        Tile currentTile = new Tile(1, 7, 0);
        Tile finTile = new Tile(0, 8, 7);

        Piece queen = new Queen(1, currentTile, false);

        boolean expectedResult = false;

        assertEquals(expectedResult, queen.isVerticalMove(finTile));
    }


    @Test
    public void isHorizontalMoveTest_ReturnTrue() {
        tile = mock(Tile.class);
        currentTile = mock(Tile.class);
        Piece queen = new Queen(1, currentTile, false);
        when(currentTile.getX()).thenReturn(3);
        when(currentTile.getY()).thenReturn(2);

        when(tile.getY()).thenReturn(2);
        when(tile.getX()).thenReturn(6);

        assertTrue(queen.isHorizontalMove(tile));

    }

    @Test
    public void isHorizontalMoveTest_ReturnFalse() {
        tile = mock(Tile.class);
        currentTile = mock(Tile.class);
        Piece queen = new Queen(1, currentTile, false);
        when(currentTile.getX()).thenReturn(3);
        when(currentTile.getY()).thenReturn(2);

        when(tile.getY()).thenReturn(4);
        when(tile.getX()).thenReturn(6);

        assertFalse(queen.isHorizontalMove(tile));

    }

    @Test
    public void isAnyTileIsOccupiedHorizontalMoveTest_IfNotFindAnyOccupiedTiles_ReturnFalse() {

        Piece queen = new Queen(1, startTile, false);

        assertFalse(queen.isAnyTileIsOccupiedHorizontalMove(finishTile));
    }

    @Test
    public void isAnyTileIsOccupiedHorizontalMoveTest_IfFindAnyOccupiedTiles_ReturnTrue() {

        Tile intermediateTile = tileList.stream().filter(p -> p.getX() == 7 && p.getY() == 2)
                .findAny().get();
        Piece pawn = mock(Piece.class);
        pawn.setCurrentTile(intermediateTile);
        intermediateTile.setCurrentPiece(pawn);

        Piece queen = new Queen(1, startTile, false);

        assertTrue(queen.isAnyTileIsOccupiedHorizontalMove(finishTile));
    }

    static public Tile[] mpTiles() {
        return new Tile[]{new Tile(1, 7, 0), new Tile(0, 8, 0)};
    }

    @ParameterizedTest
    @MethodSource("mpTiles")
    void test_MethodSource_Objects(Tile t) {
        assertNotNull(t);
        //assertEquals(1, t.getColor());
    }

    @Test
    public void getListOfIntermediateHorizontalTilesTest_IfListNotEmpty_ReturnTrue() {

        Piece queen = new Queen(1, startTile, false);

        List<Tile> tempList = queen.getListOfIntermediateHorizontalTiles(finishTile);
        int size = tempList.size();

        boolean expectedResult = true;
        boolean result = size > 0;

        assertEquals(expectedResult, result);

    }

    @Test
    public void getListOfIntermediateHorizontalTilesTest_SizeOfListIsEqualSix_ReturnTrue() {

        Piece queen = new Queen(1, startTile, false);

        List<Tile> tempList = queen.getListOfIntermediateHorizontalTiles(finishTile);
        int size = tempList.size();

        int expectedResult = 6;

        assertEquals(expectedResult, size);

    }

    @Test
    public void getListOfIntermediateHorizontalTilesTest_ReturnNotNullObject() {

        Piece queen = new Queen(1, startTile, false);

        List<Tile> tempList = queen.getListOfIntermediateHorizontalTiles(finishTile);

        assertNotNull(tempList);

    }

    @Test
    public void getListOfIntermediateHorizontalTilesTest_SizeOfListIsEqualZero_ReturnTrue() {

        Tile finTile = tileList.stream().filter(p -> p.getX() == 5 && p.getY() == 2)
                .findAny().get();
        Piece queen = new Queen(1, startTile, false);
        List<Tile> tempList = queen.getListOfIntermediateHorizontalTiles(finTile);
        int size = tempList.size();

        int expectedResult = 0;

        assertEquals(expectedResult, size);

    }

    @Test
    public void moveTest_FinishTileIsNotEmptyAfterMove_ReturnFalse() {

        Piece queen = new Queen(1, startTile, false);
        HashSet<String> tags = new HashSet<>();
        LinkedList<Piece> lastRemoved = new LinkedList<>();
        queen.move(finishTile, tags, lastRemoved);

        assertFalse(finishTile.getIsEmpty());
    }

    @Test
    public void moveTest_StartTileIsEmptyAfterMove_ReturnTrue() {

        Piece queen = new Queen(1, startTile, false);
        HashSet<String> tags = new HashSet<>();
        LinkedList<Piece> lastRemoved = new LinkedList<>();
        queen.move(finishTile, tags, lastRemoved);

        assertTrue(startTile.getIsEmpty());
    }

    @Test
    public void moveTest_PieceSetsOnFinishTileAfterMove_ReturnTrue() {

        Piece queen = new Queen(1, startTile, false);
        HashSet<String> tags = new HashSet<>();
        LinkedList<Piece> lastRemoved = new LinkedList<>();
        queen.move(finishTile, tags, lastRemoved);

        assertEquals(finishTile.getCurrentPiece(), queen);
    }

    @Test
    public void moveTest_StackContains_x_ReturnTrue() {
        gameManager = GameManager.getInstance();
        gameManager.initializeTiles();
        tileList = gameManager.getTileList();
        startTile = tileList.stream().filter(p -> p.getX() == 4 && p.getY() == 2)
                .findAny().get();
        Tile finTile = tileList.stream().filter(p -> p.getX() == 11 && p.getY() == 2)
                .findAny().get();
        Piece queen = new Queen(1, startTile, false);
        HashSet<String> tags = new HashSet<>();
        LinkedList<Piece> lastRemoved = new LinkedList<>();

        Piece pawn = mock(Piece.class);
        pawn.setCurrentTile(finTile);
        finTile.setCurrentPiece(pawn);
        queen.move(finTile, tags, lastRemoved);

        assertTrue(tags.contains("x"));
    }

    @Test
    public void moveTest_KilledPieceIsRemoved_ReturnTrue() {

        Piece queen = new Queen(1, startTile, false);
        HashSet<String> tags = new HashSet<>();
        LinkedList<Piece> lastRemoved = new LinkedList<>();

        Piece pawn = new Pawn(0, finishTile, false);
        finishTile.setCurrentPiece(pawn);
        lastRemoved.add(pawn);
        queen.move(finishTile, tags, lastRemoved);

        assertTrue(pawn.wasRemoved);
    }

    @Test
    public void isMoveAllowedTest_IfVerticalMoveIsAllowed_ReturnTrue() {
        Tile finTile = tileList.stream().filter(p -> p.getX() == 4 && p.getY() == 7)
                .findAny().get();
        Piece queen = new Queen(1, startTile, false);

        assertTrue(queen.isMoveAllowed(finTile));
    }

    @Test
    public void isMoveAllowedTest_IfVerticalMoveIsNotAllowed_ReturnFalse() {

        Tile finTile = tileList.stream().filter(p -> p.getX() == 4 && p.getY() == 7)
                .findAny().get();
        Piece queen = new Queen(1, startTile, false);
        HashSet<String> tags = new HashSet<>();
        List<Piece> removedPieces = new ArrayList<>();

        Tile intermediateTile = tileList.stream().filter(p -> p.getX() == 4 && p.getY() == 6)
                .findAny().get();
        Piece pawn = mock(Piece.class);
        pawn.setCurrentTile(intermediateTile);
        intermediateTile.setCurrentPiece(pawn);

        assertFalse(queen.isMoveAllowed(finTile));
    }

    @Test
    public void isTheSameColorTest_IfPieceMoveToEmptyTile_ReturnFalse() {

        finTile = mock(Tile.class);
        currentTile = mock(Tile.class);
        Piece queen = new Queen(1, currentTile, false);
//        when(currentTile.getX()).thenReturn(7);
//        when(currentTile.getY()).thenReturn(0);
//        when(finTile.getX()).thenReturn(8);
//        when(finTile.getY()).thenReturn(1);
        when(finTile.getIsEmpty()).thenReturn(true);

        assertFalse(queen.isTheSameColor(finTile));

    }

    @Test
    public void isTheSameColorTest_IfPieceKillPieceSameColor_ReturnTrue() {

        finTile = mock(Tile.class);
        currentTile = mock(Tile.class);
        Piece queen = new Queen(1, currentTile, false);
        Pawn pawn = new Pawn(1, finTile, false);
        when(finTile.getCurrentPiece()).thenReturn(pawn);
        when(currentTile.getCurrentPiece()).thenReturn(queen);

        assertTrue(queen.isTheSameColor(finTile));

    }

    @Test
    public void isTheSameColorTest_IfPieceKillPieceOppositeColor_ReturnFalse() {

        finTile = mock(Tile.class);
        currentTile = mock(Tile.class);
        Piece queen = new Queen(1, currentTile, false);
        Pawn pawn = new Pawn(0, finTile, false);
        when(finTile.getCurrentPiece()).thenReturn(pawn);
        when(currentTile.getCurrentPiece()).thenReturn(queen);

        assertFalse(queen.isTheSameColor(finTile));

    }

    @Test
    public void toStringTestEquals_Q() {
        Piece queen = new Queen(1, tile, false);
        assertEquals("Q", queen.toString());
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
//
//    }

}
