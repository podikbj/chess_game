package cz.cvut.fel.pjv.chessgame;

import cz.cvut.fel.pjv.start.GameManager;
import cz.cvut.fel.pjv.view.StartMenu;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.imageio.ImageIO;

public abstract class Piece {

    protected final int color; // 1 - white; 0 - black
    protected Tile currentTile;
    protected String imageFilePath;
    private BufferedImage pieceImage;
    protected boolean wasMoved = false;
    protected boolean wasRemoved;
    protected GameManager gameManager = GameManager.getInstance();
    protected List<Tile> tileList = gameManager.getTileList();
    private List<Tile> tempList = null;

    public Piece(int color, Tile tile, String blackPiecePath, String whitePiecePath, boolean wasRemoved) {
        this.color = color;
        this.currentTile = tile;
        this.wasRemoved = wasRemoved;
        imageFilePath = (this.color == 0) ? blackPiecePath : whitePiecePath;
        this.tileList = gameManager.getTileList();
        try {
            if (this.pieceImage == null) {
                this.pieceImage = ImageIO.read(new File(imageFilePath));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }

    }

    public abstract boolean isMoveAllowed(Tile finTile);

    public abstract List<Tile> getIntermediateTiles(Tile finTile);

    public boolean isDiagonalMove(Tile finTile) {
        return (Math.abs(finTile.getX() - currentTile.getX()) == Math.abs(finTile.getY() - currentTile.getY()));
    }

    public boolean isVerticalMove(Tile finTile) { // x const
        return (finTile.getX() == currentTile.getX() && finTile.getY() != currentTile.getY());
    }

    public boolean isHorizontalMove(Tile finTile) { // x const

        return (finTile.getY() == currentTile.getY() && finTile.getX() != currentTile.getX());
    }

    protected boolean isAnyTileIsOccupiedHorizontalMove(Tile finTile) {

        boolean b = false;
        tempList = getListOfIntermediateHorizontalTiles(finTile);
        if (tempList != null) {
            b = tempList.stream().filter(p -> !p.getIsEmpty()).findAny().isPresent();
        }
        return b;
    }

    public List<Tile> getListOfIntermediateHorizontalTiles(Tile finTile) {
        tempList = tileList.stream()
                .filter(p -> p.getY() == currentTile.getY())
                .filter((p)
                        -> {
                    boolean x = false;
                    if (currentTile.getX() > finTile.getX()) {
                        x = (p.getX() > finTile.getX() && p.getX() < currentTile.getX());
                    }
                    if (currentTile.getX() < finTile.getX()) {
                        x = (p.getX() < finTile.getX() && p.getX() > currentTile.getX());
                    }
                    return x;
                }
                )
                .collect(toList());

        return tempList;
    }

    protected boolean isAnyTileIsOccupiedVerticalMove(Tile finTile) {

        boolean b = false;
        tempList = getListOfIntermediateVerticalTiles(finTile);
        if (tempList != null) {
            b = tempList.stream().filter(p -> !p.getIsEmpty()).findAny().isPresent();
        }
        return b;
    }

    public List<Tile> getListOfIntermediateVerticalTiles(Tile finTile) {
        tempList = tileList.stream()
                .filter(p -> p.getX() == currentTile.getX())
                .filter((p)
                        -> {
                    boolean y = false;
                    if (currentTile.getY() > finTile.getY()) {
                        y = (p.getY() > finTile.getY() && p.getY() < currentTile.getY());
                    }
                    if (currentTile.getY() < finTile.getY()) {
                        y = (p.getY() < finTile.getY() && p.getY() > currentTile.getY());
                    }
                    return y;
                }
                )
                .collect(toList());

        return tempList;
    }

    protected boolean isAnyTileIsOccupiedDiagonalMove(Tile finTile) {

        boolean b = false;
        tempList = getListOfIntermediateDiagonalTiles(finTile);
        if (tempList != null) {
            b = tempList.stream().filter(p -> !p.getIsEmpty()).findAny().isPresent();
        }

        return b;
    }

    public List<Tile> getListOfIntermediateDiagonalTiles(Tile finTile) {

        tempList = tileList.stream()
                .filter(p -> Math.abs(p.getX() - currentTile.getX()) == Math.abs(p.getY() - currentTile.getY()))
                .filter((p)
                        -> {
                    boolean xy = false;
                    if (finTile.getX() > currentTile.getX() && finTile.getY() > currentTile.getY()) {

                        xy = (p.getX() > currentTile.getX()
                                && p.getY() > currentTile.getY()
                                && p.getX() < finTile.getX()
                                && p.getY() < finTile.getY());
                    }

                    if (finTile.getX() > currentTile.getX() && finTile.getY() < currentTile.getY()) {

                        xy = (p.getX() > currentTile.getX()
                                && p.getY() < currentTile.getY()
                                && p.getX() < finTile.getX()
                                && p.getY() > finTile.getY());

                    }

                    if (finTile.getX() < currentTile.getX() && finTile.getY() > currentTile.getY()) {

                        xy = (p.getX() < currentTile.getX()
                                && p.getY() < currentTile.getY()
                                && p.getX() > finTile.getX()
                                && p.getY() > finTile.getY());

                    }

                    if (finTile.getX() < currentTile.getX() && finTile.getY() < currentTile.getY()) {

                        xy = (p.getX() < currentTile.getX()
                                && p.getY() > currentTile.getY()
                                && p.getX() > finTile.getX()
                                && p.getY() < finTile.getY());

                    }

                    return xy;
                }
                )
                .
                collect(toList());
        return tempList;
    }

    protected void addMoveEntry(Tile finTile) {
        GameManager gameManager = GameManager.getInstance();
        gameManager.getMoveSequence().add(currentTile.coordinatesString() + "-" + finTile.coordinatesString());
    }

    public boolean move(Tile finTile, HashSet<String> tags, List<Piece> removedPieces) {
        
        if (!StartMenu.isManual) {
            wasMoved = true;
        }
        if (!finTile.getIsEmpty() && this.color != finTile.getCurrentPiece().color) {
            tags.add("x");
            Piece removedPiece = finTile.getCurrentPiece();
            removedPiece.setCurrentTile(null);
            removedPiece.wasRemoved = true;
            removedPieces.add(removedPiece);
            finTile.removePiece();

            currentTile.removePiece();
            finTile.setCurrentPiece(this);
            this.currentTile = finTile;
            addMoveEntry(finTile);

            return true;
        }

        currentTile.removePiece();
        finTile.setCurrentPiece(this);
        this.currentTile = finTile;
        finTile.setIsEmpty(false);
        addMoveEntry(finTile);
        return true;
    }

    protected boolean isTheSameColor(Tile finTile) {
        if (finTile.getIsEmpty()) {
            return false;
        }
        return (currentTile.getCurrentPiece().color == finTile.getCurrentPiece().color);
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

    public boolean isWasMoved() {
        return wasMoved;
    }

    public boolean isWasRemoved() {
        return wasRemoved;
    }

    public void setWasMoved(boolean wasMoved) {
        this.wasMoved = wasMoved;
    }
    
    
}
