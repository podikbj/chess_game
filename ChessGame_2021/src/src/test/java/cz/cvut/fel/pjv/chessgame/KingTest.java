package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class KingTest {

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
    
    @Test
    public void moveTest_IfKingIsOnCheck_ThenReturnTrue() {
        HashSet<String> tags = new HashSet<String>();
        List<Piece> lastRemoved = new ArrayList<>();
        
    }
    

}
