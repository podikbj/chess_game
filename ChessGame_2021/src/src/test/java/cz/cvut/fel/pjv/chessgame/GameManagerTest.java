package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.CheckMatePositionControl;
import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.chessgame.Tile;

import java.util.HashSet;

import java.util.LinkedList;

import org.junit.Before;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

public class GameManagerTest {
//
//
//    @Mock
//    CheckMatePositionControl checkMatePositionControl;
//
//    @Mock
//    Piece king;
//
//    @Mock
//    Tile kingTile;

    private static GameManager gameManager;
    private static LinkedList<Tile> tileList;

    @BeforeAll
    public static void setUp() {
        gameManager = GameManager.getInstance();
        gameManager.initializeTiles();
        tileList = gameManager.getTileList();
        gameManager.initializePieces();
        gameManager.initializeCheckMatePositionControl();

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void changePawnTest_IfPawnIsChangedForAnotherPiece_ReturnTrue(int x) {

        HashSet<String> tags = new HashSet<String>();

        Tile pawnTile = tileList.stream().filter(p -> p.getX() == 4 && p.getY() == 7)
                .findAny().get();

        Piece pawn = new Pawn(1, pawnTile, false);
        pawnTile.setCurrentPiece(pawn);

        gameManager.changePawn(pawnTile, true, x, tags);

        String result = pawnTile.getCurrentPiece().toString();
        switch (x) {
            case 0:
                assertEquals("Q", result);
                break;
            case 1:
                assertEquals("R", result);
                break;
            case 2:
                assertEquals("B", result);
                break;
            case 3:
                assertEquals("N", result);
                break;
        }
    }

}
