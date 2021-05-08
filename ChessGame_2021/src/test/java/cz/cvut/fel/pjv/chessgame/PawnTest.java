package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import org.junit.jupiter.api.BeforeAll;

import java.util.LinkedList;

public class PawnTest {
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

//        Pawn wPawm = new Pawn();
//        Pawn bPawm = new Pawn();

    }

    public void isAllowedEnPassantTest_IfIsAllowed_ReturnTrue() {
       
    }


}
