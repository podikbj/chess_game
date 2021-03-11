package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.imageio.ImageIO;

public abstract class Piece {

    protected final int color; // 1 - white; 0 - black
    protected Tile currentTile;
    protected String imageFilePath;
    private BufferedImage pieceImage;

    public Piece(int color, String blackPiecePath, String whitePiecePath) {
        this.color = color;
        imageFilePath = (this.color == 0) ? blackPiecePath : whitePiecePath;
        try {
            if (this.pieceImage == null) {
                this.pieceImage = ImageIO.read(new File(imageFilePath));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public abstract boolean isMoveAllowed(Tile finTile);

    protected boolean isDiagonalMove(Tile finTile) {
        return (Math.abs(finTile.getX() - currentTile.getX()) == Math.abs(finTile.getY() - currentTile.getY()));
    }

    protected boolean isVerticalMove(Tile finTile) { // x const
        return finTile.getY() == currentTile.getY();
    }

    protected boolean isHorizontalMove(Tile finTile) { // x const
        return finTile.getX() == currentTile.getX();
    }

    protected boolean isAnyTileIsOccupiedHorizontalMove(Tile finTile) {
        GameManager gameManager;
        boolean b = false;
        //      try {
        gameManager = GameManager.getInstance();
        List<Tile> tileList = gameManager.getTileList();
        b = tileList.stream()
                .filter(p -> p.getX() == currentTile.getX())
                .filter(p -> p.getY() > currentTile.getY())
                .filter(p -> p.getY() <= finTile.getY())
                .filter(p -> !p.getIsEmpty())
                .findAny().isPresent();
//        } catch (IOException ex) {
//            Logger.getLogger(Piece.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return b;
    }

    protected boolean isAnyTileIsOccupiedVerticalMove(Tile finTile) {
        GameManager gameManager;
        boolean b = false;
        //      try {
        gameManager = GameManager.getInstance();
        List<Tile> tileList = gameManager.getTileList();
        b = tileList.stream()
                .filter(p -> p.getY() == currentTile.getY())
                .filter(p -> p.getX() > currentTile.getX())
                .filter(p -> p.getX() <= finTile.getX())
                .filter(p -> !p.getIsEmpty())
                .findAny().isPresent();
//        } catch (IOException ex) {
//            Logger.getLogger(Piece.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return b;
    }

    protected boolean isAnyTileIsOccupiedDiagonalMove(Tile finTile) {
        GameManager gameManager;
        boolean b = false;
        //      try {
        gameManager = GameManager.getInstance();
        List<Tile> tileList = gameManager.getTileList();

        b = tileList.stream()
                .filter(p -> Math.abs(p.getX() - currentTile.getX()) == Math.abs(p.getY() - currentTile.getY()))
                .filter(p -> !p.getIsEmpty())
                .findAny().isPresent();
//        } catch (IOException ex) {
//            Logger.getLogger(Piece.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return b;
    }

    private void addMoveEntry(Tile finTile) {
        GameManager gameManager;
        //      try {
        gameManager = GameManager.getInstance();
        gameManager.getMoveSequence().add(currentTile.coordinatesString() + "-" + finTile.coordinatesString());
//        } catch (IOException ex) {
//            Logger.getLogger(Piece.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public boolean move(Tile finTile) {

        if (!finTile.getIsEmpty()) {
            finTile.removePiece();
            finTile.setCurrentPiece(this);
            addMoveEntry(finTile);
            return true;
        }

        finTile.setCurrentPiece(this);
        addMoveEntry(finTile);
        return true;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile tile) {
        this.currentTile = tile;
    }

    public int getColor() {
        return color;
    }

    public void setPath(String path) {
        this.imageFilePath = path;
    }

    public String getPath() {
        return imageFilePath;
    }

    public BufferedImage getPieceImage() {
        return pieceImage;
    }
    
    

}
