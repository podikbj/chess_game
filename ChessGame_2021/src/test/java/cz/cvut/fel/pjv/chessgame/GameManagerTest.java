package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.chessgame.Tile;

import java.util.LinkedList;

import org.junit.Before;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class GameManagerTest {

    private static  GameManager gameManager;
    private static LinkedList<Tile> tileList;

    @BeforeAll
    public static void setUp() {
        gameManager = GameManager.getInstance();
        gameManager.initializeTiles();
        tileList = gameManager.getTileList();

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void changePawnTest_IfPawnIsChangedOnQueen_ReturnTrue(int x) {
        gameManager.initializeTiles();
        Tile currentTile = tileList.stream().filter(p -> p.getX() == 4 && p.getY() == 7)
                .findAny().get();

        Piece currentPiece = new Pawn(1, currentTile, false);

        gameManager.changePawn(currentPiece, currentTile, true, x);

        String result = currentTile.getCurrentPiece().toString();
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
