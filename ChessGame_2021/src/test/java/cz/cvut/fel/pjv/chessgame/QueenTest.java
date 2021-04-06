package cz.cvut.fel.pjv.chessgame;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class QueenTest {


    @Mock
    Tile tile;

    @Mock
    Tile currentTile;

    @Test
    public void mockedConstructorQueen() {
        try (MockedConstruction mocked = mockConstruction(Queen.class)) {
            Tile tile = new Tile(1, 3, 0);
            Piece queen = new Queen(1, tile);
            when(queen.toString()).thenReturn("Q");
            assertEquals("Q", queen.toString());
            // verify(queen).toString();
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    public void toStringTestEquals_Q() {
        Piece queen = new Queen(1, tile);
        assertEquals("Q", queen.toString());
    }

    @Test
    public void isHorizontalMoveTestThenReturnTrue() {
        tile = mock(Tile.class);
        currentTile = mock(Tile.class);
        Piece queen = new Queen(1, currentTile);
        when(currentTile.getX()).thenReturn(3);
        when(tile.getY()).thenReturn(2);
        when(currentTile.getY()).thenReturn(2);
        when(tile.getX()).thenReturn(6);
        
        assertTrue(queen.isHorizontalMove(tile));

    }


}
