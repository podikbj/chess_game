package cz.cvut.fel.pjv.start;

import cz.cvut.fel.pjv.view.BoardPanel;
import cz.cvut.fel.pjv.chessgame.King;
import cz.cvut.fel.pjv.chessgame.Piece;
import cz.cvut.fel.pjv.chessgame.Tile;
import java.util.Comparator;
import java.util.LinkedList;
import static java.util.stream.Collectors.toList;

public class CheckMatePositionControl {

    private boolean whiteIsOnChech = false;
    private boolean blackIsOnCheck = false;

    private Piece wKing;
    private Piece bKing;

    public CheckMatePositionControl() {
    }

    public boolean isChecked(Piece currentPiece, Tile finTile, boolean whiteIsActive) {

        boolean isChecked = false;
        //GameManager gameManager = GameManager.getInstance();

        Piece king = (whiteIsActive == true) ? wKing : bKing;
        Tile currentKingPosition = king.getCurrentTile();
        if (currentPiece.isMoveAllowed(currentKingPosition)) {
            isChecked = true;
            if (whiteIsActive) {
                whiteIsOnChech = true;
            } else {
                blackIsOnCheck = true;
            }
            System.out.println("king " + king.getColor() + " isChecked");
        }

        return isChecked;
    }

    public boolean isKingTile(Tile finTile, boolean whiteIsActive) {

        boolean isKingTile = false;
        //GameManager gameManager = GameManager.getInstance();
        Piece king = (whiteIsActive == true) ? bKing : wKing;
        Tile currentKingPosition = king.getCurrentTile();
        if (currentKingPosition == finTile) {
            isKingTile = true;
        }
        return isKingTile;

    }


    public void setwKing(Piece wKing) {
        GameManager gameManager = GameManager.getInstance();
        LinkedList<Piece> wPieceses = gameManager.getwPieceses();

        wKing = wPieceses.stream()
                .filter(p -> p.toString()
                .equals("K"))
                .findAny().get();

    }

    public void setbKing(Piece bKing) {
        GameManager gameManager = GameManager.getInstance();

        LinkedList<Piece> bPieceses = gameManager.getbPieceses();

        bKing = bPieceses.stream()
                .filter(p -> p.toString()
                .equals("K"))
                .findAny().get();

    }
}
